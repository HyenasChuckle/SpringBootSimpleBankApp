-- accounts
CREATE TABLE accounts (
    id bigserial NOT NULL,
    owner_id bigint NOT NULL,
    number varchar(24) UNIQUE NOT NULL,
    balance decimal(15,2) NOT NULL,
    type varchar(24) NOT NULL,
    CONSTRAINT accounts_pk PRIMARY KEY (id)
);

-- authorities
CREATE TABLE authorities (
    customer_id bigint NOT NULL,
    authority_type varchar(16) NOT NULL,
    CONSTRAINT authorities_pk PRIMARY KEY (customer_id,authority_type)
);

-- customers
CREATE TABLE customers (
    id bigserial NOT NULL,
    email varchar(32) UNIQUE NOT NULL,
    password varchar(255) NOT NULL,
    first_name varchar(64) NOT NULL,
    last_name varchar(64) NOT NULL,
    status varchar(16) NOT NULL,
    CONSTRAINT customers_pk PRIMARY KEY (id)
);

-- transactions
CREATE TABLE transactions (
    id bigserial NOT NULL,
    receiver_number varchar(24) NOT NULL,
    source_id bigint NOT NULL,
    amount decimal(15,2) NOT NULL,
    type varchar(16) NOT NULL,
    timestamp timestamp NOT NULL,
    CONSTRAINT transactions_pk PRIMARY KEY (id)
);

-- accounts_customers
ALTER TABLE accounts ADD CONSTRAINT accounts_customers
    FOREIGN KEY (owner_id)
    REFERENCES customers (id)
    NOT DEFERRABLE
    INITIALLY IMMEDIATE;

-- authorities_customers
ALTER TABLE authorities ADD CONSTRAINT authorities_customers
    FOREIGN KEY (customer_id)
    REFERENCES customers (id)
    NOT DEFERRABLE
    INITIALLY IMMEDIATE;

-- transactions_accounts
ALTER TABLE transactions ADD CONSTRAINT transactions_accounts
    FOREIGN KEY (source_id)
    REFERENCES accounts (id)
    NOT DEFERRABLE
    INITIALLY IMMEDIATE;