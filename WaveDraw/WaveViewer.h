#if !defined(AFX_WAVEViewer_H__69602A0F_2118_48D4_8FC5_FC9D3EBEC3D3__INCLUDED_)
#define AFX_WAVEViewer_H__69602A0F_2118_48D4_8FC5_FC9D3EBEC3D3__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

//////////////////////////////////////////////////////////////////////////////
// VC++ Class for 簡易波形顯示器
// 原始作者：Bala
// 完成日期：2003-2-1
// 修改日期：2004-12-10 加入滑鼠選取功能
//
// 修改者01：CCL
// 修改日期：2004-11-29 開始參與測試及修改
// 修改日期：2004-12-27 Fix Bug SetViewBoundary
// 修改日期：2005-06-15 Modify SetWaveData 在 BYTE* 及 short* 模式
// 　　　　　2005-06-15 修正 AddSelection 及 AddMarkLine 的邊界潛在安全性問題
//
// 修改者02：Bala -- 簡易波形顯示器 V2.0 版開發
// 修改日期：2006-03-08 完成 V2.0 版開發
//
// *** 歡迎修改並加入修改者簽名 ***";
//
// 注意：本程式需引入 winmm.lib
//////////////////////////////////////////////////////////////////////////////

// WaveViewer.h : header file
//

#include <mmsystem.h>
#include <afxtempl.h>
#include <float.h>

// ========================================================================================
//
// constance definition
//
// ========================================================================================

// 設定顯示物件用變數
#define SHOW_X_LABEL SHOW_PRI_LABEL	// for previous version compatibility 
#define SHOW_Y_LABEL SHOW_PRI_LABEL	// for previous version compatibility
#define SHOW_X_IN_SAM SHOW_IN_SAM	// for previous version compatibility
#define SHOW_LABLE_DEFAULT SHOW_PRI_LABEL|SHOW_PRI_LINE|SHOW_SEC_LINE
const BYTE SHOW_PRI_LABEL	= 0x01; // for coordinate
const BYTE SHOW_SEC_LABEL	= 0x02; // for coordinate
const BYTE SHOW_PRI_LINE	= 0x04; // for coordinate
const BYTE SHOW_SEC_LINE	= 0x08; // for coordinate
const BYTE SHOW_IN_SAM		= 0x10; // for coordinate
const BYTE SHOW_SAMPLE		= 0x01; // for wave
const BYTE SHOW_CONTINUES	= 0x02; // for wave : 是否連續, 若不連續則不畫值為 0 的部分
const BYTE SHOW_ML_IN_UTRI	= 0x01;
const BYTE SHOW_ML_IN_LTRI	= 0x02;
const BYTE SHOW_ML_IN_LINE	= 0x04;
const BYTE SINGLE_SELECTION	= 0x08;

// 設定顏色用變數
const BYTE SETCOLOR_OUTL	= 0x00;
const BYTE SETCOLOR_BKG		= 0x01;
const BYTE SETCOLOR_WAVE	= 0x02;
const BYTE SETCOLOR_PLINE	= 0x04;
const BYTE SETCOLOR_SLINE	= 0x08;
const BYTE SETCOLOR_CURSOR	= 0x10;
const BYTE SETCOLOR_SELECT	= 0x20;
const BYTE SETCOLOR_MARK	= 0x40;

// 其他
const BYTE SET_NON_COOR		= 0;
const BYTE SET_X_COOR		= 1;
const BYTE SET_Y_COOR		= 2;
const BYTE SET_XY_COOR		= 3;
const UINT NO_ID			= 0xFFFFFFFF;
const UINT ADD_NEW			= 0xFFFFFFFE;
const BYTE STATE_NOTHING	= 0;
const BYTE STATE_PLAYING	= 1;
const BYTE STATE_PAUSING	= 2;

// ========================================================================================
//
// strcutures type define
//
// ========================================================================================

struct LINE_INFO{ // 基本線型參數
	int nWidth;
	BOOL bVisible;
	COLORREF clrColor;

