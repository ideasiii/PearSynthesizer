#ifndef __CART_H
#define __CART_H

#include <afxtempl.h>
#include <math.h>
#include <float.h>

#include <vector>
using namespace std;

typedef struct CART_DATA
{
	int clu;					// 所屬類別
	CUIntArray Att_Catagory;	// 類別型屬性, 不管在哪個node, 每筆資料這個陣列的長度都要相等
//	CArray<double, double&> Att_Ordered;		// 大小型屬性
//	CArray<double, double&> PerdictionError;	// 與每一群的誤差
} CartData;

class CART_NODE
{
public:
	CART_NODE();
	~CART_NODE();

	CArray<CART_DATA*, CART_DATA*> caDataArray;	// 在這個node中的資料
	// 若dim小於CART_DATA.Att_Catagory.GetSize(), 表是Questoin是Catagory的, 維度是dim
	// 若dim大於CART_DATA.Att_Catagory.GetSize(), 表是Question是Ordered的, 維度是dim-CART_DATA.Att_Catagory.GetSize()
	int dim;									// 此一question是在哪個維度上的
	CUIntArray cuiaQuestion;					// 所用的Catagory question
	// Ordered question: a<=x<b
	double a;									// 所用的Ordered question
	double b;									// 所用的Ordered question
	double dbEntropy;							// 亂度, 不純度
	int clu;									// 這個node是屬於哪個類別
	double dbProb;								// 是這個類別的機率值
	CART_NODE *Lchild;							// 左子樹
	CART_NODE *Rchild;							// 右子樹
};

// 關於有類別為-1的問題, 請在輸入CART之前處理掉
// 目前仍在LoadData()中處理
class CART
{
public:
	// constructor and de-constructor
	CART();
	~CART();
	// public functions
	void LoadTextData(CString csFileName);		// 加入單一個文字檔格式的資料
	void LoadTextData(CString csFolder, CString csExt);		// 相對於程式執行目錄的路徑
	void LoadBinaryData(CString csFileName);	// 加入單一個二進位檔格式的資料
	void LoadBinaryData(CString csFolder, CString csExt);	// 相對於程式執行目錄的路徑
	void InitRoot(void);	// 載入完資料後, 要呼叫一下這個函式, 對根節點進一步作初始化
	void Build(void);		// 建樹, 給外部呼叫的函式
	double GetAccuracy(void);	// 計算整顆樹的正確率
	int GetLeafNodeNum(void);	// 計算有幾個leaf node
//	double GetPredictionError(void);	// 計算總誤差

	CTreeCtrl *pTreeCtrl;
	void ShowTree(CTreeCtrl *pTreeCtrlX);

	CART_NODE *cnRoot;

	// save and load
	CStdioFile slfile;
	void SAVE(CString csFileName);
	void LOAD(CString csFileName);

	// test
	void TEST(CART_DATA *cdData);

	//Add by Mai-Chun
	BOOL SaveCARTModel(CString csfile);
	BOOL LoadCARTModel(CString csfile);
	int GetCluID(CUIntArray& lingfeature);
	vector<CART_NODE*> gnodeArray;	// ky add: i doubt that not all the CART_NODEs in 
									//	the vector are added to tree, so release node 
									//	using the global vector, not the tree


private:
	// private functions
	void GetEntropy(CART_NODE *cnNode);	// 除了計算這個node的entropy之外, 還會順便設定clu
	void KSubSet(CUIntArray *pcuiaSet, CUIntArray *pcuiaSubSet, int k);
	// 建樹用的內部函式
	void GrowTree(CART_NODE *cnNode);
	// 計算正確率用的內部函式
	int GetCorrectCount(CART_NODE *cnNode);
	// 計算有幾個leaf node用的內部函式
	int GetLeafNodeNum(CART_NODE *cnNode);
	// 計算總誤差用的內部函式
//	double GetPredictionError(CART_NODE *cnNode);
	// save與load用的內部函式
	void SAVE(CART_NODE *cnNode);
	CART_NODE* LOAD(void);
	// test用的內部函式
	void TEST(CART_DATA *cdData, CART_NODE *cnNode);
	// ShowTree用的內部函式
	void InsTreeNode(HTREEITEM RT, CART_NODE *cnNode);

	//Add by Mai-Chun
	BOOL SaveModel(CString csfile);
	BOOL SaveNodeInfo(CStdioFile& cf,CStdioFile& cfText,CART_NODE *cnNode,int nodeID);

	BOOL ConstructCART(int nodeID,vector<int>& IDArray,vector<CART_NODE*>& pNodeData,CART_NODE *pNode);
	BOOL DeleteNode(CART_NODE *cnNode);
};

#endif