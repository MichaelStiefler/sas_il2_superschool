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
// Definitions
//*************************************************************************
#define _IMM_ // Define _IMM_ in order to avoid forward declaration of ImmDisableIME() in Imm.h

//*************************************************************************
// Includes
//*************************************************************************
#include "_dinput.h"
#include <windows.h>
#include <tchar.h>
#include <vector>
#include <algorithm>
#include "jni.h"
#include "trace.h"

// Suppress compiler warnings about unsafe functions
#define _CRT_SECURE_NO_WARNINGS
#pragma warning( disable : 4996 )

//*************************************************************************
// External variables
//*************************************************************************
extern HMODULE hDInput;
extern HMODULE hImm32;
extern TCHAR szCurDir[MAX_PATH];
extern HMODULE hJvm;
extern TCHAR szCurDir[MAX_PATH];
extern TCHAR szIniFile[MAX_PATH];
extern char cIniFile[MAX_PATH];

//*************************************************************************
// Globals
//*************************************************************************
BYTE g_oldMem[9];
std::vector<MyJvmOptionItem> JvmOptions;
std::vector<LPSTR> FinalJvmOptions;
TCHAR szBuffer[MAX_PATH];
BOOL bDisableMutex = FALSE;

ORI_CreateJavaVM		JniCreateJavaVM = NULL;
IMMDISABLEIME			SysImmDisableIme = NULL;
DIRECTINPUTCREATEA		SysDirectInputCreateA = NULL;
DIRECTINPUTCREATEW		SysDirectInputCreateW = NULL;
DIRECTINPUTCREATEEX		SysDirectInputCreateEx = NULL;
DLLREGISTERSERVER		SysDllRegisterServer = NULL;
DLLUNREGISTERSERVER		SysDllUnregisterServer = NULL;

const TCHAR *	c_szMsgWndClass = _T("Il2SASWatchdogMsgWnd");

//************************************
// Method:    DirectInputCreateA
// FullName:  DirectInputCreateA
// Access:    public
// Returns:   DINPUT_API HRESULT __stdcall
// Qualifier:
// Parameter: HINSTANCE hinst
// Parameter: DWORD dwVersion
// Parameter: LPDIRECTINPUTA * ppDI
// Parameter: LPUNKNOWN punkOuter
//************************************
DINPUT_API HRESULT __stdcall DirectInputCreateA(HINSTANCE hinst, DWORD dwVersion, LPDIRECTINPUTA *ppDI, LPUNKNOWN punkOuter)
{
    // forward any DirectInputCreateA function call to windows native dinput.dll
    if(hDInput == NULL) {
        return E_NOINTERFACE;
    }

    if(SysDirectInputCreateA == NULL) {
        SysDirectInputCreateA = (DIRECTINPUTCREATEA)GetProcAddress(hDInput, "DirectInputCreateA");
    }

    return SysDirectInputCreateA(hinst, dwVersion, ppDI, punkOuter);
}

//************************************
// Method:    DirectInputCreateEx
// FullName:  DirectInputCreateEx
// Access:    public
// Returns:   DINPUT_API HRESULT __stdcall
// Qualifier:
// Parameter: HINSTANCE hinst
// Parameter: DWORD dwVersion
// Parameter: REFIID refIID
// Parameter: LPVOID * ppDI
// Parameter: LPUNKNOWN punkOuter
//************************************
DINPUT_API HRESULT __stdcall DirectInputCreateEx(HINSTANCE hinst, DWORD dwVersion, REFIID refIID, LPVOID *ppDI, LPUNKNOWN punkOuter)
{
    // forward any DirectInputCreateEx function call to windows native dinput.dll
    if(hDInput == NULL) {
        return E_NOINTERFACE;
    }

    if(SysDirectInputCreateEx == NULL) {
        SysDirectInputCreateEx = (DIRECTINPUTCREATEEX)GetProcAddress(hDInput, "DirectInputCreateEx");
    }

    return SysDirectInputCreateEx(hinst, dwVersion, refIID, ppDI, punkOuter);
}

//************************************
// Method:    DirectInputCreateW
// FullName:  DirectInputCreateW
// Access:    public
// Returns:   DINPUT_API HRESULT __stdcall
// Qualifier:
// Parameter: HINSTANCE hinst
// Parameter: DWORD dwVersion
// Parameter: LPDIRECTINPUTW * ppDI
// Parameter: LPUNKNOWN punkOuter
//************************************
DINPUT_API HRESULT __stdcall DirectInputCreateW(HINSTANCE hinst, DWORD dwVersion, LPDIRECTINPUTW *ppDI, LPUNKNOWN punkOuter)
{
    // forward any DirectInputCreateW function call to windows native dinput.dll
    if(hDInput == NULL) {
        return E_NOINTERFACE;
    }

    if(SysDirectInputCreateW == NULL) {
        SysDirectInputCreateW = (DIRECTINPUTCREATEW)GetProcAddress(hDInput, "DirectInputCreateW");
    }

    return SysDirectInputCreateW(hinst, dwVersion, ppDI, punkOuter);
}

