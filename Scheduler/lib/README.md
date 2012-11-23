Scheduler dll
===========

This is mavenized c++ Scheuler.

It builds Scheduler dll (currently only on Windows).

Prerequisites
-------------------
### Libraries
* install boost date_time library
* install MySQL c++ Connector

#### Fix comile time errors on Windows 7 64 bit in MySQL C++ connector
To fix compilation error which occurs under Windows 7 64 bit, fix
`${mysql.connector.cpp.include}\cppconn\config.h`:

* comment out lines defining types, which are alrteady defined in `stdint.h`.

#### Configure Maven
Add to users' maven `settings.xml` (`/home/usr/.m2/settings.xml` or `C:\Users\user\.m2\settings.xml`) the following properties (replace paths like `D:\\..` with your own):

    <settngs> 
        ...
    <profiles>
        <profile>
            <id>native</id>
            <properties>
                <boost.include>D:\bin\boost\1.52.0\src</boost.include>
                <boost.lib>D:\bin\boost\1.52.0\src\stage\lib</boost.lib>
                <mysql.connector.cpp.include>D:\bin\mysql-connector-c++-noinstall-1.1.1-win32\include</mysql.connector.cpp.include>
                <mysql.connector.cpp.lib>D:\bin\mysql-connector-c++-noinstall-1.1.1-win32\lib</mysql.connector.cpp.lib>
            </properties>
        </profile>
    </profiles>
    <activeProfiles>
        <activeProfile>native</activeProfile>
    </activeProfiles>
        ...
    </settngs>

 
## TODO
* remove os-depoendent suffix from boost library references in pom.xml
 
 