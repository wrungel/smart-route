CFLAGS = -g -Wall -W -ansi

DIR_UNIT_TEST_PP     = ../3rdParty/UnitTest++
INC_UNIT_TEST_PP     = ${DIR_UNIT_TEST_PP}/src
LIB_UNIT_TEST_PP     = ${DIR_UNIT_TEST_PP}/libUnitTest++.a

OUT_DIR = ../out
JNI_API_DIR = ../lib/linux/target/native/javah

OBJ = loadAmmount.o situation.o mySqlHelper.o DatabaseReader.o
OBJ_TEST = Test/unit_tests.o Test/TestDBConnection.o

all: schedulerlib scheduler test

test:  $(OBJ) $(OBJ_TEST) ${LIB_UNIT_TEST_PP} makeoutdir
	g++ $(CFLAGS) -o $(OUT_DIR)/unit_tests -lmysqlcppconn -lboost_date_time $(OBJ) $(OBJ_TEST) ${LIB_UNIT_TEST_PP}
	cp ../../Database/create_tables.sql $(OUT_DIR)/create_tables.sql
	cp Test/*.sql $(OUT_DIR)

schedulerlib: $(OBJ) com_smartroute_util_SchedulerApi.o makeoutdir
	g++ $(CFLAGS) -shared -o $(OUT_DIR)/libScheduler.so -lmysqlcppconn -lboost_date_time $(OBJ) com_smartroute_util_SchedulerApi.o

scheduler: main.o $(OUT_DIR)/libScheduler.so makeoutdir
	g++ $(CFLAGS) -o $(OUT_DIR)/Scheduler -lmysqlcppconn -lboost_date_time main.o $(OBJ) -L../out -lScheduler

com_smartroute_util_SchedulerApi.o:
	g++ -fPIC -c $(CFLAGS) -I$(JNI_API_DIR) -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux -o com_smartroute_util_SchedulerApi.o com_smartroute_util_SchedulerApi.cpp

%.o: %.cpp
	g++ -fPIC -c $(CFLAGS) -I${INC_UNIT_TEST_PP} -o $@ $<

makeoutdir:
	if [ ! -d "$(OUT_DIR)" ] ; then \
	mkdir $(OUT_DIR); \
	fi \

clean:
	rm -rf Test/*.o
	rm -rf ./*.o
	rm -rf $(OUT_DIR)/*



