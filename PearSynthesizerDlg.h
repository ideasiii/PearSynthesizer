// PearSynthesizerDlg.h : header file
//

#if !defined(AFX_PEARSYNTHESIZERDLG_H__E6449848_EC04_409C_8A79_CF3431EAFD5A__INCLUDED_)
#define AFX_PEARSYNTHESIZERDLG_H__E6449848_EC04_409C_8A79_CF3431EAFD5A__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "TextProcess.h"
#include "TextProgressDlg.h"
#include ".\\WaveDraw\\WaveViewer.h"
#include ".\\ColorList\\ColorListCtrl.h"

/////////////////////////////////////////////////////////////////////////////
// CPearSynthesizerDlg dialog

class CPearSynthesizerDlg : public CDialog
{
// Construction
public:
	TextProcess* txtProc;
	TextProgressDlg* txtProgDlg;
	BOOL Cont;
	static UINT ThreadProc(LPVOID Thread_Info);
public:
	CPearSynthesizerDlg(CWnd* pParent = NULL);	// standard constructor

// Dialog Data
	//{{AFX_DATA(CPearSynthesizerDlg)
	enum { IDD = IDD_PEARSYNTHESIZER_DIALOG };
	CComboBox	m_speaker;
	CSliderCtrl	m_slider_sr;
	CSliderCtrl	m_slider_f0r;
	CColorListCtrl	m_OutputList;
	CWaveViewer	m_WaveViewer1;
	CString	m_edit1;
	double	m_f0r;
	double	m_sr;
	//}}AFX_DATA

	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CPearSynthesizerDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
	HICON m_hIcon;

	// Generated message map functions
	//{{AFX_MSG(CPearSynthesizerDlg)
	virtual BOOL OnInitDialog();
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	afx_msg void OnSynthesizeBtn();
	afx_msg void OnPlayBtn();
	afx_msg void OnMtr2();
	afx_msg void OnMtr3();
	afx_msg void OnMtr4();
	afx_msg void OnMtr1();
	afx_msg void OnMtr5();
	afx_msg void OnMtr6();
	afx_msg void OnMtr7();
	afx_msg void OnMtr8();
	afx_msg void OnChecksingle();
	afx_msg void OnOpenfileBtn();
	afx_msg void OnDblclkOutputList(NMHDR* pNMHDR, LRESULT* pResult);
	virtual void OnOK();
	afx_msg void OnCustomdrawSlider1(NMHDR* pNMHDR, LRESULT* pResult);
	afx_msg void OnCustomdrawSlider2(NMHDR* pNMHDR, LRESULT* pResult);
	afx_msg void Onvocal();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
private:
	char path[1000];
	CString dirpath;
	CString strFileTitle;
	vector<int> UtteranceIdx;
	int SelectedIdx;
	void Print2List(vector<int>& indexArray, CString& allBig5, vector<int>& allPW, vector<int>& allPP);
	afx_msg void OnBnClickedOk();
	afx_msg void OnClose();
//	afx_msg void OnChar(UINT nChar, UINT nRepCnt, UINT nFlags);
	afx_msg void OnBnClickedButton1();  // ky add: generate labels for training
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_PEARSYNTHESIZERDLG_H__E6449848_EC04_409C_8A79_CF3431EAFD5A__INCLUDED_)
