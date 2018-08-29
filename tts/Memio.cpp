// Chen Jau-Hung (³¯¬L§») 1997.1.13
#include "stdafx.h"
#include <sys\stat.h>
#include <new.h>
#include "Memio.h"

//********************************** Memory ******************************

/*C++ xalloc Exceptions
Microsoft C++ implements an alternate method of handling new memory allocation failure, based on 
the current ANSI C++ working paper proposal. Using this method, a new run-time function, 
_standard_new_handler, throws a C++ exception of type xalloc in the event of a new allocation 
failure. xalloc exceptions are based on the exception class hierarchy defined in STDEXCPT.H. 
This header file, along with an example program, is located in the 
\SAMPLES\WIN32\XALLOC subdirectory of your Visual C++ installation. You must include STDEXCPT.H 
and its accompanying .CXX files in your project in order to use xalloc exceptions.

Using xalloc
If the new operator fails to allocate memory for any reason, you can choose to have your program 
throw an xalloc exception object.
To facilitate using the exception classes, a new run-time function has been added. The 
_standard_new_handler function, declared in STDEXCPT.H, is prototyped as follows:
  
int _standard_new_handler( size_t );
  
If you want new to throw an xalloc exception in the event of a memory allocation failure, compile
 with the /GX option (Enable Exception Handling), and in your code, call _set_new_handler with 
 _standard_new_handler as its argument. You can then use try/catch exception handling constructs
 to detect and handle xalloc exceptions. In addition, you must copy the STDEXCPT.H header file 
 and associated .CXX implementation files to your project subdirectory. Be sure to include 
 STDEXCPT.H in your code, and add the .CXX files in that subdirectory to your own project.
The _standard_new_handler function creates a local static object of the xalloc class, which in 
turn calls the raise member function; thereby throwing an xalloc exception. Note that 
_standard_new_handler does not allocate memory from the free store (it does not call new or 
malloc); thus, it will not recurse.
If you are programming in C++ using the Microsoft Foundation Classes, note that MFC installs its
 own new exception handler that throws an exception of type CMemoryException. This will override 
 the xalloc exception behavior described above.
For more information on xalloc exceptions, see the sample program and its accompanying help file 
in the \SAMPLES\WIN32\XALLOC subdirectory of your Visual C++ installation.  

Exception Class Hierarchy

The xalloc class defines the type of objects thrown as exceptions to report a failure to allocate
memory. This class, defined in STDEXCPT.H, is part of the exception class hierarchy. This class 
hierarchy is provided as a general framework for exception classes.
The base class for the exception object hierarchy is exception, defined in STDEXCPT.H. Note that 
the name xmsg is defined as a synonym for exception. If your code adheres to older working paper 
standards, the compiler will not generate an error if you use xmsg instead of exception. 
The hierarchy is as follows:
  
class exception
{
    ...
}
    class logic: public exception
    {
        // Defines type of objects thrown as exceptions to
        // report logic errors, such as violated preconditions
        ...
    };
        class  domain: public logic
        {
            // Base class for objects thrown as exceptions in
            // response to domain errors
            ...
        };
    class runtime: public exception
    {
        // Base class for objects thrown as exceptions in
        // response to runtime errors
        ...
    };
        class range: public runtime
        {
            // Base class for objects thrown as exceptions in
            // response to range errors
            ...
        };
        class alloc: public runtime
        {
            // Base class for objects thrown as exceptions to
            // report memory allocation failure
            ...
        };
            class xalloc: public alloc
            {
                ...
            };
    
Note   Because the xalloc exception specification in the ANSI working paper proposal is not finalized,
Microsoft does not guarantee the same implementation of the exception class hierarchy in future releases.
*/  
// CMemoryState::DumpAllObjectsSince

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

CMemNew gNew(1); //to set my handler of "new"
int CMemNew::counter=0;
// Define a function to be called if new or malloc() fails to allocate memory.
CMemNew::CMemNew(int global)
{	
	//if(AfxGetApp()==NULL) MessageBox(NULL,"This is SDI","MDI is not NULL",MB_OK);
/*	if(global && counter==0 && AfxGetApp()!=NULL){
		MessageBox(NULL,"Please add the following codes into the constructor of C..App()\n"
			"#include \"memio.h\" \n CMemNew    mem;", "Programm error in Memio.cpp ",MB_OK);
		exit(1);
	}*/
	counter++;
	// Set the failure handler for new to be NewHandler.
    _set_new_handler( NewHandler );
	_set_new_mode(1);//if malloc() fails, call newHandler
	AfxSetNewHandler(NewHandler );
}

int NewHandler( size_t size )
{
   OutOfMemory("new", size);
   return 1;//retry allocation again
}

