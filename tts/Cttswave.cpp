 // cwave.cpp : implementation file
// (for visual c++ 4.0)

//not check error in Record part

#include "stdafx.h"

#include <windowsx.h>

#include <memory.h>
#include "cttswave.h"
//顯示波形用
#include "cwave.h"
#include "memio.h"
//#include "win95.h"
//#include "rec.h"
//#include <resource.h>
//#include "d:\vctts\tcinfodb\tcinfodb.h"
//#include "\vctts\vctts\vctts.h"
#ifdef _DEBUG
#undef THIS_FILE
static char BASED_CODE THIS_FILE[] = __FILE__;
#endif

void CPU();
/////////////////////////////////////////////////////////////////////////////
// CTTSWave

CTTSWave::CTTSWave(CWnd* pParentWnd,int sizeByte,DWORD nRate, WORD wBit, WORD nChannel) : BUFFER_SIZE2(sizeByte)
{
	m_state = CWAVE_NONE;
	if( (m_pParentWnd=pParentWnd) !=NULL) {
	   	Create();
    }
	m_dwByte = 0;
	m_pData = (BYTE*)AllocMem(sizeof(BYTE));
	m_pData_Temp = (BYTE*)AllocMem(sizeof(BYTE));
	m_pWaveHdr1 = (LPWAVEHDR)AllocMem(sizeof(WAVEHDR));
	m_pWaveHdr2 = (LPWAVEHDR)AllocMem(sizeof(WAVEHDR));
	m_pWaveHdr3 = (LPWAVEHDR)AllocMem(sizeof(WAVEHDR));
	
	SetWaveFormat(nRate, wBit, nChannel);
	count =0;
}

CTTSWave::~CTTSWave()
{
	if(m_state != CWAVE_NONE){
	   Terminate();
	}
	DestroyWindow();

	FreeMem(m_pWaveHdr1);
	FreeMem(m_pWaveHdr2);
	FreeMem(m_pWaveHdr3);
	FreeMem(m_pData_Temp);
	FreeMem(m_pData);
}

void CTTSWave::Create()
{
	if(m_hWnd) return;
    RECT rect;
	rect.left	= rect.top	=0;
	rect.right	= rect.bottom =100;
    
	if(m_pParentWnd==NULL) m_pParentWnd=AfxGetMainWnd();//::GetDesktopWindow(); Attach()
	if(m_pParentWnd!=NULL) {
		CWnd::Create(NULL,NULL,WS_OVERLAPPEDWINDOW,rect,m_pParentWnd,0);
	}
/*
    if (!m_wndToolBar.Create(m_pParentWnd) ||
		!m_wndToolBar.LoadToolBar(IDR_MAINFRAME1))
	{
		TRACE0("Failed to create toolbar\n");
		//return -1;      // fail to create
	}

	// TODO: Remove this if you don't want tool tips or a resizeable toolbar
	m_wndToolBar.SetBarStyle(m_wndToolBar.GetBarStyle() |
		CBRS_TOOLTIPS | CBRS_FLYBY | CBRS_SIZE_DYNAMIC);

	// TODO: Delete these three lines if you don't want the toolbar to
	//  be dockable
	*/
	/*
	m_wndToolBar.EnableDocking(CBRS_ALIGN_ANY);

	m_wndToolBar.EnableDocking(CBRS_ALIGN_ANY);
	//((CView*)m_pParentWnd)->m_wndToolBar.DockControlBar(&m_wndToolBar);
*/
}

/* mmsystem.h
typedef struct waveformat_tag {
    WORD    wFormatTag;        // format type 
    WORD    nChannels;         // number of channels (i.e. mono, stereo, etc.) 
    DWORD   nSamplesPerSec;    // sample rate
    DWORD   nAvgBytesPerSec;   // for buffer estimation
    WORD    nBlockAlign;       // block size of data 
} WAVEFORMAT, *PWAVEFORMAT, NEAR *NPWAVEFORMAT, FAR *LPWAVEFORMAT;
*/
/* flags for wFormatTag field of WAVEFORMAT */
//#define WAVE_FORMAT_PCM     1

