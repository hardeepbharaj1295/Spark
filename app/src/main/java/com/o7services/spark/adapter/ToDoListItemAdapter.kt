package com.o7services.spark.adapter

import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import com.o7services.spark.R
import com.o7services.spark.model.ToDoListItemContent


import com.o7services.spark.model.ToDoListItemContent.ToDoListItemContentItem
import kotlinx.android.synthetic.main.fragment_todolistitem.view.*

class ToDoListItemAdapter(
    private val mValues: List<ToDoListItemContentItem>
) : RecyclerView.Adapter<ToDoListItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_todolistitem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mCheckBox.isChecked = item.checkedList.toBoolean()
        holder.mEditText.setText(item.nameItem)

        holder.mCheckBox.setOnClickListener(View.OnClickListener {
            if (holder.mCheckBox.isChecked){
                ToDoListItemContent.ITEMS[position].checkedList = "true"
            }

             else {
                ToDoListItemContent.ITEMS[position].checkedList = "false"
            }

        })

        holder.mEditText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                ToDoListItemContent.ITEMS[position].nameItem = holder.mEditText.text.toString()

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //do nothing
            }

        } )



    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        val mCheckBox : CheckBox = mView.tdcheckBox
        val mEditText : EditText = mView.tdeditText

    }
}
