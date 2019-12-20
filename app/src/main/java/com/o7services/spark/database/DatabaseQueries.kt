package com.o7services.spark.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.o7services.spark.model.*

class DatabaseQueries {

    lateinit var sql : DatabaseCreation

    constructor(context : Context){
        sql = DatabaseCreation(context)
    }

    fun insertEvent(title: String, startDay: String, startTime: String,
                    endDay : String, endTime : String, firstReminder : String,
                    secondReminder : String, color : String, description :String,
                    backgroundImage : String) : Long {
        val db = sql.writableDatabase
        val cv = ContentValues()
        cv.put(DatabaseColumns.E_TITLE,title)
        cv.put(DatabaseColumns.E_START_DAY,startDay)
        cv.put(DatabaseColumns.E_START_TIME,startTime)
        cv.put(DatabaseColumns.E_END_DAY,endDay)
        cv.put(DatabaseColumns.E_END_TIME,endTime)
        cv.put(DatabaseColumns.E_FIRST_REMINDER,firstReminder)
        cv.put(DatabaseColumns.E_SECOND_REMINDER,secondReminder)
        cv.put(DatabaseColumns.E_COLOR,color)
        cv.put(DatabaseColumns.E_DESCRIPTION,description)
        cv.put(DatabaseColumns.E_BACKGROUND_IMAGE,backgroundImage)
        return db.insert(DatabaseColumns.EVENT_TABLE_NAME,null,cv)
    }

    fun getAllEvents() : Cursor {
        val db = sql.readableDatabase
        return db.query(DatabaseColumns.EVENT_TABLE_NAME,null,null,null,null,null,null)
    }

    fun updateEvent(oldEventItem: EventContent.EventItem, newEventItem: EventContent.EventItem) : Int{
        val db = sql.writableDatabase
        val cv = ContentValues()
        cv.put(DatabaseColumns.E_TITLE,newEventItem.title)
        cv.put(DatabaseColumns.E_START_DAY,newEventItem.startDay)
        cv.put(DatabaseColumns.E_START_TIME,newEventItem.startTime)
        cv.put(DatabaseColumns.E_END_DAY,newEventItem.endDay)
        cv.put(DatabaseColumns.E_END_TIME,newEventItem.endTime)
        cv.put(DatabaseColumns.E_FIRST_REMINDER,newEventItem.firstReminder)
        cv.put(DatabaseColumns.E_SECOND_REMINDER,newEventItem.secondReminder)
        cv.put(DatabaseColumns.E_COLOR,newEventItem.color)
        cv.put(DatabaseColumns.E_DESCRIPTION,newEventItem.description)
        cv.put(DatabaseColumns.E_BACKGROUND_IMAGE,newEventItem.backgroundImage)
        val whereClause = "${DatabaseColumns.E_TITLE}=? AND ${DatabaseColumns.E_START_DAY}=? AND " +
                "${DatabaseColumns.E_START_TIME}=? AND ${DatabaseColumns.E_END_DAY}=? AND " +
                "${DatabaseColumns.E_END_TIME}=? AND ${DatabaseColumns.E_FIRST_REMINDER}=? AND " +
                "${DatabaseColumns.E_SECOND_REMINDER}=? AND ${DatabaseColumns.E_COLOR}=? AND " +
                "${DatabaseColumns.E_DESCRIPTION}=? AND ${DatabaseColumns.E_BACKGROUND_IMAGE}=?"
        val array = arrayOf(oldEventItem.title,oldEventItem.startDay,oldEventItem.startTime,oldEventItem.endDay,oldEventItem.endTime,
            oldEventItem.firstReminder,oldEventItem.secondReminder,oldEventItem.color,oldEventItem.description,oldEventItem.backgroundImage)
        return db.update(DatabaseColumns.EVENT_TABLE_NAME,cv,whereClause,array)
    }

    fun deleteEvent(event : EventContent.EventItem):Int{
        val db = sql.writableDatabase
        val whereClause = "${DatabaseColumns.E_TITLE}=? AND ${DatabaseColumns.E_START_DAY}=? AND " +
                "${DatabaseColumns.E_START_TIME}=? AND ${DatabaseColumns.E_END_DAY}=? AND " +
                "${DatabaseColumns.E_END_TIME}=? AND ${DatabaseColumns.E_FIRST_REMINDER}=? AND " +
                "${DatabaseColumns.E_SECOND_REMINDER}=? AND ${DatabaseColumns.E_COLOR}=? AND " +
                "${DatabaseColumns.E_DESCRIPTION}=? AND ${DatabaseColumns.E_BACKGROUND_IMAGE}=?"
        val array = arrayOf(event.title,event.startDay,event.startTime,event.endDay,event.endTime,
            event.firstReminder,event.secondReminder,event.color,event.description,event.backgroundImage)
        return db.delete(DatabaseColumns.EVENT_TABLE_NAME,whereClause,array)
    }




