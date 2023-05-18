#include "stdafx.h"
//#include <winternl.h>
//#include <limits.h>
//#include <assert.h> // TODO: Removed by SAS~Storebror. Don't _ever_ crash with assertion errors, keep going!
#include <tchar.h>
//#include <WinIoCtl.h>

#define _CRT_SECURE_NO_WARNINGS
#pragma warning( disable : 4996 )

// TODO: Changed SAS~Storebror: Replace static link to ntdll.dll by dynamically linking the file on demand.
//       Reason: There's many PCs out there which have this file removed/damaged, and we really only ever
//               need it on ancient XP machines, so there's no need to bother Vista/7/10 users with a missing
//               file dependency on ntdll.dll.
//#pragma comment(lib, "lib/ntdll.lib")

#define METHOD_BUFFERED                 0
#define FILE_ANY_ACCESS                 0
#define CTL_CODE( DeviceType, Function, Method, Access ) (                 \
    ((DeviceType) << 16) | ((Access) << 14) | ((Function) << 2) | (Method) \
)
#define USHRT_MAX     0xffff        // maximum unsigned short value
typedef __success(return >= 0) LONG NTSTATUS;

enum OBJECT_INFORMATION_CLASS { ObjectNameInformation = 1 };
enum FILE_INFORMATION_CLASS { FileNameInformation = 9 };
struct FILE_NAME_INFORMATION { ULONG FileNameLength; WCHAR FileName[1]; };
typedef struct _IO_STATUS_BLOCK { PVOID Dummy; ULONG_PTR Information; } IO_STATUS_BLOCK, * PIO_STATUS_BLOCK;
struct UNICODE_STRING { USHORT Length; USHORT MaximumLength; PWSTR Buffer; };
struct MOUNTMGR_TARGET_NAME { USHORT DeviceNameLength; WCHAR DeviceName[1]; };
struct MOUNTMGR_VOLUME_PATHS { ULONG MultiSzLength; WCHAR MultiSz[1]; };

//extern "C" NTSYSAPI NTSTATUS NTAPI NtQueryObject(IN HANDLE Handle OPTIONAL,
//	IN OBJECT_INFORMATION_CLASS ObjectInformationClass,
//	OUT PVOID ObjectInformation OPTIONAL, IN ULONG ObjectInformationLength,
//	OUT PULONG ReturnLength OPTIONAL);
//extern "C" NTSYSAPI NTSTATUS NTAPI NtQueryInformationFile(IN HANDLE FileHandle,
//	OUT PIO_STATUS_BLOCK IoStatusBlock, OUT PVOID FileInformation,
//	IN ULONG Length, IN FILE_INFORMATION_CLASS FileInformationClass);

typedef NTSTATUS(NTAPI* NTQUERYOBJECT)(IN HANDLE Handle OPTIONAL,
	IN OBJECT_INFORMATION_CLASS ObjectInformationClass,
	OUT PVOID ObjectInformation OPTIONAL, IN ULONG ObjectInformationLength,
	OUT PULONG ReturnLength OPTIONAL);

typedef NTSTATUS(NTAPI* NTQUERYINFORMATIONFILE)(IN HANDLE FileHandle,
	OUT PIO_STATUS_BLOCK IoStatusBlock, OUT PVOID FileInformation,
	IN ULONG Length, IN FILE_INFORMATION_CLASS FileInformationClass);

NTQUERYOBJECT NtQueryObject = NULL;
NTQUERYINFORMATIONFILE NtQueryInformationFile = NULL;

#define MOUNTMGRCONTROLTYPE ((ULONG) 'm')
#define IOCTL_MOUNTMGR_QUERY_DOS_VOLUME_PATH \
    CTL_CODE(MOUNTMGRCONTROLTYPE, 12, METHOD_BUFFERED, FILE_ANY_ACCESS)

