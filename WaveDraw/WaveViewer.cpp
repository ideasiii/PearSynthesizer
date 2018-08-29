// WaveViewer.cpp : implementation file
//

#include "stdafx.h"
#include "WaveViewer.h"
#include "math.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CWaveViewer

CWaveViewer::CWaveViewer() : m_nDigitsY(0)
{
	nMouseType = 0;
	bIsLPPress = FALSE;
	nShowMLFlag = SHOW_ML_IN_UTRI|SHOW_ML_IN_LTRI|SHOW_ML_IN_LINE;
	dbSelectingStart = -1;
	dbSelectingEnd = -1;
	nPlayStatus = 0; // not in playing

	srand((unsigned)time(NULL));
	nTimerIventID = rand();
}

CWaveViewer::~CWaveViewer()
{
}


BEGIN_MESSAGE_MAP(CWaveViewer, CStatic)
	//{{AFX_MSG_MAP(CWaveViewer)
	ON_WM_PAINT()
	ON_WM_SIZE()
	ON_WM_LBUTTONDOWN()
	ON_WM_LBUTTONUP()
	ON_WM_RBUTTONUP()
	ON_WM_MOUSEMOVE()
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CWaveViewer message handlers

//////////////////////////////////////
// System message function

VOID CWaveViewer::OnPaint() 
{
	CPaintDC DC(this); // device context for painting

	// prepare normalize term...
	double dbVTXDisplayRange = dbVTXDisplayTo - dbVTXDisplayFrom;
	double dbVTYDisplayRange = dbVTYDisplayTo - dbVTYDisplayFrom;

	// prepare memory dc
	CDC dc;
	CBitmap bm;
	dc.CreateCompatibleDC(&DC);
	bm.CreateCompatibleBitmap(&DC, cViewerRect.Width(), cViewerRect.Height());
	CBitmap *pOldBitmap = (CBitmap*)dc.SelectObject(&bm);

	// overall variables
	CPen cCurPen, *pOldPen;
	CBrush cCurBrush, *pOldBrush;

	// paint outline
	cCurPen.CreatePen(PS_SOLID, 0, ViewerInfo.OutlColor);
	pOldPen = (CPen*)dc.SelectObject(&cCurPen);
	dc.SelectStockObject(NULL_BRUSH);
	dc.FillSolidRect((LPCRECT)cViewerRect, ViewerInfo.BkgdColor);
	dc.Rectangle(cWaveRect);
	dc.SelectObject(pOldPen);
	cCurPen.DeleteObject();

	// �e�D�Ʈ�u�μ���
	PaintCoordinate(&dc, TRUE);  // X �b
	PaintCoordinate(&dc, FALSE); // Y �b

	// ��ø�Ͻd�򭭨�b�i�ΰ϶�
	CRgn cRegion;
	cRegion.CreateRectRgn(cWaveRect.left+1, cWaveRect.top+1, cWaveRect.right-1, cWaveRect.bottom-1);
	dc.SelectClipRgn(&cRegion);

	// �}�l�e�i��
	UINT nIndex, i;
	double dbTemp, dbCDXRange, dbCDYRange;
	int x, y;
	WAVE_INFO *pCurWave = NULL;
	COOR_INFO *pCurXCoor = NULL, *pCurYCoor = NULL;
	for (nIndex=0; nIndex<ViewerInfo.nWaveCount; nIndex++){
		pCurWave = &(ViewerInfo.pWaveInfo[nIndex]);

		cCurPen.CreatePen(	PS_SOLID,
							pCurWave->PrimaryInfo.nWidth,
							pCurWave->PrimaryInfo.clrColor);
		cCurBrush.CreateSolidBrush(pCurWave->PrimaryInfo.clrColor);
		pOldPen = (CPen*)dc.SelectObject(&cCurPen);
		pOldBrush = (CBrush*)dc.SelectObject(&cCurBrush);
		pCurXCoor = &(ViewerInfo.pXCoorInfo[pCurWave->nXCorID]);
		pCurYCoor = &(ViewerInfo.pYCoorInfo[pCurWave->nYCorID]);
		dbCDXRange = pCurXCoor->dbMaxValue - pCurXCoor->dbMinValue;
		dbCDYRange = pCurYCoor->dbMaxValue - pCurYCoor->dbMinValue;

		// ø�ϰ_���I
		dbTemp = pCurXCoor->dbMinValue + dbVTXDisplayFrom * dbCDXRange;
		UINT nSampleStart = (DWORD)(dbTemp * pCurWave->dbSampleRate / 1000.0);
		if (nSampleStart < 0) nSampleStart = 0;
		dbTemp = pCurXCoor->dbMinValue + dbVTXDisplayTo * dbCDXRange;
		UINT nSampleEnd = (DWORD)(dbTemp * pCurWave->dbSampleRate / 1000.0);
		if (nSampleEnd > pCurWave->nWaveLength) nSampleEnd = pCurWave->nWaveLength;

		// ���F�[�t !!!
		dbTemp = dbVTXDisplayRange * dbCDXRange;
		dbTemp = dbTemp * pCurWave->dbSampleRate / 1000.0;
		UINT nSamplesPerPixel = (UINT)(dbTemp / cWaveRect.Width());
		if (nSamplesPerPixel > 1)
		{
			// ���ΨC�� sample ���e, �u�n�e�X�̤j�̤p�N�n�F
			// move to start point...
			dbTemp = -1.0 * pCurXCoor->dbMinValue / dbCDXRange; // x'
			dbTemp = (dbTemp - dbVTXDisplayFrom) / dbVTXDisplayRange;
			x = (int)(cWaveRect.left + dbTemp * cWaveRect.Width());
			y = (int)(cWaveRect.top + (double)cWaveRect.Height() * (1.0 + dbVTYDisplayFrom / dbVTYDisplayRange));
			dc.MoveTo(x, y);

			double dmin = 1.7E+308;
			double dmax = -1.7E+308;
			DWORD nS = nSampleStart;
			while(nS < nSampleEnd)
			{
				for (i=0; i<nSamplesPerPixel; i++)
				{
					if (nS >= nSampleEnd) break;
					if (pCurWave->pdbWaveData[nS] > dmax) dmax = pCurWave->pdbWaveData[nS];
					if (pCurWave->pdbWaveData[nS] < dmin) dmin = pCurWave->pdbWaveData[nS];
					nS += 1;
				}
				dbTemp = 1000.0 * nS / pCurWave->dbSampleRate; // x
				dbTemp -= pCurXCoor->dbMinValue;
				dbTemp /= dbCDXRange; // x'
				dbTemp = (dbTemp - dbVTXDisplayFrom) / dbVTXDisplayRange;
				x = (int)(cWaveRect.left + dbTemp * cWaveRect.Width());
				y = (int)(cWaveRect.top + (double)cWaveRect.Height() * (1.0 - (dmax-dbVTYDisplayFrom) / dbVTYDisplayRange));
				if (pCurWave->nDisplayFlag & SHOW_CONTINUES) dc.LineTo(x, y);
				else{
					if (dmax != 0){
						dc.MoveTo(x, y);
						dc.LineTo(x, y);
					}
				}
				y = (int)(cWaveRect.top + (double)cWaveRect.Height() * (1.0 - (dmin-dbVTYDisplayFrom) / dbVTYDisplayRange));
				if (pCurWave->nDisplayFlag & SHOW_CONTINUES) dc.LineTo(x, y);
				else{
					if (dmin != 0){
						dc.MoveTo(x, y);
						dc.LineTo(x, y);
					}
				}
				dmin = 1.7E+308;
				dmax = -1.7E+308;
			}
		}else{
			// �C�� sample ���n�e
			// move to start point...
			dbTemp = -1.0 * pCurXCoor->dbMinValue / dbCDXRange; // x'
			dbTemp = (dbTemp - dbVTXDisplayFrom) / dbVTXDisplayRange;
			x = (int)(cWaveRect.left + dbTemp * cWaveRect.Width());
			y = (int)(cWaveRect.top + (double)cWaveRect.Height() * (1.0 + dbVTYDisplayFrom / dbVTYDisplayRange));
			dc.MoveTo(x, y);

			bool bPrevDataIsZero = false;
			for (i=nSampleStart; i<nSampleEnd; i++)
			{
				dbTemp = 1000.0 * i / pCurWave->dbSampleRate; // x
				dbTemp -= pCurXCoor->dbMinValue;
				dbTemp /= dbCDXRange; // x'
				dbTemp = (dbTemp - dbVTXDisplayFrom) / dbVTXDisplayRange;
				x = (int)(cWaveRect.left + dbTemp * cWaveRect.Width());
				y = (int)(cWaveRect.top + (double)cWaveRect.Height() * (1.0 - (pCurWave->pdbWaveData[i]-dbVTYDisplayFrom) / dbVTYDisplayRange));
				if (pCurWave->nDisplayFlag & SHOW_CONTINUES)
					dc.LineTo(x, y);
				else{
					if (pCurWave->pdbWaveData[i] != 0){
						if (bPrevDataIsZero) dc.MoveTo(x, y);
						else				 dc.LineTo(x, y);
						bPrevDataIsZero = false;
					}else{
						dc.MoveTo(x, y);
						bPrevDataIsZero = true;
					}
				}
				if (pCurWave->nDisplayFlag & SHOW_SAMPLE) dc.Rectangle(x-1, y-1, x+2, y+2);
			}
		}
		dc.SelectObject(pOldPen);
		dc.SelectObject(pOldBrush);
		cCurPen.DeleteObject();
		cCurBrush.DeleteObject();
	}

	// draw selection
	cCurBrush.CreateSolidBrush(ViewerInfo.SeleColor);
	pOldBrush = (CBrush*)dc.SelectObject(&cCurBrush);
	int x1, x2, nPreROP = dc.SetROP2(R2_XORPEN);
	for (nIndex=0; nIndex<(UINT)cSelectionArray.GetSize(); nIndex++)
	{	// cSelectionArray[].x-->start, cSelectionArray[].y-->end
		if ((cSelectionArray[nIndex].dbEnd	 <= dbVTXDisplayFrom) ||
			(cSelectionArray[nIndex].dbStart >= dbVTXDisplayTo)) continue;

		if (cSelectionArray[nIndex].dbStart < dbVTXDisplayFrom)
			x1 = cWaveRect.left;
		else{
			dbTemp = (cSelectionArray[nIndex].dbStart - dbVTXDisplayFrom) / dbVTXDisplayRange;
			x1 = (int)(cWaveRect.left + dbTemp * cWaveRect.Width()); // CCL modified 20041111 7:43PM
		}
		if (cSelectionArray[nIndex].dbEnd > dbVTXDisplayTo)
			x2 = cWaveRect.right;
		else{
			dbTemp = (cSelectionArray[nIndex].dbEnd - dbVTXDisplayFrom) / dbVTXDisplayRange;
			x2 = (int)(cWaveRect.left + dbTemp * cWaveRect.Width()); // CCL modified 20041111 7:43PM
		}
		dc.Rectangle(x1, cWaveRect.top, x2, cWaveRect.bottom);
	}
	// ���b��ܪ��ɭԥγo��...
	if ((dbSelectingStart != -1) && (dbSelectingEnd != -1)){
		dbTemp = (dbSelectingStart - dbVTXDisplayFrom) / dbVTXDisplayRange;
		x1 = (int)(cWaveRect.left + dbTemp * cWaveRect.Width());
		dbTemp = (dbSelectingEnd - dbVTXDisplayFrom) / dbVTXDisplayRange;
		x2 = (int)(cWaveRect.left + dbTemp * cWaveRect.Width());
		dc.Rectangle(x1, cWaveRect.top, x2, cWaveRect.bottom);
	}
	dc.SetROP2(nPreROP);
	dc.SelectObject(pOldBrush);
	cCurBrush.DeleteObject();

	// draw mark line
	cCurPen.CreatePen(PS_SOLID, 0, ViewerInfo.MarkColor);
	cCurBrush.CreateSolidBrush(ViewerInfo.MarkColor);
	pOldPen = (CPen*)dc.SelectObject(&cCurPen);
	pOldBrush = (CBrush*)dc.SelectObject(&cCurBrush);
	POINT pTriangle[3];
	for (nIndex=0; nIndex<(UINT)cMarkLineArray.GetSize(); nIndex++)
	{
		dbTemp = (cMarkLineArray[nIndex] - dbVTXDisplayFrom) / dbVTXDisplayRange;
		x = (int)(cWaveRect.left + dbTemp * cWaveRect.Width());
		pTriangle[0].x = x-5;
		pTriangle[1].x = x+5;
		pTriangle[2].x = x;
		if (ViewerInfo.nDisplayFlag & SHOW_ML_IN_UTRI)
		{
			pTriangle[0].y = cWaveRect.top;
			pTriangle[1].y = cWaveRect.top;
			pTriangle[2].y = cWaveRect.top+5;
			dc.Polygon(pTriangle, 3);
		}
		if (ViewerInfo.nDisplayFlag & SHOW_ML_IN_LTRI)
		{
			pTriangle[0].y = cWaveRect.bottom-1;
			pTriangle[1].y = cWaveRect.bottom-1;
			pTriangle[2].y = cWaveRect.bottom-6;
			dc.Polygon(pTriangle, 3);
		}
		if (ViewerInfo.nDisplayFlag & SHOW_ML_IN_LINE)
		{
			dc.MoveTo(x, cWaveRect.top);
			dc.LineTo(x, cWaveRect.bottom);
		}
	}
	dc.SelectObject(pOldPen);
	dc.SelectObject(pOldBrush);
	cCurPen.DeleteObject();
	cCurBrush.DeleteObject();

	// set to original draw region...
	cRegion.SetRectRgn((LPCRECT)cViewerRect);
	dc.SelectClipRgn(&cRegion);

	// copy to real DC
	DC.BitBlt(0, 0, cViewerRect.Width(), cViewerRect.Height(), &dc, 0, 0, SRCCOPY);
	dc.SelectObject(pOldBitmap);
}

