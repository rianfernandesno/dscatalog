CREATE TABLE tb_product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(10, 2),
    img_url VARCHAR(255),
    date TIMESTAMP WITHOUT TIME ZONE
);