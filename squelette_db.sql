create table account_type
(
    id   int auto_increment
        primary key,
    type varchar(255) not null
);

create table ai_type
(
    id   int auto_increment
        primary key,
    type varchar(255) not null
);

create table ai
(
    id   int auto_increment
        primary key,
    name varchar(255) null,
    host varchar(255) not null,
    port int          not null,
    type int          null,
    constraint ai_ai_type_id_fk
        foreign key (type) references ai_type (id)
);

create table challenge
(
    challenge_id          smallint auto_increment
        primary key,
    challenge_name        varchar(20) not null,
    challenge_description text        not null
);

create table gender
(
    id     int auto_increment
        primary key,
    gender varchar(25) not null
);

create table `match`
(
    match_id          smallint auto_increment,
    id_player_1       smallint null,
    id_player_2       smallint null,
    id_player_3       smallint null,
    id_player_4       smallint null,
    match_time        int      null,
    mean_time_player1 int      null,
    mean_time_player2 int      null,
    mean_time_player3 int      null,
    mean_time_player4 int      null,
    id_winner         smallint null,
    constraint match_match_id_uindex
        unique (match_id)
);

alter table `match`
    add primary key (match_id);

create table prediction
(
    id_prediction smallint   null,
    id_predicter  smallint   null,
    id_predicted  smallint   null,
    id_match      smallint   null,
    thinkAI       tinyint(1) null
);

create table user
(
    id        smallint auto_increment
        primary key,
    username  varchar(20)   not null,
    password  varchar(255)  null,
    birthdate date          null,
    gender    int default 2 null,
    type      int           null,
    id_labo   int           null,
    constraint user_username_uindex
        unique (username),
    constraint user_account_type_id_fk
        foreign key (type) references account_type (id),
    constraint user_gender_id_fk
        foreign key (gender) references gender (id)
);

