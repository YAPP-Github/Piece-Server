DROP TABLE IF EXISTS match_info;
DROP TABLE IF EXISTS user_table;
DROP TABLE IF EXISTS profile;

CREATE TABLE match_info
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_1              BIGINT      NOT NULL,
    user_2              BIGINT      NOT NULL,
    user_1_match_status VARCHAR(20) NOT NULL,
    user_2_match_status VARCHAR(20) NOT NULL,
    created_at          TIMESTAMP   NOT NULL,
    updated_at          TIMESTAMP
);

CREATE TABLE user_table
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    phone      VARCHAR(20),
    role       VARCHAR(255),
    profile_id LONG,
    created_at TIMESTAMP NOT NULL
);