void OutOfMemory(char * who, size_t size)
{
	MEMORYSTATUS mem;
	char msg_buf[1000];

	if(!AfxCheckMemory( )) AfxMessageBox("Memory corrupted",MB_OK);
	mem.dwLength=sizeof(MEMORYSTATUS);
    GlobalMemoryStatus(&mem);
/*
	typedef struct _MEMORYSTATUS { // mst  
    DWORD dwLength;        // 
    DWORD dwMemoryLoad;    // percent of memory in use 
    DWORD dwTotalPhys;     // bytes of physical memory 
    DWORD dwAvailPhys;     // free physical memory bytes 
    DWORD dwTotalPageFile; // bytes of paging file
    DWORD dwAvailPageFile; // free bytes of paging file 
    DWORD dwTotalVirtual;  // user bytes of address space 
    DWORD dwAvailVirtual;  // free user bytes 
} , *LPMEMORYSTATUS; 
*/
	
	wsprintf(msg_buf,"Out of memory:   %s\n"
		"\nPlease exit other programs to obtain more free memory\n"
		"Click Retry to allocate memory again\n"
	    "Click Cancel to exit this program\n"
	     "\nRequested memory = %d bytes (%d K)\n"
	     "Percent of memory in use = %d%%\n"
	     "Total physical memory = %d K\n"
		 "Free  physical memory = %d K\n"
		 "Total paging file = %d K\n"
		 "Free  paging file = %d K\n"
		 "Total user address space = %d K\n"
		 "Free  user address space = %d K\n" ,
         who,size, size/1024,
		 100-mem.dwMemoryLoad,
		 mem.dwTotalPhys/1024,
		 mem.dwAvailPhys/1024,
		 mem.dwTotalPageFile/1024,
		 mem.dwAvailPageFile/1024,
		 mem.dwTotalVirtual/1024,
		 mem.dwAvailVirtual/1024);
	
    FatalErrorMessage(msg_buf, "Memory operation error!",TRUE);
}


/****** windowsx.h: KERNEL Macro APIs ****************************************************/
//In the linear Win32 API environment, there is no difference between the local heap and the global heap. 
/*
#define     GetInstanceModule(hInstance) (HMODULE)(hInstance)

#define     GlobalPtrHandle(lp)         \
                ((HGLOBAL)GlobalHandle(lp))

#define     GlobalLockPtr(lp)                \
                ((BOOL)GlobalLock(GlobalPtrHandle(lp)))
#define     GlobalUnlockPtr(lp)      \
                GlobalUnlock(GlobalPtrHandle(lp))

#define     GlobalAllocPtr(flags, cb)        \
                (GlobalLock(GlobalAlloc((flags), (cb))))
#define     GlobalReAllocPtr(lp, cbNew, flags)       \
                (GlobalUnlockPtr(lp), GlobalLock(GlobalReAlloc(GlobalPtrHandle(lp) , (cbNew), (flags))))
#define     GlobalFreePtr(lp)                \
                (GlobalUnlockPtr(lp), (BOOL)GlobalFree(GlobalPtrHandle(lp)))
*/

LPVOID AllocMem(UINT  uFlags,	// object allocation attributes
			    DWORD  dwBytes)	// number of bytes to allocate   
{
   LPVOID p;

   while (1) {
	  if( (p=GlobalAllocPtr(uFlags, dwBytes))==NULL )
		 OutOfMemory("AllocMem", dwBytes);
	  else break;
   }
   
   return p;
}
  
LPVOID AllocMem(DWORD  dwBytes)	// number of bytes to allocate
{
   //return AllocMem(GMEM_MOVEABLE|GMEM_SHARE, dwBytes);
	return AllocMem(GHND|GMEM_SHARE, dwBytes);
}

LPVOID ReAllocMem(LPCVOID p, DWORD  dwBytes, UINT  uFlags)
{
   LPVOID q;
   while (1) {
	  if( (q=GlobalReAllocPtr(p, dwBytes, uFlags))==NULL )
		 OutOfMemory("ReAllocMem", dwBytes);
	  else break;
   }
   
   return q;
}
  
LPVOID ReAllocMem(LPCVOID p, DWORD  dwBytes)
{
   //return ReAllocMem(p, dwBytes, GMEM_MOVEABLE|GMEM_SHARE);
	return ReAllocMem(p, dwBytes, GMEM_ZEROINIT|GMEM_SHARE);
}


void FreeMEMory(void**p)
{
	if(*p==NULL) return;
	GlobalUnlockPtr(*p);
	if( (*p=GlobalFree(GlobalPtrHandle(*p)))!=NULL) AfxMessageBox("FreeMem failed");

	*p=NULL;
}

