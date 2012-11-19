-- You can use this file to load seed data into the database using SQL statements
insert into Account(id, login, passwd) values (0, 'sanek', 'sanek123')
insert into Account(id, login, passwd) values (1, 'et', 'sanek123')
insert into Account(id, login, passwd) values (2, 'frol', 'frol123')

insert into Customer(id, name, email, phone, account_id) values (0, 'Alexander Haritonov', 'alexander.haritonov@googlemail.com', '4915785864627', 0)
insert into Customer(id, name, email, phone, account_id) values (1, 'Yevgeniy Turok', 'eturok@googlemail.com', '4917648678943', 1)
insert into Customer(id, name, email, phone, account_id) values (2, 'Maxim Frolov', 'frolovmx@googlemail.com', '4915209877419', 2)

