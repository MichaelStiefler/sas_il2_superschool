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

#include "stdafx.h"
#include "SplashScreen.h"
#include "resource.h"
#include "trace.h"
#include "gdiplus.h"
#include "tchar.h"
#include <assert.h>
#include "winver.h"
#pragma comment(lib, "version.lib")

#pragma comment(lib, "gdiplus.lib")

HWND g_hSplashWnd = NULL;
static ULONG_PTR gdiplusToken;
static ULONG_PTR gdiplusBGThreadToken;
static Gdiplus::GdiplusStartupInput gdiplusStartupInput;
static Gdiplus::GdiplusStartupOutput gdiplusStartupOutput;
static TCHAR szVersionInfo[MAX_PATH];


// Creates a stream object initialized with the data from an executable resource.
//************************************
// Method:    CreateStreamOnResource
// FullName:  CreateStreamOnResource
// Access:    public 
// Returns:   IStream *
// Qualifier:
// Parameter: HMODULE hModule
// Parameter: LPCTSTR lpName
// Parameter: LPCTSTR lpType
//************************************
IStream * CreateStreamOnResource(HMODULE hModule, LPCTSTR lpName, LPCTSTR lpType)
{
    // initialize return value
    IStream * ipStream = NULL;
    // find the resource
    HRSRC hrsrc = FindResource(hModule, lpName, lpType);

    if(hrsrc == NULL) {
        return ipStream;
    }

    // load the resource
    DWORD dwResourceSize = SizeofResource(hModule, hrsrc);
    HGLOBAL hglbImage = LoadResource(hModule, hrsrc);

    if(hglbImage == NULL) {
        return ipStream;
    }

    // lock the resource, getting a pointer to its data
    LPVOID pvSourceResourceData = LockResource(hglbImage);

    if(pvSourceResourceData == NULL) {
        return ipStream;
    }

    // allocate memory to hold the resource data
    HGLOBAL hgblResourceData = GlobalAlloc(GMEM_MOVEABLE, dwResourceSize);

    if(hgblResourceData == NULL) {
        return ipStream;
    }

    // get a pointer to the allocated memory
    LPVOID pvResourceData = GlobalLock(hgblResourceData);

    if(pvResourceData != NULL) {
        // copy the data from the resource to the new memory block
        CopyMemory(pvResourceData, pvSourceResourceData, dwResourceSize);
        GlobalUnlock(hgblResourceData);

        // create a stream on the HGLOBAL containing the data
        if(SUCCEEDED(CreateStreamOnHGlobal(hgblResourceData, TRUE, &ipStream))) {
            return ipStream;
        }
    }

    // couldn't create stream; free the memory
    GlobalFree(hgblResourceData);
    // no need to unlock or free the resource
    return ipStream;
}

// Loads a PNG image from the specified stream (using Windows Imaging Component).
//************************************
// Method:    LoadBitmapFromStream
// FullName:  LoadBitmapFromStream
// Access:    public 
// Returns:   IWICBitmapSource *
// Qualifier:
// Parameter: IStream * ipImageStream
//************************************
IWICBitmapSource * LoadBitmapFromStream(IStream * ipImageStream)
{
    // initialize return value
    IWICBitmapSource * ipBitmap = NULL;
    // load WIC's PNG decoder
    IWICBitmapDecoder * ipDecoder = NULL;

    if(FAILED(CoCreateInstance(CLSID_WICPngDecoder, NULL, CLSCTX_INPROC_SERVER, __uuidof(ipDecoder), reinterpret_cast<void**>(&ipDecoder)))) {
        return ipBitmap;
    }

    // load the PNG
    if(SUCCEEDED(ipDecoder->Initialize(ipImageStream, WICDecodeMetadataCacheOnLoad))) {
        // check for the presence of the first frame in the bitmap
        UINT nFrameCount = 0;

        if(SUCCEEDED(ipDecoder->GetFrameCount(&nFrameCount)) || nFrameCount != 1) {
            // load the first frame (i.e., the image)
            IWICBitmapFrameDecode * ipFrame = NULL;

            if(SUCCEEDED(ipDecoder->GetFrame(0, &ipFrame))) {
                // convert the image to 32bpp BGRA format with pre-multiplied alpha
                //   (it may not be stored in that format natively in the PNG resource,
                //   but we need this format to create the DIB to use on-screen)
                WICConvertBitmapSource(GUID_WICPixelFormat32bppPBGRA, ipFrame, &ipBitmap);
                ipFrame->Release();
            }
        }
    }

    ipDecoder->Release();
    return ipBitmap;
}

