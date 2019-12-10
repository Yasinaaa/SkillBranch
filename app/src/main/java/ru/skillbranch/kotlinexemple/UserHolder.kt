package ru.skillbranch.kotlinexemple

import ru.skillbranch.kotlinexemple.User
import java.util.regex.Pattern

object UserHolder {
    private val map = mutableMapOf<String, User>()

    fun registerUser(
        fullName : String,
        email : String,
        password : String
    ) : User {
        return User.makeUser(fullName, email = email, password = password)
            .also { user ->
                if (!map.contains(user.login)) map[user.login] = user
                else throw IllegalArgumentException("A user with this email already exists")
            }
    }

    fun clear(){
        map.clear()
    }

    fun registerUserByPhone(
        fullName : String,
        rawPhone : String
    ) : User {
        return User.makeUser(fullName, phone = rawPhone)
            .also { user ->
                if (Pattern.matches("^\\+\\d{11}$", user.getPhone())) {
                    for ((_,el) in map) {
                        if (el.getPhone().equals(user.getPhone()))
                            throw IllegalArgumentException("A user with this phone already exists")
                    }
                    map[user.login] = user
                }
                else throw IllegalArgumentException("Enter a valid phone number starting" +
                        " with a + and containing 11 digits")

            }
    }


    fun loginUser (login:String, password:String) : String? {
        return map[login.trim().replace("[ ()-]".toRegex(), "")]?.run {
            if (checkPassword(password)) this.userInfo
            else null
        }
    }

    fun requestAccessCode(login: String) {
        map[login.trim().replace("[ ()-]".toRegex(), "")]?.run {
            this.changePassword(this.accessCode!!, this.generateAccessCode())
        }
    }

}