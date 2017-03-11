CREATE TABLE user_lab
(
  username VARCHAR (55) PRIMARY KEY,
  name VARCHAR (55),
  second_name VARCHAR (55),
  email VARCHAR (100),
  token VARCHAR (100),
  tokenType VARCHAR (100)
);

CREATE TABLE note_lab (
  id bigint auto_increment PRIMARY KEY,
  title VARCHAR (255) ,
  content VARCHAR (255),
  date_creation TIMESTAMP ,
  date_edit TIMESTAMP ,
  owner VARCHAR (55),
  FOREIGN KEY (owner)
  REFERENCES user_lab(username) ON DELETE CASCADE
);

CREATE  TABLE users (
  username VARCHAR(45) NOT NULL ,
  password VARCHAR(70) NOT NULL ,
  token VARCHAR(80) ,
  enabled TINYINT NOT NULL DEFAULT 1 ,
  PRIMARY KEY (username));

CREATE TABLE user_roles (
  user_role_id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(45) NOT NULL,
  role varchar(45) NOT NULL,
  PRIMARY KEY (user_role_id),
  UNIQUE KEY uni_username_role (role,username),
  CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES users (username));
