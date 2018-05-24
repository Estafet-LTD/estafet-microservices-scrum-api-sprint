create sequence SPRINT_ID_SEQ start 1 increment 1;
create table MESSAGE_EVENT (TOPIC_ID varchar(255) not null, MESSAGE_REFERENCE varchar(255) not null, VERSION int4, primary key (TOPIC_ID));
create table SPRINT (SPRINT_ID int4 not null, END_DATE varchar(255) not null, NO_DAYS int4 not null, SPRINT_NUMBER int4 not null, PROJECT_ID int4 not null, START_DATE varchar(255) not null, SPRINT_STATUS varchar(255) not null, primary key (SPRINT_ID));
create table STORY (STORY_ID int4 not null, STATUS varchar(255) not null, SPRINT_ID int4 not null, primary key (STORY_ID));
alter table STORY add constraint STORY_TO_SPRINT_FK foreign key (SPRINT_ID) references SPRINT;
