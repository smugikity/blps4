INSERT INTO roles(name) VALUES
    ('ADMIN'),
    ('USER');
INSERT INTO users(username,password,point) VALUES
    ('admin','$2a$12$cCyk9E1cmp.RWcP9kImtp.hHpTQ8wnUgp7AzuaHHiDNH8Mxckw7/a',100000),
    ('user','$2a$12$/Dhzk9ACLDbKuswnnL1TkOgJIJT8vOzzGXjvrkVW/Gpc/Sf.6Zm/6',1000);
INSERT INTO users_roles(user_id,roles_id) VALUES
    (1,1),
    (2,2);
INSERT INTO teams(city,name,stadium) VALUES
    ('Milan','Inter Milan','San Siro'),
    ('London','Chelsea','Stamford Bridge'),
    ('Manchester','Manchester United','Old Trafford'),
    ('Barcelona','Barcelona','Camp Nou'),
    ('Madrid','Real Madrid','Santiago Bernabeu'),
    ('Munich','Bayern Munich','Allianz Arena');
INSERT INTO matches(name,team1_id,team2_id,team1_score,team2_score) VALUES
    ('Friendly match 2023',1,2,0,0),
    ('2012 UCL final',2,6,0,0),
    ('2023 EPL matchday 10',2,3,0,0),
    ('2023 UEL semi-final 1',1,4,0,0),
    ('2023 LaLiga matchday 3',4,5,0,0);