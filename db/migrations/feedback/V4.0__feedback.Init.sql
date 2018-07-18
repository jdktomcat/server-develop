CREATE TABLE t_feedback (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  contact VARCHAR(100),
  content VARCHAR(2000),
  create_time TIMESTAMP DEFAULT  current_timestamp()
);