#include "com_smartroute_util_SchedulerApi.h"
#include <string.h>

JNIEXPORT jstring JNICALL Java_com_smartroute_util_SchedulerApi_fnTryScheduleFavorable (JNIEnv *env, jobject obj, jintArray orderIDs, jint maxSuggestionsPerOrder)
{
	jstring ret = (*env)->NewStringUTF(env, "<suggestions></suggestions>");
  return ret;
}

JNIEXPORT jstring JNICALL Java_com_smartroute_util_SchedulerApi_fnBestAvailable (JNIEnv *env, jobject obj, jintArray orderIDs, jint maxSuggestionsPerOrder)
{
  jstring ret = (*env)->NewStringUTF(env, "<suggestions></suggestions>");
  return ret;
}

JNIEXPORT jint JNICALL Java_com_smartroute_util_SchedulerApi_fnEstimatePrice(JNIEnv *env, jobject obj, jstring orderXML)
{
  return 100;
}