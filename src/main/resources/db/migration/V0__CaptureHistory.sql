-- -----------------------------------------------------
-- Table `wcapture`.`captureHistory`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wcapture`.`capture_history`;

CREATE TABLE IF NOT EXISTS `wcapture`.`capture_history` (
  `id`            INT          NOT NULL AUTO_INCREMENT,
  `created_date`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_date` TIMESTAMP    NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `url`           VARCHAR(255) NOT NULL,
  `filename`      VARCHAR(255) NOT NULL,
#   `storage_path`  VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
);
