package com.example.guessthenumber

class User {
    var username : String? = null
    var password : String? = null
    var bestScore : Int = 0

    constructor(){}

    constructor(bestScore:Int, username:String, password:String){
        this.bestScore = bestScore
        this.username = username
        this.password = password
    }

}