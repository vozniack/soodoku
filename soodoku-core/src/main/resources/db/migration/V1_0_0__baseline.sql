/* Schema */

/* User related tables */

CREATE TABLE users
(
    id       UUID         NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),

    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),

    username VARCHAR(255) NOT NULL,
    language VARCHAR(5)   NOT NULL,
    theme    VARCHAR(16)  NOT NULL
);

/* Friend related tables */

CREATE TABLE friends
(
    id        UUID PRIMARY KEY   DEFAULT gen_random_uuid(),

    user_id   UUID      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    friend_id UUID      NOT NULL REFERENCES users (id) ON DELETE CASCADE,

    since     TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT uq_user_friend UNIQUE (user_id, friend_id)
);

CREATE TABLE friend_invitations
(
    id           UUID PRIMARY KEY     DEFAULT gen_random_uuid(),

    sender_id    UUID        NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    receiver_id  UUID        NOT NULL REFERENCES users (id) ON DELETE CASCADE,

    status       VARCHAR(16) NOT NULL,

    created_at   TIMESTAMP   NOT NULL DEFAULT now(),
    responded_at TIMESTAMP   NULL,

    CONSTRAINT uq_friend_invitation UNIQUE (sender_id, receiver_id)
);

/* Game related tables */

CREATE TABLE games
(
    id            UUID         NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),

    type          VARCHAR(16)  NOT NULL,
    difficulty    VARCHAR(16)  NOT NULL,

    initial_board VARCHAR(128) NOT NULL,
    solved_board  VARCHAR(128) NOT NULL,
    current_board VARCHAR(128) NOT NULL,
    locks         VARCHAR(512) NOT NULL,
    notes         VARCHAR(2048),

    hints         INT          NOT NULL,

    started_at    TIMESTAMP    NOT NULL             DEFAULT now(),
    updated_at    TIMESTAMP    NULL,
    finished_at   TIMESTAMP    NULL,

    user_id       UUID         NULL REFERENCES users (id)
);

CREATE TABLE game_moves
(
    id          UUID        NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),

    game_id     UUID        NOT NULL REFERENCES games (id),
    type        VARCHAR(16) NOT NULL,

    created_at  TIMESTAMP   NOT NULL             DEFAULT now(),
    reverted_at TIMESTAMP   NULL,

    row_index   INT         NOT NULL,
    col_index   INT         NOT NULL,

    before      INT         NOT NULL,
    after       INT         NOT NULL
);

CREATE TABLE game_sessions
(
    id         UUID      NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),

    game_id    UUID      NOT NULL REFERENCES games (id) ON DELETE CASCADE,
    started_at TIMESTAMP NOT NULL             DEFAULT now(),
    paused_at  TIMESTAMP NULL
);

CREATE TABLE game_history
(
    id            UUID        NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),

    game_id       UUID        NOT NULL UNIQUE REFERENCES games (id),
    user_id       UUID        NOT NULL REFERENCES users (id),

    type          VARCHAR(16) NOT NULL,
    difficulty    VARCHAR(16) NOT NULL,

    duration      BIGINT      NOT NULL,

    missing_cells INT         NOT NULL,
    total_moves   INT         NOT NULL,
    used_hints    INT         NOT NULL,

    victory       BOOLEAN     NOT NULL,

    started_at    TIMESTAMP   NOT NULL,
    finished_at   TIMESTAMP   NOT NULL
);

CREATE INDEX idx_game_sessions_game_id ON game_sessions (game_id);

INSERT INTO users (email, password, username, language, theme)
VALUES ('rajeshkootrappali@bbt.com',
        '$2y$10$YVNlvW0m/Iug.tWQ28ibpOBZ3XoN0oPpRG.HrrGQv./WU6WdG5tnO',
        'koothrappali9000', 'en_EN', 'light');
