package com.o7services.spark.prefrences

import android.content.Context
import android.content.SharedPreferences

class MySharedPrefrences {

    lateinit var sharedPrefrences : SharedPreferences

    constructor(context: Context){
        sharedPrefrences = context.getSharedPreferences("prefrence",Context.MODE_PRIVATE)
    }

    fun setArchivePassword(password : String){
        val editor = sharedPrefrences.edit()
        editor.putString("password",password)
        editor.apply()
    }

    fun getArchivePassword() : String {
        return sharedPrefrences.getString("password","")
    }

    fun setDiaryPassword(password: String){
        val editor = sharedPrefrences.edit()
        editor.putString("diaryPassword",password)
        editor.apply()
    }

    fun getDiaryPassword(): String{
        return sharedPrefrences.getString("diaryPassword","")
    }
}
