package com.example.guessthenumber
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView


class LeaderboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        supportActionBar?.hide()

        val goBack = findViewById<Button>(R.id.go_back)
        val usersList = findViewById<ListView>(R.id.usersRanking)

        val db = DBHelper(this)

        goBack.setOnClickListener {
            super.onBackPressed()
            onStop()
        }

        val getList = db.getAllUsers()
        val listItems = arrayOfNulls<String>(getList.size)

        for(i in getList.indices){
            listItems[i] = getList[i].username + " points: " + getList[i].bestScore
        }

        val adapter = ArrayAdapter(this, R.layout.single_item, listItems)
        usersList.adapter = adapter

    }
}