    fun insertNote(title : String, description : String, color : String) : Long{
        val db = sql.writableDatabase
        val cv = ContentValues()
        cv.put("${DatabaseColumns.N_TITLE}",title)
        cv.put("${DatabaseColumns.N_DESCRIPTION}",description)
        cv.put("${DatabaseColumns.N_COLOR}",color)
        return db.insert(DatabaseColumns.NOTE_TABLE_NAME,null,cv)
    }

    fun deleteNote(item : NoteContent.NoteItem):Int{
        val db = sql.writableDatabase
        val whereClause = "${DatabaseColumns.N_TITLE}=? AND ${DatabaseColumns.N_DESCRIPTION}=? AND " +
                "${DatabaseColumns.N_COLOR}=?"
        val array = arrayOf(item.title,item.description,item.color)
        return db.delete(DatabaseColumns.NOTE_TABLE_NAME,whereClause,array)
    }

    fun updateNote(oldNoteItem : NoteContent.NoteItem, newNoteItem : NoteContent.NoteItem) : Int {
        val db = sql.writableDatabase
        val cv = ContentValues()
        cv.put("${DatabaseColumns.N_TITLE}",newNoteItem.title)
        cv.put("${DatabaseColumns.N_DESCRIPTION}",newNoteItem.description)
        cv.put("${DatabaseColumns.N_COLOR}",newNoteItem.color)
        val whereClause = "${DatabaseColumns.N_TITLE}=? AND ${DatabaseColumns.N_DESCRIPTION}=? AND " +
                "${DatabaseColumns.N_COLOR}=?"
        val array = arrayOf(oldNoteItem.title,oldNoteItem.description,oldNoteItem.color)
        return db.update(DatabaseColumns.NOTE_TABLE_NAME,cv,whereClause,array)
    }

    fun getAllNotes() : Cursor {
        val db = sql.readableDatabase
        return db.query(DatabaseColumns.NOTE_TABLE_NAME,null,null,null,null,null,null)
    }





    fun insertArchive(title : String, description : String, color : String) : Long{
        val db = sql.writableDatabase
        val cv = ContentValues()
        cv.put("${DatabaseColumns.A_TITLE}",title)
        cv.put("${DatabaseColumns.A_DESCRIPTION}",description)
        cv.put("${DatabaseColumns.A_COLOR}",color)
        return db.insert(DatabaseColumns.ARCHIVE_TABLE_NAME,null,cv)
    }

    fun deleteArchive(item : ArchiveContent.ArchiveItem):Int{
        val db = sql.writableDatabase
        val whereClause = "${DatabaseColumns.A_TITLE}=? AND ${DatabaseColumns.A_DESCRIPTION}=? AND " +
                "${DatabaseColumns.A_COLOR}=?"
        val array = arrayOf(item.title,item.description,item.color)
        return db.delete(DatabaseColumns.ARCHIVE_TABLE_NAME,whereClause,array)
    }

    fun updateArchive(oldArchiveItem : ArchiveContent.ArchiveItem, newArchiveItem : ArchiveContent.ArchiveItem) : Int {
        val db = sql.writableDatabase
        val cv = ContentValues()
        cv.put("${DatabaseColumns.A_TITLE}",newArchiveItem.title)
        cv.put("${DatabaseColumns.A_DESCRIPTION}",newArchiveItem.description)
        cv.put("${DatabaseColumns.A_COLOR}",newArchiveItem.color)
        val whereClause = "${DatabaseColumns.A_TITLE}=? AND ${DatabaseColumns.A_DESCRIPTION}=? AND " +
                "${DatabaseColumns.A_COLOR}=?"
        val array = arrayOf(oldArchiveItem.title,oldArchiveItem.description,oldArchiveItem.color)
        return db.update(DatabaseColumns.ARCHIVE_TABLE_NAME,cv,whereClause,array)
    }

