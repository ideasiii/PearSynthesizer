// MyRecordset.cpp : implementation file
//

#include "stdafx.h"
#include "MyRecordset.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// MyRecordset

IMPLEMENT_DYNAMIC(MyRecordset, CRecordset)

MyRecordset::MyRecordset(CDatabase* pdb)
	: CRecordset(pdb)
{
	//{{AFX_FIELD_INIT(MyRecordset)
	m_Index = 0;
	m_Start = _T("");
	m_Content = _T("");
	m_Phone1 = 0;
	m_Phone2 = 0;
	m_Phone3 = 0;
	m_Phone4 = 0;
	m_Phone5 = 0;
	m_Phone6 = 0;
	m_Phone7 = 0;
	m_Phone8 = 0;
	m_Phone9 = 0;
	m_Phone10 = 0;
	m_Bytes = _T("");
	m_Counter = 0;
	m_Attr = _T("");
	m_nFields = 16;
	//}}AFX_FIELD_INIT
	m_nDefaultType = dynaset;
}


CString MyRecordset::GetDefaultConnect()
{
	return _T("ODBC;DSN=WORD_DB");
}

CString MyRecordset::GetDefaultSQL()
{
	return _T("[WORD_DAT]");
}

void MyRecordset::DoFieldExchange(CFieldExchange* pFX)
{
	//{{AFX_FIELD_MAP(MyRecordset)
	pFX->SetFieldType(CFieldExchange::outputColumn);
	RFX_Long(pFX, _T("[Index]"), m_Index);
	RFX_Text(pFX, _T("[Start]"), m_Start);
	RFX_Text(pFX, _T("[Content]"), m_Content);
	RFX_Long(pFX, _T("[Phone1]"), m_Phone1);
	RFX_Long(pFX, _T("[Phone2]"), m_Phone2);
	RFX_Long(pFX, _T("[Phone3]"), m_Phone3);
	RFX_Long(pFX, _T("[Phone4]"), m_Phone4);
	RFX_Long(pFX, _T("[Phone5]"), m_Phone5);
	RFX_Long(pFX, _T("[Phone6]"), m_Phone6);
	RFX_Long(pFX, _T("[Phone7]"), m_Phone7);
	RFX_Long(pFX, _T("[Phone8]"), m_Phone8);
	RFX_Long(pFX, _T("[Phone9]"), m_Phone9);
	RFX_Long(pFX, _T("[Phone10]"), m_Phone10);
	RFX_Text(pFX, _T("[Bytes]"), m_Bytes);
	RFX_Long(pFX, _T("[Counter]"), m_Counter);
	RFX_Text(pFX, _T("[Attr]"), m_Attr);
	//}}AFX_FIELD_MAP
}

/////////////////////////////////////////////////////////////////////////////
// MyRecordset diagnostics

#ifdef _DEBUG
void MyRecordset::AssertValid() const
{
	CRecordset::AssertValid();
}

void MyRecordset::Dump(CDumpContext& dc) const
{
	CRecordset::Dump(dc);
}
#endif //_DEBUG