/* specific waveform format structure for PCM data */
/*
typedef struct pcmwaveformat_tag {
    WAVEFORMAT  wf;
    WORD        wBitsPerSample;
} PCMWAVEFORMAT, *PPCMWAVEFORMAT, NEAR *NPPCMWAVEFORMAT, FAR *LPPCMWAVEFORMAT;
*/
void CTTSWave::SetWaveFormat(DWORD nSamplesPerSec, WORD wBitsPerSample, WORD nChannel)
{
    if( (wBitsPerSample & 7) != 0 ) {
		AfxMessageBox("wBitsPerSample is not divided by 8: program will exit.");
		exit(1);
	}
	m_pcm.wBitsPerSample	= wBitsPerSample;
	m_pcm.wf.wFormatTag		= WAVE_FORMAT_PCM;
	m_pcm.wf.nChannels		= nChannel;
	m_pcm.wf.nSamplesPerSec = nSamplesPerSec;
	m_pcm.wf.nBlockAlign	= (short)((wBitsPerSample/8) * nChannel);
	m_pcm.wf.nAvgBytesPerSec= m_pcm.wf.nBlockAlign * nSamplesPerSec;
}

void CTTSWave::SetSamplingRate(int rate)
{
	SetWaveFormat(rate);
}

void CTTSWave::SetWaveFormat(PCMWAVEFORMAT pcm)
{
	m_pcm = pcm;
}

void CTTSWave::ExtractWave(DWORD beg, DWORD end)
{
	DWORD len=(end-beg)*m_pcm.wf.nBlockAlign;

	if(len==0 || len>=m_dwByte) {
		AfxMessageBox("ExtractWave error():len==0 || len>=m_dwByte");
		return;
	}
	memmove(m_pData, m_pData+(beg*m_pcm.wf.nBlockAlign),len);
	m_pData = (BYTE*)ReAllocMem(m_pData,len);
	m_dwByte=len;
}

void CTTSWave::SetWave(PCMWAVEFORMAT pcm, BYTE* pData,DWORD dwByte)
{
	if(/*m_hWnd==NULL || maybe h_hWnd is NULL in constructor*/dwByte==0 || pData==NULL)
		return;

	m_dwByte = dwByte;
	m_pData = (BYTE*)ReAllocMem(m_pData,m_dwByte);
	memcpy(m_pData,pData,dwByte);
	m_pcm = pcm;
}

void CTTSWave::SetWave(BYTE* pData,DWORD dwByte)
{
	SetWave(m_pcm, pData, dwByte);
}


BEGIN_MESSAGE_MAP(CTTSWave, CWnd)
	//{{AFX_MSG_MAP(CTTSWave)
	ON_MESSAGE(MM_WOM_OPEN, OnMmWomOpen)
	ON_MESSAGE(MM_WOM_CLOSE,OnMmWomClose)
	ON_MESSAGE(MM_WOM_DONE, OnMmWomDone)
	ON_MESSAGE(MM_WIM_OPEN, OnMmWimOpen)
	ON_MESSAGE(MM_WIM_CLOSE,OnMmWimClose)
	ON_MESSAGE(MM_WIM_DATA, OnMmWimData)
	ON_WM_VSCROLL()
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CTTSWave message handlers
// UINT wParam should be changed to "DWORD wParam"
int CTTSWave::Terminate()
{
	int state;

	if(this==NULL) return CWAVE_NONE;
	state=m_state; //backup m_state: because RecordEnd and PlayEnd will reset it

    if(m_state == CWAVE_RECORD) {
	   RecordEnd();
	} else if(m_state == CWAVE_PLAY) {
       PlayEnd();
	}
	return state;
}
/* 會重社DATA ==> BUG ?
BOOL CTTSWave::Play(void *pData,DWORD dwSample, BOOL bAsync)
{
	//should use sample instead of dwByte
	DWORD dwByte=dwSample*m_pcm.wf.nBlockAlign;
	Create();
	if(m_hWnd==NULL || dwByte==0 || pData==NULL)
		return FALSE;

	m_dwByte = dwByte;
	m_pData = (BYTE*)ReAllocMem(m_pData,m_dwByte);
	memcpy(m_pData,pData,dwByte);
	return Play(bAsync);
}
*/
BOOL CTTSWave::Play(BOOL bAsync)
{
	return PlayPart(0,Samples(),bAsync);
}

