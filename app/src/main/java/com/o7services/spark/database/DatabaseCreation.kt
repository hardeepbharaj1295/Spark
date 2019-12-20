package com.o7services.spark.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Environment
import android.widget.Toast
import com.o7services.spark.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream



class DatabaseCreation(val context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {

    val E_CREATE_TABLE = "CREATE TABLE ${DatabaseColumns.EVENT_TABLE_NAME} (${DatabaseColumns.E_TITLE} TEXT, " +
            "${DatabaseColumns.E_START_DAY} TEXT, ${DatabaseColumns.E_START_TIME} TEXT, ${DatabaseColumns.E_END_DAY} TEXT, " +
            "${DatabaseColumns.E_END_TIME} TEXT, ${DatabaseColumns.E_FIRST_REMINDER} TEXT, ${DatabaseColumns.E_SECOND_REMINDER} TEXT, " +
            "${DatabaseColumns.E_COLOR} TEXT, ${DatabaseColumns.E_DESCRIPTION} TEXT,${DatabaseColumns.E_BACKGROUND_IMAGE} TEXT)"

    val E_DROP_TABLE = "DROP TABLE IF EXISTS ${DatabaseColumns.EVENT_TABLE_NAME}"

    val N_CREATE_TABLE = "CREATE TABLE ${DatabaseColumns.NOTE_TABLE_NAME} (${DatabaseColumns.N_TITLE} TEXT, ${DatabaseColumns.N_DESCRIPTION}" +
            " TEXT, ${DatabaseColumns.N_COLOR} TEXT)"

    val N_DROP_TABLE = "DROP TABLE IF EXISTS ${DatabaseColumns.NOTE_TABLE_NAME}"


    val A_CREATE_TABLE = "CREATE TABLE ${DatabaseColumns.ARCHIVE_TABLE_NAME} (${DatabaseColumns.A_TITLE} TEXT, ${DatabaseColumns.A_DESCRIPTION}" +
            " TEXT, ${DatabaseColumns.A_COLOR} TEXT)"

    val A_DROP_TABLE = "DROP TABLE IF EXISTS ${DatabaseColumns.ARCHIVE_TABLE_NAME}"

    val T_CREATE_TABLE = "CREATE TABLE ${DatabaseColumns.ToDo_TABLE_NAME} (${DatabaseColumns.T_TITLE} TEXT,${DatabaseColumns.T_ITEM_NAME_LIST}" +
            " TEXT,${DatabaseColumns.T_ITEM_CHECKED_LIST} TEXT,${DatabaseColumns.T_COLOR} TEXT)"

    val T_DROP_TABLE = "DROP TABLE IF EXISTS ${DatabaseColumns.ToDo_TABLE_NAME}"

    val D_CREATE_TABLE = "CREATE TABLE ${DatabaseColumns.DIARY_TABLE_NAME} (${DatabaseColumns.D_DATE} TEXT," +
            " ${DatabaseColumns.D_DESCRIPTION} TEXT, ${DatabaseColumns.D_COLOR} TEXT)"

    val D_DROP_TABLE = "DROP TABLE IF EXISTS ${DatabaseColumns.DIARY_TABLE_NAME}"

    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db!!.disableWriteAheadLogging()
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(E_CREATE_TABLE)
        db.execSQL(N_CREATE_TABLE)
        db.execSQL(A_CREATE_TABLE)
        db.execSQL(T_CREATE_TABLE)
        db.execSQL(D_CREATE_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(E_DROP_TABLE)
        db.execSQL(N_DROP_TABLE)
        db.execSQL(A_DROP_TABLE)
        db.execSQL(T_DROP_TABLE)
        db.execSQL(D_DROP_TABLE)
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "database.db"
        const val VERSION = 1
    }


    fun backup(outFileName: String) {

        //database path
        val inFileName = context!!.getDatabasePath(DATABASE_NAME).toString()

        try {

            if (isSDCardWriteable()) {
                val dbFile = File(inFileName)
                val fis = FileInputStream(dbFile)

                // Open the empty db as the output stream
                val output = FileOutputStream(outFileName)

                // Transfer bytes from the input file to the output file
                val buffer = ByteArray(1024)
                var length: Int

                while (fis.read(buffer).also { length = it } > 0) {
                    output.write(buffer, 0, length)
                }

                // Close the streams
                output.flush()
                output.close()
                fis.close()

                Toast.makeText(context, "Backup Completed", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to backup database. Retry", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    fun importDB(inFileName: String) {

//        val outFileName = android.os.Environment.getExternalStorageDirectory().getPath() + File.separatorChar +
//                "Spark" + File.separatorChar + DATABASE_NAME

        val outFileName =context!!.getDatabasePath(DATABASE_NAME).toString()
        try {
            val dbFile = File(inFileName)
            val fis = FileInputStream(dbFile)

            // Open the empty db as the output stream
            val output = FileOutputStream(outFileName)

            // Transfer bytes from the input file to the output file
            val buffer = ByteArray(1024)
            var length: Int

            while (fis.read(buffer).also { length = it } > 0) {
                output.write(buffer, 0, length)
            }

            // Close the streams
            output.flush()
            output.close()
            fis.close()

            Toast.makeText(context, "Import Completed", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Toast.makeText(context, "Unable to my_import database. Retry", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

    }

    private fun isSDCardWriteable(): Boolean {
        var rc = false
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            rc = true
        }
        return rc
    }


}