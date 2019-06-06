create table MOVIE
(
	ID NUMBER(19) not null primary key,
	TITLE VARCHAR2(256),
	RELEASE_DATE DATE
);
create sequence MOVIE_SEQ;

create table MOVIE_GENRE
(
	MOVIE_ID NUMBER(19) constraint MOVIE_GENRE_MOVIE_ID_FK references MOVIE (ID),
	GENRE VARCHAR2(256),
	primary key (MOVIE_ID, GENRE)
);

create table HALL
(
	ID NUMBER(19) not null primary key,
	NAME VARCHAR2(256),
	ROWS_COUNT NUMBER(10),
	SEATS_COUNT NUMBER(10)
);
create sequence HALL_SEQ;

create table MOVIE_SESSION
(
	ID NUMBER(19) not null primary key,
	MOVIE_ID NUMBER(19) constraint MOVIE_SESSION_MOVIE_ID_FK references MOVIE (ID),
	HALL_ID NUMBER(19) constraint MOVIE_SESSION_HALL_ID_FK references HALL (ID),
	START_TIME DATE,
	PRICE NUMBER(19)
);
create sequence MOVIE_SESSION_SEQ;

create table APP_USER
(
	ID NUMBER(19) not null primary key,
	FULL_NAME VARCHAR2(256),
	USERNAME VARCHAR2(256) unique,
	PASSWORD VARCHAR2(256)
);
create sequence APP_USER_SEQ;

create table BOOKING
(
	ID NUMBER(19) not null primary key,
	USER_ID NUMBER(19) constraint BOOKING_USER_ID_FK references APP_USER (ID),
	SESSION_ID NUMBER(19) constraint BOOKING_SESSION_ID_FK references MOVIE_SESSION (ID),
	ROW_NUM NUMBER(10),
	SEAT_NUM NUMBER(10),
	CREATED_DATE TIMESTAMP(6),
	STATUS VARCHAR2(256)
);
create sequence BOOKING_SEQ;

create table USER_FAVORITE_MOVIE
(
	USER_ID NUMBER(19) constraint USER_FAVORITE_MOVIE_USER_ID_FK references APP_USER (ID),
	MOVIE_ID NUMBER(19) constraint USER_FAVORITE_MOVIE_ID_FK references MOVIE (ID),
  primary key (USER_ID, MOVIE_ID)
);
