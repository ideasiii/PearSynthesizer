// PearSynthesizerDlg.cpp : implementation file
//

#include "stdafx.h"
#include "PearSynthesizer.h"
#include "PearSynthesizerDlg.h"

#include".\\WaveDraw\\Sound_to_Pitch.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

struct thread_info 
{
	thread_info (){};
	TextProcess* tTxtProc;
	BOOL* tContinue;
} THREAD_INFO;

/////////////////////////////////////////////////////////////////////////////
// CAboutDlg dialog used for App About

class CAboutDlg : public CDialog
{
public:
	CAboutDlg();

// Dialog Data
	//{{AFX_DATA(CAboutDlg)
	enum { IDD = IDD_ABOUTBOX };
	//}}AFX_DATA

	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CAboutDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
	//{{AFX_MSG(CAboutDlg)
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

CAboutDlg::CAboutDlg() : CDialog(CAboutDlg::IDD)
{
	//{{AFX_DATA_INIT(CAboutDlg)
	//}}AFX_DATA_INIT
}

void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CAboutDlg)
	//}}AFX_DATA_MAP
}

BEGIN_MESSAGE_MAP(CAboutDlg, CDialog)
	//{{AFX_MSG_MAP(CAboutDlg)
		// No message handlers
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CPearSynthesizerDlg dialog

CPearSynthesizerDlg::CPearSynthesizerDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CPearSynthesizerDlg::IDD, pParent), m_OutputList(4)
{
	//{{AFX_DATA_INIT(CPearSynthesizerDlg)
	m_edit1 = _T("");
	m_f0r = 0.0;
	m_sr = 0.0;
	//}}AFX_DATA_INIT
	// Note that LoadIcon does not require a subsequent DestroyIcon in Win32
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
}

void CPearSynthesizerDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CPearSynthesizerDlg)
	DDX_Control(pDX, IDC_COMBO2, m_speaker);
	DDX_Control(pDX, IDC_SLIDER2, m_slider_sr);
	DDX_Control(pDX, IDC_SLIDER1, m_slider_f0r);
	DDX_Control(pDX, IDC_OUTPUT_LIST, m_OutputList);
	DDX_Control(pDX, IDC_WAVE1, m_WaveViewer1);
	DDX_Text(pDX, IDC_EDIT_INPUT, m_edit1);
	DDX_Text(pDX, IDC_EDIT1, m_f0r);
	DDV_MinMaxDouble(pDX, m_f0r, 0.5, 5.);
	DDX_Text(pDX, IDC_EDIT2, m_sr);
	DDV_MinMaxDouble(pDX, m_sr, -1., 1.);
	//}}AFX_DATA_MAP
}

BEGIN_MESSAGE_MAP(CPearSynthesizerDlg, CDialog)
	//{{AFX_MSG_MAP(CPearSynthesizerDlg)
	ON_WM_SYSCOMMAND()
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	ON_BN_CLICKED(IDC_SYNTHESIZE_BTN, OnSynthesizeBtn)
	ON_BN_CLICKED(IDC_PLAY_BTN, OnPlayBtn)
	ON_BN_CLICKED(IDC_MTR2, OnMtr2)
	ON_BN_CLICKED(IDC_MTR3, OnMtr3)
	ON_BN_CLICKED(IDC_MTR4, OnMtr4)
	ON_BN_CLICKED(IDC_MTR1, OnMtr1)
	ON_BN_CLICKED(IDC_MTR5, OnMtr5)
	ON_BN_CLICKED(IDC_MTR6, OnMtr6)
	ON_BN_CLICKED(IDC_MTR7, OnMtr7)
	ON_BN_CLICKED(IDC_MTR8, OnMtr8)
	ON_BN_CLICKED(IDC_CHECKSINGLE, OnChecksingle)
	ON_BN_CLICKED(IDC_OPENFILE_BTN, OnOpenfileBtn)
	ON_NOTIFY(NM_DBLCLK, IDC_OUTPUT_LIST, OnDblclkOutputList)
	ON_NOTIFY(NM_CUSTOMDRAW, IDC_SLIDER1, OnCustomdrawSlider1)
	ON_NOTIFY(NM_CUSTOMDRAW, IDC_SLIDER2, OnCustomdrawSlider2)
	//}}AFX_MSG_MAP
	ON_BN_CLICKED(IDOK, &CPearSynthesizerDlg::OnBnClickedOk)
	ON_WM_CLOSE()