//****************************** CFile **********************************
#include <direct.h>
#include <errno.h>
/*
BOOL Alive(CString &cs, struct stat *pStat)
{
	return Alive(cs.GetBuffer(0), pStat);
}
*/
BOOL Alive(const char *path, struct stat *pStat/*=NULL*/)
{//should use .GetStatus()
   struct stat statbuf, *p;
/*
   if(access(path,0)==0){
      stat(path, &statbuf);
      if( (statbuf.st_mode & S_IFDIR) || statbuf.st_size) return 1;
   }
   */
   if(pStat==NULL) p=&statbuf;
   else p=pStat;
   memset(p,0,sizeof(statbuf));
   stat(path, p);
   if( (p->st_mode & S_IFDIR) || p->st_size) return TRUE;

   return FALSE;
}

BOOL IsDir(const char *path)
{/*  CFileStatus status;   CFile::GetStatus( path, status );    // static function
 */
   struct stat statbuf;

   if(Alive(path, &statbuf) && (statbuf.st_mode& S_IFDIR)) return TRUE;
   return FALSE;
}

BOOL MkDir(const char *path)
{
	int result;

	if(IsDir(path)) return TRUE;
retry:
	result=_mkdir(path);
/*EACCES   Directory was not created because dirname is the name of an existing file, directory, or device
  ENOENT   Path was not found */
	if(result==0) return TRUE;
	CString cs;
	cs=cs+"Can not Make Directory:\n\n "+path+"\n\n1. Path was not found."
		"\n2. Or directory is the name of an existing file or device\n";
	
	if(AfxMessageBox((LPCTSTR)cs, MB_RETRYCANCEL | MB_ICONEXCLAMATION) == IDCANCEL){
		exit(1);
	}
	goto retry;
}
/*Read or Write mode*/
// and Binary mode

BOOL LoadFile(LPCTSTR path_name, void *pData, DWORD dataLen)
{
	CCFile cf;

	if(!cf.AliveOpen(path_name)) return FALSE;
	DWORD len=(DWORD)cf.GetLength();
	if(len==0) return FALSE; //destructor will close file
	if(dataLen>len || dataLen==0) dataLen=len;
	return cf.Cread(pData,dataLen);
}

void * LoadFile(LPCTSTR path_name, int block_size, int *size)
{
	CCFile cf;

	if(!cf.AliveOpen(path_name)) return NULL;
	DWORD len=(DWORD)cf.GetLength();
	if(size!=NULL) *size=len/block_size;
	if(len==0) return NULL; //destructor will close file
	char *pData;
	pData=new char[len];
	if(!cf.Cread(pData,len)) {
		delete[] pData;
		return NULL;
	}
	return (void *) pData;
}

BOOL SaveFile(LPCTSTR path_name, void *pData, DWORD dataLen)
{
	CCFile cf;

	if(dataLen==0) return FALSE;
	if(!cf.KillOpen(path_name)) return FALSE;
	return cf.Cwrite(pData,dataLen);
}


BOOL CCFile::AliveOpen(LPCTSTR path_name) 
{
   return Copen(path_name,CFile::typeBinary|CFile::modeCreate|
	            CFile::modeNoTruncate|CFile::modeReadWrite);
}

BOOL CCFile::KillOpen(LPCTSTR path_name) 
{
   return Copen(path_name,
	   CFile::typeBinary|CFile::modeReadWrite|CFile::modeCreate);
}

LONG CCFile::Cseek(LONG off)
{
	return (LONG)CFile::Seek(off,CFile::begin);
}

BOOL CCFile::Copen(LPCTSTR path_name,UINT mode)
{
  CFileException e;

  //mode |= CFile::modeCreate; ==> this will always create a file,
  //if the file does not exist, it create too!

  //!!! need to close() first
  //if(m_hFile != CFile::hFileNull) Cclose();

  Cclose();
  if(!Open(path_name,mode,&e)) {
     FileError(&e,"Can not open file");
     return FALSE;//this code has no effect: becz FileError() does not return
  }
  return TRUE;
}

BOOL CCFile::Cread(void *buffer,UINT nCount)
{
  CFileException e;
  UINT total;

  if(buffer==NULL) {
	  FileError(&e,"NULL buffer pointer: Cread()");
	  return FALSE;
  }
  
  if(nCount==0){
	  FileError(&e,"Cread size is zero");
	  return FALSE;
  }

  TRY
  {
    total=Read(buffer,nCount);//throw( CFileException );
  }
  CATCH( CFileException, e )//e is a POINTER!!!
  {//CATCH( exception_class, exception_object_pointer_name )
	FileError(e,"Read");	  
	return FALSE;
  }
  END_CATCH

 if(total!=nCount){
     FileError(&e,"Read Lost or Read Error");
	 return FALSE;
  }
  return TRUE;
}

