package com.o7services.spark.adapter

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import com.o7services.spark.R
import com.o7services.spark.database.DatabaseQueries
import com.o7services.spark.DiaryActivity
import com.o7services.spark.model.DiaryContent


import com.o7services.spark.model.DiaryContent.DiaryItem
import kotlinx.android.synthetic.main.fragment_diary.view.*

class DiaryAdapter(
    private val mValues: List<DiaryItem>, val activity : FragmentActivity?
) : RecyclerView.Adapter<DiaryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_diary, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mCardView.setCardBackgroundColor(Color.parseColor(getColorHex(DiaryContent.ITEMS[position].color)))
        holder.mTitleTextView.setText(DiaryContent.ITEMS[position].date)
        holder.mDescriptionTextView.setText(DiaryContent.ITEMS[position].description)

        holder.mMainLinearLayout.setOnClickListener(View.OnClickListener {
            var i = Intent(activity, DiaryActivity::class.java)
            i.putExtra("position",position)
            activity!!.startActivity(i)
        })

        holder.mMainLinearLayout.setOnLongClickListener(object : View.OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {

                AlertDialog.Builder(activity)
                    .setTitle("Alert")
                    .setIcon(R.drawable.ic_delete_grey)
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("No",null)
                    .setNegativeButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                        var db = DatabaseQueries(activity!!.baseContext)
                        db.deleteDiary(DiaryContent.ITEMS[position])
                        DiaryContent.ITEMS.removeAt(position)
                        notifyDataSetChanged()
                    })
                    .show()
                return true
            }
        })


    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mMainLinearLayout : LinearLayout = mView.dListMainLinearLayout
        val mTitleTextView : TextView = mView.dListTitleTextView
        val mDescriptionTextView : TextView = mView.dListDescriptionTextView
        val mInnerLinearLayout : LinearLayout = mView.dListInnerLinearLayout
        val mCardView : CardView = mView.dListCardView

    }


    fun getColorHex(color : String) : String {
        return when(color){
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
