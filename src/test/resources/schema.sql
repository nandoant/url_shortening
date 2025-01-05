CREATE TABLE IF NOT EXISTS url (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    long_url CLOB NOT NULL,
    short_url VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP,
    expiration_date TIMESTAMP
);