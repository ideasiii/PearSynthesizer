/*Chen Jau-Hung  1997.1.13 Mon*/
#ifndef memio_h_
#define memio_h_

#include <afx.h>
#include <afxwin.h>
#include <windowsx.h>

void FatalErrorMessage(CString csText, char *pTitle, BOOL bRetry=FALSE); 
void TrimLR(CString &cs);


//********************************** Memory ******************************

int NewHandler( size_t size );
void OutOfMemory(char * who, size_t size);

class CMemNew
{
public:

	CMemNew(int global=0);

    static int counter;
};

LPVOID AllocMem(UINT  uFlags,	// object allocation attributes 
				DWORD  dwBytes);	// number of bytes to allocate   

LPVOID AllocMem(DWORD  dwBytes);// number of bytes to allocate   
LPVOID ReAllocMem(LPCVOID p, DWORD  dwBytes, UINT  uFlags);
LPVOID ReAllocMem(LPCVOID p, DWORD  dwBytes);
	
void FreeMEMory(void **p); //this "MEM" is for static use			
#define FreeMem(p) (FreeMEMory((void **)&p))

//****************************** CCFile **********************************

BOOL LoadFile(LPCTSTR path_name, void *pData, DWORD dataLen);
void * LoadFile(LPCTSTR path_name, int block_size, int *size);
BOOL SaveFile(LPCTSTR path_name, void *pData, DWORD dataLen);
BOOL Alive(const char *path,struct stat *pStat=NULL);
BOOL IsDir(const char *path);
BOOL MkDir(const char *path);

class CCFile: public CFile
{
public:
	CCFile():CFile()
	{
		;
    }
	~CCFile()
	{
		Cclose();
	}
	BOOL AliveOpen(LPCTSTR path_name);
	BOOL KillOpen(LPCTSTR path_name);
	BOOL Copen(LPCTSTR path_name,UINT mode=CFile::typeBinary|CFile::modeRead);
                              //default mode is for read      
	LONG Cseek(LONG off);
	BOOL Cread(void *buffer,UINT nCount);
	BOOL GetLine(CString &cs); //remove CRLF
	BOOL PutLine(CString &cs); //append CRLF
	BOOL PutLine(LPCTSTR p); //append CRLF
	void CCFile::fprintf(const char *pFormat, ...);
	BOOL Cwrite(const void *buffer,UINT nCount);
	BOOL Cclose(void);
	void FileError(CFileException *e, LPCTSTR s);
};
#endif