//************************************
// Method:    ImmDisableIME
// FullName:  ImmDisableIME
// Access:    public
// Returns:   DINPUT_API BOOL __stdcall
// Qualifier:
// Parameter: __in DWORD idThread
//************************************
DINPUT_API BOOL __stdcall ImmDisableIME(__in  DWORD idThread)
{
    // forward any ImmDisableIME function call to windows native imm32.dll
    if(hImm32 == NULL) {
        return E_NOINTERFACE;
    }

    if(SysImmDisableIme == NULL) {
        SysImmDisableIme = (IMMDISABLEIME)GetProcAddress(hImm32, "ImmDisableIME");
    }

    return SysImmDisableIme(idThread);
}

//************************************
// Method:    DllRegisterServer
// FullName:  DllRegisterServer
// Access:    public
// Returns:   DINPUT_API HRESULT __stdcall
// Qualifier:
// Parameter: void
//************************************
DINPUT_API HRESULT __stdcall DllRegisterServer(void)
{
    // forward any DllRegisterServer function call to windows native dinput.dll
    if(hDInput == NULL) {
        return E_NOINTERFACE;
    }

    if(SysDllRegisterServer == NULL) {
        SysDllRegisterServer = (DLLREGISTERSERVER)GetProcAddress(hDInput, "DllRegisterServer");
    }

    return SysDllRegisterServer();
}

//************************************
// Method:    DllUnregisterServer
// FullName:  DllUnregisterServer
// Access:    public
// Returns:   DINPUT_API HRESULT __stdcall
// Qualifier:
// Parameter: void
//************************************
DINPUT_API HRESULT __stdcall DllUnregisterServer(void)
{
    // forward any DllUnregisterServer function call to windows native dinput.dll
    if(hDInput == NULL) {
        return E_NOINTERFACE;
    }

    if(SysDllUnregisterServer == NULL) {
        SysDllUnregisterServer = (DLLUNREGISTERSERVER)GetProcAddress(hDInput, "DllUnregisterServer");
    }

    return SysDllUnregisterServer();
}

//************************************
// Method:    trim
// FullName:  trim
// Access:    public
// Returns:   void
// Qualifier:
// Parameter: std::string & str
//************************************
void trim(std::string& str)
{
    std::string::size_type pos = str.find_last_not_of(' ');

    if(pos != std::string::npos) {
        str.erase(pos + 1);
        pos = str.find_first_not_of(' ');

        if(pos != std::string::npos) {
            str.erase(0, pos);
        }
    } else {
        str.erase(str.begin(), str.end());
    }
}