BOOL CTTSWave::PlayPart(DWORD begin,DWORD end,BOOL bAsync)
{

	PlayEnd();
	while(IsBusy());
	if(end==0 || end>Samples()) end=Bytes();
	bytes=(end-begin)*m_pcm.wf.nBlockAlign;
	Part_begin = begin;
	//輸出單元波形
	Part_bytes = bytes;
	
	m_bAsync=bAsync;
	Create();
	if(begin>end || m_state!=CWAVE_NONE || m_hWnd==NULL || bytes==0 /*|| m_pData==NULL*/)
		return FALSE;

	// generate MM_WOM_OPEN 
	if( 0!=(m_errorNo=waveOutOpen(&m_hWaveOut,WAVE_MAPPER,(LPWAVEFORMATEX)&m_pcm.wf ,(DWORD)m_hWnd,0L,CALLBACK_WINDOW))) {
		WaveOutError();
		return FALSE;
	}

	memset(m_pWaveHdr1, 0, sizeof(WAVEHDR));
	
	m_pWaveHdr1->lpData			= (LPSTR)&m_pData[m_pcm.wf.nBlockAlign*begin]; //if UNICODE: LPBYTE
	m_pWaveHdr1->dwBufferLength	= bytes;;
	//m_pWaveHdr1->dwBytesRecorded = 0;
	//m_pWaveHdr1->dwUser			= 0;
	m_pWaveHdr1->dwFlags		= WHDR_BEGINLOOP|WHDR_ENDLOOP;
	m_pWaveHdr1->dwLoops		= 1; // How many times to repeat playing.
	//m_pWaveHdr1->lpNext			= NULL;
	//m_pWaveHdr1->reserved		= 0;


//	memset(m_pWaveHdr3, 0, sizeof(WAVEHDR));
//	memcpy(m_pWaveHdr3 , m_pWaveHdr1 , sizeof(WAVEHDR));
	
	if((m_errorNo=waveOutPrepareHeader(m_hWaveOut,m_pWaveHdr1,sizeof(WAVEHDR)))!=MMSYSERR_NOERROR){
       WaveOutError();
       return FALSE;
	}

	m_state = CWAVE_PLAY;
	Convert2SignedChar();
	if( (m_errorNo=waveOutWrite(m_hWaveOut,m_pWaveHdr1,sizeof(WAVEHDR)))!=MMSYSERR_NOERROR){
	   WaveOutError();
	   Convert2SignedChar();
       return FALSE;
	}
	if(!bAsync) {
       while(m_state == CWAVE_PLAY && m_errorNo==0) CPU();
	   Convert2SignedChar();
	}
	return TRUE;
}

void CTTSWave::PlayEnd()
{
	if(m_state!=CWAVE_PLAY) return;
	if(0!=(m_errorNo=waveOutReset(m_hWaveOut))){
	   WaveOutError();
	} else { 
       waveOutMessage(m_hWaveOut, MM_WOM_DONE, (ULONG)m_hWaveOut, (ULONG)m_pWaveHdr1);
	   if(m_bAsync) Convert2SignedChar();
	}
	//m_state=CWAVE_NONE; //bug
}

LONG CTTSWave::OnMmWomOpen(UINT wParam,LONG lParam)
{
	return 0L;
}

LONG CTTSWave::OnMmWomDone(UINT wParam,LONG lParam)
{//Note: PlayEnd call this too.
 
 /* For double buffer:

	static int i=0;
	if(++i<2){
	  waveOutWrite(m_hWaveOut,m_pWaveHdr1,sizeof(WAVEHDR));
		  return 0;
    }
*/
	if( 0!=(m_errorNo=waveOutUnprepareHeader((HWAVEOUT)wParam,(LPWAVEHDR)lParam,sizeof(WAVEHDR)))){
	   WaveOutError();
       return 0L;
	}
	if(0!=(m_errorNo=waveOutClose((HWAVEOUT)wParam))){
	   WaveOutError();
       return 0L;
	}
   	//m_state=CWAVE_NONE;//!!!!!	why bug  1997.3.24 :not close: if open again==>error! ???
	return 0L;
}

