create table tickets(
       id VARCHAR(36) primary key,
       email VARCHAR(32) not null,
       voetbal_code1 INT not null,
       voetbal_code2 INT not null,
       wedstrijd_id VARCHAR(36) not null,
       constraint tickets_wedstrijden_fk foreign key(wedstrijd_id) references wedstrijden(id)
);
