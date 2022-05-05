package com.example.guessthenumber

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random

class GameActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        supportActionBar?.hide()

        val confirmGuess = findViewById<Button>(R.id.confirmGuess)
        val restartGame = findViewById<ImageButton>(R.id.restartGame)
        val urGuess = findViewById<TextView>(R.id.urGuess)
        val pointsGained = findViewById<TextView>(R.id.pointsGained)
        val numberOfGuesses = findViewById<TextView>(R.id.numberOfGuesses)
        val leaderboard = findViewById<Button>(R.id.leaderboard)
        val logOut = findViewById<Button>(R.id.log_out)
        val loginShow = findViewById<TextView>(R.id.loginShow)

        var drawnNumber = Random.nextInt(0, 20)
        var guessCount = 0
        var pointsCount = 0

        val loggedUser = intent.getStringExtra("loginShow")
        val db = DBHelper(this)


        loginShow.text = loggedUser.toString()

        restartGame.setOnClickListener {
            pointsCount = 0
            pointsGained.text = pointsCount.toString()
            guessCount = 0
            numberOfGuesses.text = guessCount.toString()
            drawnNumber = Random.nextInt(0, 20)
        }

        leaderboard.setOnClickListener {
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
            onPause()
        }

        logOut.setOnClickListener{
            super.onBackPressed()
            finish()
        }

        confirmGuess.setOnClickListener {
            val text = urGuess.text.toString()
            val number = text.toInt()
            if ((number > 20) || (number < 0)) {
                Toast.makeText(
                    applicationContext,
                    "You have to choose number from 0 to 20",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                guessCount += 1
                numberOfGuesses.text = guessCount.toString()
                when {
                    number > drawnNumber -> {
                        Toast.makeText(
                            applicationContext,
                            "Too big",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                    number < drawnNumber -> {
                        Toast.makeText(
                            applicationContext,
                            "Too small",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                    else -> {
                        when {
                            guessCount <= 1 -> {
                                Toast.makeText(
                                    applicationContext,
                                    "Correct! 5 points!",
                                    Toast.LENGTH_LONG
                                ).show()
                                pointsCount += 5
                                pointsGained.text = pointsCount.toString()
                            }
                            guessCount <= 4 -> {
                                Toast.makeText(
                                    applicationContext,
                                    "Correct! 3 points!",
                                    Toast.LENGTH_LONG
                                ).show()
                                pointsCount += 3
                                pointsGained.text = pointsCount.toString()
                            }
                            guessCount <= 6 -> {
                                Toast.makeText(
                                    applicationContext,
                                    "Correct! 2 points!",
                                    Toast.LENGTH_LONG
                                ).show()
                                pointsCount += 2
                                pointsGained.text = pointsCount.toString()
                            }
                            guessCount <= 10 -> {
                                Toast.makeText(
                                    applicationContext,
                                    "Correct! 1 point!",
                                    Toast.LENGTH_LONG
                                ).show()
                                pointsCount += 1
                                pointsGained.text = pointsCount.toString()
                            }
                        }
                        guessCount = 0
                        numberOfGuesses.text = guessCount.toString()
                        drawnNumber = Random.nextInt(0, 20)
                    }
                }
                if (guessCount > 9) {
                    db.updateBestScore(loggedUser.toString(), pointsCount)
                    Toast.makeText(
                        applicationContext,
                        "You lose!",
                        Toast.LENGTH_LONG
                    ).show()
                    pointsCount = 0
                    pointsGained.text = pointsCount.toString()
                    guessCount = 0
                    numberOfGuesses.text = guessCount.toString()
                    drawnNumber = Random.nextInt(0, 20)
                }
            }
        }


    }
}