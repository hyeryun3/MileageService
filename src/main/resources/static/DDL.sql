create database triple character set utf8mb4 collate utf8mb4_unicode_ci;

create table user
(
    user_id   bigint primary key auto_increment,
    user_uuid varchar(36) not null,
    point     int default 0
) charset = utf8;

create table place
(
    place_id   bigint primary key auto_increment,
    place_uuid varchar(36) not null
) charset = utf8;

create table review
(
    review_id    bigint primary key auto_increment,
    review_uuid  varchar(36)  not null,
    content      varchar(255) not null,
    date         date         not null,
    first_review tinyint(1) default 0,
    user_id      bigint,
    place_id     bigint,
    foreign key (user_id) references user (user_id) on delete cascade,
    foreign key (place_id) references place (place_id) on delete cascade
) charset = utf8;


create table photo
(
    photo_id       bigint primary key auto_increment,
    attached_photo varchar(50) not null,
    review_id      bigint,
    foreign key (review_id) references review (review_id) on delete cascade
) charset = utf8;


create table point
(
    point_id bigint primary key auto_increment,
    info     varchar(50) not null,
    point    tinyint(1)  not null,
    date     date        not null,
    user_id  bigint,
    place_id bigint,
    foreign key (user_id) references user (user_id) on delete cascade,
    foreign key (place_id) references place (place_id) on delete cascade
) charset = utf8