// Creates a 32-bit DIB from the specified WIC bitmap.
//************************************
// Method:    CreateHBITMAP
// FullName:  CreateHBITMAP
// Access:    public 
// Returns:   HBITMAP
// Qualifier:
// Parameter: IWICBitmapSource * ipBitmap
//************************************
HBITMAP CreateHBITMAP(IWICBitmapSource * ipBitmap)
{
    // initialize return value
    HBITMAP hbmp = NULL;
    // get image attributes and check for valid image
    UINT width = 0;
    UINT height = 0;

    if(FAILED(ipBitmap->GetSize(&width, &height)) || width == 0 || height == 0) {
        return hbmp;
    }

    // prepare structure giving bitmap information (negative height indicates a top-down DIB)
    BITMAPINFO bminfo;
    ZeroMemory(&bminfo, sizeof(bminfo));
    bminfo.bmiHeader.biSize = sizeof(BITMAPINFOHEADER);
    bminfo.bmiHeader.biWidth = width;
    bminfo.bmiHeader.biHeight = -((LONG) height);
    bminfo.bmiHeader.biPlanes = 1;
    bminfo.bmiHeader.biBitCount = 32;
    bminfo.bmiHeader.biCompression = BI_RGB;
    // create a DIB section that can hold the image
    void * pvImageBits = NULL;
    HDC hdcScreen = GetDC(NULL);
    hbmp = CreateDIBSection(hdcScreen, &bminfo, DIB_RGB_COLORS, &pvImageBits, NULL, 0);
    ReleaseDC(NULL, hdcScreen);

    if(hbmp == NULL) {
        return hbmp;
    }

    // extract the image into the HBITMAP
    const UINT cbStride = width * 4;
    const UINT cbImage = cbStride * height;

    if(FAILED(ipBitmap->CopyPixels(NULL, cbStride, cbImage, static_cast<BYTE *>(pvImageBits)))) {
        // couldn't extract image; delete HBITMAP
        DeleteObject(hbmp);
        hbmp = NULL;
    }

    return hbmp;
}

// Loads the PNG containing the splash image into a HBITMAP.
//************************************
// Method:    LoadSplashImage
// FullName:  LoadSplashImage
// Access:    public 
// Returns:   HBITMAP
// Qualifier:
// Parameter: HMODULE hModule
// Parameter: LPCTSTR lpName
// Parameter: LPCTSTR lpType
//************************************
HBITMAP LoadSplashImage(HMODULE hModule, LPCTSTR lpName, LPCTSTR lpType)
{
    HBITMAP hbmpSplash = NULL;
    // load the PNG image data into a stream
    IStream * ipImageStream = CreateStreamOnResource(hModule, lpName, lpType);

    if(ipImageStream == NULL) {
        return hbmpSplash;
    }

    // load the bitmap with WIC
    IWICBitmapSource * ipBitmap = LoadBitmapFromStream(ipImageStream);

    if(ipBitmap != NULL) {
        // create a HBITMAP containing the image
        hbmpSplash = CreateHBITMAP(ipBitmap);
        ipBitmap->Release();
    }

    ipImageStream->Release();
    return hbmpSplash;
}

// Registers a window class for the splash and splash owner windows.
//************************************
// Method:    RegisterWindowClass
// FullName:  RegisterWindowClass
// Access:    public 
// Returns:   void
// Qualifier:
// Parameter: HINSTANCE hInstance
// Parameter: LPCTSTR lpIconName
// Parameter: LPCTSTR lpszClassName
//************************************
void RegisterWindowClass(HINSTANCE hInstance, LPCTSTR lpIconName, LPCTSTR lpszClassName)
{
    WNDCLASS wc = { 0 };
    //wc.lpfnWndProc = DefWindowProc;
    wc.lpfnWndProc = WndProc;
    wc.hInstance = hInstance;
    //wc.hIcon = LoadIcon(hInstance, MAKEINTRESOURCE(IDI_ICON_SASUP));
    wc.hIcon = LoadIcon(hInstance, lpIconName);
    wc.hCursor = LoadCursor(NULL, IDC_ARROW);
    wc.lpszClassName = lpszClassName;
    RegisterClass(&wc);
}

