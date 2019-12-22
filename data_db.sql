INSERT INTO performia.account_type (id, type) VALUES (1, 'Utilisateur');
INSERT INTO performia.account_type (id, type) VALUES (2, 'Laboratoire');
INSERT INTO performia.account_type (id, type) VALUES (3, 'IA');
INSERT INTO performia.ai (id, name, host, port, type) VALUES (1, 'Random Connect4 Java', 'localhost', 40000, 1);
INSERT INTO performia.ai (id, name, host, port, type) VALUES (2, 'Reflex Java', 'localhost', 40001, 2);
INSERT INTO performia.ai (id, name, host, port, type) VALUES (3, 'Smart Connect4 Java', 'localhost', 40002, 1);
INSERT INTO performia.ai_type (id, type) VALUES (1, 'Connect4');
INSERT INTO performia.ai_type (id, type) VALUES (2, 'Reflex');
INSERT INTO performia.challenge (challenge_id, challenge_name, challenge_description) VALUES (1, 'Connect 4', 'Connect four of your checkers in a row while preventing your opponent from doing the same. But, look out -- your opponent can sneak up on you and win the game!');
INSERT INTO performia.challenge (challenge_id, challenge_name, challenge_description) VALUES (2, 'Reflex', 'First to touch the cell that lights up is the first to win');
INSERT INTO performia.gender (id, gender) VALUES (1, 'Homme');
INSERT INTO performia.gender (id, gender) VALUES (2, 'Femme');
INSERT INTO performia.gender (id, gender) VALUES (3, 'Autre');
INSERT INTO performia.`match` (match_id, id_player_1, id_player_2, id_player_3, id_player_4, match_time, mean_time_player1, mean_time_player2, mean_time_player3, mean_time_player4, id_winner) VALUES (1, 2, 15, null, null, 29, 1, 2, null, null, 2);
INSERT INTO performia.`match` (match_id, id_player_1, id_player_2, id_player_3, id_player_4, match_time, mean_time_player1, mean_time_player2, mean_time_player3, mean_time_player4, id_winner) VALUES (2, 2, 15, null, null, 30, 1, 2, null, null, 2);
INSERT INTO performia.`match` (match_id, id_player_1, id_player_2, id_player_3, id_player_4, match_time, mean_time_player1, mean_time_player2, mean_time_player3, mean_time_player4, id_winner) VALUES (3, 2, 15, null, null, 32, 1, 2, null, null, 2);
INSERT INTO performia.`match` (match_id, id_player_1, id_player_2, id_player_3, id_player_4, match_time, mean_time_player1, mean_time_player2, mean_time_player3, mean_time_player4, id_winner) VALUES (4, 15, 2, null, null, 270, 1, 0, null, null, 2);
INSERT INTO performia.`match` (match_id, id_player_1, id_player_2, id_player_3, id_player_4, match_time, mean_time_player1, mean_time_player2, mean_time_player3, mean_time_player4, id_winner) VALUES (5, 15, 2, null, null, 271, 1, 0, null, null, 2);
INSERT INTO performia.`match` (match_id, id_player_1, id_player_2, id_player_3, id_player_4, match_time, mean_time_player1, mean_time_player2, mean_time_player3, mean_time_player4, id_winner) VALUES (6, 15, 2, null, null, 272, 1, 0, null, null, 2);
INSERT INTO performia.`match` (match_id, id_player_1, id_player_2, id_player_3, id_player_4, match_time, mean_time_player1, mean_time_player2, mean_time_player3, mean_time_player4, id_winner) VALUES (7, 15, 2, null, null, 273, 1, 0, null, null, 2);
INSERT INTO performia.`match` (match_id, id_player_1, id_player_2, id_player_3, id_player_4, match_time, mean_time_player1, mean_time_player2, mean_time_player3, mean_time_player4, id_winner) VALUES (8, 2, 15, null, null, 21, 0, 3, null, null, 2);
INSERT INTO performia.`match` (match_id, id_player_1, id_player_2, id_player_3, id_player_4, match_time, mean_time_player1, mean_time_player2, mean_time_player3, mean_time_player4, id_winner) VALUES (9, 2, 15, null, null, 22, 0, 3, null, null, 2);
INSERT INTO performia.`match` (match_id, id_player_1, id_player_2, id_player_3, id_player_4, match_time, mean_time_player1, mean_time_player2, mean_time_player3, mean_time_player4, id_winner) VALUES (10, 2, 15, null, null, 24, 0, 3, null, null, 2);
INSERT INTO performia.`match` (match_id, id_player_1, id_player_2, id_player_3, id_player_4, match_time, mean_time_player1, mean_time_player2, mean_time_player3, mean_time_player4, id_winner) VALUES (11, 2, 15, null, null, 23, 7, 6, null, null, 2);
INSERT INTO performia.`match` (match_id, id_player_1, id_player_2, id_player_3, id_player_4, match_time, mean_time_player1, mean_time_player2, mean_time_player3, mean_time_player4, id_winner) VALUES (12, 2, 15, null, null, 23, 7, 6, null, null, 2);
INSERT INTO performia.`match` (match_id, id_player_1, id_player_2, id_player_3, id_player_4, match_time, mean_time_player1, mean_time_player2, mean_time_player3, mean_time_player4, id_winner) VALUES (13, 2, 15, null, null, 25, 7, 6, null, null, 2);
INSERT INTO performia.`match` (match_id, id_player_1, id_player_2, id_player_3, id_player_4, match_time, mean_time_player1, mean_time_player2, mean_time_player3, mean_time_player4, id_winner) VALUES (14, 16, 15, null, null, 59, 13, 5, null, null, 15);
INSERT INTO performia.`match` (match_id, id_player_1, id_player_2, id_player_3, id_player_4, match_time, mean_time_player1, mean_time_player2, mean_time_player3, mean_time_player4, id_winner) VALUES (15, 16, 15, null, null, 59, 13, 5, null, null, 15);
INSERT INTO performia.`match` (match_id, id_player_1, id_player_2, id_player_3, id_player_4, match_time, mean_time_player1, mean_time_player2, mean_time_player3, mean_time_player4, id_winner) VALUES (16, 15, 16, null, null, 45, 6, 11, null, null, 15);
INSERT INTO performia.`match` (match_id, id_player_1, id_player_2, id_player_3, id_player_4, match_time, mean_time_player1, mean_time_player2, mean_time_player3, mean_time_player4, id_winner) VALUES (17, 20, 17, null, null, 55, 10, 14, null, null, 17);
INSERT INTO performia.`match` (match_id, id_player_1, id_player_2, id_player_3, id_player_4, match_time, mean_time_player1, mean_time_player2, mean_time_player3, mean_time_player4, id_winner) VALUES (18, 2, 2, null, null, 2, 39, 39, null, null, 2);
INSERT INTO performia.`match` (match_id, id_player_1, id_player_2, id_player_3, id_player_4, match_time, mean_time_player1, mean_time_player2, mean_time_player3, mean_time_player4, id_winner) VALUES (19, 16, 15, null, null, 32, 33, 9, null, null, 16);
INSERT INTO performia.`match` (match_id, id_player_1, id_player_2, id_player_3, id_player_4, match_time, mean_time_player1, mean_time_player2, mean_time_player3, mean_time_player4, id_winner) VALUES (20, 16, 15, null, null, 86, 20, 12, null, null, 15);
INSERT INTO performia.user (id, username, password, birthdate, gender, type, id_labo) VALUES (1, 'user1', '5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8', '2000-06-01', 2, 1, null);
INSERT INTO performia.user (id, username, password, birthdate, gender, type, id_labo) VALUES (2, 'toto', '0b9c2625dc21ef05f6ad4ddf47c5f203837aa32c', '1980-12-12', 1, 1, null); # mdp : toto
INSERT INTO performia.user (id, username, password, birthdate, gender, type, id_labo) VALUES (3, 'ia', 'f30accb48e68b071cb68125f46f669d5522b9ee8', '2019-11-14', null, 3, 16); # mdp : ia
INSERT INTO performia.user (id, username, password, birthdate, gender, type, id_labo) VALUES (4, 'bastien', '7288edd0fc3ffcbe93a0cf06e3568e28521687bc', '2019-11-27', 1, 1, null);
INSERT INTO performia.user (id, username, password, birthdate, gender, type, id_labo) VALUES (11, 'test', 'ab874467a7d1ff5fc71a4ade87dc0e098b458aae', '2019-11-27', 3, 1, null);
INSERT INTO performia.user (id, username, password, birthdate, gender, type, id_labo) VALUES (14, 'Alex', '86f7e437faa5a7fce15d1ddcb9eaeaea377667b8', '1998-10-10', 2, 1, null);
INSERT INTO performia.user (id, username, password, birthdate, gender, type, id_labo) VALUES (15, 'connect4_random', '3bc60f2a3c0961735ff7853dd6137ecbc6214722', '2019-12-08', 3, 3, 16); # mdp : connect4_random
INSERT INTO performia.user (id, username, password, birthdate, gender, type, id_labo) VALUES (16, 'lab1', 'A875574DC2EAD409AE2C142DA1C2AE32437DAE7F', '2019-12-08', 3, 2, null); # mdp : lab1
INSERT INTO performia.user (id, username, password, birthdate, gender, type, id_labo) VALUES (17, 'lab2', '92518BD2AFE62DFCCDD3E0B7F8075A69854A8765', '2019-12-08', 3, 2, null); # mdp : lab2
INSERT INTO performia.user (id, username, password, birthdate, gender, type, id_labo) VALUES (18, 'connect_random_2', '3825026a067067fbec7ab83ecda11b14f5ed8220', '2019-12-08', 3, 3, 17); # mdp : connect_random_2
INSERT INTO performia.user (id, username, password, birthdate, gender, type, id_labo) VALUES (19, 'reflex_random', 'ed0dd07567df7339e20e61a5b2119f5be4433079', '2019-12-08', 3, 3, 16);
INSERT INTO performia.user (id, username, password, birthdate, gender, type, id_labo) VALUES (20, 'connect4_smart', '35774369adda9326e9c110482ab3c3268736ae63', '2019-12-08', 3, 3, 17); # mdp : connect4_smart
