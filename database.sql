CREATE DATABASE test1;
CREATE DATABASE test2;
CREATE DATABASE test3;

use test1;
CREATE TABLE user(
  id INT(11) PRIMARY KEY ,
  name VARCHAR(50) NOT NULL,
  sex BIT,
  age INT(11)
);

use test2;
CREATE TABLE user(
  id INT(11) PRIMARY KEY ,
  name VARCHAR(50) NOT NULL,
  sex BIT,
  age INT(11)
);

use test3;
CREATE TABLE user(
  id INT(11) PRIMARY KEY ,
  name VARCHAR(50) NOT NULL,
  sex BIT,
  age INT(11)
);
