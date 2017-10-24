alter table SPRINT drop constraint FKr80l66xj2ct55kqcu9492uajt;
alter table STORY drop constraint FKgsgk46gsij1um5uo8h16kf5s9;
drop table if exists SPRINT cascade;
drop table if exists STORY cascade;
drop sequence SPRINT_ID_SEQ;
