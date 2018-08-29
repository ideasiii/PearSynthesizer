#if !defined(AFX_MYRECORDSET_H__E13B44F0_0462_42BF_8BA0_C216B4BF3536__INCLUDED_)
#define AFX_MYRECORDSET_H__E13B44F0_0462_42BF_8BA0_C216B4BF3536__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000
// MyRecordset.h : header file
//
#include "afxdb.h" //這行要自已加，不然會出錯
/////////////////////////////////////////////////////////////////////////////
// MyRecordset recordset

class MyRecordset : public CRecordset
{
public:
	MyRecordset(CDatabase* pDatabase = NULL);
	DECLARE_DYNAMIC(MyRecordset)

// Field/Param Data
	//{{AFX_FIELD(MyRecordset, CRecordset)
	long	m_Index;
	CString	m_Start;
	CString	m_Content;
	long	m_Phone1;
	long	m_Phone2;
	long	m_Phone3;
	long	m_Phone4;
	long	m_Phone5;
	long	m_Phone6;
	long	m_Phone7;
	long	m_Phone8;
	long	m_Phone9;
	long	m_Phone10;
	CString	m_Bytes;
	long	m_Counter;
	CString	m_Attr;
	//}}AFX_FIELD


// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(MyRecordset)
	public:
	virtual CString GetDefaultConnect();    // Default connection string
	virtual CString GetDefaultSQL();    // Default SQL for Recordset
	virtual void DoFieldExchange(CFieldExchange* pFX);  // RFX support
	//}}AFX_VIRTUAL

// Implementation
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_MYRECORDSET_H__E13B44F0_0462_42BF_8BA0_C216B4BF3536__INCLUDED_)
