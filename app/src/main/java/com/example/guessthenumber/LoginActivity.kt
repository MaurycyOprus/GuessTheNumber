package com.example.guessthenumber

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        val leaderboard = findViewById<Button>(R.id.leaderboard)
        val login = findViewById<Button>(R.id.login)
        val switchReg = findViewById<SwitchCompat>(R.id.switch_reg)
        val register = findViewById<Button>(R.id.register_button)
        val loginEntry = findViewById<EditText>(R.id.loginEntry)
        val passwordEntry = findViewById<EditText>(R.id.passwordEntry)

        val db = DBHelper(this)

        login.visibility = View.VISIBLE
        register.visibility = View.INVISIBLE


        register.setOnClickListener {
            val user = User(
                bestScore = 0,
                username = loginEntry.text.toString(),
                password = passwordEntry.text.toString()
            )
            if (loginEntry.text.isEmpty() || passwordEntry.text.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Fill all empty spaces",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                if (db.addUser(user)) {
                    Toast.makeText(
                        applicationContext,
                        "Successful Registration",
                        Toast.LENGTH_LONG
                    ).show()
                    loginEntry.text.clear()
                    passwordEntry.text.clear()
                    switchReg.isChecked = false
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Username already taken",
                        Toast.LENGTH_LONG
                    ).show()
                    passwordEntry.text.clear()
                }
            }
        }

        leaderboard.setOnClickListener {
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
        }

        login.setOnClickListener{
            val user = User(
                bestScore = 0,
                username = loginEntry.text.toString(),
                password = passwordEntry.text.toString()
            )
            if(db.findUser(user)){
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra("loginShow", user.username)
                startActivity(intent)
                onStop()
            }
            else{
                Toast.makeText(
                    applicationContext,
                    "Wrong username or password",
                    Toast.LENGTH_LONG
                ).show()
            }

        }

        switchReg.setOnCheckedChangeListener{_, isChecked ->
            if (isChecked){
                login.visibility = View.INVISIBLE
                register.visibility = View.VISIBLE
            }else{
                login.visibility = View.VISIBLE
                register.visibility = View.INVISIBLE
            }

        }
    }


}