union ANY_BUFFER {
	MOUNTMGR_TARGET_NAME TargetName;
	MOUNTMGR_VOLUME_PATHS TargetPaths;
	FILE_NAME_INFORMATION NameInfo;
	UNICODE_STRING UnicodeString;
	WCHAR Buffer[USHRT_MAX];
};

LPWSTR GetXpFilePath(HANDLE hFile)
{
	static ANY_BUFFER nameFull, nameRel, nameMnt;
	ULONG returnedLength; IO_STATUS_BLOCK iosb; NTSTATUS status;

	BOOL bNeedsFreeLibrary = FALSE;

	if (NtQueryObject == NULL || NtQueryInformationFile == NULL) {
		HMODULE ntdll = GetModuleHandle(_T("ntdll.dll"));
		if (ntdll == NULL || ntdll == INVALID_HANDLE_VALUE) {
			bNeedsFreeLibrary = TRUE;
			HMODULE ntdll = LoadLibrary(_T("ntdll.dll"));
			if (ntdll == NULL || ntdll == INVALID_HANDLE_VALUE) {
				return NULL;
			}
		}
		if (NtQueryObject == NULL) {
			NtQueryObject = (NTQUERYOBJECT)GetProcAddress(ntdll, "NtQueryObject");
			if (NtQueryObject == NULL || NtQueryObject == INVALID_HANDLE_VALUE) {
				return NULL;
			}
		}
		if (NtQueryInformationFile == NULL) {
			NtQueryInformationFile = (NTQUERYINFORMATIONFILE)GetProcAddress(ntdll, "NtQueryInformationFile");
			if (NtQueryInformationFile == NULL || NtQueryInformationFile == INVALID_HANDLE_VALUE) {
				return NULL;
			}
		}
		if (bNeedsFreeLibrary) FreeLibrary(ntdll);
	}

	status = NtQueryObject(hFile, ObjectNameInformation,
		nameFull.Buffer, sizeof(nameFull.Buffer), &returnedLength);
	if (status != 0) return NULL;
	//assert(status == 0);
	status = NtQueryInformationFile(hFile, &iosb, nameRel.Buffer,
		sizeof(nameRel.Buffer), FileNameInformation);
	if (status != 0) return NULL;
	//assert(status == 0);
	//I'm not sure how this works with network paths...
	if (nameFull.UnicodeString.Length < nameRel.NameInfo.FileNameLength) return NULL;
	//assert(nameFull.UnicodeString.Length >= nameRel.NameInfo.FileNameLength);
	nameMnt.TargetName.DeviceNameLength = (USHORT)(
		nameFull.UnicodeString.Length - nameRel.NameInfo.FileNameLength);
	wcsncpy(nameMnt.TargetName.DeviceName, nameFull.UnicodeString.Buffer,
		nameMnt.TargetName.DeviceNameLength / sizeof(WCHAR));
	HANDLE hMountPointMgr = CreateFile(_T("\\\\.\\MountPointManager"),
		0, FILE_SHARE_READ | FILE_SHARE_WRITE | FILE_SHARE_DELETE,
		NULL, OPEN_EXISTING, 0, NULL);
	__try
	{
		DWORD bytesReturned;
		BOOL success = DeviceIoControl(hMountPointMgr,
			IOCTL_MOUNTMGR_QUERY_DOS_VOLUME_PATH, &nameMnt,
			sizeof(nameMnt), &nameMnt, sizeof(nameMnt),
			&bytesReturned, NULL);
		if (!success || nameMnt.TargetPaths.MultiSzLength <= 0) return NULL;
		//assert(success && nameMnt.TargetPaths.MultiSzLength > 0);
		wcsncat(nameMnt.TargetPaths.MultiSz, nameRel.NameInfo.FileName,
			nameRel.NameInfo.FileNameLength / sizeof(WCHAR));
		return nameMnt.TargetPaths.MultiSz;
	}
	__finally { CloseHandle(hMountPointMgr); }
}
