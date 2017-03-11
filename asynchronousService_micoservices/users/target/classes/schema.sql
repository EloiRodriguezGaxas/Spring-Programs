CREATE TABLE users
(
  username VARCHAR (55) PRIMARY KEY,
  name VARCHAR (55) NOT NULL ,
  second_name VARCHAR (55) NOT NULL ,
  email VARCHAR (100),
  password VARCHAR (150),
  token VARCHAR (40)
);