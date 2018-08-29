# Microsoft Developer Studio Project File - Name="PearSynthesizer" - Package Owner=<4>
# Microsoft Developer Studio Generated Build File, Format Version 6.00
# ** DO NOT EDIT **

# TARGTYPE "Win32 (x86) Application" 0x0101

CFG=PearSynthesizer - Win32 Debug
!MESSAGE This is not a valid makefile. To build this project using NMAKE,
!MESSAGE use the Export Makefile command and run
!MESSAGE 
!MESSAGE NMAKE /f "PearSynthesizer.mak".
!MESSAGE 
!MESSAGE You can specify a configuration when running NMAKE
!MESSAGE by defining the macro CFG on the command line. For example:
!MESSAGE 
!MESSAGE NMAKE /f "PearSynthesizer.mak" CFG="PearSynthesizer - Win32 Debug"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "PearSynthesizer - Win32 Release" (based on "Win32 (x86) Application")
!MESSAGE "PearSynthesizer - Win32 Debug" (based on "Win32 (x86) Application")
!MESSAGE 

# Begin Project
# PROP AllowPerConfigDependencies 0
# PROP Scc_ProjName ""
# PROP Scc_LocalPath ""
CPP=cl.exe
MTL=midl.exe
RSC=rc.exe

!IF  "$(CFG)" == "PearSynthesizer - Win32 Release"

# PROP BASE Use_MFC 6
# PROP BASE Use_Debug_Libraries 0
# PROP BASE Output_Dir "Release"
# PROP BASE Intermediate_Dir "Release"
# PROP BASE Target_Dir ""
# PROP Use_MFC 6
# PROP Use_Debug_Libraries 0
# PROP Output_Dir "Release"
# PROP Intermediate_Dir "Release"
# PROP Ignore_Export_Lib 0
# PROP Target_Dir ""
# ADD BASE CPP /nologo /MD /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_AFXDLL" /Yu"stdafx.h" /FD /c
# ADD CPP /nologo /MD /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_AFXDLL" /D "_MBCS" /FR /Yu"stdafx.h" /FD /c
# ADD BASE MTL /nologo /D "NDEBUG" /mktyplib203 /win32
# ADD MTL /nologo /D "NDEBUG" /mktyplib203 /win32
# ADD BASE RSC /l 0x404 /d "NDEBUG" /d "_AFXDLL"
# ADD RSC /l 0x404 /d "NDEBUG" /d "_AFXDLL"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 /nologo /subsystem:windows /machine:I386
# ADD LINK32 winmm.lib /nologo /subsystem:windows /machine:I386

!ELSEIF  "$(CFG)" == "PearSynthesizer - Win32 Debug"

# PROP BASE Use_MFC 6
# PROP BASE Use_Debug_Libraries 1
# PROP BASE Output_Dir "Debug"
# PROP BASE Intermediate_Dir "Debug"
# PROP BASE Target_Dir ""
# PROP Use_MFC 6
# PROP Use_Debug_Libraries 1
# PROP Output_Dir "Debug"
# PROP Intermediate_Dir "Debug"
# PROP Ignore_Export_Lib 0
# PROP Target_Dir ""
# ADD BASE CPP /nologo /MDd /W3 /Gm /GX /ZI /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /D "_AFXDLL" /Yu"stdafx.h" /FD /GZ /c
# ADD CPP /nologo /MDd /W3 /Gm /GX /ZI /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /D "_AFXDLL" /D "_MBCS" /FR /Yu"stdafx.h" /FD /GZ /c
# ADD BASE MTL /nologo /D "_DEBUG" /mktyplib203 /win32
# ADD MTL /nologo /D "_DEBUG" /mktyplib203 /win32
# ADD BASE RSC /l 0x404 /d "_DEBUG" /d "_AFXDLL"
# ADD RSC /l 0x404 /d "_DEBUG" /d "_AFXDLL"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 /nologo /subsystem:windows /debug /machine:I386 /pdbtype:sept
# ADD LINK32 winmm.lib /nologo /subsystem:windows /debug /machine:I386 /pdbtype:sept

!ENDIF 

# Begin Target

# Name "PearSynthesizer - Win32 Release"
# Name "PearSynthesizer - Win32 Debug"
# Begin Group "Source Files"

# PROP Default_Filter "cpp;c;cxx;rc;def;r;odl;idl;hpj;bat"
# Begin Source File

SOURCE=.\PearSynthesizer.cpp
# End Source File
# Begin Source File

SOURCE=.\PearSynthesizer.rc
# End Source File
# Begin Source File