//************************************
// Method:    SAS_CreateJavaVM
// FullName:  SAS_CreateJavaVM
// Access:    public
// Returns:   jint JNICALL
// Qualifier:
// Parameter: JavaVM * * p_vm
// Parameter: void * * p_env
// Parameter: void * vm_args
//************************************
jint JNICALL SAS_CreateJavaVM(JavaVM **p_vm, void **p_env, void *vm_args)
{
    // This function is being called when the JMP command has been successfully
    // injected into JVM.dll and an application calls JNI_CreateJavaVM().
    TRACE(L"Hooked \"SAS_CreateJavaVM\" function activated, injecting JVM Parameters\r\n");
    DWORD dwOldProtect = 0;
    DWORD dwNumRead = 0;
    DWORD dwProcRights = PROC_FULL_RIGHTS;
    DWORD dwCurProc = GetCurrentProcessId();
    HANDLE hCurProc = OpenProcess(dwProcRights, FALSE, dwCurProc);
    int iJniCreateJavaVM = (int)JniCreateJavaVM; // JniCreateJavaVM holds the address of the original JNI_CreateJavaVM() function

    if(hCurProc != NULL) {
        try {
            if(NULL != VirtualProtectEx(hCurProc, (LPVOID)JniCreateJavaVM, 9, PAGE_EXECUTE_READWRITE, &dwOldProtect)) {
                memcpy(JniCreateJavaVM, g_oldMem, 9); // restore the old JNI_CreateJavaVM() function header
                VirtualProtectEx(hCurProc, (LPVOID)0x00420C00, 9, dwOldProtect, NULL);
            } else {
                TRACE(L"Error in SAS_CreateJavaVM: Couldn't get access to JNI_CreateJavaVM function header memory area!\r\n");
            }
        } catch(...) {
            TRACE(L"Error in SAS_CreateJavaVM while restoring function entry point of CreateJavaVM!\r\n");
        }

        CloseHandle(hCurProc);
    } else {
        TRACE(L"Error in SAS_CreateJavaVM: Couldn't aquire current process handle!\r\n");
    }

    // get access to JVM arguments being passed to the JNI_CreateJavaVM() function.
    JavaVMInitArgs *vmargs = (JavaVMInitArgs *) vm_args;
    // create a new set of  JVM arguments which holds our modified JVM parameters
    JavaVMInitArgs newArgs;
    newArgs.ignoreUnrecognized = JNI_TRUE; // always ignore unknown JVM options and parameters
    newArgs.version = vmargs->version;
    // allocate memory for our new JVM parameters
    JavaVMOption* newOptions = (JavaVMOption*)malloc((FinalJvmOptions.size()) * sizeof(JavaVMOption));
    memset(newOptions, 0, sizeof(newOptions));

    // fill new JVM arguments array.
    try {
        int iCurOption = 0;
        std::vector <LPSTR>::iterator Iter;

        for(Iter = FinalJvmOptions.begin() ; Iter != FinalJvmOptions.end() ; Iter++) {
            if(*Iter != NULL) {
                newOptions[iCurOption++].optionString = *Iter;
            }
        }

        newArgs.options = newOptions;
        newArgs.nOptions = iCurOption;
    } catch(...) {
        TRACE(L"Error in SAS_CreateJavaVM while generating new JavaVMInitArgs!\r\n");
        return JNI_ERR;
    }

    // make sure we have a valid function pointer to continue with JNI_CreateJavaVM().
    if(JniCreateJavaVM == NULL) {
        JniCreateJavaVM = (ORI_CreateJavaVM)GetProcAddress(hJvm, "JNI_CreateJavaVM");
    }

    if(JniCreateJavaVM == NULL) {
        TRACE(L"Error in SAS_CreateJavaVM: Couldn't find original JNI_CreateJavaVM entry point!\r\n");
        return JNI_ERR;
    }

    // call back into original function JNI_CreateJavaVM() (with restored header)
    jint jRet = JniCreateJavaVM(p_vm, p_env, &newArgs);

    if(jRet == JNI_OK) {
        TRACE(L"JVM Parameters injected successfully\r\n");
    } else {
        TRACE(L"Error in SAS_CreateJavaVM: JNI_CreateJavaVM return code = %d\r\n", jRet);
    }

    return jRet;
}

//************************************
// Method:    ApplyMutexSetting
// FullName:  ApplyMutexSetting
// Access:    public
// Returns:   void
// Qualifier:
//************************************
void ApplyMutexSetting()
{
    // This function is called by modified entry point header from Stock il2fb.exe
    // This is necessary since stock il2fb.exe is UPX-compressed, so we have to wait
    // until the executable has been decompressed in memory and step in before it's
    // being executed then.
    DWORD dwOldProtect = 0;
    DWORD dwNumRead = 0;
    DWORD dwProcRights = PROC_FULL_RIGHTS;
    DWORD dwCurProc = GetCurrentProcessId();
    HANDLE hCurProc = OpenProcess(dwProcRights, FALSE, dwCurProc);

    if(hCurProc != NULL) {
        int iIl2fbMutex = 0x00422068;

        try {
            if(NULL != VirtualProtectEx(hCurProc, (LPVOID)iIl2fbMutex, 15, PAGE_EXECUTE_READWRITE, &dwOldProtect)) {
                memset((LPVOID)iIl2fbMutex, 0, 15); // disable Mutex
                VirtualProtectEx(hCurProc, (LPVOID)iIl2fbMutex, 9, dwOldProtect, NULL);
            } else {
                TRACE(L"Error in ApplyMutexSetting: Couldn't get access to mutex string memory area!\r\n");
            }
        } catch(...) {
            TRACE(L"Error in ApplyMutexSetting while overwriting mutex string!\r\n");
        }

        CloseHandle(hCurProc);
    } else {
        TRACE(L"Error in ApplyMutexSetting: Couldn't aquire current process handle!\r\n");
    }

    __asm { // jump back to original entry point at process address 0x4128E9
        mov eax, 0x004128E9
        jmp eax
    }
    return;
}

