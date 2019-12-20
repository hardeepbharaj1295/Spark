package com.o7services.spark.fragment

import android.content.Intent
import android.database.Cursor
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
import com.o7services.spark.*
import com.o7services.spark.adapter.ArchiveAdapter
import com.o7services.spark.database.DatabaseColumns
import com.o7services.spark.database.DatabaseQueries

import com.o7services.spark.model.ArchiveContent
import com.o7services.spark.model.ArchiveContent.ArchiveItem
import com.google.android.material.navigation.NavigationView
import java.util.ArrayList

class ArchiveFragment : Fragment() {

    lateinit var drawerOpenerImageView : ImageView
    lateinit var drawer : DrawerLayout
    lateinit var recyclerView: RecyclerView
    lateinit var listSwitcherImageView : ImageView
    lateinit var searchEditText: EditText
    lateinit var createToDoImageView: ImageView
    lateinit var createEventImageView: ImageView
    lateinit var createNoteTextView: TextView
    lateinit var noMatchImageView: ImageView
    lateinit var noMatchTextView: TextView
    lateinit var createNoteImageView: ImageView
    lateinit var diaryImageView: ImageView


    private var columnCount = 1
    lateinit var myAdapter : ArchiveAdapter

    lateinit var fm : FragmentManager

    lateinit var navView : NavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_archive_list, container, false)


        drawer = activity!!.findViewById(R.id.drawer_layout)
        recyclerView = view.findViewById(R.id.archiveRecyclerView)
        listSwitcherImageView = view.findViewById(R.id.archiveListViewSwitchImageView)
        searchEditText = view.findViewById(R.id.archiveSearchEditText)
        createToDoImageView = view.findViewById(R.id.archiveCreateToDoImageView)
        createEventImageView = view.findViewById(R.id.archiveCreateEventImageView)
        createNoteTextView = view.findViewById(R.id.createArchiveTextView)
        noMatchTextView = view.findViewById(R.id.archiveNoMatchTextView)
        noMatchImageView = view.findViewById(R.id.archiveNoMatchImageView)
        drawerOpenerImageView = view.findViewById(R.id.archiveDrawerImageView)
        navView = activity!!.findViewById(R.id.nav_view)
        createNoteImageView = view.findViewById(R.id.archiveCreateNoteImageView)
        diaryImageView = view.findViewById(R.id.archiveCreateDiaryImageView)

        fm = activity!!.supportFragmentManager

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



        populateArchiveContentFromDB()

        myAdapter = ArchiveAdapter(ArchiveContent.ITEMS,activity)
        recyclerView.adapter = myAdapter

        drawerOpenerImageView.setOnClickListener(View.OnClickListener {
            drawer.openDrawer(GravityCompat.START)
        })

        createNoteTextView.setOnClickListener(View.OnClickListener {

            var i = Intent(activity, ArchiveActivity::class.java)
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

        createEventImageView.setOnClickListener(View.OnClickListener {
            fm.beginTransaction().replace(R.id.main_layout,EventFragment()).commit()
            navView.setCheckedItem(R.id.nav_events)
            var i = Intent(activity, EventActivity::class.java)
            i.putExtra("position", 999999)
            startActivity(i)
        })

        createNoteImageView.setOnClickListener(View.OnClickListener {
            fm.beginTransaction().replace(R.id.main_layout,NoteFragment()).commit()
            navView.setCheckedItem(R.id.nav_note)
            var i = Intent(activity, NoteActivity::class.java)
            i.putExtra("position", 999999)
            startActivity(i)
        })

        diaryImageView.setOnClickListener(View.OnClickListener {
            fm.beginTransaction().replace(R.id.main_layout,DiaryLockFragment()).commit()
            navView.setCheckedItem(R.id.nav_diary)
        })

        listSwitcherImageView.setOnClickListener(View.OnClickListener {
            if(columnCount == 1){

                val bundle : Bundle = Bundle()
                bundle.putInt("columnCount",2)
                val fragClass = ArchiveFragment::class.java
                val frag = fragClass.newInstance()
                frag.arguments = bundle
                fm.beginTransaction().replace(R.id.main_layout,frag).commit()

            } else {
                val bundle : Bundle = Bundle()
                bundle.putInt("columnCount",1)
                val fragClass = ArchiveFragment::class.java
                val frag = fragClass.newInstance()
                frag.arguments = bundle
                fm.beginTransaction().replace(R.id.main_layout,frag).commit()

            }
        })

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                noMatchImageView.setImageResource(R.drawable.ic_search_yellow)
                noMatchTextView.setText("No matching notes")
                noMatchTextView.visibility = View.GONE
                noMatchImageView.visibility = View.GONE

                val wholeList: MutableList<ArchiveItem> = ArrayList()

                val db = DatabaseQueries(activity!!.baseContext)
                var cursor = db.getAllArchives()
                if (cursor.moveToFirst()) {
                    do {
                        wholeList.add(0,
                            ArchiveContent.ArchiveItem(cursor.getString(cursor.getColumnIndex(DatabaseColumns.A_TITLE)),
                                cursor.getString(cursor.getColumnIndex(DatabaseColumns.A_DESCRIPTION)),
                                cursor.getString(cursor.getColumnIndex(DatabaseColumns.A_COLOR)),true)
                        )
                    } while (cursor.moveToNext())
                }

                if (searchEditText.text.toString().equals("")) {

                    ArchiveContent.ITEMS.clear()
                    for(currentWholeListArchive in wholeList){
                        ArchiveContent.ITEMS.add(currentWholeListArchive)
                    }
                    myAdapter.notifyDataSetChanged()
                    noMatchImageView.visibility = View.GONE
                    noMatchTextView.visibility = View.GONE
                    if(wholeList.size == 0){
                        noMatchImageView.setImageResource(R.drawable.empty_logo)
                        noMatchTextView.setText("Notes you add appear here")
                        noMatchTextView.visibility = View.VISIBLE
                        noMatchImageView.visibility = View.VISIBLE
                    }
                } else {
                    val searchedList: MutableList<ArchiveItem> = ArrayList()
                    for (currentWholeListArchive in wholeList){
                        if(currentWholeListArchive.title.contains(searchEditText.text.toString(),true))
                            searchedList.add(currentWholeListArchive)
                        else if(currentWholeListArchive.description.contains(searchEditText.text.toString(),true))
                            searchedList.add(currentWholeListArchive)
                    }

                    if(searchedList.isEmpty()){
                        ArchiveContent.ITEMS.clear()
                        myAdapter.notifyDataSetChanged()
                        noMatchImageView.visibility = View.VISIBLE
                        noMatchTextView.visibility = View.VISIBLE

                    } else {
                        noMatchImageView.visibility = View.GONE
                        noMatchTextView.visibility = View.GONE
                        ArchiveContent.ITEMS.clear()
                        for(item in searchedList){
                            ArchiveContent.ITEMS.add(item)
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

    fun populateArchiveContentFromDB() {
        ArchiveContent.ITEMS.clear()
        val db = DatabaseQueries(activity!!.baseContext)
        var cursor : Cursor = db.getAllArchives()
        if(cursor.moveToFirst()){


            noMatchImageView.setImageResource(R.drawable.ic_search_yellow)
            noMatchTextView.setText("No matching notes")
            noMatchTextView.visibility = View.GONE
            noMatchImageView.visibility = View.GONE

            do{
                ArchiveContent.ITEMS.add(0,
                    ArchiveItem(cursor.getString(cursor.getColumnIndex(DatabaseColumns.A_TITLE)),
                        cursor.getString(cursor.getColumnIndex(DatabaseColumns.A_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(DatabaseColumns.A_COLOR)),true))
            } while(cursor.moveToNext())
        } else {
            noMatchImageView.setImageResource(R.drawable.empty_logo)
            noMatchTextView.setText("Notes you add appear here")
            noMatchTextView.visibility = View.VISIBLE
            noMatchImageView.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        populateArchiveContentFromDB()
        myAdapter.notifyDataSetChanged()
    }


}
