package com.o7services.spark.model

import java.util.ArrayList

object DiaryContent {

    val ITEMS: MutableList<DiaryItem> = ArrayList()

    data class DiaryItem(var date: String, var description : String, var color : String)
}
