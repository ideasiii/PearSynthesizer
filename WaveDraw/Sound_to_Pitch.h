// Sound_to_Pitch.h
//
// 原始來源 : Copyright (C) 1992-2004 Paul Boersma
//
// 修改 : Bala in 2004/10/13
// 使用於 VC++ 6.0 & MFC 環境, 使用前需連結 winmm.lib
//
//
// 函式說明:
// int File_To_Sound(CString csFilename, double*& pdbRawData, int& nSamepleRate)
// 輸入 :	CString csFilename :	檔名.
//			double*& pdbRawData :	用來放訊號內容的陣列. 使用者只需自行宣告並傳入, 不需
//									自行建立所需記憶體空間. 但使用後需自行使用 delete 釋
//									放記憶體.
//			int& nSamepleRate :		此音檔的 sample rate
// 輸出 :	此音檔的 sample 數
//
// int Sound_to_Pitch(double *pdbSoundData, double*& pdbPitchContour, int nSoundLength, int nSampleRate)
// 輸入 :	double *pdbSoundData :		存放聲音資料的陣列.
//			double*& pdbPitchContour :	用來放 pitch data 的陣列. 使用者只需自行宣告並傳
//										入, 不需自行建立所需記憶體空間. 但使用後需自行使
//										用 delete 釋放記憶體.
//			int nSoundLength :			聲音資料陣列的陣列長度
//			int nSampleRate :			此音檔的 sample rate
// 輸出 :	pitch data 陣列的陣列長度

#include <math.h>
#include <mmsystem.h>
#include "DFFT.h"

#define macReturnError(Msg) {AfxMessageBox(Msg);return -1;}
#define NUMpi  3.14159265358979323846