	LINE_INFO(){
		nWidth = 0;
		bVisible = TRUE;
		clrColor = RGB(0, 0, 0);
	}
	LINE_INFO& operator=(LINE_INFO& m_null){}; // no copy operator
};

struct WAVE_INFO{ // 波形參數
	UINT nID;			 // 編號
	UINT nXCorID;		 // 所使用的 X 軸
	UINT nYCorID;		 // 所使用的 Y 軸
	double dbSampleRate; // 每個波形都要記, 才能畫在一起!
	double *pdbWaveData; // 波形資料
	UINT nWaveLength;	 // 波形長度 (in sample)
	BYTE nDisplayFlag;	 // 設定各項顯示
	LINE_INFO PrimaryInfo;
	PCMWAVEFORMAT m_WaveFormat; // CCL 加的, 不知何用

	WAVE_INFO(){
		nID = 0;
		nXCorID = 0;
		nYCorID = 0;
		dbSampleRate = 0;
		nWaveLength = 0;
		pdbWaveData = NULL;
		nDisplayFlag = SHOW_CONTINUES;
		PrimaryInfo.clrColor = RGB(255, 255, 255);
	}
	~WAVE_INFO(){
		delete [] pdbWaveData;
		pdbWaveData = NULL;
	}
	WAVE_INFO& operator=(WAVE_INFO& m_w){
		nID = m_w.nID;
		nXCorID = m_w.nXCorID;
		nYCorID = m_w.nYCorID;
		dbSampleRate = m_w.dbSampleRate;
		nDisplayFlag = m_w.nDisplayFlag;
		nWaveLength = m_w.nWaveLength;
		pdbWaveData = new double[nWaveLength];
		memcpy(pdbWaveData, m_w.pdbWaveData, nWaveLength*sizeof(double));
		memcpy(&PrimaryInfo, &(m_w.PrimaryInfo), sizeof(LINE_INFO));
		memcpy(&m_WaveFormat, &(m_w.m_WaveFormat), sizeof(PCMWAVEFORMAT));

		return *this;
	}
};

struct COOR_INFO{ // 座標軸參數
	UINT nID;				// 編號
	UINT nShowLableID;		// 要顯示標籤的波形編號
	BYTE nPosType;			// 座標軸位置 (0, 1, 2) = (原點, 右/下, 左/上)
	double dbMaxValue;		// 最大值, X 軸以 ms 記錄, Y 軸由使用者自訂
	double dbMinValue;		// 最小值, X 軸以 ms 記錄, Y 軸由使用者自訂
	double dbPriLineInt;	// 主要線間隔 (X 軸以 ms 為單位記錄, Y 軸以使用者自訂單位記錄)
	UINT nSecLineInt;		// 次要線間隔 (每個主要線間分成幾個間隔)
	BYTE nDisplayFlag;		// 設定各項顯示
	LOGFONT lgPriLogFont;
	LOGFONT lgSecLogFont;
	LINE_INFO PriLineInfo;
	LINE_INFO SecLineInfo;

