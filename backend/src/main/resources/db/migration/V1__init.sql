CREATE TABLE app_user (
                          id BIGSERIAL PRIMARY KEY,
                          first_name VARCHAR(255),
                          last_name VARCHAR(255),
                          email VARCHAR(255) UNIQUE NOT NULL,
                          password VARCHAR(255),
                          role VARCHAR(20)
);

CREATE TABLE subject (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(255),
                         exam_date DATE,
                         difficulty INTEGER,
                         user_id BIGINT REFERENCES app_user(id)
);

CREATE TABLE topic (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(255),
                       estimated_hours INTEGER,
                       completed BOOLEAN,
                       subject_id BIGINT REFERENCES subject(id)
);

CREATE TABLE study_session (
                               id BIGSERIAL PRIMARY KEY,
                               planned_duration_minutes INTEGER,
                               actual_duration_minutes INTEGER,
                               study_interval_minutes INTEGER,
                               break_interval_minutes INTEGER,
                               cycles_completed INTEGER,
                               status VARCHAR(20),
                               started_at TIMESTAMP,
                               ended_at TIMESTAMP,
                               topic_id BIGINT REFERENCES topic(id)
);

CREATE TABLE quiz (
                      id BIGSERIAL PRIMARY KEY,
                      generated_at TIMESTAMP,
                      score INTEGER,
                      topic_id BIGINT REFERENCES topic(id)
);

CREATE TABLE quiz_question (
                               id BIGSERIAL PRIMARY KEY,
                               question_text VARCHAR(1000),
                               correct_answer VARCHAR(255),
                               option_a VARCHAR(255),
                               option_b VARCHAR(255),
                               option_c VARCHAR(255),
                               option_d VARCHAR(255),
                               quiz_id BIGINT REFERENCES quiz(id)
);

CREATE TABLE flashcard (
                           id BIGSERIAL PRIMARY KEY,
                           question VARCHAR(1000),
                           answer VARCHAR(1000),
                           topic_id BIGINT REFERENCES topic(id)
);