void CWaveViewer::PaintCoordinate(CDC *pDC, BOOL bIsX)
{
	// prepare normalize term...

	// �]���P�ɭn�e X �M Y �b, �ҥH�n�Ҽ{ 0 ����m
	int nATDisplayRange, nATDisplayFrom, nZeroPos;
	int nIndex, nCoorCount, nTemp, nTemp2;
	int nCoorY, nLableY, nCoorX, nLableX;
	int *pnPriPos_U0 = NULL, *pnPriPos_A0 = NULL, *pnSecPos_U0 = NULL, *pnSecPos_A0 = NULL;
	UINT i, j, nPriLineCount_U0, nPriLineCount_A0, nSecLineCount_U0, nSecLineCount_A0;
	double dbTemp, dbSecLineInt, dbVTMin, dbVTRange;
	double dbVTDisplayFrom, dbVTDisplayRange, dbCDRange;
	COOR_INFO *pCoor = NULL, *pCurCoor = NULL;
	CPen cCurPen, *pOldPen;
	CFont cCurFont, *pOldFont;

	if (bIsX){
		// �e X �b
		nCoorCount = ViewerInfo.nXCoorCount;
		pCoor = ViewerInfo.pXCoorInfo;
		dbVTDisplayRange = dbVTXDisplayTo - dbVTXDisplayFrom;
		dbVTDisplayFrom = dbVTXDisplayFrom;
		dbVTMin = 0;
		dbVTRange = 1;
		nATDisplayRange = cWaveRect.Width();
		nATDisplayFrom = cWaveRect.left;
	}else{
		// �e Y �b
		nCoorCount = ViewerInfo.nYCoorCount;
		pCoor = ViewerInfo.pYCoorInfo;
		dbVTDisplayRange = dbVTYDisplayTo - dbVTYDisplayFrom;
		dbVTDisplayFrom = dbVTYDisplayFrom;
		dbVTMin = -1;
		dbVTRange = 2;
		nATDisplayRange = cWaveRect.Height();
		nATDisplayFrom = 0;
	}

	for (nIndex=0; nIndex<nCoorCount; nIndex++){
		// �̷Ӯy�ж��ǵe, �᭱���|�\�b�e���W
		pCurCoor = &(pCoor[nIndex]);
		if (!pCurCoor->PriLineInfo.bVisible) continue;

		// �p��C�@���u��m
		dbSecLineInt = pCurCoor->dbPriLineInt / pCurCoor->nSecLineInt;
		dbCDRange = pCurCoor->dbMaxValue - pCurCoor->dbMinValue;

		// �p��C�@���u��m: for grids above 0
		nPriLineCount_A0 = nSecLineCount_A0 = 0;
		if (pCurCoor->dbMaxValue > 0){
			// allocate memory... use upper bound
			nPriLineCount_A0 = (int)(ceil(pCurCoor->dbMaxValue / pCurCoor->dbPriLineInt));
			if (nPriLineCount_A0 > 0)
				pnPriPos_A0 = new int[nPriLineCount_A0];
			nSecLineCount_A0 = (nPriLineCount_A0 + 1) * (pCurCoor->nSecLineInt - 1);
			if (nSecLineCount_A0 > 0)
				pnSecPos_A0 = new int[nSecLineCount_A0];
			// calculation
			nTemp = nTemp2 = 0;
			for (i=0; i<nPriLineCount_A0; i++){
				// primary line
				dbTemp = pCurCoor->dbPriLineInt * (i+1); // x
				if ((dbTemp < pCurCoor->dbMaxValue) && 
					(dbTemp > pCurCoor->dbMinValue)){
					dbTemp = (dbTemp - pCurCoor->dbMinValue) / dbCDRange;
					dbTemp = dbVTMin + dbVTRange * dbTemp; // x'
					dbTemp = (dbTemp - dbVTDisplayFrom) / dbVTDisplayRange;
					pnPriPos_A0[nTemp++] = (int)(nATDisplayFrom + nATDisplayRange * dbTemp); // x"
				}
				// secondary line
				for (j=1; j<pCurCoor->nSecLineInt; j++){
					dbTemp = pCurCoor->dbPriLineInt * i + dbSecLineInt * j; // x
					if ((dbTemp < pCurCoor->dbMaxValue) &&
						(dbTemp > pCurCoor->dbMinValue)){
						dbTemp = (dbTemp - pCurCoor->dbMinValue) / dbCDRange;
						dbTemp = dbVTMin + dbVTRange * dbTemp; // x'
						dbTemp = (dbTemp - dbVTDisplayFrom) / dbVTDisplayRange;
						pnSecPos_A0[nTemp2++] = (int)(nATDisplayFrom + nATDisplayRange * dbTemp); // x"
					}
				}
			}
			nPriLineCount_A0 = nTemp;
			nSecLineCount_A0 = nTemp2;
		}

		// �p��C�@���u��m: for grids under 0
		nPriLineCount_U0 = nSecLineCount_U0 = 0;
		if (pCurCoor->dbMinValue < 0){
			// allocate memory
			nPriLineCount_U0 = (int)(ceil(fabs(pCurCoor->dbMinValue / pCurCoor->dbPriLineInt)));
			if (nPriLineCount_U0 > 0)
				pnPriPos_U0 = new int[nPriLineCount_U0];
			nSecLineCount_U0 = (int)((nPriLineCount_U0 + 1) * pCurCoor->nSecLineInt);
			if (nSecLineCount_U0 > 0)
				pnSecPos_U0 = new int[nSecLineCount_U0];
			// calculation
			nTemp = nTemp2 = 0;
			for (i=0; i<nPriLineCount_U0; i++){
				// primary line
				dbTemp = -1.0 * (i+1) * pCurCoor->dbPriLineInt; // x
				if ((dbTemp < pCurCoor->dbMaxValue) &&
					(dbTemp > pCurCoor->dbMinValue)){
					dbTemp = (dbTemp - pCurCoor->dbMinValue) / dbCDRange;
					dbTemp = dbVTMin + dbVTRange * dbTemp; // x'
					dbTemp = (dbTemp - dbVTDisplayFrom) / dbVTDisplayRange;
					pnPriPos_U0[nTemp++] = (int)(nATDisplayFrom + nATDisplayRange * dbTemp); // x"
				}
				// secondary line
				for (j=1; j<pCurCoor->nSecLineInt; j++){
					dbTemp = -1.0 * (i * pCurCoor->dbPriLineInt + j * dbSecLineInt); // x
					if ((dbTemp < pCurCoor->dbMaxValue) &&
						(dbTemp > pCurCoor->dbMinValue)){
						dbTemp = (dbTemp - pCurCoor->dbMinValue) / dbCDRange;
						dbTemp = dbVTMin + dbVTRange * dbTemp; // x'
						dbTemp = (dbTemp - dbVTDisplayFrom) / dbVTDisplayRange;
						pnSecPos_U0[nTemp2++] = (int)(nATDisplayFrom + nATDisplayRange * dbTemp); // x"
					}
				}
			}
			nPriLineCount_U0 = nTemp;
			nSecLineCount_U0 = nTemp2;
		}

		// �p�� 0 �������y�Ц�m
		if ((pCurCoor->dbMaxValue < 0) || (pCurCoor->dbMinValue > 0))
			nZeroPos = -1;
		else{
			dbTemp = (0.0 - pCurCoor->dbMinValue) / dbCDRange;
			dbTemp = dbVTMin + dbVTRange * dbTemp; // x'
			dbTemp = (dbTemp - dbVTDisplayFrom) / dbVTDisplayRange;
			nZeroPos = (int)(nATDisplayFrom + nATDisplayRange * dbTemp); // x"
		}

		// �]�w��m
		switch(pCurCoor->nPosType){
		case 0: // �b�u�e�b���I��m
			nCoorX = nZeroPos;				// �b�u��m
			nLableX = nZeroPos + 1;			// ��r��m
			nCoorY = nZeroPos;				// �b�u��m
			nLableY = nZeroPos + 1;			// ��r��m
			break;
		case 1: // �b�u�e�b�k/�U��
			nCoorX = cWaveRect.right;		// �b�u��m
			nLableX = cWaveRect.right + 3;	// ��r��m
			nCoorY = cWaveRect.bottom;		// �b�u��m
			nLableY = cWaveRect.bottom + 3;	// ��r��m
			break;
		case 2: // �b�u�e�b��/�W��
			nCoorX = cWaveRect.left;		// �b�u��m
			nLableX = cViewerRect.left + 3;	// ��r��m
			nCoorY = cWaveRect.top;			// �b�u��m
			nLableY = cViewerRect.top + 3;	// ��r��m
			break;
		}

		// �Ʈ�u
		if (pCurCoor->nDisplayFlag & SHOW_SEC_LINE){
			cCurPen.CreatePen(	PS_SOLID,
								pCurCoor->SecLineInfo.nWidth,
								pCurCoor->SecLineInfo.clrColor);
			pOldPen = (CPen*)pDC->SelectObject(&cCurPen);
			if (bIsX){
				// �e X �b
				for (i=0; i<nSecLineCount_A0; i++){
					if (pnSecPos_A0[i] <= cWaveRect.left) continue;
					if (pnSecPos_A0[i] >= cWaveRect.right) continue;
					pDC->MoveTo(pnSecPos_A0[i], cWaveRect.top+1);
					pDC->LineTo(pnSecPos_A0[i], cWaveRect.bottom-1);
				}
				for (i=0; i<nSecLineCount_U0; i++){
					if (pnSecPos_U0[i] <= cWaveRect.left) continue;
					if (pnSecPos_U0[i] >= cWaveRect.right) continue;
					pDC->MoveTo(pnSecPos_U0[i], cWaveRect.top+1);
					pDC->LineTo(pnSecPos_U0[i], cWaveRect.bottom-1);
				}
			}else{
				// �e Y �b
				for (i=0; i<nSecLineCount_A0; i++){
					nTemp = cWaveRect.top + nATDisplayRange - pnSecPos_A0[i];
					if (nTemp <= cWaveRect.top) continue;
					if (nTemp >= cWaveRect.bottom) continue;
					pDC->MoveTo(cWaveRect.left+1,  nTemp);
					pDC->LineTo(cWaveRect.right-1, nTemp);
				}
				for (i=0; i<nSecLineCount_U0; i++){
					nTemp = cWaveRect.top + nATDisplayRange - pnSecPos_U0[i];
					if (nTemp <= cWaveRect.top) continue;
					if (nTemp >= cWaveRect.bottom) continue;
					pDC->MoveTo(cWaveRect.left+1,  nTemp);
					pDC->LineTo(cWaveRect.right-1, nTemp);
				}
			}
			pDC->SelectObject(pOldPen);
			cCurPen.DeleteObject();
		}

		// �D��u
		cCurPen.CreatePen(	PS_SOLID,
							pCurCoor->PriLineInfo.nWidth,
							pCurCoor->PriLineInfo.clrColor);
		pOldPen = (CPen*)pDC->SelectObject(&cCurPen);
		if (pCurCoor->nDisplayFlag & SHOW_PRI_LINE){
			if (bIsX){
				// �e X �b
				for (i=0; i<nPriLineCount_A0; i++){
					if (pnPriPos_A0[i] <= cWaveRect.left) continue;
					if (pnPriPos_A0[i] >= cWaveRect.right) continue;
					pDC->MoveTo(pnPriPos_A0[i], cWaveRect.top+1);
					pDC->LineTo(pnPriPos_A0[i], cWaveRect.bottom-1);
				}
				for (i=0; i<nPriLineCount_U0; i++){
					if (pnPriPos_U0[i] <= cWaveRect.left) continue;
					if (pnPriPos_U0[i] >= cWaveRect.right) continue;
					pDC->MoveTo(pnPriPos_U0[i], cWaveRect.top+1);
					pDC->LineTo(pnPriPos_U0[i], cWaveRect.bottom-1);
				}
			}else{
				// �e Y �b
				for (i=0; i<nPriLineCount_A0; i++){
					nTemp = cWaveRect.top + nATDisplayRange - pnPriPos_A0[i];
					if (nTemp <= cWaveRect.top) continue;
					if (nTemp >= cWaveRect.bottom) continue;
					pDC->MoveTo(cWaveRect.left+1,  nTemp);
					pDC->LineTo(cWaveRect.right-1, nTemp);
				}
				for (i=0; i<nPriLineCount_U0; i++){
					nTemp = cWaveRect.top + nATDisplayRange - pnPriPos_U0[i];
					if (nTemp <= cWaveRect.top) continue;
					if (nTemp >= cWaveRect.bottom) continue;
					pDC->MoveTo(cWaveRect.left+1,  nTemp);
					pDC->LineTo(cWaveRect.right-1, nTemp);
				}
			}
		}
		pDC->SelectObject(pOldPen);
		cCurPen.DeleteObject();

		// �b�u
		cCurPen.CreatePen(	PS_SOLID,
							pCurCoor->PriLineInfo.nWidth,
							ViewerInfo.OutlColor);
		pOldPen = (CPen*)pDC->SelectObject(&cCurPen);
		if (bIsX){
			// �e X �b
			pDC->MoveTo(cWaveRect.left, nCoorY);
			pDC->LineTo(cWaveRect.right, nCoorY);
			// �e x=0 �b
			pDC->MoveTo(nZeroPos, cWaveRect.top);
			pDC->LineTo(nZeroPos, cWaveRect.bottom);
		}else{
			// �e Y �b
			pDC->MoveTo(nCoorX, cWaveRect.top);
			pDC->LineTo(nCoorX, cWaveRect.bottom);
			// �e y=0 �b
			pDC->MoveTo(cWaveRect.left, cWaveRect.top + cWaveRect.Width() - nZeroPos);
			pDC->LineTo(cWaveRect.right, cWaveRect.top + cWaveRect.Width() - nZeroPos);
		}
		pDC->SelectObject(pOldPen);
		cCurPen.DeleteObject();

		// �D����
		CString csTemp;
		if (pCurCoor->nDisplayFlag & SHOW_PRI_LABEL){
			cCurFont.CreateFontIndirect(&(pCurCoor->lgPriLogFont));
			pOldFont = (CFont*)pDC->SelectObject(&cCurFont);
			pDC->SetTextColor(pCurCoor->PriLineInfo.clrColor);
			if (bIsX){
				// �e X �b
				if (pCurCoor->nDisplayFlag & SHOW_IN_SAM){
					csTemp = "0";
					nTemp = nZeroPos - (pDC->GetTextExtent(csTemp)).cx / 2;
					if ((nTemp > cViewerRect.left) &&
						(nTemp < cViewerRect.right))
						pDC->TextOut(nTemp, nLableY, csTemp);
				}else{
					csTemp = "00:00:00";
					nTemp = nZeroPos - (pDC->GetTextExtent(csTemp)).cx / 2;
					if ((nTemp > cViewerRect.left) &&
						(nTemp < cViewerRect.right))
						pDC->TextOut(nTemp, nLableY, csTemp);
				}
				for (i=0; i<nPriLineCount_A0; i++){
					if (pCurCoor->nDisplayFlag & SHOW_IN_SAM){
						j = pCurCoor->nShowLableID;
						dbTemp = pCurCoor->dbPriLineInt * (1+i) * ViewerInfo.pWaveInfo[j].dbSampleRate;
						csTemp.Format("%.0f", dbTemp);
						nTemp = pnPriPos_A0[i] - (pDC->GetTextExtent(csTemp)).cx / 2;
						if ((nTemp > cViewerRect.left) &&
							(nTemp < cViewerRect.right))
							pDC->TextOut(nTemp, nLableY, csTemp);
					}else{
						dbTemp = pCurCoor->dbPriLineInt * (1+i);
						csTemp.Format("%02d:%02d:%05.2f", (int)(dbTemp/3600000), (int)(dbTemp/60000), dbTemp/1000);
						nTemp = pnPriPos_A0[i] - (pDC->GetTextExtent(csTemp)).cx / 2;
						if ((nTemp > cViewerRect.left) &&
							(nTemp < cViewerRect.right))
							pDC->TextOut(nTemp, nLableY, csTemp);
					}
				}
				for (i=0; i<nPriLineCount_U0; i++){
					if (pCurCoor->nDisplayFlag & SHOW_IN_SAM){
						// ��� sample ��, �Ҧ��s����o�Ӯy�жb���i�γ����
						j = pCurCoor->nShowLableID;
						dbTemp = pCurCoor->dbPriLineInt * (1+i) * ViewerInfo.pWaveInfo[j].dbSampleRate;
						csTemp.Format("%.0f", dbTemp);
						nTemp = pnPriPos_U0[i] - (pDC->GetTextExtent(csTemp)).cx / 2;
						if ((nTemp > cViewerRect.left) &&
							(nTemp < cViewerRect.right))
							pDC->TextOut(nTemp, nLableY, csTemp);
					}else{
						dbTemp = pCurCoor->dbPriLineInt * (1+i) * -1.0;
						csTemp.Format("%02d:%02d:%05.2f", (int)(dbTemp/3600000), (int)(dbTemp/60000), dbTemp/1000);
						nTemp = pnPriPos_U0[i] - pDC->GetTextExtent(csTemp).cx / 2;
						if ((nTemp > cViewerRect.left) &&
							(nTemp < cViewerRect.right))
							pDC->TextOut(nTemp, nLableY, csTemp);
					}
				}
			}else{
				// �e Y �b
				csTemp = "0.0000";
				nTemp = nZeroPos + pDC->GetTextExtent(csTemp).cy / 2;
				nTemp = cWaveRect.top + nATDisplayRange - nTemp;
				if (nTemp < cViewerRect.top) break;
				if (nTemp > cViewerRect.bottom) break;
				pDC->TextOut(nLableX, nTemp, csTemp);
				for (i=0; i<nPriLineCount_A0; i++){
					dbTemp = pCurCoor->dbPriLineInt * (1+i);
					csTemp.Format("%.4f", dbTemp);
					nTemp = pnPriPos_A0[i] + pDC->GetTextExtent(csTemp).cy / 2;
					nTemp = cWaveRect.top + nATDisplayRange - nTemp;
					if ((nTemp > cViewerRect.top) &&
						(nTemp < cViewerRect.bottom))
						pDC->TextOut(nLableX, nTemp, csTemp);
				}
				for (i=0; i<nPriLineCount_U0; i++){
					dbTemp = pCurCoor->dbPriLineInt * (1+i) * -1.0;
					csTemp.Format("%.4f", dbTemp);
					nTemp = pnPriPos_U0[i] + pDC->GetTextExtent(csTemp).cy / 2;
					nTemp = cWaveRect.top + nATDisplayRange - nTemp;
					if ((nTemp > cViewerRect.top) &&
						(nTemp < cViewerRect.bottom))
						pDC->TextOut(nLableX, nTemp, csTemp);
				}
			}
			pDC->SelectObject(pOldFont);
			cCurFont.DeleteObject();
		}

		// �Ƽ���
		if (pCurCoor->nDisplayFlag & SHOW_SEC_LABEL){
			// �ثe�����e secondary lables....
		}

		// delete
		delete [] pnPriPos_U0;
		delete [] pnPriPos_A0;
		delete [] pnSecPos_U0;
		delete [] pnSecPos_A0;
	}
}