//************************************
// Method:    IsServerExe
// FullName:  IsServerExe
// Access:    public
// Returns:   BOOL
// Qualifier:
//************************************
BOOL IsServerExe()
{
    // Check if the process executable is running IL-2 Server or Client
    DWORD dwOldProtect = 0;
    DWORD dwNumRead = 0;
    DWORD dwProcRights = DELETE | READ_CONTROL | SYNCHRONIZE | WRITE_DAC | WRITE_OWNER
                         | PROCESS_TERMINATE | PROCESS_CREATE_THREAD | PROCESS_SET_SESSIONID | PROCESS_VM_OPERATION | PROCESS_VM_READ | PROCESS_VM_WRITE | PROCESS_DUP_HANDLE | PROCESS_CREATE_PROCESS | PROCESS_SET_QUOTA | PROCESS_SET_INFORMATION | PROCESS_QUERY_INFORMATION | PROCESS_SUSPEND_RESUME;
    DWORD dwCurProc = GetCurrentProcessId();
    HANDLE hCurProc = OpenProcess(dwProcRights, FALSE, dwCurProc);
    BOOL bIsServer = FALSE;

    try {
        int iIl2ServerID = 0x0041EBA8;
        char* cFilesServer = "filesserver.sfs"; // this file reference is present in IL-2 Server executable only.

        if(NULL !=  VirtualProtectEx(hCurProc, (LPVOID)iIl2ServerID, 15, PAGE_EXECUTE_READWRITE, &dwOldProtect)) {
            if(memcmp((LPVOID)iIl2ServerID, cFilesServer, 15) == 0) {
                bIsServer = TRUE;
            }

            VirtualProtectEx(hCurProc, (LPVOID)iIl2ServerID, 9, dwOldProtect, NULL);
        } else {
            TRACE(L"Error in AdjustJvmParams: Couldn't get access to modded il2fb.exe server ID memory area!\r\n");
        }
    } catch(...) {
        TRACE(L"Error in AdjustJvmParams: Couldn't check modded il2fb.exe server ID!\r\n");
    }

    CloseHandle(hCurProc);
    return bIsServer;
}

