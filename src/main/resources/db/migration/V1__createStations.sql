create table stadions(
    id VARCHAR(36) primary key,
    naam varchar(32) not null unique,
    plaatsen integer not null
);