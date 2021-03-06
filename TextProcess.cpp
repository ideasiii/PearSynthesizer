// TextProcess.cpp: implementation of the TextProcess class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "PearSynthesizer.h"
#include "TextProcess.h"
//#include "digit.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__; 
#define new DEBUG_NEW
#endif


char *POStags [34] = { " ","VA", "VC", "VE", "VV", "NR", "NT", "NN", "LC", "PN", "DT", 
					  "CD", "OD", "M", "AD", "P", "CC", "CS", "DEC", "DEG", "DER",
					  "DEV", "SP", "AS", "ETC", "MSP", "IJ", "ON", "PU", "JJ", "FW", 
					  "LB", "SB", "BA"};
struct thread_child_info
{
	thread_child_info(){};
	CTTSWave* tWave;
	BOOL* tPlayEnd;
}THREAD_CHILD_INFO;

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

TextProcess::TextProcess()
{
	PlayEnd = TRUE;
	duration_s=0;
	duration_sy=0;
	duration_p=0;
	duration_c=0;
	word.InitWord("");
	giCnt = 0 ;
}
TextProcess::~TextProcess()
{

}


UINT TextProcess::ThreadChildProc(LPVOID ThreadInfo)
{
	thread_child_info* tPara = (thread_child_info*)ThreadInfo;
	tPara->tWave->Play(true);
	while(tPara->tWave->IsBusy())Sleep(1.0);
	*tPara->tPlayEnd = TRUE;
	delete tPara->tWave;
	/************************************************************************/
	/*20080527 fable edition   remove"delete"                               */
	/************************************************************************/
	return 0;
}

void TextProcess::ProcessTheText()
{
	SetCurrentDirectory(strDirPath);


	// 斷句. 先把input的文章存成sentence Array
	int i, j, k, l, lcount, sIndex, wIndex, pIndex;
	CStringArray SentenceArray;
	CString strTemp1, strResult;
	strTemp1 = strInput.SpanExcluding("\n");
	strTemp1 = strTemp1.SpanExcluding("\r");
	strResult = strTemp1;
	while(strResult.FindOneOf("。？！；，") != -1)
	{
		CString temp;
		i = strResult.FindOneOf("。？！；，");
		temp = strResult.Left(i);
		strResult = strResult.Right(strResult.GetLength()-i-2);
		SentenceArray.Add(temp);
	}
	if(strResult != "")	SentenceArray.Add(strResult);
	
	while(strTemp1.GetLength() < strInput.GetLength())
	{
		strInput = strInput.Mid(strTemp1.GetLength()+2);
		strTemp1 = strInput.SpanExcluding("\n");
		strTemp1 = strTemp1.SpanExcluding("\r");
		strResult = strTemp1;
		while(strResult.FindOneOf("。？！；，") != -1)
		{
			CString temp;
			i = strResult.FindOneOf("。？！；，");
			temp = strResult.Left(i);
			strResult = strResult.Right(strResult.GetLength()-i-2);
			SentenceArray.Add(temp);
		}
		if(strResult != "")	SentenceArray.Add(strResult);
	}
	
	// 合成.   以sentence為單位
	char s[10];
	int SyllableBound[100];		// syllable邊界
	int SyllableTone[100];		// tone of syllable
	int WordBound[100];			// word boundary, tts 斷詞結果 
	int PhraseBound[20];		// phrase boundary
	SyllableTone[0] = 0;
	initrd() ;  // ky add: initial shift of syllable index for sentences: giSftIdx
	int playcount = 0;	
	for( lcount = 0; lcount < SentenceArray.GetSize(); lcount++)
	{
		CStringArray PhoneSeq;	// 紀錄整個utterance的phone model sequence  音節  音素
		CString strBig5;
		vector <int> PWCluster;
		vector <int> PPCluster;

		// initial boundaries and indexes for a sentence
		sIndex = wIndex = pIndex = 0;
		SyllableBound[0] = WordBound[0] = PhraseBound[0] = -1;
		for(i = 1; i < 100; i++)		SyllableBound[i] = WordBound[i] = 0;
		for(i = 1; i < 20; i++)			PhraseBound[i] = 0;

		// 簡單處理標點符號及分隔字符
		SentenceArray[lcount].Replace(" ","");
		SentenceArray[lcount].Replace("\t","");
		for(i = SentenceArray[lcount].FindOneOf("：、（）「」") ; i != -1; i = SentenceArray[lcount].FindOneOf("：、（）「」"))
			SentenceArray[lcount].Delete(i, 2);
		for(i = SentenceArray[lcount].FindOneOf(":;,?!()[]") ; i != -1; i = SentenceArray[lcount].FindOneOf(":;,?!()[]"))
			SentenceArray[lcount].Delete(i, 1);
		//DispatchMessageA()

		CartPrediction( SentenceArray[lcount], strBig5, PWCluster, PPCluster);
		vector<int>	tmpIdx(PWCluster.size(), lcount);
		indexArray.insert(indexArray.end(), tmpIdx.begin(), tmpIdx.end());
			AllBig5 += strBig5;
		AllPWCluster.insert(AllPWCluster.end(), PWCluster.begin(), PWCluster.end());
		AllPPCluster.insert(AllPPCluster.end(), PPCluster.begin(), PPCluster.end());
		int iOldPlayCnt = playcount ;
		k = l = 0 ;
		for(i = 0; i<word.wnum; i++)
		{
			for(j = 0; j< word.w_info[i].wlen; j++) 
			{
				SID2Phone((word.w_info[i].phone[j]),&s[0]);
				SyllableTone[++sIndex] = (word.w_info[i].phone[j]%10);
				SplitString(Phone2Ph97(s,SyllableTone[sIndex])," ",PhoneSeq);
				SyllableBound[sIndex] = PhoneSeq.GetSize()-1;
				if(PWCluster[k] == 1)
				{
					WordBound[++wIndex] = SyllableBound[sIndex];
					if(PPCluster[l] == 2)
						PhraseBound[++pIndex] = WordBound[wIndex];
					l++;
				}
				k++;
			}
		}

		// generate label for current sentence
		CStdioFile csLabFile;
		CString strFileName;
		strFileName.Format("%s\\gen\\%s_%d.lab", strDirPath, strFileTitle, lcount);
		csLabFile.Open(strFileName, CFile::modeCreate|CFile::typeText|CFile::modeWrite, NULL);
		GenerateLabelFile(PhoneSeq,SyllableBound,WordBound,PhraseBound,sIndex,wIndex,pIndex, csLabFile, NULL );
		csLabFile.Close();

		// 合成
		clock_t start, finish;
		start=clock();	
		Synthesize( strFileTitle, lcount);
		finish=clock();
		duration_sy+=finish-start;

		// start a thread to play wav sentence by sentence
		while(!PlayEnd)		Sleep(1.0);
		PlayEnd = FALSE;
		THREAD_CHILD_INFO.tPlayEnd = &PlayEnd;
		ttswave = new CTTSWave(NULL, 10, 16000, 16, 1);
		strTemp1.Format("gen\\%s_%d.wav", strFileTitle, playcount++);
		ttswave->Read(strTemp1);
		THREAD_CHILD_INFO.tWave = ttswave;
		AfxBeginThread(ThreadChildProc, (LPVOID)&THREAD_CHILD_INFO);		
		
		PhoneSeq.RemoveAll();
	}
	lcount--;
	while(!PlayEnd)		Sleep(1.0);
 	while(playcount <= lcount)	PlayThePart(strFileTitle, playcount++);
	ConcatenateWave(strFileTitle, lcount);
}

