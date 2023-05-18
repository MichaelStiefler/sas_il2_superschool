#ifdef SASDLL_EXPORTS
#define SASDLL_API __declspec(dllexport)
#else
#define SASDLL_API __declspec(dllimport)
#endif

#include "jni\jni.h"

JNIEXPORT jlong JNICALL Java_com_maddox_sas1946_il2_util_HighPrecisionTimer_queryPerformanceFrequency(JNIEnv *, jobject);
JNIEXPORT jlong JNICALL Java_com_maddox_sas1946_il2_util_HighPrecisionTimer_queryPerformanceCounter(JNIEnv *, jobject);
JNIEXPORT jboolean JNICALL Java_com_maddox_sas1946_il2_util_FileTools_jniIsSymbolicLink(JNIEnv* env, jobject jobj, jstring jstr);
JNIEXPORT jstring JNICALL Java_com_maddox_sas1946_il2_util_FileTools_jniResolveSymbolicLink(JNIEnv* env, jobject jobj, jstring jstr);
JNIEXPORT jint JNICALL Java_com_maddox_sas1946_il2_util_CommonTools_jniGetTimeZoneBias(JNIEnv* env, jobject jobj);