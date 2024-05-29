-- Create the database.
create database if not exists cs4370_mb_platform;

-- Use the created database.
use cs4370_mb_platform;

-- Create the user table.
create table if not exists user (
    userId int auto_increment,
    username varchar(255) not null,
    password varchar(255) not null,
    firstName varchar(255) not null,
    lastName varchar(255) not null,
    primary key (userId),
    unique (username),
    constraint userName_min_length check (char_length(trim(userName)) >= 2),
    constraint firstName_min_length check (char_length(trim(firstName)) >= 2),
    constraint lastName_min_length check (char_length(trim(lastName)) >= 2)
);

-- post(postId<pk>, userId<fk>, postDate, postText)
-- Create the post table.
create table if not exists post (
    postId int auto_increment,
    userId int not null,
    postDate datetime not null,
    postText varchar(255) not null,
    primary key (postId),
    foreign key (userId) references user(userId)

);

--comment(commentId<pk>, postId<fk>, userId<fk>, commentDate, commentText)
--Create the comment table.
create table if not exists comment (
    commentId int auto_increment,
    postId int not null,
    userId int not null,
    commentDate datetime not null,
    commentText varchar(255) not null,
    primary key (commentId),
    foreign key (postId) references post(postId),
    foreign key (userId) references user(userId)
);

--heart(postId<fk>, userId<fk>) composite primary key (postId, userId)
--Create the heart table.
create table if not exists heart (
    postId int not null,
    userId int not null,
    primary key (postId, userId),
    foreign key (postId) references post(postId),
    foreign key (userId) references user(userId)
);

--bookmark(postId<fk>, userId<fk>) composite primary key (postId, userId)
create table if not exists bookmark (
    postId int not null,
    userId int not null,
    primary key (postId, userId),
    foreign key (postId) references post(postId),
    foreign key (userId) references user(userId)
);

--hashtag(hashTag, postId<fk>) composite primary key (hashTag, postId)
create table if not exists hashtag (
    hashTag varchar(255) not null,
    postId int not null,
    primary key (hashTag, postId),
    foreign key (postId) references post(postId)
);

--follow(followerUserId<fk>, followeeUserId<fk>) composite primary key (followerUserId,
--followeeUserId)
create table if not exists follow ( 
    followerUserId int not null,
    followeeUserId int not null,
    primary key (followerUserId, followeeUserId),
    foreign key (followerUserId) references user(userId),
    foreign key (followeeUserId) references user(userId)
);

--populate with users
insert into user (username, password, firstName, lastName) 
    values ('john', '$2a$10$TiqxekHptX3csiF9jJ17Pe9hFWa2BkzBc4sS9e3cY3lfZfpEofKM2', 'John', 'Doe');
insert into user (username, password, firstName, lastName) 
    values ('jane', '$2a$10$.2eqbmGpsxboc6YXe7YdE.gqg61t2g2KufJI/yrRgOHeOJKcxx2x6', 'Jane', 'Doe');
insert into user (username, password, firstName, lastName) 
    values ('arya', '$2a$10$Atnq4K2O4okbCdm4YBPeq.Y2k2hYZpBnS7VWoZk6TSeBmEKhwyhD.', 'Arya', 'Stark');
insert into user (username, password, firstName, lastName) 
    values ('tony', '$2a$10$ilOdXhk6Gux.gKoNqqItmeZOQb292oX5p3WzH72k7lbn89VjGQCx6', 'Tony', 'Stark');
insert into user (username, password, firstName, lastName) 
    values ('amy', '$2a$10$iufhLMHL/Wb8L1dBLoZIr.vXK7NJrKpZS5SsYpaGRicMZ9oYjR5ZS', 'Amy', 'Santiago');

--populate some follow relations
insert into follow (followerUserId, followeeUserId) values ('1', '2');
insert into follow (followerUserId, followeeUserId) values ('1', '3');
insert into follow (followerUserId, followeeUserId) values ('4', '2');
insert into follow (followerUserId, followeeUserId) values ('5', '1');

--populate some posts
insert into post (userId, postDate, postText) values ('1', now(), 'Hey! Glad to be here. #post'); 
insert into post (userId, postDate, postText) values ('1', now(), 'This is my #second post'); 
insert into post (userId, postDate, postText) values ('2', now(), 'Hey! Read my #first #post'); 
insert into post (userId, postDate, postText) values ('3', now(), 'I am scared to make a #post'); 
insert into post (userId, postDate, postText) values ('4', now(), 'Hello! Excited to make new friends'); 
insert into post (userId, postDate, postText) values ('4', now(), 'Hi! My name is Tony'); 
insert into post (userId, postDate, postText) values ('5', now(), 'Hi! This is Amy!'); 

--populate hashtags
insert into hashtag (hashTag, postId) values ('#post', 1);
insert into hashtag (hashTag, postId) values ('#second', 2);
insert into hashtag (hashTag, postId) values ('#first', 3);
insert into hashtag (hashTag, postId) values ('#post', 3);
insert into hashtag (hashTag, postId) values ('#post', 4);

--populate hearts
insert into heart (postId, userId) values ('2', '2');
insert into heart (postId, userId) values ('2', '3');
insert into heart (postId, userId) values ('3', '3');
insert into heart (postId, userId) values ('1', '4');

--populate comments
insert into comment (postId, userId, commentDate, commentText) values ('1', '2', now(), 'Hey! Nice to meet you'); 
insert into comment (postId, userId, commentDate, commentText) values ('3', '3', now(), 'Hey! I read it.'); 
insert into comment (postId, userId, commentDate, commentText) values ('5', '5', now(), 'We can be friends!'); 