VOID CWaveViewer::OnSize(UINT nType, int cx, int cy) 
{
	CStatic::OnSize(nType, cx, cy);

	// get paint rect
	SetWaveRect();
}

VOID CWaveViewer::PreSubclassWindow() 
{
	// get paint rect
	SetWaveRect();

	// to make this object notify to the mouse action !!!
	ModifyStyle(0,SS_NOTIFY);
	
	CStatic::PreSubclassWindow();
}

VOID CWaveViewer::OnLButtonDown(UINT nFlags, CPoint point) 
{
	cStartPoint = point;
	bIsLPPress = TRUE;
	SetCapture();
	double dbTemp = (double)(point.x - cWaveRect.left) / cWaveRect.Width();
	if (nMouseType == 7){
		dbSelectingStart = dbVTXDisplayFrom + dbTemp * (dbVTXDisplayTo - dbVTXDisplayFrom);
		dbSelectingEnd = dbSelectingStart;
	}
	if (nMouseType == 8){
		double dbMark = dbVTXDisplayFrom + dbTemp * (dbVTXDisplayTo - dbVTXDisplayFrom);
		cMarkLineArray.Add(dbMark);
	}
	CStatic::OnLButtonDown(nFlags, point);
}

VOID CWaveViewer::OnLButtonUp(UINT nFlags, CPoint point) 
{
	bIsLPPress = FALSE;
	ReleaseCapture();
	if (nMouseType == 7){
		SELECTION_INFO sif;
		sif.dbStart	= dbSelectingStart;
		double dbTemp = (double)(point.x - cWaveRect.left) / cWaveRect.Width();
		sif.dbEnd	= dbVTXDisplayFrom + dbTemp * (dbVTXDisplayTo - dbVTXDisplayFrom);
		if (ViewerInfo.nDisplayFlag & SINGLE_SELECTION) cSelectionArray.RemoveAll();
		cSelectionArray.Add(sif);
		dbSelectingStart = dbSelectingEnd = -1;
		Invalidate(TRUE);
	}
	CStatic::OnLButtonUp(nFlags, point);
}

VOID CWaveViewer::OnRButtonUp(UINT nFlags, CPoint point) 
{
	// Show Copyright Dialog
	CString s = "VC++ Class for ²���i����ܾ�\n\n";
	s += "��l�@�̡GBala\n��������G2003-2-1\n";
	s += "�ק����G2004-12-10 �[�J�ƹ�����\��\n\n";
	s += "�ק��01�GCCL\n�ק����G2004-11-29 �}�l�ѻP���դέק�\n";
	s += "�ק����G2004-12-27 Fix Bug SetViewBoundary\n";
	s += "�ק����G2005-06-15 Modify SetWaveData �b \"BYTE*\" �� \"short*\" �Ҧ�\n";
	s += "�@�@�@�@�@2005-06-15 �ץ� AddSelection �� AddMarkLine ����ɼ�b�w���ʰ��D\n\n";
	s += "�ק��02�GBala -- ²���i����ܾ� V2.0 ���}�o\n";
	s += "�ק����G2006-03-08 ���� V2.0 ���}�o \n\n";
	s += "*** �w��ק�å[�J�ק��ñ�W ***";
	MessageBox(s);
	CStatic::OnRButtonUp(nFlags, point);
}

VOID CWaveViewer::OnMouseMove(UINT nFlags, CPoint point) 
{
	if (bIsLPPress)
	{
		double dbDisX, dbDisY, dbTemp;
		CPoint pntStart, pntEnd;
		switch(nMouseType){
		case 0:	// nothing happen... (�S��)
			break;
		case 1:	// move viewport at H-direction (���k����)
			dbDisX = (double)(point.x - cStartPoint.x);
			dbDisX *= (dbVTXDisplayTo - dbVTXDisplayFrom) / cWaveRect.Width();
			if (dbDisX == 0.0) break;

			if (dbDisX > 0.0){	// ���k����
				if(dbVTXDisplayFrom >= dbDisX){
					dbVTXDisplayFrom -= dbDisX;
					dbVTXDisplayTo -= dbDisX;
				}else{
					dbVTXDisplayTo -= dbVTXDisplayFrom;
					dbVTXDisplayFrom = 0.0;
				}
			}else{				// ��������
				dbDisX = 0.0 - dbDisX;
				if((dbVTXDisplayTo + dbDisX) <= 1.0){
					dbVTXDisplayTo += dbDisX;
					dbVTXDisplayFrom += dbDisX;
				}else{
					dbVTXDisplayFrom += (1.0 - dbVTXDisplayTo);
					dbVTXDisplayTo = 1.0;
				}
			}

			Invalidate(TRUE);
			break;
		case 2:	// move viewport at V-direction (�W�U����)
			dbDisY = (double)(cStartPoint.y - point.y);
			dbDisY *= (dbVTYDisplayTo - dbVTYDisplayFrom) / cWaveRect.Height();
			if (dbDisY == 0.0) break;
			dbVTYDisplayFrom -= dbDisY;
			dbVTYDisplayTo -= dbDisY;
			if (dbVTYDisplayFrom < -1)
			{
				dbVTYDisplayTo += -1 - dbVTYDisplayFrom;
				dbVTYDisplayFrom = -1;
			}
			if (dbVTYDisplayTo > 1)
			{
				dbVTYDisplayFrom -= dbVTYDisplayTo - 1;
				dbVTYDisplayTo = 1;
			}
			Invalidate(TRUE);
			break;
		case 3:	// move viewport arbitrary (���N����)
			dbDisX = (double)(point.x - cStartPoint.x);
			dbDisX *= (dbVTXDisplayTo - dbVTXDisplayFrom) / (double)cWaveRect.Width();
			dbDisY = (double)(cStartPoint.y - point.y);
			dbDisY *= (dbVTYDisplayTo - dbVTYDisplayFrom) / (double)cWaveRect.Height();
			if (dbDisX == 0.0) break;
			if (dbDisY == 0.0) break;
			dbVTYDisplayFrom -= dbDisY;
			dbVTYDisplayTo -= dbDisY;

			if (dbDisX > 0.0){	// ���k����
				if(dbVTXDisplayFrom >= dbDisX){
					dbVTXDisplayFrom -= dbDisX;
					dbVTXDisplayTo -= dbDisX;
				}else{
					dbVTXDisplayTo -= dbVTXDisplayFrom;
					dbVTXDisplayFrom = 0.0;
				}
			}else{				// ��������
				dbDisX = 0-dbDisX;
				if((dbVTXDisplayTo + dbDisX) <= 1.0){
					dbVTXDisplayTo += dbDisX;
					dbVTXDisplayFrom += dbDisX;
				}else{
					dbVTXDisplayFrom += (1.0 - dbVTXDisplayTo);
					dbVTXDisplayTo = 1.0;
				}
			}
			
			if (dbVTYDisplayFrom < -1)
			{
				dbVTYDisplayTo += -1 - dbVTYDisplayFrom;
				dbVTYDisplayFrom = -1;
			}
			if (dbVTYDisplayTo > 1)
			{
				dbVTYDisplayFrom -= dbVTYDisplayTo - 1;
				dbVTYDisplayTo = 1;
			}
			Invalidate(TRUE);
			break;
		case 4:	// scale viewport at H-direction (���k�Y��)
			dbDisX = (double)(point.x - cStartPoint.x);
			dbDisX *= (dbVTXDisplayTo - dbVTXDisplayFrom) / cWaveRect.Width();
			dbDisX /= 2.0;
			if (dbDisX == 0.0) break;
			if (dbDisX > 0.0)
			{
				if (dbDisX*2 < (dbVTXDisplayTo - dbVTXDisplayFrom))
				{
					dbVTXDisplayFrom += dbDisX;
					dbVTXDisplayTo -= dbDisX;
				}
			}else{
				if (-dbDisX > dbVTXDisplayFrom)
					dbVTXDisplayFrom = 0.0;
				else
					dbVTXDisplayFrom -= (0.0-dbDisX);
				
				if (-dbDisX > (1.0-dbVTXDisplayTo))
					dbVTXDisplayTo = 1.0;
				else
					dbVTXDisplayTo += (0.0 - dbDisX);
			}
			Invalidate(TRUE);
			break;
		case 5:	// scale viewport at V-direction (�W�U�Y��)
			dbDisY = (double)(cStartPoint.y - point.y);
			dbDisY *= (dbVTYDisplayTo - dbVTYDisplayFrom) / cWaveRect.Height();
			dbDisY /= 2.0;
			if (dbDisY == 0.0) break;
			if (dbDisY > 0.0)
			{
				if (2*dbDisY < (dbVTYDisplayTo-dbVTYDisplayFrom))
				{
					dbVTYDisplayFrom += dbDisY;
					dbVTYDisplayTo -= dbDisY;
				}
			}else{
				if (-dbDisY > 1-dbVTYDisplayTo)
					dbVTYDisplayTo = +1.0;
				else
					dbVTYDisplayTo -= dbDisY;
				
				if (-dbDisY > dbVTYDisplayFrom+1)
					dbVTYDisplayFrom = -1.0;
				else
					dbVTYDisplayFrom += dbDisY;
			}
			Invalidate(TRUE);
			break;
		case 6:	// scale viewport arbitrary (���N�Y��)
			dbDisX = (double)(point.x - cStartPoint.x);
			dbDisX *= (dbVTXDisplayTo - dbVTXDisplayFrom) / cWaveRect.Width();	// �q�e����m�y���ഫ���ڨ��ˮy��
			dbDisX /= 2.0;
			dbDisY = (double)(cStartPoint.y - point.y);
			dbDisY *= (dbVTYDisplayTo - dbVTYDisplayFrom) / cWaveRect.Height();
			dbDisY /= 2.0;
			if (dbDisX == 0.0) break;
			if (dbDisY == 0.0) break;
			if (dbDisX > 0.0)
			{
				if (dbDisX*2.0 < (dbVTXDisplayTo - dbVTXDisplayFrom))
				{
					dbVTXDisplayFrom += dbDisX;
					dbVTXDisplayTo -= dbDisX;
				}
			}else{
				if (-dbDisX > dbVTXDisplayFrom)
					dbVTXDisplayFrom = 0.0;
				else
					dbVTXDisplayFrom -= (0.0-dbDisX);
				
				if (-dbDisX > ((double)1.0-dbVTXDisplayTo))
					dbVTXDisplayTo = 1.0;
				else
					dbVTXDisplayTo += (0.0-dbDisX);
			}

			if (dbDisY > 0.0)
			{
				if (dbDisY*2.0 < (dbVTYDisplayTo-dbVTYDisplayFrom))
				{
					dbVTYDisplayFrom += dbDisY;
					dbVTYDisplayTo -= dbDisY;
				}
			}else{
				if (-dbDisY > 1.0-dbVTYDisplayTo)
					dbVTYDisplayTo = +1.0;
				else
					dbVTYDisplayTo -= dbDisY;
				
				if (-dbDisY > dbVTYDisplayFrom+1.0)
					dbVTYDisplayFrom = -1.0;
				else
					dbVTYDisplayFrom += dbDisY;
			}
			Invalidate(TRUE);
			break;
		case 7:	// �إ߿�ܬq
			dbTemp = (double)(point.x - cWaveRect.left) / cWaveRect.Width();
			dbSelectingEnd = dbVTXDisplayFrom + dbTemp * (dbVTXDisplayTo - dbVTXDisplayFrom);
			Invalidate(TRUE);
			break;
		case 8:	// �[�J�аO�u�A�]���S���ݭn�b�ƹ����ʮɶi��B�z�A�ҥH�N���� Break; 20050615 CCL Added for AddMarkLine
			break;
		default:
			nMouseType = 0;	// ���F�e���A�ҥH�����N�ƹ��]�����@�� 20050615 CCL Added for sercurity
			break;
		}
		if (nMouseType != 7) cStartPoint = point;
	}

	CStatic::OnMouseMove(nFlags, point);
}

