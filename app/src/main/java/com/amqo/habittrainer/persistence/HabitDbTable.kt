package com.amqo.habittrainer.persistence

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.amqo.habittrainer.model.Habit
import com.amqo.habittrainer.persistence.HabitEntry.DESCR_COL
import com.amqo.habittrainer.persistence.HabitEntry.IMAGE_COL
import com.amqo.habittrainer.persistence.HabitEntry.TABLE_NAME
import com.amqo.habittrainer.persistence.HabitEntry.TITLE_COL
import com.amqo.habittrainer.persistence.HabitEntry._ID
import java.io.ByteArrayOutputStream

class HabitDbTable(context: Context) {

    private val TAG = HabitDbTable::class.java.simpleName

    private val dbHelper = HabitTrainerDb(context)

    fun store(habit: Habit): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues()
        with(values) {
            put(TITLE_COL, habit.title)
            put(DESCR_COL, habit.description)
            put(IMAGE_COL, toByteArray(habit.image))
        }

        val id = db.transaction {
            insert(TABLE_NAME, null, values)
        }
        Log.d(TAG, "Store new habit to the DB $habit")

        return id
    }

    fun read(): List<Habit> {
        val db = dbHelper.readableDatabase

        val columns = arrayOf(TITLE_COL, DESCR_COL, IMAGE_COL)
        val order = "$_ID ASC"

        val cursor = db.doQuery(TABLE_NAME, columns, orderBy = order)

        return parseHabitsFrom(cursor)
    }

    private fun parseHabitsFrom(cursor: Cursor): MutableList<Habit> {
        val habits = mutableListOf<Habit>()
        with(cursor) {
            while (moveToNext()) {
                val title = getString(TITLE_COL)
                val description = getString(DESCR_COL)
                val habit = Habit(title, description, getBitmap(IMAGE_COL))
                habits.add(habit)
            }
            close()
        }
        return habits
    }

    private fun toByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }
}

private fun Cursor.getString(columnName: String) = getString(getColumnIndex(columnName))

private fun Cursor.getBitmap(columnName: String): Bitmap {
    val imageByteArray = getBlob(getColumnIndex(columnName))
    return BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
}

private fun SQLiteDatabase.doQuery(
        table: String, columns: Array<String>, selection: String? = null,
        selectionArgs: Array<String>? = null, groupBy: String? = null,
        having: String? = null, orderBy: String? = null): Cursor {

    return query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
}

// inline here will take this exact code and put it in the place where it was called from
// after the concrete function was replaced in it
// This avoids the anonymous object creations for each function when called
private inline fun <T> SQLiteDatabase.transaction(function: SQLiteDatabase.() -> T): T {
    beginTransaction()
    val result = try {
        val returnValue = function()
        setTransactionSuccessful()

        returnValue
    } finally {
        endTransaction()
    }
    close()
    return result
}
