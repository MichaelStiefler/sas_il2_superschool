//*****************************************************************
// IL-2 Watchdog.exe - il2fb.exe Watchdog
// Copyright (C) 2013 SAS~Storebror
//
// This file is part of IL-2 Watchdog.exe.
//
// IL-2 Watchdog.exe is free software.
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
#include "StdAfx.h"
#include <stdio.h>
#include <stdlib.h>
#include <malloc.h>
#include "SAS IL-2 Watchdog.h"
#include "trace.h"
#include "SplashScreen.h"
#include "resource.h"
#include "CopyData.h"

#define _CRT_SECURE_NO_WARNINGS
#pragma warning( disable : 4996 )

#define LOGFILE_NAME		L"initlog.lst"

typedef void (WINAPI *PROCSWITCHTOTHISWINDOW)(HWND, BOOL);
PROCSWITCHTOTHISWINDOW MySwitchToThisWindow = NULL;

TCHAR LogFileName[MAX_PATH];

const TCHAR *	c_szSplashTitle = _T("IL-2 Sturmovik 1946 Launcher ©SAS 2013 (http://www.sas1946.com)");
const TCHAR *	c_szSplashClass = _T("Il2SASLauncherWnd");
const TCHAR *	c_szMsgWndTitle = _T("IL-2 Sturmovik 1946 Watchdog ©SAS 2013 (http://www.sas1946.com)");
const TCHAR *	c_szMsgWndClass = _T("Il2SASWatchdogMsgWnd");

HWND g_hMsgWnd = NULL;

HWND g_hIl2MainWindow = NULL;
extern HWND g_hSplashWnd;
HANDLE g_hIl2ProcessHandle = NULL;
HANDLE g_hDINPUTThreadHandle = NULL;


//************************************
// Method:    _tWinMain
// FullName:  _tWinMain
// Access:    public
// Returns:   int WINAPI
// Qualifier:
// Parameter: HINSTANCE hInstance
// Parameter: HINSTANCE hPrevInstance
// Parameter: LPWSTR lpCmdLine
// Parameter: int nShowCmd
//************************************
int WINAPI _tWinMain(
					 HINSTANCE hInstance,
					 HINSTANCE hPrevInstance,
					 LPWSTR lpCmdLine,
					 int nShowCmd)
{
	ATOM		atomClass;
	MSG			msg;
	int			nRet;
	WNDCLASSEX wx = {};
	GetCurrentDirectory(MAX_PATH, LogFileName);
	_tcscat(LogFileName, L"\\");
	_tcscat(LogFileName, LOGFILE_NAME);
	TRACE(L"IL-2 Watchdog started.\r\n");
	wx.cbSize = sizeof(WNDCLASSEX);
	wx.lpfnWndProc = (WNDPROC)MsgWndProc;        // function which will handle messages
	wx.hInstance = hInstance;
	wx.lpszClassName = c_szMsgWndClass;
	atomClass = RegisterClassEx(&wx);

	if(atomClass) {
		g_hMsgWnd = CreateWindowEx(0, c_szMsgWndClass, c_szMsgWndTitle, 0, 0, 0, 0, 0, HWND_MESSAGE, NULL, NULL, NULL);
	}

	if(g_hMsgWnd == NULL) {
		TRACE(L"Couldn't create Message Window, exiting.\r\n");
		return -1;
	}

	TRACE(L"Message Window Created.\r\n");
	ShowSplash(hInstance, MAKEINTRESOURCE(IDI_ICON_SASUP), c_szSplashClass, c_szSplashTitle, MAKEINTRESOURCE(IDB_PNG_SPLASH), _T("PNG"));

	if(g_hSplashWnd == NULL) {
		TRACE(L"Couldn't create Splash Screen, exiting.\r\n");
		return -2;
	}

	TRACE(L"Splash Screen Created.\r\n");
	//SetTimer(g_hMsgWnd, WATCHDOG_TIMER_EVENT, WATCHDOG_TIMER_TIMEOUT, NULL);
	HANDLE hWatchdogMessageWindowCallbackEvent = CreateEvent(NULL, FALSE, FALSE, EVENT_WATCHDOG_MESSAGEWINDOW_ACTIVE);

	if(hWatchdogMessageWindowCallbackEvent == NULL || hWatchdogMessageWindowCallbackEvent == INVALID_HANDLE_VALUE) {
		TRACE(L"Watchdog Callback Event not created\r\n");
		return -3;
	}

	SetEvent(hWatchdogMessageWindowCallbackEvent);
	CloseHandle(hWatchdogMessageWindowCallbackEvent);
	TRACE(L"Callback Notification Event to dinput.dll set.\r\n");

	HWINEVENTHOOK hWinEventHook = SetWinEventHook(
		EVENT_OBJECT_CREATE, EVENT_OBJECT_DESTROY,
		NULL, WinEventProc, 0, 0,
		WINEVENT_OUTOFCONTEXT | WINEVENT_SKIPOWNPROCESS);

	if(g_hMsgWnd) {
		while(true) {
			// retrieve and process messages until WM_QUIT is encountered
			nRet = ::GetMessage(&msg, NULL, 0, 0);

			if((nRet == 0) || (nRet == -1)) {
				break;    // WM_QUIT (0) or ERROR (-1) is detected
			}

			::TranslateMessage(&msg);
			::DispatchMessage(&msg);
		}

		g_hMsgWnd = NULL;								// window is already destroyed by DefWindowProc
	}

	TRACE(L"Freeing resources.\r\n");

	if (hWinEventHook) UnhookWinEvent(hWinEventHook);

	if(atomClass != 0) {
		UnregisterClass(c_szMsgWndClass, (HINSTANCE)HWND_MESSAGE);
	}

	if(g_hDINPUTThreadHandle != NULL) {
		CloseHandle(g_hDINPUTThreadHandle);
	}

	if(g_hIl2ProcessHandle != NULL) {
		CloseHandle(g_hIl2ProcessHandle);
	}

	TRACE(L"IL-2 Watchdog exiting.\r\n");
	return 0;
}