//////////////////////////////////////
// Main interface function

// Ū Wave ��, �p�G�S���S�O���w, �N��o�� wave ������Ӫ��y�жb�W
BOOL CWaveViewer::ReadWaveFile(CString csFilename, UINT nAppX/*=NO_ID*/, UINT nAppY/*=NO_ID*/)
{
	HMMIO hmmio;
	MMCKINFO ckRIFF,ckInfo;
	PCMWAVEFORMAT pcm;

	// open eave file
	hmmio=mmioOpen(csFilename.GetBuffer(csFilename.GetLength()), NULL, MMIO_READ);
	csFilename.ReleaseBuffer();

	// check wave format
	if(hmmio==NULL) return FALSE;
	if(mmioDescend(hmmio,&ckRIFF,NULL,0)){
		mmioClose(hmmio,0);
		return FALSE;
	}
	if(ckRIFF.ckid!=FOURCC_RIFF || (ckRIFF.fccType!=mmioFOURCC('W','A','V','E'))){
		mmioClose(hmmio,0);
		return FALSE;
	}
	
	// find "fmt" chunk
	ckInfo.ckid=mmioFOURCC('f','m','t',' ');
	if(!mmioDescend(hmmio,&ckInfo,&ckRIFF,MMIO_FINDCHUNK)){
		if(ckInfo.cksize>=(long)sizeof(PCMWAVEFORMAT)){
			if(mmioRead(hmmio,(LPSTR)&pcm,sizeof(PCMWAVEFORMAT))==sizeof(PCMWAVEFORMAT)){
				if(mmioAscend(hmmio,&ckInfo,0)){
					mmioClose(hmmio,0);
					return FALSE;
				}
			}else{
				mmioClose(hmmio,0);
				return FALSE;
			}
		}else{
			mmioClose(hmmio,0);
			return FALSE;
		}
	}else{
		mmioClose(hmmio,0);
		return FALSE;
	}

	// find "data" chunk and read date
	DWORD m_dwDataLength;
	BYTE *m_pData;
	ckInfo.ckid=mmioFOURCC('d','a','t','a');
	if(!mmioDescend(hmmio,&ckInfo,&ckRIFF,MMIO_FINDCHUNK)){
		m_dwDataLength = ckInfo.cksize;
		m_pData = new BYTE[ckInfo.cksize];
		if(mmioRead(hmmio,(LPSTR)m_pData,m_dwDataLength) != (long)m_dwDataLength){
			mmioClose(hmmio,0);
			return FALSE;
		}
	}else{
		mmioClose(hmmio,0);
		return FALSE;
	}
	mmioClose(hmmio,0);

	// Read over, set to the double array
	if(pcm.wBitsPerSample == 8){
		SetWaveData(m_pData, m_dwDataLength, pcm.wf.nSamplesPerSec, nAppX, nAppY);
	}else if(pcm.wBitsPerSample == 16){
		short *psData = (short*)m_pData;
		SetWaveData(psData, m_dwDataLength/2, pcm.wf.nSamplesPerSec, nAppX, nAppY);
		psData = NULL;
	}
	delete [] m_pData;

	//m_WaveFormat = pcm;

	return TRUE;
}

// ��J 8 bits ���
BOOL CWaveViewer::SetWaveData(BYTE *m_pData, DWORD dwLen, double dbSamRate, UINT nAppX/*=NO_ID*/, UINT nAppY/*=NO_ID*/)
{
	// dwLen ���i�� sample ��
	if (dwLen < 0) return FALSE;

	// copy and transfer wave data
	// and normalize all the data into (-1, +1)
	double *pNorWaveData = new double[dwLen];
	ZeroMemory(pNorWaveData, dwLen * sizeof(double));
	if (m_pData){for (DWORD i=0; i<dwLen; i++) pNorWaveData[i] = ((double)m_pData[i]-128)/128;}

	// add wave data
	BOOL bReturn = SetNormalWaveData(pNorWaveData, dwLen, dbSamRate, nAppX, nAppY);
	delete [] pNorWaveData;

	return bReturn;
}

// ��J 16 bits ���
BOOL CWaveViewer::SetWaveData(short *m_pData, DWORD dwLen, double dbSamRate, UINT nAppX/*=NO_ID*/, UINT nAppY/*=NO_ID*/)
{
	// dwLen ���i�� sample ��
	if (dwLen < 0) return FALSE;

	// copy and transfer wave data
	// and normalize all the data into (-1, +1)
	double *pNorWaveData = new double[dwLen];
	ZeroMemory(pNorWaveData, dwLen * sizeof(double));
	if (m_pData) {for (DWORD i=0; i<dwLen; i++) pNorWaveData[i] = (double)m_pData[i]/SHRT_MAX;}

	// add wave data
	BOOL bReturn = SetNormalWaveData(pNorWaveData, dwLen, dbSamRate, nAppX, nAppY);
	delete [] pNorWaveData;

	return bReturn;
}

// ��J double ���
BOOL CWaveViewer::SetWaveData(double *m_pData, DWORD dwLen, double dbSamRate, UINT nAppX/*=NO_ID*/, UINT nAppY/*=NO_ID*/)
{
	// dwLen ���i�� sample ��
	if (dwLen < 0) return FALSE;

	// copy and transfer wave data
	// and normalize all the data into (-1, +1)
	double *pNorWaveData = new double[dwLen];
	ZeroMemory(pNorWaveData, dwLen * sizeof(double));
	if (m_pData)
	{
		// normalize all data to (-1 ~ 1)
		DWORD i;
		double dbMax = 0;
		for (i=0; i<dwLen; ++i) {if (fabs(m_pData[i]) > dbMax) dbMax = fabs(m_pData[i]);}
		dbMax *= 1.001;	// ���F���̤j�Ȥ]�@�w���b(1,-1)����
		for (i=0; i<dwLen; ++i) pNorWaveData[i] = m_pData[i] / dbMax;
	}

	// add wave data
	BOOL bReturn = SetNormalWaveData(pNorWaveData, dwLen, dbSamRate, nAppX, nAppY);
	delete [] pNorWaveData;

	return bReturn;
}

// ��J double ���
BOOL CWaveViewer::SetWaveData2(double *m_pData, DWORD dwLen, double dbSamRate, UINT nAppX/*=NO_ID*/, UINT nAppY/*=NO_ID*/)
{
	// dwLen ���i�� sample ��
	if (dwLen < 0) return FALSE;

	// copy and transfer wave data
	// and normalize all the data into (-1, +1)
	double *pNorWaveData = new double[dwLen];
	ZeroMemory(pNorWaveData, dwLen * sizeof(double));
	if (m_pData)
	{
		// normalize all data to (-1 ~ 1)
		DWORD i;
		theMax *= 1.001;	// ���F���̤j�Ȥ]�@�w���b(1,-1)����
		for (i=0; i<dwLen; ++i) pNorWaveData[i] = m_pData[i] / theMax;
	}

	// add wave data
	BOOL bReturn = SetNormalWaveData(pNorWaveData, dwLen, dbSamRate, nAppX, nAppY);
	delete [] pNorWaveData;

	return bReturn;
}

BOOL CWaveViewer::FindtheMax(double *m_pData, DWORD dwLen)
{
	// dwLen ���i�� sample ��
	if (dwLen < 0) return FALSE;

	if (m_pData)
	{
		DWORD i;
		for (i=0; i<dwLen; ++i) {if (fabs(m_pData[i]) > theMax) theMax = fabs(m_pData[i]);}
	}
	return TRUE;
}

// �u�ǤJ�w normalized �����, �����ϥΪ̩I�s
BOOL CWaveViewer::SetNormalWaveData(double *m_pData, DWORD dwLen, double dbSamRate, UINT nAppX, UINT nAppY)
{
	// dwLen ���i�� sample ��
	if (dwLen < 0) return FALSE;

	// setup parameters and prepare memory
	int nCurWaveID = 0;
	if (ViewerInfo.nWaveCount == 0){
		// �ثe�S������i��, ���s�W�@��
		nCurWaveID = 0;
		ViewerInfo.pWaveInfo = new WAVE_INFO[1];
		ViewerInfo.nWaveCount = 1;
	}else{
		// �ثe�w���i��, �[�J�@��
		nCurWaveID = ViewerInfo.nWaveCount;
		WAVE_INFO *pTemp = ViewerInfo.pWaveInfo;
		ViewerInfo.pWaveInfo = new WAVE_INFO[ViewerInfo.nWaveCount + 1];
		for (UINT i=0; i<ViewerInfo.nWaveCount; i++) ViewerInfo.pWaveInfo[i] = pTemp[i];
		delete [] pTemp;
		ViewerInfo.nWaveCount ++;
	}
	ViewerInfo.pWaveInfo[nCurWaveID].nID = nCurWaveID;
	ViewerInfo.pWaveInfo[nCurWaveID].nWaveLength = dwLen;
	ViewerInfo.pWaveInfo[nCurWaveID].dbSampleRate = dbSamRate;
	ViewerInfo.pWaveInfo[nCurWaveID].pdbWaveData = new double[dwLen];
	// 20050618 CCL Added
	ViewerInfo.pWaveInfo[nCurWaveID].m_WaveFormat.wBitsPerSample = 16;
	ViewerInfo.pWaveInfo[nCurWaveID].m_WaveFormat.wf.nBlockAlign = 2;
	ViewerInfo.pWaveInfo[nCurWaveID].m_WaveFormat.wf.nSamplesPerSec = (int)dbSamRate;
	ViewerInfo.pWaveInfo[nCurWaveID].m_WaveFormat.wf.nAvgBytesPerSec = (int)(dbSamRate * 2);

	// copy normalized wave data
	if (m_pData) memcpy(ViewerInfo.pWaveInfo[nCurWaveID].pdbWaveData, m_pData, dwLen*sizeof(double));
	else		 ZeroMemory(ViewerInfo.pWaveInfo[nCurWaveID].pdbWaveData, dwLen * sizeof(double));

	// setup X coordinates
	int nCurXCoorID;
	double dbTemp;
	COOR_INFO *pCoorTemp;
	if (ViewerInfo.nXCoorCount == 0){
		// �ثe�S�� X �b, �s�W�@��
		nCurXCoorID = 0;
		ViewerInfo.pXCoorInfo = new COOR_INFO[1];
		ViewerInfo.pXCoorInfo[0].nID = 0;
		ViewerInfo.pXCoorInfo[0].nShowLableID = nCurWaveID;
		ViewerInfo.nXCoorCount = 1;
	}else{
		// �ثe�w�� X �b, �i��t��
		// ��Ӫ��i�ιw�]����ܼ���
		switch(nAppX){
		case NO_ID:
			// �����w, �N�M�Ĥ@�Ӱt��
			nCurXCoorID = 0;
			break;
		case ADD_NEW:
			// ���w�n�s�W!
			nCurXCoorID = ViewerInfo.nXCoorCount;
			pCoorTemp = ViewerInfo.pXCoorInfo;
			ViewerInfo.pXCoorInfo = new COOR_INFO[nCurXCoorID + 1];
			memcpy(ViewerInfo.pXCoorInfo, pCoorTemp, nCurXCoorID*sizeof(COOR_INFO));
			delete [] pCoorTemp;
			ViewerInfo.pXCoorInfo[nCurXCoorID].nID = nCurXCoorID;
			ViewerInfo.pXCoorInfo[nCurXCoorID].PriLineInfo.bVisible = FALSE;
			ViewerInfo.nXCoorCount ++;
			break;
		default:
			// �p�G���w���t��y�жb���s�b, �۰ʩM�Ĥ@�Ӱt��
			if (nAppX >= ViewerInfo.nXCoorCount) nCurXCoorID = 0;
			else								 nCurXCoorID = nAppX;
			break;
		}
	}
	ViewerInfo.pXCoorInfo[nCurXCoorID].dbPriLineInt = 1000.0;
	dbTemp = ViewerInfo.pXCoorInfo[nCurXCoorID].dbMaxValue; // ���]�̤j��
	ViewerInfo.pXCoorInfo[nCurXCoorID].dbMaxValue = max(dbTemp, 1000.0 * dwLen / dbSamRate);
	dbTemp = ViewerInfo.pXCoorInfo[nCurXCoorID].dbMinValue; // ���]�̤p��
	ViewerInfo.pXCoorInfo[nCurXCoorID].dbMinValue = min(dbTemp, 0);
	ViewerInfo.pWaveInfo[nCurWaveID].nXCorID = nCurXCoorID; // �M�ثe�s�W���t��

	// setup Y coordinates
	int nCurYCoorID;
	if (ViewerInfo.nYCoorCount == 0){
		// �ثe�S�� Y �b, �s�W�@��
		// ��Ӫ��i�ιw�]����ܼ���
		nCurYCoorID = 0;
		ViewerInfo.pYCoorInfo = new COOR_INFO[1];
		ViewerInfo.pYCoorInfo[0].nID = 0;
		ViewerInfo.pYCoorInfo[0].nShowLableID = nCurWaveID;
		ViewerInfo.nYCoorCount = 1;
	}else{
		// �ثe�w�� Y �b, �i��t��
		switch(nAppY){
		case NO_ID:
			// �����w, �N�M�Ĥ@�Ӱt��
			nCurYCoorID = 0;
			break;
		case ADD_NEW:
			// ���w�n�s�W!
			nCurYCoorID = ViewerInfo.nYCoorCount;
			pCoorTemp = ViewerInfo.pYCoorInfo;
			ViewerInfo.pYCoorInfo = new COOR_INFO[nCurYCoorID + 1];
			memcpy(ViewerInfo.pYCoorInfo, pCoorTemp, nCurYCoorID*sizeof(COOR_INFO));
			delete [] pCoorTemp;
			ViewerInfo.pYCoorInfo[nCurYCoorID].nID = nCurYCoorID;
			ViewerInfo.pYCoorInfo[nCurXCoorID].PriLineInfo.bVisible = FALSE;
			ViewerInfo.nYCoorCount ++;
			break;
		default:
			// �p�G���w���t��y�жb���s�b, �۰ʩM�Ĥ@�Ӱt��
			if (nAppY >= ViewerInfo.nYCoorCount) nCurYCoorID = 0;
			else								 nCurYCoorID = nAppY;
			break;
		}
	}
	ViewerInfo.pYCoorInfo[nCurYCoorID].dbPriLineInt = 0.5;
	dbTemp = ViewerInfo.pYCoorInfo[nCurYCoorID].dbMaxValue; // ���]�̤j��
	ViewerInfo.pYCoorInfo[nCurYCoorID].dbMaxValue = max(dbTemp,  1);
	dbTemp = ViewerInfo.pYCoorInfo[nCurYCoorID].dbMinValue; // ���]�̤p��
	ViewerInfo.pYCoorInfo[nCurYCoorID].dbMinValue = min(dbTemp, -1);
	ViewerInfo.pWaveInfo[nCurWaveID].nYCorID = nCurYCoorID; // �M�ثe�s�W���t��

	// reset current viewport, all use normalized size
	dbVTXDisplayFrom = 0;
	dbVTXDisplayTo = 1;
	dbVTYDisplayFrom = -1;
	dbVTYDisplayTo = 1;

	SetWaveRect();
	Invalidate(TRUE);
	return TRUE;
}

