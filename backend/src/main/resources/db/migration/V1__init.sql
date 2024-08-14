CREATE TABLE answer
(
    answer_id    BIGINT NOT NULL AUTO_INCREMENT,
    applicant_id BIGINT,
    question_id  BIGINT,
    content      VARCHAR(255),
    PRIMARY KEY (answer_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE applicant
(
    applicant_id BIGINT NOT NULL AUTO_INCREMENT,
    created_date DATETIME(6),
    process_id   BIGINT,
    updated_date DATETIME(6),
    email        VARCHAR(255),
    name         VARCHAR(255),
    phone        VARCHAR(255),
    state        VARCHAR(255),
    PRIMARY KEY (applicant_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE apply_form
(
    apply_form_id BIGINT NOT NULL AUTO_INCREMENT,
    created_date  DATETIME(6),
    dashboard_id  BIGINT UNIQUE,
    end_date      DATETIME(6),
    start_date    DATETIME(6),
    updated_date  DATETIME(6),
    description   TEXT,
    title         VARCHAR(255),
    url           VARCHAR(255),
    PRIMARY KEY (apply_form_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE choice
(
    choice_id   BIGINT NOT NULL AUTO_INCREMENT,
    sequence    INTEGER,
    question_id BIGINT,
    content     VARCHAR(255),
    PRIMARY KEY (choice_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE club
(
    club_id   BIGINT NOT NULL AUTO_INCREMENT,
    member_id BIGINT,
    name      VARCHAR(255),
    PRIMARY KEY (club_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE dashboard
(
    dashboard_id BIGINT NOT NULL AUTO_INCREMENT,
    club_id      BIGINT,
    PRIMARY KEY (dashboard_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE evaluation
(
    evaluation_id BIGINT NOT NULL AUTO_INCREMENT,
    score         INTEGER,
    applicant_id  BIGINT,
    created_date  DATETIME(6),
    process_id    BIGINT,
    updated_date  DATETIME(6),
    content       VARCHAR(255),
    PRIMARY KEY (evaluation_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE member
(
    member_id    BIGINT NOT NULL AUTO_INCREMENT,
    created_date DATETIME(6),
    updated_date DATETIME(6),
    email        VARCHAR(255) UNIQUE,
    password     VARCHAR(255),
    phone        VARCHAR(255),
    role         VARCHAR(255),
    PRIMARY KEY (member_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE process
(
    process_id   BIGINT NOT NULL AUTO_INCREMENT,
    sequence     INTEGER,
    dashboard_id BIGINT,
    description  VARCHAR(255),
    name         VARCHAR(255),
    PRIMARY KEY (process_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE question
(
    question_id   BIGINT NOT NULL AUTO_INCREMENT,
    required      BOOLEAN,
    sequence      INTEGER,
    apply_form_id BIGINT,
    content       VARCHAR(255),
    description   VARCHAR(255),
    question_type VARCHAR(255),
    PRIMARY KEY (question_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