int Sound_to_Pitch(double *pdbSoundData, double*& pdbPitchContour, double*& pdbEnergyContour,
				   int nSoundLength, int nSampleRate)
{
	double dbMinPitch = 75;				// (Hertz) 用預設值
	double dbMaxPitch = 600;			// (Hertz) 用預設值
	double dbPeriodsPerWindow = 3.0;	// ac3 for pitch analysis, 6 or 4.5 for HNR, 1 for FCC
	double octaveCost = 0.01;			// favours higher pitches; default 0.01
	double dbWinLen_Sec;				// Window length in seconds.
	double dbGlobalPeak, dbInterpolationDepth = 0.5;
	int i, j, n, nWinLen_Sam, nHalfWinLen_Sam; // Number of samples per window.
	int nPeriodLen_Sam, nHalfPeriodLen_Sam; // Number of samples in longest period.
	int nPitchLength, nMinLag, nMaxLag;
	double dbSoundLength_Sec = (double)nSoundLength / nSampleRate;

	// time step (seconds); 0.0 = automatic = dbPeriodsPerWindow / dbMinPitch / 4
	// e.g. 3 periods, 75 Hz: 10 milliseconds.
	// 每個 frame 間的偏移量 (in sec)
	double dbTimeStep = dbPeriodsPerWindow / dbMinPitch / 4.0;

	// Determine the number of samples in the longest period.
	// We need this to compute the local mean of the sound (looking one period in both directions),
	// and to compute the local peak of the sound (looking half a period in both directions).
	// 求每一個 period 的 sample 數和每半個 period 的 sample 數
	nPeriodLen_Sam = (int)(nSampleRate / dbMinPitch);
	nHalfPeriodLen_Sam = nPeriodLen_Sam / 2 + 1;

	// dbMaxPitch 最小應該是 nSampleRate / 2
	if (dbMaxPitch > nSampleRate/2) dbMaxPitch = nSampleRate / 2;

	// Determine window length in seconds and in samples.
	// frame 長度用 minimum Pitch 來決定
	dbWinLen_Sec = dbPeriodsPerWindow / dbMinPitch;
	nWinLen_Sam = (int)(dbWinLen_Sec * nSampleRate);
	nHalfWinLen_Sam = nWinLen_Sam / 2 - 1;
	if (nHalfWinLen_Sam < 2)
		macReturnError("Analysis window too short.")
	nWinLen_Sam = nHalfWinLen_Sam * 2;
	double dbAnalizeStar = dbWinLen_Sec / 2; // 原來的 my x1, 用 window length 計算

	// Determine the minimum and maximum lags.
	nMinLag = (int)(nSampleRate / dbMaxPitch);
	if (nMinLag < 2) nMinLag = 2;
	nMaxLag = (int)(nWinLen_Sam / dbPeriodsPerWindow) + 2;
	if (nMaxLag > nWinLen_Sam) nMaxLag = nWinLen_Sam;

	// Determine the number of frames.
	// Fit as many frames as possible symmetrically in the total dbSoundLength_Sec.
	// We do this even for the forward cross-correlation method,
	// because that allows us to compare the two methods.
	if (dbWinLen_Sec > dbSoundLength_Sec)
		macReturnError("shorter than window length.")
	nPitchLength = (int)((dbSoundLength_Sec - dbWinLen_Sec) / dbTimeStep) + 1;

	// create an empty pitch contour (voiceless), or NULL if out of memory.
	pdbPitchContour = new double[nPitchLength];
	pdbEnergyContour = new double[nPitchLength];
	ZeroMemory(pdbPitchContour, nPitchLength * sizeof(double));
	ZeroMemory(pdbEnergyContour, nPitchLength * sizeof(double));

	// Step 1: compute global absolute peak for determination of silence threshold.
	//         找最大的 peak
	double mean = 0.0;
	dbGlobalPeak = 0;
	for (i=0; i<nSoundLength; i++) mean += pdbSoundData[i];
	mean /= nSoundLength;
	for (i=0; i<nSoundLength; i++)
	{
		double damp = pdbSoundData[i] - mean;
		if (fabs(damp) > dbGlobalPeak) dbGlobalPeak = fabs(damp);
	}
	if (dbGlobalPeak == 0.0) macReturnError("No Pitch !!!") // ‥沒有 pitch!

	// FFT 長度比 window 長度長, 多的部分補 0
	// For autocorrelation analysis.
	// Compute the number of samples needed for doing FFT.
	// To avoid edge effects, we have to append zeroes to the window.
	// The maximum lag considered for maxima is nMaxLag.
	// The maximum lag used in interpolation is nWinLen_Sam * dbInterpolationDepth.
	int nFFTLength = 1;
	while (nFFTLength < nWinLen_Sam * (1 + dbInterpolationDepth)) nFFTLength *= 2;

	// Create buffers for autocorrelation analysis.
	// A Gaussian or Hanning window is applied against phase effects.
	// The Hanning window is 2 to 5 dB better for 3 periods/window.
	// The Gaussian window is 25 to 29 dB better for 6 periods/window.
	double *dbHannWindow = new double[nWinLen_Sam];
	ZeroMemory(dbHannWindow, nWinLen_Sam*sizeof(double));
	for (i=0; i<nWinLen_Sam; i++)
		dbHannWindow[i] = 0.5 - 0.5 * cos(2*NUMpi*i/(nWinLen_Sam-1));

	// Compute the normalized autocorrelation of the window.
	// 先對 Hannging window 求 autocorrelation
	// 目前不做這一步, 因為他是用來做看不懂的事...

	double *pdbRealIn = new double[nFFTLength];
	double *pdbImagIn = new double[nFFTLength];
	double *pdbRealOut = new double[nFFTLength];
	double *pdbImagOut = new double[nFFTLength];
	double *frame = new double[nFFTLength];
	double **pdbMaxFreq = new double*[nPitchLength];
	double **pdbMaxStre = new double*[nPitchLength];
	BOOL *pbIsPitch = new BOOL[nPitchLength];
	for (i=0; i<nPitchLength; i++)
	{
		pdbMaxFreq[i] = new double[3];
		pdbMaxStre[i] = new double[3];
	}
	for (n=0; n<nPitchLength; n++)
	{
		double dbLocalMean, dbLocalPeak;
		int nStartSample, nEndSample;

		// 取得本 frame 的開頭位置 (in second)
		double t = dbAnalizeStar + n * dbTimeStep;

		// 取得本 frame 的開頭位置 (in sample)
		int nLeftSample = (int)(t * nSampleRate);
		int nRightSample = nLeftSample + 1;

		// Compute the local mean; look one longest period to both sides.
		dbLocalMean = 0.0;
		nStartSample =	max(nRightSample - nPeriodLen_Sam, 0);
		nEndSample =	min(nLeftSample + nPeriodLen_Sam, nSoundLength-1);
		for (i=nStartSample; i<=nEndSample; i++)
			dbLocalMean += pdbSoundData[i];
		dbLocalMean /= 2 * nPeriodLen_Sam;

		// Copy a window to a frame and subtract the local mean.
		// We are going to kill the DC component before windowing.
		nStartSample =	max(nRightSample - nHalfWinLen_Sam, 0);
		nEndSample =	min(nLeftSample + nHalfWinLen_Sam, nSoundLength-1);
		ZeroMemory(frame, nFFTLength*sizeof(double));
		for (j=0, i=nStartSample; j<nWinLen_Sam; j++)
			frame[j] = (pdbSoundData[i++]-dbLocalMean) * dbHannWindow[j];

		// 求能量強度!
		for (j=0; j<nWinLen_Sam; j++)
			pdbEnergyContour[n] += frame[j] * frame[j];
		//pdbEnergyContour[n] = 10 * log10(pdbEnergyContour[n]);

		// Compute the local peak; look half a longest period to both sides.
		dbLocalPeak = 0;
		nStartSample =	max(nHalfWinLen_Sam+1-nHalfPeriodLen_Sam, 1);
		nEndSample =	min(nHalfWinLen_Sam+nHalfPeriodLen_Sam, nWinLen_Sam);
		for (i=nStartSample; i<=nEndSample; i++)
		{
			if (fabs(frame[i]) > dbLocalPeak) dbLocalPeak = fabs(frame[i]);
		}
		double dbIntensity = dbLocalPeak > dbGlobalPeak ? 1 : dbLocalPeak / dbGlobalPeak;

		// Compute the correlation into the array 'r'.
		// The FFT of the autocorrelation is the power spectrum.
		// autocorrelation 求法是:
		//   1. window value -> FFT -> power spectrum
		//   2. power spectrum -> IFFT -> autocorrelation !!
		ZeroMemory(pdbRealIn, nFFTLength*sizeof(double));
		ZeroMemory(pdbImagIn, nFFTLength*sizeof(double));
		// FFT for Complex spectrum.
		fft_double (nFFTLength, FALSE, frame, pdbImagIn, pdbRealOut, pdbImagOut);
		pdbRealIn[0] *= pdbRealOut[0] * pdbRealOut[0];		// DC component.
		pdbImagIn[0] *= pdbImagOut[0] * pdbImagOut[0];		// Nyquist frequency.
		for (j=1; j<nFFTLength/2; j++)
			pdbRealIn[j] = pdbRealOut[j] * pdbRealOut[j] + pdbImagOut[j] * pdbImagOut[j];
		// IFFT for Autocorrelation.
		fft_double (nFFTLength, TRUE, pdbRealIn, pdbImagIn, frame, pdbImagOut);
		for (i=nFFTLength/2; i<nFFTLength; i++) // 後半部會和前半部一樣
			frame[i] = frame[nFFTLength/2-1];

		// 先用 autocorrelation 找 pitch 就好!
		// 不管接下來的東西!
		pdbMaxFreq[n][0] = pdbMaxFreq[n][1] = pdbMaxFreq[n][2] = 0;
		pdbMaxStre[n][0] = pdbMaxStre[n][1] = pdbMaxStre[n][2] = 0;
		double dbMaxI = 0;
		int nMaxNum = 0;
		for (i=(int)(nSampleRate/dbMaxPitch); i<=(int)(1.0+nSampleRate/dbMinPitch); i++)
		{
			if ((frame[i] >= frame[i-1]) &&
				(frame[i] >= frame[i+1]))
			{
				// 有可能是 pitch!
				nMaxNum ++;

				// 用前後強度值做內插
				double dr = 0.5 * (frame[i+1] - frame[i-1]);
				double d2r = 2 * frame[i] - frame[i-1] - frame[i+1];
				double dbMaxFrequency = nSampleRate / (i + dr / d2r);

				// 紀錄前三大的強度值及其對應的 pitch 值!
				if (frame[i] > pdbMaxStre[n][0]){
					pdbMaxFreq[n][2] = pdbMaxFreq[n][1];
					pdbMaxFreq[n][1] = pdbMaxFreq[n][0];
					pdbMaxFreq[n][0] = dbMaxFrequency;
					pdbMaxStre[n][2] = pdbMaxStre[n][1];
					pdbMaxStre[n][1] = pdbMaxStre[n][0];
					pdbMaxStre[n][0] = frame[i];
				}else if(frame[i] > pdbMaxStre[n][1]){
					pdbMaxFreq[n][2] = pdbMaxFreq[n][1];
					pdbMaxFreq[n][1] = dbMaxFrequency;
					pdbMaxStre[n][2] = pdbMaxStre[n][1];
					pdbMaxStre[n][1] = frame[i];
				}else if(frame[i] > pdbMaxStre[n][2]){
					pdbMaxFreq[n][2] = dbMaxFrequency;
					pdbMaxStre[n][2] = frame[i];
				}
			}
		}
		if ((dbIntensity < 0.2) || (nMaxNum > 30))
			pbIsPitch[n] = FALSE;
		else
			pbIsPitch[n] = TRUE;
	} // Next frame.

	// 用 DP 找每段的最佳值
	ZeroMemory(pdbPitchContour, nPitchLength*sizeof(double));
	n = 0;
	while(n < nPitchLength)
	{
		int nStart = 0, nEnd = 0;
		while(!pbIsPitch[n] && n<nPitchLength) n++;
		nStart = n;
		while(pbIsPitch[n] && n<nPitchLength) n++;
		nEnd = n-1;
		if (nStart >= nPitchLength) break;
		if (nEnd >= nPitchLength) nEnd = nPitchLength;

		// forward pass
		int **pnAddFrom = new int*[nEnd-nStart+1];
		double **pdbLocalCost = new double*[nEnd-nStart+1]; // 記錄兩兩 frame 間的最小 pitch 差
		double **pdbAccmuCost = new double*[nEnd-nStart+1]; // 記錄 pdbLocalCost[] 的累加
		for(i=0; i<nEnd-nStart+1; i++)
		{
			pdbLocalCost[i] = new double[3];
			pdbAccmuCost[i] = new double[3];
			pnAddFrom[i] = new int[3];
		}
		//pdbLocalCost[0][0] = pdbLocalCost[0][1] = pdbLocalCost[0][2] = 0;
		pdbAccmuCost[0][0] = pdbAccmuCost[0][1] = pdbAccmuCost[0][2] = 0;
		pnAddFrom[0][0] = pnAddFrom[0][1] = pnAddFrom[0][2] = 0;
		for (i=nStart+1; i<=nEnd; i++)
		{
			for (j=0; j<3; j++)
			{
				double d1 = fabs(pdbMaxFreq[i][j]-pdbMaxFreq[i-1][0]);
				double d2 = fabs(pdbMaxFreq[i][j]-pdbMaxFreq[i-1][1]);
				double d3 = fabs(pdbMaxFreq[i][j]-pdbMaxFreq[i-1][2]);
				if (d1 < d2){
					if (d1 < d3){
						// d1 最小
						//pdbLocalCost[i-nStart][j] = d1;
						pdbAccmuCost[i-nStart][j] = pdbAccmuCost[i-nStart-1][0] + d1;
						pnAddFrom[i-nStart][j] = 0;
					}else{
						// d3 最小
						//pdbLocalCost[i-nStart][j] = d3;
						pdbAccmuCost[i-nStart][j] = pdbAccmuCost[i-nStart-1][2] + d3;
						pnAddFrom[i-nStart][j] = 2;
					}
				}else{
					if (d2 < d3){
						// d2 最小
						//pdbLocalCost[i-nStart][j] = d2;
						pdbAccmuCost[i-nStart][j] = pdbAccmuCost[i-nStart-1][1] + d2;
						pnAddFrom[i-nStart][j] = 1;
					}else{
						// d3 最小
						//pdbLocalCost[i-nStart][j] = d3;
						pdbAccmuCost[i-nStart][j] = pdbAccmuCost[i-nStart-1][2] + d3;
						pnAddFrom[i-nStart][j] = 2;
					}
				}
			}
		}

		// backward pass
		// 用 "累計最小誤差" 來決定起點, 用 "局部最小誤差" 決定路徑
		if (pdbAccmuCost[nEnd-nStart][0] < pdbAccmuCost[nEnd-nStart][1]){
			if (pdbAccmuCost[nEnd-nStart][0] < pdbAccmuCost[nEnd-nStart][2])
				n = 0; // pdbAccmuCost[nEnd-nStart][0] 最小
			else				
				n = 2; // pdbAccmuCost[nEnd-nStart][2] 最小
		}else{
			if (pdbAccmuCost[nEnd-nStart][1] < pdbAccmuCost[nEnd-nStart][2])
				n = 1; // pdbAccmuCost[nEnd-nStart][1] 最小
			else
				n = 2; // pdbAccmuCost[nEnd-nStart][2] 最小
		}
		for (i=nEnd; i>=nStart; i--)
		{
			pdbPitchContour[i] = pdbMaxFreq[i][n];
			n = pnAddFrom[i-nStart][n];
		}
		for(i=0; i<nEnd-nStart+1; i++)
		{
			//delete [] pdbLocalCost[i];
			delete [] pdbAccmuCost[i];
			delete [] pnAddFrom[i];
		}
		//delete [] pdbLocalCost;
		delete [] pdbAccmuCost;
		delete [] pnAddFrom;

		n = nEnd+1;
	}

	// 去掉長度不足的不連續段!
	n = 0;
	while(n < nPitchLength)
	{
		int nStart = 0, nEnd = 0;
		while(!pdbPitchContour[n] && n<nPitchLength) n++;
		nStart = n;
		if (nStart >= nPitchLength) break;
		nEnd = nStart + 1;
		while(fabs(pdbPitchContour[nEnd]-pdbPitchContour[nEnd-1]) < 30) nEnd++;
		if (nEnd-nStart <= 5)
			ZeroMemory(&(pdbPitchContour[nStart]), (nEnd-nStart+1)*sizeof(double));
		n = nEnd + 1;
	}

	// delete...
	for (i=0; i<nPitchLength; i++)
	{
		delete [] pdbMaxFreq[i];
		delete [] pdbMaxStre[i];
	}
	delete [] pdbRealIn;
	delete [] pdbImagIn;
	delete [] pdbRealOut;
	delete [] pdbImagOut;
	delete [] frame;
	delete [] dbHannWindow;
	delete [] pbIsPitch;
	delete [] pdbMaxFreq;
	delete [] pdbMaxStre;

	return nPitchLength;
}