//************************************
// Method:    ShowLastError
// FullName:  ShowLastError
// Access:    public
// Returns:   void
// Qualifier:
//************************************
void ShowLastError()
{
	LPVOID lpMsgBuf;
	FormatMessage(
		FORMAT_MESSAGE_ALLOCATE_BUFFER |
		FORMAT_MESSAGE_FROM_SYSTEM |
		FORMAT_MESSAGE_IGNORE_INSERTS,
		NULL,
		GetLastError(),
		0, // Default language
		(LPTSTR) &lpMsgBuf,
		0,
		NULL
		);
	// Process any inserts in lpMsgBuf.
	// ...
	// Display the string.
	MessageBox(NULL, (LPCTSTR)lpMsgBuf, L"SAS IL-2 Watchdog Error", MB_OK | MB_ICONINFORMATION | MB_TOPMOST);
	// Free the buffer.
	LocalFree(lpMsgBuf);
}

//************************************
// Method:    MsgWndProc
// FullName:  MsgWndProc
// Access:    public static 
// Returns:   LRESULT
// Qualifier:
// Parameter: HWND hwnd
// Parameter: UINT message
// Parameter: WPARAM wParam
// Parameter: LPARAM lParam
//************************************
static LRESULT MsgWndProc(HWND hwnd, UINT message, WPARAM wParam, LPARAM lParam)
{
	switch(message) {
	case WM_TIMER:
		switch (wParam) {
			case WATCHDOG_TIMER_EVENT:
				KillTimer(g_hMsgWnd, WATCHDOG_TIMER_EVENT);
				ActivateIl2MainWindow(g_hIl2MainWindow);
				SetTimer(g_hMsgWnd, WATCHDOG_TIMER_CHECKACTIVE_EVENT, WATCHDOG_TIMER_TIMEOUT, NULL);
				break;
			case WATCHDOG_TIMER_CHECKACTIVE_EVENT:
				KillTimer(g_hMsgWnd, WATCHDOG_TIMER_CHECKACTIVE_EVENT);
				if (!CheckActivated(g_hIl2MainWindow)) SetTimer(g_hMsgWnd, WATCHDOG_TIMER_EVENT, WATCHDOG_TIMER_TIMEOUT, NULL);
				break;
			default:
				break;
		}
		break;

	case WM_WATCHDOG:
		g_hIl2ProcessHandle = OpenProcess(PROCESS_QUERY_LIMITED_INFORMATION  | PROCESS_TERMINATE | SYNCHRONIZE, FALSE, (DWORD)wParam);
		g_hDINPUTThreadHandle = OpenThread(PROCESS_QUERY_LIMITED_INFORMATION  | THREAD_TERMINATE | SYNCHRONIZE, FALSE, (DWORD)lParam);

		if(g_hIl2ProcessHandle == NULL) {
			TRACE(L"Could not open il2fb.exe process (id=0x%08X)\r\n", (DWORD)wParam);
		}

		break;

	case WM_CLOSE:
		PostQuitMessage(0);
		break;

	default:
		break;
	}

	return DefWindowProc(hwnd, message, wParam, lParam);
}

