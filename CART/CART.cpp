#include "stdafx.h"
#include "CART.h"

///////////////////////////////////////////////////////////
//  CART_NODE
CART_NODE::CART_NODE()
{
	caDataArray.RemoveAll();
	dim = -1;
	cuiaQuestion.RemoveAll();
	dbEntropy = DBL_MAX;
	clu = -1;
	dbProb = 0;
	Lchild = NULL;
	Rchild = NULL;
	a = 0.0;
	b = 0.0;
}

CART_NODE::~CART_NODE()
{
	// 釋放Lchild與Rchild要與CART的deconstructor一起寫
	caDataArray.RemoveAll();
	cuiaQuestion.RemoveAll();
}

///////////////////////////////////////////////////////////
//  CART
CART::CART()
{
	// 初始化根節點
	cnRoot = new CART_NODE();
}

CART::~CART()
{
	// 拜訪這棵樹的每個節點, 將所有的資料釋放
// 	DeleteNode(cnRoot); // ky add: release memory
	for( int i = 0 ; i < gnodeArray.size() ; i++ )
		delete gnodeArray[i] ;
}

void CART::LoadTextData(CString csFileName)
{
	CStdioFile csfFile;
	csfFile.Open(csFileName, CFile::modeRead|CFile::typeText, NULL);

	CString cstemp = "";
	char seps[] = " \n";
	char *token = NULL;
	while(csfFile.ReadString(cstemp)){
		token = strtok(cstemp.GetBuffer(0), seps);
		// 去除類別為-1者
		if(atoi(token)==-1) continue;
		if(token){
			CART_DATA *pcdData = new CART_DATA();
			pcdData->clu = atoi(token);
			token = strtok(NULL, seps);
			int count = 0;
			while(token != NULL){
				pcdData->Att_Catagory.Add(atoi(token));
				token = strtok(NULL, seps);
				count++;
				if(count == 9) break;	// 用來控制只用0~8維的attributes
			}
			cnRoot->caDataArray.Add(pcdData);
		}
	}
	csfFile.Close();
}

void CART::LoadTextData(CString csFolder, CString csExt)
{
	// 切換到指定目錄
	SetCurrentDirectory(csFolder);

	CFileFind finder;
	BOOL find = FALSE;
	CString csFileName;

	find = finder.FindFile("*."+csExt, 0);
	while( find ){
		find = finder.FindNextFile();		// 移到下一個檔案
		csFileName = finder.GetFileName();	// 找到檔名
		LoadTextData(csFileName);
	}
}

void CART::LoadBinaryData(CString csFileName)
//==================================//
//===加入單一個二進位檔格式的資料===//
//==================================//
{
	int i, j;
	CStdioFile csfFile;
	csfFile.Open(csFileName, CFile::modeRead|CFile::typeBinary, NULL);

	int nDataNum;
	csfFile.Read(&nDataNum, sizeof(int));
	int Dim_Att_Catagory;
	csfFile.Read(&Dim_Att_Catagory, sizeof(int));
	int Dim_Att_Ordered = 0;
//	csfFile.Read(&Dim_Att_Ordered, sizeof(int));
	int nClu;
	csfFile.Read(&nClu, sizeof(int));

	for(i=0; i<nDataNum; i++){
		int clu;
		csfFile.Read(&clu, sizeof(int));

		unsigned int a;
		double b;
		// 去除類別為-1者
		if(clu==-1){
			for(j=0; j<Dim_Att_Catagory; j++){
				csfFile.Read(&a, sizeof(unsigned int));
			}
			for(j=0; j<Dim_Att_Ordered; j++){
				csfFile.Read(&b, sizeof(double));
			}
			for(j=0; j<nClu; j++){
				csfFile.Read(&b, sizeof(double));
			}
		}else{
			CART_DATA *pcdData = new CART_DATA();
			pcdData->clu = clu;
			//int count = 0;
			for(j=0; j<Dim_Att_Catagory; j++){
				csfFile.Read(&a, sizeof(unsigned int));
				//if(count<14){
					pcdData->Att_Catagory.Add(a);
				//}
				//count++;
			}
/*			for(j=0; j<Dim_Att_Ordered; j++){
				csfFile.Read(&b, sizeof(double));
				pcdData->Att_Ordered.Add(b);
			}*/
/*			for(j=0; j<nClu; j++){
				csfFile.Read(&b, sizeof(double));
				pcdData->PerdictionError.Add(b);
			}*/
			cnRoot->caDataArray.Add(pcdData);
		}
	}
	csfFile.Close();
}