void TextProcess::CartPrediction(CString &sentence, CString& strBig5, vector<int>& PWCluster, vector<int>& PPCluster)
{
	clock_t start, finish;
	bool newone=true;
	int featureDim = 14;
	int nCluster = 2;
 	vector<int> wordpar;	// 當前的詞長
	vector<int> syllpos;	// 當前的字在詞中位置
	vector<int> cluster;	// 韻律詞結尾=1, otherwise 0
	vector<int> pos;		// first: 幾字詞, second: POS tagging
	vector<int> pwBoundary;

	CString tempPOS = "";
	CStringArray tempPOSArray;

	CString cstemp = "";
	CString FileTitle = "";
	CString FileName = "";	
	CStdioFile TokenFile, pos_file, att_file;
	int i, j, k;

	// 斷詞
	int textNdx = 0;
	
	/*****************************fable 2007 09 29*********************/
	/*start         將資料全行文字轉換成半形 針對數字部份             */
	/*****************************fable 2007 09 29*********************/
	//char buf[SENTENCE_LEN*3];
	//convert_digit(sentence.GetBuffer(0),(char*)buf);
	word.GetSentence((UCHAR*)sentence.GetBuffer(0), &textNdx);
	/*****************************fable 2007 09 29*********************/
	/*end         將資料全行文字轉換成半形 針對數字部份               */
	/*****************************fable 2007 09 29*********************/
	/******fable timer*******/
	start=clock(); 
	word.GetWord();
	finish=clock();
	duration_s+=(double)finish-start;
	/******fable timer*******/
	for( i = 0 ; i < word.wnum ; i++ )
	{
		switch(word.w_info[i].wlen) 
		{
			case 1:	//1字詞
				wordpar.push_back(1);
				syllpos.push_back(1);
			break;
			case 2: //2字詞
				wordpar.push_back(2);
				syllpos.push_back(2);
				wordpar.push_back(2);
				syllpos.push_back(3);
			break;
			case 3: //3字詞
				wordpar.push_back(3);
				syllpos.push_back(2);
				wordpar.push_back(3);
				syllpos.push_back(4);
				wordpar.push_back(3);
				syllpos.push_back(3);
			break;
			case 4:	//4字詞 
				wordpar.push_back(4);
				syllpos.push_back(2);
				wordpar.push_back(4);
				syllpos.push_back(42);	//中前
				wordpar.push_back(4);
				syllpos.push_back(43);	//中後
				wordpar.push_back(4);
				syllpos.push_back(3);
			break;
		}
		strBig5 = strBig5 + word.w_info[i].big5;
		cstemp = cstemp + word.w_info[i].big5;
		cstemp = cstemp + " ";
	}
	FileTitle.Format("input_line");
	FileName.Format("%s.tok",FileTitle);
	TokenFile.Open(FileName,CFile::modeCreate | CFile::modeWrite | CFile::typeText);
	TokenFile.WriteString(cstemp);
	TokenFile.Close();
	AfxMessageBox(cstemp);
	/*****************************fable 2008 05 27*********************/
	/*start         newone true®É¬°·sªºparser					      */
	/*****************************fable 2008 05 27*********************/
	if(newone)
	{
		FileTitle.Format("input_line");
		FileName.Format("%s.flag",FileTitle);
		TokenFile.Open(FileName,CFile::modeCreate | CFile::modeWrite);
		/******fable timer*******/
		start=clock(); 
		TokenFile.Close();
		/******fable timer*******/
	}
		// POS tagging
	if(!newone)
	{
		/******fable timer*******/
		start=clock(); 
		/******fable timer*******/
		CStdioFile parFile;
		parFile.Open("Parsing.bat", CFile::modeCreate | CFile::modeWrite | CFile::typeText, NULL);

		CString cstemp2;
		cstemp.Empty();
		cstemp2 = strDirPath.Left(2);
		cstemp.Format("%s && cd %s && java -mx500m edu.stanford.nlp.parser.lexparser.LexicalizedParser -encoding big5 -outputFormat \"wordsAndTags\" xinhuaFactored.ser.gz input_line.tok > input_line.pos", cstemp2, strDirPath);
		parFile.WriteString(cstemp);
		parFile.Close();
		AfxMessageBox(cstemp);
		ExecuteCommand("Parsing.bat");
		FileName.Format("input_line.pos");
		pos_file.Open(FileName, CFile::modeRead|CFile::typeText);
		pos_file.ReadString(tempPOS);
		pos_file.Close();

		DeleteFile("Parsing.bat");
		DeleteFile("input_line.tok");
		/******fable timer*******/
		finish=clock();
		duration_p+=(double)finish-start;
		/******fable timer*******/
	}
	if(newone)
	{
		FileName.Format("output_line.flag");
		while(!TokenFile.Open(FileName,CFile::modeRead))Sleep(500);
		/******fable timer*******/
		finish=clock();
		duration_p+=(double)finish-start;
		/******fable timer*******/
		TokenFile.Close();
		FileName.Format("output_line.tok");
		pos_file.Open(FileName, CFile::modeRead|CFile::typeText);
		pos_file.ReadString(tempPOS);
		pos_file.Close();
		AfxMessageBox(tempPOS);
		DeleteFile("output_line.flag");
		DeleteFile("input_line.tok");
	}
	/*****************************fable 2008 05 27*********************/
	/*start         ­p®É¥Î										      */
	/*****************************fable 2008 05 27*********************/
	if(SplitString(tempPOS," ",tempPOSArray) == 0)	
		tempPOSArray.Add(tempPOS);
	for(i = 0; i < tempPOSArray.GetSize(); i++)
	{	
		int index = tempPOSArray[i].Find("/",0);
		int length = tempPOSArray[i].GetLength();
		cstemp.Format("%s",tempPOSArray[i].Mid(index+1,length));
		index /= 2;
		bool bAdded = false ;
		for(j = 1; j<34; j++) // rosy modify: infinite loop
		{
			if(cstemp == POStags[j]) {
				for( k = 0; k < index; k++)	
					pos.push_back(j);
				bAdded = true ;// rosy modify: infinite loop
				break;
			} else NULL;
		}
		if( bAdded == false )// rosy modify: infinite loop
			for( k = 0; k < index; k++)	
				pos.push_back(j);
// 		VERIFY (j < 34);//34??
	}
	
	int size = syllpos.size();
	FileName.Format("%s.att", FileTitle);
	VERIFY(att_file.Open(FileName,CFile::modeCreate|CFile::modeWrite|CFile::typeBinary, NULL));
	
	att_file.Write(&size, sizeof(int));
	att_file.Write(&featureDim, sizeof(int));
	att_file.Write(&nCluster, sizeof(int));

	for ( i = 0 ; i < size ; i++ )						// 每一次iteration處理一個syllable的attribute
	{
		int nCID = -1;							// Belongs to which cluster
		att_file.Write(&nCID, sizeof(int));	
			
		for ( j = 2 ; j >= -2; j-- )					// (LL/L/C/R/RR) syllable所在LW長度
		{
			int valFeature;
			if(((i-j)>=0) && ((i-j)<size))		valFeature = wordpar[i-j]; 
			else 		valFeature = 0;
			att_file.Write(&valFeature, sizeof(int));
		}

		int Sen_Length = size;							// Sentence length
		int F_PositionInSen = i;						// Position in sentence (Forward)
		int B_PositionInSen = size-i;					// Position in sentence (Backward)
		int PositionInWord = syllpos[i];				// Position in lexicon word
	
		att_file.Write(&Sen_Length, sizeof(int));
		att_file.Write(&F_PositionInSen, sizeof(int));				
		att_file.Write(&B_PositionInSen, sizeof(int));
		att_file.Write(&PositionInWord, sizeof(int));	
				
		for ( j = 2 ; j >= -2; j-- )					// (LL/L/C/R/RR) syllable所在POS tags
		{
			int valFeature;
			if(((i-j)>=0) && ((i-j)<size))		valFeature = pos[i-j]; 
			else	valFeature = 0;
			att_file.Write(&valFeature, sizeof(int));
		}
	}
	att_file.Close();	
	
	CString strModelName;
	strModelName.Format(".\\model\\Cart Model.bin");
	/*******************/
	start=clock();
	GenerateBoundary(FileName, cluster, strModelName);
	finish=clock();
	duration_c+=(double)finish-start;
	/*******************/
	DeleteFile(FileName);
//	CString strResult1, strResult2;
//	att_file.Open("Result.txt", CFile::modeCreate | CFile::modeWrite | CFile::typeText);
	for (i = 0; i < cluster.size(); i++) 
	{
//		strResult2.Format("%d\n",cluster[i]);
		PWCluster.push_back(cluster[i]);
//		strResult1 += strResult2;
		if(cluster[i] == 1)
			pwBoundary.push_back(i);;
	}
//	att_file.WriteString(strResult1);
//	att_file.Close();

	// Tier2 !!!!	
	int sizet2 = pwBoundary.size();
	FileName.Format("%s_tier2.att",FileTitle);
	VERIFY(att_file.Open(FileName,CFile::modeCreate|CFile::modeWrite|CFile::typeBinary, NULL));
			
	att_file.Write(&sizet2, sizeof(int));
	att_file.Write(&featureDim, sizeof(int));
	att_file.Write(&nCluster, sizeof(int));

	sizet2 = syllpos.size();
	for ( i = 0, k = 0; i < sizet2; i++ )						// 每一次iteration處理一個syllable的attribute
	{
		int nCID = cluster[i];							// Belongs to which cluster
		if(nCID == 0)
			continue;
		else {
			att_file.Write(&nCID, sizeof(int));			
			for ( j = 2 ; j >= -2; j-- )					// (LL/L/C/R/RR) syllable所在LW長度
			{
				int valFeature;
				if(((k-j-1)>=0) && ((k-j)<pwBoundary.size()))
					valFeature = abs(pwBoundary[k-j] - pwBoundary[k-j-1]); 
				else 
					valFeature = 0;
				att_file.Write(&valFeature, sizeof(int));
			}

			int Sen_Length = pwBoundary.size();							// Sentence length
			int F_PositionInSen = k;						// Position in sentence (Forward)
			int B_PositionInSen = pwBoundary.size()-k;					// Position in sentence (Backward)
			int PositionInWord = syllpos[i];				// Position in lexicon word
	
			att_file.Write(&Sen_Length, sizeof(int));
			att_file.Write(&F_PositionInSen, sizeof(int));				
			att_file.Write(&B_PositionInSen, sizeof(int));
			att_file.Write(&PositionInWord, sizeof(int));	
				
			for ( j = 2 ; j >= -2; j-- )					// (LL/L/C/R/RR) syllable所在POS tags
			{
				int valFeature;
				if(((k-j)>=0) && ((k-j)<pwBoundary.size()))		valFeature = pos[pwBoundary[k-j]]; 
				else	valFeature = 0;
				att_file.Write(&valFeature, sizeof(int));
			}
		}
		k++;
	}
	att_file.Close();	
	
	cluster.clear();
	strModelName.Format(".\\model\\Cart Model2.bin");
	/*************/
	start=clock();
	GenerateBoundary(FileName, cluster, strModelName);
	finish=clock();
	duration_c+=(double)finish-start;
	/*************/
	DeleteFile(FileName);
//	strResult1.Empty();
//	att_file.Open("Result_tier2.txt", CFile::modeCreate | CFile::modeWrite | CFile::typeText);
	for (i = 0; i < cluster.size(); i++) {
//		strResult2.Format("%d\n",cluster[i]);
		PPCluster.push_back(cluster[i]);
//		strResult1 += strResult2;
	}
//	att_file.WriteString(strResult1);
//	att_file.Close();
}

