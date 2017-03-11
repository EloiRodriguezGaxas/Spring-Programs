CREATE TABLE note (
  title VARCHAR (255) ,
  content VARCHAR (255),
  date_creation TIMESTAMP ,
  date_edit TIMESTAMP ,
  owner VARCHAR (55),
  PRIMARY KEY (title, owner)
);

