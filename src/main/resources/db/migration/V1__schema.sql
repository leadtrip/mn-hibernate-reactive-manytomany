CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

drop table if exists book_genre;
DROP TABLE IF EXISTS genre;
drop table if exists book;

CREATE TABLE genre (
    id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
   name  VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE book (
    id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name  VARCHAR(255) NOT NULL UNIQUE
);

create table book_genre (
    book_id uuid not null,
    genre_id uuid not null
);

insert into genre values ('513716f3-eda5-437a-a320-37278e7e4a89', 'fiction');
insert into genre values ('d7c37b53-9572-42d0-b58f-087b153d3db4', 'sci-fi');
insert into genre values ('1d6ca063-1fcf-4876-a659-3433211b2e11', 'horror');

insert into book values('e009800f-4bd7-4c6b-b97a-c18a6049ccec', 'War of the worlds');
insert into book values('58d9993a-a725-443d-8a3e-f645cdf4fcbb', 'The shining');

insert into book_genre values('e009800f-4bd7-4c6b-b97a-c18a6049ccec', '513716f3-eda5-437a-a320-37278e7e4a89');
insert into book_genre values('e009800f-4bd7-4c6b-b97a-c18a6049ccec', 'd7c37b53-9572-42d0-b58f-087b153d3db4');

insert into book_genre values('58d9993a-a725-443d-8a3e-f645cdf4fcbb', '513716f3-eda5-437a-a320-37278e7e4a89');
insert into book_genre values('58d9993a-a725-443d-8a3e-f645cdf4fcbb', '1d6ca063-1fcf-4876-a659-3433211b2e11');