//************************************
// Method:    AdjustJvmParams
// FullName:  AdjustJvmParams
// Access:    public
// Returns:   void
// Qualifier:
//************************************
void AdjustJvmParams()
{
    // This function is called when our DINPUT.dll is getting linked.
    // The process of IL-2 exists at this time so we can hijack a set of memory
    // areas and inject code, overwrite memory areas or shift pointers around
    GetParams();
    DWORD dwOldProtect = 0;
    DWORD dwProcRights = DELETE | READ_CONTROL | SYNCHRONIZE | WRITE_DAC | WRITE_OWNER
                         | PROCESS_TERMINATE | PROCESS_CREATE_THREAD | PROCESS_SET_SESSIONID | PROCESS_VM_OPERATION | PROCESS_VM_READ | PROCESS_VM_WRITE | PROCESS_DUP_HANDLE | PROCESS_CREATE_PROCESS | PROCESS_SET_QUOTA | PROCESS_SET_INFORMATION | PROCESS_QUERY_INFORMATION | PROCESS_SUSPEND_RESUME;
    DWORD dwCurProc = GetCurrentProcessId();
    HANDLE hCurProc = OpenProcess(dwProcRights, FALSE, dwCurProc);

    // Get original function pointer of Java VM's JNI_CreateJavaVM() function.
    if(JniCreateJavaVM == NULL) {
        JniCreateJavaVM = (ORI_CreateJavaVM)GetProcAddress(hJvm, "JNI_CreateJavaVM");
    }

    if(JniCreateJavaVM == NULL) {
        TRACE(L"Error in AdjustJvmParams: Couldn't find original JNI_CreateJavaVM entry point!\r\n");
        return;
    }

    // When running an IL-2 server, show some output string on command line
    if(IsServerExe()) {
        printf("#########################################################\r\n");
        printf("#                                                       #\r\n");
        printf("# Starting IL-2 Server with SAS/UP il2server.ini parser #\r\n");
        printf("#      with enhanced JVM parameter settings enabled     #\r\n");
        printf("#                                                       #\r\n");
        printf("#########################################################\r\n");
        printf("\r\n");
    }

    if(hCurProc != NULL) {
        int iJniCreateJavaVM = (int)JniCreateJavaVM;

        // Hijack the JNI_CreateJavaVM() function from JVM.dll and let it run SAS_CreateJavaVM() first.
        try {
            if(NULL != VirtualProtectEx(hCurProc, (LPVOID)JniCreateJavaVM, 9, PAGE_EXECUTE_READWRITE, &dwOldProtect)) {
                memcpy(g_oldMem, JniCreateJavaVM, 9);
                // overwrite 9 bytes of the function header with NOPs first.
                *(DWORD *)iJniCreateJavaVM = 0x90909090;
                *(DWORD *)(iJniCreateJavaVM + 4) = 0x90909090;
                *(BYTE *)(iJniCreateJavaVM + 8) = 0x90;
                // create a relative near jump (JMP bytecode E9) to our hook function SAS_CreateJavaVM()
                *(BYTE *)iJniCreateJavaVM = 0xE9;
                *(DWORD *)(iJniCreateJavaVM + 1) = (DWORD)SAS_CreateJavaVM - iJniCreateJavaVM - 5;
                VirtualProtectEx(hCurProc, (LPVOID)0x00420C00, 9, dwOldProtect, NULL);
            } else {
                TRACE(L"Error in AdjustJvmParams: Couldn't get access to JNI_CreateJavaVM function header memory area!\r\n");
            }
        } catch(...) {
            TRACE(L"Error in AdjustJvmParams: Couldn't inject code into JNI_CreateJavaVM function header memory area!\r\n");
        }

        if(bDisableMutex) {  // strip mutex string from process
            BOOL bIsStockExe = FALSE;
            BYTE checkStock[] = {0x32, 0x2E, 0x30, 0x30, 0x00}; // UPX 2.00 identification string in Stock il2fb.exe
            int iIl2fbUpxCheck = 0x004003DB; // Address of UPX 2.00 ID string prior to decompression

            try {
                if(NULL != VirtualProtectEx(hCurProc, (LPVOID)iIl2fbUpxCheck, 5, PAGE_EXECUTE_READWRITE, &dwOldProtect)) {
                    if(memcmp((LPVOID)iIl2fbUpxCheck, checkStock, 5) == 0) {
                        bIsStockExe = TRUE;
                    }

                    VirtualProtectEx(hCurProc, (LPVOID)iIl2fbUpxCheck, 5, dwOldProtect, NULL);
                } else {
                    TRACE(L"Error in AdjustJvmParams: Couldn't get access to il2fb.exe Version Check memory area!\r\n");
                }

                if(bIsStockExe) {  // disable Mutex for Stock il2fb.exe
                    try {
                        int iIl2fbUpxDecompressEnd = 0x01EA16EB;

                        if(NULL !=  VirtualProtectEx(hCurProc, (LPVOID)iIl2fbUpxDecompressEnd, 9, PAGE_EXECUTE_READWRITE, &dwOldProtect)) {
                            *(DWORD *)iIl2fbUpxDecompressEnd = 0x90909090; // fill with NOPs
                            *(DWORD *)(iIl2fbUpxDecompressEnd + 4) = 0x90909090; // fill with NOPs
                            *(BYTE *)(iIl2fbUpxDecompressEnd + 8) = 0x90; // fill with NOPs
                            *(BYTE *)iIl2fbUpxDecompressEnd = 0xE9; // near JMP
                            *(DWORD *)(iIl2fbUpxDecompressEnd + 1) = (DWORD)ApplyMutexSetting - 0x01EA16EB - 5; // run ApplyMutexSetting() after decompressing
                            VirtualProtectEx(hCurProc, (LPVOID)iIl2fbUpxDecompressEnd, 9, dwOldProtect, NULL);
                        } else {
                            TRACE(L"Error in AdjustJvmParams: Couldn't get access to Stock il2fb.exe entry point memory area!\r\n");
                        }
                    } catch(...) {
                        TRACE(L"Error in AdjustJvmParams: Couldn't inject code into Stock il2fb.exe entry point memory area!\r\n");
                    }
                } else { // disable Mutex for mod enabled il2fb.exe
                    if(!IsServerExe()) {
                        try {
                            int iIl2fbMutex = 0x0042206C;

                            if(NULL !=  VirtualProtectEx(hCurProc, (LPVOID)iIl2fbMutex, 15, PAGE_EXECUTE_READWRITE, &dwOldProtect)) {
                                memset((LPVOID)iIl2fbMutex, 0, 15); // disable Mutex
                                VirtualProtectEx(hCurProc, (LPVOID)iIl2fbMutex, 9, dwOldProtect, NULL);
                            } else {
                                TRACE(L"Error in AdjustJvmParams: Couldn't get access to modded il2fb.exe mutex string memory area!\r\n");
                            }
                        } catch(...) {
                            TRACE(L"Error in AdjustJvmParams: Couldn't erase modded il2fb.exe mutex string!\r\n");
                        }
                    }
                }
            } catch(...) {
                TRACE(L"Error in AdjustJvmParams: Couldn't check il2fb.exe Version!\r\n");
            }
        }
    } else {
        TRACE(L"Error in SAS_CreateJavaVM: Couldn't aquire current process handle!\r\n");
    }

    CloseHandle(hCurProc);
}

