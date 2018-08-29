// cwave.cpp : implementation file
// (for visual c++ 4.0)

#include "stdafx.h"
#include "cwave.h"

#ifdef _DEBUG
#undef THIS_FILE
static char BASED_CODE THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CWave

CWave::CWave(CWnd* pParentWnd)
{
	if( (m_pParentWnd=pParentWnd) == NULL) {
	   	Create();
    }

	m_dwDataLength = 0;
	m_pData = (BYTE*)GlobalAllocPtr(GMEM_MOVEABLE,sizeof(BYTE));
	m_pWaveHdr1 = (LPWAVEHDR)GlobalAllocPtr(GMEM_MOVEABLE,sizeof(WAVEHDR));
	m_pWaveHdr2 = (LPWAVEHDR)GlobalAllocPtr(GMEM_MOVEABLE,sizeof(WAVEHDR));
	m_uState = CWAVE_STATE_NOTHING;
}

CWave::~CWave()
{
	DestroyWindow();
	GlobalFreePtr(m_pWaveHdr2);
	GlobalFreePtr(m_pWaveHdr1);
	GlobalFreePtr(m_pData);
	
}

BOOL CWave::Create()
{
	if (m_hWnd)
		return TRUE;
    RECT rect;
	rect.left	= rect.top	=0;
	rect.right	= rect.bottom =100;
    
	if(m_pParentWnd==NULL){
		m_pParentWnd=AfxGetMainWnd();
		if (m_pParentWnd == NULL)
			return FALSE;
	}
	CWnd::Create(NULL,NULL,WS_OVERLAPPEDWINDOW,rect,m_pParentWnd,0);
	return TRUE;
}

BEGIN_MESSAGE_MAP(CWave, CWnd)
	//{{AFX_MSG_MAP(CWave)
	ON_MESSAGE(MM_WOM_OPEN, OnMmWomOpen)
	ON_MESSAGE(MM_WOM_CLOSE,OnMmWomClose)
	ON_MESSAGE(MM_WOM_DONE, OnMmWomDone)
	ON_MESSAGE(MM_WIM_OPEN, OnMmWimOpen)
	ON_MESSAGE(MM_WIM_CLOSE,OnMmWimClose)
	ON_MESSAGE(MM_WIM_DATA, OnMmWimData)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()


/////////////////////////////////////////////////////////////////////////////
// CWave message handlers
LONG CWave::OnMmWomOpen(UINT wParam,LONG lParam)
{
	m_uState=CWAVE_STATE_PLAY;

	m_pWaveHdr1->lpData			= (LPSTR)m_pData;
	m_pWaveHdr1->dwBufferLength	= m_dwDataLength;;
	m_pWaveHdr1->dwBytesRecorded= 0;
	m_pWaveHdr1->dwUser			= 0;
	m_pWaveHdr1->dwFlags		= WHDR_BEGINLOOP|WHDR_ENDLOOP;
	m_pWaveHdr1->dwLoops		= 1;
	m_pWaveHdr1->lpNext			= NULL;
	m_pWaveHdr1->reserved		= 0;
	waveOutPrepareHeader(m_hWaveOut,m_pWaveHdr1,sizeof(WAVEHDR));
	waveOutWrite(m_hWaveOut,m_pWaveHdr1,sizeof(WAVEHDR));
	return 0L;
}

LONG CWave::OnMmWomDone(UINT wParam,LONG lParam)
{
	waveOutUnprepareHeader(m_hWaveOut,m_pWaveHdr1,sizeof(WAVEHDR));
	waveOutClose(m_hWaveOut);
	return 0L;
}

LONG CWave::OnMmWomClose(UINT wParam,LONG lParam)
{
	m_uState=CWAVE_STATE_NOTHING;
	return 0L;
}

LONG CWave::OnMmWimOpen(UINT wParam,LONG lParam)
{
	m_uState=CWAVE_STATE_RECORD;

	m_pData = (BYTE*)GlobalReAllocPtr(m_pData,1,GHND);

	m_pWaveHdr1->lpData			= (LPSTR)m_pBuffer1;
	m_pWaveHdr1->dwBufferLength = BUFFER_SIZE;
	m_pWaveHdr1->dwBytesRecorded= 0L;
	m_pWaveHdr1->dwUser			= 0L;
	m_pWaveHdr1->dwFlags		= 0L;
	m_pWaveHdr1->dwLoops		= 1L;
	m_pWaveHdr1->lpNext			= NULL;
	m_pWaveHdr1->reserved		= 0L;
	waveInPrepareHeader(m_hWaveIn,m_pWaveHdr1,sizeof(WAVEHDR));
	waveInAddBuffer(m_hWaveIn,m_pWaveHdr1,sizeof(WAVEHDR));
	
	m_pWaveHdr2->lpData			= (LPSTR)m_pBuffer2;
	m_pWaveHdr2->dwBufferLength = BUFFER_SIZE;
	m_pWaveHdr2->dwBytesRecorded= 0L;
	m_pWaveHdr2->dwUser			= 0L;
	m_pWaveHdr2->dwFlags		= 0L;
	m_pWaveHdr2->dwLoops		= 1L;
	m_pWaveHdr2->lpNext			= NULL;
	m_pWaveHdr2->reserved		= 0L;
	waveInPrepareHeader(m_hWaveIn,m_pWaveHdr2,sizeof(WAVEHDR));
	waveInAddBuffer(m_hWaveIn,m_pWaveHdr2,sizeof(WAVEHDR));

	m_dwDataLength = 0;
	waveInStart(m_hWaveIn);
	return 0L;
}

