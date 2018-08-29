// TextProcess.h: interface for the TextProcess class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_TEXTPROCESS_H__7AD762D3_9284_4E2A_94FE_D345C4A7A8F5__INCLUDED_)
#define AFX_TEXTPROCESS_H__7AD762D3_9284_4E2A_94FE_D345C4A7A8F5__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include ".\\CART\\CART.h"
#include ".\\tts\\Phone.h"
#include ".\\tts\\Cttswave.h"
#include ".\\tts\\Word.h"
#include ".\\hts_engine\\hts_engine.h"
#include <vector>
#include "WaveFile.h"// ky add

using namespace std;

// ==================  �`�N���Ǥ��n����!!!!  ==========================

static char* Tonal[] = {"��","��","��","��","��","��","��",	// 7*2 + 1 = 15 (tonal-initial)
"��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��"}; // 16*2 + 6 = 38	(tonal-final)

static char* ExtendedInitial[] = {
"�t","�u","�v","�w","�x","�y","�z","�{","�|","�}",
"�~","��","��","��","��","��","��","��","��","��",
"��","��","��","��","�t��","�u��","�v��","�x��","�y��","�z��",
"�{��","����","����","����","����","����","����","����","����","����",
"����","�|��","�}��","�~��","�x��","�y��","�z��","�{��","����","����",
"����","�z��","�{��"
};	// 53

//// �ҥH�`�@�|��15 + 38 + 53 + 1(pau) = 107��sub-syllable model

static char* Ph97Phoneme[] = {
"jr","chr","shr","r","tz","tsz","sz",	// 0~6	(for tonal-initial)
"yi","wu","yu","a","o","e","ai","ei","au","ou","an","en","ang","ng","eh","er",	// 7~22	(for toanl-final)
"b","p","m","f","d","t","n","l","g","k",					// 23~32	(for extended-initial)
"h","j","ch","sh","jr","chr","shr","r","tz","tsz",			// 33~42
"sz","yi","wu","yu","bi","pi","mi","di","ti","ni",			// 43~52
"li","ji","chi","shi","ju","chu","shu","ru","tzu","tsu",	// 53~62	
"su","gu","ku","hu","du","tu","nu","lu","jiu","chiu",		// 63~72
"shiu","niu","liu"											// 73~75
};

// =====================================================================
static int giCnt ;

class TextProcess  
{
public:
	TextProcess();
	virtual ~TextProcess();

public:
	CTTSWave* ttswave;
	BOOL PlayEnd;
	static UINT ThreadChildProc(LPVOID ThreadInfo);
	double duration_s;
	double duration_p;
	double duration_c;
	double duration_sy;


public:
	CWord word;
	CString strInput, strDirPath, strFileTitle;
 	double dPitchRatio, dSpeakRate;
	int nSpeaker;
	int giSftIdx ; // ky add: shift of cue idx for each sentence. word-based.
	int* gduration_s;// ky add: time cue
	int* gduration_e;// ky add: time cue
	CString AllBig5;
	vector<int>	indexArray;
	vector<int> AllPWCluster;
	vector<int> AllPPCluster;

	void ProcessTheText();
	void GenLabels() ; // ky add
	void ConcatenateLabel( char* outfilename, char* dir, int iSentenceCnt ) ;// ky add

private:
	void CartPrediction(CString &sentence, CString &strBig5, vector<int>& allPWCluster, vector<int>& allPPCluster);
	int SplitString(const CString& input, const CString& delimiter, CStringArray& results);
	CString Phone2Ph97(CString phone,int tone);
	void GenerateLabelFile( CStringArray& sequence,		//	�y�l�������ǦC
						const int sBound[],const int wBound[],const int pBound[],	//	syllable/word/phrase ��� (�b�ĴX��phoneme����)
						const int sCount,const int wCount,const int pCount, CStdioFile& csFile, CStdioFile *pcsFile2 );	// syllable/word/phrase �ƥ� // ky modify
	void Synthesize(CString name,int c);
	void ConcatenateWave(CString name, int c);
	void GenerateBoundary(CString& csFileName, vector<int>& vecCluster, CString& strModelName);
	void PlayThePart(CString name, int c);
	bool timeinfo(int* duration_si,int* duration_ei); // ky add: assign time cue information
	bool initrd(); // ky add: initial global time cue information and shift
};

#endif // !defined(AFX_TEXTPROCESS_H__7AD762D3_9284_4E2A_94FE_D345C4A7A8F5__INCLUDED_)
