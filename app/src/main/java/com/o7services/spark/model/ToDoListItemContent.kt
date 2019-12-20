package com.o7services.spark.model

import java.util.ArrayList

object ToDoListItemContent {
    val ITEMS: MutableList<ToDoListItemContentItem> = ArrayList()
    data class ToDoListItemContentItem(var nameItem: String, var checkedList: String)
}
