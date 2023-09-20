CREATE TABLE recipe (
    id bigint not null primary key,
    name varchar(255) NOT NULL,
    instructions text
);

CREATE SEQUENCE recipe_id_seq OWNED BY recipe.id;

CREATE TABLE ingredient (
    id bigint not null primary key,
    name varchar(255) NOT NULL,
    UNIQUE (name)
);

CREATE TABLE recipe_ingredients (
    recipe_id bigint not null,
    ingredient_id bigint not null,
    FOREIGN KEY (recipe_id) REFERENCES recipe (id),
    FOREIGN KEY (ingredient_id) REFERENCES ingredient (id),
    PRIMARY KEY (recipe_id, ingredient_id)
);

CREATE SEQUENCE ingredient_id_seq OWNED BY ingredient.id;