int File_To_Sound(CString csFilename, double*& pdbRawData, int& nSamepleRate) // 讀 Wave 檔
{
	HMMIO hmmio;
	MMCKINFO ckRIFF,ckInfo;
	PCMWAVEFORMAT m_pcmwf;

	hmmio=mmioOpen(csFilename.GetBuffer(256), NULL, MMIO_READ);
	csFilename.ReleaseBuffer();

	if(hmmio==NULL) return FALSE;

	if(mmioDescend(hmmio,&ckRIFF,NULL,0))
	{
		mmioClose(hmmio,0);
		return FALSE;
	}

	// Wave format
	if(ckRIFF.ckid!=FOURCC_RIFF || (ckRIFF.fccType!=mmioFOURCC('W','A','V','E')))
	{
		mmioClose(hmmio,0);
		return FALSE;
	}
	
	// 'fmt ' chunk
	ckInfo.ckid=mmioFOURCC('f','m','t',' ');
	if(!mmioDescend(hmmio,&ckInfo,&ckRIFF,MMIO_FINDCHUNK))
	{
		if(ckInfo.cksize>=(long)sizeof(PCMWAVEFORMAT))
		{
			if(mmioRead(hmmio,(LPSTR)&m_pcmwf,sizeof(PCMWAVEFORMAT))==sizeof(PCMWAVEFORMAT))
			{
				if(mmioAscend(hmmio,&ckInfo,0))
				{
					mmioClose(hmmio,0);
					return FALSE;
				}
			}else{
				mmioClose(hmmio,0);
				return FALSE;
			}
		}else{
			mmioClose(hmmio,0);
			return FALSE;
		}
	}else{
		mmioClose(hmmio,0);
		return FALSE;
	}

	// 'data' chunk
	DWORD m_dwDataLength;
	BYTE *m_pData;
	ckInfo.ckid=mmioFOURCC('d','a','t','a');
	if(!mmioDescend(hmmio,&ckInfo,&ckRIFF,MMIO_FINDCHUNK))
	{
		m_dwDataLength = ckInfo.cksize;
		m_pData = new BYTE[ckInfo.cksize];
		memset(m_pData, 0, m_dwDataLength*sizeof(BYTE));
		if(mmioRead(hmmio,(LPSTR)m_pData,m_dwDataLength) != (long)m_dwDataLength)
		{
			mmioClose(hmmio,0);
			return FALSE;
		}
	}else{
		mmioClose(hmmio,0);
		return FALSE;
	}
	mmioClose(hmmio,0);

	// Read over, set to the double array
	int n, nDataLength;
	nSamepleRate = m_pcmwf.wf.nSamplesPerSec;
	if(m_pcmwf.wBitsPerSample == 8)
	{
		nDataLength = m_dwDataLength;
		pdbRawData = new double[m_dwDataLength];
		for (n=0; n<nDataLength; n++)
			pdbRawData[n] = ((double)m_pData[n] - 128.0) / 128.0;
	}else if(m_pcmwf.wBitsPerSample == 16){
		short *psData = new short[m_dwDataLength/2];
		memcpy(psData, m_pData, m_dwDataLength);
		nDataLength = m_dwDataLength / 2;
		pdbRawData = new double[m_dwDataLength / 2];
		for (n=0; n<nDataLength; n++)
			pdbRawData[n] = (double)psData[n] / 32767.0;
		delete [] psData;
	}
	delete [] m_pData;

	return nDataLength;
}