int TextProcess::SplitString(const CString& input, const CString& delimiter, CStringArray& results)
{
	 int iPos = 0;
  int newPos = -1;
  int sizeS2 = delimiter.GetLength();
  int isize = input.GetLength();

  CArray <int, int> positions;
  newPos = input.Find (delimiter, 0);
  if( newPos < 0 ) { return 0; }
  int numFound = 0;

  while( newPos > iPos ) {
    numFound++;
    positions.Add(newPos);
    iPos = newPos;
    newPos = input.Find (delimiter, iPos+sizeS2+1);
  }

  for( int i=0; i <= positions.GetSize(); i++ ) {
	CString s;
    if( i == 0 )
      s = input.Mid( i, positions[i] );
    else {
      int offset = positions[i-1] + sizeS2;
      if( offset < isize ) {
		if( i == positions.GetSize() )
          s = input.Mid(offset);
        else if( i > 0 )
          s = input.Mid( positions[i-1] + sizeS2, positions[i] - positions[i-1] - sizeS2 );
      }
    }
    if( s.GetLength() > 0 )
		results.Add(s);
  }
  return numFound;
}


// Ph97: Extended initial + final 
// 三個部分: part1: Extended initial
//			 part2 and part3: 含tone的tonal final or 單獨存在的 tonal initial 
CString TextProcess::Phone2Ph97(CString phone,int tone)
{
	CString result, tmp, whatever;
	result = tmp = "";
	char *buffer;
	int i,j,find;
	find = 0;
	j = 0;
	buffer = phone.GetBuffer(0);
	int len = phone.GetLength();  //一個字元2 bytes !!
	if((len == 2) || ((len == 4)&&(tone != 1))) {	// 單獨母音或單獨子音
		for(i=0; i<23; i++) {
			if(memcmp(&buffer[j],Tonal[i],2) == 0) {
				tmp.Format("%s",Ph97Phoneme[i]);
				result += tmp;
//				tmp.Format(" %s",Ph97Phoneme[i]);
//				result += tmp;
				break;
			}
		}
		i++;
	} else if(((len == 4)&&(tone ==1)) || ((len == 6)&&(tone != 1))) {	// initial + final
		for(i=0; (i<53) && (find !=1); i++)
		{
			if(memcmp(&buffer[j],ExtendedInitial[i],2) == 0) {
				tmp.Format("%s ",Ph97Phoneme[i+23]);
				result += tmp;		// part 1
				j += 2;
				for(i = 0; i < 23; i++){
					if(memcmp(&buffer[j],Tonal[i],2) == 0) {
						result += Ph97Phoneme[i];
						find = 1;
						break;
					}
				}
			}
		}
	} else	{
		for(i=24; (i<53) && (find!=1); i++) {
			if(memcmp(&buffer[j],ExtendedInitial[i],4) == 0) {
				tmp.Format("%s ",Ph97Phoneme[i+23]);
				result += tmp;
				j += 4;
				for(i = 0; i < 23; i++)	{
					if((memcmp(&buffer[j],Tonal[i],2)) == 0) {
						result += Ph97Phoneme[i];
						find = 1;
						break;
					}
				}
			}	
		}
	}
	i--;
	if(tone != 5) {
		if((tone == 1) || (tone == 4))		// part 2
			result += "H ";		
		else
			result += "L ";
		result += Ph97Phoneme[i];		
		if((tone ==1) || (tone == 2))		// part 3
			result += "H";
		else
			result += "L";
	} else {
		tmp.Format("M %sM",Ph97Phoneme[i]);
		result += tmp;
	}
	return result;	
}