//************************************
// Method:    AddJvmOption
// FullName:  AddJvmOption
// Access:    public
// Returns:   void
// Qualifier:
// Parameter: LPSTR lpOptionString
//************************************
void AddJvmOption(LPSTR lpOptionString)
{
    LPSTR lpNewOptionString = (LPSTR)malloc(sizeof(char) * (strlen(lpOptionString) + 1));
    LPSTR lpNewOptionStringLwr = (LPSTR)malloc(sizeof(char) * (strlen(lpOptionString) + 1));
    strcpy(lpNewOptionString, lpOptionString);
    strcpy(lpNewOptionStringLwr, lpOptionString);
    strlwr(lpNewOptionStringLwr);
    MyJvmOptionItem myNewItem;
    myNewItem.lpJvmOptionItem = lpNewOptionString;
    myNewItem.lpJvmOptionItemLowercase = lpNewOptionStringLwr;
    JvmOptions.push_back(myNewItem);
    TRACE("Adding JVM Option: %s\r\n", lpNewOptionString);
}

//************************************
// Method:    ReadSelectorSettings
// FullName:  ReadSelectorSettings
// Access:    public
// Returns:   void
// Qualifier:
//************************************
void ReadSelectorSettings()
{
    bDisableMutex = (GetPrivateProfileInt(L"Settings", L"MultipleInstances", 0, szIniFile) != 0);
    int iSelectorRamSize = GetPrivateProfileInt(L"Settings", L"RamSize", 0, szIniFile);
    int iSelectorMemStrategy = GetPrivateProfileInt(L"Settings", L"MemoryStrategy", 0, szIniFile);

    if(iSelectorRamSize != 0) {
        TRACE(L"Applying JVM Memory Settings from IL-2 Selector...\r\n");
        int iStackSize = (iSelectorRamSize * 1024) / XSS_DIVIDER;
        int iPermSize = iSelectorRamSize / PERM_DIVIDER;

        switch(iSelectorMemStrategy) {
        case MEM_STRATEGY_CONSERVATIVE:
                iStackSize /= 2;
            iPermSize /= 2;
            break;

        case MEM_STRATEGY_HEAPONLY:
                iStackSize = 0;
            iPermSize = 0;
            break;

        default:
                break;
        }

        int iDynamicSize = iSelectorRamSize - (iStackSize / 1024) - iPermSize;
        LPSTR lpBuf = (LPSTR)malloc(sizeof(char) * 16);
        memset(lpBuf, 0, sizeof(lpBuf));
        sprintf(lpBuf, "-Xms%dM", iDynamicSize);
        AddJvmOption(lpBuf);
        memset(lpBuf, 0, sizeof(lpBuf));
        sprintf(lpBuf, "-Xmx%dM", iDynamicSize);
        AddJvmOption(lpBuf);

        if(iSelectorMemStrategy == MEM_STRATEGY_HEAPONLY) {
            return;
        }

        memset(lpBuf, 0, sizeof(lpBuf));
        sprintf(lpBuf, "-Xss%dK", iStackSize);
        AddJvmOption(lpBuf);
        memset(lpBuf, 0, sizeof(lpBuf));
        sprintf(lpBuf, "-XX:PermSize=%dM", iPermSize);
        AddJvmOption(lpBuf);
        memset(lpBuf, 0, sizeof(lpBuf));
        sprintf(lpBuf, "-XX:MaxPermSize=%dM", iPermSize);
        AddJvmOption(lpBuf);
    }
}

//************************************
// Method:    ReadJvmOptions
// FullName:  ReadJvmOptions
// Access:    public
// Returns:   void
// Qualifier:
//************************************
void ReadJvmOptions()
{
    TCHAR szJvmOptionsInFile[0xFFFF];
    memset(szJvmOptionsInFile, 0, sizeof(szJvmOptionsInFile));
    TCHAR szJvmOptions[0xFFFF];
    memset(szJvmOptions, 0, sizeof(szJvmOptions));
    char cBuf[MAX_PATH];
    GetPrivateProfileSection(L"JVM", szJvmOptionsInFile, 0xFFFF, szIniFile);
    LPTSTR lpJvmOptionToken = szJvmOptionsInFile;

    while(_tcslen(lpJvmOptionToken) != 0) {
        memset(cBuf, 0, sizeof(cBuf));
        wcstombs(cBuf, lpJvmOptionToken, MAX_PATH);
        AddJvmOption(cBuf);
        lpJvmOptionToken += _tcslen(lpJvmOptionToken) + 1;
    }
}

