package com.example.yoel_beta.models

data class User(
//    private var userId:String,
    private var email: String,
    private var pass: String,
    private var exp: Int,
){
    // Конструктор
    init {
        this.email = email
        this.pass = pass
        this.exp = exp
    }
    // Геттер для свойства email
    fun getEmail(): String {
        return email
    }

    // Сеттер для свойства email
    fun setEmail(newEmail: String) {
        email = newEmail
    }

    // Геттер для свойства pass
    fun getPass(): String {
        return pass
    }

    // Сеттер для свойства pass
    fun setPass(newPass: String) {
        pass = newPass
    }

    // Геттер для свойства exp
    fun getExp(): Int {
        return exp
    }

    // Сеттер для свойства exp
    fun setExp(newExp: Int) {
        exp = newExp
    }
}