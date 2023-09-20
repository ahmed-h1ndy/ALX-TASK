package com.ahmed.myapplication

class Account {

     var id: Int = 1

    var name: String = "ahmed"

    var email:String = "ahmed@gmail.com"

    var balance:Int = 1

    constructor(id:Int, name: String, email: String, balance: Int) {
        this.id = id
        this.name = name
        this.email = email
        this.balance = balance
    }

    constructor()


}