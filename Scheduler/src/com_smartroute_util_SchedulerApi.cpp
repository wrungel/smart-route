
#include <boost/assert.hpp>
#include <string.h>

#include "DatabaseReader.h"
#include "mySqlHelper.h"

#include "com_smartroute_util_SchedulerApi.h"


using namespace Scheduler;

int ReadDB()
{
  try {
    CDatabaseConnectible connectible(
        /* _host = */"localhost",
        /* _user = */"LkwScheduler",
        /* _password = */"sched5",
        /* _database = */"LkwSchedulerDB");

     CDatabaseReader dbReader(connectible);

     dbReader.Open();

     CFramingData framingData;
     dbReader.DoReadFromDatabase(framingData);

     dbReader.Close();
  }
  catch (sql::SQLException& e)
  {
     PrintSqlException(e);

     return -1;
  }
  catch (...)
  {
     return -1;
  }
}

JNIEXPORT jstring JNICALL Java_com_smartroute_util_SchedulerApi_fnTryScheduleFavorable (JNIEnv *env, jobject obj, jintArray orderIDs, jint maxSuggestionsPerOrder)
{
  BOOST_ASSERT(maxSuggestionsPerOrder == 8);

  int retReadDB = ReadDB();

  jstring ret = env->NewStringUTF("<suggestions></suggestions>");
  return ret;
}

JNIEXPORT jstring JNICALL Java_com_smartroute_util_SchedulerApi_fnBestAvailable (JNIEnv *env, jobject obj, jintArray orderIDs, jint maxSuggestionsPerOrder)
{
  int retReadDB = ReadDB();

  jstring ret = env->NewStringUTF("<suggestions></suggestions>");
  return ret;
}

JNIEXPORT jint JNICALL Java_com_smartroute_util_SchedulerApi_fnEstimatePrice(JNIEnv *env, jobject obj, jstring orderXML)
{
  return 100;
}