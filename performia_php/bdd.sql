CREATE DATABASE IF NOT EXISTS performia;

USE performia;

CREATE TABLE user(id smallint NOT NULL AUTO_INCREMENT, username varchar(20) NOT NULL, password varchar(256), constraint pk_user primary key (id));

INSERT INTO user (id, username, password) VALUES(NULL, 'user1', '5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8');

-- login: toto, password: toto
INSERT INTO user (id, username, password) VALUES(NULL, 'toto', '0b9c2625dc21ef05f6ad4ddf47c5f203837aa32c');

CREATE TABLE challenge(challenge_id smallint NOT NULL AUTO_INCREMENT, challenge_name varchar(20) NOT NULL, challenge_description TEXT NOT NULL, constraint pk_challenge primary key(challenge_id));

INSERT INTO challenge (challenge_id, challenge_name, challenge_description) VALUES (NULL, 'Connect 4', 'Connect four of your checkers in a row while preventing your opponent from doing the same. But, look out -- your opponent can sneak up on you and win the game!'); 
