CREATE TABLE t_contact_info (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100),
  phone VARCHAR(300),
  uuid VARCHAR(300) UNIQUE ,
  create_time TIMESTAMP DEFAULT  current_timestamp(),
  modify_time TIMESTAMP DEFAULT  current_timestamp()
);


CREATE TABLE t_prize_winner_list (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  published INT(3),
  create_time TIMESTAMP DEFAULT  current_timestamp(),
  modify_time TIMESTAMP DEFAULT  current_timestamp()
);


CREATE TABLE t_prize_winner_list_detail (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  list_id BIGINT,
  user_id BIGINT,
  create_time TIMESTAMP DEFAULT  current_timestamp(),
  modify_time TIMESTAMP DEFAULT  current_timestamp()
);