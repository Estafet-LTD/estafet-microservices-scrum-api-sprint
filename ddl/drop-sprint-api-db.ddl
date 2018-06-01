alter table STORY drop constraint STORY_TO_SPRINT_FK;
drop table if exists MESSAGE_EVENT cascade;
drop table if exists SPRINT cascade;
drop table if exists STORY cascade;
drop sequence SPRINT_ID_SEQ;