// Creates the splash owner window and the splash window.
//************************************
// Method:    CreateSplashWindow
// FullName:  CreateSplashWindow
// Access:    public 
// Returns:   HWND
// Qualifier:
// Parameter: HINSTANCE hInstance
// Parameter: LPCTSTR lpszClassName
//************************************
HWND CreateSplashWindow(HINSTANCE hInstance, LPCTSTR lpszClassName)
{
    HWND hwndOwner = CreateWindow(lpszClassName, NULL, WS_POPUP,
                                  0, 0, 0, 0, NULL, NULL, hInstance, NULL);
    return CreateWindowEx(WS_EX_LAYERED | WS_EX_TOPMOST | WS_EX_NOACTIVATE, lpszClassName, NULL, WS_POPUP | WS_VISIBLE,
                          0, 0, 0, 0, hwndOwner, NULL, hInstance, NULL);
}

// Calls UpdateLayeredWindow to set a bitmap (with alpha) as the content of the splash window.
//************************************
// Method:    SetSplashImage
// FullName:  SetSplashImage
// Access:    public 
// Returns:   void
// Qualifier:
// Parameter: HWND hwndSplash
// Parameter: HBITMAP hbmpSplash
//************************************
void SetSplashImage(HWND hwndSplash, HBITMAP hbmpSplash)
{
    // get the size of the bitmap
    BITMAP bm;
    GetObject(hbmpSplash, sizeof(bm), &bm);
    SIZE sizeSplash = { bm.bmWidth, bm.bmHeight };
    // get the primary monitor's info
    POINT ptZero = { 0 };
    HMONITOR hmonPrimary = MonitorFromPoint(ptZero, MONITOR_DEFAULTTOPRIMARY);
    MONITORINFO monitorinfo = { 0 };
    monitorinfo.cbSize = sizeof(monitorinfo);
    GetMonitorInfo(hmonPrimary, &monitorinfo);
    // center the splash screen in the middle of the primary work area
    const RECT & rcWork = monitorinfo.rcWork;
    POINT ptOrigin;
    ptOrigin.x = rcWork.left + (rcWork.right - rcWork.left - sizeSplash.cx) / 2;
    ptOrigin.y = rcWork.top + (rcWork.bottom - rcWork.top - sizeSplash.cy) / 2;
    // create a memory DC holding the splash bitmap
    HDC hdcScreen = GetDC(NULL);
    HDC hdcMem = CreateCompatibleDC(hdcScreen);
    HBITMAP hbmpOld = (HBITMAP) SelectObject(hdcMem, hbmpSplash);
    Gdiplus::Graphics Gx(hdcMem);
    Gdiplus::Font myFont(L"Times New Roman", 10);
    Gdiplus::PointF thePoint(40, 320);
    Gdiplus::SolidBrush GxTextBrush(Gdiplus::Color(255, 255, 0, 0));
    //WCHAR thetext[] = L"Selector 3.0 Beta 02";
    int stats = Gx.DrawString(szVersionInfo, -1, &myFont, thePoint, &GxTextBrush);
    // use the source image's alpha channel for blending
    BLENDFUNCTION blend = { 0 };
    blend.BlendOp = AC_SRC_OVER;
    blend.SourceConstantAlpha = 255;
    blend.AlphaFormat = AC_SRC_ALPHA;
    // paint the window (in the right location) with the alpha-blended bitmap
    UpdateLayeredWindow(hwndSplash, hdcScreen, &ptOrigin, &sizeSplash,
                        hdcMem, &ptZero, RGB(0, 0, 0), &blend, ULW_ALPHA);
    // delete temporary objects
    SelectObject(hdcMem, hbmpOld);
    //cleanup
    DeleteDC(hdcMem);
    ReleaseDC(NULL, hdcScreen);
}