// 它把輸出的所有行都存在變數 fullstr, monostr, 分別會在function最後用csFile and *pcsFile2寫出full and mono
void TextProcess::GenerateLabelFile(CStringArray& sequence,		//	句子的音素序列
									  const int sBound[],const int wBound[], const int pBound[],	//	syllable/word/phrase 邊界 (在第幾個phoneme之後)
									  const int sCount,const int wCount, const int pCount, // syllable/word/phrase 數目 
									  CStdioFile& csFile, CStdioFile *pcsFile2 )	// ky modify:如果pcsFile2是NULL的話就表示它不用輸出mono style label
{	
	CStdioFile labelfile;
	CString fullstr, tempstr; // fullstr: store all lines for full labels
	CString monostr ; // ky add: store all lines for mono labels
	int sIndex, wIndex,pIndex; // syllable/word/phrase index for sBound/wBound/pBound
	sIndex = wIndex = pIndex = 0;
	fullstr = "" ;  
	monostr = "" ;
	
	// ky add: output time information from cue. use timeinfo() to enable "outpu time information"
	char timebuf[25]; // tag of time. calculated from cue and syllabel boundaries
	if (gduration_s!=NULL) // output time info for the first pause label
	{
		int tmp=0;
		if(gduration_s[1]-1000000>0)
			tmp=gduration_s[1]-1000000;
		tempstr.Format( "%10d %10d ", tmp, gduration_s[1] ) ;
		fullstr += tempstr;
		monostr += tempstr;
	}
	//

	// p1^p2-p3+p4=p5@p6_p7/A:a3/B:b3@b4-b5&b6-b7/C:c3/D:d2/E:e2@e3+e4
	// /F:f2/G:g1_g2/H:h1=h2@h3=h4/I:i1=i2/J:j1+j2-j3
	tempstr.Format("x^x-pau+%s=%s@x_x/A:0/B:x@x-x&x-x/C:%d/D:0/E:x@x+x", sequence[0],sequence[1],sBound[1]+1);	// current phoneme = pause (pau);
	fullstr += tempstr ; // ky modify: don't initial fullstr, since I'll do it myself.
	tempstr.Format("pau\n"); // ky add: for mono
	monostr += tempstr; // ky add: for mono
	int anchor,anchor2;
	anchor = anchor2 = 0;
	while(sBound[anchor] != wBound[1]) 	// f2
		anchor++;
	tempstr.Format("/F:%d/G:0_0/H:x=x@1=%d",anchor,pCount);
	fullstr += tempstr;
	if(pCount == 1)	// i1, i2
		tempstr.Format("/I:%d=%d",sCount,wCount);
	else {
		anchor = 0;
		while(sBound[anchor] != pBound[1])	// i1
			anchor++;
		tempstr.Format("/I:%d",anchor);
		fullstr += tempstr;
		anchor = 0;
		while(wBound[anchor] != pBound[1])	// i2
			anchor++;
		tempstr.Format("=%d",anchor);
		fullstr += tempstr;
	}
	tempstr.Format("/J:%d+%d-%d\n",sCount,wCount,pCount);
	fullstr += tempstr;
	int iMM  = INT_MAX ; //index mm: syllable_phone   phone index. since the mm is assigned from sylla_p[ll], the value is initialed as maximum value, and reassigned in the loop
	for (int index = 0; index < sequence.GetSize(); index++)	// index = current phone
	{	
		if(sBound[sIndex] < index) sIndex++;
		if(wBound[wIndex] < index) wIndex++;
		if(pBound[pIndex] < index) pIndex++;
		
		// ky add: add time info for each line of label.
		if (gduration_s!=NULL) {
			// simulate indexes in old version: window_synthesis_demo: textpross::labelgen()
			int iLL = sIndex-1;				// index ll: word_syllable   syllable index
			int iSy_p_ll = sBound[iLL] + 1;			// sylla_p[ll]
			int iSy_p_ll_1 = sBound[iLL+1] + 1;		// sylla_p[ll+1]
			if( iMM >=iSy_p_ll_1+2 )				// index mm: syllable_phone   phone index. simulate the loop of index mm
				iMM = iSy_p_ll+2 ;
			else
				iMM++ ;

			// output time info
			tempstr.Format( "%10d %10d ", 
				gduration_s[iLL+1] + (gduration_e[iLL+1] - gduration_s[iLL+1]) * ( iMM - iSy_p_ll - 2 ) / ( iSy_p_ll_1 - iSy_p_ll ), 
				gduration_s[iLL+1] + (gduration_e[iLL+1] - gduration_s[iLL+1]) * ( iMM - iSy_p_ll - 1 ) / ( iSy_p_ll_1 - iSy_p_ll) ) ;
			fullstr += tempstr;
			monostr += tempstr ;
			tempstr.Format("%s\n",sequence[index]);
			monostr += tempstr ;
		}
		//
		
		// p1~p5
		if(index < 2) 
		{
			if(index == 0)
			{
				if(sequence.GetSize() == 1)
					tempstr.Format("x^pau-%s+x=x",sequence[index]);
				else if (sequence.GetSize() == 2)
					tempstr.Format("x^pau-%s+%s=x",sequence[index],sequence[index+1]);
				else
					tempstr.Format("x^pau-%s+%s=%s",sequence[index],sequence[index+1],sequence[index+2]);
			}
			else	// index == 1
			{
				if(sequence.GetSize() == 2)
					tempstr.Format("pau^%s-%s+x=x",sequence[index-1],sequence[index]);
				else if(sequence.GetSize() == 3)
					tempstr.Format("pau^%s-%s+%s=x",sequence[index-1],sequence[index],sequence[index+1]);
				else
					tempstr.Format("pau^%s-%s+%s=%s",sequence[index-1],sequence[index],sequence[index+1],sequence[index+2]);
			}
		} 
		else
			if(index > sequence.GetSize()-3) 
			{
				if(index == sequence.GetSize()-2) 
					tempstr.Format("%s^%s-%s+%s=pau",sequence[index-2],sequence[index-1],sequence[index],sequence[index+1]);
				else 
					tempstr.Format("%s^%s-%s+pau=x",sequence[index-2],sequence[index-1],sequence[index]);
			} 
			else 
				tempstr.Format("%s^%s-%s+%s=%s",sequence[index-2],sequence[index-1],sequence[index],sequence[index+1],sequence[index+2]);
		fullstr += tempstr;
		
		// p6, p7
		tempstr.Format("@%d_%d",index-sBound[sIndex-1], sBound[sIndex]+1-index);
		fullstr += tempstr;

		// a3, b3
		if(sIndex == 1)	tempstr.Format("/A:0/B:%d",sBound[sIndex]-sBound[sIndex-1]);	
		else	tempstr.Format("/A:%d/B:%d",sBound[sIndex-1]-sBound[sIndex-2],sBound[sIndex]-sBound[sIndex-1]);
		fullstr += tempstr;
		
		// b4, b5
		anchor = sIndex;
		while(wBound[wIndex-1] < sBound[anchor])
			anchor--;
		tempstr.Format("@%d",sIndex - anchor);
		fullstr += tempstr;
		anchor = sIndex;
		while(wBound[wIndex] > sBound[anchor])
			anchor++;
		tempstr.Format("-%d",anchor-sIndex+1);
		fullstr += tempstr;

		// b6, b7
		anchor = sIndex;
		while(pBound[pIndex-1] < sBound[anchor])
			anchor--;
		tempstr.Format("&%d",sIndex - anchor);
		fullstr += tempstr;
		anchor = sIndex;
		while(pBound[pIndex] > sBound[anchor])
			anchor++;
		tempstr.Format("-%d",anchor-sIndex+1);
		fullstr += tempstr;
	
		// c3
		if(sIndex == sCount)
			tempstr.Format("/C:0");
		else
			tempstr.Format("/C:%d",sBound[sIndex+1]-sBound[sIndex]);
		fullstr += tempstr;

		// d2
		if(wIndex==1)
		tempstr.Format("/D:0");
		else {
			anchor = sIndex;
			while(sBound[anchor] != wBound[wIndex-1])
				anchor--;
			anchor2 = anchor;
			while(wBound[wIndex-2] < sBound[anchor2])
				anchor2--;
			tempstr.Format("/D:%d",anchor-anchor2);
		}
		fullstr += tempstr;
		// e2
		anchor = sIndex;
		while(wBound[wIndex-1] < sBound[anchor]) anchor--;
		anchor2 = sIndex;
		while(wBound[wIndex] > sBound[anchor2]) anchor2++;
		tempstr.Format("/E:%d",anchor2-anchor);
		fullstr += tempstr;

		// e3, e4
		anchor = wIndex;
		while(pBound[pIndex-1] < wBound[anchor])
			anchor--;
		tempstr.Format("@%d",wIndex-anchor);
		fullstr += tempstr;
		anchor = wIndex;
		while(pBound[pIndex] > wBound[anchor])
			anchor++;
		tempstr.Format("+%d",anchor-wIndex+1);
		fullstr += tempstr;

		// f2:  #of syllable in the next next word
		anchor = sIndex;
		while(sBound[anchor] < wBound[wIndex])	anchor++;
		anchor2 = anchor;	// anchor2: where the next word start
		while(sBound[anchor] < wBound[wIndex+1])	anchor++;	
		tempstr.Format("/F:%d",anchor-anchor2);	
		fullstr += tempstr;

		// g1:	#of syllables in the previous phrase
		// g2:	#of words in the previous phrase
		if(pIndex == 1)
			tempstr.Format("/G:0_0");
		else {
			anchor = sIndex;
			while(sBound[anchor] > pBound[pIndex-1])	anchor--;
			anchor2 = anchor;
			while(pBound[pIndex-2] < sBound[anchor2])	anchor2--;
			tempstr.Format("/G:%d",anchor-anchor2);
			fullstr += tempstr;
			anchor = wIndex;
			while(wBound[anchor] > pBound[pIndex-1])	anchor--;
			anchor2 = anchor;
			while(pBound[pIndex-2] < wBound[anchor2])	anchor2--;
			tempstr.Format("_%d",anchor-anchor2);
		}
		fullstr += tempstr;

		// h1:	#of syllables in the current phrase
		// h2:	#of words in the current phrase
		anchor = anchor2 = sIndex;
		while(pBound[pIndex-1] < sBound[anchor])	anchor--;
		while(pBound[pIndex] > sBound[anchor2])	anchor2++;
		tempstr.Format("/H:%d",anchor2-anchor);
		fullstr += tempstr;
		anchor = anchor2 = wIndex;
		while(pBound[pIndex-1] < wBound[anchor])	anchor--;
		while(pBound[pIndex] > wBound[anchor2])	anchor2++;
		tempstr.Format("=%d",anchor2-anchor);
		fullstr += tempstr;

		tempstr.Format("@%d=%d",pIndex,pCount-pIndex+1);	// h3, h4
		fullstr += tempstr;
		// i1, i2
		if(pCount == 1)
			tempstr.Format("/I:0=0");
		else {
			anchor = anchor2 = sIndex;
			while(pBound[pIndex] > sBound[anchor])	anchor++;
			anchor2 = anchor;
			while(pBound[pIndex+1] > sBound[anchor])	anchor++;
			tempstr.Format("/I:%d",anchor-anchor2);
			fullstr += tempstr;
			anchor = anchor2 = wIndex;
			while(pBound[pIndex] > wBound[anchor])	anchor++;
			anchor2 = anchor;
			while(pBound[pIndex+1] > wBound[anchor])	anchor++;
			tempstr.Format("=%d",anchor-anchor2);
			fullstr += tempstr;
		}	
		tempstr.Format("/J:%d+%d-%d\n",sCount,wCount,pCount);	// j1,j2,j3
		fullstr += tempstr;
	}
//	index--;
	int index = sequence.GetSize()-1;		//20091211 rosy edit for .Net 取代上面那行 index --

	// ky add: add time info for pau at the end of the sentence
	if (gduration_s!=NULL)
	{
		int tmp=0;
		int iCount_2 = sCount ;  // simulate index for count[2] 
		giSftIdx += sCount ;  // update global shift of syllable index for each sentence
		tmp=gduration_e[ iCount_2 ]+1000000;
		tempstr.Format( "%10d %10d ", gduration_e[ iCount_2 ],tmp ) ;
		fullstr += tempstr;
		monostr += tempstr;
	}
	//

	tempstr.Format("%s^%s-pau+x=x@x_x/A:%d/B:x@x-x&x-x/C:0",sequence[index-1],sequence[index],sBound[sIndex]-sBound[sIndex-1]);
	fullstr += tempstr;
	tempstr.Format("pau\n");// ky add
	monostr += tempstr; // ky add
	anchor = sIndex;
	while(wBound[wIndex-1] < sBound[anchor])	// d2 ~ f2
		anchor--;
	tempstr.Format("/D:%d/E:x@x+x/F:0",sIndex-anchor);
	fullstr += tempstr;
	anchor = sIndex;	// g1 ~ j3
	while(pBound[pIndex-1] < sBound[anchor])	anchor--;
	tempstr.Format("/G:%d",sIndex-anchor);
	fullstr += tempstr;
	anchor = wIndex;
	while(pBound[pIndex-1] < wBound[anchor])	anchor--;
	tempstr.Format("_%d/H:x=x@%d=1/I:0=0/J:%d+%d-%d\n",wIndex-anchor,pCount,sCount,wCount,pCount);
	fullstr += tempstr;
	csFile.WriteString(fullstr);//fullstr即為輸出的Label內容

	if( pcsFile2 != NULL )	// ky add: write out mono labels if needed
		(*pcsFile2).WriteString(monostr);
}


