/*
package com.zzyyff.xrecord.manager.database

import androidx.room.*
import com.zzyyff.xrecord.model.api.Person
import io.reactivex.Completable
import io.reactivex.Single

@Dao
abstract class PersonDao {

    @Query("SELECT * FROM Person")
    abstract fun getAllPerson(): Single<List<Person>>

    @Insert
    abstract fun insert(person: Person):Completable

    @Update
    abstract fun update(person: Person):Completable

    @Delete
    abstract fun delete(person: Person):Completable

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
