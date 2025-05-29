CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table payment_provider_callbacks
(
    uid                          uuid      default uuid_generate_v4() not null
        primary key,
    created_at                   timestamp default now()              not null,
    updated_at                   timestamp default now()              not null,
    body                         text                                 not null,
    provider_transaction_uid     varchar,
    type                         varchar(255),
    provider                     varchar(255),
    transaction_status           varchar,
    external_service_send_status varchar
);
create table verification_callbacks
(
    uid             uuid      default uuid_generate_v4() not null
        primary key,
    created_at      timestamp default now()              not null,
    modified_at     timestamp default now()              not null,
    body            text                                 not null,
    transaction_uid uuid,
    profile_uid     uuid                                 not null,
    status          varchar(25),
    type            varchar(255)
);
create table unknown_callbacks
(
    uid        uuid      default uuid_generate_v4() not null
        primary key,
    created_at timestamp default now()              not null,
    updated_at timestamp default now()              not null,
    body       text                                 not null
);