void CART::LoadBinaryData(CString csFolder, CString csExt)
{
	// 切換到指定目錄
	SetCurrentDirectory(csFolder);

	CFileFind finder;
	BOOL find = FALSE;
	CString csFileName;

	find = finder.FindFile("*."+csExt, 0);
	while( find ){
		find = finder.FindNextFile();		// 移到下一個檔案
		csFileName = finder.GetFileName();	// 找到檔名
		LoadBinaryData(csFileName);
	}
}

void CART::InitRoot(void)
{
	// 計算Entropy
	GetEntropy(cnRoot);
}

void CART::GetEntropy(CART_NODE *cnNode)
{
	double entropy = DBL_MAX;
	int i=0, j=0;
	CUIntArray cuiaCluArray;
	cuiaCluArray.RemoveAll();
	CUIntArray cuiaCountArray;
	cuiaCountArray.RemoveAll();
	unsigned int clu;

	// 找出這個node中, 出現了哪些類別, 並同時計算每種類別出現的次數
	for(i=0; i<cnNode->caDataArray.GetSize(); i++){
		clu = cnNode->caDataArray[i]->clu;
		BOOL flag = FALSE;	// 預設是一個沒出現過的clu
		for(j=0; j<cuiaCluArray.GetSize(); j++){
			if(clu == cuiaCluArray[j]){
				flag = TRUE;			// 如果是出現過的, 將旗標設為TRUE
				cuiaCountArray[j]++;	// 對應的次數加一
			}
		}
		if(!flag){	// 如果真是一個沒出現過的
			cuiaCluArray.Add(clu);	// 把這個類別加入
			cuiaCountArray.Add(1);	// 次數是一次
		}
	}

	// 計算entropy, 並找到出現次數最多的clu與其對應的機率值
	entropy = 0;
	double N = cnNode->caDataArray.GetSize();
	clu = -1;
	double prob = 0;
	for(i=0; i<cuiaCluArray.GetSize(); i++){
		double n = cuiaCountArray[i];
		double p = n/N;
		entropy += -p*(log(p)/log(2.0));	// 用了換底公式, 將log的底換成base-2

		if( p > prob ){
			clu = cuiaCluArray[i];
			prob = p;
		}
	}

	// 設定entropy
	cnNode->dbEntropy = entropy;
	// 設定clu
	cnNode->clu = clu;
	// 設定dbProb
	cnNode->dbProb = prob;
}

void CART::Build(void)
{
	GrowTree(cnRoot);
}

