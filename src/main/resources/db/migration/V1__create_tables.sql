CREATE TABLE tb_location(
    id BIGSERIAL PRIMARY KEY,
    city VARCHAR (30) NOT NULL,
    state VARCHAR (30) NOT NULL,
    address1 VARCHAR(50) NOT NULL,
     address2 VARCHAR(50) NOT NULL,
     zip_code VARCHAR(11) NOT NULL
);
CREATE TABLE tb_user(
    id BIGSERIAL PRIMARY KEY ,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    phone VARCHAR(11) NOT NULL,
    birth_date DATE,
    location_id BIGINT NOT NULL REFERENCES tb_location(id) ON DELETE CASCADE
);
CREATE TABLE tb_bank_account(
    id BIGSERIAL PRIMARY KEY,
    balance NUMERIC(19,2) NOT NULL,
    type_account VARCHAR(20) NOT NULL,
    credit_card_closing_date TIMESTAMP,
    user_id BIGINT NOT NULL REFERENCES tb_user(id) ON DELETE CASCADE
);
CREATE TABLE tb_category(
    id BIGSERIAL PRIMARY KEY,
    notify_limit NUMERIC(19,2) NOT NULL,
    name VARCHAR(30) NOT NULL
);
CREATE TABLE tb_monthly_expense(
    id BIGSERIAL PRIMARY KEY,
    month_total NUMERIC(19,2) NOT NULL,
    month VARCHAR(255) NOT NULL,
    limit_expense NUMERIC (19,2) NOT NULL,
    bank_account_id BIGINT NOT NULL REFERENCES tb_bank_account(id) ON DELETE CASCADE
);
CREATE TABLE tb_expense(
    id BIGSERIAL PRIMARY KEY,
    amount NUMERIC(19,2) NOT NULL,
    description VARCHAR(255) NOT NULL,
    expense_moment TIMESTAMP NOT NULL,
    category_id BIGINT NOT NULL REFERENCES tb_category(id) ON DELETE CASCADE,
    monthly_expense_id BIGINT NOT NULL REFERENCES tb_monthly_expense(id) ON DELETE CASCADE
);