VOID CWaveViewer::ResetWaveData()
{
	// reset viewport
	dbVTXDisplayFrom = 0;
	dbVTXDisplayTo = 1;
	dbVTYDisplayFrom = -1;
	dbVTYDisplayTo = 1;

	// reset all selections
	cSelectionArray.RemoveAll();
	
	// reset all mark-lines
	cMarkLineArray.RemoveAll();

	// delete all wave and coordinate data
	for (UINT i=0; i<ViewerInfo.nWaveCount; i++) delete [] ViewerInfo.pWaveInfo[i].pdbWaveData;
//	delete [] ViewerInfo.pWaveInfo;
	delete [] ViewerInfo.pXCoorInfo;
	delete [] ViewerInfo.pYCoorInfo;
	ViewerInfo.nWaveCount = 0;
	ViewerInfo.nXCoorCount = 0;
	ViewerInfo.nYCoorCount = 0;
	ViewerInfo.pWaveInfo = NULL;
	ViewerInfo.pXCoorInfo = NULL;
	ViewerInfo.pYCoorInfo = NULL;

	// reset other parameters
	nMouseType = 0;
	bIsLPPress = FALSE;
//	Invalidate(TRUE);
}

// �ק�y�жb��ܰѼ�
BOOL CWaveViewer::ShowLabel(BYTE nDisplayFlag/*=0*/, BYTE nTarget/*=SET_X_COOR*/, UINT nCoorID/*=0*/)
{
	// nTarget: �]�w�n�ק諸�ؼ�
	// �i�μƭ�: SET_NON_COOR, SET_X_COOR, SET_Y_COOR, SET_XY_COOR
	switch(nTarget){
	case SET_NON_COOR:
		return TRUE;
		break;
	case SET_X_COOR:
		if (nCoorID >= ViewerInfo.nXCoorCount) return FALSE;
		ViewerInfo.pXCoorInfo[nCoorID].nDisplayFlag = nDisplayFlag;
		break;
	case SET_Y_COOR:
		if (nCoorID >= ViewerInfo.nYCoorCount) return FALSE;
		ViewerInfo.pYCoorInfo[nCoorID].nDisplayFlag = nDisplayFlag;
		break;
	case SET_XY_COOR:
		if (nCoorID >= ViewerInfo.nXCoorCount) return FALSE;
		if (nCoorID >= ViewerInfo.nYCoorCount) return FALSE;
		ViewerInfo.pXCoorInfo[nCoorID].nDisplayFlag = nDisplayFlag;
		ViewerInfo.pYCoorInfo[nCoorID].nDisplayFlag = nDisplayFlag;
		break;
	}
	SetWaveRect();
	Invalidate(TRUE);
	return TRUE;
}

VOID CWaveViewer::ShowMarkLine(BYTE nDisplayFlag /*=0*/)
{
	ViewerInfo.nDisplayFlag = nDisplayFlag;
	Invalidate(TRUE);
}

VOID CWaveViewer::ShowSamplePoint(BOOL bShow /*=FALSE*/)
{
	if (bShow)
		ViewerInfo.nDisplayFlag |= SHOW_SAMPLE;
	else
		ViewerInfo.nDisplayFlag &= !SHOW_SAMPLE;

	Invalidate(TRUE);
}

VOID CWaveViewer::ResetViewport()
{
	dbVTXDisplayFrom = 0;
	dbVTXDisplayTo = 1;
	dbVTYDisplayFrom = -1;
	dbVTYDisplayTo = 1;
	Invalidate(TRUE);
}

VOID CWaveViewer::ResetViewportX()
{
	dbVTXDisplayFrom = 0;
	dbVTXDisplayTo = 1;
	Invalidate(TRUE);
}

VOID CWaveViewer::ResetViewportY()
{
	dbVTYDisplayFrom = -1;
	dbVTYDisplayTo = 1;
	Invalidate(TRUE);
}

VOID CWaveViewer::SetColor(COLORREF crColor, BYTE nFlag, UINT nID/*=NO_ID*/)
{
	UINT i;

	// reset color
	if (nFlag & SETCOLOR_OUTL)		ViewerInfo.OutlColor = crColor;
	if (nFlag & SETCOLOR_BKG)		ViewerInfo.BkgdColor = crColor;
	if (nFlag & SETCOLOR_CURSOR)	ViewerInfo.PCurColor = crColor;
	if (nFlag & SETCOLOR_SELECT)	ViewerInfo.SeleColor = crColor;
	if (nFlag & SETCOLOR_MARK)		ViewerInfo.MarkColor = crColor;

	// reset color, �Y�����w�Ϋ��w ID ���s�b, �h�ܧ����
	if (nFlag & SETCOLOR_WAVE){
		if (nID >= ViewerInfo.nWaveCount){
			for (i=0; i<ViewerInfo.nWaveCount; i++)
				ViewerInfo.pWaveInfo[i].PrimaryInfo.clrColor = crColor;
		}else ViewerInfo.pWaveInfo[nID].PrimaryInfo.clrColor = crColor;
	}
	if (nFlag & SETCOLOR_PLINE){
		if (nID >= ViewerInfo.nXCoorCount){
			for (i=0; i<ViewerInfo.nXCoorCount; i++)
				ViewerInfo.pXCoorInfo[i].PriLineInfo.clrColor = crColor;
		}else ViewerInfo.pXCoorInfo[nID].PriLineInfo.clrColor = crColor;
		if (nID >= ViewerInfo.nYCoorCount){
			for (i=0; i<ViewerInfo.nYCoorCount; i++)
				ViewerInfo.pYCoorInfo[i].PriLineInfo.clrColor = crColor;
		}else ViewerInfo.pYCoorInfo[nID].PriLineInfo.clrColor = crColor;
	}
	if (nFlag & SETCOLOR_SLINE){
		if (nID >= ViewerInfo.nXCoorCount){
			for (i=0; i<ViewerInfo.nXCoorCount; i++)
				ViewerInfo.pXCoorInfo[i].SecLineInfo.clrColor = crColor;
		}else ViewerInfo.pXCoorInfo[nID].SecLineInfo.clrColor = crColor;
		if (nID >= ViewerInfo.nYCoorCount){
			for (i=0; i<ViewerInfo.nYCoorCount; i++)
				ViewerInfo.pYCoorInfo[i].SecLineInfo.clrColor = crColor;
		}else ViewerInfo.pYCoorInfo[nID].SecLineInfo.clrColor = crColor;
	}

	Invalidate(TRUE);
}

VOID CWaveViewer::SetWaveLineParameter(COLORREF crColor, int nWidth, BOOL bVisible, BOOL bContinuous, UINT nWaveID/*=NO_ID*/)
{
	//����Y�@�� wave, �Y�����w�Ϋ��w ID ���s�b, �h����Ĥ@��
	if (ViewerInfo.nWaveCount == 0) return;
	if (nWaveID >= ViewerInfo.nWaveCount) nWaveID = 0;

	ViewerInfo.pWaveInfo[nWaveID].PrimaryInfo.nWidth = nWidth;
	ViewerInfo.pWaveInfo[nWaveID].PrimaryInfo.bVisible = bVisible;
	ViewerInfo.pWaveInfo[nWaveID].PrimaryInfo.clrColor = crColor;
	if (bContinuous)
		ViewerInfo.pWaveInfo[nWaveID].nDisplayFlag |= SHOW_CONTINUES;
	else
		ViewerInfo.pWaveInfo[nWaveID].nDisplayFlag &= !SHOW_CONTINUES;
}

VOID CWaveViewer::SetXCoordinateParameter(BYTE nPosType, CFont *pPriLableFont, CFont *pSecLableFont, BOOL bVisible, UINT nCoorID/*=NO_ID*/)
{
	//����Y�@�� coordinate, �Y�����w�Ϋ��w ID ���s�b, �h����Ĥ@��
	if (ViewerInfo.nXCoorCount == 0) return;
	if (nCoorID >= ViewerInfo.nXCoorCount) nCoorID = 0;

	ViewerInfo.pXCoorInfo[nCoorID].PriLineInfo.bVisible = bVisible;
	if (nPosType < 3) ViewerInfo.pXCoorInfo[nCoorID].nPosType = nPosType;
	if (pPriLableFont) pPriLableFont->GetLogFont(&(ViewerInfo.pXCoorInfo[nCoorID].lgPriLogFont));
	if (pSecLableFont) pSecLableFont->GetLogFont(&(ViewerInfo.pXCoorInfo[nCoorID].lgSecLogFont));
	SetWaveRect();
	Invalidate(TRUE);
}

VOID CWaveViewer::SetYCoordinateParameter(BYTE nPosType, CFont *pPriLableFont, CFont *pSecLableFont, BOOL bVisible, UINT nCoorID/*=NO_ID*/)
{
	//����Y�@�� coordinate, �Y�����w�Ϋ��w ID ���s�b, �h����Ĥ@��
	if (ViewerInfo.nYCoorCount == 0) return;
	if (nCoorID >= ViewerInfo.nYCoorCount) nCoorID = 0;

	ViewerInfo.pYCoorInfo[nCoorID].PriLineInfo.bVisible = bVisible;
	if (nPosType < 3) ViewerInfo.pYCoorInfo[nCoorID].nPosType = nPosType;
	if (pPriLableFont) pPriLableFont->GetLogFont(&(ViewerInfo.pYCoorInfo[nCoorID].lgPriLogFont));
	if (pSecLableFont) pSecLableFont->GetLogFont(&(ViewerInfo.pYCoorInfo[nCoorID].lgSecLogFont));
	SetWaveRect();
	Invalidate(TRUE);
}

VOID CWaveViewer::SetXInt(int nPriInt, int nSecInt, UINT nCoorID/*=NO_ID*/)
{
	// �]�w X �b���j,
	// nPriInt ��ܥD�n�����X�j�j
	// nSecInt ��ܨC�@�j�j�A�����X�p�j
	UINT i, nInt;
	double dbMaxDuration, dbDuration;
	if ((nCoorID == NO_ID) || (nCoorID >= ViewerInfo.nXCoorCount)){
		// �����w�]�w���Ӯy�жb, �Ϋ��w���y�жb���s�b, �N�����]���@�˪�
		// �γ̪������� ms �ӳ]
		dbMaxDuration = 0;
		for (i=0; i<ViewerInfo.nXCoorCount; i++){
			dbDuration = ViewerInfo.pXCoorInfo[i].dbMaxValue - ViewerInfo.pXCoorInfo[i].dbMinValue;
			if (dbMaxDuration < dbDuration) dbMaxDuration = dbDuration;
		}
		nInt = (int)(dbMaxDuration / nPriInt); // �C�@�ӥD���j�� ms ��
		for (i=0; i<ViewerInfo.nXCoorCount; i++){
			ViewerInfo.pXCoorInfo[i].dbPriLineInt = nInt;
			ViewerInfo.pXCoorInfo[i].nSecLineInt = nSecInt;
		}
	}else{
		dbDuration = ViewerInfo.pXCoorInfo[nCoorID].dbMaxValue - ViewerInfo.pXCoorInfo[nCoorID].dbMinValue;
		nInt = (int)(dbDuration / nPriInt);
		ViewerInfo.pXCoorInfo[nCoorID].dbPriLineInt = nInt;
		ViewerInfo.pXCoorInfo[nCoorID].nSecLineInt = nSecInt;
	}
	Invalidate(TRUE);
}

