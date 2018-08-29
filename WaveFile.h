#ifndef WaveFile_h

//#include <malloc.h>
//#include <stdio.h>
//#include <string.h>
#include "stdafx.h"

class CWaveFile : public CFile
{
protected:
	UINT total_cues;		//Á` cue ¼Æ

public:
	CWaveFile();
	UINT GetTotalCue();
	UINT GetWaveLength();
	void GetCueParameter(UINT* cuestart, UINT* cuelength);
	void CutWave(CString& csNewFileName, UINT& cuestart, UINT& cuelength);
};

#endif