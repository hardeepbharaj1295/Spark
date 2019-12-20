package com.o7services.spark.adapter

import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.o7services.spark.EventActivity
import com.o7services.spark.R
import com.o7services.spark.database.DatabaseQueries
import com.o7services.spark.model.EventContent


import com.o7services.spark.model.EventContent.EventItem

import kotlinx.android.synthetic.main.fragment_event.view.*

class EventAdapter(
    private val mValues: List<EventItem>, val activity: FragmentActivity?, val columnCount: Int
) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_event, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]

        with(holder.mView) {
            tag = item
        }

        holder.mOuterConstraintLayout.setOnClickListener(View.OnClickListener {
            var i = Intent(activity, EventActivity::class.java)
            i.putExtra("position", position)
            activity!!.startActivity(i)
        })


        holder.mdeleteImageView.setOnClickListener(View.OnClickListener {
            val db = DatabaseQueries(activity!!.baseContext)
            db.deleteEvent(EventContent.ITEMS[position])
            if (ActivityCompat.checkSelfPermission(
                    activity, android.Manifest.permission.WRITE_CALENDAR
                )
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    activity, android.Manifest.permission.READ_CALENDAR
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(android.Manifest.permission.WRITE_CALENDAR , android.Manifest.permission.READ_CALENDAR),
                    100)
            }
            else
            {
                val CALENDAR_URI = Uri.parse(getCalendarUriBase(activity) + "events")
                val cursors = activity.contentResolver.query(CALENDAR_URI, null, null, null, null)
                if (cursors!!.moveToFirst()) {
                    while (cursors.moveToNext()) {
                        val desc = cursors.getString(cursors.getColumnIndex("description"))
                        val title = cursors.getString(cursors.getColumnIndex("title"))
                        val id = cursors.getString(cursors.getColumnIndex("_id"))
                        if (desc == null && title == null) {
                        } else {
                            if (desc == EventContent.ITEMS[position].description &&
                                title == EventContent.ITEMS[position].title
                            ) {
                                val uri = ContentUris.withAppendedId(
                                    CALENDAR_URI,
                                    Integer.parseInt(id).toLong()
                                )
                                activity.contentResolver.delete(uri, null, null)
                                break
                            }
                        }
                    }
                }
                cursors.close()
                EventContent.ITEMS.removeAt(position)
                notifyDataSetChanged()
            }
        })

        if (columnCount == 1) {
            holder.mInnerContraintLayout.setBackgroundColor(
                Color.parseColor(
                    getColorHex(
                        EventContent.ITEMS[position].color
                    )
                )
            )
            holder.mbackgroundImageView.setImageResource(getBackgroundImageRes(EventContent.ITEMS[position].backgroundImage))
            holder.mtitleTextView.text = EventContent.ITEMS[position].title
            holder.mstartTextView.text =
                "${EventContent.ITEMS[position].startDay}    ${EventContent.ITEMS[position].startTime}"
            holder.mendTextView.text =
                "${EventContent.ITEMS[position].endDay}    ${EventContent.ITEMS[position].endTime}"
        } else {
            holder.mInnerContraintLayout.setBackgroundColor(
                Color.parseColor(
                    getColorHex(
                        EventContent.ITEMS[position].color
                    )
                )
            )
            holder.mbackgroundImageView.setImageResource(getBackgroundImageRes(EventContent.ITEMS[position].backgroundImage))
            holder.mtitleTextView.text = EventContent.ITEMS[position].title
            holder.mstartTextView.text =
                "${EventContent.ITEMS[position].startDay}\n${EventContent.ITEMS[position].startTime}"
            holder.mendTextView.text =
                "${EventContent.ITEMS[position].endDay}\n${EventContent.ITEMS[position].endTime}"
        }


    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var mOuterConstraintLayout: ConstraintLayout = mView.eListOuterConstraintLayout
        var mbackgroundImageView: ImageView = mView.eListBackgroundImageView
        var mtitleTextView: TextView = mView.eListTitleTextView
        var mstartTextView: TextView = mView.eListStartTextView
        var mendTextView: TextView = mView.eListEndTextView
        var mdeleteImageView: ImageView = mView.eListDeleteImageView
        val mInnerContraintLayout: ConstraintLayout = mView.eListInsderConstraintlayout

    }

    private fun getCalendarUriBase(act: Activity): String? {

        var calendarUriBase: String? = null
        var calendars = Uri.parse("content://calendar/calendars")
        var managedCursor: Cursor? = null
        try {
            managedCursor = act.managedQuery(calendars, null, null, null, null)
        } catch (e: Exception) {
        }

        if (managedCursor != null) {
            calendarUriBase = "content://calendar/"
        } else {
            calendars = Uri.parse("content://com.android.calendar/calendars")
            try {
                managedCursor = act.managedQuery(calendars, null, null, null, null)
            } catch (e: Exception) {
            }

            if (managedCursor != null) {
                calendarUriBase = "content://com.android.calendar/"
            }
        }
        return calendarUriBase
    }

    fun getBackgroundImageRes(backgroundImage: String): Int {
        return when (backgroundImage) {
            "Background Image 1" -> R.drawable.card_view_background_0
            "Background Image 2" -> R.drawable.card_view_background_1
            "Background Image 3" -> R.drawable.card_view_background_2
            "Background Image 4" -> R.drawable.card_view_background_3
            "Background Image 5" -> R.drawable.card_view_background_4
            "Background Image 6" -> R.drawable.card_view_background_5
            "Background Image 7" -> R.drawable.card_view_background_6
            "Background Image 8" -> R.drawable.card_view_background_7
            "Background Image 9" -> R.drawable.card_view_background_8
            "Background Image 10" -> R.drawable.card_view_background_9
            "Background Image 11" -> R.drawable.card_view_background_10
            "Background Image 12" -> R.drawable.card_view_background_11
            else -> R.drawable.card_view_background_0
        }
    }

    fun getColorHex(color: String): String {
        return when (color) {
            "Default Color" -> "#ffffff"
            "Froly" -> "#F28B82"
            "Banana" -> "#FBBC04"
            "Paris Daisy" -> "#FFF475"
            "Reef" -> "#CCFF90"
            "Aero Blue" -> "#A7FFEB"
            "Humming Bird" -> "#CBF0F8"
            "Sail" -> "#AECBFA"
            "Mauve" -> "#D7AEFB"
            "Lotus Pink" -> "#FDCFE8"
            "Cashmere" -> "#E6C9A8"
            "Athens Gray" -> "#E8EAED"
            else -> "#ffffff"
        }
    }

}