void TextProcess::Synthesize(CString name, int c)
{
	CStringArray strCommandArray;
	CString command;
	int i;
	command.Format("gen\\%s_%d.lab", name, c);	// argument 1
	strCommandArray.Add(command);				
	command.Replace(".lab", ".f0");			// argument 2			
	strCommandArray.Add(command);
	command.Replace(".f0", ".raw");			// argument 3
	strCommandArray.Add(command);		
	command.Replace(".raw", ".trace");			// argument 4
	strCommandArray.Add(command);
	command.Replace(".trace", ".raw");
	char **commandLine = new char* [strCommandArray.GetSize()];
	for(i=0; i < strCommandArray.GetSize(); i++)
	{
		commandLine[i] = new char [strCommandArray[i].GetLength()+2];		// CString GetLength回傳的大小不包含"\0" (預留空間給\0)
		strcpy( commandLine[i], strCommandArray[i].GetBuffer(strCommandArray[i].GetLength()));		// strcpy會在最後加上"\0"
		strCommandArray[i].ReleaseBuffer();
	}
	
	hts_engine(commandLine, dPitchRatio, dSpeakRate, nSpeaker);
	
	for(i=0; i < strCommandArray.GetSize(); i++)	delete []commandLine[i];
	delete [] commandLine;

	CStdioFile rawFile;
	rawFile.Open(command, CFile::modeRead | CFile::typeBinary, NULL);
	int sylen = rawFile.GetLength();	
	short *tmpsy = new short[sylen];
	rawFile.Read(&tmpsy[0], sylen*sizeof(short));
	rawFile.Close();
//	DeleteFile(command);	//kennyou mark
//	AfxMessageBox("hts_engin finish");
//	return;
	CTTSWave *OutputWave = new CTTSWave(NULL, 10, 16000, 16, 1);
	PCMWAVEFORMAT pear;
	pear.wBitsPerSample = 16;
	pear.wf.wFormatTag = 1;
	pear.wf.nChannels = 1;
	pear.wf.nSamplesPerSec = 16000;
	pear.wf.nAvgBytesPerSec = 32000;
	pear.wf.nBlockAlign = 2;
	OutputWave->SetWave(pear, (BYTE*)tmpsy, sylen);
	command.Replace(".raw", ".wav");
	OutputWave->Write(command);
}

