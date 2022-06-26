DROP ALL OBJECTS;

CREATE TABLE IF NOT EXISTS todo (
  id INT PRIMARY KEY ,
  content TEXT,
  done BOOLEAN DEFAULT false,
  created_at DATETIME,
  updated_at DATETIME
);
