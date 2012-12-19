UNAME := $(shell uname)

ifeq ($(UNAME), Linux)
RM = rm -f
FixPath = $1
JNI_API_DIR = ../lib/linux/target/native/javah
MAKEOUTDIR = makeoutdirLinux
CP = cp
JAVAJNIINCLUDE = $(JAVA_HOME)/include/linux
else
RM = DEL /Q
FixPath = $(subst /,\,$1)
JNI_API_DIR = ../lib/windows/target/native/javah
MAKEOUTDIR = makeoutdirWin
CP = copy
JAVAJNIINCLUDE = $(JAVA_HOME)/include/windows
endif

CFLAGS = -g -Wall -W -ansi

DIR_TYNIXML          = ../3rdParty/tinyxml
LIB_TYNIXML          = ../3rdParty/tinyxml/libtinyxml.a

DIR_UNIT_TEST_PP     = ../3rdParty/UnitTest++
INC_UNIT_TEST_PP     = ${DIR_UNIT_TEST_PP}/src
LIB_UNIT_TEST_PP     = ${DIR_UNIT_TEST_PP}/libUnitTest++.a

OUT_DIR = ../out

OBJ = loadAmmount.o situation.o mySqlHelper.o DatabaseReader.o xmlHelper.o HelperFunctions.o
OBJ_TEST = Test/unit_tests.o Test/TestDBConnection.o Test/TestContractReader.o

INCLUDES = -I${INC_UNIT_TEST_PP} -I${DIR_TYNIXML}

all: schedulerlib scheduler test

test:  $(OBJ) $(OBJ_TEST) ${LIB_UNIT_TEST_PP} $(MAKEOUTDIR) $(LIB_TYNIXML)
	g++ $(CFLAGS) -o $(OUT_DIR)/unit_tests $(OBJ) $(OBJ_TEST) $(LIB_UNIT_TEST_PP) $(LIB_TYNIXML) -lmysqlcppconn -lboost_date_time
	$(CP) $(call FixPath, ../../Database/create_tables.sql) $(call FixPath,$(OUT_DIR)/create_tables.sql)
	$(CP) $(call FixPath, Test/*.sql) $(call FixPath, $(OUT_DIR))

schedulerlib: $(OBJ) com_smartroute_util_SchedulerApi.o $(MAKEOUTDIR) $(LIB_TYNIXML)
	g++ $(CFLAGS) -shared -o $(OUT_DIR)/libScheduler.so $(OBJ) com_smartroute_util_SchedulerApi.o $(LIB_TYNIXML) -lmysqlcppconn -lboost_date_time

scheduler: main.o schedulerlib $(OUT_DIR)/libScheduler.so $(MAKEOUTDIR)
	g++ $(CFLAGS) -o $(OUT_DIR)/Scheduler main.o $(OBJ) -L../out -lScheduler -lmysqlcppconn -lboost_date_time

com_smartroute_util_SchedulerApi.o: com_smartroute_util_SchedulerApi.cpp $(JNI_API_DIR)/com_smartroute_util_SchedulerApi.h
	g++ -fPIC -c $(CFLAGS) -I$(JNI_API_DIR) -I$(JAVA_HOME)/include -I$(JAVAJNIINCLUDE) -o com_smartroute_util_SchedulerApi.o com_smartroute_util_SchedulerApi.cpp

main.o: main.cpp
	g++ -fPIC -c $(CFLAGS) -o $@ $<

Test/%.o: Test/%.cpp
	g++ -fPIC -c $(CFLAGS) ${INCLUDES} -o $@ $<

%.o: %.cpp %.h
	g++ -fPIC -c $(CFLAGS) ${INCLUDES} -o $@ $<

makeoutdirLinux:
	if [ ! -d "$(OUT_DIR)" ] ; then \
	mkdir $(OUT_DIR); \
	fi \

makeoutdirWin:
	IF NOT EXIST $(call FixPath, $(OUT_DIR)) md $(call FixPath, $(OUT_DIR))

clean:
	$(RM) $(call FixPath, Test/*.o)
	$(RM) $(call FixPath, ./*.o)
	$(RM) $(call FixPath, $(OUT_DIR)/*)
	$(RM) $(call FixPath, $(DIR_TYNIXML)/*.o)
	$(RM) $(call FixPath, $(DIR_TYNIXML)/*.a)

$(LIB_TYNIXML):
	cd $(DIR_TYNIXML); make; ar rvs libtinyxml.a tinyxml.o tinyxmlparser.o xmltest.o tinyxmlerror.o tinystr.o

