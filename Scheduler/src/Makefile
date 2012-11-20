CFLAGS = -g -Wall -W -ansi

DIR_UNIT_TEST_PP     = ../3rdParty/UnitTest++
INC_UNIT_TEST_PP     = ${DIR_UNIT_TEST_PP}/src
LIB_UNIT_TEST_PP     = ${DIR_UNIT_TEST_PP}/libUnitTest++.a

OBJ = loadAmmount.o situation.o mySqlHelper.o DatabaseReader.o
OBJ_TEST = Test/unit_tests.o Test/TestDBConnection.o

all: schedulerlib scheduler test

test:  $(OBJ) $(OBJ_TEST) ${LIB_UNIT_TEST_PP}
	g++ $(CFLAGS) -o unit_tests -lmysqlcppconn -lboost_date_time $(OBJ) $(OBJ_TEST) ${LIB_UNIT_TEST_PP}

schedulerlib: $(OBJ)
        g++ $(CFLAGS) -o libScheduler.so -lmysqlcppconn -lboost_date_time -shared \
        -I/usr/local/jdk1.6.0_03/include \
        -I/usr/local/jdk1.6.0_03/include/linux com_marakana_jniexamples_Hello.c

scheduler: main.o
	g++ $(CFLAGS) -o Scheduler -lmysqlcppconn -lboost_date_time main.o $(OBJ)

%.o: %.cpp
	g++ -c $(CFLAGS) -I${INC_UNIT_TEST_PP} -o $@ $<

clean:
	rm -rf Test/*.o
	rm -rf ./*.o
	rm -rf ./unit_tests


