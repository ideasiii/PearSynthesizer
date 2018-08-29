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

// ���o2D���}�C
void **Alloc2D(DWORD row, DWORD col, DWORD type_size);

// ���o2D���}�C
void **ReAlloc2D(void **InputPtr, DWORD row, DWORD col, DWORD type_size);

// ����2D���}�C
void Free2D(void **p);

// ���o3D���}�C
void ***Alloc3D(UINT layer, UINT row, UINT col, UINT type_size);

// ����3D���}�C
void Free3D(void ***p);

#endif