void CART::GrowTree(CART_NODE *cnNode)
{
	int i=0, j=0, k=0, t=0, s=0;

	// 請保證至少有一筆資料
	int nDimensions = cnNode->caDataArray[0]->Att_Catagory.GetSize();

	// 計算cnNode的預測誤差
	int clu = cnNode->clu;
/*	double Error = 0.0;
	for(i=0; i<cnNode->caDataArray.GetSize(); i++){
		Error += cnNode->caDataArray[i]->PerdictionError[clu];
	}*/

	double Ratio = 0.0;

	// 對每個維度去找
	for(i=0; i<nDimensions; i++){
		//-----------------------//
		// 先找出第i維的所有可能 //
		//-----------------------//
		CUIntArray cuiaAllPossible;		// 紀錄所有出現過的attribute
		cuiaAllPossible.RemoveAll();
		for(j=0; j<cnNode->caDataArray.GetSize(); j++){
			unsigned int att = cnNode->caDataArray[j]->Att_Catagory[i];		// 第j筆資料的第i維attribute
			BOOL flag = FALSE;	//預設是一個沒出現過的att
			for(k=0; k<cuiaAllPossible.GetSize(); k++){
				if( att == cuiaAllPossible[k] ){
					flag = TRUE;	// 如果是出現過的, 將旗標設為TRUE
				}
			}
			if(!flag){
				// 如果是沒出現過的
				cuiaAllPossible.Add(att);
			}
		}

		// 產生所有的Question
		CUIntArray cuiaSubSet;
		double n = cuiaAllPossible.GetSize();
		for(k=1; k<=floor(n/2.0); k++){
			if( n>10 && k>3 ){
				break;
			}
			KSubSet(&cuiaAllPossible, &cuiaSubSet, k);
			// cuiaSubSet是長度為k的Question的集合
			// 所以存取的時候要每k個拿出來當Question
			for(j=0; (j<cuiaSubSet.GetSize()) && ((j+k)<cuiaSubSet.GetSize()); j+=k){
				// cuiaSubSet[j]~cuiaSubSet[j+k-1]算一個Question

				// 準備左右子節點
				CART_NODE *Lchild = new CART_NODE();
				CART_NODE *Rchild = new CART_NODE();

				// 對所有資料開始分到左右子節點
				for(t=0; t<cnNode->caDataArray.GetSize(); t++){
					unsigned int att = cnNode->caDataArray[t]->Att_Catagory[i];
					BOOL flag = FALSE;	// 預設是不在這Question中
					for(s=j; s<j+k; s++){
						if(att == cuiaSubSet[s]){
							flag = TRUE;
							break;
						}
					}
					if(flag){
						// 住左邊
						Lchild->caDataArray.Add(cnNode->caDataArray[t]);
					}else{
						// 住右邊
						Rchild->caDataArray.Add(cnNode->caDataArray[t]);
					}
				}
				// 檢測Entropy
				GetEntropy(Lchild);
				GetEntropy(Rchild);
				double Le = Lchild->dbEntropy;
				double Re = Rchild->dbEntropy;
				double pL = Lchild->caDataArray.GetSize();
				double pR = Rchild->caDataArray.GetSize();
				pL /= (double)cnNode->caDataArray.GetSize();
				pR /= (double)cnNode->caDataArray.GetSize();
				double ReducedEntropyRatio = (cnNode->dbEntropy - pL*Le - pR*Re)/cnNode->dbEntropy;
				// 檢測預測誤差
//				double Lr = GetPredictionError(Lchild);	
//				double Rr = GetPredictionError(Rchild);	
//				double ReducedErrorRatio = (Error - Lr - Rr)/Error;	
				//double beta = 1.0;
				//ReducedEntropyRatio = pow(ReducedEntropyRatio, beta);
				//ReducedErrorRatio = pow(ReducedErrorRatio, 1.0-beta);
				// 記錄結果
				//if( ReducedEntropyRatio*ReducedErrorRatio>Ratio && ReducedErrorRatio>0 && ReducedEntropyRatio>0){
				if( ReducedEntropyRatio>Ratio ){
					//Ratio = ReducedEntropyRatio*ReducedErrorRatio;
					Ratio = ReducedEntropyRatio;
					// 紀錄dimension
					cnNode->dim = i;
					// 紀錄Question
					cnNode->cuiaQuestion.RemoveAll();
					for(s=j; s<j+k; s++){
						cnNode->cuiaQuestion.Add(cuiaSubSet[s]);
					}
					// 紀錄左右節點
					cnNode->Lchild = Lchild;
					cnNode->Rchild = Rchild;
				}else{
					Lchild->~CART_NODE();
					Rchild->~CART_NODE();
					delete Lchild;
					delete Rchild;
				}
			}
		}
	}
/*
	nDimensions = cnNode->caDataArray[0]->Att_Ordered.GetSize();

	// 對每個Ordered Attribute去找
	for(i=0; i<nDimensions; i++){
		// 先找出這一維的最大與最小值
		double Max = -DBL_MAX;
		double Min = DBL_MAX;
		for(j=0; j<cnNode->caDataArray.GetSize(); j++){
			double att_ordered = cnNode->caDataArray[j]->Att_Ordered[i];
			if( att_ordered > Max ){
				Max = att_ordered;
			}
			if( att_ordered < Min ){
				Min = att_ordered;
			}
		}

		// 訂出step size
		double step = (Max-Min)/(double)cnNode->caDataArray.GetSize()*5;

		// 用兩層迴圈搜尋最佳的interval
		for(double a=Min; a<=Max; a+=step){
			for(double b=a+step; b<=Max+step; b+=step){
				// a<= x <b算一個Question

				// 準備左右子節點
				CART_NODE *Lchild = new CART_NODE();
				CART_NODE *Rchild = new CART_NODE();

				// 對所有的資料開始分到左右子節點
				for(j=0; j<cnNode->caDataArray.GetSize(); j++){
					double att = cnNode->caDataArray[j]->Att_Ordered[i];
					if( (a<=att) && (att<b) ){
						// 住左邊
						Lchild->caDataArray.Add(cnNode->caDataArray[j]);
					}else{
						// 住右邊
						Rchild->caDataArray.Add(cnNode->caDataArray[j]);
					}
				}

				// 先確定左右兩邊都有人住
				if( !(Lchild->caDataArray.GetSize()) || !(Rchild->caDataArray.GetSize()) ){
					Lchild->~CART_NODE();
					Rchild->~CART_NODE();
					delete Lchild;
					delete Rchild;
					continue;
				}

				// 檢測Entropy
				GetEntropy(Lchild);
				GetEntropy(Rchild);
				double Le = Lchild->dbEntropy;
				double Re = Rchild->dbEntropy;
				double pL = Lchild->caDataArray.GetSize();
				double pR = Rchild->caDataArray.GetSize();
				pL /= (double)cnNode->caDataArray.GetSize();
				pR /= (double)cnNode->caDataArray.GetSize();
				// 記錄結果
				double tempDiffEntropy = cnNode->dbEntropy - pL*Le - pR*Re;
				if(tempDiffEntropy > DiffEntropy){
					DiffEntropy = tempDiffEntropy;
					// 紀錄Dim
					cnNode->dim = i+cnNode->caDataArray[0]->Att_Catagory.GetSize();
					// 紀錄Question
					cnNode->a = a;
					cnNode->b = b;
					// 紀錄左右子節點
					cnNode->Lchild = Lchild;
					cnNode->Rchild = Rchild;
				}else{
					Lchild->~CART_NODE();
					Rchild->~CART_NODE();
					delete Lchild;
					delete Rchild;
				}
			}
		}
	}
*/
	// 繼續往下長
	//if(cnNode->dim>=cnNode->caDataArray[0]->Att_Catagory.GetSize()){
	//	cnNode->cuiaQuestion.RemoveAll();
	//}
	if(cnNode->Lchild){
		if(cnNode != cnRoot){
			cnNode->caDataArray.RemoveAll();
		}
		if(cnNode->Lchild->caDataArray.GetSize()>60){
			GrowTree(cnNode->Lchild);
		}
	}
	if(cnNode->Rchild){
		if(cnNode != cnRoot){
			cnNode->caDataArray.RemoveAll();
		}
		if(cnNode->Rchild->caDataArray.GetSize()>60){
			GrowTree(cnNode->Rchild);
		}
	}
}