LONG CTTSWave::OnMmWomClose(UINT wPAram,LONG lParam)
{
	m_state=CWAVE_NONE;
	//AfxMessageBox("close");
	//MessageBeep(0);
	return 0L;
}

static long w_len[100];
static int len_cnt=0;
LONG CTTSWave::OnMmWimOpen(UINT wParam,LONG lParam)
{
	m_state=CWAVE_RECORD;
len_cnt=0;
	m_pData = (BYTE*)GlobalReAllocPtr(m_pData,1,GHND);

	m_pWaveHdr1->lpData			= (LPSTR)m_pBuffer1;
	m_pWaveHdr1->dwBufferLength = BUFFER_SIZE2;
	m_pWaveHdr1->dwBytesRecorded= 0L;
	m_pWaveHdr1->dwUser			= 0L;
	m_pWaveHdr1->dwFlags		= 0L;
	m_pWaveHdr1->dwLoops		= 1L;
	m_pWaveHdr1->lpNext			= NULL;
	m_pWaveHdr1->reserved		= 0L;
	waveInPrepareHeader(m_hWaveIn,m_pWaveHdr1,sizeof(WAVEHDR));
	waveInAddBuffer(m_hWaveIn,m_pWaveHdr1,sizeof(WAVEHDR));
	
	m_pWaveHdr2->lpData			= (LPSTR)m_pBuffer2;
	m_pWaveHdr2->dwBufferLength = BUFFER_SIZE2;
	m_pWaveHdr2->dwBytesRecorded= 0L;
	m_pWaveHdr2->dwUser			= 0L;
	m_pWaveHdr2->dwFlags		= 0L;
	m_pWaveHdr2->dwLoops		= 1L;
	m_pWaveHdr2->lpNext			= NULL;
	m_pWaveHdr2->reserved		= 0L;
	waveInPrepareHeader(m_hWaveIn,m_pWaveHdr2,sizeof(WAVEHDR));
	waveInAddBuffer(m_hWaveIn,m_pWaveHdr2,sizeof(WAVEHDR));

	m_dwByte = 0;
	waveInStart(m_hWaveIn);
	return 0L;
}

LONG CTTSWave::OnMmWimData(UINT wParam,LONG lParam)
{
	LPWAVEHDR pWaveHdr=(LPWAVEHDR)lParam;
	m_pData=(BYTE*)GlobalReAllocPtr(m_pData,m_dwByte+pWaveHdr->dwBytesRecorded,GMEM_MOVEABLE | GMEM_SHARE);
	memcpy((BYTE*)(m_pData+m_dwByte),pWaveHdr->lpData,pWaveHdr->dwBytesRecorded);
	m_dwByte+=pWaveHdr->dwBytesRecorded;
w_len[len_cnt++]=pWaveHdr->dwBytesRecorded;
w_len[len_cnt++]=m_dwByte;
	pWaveHdr->dwBufferLength	= BUFFER_SIZE2;
	pWaveHdr->dwBytesRecorded	= 0L;
	pWaveHdr->dwUser			= 0L;
	pWaveHdr->dwFlags			= 0L;
	waveInAddBuffer(m_hWaveIn,pWaveHdr,sizeof(WAVEHDR));
	//Convert2SignedChar();??
	return 0L;
}

LONG CTTSWave::OnMmWimClose(UINT wPAram,LONG lParam)
{
	m_state=CWAVE_NONE;

	waveInUnprepareHeader(m_hWaveIn,m_pWaveHdr1,sizeof(WAVEHDR));
	waveInUnprepareHeader(m_hWaveIn,m_pWaveHdr2,sizeof(WAVEHDR));
	GlobalFreePtr(m_pBuffer1);
	GlobalFreePtr(m_pBuffer2);
	return 0L;
}