//	ON_WM_CHAR()
ON_BN_CLICKED(IDC_BUTTON1, &CPearSynthesizerDlg::OnBnClickedButton1)
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CPearSynthesizerDlg message handlers

UINT MyThread(LPVOID pVar)
{
	CString cstemp;
	char path[1000];
	GetCurrentDirectory(1000, path);
	// kennyou said, kill old java 
	ExecuteCommand("taskkill /f /im java.exe");

	cstemp.Format("cd %s && java -Xmx400m -jar \".\\ChineseParserServer\\ChineseParserServer.jar\" -p \".\\ChineseParserServer\\chineseFactored.ser.gz\" -i input_line.tok -o output_line.tok -is input_line.flag -os output_line.flag -l 40",path);
	CStdioFile csf ;
	csf.Open( "s.bat", CFile::modeCreate | CFile::modeWrite ) ;
	csf.WriteString( cstemp ) ;
	csf.Close() ;
	ExecuteCommand("s.bat");
	
	cstemp.ReleaseBuffer() ;

	return 0;
}/*/**/

BOOL CPearSynthesizerDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// Add "About..." menu item to system menu.

	// IDM_ABOUTBOX must be in the system command range.
	ASSERT((IDM_ABOUTBOX & 0xFFF0) == IDM_ABOUTBOX);
	ASSERT(IDM_ABOUTBOX < 0xF000);

	CMenu* pSysMenu = GetSystemMenu(FALSE);
	if (pSysMenu != NULL)
	{
		CString strAboutMenu;
		strAboutMenu.LoadString(IDS_ABOUTBOX);
		if (!strAboutMenu.IsEmpty())
		{
			pSysMenu->AppendMenu(MF_SEPARATOR);
			pSysMenu->AppendMenu(MF_STRING, IDM_ABOUTBOX, strAboutMenu);
		}
	}

	// Set the icon for this dialog.  The framework does this automatically
	//  when the application's main window is not a dialog
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon
	
	// TODO: Add extra initialization here

	// 取得程式執行路徑
	strFileTitle = "";
	GetCurrentDirectory(1000, path);
	dirpath = path;

	// Parser
	bool newone=true;
	if (newone)
	{
		CWinThread* m_thread;
		m_thread = AfxBeginThread(MyThread,(LPVOID)this);
	}		
	
	// 初始化m_OutputList
	SelectedIdx = -1;
	CStringArray Head;
	CByteArray Cols;
	Head.Add("Utt");			Cols.Add(20);
	Head.Add("BIG5");				Cols.Add(30);
	Head.Add("PW");			Cols.Add(25);
	Head.Add("PP");			Cols.Add(25);
	m_OutputList.InitCtrl(&Head, &Cols);

	// 初始化Slider
	m_slider_f0r.SetRange(0, 50, FALSE);
	m_slider_sr.SetRange(0, 10, FALSE);
	m_slider_f0r.SetPos(5);
	m_slider_sr.SetPos(5);

	// 初始化 Combo box (Speaker)
	m_speaker.AddString("女聲");	// selection = 0
	m_speaker.AddString("男聲");	// selection = 1
	m_speaker.AddString("高興");	// selection = 2
	m_speaker.AddString("生氣");	// selection = 3
	m_speaker.AddString("傷心");	// selection = 4
	m_speaker.AddString("小孩");	// selection = 5
	m_speaker.AddString("老人");	// selection = 6
	m_speaker.AddString("唐老鴨");	// selection = 7

	m_speaker.SetCurSel(0);

	m_f0r = 1.f;
	m_sr = 0.f;

	return TRUE;  // return TRUE  unless you set the focus to a control
}

