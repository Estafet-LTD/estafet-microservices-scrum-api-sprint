create sequence SPRINT_ID_SEQ start 1 increment 1;
create table SPRINT (SPRINT_ID int4 not null, END_DATE varchar(255) not null, NO_DAYS int4 not null, SPRINT_NUMBER int4 not null, PROJECT_ID int4 not null, START_DATE varchar(255) not null, SPRINT_STATUS varchar(255) not null, primary key (SPRINT_ID));
