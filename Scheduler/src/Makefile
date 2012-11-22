CFLAGS = -g -Wall -W -ansi

DIR_UNIT_TEST_PP     = ../3rdParty/UnitTest++
INC_UNIT_TEST_PP     = ${DIR_UNIT_TEST_PP}/src
LIB_UNIT_TEST_PP     = ${DIR_UNIT_TEST_PP}/libUnitTest++.a

OUT_DIR = ../out
OBJ = loadAmmount.o situation.o mySqlHelper.o DatabaseReader.o
OBJ_TEST = Test/unit_tests.o Test/TestDBConnection.o

all: schedulerlib scheduler test

test:  $(OBJ) $(OBJ_TEST) ${LIB_UNIT_TEST_PP}
	g++ $(CFLAGS) -o $(OUT_DIR)/unit_tests -lmysqlcppconn -lboost_date_time $(OBJ) $(OBJ_TEST) ${LIB_UNIT_TEST_PP}

schedulerlib: $(OBJ)
	g++ $(CFLAGS) -shared -o $(OUT_DIR)/libScheduler.so -lmysqlcppconn -lboost_date_time $(OBJ)

scheduler: main.o $(OUT_DIR)/libScheduler.so
	g++ $(CFLAGS) -o $(OUT_DIR)/Scheduler -lmysqlcppconn -lboost_date_time main.o $(OBJ) -L../out -lScheduler

%.o: %.cpp
	g++ -fPIC -c $(CFLAGS) -I${INC_UNIT_TEST_PP} -o $@ $<

clean:
	rm -rf Test/*.o
	rm -rf ./*.o
	rm -rf $(OUT_DIR)/*



