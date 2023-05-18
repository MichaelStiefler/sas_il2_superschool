// SASDll.cpp : Defines the exported functions for the DLL application.
//

#include "stdafx.h"
#include "CommonUtils.h"
#include "jni\jni.h"
#include <fstream>
#include <stdio.h>
#include <string>
#include <vector>
#include <windows.h>
#include <conio.h>
#include <iostream>

#define _CRT_SECURE_NO_WARNINGS
#pragma warning( disable : 4996 )

// Forward Declarations
LPWSTR GetXpFilePath(HANDLE hFile);

// Global Variables
static CHAR cBuf[MAX_PATH];

BOOL isVistaOrNewer() {
	OSVERSIONINFO info;
	ZeroMemory(&info, sizeof(OSVERSIONINFO));
	info.dwOSVersionInfoSize = sizeof(OSVERSIONINFO);
	GetVersionEx(&info);
	return (info.dwMajorVersion > 5);
}

std::wstring s2ws(const std::string& s)
{
	int len;
	int slength = (int)s.length() + 1;
	len = MultiByteToWideChar(CP_ACP, 0, s.c_str(), slength, 0, 0);
	wchar_t* buf = new wchar_t[len];
	MultiByteToWideChar(CP_ACP, 0, s.c_str(), slength, buf, len);
	std::wstring r(buf);
	delete[] buf;
	return r;
}

std::string ws2s(const std::wstring& s)
{
	int len;
	int slength = (int)s.length() + 1;
	len = WideCharToMultiByte(CP_ACP, 0, s.c_str(), slength, 0, 0, 0, 0);
	char* buf = new char[len];
	WideCharToMultiByte(CP_ACP, 0, s.c_str(), slength, buf, len, 0, 0);
	std::string r(buf);
	delete[] buf;
	return r;
}

std::string js2s(JNIEnv* env, const jstring jstr) {
	const jclass stringClass = env->GetObjectClass(jstr);
	const jmethodID getBytes = env->GetMethodID(stringClass, "getBytes", "(Ljava/lang/String;)[B");
	const jbyteArray stringJbytes = (jbyteArray)env->CallObjectMethod(jstr, getBytes, env->NewStringUTF("UTF-8"));

	size_t length = (size_t)env->GetArrayLength(stringJbytes);
	jbyte* pBytes = env->GetByteArrayElements(stringJbytes, NULL);

	std::string ret = std::string((char*)pBytes, length);
	env->ReleaseByteArrayElements(stringJbytes, pBytes, JNI_ABORT);

	env->DeleteLocalRef(stringJbytes);
	env->DeleteLocalRef(stringClass);
	return ret;
}

std::wstring GetLinkTarget(const std::wstring& a_Link) {
	// Define smart pointer type for automatic HANDLE cleanup.
	typedef std::unique_ptr<std::remove_pointer<HANDLE>::type,
		decltype(&::CloseHandle)> FileHandle;
	// Open file for querying only (no read/write access).
	FileHandle h(::CreateFileW(a_Link.c_str(), 0,
		FILE_SHARE_READ | FILE_SHARE_WRITE | FILE_SHARE_DELETE,
		nullptr, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, nullptr),
		&::CloseHandle);
	if (h.get() == INVALID_HANDLE_VALUE) {
		h.release();
		//throw std::runtime_error("CreateFileW() failed.");
		return std::wstring(a_Link);
	}

	try
	{
		if (isVistaOrNewer()) {
			const size_t requiredSize = ::GetFinalPathNameByHandleW(h.get(), nullptr, 0,
				FILE_NAME_NORMALIZED);
			if (requiredSize == 0) {
				//throw std::runtime_error("GetFinalPathNameByHandleW() failed.");
				return std::wstring(a_Link);
			}
			std::vector<wchar_t> buffer(requiredSize);
			::GetFinalPathNameByHandleW(h.get(), buffer.data(),
				static_cast<DWORD>(buffer.size()),
				FILE_NAME_NORMALIZED);

			std::wstring targetPath = std::wstring(buffer.begin(), buffer.end() - 1);
			if (targetPath.substr(0, 8).compare(L"\\\\?\\UNC\\") == 0)
			{
				// In case of a network path, replace "\\?\UNC\" with "\\".
				targetPath = L"\\" + targetPath.substr(7);
			}
			else if (targetPath.substr(0, 4).compare(L"\\\\?\\") == 0)
			{
				// In case of a local path, crop "\\?\".
				targetPath = targetPath.substr(4);
			}

			return targetPath;
		}

		LPWSTR lpPath = GetXpFilePath(h.get());
		if (lpPath == NULL) return std::wstring(a_Link); // TODO: Added by SAS~Storebror: If anything goes wrong in XP Symlink dereferencing, just return the original path.
		return std::wstring(lpPath);
	} catch (...) {
		return std::wstring(a_Link); // TODO: Added by SAS~Storebror: If anything goes wrong in dereferencing, just return the original path.
	}
}

JNIEXPORT jlong JNICALL Java_com_maddox_sas1946_il2_util_HighPrecisionTimer_queryPerformanceFrequency
(JNIEnv *env, jobject obj) {
	LARGE_INTEGER li;
	QueryPerformanceFrequency(&li);
	return li.QuadPart;

}

JNIEXPORT jlong JNICALL Java_com_maddox_sas1946_il2_util_HighPrecisionTimer_queryPerformanceCounter
(JNIEnv *env, jobject obj) {
	LARGE_INTEGER li;
	QueryPerformanceCounter(&li);
	return li.QuadPart;
}

JNIEXPORT jboolean JNICALL Java_com_maddox_sas1946_il2_util_FileTools_jniIsSymbolicLink
(JNIEnv* env, jobject jobj, jstring jstr) {
	if (!jstr) return JNI_FALSE;
	std::string sPath = js2s(env, jstr);
	if (::GetFileAttributesA(sPath.c_str()) & FILE_ATTRIBUTE_REPARSE_POINT) return JNI_TRUE;
	return JNI_FALSE;
}

JNIEXPORT jstring JNICALL Java_com_maddox_sas1946_il2_util_FileTools_jniResolveSymbolicLink
(JNIEnv* env, jobject jobj, jstring jstr) {
	std::wstring wPath = s2ws(js2s(env, jstr));
	std::wstring rPath = GetLinkTarget(wPath);
	jstring result = env->NewStringUTF(ws2s(rPath).c_str());
	return result;
}

JNIEXPORT jint JNICALL Java_com_maddox_sas1946_il2_util_CommonTools_jniGetTimeZoneBias
(JNIEnv* env, jobject jobj) {
	TIME_ZONE_INFORMATION timeZoneInformation;
	memset(&timeZoneInformation, 0, sizeof(TIME_ZONE_INFORMATION));
	DWORD dwTest = GetTimeZoneInformation(&timeZoneInformation);
	long offset = -timeZoneInformation.Bias;
	switch (dwTest) {
	case 1:
		offset -= timeZoneInformation.StandardBias;
		break;
	case 2:
		offset -= timeZoneInformation.DaylightBias;
		break;
	case 0:
	default:
		break;
	}
	return offset;
}

