#ifndef __CART_H
#define __CART_H

#include <afxtempl.h>
#include <math.h>
#include <float.h>

#include <vector>
using namespace std;

typedef struct CART_DATA
{
	int clu;					// �������O
	CUIntArray Att_Catagory;	// ���O���ݩ�, ���ަb����node, �C����Ƴo�Ӱ}�C�����׳��n�۵�
//	CArray<double, double&> Att_Ordered;		// �j�p���ݩ�
//	CArray<double, double&> PerdictionError;	// �P�C�@�s���~�t
} CartData;

class CART_NODE
{
public:
	CART_NODE();
	~CART_NODE();

	CArray<CART_DATA*, CART_DATA*> caDataArray;	// �b�o��node�������
	// �Ydim�p��CART_DATA.Att_Catagory.GetSize(), ��OQuestoin�OCatagory��, ���׬Odim
	// �Ydim�j��CART_DATA.Att_Catagory.GetSize(), ��OQuestion�OOrdered��, ���׬Odim-CART_DATA.Att_Catagory.GetSize()
	int dim;									// ���@question�O�b���Ӻ��פW��
	CUIntArray cuiaQuestion;					// �ҥΪ�Catagory question
	// Ordered question: a<=x<b
	double a;									// �ҥΪ�Ordered question
	double b;									// �ҥΪ�Ordered question
	double dbEntropy;							// �ë�, ���«�
	int clu;									// �o��node�O�ݩ�������O
	double dbProb;								// �O�o�����O�����v��
	CART_NODE *Lchild;							// ���l��
	CART_NODE *Rchild;							// �k�l��
};

// �������O��-1�����D, �Цb��JCART���e�B�z��
// �ثe���bLoadData()���B�z
class CART
{
public:
	// constructor and de-constructor
	CART();
	~CART();
	// public functions
	void LoadTextData(CString csFileName);		// �[�J��@�Ӥ�r�ɮ榡�����
	void LoadTextData(CString csFolder, CString csExt);		// �۹��{������ؿ������|
	void LoadBinaryData(CString csFileName);	// �[�J��@�ӤG�i���ɮ榡�����
	void LoadBinaryData(CString csFolder, CString csExt);	// �۹��{������ؿ������|
	void InitRoot(void);	// ���J����ƫ�, �n�I�s�@�U�o�Ө禡, ��ڸ`�I�i�@�B�@��l��
	void Build(void);		// �ؾ�, ���~���I�s���禡
	double GetAccuracy(void);	// �p������𪺥��T�v
	int GetLeafNodeNum(void);	// �p�⦳�X��leaf node
//	double GetPredictionError(void);	// �p���`�~�t

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
	void GetEntropy(CART_NODE *cnNode);	// ���F�p��o��node��entropy���~, �ٷ|���K�]�wclu
	void KSubSet(CUIntArray *pcuiaSet, CUIntArray *pcuiaSubSet, int k);
	// �ؾ�Ϊ������禡
	void GrowTree(CART_NODE *cnNode);
	// �p�⥿�T�v�Ϊ������禡
	int GetCorrectCount(CART_NODE *cnNode);
	// �p�⦳�X��leaf node�Ϊ������禡
	int GetLeafNodeNum(CART_NODE *cnNode);
	// �p���`�~�t�Ϊ������禡
//	double GetPredictionError(CART_NODE *cnNode);
	// save�Pload�Ϊ������禡
	void SAVE(CART_NODE *cnNode);
	CART_NODE* LOAD(void);
	// test�Ϊ������禡
	void TEST(CART_DATA *cdData, CART_NODE *cnNode);
	// ShowTree�Ϊ������禡
	void InsTreeNode(HTREEITEM RT, CART_NODE *cnNode);

	//Add by Mai-Chun
	BOOL SaveModel(CString csfile);
	BOOL SaveNodeInfo(CStdioFile& cf,CStdioFile& cfText,CART_NODE *cnNode,int nodeID);

	BOOL ConstructCART(int nodeID,vector<int>& IDArray,vector<CART_NODE*>& pNodeData,CART_NODE *pNode);
	BOOL DeleteNode(CART_NODE *cnNode);
};

#endif