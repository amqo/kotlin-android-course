package com.amqo.habittrainer.persistence

import android.provider.BaseColumns

val DATABASE_NAME = "habit_trainer.db"
val DATABASE_VERSION = 10

object HabitEntry: BaseColumns {
    val TABLE_NAME = "habit"
    val TITLE_COL = "title"
    val DESCR_COL = "description"
    val IMAGE_COL = "image"
}
