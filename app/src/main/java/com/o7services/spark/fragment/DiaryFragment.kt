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
import com.o7services.spark.adapter.DiaryAdapter
import com.o7services.spark.database.DatabaseColumns
import com.o7services.spark.database.DatabaseQueries

import com.o7services.spark.model.DiaryContent
import com.google.android.material.navigation.NavigationView
import java.util.ArrayList

class DiaryFragment : Fragment() {

    lateinit var drawerOpenerImageView : ImageView
    lateinit var drawer : DrawerLayout
    lateinit var recyclerView: RecyclerView
    lateinit var listSwitcherImageView : ImageView
    lateinit var searchEditText: EditText
    lateinit var createToDoImageView: ImageView
    lateinit var createEventImageView: ImageView
    lateinit var createNoteImageView: ImageView
    lateinit var noMatchImageView: ImageView
    lateinit var noMatchTextView: TextView
    lateinit var createDiaryTextView: TextView
    lateinit var createArchiveImageView: ImageView


    private var columnCount = 1
    lateinit var myAdapter : DiaryAdapter

    lateinit var fm : FragmentManager

    lateinit var navView : NavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diary_list, container, false)
        drawer = activity!!.findViewById(R.id.drawer_layout)
        recyclerView = view.findViewById(R.id.diaryRecyclerView)
        listSwitcherImageView = view.findViewById(R.id.diaryListViewSwitchImageView)
        searchEditText = view.findViewById(R.id.diarySearchEditText)
        createToDoImageView = view.findViewById(R.id.diaryCreateToDoImageView)
        createEventImageView = view.findViewById(R.id.diaryCreateEventImageView)
        createNoteImageView = view.findViewById(R.id.diaryCreateNoteImageView)
        noMatchTextView = view.findViewById(R.id.diaryNoMatchTextView)
        noMatchImageView = view.findViewById(R.id.diaryNoMatchImageView)
        drawerOpenerImageView = view.findViewById(R.id.diaryDrawerImageView)
        navView = activity!!.findViewById(R.id.nav_view)
        createArchiveImageView = view.findViewById(R.id.diaryCreateArchiveImageView)
        createDiaryTextView = view.findViewById(R.id.createDiaryTextView)

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



        populateDiaryContentFromDB()

        myAdapter = DiaryAdapter(DiaryContent.ITEMS,activity)
        recyclerView.adapter = myAdapter

        drawerOpenerImageView.setOnClickListener(View.OnClickListener {
            drawer.openDrawer(GravityCompat.START)
        })

        createDiaryTextView.setOnClickListener(View.OnClickListener {

            var i = Intent(activity, DiaryActivity::class.java)
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

        createArchiveImageView.setOnClickListener(View.OnClickListener {
            fm.beginTransaction().replace(R.id.main_layout,ArchiveLockFragment()).commit()
            navView.setCheckedItem(R.id.nav_archive)
        })

        createNoteImageView.setOnClickListener(View.OnClickListener {
            fm.beginTransaction().replace(R.id.main_layout,NoteFragment()).commit()
            navView.setCheckedItem(R.id.nav_note)
            var i = Intent(activity, NoteActivity::class.java)
            i.putExtra("position", 999999)
            startActivity(i)
        })

        listSwitcherImageView.setOnClickListener(View.OnClickListener {
            if(columnCount == 1){
                val bundle : Bundle = Bundle()
                bundle.putInt("columnCount",2)
                val fragClass = DiaryFragment::class.java
                val frag = fragClass.newInstance()
                frag.arguments = bundle
                fm.beginTransaction().replace(R.id.main_layout,frag).commit()

            } else {
                val bundle : Bundle = Bundle()
                bundle.putInt("columnCount",1)
                val fragClass = DiaryFragment::class.java
                val frag = fragClass.newInstance()
                frag.arguments = bundle
                fm.beginTransaction().replace(R.id.main_layout,frag).commit()
            }
        })

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {


                noMatchImageView.setImageResource(R.drawable.ic_search_yellow)
                noMatchTextView.setText("No Match")
                noMatchTextView.visibility = View.GONE
                noMatchImageView.visibility = View.GONE

                val wholeList: MutableList<DiaryContent.DiaryItem> = ArrayList()

                val db = DatabaseQueries(activity!!.baseContext)
                var cursor = db.getWholeDiary()
                if (cursor.moveToFirst()) {
                    do {
                        wholeList.add(0,
                            DiaryContent.DiaryItem(cursor.getString(cursor.getColumnIndex(DatabaseColumns.D_DATE)),
                                cursor.getString(cursor.getColumnIndex(DatabaseColumns.D_DESCRIPTION)),
                                cursor.getString(cursor.getColumnIndex(DatabaseColumns.D_COLOR)))
                        )
                    } while (cursor.moveToNext())
                }

                if (searchEditText.text.toString().equals("")) {

                    DiaryContent.ITEMS.clear()
                    for(currentWholeListItem in wholeList){
                        DiaryContent.ITEMS.add(currentWholeListItem)
                    }
                    myAdapter.notifyDataSetChanged()
                    noMatchImageView.visibility = View.GONE
                    noMatchTextView.visibility = View.GONE
                    if(wholeList.size == 0){
                        noMatchImageView.setImageResource(R.drawable.empty_logo)
                        noMatchTextView.setText("Diary you write will appear here")
                        noMatchTextView.visibility = View.VISIBLE
                        noMatchImageView.visibility = View.VISIBLE
                    }
                } else {
                    val searchedList: MutableList<DiaryContent.DiaryItem> = ArrayList()
                    for (currentWholeListItem in wholeList){
                        if(currentWholeListItem.date.contains(searchEditText.text.toString(),true))
                            searchedList.add(currentWholeListItem)
                        else if(currentWholeListItem.description.contains(searchEditText.text.toString(),true))
                            searchedList.add(currentWholeListItem)
                    }

                    if(searchedList.isEmpty()){
                        DiaryContent.ITEMS.clear()
                        myAdapter.notifyDataSetChanged()
                        noMatchImageView.visibility = View.VISIBLE
                        noMatchTextView.visibility = View.VISIBLE

                    } else {
                        noMatchImageView.visibility = View.GONE
                        noMatchTextView.visibility = View.GONE
                        DiaryContent.ITEMS.clear()
                        for(item in searchedList){
                            DiaryContent.ITEMS.add(item)
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

    fun populateDiaryContentFromDB() {
        DiaryContent.ITEMS.clear()
        val db = DatabaseQueries(activity!!.baseContext)
        var cursor : Cursor = db.getWholeDiary()
        if(cursor.moveToFirst()){
            noMatchImageView.setImageResource(R.drawable.ic_search_yellow)
            noMatchTextView.setText("No Match")
            noMatchTextView.visibility = View.GONE
            noMatchImageView.visibility = View.GONE
            do{
                DiaryContent.ITEMS.add(0,DiaryContent.DiaryItem(cursor.getString(cursor.getColumnIndex(DatabaseColumns.D_DATE)),
                    cursor.getString(cursor.getColumnIndex(DatabaseColumns.D_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(DatabaseColumns.D_COLOR))))
            } while(cursor.moveToNext())
        } else {
            noMatchImageView.setImageResource(R.drawable.empty_logo)
            noMatchTextView.setText("Diary you write will appear here")
            noMatchTextView.visibility = View.VISIBLE
            noMatchImageView.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        populateDiaryContentFromDB()
        myAdapter.notifyDataSetChanged()
    }

}
