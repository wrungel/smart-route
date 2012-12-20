
#include <boost/assert.hpp>
#include <string.h>

#include "DatabaseReader.h"
#include "mySqlHelper.h"
#include "xmlHelper.h"

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

     return 1;
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

// ! conversion jstring ->std::string for UTF
void GetJStringContent(JNIEnv *env, jstring javastr, std::string &resultStr)
{
  if (!javastr) {
    resultStr.clear();
    return;
  }

  const char *s = env->GetStringUTFChars(javastr, 0);
  resultStr=s;
  env->ReleaseStringUTFChars(javastr, s);
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
  try
  {
    std::string orderXmlStr;
    GetJStringContent(env, orderXML, orderXmlStr);

    CXmlHelper xmlHelper;
    std::auto_ptr<CContract> contract = xmlHelper.ReadContract(orderXmlStr);

    int retReadDB = ReadDB();
    return retReadDB;
  }
  catch(...)
  {
    return -1;
  }
}