//************************************
// Method:    CheckIl2Termination
// FullName:  CheckIl2Termination
// Access:    public 
// Returns:   void
// Qualifier:
//************************************
void CheckIl2Termination()
{
	TRACE(L"IL-2 main window (handle 0x%08X) disappeared, checking process status.\r\n", g_hIl2MainWindow);

	if(g_hIl2ProcessHandle != NULL) {
		DWORD dwProcessExitCode = 0;

		if(!GetExitCodeProcess(g_hIl2ProcessHandle, &dwProcessExitCode)) {
			TRACE(L"Could not get il2fb.exe process exit code (handle = 0x%08X), maybe il2fb.exe terminated unexpected.\r\n", g_hIl2ProcessHandle);
			PostQuitMessage(0);
			return;
		}

		if(dwProcessExitCode != STILL_ACTIVE) {
			TRACE(L"il2fb.exe process exited safely with exit code %d.\r\n", dwProcessExitCode);
			PostQuitMessage(0);
			return;
		}

		if(WaitForSingleObject(g_hIl2ProcessHandle, IL2FBEXE_WAIT_TIMEOUT) != WAIT_TIMEOUT) {
			if(!GetExitCodeProcess(g_hIl2ProcessHandle, &dwProcessExitCode)) {
				TRACE(L"il2fb.exe process exited safely with unknown exit code \r\n");
				PostQuitMessage(0);
				return;
			}

			TRACE(L"il2fb.exe process exited safely with exit code %d.\r\n", dwProcessExitCode);
			PostQuitMessage(0);
			return;
		}

		TRACE(L"il2fb.exe process is still running even %d milliseconds after main window has been closed, terminating process now.\r\n", IL2FBEXE_WAIT_TIMEOUT);
		TerminateProcess(g_hIl2ProcessHandle, 1946);

		if(WaitForSingleObject(g_hIl2ProcessHandle, IL2FBEXE_WAIT_TIMEOUT) != WAIT_TIMEOUT) {
			if(!GetExitCodeProcess(g_hIl2ProcessHandle, &dwProcessExitCode)) {
				TRACE(L"il2fb.exe process exited after termination with unknown exit code \r\n");
				PostQuitMessage(0);
				return;
			}

			TRACE(L"il2fb.exe process exited after termination with exit code %d.\r\n", dwProcessExitCode);
			PostQuitMessage(0);
			return;
		}

		TRACE(L"il2fb.exe process is still running even %d milliseconds after termination process, no chance to further stop the program from running.\r\n", IL2FBEXE_WAIT_TIMEOUT);
		PostQuitMessage(0);
		return;
	}

	PostQuitMessage(0);
	return;
}

//************************************
// Method:    WinEventProc
// FullName:  WinEventProc
// Access:    public 
// Returns:   void CALLBACK
// Qualifier:
// Parameter: HWINEVENTHOOK hWinEventHook
// Parameter: DWORD event
// Parameter: HWND hwnd
// Parameter: LONG idObject
// Parameter: LONG idChild
// Parameter: DWORD dwEventThread
// Parameter: DWORD dwmsEventTime
//************************************
void CALLBACK WinEventProc(
						   HWINEVENTHOOK hWinEventHook,
						   DWORD event,
						   HWND hwnd,
						   LONG idObject,
						   LONG idChild,
						   DWORD dwEventThread,
						   DWORD dwmsEventTime
						   )
{
	if (hwnd && idObject == OBJID_WINDOW && idChild == CHILDID_SELF)
	{
		LPCTSTR pszAction = NULL;
		BOOL bCreated = FALSE;
		switch (event) {
		  case EVENT_OBJECT_CREATE:
			  pszAction = TEXT("created");
			  bCreated = TRUE;
			  break;
		  case EVENT_OBJECT_DESTROY:
			  pszAction = TEXT("destroyed");
			  break;
		}
		if (pszAction) {
			TCHAR szClass[80];
			szClass[0] = TEXT('\0');
			if (IsWindow(hwnd)) {
				GetClassName(hwnd, szClass, ARRAYSIZE(szClass));
			}
			if (_tcscmp(szClass, IL2_MAIN_WINDOW_CLASS) == 0) {
				TCHAR szName[80];
				szName[0] = TEXT('\0');
				GetWindowText(hwnd, szName, ARRAYSIZE(szName));
				TRACE(L"IL-2 Main Window %s: \"%s\" (%s), Handle= 0x%p\r\n", pszAction, szName, szClass, hwnd);
				if (bCreated) {
					g_hIl2MainWindow = hwnd;
					//SetTimer(g_hMsgWnd, WATCHDOG_TIMER_EVENT, WATCHDOG_TIMER_TIMEOUT, NULL);
					ActivateIl2MainWindow(g_hIl2MainWindow);
					SetTimer(g_hMsgWnd, WATCHDOG_TIMER_CHECKACTIVE_EVENT, WATCHDOG_TIMER_TIMEOUT, NULL);
					PostMessage(g_hSplashWnd, WM_CLOSE, 0, 0);
				} else {
					CheckIl2Termination();
				}
			}
		}
	}
}