void TextProcess::ConcatenateWave(CString name, int c)
{
	// 將單元串接
	CTTSWave *wave1 = new CTTSWave(NULL, 10, 16000, 16, 1);
	CTTSWave *wave2 = new CTTSWave(NULL, 10, 16000, 16, 1);
	CString cstemp = "";
	for(int i = 0; i <= c ; i++)
	{
		cstemp.Format("gen\\%s_%d.wav",name,i);
		wave1->Read(cstemp);
		wave2->Append((short*)wave1->GetBuffer(),wave1->GetLength()/2);
		while(wave2->IsBusy())
			;
	}
	name += ".wav";
//	wave2->Play(true);
//	while(wave2->IsBusy())
//			;
	wave2->Write(name);
	delete wave1;
	delete wave2;
}

void TextProcess::GenerateBoundary(CString& csFileName, vector<int>& vecCluster, CString& strModelName)
{
	CART aCartObj;
	aCartObj.LoadCARTModel(strModelName);
	
	double d=0.0;	
	int i, j;
	CStdioFile csfFile;
	csfFile.Open(csFileName, CFile::modeRead|CFile::typeBinary, NULL);

	int nDataNum;
	csfFile.Read(&nDataNum, sizeof(int));
	int Dim_Att_Catagory;
	csfFile.Read(&Dim_Att_Catagory, sizeof(int));
	int Dim_Att_Ordered = 0;
	int nClu;
	csfFile.Read(&nClu, sizeof(int));

	for(i=0; i<nDataNum; i++)
	{
		int clu;
		csfFile.Read(&clu, sizeof(int));
		unsigned int a;
		CART_DATA *pcdData = new CART_DATA();
		pcdData->clu = clu;

		for(j=0; j<Dim_Att_Catagory; j++){
			csfFile.Read(&a, sizeof(unsigned int));
			pcdData->Att_Catagory.Add(a);
		}
		aCartObj.TEST(pcdData);
		vecCluster.push_back(pcdData->clu);
		delete pcdData ; // ky add: release memory
	}
	csfFile.Close();
}


