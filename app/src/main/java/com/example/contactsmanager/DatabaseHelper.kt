package com.example.contactsmanager

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_MOBILE + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_ADDRESS + " TEXT" + ")")
        db.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    fun addContact(name: String?, mobile: String?, email: String?, address: String?) {
        val db = this.getWritableDatabase()
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_MOBILE, mobile)
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_ADDRESS, address)
        db.insert(TABLE_CONTACTS, null, values)
        db.close()
    }

    fun updateContact(id: Int, name: String?, mobile: String?, email: String?, address: String?) {
        val db = this.getWritableDatabase()
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_MOBILE, mobile)
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_ADDRESS, address)
        db.update(TABLE_CONTACTS, values, "$COLUMN_ID = ?", arrayOf<String>(id.toString()))
        db.close()
    }

    val allContacts: Cursor?
        get() {
            val db = this.getReadableDatabase()
            return db.rawQuery("SELECT * FROM $TABLE_CONTACTS", null)
        }

    fun deleteContact(id: Int) {
        val db = this.getWritableDatabase()
        db.delete(TABLE_CONTACTS, COLUMN_ID + " = ?", arrayOf<String>(id.toString()))
        db.close()
    }

    fun getContactById(id: Int): Cursor? {
        val db = this.getReadableDatabase()
        return db.rawQuery(
            "SELECT * FROM $TABLE_CONTACTS WHERE $COLUMN_ID = ?",
            arrayOf<String>(id.toString())
        )
    }

    companion object {
        private const val DATABASE_NAME = "contacts.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_CONTACTS = "contacts"

        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_MOBILE = "mobile"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_ADDRESS = "address"
    }
}