void CPearSynthesizerDlg::OnSysCommand(UINT nID, LPARAM lParam)
{
	if ((nID & 0xFFF0) == IDM_ABOUTBOX)
	{
		CAboutDlg dlgAbout;
		dlgAbout.DoModal();
	}
	else
	{
		CDialog::OnSysCommand(nID, lParam);
	}
}

// If you add a minimize button to your dialog, you will need the code below
//  to draw the icon.  For MFC applications using the document/view model,
//  this is automatically done for you by the framework.

void CPearSynthesizerDlg::OnPaint() 
{
	if (IsIconic())
	{
		CPaintDC dc(this); // device context for painting

		SendMessage(WM_ICONERASEBKGND, (WPARAM) dc.GetSafeHdc(), 0);

		// Center icon in client rectangle
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// Draw the icon
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialog::OnPaint();
	}
}

// The system calls this to obtain the cursor to display while the user drags
//  the minimized window.
HCURSOR CPearSynthesizerDlg::OnQueryDragIcon()
{
	return (HCURSOR) m_hIcon;
}

void CPearSynthesizerDlg::OnMtr1() 
{
	m_WaveViewer1.SetMouseType(0);	
}

void CPearSynthesizerDlg::OnMtr2() 
{
	m_WaveViewer1.SetMouseType(1);	
}

void CPearSynthesizerDlg::OnMtr3() 
{
	m_WaveViewer1.SetMouseType(2);
}

void CPearSynthesizerDlg::OnMtr4() 
{
	m_WaveViewer1.SetMouseType(3);
}

void CPearSynthesizerDlg::OnMtr5() 
{
	m_WaveViewer1.SetMouseType(4);	
}

void CPearSynthesizerDlg::OnMtr6() 
{
	m_WaveViewer1.SetMouseType(5);	
}

void CPearSynthesizerDlg::OnMtr7() 
{
	m_WaveViewer1.SetMouseType(6);	
}

void CPearSynthesizerDlg::OnMtr8() 
{
	m_WaveViewer1.SetMouseType(7);	
}

void CPearSynthesizerDlg::OnChecksingle() 
{
	m_WaveViewer1.SetSingleSelection(((CButton*)(GetDlgItem(IDC_CHECKSINGLE)))->GetCheck());	
}

UINT CPearSynthesizerDlg::ThreadProc(LPVOID Thread_Info)
{
	thread_info *tPara = (thread_info *)Thread_Info;
	tPara->tTxtProc->ProcessTheText();
	*tPara->tContinue = FALSE;
	return 0;
}

void CPearSynthesizerDlg::OnOpenfileBtn() 
{
	CString csFilename = "Text Files (*.txt)|*.txt||";
	CFileDialog CT(TRUE, NULL, NULL, OFN_HIDEREADONLY|OFN_OVERWRITEPROMPT, csFilename, this);
	CStdioFile csFile;
	CString cstemp = "";
	m_edit1 = "";
	if( CT.DoModal() == IDOK ){
		strFileTitle = CT.GetFileTitle();
		csFile.Open( CT.GetFileName(), CFile::modeRead|CFile::typeText, NULL);
		csFile.ReadString(cstemp);	// 處理第一行
		m_edit1 += cstemp;
		while(csFile.ReadString(cstemp)){
// 			m_edit1 += 0x0D;	// 畫面上顯示換行
// 			m_edit1 += 0x0A;
			m_edit1 = m_edit1 + "\n" + cstemp;
		}
		UpdateData(FALSE);
		csFile.Close();
	}
}

