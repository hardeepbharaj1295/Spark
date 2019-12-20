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
import com.o7services.spark.ToDoActivity
import com.o7services.spark.database.DatabaseQueries
import com.o7services.spark.model.ToDoContent


import com.o7services.spark.model.ToDoContent.ToDoItem
import com.google.gson.GsonBuilder

import kotlinx.android.synthetic.main.fragment_todo.view.*

class ToDoAdapter(
    private val mValues: MutableList<ToDoItem>, val activity : FragmentActivity?
) : RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_todo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]

        holder.mCardView.setCardBackgroundColor(Color.parseColor(getColorHex(item.color)))
        holder.mTitleTextView.text = item.title
        val size = item.nameList.size
        when(size){
            0 -> {
                holder.mDescriptiontextView.visibility = View.GONE
                holder.mItemTextView.visibility = View.GONE
            }
            1 -> {

                holder.mDescriptiontextView.visibility = View.VISIBLE
                holder.mDescriptiontextView.text = item.nameList.get(0)
                holder.mItemTextView.visibility = View.GONE
            }
            2 ->{

                holder.mDescriptiontextView.visibility = View.VISIBLE
                holder.mDescriptiontextView.text = "${item.nameList.get(0)}\n${item.nameList.get(1)}"
                holder.mItemTextView.visibility = View.GONE
            }
            3 ->{
                holder.mDescriptiontextView.visibility = View.VISIBLE
                holder.mDescriptiontextView.text = "${item.nameList.get(0)}\n${item.nameList.get(1)}\n${item.nameList.get(2)}"
                holder.mItemTextView.visibility = View.GONE
            }
            4 ->{
                holder.mDescriptiontextView.visibility = View.VISIBLE
                holder.mItemTextView.visibility = View.VISIBLE
                holder.mDescriptiontextView.text = "${item.nameList.get(0)}\n${item.nameList.get(1)}\n${item.nameList.get(2)}"
                holder.mItemTextView.text = "+1 item"
            }
            else -> {
                holder.mDescriptiontextView.visibility = View.VISIBLE
                holder.mItemTextView.visibility = View.VISIBLE
                holder.mDescriptiontextView.text = "${item.nameList.get(0)}\n${item.nameList.get(1)}\n${item.nameList.get(2)}"
                holder.mItemTextView.text = "+${size - 3} items"
            }
        }

        holder.mMainLinearLayout.setOnClickListener(View.OnClickListener {
            var i = Intent(activity,ToDoActivity::class.java)
            i.putExtra("position",position)
            activity!!.startActivity(i)

        })

        holder.mMainLinearLayout.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                AlertDialog.Builder(activity)
                    .setTitle("Alert")
                    .setIcon(R.drawable.ic_delete_grey)
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("No",null)
                    .setNegativeButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                        var db = DatabaseQueries(activity!!.baseContext)
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        var nameListJson : String = gson.toJson(ToDoContent.ITEMS[position].nameList)
                        var checkedListJson : String = gson.toJson(ToDoContent.ITEMS[position].checkedList)
                        db.deleteToDo(ToDoContent.ITEMS[position].title,nameListJson,checkedListJson,ToDoContent.ITEMS[position].color)
                        ToDoContent.ITEMS.removeAt(position)
                        notifyDataSetChanged()
                    })
                    .show()
                return true

            }
        })
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mMainLinearLayout : LinearLayout = mView.tListMainLinearLayout
        val mCardView : CardView = mView.tListCardView
        val mTitleTextView : TextView = mView.tListTitleTextView
        val mDescriptiontextView = mView.tListDescriptionTextView
        val mItemTextView = mView.tListItemTextView

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
