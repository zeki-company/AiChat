/*
package com.zzyyff.xrecord.manager.database

import androidx.room.*
import com.zzyyff.xrecord.model.api.Record
import io.reactivex.Completable
import io.reactivex.Single

@Dao
abstract class RecordDao {

    @Query("SELECT * FROM Record")
    abstract fun getAllRecord(): Single<List<Record>>

    @Insert
    abstract fun insert(record: Record):Completable

    @Update
    abstract fun update(record: Record):Completable

    @Delete
    abstract fun delete(record: Record):Completable

    */
/*@Query("SELECT * FROM RecordTable WHERE user_id = :userId")
    abstract fun getHasReads(userId: Long): List<HasReadTable>

    @Query("SELECT * FROM RecordTable WHERE user_id = :userId")
    abstract fun getHasReadsLiveData(userId: Long = MainApplication.nowUserId!!): LiveData<List<HasReadTable>>

    @Query("SELECT * FROM RecordTable WHERE user_id = :userId AND key_body = :keyBody")
    abstract fun getHasReads(userId: String, keyBody: String): List<HasReadTable>

//    @Query("SELECT * FROM HasReadTable WHERE (user_id ==  :userId) and (key_body == :keybody) LIMIT 1")
//    fun getHasRead(userId: String, keyBody: String): HasReadTable

//    @Insert(entity = HasReadTable::class)
//    fun saveHasRead(hasRead: HasReadTable)

    @Query("INSERT OR ABORT INTO RecordTable (user_id , key_body) VALUES (:userId,:keyBody)")
    abstract fun setHasRead(userId: String, keyBody: String)

    @Query("DELETE FROM RecordTable WHERE user_id = :userId")
    abstract fun deleteAll(userId: String)*//*


}*/