LONG CWave::OnMmWimData(UINT wParam,LONG lParam)
{
	LPWAVEHDR pWaveHdr=(LPWAVEHDR)lParam;
	m_pData=(BYTE*)GlobalReAllocPtr(m_pData,m_dwDataLength+pWaveHdr->dwBytesRecorded,GMEM_MOVEABLE);
	memcpy((BYTE*)(m_pData+m_dwDataLength),pWaveHdr->lpData,pWaveHdr->dwBytesRecorded);
	m_dwDataLength+=pWaveHdr->dwBytesRecorded;
	
	pWaveHdr->dwBufferLength	= BUFFER_SIZE;
	pWaveHdr->dwBytesRecorded	= 0L;
	pWaveHdr->dwUser			= 0L;
	pWaveHdr->dwFlags			= 0L;
	waveInPrepareHeader(m_hWaveIn,pWaveHdr,sizeof(WAVEHDR));
	waveInAddBuffer(m_hWaveIn,pWaveHdr,sizeof(WAVEHDR));
	return 0L;
}

LONG CWave::OnMmWimClose(UINT wPAram,LONG lParam)
{
	m_uState=CWAVE_STATE_NOTHING;

	waveInUnprepareHeader(m_hWaveIn,m_pWaveHdr1,sizeof(WAVEHDR));
	waveInUnprepareHeader(m_hWaveIn,m_pWaveHdr2,sizeof(WAVEHDR));
	GlobalFreePtr(m_pBuffer1);
	GlobalFreePtr(m_pBuffer2);
	return 0L;
}

BOOL CWave::Play_Begin(PCMWAVEFORMAT pcm,BYTE* pData,DWORD dwLength)
{
	if(m_uState!=CWAVE_STATE_NOTHING)
		return FALSE;
	if(m_hWnd==NULL){
		if (!Create())
			return FALSE;
	}
	if(dwLength==0)
		return FALSE;

	m_dwDataLength = dwLength;
	m_pcmwf = pcm;
	m_pData = (BYTE*)GlobalReAllocPtr(m_pData,m_dwDataLength,GMEM_MOVEABLE);
	memcpy(m_pData,pData,dwLength);
	if(waveOutOpen(&m_hWaveOut,WAVE_MAPPER,(LPWAVEFORMATEX)&pcm,(DWORD)m_hWnd,0L,CALLBACK_WINDOW))
	{
		return FALSE;
	}
	return TRUE;
}

BOOL CWave::Play_Begin()
{
	if (m_uState!=CWAVE_STATE_NOTHING)
		return FALSE;
	if (m_hWnd==NULL){
		if (!Create())
			return FALSE;
	}
	if(m_dwDataLength==0)
		return TRUE;

	if(waveOutOpen(&m_hWaveOut,WAVE_MAPPER,(LPWAVEFORMATEX)&m_pcmwf,(DWORD)m_hWnd,0L,CALLBACK_WINDOW))
	{
		return FALSE;
	}
	return TRUE;
}

void CWave::Play_End()
{
	if(m_uState!=CWAVE_STATE_PLAY) return;
	waveOutReset(m_hWaveOut);
	waveOutClose(m_hWaveOut);
}

BOOL CWave::Record_Begin(PCMWAVEFORMAT pcm)
{
	if(m_uState!=CWAVE_STATE_NOTHING) return FALSE;
	if(m_hWnd==NULL){
		if (!Create())
			return FALSE;
	}
	m_pcmwf=pcm;
	m_pBuffer1=(BYTE*)GlobalAllocPtr(GMEM_MOVEABLE,BUFFER_SIZE);
	m_pBuffer2=(BYTE*)GlobalAllocPtr(GMEM_MOVEABLE,BUFFER_SIZE);
	if(waveInOpen(&m_hWaveIn,WAVE_MAPPER,(LPWAVEFORMATEX)&pcm,(DWORD)m_hWnd,0L,CALLBACK_WINDOW))
	{
		GlobalFreePtr(m_pBuffer1);
		GlobalFreePtr(m_pBuffer2);
		return FALSE;
	}
	return TRUE;
}

