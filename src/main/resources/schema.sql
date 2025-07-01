CREATE TABLE IF NOT EXISTS users (
                                     id INTEGER PRIMARY KEY AUTOINCREMENT,
                                     name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS options (
                                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                                       ticker TEXT,
                                       strike REAL,
                                       expiry TEXT,
                                       type TEXT,
                                       bid REAL,
                                       ask REAL,
                                       iv REAL
);

CREATE TABLE IF NOT EXISTS trades (
                                      id INTEGER PRIMARY KEY AUTOINCREMENT,
                                      user_id INTEGER,
                                      option_id INTEGER,
                                      quantity INTEGER,
                                      price REAL,
                                      direction TEXT, -- 'BUY' or 'SELL'
                                      timestamp TEXT,
                                      FOREIGN KEY(user_id) REFERENCES users(id),
    FOREIGN KEY(option_id) REFERENCES options(id)
    );