//************************************
// Method:    SetForegroundWindowInternal
// FullName:  SetForegroundWindowInternal
// Access:    public 
// Returns:   BOOL
// Qualifier:
// Parameter: HWND hWnd
//************************************
BOOL SetForegroundWindowInternal(HWND hWnd)
{
    if(!IsWindow(hWnd)) {
        return FALSE;
    }

    HWND	hCurrWnd = GetForegroundWindow(),
            hWndPreviouslyActive = NULL;
    DWORD	dwThisTID = GetCurrentThreadId(),
            dwCurrTID = GetWindowThreadProcessId(hCurrWnd, 0);
    BOOL bRet = FALSE;
    TRACE(L"SetForegroundWindowInternal hWnd=0x%08X, hCurrWnd=0x%08X, dwThisTID=0x%08X, dwCurrTID=0x%08X\r\n", hWnd, hCurrWnd, dwThisTID, dwCurrTID);

    try {
        bRet = AttachThreadInput(dwCurrTID, dwThisTID, TRUE);
        TRACE(L"Attaching Thread Input Queue result = %s\r\n", (bRet) ? L"true" : L"false");
        bRet = SetForegroundWindow(hWnd);
        TRACE(L"SetForegroundWindow(0x%08X) = %s\r\n", hWnd, bRet ? L"true" : L"false");
        hWndPreviouslyActive = SetFocus(hWnd);
        TRACE(L"SetFocus(0x%08X) = 0x%08X\r\n", hWnd, hWndPreviouslyActive);
        bRet = AttachThreadInput(dwCurrTID, dwThisTID, FALSE);
        TRACE(L"Detaching Thread Input Queue result = %s\r\n", (bRet) ? L"true" : L"false");
    } catch(...) {}

    return (hWndPreviouslyActive != NULL);
}

//************************************
// Method:    SetForegroundWindowInternalThread
// FullName:  SetForegroundWindowInternalThread
// Access:    public 
// Returns:   UINT
// Qualifier:
// Parameter: LPVOID pParam
//************************************
UINT SetForegroundWindowInternalThread(LPVOID pParam)
{
    HWND hWndIl2Main = (HWND)pParam;
    Sleep(0);
    HMODULE hUser32 = GetModuleHandle(L"user32");

    if(hUser32) {
        MySwitchToThisWindow = (PROCSWITCHTOTHISWINDOW)GetProcAddress(hUser32, "SwitchToThisWindow");
    }

    if(MySwitchToThisWindow != NULL) {
        TRACE(L"Activating IL-2 Main Window (0x%08X) using SwitchToThisWindow()\r\n", hWndIl2Main);
        MySwitchToThisWindow(hWndIl2Main, TRUE);
    } else {
		TRACE(L"Activating IL-2 Main Window (0x%08X) using AttachThreadInput()\r\n", hWndIl2Main);
        SetForegroundWindowInternal(hWndIl2Main);
    }

    return 0;
}


//************************************
// Method:    ActivateIl2MainWindow
// FullName:  ActivateIl2MainWindow
// Access:    public 
// Returns:   void
// Qualifier:
// Parameter: HWND hWndIl2Main
//************************************
void ActivateIl2MainWindow(HWND hWndIl2Main)
{
    CloseHandle(CreateThread(NULL, 0, (LPTHREAD_START_ROUTINE)SetForegroundWindowInternalThread, hWndIl2Main, 0, NULL));
}

BOOL CheckActivated(HWND hWndToCheck)
{
	HWND hwnd = GetForegroundWindow();
	if (hwnd != hWndToCheck) {
		TRACE(L"IL-2 Main Window (Handle: 0x%p) is not Foreground Window yet (Foreground Window has Handle: 0x%p)\r\n", hWndToCheck, hwnd);
		return FALSE;
	}
	DWORD remoteThreadId = GetWindowThreadProcessId(hwnd, NULL);
	DWORD currentThreadId = GetCurrentThreadId();
	AttachThreadInput(remoteThreadId, currentThreadId, TRUE);
	//SetActiveWindow(hWndToCheck);
	//SetFocus(hWndToCheck);
	HWND focused = GetFocus();
	AttachThreadInput(remoteThreadId, currentThreadId, FALSE);
	if (focused != hWndToCheck) {
		TRACE(L"IL-2 Main Window (Handle: 0x%p) is not focused Window yet (focused Window has Handle: 0x%p)\r\n", hWndToCheck, focused);
		return FALSE;
	}
	TRACE(L"Successfully activated IL-2 Main Window (Handle: 0x%p)\r\n", hWndToCheck);
	return TRUE;
}