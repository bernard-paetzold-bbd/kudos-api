CREATE TABLE [users] (
    [user_id] int PRIMARY KEY IDENTITY(1,1),
    [is_admin] bit NOT NULL,
    [username] varchar
)

CREATE TABLE [kudos] (
    [kudos_id] int PRIMARY KEY IDENTITY(1,1),
    [message] varchar(250) NOT NULL,
    [sending_user_id] int NOT NULL FOREIGN KEY REFERENCES users(user_id),
    [target_user_id] int NOT NULL FOREIGN KEY REFERENCES users(user_id),
    [created_at] datetime NOT NULL,
    [flagged] bit NOT NULL
)

CREATE TABLE [team] (
    [team_id] int PRIMARY KEY IDENTITY(1,1),
    [name] varchar(50)
)

CREATE TABLE [log] (
    [log_id] int PRIMARY KEY IDENTITY(1,1),
    [acting_user_id] int NOT NULL FOREIGN KEY REFERENCES users(user_id),
    [target_user_id] int NOT NULL FOREIGN KEY REFERENCES users(user_id),
    [kudos_id] int NOT NULL,
    [log_event] int NOT NULL FOREIGN KEY REFERENCES logEvents(event_id)
)

CREATE TABLE [logEvents] (
    [event_id] int PRIMARY KEY IDENTITY(1,1),
    [description] varchar(100) NOT NULL
)