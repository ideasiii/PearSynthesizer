// cwave.h : header file
// (for visual c++ 4.0)

#ifndef __CWAVE_H__
#define __CWAVE_H__

#include "common.h"

#define CWAVE_STATE_NOTHING	0
#define CWAVE_STATE_RECORD	1
#define CWAVE_STATE_PLAY	2

#define BUFFER_SIZE         8192

/////////////////////////////////////////////////////////////////////////////
// CWave window

class CWave : public CWnd
{
private:
	HWAVEOUT	m_hWaveOut;
	HWAVEIN		m_hWaveIn;
	LPWAVEHDR	m_pWaveHdr1;
	LPWAVEHDR	m_pWaveHdr2;
	BYTE*		m_pBuffer1;
	BYTE*		m_pBuffer2;
	
protected:
	CWnd*		m_pParentWnd;
	DWORD		m_dwDataLength;
	BYTE*		m_pData;
	PCMWAVEFORMAT m_pcmwf;
	UINT		m_uState;

// Construction
public:
	CWave(CWnd* pParentWnd = NULL);
	BOOL Create();
	BOOL Play_Begin(PCMWAVEFORMAT pcm,BYTE* pData,DWORD dwLength);
	BOOL Play_Begin();
	void Play_End();
	BOOL Record_Begin(PCMWAVEFORMAT pcm);
	void Record_End();
	
	UINT State() { return m_uState; }
	DWORD GetLength() { return m_dwDataLength;}
	BYTE* GetBuffer() { return m_pData;}
	PCMWAVEFORMAT Info() { return m_pcmwf;}

	BOOL Read(char* filename);
	BOOL Write(char* filename);

	void Set_Wave(PCMWAVEFORMAT pcm, BYTE* pData, DWORD dwLength);

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CWave)
	//}}AFX_VIRTUAL

// Implementation
public:
	virtual ~CWave();

	// Generated message map functions
protected:
	//{{AFX_MSG(CWave)
	afx_msg LONG OnMmWomOpen (UINT wParam,LONG lParam);
	afx_msg LONG OnMmWomClose(UINT wParam,LONG lParam);
	afx_msg LONG OnMmWomDone (UINT wParam,LONG lParam);
	afx_msg LONG OnMmWimOpen (UINT wParam,LONG lParam);
	afx_msg LONG OnMmWimClose(UINT wParam,LONG lParam);
	afx_msg LONG OnMmWimData (UINT wParam,LONG lParam);
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

/////////////////////////////////////////////////////////////////////////////

#endif // __CWAVE_H__
