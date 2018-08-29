#include "stdafx.h"
#include "common.h"

// 取得2D的陣列
void **Alloc2D(DWORD row, DWORD col, DWORD type_size)
{
	UINT			i, pointer_size;
	void			**p;
	unsigned char	*temp_ptr;
	char			ErrMsg[255];

	col			 *= type_size;
	pointer_size = row*sizeof(void *);

	// 取得2D的頭
	p = (void **)GlobalAllocPtr(GHND, pointer_size);
	if (p == NULL){
		sprintf(ErrMsg, "記憶體不足 %u * %u * %u at row", row, col/type_size, type_size);
		AfxMessageBox(ErrMsg);
		return NULL;
	}

	// 取得記憶體空間
	p[0] = GlobalAllocPtr(GHND, row*col);
	if (p[0] == NULL){
		sprintf(ErrMsg, "記憶體不足 %u * %u * %u at col", row, col/type_size, type_size);
		GlobalFreePtr(p);
		AfxMessageBox(ErrMsg);
		return NULL;
	}

	// 設定每一欄位的頭
	temp_ptr = (unsigned char *)p[0];
	for (i=1; i<row; i++){
		temp_ptr += col;
		p[i] = (void *)temp_ptr;
	}

	return p;
}

// 取得2D的陣列
void **ReAlloc2D(void **InputPtr, DWORD row, DWORD col, DWORD type_size)
{
	UINT			i, pointer_size;
	void			**Head_Ptr;
	unsigned char	*Data_Ptr;
	char			ErrMsg[255];

	//  取得Pointer
	Data_Ptr = (unsigned char *)InputPtr[0];
	Head_Ptr = InputPtr;

	//  計算資料區間
	col			 *= type_size;
	pointer_size = row*sizeof(void *);

	// 取得記憶體空間
	Data_Ptr = (unsigned char *)GlobalReAllocPtr(Data_Ptr, row*col, GMEM_MOVEABLE);
	if (Data_Ptr == NULL){
		GlobalFreePtr(Head_Ptr);
		sprintf(ErrMsg, "記憶體不足 %u * %u * %u at col", row, col/type_size, type_size);
		AfxMessageBox(ErrMsg);
		return NULL;
	}

	// 取得2D的頭
	Head_Ptr = (void **)GlobalReAllocPtr(Head_Ptr, pointer_size, GMEM_MOVEABLE);
	if (Head_Ptr == NULL){
		GlobalFreePtr(Data_Ptr);
		sprintf(ErrMsg, "記憶體不足 %u * %u * %u at row", row, col/type_size, type_size);
		AfxMessageBox(ErrMsg);
		return NULL;
	}

	// 設定每一欄位的頭
	for (i=0; i<row; i++){
		Head_Ptr[i] = Data_Ptr + col*i;
	}

	return Head_Ptr;
}

// 釋放2D的陣列
void Free2D(void **p)
{
	if (GlobalFreePtr(p[0]) != NULL)
		AfxMessageBox("Free2D error of p[0]");
	if (GlobalFreePtr(p) != NULL)
		AfxMessageBox("Free2D error of p");

	p = NULL;
}

// 取得3D的陣列
void ***Alloc3D(UINT layer, UINT row, UINT col, UINT type_size)
{
	UINT i, j;
	long pointer_size, col_size; 
	void ***p;
	void **p1;
	void *p2;
	void **temp_ptr1;
	unsigned char *temp_ptr2;

	// 取得layer & row的頭
	pointer_size = (long)layer*sizeof(void ***);
	p = (void ***)GlobalAllocPtr(GHND, pointer_size);
	if (p == NULL){
		AfxMessageBox("記憶體不足 memory.cpp 3D p");
		return NULL;
	}

	pointer_size = (long)layer*row*sizeof(void **);
	p1 = (void **)GlobalAllocPtr(GHND, pointer_size);
	if (p1 == NULL){
		AfxMessageBox("記憶體不足 memory.cpp 3D p1");
		GlobalFreePtr(p1);
		return NULL;
	}

	// 設定row的頭至layer中
	temp_ptr1 = (void **)p1;
	for (i=0; i<layer; i++){
		p[i] = temp_ptr1;
		temp_ptr1 += row;
	}

	// 配置實際記憶體
	pointer_size = (long)layer*row*col*type_size;
	p2 = (void *)GlobalAllocPtr(GHND, pointer_size);
	if (p2 == NULL){
		AfxMessageBox("記憶體不足 memory.cpp 3D p2");
		GlobalFreePtr(p);
		GlobalFreePtr(p1);
		return NULL;
	}
	col_size = (long)col*type_size;
	temp_ptr2 = (unsigned char *)p2;
	for (i=0; i<layer; i++)
		for (j=0; j<row; j++){
			p[i][j] = (void *)temp_ptr2;
			temp_ptr2 += col_size;
		}

	return p;
}

// 釋放3D的陣列
void Free3D(void ***p)
{
	GlobalFreePtr(p[0][0]);
	GlobalFreePtr(p[0]);
	GlobalFreePtr(p);

	p = NULL;

	_heapmin();
}