    fun getAllArchives() : Cursor {
        val db = sql.readableDatabase
        return db.query(DatabaseColumns.ARCHIVE_TABLE_NAME,null,null,null,null,null,null)
    }

    fun insertToDo(title : String,nameList : String,checkedList : String, color : String) : Long{
        val db = sql.writableDatabase
        val cv = ContentValues()
        cv.put("${DatabaseColumns.T_TITLE}",title)
        cv.put("${DatabaseColumns.T_ITEM_NAME_LIST}",nameList)
        cv.put("${DatabaseColumns.T_ITEM_CHECKED_LIST}",checkedList)
        cv.put("${DatabaseColumns.T_COLOR}",color)
        return db.insert(DatabaseColumns.ToDo_TABLE_NAME,null,cv)
    }

    fun getAllToDo() : Cursor {
        val db = sql.readableDatabase
        return db.query(DatabaseColumns.ToDo_TABLE_NAME,null,null,null,null,null,null)
    }

    fun deleteToDo(title: String, nameList: String, checkedList: String,color: String) : Int {
        val db = sql.writableDatabase
        val whereClause : String = "${DatabaseColumns.T_TITLE}=? AND ${DatabaseColumns.T_ITEM_NAME_LIST}=? AND" +
                " ${DatabaseColumns.T_ITEM_CHECKED_LIST}=? AND ${DatabaseColumns.T_COLOR}=?"
        val array = arrayOf(title,nameList,checkedList,color)
        return db.delete(DatabaseColumns.ToDo_TABLE_NAME,whereClause,array)
    }

    fun updateToDo(oldTitle: String, oldNameList: String, oldCheckedList: String,oldColor: String,newTitle: String, newNameList: String, newCheckedList: String,newColor: String) : Int {
        val db = sql.writableDatabase
        val cv = ContentValues()
        cv.put("${DatabaseColumns.T_TITLE}",newTitle)
        cv.put("${DatabaseColumns.T_ITEM_NAME_LIST}",newNameList)
        cv.put("${DatabaseColumns.T_ITEM_CHECKED_LIST}",newCheckedList)
        cv.put("${DatabaseColumns.T_COLOR}",newColor)
        val whereClause : String = "${DatabaseColumns.T_TITLE}=? AND ${DatabaseColumns.T_ITEM_NAME_LIST}=? AND ${DatabaseColumns.T_ITEM_CHECKED_LIST}=? AND ${DatabaseColumns.T_COLOR}=?"
        val array = arrayOf(oldTitle,oldNameList,oldCheckedList,oldColor)
        return db.update(DatabaseColumns.ToDo_TABLE_NAME,cv,whereClause,array)
    }

    fun insertDiary(date : String, description: String, color : String): Long{
        var db = sql.writableDatabase
        val cv = ContentValues()
        cv.put("${DatabaseColumns.D_DATE}",date)
        cv.put("${DatabaseColumns.D_DESCRIPTION}",description)
        cv.put("${DatabaseColumns.D_COLOR}",color)
        return db.insert(DatabaseColumns.DIARY_TABLE_NAME,null,cv)
    }

    fun updateDiary(newItem : DiaryContent.DiaryItem, oldItem : DiaryContent.DiaryItem) : Int {
        var db = sql.writableDatabase
        val cv = ContentValues()
        cv.put("${DatabaseColumns.D_DATE}",newItem.date)
        cv.put("${DatabaseColumns.D_DESCRIPTION}",newItem.description)
        cv.put("${DatabaseColumns.D_COLOR}",newItem.color)
        return db.update(DatabaseColumns.DIARY_TABLE_NAME,cv,"${DatabaseColumns.D_DATE}=? AND ${DatabaseColumns.D_DESCRIPTION}=? AND ${DatabaseColumns.D_COLOR}=?",
            arrayOf(oldItem.date,oldItem.description,oldItem.color))
    }

    fun deleteDiary(item : DiaryContent.DiaryItem):Int{
        val db = sql.writableDatabase
        return db.delete(DatabaseColumns.DIARY_TABLE_NAME,"${DatabaseColumns.D_DATE}=? AND ${DatabaseColumns.D_DESCRIPTION}=? AND ${DatabaseColumns.D_COLOR}=?",
            arrayOf(item.date,item.description,item.color))
    }

    fun getWholeDiary(): Cursor{
        val db = sql.readableDatabase
        return db.query(DatabaseColumns.DIARY_TABLE_NAME,null,null,null,null,null,null)
    }

}