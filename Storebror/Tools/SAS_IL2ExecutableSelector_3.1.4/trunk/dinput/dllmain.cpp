//*****************************************************************
// DINPUT.dll - JVM Parameter parser and il2fb.exe modifier
// Copyright (C) 2013 SAS~Storebror
//
// This file is part of DINPUT.dll.
//
// DINPUT.dll is free software.
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

//*************************************************************************
// Includes
//*************************************************************************
#include <windows.h>
#include <stdlib.h>
#include <stdio.h>
#include <tchar.h>
#include "trace.h"
#include <share.h>
#include "_dinput.h"

#define _CRT_SECURE_NO_WARNINGS
#pragma warning( disable : 4996 )


//*************************************************************************
// Globals
//*************************************************************************
HMODULE hJvm = NULL;
HMODULE hDInput = NULL;
HMODULE hImm32 = NULL;
TCHAR szDirectInputPath[MAX_PATH];
TCHAR szImm32Path[MAX_PATH];
TCHAR szJvmPath[MAX_PATH];
TCHAR szCurDir[MAX_PATH];
TCHAR szIniFile[MAX_PATH];
TCHAR LogFileName[MAX_PATH];
char cIniFile[MAX_PATH];
BOOL g_bProcessAttached = FALSE;

//************************************
// Method:    DllMain
// FullName:  DllMain
// Access:    public
// Returns:   BOOL APIENTRY
// Qualifier:
// Parameter: HMODULE hModule
// Parameter: DWORD ul_reason_for_call
// Parameter: LPVOID lpReserved
//************************************
BOOL APIENTRY DllMain(HMODULE hModule,
                      DWORD  ul_reason_for_call,
                      LPVOID lpReserved
                     )
{
    switch(ul_reason_for_call) {
    case DLL_PROCESS_ATTACH:
        if(hDInput == NULL) {
            if(!StartWatchdog()) {
                return FALSE;
            }

            memset(szCurDir, 0, sizeof(szCurDir));
            GetModuleFileName(NULL, szCurDir, MAX_PATH);
            LPTSTR pszFileName = NULL;
            pszFileName = _tcsrchr(szCurDir, '\\') + 1;
            *pszFileName = '\0';
            _stprintf(LogFileName, L"%s%s", szCurDir, LOGFILE_NAME);
            FILE * log;

            while(true) {
                log = _tfsopen(LogFileName, L"wb", _SH_DENYWR);

                if(log != NULL) {
                    break;
                }

                Sleep(rand() % 100);
            }

            fflush(log);
            fclose(log);
            LPCTSTR lpEnvPath = _wgetenv(L"SystemRoot");
            _stprintf(szDirectInputPath, L"%s\\system32\\dinput.dll", lpEnvPath);
            hDInput = LoadLibraryW(szDirectInputPath);
            _stprintf(szImm32Path, L"%s\\system32\\imm32.dll", lpEnvPath);
            hImm32 = LoadLibraryW(szImm32Path);
            _stprintf(szJvmPath, L"%sbin\\hotspot\\jvm.dll", szCurDir);
            hJvm = LoadLibraryW(szJvmPath);

            if(IsServerExe()) {
                _stprintf(szIniFile, L"%s%s", szCurDir, SERVER_INI);
            } else {
                _stprintf(szIniFile, L"%s%s", szCurDir, CLIENT_INI);
            }

            wcstombs(cIniFile, szIniFile, MAX_PATH);
        }

        TRACE(L"JVM Parameters injector activated\r\n");

        if(!g_bProcessAttached) {
            g_bProcessAttached = TRUE;
            AdjustJvmParams();
        }

        break;

    case DLL_THREAD_ATTACH:
        break;

    case DLL_THREAD_DETACH:
        break;

    case DLL_PROCESS_DETACH:
        TRACE(L"JVM Parameters injector deactivated\r\n");

        if(hDInput != NULL) {
            FreeLibrary(hDInput);
            hDInput = NULL;
        }

        break;
    }

    return TRUE;
}