#include "memio.h"
BOOL CTTSWave::Record()
{
	Create();
    //AfxMessageBox("try\nfsdafhdas",MB_RETRY);
	if(m_state!=CWAVE_NONE) return FALSE;
	if(m_hWnd==NULL) return FALSE;
//	m_pcm=pcm;
	m_pBuffer1=(BYTE*)GlobalAllocPtr(GMEM_MOVEABLE | GMEM_SHARE,BUFFER_SIZE2);
	m_pBuffer2=(BYTE*)GlobalAllocPtr(GMEM_MOVEABLE | GMEM_SHARE,BUFFER_SIZE2);
	if(0!=(m_errorNo=waveInOpen(&m_hWaveIn,WAVE_MAPPER,(LPWAVEFORMATEX)&m_pcm.wf,(DWORD)m_hWnd,0L,CALLBACK_WINDOW)))
	{
		GlobalFreePtr(m_pBuffer1);
		GlobalFreePtr(m_pBuffer2);
		return FALSE;
	}
	m_state=CWAVE_RECORD; //1997 3.24: 
	while(m_state == CWAVE_RECORD && m_errorNo==0) CPU();
	//Convert2SignedChar();??
	return TRUE;
}

void CTTSWave::RecordEnd()
{
	if(m_state!=CWAVE_RECORD) return;
/*char s[200];
long r1,r2,e1,e2;
    r1=m_pWaveHdr1->dwBytesRecorded;
	r2=m_pWaveHdr2->dwBytesRecorded;*/
	waveInStop(m_hWaveIn);
	waveInReset(m_hWaveIn);
	waveInClose(m_hWaveIn);
	
	m_state=CWAVE_NONE;
	Convert2SignedChar();
/*
	e1=m_pWaveHdr1->dwBytesRecorded;
	e2=m_pWaveHdr2->dwBytesRecorded;
	sprintf(s,"%lu, %ld, %ld:",m_dwByte,e1,e2);
	for(int i=0; i<len_cnt;i++){
		sprintf(s+150,",%ld",w_len[i]);
	    strcat(s,s+150);
    }
    MessageBox(s,s);
e1=m_pWaveHdr1->dwBytesRecorded;
	e2=m_pWaveHdr2->dwBytesRecorded;
	sprintf(s,"%lu, %ld, %ld:",m_dwByte,e1,e2);
for(i=0; i<len_cnt;i++){
		sprintf(s+150,",%ld",w_len[i]);
	    strcat(s,s+150);
    }
		
		MessageBox(s,s);*/
}

BOOL CTTSWave::Read(CString &cs)
{
	return Read(cs.GetBuffer(0));
}

BOOL CTTSWave::Write(CString &cs)
{
	return Write(cs.GetBuffer(0));
}

BOOL CTTSWave::Read(char* filename)
{
	HMMIO hmmio;
	MMCKINFO ckRIFF,ckInfo;

	if(m_state!=CWAVE_NONE) return FALSE;
	hmmio=mmioOpen(filename,NULL,MMIO_READ);
	if(hmmio==NULL) return FALSE;
	if(mmioDescend(hmmio,&ckRIFF,NULL,0))
	{
		mmioClose(hmmio,0);
		return FALSE;
	}
	if(ckRIFF.ckid!=FOURCC_RIFF || (ckRIFF.fccType!=mmioFOURCC('W','A','V','E')))
	{
		mmioClose(hmmio,0);
		return FALSE;
	}
	// 'fmt ' chunk
	ckInfo.ckid=mmioFOURCC('f','m','t',' ');
	if(!mmioDescend(hmmio,&ckInfo,&ckRIFF,MMIO_FINDCHUNK))
	{
		if(ckInfo.cksize>=(long)sizeof(PCMWAVEFORMAT))
		{
			if(mmioRead(hmmio,(LPSTR)&m_pcm,sizeof(PCMWAVEFORMAT))==sizeof(PCMWAVEFORMAT))
			{
				if(mmioAscend(hmmio,&ckInfo,0)) { mmioClose(hmmio,0); return FALSE; }
			}
			else { mmioClose(hmmio,0); return FALSE; }
		}
		else { mmioClose(hmmio,0); return FALSE; }
	}
	else { mmioClose(hmmio,0); return FALSE; } 
	// 'data' chunk
	ckInfo.ckid=mmioFOURCC('d','a','t','a');
	if(!mmioDescend(hmmio,&ckInfo,&ckRIFF,MMIO_FINDCHUNK))
	{
		m_dwByte=ckInfo.cksize;
		m_pData=(BYTE*)GlobalReAllocPtr(m_pData,m_dwByte,GHND);
		if(mmioRead(hmmio,(LPSTR)m_pData,m_dwByte)!=(long)m_dwByte)
		{
			mmioClose(hmmio,0); return FALSE;
		}
	}
	else { mmioClose(hmmio,0); return FALSE; }
	mmioClose(hmmio,0);
	Convert2SignedChar();
	return TRUE;
}

