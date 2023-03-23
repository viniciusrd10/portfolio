DROP TABLE IF EXISTS PUBLIC.EVENT CASCADE;
DROP TABLE IF EXISTS PUBLIC.SUBSCRIPTION;

CREATE TABLE if not exists PUBLIC.EVENT
(
    event_id     INT GENERATED ALWAYS AS IDENTITY,
    event_type   VARCHAR(50),
    status       VARCHAR(50),
    created_date TIMESTAMP,
    UNIQUE (event_type),
    PRIMARY KEY (event_id)
);

CREATE TABLE if not exists PUBLIC.SUBSCRIPTION
(
    subscription_id INT GENERATED ALWAYS AS IDENTITY,
    callback_url    VARCHAR(255),
    event_id        INT          NOT NULL,
    owner_id        VARCHAR(255) NOT NULL,
    created_date    TIMESTAMP    NOT NULL,
    PRIMARY KEY (subscription_id),
    CONSTRAINT fk_event
        FOREIGN KEY (event_id)
            REFERENCES EVENT (event_id)
            ON DELETE CASCADE
);
    COMMIT;