void TextProcess::PlayThePart(CString name, int c)
{
	CTTSWave *wave1 = new CTTSWave(NULL, 10, 16000, 16, 1);
	CString cstemp;
	cstemp.Format("gen\\%s_%d.wav", name, c);
	wave1->Read(cstemp);
	wave1->Play(true);
	while(wave1->IsBusy()) ;
	delete wave1;
}


void TextProcess::GenLabels(){ // ky add: generate one label file for training
	// load cue informations
	CWaveFile WaveFile;  // class for loading cue information from wav file
	char csTargetFileName[MAX_PATH];  // filename of wav file with absolute path
	sprintf(csTargetFileName,"%s/wav/%s.wav",strDirPath,strFileTitle);
	WaveFile.Open(csTargetFileName, CFile::modeRead|CFile::typeBinary, NULL);
	int nTotalCue = WaveFile.GetTotalCue();  // total # cue tags in the wav file, including tags of all sentences.
// 	if (datain.GetLength()/2!=nTotalCue)	// rosy mark: skip error check while sentences with 標點符號
// 		continue;
	UINT *cuestart = new UINT[nTotalCue];		//cue start
	UINT *cuelength = new UINT[nTotalCue];		//cue length
	UINT *cuestart2 = new UINT[nTotalCue+1];	//cue start
	UINT *cuelength2 = new UINT[nTotalCue+1];	//cue length
	WaveFile.GetCueParameter(cuestart, cuelength);
	for (int i=0;i<nTotalCue;i++) {  // convert unit to sample-point based.
		cuestart2[i+1]=cuestart[i]*1000/1.6;
		cuelength2[i+1]=cuestart2[i+1]+cuelength[i]*1000/1.6;
	}

	// 斷句. 先把input的文章存成sentence Array
	CStringArray SentenceArray;
	CString strTemp1, strResult;
	strTemp1 = strInput.SpanExcluding("\n");
	strTemp1 = strTemp1.SpanExcluding("\r");
	strResult = strTemp1;
	while(strResult.FindOneOf("。？！；，") != -1)
	{
		CString temp;
		int i = strResult.FindOneOf("。？！；，");
		temp = strResult.Left(i);
		strResult = strResult.Right(strResult.GetLength()-i-2);
		SentenceArray.Add(temp);
	}
	if(strResult != "")	SentenceArray.Add(strResult);
	while(strTemp1.GetLength() < strInput.GetLength())
	{
		strInput = strInput.Mid(strTemp1.GetLength()+2);
		strTemp1 = strInput.SpanExcluding("\n");
		strTemp1 = strTemp1.SpanExcluding("\r");
		strResult = strTemp1;
		while(strResult.FindOneOf("。？！；，") != -1)
		{
			CString temp;
			int i = strResult.FindOneOf("。？！；，");
			temp = strResult.Left(i);
			strResult = strResult.Right(strResult.GetLength()-i-2);
			SentenceArray.Add(temp);
		}
		if(strResult != "")	SentenceArray.Add(strResult);
	}

	// generate labels sentence by sentence
	char s[10];
	int SyllableBound[100];		// syllable邊界
	int SyllableTone[100];		// tone of syllable
	int WordBound[100];			// word boundary, tts 斷詞結果 
	int PhraseBound[20];		// phrase boundary
	initrd(); // initial cue and shift as empty.
	for( int lcount = 0; lcount < SentenceArray.GetSize(); lcount++)
	{
		// 簡單處理標點符號及分隔字符
		SentenceArray[lcount].Replace(" ","");
		SentenceArray[lcount].Replace("\t","");
		for(int i = SentenceArray[lcount].FindOneOf("：、（）「」") ; i != -1; i = SentenceArray[lcount].FindOneOf("：、（）「」"))
			SentenceArray[lcount].Delete(i, 2);
		for(int i = SentenceArray[lcount].FindOneOf(":;,?!()[]") ; i != -1; i = SentenceArray[lcount].FindOneOf(":;,?!()[]"))
			SentenceArray[lcount].Delete(i, 1);

		// CART
		CString strBig5;
		vector <int> PWCluster;
		vector <int> PPCluster;
		CartPrediction( SentenceArray[lcount], strBig5, PWCluster, PPCluster);


		// reset boundaries and indexes
		SyllableBound[0] = WordBound[0] = PhraseBound[0] = -1;
		SyllableTone[0] = 0;
		for(int i = 1; i < 100; i++)	SyllableBound[i] = WordBound[i] = SyllableTone[i] =  0;
		for(int i = 1; i < 20; i++)		PhraseBound[i] = 0;	
		int sIndex = 0, wIndex = 0, pIndex = 0; // # of syllable/word/phrase in this sentence
		int k = 0, l = 0 ;

		//算boundaries, and count # syllable/word/phrase:sIndex/wIndex/pIndex
		CStringArray PhoneSeq;	// 紀錄整個utterance的phone model sequence
		vector<int>	tmpIdx(PWCluster.size(), lcount);
		indexArray.insert(indexArray.end(), tmpIdx.begin(), tmpIdx.end());
		AllBig5 += strBig5;
		AllPWCluster.insert(AllPWCluster.end(), PWCluster.begin(), PWCluster.end());
		AllPPCluster.insert(AllPPCluster.end(), PPCluster.begin(), PPCluster.end());
		for(int i = 0; i<word.wnum; i++)
		{
			for(int j = 0; j< word.w_info[i].wlen; j++) 
			{
				SID2Phone((word.w_info[i].phone[j]),&s[0]);
				SyllableTone[++sIndex] = (word.w_info[i].phone[j]%10);
				SplitString(Phone2Ph97(s,SyllableTone[sIndex])," ",PhoneSeq);
				SyllableBound[sIndex] = PhoneSeq.GetSize()-1;
				if(PWCluster[k] == 1)
				{
					WordBound[++wIndex] = SyllableBound[sIndex];
					if(PPCluster[l] == 2)
						PhraseBound[++pIndex] = WordBound[wIndex];
					l++;
				}
				k++;
			}
		}
		
		// generate labels of full/mono
		CStdioFile csLabFileFull;
		CStdioFile csLabFileMono;
		CString strFileName;
		strFileName.Format("%s\\train\\full\\temp_%s_%d.lab", strDirPath, strFileTitle, lcount);
		csLabFileFull.Open(strFileName, CFile::modeCreate|CFile::typeText|CFile::modeWrite, NULL);
		strFileName.Format("%s\\train\\mono\\temp_%s_%d.lab", strDirPath, strFileTitle, lcount);
		csLabFileMono.Open(strFileName, CFile::modeCreate|CFile::typeText|CFile::modeWrite, NULL);
		timeinfo((int*)cuestart2,(int*)cuelength2); // ky note: the shift giSftIdx is added in class: TextProcess
		GenerateLabelFile(PhoneSeq,SyllableBound,WordBound,PhraseBound,sIndex,wIndex,pIndex, csLabFileFull, &csLabFileMono );
		csLabFileFull.Close();
		csLabFileMono.Close();
	}
	//還要加個串接文字檔
	CString strFileName;
	strFileName.Format("%s\\train", strDirPath );
	ConcatenateLabel( strFileTitle.GetBuffer(0), strFileName.GetBuffer(0), SentenceArray.GetSize() ) ;

	// release memory
	delete [] cuestart ;
	delete [] cuelength ;
	delete [] cuestart2 ;
	delete [] cuelength2 ;
}