// should show error message
BOOL CTTSWave::Write(char* filename)
{
	HMMIO hmmio;
	MMCKINFO ckRIFF,ckInfo;

	if(m_dwByte<20) return FALSE;
	if(m_state!=CWAVE_NONE) return FALSE;
//	if(m_dwByte==0) return FALSE;
	hmmio=mmioOpen(filename,NULL,MMIO_WRITE|MMIO_CREATE);
	if(hmmio==NULL) return FALSE;
	memset(&ckRIFF,0,sizeof(MMCKINFO));
	ckRIFF.fccType=mmioFOURCC('W','A','V','E');
	if(mmioCreateChunk(hmmio,&ckRIFF,MMIO_CREATERIFF)!=0)
	{
		mmioClose(hmmio,0);	return FALSE;
	}
	// 'fmt ' chunk
	memset(&ckInfo,0,sizeof(MMCKINFO));
	ckInfo.ckid=mmioFOURCC('f','m','t',' ');
	if(!mmioCreateChunk(hmmio,&ckInfo,0))
	{
		if(mmioWrite(hmmio,(LPSTR)&m_pcm,sizeof(PCMWAVEFORMAT))==sizeof(PCMWAVEFORMAT))
		{
			if(mmioAscend(hmmio,&ckInfo,0)) { mmioClose(hmmio,0); return FALSE; }
		}
		else { mmioClose(hmmio,0); return FALSE; }
	}
	else { mmioClose(hmmio,0); return FALSE; }
	// 'data' chunk
	memset(&ckInfo,0,sizeof(MMCKINFO));
	ckInfo.ckid=mmioFOURCC('d','a','t','a');
	if(!mmioCreateChunk(hmmio,&ckInfo,0))
	{
		Convert2SignedChar();
		if(mmioWrite(hmmio,(LPSTR)m_pData,m_dwByte)==(long)m_dwByte)
//		if(mmioWrite(hmmio,(LPSTR)m_pWaveHdr1->lpData,bytes)==(long)bytes)
		//if(mmioWrite(hmmio,(LPSTR)Part_pData,Part_bytes)==(long)Part_bytes)
		{
			Convert2SignedChar();
			if(mmioAscend(hmmio,&ckInfo,0)) { 
				mmioClose(hmmio,0); 
				return FALSE; 
			}
		}
		else { 
			Convert2SignedChar();
			mmioClose(hmmio,0); 
			return FALSE; 
		}
	}
	else { mmioClose(hmmio,0); return FALSE; }

	if(mmioAscend(hmmio,&ckRIFF,0)) { mmioClose(hmmio,0); return FALSE; }
	mmioClose(hmmio,0);
	return TRUE;
}
/*
void CTTSWave::OnVScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar) 
{
	// TODO: Add your message handler code here and/or call default
	
	CWnd::OnVScroll(nSBCode, nPos, pScrollBar);
}
*/

void CTTSWave::WaveOutError()
{
	char msg[MAXERRORLENGTH+1];

	DWORD err=GetLastError();
	waveOutGetErrorText(m_errorNo, msg, MAXERRORLENGTH);
	AfxMessageBox(msg);
}

BOOL CTTSWave::IsBusy()
{
/* This is very important to release CPU. 
   If not, the last WM_WAVE_DONE and WM_WAVE_CLOSE is not Dispatched !!!*/
	if(this==NULL) return FALSE;
	MSG msg;

	if (::PeekMessage(&msg, NULL, 0, 0, PM_REMOVE))	{
		::TranslateMessage(&msg);
		::DispatchMessage(&msg);
	}

	if(m_state!=CWAVE_NONE) return TRUE;
	return FALSE;
}

DWORD CTTSWave::Bytes() 
{ 
	return m_dwByte;
}

DWORD CTTSWave::Samples() 
{ 
	return m_dwByte/m_pcm.wf.nBlockAlign;
} // ??m_pcm.wf.nBlockAlign include channel 

BYTE* CTTSWave::GetBuffer() 
{ 
	return m_pData;
}