void CPearSynthesizerDlg::OnSynthesizeBtn() 
{
	UpdateData(TRUE);
	if(m_edit1 == ""){
		AfxMessageBox("請輸入文字", MB_OK, 0);
	}
	else
	{
		m_WaveViewer1.ResetWaveData();
		//m_WaveViewer1.ResetWaveData();

		SetCurrentDirectory(dirpath);
		clock_t start, finish;		//	Compute the total running time 
		start = clock();
		if(strFileTitle.IsEmpty())	strFileTitle = "SomeFile";
		Cont = TRUE;

		THREAD_INFO.tContinue = &Cont;
		txtProc = new TextProcess;
		txtProc->strInput = m_edit1;
		txtProc->strDirPath = dirpath;
		txtProc->strFileTitle = strFileTitle;
		txtProc->dPitchRatio = m_f0r;
		txtProc->dSpeakRate = m_sr/10;
		txtProc->nSpeaker = m_speaker.GetCurSel();	// 0: 女聲, 1: 男聲
		THREAD_INFO.tTxtProc = txtProc;
		AfxBeginThread(ThreadProc, (LPVOID)&THREAD_INFO);

 		txtProgDlg = new TextProgressDlg;
 		txtProgDlg->Create(IDD_DIALOG_PROGRESS, this);

		CString strShow;
		int progress = 0;
		// set progress bar
		txtProgDlg->m_progress.SetBarColour(RGB(224, 102, 255));
		txtProgDlg->m_progress.SetRange(0, 100);
		txtProgDlg->m_progress.SetPos(0);
		txtProgDlg->ShowWindow(SW_SHOW);
		
		while(Cont == TRUE)
		{
			MSG msg;
			if(PeekMessage(&msg, NULL, 0, 0, PM_REMOVE))
			{
				TranslateMessage(&msg);
				DispatchMessage(&msg);
			}
			if(progress > 50)	Sleep(100);
			if(progress < 95)
			{
				strShow.Format("Processing %d%%", ++progress);
				txtProgDlg->m_progress.SetWindowText(strShow);
				txtProgDlg->m_progress.StepIt();
			}
			if(m_edit1.GetLength() < 40)	Sleep(50);
			//else	Sleep(2*m_edit1.GetLength());
		}

		// 讓進度表跑完
		while(progress < 100)
		{
			MSG msg;
			if(PeekMessage(&msg, NULL, 0, 0, PM_REMOVE))
			{
				TranslateMessage(&msg);
				DispatchMessage(&msg);
			}

			strShow.Format("Processing %d%%", ++progress);
			txtProgDlg->m_progress.SetWindowText(strShow);
			txtProgDlg->m_progress.StepIt();
			Sleep(2.0);
		}
		delete txtProgDlg;

		finish = clock();
		double duration = (double)(finish - start) / CLOCKS_PER_SEC;
		CString outtmp;
		CString strTimer;
		strTimer.Format( "duration=%2.1f seconds\n", duration);
		outtmp=strTimer;
		strTimer.Format( "sep=%2.1f seconds\n", txtProc->duration_s/ CLOCKS_PER_SEC);
		outtmp=outtmp+strTimer;
		strTimer.Format( "parser=%2.1f seconds\n", txtProc->duration_p/ CLOCKS_PER_SEC);
		outtmp=outtmp+strTimer;
		strTimer.Format( "cart=%2.1f seconds\n", txtProc->duration_c/ CLOCKS_PER_SEC);
		outtmp=outtmp+strTimer;
		strTimer.Format( "synthesis=%2.1f seconds\n", txtProc->duration_sy/ CLOCKS_PER_SEC);
		outtmp=outtmp+strTimer;
		MessageBox(outtmp, "Time to complete", 64);
		Print2List(txtProc->indexArray, txtProc->AllBig5, txtProc->AllPWCluster, txtProc->AllPPCluster);
		UtteranceIdx.resize(txtProc->indexArray.size(), 0);
		copy(txtProc->indexArray.begin(), txtProc->indexArray.end(), UtteranceIdx.begin());
		delete txtProc;
		m_WaveViewer1.ReadWaveFile(strFileTitle+".wav");
		m_WaveViewer1.SetWaveLineParameter(RGB(100,100,100), 0, FALSE, TRUE, 0);
		SelectedIdx = -1;
	}
}