void CART::KSubSet(CUIntArray *pcuiaSet, CUIntArray *pcuiaSubSet, int k)
//==========================================//
//===取用的時候, 是每k個從pcuiaSubSet取出===//
//==========================================//
{
	pcuiaSubSet->RemoveAll();

	int n = pcuiaSet->GetSize();
	int position;
	int i, j;

	int *set = (int*)calloc(n, sizeof(int));

	for(i=0; i<k; i++)
		set[i] = i+1;

	for(j=0; j<k; j++)
		pcuiaSubSet->Add(pcuiaSet->GetAt(set[j]-1));

	position = k-1;
	while(1){
		if(set[k-1]==n)
			position--;
		else
			position = k-1;
		set[position]++;
		for(i=position+1; i<k; i++)
			set[i] = set[i-1] + 1;

		for(j=0; j<k; j++)
			pcuiaSubSet->Add(pcuiaSet->GetAt(set[j]-1));

		if(set[0] >= n-k+1)
			break;
	}
}

void CART::ShowTree(CTreeCtrl *pTreeCtrlX)
//==================================//
//===用來在圖形介面上秀出建好的樹===//
//==================================//
{
	pTreeCtrl = pTreeCtrlX;
	pTreeCtrl->DeleteAllItems();
	InsTreeNode(TVI_ROOT, cnRoot);
}

