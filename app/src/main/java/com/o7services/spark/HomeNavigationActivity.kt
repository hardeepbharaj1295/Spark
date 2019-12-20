package com.o7services.spark

import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import androidx.fragment.app.FragmentManager
import com.o7services.spark.backup.RemoteBackup
import com.o7services.spark.fragment.*

class HomeNavigationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var fragmentManager: FragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_navigation)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar!!.hide()

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setCheckedItem(R.id.nav_note)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.main_layout,NoteFragment()).commit()



    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START)
        } else {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home_navigation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_note -> {
                fragmentManager.beginTransaction().replace(R.id.main_layout,NoteFragment()).commit()
            }
            R.id.nav_to_do -> {
                fragmentManager.beginTransaction().replace(R.id.main_layout,ToDoFragment()).commit()
            }
            R.id.nav_events -> {
                fragmentManager.beginTransaction().replace(R.id.main_layout,EventFragment()).commit()
            }
            R.id.nav_archive -> {
                fragmentManager.beginTransaction().replace(R.id.main_layout,ArchiveLockFragment()).commit()
            }
            R.id.nav_diary -> {
                fragmentManager.beginTransaction().replace(R.id.main_layout, DiaryLockFragment()).commit()
            }
            R.id.nav_import -> {
                try{
                    fragmentManager.beginTransaction().replace(R.id.main_layout,
                        ImportExportFragment()
                    ).commit()
                }
                catch (e: Exception){
                    e.printStackTrace()
                }
//                val import = RemoteBackup(this)
//                import.connectToDrive(false)
            }

            R.id.nav_export -> {
//                val import = RemoteBackup(this)
//                import.connectToDrive(true)
                try{
                    fragmentManager.beginTransaction().replace(R.id.main_layout,
                        ImportExportFragment()
                    ).commit()
                }
                catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
