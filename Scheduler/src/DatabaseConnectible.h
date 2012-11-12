#ifndef SCHEDULER_DATABASE_CONNECTIBLE_H
#define SCHEDULER_DATABASE_CONNECTIBLE_H

#include <mysql_connection.h>

#include <cppconn/driver.h>
#include <cppconn/exception.h>
#include <cppconn/resultset.h>
#include <cppconn/statement.h>
#include <cppconn/prepared_statement.h>

namespace Scheduler
{
  class CDatabaseConnectible
  {
    public:
      CDatabaseConnectible(const std::string& aHost,
                           const std::string& aUser,
                           const std::string& aPassword,
                           const std::string& aDatabase)
      : _host(aHost),
        _user(aUser),
        _password(aPassword),
        _database(aDatabase),
        _driver(NULL),
        _connection(NULL)
      {}

      CDatabaseConnectible()
      : _host(""),
        _user(""),
        _password(""),
        _database(""),
        _driver(NULL),
        _connection(NULL)
      {}

      virtual ~CDatabaseConnectible()
      {
        delete _connection;
      }

      void Open()
      {
        _driver = get_driver_instance();
        _connection = _driver->connect(_host, _user, _password);

        std::auto_ptr<sql::Statement> useStmt(_connection->createStatement());
        useStmt->execute("USE "+ _database + " ;");
      }

      void Close() { delete _connection; _connection = NULL;}
      bool IsOpen() const { return _connection != NULL; }

    public: // getter
      sql::Connection* Connection() const { return _connection; };

      const std::string& Host() const { return _host; }
      const std::string& User() const { return _user; }
      const std::string& Password() const { return _password; }
      const std::string& Database() const { return _database; }

    protected:
      std::string _host;
      std::string _user;
      std::string _password;
      std::string _database;

      sql::Driver* _driver;
      sql::Connection* _connection;
  };
}

#endif // SCHEDULER_DATABASE_CONNECTIBLE_H