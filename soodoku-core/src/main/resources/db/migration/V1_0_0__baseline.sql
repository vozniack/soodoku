/* Schema */

CREATE TABLE users
(
    id       UUID         NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),

    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255)
);

INSERT INTO users (email, password)
VALUES ('admin@soodoku.dev', '$2y$10$YVNlvW0m/Iug.tWQ28ibpOBZ3XoN0oPpRG.HrrGQv./WU6WdG5tnO');