void CPearSynthesizerDlg::OnPlayBtn() 
{
	SetCurrentDirectory(dirpath);

	CTTSWave *wave1 = new CTTSWave(NULL, 10, 16000, 16, 1);
	CFileFind finder;
	BOOL find;
	CString strFileName;

	// 決定現在顯示的波形為哪段音檔
	(SelectedIdx == -1) ? strFileName.Format("%s.wav",strFileTitle) : strFileName.Format("gen\\%s_%d.wav",strFileTitle, SelectedIdx);
	
	find = finder.FindFile(strFileName);
	if(!find)
	{
		strFileName.Format("Can't find the synthesized file !");
		MessageBox(strFileName, "Whoooops ~", 0);
	}
	else
	{
		wave1->Read(strFileName);
		wave1->Play(true);
		m_WaveViewer1.SetPlayCursor(-1, -1, 0);
		while(wave1->IsBusy())	;
		delete wave1;
	}
}

void CPearSynthesizerDlg::Print2List(vector<int>& indexArray, CString& allBig5, vector<int>& allPW, vector<int>& allPP)
{
	CString cstemp = "";
	CStringArray cstra;
	int i, j, k;
	m_OutputList.DeleteAllItems();

	for(i = 0, j = 0; i < allBig5.GetLength(); i += 2)
	{
		cstemp.Format("%d", indexArray[i/2]);
		cstra.Add(cstemp);		////////
		cstemp.Format("%c%c",allBig5.GetAt(i), allBig5.GetAt(i+1));
		cstra.Add(cstemp);
		cstemp.Format("%d", allPW[i/2]);
		cstra.Add(cstemp);
		if(allPW[i/2] == 1) {
			cstemp.Format("%d", allPP[j]-1);
			j++;
		}
		else
			cstemp.Format("0");
		cstra.Add(cstemp);

		m_OutputList.AddItem(&cstra, i/2);
		if(allPW[i/2] == 1) {
			if(allPP[j-1] == 2) {
				for(k = 0; k < 4; k++)
					m_OutputList.SetItemBackgndColor(ITEM_COLOR(22), i/2, k);
			}
			else {
				for(k = 0; k < 3; k++)
					m_OutputList.SetItemBackgndColor(ITEM_COLOR(23), i/2, k);
			}
		}
		cstra.RemoveAll();
	}
}

void CPearSynthesizerDlg::OnDblclkOutputList(NMHDR* pNMHDR, LRESULT* pResult) 
{
	SetCurrentDirectory(dirpath);

	POSITION pos = m_OutputList.GetFirstSelectedItemPosition();
	if(!pos) return;
	int nItem = m_OutputList.GetNextSelectedItem(pos);
	SelectedIdx = UtteranceIdx[nItem];
	CString strSelectedFile;
	strSelectedFile.Format("gen\\%s_%d.wav", strFileTitle, SelectedIdx);

	m_WaveViewer1.ResetWaveData();
	m_WaveViewer1.ResetWaveData();

	m_WaveViewer1.ReadWaveFile(strSelectedFile);

	strSelectedFile.Replace(".wav", ".f0");
	CFile f;
	f.Open( strSelectedFile, CFile::modeRead|CFile::typeBinary);
	int nLen1 = f.GetLength()/sizeof(float);
	float* pDataTemp = new float[nLen1];
	double *pData1 = new double[nLen1];		// pitch檔長度(in frames)
	f.Read(pDataTemp, nLen1*sizeof(float));
	f.Close();
	for(int i=0; i<nLen1; i++)	pData1[i] = (double)pDataTemp[i];
	delete [] pDataTemp;

	m_WaveViewer1.theMax = 0.0;
	m_WaveViewer1.FindtheMax(pData1, nLen1);
	m_WaveViewer1.SetWaveData2( pData1, nLen1, (double)nLen1/2.509, ADD_NEW, ADD_NEW);
	
	m_WaveViewer1.SetWaveLineParameter(RGB(100,100,100), 0, FALSE, TRUE, 0);
	m_WaveViewer1.SetWaveLineParameter(RGB(205,  0,205), 2, TRUE,  TRUE, 1);
	m_WaveViewer1.SetMaxYValue(m_WaveViewer1.theMax);

	delete [] pData1;
	*pResult = 0;
}