//************************************
// Method:    GetSelectorVersion
// FullName:  GetSelectorVersion
// Access:    public 
// Returns:   void
// Qualifier:
//************************************
void GetSelectorVersion()
{
    // allocate a block of memory for the version info
    TCHAR szWatchdogFileName[MAX_PATH];
    DWORD dummy;
    GetModuleFileName(NULL, szWatchdogFileName, MAX_PATH);
    DWORD dwSize = GetFileVersionInfoSize(szWatchdogFileName, &dummy);

    if(dwSize != 0) {
        BYTE *data = (BYTE*)malloc(dwSize);

        // load the version info
        if(GetFileVersionInfo(szWatchdogFileName, NULL, dwSize, &data[0])) {
            // get the name and version strings
            LPVOID pvProductName = NULL;
            unsigned int iProductNameLen = 0;
            LPVOID pvProductVersion = NULL;
            unsigned int iProductVersionLen = 0;

            // replace "040904e4" with the language ID of your resources
            if(VerQueryValue(&data[0], _T("\\StringFileInfo\\000004b0\\ProductName"), &pvProductName, &iProductNameLen) &&
                    VerQueryValue(&data[0], _T("\\StringFileInfo\\000004b0\\ProductVersion"), &pvProductVersion, &iProductVersionLen)) {
                _tcscpy(szVersionInfo, L"IL-2 Selector ");
                _tcsncat(szVersionInfo, (LPCTSTR)pvProductVersion, iProductVersionLen);
                _tcscat(szVersionInfo, L" © by SAS/Ultrapack");
                //strProductName.SetString((LPCTSTR)pvProductName, iProductNameLen);
                //strProductVersion.SetString((LPCTSTR)pvProductVersion, iProductVersionLen);
            }
        }
    }
}

//************************************
// Method:    ShowSplash
// FullName:  ShowSplash
// Access:    public 
// Returns:   void
// Qualifier:
// Parameter: HINSTANCE hInstance
// Parameter: LPCTSTR lpIconName
// Parameter: LPCTSTR lpszClassName
// Parameter: LPCTSTR lpszSplashTitle
// Parameter: LPCTSTR lpSplashImageName
// Parameter: LPCTSTR lpSplashImageType
//************************************
void ShowSplash(HINSTANCE hInstance, LPCTSTR lpIconName, LPCTSTR lpszClassName, LPCTSTR lpszSplashTitle, LPCTSTR lpSplashImageName, LPCTSTR lpSplashImageType)
{
    GetSelectorVersion();
    SplashParams* mySplashParams = new SplashParams(hInstance, lpIconName, lpszClassName, lpszSplashTitle, lpSplashImageName, lpSplashImageType);
    HANDLE hSplashThread = CreateThread(NULL, 0, (LPTHREAD_START_ROUTINE)StartSplashThread, mySplashParams, CREATE_SUSPENDED, NULL);
    SetThreadPriority(hSplashThread, THREAD_PRIORITY_HIGHEST);
    ResumeThread(hSplashThread);
    CloseHandle(hSplashThread);

    //CloseHandle(CreateThread(NULL,0,(LPTHREAD_START_ROUTINE)StartSplashThread,mySplashParams,0,NULL));

    while(FindWindow(lpszClassName, NULL) == NULL) {
        Sleep(100);
    }

    //CoInitialize(NULL);
    //// Initialization
    //gdiplusStartupInput.SuppressBackgroundThread = TRUE;
    //Gdiplus::GdiplusStartup(&gdiplusToken, &gdiplusStartupInput,
    //	&gdiplusStartupOutput);
    //Gdiplus::Status stat = gdiplusStartupOutput.NotificationHook(
    //	&gdiplusBGThreadToken);
    //assert(stat == Gdiplus::Ok);
    //RegisterWindowClass(hInstance, lpIconName, lpszClassName);
    //HBITMAP hSplashImage = LoadSplashImage(hInstance, lpSplashImageName, lpSplashImageType);
    //g_hSplashWnd = CreateSplashWindow(hInstance, lpszClassName);
    //SetWindowTextW(g_hSplashWnd, lpszSplashTitle);
    //SetSplashImage(g_hSplashWnd, hSplashImage);
}

