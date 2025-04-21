package com.sycodes.careerbot.data

import android.content.Context

class SharedPreferencesHelper(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    fun saveUserData(name: String, email: String, profession: String, age: Int) {
        sharedPreferences.edit().apply {
            putString("name", name)
            putString("email", email)
            putString("profession", profession)
            putInt("age", age)
        }.apply()
    }

    fun getUserData(): Map<String, Any> {
        return mapOf(
            "name" to sharedPreferences.getString("name", "")!!,
            "email" to sharedPreferences.getString("email", "")!!,
            "profession" to sharedPreferences.getString("profession", "")!!,
            "age" to sharedPreferences.getInt("age", 0))
    }

    fun getUserName() : String{
        return sharedPreferences.getString("name", "")!!
    }

    fun saveFirstTimeUser(){
        sharedPreferences.edit().apply {
            putBoolean("firstTimeUser", false)
        }.apply()
    }

    fun isFirstTimeUser(): Boolean {
        return sharedPreferences.getBoolean("firstTimeUser", true)
    }
}