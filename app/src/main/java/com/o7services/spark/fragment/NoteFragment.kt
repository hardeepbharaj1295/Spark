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
import com.o7services.spark.EventActivity
import com.o7services.spark.NoteActivity
import com.o7services.spark.adapter.NoteAdapter
import com.o7services.spark.R
import com.o7services.spark.ToDoActivity
import com.o7services.spark.database.DatabaseColumns
import com.o7services.spark.database.DatabaseQueries

import com.o7services.spark.model.NoteContent
import com.google.android.material.navigation.NavigationView
import java.util.ArrayList

class NoteFragment : Fragment() {

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
    lateinit var archiveImageView : ImageView
    lateinit var diaryImageView: ImageView


    private var columnCount = 1
    lateinit var myAdapter : NoteAdapter

    lateinit var fm : FragmentManager

    lateinit var navView : NavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note_list, container, false)

        drawer = activity!!.findViewById(R.id.drawer_layout)
        recyclerView = view.findViewById(R.id.noteRecyclerView)
        listSwitcherImageView = view.findViewById(R.id.noteListViewSwitchImageView)
        searchEditText = view.findViewById(R.id.noteSearchEditText)
        createToDoImageView = view.findViewById(R.id.noteCreateToDoImageView)
        createEventImageView = view.findViewById(R.id.noteCreateEventImageView)
        createNoteTextView = view.findViewById(R.id.createNoteTextView)
        noMatchTextView = view.findViewById(R.id.noteNoMatchTextView)
        noMatchImageView = view.findViewById(R.id.noteNoMatchImageView)
        drawerOpenerImageView = view.findViewById(R.id.noteDrawerImageView)
        navView = activity!!.findViewById(R.id.nav_view)
        archiveImageView = view.findViewById(R.id.noteCreateArchiveImageView)
        diaryImageView = view.findViewById(R.id.noteCreateDiaryImageView)

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



        populateNoteContentFromDB()

        myAdapter = NoteAdapter(NoteContent.ITEMS,activity)
        recyclerView.adapter = myAdapter

        drawerOpenerImageView.setOnClickListener(View.OnClickListener {
            drawer.openDrawer(GravityCompat.START)
        })

        createNoteTextView.setOnClickListener(View.OnClickListener {

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

        listSwitcherImageView.setOnClickListener(View.OnClickListener {
            if(columnCount == 1){
                val bundle : Bundle = Bundle()
                bundle.putInt("columnCount",2)
                val fragClass = NoteFragment::class.java
                val frag = fragClass.newInstance()
                frag.arguments = bundle
                fm.beginTransaction().replace(R.id.main_layout,frag).commit()
//                listSwitcherImageView.setImageResource(R.drawable.ic_list_double)
//                recyclerView.layoutManager = GridLayoutManager(context, columnCount)
//                myAdapter = NoteAdapter(NoteContent.ITEMS,activity)

            } else {
                val bundle : Bundle = Bundle()
                bundle.putInt("columnCount",1)
                val fragClass = NoteFragment::class.java
                val frag = fragClass.newInstance()
                frag.arguments = bundle
                fm.beginTransaction().replace(R.id.main_layout,frag).commit()
//                listSwitcherImageView.setImageResource(R.drawable.ic_list_single)
//                recyclerView.layoutManager = LinearLayoutManager(context)
//                myAdapter = NoteAdapter(NoteContent.ITEMS,activity)
            }
        })

        searchEditText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {


                noMatchImageView.setImageResource(R.drawable.ic_search_yellow)
                noMatchTextView.setText("No matching notes")
                noMatchTextView.visibility = View.GONE
                noMatchImageView.visibility = View.GONE

                val wholeList: MutableList<NoteContent.NoteItem> = ArrayList()

                val db = DatabaseQueries(activity!!.baseContext)
                var cursor = db.getAllNotes()
                if (cursor.moveToFirst()) {
                    do {
                        wholeList.add(0,
                            NoteContent.NoteItem(cursor.getString(cursor.getColumnIndex(DatabaseColumns.N_TITLE)),
                                cursor.getString(cursor.getColumnIndex(DatabaseColumns.N_DESCRIPTION)),
                                cursor.getString(cursor.getColumnIndex(DatabaseColumns.N_COLOR)),false)
                        )
                    } while (cursor.moveToNext())
                }

                if (searchEditText.text.toString().equals("")) {

                    NoteContent.ITEMS.clear()
                    for(currentWholeListNote in wholeList){
                        NoteContent.ITEMS.add(currentWholeListNote)
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
                    val searchedList: MutableList<NoteContent.NoteItem> = ArrayList()
                    for (currentWholeListNote in wholeList){
                        if(currentWholeListNote.title.contains(searchEditText.text.toString(),true))
                            searchedList.add(currentWholeListNote)
                        else if(currentWholeListNote.description.contains(searchEditText.text.toString(),true))
                            searchedList.add(currentWholeListNote)
                    }

                    if(searchedList.isEmpty()){
                        NoteContent.ITEMS.clear()
                        myAdapter.notifyDataSetChanged()
                        noMatchImageView.visibility = View.VISIBLE
                        noMatchTextView.visibility = View.VISIBLE

                    } else {
                        noMatchImageView.visibility = View.GONE
                        noMatchTextView.visibility = View.GONE
                        NoteContent.ITEMS.clear()
                        for(item in searchedList){
                            NoteContent.ITEMS.add(item)
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

    fun populateNoteContentFromDB() {
        NoteContent.ITEMS.clear()
        val db = DatabaseQueries(activity!!.baseContext)
        var cursor : Cursor = db.getAllNotes()
        if(cursor.moveToFirst()){
            noMatchImageView.setImageResource(R.drawable.ic_search_yellow)
            noMatchTextView.setText("No matching notes")
            noMatchTextView.visibility = View.GONE
            noMatchImageView.visibility = View.GONE
            do{
                NoteContent.ITEMS.add(0,NoteContent.NoteItem(cursor.getString(cursor.getColumnIndex(DatabaseColumns.N_TITLE)),
                    cursor.getString(cursor.getColumnIndex(DatabaseColumns.N_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(DatabaseColumns.N_COLOR)),false))
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
        populateNoteContentFromDB()
        myAdapter.notifyDataSetChanged()
    }


}