void CPearSynthesizerDlg::OnOK() 
{
	// TODO: Add extra validation here
// 	CString strFileName = "";
// 	CFileFind finder;
// 	BOOL find;
// 	find = finder.FindFile(dirpath+"\\gen\\*.*");
// 	while(find)
// 	{
// 		find = finder.FindNextFile();
// 		strFileName = finder.GetFilePath();
// 		DeleteFile(LPCSTR(strFileName));
// 	}

	CDialog::OnOK();
}

void CPearSynthesizerDlg::OnCustomdrawSlider1(NMHDR* pNMHDR, LRESULT* pResult) 
{
	m_f0r = double(m_slider_f0r.GetPos())/(double)10+0.5;
	UpdateData(FALSE);
	*pResult = 0;
}

void CPearSynthesizerDlg::OnCustomdrawSlider2(NMHDR* pNMHDR, LRESULT* pResult) 
{
	m_sr = double(m_slider_sr.GetPos())/(double)10-0.5;
	UpdateData(FALSE);
	*pResult = 0;
}

void CPearSynthesizerDlg::Onvocal() 
{
	// TODO: Add your control notification handler code here
	
}

void CPearSynthesizerDlg::OnBnClickedOk()
{
	// TODO: 在此加入控制項告知處理常式程式碼
	OnClose();
	CDialog::OnOK();
}

void CPearSynthesizerDlg::OnClose()
{
	// TODO: 在此加入您的訊息處理常式程式碼和 (或) 呼叫預設值
	// kennyou said, kill old java 
	ExecuteCommand("taskkill /f /im java.exe");
	CDialog::OnClose();
}


void CPearSynthesizerDlg::OnBnClickedButton1() // ky add: generate labels for training
{
	char TWavePath[MAX_PATH];   // root path of training data

	// get path of root of training data
	char szDir[MAX_PATH]; 
	BROWSEINFO   bi;  
	ITEMIDLIST   *pidl; 
	bi.hwndOwner   =   this->m_hWnd;  
	bi.pidlRoot   =   NULL;  
	bi.pszDisplayName   =   szDir;  
	bi.lpszTitle   =   "getdir";  
	bi.ulFlags   =   BIF_RETURNONLYFSDIRS;  
	bi.lpfn   =   NULL;  
	bi.lParam   =   0;  
	bi.iImage   =   0;  
	pidl   =   SHBrowseForFolder(&bi);  
	if(pidl   ==   NULL || !SHGetPathFromIDList(pidl,   szDir))    
		return;
	SHGetPathFromIDList( pidl, TWavePath );

	// new a TextProcess, and assign parameter for generating labels
	txtProc = new TextProcess;
	txtProc->dPitchRatio = m_f0r;
	txtProc->dSpeakRate = m_sr/10;
	txtProc->nSpeaker = m_speaker.GetCurSel();	// 0: 女聲, 1: 男聲
	txtProc->strDirPath = TWavePath; // path to root of training data

	// get file list from name of wav files in /wav/*.wav
	CFileFind finder;
	CString filepath;  // file mask for CFileFinder
	CString filename;  // filename of the training file
	CStdioFile label;  // IO class. used in loading sentences in the training file
	CString datain;	// sentences in the current training file
	char csTargetFileName[MAX_PATH];  // filename of training file with absolute path
	filepath.Format("%s/wav/*.wav",TWavePath);
	bool bworking = finder.FindFile(filepath);
	while (bworking)
	{
		// get filename of training path, and load sentences into datain
		bworking = finder.FindNextFile();
		filename= finder.GetFileTitle();
		sprintf(csTargetFileName,"%s/txt/%s.txt",TWavePath,filename);
		label.Open(csTargetFileName,CFile::modeRead,NULL);
		label.ReadString(datain);
		label.Close() ;

		// assign parameters, then generate labels
		txtProc->strFileTitle = filename;  // filename of the training file without extension name
		txtProc->strInput = datain;  // sentences in this training training file
		txtProc->GenLabels();  // DO label generation for sentences: datain
	}
	delete txtProc ;

	AfxMessageBox( _T("Fanish!") ) ; // rosy said.
}

