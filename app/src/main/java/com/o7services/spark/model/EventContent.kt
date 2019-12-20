package com.o7services.spark.model

import java.util.ArrayList

object EventContent {

    val ITEMS: MutableList<EventItem> = ArrayList()

    data class EventItem(var title: String, var startDay: String, var startTime: String,
                         var endDay : String, var endTime : String, var firstReminder : String,
                         var secondReminder : String, var color : String, var description :String,
                         var backgroundImage : String)
}
