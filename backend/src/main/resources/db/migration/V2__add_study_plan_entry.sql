CREATE TABLE study_plan_entry(
    id BIGSERIAL PRIMARY KEY,
    planned_date DATE,
    planned_hours INTEGER,
    topic_id BIGINT REFERENCES topic(id)
);