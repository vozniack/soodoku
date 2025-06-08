/* Schema */

CREATE TABLE users
(
    id       UUID         NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),

    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255)
);

CREATE TABLE games
(
    id            UUID         NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),

    initial_board VARCHAR(255) NOT NULL,
    current_board VARCHAR(255) NOT NULL,
    locks         VARCHAR(255) NOT NULL,

    difficulty    VARCHAR(64)  NOT NULL,

    created_at    TIMESTAMP    NOT NULL             DEFAULT now(),
    updated_at    TIMESTAMP    NULL,
    finished_at   TIMESTAMP    NULL,

    user_id       UUID         NULL,

    CONSTRAINT fk_games_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE moves
(
    id         UUID      NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),

    game_id    UUID      NOT NULL,
    created_at TIMESTAMP NOT NULL             DEFAULT now(),

    row        INT       NOT NULL,
    col        INT       NOT NULL,

    before     INT       NOT NULL,
    after      INT       NOT NULL,

    CONSTRAINT fk_moves_game FOREIGN KEY (game_id) REFERENCES games (id)
);

INSERT INTO users (email, password)
VALUES ('admin@soodoku.dev', '$2y$10$YVNlvW0m/Iug.tWQ28ibpOBZ3XoN0oPpRG.HrrGQv./WU6WdG5tnO');