VOID CWaveViewer::SetXInt_Samp(int nPriInt, int nSecInt, UINT nCoorID/*=NO_ID*/)
{
	// �]�w X �b���j,
	// nPriInt ��ܥD���j�� sample ��
	// nSecInt ��ܽƶ��j�� sample ��
	UINT i, nInt;
	if ((nCoorID == NO_ID) || (nCoorID >= ViewerInfo.nXCoorCount)){
		// �����w�]�w���Ӯy�жb, �Ϋ��w���y�жb���s�b, �N�����]���@�˪�
		if (ViewerInfo.nWaveCount == 0){
			// �ثe�S������ Wave ���, �w�]�� 8kHz sample rate �]�w
			nInt = nPriInt / 8;
		}else{
			// �βĤ@�Ӫi�Ϊ� sample rate �ӳ]
			nInt = (int)(1000.0 * nPriInt / ViewerInfo.pWaveInfo[0].dbSampleRate);
		}
		for (i=0; i<ViewerInfo.nXCoorCount; i++){
			ViewerInfo.pXCoorInfo[i].dbPriLineInt = nInt;
			ViewerInfo.pXCoorInfo[i].nSecLineInt = nPriInt / nSecInt;
		}
	}else{
		// �n�ΰѦҨ�o�Ӯy�жb���i�Ψӳ] !
		// �p�G���u�@��, ���γ̫e�����@��
		for (i=0; i<ViewerInfo.nWaveCount; i++){
			if (ViewerInfo.pWaveInfo[i].nXCorID == nCoorID){
				nInt = (int)(1000.0 * nPriInt / ViewerInfo.pWaveInfo[i].dbSampleRate);
				ViewerInfo.pXCoorInfo[nCoorID].dbPriLineInt = nInt;
				ViewerInfo.pXCoorInfo[nCoorID].nSecLineInt = nPriInt / nSecInt;
				break;
			}
		}
	}
	Invalidate(TRUE);
}

VOID CWaveViewer::SetXInt_MSec(int nPriInt, int nSecInt, UINT nCoorID/*=NO_ID*/)
{
	// �]�w X �b���j,
	// nPriInt ��ܥD���j�� ms ��
	// nSecInt ��ܽƶ��j�� ms ��
	if ((nCoorID == NO_ID) || (nCoorID >= ViewerInfo.nXCoorCount)){
		// �����w�]�w���Ӯy�жb, �Ϋ��w���y�жb���s�b, �N�����]���@�˪�
		for (UINT i=0; i<ViewerInfo.nXCoorCount; i++){
			ViewerInfo.pXCoorInfo[i].dbPriLineInt = nPriInt;
			ViewerInfo.pXCoorInfo[i].nSecLineInt = nPriInt / nSecInt;
		}
	}else{
		ViewerInfo.pXCoorInfo[nCoorID].dbPriLineInt = nPriInt;
		ViewerInfo.pXCoorInfo[nCoorID].nSecLineInt = nPriInt / nSecInt;
	}
	Invalidate(TRUE);
}

VOID CWaveViewer::SetYInt(int nPriInt, int nSecInt, UINT nCoorID/*=NO_ID*/)
{
	// �]�w Y �b���j,
	// nPriInt ��ܥD�n�����X�j�j
	// nSecInt ��ܨC�@�j�j�A�����X�p�j
	if ((nCoorID == NO_ID) || (nCoorID >= ViewerInfo.nXCoorCount)){
		// �����w�]�w���Ӯy�жb, �Ϋ��w���y�жb���s�b, �N�����]���@�˪�
		for (UINT i=0; i<ViewerInfo.nYCoorCount; i++){
			ViewerInfo.pYCoorInfo[i].dbPriLineInt = nPriInt;
			ViewerInfo.pYCoorInfo[i].nSecLineInt = nSecInt;
		}
	}else{
		ViewerInfo.pXCoorInfo[nCoorID].dbPriLineInt = nPriInt;
		ViewerInfo.pXCoorInfo[nCoorID].nSecLineInt = nSecInt;
	}
	Invalidate(TRUE);
}

VOID CWaveViewer::SetMaxYValue(double dbMR, UINT nCoorID/*=NO_ID*/)
{
	// �w��Y�� coordinate �]�w Y �̤j��, �Y�����w�Ϋ��w ID ���s�b, �h�w��Ĥ@��
	if (ViewerInfo.nYCoorCount == 0) return;
	if (nCoorID >= ViewerInfo.nYCoorCount) nCoorID = 0;

	// �ܧ�̤j�Ȫ��P��, �]�P�ɭn�ܧ󶡹j��
	double dbOrgMax = ViewerInfo.pYCoorInfo[nCoorID].dbMaxValue;
	double dbOrdInv = ViewerInfo.pYCoorInfo[nCoorID].dbPriLineInt;
	ViewerInfo.pYCoorInfo[nCoorID].dbMaxValue = fabs(dbMR);
	ViewerInfo.pYCoorInfo[nCoorID].dbMinValue = -1.0 * fabs(dbMR);
	ViewerInfo.pYCoorInfo[nCoorID].dbPriLineInt = fabs(dbMR) * dbOrdInv / dbOrgMax;
	SetWaveRect();
	Invalidate(TRUE);
}

//////////////////////////////////////
// Helper function

VOID CWaveViewer::SetWaveRect()
{
	// get paint rect
	this->GetClientRect((LPRECT)cViewerRect);
	cWaveRect = cViewerRect;

	CPaintDC dc(this); // device context for painting
	CFont cFont, *pOldFont;

	int nTM = 5, nBM = 5;
	int nLM = 5, nRM = 5;

	// �p��C����ɭn���ʪ��Z��
	UINT i;
	for (i=0; i<ViewerInfo.nXCoorCount; i++){
		if (!ViewerInfo.pXCoorInfo[i].PriLineInfo.bVisible) continue;
		if (ViewerInfo.pXCoorInfo[i].nDisplayFlag & SHOW_PRI_LABEL){
			cFont.CreateFontIndirect(&(ViewerInfo.pXCoorInfo[i].lgPriLogFont));
			pOldFont = (CFont*)dc.SelectObject(&cFont);
			switch(ViewerInfo.pXCoorInfo[i].nPosType){
			case 0:
				break;
			case 1:
				nBM = max(nBM, dc.GetTextExtent("0").cy + 7);
				break;
			case 2:
				nTM = max(nTM, dc.GetTextExtent("0").cy + 7);
				break;
			}
			dc.SelectObject(pOldFont);
			cFont.DeleteObject();
		}
		if (ViewerInfo.pXCoorInfo[i].nDisplayFlag & SHOW_SEC_LABEL){
			cFont.CreateFontIndirect(&(ViewerInfo.pXCoorInfo[i].lgSecLogFont));
			pOldFont = (CFont*)dc.SelectObject(&cFont);
			switch(ViewerInfo.pXCoorInfo[i].nPosType){
			case 0:
				break;
			case 1:
				nBM = max(nBM, dc.GetTextExtent("0").cy + 7);
				break;
			case 2:
				nTM = max(nTM, dc.GetTextExtent("0").cy + 7);
				break;
			}
			dc.SelectObject(pOldFont);
			cFont.DeleteObject();
		}
	}

	// �n��̪������� lable �ӯd�Ŷ�
	CString csTemp;
	int nMax = 0;
	for (i=0; i<ViewerInfo.nYCoorCount; i++){
		if (!ViewerInfo.pYCoorInfo[i].PriLineInfo.bVisible) continue;
		if (ViewerInfo.pYCoorInfo[i].nDisplayFlag & SHOW_PRI_LABEL){
			cFont.CreateFontIndirect(&(ViewerInfo.pYCoorInfo[i].lgPriLogFont));
			pOldFont = (CFont*)dc.SelectObject(&cFont);
			csTemp.Format("%.4f", ViewerInfo.pYCoorInfo[i].dbMaxValue);
			switch(ViewerInfo.pXCoorInfo[i].nPosType){
			case 0:
				break;
			case 1:
				if (m_nDigitsY > 0)
					nRM = max(nRM, m_nDigitsY * dc.GetTextExtent("0").cx + 10);
				else
					nRM = max(nRM, dc.GetTextExtent(csTemp).cx + 10);
				break;
			case 2:
				if (m_nDigitsY > 0)
					nLM = max(nLM, m_nDigitsY * dc.GetTextExtent("0").cx + 10);
				else
					nLM = max(nLM, dc.GetTextExtent(csTemp).cx + 10);
				break;
			}
			nBM = max(nBM, dc.GetTextExtent("0").cy/2);
			nTM = max(nTM, dc.GetTextExtent("0").cy/2);
			dc.SelectObject(pOldFont);
			cFont.DeleteObject();
		}
		if (ViewerInfo.pYCoorInfo[i].nDisplayFlag & SHOW_SEC_LABEL){
			cFont.CreateFontIndirect(&(ViewerInfo.pYCoorInfo[i].lgSecLogFont));
			pOldFont = (CFont*)dc.SelectObject(&cFont);
			csTemp.Format("%.4f", ViewerInfo.pYCoorInfo[i].dbMaxValue);
			switch(ViewerInfo.pXCoorInfo[i].nPosType){
			case 0:
				break;
			case 1:
				if (m_nDigitsY > 0)
					nRM = max(nRM, m_nDigitsY * dc.GetTextExtent("0").cx + 10);
				else
					nRM = max(nRM, dc.GetTextExtent(csTemp).cx + 10);
				break;
			case 2:
				if (m_nDigitsY > 0)
					nLM = max(nLM, m_nDigitsY * dc.GetTextExtent("0").cx + 10);
				else
					nLM = max(nLM, dc.GetTextExtent(csTemp).cx + 10);
				break;
			}
			nBM = max(nBM, dc.GetTextExtent("0").cy/2);
			nTM = max(nTM, dc.GetTextExtent("0").cy/2);
			dc.SelectObject(pOldFont);
			cFont.DeleteObject();
		}
	}

	cWaveRect.top += nTM;
	cWaveRect.left += nLM;
	cWaveRect.right -= nRM;
	cWaveRect.bottom -= nBM;
}

VOID CWaveViewer::SetAxisFormatY(int nDigitsY/*=0*/)
{
	// �p�G��J�� 0 �h�۰ʽվ�e��
	if(m_nDigitsY != nDigitsY){
		m_nDigitsY = nDigitsY;
		SetWaveRect();
		Invalidate(TRUE);
	}
}

VOID CWaveViewer::SetPlayCursor(DWORD nStart /*=0*/, DWORD nEnd /*=UINT_MAX*/, UINT nWaveID/*=NO_ID*/)
{
	//����Y�@�� wave, �Y�����w�Ϋ��w ID ���s�b, �h����Ĥ@��
	if (nPlayStatus != STATE_NOTHING) return;
	if (ViewerInfo.nWaveCount == 0) return;
	if (nWaveID >= ViewerInfo.nWaveCount) nWaveID = 0;
	if (nStart == UINT_MAX) nStart = 0;
	if (nEnd == UINT_MAX) nEnd = ViewerInfo.pWaveInfo[nWaveID].nWaveLength;
	if (nEnd < nStart){
		DWORD nT = nEnd;
		nEnd = nStart;
		nStart = nT;
	}
	nStart	= max(nStart, 0);
	nEnd	= min(nEnd, ViewerInfo.pWaveInfo[nWaveID].nWaveLength);
	nPlayCoordinate = ViewerInfo.pWaveInfo[nWaveID].nXCorID;

	// ��_���I�ন�H ms ���
	double dbTemp = ViewerInfo.pXCoorInfo[nPlayCoordinate].dbMaxValue - ViewerInfo.pXCoorInfo[nPlayCoordinate].dbMinValue;
	dbPlayStart	= 1000.0 * nStart / ViewerInfo.pWaveInfo[nWaveID].dbSampleRate;
	dbPlayEnd	= 1000.0 * nEnd   / ViewerInfo.pWaveInfo[nWaveID].dbSampleRate;

	// go play thread
	AfxBeginThread(PlayCursorThread, this, THREAD_PRIORITY_NORMAL, 0, 0, NULL);
}

VOID CWaveViewer::SetPlayStop()
{
	if (nPlayStatus == STATE_NOTHING) return;
	nPlayStatus = 0;
	Invalidate(TRUE);
}

VOID CWaveViewer::SetPlayPause()
{
	if (nPlayStatus == STATE_NOTHING) return;
	nPlayStatus = 2;
}

VOID CWaveViewer::SetPlayRestart()
{
	if (nPlayStatus != STATE_PAUSING) return;
	nPlayStatus = 1;
}

UINT CWaveViewer::PlayCursorThread(LPVOID pParam)
{
	CWaveViewer *pthis = (CWaveViewer*)pParam;

	MSG msg;
	int nRectL, nRectR, nRectT, nRectB, nRectW;
	double dbCoorMin, dbCoorRange;
	double dbVTXDisF, dbVTXDisR;
	pthis->nPlayStatus = 1; // is playing
	nRectL = pthis->cWaveRect.left;
	nRectR = pthis->cWaveRect.right;
	nRectT = pthis->cWaveRect.top;
	nRectB = pthis->cWaveRect.bottom;
	nRectW = pthis->cWaveRect.Width();
	dbCoorMin = pthis->ViewerInfo.pXCoorInfo[pthis->nPlayCoordinate].dbMinValue;
	dbCoorRange = pthis->ViewerInfo.pXCoorInfo[pthis->nPlayCoordinate].dbMaxValue - pthis->ViewerInfo.pXCoorInfo[pthis->nPlayCoordinate].dbMinValue;
	dbVTXDisF = pthis->dbVTXDisplayFrom;
	dbVTXDisR = pthis->dbVTXDisplayTo - pthis->dbVTXDisplayFrom;

	CClientDC dc(pthis);
	CPen cCursorPen(PS_SOLID, 0, pthis->ViewerInfo.PCurColor);
	CPen* pOldPen = (CPen*)dc.SelectObject(&cCursorPen);
	int nPreROP = dc.SetROP2(R2_XORPEN);

	// ���򼽩񪽨켽�񵲧��Τ����
	clock_t ckBeginClock = clock(); // start time
	double dbTemp;
	int nPlayCurrent, nPlayPrevious = 0;
	while(TRUE){
		// calculate current position
		dbTemp = 1000.0 * (double)(clock() - ckBeginClock) / CLOCKS_PER_SEC;
		pthis->dbPlayCurrent = pthis->dbPlayStart + dbTemp;
		dbTemp = (pthis->dbPlayCurrent - dbCoorMin) / dbCoorRange;
		dbTemp = (dbTemp - dbVTXDisF) / dbVTXDisR;
		nPlayCurrent = (int)(dbTemp * nRectW + nRectL);

		// check if play end
		if (nPlayCurrent >= nRectR){
			dc.MoveTo(nPlayPrevious, nRectT);
			dc.LineTo(nPlayPrevious, nRectB);
			pthis->nPlayStatus = 0;
			pthis->Invalidate(TRUE);
			break;
		}

		// check user suspend
		if (pthis->nPlayStatus == 0){
			dc.MoveTo(nPlayPrevious, nRectT);
			dc.LineTo(nPlayPrevious, nRectB);
			pthis->Invalidate(TRUE);
			break;
		}

		// check user suspend
		while(pthis->nPlayStatus == 2){
			if (::PeekMessage(&msg, NULL, 0, 0, PM_REMOVE)){
				::TranslateMessage(&msg);
				::DispatchMessage(&msg);
			}
			ckBeginClock = clock(); // reset start time
			pthis->dbPlayStart = pthis->dbPlayCurrent; // reset start position
		}

		if (nPlayPrevious != nPlayCurrent){
			dc.MoveTo(nPlayPrevious, nRectT);
			dc.LineTo(nPlayPrevious, nRectB);
			dc.MoveTo(nPlayCurrent, nRectT);
			dc.LineTo(nPlayCurrent, nRectB);
		}

		nPlayPrevious = nPlayCurrent;

		if (::PeekMessage(&msg, NULL, 0, 0, PM_REMOVE)){
			::TranslateMessage(&msg);
			::DispatchMessage(&msg);
		}
	}
	dc.SetROP2(nPreROP);
	dc.SelectObject(pOldPen);
	return 0;
}

