package com.o7services.spark.fragment

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import com.o7services.spark.EventActivity
import com.o7services.spark.NoteActivity
import com.o7services.spark.ToDoActivity
import com.o7services.spark.adapter.EventAdapter
import com.o7services.spark.database.DatabaseColumns
import com.o7services.spark.database.DatabaseQueries

import com.o7services.spark.model.EventContent
import com.o7services.spark.model.EventContent.EventItem
import java.util.ArrayList
import com.o7services.spark.R
import com.google.android.material.navigation.NavigationView


class EventFragment : Fragment() {

    lateinit var drawerOpenerImageView: ImageView
    lateinit var searchEditText: EditText
    lateinit var listSwitcherImageView: ImageView
    lateinit var createEventTextView: TextView
    lateinit var createNoteImageView: ImageView
    lateinit var createToDoImageView: ImageView
    lateinit var recyclerView: RecyclerView
    lateinit var noMatchImageView : ImageView
    lateinit var noMatchTextView: TextView
    lateinit var drawer : DrawerLayout
    lateinit var archiveImageView: ImageView
    lateinit var diaryImageView: ImageView

    private var columnCount = 1
    lateinit var myAdapter : EventAdapter

    lateinit var navView : NavigationView


    lateinit var fm : FragmentManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event_list, container, false)

        drawerOpenerImageView = view.findViewById(R.id.eventDrawerImageView)
        searchEditText = view.findViewById(R.id.eventSearchEditText)
        listSwitcherImageView = view.findViewById(R.id.eventListViewSwitchImageView)
        createEventTextView = view.findViewById(R.id.createEventTextView)
        createNoteImageView = view.findViewById(R.id.eventCreateNoteImageView)
        createToDoImageView = view.findViewById(R.id.eventCreateToDoImageView)
        recyclerView = view.findViewById(R.id.eventRecyclerView)
        noMatchImageView = view.findViewById(R.id.eventNoMatchImageView)
        noMatchTextView = view.findViewById(R.id.eventNoMatchTextView)
        drawer = activity!!.findViewById(R.id.drawer_layout)
        archiveImageView = view.findViewById(R.id.eventCreateArchiveImageView)
        diaryImageView = view.findViewById(R.id.eventCreateDiaryImageView)

        fm = activity!!.supportFragmentManager

        navView = activity!!.findViewById(R.id.nav_view)


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

        populateEventContentFromDB()

        myAdapter = EventAdapter(EventContent.ITEMS, activity,columnCount)
        recyclerView.adapter = myAdapter

        createNoteImageView.setOnClickListener(View.OnClickListener {
            fm.beginTransaction().replace(R.id.main_layout,NoteFragment()).commit()
            navView.setCheckedItem(R.id.nav_note)
            var i = Intent(activity, NoteActivity::class.java)
            i.putExtra("position", 999999)
            startActivity(i)
        })

        createToDoImageView.setOnClickListener(View.OnClickListener {
            fm.beginTransaction().replace(R.id.main_layout,ToDoFragment()).commit()
            navView.setCheckedItem(R.id.nav_to_do)
            var i = Intent(activity, ToDoActivity::class.java)
            i.putExtra("position", 999999)
            startActivity(i)
        })

        createEventTextView.setOnClickListener(View.OnClickListener {
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

        listSwitcherImageView.setOnClickListener(View.OnClickListener {

            if (columnCount == 1) {

                val bundle : Bundle = Bundle()
                bundle.putInt("columnCount",2)
                val fragClass = EventFragment::class.java
                val frag = fragClass.newInstance()
                frag.arguments = bundle
                fm.beginTransaction().replace(R.id.main_layout,frag).commit()

            } else {

                val bundle : Bundle = Bundle()
                bundle.putInt("columnCount",1)
                val fragClass = EventFragment::class.java
                val frag = fragClass.newInstance()
                frag.arguments = bundle
                fm.beginTransaction().replace(R.id.main_layout,frag).commit()
            }
        })

        searchEditText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {


                noMatchImageView.setImageResource(R.drawable.ic_search_yellow)
                noMatchTextView.setText("No matching Events")
                noMatchTextView.visibility = View.GONE
                noMatchImageView.visibility = View.GONE


                val wholeList: MutableList<EventItem> = ArrayList()

                val db = DatabaseQueries(activity!!.baseContext)
                var cursor = db.getAllEvents()
                if (cursor.moveToFirst()) {
                    do {
                        wholeList.add(0,
                            EventItem(
                                cursor.getString(cursor.getColumnIndex(DatabaseColumns.E_TITLE)),
                                cursor.getString(cursor.getColumnIndex(DatabaseColumns.E_START_DAY)),
                                cursor.getString(cursor.getColumnIndex(DatabaseColumns.E_START_TIME)),
                                cursor.getString(cursor.getColumnIndex(DatabaseColumns.E_END_DAY)),
                                cursor.getString(cursor.getColumnIndex(DatabaseColumns.E_END_TIME)),
                                cursor.getString(cursor.getColumnIndex(DatabaseColumns.E_FIRST_REMINDER)),
                                cursor.getString(cursor.getColumnIndex(DatabaseColumns.E_SECOND_REMINDER)),
                                cursor.getString(cursor.getColumnIndex(DatabaseColumns.E_COLOR)),
                                cursor.getString(cursor.getColumnIndex(DatabaseColumns.E_DESCRIPTION)),
                                cursor.getString(cursor.getColumnIndex(DatabaseColumns.E_BACKGROUND_IMAGE))
                            )
                        )
                    } while (cursor.moveToNext())
                }

                if (searchEditText.text.toString().equals("")) {

                    EventContent.ITEMS.clear()
                    for(currentWholeListEvent in wholeList){
                        EventContent.ITEMS.add(currentWholeListEvent)
                    }
                    myAdapter.notifyDataSetChanged()
                    noMatchImageView.visibility = View.GONE
                    noMatchTextView.visibility = View.GONE

                    if(wholeList.size == 0){
                        noMatchImageView.setImageResource(R.drawable.empty_logo)
                        noMatchTextView.setText("Events you add appear here")
                        noMatchTextView.visibility = View.VISIBLE
                        noMatchImageView.visibility = View.VISIBLE
                    }
                } else {
                    val searchedList: MutableList<EventItem> = ArrayList()
                    for (currentWholeListEvent in wholeList){
                        if(currentWholeListEvent.title.contains(searchEditText.text.toString(),true))
                            searchedList.add(currentWholeListEvent)
                        else if(currentWholeListEvent.description.contains(searchEditText.text.toString(),true))
                            searchedList.add(currentWholeListEvent)
                    }

                    if(searchedList.isEmpty()){
                        EventContent.ITEMS.clear()
                        myAdapter.notifyDataSetChanged()
                        noMatchImageView.visibility = View.VISIBLE
                        noMatchTextView.visibility = View.VISIBLE

                    } else {
                        noMatchImageView.visibility = View.GONE
                        noMatchTextView.visibility = View.GONE
                        EventContent.ITEMS.clear()
                        for(item in searchedList){
                            EventContent.ITEMS.add(item)
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

       drawerOpenerImageView.setOnClickListener(View.OnClickListener {
           drawer.openDrawer(GravityCompat.START)
       })

        return view
    }

    override fun onResume() {
        super.onResume()
        populateEventContentFromDB()
        myAdapter.notifyDataSetChanged()
    }

    fun populateEventContentFromDB(){

        EventContent.ITEMS.clear()
        val db = DatabaseQueries(activity!!.baseContext)
        var cursor = db.getAllEvents()
        if (cursor.moveToFirst()) {

            noMatchImageView.setImageResource(R.drawable.ic_search_yellow)
            noMatchTextView.setText("No matching events")
            noMatchTextView.visibility = View.GONE
            noMatchImageView.visibility = View.GONE

            do {
                EventContent.ITEMS.add(0,
                    EventItem(
                        cursor.getString(cursor.getColumnIndex(DatabaseColumns.E_TITLE)),
                        cursor.getString(cursor.getColumnIndex(DatabaseColumns.E_START_DAY)),
                        cursor.getString(cursor.getColumnIndex(DatabaseColumns.E_START_TIME)),
                        cursor.getString(cursor.getColumnIndex(DatabaseColumns.E_END_DAY)),
                        cursor.getString(cursor.getColumnIndex(DatabaseColumns.E_END_TIME)),
                        cursor.getString(cursor.getColumnIndex(DatabaseColumns.E_FIRST_REMINDER)),
                        cursor.getString(cursor.getColumnIndex(DatabaseColumns.E_SECOND_REMINDER)),
                        cursor.getString(cursor.getColumnIndex(DatabaseColumns.E_COLOR)),
                        cursor.getString(cursor.getColumnIndex(DatabaseColumns.E_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(DatabaseColumns.E_BACKGROUND_IMAGE))
                    )
                )
            } while (cursor.moveToNext())
        } else {
            noMatchImageView.setImageResource(R.drawable.empty_logo)
            noMatchTextView.setText("Events you add appear here")
            noMatchTextView.visibility = View.VISIBLE
            noMatchImageView.visibility = View.VISIBLE
        }
    }

}
