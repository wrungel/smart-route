-- You can use this file to load seed data into the database using SQL statements
insert into Account(id, login, passwd, name, email) values (0, 'sanek', 'sanek123', 'Alexander Haritonov', 'alexander.haritonov@googlemail.com')
insert into Account(id, login, passwd, name, email) values (1, 'et', 'et123', 'Yevgeniy Turok', 'eturok@googlemail.com')
insert into Account(id, login, passwd, name, email) values (2, 'frol', 'frol123', 'Frolov Maxim', 'frolovmx@gmail.com')

insert into Customer(id, address, phone, account_id) values (3, 'Berlin', '4915785864627', 0)
insert into Customer(id, address, phone, account_id) values (4, 'Düsseldorf', '4917648678943', 1)
insert into Customer(id, address, phone, account_id) values (5, 'Aachen', '4915209877419', 2)

