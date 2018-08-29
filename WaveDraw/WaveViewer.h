#if !defined(AFX_WAVEViewer_H__69602A0F_2118_48D4_8FC5_FC9D3EBEC3D3__INCLUDED_)
#define AFX_WAVEViewer_H__69602A0F_2118_48D4_8FC5_FC9D3EBEC3D3__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

//////////////////////////////////////////////////////////////////////////////
// VC++ Class for ²���i����ܾ�
// ��l�@�̡GBala
// ��������G2003-2-1
// �ק����G2004-12-10 �[�J�ƹ�����\��
//
// �ק��01�GCCL
// �ק����G2004-11-29 �}�l�ѻP���դέק�
// �ק����G2004-12-27 Fix Bug SetViewBoundary
// �ק����G2005-06-15 Modify SetWaveData �b BYTE* �� short* �Ҧ�
// �@�@�@�@�@2005-06-15 �ץ� AddSelection �� AddMarkLine ����ɼ�b�w���ʰ��D
//
// �ק��02�GBala -- ²���i����ܾ� V2.0 ���}�o
// �ק����G2006-03-08 ���� V2.0 ���}�o
//
// *** �w��ק�å[�J�ק��ñ�W ***";
//
// �`�N�G���{���ݤޤJ winmm.lib
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

// �]�w��ܪ�����ܼ�
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
const BYTE SHOW_CONTINUES	= 0x02; // for wave : �O�_�s��, �Y���s��h���e�Ȭ� 0 ������
const BYTE SHOW_ML_IN_UTRI	= 0x01;
const BYTE SHOW_ML_IN_LTRI	= 0x02;
const BYTE SHOW_ML_IN_LINE	= 0x04;
const BYTE SINGLE_SELECTION	= 0x08;

// �]�w�C����ܼ�
const BYTE SETCOLOR_OUTL	= 0x00;
const BYTE SETCOLOR_BKG		= 0x01;
const BYTE SETCOLOR_WAVE	= 0x02;
const BYTE SETCOLOR_PLINE	= 0x04;
const BYTE SETCOLOR_SLINE	= 0x08;
const BYTE SETCOLOR_CURSOR	= 0x10;
const BYTE SETCOLOR_SELECT	= 0x20;
const BYTE SETCOLOR_MARK	= 0x40;

// ��L
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

struct LINE_INFO{ // �򥻽u���Ѽ�
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

struct WAVE_INFO{ // �i�ΰѼ�
	UINT nID;			 // �s��
	UINT nXCorID;		 // �ҨϥΪ� X �b
	UINT nYCorID;		 // �ҨϥΪ� Y �b
	double dbSampleRate; // �C�Ӫi�γ��n�O, �~��e�b�@�_!
	double *pdbWaveData; // �i�θ��
	UINT nWaveLength;	 // �i�Ϊ��� (in sample)
	BYTE nDisplayFlag;	 // �]�w�U�����
	LINE_INFO PrimaryInfo;
	PCMWAVEFORMAT m_WaveFormat; // CCL �[��, �������

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

struct COOR_INFO{ // �y�жb�Ѽ�
	UINT nID;				// �s��
	UINT nShowLableID;		// �n��ܼ��Ҫ��i�νs��
	BYTE nPosType;			// �y�жb��m (0, 1, 2) = (���I, �k/�U, ��/�W)
	double dbMaxValue;		// �̤j��, X �b�H ms �O��, Y �b�ѨϥΪ̦ۭq
	double dbMinValue;		// �̤p��, X �b�H ms �O��, Y �b�ѨϥΪ̦ۭq
	double dbPriLineInt;	// �D�n�u���j (X �b�H ms �����O��, Y �b�H�ϥΪ̦ۭq���O��)
	UINT nSecLineInt;		// ���n�u���j (�C�ӥD�n�u�������X�Ӷ��j)
	BYTE nDisplayFlag;		// �]�w�U�����
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

struct VIEWER_INFO // �����ܾ��Ѽ�
{
	COLORREF OutlColor;	// �~���C��
	COLORREF BkgdColor;	// �I���C��
	COLORREF PCurColor;	// �������C��
	COLORREF SeleColor;	// ��ܰϬq�C��
	COLORREF MarkColor;	// �аO�u�C��
	BYTE nDisplayFlag;	// �]�w�U�����
	UINT nWaveCount;
	UINT nXCoorCount;
	UINT nYCoorCount;
	WAVE_INFO *pWaveInfo;
	COOR_INFO *pXCoorInfo;
	COOR_INFO *pYCoorInfo;

