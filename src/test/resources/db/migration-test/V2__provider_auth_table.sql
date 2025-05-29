create table provider_auth_datas
(
    id                int          not null primary key,
    provider_name     varchar(255) not null,
    provider_password varchar(255) not null

);

INSERT INTO provider_auth_datas (id, provider_name, provider_password)
VALUES (1, 'FakePay', '1234');


