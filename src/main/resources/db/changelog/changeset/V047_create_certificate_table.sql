-- Расширение для UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS certificate_schema.certificate (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    serial_number VARCHAR(255) NOT NULL UNIQUE,
    course_id UUID NOT NULL,
    course_name VARCHAR(255) NOT NULL,
    listener_id UUID NOT NULL,
    listener_full_name VARCHAR(255) NOT NULL,
    score NUMERIC(5,2) NOT NULL,
    issued_date TIMESTAMP WITH TIME ZONE NOT NULL,
    expire_date TIMESTAMP WITH TIME ZONE NULL,
    certificate_template VARCHAR(50),
    upload_path TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_by VARCHAR(255),
    last_modified_at TIMESTAMP WITH TIME ZONE NULL,
    last_modified_by VARCHAR(255),
    CONSTRAINT uk_certificate_course_listener_date UNIQUE (course_id, listener_id, issued_date)
    );

-- Индексы (как в Entity)
CREATE INDEX IF NOT EXISTS idx_user_id
    ON certificate_schema.certificate(listener_id);

CREATE INDEX IF NOT EXISTS idx_reg_number
    ON certificate_schema.certificate(serial_number);

CREATE INDEX IF NOT EXISTS idx_certificate_course_id
    ON certificate_schema.certificate(course_id);