	COOR_INFO(){
		nID = 0;
		nShowLableID = 0;
		nPosType = 1;
		dbMaxValue = -1.7E308;
		dbMinValue =  1.7E308;
		dbPriLineInt = 0;
		nSecLineInt = 5;
		nDisplayFlag = SHOW_LABLE_DEFAULT;
		PriLineInfo.clrColor = RGB(100, 100, 100);
		SecLineInfo.clrColor = RGB(40, 40, 40);

		// wave format 20050618 CCL modified
		lgPriLogFont.lfHeight = 12;
		lgPriLogFont.lfWidth = 0;
		lgPriLogFont.lfEscapement = 0;
		lgPriLogFont.lfOrientation = 0;
		lgPriLogFont.lfWeight = FW_NORMAL;
		lgPriLogFont.lfItalic = FALSE;
		lgPriLogFont.lfUnderline = FALSE;
		lgPriLogFont.lfStrikeOut = FALSE;
		lgPriLogFont.lfCharSet = DEFAULT_CHARSET;
		lgPriLogFont.lfOutPrecision = OUT_DEFAULT_PRECIS;
		lgPriLogFont.lfClipPrecision = CLIP_DEFAULT_PRECIS;
		lgPriLogFont.lfQuality = ANTIALIASED_QUALITY;
		lgPriLogFont.lfPitchAndFamily  = DEFAULT_PITCH | FF_DONTCARE;

		lgSecLogFont.lfHeight = 10;
		lgSecLogFont.lfWidth = 0;
		lgSecLogFont.lfEscapement = 0;
		lgSecLogFont.lfOrientation = 0;
		lgSecLogFont.lfWeight = FW_NORMAL;
		lgSecLogFont.lfItalic = FALSE;
		lgSecLogFont.lfUnderline = FALSE;
		lgSecLogFont.lfStrikeOut = FALSE;
		lgSecLogFont.lfCharSet = DEFAULT_CHARSET;
		lgSecLogFont.lfOutPrecision = OUT_DEFAULT_PRECIS;
		lgSecLogFont.lfClipPrecision = CLIP_DEFAULT_PRECIS;
		lgSecLogFont.lfQuality = ANTIALIASED_QUALITY;
		lgSecLogFont.lfPitchAndFamily  = DEFAULT_PITCH | FF_DONTCARE;

		char cFontFace[] = "Times New Roman";
		memcpy(lgPriLogFont.lfFaceName, cFontFace, LF_FACESIZE);
		memcpy(lgSecLogFont.lfFaceName, cFontFace, LF_FACESIZE);
	}
	COOR_INFO& operator=(COOR_INFO& m_null){}; // no copy operator
};

struct VIEWER_INFO // 整個顯示器參數
{
	COLORREF OutlColor;	// 外框顏色
	COLORREF BkgdColor;	// 背景顏色
	COLORREF PCurColor;	// 播放游標顏色
	COLORREF SeleColor;	// 選擇區段顏色
	COLORREF MarkColor;	// 標記線顏色
	BYTE nDisplayFlag;	// 設定各項顯示
	UINT nWaveCount;
	UINT nXCoorCount;
	UINT nYCoorCount;
	WAVE_INFO *pWaveInfo;
	COOR_INFO *pXCoorInfo;
	COOR_INFO *pYCoorInfo;

	VIEWER_INFO(){
		OutlColor = RGB(200, 200, 200);	// 外框顏色
		BkgdColor = RGB(0, 0, 0);		// 背景顏色
		PCurColor = RGB(150, 150, 255);	// 播放游標顏色
		SeleColor = RGB(255, 255, 150);	// 選擇區段顏色
		MarkColor = RGB(255, 0, 0);		// 標記線顏色
		nDisplayFlag = 0xFF;
		nWaveCount = 0;
		nXCoorCount = 0;
		nYCoorCount = 0;
		pWaveInfo = NULL;
		pXCoorInfo = NULL;
		pYCoorInfo = NULL;
	}
	~VIEWER_INFO(){
		delete [] pWaveInfo;
		delete [] pXCoorInfo;
		delete [] pYCoorInfo;
		pWaveInfo = NULL;
		pXCoorInfo = NULL;
		pYCoorInfo = NULL;
	}
	VIEWER_INFO& operator=(VIEWER_INFO& m_null){}; // no copy operator
};

struct SELECTION_INFO // 選擇區段參數
{
	// 因為要支援多個標軸,
	// 所以要以整個 waveviewer 的百分比記錄
	double dbStart;
	double dbEnd;
};

// ========================================================================================
//
// CWaveViewer class declaration
//
// ========================================================================================

class CWaveViewer : public CStatic
{
// Construction and Destruction
public:
	CWaveViewer();
	virtual ~CWaveViewer();

// Attributes
public:

// Operations
public:

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CWaveViewer)
	protected:
	virtual void PreSubclassWindow();
	//}}AFX_VIRTUAL

