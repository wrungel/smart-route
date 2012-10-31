#ifndef SCHEDULER_MYSQLHELPER_H
#define SCHEDULER_MYSQLHELPER_H

#include <string>
#include <stdlib.h>
#include <iostream>
#include <stdexcept>

#include <mysql_connection.h>

#include <cppconn/driver.h>
#include <cppconn/exception.h>
#include <cppconn/resultset.h>
#include <cppconn/statement.h>
#include <cppconn/prepared_statement.h>

namespace Scheduler
{
  void PrintSqlException(const sql::SQLException &e);

  //!finds out the id of last row, which was inserted by passed connection
  int getLastInsertId(sql::Connection* conn);

  //! reads  the number with passed number of signs after comma and returns it as int (multiplied wit 10^numAfterComma)
  int getDecimal(const sql::ResultSet& rs, const std::string& column, unsigned char numAfterComma);

  //! divides arg by 10^power and sets it as Decimal into the prepared statement
  void setDecimal(sql::PreparedStatement& stmt, int parameterIndex, int arg, unsigned char power);
}

#endif // SCHEDULER_MYSQLHELPER_H