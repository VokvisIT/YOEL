package com.example.yoel_beta.models

data class User @JvmOverloads constructor(
    private var username: String = "",
    private var imageurl: String = "",
    private var email: String = "",
    private var pass: String = "",
    private var exp: Int = 0,
){
    // Конструктор
    init {
        this.username = username
        this.imageurl = imageurl
        this.email = email
        this.pass = pass
        this.exp = exp
    }
    // Геттер для свойства Username
    fun getUsername(): String {
        return username
    }
    // Сеттер для свойства Username
    fun setUsername(newUsername: String) {
        username = newUsername
    }
    // Геттер для свойства ImageUrl
    fun getImageUrl(): String {
        return imageurl
    }
    // Сеттер для свойства ImageUrl
    fun setImageUrl(newImageUrl: String) {
        imageurl = newImageUrl
    }
    // Геттер для свойства email
    fun getEmail(): String {
        return email.toString()
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