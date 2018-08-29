#ifndef _COMMON_H
#define _COMMON_H

#include <mmsystem.h>
#include <math.h>
#include <windowsx.h>
#include <float.h>
#include <malloc.h>
#include <direct.h>
#include <afxtempl.h>
#include <fcntl.h>
#include <process.h>

// 取得2D的陣列
void **Alloc2D(DWORD row, DWORD col, DWORD type_size);

// 取得2D的陣列
void **ReAlloc2D(void **InputPtr, DWORD row, DWORD col, DWORD type_size);

// 釋放2D的陣列
void Free2D(void **p);

// 取得3D的陣列
void ***Alloc3D(UINT layer, UINT row, UINT col, UINT type_size);

// 釋放3D的陣列
void Free3D(void ***p);

#endif