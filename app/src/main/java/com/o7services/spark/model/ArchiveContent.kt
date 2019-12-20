package com.o7services.spark.model

import java.util.ArrayList

object ArchiveContent {

    val ITEMS: MutableList<ArchiveItem> = ArrayList()

    data class ArchiveItem(val title: String, val description: String, val color: String, val isArchived : Boolean)
}
