package kr.ourguide.sqllite_tutorial

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {
    private var sqliteDB: SQLiteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sqliteDB = initDatabase()

        initTables()

        if (sqliteDB != null) {


            loadValues()

            saveButton.setOnClickListener {
                saveValues()
            }

            clearButton.setOnClickListener {
                deleteValues()
            }
        }

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        val listAdapter = ListAdapter()
        listRecyclerView.adapter = listAdapter
    }

    private fun initDatabase(): SQLiteDatabase? {
        val dbFileName = "example.db"

        try {
            val databaseFile: File = getDatabasePath(dbFileName)
            sqliteDB = SQLiteDatabase.openOrCreateDatabase(databaseFile, null)
        } catch (e: SQLiteException) {
            e.printStackTrace()
        }

        return sqliteDB
    }

    private fun initTables() {
        val sqlCreateTable = "CREATE TABLE IF NOT EXISTS CONTACT_T (" +
                "NUM " + "INTEGER NOT NULL," +
                "NAME " + "TEXT," +
                "PHONE " + "TEXT," +
                "OVER20 " + "INTEGER" + ")"

        println(sqlCreateTable)

        sqliteDB?.execSQL(sqlCreateTable)

    }

    private fun loadValues() {
        val sqlQueryTable = "SELECT * FROM CONTACT_T"
        val cursor = sqliteDB?.rawQuery(sqlQueryTable, null)

        if (cursor != null) {
            if (cursor.moveToNext()) {
                val num = cursor.getInt(0)
                numEditText.setText(num.toString())

                val name = cursor.getString(1)
                nameEditText.setText(name)

                val phone = cursor.getString(2)
                phoneEditText.setText(phone)

                val overTwenty = cursor.getInt(3)
                overTwentyCheckBox.isChecked = overTwenty != 0
            }

            cursor.close()
        }
    }

    private fun saveValues() {
//        sqliteDB?.execSQL("DELETE FROM CONTACT_T")

        val numText = numEditText.text

        val num = if (numText != null && numText.isNotEmpty()) {
            numText.toString().toInt()
        } else 0

        val name: String = nameEditText.text.toString()

        val phone = phoneEditText.text.toString()

        val isOverTwenty = overTwentyCheckBox.isChecked

        val sqlInsert = "INSERT INTO CONTACT_T " +
                "(NUM, NAME, PHONE, OVER20) VALUES (" +
                num.toString() + "," +
                "'" + name + "'," +
                "'" + phone + "'," +
                (if (isOverTwenty) "1" else "0") + ")"

        sqliteDB?.execSQL(sqlInsert)
    }

    private fun deleteValues() {
        val sqlDelete = "DELETE FROM CONTACT_T"

        sqliteDB?.execSQL(sqlDelete)

        numEditText.setText("")

        nameEditText.setText("")

        phoneEditText.setText("")

        overTwentyCheckBox.isChecked = false
    }
}


