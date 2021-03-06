CREATE DATABASE IF NOT EXISTS `LkwSchedulerDB` ;
CREATE USER 'LkwScheduler'@'localhost' IDENTIFIED BY 'sched5';
GRANT USAGE ON * . * TO 'LkwScheduler'@'localhost' IDENTIFIED BY 'sched5' WITH MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0 ;
GRANT ALL PRIVILEGES ON `LkwSchedulerDB` . * TO 'LkwScheduler'@'localhost';

CREATE DATABASE IF NOT EXISTS `LkwSchedulerDBTest` ;
CREATE USER 'TestScheduler'@'localhost' IDENTIFIED BY 'TestScheduler5';
GRANT USAGE ON * . * TO 'TestScheduler'@'localhost' IDENTIFIED BY 'TestScheduler5' WITH MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0 ;
GRANT ALL PRIVILEGES ON `LkwSchedulerDBTest` . * TO 'TestScheduler'@'localhost';
