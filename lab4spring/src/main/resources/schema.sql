
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    point INT NOT NULL DEFAULT 1000
);

CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS users_roles (
    user_id BIGINT NOT NULL,
    roles_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, roles_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (roles_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS teams (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    stadium VARCHAR(255),
    city VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS matches (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    team1_id BIGINT NOT NULL,
    team2_id BIGINT NOT NULL,
    team1_score SMALLINT DEFAULT 0,
    team2_score SMALLINT DEFAULT 0,
    FOREIGN KEY (team1_id) REFERENCES teams(id),
    FOREIGN KEY (team2_id) REFERENCES teams(id)
);

CREATE TABLE IF NOT EXISTS bets (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    match_id BIGINT NOT NULL,
    team_id BIGINT NOT NULL,
    point INT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (match_id) REFERENCES matches(id),
    FOREIGN KEY (team_id) REFERENCES teams(id)
);