short* CTTSWave::ShortBuffer(int begin/*=0*/)
{
	short *s;

	s=(short*)m_pData;
	return &s[begin];
}

void CTTSWave::Convert2SignedChar()
{
	if(m_pcm.wBitsPerSample !=8 ) return;

	for(UINT i=0; i<m_dwByte; i++) {
//	for(UINT i=0; i<Part_bytes; i++) {
		m_pData[i] -= 128;
	}
}

void CTTSWave::Append(void *pData,int samples)
{
	if(samples<=0) return;
	DWORD dwBytes,prevByte;
	dwBytes=samples*m_pcm.wf.nBlockAlign;
	prevByte=m_dwByte;
	m_dwByte += dwBytes;
	//prevByte=old_dwByte;
	//old_dwByte += dwBytes;
	//m_pData = (BYTE*)ReAllocMem(m_pData,old_dwByte);
	m_pData = (BYTE*)ReAllocMem(m_pData,m_dwByte);
	memcpy(m_pData+prevByte,pData,dwBytes);
}

void CTTSWave::Reset()
{
	m_dwByte =0 ;
	m_pData = (BYTE*)ReAllocMem(m_pData,1);
}

struct WAVE_HEADER_TEMP {
   char RIFFtag[4];
   long file_len_8;
   char WAVEtag[4];
   char fmt_tag[4];
   long  CtrlSize;
   short wFormatTag;
   short channel;
   long  sample_rate;
   long  avg_byte_sec;
   short block_align;
   short bit_per_sample;
   char  DATAtag[4];
   long  wave_byte;
} ;

//這個Read()方法不是很好，主要是檔頭大小一定是44 BYTES的簡單格式.

BOOL CTTSWave::Read(LPCTSTR filename, int begin, int sample)
{
	CCFile cf;
	WAVE_HEADER_TEMP hd;
	char str[200];
//f=fopen(filename,"rb");
	if(begin<0 || sample <=0) return FALSE;
	cf.Copen( filename);
	cf.Cread(&hd,sizeof(WAVE_HEADER_TEMP));
	SetWaveFormat(hd.sample_rate, hd.bit_per_sample, hd.channel);
	begin*=m_pcm.wf.nBlockAlign;
	m_dwByte=sample*m_pcm.wf.nBlockAlign;
	if((UINT)begin+m_dwByte>(UINT)hd.wave_byte) {
		sprintf(str,"Wave read error: %s\nbegin(%d)+bytes(%d) > wave_size(%d)",
			LPCTSTR(filename),begin, m_dwByte,hd.wave_byte);
		AfxMessageBox(str);
		/*if(begin<hd.wave_byte)
		elsebegin=0;*/
		return FALSE;
	}	
	m_pData=(BYTE*)GlobalReAllocPtr(m_pData,m_dwByte,GHND);
	cf.Seek(begin,CFile::current);
    cf.Cread(m_pData, m_dwByte) ;
	cf.Cclose();
	Convert2SignedChar();
	return TRUE;
}
	

// type = TIME_MS TIME_SAMPLES TIME_BYTES TIME_SMPTE TIME_MIDI 

BOOL CTTSWave::GetPosition(int &pos, UINT type, BYTE *smpte)
{
	UINT waveError;
	static MMTIME ad_mmtime;
	
	ad_mmtime.wType = type;
	waveError= waveOutGetPosition(m_hWaveOut, (LPMMTIME)&ad_mmtime, sizeof(ad_mmtime));
	if (waveError) {
		char text[1000];
		waveOutGetErrorText(waveError,text,sizeof(text)-1);
		return FALSE;
		/*return WaveOutError("GetPlayProgress: waveoutGetPosition", waverr);*/
	}
	switch (ad_mmtime.wType)
	{
		case TIME_MS: pos = ad_mmtime.u.ms; break;
        case TIME_SAMPLES: pos = ad_mmtime.u.sample; break;
		case TIME_BYTES: pos = ad_mmtime.u.cb; break;
		case TIME_SMPTE: smpte = &ad_mmtime.u.smpte.hour; break;// smpte = BYTE[5] = [hour, min, sec, frame, fps]
		case TIME_MIDI: pos = ad_mmtime.u.midi.songptrpos; break;
		default: pos=0;
	}
	if (ad_mmtime.wType != type)
	{
		//TRACE("GetPlayProgress: ad_mmtime.wType %u != type %u\n", ad_mmtime.wType, type);
		//return MMSYSERR_NOTSUPPORTED;
		return FALSE;
	}
	//TRACE("GetPosition ok\n");
   return TRUE;
}                     