// ky add
void TextProcess::ConcatenateLabel( char* outfilename, char* dir, int iSentenceCnt )
{
	FILE *fpMono;
	FILE *fpFull;
	char output[_MAX_PATH];
	char cBuf[1024] ;
	sprintf(output,"%s/mono/%s.lab",dir,outfilename);
	fpMono=fopen(output,"w+");
	sprintf(output,"%s/full/%s.lab",dir,outfilename);
	fpFull=fopen(output,"w+");
	for( int i = 0 ; i < iSentenceCnt ; i++ ){
		sprintf(output,"%s/mono/temp_%s_%d.lab",dir,outfilename, i );
		FILE *fp = fopen( output, "r" ) ;
		while( !feof(fp) ) {
			cBuf[0]=0 ;
			fgets(cBuf,1024,fp) ;
			if( strlen(cBuf) == 0 )
				continue ;
			fprintf( fpMono, "%s", cBuf ) ;
		}
		fclose(fp) ;


		sprintf(output,"%s/full/temp_%s_%d.lab",dir,outfilename, i);
		fp = fopen( output, "r" ) ;
		while( !feof(fp) ) {
			cBuf[0]=0 ;
			fgets(cBuf,1024,fp) ;
			if( strlen(cBuf) == 0 )
				continue ;
			fprintf( fpFull, "%s", cBuf ) ;
		}
		fclose(fp) ;
	}
	fclose(fpMono) ;
	fclose(fpFull) ;
}
//


bool TextProcess::timeinfo(int* duration_si,int* duration_ei)
{
	//	duration_e=duration_ei;
	gduration_e=duration_ei+giSftIdx; // ky modify
	//	duration_s=duration_si;
	gduration_s=duration_si+giSftIdx; // ky modify
	return true;
}


bool TextProcess::initrd()
{
	gduration_s=NULL;
	gduration_e=NULL;
	giSftIdx = 0 ; // ky add
	return true;
}

