package com.startproject.startkotlin

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    internal val LOG_TAG = "myLogs"

    private val btnAddView: Button by lazy { btnAdd }
    private val btnReadView by lazy { btnRead }
    private val btnClearView by lazy { btnClear }
    private val etNameView by lazy { etName }
    private val etEmailView by lazy { etEmail }

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // создаем объект для создания и управления версиями БД
        btnAddView.setOnClickListener(this)
        btnReadView.setOnClickListener(this)
        btnClearView.setOnClickListener(this)

        dbHelper = DBHelper(this)
    }


    override fun onClick(v: View) {

        val contentValues = ContentValues()

        val name = etNameView.text.toString()
        val email = etEmailView.text.toString()

        val db = dbHelper.writableDatabase

        when(v.id) {
            R.id.btnAdd -> {
                Log.d(LOG_TAG, "--- Insert in mytable: ---")
                contentValues.put("name", name)
                contentValues.put("email", email)
                val rowId = db.insert("mytable", null, contentValues)
                Log.d(LOG_TAG, "row inserted, ID = $rowId")
            }
            R.id.btnRead -> {
                Log.d(LOG_TAG, "--- Rows in mytable: ---")
                val c = db.query("mytable", null, null, null, null, null, null)

                if (c.moveToFirst()) {
                    val idColIndex = c.getColumnIndex("id")
                    val nameColIndex = c.getColumnIndex("name")
                    val emailColIndex = c.getColumnIndex("email")
                    do {
                        Log.d(LOG_TAG,
                                "ID = " + c.getInt(idColIndex) +
                                        ", name = " + c.getString(nameColIndex) +
                                        ", email = " + c.getString(emailColIndex))
                    } while (c.moveToNext())
                } else
                    Log.d(LOG_TAG, "0 rows")
                c.close()
            }
            R.id.btnClear -> {
                Log.d(LOG_TAG, "--- Clear mytable: ---")
                val clearCount = db.delete("mytable", null, null)
                Log.d(LOG_TAG, "deleted rows count = $clearCount")
            }
        }
        dbHelper.close()
    }

    inner class DBHelper(context: Context): SQLiteOpenHelper(context, "myDB", null, 1) {

        override fun onCreate(db: SQLiteDatabase?) {
            Log.d(LOG_TAG, "--- onCreate database ---")
            db?.execSQL("create table mytable ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "email text" + ");")
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        }
    }



}