void CWave::Record_End()
{
	if(m_uState!=CWAVE_STATE_RECORD) return;
	waveInStop(m_hWaveIn);
	waveInReset(m_hWaveIn);
	waveInClose(m_hWaveIn);
}

BOOL CWave::Read(char* filename)
{
	HMMIO hmmio;
	MMCKINFO ckRIFF,ckInfo;
	//if(m_uState!=CWAVE_STATE_NOTHING) return FALSE;
	hmmio=mmioOpen(filename,NULL,MMIO_READ);
	if(hmmio==NULL) return FALSE;
	if(mmioDescend(hmmio,&ckRIFF,NULL,0))
	{
		mmioClose(hmmio,0);
		return FALSE;
	}
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
				if(mmioAscend(hmmio,&ckInfo,0)) { mmioClose(hmmio,0); return FALSE; }
			}
			else { mmioClose(hmmio,0); return FALSE; }
		}
		else { mmioClose(hmmio,0); return FALSE; }
	}
	else { mmioClose(hmmio,0); return FALSE; } 
	// 'data' chunk
	ckInfo.ckid=mmioFOURCC('d','a','t','a');
	if(!mmioDescend(hmmio,&ckInfo,&ckRIFF,MMIO_FINDCHUNK))
	{
		m_dwDataLength=ckInfo.cksize;
		m_pData=(BYTE*)GlobalReAllocPtr(m_pData,m_dwDataLength,GHND);
		if(mmioRead(hmmio,(LPSTR)m_pData,m_dwDataLength)!=(long)m_dwDataLength)
		{
			mmioClose(hmmio,0); return FALSE;
		}
	}
	else { mmioClose(hmmio,0); return FALSE; }
	mmioClose(hmmio,0);
	return TRUE;
}

BOOL CWave::Write(char* filename)
{
	HMMIO hmmio;
	MMCKINFO ckRIFF,ckInfo;
	//if(m_uState!=CWAVE_STATE_NOTHING) return FALSE;
	if(m_dwDataLength==0) return FALSE;
	hmmio=mmioOpen(filename,NULL,MMIO_WRITE|MMIO_CREATE);
	if(hmmio==NULL) return FALSE;
	memset(&ckRIFF,0,sizeof(MMCKINFO));
	ckRIFF.fccType=mmioFOURCC('W','A','V','E');
	if(mmioCreateChunk(hmmio,&ckRIFF,MMIO_CREATERIFF)!=0)
	{
		mmioClose(hmmio,0);	return FALSE;
	}
	// 'fmt ' chunk
	memset(&ckInfo,0,sizeof(MMCKINFO));
	ckInfo.ckid=mmioFOURCC('f','m','t',' ');
	if(!mmioCreateChunk(hmmio,&ckInfo,0))
	{
		if(mmioWrite(hmmio,(LPSTR)&m_pcmwf,sizeof(PCMWAVEFORMAT))==sizeof(PCMWAVEFORMAT))
		{
			if(mmioAscend(hmmio,&ckInfo,0)) { mmioClose(hmmio,0); return FALSE; }
		}
		else { mmioClose(hmmio,0); return FALSE; }
	}
	else { mmioClose(hmmio,0); return FALSE; }
	// 'data' chunk
	memset(&ckInfo,0,sizeof(MMCKINFO));
	ckInfo.ckid=mmioFOURCC('d','a','t','a');
	if(!mmioCreateChunk(hmmio,&ckInfo,0))
	{
		if(mmioWrite(hmmio,(LPSTR)m_pData,m_dwDataLength)==(long)m_dwDataLength)
		{
			if(mmioAscend(hmmio,&ckInfo,0)) { mmioClose(hmmio,0); return FALSE; }
		}
		else { mmioClose(hmmio,0); return FALSE; }
	}
	else { mmioClose(hmmio,0); return FALSE; }

	if(mmioAscend(hmmio,&ckRIFF,0)) { mmioClose(hmmio,0); return FALSE; }
	mmioClose(hmmio,0);
	return TRUE;
}

void CWave::Set_Wave(PCMWAVEFORMAT pcm,BYTE* pData,DWORD dwLength)
{
	if(m_hWnd==NULL) return;
	
	m_dwDataLength = dwLength;
	m_pcmwf = pcm;
	m_pData = (BYTE*)GlobalReAllocPtr(m_pData,m_dwDataLength,GMEM_MOVEABLE);
	memcpy(m_pData,pData,dwLength);
}
