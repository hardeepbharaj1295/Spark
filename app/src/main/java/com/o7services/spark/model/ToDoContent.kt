package com.o7services.spark.model

import java.util.ArrayList

object ToDoContent {

    val ITEMS: MutableList<ToDoItem> = ArrayList()

    data class ToDoItem(val title: String, val nameList: MutableList<String>, val checkedList: MutableList<String>,val color : String)
}
