// TextProgressDlg.cpp : implementation file
//

#include "stdafx.h"
#include "PearSynthesizer.h"
#include "TextProgressDlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// TextProgressDlg dialog


TextProgressDlg::TextProgressDlg(CWnd* pParent /*=NULL*/)
	: CDialog(TextProgressDlg::IDD, pParent)
{
	//{{AFX_DATA_INIT(TextProgressDlg)
		// NOTE: the ClassWizard will add member initialization here
	//}}AFX_DATA_INIT
}


void TextProgressDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(TextProgressDlg)
	DDX_Control(pDX, IDC_PROGRESS1, m_progress);
	//}}AFX_DATA_MAP
}


BEGIN_MESSAGE_MAP(TextProgressDlg, CDialog)
	//{{AFX_MSG_MAP(TextProgressDlg)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// TextProgressDlg message handlers

BOOL TextProgressDlg::OnInitDialog() 
{
	CDialog::OnInitDialog();
	
	// TODO: Add extra initialization here
	// Progress control setting
	m_progress.SetShowText(TRUE);
	m_progress.SetBarColour(RGB(17, 177, 255));

	return TRUE;  // return TRUE unless you set the focus to a control
	              // EXCEPTION: OCX Property Pages should return FALSE
}