// Implementation
public:
	BOOL ShowLabel(BYTE nDisplayFlag = 0, BYTE nTarget = SET_X_COOR, UINT nCoorID = 0);
	void ShowMarkLine(BYTE nDisplayFlag = 0);
	void ShowSamplePoint(BOOL bShow = FALSE);
	BOOL ReadWaveFile(CString csFilename, UINT nAppX = NO_ID, UINT nAppY = NO_ID);	// 直接輸入音檔資料
	BOOL SetWaveData(BYTE *pnData, DWORD dwLen, double dbSamRate, UINT nAppX = NO_ID, UINT nAppY = NO_ID);
	BOOL SetWaveData(short *pnData, DWORD dwLen, double dbSamRate, UINT nAppX = NO_ID, UINT nAppY = NO_ID);
	BOOL SetWaveData(double *pdbData, DWORD dwLen, double dbSamRate, UINT nAppX = NO_ID, UINT nAppY = NO_ID);
	BOOL SetWaveData2(double *pdbData, DWORD dwLen, double dbSamRate, UINT nAppX = NO_ID, UINT nAppY = NO_ID);
	BOOL FindtheMax(double *pdbData, DWORD dwLen);
	double theMax;
	VOID ResetWaveData();

	// 0:沒事
	// 1:左右平移 2:上下平移
	// 3:任意平移 4:左右縮放 5:上下縮放 6:任意縮放
	// 7:選擇音段 8:加入標線
	VOID SetMouseType(BYTE nMT = 0) {nMouseType = nMT;};

	VOID SetSingleSelection(BOOL bSingle = FALSE);
	VOID SetXInt(int nPriInt, int nSecInt, UINT nCoorID = NO_ID);		// 設定 X 軸線間隔
	VOID SetXInt_Samp(int nPriInt, int nSecInt, UINT nCoorID = NO_ID);	// 以 Sample 數設定 X 軸線間隔
	VOID SetXInt_MSec(int nPriInt, int nSecInt, UINT nCoorID = NO_ID);	// 以 m-sec 設定 X 軸線間隔
	VOID SetYInt(int nPriInt, int nSecInt, UINT nCoorID = NO_ID);		// 設定 Y 軸線間隔
	VOID SetMaxYValue(double dbMR, UINT nCoorID = NO_ID);
	VOID ResetViewport();
	VOID ResetViewportX();
	VOID ResetViewportY();

	virtual VOID SetAxisFormatY(int nDigitsY = 0);	// 設定 Y 座標軸需要多少數字寬度

	VOID SetColor(COLORREF crColor, BYTE nFlag, UINT nID = NO_ID);
	VOID SetPlayCursor(DWORD nStart = 0, DWORD nEnd = UINT_MAX, UINT nWaveID = NO_ID); // 設定播放動畫
	VOID SetPlayPause();
	VOID SetPlayRestart();
	VOID SetPlayStop();
	VOID AddSelection(DWORD nStart = 0, DWORD nEnd = UINT_MAX, UINT nWaveID = NO_ID);	// 加入選擇範圍
	VOID AddSelection_MS(double dbStart = -1, double dbEnd = -1, UINT nWaveID = NO_ID);	// 加入選擇範圍
	VOID ClearSelection();																// 清除全部選擇範圍
	VOID ClearSelection(int nIndex);													// 清除某個選擇範圍
	VOID AddMarkLine(DWORD nMark = UINT_MAX, UINT nWaveID = NO_ID);	// 加入標記線
	VOID AddMarkLine_MS(double dbMark = -1, UINT nWaveID = NO_ID);	// 加入標記線
	VOID ClearMarkLine();											// 清除標記線
	BOOL GetViewBoundary(double *pdbStart = NULL, double *pdbEnd = NULL,
						 double *pdbTop = NULL, double *pdbBottom = NULL, UINT nWaveID = NO_ID);
	BOOL SetViewBoundary(double dbLeft = -1, double dbRight = -1,
						 double dbTop = DBL_MAX, double dbBottom = -DBL_MAX, UINT nWaveID = NO_ID);
	CRect GetViewerRect();
	CRect GetWaveRect();
	int GetPlayPos_Sam(UINT nWaveID = NO_ID);  // 取得目前播放游標位置 (單位 Sample)
	double GetPlayPos_Sec(UINT nWaveID = NO_ID); // 取得目前播放游標位置 (單位 Second)
	LONG GetXPos(DWORD dwSample, UINT nWaveID = NO_ID);  // 取得 dwSample 目前在螢幕上的座標
	LONG GetXPos(double dbSecond, UINT nWaveID = NO_ID); // 取得 dbSecond 目前在螢幕上的座標
	int GetMarklineCount(){return cMarkLineArray.GetSize();};
	int GetSelectionCount(){return cSelectionArray.GetSize();};
	int GetMarklinePOS_Sam(int nIdx = 0, UINT nWaveID = NO_ID);
	double GetMarklinePOS_Sec(int nIdx = 0, UINT nWaveID = NO_ID);
	BOOL GetSelectionPOS_Sam(int nIdx = 0, DWORD *pStart = NULL, DWORD *pEnd = NULL, UINT nWaveID = NO_ID);
	BOOL GetSelectionPOS_Sec(int nIdx = 0, double *pStart = NULL, double *pEnd = NULL, UINT nWaveID = NO_ID);

	// New member functions in V2.0
	VOID SetWaveLineParameter(COLORREF crColor, int nWidth, BOOL bVisible, BOOL bContinuous, UINT nWaveID = NO_ID);
	VOID SetXCoordinateParameter(BYTE nPosType, CFont *pPriLableFont, CFont *pSecLableFont, BOOL bVisible, UINT nCoorID = NO_ID);
	VOID SetYCoordinateParameter(BYTE nPosType, CFont *pPriLableFont, CFont *pSecLableFont, BOOL bVisible, UINT nCoorID = NO_ID);

	// PCMWAVEFORMAT support -- by CCL
	// 20060616 CCL added for less memory usage
	BOOL SetWaveFormat(PCMWAVEFORMAT const & waveFormat, UINT nWaveID = 0);
	PCMWAVEFORMAT GetWaveFormat(UINT nWaveID = 0) const;
	const double * GetWaveData(UINT nWaveID = 0);
	DWORD GetWaveLangthInSample(UINT nWaveID = 0) const;
	// End of 20060616 CCL added for less memory usage

	// Generated message map functions