//************************************
// Method:    CloseSplash
// FullName:  CloseSplash
// Access:    public 
// Returns:   void
// Qualifier:
//************************************
void CloseSplash()
{
    if(g_hSplashWnd == NULL) {
        return;
    }

    PostMessage(g_hSplashWnd, WM_CLOSE, 0, 0);
    return;
    /*
    //OutputDebugString(L"CloseSplash.\r\n");
    ShowWindowAsync(g_hSplashWnd, SW_HIDE);
    //DestroyWindow(g_hSplashWnd);
    //PostMessage(g_hSplashWnd, WM_CLOSE, 0, 0);
    PostMessage(g_hSplashWnd, WM_DESTROY, 0, 0);
    PostMessage(g_hSplashWnd, WM_NCDESTROY, 0, 0);
    g_hSplashWnd = NULL;
    CoUninitialize();
    // Termination
    gdiplusStartupOutput.NotificationUnhook(gdiplusBGThreadToken);
    Gdiplus::GdiplusShutdown(gdiplusToken);
    */
}


//************************************
// Method:    StartSplashThread
// FullName:  StartSplashThread
// Access:    public 
// Returns:   UINT
// Qualifier:
// Parameter: LPVOID pParam
//************************************
UINT StartSplashThread(LPVOID pParam)
{
    CoInitialize(NULL);
    gdiplusStartupInput.SuppressBackgroundThread = TRUE;
    Gdiplus::GdiplusStartup(&gdiplusToken, &gdiplusStartupInput,
                            &gdiplusStartupOutput);
    Gdiplus::Status stat = gdiplusStartupOutput.NotificationHook(
                               &gdiplusBGThreadToken);
    assert(stat == Gdiplus::Ok);
    SplashParams* mySplashParams = (SplashParams*)pParam;
    RegisterWindowClass(mySplashParams->hInstance, mySplashParams->lpIconName, mySplashParams->lpszClassName);
    HBITMAP hSplashImage = LoadSplashImage(mySplashParams->hInstance, mySplashParams->lpSplashImageName, mySplashParams->lpSplashImageType);
    g_hSplashWnd = CreateSplashWindow(mySplashParams->hInstance, mySplashParams->lpszClassName);
    SetWindowTextW(g_hSplashWnd, mySplashParams->lpszSplashTitle);
    SetSplashImage(g_hSplashWnd, hSplashImage);
    MSG msg;
    BOOL bRet;

    while((bRet = GetMessage(&msg, NULL, 0, 0)) != 0) {
        if(bRet == -1) {
            // handle the error and possibly exit
            break;
        } else {
            TranslateMessage(&msg);
            DispatchMessage(&msg);

            //if (msg.hwnd == g_hSplashWnd) {
            if((msg.message == WM_CLOSE) || (msg.message == WM_DESTROY)) {
                break;
            }

            //}
        }
    }

    // Return the exit code to the system.
    CoUninitialize();
    //OutputDebugString(L"SplashThread Stop.\r\n");
    return msg.wParam;
    //return 0;
}

// Callback Function
//************************************
// Method:    WndProc
// FullName:  WndProc
// Access:    public 
// Returns:   LRESULT CALLBACK
// Qualifier:
// Parameter: HWND hwnd
// Parameter: UINT Message
// Parameter: WPARAM wParam
// Parameter: LPARAM lParam
//************************************
LRESULT CALLBACK WndProc(HWND hwnd, UINT Message, WPARAM wParam, LPARAM lParam)
{
    switch(Message) {
    case WM_CLOSE:
        ShowWindowAsync(g_hSplashWnd, SW_HIDE);
        DestroyWindow(hwnd);
        g_hSplashWnd = NULL;
        CoUninitialize();
        // Termination
        gdiplusStartupOutput.NotificationUnhook(gdiplusBGThreadToken);
        Gdiplus::GdiplusShutdown(gdiplusToken);
        break;

        //case WM_DESTROY:
        //	//PostQuitMessage(0);
        //	break;
    default:
        return DefWindowProc(hwnd, Message, wParam, lParam);
    }

    return 0;
}