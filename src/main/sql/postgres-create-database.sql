-- this was simply taken from spring log so Hibernate will be alright with table
-- im not sure what will happen with OneToMany relations as there are no constraints
-- we'll see next time
-- maybe set ddl-auto=update in application.properties

create table place (
  id  bigserial not null,
  address varchar(255),
  description varchar(2000),
  name varchar(255),
  primary key (id)
);

create table tusovka (
  id  bigserial not null,
  date timestamp,
  description varchar(2000),
  link varchar(255),
  name varchar(255),
  place varchar(255),
  price int4 not null,
  primary key (id)
);

-- here are some test insertions

INSERT INTO place (name, address, description, latitude, longitude) VALUES ('Стёпик', 'Москва, ул. Пушкина, д. 2', 'Просто хорошее место для отдыха.', 37.6480051, 37.6480051);
INSERT INTO place (name, address, description, latitude, longitude) VALUES ('Powerhouse', 'Москва, около Таганской', 'Здесь играет музыка.', 54.7177024, 37.6480051);
INSERT INTO place (name, address, description, latitude, longitude) VALUES ('test_place1', 'Берлин', 'Берлин', 52.524063, 13.361528);
INSERT INTO place (name, address, description, latitude, longitude) VALUES ('test_place2', 'МГТУ им. Н.Э.Баумана, 2-я Бауманская улица, Москва', 'Бомонка', 55.765900, 37.685198);
INSERT INTO place (name, address, description, latitude, longitude) VALUES ('test_place3', 'Олимпийский проспект, 13А, Мытищи', 'Мытищи ебаные', 55.918597,37.763928);

insert INTO tusovka (name, description, date, price, place) VALUES ('asd', 'adad', '2005-08-09T18:31:42', 100, 'Powerhouse');

select * from place;
select * from tusovka;
select * from message_log;