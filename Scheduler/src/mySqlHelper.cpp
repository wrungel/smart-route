#include "mySqlHelper.h"

#include <sstream>

#include "HelperFunctions.h"

namespace Scheduler{

void PrintSqlException(const sql::SQLException &e)
{
    /*
      The MySQL Connector/C++ throws three different exceptions:

      - sql::MethodNotImplementedException (derived from sql::SQLException)
      - sql::InvalidArgumentException (derived from sql::SQLException)
      - sql::SQLException (derived from std::runtime_error)
    */

    std::cout << "# ERR: SQLException in " << __FILE__;
    std::cout << "(" << __FUNCTION__ << ") on line " << __LINE__ << std::endl;
    /* Use what() (derived from std::runtime_error) to fetch the error message */
    std::cout << "# ERR: " << e.what();
    std::cout << " (MySQL error code: " << e.getErrorCode();
    std::cout << ", SQLState: " << e.getSQLState() << " )" << std::endl;
}

int getLastInsertId(sql::Connection* conn)
{
  std::auto_ptr<sql::Statement> stmt(conn->createStatement());
  std::auto_ptr<sql::ResultSet> rs(stmt->executeQuery("SELECT LAST_INSERT_ID();"));

  rs->next();
  return rs->getInt(1);
}

//! reads  the number with passed number of signs after comma and returns it as int (multiplied wit 10^numAfterComma)
int getDecimal(const sql::ResultSet& rs, const std::string& column, unsigned char numAfterComma)
{
  std::string s = rs.getString(column);
  return ParseDecimal(s, numAfterComma);
}

//! divides arg by 10^power and sets it as Decimal into the prepared statement
void setDecimal(sql::PreparedStatement& stmt, int parameterIndex, int arg, unsigned char power)
{
  bool positiv = (arg >= 0);
  int p = 1;
  for (unsigned char i = 0; i < power; ++i) { p *= 10; }

  int beforeComma;
  int afterComma;
  if (positiv)
  {
    beforeComma = arg / p;
    afterComma = arg - p * beforeComma;
  }
  else
  {
    int posArg = arg;
    beforeComma = - (posArg / p);
    afterComma = posArg - p * (-beforeComma);
  }

  std::stringstream strm;
  strm << beforeComma << '.' << afterComma;
  stmt.setString(parameterIndex, strm.str());
}

}