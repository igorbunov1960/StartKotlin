package com.startproject.startkotlin

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    internal val LOG_TAG = "myLogs"

    val name = arrayOf("Китай", "США", "Бразилия", "Россия", "Япония",
            "Германия", "Египет", "Италия", "Франция", "Канада")
    val people = arrayOf(1400, 311, 195, 142, 128, 82, 80, 60, 66, 35)
    val region = arrayOf( "Азия", "Америка", "Америка", "Европа", "Азия",
            "Европа", "Африка", "Европа", "Европа", "Америка" )

    val btnAllView by lazy { btnAll }
    val btnFuncView by lazy { btnFunc }
    val btnPeopleView by lazy { btnPeople }
    val btnSortView by lazy { btnSort }
    val btnGroupView by lazy { btnGroup }
    val btnHavingView by lazy { btnHaving }
    val etFuncView by lazy { etFunc }
    val etPeopleView by lazy { etPeople }
    val etRegionPeopleView by lazy { etRegionPeople }
    val rgSortView by lazy { rgSort }

    private lateinit var dbHelper: DBHelper
    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAllView.setOnClickListener(this)
        btnFuncView.setOnClickListener(this)
        btnPeopleView.setOnClickListener(this)
        btnSortView.setOnClickListener(this)
        btnGroupView.setOnClickListener(this)
        btnHavingView.setOnClickListener(this)
        etFuncView
        etPeopleView
        etRegionPeopleView

        dbHelper = DBHelper(this)
        db = dbHelper.writableDatabase

        var c = db.query("mytable", null, null, null, null, null, null)
        if (c.count == 0) {
            var contentValue = ContentValues()

            for (i in 1..10) {
                contentValue.put("name", name[i - 1])
                contentValue.put("people", people[i - 1])
                contentValue.put("region", region[i - 1])

                Log.d(LOG_TAG, "id = " + db.insert("mytable", null, contentValue))
            }
        }
        c.close()
        dbHelper.close()
        onClick(btnAllView)
    }


    override fun onClick(v: View) {
        db = dbHelper.writableDatabase

        var func = etFuncView.text.toString()
        var peop = etPeopleView.text.toString()
        var regPeop = etRegionPeopleView.text.toString()

        var columns: Array<String>? = null
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        var groupBy: String? = null
        var having: String? = null
        var orderBy: String? = null

        var c: Cursor? = null

        when (v.id) {
        // Все записи
            R.id.btnAll -> {
                Log.d(LOG_TAG, "--- Все записи ---")
                c = db.query("mytable", null, null, null, null, null, null)
            }
        // Функция
            R.id.btnFunc -> {
                Log.d(LOG_TAG, "--- Функция $func ---")
                columns = arrayOf(func)
                c = db.query("mytable", columns, null, null, null, null, null)
            }
        // Население больше, чем
            R.id.btnPeople -> {
                Log.d(LOG_TAG, "--- Население больше $peop ---")
                selection = "people > ?"
                selectionArgs = arrayOf(peop)
                c = db.query("mytable", null, selection, selectionArgs, null, null, null)
            }
        // Население по региону
            R.id.btnGroup -> {
                Log.d(LOG_TAG, "--- Население по региону ---")
                columns = arrayOf("region", "sum(people) as people")
                groupBy = "region"
                c = db.query("mytable", columns, null, null, groupBy, null, null)
            }
        // Население по региону больше чем
            R.id.btnHaving -> {
                Log.d(LOG_TAG, "--- Регионы с населением больше " + regPeop
                        + " ---")
                columns = arrayOf("region", "sum(people) as people")
                groupBy = "region"
                having = "sum(people) > $regPeop"
                c = db.query("mytable", columns, null, null, groupBy, having, null)
            }
        // Сортировка
            R.id.btnSort -> {
                // сортировка по
                when (rgSort.checkedRadioButtonId) {
                // наименование
                    R.id.rName -> {
                        Log.d(LOG_TAG, "--- Сортировка по наименованию ---")
                        orderBy = "name"
                    }
                // население
                    R.id.rPeople -> {
                        Log.d(LOG_TAG, "--- Сортировка по населению ---")
                        orderBy = "people"
                    }
                // регион
                    R.id.rRegion -> {
                        Log.d(LOG_TAG, "--- Сортировка по региону ---")
                        orderBy = "region"
                    }
                }
                c = db.query("mytable", null, null, null, null, null, orderBy)
            }
        }

        if (c != null) {
            if (c.moveToFirst()) {
                var str: String
                do {
                    str = ""
                    for (cn in c.columnNames) {
                        str += (cn + " = "
                                + c.getString(c.getColumnIndex(cn)) + "; ")
                    }
                    Log.d(LOG_TAG, str)

                } while (c.moveToNext())
            }
            c.close()
        } else
            Log.d(LOG_TAG, "Cursor is null")

        dbHelper.close()
    }

    inner class DBHelper(context: Context): SQLiteOpenHelper(context, "myDB", null, 1) {

        override fun onCreate(db: SQLiteDatabase?) {
            Log.d(LOG_TAG, "--- onCreate database ---")
            // создаем таблицу с полями
            db?.execSQL("create table mytable ("
                    + "id integer primary key autoincrement," + "name text,"
                    + "people integer," + "region text" + ");")
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        }
    }



}