//************************************
// Method:    IsInJvmOptions
// FullName:  IsInJvmOptions
// Access:    public
// Returns:   int
// Qualifier:
// Parameter: LPCSTR lpSearch
// Parameter: unsigned int iStartIndex
//************************************
int IsInJvmOptions(LPCSTR lpSearch, unsigned int iStartIndex)
{
    for(unsigned int i = iStartIndex; i < JvmOptions.size(); i++) {
        try {
            if(JvmOptions[i].lpJvmOptionItemLowercase == NULL) {
                continue;
            }

            if(strnicmp(JvmOptions[i].lpJvmOptionItemLowercase, lpSearch, strlen(lpSearch)) == 0) {
                return i;
            }
        } catch(...) {
            continue;
        }
    }

    return -1;
}

//************************************
// Method:    AddMandatoryJvmOptions
// FullName:  AddMandatoryJvmOptions
// Access:    public
// Returns:   void
// Qualifier:
//************************************
void AddMandatoryJvmOptions()
{
    TRACE(L"Checking mandatory JVM Options...\r\n");

    if(IsInJvmOptions("-Djava.class.path", 0) == -1) {
        AddJvmOption("-Djava.class.path=.");
    }

    if(IsInJvmOptions("-Xincgc", 0) == -1) {
        AddJvmOption("-Xincgc");
    }

    if(IsInJvmOptions("-Xverify", 0) == -1) {
        AddJvmOption("-Xverify:none");
    }

    if(IsInJvmOptions("-Xcomp", 0) == -1) {
        AddJvmOption("-Xcomp");
    }
}

//************************************
// Method:    EliminateDuplicateJvmOptions
// FullName:  EliminateDuplicateJvmOptions
// Access:    public
// Returns:   void
// Qualifier:
//************************************
void EliminateDuplicateJvmOptions()
{
    TRACE(L"Checking duplicate JVM Options...\r\n");
    char cOption[MAX_PATH];
    LPSTR lpOption = NULL;

    for(unsigned int i = 0; i < JvmOptions.size() - 1; i++) {
        strcpy(cOption, JvmOptions[i].lpJvmOptionItemLowercase);

        if(strnicmp(cOption, "-xx:", 4) == 0) {
            lpOption = &cOption[4];
        } else {
            lpOption = cOption;
        }

        int iPos = strcspn(lpOption, "=:0123456789");
        lpOption[iPos] = '\0';

        if(IsInJvmOptions(lpOption, i + 1) != -1) {
            // remove Element from vector
            // since it's overridden by another element of same type later
            TRACE("Removing duplicate JVM Option: %s\r\n", JvmOptions[i].lpJvmOptionItem);
            free((LPVOID)(JvmOptions[i].lpJvmOptionItem));
            free((LPVOID)(JvmOptions[i].lpJvmOptionItemLowercase));
            JvmOptions[i].lpJvmOptionItem = NULL;
            JvmOptions[i].lpJvmOptionItemLowercase = NULL;
        }
    }
}

//************************************
// Method:    CreateFinalJvmOptions
// FullName:  CreateFinalJvmOptions
// Access:    public
// Returns:   void
// Qualifier:
//************************************
void CreateFinalJvmOptions()
{
    TRACE(L"Final JVM Option List:\r\n");
    std::vector <MyJvmOptionItem>::iterator Iter;

    for(Iter = JvmOptions.begin() ; Iter != JvmOptions.end() ; Iter++) {
        if(Iter->lpJvmOptionItem != NULL) {
            // reserve memory for final Options list Item
            LPSTR lpJvmOption = (LPSTR)malloc(sizeof(char) * (strlen(Iter->lpJvmOptionItem) + 1));
            strcpy(lpJvmOption, Iter->lpJvmOptionItem);
            TRACE("%s\r\n", lpJvmOption);
            FinalJvmOptions.push_back(lpJvmOption);
        }
    }
}

//************************************
// Method:    ClearJvmOptionsList
// FullName:  ClearJvmOptionsList
// Access:    public
// Returns:   void
// Qualifier:
//************************************
void ClearJvmOptionsList()
{
    std::vector <MyJvmOptionItem>::iterator Iter;

    for(Iter = JvmOptions.begin() ; Iter != JvmOptions.end() ; Iter++) {
        if(Iter->lpJvmOptionItem != NULL) {
            free((LPVOID)Iter->lpJvmOptionItem);
        }

        if(Iter->lpJvmOptionItemLowercase != NULL) {
            free((LPVOID)Iter->lpJvmOptionItemLowercase);
        }
    }

    JvmOptions.clear();
}