void CART::InsTreeNode(HTREEITEM RT, CART_NODE *cnNode)
{
	CString csTemp1 = "";
	CString csTemp2 = "";

	// 所用的Question
	csTemp1.Format("Dim %d:", cnNode->dim);
	for(int i=0; i<cnNode->cuiaQuestion.GetSize(); i++){
		csTemp2.Format(" %d", cnNode->cuiaQuestion[i]);
		csTemp1 += csTemp2;
	}
	csTemp1 += "; ";

	// 機率值
	csTemp2.Format("Prob: %f", cnNode->dbProb);
	csTemp1 += csTemp2;
	csTemp1 += "; ";

	// 資料個數
	csTemp2.Format("資料個數: %d", cnNode->caDataArray.GetSize());
	csTemp1 += csTemp2;

	HTREEITEM newRT = pTreeCtrl->InsertItem(csTemp1, RT, TVI_LAST);
	pTreeCtrl->Expand(newRT,TVE_EXPAND);

	if(cnNode->Rchild){
		InsTreeNode(newRT, cnNode->Rchild);
	}
	if(cnNode->Lchild){
		InsTreeNode(newRT, cnNode->Lchild);
	}
}

double CART::GetAccuracy(void)
//========================//
//===計算整顆樹的正確率===//
//========================//
{
	double count = GetCorrectCount(cnRoot);
	return count/(double)(cnRoot->caDataArray.GetSize());
}

int CART::GetCorrectCount(CART_NODE *cnNode)
//============================//
//===計算正確率用的內部函式===//
//============================//
{
	// 只有當左右都沒有小孩時, 才會是一個leaf node
	// 但不會發生只有一邊有, 另一邊沒有的情形
	// 所以只要檢查其中一邊, 如果是NULL, 那就會是leaf node
	int count = 0;
	if(cnNode->Lchild){
		count += GetCorrectCount(cnNode->Lchild);
		count += GetCorrectCount(cnNode->Rchild);
	}else{
		for(int i=0; i<cnNode->caDataArray.GetSize(); i++){
			if(cnNode->caDataArray[i]->clu == cnNode->clu){
				count++;
			}
		}
	}
	return count;
}

int CART::GetLeafNodeNum(void)
//=========================//
//===計算有幾個leaf node===//
//=========================//
{
	return GetLeafNodeNum(cnRoot);
}

int CART::GetLeafNodeNum(CART_NODE *cnNode)
{
	int num = 0;
	if(cnNode->Lchild){
		num += GetLeafNodeNum(cnNode->Lchild);
		num += GetLeafNodeNum(cnNode->Rchild);
	}else{
		num = 1;
	}
	return num;
}

/*double CART::GetPredictionError(void)
//================//
//===計算總誤差===//
//================//
{
	return GetPredictionError(cnRoot);
}

double CART::GetPredictionError(CART_NODE *cnNode)
{
	double result = 0.0;
	if(cnNode->Lchild){
		result += GetPredictionError(cnNode->Lchild);
		result += GetPredictionError(cnNode->Rchild);
	}else{
		int clu = cnNode->clu;
		for(int i=0; i<cnNode->caDataArray.GetSize(); i++){
			result += cnNode->caDataArray[i]->PerdictionError[clu];
		}
	}
	return result;
}*/

void CART::SAVE(CString csFileName)
{
	slfile.Open(csFileName, CFile::modeCreate|CFile::modeReadWrite|CFile::typeBinary, NULL);

	if(cnRoot){
		SAVE(cnRoot);
	}

	slfile.Close();
}

void CART::SAVE(CART_NODE *cnNode)
{
	int a = 0;
	unsigned int b = 0;
	double c = 0.0;
	BOOL d = FALSE;

	// 先存自己本身
	a = cnNode->dim;
	slfile.Write(&a, sizeof(int));
	a = cnNode->cuiaQuestion.GetSize();
	slfile.Write(&a, sizeof(int));
	for(int i=0; i<a; i++){
		b = cnNode->cuiaQuestion[i];
		slfile.Write(&b, sizeof(unsigned int));
	}
	c = cnNode->a;
	slfile.Write(&c, sizeof(double));
	c = cnNode->b;
	slfile.Write(&c, sizeof(double));
	c = cnNode->dbEntropy;
	slfile.Write(&c, sizeof(double));
	a = cnNode->clu;
	slfile.Write(&a, sizeof(int));
	c = cnNode->dbProb;
	slfile.Write(&c, sizeof(double));
	if(cnNode->Lchild){
		d = TRUE;
	}else{
		d = FALSE;
	}
	slfile.Write(&d, sizeof(BOOL));
	if(cnNode->Rchild){
		d = TRUE;
	}else{
		d = FALSE;
	}
	slfile.Write(&d, sizeof(BOOL));

	// 存左子樹
	if(cnNode->Lchild){
		SAVE(cnNode->Lchild);
	}
	// 存右子樹
	if(cnNode->Rchild){
		SAVE(cnNode->Rchild);
	}
}

