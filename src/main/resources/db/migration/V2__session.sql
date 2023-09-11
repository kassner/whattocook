CREATE TABLE session (
    id bigint NOT NULL,
    created_at timestamp without time zone NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE session_event (
    id bigint NOT NULL,
    session_id bigint NOT NULL,
    event_type varchar(255) NOT NULL,
    created_at timestamp NOT NULL,
    payload text,
    PRIMARY KEY (id),
    FOREIGN KEY (session_id) REFERENCES session (id)
);

CREATE SEQUENCE session_id_seq OWNED BY session.id;
CREATE SEQUENCE session_event_id_seq OWNED BY session_event.id;
