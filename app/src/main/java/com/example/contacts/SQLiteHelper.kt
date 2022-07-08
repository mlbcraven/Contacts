package com.example.contacts

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception

class SQLiteHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        val DATABASE_VERSION: Int = 1
        val DATABASE_NAME: String = "entries.db"

        val TABLE_NAME: String = "entries"
        val KEY_ID: String = "id"
        val KEY_NAME: String = "name"
        val KEY_LASTNAME: String = "lastname"
        val KEY_TELEPHONE: String = "telephone"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        var CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_NAME + " TEXT," +
                KEY_LASTNAME + " TEXT," +
                KEY_TELEPHONE + " TEXT" + ")")
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addEntry(entry: Entry) : Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, entry.id)
        contentValues.put(KEY_NAME, entry.name)
        contentValues.put(KEY_LASTNAME, entry.lastname)
        contentValues.put(KEY_TELEPHONE, entry.telephone)

        val success = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return success
    }


    @SuppressLint("Range")
    fun getEntry(): ArrayList<Entry> {
        val entryList: ArrayList<Entry> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        }catch (e:Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id:Int
        var name:String
        var lastname:String
        var telephone:String

        if (cursor.moveToFirst()) {
            do{
                id = cursor.getInt(cursor.getColumnIndex("id"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                lastname = cursor.getString(cursor.getColumnIndex("lastname"))
                telephone = cursor.getString(cursor.getColumnIndex("telephone"))
                val ent = Entry(id = id, name = name, lastname = lastname , telephone = telephone)
                entryList.add(ent)

            }while (cursor.moveToNext())
        }
        return entryList
    }

    fun updateEntry(ent: Entry) : Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_ID, ent.id)
        contentValues.put(KEY_NAME,ent.name)
        contentValues.put(KEY_LASTNAME,ent.lastname)
        contentValues.put(KEY_TELEPHONE,ent.telephone)
        val success = db.update(TABLE_NAME, contentValues, "id=" + ent.id, null)
        db.close()
        return success
    }

    fun deleteEntryById(id: Int): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_ID, id)

        val success = db.delete(TABLE_NAME,"id=$id" , null)

        db.close()
        return success
    }

}