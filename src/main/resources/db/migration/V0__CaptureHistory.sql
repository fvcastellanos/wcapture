DROP TABLE IF EXISTS capture_history;

CREATE TABLE IF NOT EXISTS capture_history
(
    id            INT          NOT NULL AUTO_INCREMENT,
    created_date  TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP,
    modified_date TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    url           VARCHAR(255) NOT NULL,
    filename      VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci;
;
