create table wedstrijden(
       id VARCHAR(36) primary key,
       land1 VARCHAR(32) not null,
       land2 VARCHAR(32) not null,
       tijdstip DATETIME not null,
       stadion_id VARCHAR(36) not null,
       aantal_beschikbare_plaatsen INTEGER not null,
       constraint wedstrijden_stadions_fk foreign key(stadion_id) references stadions(id)
);
