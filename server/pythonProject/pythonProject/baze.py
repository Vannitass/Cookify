import sqlite3

# Создаем или открываем базу данных
conn = sqlite3.connect('app_database.db')

# Создаем объект курсора для выполнения SQL-запросов
cursor = conn.cursor()

# Создаем таблицу с четырьмя полями
cursor.execute('''
CREATE TABLE IF NOT EXISTS my_table (
    id INTEGER PRIMARY KEY,
    loggin TEXT NOT NULL,
    email TEXT NOT NULL,
    address TEXT NOT NULL
)
''')

# Таблица рецептов
cursor.execute('''
CREATE TABLE IF NOT EXISTS recipes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    image_path TEXT NOT NULL,
    content TEXT NOT NULL,
    author TEXT NOT NULL
)
''')

conn.commit()
conn.close()