VOID CWaveViewer::AddSelection(DWORD nStart /*=0*/, DWORD nEnd /*=UINT_MAX*/, UINT nWaveID/*=NO_ID*/)
{
	// nStart �M nEnd �O�Y�� Wave �� sample ��
	// �w��Y�� wave �]�w��ܰϬq, �Y�����w�Ϋ��w ID ���s�b, �h�w��Ĥ@��
	if (ViewerInfo.nWaveCount == 0) return;
	if (nWaveID >= ViewerInfo.nWaveCount) nWaveID = 0;
	nStart	= max(nStart, 0);
	nEnd	= min(nEnd, ViewerInfo.pWaveInfo[nWaveID].nWaveLength - 1);
	if (nEnd < nStart){
		DWORD nT = nEnd;
		nEnd = nStart;
		nStart = nT;
	}else if(nEnd == nStart){
		// �T�O nEnd > nStart, modified by CCL, 20050615
		if(nEnd < (ViewerInfo.pWaveInfo[nWaveID].nWaveLength - 1))
			++nEnd;
		else if(nStart > 0)
			--nStart;
	}
	// End of 20050615 CCL modified for sercurity

	// �令�Φʤ��� => �q nStart �Y��� nEnd ��!
	SELECTION_INFO sif;
	UINT nCoorID = ViewerInfo.pWaveInfo[nWaveID].nXCorID;
	double dbCoorLength = ViewerInfo.pXCoorInfo[nCoorID].dbMaxValue - ViewerInfo.pXCoorInfo[nCoorID].dbMinValue;
	double dbStartMs = 1000.0 * nStart / ViewerInfo.pWaveInfo[nWaveID].dbSampleRate;
	double dbEndMs = 1000.0 * nEnd / ViewerInfo.pWaveInfo[nWaveID].dbSampleRate;
	sif.dbStart	= (dbStartMs - ViewerInfo.pXCoorInfo[nCoorID].dbMinValue) / dbCoorLength;
	sif.dbEnd	= (dbEndMs	 - ViewerInfo.pXCoorInfo[nCoorID].dbMinValue) / dbCoorLength;
	if (ViewerInfo.nDisplayFlag & SINGLE_SELECTION) cSelectionArray.RemoveAll();
	cSelectionArray.Add(sif);
	Invalidate(TRUE);
}

VOID CWaveViewer::AddSelection_MS(double dbStart/*=-1*/, double dbEnd/*=-1*/, UINT nWaveID/*=NO_ID*/)
{
	// dbStart �M dbEnd �O�Y�� Wave �� ms ��
	// �w��Y�� wave �]�w��ܰϬq, �Y�����w�Ϋ��w ID ���s�b, �h�w��Ĥ@��
	if (ViewerInfo.nWaveCount == 0) return;
	if (nWaveID >= ViewerInfo.nWaveCount) nWaveID = 0;
	double dbTargetDuration = 1000.0 * ViewerInfo.pWaveInfo[nWaveID].nWaveLength / ViewerInfo.pWaveInfo[nWaveID].dbSampleRate;
	if (dbEnd == -1) dbEnd = dbTargetDuration;
	dbStart	= max(dbStart, 0);
	dbEnd	= min(dbEnd, dbTargetDuration);
	if (dbEnd < dbStart){
		double dbT = dbEnd;
		dbEnd = dbStart;
		dbStart = dbT;
	}else if(dbEnd == dbStart){
		// �T�O nEnd > nStart, modified by CCL, 20050615
		// �p�G�o�̤@��, �N�����令�[�аO�u
		cMarkLineArray.Add(dbStart);
		return;
		// End of 20050615 CCL modified for sercurity
	}

	// dbStart �Y��� dbEnd ��!
	SELECTION_INFO sif;
	sif.dbStart	= dbStart;
	sif.dbEnd	= dbEnd;
	if (ViewerInfo.nDisplayFlag & SINGLE_SELECTION) cSelectionArray.RemoveAll();
	cSelectionArray.Add(sif);
	Invalidate(TRUE);
}

VOID CWaveViewer::ClearSelection()
{
	cSelectionArray.RemoveAll();
	Invalidate(TRUE);
}

VOID CWaveViewer::ClearSelection(int nIndex)
{
	if (cSelectionArray.GetSize() == 0) return;
	if ((nIndex < 0) || (nIndex >= cSelectionArray.GetSize())) return;
	cSelectionArray.RemoveAt(nIndex);
	Invalidate(TRUE);
}

VOID CWaveViewer::AddMarkLine(DWORD nMark/*=UINT_MAX*/, UINT nWaveID/*=NO_ID*/)
{
	// nMark �O�Y�� Wave �� sample ��
	// �w��Y�� wave �]�w�аO�u, �Y�����w�Ϋ��w ID ���s�b, �h�w��Ĥ@��
	if (ViewerInfo.nWaveCount == 0) return;
	if (nWaveID >= ViewerInfo.nWaveCount) nWaveID = 0;

	// 20050615 CCL modified for sercurity
	if (nMark >= ViewerInfo.pWaveInfo[nWaveID].nWaveLength)
		nMark  = ViewerInfo.pWaveInfo[nWaveID].nWaveLength - 1;
	if (nMark < 0) nMark = 0;

	// �令�Φʤ��� => �аO�u��m�b nMark �Y!
	UINT nCoorID = ViewerInfo.pWaveInfo[nWaveID].nXCorID;
	double dbCoorLength = ViewerInfo.pXCoorInfo[nCoorID].dbMaxValue - ViewerInfo.pXCoorInfo[nCoorID].dbMinValue;
	double dbMark = 1000.0 * nMark / ViewerInfo.pWaveInfo[nWaveID].dbSampleRate; // ms
	dbMark	= (dbMark - ViewerInfo.pXCoorInfo[nCoorID].dbMinValue) / dbCoorLength;
	cMarkLineArray.Add(dbMark);
	Invalidate(TRUE);
}

VOID CWaveViewer::AddMarkLine_MS(double dbMark/*=-1*/, UINT nWaveID/*=NO_ID*/)
{
	// dbMark �O�Y�� Wave �� ms ��
	// �w��Y�� wave �]�w�аO�u, �Y�����w�Ϋ��w ID ���s�b, �h�w��Ĥ@��
	if (ViewerInfo.nWaveCount == 0) return;
	if (nWaveID >= ViewerInfo.nWaveCount) nWaveID = 0;
	double dbTargetDuration = 1000.0 * ViewerInfo.pWaveInfo[nWaveID].nWaveLength / ViewerInfo.pWaveInfo[nWaveID].dbSampleRate;

	// 20050615 CCL modified for sercurity
	if (dbMark >= dbTargetDuration) dbMark = dbTargetDuration - 1;
	if (dbMark < 0) dbMark = 0;

	// �аO�u��m�b dbMark �Y!
	cMarkLineArray.Add(dbMark);
	Invalidate(TRUE);
}

VOID CWaveViewer::ClearMarkLine()
{
	cMarkLineArray.RemoveAll();
	Invalidate(TRUE);
}

// ���o�ثe waveviewer ���k��ɩҹ������Y�Ӫi�� sec ��
BOOL CWaveViewer::GetViewBoundary(double *pdbStart/*=NULL*/, double *pdbEnd/*=NULL*/,
								  double *pdbTop/*=NULL*/, double *pdbBottom/*=NULL*/,
								  UINT nWaveID/*=NO_ID*/)
{
	// �w��Y�� wave �]�w���k���, �Y�����w�Ϋ��w ID ���s�b, �h�w��Ĥ@��
	if (ViewerInfo.nWaveCount == 0) return FALSE;
	if (nWaveID >= ViewerInfo.nWaveCount) nWaveID = 0;

	// pdbStart �M pdbEnd �O�Y�� Wave �� sec ��
	UINT nCoorID = ViewerInfo.pWaveInfo[nWaveID].nXCorID;
	double dbCoorLength = ViewerInfo.pXCoorInfo[nCoorID].dbMaxValue - ViewerInfo.pXCoorInfo[nCoorID].dbMinValue;
	if (pdbStart)
		*pdbStart = ViewerInfo.pXCoorInfo[nCoorID].dbMinValue + dbVTXDisplayFrom * dbCoorLength;
	if (pdbEnd)
		*pdbEnd = ViewerInfo.pXCoorInfo[nCoorID].dbMinValue + dbVTXDisplayTo * dbCoorLength;

	// Y �b���]�w�����ϥ�
	if (pdbTop)		*pdbTop		= dbVTYDisplayTo;
	if (pdbBottom)	*pdbBottom	= dbVTYDisplayFrom;
	return TRUE;
}

// �άY�Ӫi�� sec �Ƴ]�w waveviewer �����k���
BOOL CWaveViewer::SetViewBoundary(double dbLeft/*=-1*/, double dbRight/*=-1*/,
								  double dbTop/*=DBL_MAX*/, double dbBottom/*=-DBL_MAX*/,
								  UINT nWaveID/*=NO_ID*/)
{
	// �w��Y�� wave �]�w���k���, �Y�����w�Ϋ��w ID ���s�b, �h�w��Ĥ@��
	if (ViewerInfo.nWaveCount == 0) return FALSE;
	if (nWaveID >= ViewerInfo.nWaveCount) nWaveID = 0;

	// dbLeft �M dbRight �O�Y�� Wave �� sec ��
	UINT nCoorID = ViewerInfo.pWaveInfo[nWaveID].nXCorID;
	double dbCoorLength = ViewerInfo.pXCoorInfo[nCoorID].dbMaxValue - ViewerInfo.pXCoorInfo[nCoorID].dbMinValue;
	if (dbLeft == dbRight) return FALSE;
	if (dbLeft < 0)
		dbVTXDisplayFrom = 0;
	else
		dbVTXDisplayFrom = (dbLeft - ViewerInfo.pXCoorInfo[nCoorID].dbMinValue) / dbCoorLength;
	if (dbRight < 0)
		dbVTXDisplayTo = 1;
	else
		dbVTXDisplayTo = (dbRight - ViewerInfo.pXCoorInfo[nCoorID].dbMinValue) / dbCoorLength;
	if (dbVTXDisplayFrom > dbVTXDisplayTo){
		double dbTemp = dbVTXDisplayFrom;
		dbVTXDisplayFrom = dbVTXDisplayTo;
		dbVTXDisplayTo = dbTemp;
	}

	// set Y
	if (dbTop == dbBottom) return FALSE;
	if (dbTop == DBL_MAX) dbTop = dbVTYDisplayTo;
	if (dbBottom == -DBL_MAX) dbBottom = dbVTYDisplayFrom;
	if(dbTop > dbBottom){
		dbVTYDisplayTo = dbTop;
		dbVTYDisplayFrom = dbBottom;
	}else{
		dbVTYDisplayTo = dbBottom;
		dbVTYDisplayFrom = dbTop;
	}

	Invalidate(TRUE);
	return TRUE;
}

VOID CWaveViewer::SetSingleSelection(BOOL bSingle /*=FALSE*/)
{
	// �]�w�u�঳��@����q, �p�G�ثe�w�g���h�ӿ���q, �h�u�d�Ĥ@��
	if (bSingle){
		ViewerInfo.nDisplayFlag |= SINGLE_SELECTION;
		if (cSelectionArray.GetSize() > 1){
			SELECTION_INFO sif;
			sif.dbStart	= cSelectionArray[0].dbStart;
			sif.dbEnd	= cSelectionArray[0].dbEnd;
			cSelectionArray.RemoveAll();
			cSelectionArray.Add(sif);
		}
	}else
		ViewerInfo.nDisplayFlag &= !SINGLE_SELECTION;
	Invalidate(TRUE);
}

// ���o �i����ܾ� ���ù��y��
CRect CWaveViewer::GetViewerRect()
{
	CRect rtnRect = cViewerRect;
	ClientToScreen((LPRECT)rtnRect);
	return rtnRect;
}

// ���o�i����ܾ��� �i�γ��� ���ù��y��
CRect CWaveViewer::GetWaveRect()
{
	CRect rtnRect = cWaveRect;
	ClientToScreen((LPRECT)rtnRect);
	return rtnRect;
}

int CWaveViewer::GetPlayPos_Sam(UINT nWaveID/*=NO_ID*/)
{
	// ���o�ثe�����m��� nWaveID �i�Ϊ� sample ��
	// �Y�����w�Ϋ��w nWaveID ���s�b, �h�w��Ĥ@���i��
	if (ViewerInfo.nWaveCount == 0) return -1;
	if (nWaveID >= ViewerInfo.nWaveCount) nWaveID = 0;

	// �p��۹��� nWaveID ���i�Ϊ� sample ��
	UINT nCoorID = ViewerInfo.pWaveInfo[nWaveID].nXCorID;
	double dbTemp = ViewerInfo.pXCoorInfo[nCoorID].dbMaxValue - ViewerInfo.pXCoorInfo[nCoorID].dbMinValue;
	dbTemp = ViewerInfo.pXCoorInfo[nCoorID].dbMinValue + dbPlayCurrent * dbTemp;
	int nSample = (int)(dbTemp * ViewerInfo.pWaveInfo[nWaveID].dbSampleRate / 1000.0);

	// �]�����h���i��, �ҥH�����m���i��W�L�ҫ��w���i��
	if (nSample >= (int)ViewerInfo.pWaveInfo[nWaveID].nWaveLength)
		nSample = (int)(ViewerInfo.pWaveInfo[nWaveID].nWaveLength - 1);

	return nSample;
}

