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


class MainActivity : AppCompatActivity(), View.OnClickListener {

    internal val LOG_TAG = "myLogs"

    // данные для таблицы должностей
    val positionId = arrayOf( 1, 2, 3, 4 )
    val positionName = arrayOf("Директор", "Программер", "Бухгалтер", "Охранник")
    val positionSalary = arrayOf(15000, 13000, 10000, 8000)

    // данные для таблицы людей
    internal var peopleName = arrayOf("Иван", "Марья", "Петр", "Антон", "Даша", "Борис", "Костя", "Игорь")
    internal var peoplePosid = intArrayOf(2, 3, 2, 2, 3, 1, 2, 4)

    internal fun logCursor(c: Cursor?) {
        if (c != null) {
            if (c.moveToFirst()) {
                var str: String
                do {
                    str = ""
                    for (cn in c.columnNames) {
                        str += (cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ")
                    }
                    Log.d(LOG_TAG, str)
                } while (c.moveToNext())
            }
        } else
            Log.d(LOG_TAG, "Cursor is null")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Подключаемся к БД
        val dbh = DBHelper(this)
        val db = dbh.writableDatabase

        // Описание курсора
        var c: Cursor

        // выводим в лог данные по должностям
        Log.d(LOG_TAG, "--- Table position ---")
        c = db.query("position", null, null, null, null, null, null)
        logCursor(c)
        c.close()
        Log.d(LOG_TAG, "--- ---")

        // выводим в лог данные по людям
        Log.d(LOG_TAG, "--- Table people ---")
        c = db.query("people", null, null, null, null, null, null)
        logCursor(c)
        c.close()
        Log.d(LOG_TAG, "--- ---")

        Log.d(LOG_TAG, "--- INNER JOIN with rawQuery---")
        val sqlQuery = ("select PL.name as Name, PS.name as Position, salary as Salary "
                + "from people as PL "
                + "inner join position as PS "
                + "on PL.posid = PS.id "
                + "where salary > ?")
        c = db.rawQuery(sqlQuery, arrayOf("12000"))
        logCursor(c)
        c.close()
        Log.d(LOG_TAG, "--- ---")

        // выводим результат объединения
        // используем query
        Log.d(LOG_TAG, "--- INNER JOIN with query---")
        val table = "people as PL inner join position as PS on PL.posid = PS.id"
        val columns = arrayOf("PL.name as Name", "PS.name as Position", "salary as Salary")
        val selection = "salary < ?"
        val selectionArgs = arrayOf("12000")
        c = db.query(table, columns, selection, selectionArgs, null, null, null)
        logCursor(c)
        c.close()
        Log.d(LOG_TAG, "--- ---")

        // закрываем БД
        dbh.close()


    }

    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class DBHelper(context: Context): SQLiteOpenHelper(context, "myDB", null, 1) {

        override fun onCreate(db: SQLiteDatabase?) {
            Log.d(LOG_TAG, "--- onCreate database ---")
            // создаем таблицу с полями
            val cv = ContentValues()

            // создаем таблицу должностей
            db?.execSQL("create table position ("
                    + "id integer primary key,"
                    + "name text," + "salary integer"
                    + ");")

            // заполняем ее
            for (i in positionId.indices) {
                cv.clear()
                cv.put("id", positionId[i])
                cv.put("name", positionName[i])
                cv.put("salary", positionSalary[i])
                db?.insert("position", null, cv)
            }

            // создаем таблицу людей
            db?.execSQL("create table people ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "posid integer"
                    + ");")

            // заполняем ее
            for (i in peopleName.indices) {
                cv.clear()
                cv.put("name", peopleName[i])
                cv.put("posid", peoplePosid[i])
                db?.insert("people", null, cv)
            }
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        }
    }



}
