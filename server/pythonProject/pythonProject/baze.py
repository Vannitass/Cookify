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