protected:
	BYTE nShowMLFlag;
	// [name rule] VT: Virtual, CD: Coordinate, AT: actual
	double dbVTXDisplayFrom, dbVTXDisplayTo, dbVTYDisplayFrom, dbVTYDisplayTo;
	double dbSelectingStart, dbSelectingEnd;
	CRect cViewerRect, cWaveRect, cTempRect;
	CArray<double, double&> cMarkLineArray;
	CArray<SELECTION_INFO, SELECTION_INFO&> cSelectionArray;
	VIEWER_INFO ViewerInfo;

	// variables for mouse action processing
	// mouse type definition:
	// 0:沒事 1:左右平移 2:上下平移 3:任意平移 4:左右縮放
	// 5:上下縮放 6:任意縮放 7:選擇音段 8:加入標線
	BYTE nMouseType;
	BOOL bIsLPPress;
	CPoint cStartPoint;

	// variables for play cursor
	BYTE nPlayStatus;
	UINT nTimerIventID, nPlayCoordinate;
	double dbPlayStart, dbPlayEnd, dbPlayCurrent;

	int m_nDigitsY;	//預設值 = 0

	// Helper function
	void SetWaveRect();
	void PaintCoordinate(CDC *pDC, BOOL bIsX);
	BOOL SetNormalWaveData(double *m_pData, DWORD dwLen, double dbSamRate, UINT nAppX, UINT nAppY);
	static UINT PlayCursorThread(LPVOID pParam);

	//{{AFX_MSG(CWaveViewer)
	afx_msg void virtual OnPaint();
	afx_msg void virtual OnSize(UINT nType, int cx, int cy);
	afx_msg void virtual OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void virtual OnLButtonUp(UINT nFlags, CPoint point);
	afx_msg void virtual OnRButtonUp(UINT nFlags, CPoint point);
	afx_msg void virtual OnMouseMove(UINT nFlags, CPoint point);
	//}}AFX_MSG

	DECLARE_MESSAGE_MAP()
};

/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_WAVEViewer_H__69602A0F_2118_48D4_8FC5_FC9D3EBEC3D3__INCLUDED_)
