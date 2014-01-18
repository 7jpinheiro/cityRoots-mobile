CREATE TABLE photos (
    _ID INTEGER PRIMARY KEY,
    name TEXT,
    path TEXT
);

CREATE TABLE pois (
    _ID INTEGER PRIMARY KEY,
    remote_id INTEGER,
    name TEXT,
    schedule TEXT,
    language TEXT,
    description TEXT,
    transport TEXT,
    price TEXT,
    site TEXT,
    email TEXT,
    address TEXT,
    latitude REAL,
    longitude REAL,
    is_active BOOLEAN,
    timestamp INTEGER,
    has_accessibility BOOLEAN,
    type_id STRING,
    photos_id INTEGER,
    rating REAL,
    FOREIGN KEY (photos_id) REFERENCES photos(_ID)
);

CREATE TABLE types (
    _ID INTEGER PRIMARY KEY,
    type STRING
);

CREATE TABLE types_pois (
    _ID INTEGER PRIMARY KEY,
    types_id INTEGER,
    pois_id INTEGER,
    FOREIGN KEY (types_id) REFERENCES types(_ID),
    FOREIGN KEY (pois_id) REFERENCES pois(_ID)
)

CREATE TABLE attractions (
    _ID INTEGER PRIMARY KEY,
    is_reference_point BOOLEAN,
    poi_id INTEGER,
    FOREIGN KEY (poi_id) REFERENCES pois(_ID)
);

CREATE TABLE events (
    _ID INTEGER PRIMARY KEY,
    start INTEGER,
    end INTEGER,
    organization TEXT,
    program TEXT,
    poi_id INTEGER,
    FOREIGN KEY (poi_id) REFERENCES pois(_ID)
);

CREATE TABLE services (
    _ID INTEGER PRIMARY KEY,
    is_reference_point BOOLEAN,
    capacity INTEGER,
    details TEXT,
    poi_id INTEGER,
    FOREIGN KEY (poi_id) REFERENCES pois(_ID)
);

CREATE TABLE routes (
    _ID INTEGER PRIMARY KEY,
    name TEXT,
    description TEXT,
);

CREATE TABLE routes_pois (
    _ID INTEGER PRIMARY KEY,
    order INTEGER,
    route_id INTEGER,
    poi_id INTEGER,
    FOREIGN KEY (route_id) REFERENCES routes(_ID),
    FOREIGN KEY (poi_id) REFERENCES pois(_ID)
);

CREATE TABLE comments_pois (
    _ID INTEGER PRIMARY KEY,
    poi_id INTEGER,
    comment_id INTEGER,
    FOREIGN KEY (poi_id) REFERENCES pois(_ID),
    FOREIGN KEY (comment_id) REFERENCES comments(_ID)
);

CREATE TABLE comments (
    _ID INTEGER PRIMARY KEY,
    comment TEXT,
    rating INTEGER,
    timestamp INTEGER,
    user_id INTEGER,
    FOREIGN KEY (user_id) REFERENCES users(_ID)
);

CREATE TABLE users (
    _ID INTEGER PRIMARY KEY,
    remote_id INTEGER,
    firstname TEXT,
    surname TEXT
);