void CPU()
{
	MSG msg;
	if (::PeekMessage(&msg, NULL, 0, 0, PM_REMOVE))
	{
		::TranslateMessage(&msg);
		::DispatchMessage(&msg);
	}
}	

BYTE* CTTSWave::GetPartBuffer()
{
	Part_pData = (BYTE*)malloc(Part_bytes);

	memcpy(Part_pData,&m_pData[m_pcm.wf.nBlockAlign*Part_begin],Part_bytes);
	if(count ==0){
		memcpy(m_pData ,&Part_pData[0],Part_bytes);
		count++;
	}
	return Part_pData;
}

BOOL CTTSWave::SetPart(DWORD PartBegin, DWORD PartEnd)
{
	DWORD bytes;

	PlayEnd();
	while(IsBusy());
	if(PartEnd==0 || PartEnd>Samples()) PartEnd=Bytes();
	bytes=(PartEnd-PartBegin)*m_pcm.wf.nBlockAlign;
	Part_begin = PartBegin;
	//輸出單元波形
	Part_bytes = bytes;
	if(count==0){
		old_dwByte = bytes;
		count++;
	}

	return TRUE;
}

BOOL CTTSWave::WritePart(char *filename)
{
	HMMIO hmmio;
	MMCKINFO ckRIFF,ckInfo;

	if(m_dwByte<20) return FALSE;
	if(m_state!=CWAVE_NONE) return FALSE;
//	if(m_dwByte==0) return FALSE;
	hmmio=mmioOpen(filename,NULL,MMIO_WRITE|MMIO_CREATE);
	if(hmmio==NULL) return FALSE;
	memset(&ckRIFF,0,sizeof(MMCKINFO));
	ckRIFF.fccType=mmioFOURCC('W','A','V','E');
	if(mmioCreateChunk(hmmio,&ckRIFF,MMIO_CREATERIFF)!=0)
	{
		mmioClose(hmmio,0);	return FALSE;
	}
	// 'fmt ' chunk
	memset(&ckInfo,0,sizeof(MMCKINFO));
	ckInfo.ckid=mmioFOURCC('f','m','t',' ');
	if(!mmioCreateChunk(hmmio,&ckInfo,0))
	{
		if(mmioWrite(hmmio,(LPSTR)&m_pcm,sizeof(PCMWAVEFORMAT))==sizeof(PCMWAVEFORMAT))
		{
			if(mmioAscend(hmmio,&ckInfo,0)) { mmioClose(hmmio,0); return FALSE; }
		}
		else { mmioClose(hmmio,0); return FALSE; }
	}
	else { mmioClose(hmmio,0); return FALSE; }
	// 'data' chunk
	memset(&ckInfo,0,sizeof(MMCKINFO));
	ckInfo.ckid=mmioFOURCC('d','a','t','a');
	if(!mmioCreateChunk(hmmio,&ckInfo,0))
	{
		Convert2SignedChar();
//		if(mmioWrite(hmmio,(LPSTR)m_pData,m_dwByte)==(long)m_dwByte)
		if(mmioWrite(hmmio,(LPSTR)m_pWaveHdr1->lpData,bytes)==(long)bytes)
		//if(mmioWrite(hmmio,(LPSTR)Part_pData,Part_bytes)==(long)Part_bytes)
		{
			Convert2SignedChar();
			if(mmioAscend(hmmio,&ckInfo,0)) { 
				mmioClose(hmmio,0); 
				return FALSE; 
			}
		}
		else { 
			Convert2SignedChar();
			mmioClose(hmmio,0); 
			return FALSE; 
		}
	}
	else { mmioClose(hmmio,0); return FALSE; }

	if(mmioAscend(hmmio,&ckRIFF,0)) { mmioClose(hmmio,0); return FALSE; }
	mmioClose(hmmio,0);
	return TRUE;
}
