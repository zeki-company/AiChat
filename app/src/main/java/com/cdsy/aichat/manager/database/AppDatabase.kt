/*
package com.zzyyff.xrecord.manager.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zzyyff.xrecord.model.api.Person
import com.zzyyff.xrecord.model.api.Record
import com.zzyyff.xrecord.util.Constants

@Database(
    entities = [Record::class, Person::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao
    abstract fun personDao(): PersonDao
    companion object {
        const val DATABASE_NAME_APP = Constants.DATABASE_NAME_APP
    }
}
*/
