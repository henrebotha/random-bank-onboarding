CREATE TABLE app_user
(
    id                    UUID                                 DEFAULT RANDOM_UUID() PRIMARY KEY,
    username              VARCHAR(64)                 NOT NULL,
    password              VARCHAR(64)                 NOT NULL,
    name                  VARCHAR(64)                 NOT NULL,
    address               VARCHAR(64)                 NOT NULL,
    date_of_birth         DATE                        NOT NULL,
    iban                  VARCHAR(64)                 NOT NULL,
    account_type          ENUM ('CURRENT', 'SAVINGS') NOT NULL,
    account_balance_cents INT                         NOT NULL DEFAULT 0
);