//************************************
// Method:    ClearFinalJvmOptionsList
// FullName:  ClearFinalJvmOptionsList
// Access:    public
// Returns:   void
// Qualifier:
//************************************
void ClearFinalJvmOptionsList()
{
    std::vector <LPSTR>::iterator Iter;

    for(Iter = FinalJvmOptions.begin() ; Iter != FinalJvmOptions.end() ; Iter++) {
        if(*Iter != NULL) {
            free((LPVOID)*Iter);
        }
    }

    FinalJvmOptions.clear();
}

//************************************
// Method:    GetParams
// FullName:  GetParams
// Access:    public
// Returns:   void
// Qualifier:
//************************************
void GetParams()
{
    JvmOptions.clear();
    FinalJvmOptions.clear();
    ReadSelectorSettings();
    ReadJvmOptions();
    EliminateDuplicateJvmOptions();
    AddMandatoryJvmOptions();
    CreateFinalJvmOptions();
}

//************************************
// Method:    StartWatchdog
// FullName:  StartWatchdog
// Access:    public 
// Returns:   BOOL
// Qualifier:
//************************************
BOOL StartWatchdog()
{
    STARTUPINFO si;
    PROCESS_INFORMATION pi;
    ZeroMemory(&si, sizeof(si));
    si.cb = sizeof(si);
    ZeroMemory(&pi, sizeof(pi));
    TCHAR szWatchdogFile[MAX_PATH];
    memset(szWatchdogFile, 0, sizeof(szWatchdogFile));
    TCHAR szWatchdogWorkFolder[MAX_PATH];
    memset(szWatchdogWorkFolder, 0, sizeof(szWatchdogWorkFolder));
    GetModuleFileName(NULL, szWatchdogWorkFolder, MAX_PATH);
    LPTSTR pszFileName = _tcsrchr(szWatchdogWorkFolder, '\\') + 1;
    *pszFileName = '\0';
    _tcscpy(szWatchdogFile, szWatchdogWorkFolder);
    _tcscat(szWatchdogFile, L"bin\\selector\\basefiles\\IL-2 Watchdog.exe");
    TRACE(L"Starting Watchdog at %s\r\n", szWatchdogFile);

    if(!CreateProcess(NULL, szWatchdogFile, NULL,
                      NULL, FALSE, CREATE_NEW_CONSOLE, NULL, szWatchdogWorkFolder, &si, &pi)) {
        TRACE(L"Watchdog Process not created\r\n");
        return FALSE;
    }

    CloseHandle(pi.hThread);
    CloseHandle(pi.hProcess);
    TRACE(L"Watchdog process started, waiting for message loop creation...\r\n");
    HANDLE hWaitForWatchdogMessageWindow = CreateEvent(NULL, FALSE, FALSE, EVENT_WATCHDOG_MESSAGEWINDOW_ACTIVE);

    if(hWaitForWatchdogMessageWindow == NULL || hWaitForWatchdogMessageWindow == INVALID_HANDLE_VALUE) {
        TRACE(L"Watchdog Callback Event not created\r\n");
        return FALSE;
    }

    if(WaitForSingleObject(hWaitForWatchdogMessageWindow, WATCHDOG_WAIT_TIMEOUT) == WAIT_TIMEOUT) {
        TRACE(L"Watchdog Callback not received\r\n");
        CloseHandle(hWaitForWatchdogMessageWindow);
        return FALSE;
    }

    CloseHandle(hWaitForWatchdogMessageWindow);
    TRACE(L"Watchdog Message Loop is running, fetching Message Window Handle.\r\n");
    HWND hWatchdogMessageWindow = FindWindowEx(HWND_MESSAGE, NULL, c_szMsgWndClass, NULL);

    if(hWatchdogMessageWindow == NULL) {
        TRACE(L"Watchdog Message Window not found\r\n");
        return FALSE;
    }

    TRACE(L"Watchdog Message Window found (handle 0x%08X), sending Process-ID and Thread-ID.\r\n", hWatchdogMessageWindow);
    PostMessage(hWatchdogMessageWindow, WM_WATCHDOG, (WPARAM)GetCurrentProcessId(), (LPARAM)GetCurrentThreadId());
    TRACE(L"Watchdog initialized.\r\n");
    return TRUE;
}