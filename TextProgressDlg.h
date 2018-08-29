#if !defined(AFX_TEXTPROGRESSDLG_H__9B950AA2_159D_4A4D_B501_54E0804B9DD9__INCLUDED_)
#define AFX_TEXTPROGRESSDLG_H__9B950AA2_159D_4A4D_B501_54E0804B9DD9__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000
// TextProgressDlg.h : header file
//

#include ".\\WaveDraw\\TextProgressCtrl.h"

/////////////////////////////////////////////////////////////////////////////
// TextProgressDlg dialog

class TextProgressDlg : public CDialog
{
// Construction
public:
	TextProgressDlg(CWnd* pParent = NULL);   // standard constructor

// Dialog Data
	//{{AFX_DATA(TextProgressDlg)
	enum { IDD = IDD_DIALOG_PROGRESS };
	CTextProgressCtrl	m_progress;
	//}}AFX_DATA


// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(TextProgressDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:

	// Generated message map functions
	//{{AFX_MSG(TextProgressDlg)
	virtual BOOL OnInitDialog();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_TEXTPROGRESSDLG_H__9B950AA2_159D_4A4D_B501_54E0804B9DD9__INCLUDED_)
