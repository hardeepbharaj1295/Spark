package com.o7services.spark.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.o7services.spark.R
import com.o7services.spark.adapter.ToDoListItemAdapter
import com.o7services.spark.database.DatabaseQueries
import com.o7services.spark.model.ToDoContent

import com.o7services.spark.model.ToDoListItemContent
import com.google.gson.GsonBuilder

class ToDoListItemFragment : Fragment() {


    lateinit var mainConstraintLayout: ConstraintLayout
    lateinit var colorImageView: ImageView
    lateinit var deleteImageView: ImageView
    lateinit var titleEditText: EditText
    lateinit var backPressImageView: ImageView
    lateinit var addItemImageView: ImageView
    lateinit var addItemTextView: TextView
    lateinit var recyclerView: RecyclerView

    lateinit var backgroundColor: String

    lateinit var myAdapter : ToDoListItemAdapter

    var position = 999999

        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_todolistitem_list, container, false)

        mainConstraintLayout = view.findViewById(R.id.toDoMainConstraintLayout)
        colorImageView = view.findViewById(R.id.toDoColorImageView)
        deleteImageView = view.findViewById(R.id.toDoDeleteImageView)
        titleEditText = view.findViewById(R.id.todoTitleEditText)
        backPressImageView = view.findViewById(R.id.toDoBackPressImageView)
        addItemImageView = view.findViewById(R.id.toDoAddItemImageView)
        addItemTextView = view.findViewById(R.id.toDoAdditemTextView)
        recyclerView = view.findViewById(R.id.toDoListRecyclerView)


        recyclerView.layoutManager = LinearLayoutManager(context)


        position = arguments!!.getInt("position",999999)
        if (position == 999999) {
            backgroundColor = "Default Color"
        } else {
            backgroundColor = ToDoContent.ITEMS[position].color
            mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(ToDoContent.ITEMS[position].color)))
            titleEditText.setText(ToDoContent.ITEMS[position].title)
         }

        populateList(position)

        myAdapter = ToDoListItemAdapter(ToDoListItemContent.ITEMS)
        recyclerView.adapter = myAdapter

        colorImageView.setOnClickListener(View.OnClickListener {

            val dialog = Dialog(activity)
            dialog.setContentView(R.layout.pick_color_dialog)
            dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val whiteLinearLayout = dialog.findViewById(R.id.whiteLinearLayout) as LinearLayout
            val frolyLinearLayout = dialog.findViewById(R.id.frolyLinearLayout) as LinearLayout
            val bananaLinearLayout = dialog.findViewById(R.id.bananaLinearLayout) as LinearLayout
            val parisDaisyLinearLayout = dialog.findViewById(R.id.parisDaisyLinearLayout) as LinearLayout
            val reefLinearLayout = dialog.findViewById(R.id.reefLinearLayout) as LinearLayout
            val aeroBlueLinearLayout = dialog.findViewById(R.id.aeroBlueLinearLayout) as LinearLayout
            val hummingBirdLinearLayout = dialog.findViewById(R.id.hummingBirdLinearLayout) as LinearLayout
            val sailLinearLayout = dialog.findViewById(R.id.sailLinearLayout) as LinearLayout
            val mauveLinearLayout = dialog.findViewById(R.id.mauveLinearLayout) as LinearLayout
            val lotusPinkLinearLayout = dialog.findViewById(R.id.lotusPinkLinearLayout) as LinearLayout
            val cashmereLinearLayout = dialog.findViewById(R.id.cashmereLinearLayout) as LinearLayout
            val athensGrayLinearLayout = dialog.findViewById(R.id.athensGrayLinearLayout) as LinearLayout

            whiteLinearLayout.setOnClickListener {
                backgroundColor = "Default Color"
                mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                dialog .dismiss()
            }
            frolyLinearLayout.setOnClickListener {
                backgroundColor = "Froly"
                mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                dialog .dismiss()
            }
            bananaLinearLayout.setOnClickListener {
                backgroundColor = "Banana"
                mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                dialog .dismiss()
            }
            parisDaisyLinearLayout.setOnClickListener {
                backgroundColor = "Paris Daisy"
                mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                dialog .dismiss()
            }
            reefLinearLayout.setOnClickListener {
                backgroundColor = "Reef"
                mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                dialog .dismiss()
            }
            aeroBlueLinearLayout.setOnClickListener {
                backgroundColor = "Aero Blue"
                mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                dialog .dismiss()
            }
            hummingBirdLinearLayout.setOnClickListener {
                backgroundColor = "Humming Bird"
                mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                dialog .dismiss()
            }
            sailLinearLayout.setOnClickListener {
                backgroundColor = "Sail"
                mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                dialog .dismiss()
            }
            mauveLinearLayout.setOnClickListener {
                backgroundColor = "Mauve"
                mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                dialog .dismiss()
            }
            lotusPinkLinearLayout.setOnClickListener {
                backgroundColor = "Lotus Pink"
                mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                dialog .dismiss()
            }
            cashmereLinearLayout.setOnClickListener {
                backgroundColor = "Cashmere"
                mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                dialog .dismiss()
            }
            athensGrayLinearLayout.setOnClickListener {
                backgroundColor = "Athens Gray"
                mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                dialog .dismiss()
            }
            dialog .show()
        })

        deleteImageView.setOnClickListener(View.OnClickListener {
            if(position == 999999){
                Toast.makeText(activity,"List Discarded",Toast.LENGTH_SHORT).show()
                activity!!.finish()
            }else {
                var db = DatabaseQueries(activity!!.baseContext)
                val gson = GsonBuilder().setPrettyPrinting().create()
                var nameListJson : String = gson.toJson(ToDoContent.ITEMS[position].nameList)
                var checkedListJson : String = gson.toJson(ToDoContent.ITEMS[position].checkedList)
                var result = db.deleteToDo(ToDoContent.ITEMS[position].title,nameListJson,checkedListJson, ToDoContent.ITEMS[position].color)
                activity!!.finish()
            }
            })

        backPressImageView.setOnClickListener(View.OnClickListener {
            var isListEmpty : Boolean = false
            var isTitleEmpty : Boolean = false

            if(titleEditText.text.toString().equals(""))
                isTitleEmpty = true

            if(ToDoListItemContent.ITEMS.size == 0)
                isListEmpty = true

            if(ToDoListItemContent.ITEMS.size != 0){
                var set = false
                for(i in 0..ToDoListItemContent.ITEMS.size-1)
                {
                    if(!ToDoListItemContent.ITEMS[i].nameItem.equals(""))
                        set = true
                }
                isListEmpty = !set

            }




            if(isListEmpty && isTitleEmpty){
                Toast.makeText(activity,"List Discarded", Toast.LENGTH_SHORT).show()
                activity!!.finish()
            } else if (!isListEmpty && isTitleEmpty){
                titleEditText.setError("Title Required")
            } else if(isListEmpty && !isTitleEmpty){
                Toast.makeText(activity,"Add list items to save", Toast.LENGTH_SHORT).show()
            }
            else if(!isListEmpty && !isTitleEmpty){
                val nameList : MutableList<String> = ArrayList()
                val checkedList : MutableList<String> = ArrayList()
                for(item in ToDoListItemContent.ITEMS){
                    if(item.nameItem != ""){
                        nameList.add(item.nameItem)
                        checkedList.add(item.checkedList)
                    }
                }
                var db = DatabaseQueries(activity!!.baseContext)
                val gson = GsonBuilder().setPrettyPrinting().create()
                var nameListJson : String = gson.toJson(nameList)
                var checkedListJson : String = gson.toJson(checkedList)
                if(position == 999999){
                    var result = db.insertToDo(titleEditText.text.toString(),nameListJson,checkedListJson,backgroundColor)
                    if(result >= 0){
//                        Toast.makeText(activity,"List Saved", Toast.LENGTH_SHORT).show()
                        activity!!.finish()
                    } else {
                        Toast.makeText(activity,"Error Occurred", Toast.LENGTH_SHORT).show()
                        activity!!.finish()
                    }
                } else {
                    var oldNameListJson : String = gson.toJson(ToDoContent.ITEMS[position].nameList)
                    var oldCheckedListJson : String = gson.toJson(ToDoContent.ITEMS[position].checkedList)
                    var result = db.updateToDo(ToDoContent.ITEMS[position].title,oldNameListJson,oldCheckedListJson,ToDoContent.ITEMS[position].color,
                        titleEditText.text.toString(),nameListJson,checkedListJson,backgroundColor)
                    if(result >= 0){
//                        Toast.makeText(activity,"List Updated", Toast.LENGTH_SHORT).show()
                        activity!!.finish()
                    } else {
                        Toast.makeText(activity,"Error Occurred", Toast.LENGTH_SHORT).show()
                        activity!!.finish()
                    }
                }

            }
        })

            addItemTextView.setOnClickListener(View.OnClickListener {
//                var s : String = ""
//                var int = 0
//                for(i in 0..ToDoListItemContent.ITEMS.size-1){
//                    s += "\n${ToDoListItemContent.ITEMS[i].nameItem}"
//                    int++
//                }
//
//                Toast.makeText(activity,"$s",Toast.LENGTH_SHORT).show()
                ToDoListItemContent.ITEMS.add(ToDoListItemContent.ToDoListItemContentItem("","false"))
                myAdapter = ToDoListItemAdapter(ToDoListItemContent.ITEMS)
                recyclerView.adapter = myAdapter
                //error notify 6th time
            })

            addItemImageView.setOnClickListener(View.OnClickListener {
                ToDoListItemContent.ITEMS.add(ToDoListItemContent.ToDoListItemContentItem("","false"))
                myAdapter = ToDoListItemAdapter(ToDoListItemContent.ITEMS)
                recyclerView.adapter = myAdapter
            })


        return view


    }

    override fun onResume() {
        super.onResume()
        populateList(position)
        myAdapter.notifyDataSetChanged()
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



    fun populateList(position : Int){
        ToDoListItemContent.ITEMS.clear()
        if(position != 999999){
            for (i in 0..ToDoContent.ITEMS[position].nameList.size-1)
                ToDoListItemContent.ITEMS.add(ToDoListItemContent.ToDoListItemContentItem(ToDoContent.ITEMS[position].nameList[i]
                    ,ToDoContent.ITEMS[position].checkedList[i]))

        }
    }

}
