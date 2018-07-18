/*
 * Create database
 */
/*
CREATE DATABASE base_dev DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_bin;
 */

DELIMITER //
CREATE FUNCTION _pm_base(code INT)
  RETURNS INT
  BEGIN
    RETURN code * 1000;
  END//
DELIMITER ;