void CART::LOAD(CString csFileName)
{
	slfile.Open(csFileName, CFile::modeRead|CFile::typeBinary, NULL);
	slfile.Close();
}

CART_NODE* CART::LOAD(void)
{
	int a = 0;
	unsigned int b = 0;
	double c = 0.0;
	BOOL d1 = FALSE;
	BOOL d2 = FALSE;

	CART_NODE *cnNode = new CART_NODE();

	slfile.Read(&a, sizeof(int));
	cnNode->dim = a;
	slfile.Read(&a, sizeof(int));
	cnNode->cuiaQuestion.RemoveAll();
	for(int i=0; i<a; i++){
		slfile.Read(&b, sizeof(unsigned int));
		cnNode->cuiaQuestion.Add(b);
	}
	slfile.Read(&c, sizeof(double));
	cnNode->a = c;
	slfile.Read(&c, sizeof(double));
	cnNode->b = c;
	slfile.Read(&c, sizeof(double));
	cnNode->dbEntropy = c;
	slfile.Read(&a, sizeof(int));
	cnNode->clu;
	slfile.Read(&c, sizeof(double));
	cnNode->dbProb = c;

	slfile.Read(&d1, sizeof(BOOL));
	slfile.Read(&d2, sizeof(BOOL));
	if(d1){
		cnNode->Lchild = LOAD();
	}else{
		cnNode->Lchild = NULL;
	}
	if(d2){
		cnNode->Lchild = LOAD();
	}else{
		cnNode->Lchild = NULL;
	}

	return cnNode;
}

void CART::TEST(CART_DATA *cdData)
{
	TEST(cdData, cnRoot);
}

void CART::TEST(CART_DATA *cdData, CART_NODE *cnNode)
{
	BOOL flag = FALSE;
	if(cnNode->Lchild){
		for(int i=0; i<cnNode->cuiaQuestion.GetSize(); i++){
			if(cnNode->cuiaQuestion.GetAt(i) == cdData->Att_Catagory.GetAt(cnNode->dim)){
				flag = TRUE;
				break;
			}
		}
		if(flag){
			TEST(cdData, cnNode->Lchild);
		}else{
			TEST(cdData, cnNode->Rchild);
		}
	}else{
		cdData->clu = cnNode->clu;
	}
}

BOOL CART::DeleteNode(CART_NODE *cnNode)
{
	if( cnNode->Lchild != NULL )
	{
		DeleteNode(cnNode->Lchild) ;
		delete cnNode->Lchild;
	}
	if( cnNode->Rchild != NULL )
	{
		DeleteNode(cnNode->Rchild) ;
		delete cnNode->Rchild;
	}

	return TRUE ;
}


BOOL CART::SaveCARTModel(CString csfile)
{
	return SaveModel(csfile);
}

BOOL CART::SaveModel(CString csfile)
{
	CStdioFile cf,cfText;
	CString str;

	str.Format("%s.xls",csfile.SpanExcluding("."));
	VERIFY(cfText.Open(str,CFile::modeCreate|CFile::modeWrite|CFile::typeText));
	str.Format("NodeID\t CluID\t fDim\t QSize\n");
	cfText.WriteString(str);

	str.Format("%s",csfile);
	VERIFY(cf.Open(str,CFile::modeCreate|CFile::modeWrite|CFile::typeBinary));

	SaveNodeInfo(cf,cfText,cnRoot,1);
	cf.Close();
	cfText.Close();
	return TRUE;
}