	VIEWER_INFO(){
		OutlColor = RGB(200, 200, 200);	// �~���C��
		BkgdColor = RGB(0, 0, 0);		// �I���C��
		PCurColor = RGB(150, 150, 255);	// �������C��
		SeleColor = RGB(255, 255, 150);	// ��ܰϬq�C��
		MarkColor = RGB(255, 0, 0);		// �аO�u�C��
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

struct SELECTION_INFO // ��ܰϬq�Ѽ�
{
	// �]���n�䴩�h�Ӽжb,
	// �ҥH�n�H��� waveviewer ���ʤ���O��
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
	BOOL ReadWaveFile(CString csFilename, UINT nAppX = NO_ID, UINT nAppY = NO_ID);	// ������J���ɸ��
	BOOL SetWaveData(BYTE *pnData, DWORD dwLen, double dbSamRate, UINT nAppX = NO_ID, UINT nAppY = NO_ID);
	BOOL SetWaveData(short *pnData, DWORD dwLen, double dbSamRate, UINT nAppX = NO_ID, UINT nAppY = NO_ID);
	BOOL SetWaveData(double *pdbData, DWORD dwLen, double dbSamRate, UINT nAppX = NO_ID, UINT nAppY = NO_ID);
	BOOL SetWaveData2(double *pdbData, DWORD dwLen, double dbSamRate, UINT nAppX = NO_ID, UINT nAppY = NO_ID);
	BOOL FindtheMax(double *pdbData, DWORD dwLen);
	double theMax;
	VOID ResetWaveData();

	// 0:�S��
	// 1:���k���� 2:�W�U����
	// 3:���N���� 4:���k�Y�� 5:�W�U�Y�� 6:���N�Y��
	// 7:��ܭ��q 8:�[�J�нu
	VOID SetMouseType(BYTE nMT = 0) {nMouseType = nMT;};

	VOID SetSingleSelection(BOOL bSingle = FALSE);
	VOID SetXInt(int nPriInt, int nSecInt, UINT nCoorID = NO_ID);		// �]�w X �b�u���j
	VOID SetXInt_Samp(int nPriInt, int nSecInt, UINT nCoorID = NO_ID);	// �H Sample �Ƴ]�w X �b�u���j
	VOID SetXInt_MSec(int nPriInt, int nSecInt, UINT nCoorID = NO_ID);	// �H m-sec �]�w X �b�u���j
	VOID SetYInt(int nPriInt, int nSecInt, UINT nCoorID = NO_ID);		// �]�w Y �b�u���j
	VOID SetMaxYValue(double dbMR, UINT nCoorID = NO_ID);
	VOID ResetViewport();
	VOID ResetViewportX();
	VOID ResetViewportY();

	virtual VOID SetAxisFormatY(int nDigitsY = 0);	// �]�w Y �y�жb�ݭn�h�ּƦr�e��

	VOID SetColor(COLORREF crColor, BYTE nFlag, UINT nID = NO_ID);
	VOID SetPlayCursor(DWORD nStart = 0, DWORD nEnd = UINT_MAX, UINT nWaveID = NO_ID); // �]�w����ʵe
	VOID SetPlayPause();
	VOID SetPlayRestart();
	VOID SetPlayStop();
	VOID AddSelection(DWORD nStart = 0, DWORD nEnd = UINT_MAX, UINT nWaveID = NO_ID);	// �[�J��ܽd��
	VOID AddSelection_MS(double dbStart = -1, double dbEnd = -1, UINT nWaveID = NO_ID);	// �[�J��ܽd��
	VOID ClearSelection();																// �M��������ܽd��
	VOID ClearSelection(int nIndex);													// �M���Y�ӿ�ܽd��
	VOID AddMarkLine(DWORD nMark = UINT_MAX, UINT nWaveID = NO_ID);	// �[�J�аO�u
	VOID AddMarkLine_MS(double dbMark = -1, UINT nWaveID = NO_ID);	// �[�J�аO�u
	VOID ClearMarkLine();											// �M���аO�u
	BOOL GetViewBoundary(double *pdbStart = NULL, double *pdbEnd = NULL,
						 double *pdbTop = NULL, double *pdbBottom = NULL, UINT nWaveID = NO_ID);
	BOOL SetViewBoundary(double dbLeft = -1, double dbRight = -1,
						 double dbTop = DBL_MAX, double dbBottom = -DBL_MAX, UINT nWaveID = NO_ID);
	CRect GetViewerRect();
	CRect GetWaveRect();
	int GetPlayPos_Sam(UINT nWaveID = NO_ID);  // ���o�ثe�����Ц�m (��� Sample)
	double GetPlayPos_Sec(UINT nWaveID = NO_ID); // ���o�ثe�����Ц�m (��� Second)
	LONG GetXPos(DWORD dwSample, UINT nWaveID = NO_ID);  // ���o dwSample �ثe�b�ù��W���y��
	LONG GetXPos(double dbSecond, UINT nWaveID = NO_ID); // ���o dbSecond �ثe�b�ù��W���y��
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
	// 0:�S�� 1:���k���� 2:�W�U���� 3:���N���� 4:���k�Y��
	// 5:�W�U�Y�� 6:���N�Y�� 7:��ܭ��q 8:�[�J�нu
	BYTE nMouseType;
	BOOL bIsLPPress;
	CPoint cStartPoint;

	// variables for play cursor
	BYTE nPlayStatus;
	UINT nTimerIventID, nPlayCoordinate;
	double dbPlayStart, dbPlayEnd, dbPlayCurrent;

	int m_nDigitsY;	//�w�]�� = 0

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
