//*****************************************************************
// PipeLogger.exe - Enhanced Logging Tool for IL-2 1946
// Copyright (C) 2019 SAS~Storebror
//
// This file is part of PipeLogger.exe.
//
// PipeLogger.exe is free software.
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
#include "stdafx.h"
#include "PipeLogger.h"
#include "PipeListener.h"
#include "trace.h"
#include "globals.h"
#include <shellapi.h>
#include <tlhelp32.h>

//*************************************************************************
// Suppress new style warning messages
//*************************************************************************
#define _CRT_SECURE_NO_WARNINGS
#pragma warning( disable : 4996 )

//************************************
// Method:    _tmain
// FullName:  _tmain
// Access:    public 
// Returns:   int
// Qualifier:
// Parameter: int argc
// Parameter: _TCHAR * argv[]
//************************************
int CALLBACK WinMain(
	_In_ HINSTANCE hInstance,
	_In_ HINSTANCE hPrevInstance,
	_In_ LPSTR     lpCmdLine,
	_In_ int       nCmdShow
)
{
	GetCurrentDirectory(MAX_PATH, g_szLogFileName);
	_tcscat(g_szLogFileName, L"\\");
	_tcscat(g_szLogFileName, LOGFILE_NAME);

	TRACE("Starting\r\n");

	DWORD pid = GetCurrentProcessId();
	DWORD ppid = -1;
	HANDLE h = CreateToolhelp32Snapshot(TH32CS_SNAPPROCESS, 0);
	PROCESSENTRY32 pe = { 0 };
	pe.dwSize = sizeof(PROCESSENTRY32);
	if (Process32First(h, &pe)) {
		do {
			if (pe.th32ProcessID == pid) {
				ppid = pe.th32ParentProcessID;
				TRACE("PipeLogger Process ID: %08X; Parent (IL-2) Process ID: %08X\r\n", pid, ppid);
				break;
			}
		} while (Process32Next(h, &pe));
	}
	CloseHandle(h);

	HANDLE hParent = OpenProcess(SYNCHRONIZE, FALSE, ppid);
	g_hTerminatePipeLogger = CreateEvent(NULL, TRUE, FALSE, NULL);

	RunWorker(PipeListenerThread);
	WaitForSingleObject(hParent, INFINITE);
	TRACE("il2fb.exe Termination detected, terminating Pipe Logger.\r\n");
	CloseHandle(hParent);

	SetEvent(g_hTerminatePipeLogger);

	int retry = MAX_ALL_WAIT_TIME / 100;
	while (g_aiLogWriters.load() > 0 && retry-- > 0) Sleep(100);
	if (retry > 0)
		TRACE("All Writers Threads finished, exiting.\r\n");
	else
		TRACE("%d Writer Thread(s) stalled, exiting.\r\n", g_aiLogWriters.load());

	return 0;
}

