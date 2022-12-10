drop database if exists mas_test;
create database mas_test;
use mas_test;

create table genre (
	genre_id int primary key auto_increment,
    name varchar(25) not null
);

create table media (
media_id int primary key auto_increment,
title varchar(200) not null,
`year` int not null,
`type` varchar(20) not null,
link varchar(300),
image_url varchar(300),
mean_stars decimal(3) not null
);

create table media_genre (
media_id int not null,
genre_id int not null,

constraint fk_media_id
foreign key(media_id)
references media(media_id),

constraint fk_genre_id
foreign key(genre_id)
references genre(genre_id)
);

drop table if exists app_user_role;
drop table if exists app_role;
drop table if exists app_user;

create table app_user (
	app_user_id int primary key auto_increment,
    username varchar(50) not null,
    password_hash varchar(1024) not null,
    enabled boolean not null default true,
    private_profile bit not null default 0
);

create table app_role (
	app_role_id int primary key auto_increment,
    `name` varchar(50) not null unique
);

create table app_user_role (
	app_user_id int not null,
    app_role_id int not null,
    constraint pk_app_user_role
		primary key(app_user_id, app_role_id),
	constraint fk_app_user_role_user_id
		foreign key(app_user_id)
        references app_user(app_user_id),
	constraint fk_app_user_role_role_id
		foreign key(app_role_id)
        references app_role(app_role_id)
);


create table review (
review_id int primary key auto_increment not null,
stars int not null,
opinion varchar(1000),
app_user_id int not null,
media_id int not null,

constraint fk_app_user_review_id
foreign key(app_user_id)
references app_user(app_user_id),

constraint fk_media_review_id
foreign key(media_id)
references media(media_id),

constraint uq_media_review_id
unique(app_user_id, media_id)

);

delimiter //
create procedure set_known_good_state()
begin

	set sql_safe_updates = 0;
    delete from media_genre;
    delete from review;
    alter table review auto_increment = 1;
    delete from app_user_role;
    alter table app_user_role auto_increment = 1;
    delete from app_user;
    alter table app_user auto_increment = 1;
    delete from app_role;
    alter table app_role auto_increment = 1;
    delete from genre;
    alter table genre auto_increment = 1;
    delete from media;
	alter table media auto_increment = 1;
    
    
    insert into genre values
	(1, 'Sci-Fi'),
    (2, 'Horror'),
    (3, 'Comedy'),
    (4, 'Romace'),
    (5, 'Holiday'),
    (6, 'Genre to Be Deleted');
    
insert into media values
	(1, 'Harry Potter', 2007, 'BOOK', 'https://en.wikipedia.org/wiki/Harry_Potter', 'https://en.wikipedia.org/wiki/Harry_Potter', 4),
    (2, 'Interstellar', 2014, 'MOVIE', 'https://en.wikipedia.org/wiki/Interstellar_(film)', 'https://en.wikipedia.org/wiki/Interstellar_(film)', 4);

insert into media_genre values
	(1, 1),
    (2, 1);
    

insert into app_role (app_role_id, `name`) values
	(1, 'USER'),
    (2, 'ADMIN');
    
insert into app_user (app_user_id, username, password_hash) values
	(1, 'user', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (2, 'admin', '$2a$10$z8mwVv2mOjkWkFuzxYUFcO6SH1FaEftCw4M2Ltv6/5x7nigwEJKIO');
    
insert into app_user_role(app_user_id, app_role_id) values
	(1, 1),
    (2, 2);

insert into review values 
(1, 4,'Very long book but its worth the time', 1, 1),
(2, 4,'One of my favorite movies!', 1, 2);


set sql_safe_updates = 1;

    select 1;
    end //
    
    delimiter ;
    
    call set_known_good_state();
    select * from genre;
    
    select * from media;
    
    

    
    
    