BOOL CART::SaveNodeInfo(CStdioFile& cf,CStdioFile& cfText,CART_NODE *cnNode,int nodeID)
{
	//針對每個Node記錄相關資訊
	//load後，Source Data以每個Node的ID來推算其所對應LeafNode 再得其clu
	//Save時沒將所有資料存出

	int i;
	int clu = cnNode->clu;
	int size = cnNode->cuiaQuestion.GetSize();
	cf.Write(&nodeID,sizeof(int));
	cf.Write(&clu,sizeof(int));
	cf.Write(&cnNode->dim,sizeof(int));
	cf.Write(&size,sizeof(int));
	for(i=0;i<cnNode->cuiaQuestion.GetSize();i++)	cf.Write(&cnNode->cuiaQuestion[i],sizeof(unsigned int));

	CString str,stmp;
	stmp="";
	for(i=0;i<cnNode->cuiaQuestion.GetSize();i++)
	{
		str.Format("%d\t",cnNode->cuiaQuestion[i]);
		stmp+=str;
	}
	cfText.WriteString(stmp+"\n");
	
	if(cnNode->Lchild!=NULL)	SaveNodeInfo(cf,cfText,cnNode->Lchild,2*nodeID);
	if(cnNode->Rchild!=NULL)	SaveNodeInfo(cf,cfText,cnNode->Rchild,2*nodeID+1);
	return TRUE;
}

BOOL CART::LoadCARTModel(CString csfile)
{
	DeleteNode(cnRoot);

	int i;
	vector<CART_NODE*> nodeArray;
	vector<int>  nodeIDArray;
	CStdioFile cf;
	VERIFY(cf.Open(csfile,CFile::modeRead|CFile::typeBinary));
	while(cf.GetPosition()!=cf.GetLength())
	{
		CART_NODE *node = new CART_NODE();
		int nodeID,size;
		cf.Read(&nodeID,sizeof(int));
		cf.Read(&node->clu,sizeof(int));
		cf.Read(&node->dim,sizeof(int));
		cf.Read(&size,sizeof(int));
		for(i=0;i<size;i++)
		{
			unsigned int t;
			cf.Read(&t,sizeof(unsigned int));
			node->cuiaQuestion.Add(t);
		}
		nodeIDArray.push_back(nodeID);
// 		nodeArray.push_back(node);
		gnodeArray.push_back(node); // ky modify: for avoiding lose of nodes that are not added to tree
	}
	cf.Close();

// 	cnRoot = nodeArray[0];
	if( cnRoot != NULL ) // ky modify: cnRoot is assigned a new CART_NODE in constructor
		delete cnRoot ;
	cnRoot = gnodeArray[0];
// 	ConstructCART(1,nodeIDArray,nodeArray,cnRoot);
	ConstructCART(1,nodeIDArray,gnodeArray,cnRoot); // ky modify
	return TRUE;
}

BOOL CART::ConstructCART(int nodeID,vector<int>& IDArray,vector<CART_NODE*>& pNodeData,CART_NODE *pNode)
{
	int lid = nodeID*2;
	int rid = nodeID*2+1;
	
	int index;
	for(index=0;index<IDArray.size();index++)
	{
		if(IDArray[index]==lid)
		{
			pNode->Lchild=pNodeData[index];
			ConstructCART(lid,IDArray,pNodeData,pNode->Lchild);
			break;
		}
	}
	for(index=0;index<IDArray.size();index++)
	{
		if(IDArray[index]==rid)
		{
			pNode->Rchild=pNodeData[index];
			ConstructCART(rid,IDArray,pNodeData,pNode->Rchild);
			break;
		}
	}
	return FALSE;
}

int CART::GetCluID(CUIntArray& feature)
{
	int i;
	CART_NODE *pNow = cnRoot;
	while (pNow->Lchild!=NULL || pNow->Rchild!=NULL)
	{
		int fdim = pNow->dim;

		unsigned int qvalue = feature[fdim];
		BOOL bIsTrue = false ;
		for (i=0;i<pNow->cuiaQuestion.GetSize();i++)
		{
			if(qvalue==pNow->cuiaQuestion[i])	
			{ 
				bIsTrue = TRUE ; 
				break ;
			}
		}

		if(bIsTrue)	pNow = pNow->Lchild;
		else		pNow = pNow->Rchild;
	}
	return pNow->clu;
}