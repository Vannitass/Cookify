package com.example.pageforregister

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//для корректного обрабатывания пустое значения factory в методе стоит знак ?
abstract class DbHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, "app", factory, 1){

    override fun onCreate(db: SQLiteDatabase?) { // создание базы данных
        val query = "CREATE TABLE users (id INT PRIMARY KEY, login TEXT, email TEXT, pass TEXT)"
        db!!.execSQL(query)   // !!. корректное обрабатывание значения NULL
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) { // пересоздание всей базы данных
        db!!.execSQL("DROP TABLE IF EXISTS users") // очищаем базу данных
        onCreate(db) //пересоздание базы данных
    }

    fun addUser(user: User) { // добавление нового пользователя в таблицу users
        val values = ContentValues()
        values.put("login", user.login)
        values.put("email", user.email)
        values.put("pass", user.pass)

        val db = this.writableDatabase
        db.insert("users", null, values)

        db.close()
    }

    fun getUser(login: String, pass: String): Boolean {
        val db = this.readableDatabase

        val result = db.rawQuery("SELECT * FROM users WHERE login = '$login' AND pass = '$pass'", null)
        return result.moveToFirst()
    }

    fun getAllPosts(): List<Item> {
        val posts = mutableListOf<Item>()
        val db = readableDatabase
        val cursor = db.query("dish_posts", null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                // Извлекаем данные из курсора и добавляем в список posts
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return posts
    }

}