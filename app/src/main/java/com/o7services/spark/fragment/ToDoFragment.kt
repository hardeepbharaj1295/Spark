package com.o7services.spark.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import com.o7services.spark.EventActivity
import com.o7services.spark.NoteActivity
import com.o7services.spark.R
import com.o7services.spark.ToDoActivity
import com.o7services.spark.adapter.ToDoAdapter
import com.o7services.spark.database.DatabaseColumns
import com.o7services.spark.database.DatabaseQueries

import com.o7services.spark.model.ToDoContent
import com.o7services.spark.model.ToDoContent.ToDoItem
import com.google.android.material.navigation.NavigationView
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class ToDoFragment : Fragment() {


    lateinit var drawerOpenerImageView: ImageView
    lateinit var searchEditText: EditText
    lateinit var listSwitcherImageView: ImageView
    lateinit var createToDoTextView: TextView
    lateinit var createNoteImageView: ImageView
    lateinit var createEventImageView: ImageView
    lateinit var recyclerView: RecyclerView
    lateinit var noMatchImageView: ImageView
    lateinit var noMatchTextView: TextView
    lateinit var drawer: DrawerLayout
    lateinit var archiveImageView : ImageView
    lateinit var diaryImageView: ImageView

    private var columnCount = 1
    lateinit var myAdapter: ToDoAdapter

    lateinit var fm: FragmentManager

    lateinit var navView: NavigationView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_todo_list, container, false)

        fm = activity!!.supportFragmentManager
        navView = activity!!.findViewById(R.id.nav_view)

        drawerOpenerImageView = view.findViewById(R.id.toDoDrawerImageView)
        searchEditText = view.findViewById(R.id.toDoSearchEditText)
        listSwitcherImageView = view.findViewById(R.id.toDoListViewSwitchImageView)
        createToDoTextView = view.findViewById(R.id.createToDoTextView)
        createEventImageView = view.findViewById(R.id.toDoCreateEventImageView)
        createNoteImageView = view.findViewById(R.id.toDoCreateNoteImageView)
        recyclerView = view.findViewById(R.id.toDoRecyclerView)
        noMatchImageView = view.findViewById(R.id.toDoNoMatchImageView)
        noMatchTextView = view.findViewById(R.id.toDoNoMatchTextView)
        drawer = activity!!.findViewById(R.id.drawer_layout)
        archiveImageView = view.findViewById(R.id.toDoCreateArchiveImageView)
        diaryImageView = view.findViewById(R.id.toDoCreateDiaryImageView)

        if(arguments != null){
            columnCount = arguments!!.getInt("columnCount",1)
        }


        if (columnCount == 1) {
            recyclerView.layoutManager = LinearLayoutManager(context)
            listSwitcherImageView.setImageResource(R.drawable.ic_list_single)
        } else {
            recyclerView.layoutManager = GridLayoutManager(context, columnCount)
            listSwitcherImageView.setImageResource(R.drawable.ic_list_double)
        }

        populateListFromDB()


        myAdapter = ToDoAdapter(ToDoContent.ITEMS, activity)


        recyclerView.adapter = myAdapter


        drawerOpenerImageView.setOnClickListener(View.OnClickListener {
            drawer.openDrawer(GravityCompat.START)
        })

        listSwitcherImageView.setOnClickListener(View.OnClickListener {
            if(columnCount == 1){
                val bundle : Bundle = Bundle()
                bundle.putInt("columnCount",2)
                val fragClass = ToDoFragment::class.java
                val frag = fragClass.newInstance()
                frag.arguments = bundle
                fm.beginTransaction().replace(R.id.main_layout,frag).commit()


            } else {
                val bundle : Bundle = Bundle()
                bundle.putInt("columnCount",1)
                val fragClass = ToDoFragment::class.java
                val frag = fragClass.newInstance()
                frag.arguments = bundle
                fm.beginTransaction().replace(R.id.main_layout,frag).commit()

            }
        })

        createToDoTextView.setOnClickListener(View.OnClickListener {

            var i = Intent(activity, ToDoActivity::class.java)
            i.putExtra("position", 999999)
            startActivity(i)
        } )

        createNoteImageView.setOnClickListener(View.OnClickListener {
            fm.beginTransaction().replace(R.id.main_layout,NoteFragment()).commit()
            navView.setCheckedItem(R.id.nav_note)
            var i = Intent(activity, NoteActivity::class.java)
            i.putExtra("position", 999999)
            startActivity(i)
        })

        createEventImageView.setOnClickListener(View.OnClickListener {
            fm.beginTransaction().replace(R.id.main_layout,EventFragment()).commit()
            navView.setCheckedItem(R.id.nav_events)
            var i = Intent(activity, EventActivity::class.java)
            i.putExtra("position", 999999)
            startActivity(i)
        })

        archiveImageView.setOnClickListener(View.OnClickListener {
            fm.beginTransaction().replace(R.id.main_layout,ArchiveLockFragment()).commit()
            navView.setCheckedItem(R.id.nav_archive)
        })

        diaryImageView.setOnClickListener(View.OnClickListener {
            fm.beginTransaction().replace(R.id.main_layout,DiaryLockFragment()).commit()
            navView.setCheckedItem(R.id.nav_diary)
        })

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {


                noMatchImageView.setImageResource(R.drawable.ic_search_yellow)
                noMatchTextView.setText("No matching to-do list")
                noMatchTextView.visibility = View.GONE
                noMatchImageView.visibility = View.GONE

                val wholeList: MutableList<ToDoItem> = ArrayList()

                val db = DatabaseQueries(activity!!.baseContext)
                val cursor = db.getAllToDo()
                if (cursor.moveToFirst()) {
                    do{
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        var nameList: MutableList<String> = gson.fromJson(cursor.getString(cursor.getColumnIndex(DatabaseColumns.T_ITEM_NAME_LIST)), object : TypeToken<MutableList<String>>() {}.type)
                        var checkedList : MutableList<String> = gson.fromJson(cursor.getString(cursor.getColumnIndex(DatabaseColumns.T_ITEM_CHECKED_LIST)), object : TypeToken<MutableList<String>>() {}.type)
                        wholeList.add(0,ToDoItem(cursor.getString(cursor.getColumnIndex(DatabaseColumns.T_TITLE)),nameList,checkedList,cursor.getString(cursor.getColumnIndex(DatabaseColumns.T_COLOR))))
                    } while (cursor.moveToNext())
                }




                if (searchEditText.text.toString().equals("")) {

                    ToDoContent.ITEMS.clear()
                    for(currentWholeListToDo in wholeList){
                        ToDoContent.ITEMS.add(currentWholeListToDo)
                    }
                    myAdapter.notifyDataSetChanged()
                    noMatchImageView.visibility = View.GONE
                    noMatchTextView.visibility = View.GONE
                    if(wholeList.size == 0){
                        noMatchImageView.setImageResource(R.drawable.empty_logo)
                        noMatchTextView.setText("To-Do list you add appear here")
                        noMatchTextView.visibility = View.VISIBLE
                        noMatchImageView.visibility = View.VISIBLE
                    }
                } else {
                    val searchedList: MutableList<ToDoItem> = ArrayList()
                    for (toDoItem in wholeList){
                        var addToDo : Boolean =  false
                        if(toDoItem.title.contains(searchEditText.text.toString(),true))
                            addToDo = true
                        else for(item in toDoItem.nameList){
                            if(item.contains(searchEditText.text.toString(),true))
                                addToDo = true
                        }
                        if(addToDo)
                        searchedList.add(toDoItem)
                    }

                    if(searchedList.isEmpty()){
                        ToDoContent.ITEMS.clear()
                        myAdapter.notifyDataSetChanged()
                        noMatchImageView.visibility = View.VISIBLE
                        noMatchTextView.visibility = View.VISIBLE

                    } else {
                        noMatchImageView.visibility = View.GONE
                        noMatchTextView.visibility = View.GONE
                        ToDoContent.ITEMS.clear()
                        for(item in searchedList){
                            ToDoContent.ITEMS.add(item)
                        }
                        myAdapter.notifyDataSetChanged()
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //do nothing
            }
        })









        return view
    }

    override fun onResume() {
        super.onResume()
        populateListFromDB()
        myAdapter.notifyDataSetChanged()

    }

    fun populateListFromDB() {
        ToDoContent.ITEMS.clear()
        val db = DatabaseQueries(activity!!.baseContext)
        val cursor = db.getAllToDo()
        if (cursor.moveToFirst()) {
            noMatchImageView.setImageResource(R.drawable.ic_search_yellow)
            noMatchTextView.setText("No matching to-do list")
            noMatchTextView.visibility = View.GONE
            noMatchImageView.visibility = View.GONE

            do{
                val gson = GsonBuilder().setPrettyPrinting().create()
                var nameList: MutableList<String> = gson.fromJson(cursor.getString(cursor.getColumnIndex(DatabaseColumns.T_ITEM_NAME_LIST)), object : TypeToken<MutableList<String>>() {}.type)
                var checkedList : MutableList<String> = gson.fromJson(cursor.getString(cursor.getColumnIndex(DatabaseColumns.T_ITEM_CHECKED_LIST)), object : TypeToken<MutableList<String>>() {}.type)
                ToDoContent.ITEMS.add(0,ToDoItem(cursor.getString(cursor.getColumnIndex(DatabaseColumns.T_TITLE)),nameList,checkedList,cursor.getString(cursor.getColumnIndex(DatabaseColumns.T_COLOR))))
            } while (cursor.moveToNext())
        } else {
            noMatchImageView.setImageResource(R.drawable.empty_logo)
            noMatchTextView.setText("To-Do list you add appear here")
            noMatchTextView.visibility = View.VISIBLE
            noMatchImageView.visibility = View.VISIBLE
        }
    }

}
