drop database if exists mas;
create database mas;
use mas;

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

insert into genre values
	(1, 'Sci-Fi'),
    (2, 'Horror'),
    (3, 'Comedy'),
    (4, 'Romance'),
    (5, 'Holiday'),
    (6, 'Fantasy'),
    (7, 'Contemporary'),
    (8, 'Futuristic'),
    (9, 'History'),
    (10, 'Non-Fiction'),
    (11, 'Medical'),
    (12, 'Poetry'),
    (13, 'Ethics'),
    (14, 'Dsytopian'),
    (15, 'Crime'),
    (16, 'Sports'),
    (17, 'Drama'),
    (18, 'Western'),
    (19, 'Mystery'),
    (20,'Thriller'),
    (21,'Action'),
    (22, 'Satire'),
    (23, 'Documentary'),
    (24, 'Super Hero')
    ;
    
insert into media values
	(1, 'Harry Potter and The Deathly Hallows', 2007, 'BOOK', 'https://en.wikipedia.org/wiki/Harry_Potter', 'https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1663805647l/136251.jpg', 4),
    (2, 'Interstellar', 2014, 'MOVIE', 'https://en.wikipedia.org/wiki/Interstellar_(film)','https://upload.wikimedia.org/wikipedia/en/b/bc/Interstellar_film_poster.jpg?20171108100413', 5),
    (3, 'The Golden Enclaves', 2022, 'BOOK', 'https://www.goodreads.com/en/book/show/55858638-the-golden-enclaves', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1642265468i/59743336.jpg', 4),
    (4, 'Dogs of War', 2017, 'BOOK', 'https://www.goodreads.com/book/show/35827220-dogs-of-war', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1501011224i/35827220.jpg', 4),
    (5, 'What Moves The Dead', 2022, 'BOOK', '', 'https://m.media-amazon.com/images/I/51PWwrEc3eL.jpg', 4),
    (6, 'Where The Forest Meets The Stars', 2019, 'BOOK', 'https://www.goodreads.com/book/show/40545956-where-the-forest-meets-the-stars', 'https://m.media-amazon.com/images/I/918hXn4Uy1L.jpg', 4),
    (7, 'The Facemaker', 1967, 'BOOK', 'https://en.wikipedia.org/wiki/The_Facemaker', 'https://mpd-biblio-covers.imgix.net/9780374282301.jpg', 4),
    (8, 'Three Days in Undead Shoes', 2021, 'BOOK', 'https://www.goodreads.com/book/show/53404631-three-days-in-undead-shoes', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1639780838i/59842597.jpg', 4),
    (9, 'What Is a Complex System?', 2020, 'BOOK', 'https://www.goodreads.com/en/book/show/51283448-what-is-a-complex-system', 'https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1594446565i/51283448._UY400_SS400_.jpg', 4),
    (10, 'The Lathe Of Heaven', 1971, 'BOOK', 'https://www.goodreads.com/book/show/59924.The_Lathe_of_Heaven', 'https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1433084322l/59924.jpg', 4),
    (11, 'Maid and Minstrel', 2022, 'BOOK', 'https://www.goodreads.com/book/show/60806334-maid-and-minstrel', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1649859919i/60806334.jpg', 4),
    (12, 'I Am Not a Serial Killer', 2009, 'BOOK', 'https://www.goodreads.com/book/show/35120549-i-am-not-a-serial-killer', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1494596508i/35120549.jpg', 3),
    (13, 'A Memory Called Empire', 2019, 'BOOK', 'https://www.goodreads.com/book/show/39873472-a-memory-called-empire', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1528119589i/39873472.jpg', 4),
    (14, 'Absolute Witch', 2006, 'BOOK', 'https://www.goodreads.com/book/show/8364416-1-absolute-witch-1', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1396177629i/8364416.jpg', 4),
    (15, 'The Cost-Benefit Revolution', 2018, 'BOOK', 'https://www.goodreads.com/book/show/39644138-the-cost-benefit-revolution', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1534281478i/39644138.jpg', 3),
    (16, 'Midnight Picnic', 2008, 'BOOK', 'https://www.goodreads.com/book/show/11509149-midnight-picnic', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1431463265i/11509149.jpg', 3),
    (17, 'Neuromancer', 1984, 'BOOK', 'https://www.goodreads.com/book/show/6088007-neuromancer', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1554437249i/6088007.jpg', 3),
    (18, 'Good Omens: The Nice and Accurate Prophecies of Agnes Nutter, Witch', 2000, 'BOOK', 'https://www.goodreads.com/book/show/12067.Good_Omens', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1615552073i/12067.jpg', 4),
    (19, 'The Red Wheelbarrow and Other Poems', 2018, 'BOOK', 'https://www.goodreads.com/book/show/36327050-the-red-wheelbarrow-and-other-poems', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1507842424i/36327050.jpg', 3),
    (20, 'Thor', 1992, 'BOOK', 'https://www.goodreads.com/book/show/542284.Thor', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1568820423i/542284.jpg', 4),
    (21, 'Eating Animals', 2009, 'BOOK', 'https://www.goodreads.com/book/show/7294052-eating-animals', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1592227162i/7294052.jpg', 4),
    (22, 'Fantastical', 2011, 'BOOK', 'https://www.goodreads.com/book/show/13005310-fantastical', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1327913381i/13005310.jpg', 1),
    (23, 'A Feeling of Wrongness: Pessimistic Rhetoric on the Fringes of Popular Culture', 2018, 'BOOK', 'https://www.goodreads.com/book/show/40711757-a-feeling-of-wrongness', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1544308482i/40711757.jpg', 3),
    (24, 'The Underground Railroad', 2016, 'BOOK', 'https://www.goodreads.com/book/show/54102715-the-underground-railroad', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1592235875i/54102715.jpg', 4),
    (25, 'Gokusen', 2000, 'BOOK', 'https://www.goodreads.com/book/show/2044363._1', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1487226511i/2044363.jpg', 4),
    (26, 'The Forever War', 2008, 'BOOK', 'https://www.goodreads.com/book/show/2517439.The_Forever_War', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1320478597i/2517439.jpg', 4),
    (27, 'Bone Swans: Stories', 2015, 'BOOK', 'https://www.goodreads.com/book/show/25696314-bone-swans', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1433891166i/25696314.jpg', 5),
    (28, 'The Ethics of Ambiguity', 1947, 'BOOK', 'https://www.goodreads.com/book/show/40104261-the-ethics-of-ambiguity', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1526071613i/40104261.jpg', 4),
    (29, 'Kaputt', 1944, 'BOOK', 'https://www.goodreads.com/book/show/138428.Kaputt', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1657568901i/138428.jpg', 5),
    (30, 'The Conspiracy Against the Human Race: A Contrivance of Horror', 2011, 'BOOK', 'https://www.goodreads.com/book/show/39333049-the-conspiracy-against-the-human-race', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1521580403i/39333049.jpg', 3),
    (31, 'World War Z: An Oral History of the Zombie War', 2006, 'BOOK', 'https://www.goodreads.com/book/show/8908.World_War_Z', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1528312647i/8908.jpg', 5),
    (32, 'The Blue Sword', 1982, 'BOOK', 'https://www.goodreads.com/book/show/407813.The_Blue_Sword', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1563198223i/407813.jpg', 5),
    (33, 'Snowfall', 2017, 'TV_SHOW', 'https://www.imdb.com/title/tt6439752/', 'https://flxt.tmsimg.com/assets/p14129654_b1t_v9_aa.jpg', 4),
    (34, 'YOU', 2018, 'TV_SHOW', 'https://www.imdb.com/title/tt7335184/', 'https://themommadiaries.com/wp-content/uploads/2021/10/EN-US_You_S3_Main_Best_Lies_Vertical_27x40_RGB_PRE.jpg', 4),
    (35, 'Ted Lasso', 2020, 'TV_SHOW', 'https://www.imdb.com/title/tt10986410/', 'https://www.themoviedb.org/t/p/original/oX7QdfiQEbyvIvpKgJHRCgbrLdK.jpg', 5),
    (36, 'Yellowstone', 2018, 'TV_SHOW', 'https://www.imdb.com/title/tt4236770/', 'https://static.tvtropes.org/pmwiki/pub/images/yellowstone.jpg', 5),
    (37, '1883', 2021, 'TV_SHOW', 'https://www.imdb.com/title/tt13991232/', 'https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcSvIpSYk6vPPmY80XAnsTn4cR1ToQLEDto6aGwzG3uzJXAOm1xT', 4),
    (38, 'Only Murders in the Building', 2021, 'TV_SHOW', 'https://www.imdb.com/title/tt12851524/', 'https://resizing.flixster.com/fHMwBBWG93TxbeSQTtGOTKAWHSs=/ems.cHJkLWVtcy1hc3NldHMvdHZzZWFzb24vZjNiNjkxZjgtNzBkZC00ZTY0LWFmNzEtYzk3N2MwYjQ5ZThiLnBuZw==', 4),
    (39, 'Starstruck', 2021, 'TV_SHOW', 'https://www.imdb.com/title/tt10801368/', 'https://images.justwatch.com/poster/246781240/s592/starstruck-2021-0', 4),
    (40, 'The Other Two', 2019, 'TV_SHOW', 'https://www.imdb.com/title/tt8310612/', 'https://www.shopyourtv.com/wp-content/uploads/2019/05/the-other-two.jpg', 3),
    (41, 'What We Do in the Shadows', 2019, 'TV_SHOW', 'https://www.imdb.com/title/tt7908628/', 'https://m.media-amazon.com/images/M/MV5BNWYwNGMwNTItMzZiNS00YTNhLTg5ZDItOTE5YzdhYWRjOWQ2XkEyXkFqcGdeQXVyMTUzMTg2ODkz._V1_FMjpg_UX1000_.jpg', 4),
    (42, 'Squid Game', 2021, 'TV_SHOW', 'https://www.imdb.com/title/tt10919420/', 'https://m.media-amazon.com/images/M/MV5BYWE3MDVkN2EtNjQ5MS00ZDQ4LTliNzYtMjc2YWMzMDEwMTA3XkEyXkFqcGdeQXVyMTEzMTI1Mjk3._V1_.jpg', 5),
    (43, 'The White Lotus', 2021, 'TV_SHOW', 'https://www.imdb.com/title/tt13406094/', 'https://resizing.flixster.com/1hClPf9PTeOw786rFiouVh8ibkQ=/ems.cHJkLWVtcy1hc3NldHMvdHZzZXJpZXMvUlRUVjk1NzE0NS53ZWJw', 4),
    (44, 'Euphoria', 2019, 'TV_SHOW', 'https://www.imdb.com/title/tt8772296/', 'https://flxt.tmsimg.com/assets/p16805962_b_v13_ab.jpg', 5),
    (45, 'Stranger Things', 2016, 'TV_SHOW', 'https://www.imdb.com/title/tt4574334/', 'https://resizing.flixster.com/0xxuABVVuzJrUT130WFHKE-irEg=/ems.cHJkLWVtcy1hc3NldHMvdHZzZWFzb24vNzUyMTFhOTktZTU4Ni00ODkyLWJlYjQtZTgxYTllZmU2OGM0LmpwZw==', 5),
    (46, 'For All Mankind', 2019, 'TV_SHOW', 'https://www.imdb.com/title/tt7772588/', 'https://flxt.tmsimg.com/assets/p17568214_b_v13_ag.jpg', 3),
    (47, 'Yellowjackets', 2021, 'TV_SHOW', 'https://www.imdb.com/title/tt11041332/', 'https://m.media-amazon.com/images/M/MV5BZDM1NzA2OWItMWEzNC00Yzc2LWIxMmUtOWY0ZWJhMzEwNWYxXkEyXkFqcGdeQXVyOTA3MTMyOTk@._V1_.jpg', 4),
    (48, 'Hacks', 2021, 'TV_SHOW', 'https://www.imdb.com/title/tt11815682/', 'https://m.media-amazon.com/images/M/MV5BZjgxODgwNTEtYjMyNC00NmM3LWEzN2QtN2RmOWY0ZDI2NmJmXkEyXkFqcGdeQXVyMTEyMjM2NDc2._V1_FMjpg_UX1000_.jpg', 4),
    (49, 'The Good Fight', 2017, 'TV_SHOW', 'https://www.imdb.com/title/tt5853176/', 'https://m.media-amazon.com/images/M/MV5BMDI3OThlOTUtZGY0ZC00NmNlLTk2YTEtYjA3NjE3ODAwZjMzXkEyXkFqcGdeQXVyODM0NDY1ODY@._V1_FMjpg_UX1000_.jpg', 4),
    (50, 'Reservation Dogs', 2021, 'TV_SHOW', 'https://www.imdb.com/title/tt13623580/', 'https://m.media-amazon.com/images/M/MV5BOTZmYWIyYzctZTk1Ni00MGYwLTk4MjctNjMxNTdkNzc3MTM0XkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_.jpg', 5),
    (51, 'How to With John Wilson', 2020, 'TV_SHOW', 'https://www.imdb.com/title/tt10801534/', 'https://m.media-amazon.com/images/M/MV5BN2I1ZmVlZmItYmQ4My00ZWQxLTljYjgtMWU3MjRiNjFkZWZiXkEyXkFqcGdeQXVyNjYyMjE4NDY@._V1_.jpg', 5),
    (52, 'Better Things', 2016, 'TV_SHOW', 'https://www.imdb.com/title/tt4370596/', 'https://flxt.tmsimg.com/assets/p13007967_b_v13_an.jpg', 3),
    (53, 'The Boys', 2019, 'TV_SHOW', 'https://www.imdb.com/title/tt1190634/', 'https://i0.wp.com/bloody-disgusting.com/wp-content/uploads/2022/03/the-boys-season-3-poster.png?ssl=1', 5),
    (54, 'The Righteous Gemstones', 2019, 'TV_SHOW', 'https://www.imdb.com/title/tt8634332/', 'https://flxt.tmsimg.com/assets/p17110149_b_v13_ac.jpg', 4),
    (55, 'Evil', 2019, 'TV_SHOW', 'https://www.imdb.com/title/tt9055008/', 'https://resizing.flixster.com/hwOZijRgjf0zbcrKKVH8KZpEPsA=/ems.cHJkLWVtcy1hc3NldHMvdHZzZXJpZXMvYTkwNzA5ZWItMTU5OC00MzZhLWE0MTUtMWUxMzg3Y2I4NjVlLmpwZw==', 4),
    (56, 'Atlanta', 2016, 'TV_SHOW', 'https://www.imdb.com/title/tt4288182/', 'https://m.media-amazon.com/images/M/MV5BZGU1MzRhNmMtNDExOS00NTk2LWJlYzMtMzc4YWYyN2Q3M2ZmXkEyXkFqcGdeQXVyMTUzMTg2ODkz._V1_FMjpg_UX1000_.jpg', 4),
    (57, 'Barry', 2018, 'TV_SHOW', 'https://www.imdb.com/title/tt5348176/', 'https://m.media-amazon.com/images/M/MV5BMzE0MDNiNDMtZTQ4Ni00MmQ4LTk4YzktZjFkYTVmODEzMDc2XkEyXkFqcGdeQXVyNTE1NjY5Mg@@._V1_FMjpg_UX1000_.jpg', 4),
    (58, 'Pachinko', 2022, 'TV_SHOW', 'https://www.imdb.com/title/tt8888462/', 'https://upload.wikimedia.org/wikipedia/en/2/20/Pachinko_%28TV_series%29.jpeg', 4),
    (59, 'Succession', 2018, 'TV_SHOW', 'https://www.imdb.com/title/tt7660850/', 'https://m.media-amazon.com/images/M/MV5BZDE0ODVlYjktNjJiMC00ODk4LWIwNTktMWRhZmZiOGFhYmUwXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_.jpg', 5),
    (60, 'Severance', 2022, 'TV_SHOW', 'https://www.imdb.com/title/tt11280740/', 'https://m.media-amazon.com/images/M/MV5BMjkwZjcwMGQtNDAzOC00YjJiLThiYTgtNWU3ZjRiZmY2YzEzXkEyXkFqcGdeQXVyMTMzNDExODE5._V1_FMjpg_UX1000_.jpg', 5),
    (61, 'Better Call Saul', 2015, 'TV_SHOW', 'https://www.imdb.com/title/tt3032476/', 'https://images.justwatch.com/poster/8636010/s592/season-1', 4),
    (62, 'Abbott Elementary', 2021, 'TV_SHOW', 'https://www.imdb.com/title/tt14218830/', 'https://flxt.tmsimg.com/assets/p20001979_b_v13_ad.jpg', 5),
    (63, 'Ozark', 2017, 'TV_SHOW', 'https://www.imdb.com/title/tt5071412/', 'https://resizing.flixster.com/3ko6zO6791p1QPOXHUI2eCwmHXQ=/ems.cHJkLWVtcy1hc3NldHMvdHZzZXJpZXMvMDIyOTBmN2QtMzM0Yi00ODUxLWE0MWYtMmViYWJiOGViZjRkLmpwZw==', 5),
    (64, 'Power', 2014, 'TV_SHOW', 'https://www.imdb.com/title/tt3281796/', 'https://flxt.tmsimg.com/assets/p10426936_b_v13_al.jpg', 5),
    (65, 'Inception', 2010, 'MOVIE', 'https://www.imdb.com/title/tt1375666/', 'https://flxt.tmsimg.com/assets/p7825626_p_v8_af.jpg', 5),
    (66, 'The Departed', 2006, 'MOVIE', 'https://www.imdb.com/title/tt0407887/', 'https://i0.wp.com/www.tomaprimera.es/wp-content/uploads/2014/09/DepartedSnd.jpg', 5),
    (67, 'Children of Men', 2006, 'MOVIE', 'https://www.imdb.com/title/tt0206634/', 'https://occ-0-1722-1723.1.nflxso.net/art/5d612/c81e0b14adffc6c49f5a382315b55af433d5d612.jpg', 5),
    (68, 'The Dark Knight Rises', 2012, 'MOVIE', 'https://www.imdb.com/title/tt1345836/', 'https://images.moviesanywhere.com/137de7df53b44414af057f89c0647b0b/38dd4c91-6758-46bd-969b-6acf010a249b.jpg', 5),
    (69, 'Gladiator', 2000, 'MOVIE', 'https://www.imdb.com/title/tt0172495/', 'https://sites.psu.edu/filmsforall/files/2016/11/X62M0OmqJpDsTmTNBSEnb1Dpm2-1809v2k.jpg', 5),
    (70, 'The Incredibles', 2004, 'MOVIE', 'https://www.imdb.com/title/tt0317705/', 'https://images.moviesanywhere.com/86a6d716a6c5ed372c4ee0c040c40304/096c182e-46e0-42f8-95b5-7889eaa08f9c.jpg', 5),
    (71, 'Tropic Thunder', 2008, 'MOVIE', 'https://www.imdb.com/title/tt0942385/', 'https://m.media-amazon.com/images/M/MV5BNDE5NjQzMDkzOF5BMl5BanBnXkFtZTcwODI3ODI3MQ@@._V1_.jpg', 3),
    (72, 'Shutter Island', 2010, 'MOVIE', 'https://www.imdb.com/title/tt1130884/', 'https://m.media-amazon.com/images/M/MV5BYzhiNDkyNzktNTZmYS00ZTBkLTk2MDAtM2U0YjU1MzgxZjgzXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg', 4),
	(73, 'Captain Phillips', 2013, 'MOVIE', 'https://www.imdb.com/title/tt1535109/', 'https://images.moviesanywhere.com/f7e144307314226ce58775377afd04ac/d2e5ccb3-aad9-45e4-ad0f-82f0011ba506.jpg', 4),
    (74, 'Spider-Man 2', 2002, 'MOVIE', 'https://www.imdb.com/title/tt0316654/', 'https://m.media-amazon.com/images/M/MV5BMzY2ODk4NmUtOTVmNi00ZTdkLTlmOWYtMmE2OWVhNTU2OTVkXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_.jpg', 4),
    (75, 'Get Out', 2017, 'MOVIE', 'https://www.imdb.com/title/tt5052448/', 'https://m.media-amazon.com/images/M/MV5BMjUxMDQwNjcyNl5BMl5BanBnXkFtZTgwNzcwMzc0MTI@._V1_.jpg', 5),
    (76, 'Anchorman: The Legend of Ron Burgundy', 2014, 'MOVIE', 'https://www.imdb.com/title/tt0357413/', 'https://m.media-amazon.com/images/M/MV5BMTQ2MzYwMzk5Ml5BMl5BanBnXkFtZTcwOTI4NzUyMw@@._V1_.jpg', 5),
    (77, 'The Bourne Ultimatum', 2007, 'MOVIE', 'https://www.imdb.com/title/tt0440963/', 'https://m.media-amazon.com/images/M/MV5BNGNiNmU2YTMtZmU4OS00MjM0LTlmYWUtMjVlYjAzYjE2N2RjXkEyXkFqcGdeQXVyNDk3NzU2MTQ@._V1_.jpg', 3),
    (78, 'Apocalypto', 2006, 'MOVIE', 'https://www.imdb.com/title/tt0472043/', 'https://flxt.tmsimg.com/assets/p161392_p_v8_ab.jpg', 4),
    (79, 'Kill Bill: Vol. 1', 2003, 'MOVIE', 'https://www.imdb.com/title/tt0266697/', 'https://flxt.tmsimg.com/assets/p32988_p_v8_ae.jpg', 5),
    (80, 'Kill Bill: Vol. 2', 2004, 'MOVIE', 'https://www.imdb.com/title/tt0378194/', 'https://m.media-amazon.com/images/M/MV5BNmFiYmJmN2QtNWQwMi00MzliLThiOWMtZjQxNGRhZTQ1MjgyXkEyXkFqcGdeQXVyNzQ1ODk3MTQ@._V1_FMjpg_UX1000_.jpg', 4),
	(81, 'Black Panther', 2018, 'MOVIE', 'https://www.imdb.com/title/tt1825683/', 'https://lumiere-a.akamaihd.net/v1/images/p_blackpanther_19754_4ac13f07.jpeg', 5),
    (82, 'Toy Story 3', 2010, 'MOVIE', 'https://www.imdb.com/title/tt0435761/', 'https://lumiere-a.akamaihd.net/v1/images/p_toystory3_19639_3c584e1f.jpeg', 5),
    (83, 'Mad Max: Fury Road', 2015, 'MOVIE', 'https://www.imdb.com/title/tt1392190/', 'https://m.media-amazon.com/images/M/MV5BN2EwM2I5OWMtMGQyMi00Zjg1LWJkNTctZTdjYTA4OGUwZjMyXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg', 5),
    (84, 'Wall-E', 2008, 'MOVIE', 'https://www.imdb.com/title/tt0910970/', 'https://lumiere-a.akamaihd.net/v1/images/p_walle_19753_69f7ff00.jpeg?region=0%2C0%2C540%2C810', 3),
    (85, 'The Wolf of Wall Street', 2013, 'MOVIE', 'https://www.imdb.com/title/tt0993846/', 'https://flxt.tmsimg.com/assets/p9991602_p_v12_aj.jpg', 5),
    (86, 'The Social Network', 2010, 'MOVIE', 'https://www.imdb.com/title/tt1285016/', 'https://ogden_images.s3.amazonaws.com/www.lockhaven.com/images/2021/05/19143504/social-network-592x840.jpg', 4),
    (87, 'Nope', 2022, 'MOVIE', 'https://www.imdb.com/title/tt10954984/', 'https://play-lh.googleusercontent.com/kaoIlqgp0ZWHN22Am8gg1I3OoVwuaEWdc9yt78CPw6C5qnW8qXHJwHFY-7GepdBmGmnZunWsJois0ljl-ds', 4),
    (88, 'Halloween Ends', 2022, 'MOVIE', 'https://www.imdb.com/title/tt10665342/', 'https://i0.wp.com/bloody-disgusting.com/wp-content/uploads/2022/10/halloween-ends-novel.jpg?ssl=1', 3),
	(89, 'Minions: The Rise of Gru', 2022, 'MOVIE', 'https://www.imdb.com/title/tt5113044/', 'https://amc-theatres-res.cloudinary.com/v1664281192/amc-cdn/production/2/movies/49600/49599/Poster/142316.jpg', 5),
    (90, 'Avengers: Endgame', 2019, 'MOVIE', 'https://www.imdb.com/title/tt4154796/', 'https://images.moviesanywhere.com/4677177f6f0595289bc3e767e7b47459/1d6c6c73-ab1e-4453-969c-6a4e965ebb37.jpg', 5),
    (91, 'Moonlight', 2016, 'MOVIE', 'https://www.imdb.com/title/tt4975722/', 'https://m.media-amazon.com/images/M/MV5BNzQxNTIyODAxMV5BMl5BanBnXkFtZTgwNzQyMDA3OTE@._V1_.jpg', 4),
    (92, 'F9', 2021, 'MOVIE', 'https://www.imdb.com/title/tt5433138/', 'https://m.media-amazon.com/images/M/MV5BMjI0NmFkYzEtNzU2YS00NTg5LWIwYmMtNmQ1MTU0OGJjOTMxXkEyXkFqcGdeQXVyMjMxOTE0ODA@._V1_.jpg', 2),
    (93, 'The Matrix 4', 2021, 'MOVIE', 'https://www.imdb.com/title/tt10838180/', 'https://m.media-amazon.com/images/M/MV5BMGJkNDJlZWUtOGM1Ny00YjNkLThiM2QtY2ZjMzQxMTIxNWNmXkEyXkFqcGdeQXVyMDM2NDM2MQ@@._V1_.jpg', 5),
    (94, 'Red Notice', 2021, 'MOVIE', 'https://www.imdb.com/title/tt7991608/', 'https://m.media-amazon.com/images/M/MV5BZmRjODgyMzEtMzIxYS00OWY2LTk4YjUtMGMzZjMzMTZiN2Q0XkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_.jpg', 5),
    (95, 'The Tomorrow War', 2021, 'MOVIE', 'https://www.imdb.com/title/tt9777666/', 'https://m.media-amazon.com/images/M/MV5BNTI2YTI0MWEtNGQ4OS00ODIzLWE1MWEtZGJiN2E3ZmM1OWI1XkEyXkFqcGdeQXVyODk4OTc3MTY@._V1_FMjpg_UX1000_.jpg', 5),
    (96, 'Us', 2019, 'MOVIE', 'https://www.imdb.com/title/tt6857112/', 'https://tafttoday.com/wp-content/uploads/2019/05/MV5BZTliNWJhM2YtNDc1MC00YTk1LWE2MGYtZmE4M2Y5ODdlNzQzXkEyXkFqcGdeQXVyMzY0MTE3NzU@._V1_-1-568x900.jpg', 4),
    (97, 'Parasite', 2019, 'MOVIE', 'https://www.imdb.com/title/tt6751668/', 'https://i0.wp.com/cedars.cedarville.edu/wp/wp-content/uploads/2020/05/Parasite-scaled.jpg?fit=1657%2C2560&ssl=1', 5),
    (98, 'Straight Outta Compton', 2015, 'MOVIE', 'https://www.imdb.com/title/tt1398426/', 'https://m.media-amazon.com/images/M/MV5BMTA5MzkyMzIxNjJeQTJeQWpwZ15BbWU4MDU0MDk0OTUx._V1_FMjpg_UX1000_.jpg', 5),
    (99, 'The Hateful Eight', 2015, 'MOVIE', 'https://www.imdb.com/title/tt3460252/', 'https://m.media-amazon.com/images/M/MV5BMjA1MTc1NTg5NV5BMl5BanBnXkFtZTgwOTM2MDEzNzE@._V1_.jpg', 4),
    (100, '1917', 2019, 'MOVIE', 'https://www.imdb.com/title/tt8579674/', 'https://m.media-amazon.com/images/M/MV5BOTdmNTFjNDEtNzg0My00ZjkxLTg1ZDAtZTdkMDc2ZmFiNWQ1XkEyXkFqcGdeQXVyNTAzNzgwNTg@._V1_.jpg', 4);
    

insert into media_genre values
	(1, 1),
    (2, 1),
    (3, 6),
    (4, 1),
    (4, 8),
    (5, 6),
    (5, 2),
    (6, 4),
    (7, 10),
    (7, 11),
    (8, 4),
    (8, 2),
    (9, 10),
    (10, 8),
    (10, 1),
    (11, 6),
    (12, 7),
    (12, 2),
    (12, 6),
    (13, 1),
    (14, 6),
    (15, 10),
    (16, 2),
    (16, 7),
    (17, 1),
    (18, 6),
    (19, 12),
    (20, 2),
    (21, 10),
    (21, 13),
    (21, 7),
    (22, 4),
    (22, 6),
    (23, 13),
    (24, 9),
    (25, 7),
    (26, 10),
    (27, 6),
    (28, 13),
    (29, 9),
    (29, 2),
    (30, 2),
    (30, 13),
    (31, 14),
    (32, 6),
    (33, 15),
    (34, 15),
    (34, 2),
    (34, 4),
    (35, 3),
    (35, 16),
    (35, 17),
    (36, 17),
    (36, 18),
    (37, 17),
    (37, 18),
    (38, 3),
    (38, 17),
    (38, 19),
    (39, 3),
    (40, 3),
    (41, 6),
    (41, 2),
    (42, 19),
    (42, 2),
    (42, 20),
    (42, 21),
    (43, 22),
    (43, 22),
    (44, 17),
    (45, 2),
    (45, 20),
    (45, 17),
    (45, 19),
    (46, 20),
    (47, 20),
    (47, 19),
    (47, 2),
    (48, 3),
    (48, 17),
    (49, 17),
    (50, 3),
    (50, 17),
    (51, 3),
    (51, 23),
    (52, 3),
    (52, 17),
    (53, 24),
    (53, 3),
    (53, 17),
    (53, 21),
    (54, 3),
    (54, 15),
    (55, 17),
    (55, 20),
    (56, 3),
    (56, 22),
    (57, 3),
    (57, 15),
    (58, 17),
    (59, 3),
    (60, 20),
    (60, 19),
    (60, 3),
    (61, 17),
    (61, 15),
    (61, 3),
    (62, 3),
    (63, 15),
    (63, 20),
    (63, 17),
    (64, 15),
    (64, 17);

insert into app_role (app_role_id, `name`) values
	(1, 'USER'),
    (2, 'ADMIN');
    
insert into app_user (app_user_id, username, password_hash) values
	(1, 'user', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (2, 'admin', '$2a$10$z8mwVv2mOjkWkFuzxYUFcO6SH1FaEftCw4M2Ltv6/5x7nigwEJKIO'),
    (3, 'aryann', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (4, 'marcel', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (5, 'sarah', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (6, 'POTUS', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (7, 'johnnyW', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (8, 'WillyB', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (9, 'billyD', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (10, 'ChrisPBacon', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (11, 'nashPhill', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (12, 'mj', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (13, 'LJames', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (14, 'obama44', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (15, 'nickJ', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (16, 'taylorS', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (17, 'kendrickL', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (18, 'tristanT', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (19, 'paulG', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (20, 'mikeL', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (21, 'mrIncredible', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (22, 'chadwickB', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (23, 'beyonce', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (24, 'sunnyWilliams', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (25, 'benjaminF', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (26, 'thomasEddy', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (27, 'bobTheBuilder', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (28, 'meg', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (29, 'swaggyP', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6'),
    (30, 'darthVader', '$2a$10$O07BeyVEy.eGy9zmJQR/WeIDdb5Q6PMDbTZlUXOMQ0B.EAkbiuUK6');
    
insert into app_user_role(app_user_id, app_role_id) values
	(1, 1),
    (2, 2),
    (3, 1),
    (4, 1),
    (5, 1),
    (6, 1),
    (7, 1),
    (8, 1),
    (9, 1),
    (10, 1),
    (11, 1),
    (12, 1),
    (13, 1),
    (14, 1),
    (15, 1),
    (16, 1),
    (17, 1),
    (18, 1),
    (19, 1),
    (20, 1),
    (21, 1),
    (22, 1),
    (23, 1),
    (24, 1),
    (25, 1),
    (26, 1),
    (27, 1),
    (28, 1),
    (29, 1),
    (30, 1);

insert into review values 
	(1, 4,'Very long book but its worth the time', 1, 1),
    (2, 4,'One of my favorite movies!', 1, 2),
    (3, 5, 'Really solid book', 1, 3),
    (4, 4, 'Hard to go wrong with this one', 1, 5),
    (5, 5, 'Sad but a good read', 1, 4),
    (6, 1, 'Too depressing', 2, 4),
    (7, 4, 'Original, but not the best work', 1, 6),
    (8, 2, 'I did not enjoy.', 2, 6),
    (9, 2, 'A bit much.', 2, 30),
    (10, 4, 'A lot', 1, 30),
    (11, 5, 'This show is a cultural reset. No other show is like this. The games the characters played were insane to watch. I could not believe how well thought out this show was from the directors and writers of this Netflix series. Truly something special.', 6, 42),
    (13, 5, 'The South Korean TV sensation taps into the universal apprehensions of pressing debt, strained family relations and the longing for a better life.', 7, 42),
    (14, 4, 'Its not the kind of apocalypse story that longs for hopeful human resilience; its most eloquent on the topics of financial despondence and weaponized nostalgia.', 8, 42),
    (15, 5, 'Hypnotic.', 9, 42),
    (16, 5, 'deservedly one of the hottest shows of the year, and it highlights the good streaming can do in regards to breaking down cultural barriers and exposing viewers to masterworks from across the world they would otherwise not have access to.', 10, 42),
    (17, 3, 'squid game squid game squid game', 11, 42),
    (18, 4, 'Squid Games unflinching brutality is not for the faint of heart, but sharp social commentary and a surprisingly tender core will keep viewers glued to the screen - even if its while watching between their fingers.', 12, 42),
    (19, 4, 'Its okay, i have seen more that 400 asian series, this one doesnt have any special, is just entertaining.', 13, 42),
    (20, 3, 'I found Squid Game to be interesting and full of potential, but frankly it was too drawn out and boring. In my opinion, all of the solid entertainment featured in this show could have been condensed down to a great movie! Instead, I found myself bored and easily distracted during majority of the season as the back to back clips of panicked faces and heavy breathing got old very quickly.', 14, 42),
    (21, 5, 'Squid Game has one annoying characters and some questionable choices here and there, but it is a riveting, socially relevant and brutal experience that has you consistently hooked with well-written characters and solid production.', 15, 42),
    (22, 5, 'Great series. Crazy plot like battle royale, crazy story like hunger games, at the same times feels so relevant with the worlds situation.', 16, 42),
    (23, 4, 'Was an interesting story and had good progression for its main characters. It was a refreshing show where the political nonsense wasnt made front and center. It was a good story with a steady build up and intriguing premise for a binge-able series that has me excited to check out its next season.', 17, 42),
    (24, 4, 'I saw this series without knowing what it was and I must say it was a surprise, it was really good in everything!!! I recommend.', 18, 42),
    (25, 4, 'Pacing is a bit drawn out, and a lot rides on the novelty of the games and who will make it, but theres enough there with the characters to also care about how things actually proceed. (Watched subbed, so cannot speak to quality of dub.)', 19, 42),
    (26, 4, 'I went through a roller coaster of emotions while watching this. Seong Gi-hun is amazing as player 456. The production music, costumes, and set design were incredible and unlike anything Ive seen in television or film. The story was filled with complex characters and situations and I really appreciated the many twists and surprises along the way.', 20, 42),
    (29, 5, 'Korea keeps suprising me and it taste for more.', 23, 42),
    (30, 5, 'Inception engaged on a mainly intellectually level, but that isnt to say that film didnt pack an emotional impact.', 6, 65),
    (31, 4, 'The Departed is an example of a cinematic master seeking to live up to the veneration with which he is regarded by his pupils.', 6, 66),
    (32, 5, 'The Incredibles has a thoughtful consideration of adult relationships, a romanticism about the need for superheroes, and a caution against the decline of Self.', 6, 70),
    (33, 4, 'Stranger Things is a rare example of a cultural phenomenon that has delivered wistfulness and familiarity without simply giving audiences more of the same.', 6, 45),
    (34, 5, 'A good book for anyone wanting to make sense of complexity science, itself a rather complex and cross-interdisciplinary area. The book provides an excellent overview of complexity science and provides clarity to what it is.', 6, 9),
    (35, 4, 'I initially heard this book through the audio version on YouTube while doing work and enjoyed it so much that I bought both books. 100% worth the read. It presents a fascinating prospect of a zombie virus that has been around just as long as humans. ', 6, 31),
    (36, 4, 'The Boys is a story about superheroes fantastically well suited to skepticism, fatigue, and general wariness toward stories about superheroes.', 6, 53),
    (37, 5, 'Seeing Linney play that mix of confidence and fear is a gift. Its a bonus that she spends much of her time with lawyer Helen, one of the most marvellously ruthless characters on TV.', 6, 63);
    
    

select * from review;

select * from media_genre;
select * from media;
select * from genre;