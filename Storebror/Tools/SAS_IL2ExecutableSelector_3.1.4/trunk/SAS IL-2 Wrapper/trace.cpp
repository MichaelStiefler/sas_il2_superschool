//*****************************************************************
// wrapper.dll - il2fb.exe mod files wrapper
// Copyright (C) 2013 SAS~Storebror
//
// This file is part of wrapper.dll.
//
// wrapper.dll is free software.
// It is distributed under the DWTFYWTWIPL license:
//
// DO WHAT THE FUCK YOU WANT TO WITH IT PUBLIC LICENSE
// Version 1, March 2012
//
// Copyright (C) 2013 SAS~Storebror <mike@sas1946.com>
//
// Everyone is permitted to copy and distribute verbatim or modified
// copies of this license document, and changing it is allowed as long
// as the name is changed.
//
// DO WHAT THE FUCK YOU WANT TO WITH IT PUBLIC LICENSE
// TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
//
// 0. You just DO WHAT THE FUCK YOU WANT TO WITH IT.
//
//*****************************************************************

#include "stdafx.h"
#include "SAS IL-2 Wrapper.h"

#define _CRT_SECURE_NO_WARNINGS
#pragma warning( disable : 4996 )

extern TCHAR LogFileName[];

//************************************
// Method:    _trace
// FullName:  _trace
// Access:    public 
// Returns:   bool
// Qualifier:
// Parameter: TCHAR * format
// Parameter: ...
//************************************
bool _trace(TCHAR *format, ...)
{
    va_list argptr;
    va_start(argptr, format);
    int len = _vsctprintf(format, argptr)   // _vscprintf doesn't count
              + 1; // terminating '\0'
    TCHAR* buffer = (TCHAR*)malloc(len * sizeof(TCHAR));
    memset(buffer, 0, sizeof(buffer));
    _vsntprintf(buffer, len - 1, format, argptr);
    buffer[len - 1] = L'\0';
    va_end(argptr);
#ifdef _DEBUG
    OutputDebugString(buffer);
#endif

    if(LogFileName != NULL && _tcslen(LogFileName) != 0) {
        CHAR* cBufLog = (CHAR*)malloc(len * sizeof(TCHAR));
        memset(cBufLog, 0, sizeof(cBufLog));
        wcstombs(cBufLog, buffer, len - 1);
        cBufLog[len - 1] = '\0';
        SYSTEMTIME stSystemTime;
        /////////////////////////////////////////////////////////////////////////
        // Generate Time Stamp
        /////////////////////////////////////////////////////////////////////////
        GetLocalTime(&stSystemTime);
        int lenLogFileName = _tcslen(LogFileName);
        CHAR* cLogFileName = (CHAR*)malloc((lenLogFileName + 1) * sizeof(TCHAR));
        memset(cLogFileName, 0, sizeof(cLogFileName));
        wcstombs(cLogFileName, LogFileName, lenLogFileName);
        cLogFileName[lenLogFileName] = '\0';
        FILE *log;

        while(true) {
            log = _fsopen(cLogFileName, "ab", _SH_DENYWR);

            if(log != NULL) {
                break;
            }

            Sleep(rand() % 100);
        }

        fprintf(log, "%04d-%02d-%02d %02d:%02d:%02d:%03d    (wrapper) : %s",
                stSystemTime.wYear,
                stSystemTime.wMonth,
                stSystemTime.wDay,
                stSystemTime.wHour,
                stSystemTime.wMinute,
                stSystemTime.wSecond,
                stSystemTime.wMilliseconds,
                cBufLog);
        fflush(log);
        fclose(log);
        free(cBufLog);
        free(cLogFileName);
    }

    free(buffer);
    return true;
}

//************************************
// Method:    _trace
// FullName:  _trace
// Access:    public
// Returns:   bool
// Qualifier:
// Parameter: char * format
// Parameter: ...
//************************************
bool _trace(char *format, ...)
{
    va_list argptr;
    va_start(argptr, format);
    int len = _vscprintf(format, argptr)   // _vscprintf doesn't count
              + 1; // terminating '\0'
    CHAR* buffer = (CHAR*)malloc(len * sizeof(CHAR));
    memset(buffer, 0, sizeof(buffer));
    _vsnprintf(buffer, len - 1, format, argptr);
    buffer[len - 1] = '\0';
    va_end(argptr);
#ifdef _DEBUG
    OutputDebugStringA(buffer);
#endif

    if(LogFileName != NULL && _tcslen(LogFileName) != 0) {
        SYSTEMTIME stSystemTime;
        /////////////////////////////////////////////////////////////////////////
        // Generate Time Stamp
        /////////////////////////////////////////////////////////////////////////
        GetLocalTime(&stSystemTime);
        int lenLogFileName = _tcslen(LogFileName);
        CHAR* cLogFileName = (CHAR*)malloc((lenLogFileName + 1) * sizeof(TCHAR));
        memset(cLogFileName, 0, sizeof(cLogFileName));
        wcstombs(cLogFileName, LogFileName, lenLogFileName);
        cLogFileName[lenLogFileName] = '\0';
        FILE *log;

        while(true) {
            log = _fsopen(cLogFileName, "ab", _SH_DENYWR);

            if(log != NULL) {
                break;
            }

            Sleep(rand() % 100);
        }

        fprintf(log, "%04d-%02d-%02d %02d:%02d:%02d:%03d (dinput.dll) : %s",
                stSystemTime.wYear,
                stSystemTime.wMonth,
                stSystemTime.wDay,
                stSystemTime.wHour,
                stSystemTime.wMinute,
                stSystemTime.wSecond,
                stSystemTime.wMilliseconds,
                buffer);
        fflush(log);
        fclose(log);
        free(cLogFileName);
    }

    free(buffer);
    return true;
}