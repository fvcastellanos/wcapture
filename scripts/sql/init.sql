USE mysql;

-- -----------------------------------------------------
-- Schema wcapture
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS wcapture;

CREATE SCHEMA IF NOT EXISTS wcapture
  DEFAULT CHARACTER SET utf8mb4;

-- -----------------------------------------------------
-- Users
-- -----------------------------------------------------
DROP USER IF EXISTS 'wcapture'@'localhost';
DROP USER IF EXISTS 'wcapture'@'%';

CREATE USER IF NOT EXISTS 'wcapture'@'localhost'
  IDENTIFIED BY 'Wc@ptur3$01@';

CREATE USER IF NOT EXISTS 'wcapture'@'%'
  IDENTIFIED BY 'Wc@ptur3$01@';

GRANT ALL PRIVILEGES ON wcapture.* TO 'wcapture'@'localhost'
IDENTIFIED BY 'Wc@ptur3$01@';

GRANT ALL PRIVILEGES ON wcapture.* TO 'wcapture'@'%'
IDENTIFIED BY 'Wc@ptur3$01@';

FLUSH PRIVILEGES;
