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
import com.o7services.spark.NoteActivity
import com.o7services.spark.R
import com.o7services.spark.database.DatabaseQueries
import com.o7services.spark.model.NoteContent


import com.o7services.spark.model.NoteContent.NoteItem

import kotlinx.android.synthetic.main.fragment_note.view.*
class NoteAdapter(
    private val mValues: List<NoteItem>, var activity : FragmentActivity?
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_note, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
            holder.mCardView.setCardBackgroundColor(Color.parseColor(getColorHex(NoteContent.ITEMS[position].color)))
            holder.mTitleTextView.setText(NoteContent.ITEMS[position].title)
            holder.mDescriptionTextView.setText(NoteContent.ITEMS[position].description)

        holder.mMainLinearLayout.setOnClickListener(View.OnClickListener {
            var i = Intent(activity,NoteActivity::class.java)
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
                    .setNegativeButton("Yes",DialogInterface.OnClickListener { dialog, which ->
                        var db = DatabaseQueries(activity!!.baseContext)
                        db.deleteNote(NoteContent.ITEMS[position])
                        NoteContent.ITEMS.removeAt(position)
                        notifyDataSetChanged()
                    })
                    .show()
                return true
            }
        })


    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mMainLinearLayout : LinearLayout = mView.nListMainLinearLayout
        val mTitleTextView : TextView = mView.nListTitleTextView
        val mDescriptionTextView : TextView = mView.nListDescriptionTextView
        val mInnerLinearLayout : LinearLayout = mView.nListInnerLinearLayout
        val mCardView : CardView = mView.nListCardView

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
