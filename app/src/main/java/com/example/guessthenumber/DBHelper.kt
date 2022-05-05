package com.example.guessthenumber

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null, DATABASE_VER) {
    companion object {
        private val DATABASE_VER = 2
        private val DATABASE_NAME = "users_data.db"
        //Table
        private val TABLE_NAME = "users"
        private val COL_USERNAME = "username"
        private val COL_PASSWORD = "password"
        private val COL_BESTSCORE = "bestScore"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY = ("CREATE TABLE $TABLE_NAME ($COL_USERNAME TEXT PRIMARY KEY, $COL_PASSWORD TEXT, $COL_BESTSCORE INTEGER)")
        db!!.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


    fun addUser(user: User): Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        val usernameInput = user.username
        val query = "SELECT * FROM users where username = \"$usernameInput\""
        val cursor = db.rawQuery(query, null)
        if (cursor.count == 0) {
            values.put(COL_USERNAME, user.username)
            values.put(COL_PASSWORD, user.password)
            values.put(COL_BESTSCORE, user.bestScore)

            db.insert("users", null, values)
            db.close()
            return true
        }
        return false
    }


    fun findUser(user: User): Boolean{
        val db = this.writableDatabase
        val usernameInput = user.username
        val query = "SELECT * FROM users where username = \"$usernameInput\""
        val cursor = db.rawQuery(query, null)
        if (cursor.count > 0) {
            cursor.moveToFirst()
            val passwd = cursor.getString(1)
            if (passwd == user.password){
                return true
            }
        }
        return false
    }


    fun getAllUsers(): List<User>{
        val listOfUsers = mutableListOf<User>()
        val selectQuery = "SELECT * FROM $TABLE_NAME ORDER BY bestScore DESC LIMIT 10"
        val db = this.writableDatabase
        val cursor =  db.rawQuery(selectQuery, null)
        if(cursor.moveToFirst()){
            do {
                val user = User()
                user.username = cursor.getString(0)
                user.bestScore = cursor.getInt(2)
                listOfUsers.add(user)
            } while (cursor.moveToNext())
        }
        db.close()
        return listOfUsers
    }


    fun updateBestScore(username: String, res: Int){
        val db = this.writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME where username = \"$username\""
        val cursor =  db.rawQuery(selectQuery, null)
        cursor.moveToFirst()
        val curr = cursor.getInt(2)
        if (res > curr){
            val values = ContentValues()
            values.put(COL_BESTSCORE, res)
            db.update("users", values, "username = ?", arrayOf(username))
        }
    }


}