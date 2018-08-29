 // cwave.h : header file
// (for visual c++ 4.0)

#ifndef __CTTSWAVE_H__
#define __CTTSWAVE_H__

#include <mmsystem.h>
#define SAMPLING_RATE 20000

enum {CWAVE_NONE, CWAVE_RECORD, CWAVE_PLAY};

/////////////////////////////////////////////////////////////////////////////
// CTTSWave window

class CTTSWave : public CWnd
{
private:
	int			BUFFER_SIZE2;
	HWAVEOUT	m_hWaveOut;
	HWAVEIN		m_hWaveIn;
	LPWAVEHDR	m_pWaveHdr1;
	LPWAVEHDR	m_pWaveHdr2;
	LPWAVEHDR	m_pWaveHdr3;
	BYTE*		m_pBuffer1;
	BYTE*		m_pBuffer2;
protected:
	CToolBar    m_wndToolBar;
	CWnd*		m_pParentWnd;
	DWORD		old_dwByte;
	BYTE*		Part_pData;
	int			count;
	BYTE*		Pre_pData;
	BYTE*		m_pData_Temp;
	int 		m_state;
	MMRESULT    m_errorNo;
	BOOL		m_bAsync;
	DWORD       m_playBegin,m_playEnd;
public:
	PCMWAVEFORMAT m_pcm;

// Construction
public:
	CTTSWave(CWnd* pParentWnd,int sizeByte=16384, 
		  DWORD nRate=SAMPLING_RATE, WORD wBit=16, WORD nChannel=1);
	
// Attributes


// Operations
public:
	void Create();
	void SetSamplingRate(int rate);//change rate only
	void SetWaveFormat(DWORD nSamplesPerSec, WORD wBitsPerSample=16, WORD nChannel=1);
	void SetWaveFormat(PCMWAVEFORMAT pcm);
	void ExtractWave(DWORD beg, DWORD end);
	void SetWave(PCMWAVEFORMAT pcm, BYTE* pData,DWORD dwByte);
	void SetWave(BYTE* pData,DWORD dwByte);
	int  Terminate();
	//BOOL Play(void* pData,DWORD dwSample, BOOL bAsync=FALSE);
	BOOL Play(BOOL bAsync=FALSE);
	BOOL PlayPart(DWORD begin,DWORD end=0,BOOL bAsync=FALSE);
	void PlayEnd();
	BOOL Record();
	void RecordEnd();

	BOOL IsBusy();
	DWORD Bytes();
	DWORD Samples();
	DWORD GetLength() { return m_dwByte;}
	BYTE* GetBuffer();
	short * ShortBuffer(int begin=0);
	int   State() { return m_state;}
	void MsgPass();
	//JiunFu modify
	DWORD		Part_bytes;
	DWORD		Part_begin;
	DWORD		m_dwByte;
	DWORD		bytes;
	BYTE*		m_pData;
//	short*      m_pData;

	//PCMWAVEFORMAT GetPCMWaveFormat() { return m_pcm;}

	BOOL Read(CString &cs);
	BOOL Read(char* filename);
	BOOL Read(LPCTSTR filename, int begin, int sample);

	BOOL Write(CString &cs);
	BOOL Write(char* filename);
	void Append(void *pData,int samples);
	void Reset();
	BOOL GetPosition(int &pos, UINT type, BYTE *smpte=NULL);

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CTTSWave)
	//}}AFX_VIRTUAL

// Implementation
public:
	BOOL WritePart(char* filename);
	BOOL SetPart(DWORD PartBegin,DWORD PartEnd);
	BYTE* GetPartBuffer();
	DWORD GetPartLength(){ return Part_bytes;}
	virtual ~CTTSWave();

	// Generated message map functions
protected:
	//{{AFX_MSG(CTTSWave)
	afx_msg LONG OnMmWomOpen (UINT wParam,LONG lParam);
	afx_msg LONG OnMmWomClose(UINT wParam,LONG lParam);
	afx_msg LONG OnMmWomDone (UINT wParam,LONG lParam);
	afx_msg LONG OnMmWimOpen (UINT wParam,LONG lParam);
	afx_msg LONG OnMmWimClose(UINT wParam,LONG lParam);
	afx_msg LONG OnMmWimData (UINT wParam,LONG lParam);
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()

	void WaveOutError();
	void Convert2SignedChar();
};
/////////////////////////////////////////////////////////////////////////////

#endif // __CWAVE_H__
