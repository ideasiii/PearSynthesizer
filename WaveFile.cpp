#include "stdafx.h"
#include "WaveFile.h"

CWaveFile::CWaveFile()
{
	//初始資料
	total_cues = 0;			//總 cue 數
}

UINT CWaveFile::GetTotalCue()
{
	unsigned long totalcues;
	unsigned long data;
	char head[40];
	char shortdata[5];
	char* wavebuffer = NULL;

	this->Seek(0, begin);
	this->Read(&head, 40);			//位址 0
	this->Read(&data, 4);				//位址 40 , 值為 資料長度
	wavebuffer = new char[data];
	this->Read(wavebuffer, data);		// read data into wavebuffer
	free(wavebuffer);
	wavebuffer=NULL;
		
	this->Read(shortdata, 4);			//開始 read 最後 cue 資料 
	shortdata[4] = '\0';
	//先檢查是否有 cue 資料
	
	if (strcmp(shortdata,"cue ")!=0)
	{ 
		AfxMessageBox("This file does not have any cue information", MB_OK);
		this->Close();
		AfxAbort();
	}else{
		this->Seek(4, current);
		this->Read(&totalcues, 4);		 // how many cues in the wave file
	}
	total_cues = (UINT)totalcues;

	return total_cues; 
}

UINT CWaveFile::GetWaveLength()
{
	unsigned long data;
	char head[40];
	
	this->Seek(0, begin);
	this->Read(&head,40);				//位址 0  , 直接 read 40 個 bytes
	this->Read(&data,4);				//位址 40 , 值為 資料長度

	return (UINT)(data);
}

void CWaveFile::GetCueParameter(UINT *cuestart, UINT *cuelength)
{
	unsigned long data;
	//UINT data;
	char head[40];
	char *wavebuffer=NULL;	
	
	this->Seek(0, begin);
	this->Read(&head,40);				//位址 0  , 直接 read 40 個 bytes
	this->Read(&data,4);				//位址 40 , 值為 資料長度
	wavebuffer = new char[data];
	this->Read(wavebuffer, data);		//read 聲音資料
	free(wavebuffer);
	wavebuffer=NULL;
		
	this->Seek(12, current);
	for (UINT i=0; i<total_cues; i++)
	{
		this->Seek(4, current);
		this->Read(&data, 4);
		this->Seek(16, current);
		cuestart[i] = (UINT)data;
	}
	this->Seek(12, current);
	for (UINT j=0; j<total_cues; j++)
	{
		this->Seek(12, current);
		this->Read(&data, 4);
		this->Seek(12, current);
		cuelength[j] = (UINT)data;
	}
}

void CWaveFile::CutWave(CString& csNewFileName, UINT& cuestart, UINT& cuelength)
{
	CFile in;
	char head[40];
	char* data;
	UINT nowpoint = cuestart;
	ASSERT( in.Open(csNewFileName, CFile::modeCreate|CFile::modeWrite|CFile::typeBinary) != NULL ) ;

	this->Seek(0, begin);
	this->Read(head, 40);
	in.Write(head, 40);
	
	int len = cuelength*2;
	in.Write(&len, 4);
	this->Seek(cuestart*2, begin);

	data = new char[cuelength*2];
	this->Read(data, cuelength*2);
	/*for (int i=0;i<8000;i++)
	{
		data[i] = 0 ;
		data[cuelength*2-i-1] = 0 ;
	}*/
	in.Write(data, cuelength*2);
	delete data;
	data = NULL;
		
	in.Close();
}
