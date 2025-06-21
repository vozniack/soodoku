/* Schema */

CREATE TABLE users
(
    id       UUID         NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),

    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),

    username VARCHAR(255) NOT NULL,
    language VARCHAR(5)   NOT NULL,
    theme    VARCHAR(16)  NOT NULL
);

CREATE TABLE games
(
    id            UUID         NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),

    initial_board VARCHAR(128) NOT NULL,
    solved_board  VARCHAR(128) NOT NULL,
    current_board VARCHAR(128) NOT NULL,
    locks         VARCHAR(512) NOT NULL,
    notes         VARCHAR(2048),

    difficulty    VARCHAR(16)  NOT NULL,

    hints         INT          NOT NULL,

    created_at    TIMESTAMP    NOT NULL             DEFAULT now(),
    updated_at    TIMESTAMP    NULL,
    finished_at   TIMESTAMP    NULL,

    user_id       UUID         NULL,

    CONSTRAINT fk_games_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE moves
(
    id          UUID        NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),

    game_id     UUID        NOT NULL,
    type        VARCHAR(16) NOT NULL,

    created_at  TIMESTAMP   NOT NULL             DEFAULT now(),
    reverted_at TIMESTAMP   NULL,

    row_index   INT         NOT NULL,
    col_index   INT         NOT NULL,

    before      INT         NOT NULL,
    after       INT         NOT NULL,

    CONSTRAINT fk_moves_game FOREIGN KEY (game_id) REFERENCES games (id)
);

CREATE TABLE game_summaries
(
    id            UUID        NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),

    game_id       UUID        NOT NULL UNIQUE,
    user_id       UUID        NOT NULL,

    difficulty    VARCHAR(16) NOT NULL,

    duration      BIGINT      NOT NULL,

    missing_cells INT         NOT NULL,
    total_moves   INT         NOT NULL,
    used_hints    INT         NOT NULL,

    victory       BOOLEAN     NOT NULL,

    CONSTRAINT fk_game_summaries_game FOREIGN KEY (game_id) REFERENCES games (id),
    CONSTRAINT fk_game_summaries_user FOREIGN KEY (user_id) REFERENCES users (id)
);

INSERT INTO users (email, password, username, language, theme)
VALUES ('rajeshkootrappali@bbt.com',
        '$2y$10$YVNlvW0m/Iug.tWQ28ibpOBZ3XoN0oPpRG.HrrGQv./WU6WdG5tnO',
        'koothrappali9000', 'en_EN', 'light');
