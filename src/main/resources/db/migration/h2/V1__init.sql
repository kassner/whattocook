CREATE TABLE recipe (
    id bigint not null primary key,
    name varchar(255) NOT NULL,
    instructions text,
    UNIQUE (name)
);

CREATE SEQUENCE recipe_id_seq;

CREATE TABLE ingredient (
    id bigint not null primary key,
    name varchar(255) NOT NULL,
    UNIQUE (name)
);

CREATE SEQUENCE ingredient_id_seq;

CREATE TABLE recipe_ingredients (
    recipe_id bigint not null,
    ingredient_id bigint not null,
    FOREIGN KEY (recipe_id) REFERENCES recipe (id),
    FOREIGN KEY (ingredient_id) REFERENCES ingredient (id),
    PRIMARY KEY (recipe_id, ingredient_id)
);

CREATE TABLE session (
    id bigint NOT NULL,
    created_at timestamp without time zone NOT NULL,
    PRIMARY KEY (id)
);

CREATE SEQUENCE session_id_seq;

CREATE TABLE session_event (
    id bigint NOT NULL,
    session_id bigint NOT NULL,
    event_type varchar(255) NOT NULL,
    created_at timestamp NOT NULL,
    payload text,
    PRIMARY KEY (id),
    FOREIGN KEY (session_id) REFERENCES session (id)
);

CREATE SEQUENCE session_event_id_seq;

INSERT INTO recipe (id, name) VALUES (1, 'Tacos'), (2, 'Lasagna');
INSERT INTO ingredient (id, name) VALUES (1, 'Tortilla'), (2, 'Ground beef');
INSERT INTO recipe_ingredients (recipe_id, ingredient_id) VALUES (1, 1), (1, 2), (2, 2);
