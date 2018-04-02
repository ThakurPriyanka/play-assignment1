# --- !Ups
create table userInfo(id int NOT NULL AUTO_INCREMENT primary key,first_name varchar(100), middle_name varchar(100), last_name varchar(100), email varchar(100) unique, pwd varchar(1000),mobile_number varchar(10), gender varchar(10), age int, hobbies varchar(50), isAdmin boolean default false, isEnable boolean default  true);


create table assignmentDetail(id int NOT NULL AUTO_INCREMENT primary key, title varchar(200), description varchar(1000));

# --- !Downs

drop table userInfo;
drop table assignmentDetail;