double CWaveViewer::GetPlayPos_Sec(UINT nWaveID/*=NO_ID*/)
{
	// ���o�ثe�����m��� nWaveID �i�Ϊ� sample ��
	// �Y�����w�Ϋ��w nWaveID ���s�b, �h�w��Ĥ@���i��
	if (ViewerInfo.nWaveCount == 0) return -1;
	if (nWaveID >= ViewerInfo.nWaveCount) nWaveID = 0;

	// �p��۹��� nWaveID ���i�Ϊ� second ��
	UINT nCoorID = ViewerInfo.pWaveInfo[nWaveID].nXCorID;
	double dbTemp = ViewerInfo.pXCoorInfo[nCoorID].dbMaxValue - ViewerInfo.pXCoorInfo[nCoorID].dbMinValue;
	double dbSecond = ViewerInfo.pXCoorInfo[nCoorID].dbMinValue + dbPlayCurrent * dbTemp;

	// �]�����h���i��, �ҥH�����m���i��W�L�ҫ��w���i��
	dbTemp = (double)ViewerInfo.pWaveInfo[nWaveID].nWaveLength / ViewerInfo.pWaveInfo[nWaveID].dbSampleRate;
	if (dbSecond >= dbTemp) dbSecond = dbTemp;

	return dbSecond;
}

LONG CWaveViewer::GetXPos(DWORD dwSample, UINT nWaveID/*=NO_ID*/)
{
	// ���o�� nWaveID �i�Ϊ��� dwSample �� sample ���ù��y��
	// �Y�����w�Ϋ��w nWaveID ���s�b, �h�w��Ĥ@���i��
	if (ViewerInfo.nWaveCount == 0) return FALSE;
	if (nWaveID >= ViewerInfo.nWaveCount) nWaveID = 0;
	if (dwSample >= ViewerInfo.pWaveInfo[nWaveID].nWaveLength) return -1;

	// �p�ⳡ��
	int nCoorID = ViewerInfo.pWaveInfo[nWaveID].nXCorID;
	double dbTemp = 1000.0 * dwSample / ViewerInfo.pWaveInfo[nWaveID].dbSampleRate; // x
	dbTemp -= ViewerInfo.pXCoorInfo[nCoorID].dbMinValue;
	dbTemp /= ViewerInfo.pXCoorInfo[nCoorID].dbMaxValue - ViewerInfo.pXCoorInfo[nCoorID].dbMinValue; // x'
	dbTemp = (dbTemp - dbVTXDisplayFrom) / (dbVTXDisplayTo - dbVTXDisplayFrom);
	LONG x = (int)(cWaveRect.left + dbTemp * cWaveRect.Width());
	return x;
}

LONG CWaveViewer::GetXPos(double dbSecond, UINT nWaveID/*=NO_ID*/)
{
	// ���o�� nWaveID �i�Ϊ��� dwSample �� sample ���ù��y��
	// �Y�����w�Ϋ��w nWaveID ���s�b, �h�w��Ĥ@���i��
	if (ViewerInfo.nWaveCount == 0) return FALSE;
	if (nWaveID >= ViewerInfo.nWaveCount) nWaveID = 0;

	double dbTemp = (double)ViewerInfo.pWaveInfo[nWaveID].nWaveLength / ViewerInfo.pWaveInfo[nWaveID].dbSampleRate;
	if (dbSecond > dbTemp) return -1;

	// �p�ⳡ��
	int nCoorID = ViewerInfo.pWaveInfo[nWaveID].nXCorID;
	dbTemp = 1000.0 * dbSecond - ViewerInfo.pXCoorInfo[nCoorID].dbMinValue;
	dbTemp /= ViewerInfo.pXCoorInfo[nCoorID].dbMaxValue - ViewerInfo.pXCoorInfo[nCoorID].dbMinValue; // x'
	dbTemp = (dbTemp - dbVTXDisplayFrom) / (dbVTXDisplayTo - dbVTXDisplayFrom);
	LONG x = (int)(cWaveRect.left + dbTemp * cWaveRect.Width());
	return x;
}

int CWaveViewer::GetMarklinePOS_Sam(int nIdx/*=0*/, UINT nWaveID/*=NO_ID*/)
{
	// ���o�� nIdx ���аO�u�b�� nWaveID �i�ΤW�� sample ��
	// �Y�����w�Ϋ��w nWaveID ���s�b, �h�w��Ĥ@���i��
	if ((nIdx < 0) || (nIdx >= cMarkLineArray.GetSize())) return -1;
	if (ViewerInfo.nWaveCount == 0) return -1;
	if (nWaveID >= ViewerInfo.nWaveCount) nWaveID = 0;

	// �p��۹��� nWaveID ���i�Ϊ� sample ��
	UINT nCoorID = ViewerInfo.pWaveInfo[nWaveID].nXCorID;
	double dbTemp = ViewerInfo.pXCoorInfo[nCoorID].dbMaxValue - ViewerInfo.pXCoorInfo[nCoorID].dbMinValue;
	dbTemp = ViewerInfo.pXCoorInfo[nCoorID].dbMinValue + cMarkLineArray[nIdx] * dbTemp;
	int nSample = (int)(dbTemp * ViewerInfo.pWaveInfo[nWaveID].dbSampleRate / 1000.0);

	// �]�����h���i��, �ҥH���аO�u����m���i��W�L�ҫ��w���i��
	if (nSample >= (int)ViewerInfo.pWaveInfo[nWaveID].nWaveLength)
		nSample = (int)(ViewerInfo.pWaveInfo[nWaveID].nWaveLength - 1);

	return nSample;
}

double CWaveViewer::GetMarklinePOS_Sec(int nIdx/*=0*/, UINT nWaveID/*=NO_ID*/)
{
	// ���o�� nIdx ���аO�u�b�� nWaveID �i�ΤW�� sample ��
	// �Y�����w�Ϋ��w nWaveID ���s�b, �h�w��Ĥ@���i��
	if ((nIdx < 0) || (nIdx >= cMarkLineArray.GetSize())) return -1;
	if (ViewerInfo.nWaveCount == 0) return -1;
	if (nWaveID >= ViewerInfo.nWaveCount) nWaveID = 0;

	// �p��۹��� nWaveID ���i�Ϊ� sample ��
	UINT nCoorID = ViewerInfo.pWaveInfo[nWaveID].nXCorID;
	double dbTemp = ViewerInfo.pXCoorInfo[nCoorID].dbMaxValue - ViewerInfo.pXCoorInfo[nCoorID].dbMinValue;
	double dbSecond = ViewerInfo.pXCoorInfo[nCoorID].dbMinValue + cMarkLineArray[nIdx] * dbTemp;

	// �]�����h���i��, �ҥH���аO�u����m���i��W�L�ҫ��w���i��
	dbTemp = (double)ViewerInfo.pWaveInfo[nWaveID].nWaveLength / ViewerInfo.pWaveInfo[nWaveID].dbSampleRate;
	if (dbSecond >= dbTemp) dbSecond = dbTemp;

	return dbSecond;
}

BOOL CWaveViewer::GetSelectionPOS_Sam(int nIdx/*=0*/, DWORD *pStart/*=NULL*/, DWORD *pEnd/*=NULL*/, UINT nWaveID/*=NO_ID*/)
{
	// ���o�� nIdx ���аO�u�b�� nWaveID �i�ΤW�� sample ��
	// �Y�����w�Ϋ��w nWaveID ���s�b, �h�w��Ĥ@���i��
	if ((nIdx < 0) || (nIdx >= cSelectionArray.GetSize())) return FALSE;
	if (ViewerInfo.nWaveCount == 0) return FALSE;
	if (nWaveID >= ViewerInfo.nWaveCount) nWaveID = 0;

	// �p��۹��� nWaveID ���i�Ϊ� sample ��
	UINT nCoorID = ViewerInfo.pWaveInfo[nWaveID].nXCorID;
	double dbCDXRange = ViewerInfo.pXCoorInfo[nCoorID].dbMaxValue - ViewerInfo.pXCoorInfo[nCoorID].dbMinValue;
	double dbTemp = ViewerInfo.pXCoorInfo[nCoorID].dbMinValue + cSelectionArray[nIdx].dbStart * dbCDXRange;
	*pStart = (DWORD)(dbTemp * ViewerInfo.pWaveInfo[nWaveID].dbSampleRate / 1000.0);
	dbTemp = ViewerInfo.pXCoorInfo[nCoorID].dbMinValue + cSelectionArray[nIdx].dbEnd * dbCDXRange;
	*pEnd = (DWORD)(dbTemp * ViewerInfo.pWaveInfo[nWaveID].dbSampleRate / 1000.0);

	// �]�����h���i��, �ҥH���аO�u����m���i��W�L�ҫ��w���i��
	if (*pStart < 0) *pStart = 0;
	if (*pStart >= (DWORD)ViewerInfo.pWaveInfo[nWaveID].nWaveLength)
		*pStart = (DWORD)(ViewerInfo.pWaveInfo[nWaveID].nWaveLength - 1);
	if (*pEnd < 0) *pEnd = 0;
	if (*pEnd >= (DWORD)ViewerInfo.pWaveInfo[nWaveID].nWaveLength)
		*pEnd = (DWORD)(ViewerInfo.pWaveInfo[nWaveID].nWaveLength - 1);

	return TRUE;
}

BOOL CWaveViewer::GetSelectionPOS_Sec(int nIdx/*=0*/, double *pStart/*=NULL*/, double *pEnd/*=NULL*/, UINT nWaveID/*=NO_ID*/)
{
	// ���o�� nIdx ���аO�u�b�� nWaveID �i�ΤW�� sample ��
	// �Y�����w�Ϋ��w nWaveID ���s�b, �h�w��Ĥ@���i��
	if ((nIdx < 0) || (nIdx >= cSelectionArray.GetSize())) return FALSE;
	if (ViewerInfo.nWaveCount == 0) return FALSE;
	if (nWaveID >= ViewerInfo.nWaveCount) nWaveID = 0;

	// �p��۹��� nWaveID ���i�Ϊ� sample ��
	UINT nCoorID = ViewerInfo.pWaveInfo[nWaveID].nXCorID;
	double dbCDXRange = ViewerInfo.pXCoorInfo[nCoorID].dbMaxValue - ViewerInfo.pXCoorInfo[nCoorID].dbMinValue;
	*pStart = ViewerInfo.pXCoorInfo[nCoorID].dbMinValue + cSelectionArray[nIdx].dbStart * dbCDXRange;
	*pEnd   = ViewerInfo.pXCoorInfo[nCoorID].dbMinValue + cSelectionArray[nIdx].dbEnd * dbCDXRange;
	*pStart /= 1000.0;
	*pEnd   /= 1000.0;

	// �]�����h���i��, �ҥH���аO�u����m���i��W�L�ҫ��w���i��
	double dbTemp = (double)ViewerInfo.pWaveInfo[nWaveID].nWaveLength / ViewerInfo.pWaveInfo[nWaveID].dbSampleRate;
	if (*pStart < 0.0) *pStart = 0.0;
	if (*pStart > dbTemp) *pStart = dbTemp;
	if (*pEnd < 0.0) *pEnd = 0.0;
	if (*pEnd > dbTemp) *pEnd = dbTemp;

	return TRUE;
}

BOOL CWaveViewer::SetWaveFormat(PCMWAVEFORMAT const & waveFormat, UINT nWaveID/*=0*/)
{
	// ���o�� nWaveID �i�Ϊ��� dwSample �� sample ���ù��y��
	// �Y�����w�Ϋ��w nWaveID ���s�b, �h�w��Ĥ@���i��
	if (ViewerInfo.nWaveCount == 0) return FALSE;
	if (nWaveID >= ViewerInfo.nWaveCount) nWaveID = 0;

	DWORD temp = waveFormat.wf.nBlockAlign;

	temp /= waveFormat.wf.nChannels;
	temp = (temp << 3);	// temp * 8�A�ഫ��bit��
	if(temp != waveFormat.wBitsPerSample)
		return FALSE;

	temp = waveFormat.wf.nSamplesPerSec;
	temp *= waveFormat.wf.nBlockAlign;

	if(temp != waveFormat.wf.nAvgBytesPerSec)
		return FALSE;

	ViewerInfo.pWaveInfo[nWaveID].m_WaveFormat = waveFormat;
	return TRUE;
}

PCMWAVEFORMAT CWaveViewer::GetWaveFormat(UINT nWaveID/*=0*/) const
{
	PCMWAVEFORMAT tmp;
	tmp.wBitsPerSample = -1;

	// ���o�� nWaveID �i�Ϊ��� dwSample �� sample ���ù��y��
	// �Y�����w�Ϋ��w nWaveID ���s�b, �h�w��Ĥ@���i��
	if (ViewerInfo.nWaveCount == 0) return tmp;
	if (nWaveID >= ViewerInfo.nWaveCount) nWaveID = 0;

	return ViewerInfo.pWaveInfo[nWaveID].m_WaveFormat;
}

//���F�O�Ҹ̭����Ȥ��|�Q���A�ҥH�^�� non-const pointer �� const data
const double* CWaveViewer::GetWaveData(UINT nWaveID/*=0*/)
{
	// ���o�� nWaveID �i�Ϊ��� dwSample �� sample ���ù��y��
	// �Y�����w�Ϋ��w nWaveID ���s�b, �h�w��Ĥ@���i��
	if (ViewerInfo.nWaveCount == 0) return NULL;
	if (nWaveID >= ViewerInfo.nWaveCount) nWaveID = 0;

	return ViewerInfo.pWaveInfo[nWaveID].pdbWaveData;
}

DWORD CWaveViewer::GetWaveLangthInSample(UINT nWaveID/*=0*/) const
{
	// ���o�� nWaveID �i�Ϊ��� dwSample �� sample ���ù��y��
	// �Y�����w�Ϋ��w nWaveID ���s�b, �h�w��Ĥ@���i��
	if (ViewerInfo.nWaveCount == 0) return 0;
	if (nWaveID >= ViewerInfo.nWaveCount) nWaveID = 0;

	return ViewerInfo.pWaveInfo[nWaveID].nWaveLength;
}