BOOL CCFile::GetLine(CString &cs) //remove CRLF
{
	DWORD len=(DWORD)GetLength();
	UCHAR c,prev_c;

    cs.Empty();
	prev_c=0;
	if(GetPosition()>=len) return FALSE;///1997.12.5
	while(1){
	   if(GetPosition()>=len) break;
       if( !Cread(&c,1) ) return FALSE;
	   if(prev_c=='\r'){
		   if(c=='\n') break;
		   cs+='\r';
	   }
	   if(c!='\r') cs += c;
	   prev_c = c;
	}
	return TRUE;
}

BOOL CCFile::PutLine(CString &cs) //append CRLF
{
	DWORD len;
	
	len=cs.GetLength();
	if(len && !CCFile::Cwrite((LPCSTR)cs,len)) return FALSE;
	return CCFile::Cwrite("\r\n",2);
}

BOOL CCFile::PutLine(LPCTSTR p) //append CRLF
{
	CString cs(p);
	return PutLine(cs);	
}

void CCFile::fprintf(const char *pFormat, ...)
{
	va_list  argptr;
	char szBuffer[1500]; 
	int count;

	va_start( argptr,pFormat);
	count=vsprintf(szBuffer, pFormat, argptr); //note: size of szBuffer maybe too small
    if(count<=0) {
		AfxMessageBox("CCFile fprintf error: count=vsprintf() <=0");
	} else {
		Cwrite(szBuffer,count);
	}
	va_end( argptr );
}

BOOL CCFile::Cwrite(const void *buffer,UINT nCount)
{
  CFileException e;

  if(buffer==NULL) {
	  FileError(&e,"NULL buffer pointer:Cwrite");
	  return FALSE;
  }
  
  if(nCount==0){
	 FileError(&e,"Cwrite Size Zero");
	 return FALSE;
  }
  TRY
  {
    Write(buffer,nCount);
  }
  CATCH( CFileException, e )
  {
	FileError(e,"Write");
	return FALSE;
  }
  END_CATCH

  return TRUE;
}

BOOL CCFile::Cclose(void)
{
  TRY
  {
    if(m_hFile != CFile::hFileNull) Close();
	else return TRUE;
  }
  CATCH( CFileException, e )
  {
	FileError(e,"Close");
	return FALSE;
  }
  END_CATCH
  return TRUE;
}

void CCFile::FileError(CFileException *e, LPCTSTR s)
{
	CString cs;
    
	cs=GetFilePath()+"\n"; //
    if(e!=NULL) switch(e->m_cause){
       case CFileException::none :   
		   cs +="Maybe no error occurred."; 
		   break;
	   case CFileException::generic :
		   cs +="An unspecified error occurred." ;
		   break;
	   case CFileException::fileNotFound :
		   cs +="The file could not be located/found.";
		   break;
	   case CFileException::badPath :
		   cs +="All or part of the path is invalid.";
		   break;
	   case CFileException::tooManyOpenFiles:
		   cs +="The permitted number of open files was exceeded.";
		   break;
	   case CFileException::accessDenied:   
		   cs +="The file could not be accessed.";
		   break;
	   case CFileException::invalidFile:
		   cs +="There was an attempt to use an invalid file handle.";
           break;
	   case CFileException::removeCurrentDir:
		   cs +="The current working directory cannot be removed.";
		   break;
	   case CFileException::directoryFull:
		   cs +="There are no more directory entries.";
		   break;
	   case CFileException::badSeek:
		   cs +="There was an error trying to set the file pointer.";
		   break;
	   case CFileException::hardIO :
		   cs +="There was a hardware error.";
		   break;
	   case CFileException::sharingViolation :
		   cs +="SHARE.EXE was not loaded, or a shared region was locked.";
		   break;
	   case CFileException::lockViolation :
		   cs +="There was an attempt to lock a region that was already locked.";
		   break;
	   case CFileException::diskFull:
		   cs +="The disk is full.";
		   break;
	   case CFileException::endOfFile :
		   cs +="The end of file was reached.";
		   break;
	   default:
		   cs +="Unknown CFileException";
		   break;
	}
    if(s!=NULL) cs = cs+"\n"+s+'!';
	FatalErrorMessage(cs, "File operation error!");
}  

void FatalErrorMessage(CString csText, char *pTitle,BOOL bRetry)
{
	 int i;
	 MessageBeep(0xFFFFFFFF);
	 MessageBeep(0);
	 while(1){
		 i=::MessageBox(NULL,(LPCTSTR)csText,pTitle,
		                       MB_ABORTRETRYIGNORE | MB_ICONSTOP);
		 if(i==IDABORT) exit(1);
		 if(i==IDRETRY && bRetry) return;
		 //IDIGNORE is not allowed
	 }
}

//String operation
void TrimLR(CString &cs)
{
	cs.TrimLeft();
	cs.TrimRight();
}