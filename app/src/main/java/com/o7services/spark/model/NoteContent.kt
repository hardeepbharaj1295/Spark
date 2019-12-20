package com.o7services.spark.model

import java.util.ArrayList

object NoteContent {

    val ITEMS: MutableList<NoteItem> = ArrayList()

    data class NoteItem(val title: String, val description: String, val color: String, val isArchived : Boolean)
}
