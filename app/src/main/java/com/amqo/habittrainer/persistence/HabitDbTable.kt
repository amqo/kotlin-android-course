package com.amqo.habittrainer.persistence

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.util.Log
import com.amqo.habittrainer.Habit
import java.io.ByteArrayOutputStream

class HabitDbTable(context: Context) {

    private val TAG = HabitDbTable::class.java.simpleName

    private val dbHelper = HabitTrainerDb(context)

    fun store(habit:Habit): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues()
        with(values) {
            put(HabitEntry.TITLE_COL, habit.title)
            put(HabitEntry.DESCR_COL, habit.description)
            put(HabitEntry.IMAGE_COL, toByteArray(habit.image))
        }

        val id = db.transaction {
            insert(HabitEntry.TABLE_NAME, null, values)
        }

        Log.d(TAG, "Store new habit to the DB $habit")

        return id
    }

    private fun toByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }
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