SOURCE=.\PearSynthesizerDlg.cpp
# End Source File
# Begin Source File

SOURCE=.\StdAfx.cpp
# ADD CPP /Yc"stdafx.h"
# End Source File
# Begin Source File

SOURCE=.\TextProcess.cpp
# End Source File
# Begin Source File

SOURCE=.\TextProgressDlg.cpp
# End Source File
# End Group
# Begin Group "Header Files"

# PROP Default_Filter "h;hpp;hxx;hm;inl"
# Begin Source File

SOURCE=.\PearSynthesizer.h
# End Source File
# Begin Source File

SOURCE=.\PearSynthesizerDlg.h
# End Source File
# Begin Source File

SOURCE=.\Resource.h
# End Source File
# Begin Source File

SOURCE=.\StdAfx.h
# End Source File
# Begin Source File

SOURCE=.\TextProcess.h
# End Source File
# Begin Source File

SOURCE=.\TextProgressDlg.h
# End Source File
# End Group
# Begin Group "Resource Files"

# PROP Default_Filter "ico;cur;bmp;dlg;rc2;rct;bin;rgs;gif;jpg;jpeg;jpe"
# Begin Source File

SOURCE=.\res\PearSynthesizer.ico
# End Source File
# Begin Source File

SOURCE=.\res\PearSynthesizer.rc2
# End Source File
# End Group
# Begin Group "CART"

# PROP Default_Filter ""
# Begin Source File

SOURCE=.\CART\CART.cpp
# End Source File
# Begin Source File

SOURCE=.\CART\CART.h
# End Source File
# End Group
# Begin Group "hts_engine"

# PROP Default_Filter ""
# Begin Source File

SOURCE=.\hts_engine\hts_engine.cpp
# End Source File
# Begin Source File

SOURCE=.\hts_engine\hts_engine.h
# End Source File
# End Group
# Begin Group "WaveDraw"

# PROP Default_Filter ""
# Begin Source File

SOURCE=.\WaveDraw\Sound_to_Pitch.h
# End Source File
# Begin Source File

SOURCE=.\WaveDraw\TextProgressCtrl.cpp
# End Source File
# Begin Source File

SOURCE=.\WaveDraw\TextProgressCtrl.h
# End Source File
# Begin Source File

SOURCE=.\WaveDraw\WaveViewer.cpp
# End Source File
# Begin Source File

SOURCE=.\WaveDraw\WaveViewer.h
# End Source File
# End Group
# Begin Group "tts"

# PROP Default_Filter ""
# Begin Source File

SOURCE=.\tts\common.cpp
# End Source File
# Begin Source File

SOURCE=.\tts\common.h
# End Source File
# Begin Source File

SOURCE=.\tts\Cttswave.cpp
# End Source File
# Begin Source File

SOURCE=.\tts\Cttswave.h
# End Source File
# Begin Source File

SOURCE=.\tts\Cwave.cpp
# End Source File
# Begin Source File

SOURCE=.\tts\Cwave.h
# End Source File
# Begin Source File

SOURCE=.\tts\Digit.cpp
# End Source File
# Begin Source File

SOURCE=.\tts\digit.h
# End Source File
# Begin Source File

SOURCE=.\tts\DIGITUNIT.CPP
# End Source File
# Begin Source File

SOURCE=.\tts\Memio.cpp
# End Source File
# Begin Source File

SOURCE=.\tts\Memio.h
# End Source File
# Begin Source File

SOURCE=.\tts\MyRecordset.cpp
# End Source File
# Begin Source File

SOURCE=.\tts\MyRecordset.h
# End Source File
# Begin Source File

SOURCE=.\tts\Phone.cpp
# End Source File
# Begin Source File

SOURCE=.\tts\Phone.h
# End Source File
# Begin Source File

SOURCE=.\tts\Word.cpp
# End Source File
# Begin Source File

SOURCE=.\tts\Word.h
# End Source File
# Begin Source File

SOURCE=.\tts\WordInfo.cpp
# End Source File
# Begin Source File

SOURCE=.\tts\WordInfo.h
# End Source File
# End Group
# Begin Group "ColorList"

# PROP Default_Filter ""
# Begin Source File

SOURCE=.\ColorList\color.cpp
# End Source File
# Begin Source File

SOURCE=.\ColorList\color.h
# End Source File
# Begin Source File

SOURCE=.\ColorList\ColorListCtrl.cpp
# End Source File
# Begin Source File

SOURCE=.\ColorList\ColorListCtrl.h
# End Source File
# End Group
# Begin Source File

SOURCE=.\ReadMe.txt
# End Source File
# End Target
# End Project
