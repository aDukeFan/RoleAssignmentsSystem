DROP TABLE IF EXISTS user_roles, user_ids, role_ids;

CREATE TABLE IF NOT EXISTS user_ids (
    id BIGINT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS role_ids (
    id BIGINT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES user_ids(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role_ids(id) ON DELETE CASCADE
);
