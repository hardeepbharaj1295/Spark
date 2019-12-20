package com.o7services.spark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.o7services.spark.fragment.ToDoListItemFragment

class ToDoActivity : AppCompatActivity() {

    lateinit var fm : FragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do)

        supportActionBar!!.hide()

        fm = supportFragmentManager

        var position = intent.getIntExtra("position", 999999)
        val bundle : Bundle = Bundle()
        bundle.putInt("position",position)
        val fragClass = ToDoListItemFragment::class.java
        var frag = fragClass.newInstance()
        frag.arguments = bundle
        fm.beginTransaction().replace(R.id.todo_main_layout,frag).commit()
    }
}
