package com.programmsoft.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.programmsoft.room.entity.Aphorism
import io.reactivex.rxjava3.core.Flowable

@Dao
interface AphorismDao {
    @Insert
    fun insert(aphorism: Aphorism)

    @Update
    fun update(aphorism: Aphorism)

    @Query("select *from  Aphorism")
    fun getAll(): Flowable<List<Aphorism>>

    @Query("select *from  Aphorism")
    fun getAllWithoutFlowable(): List<Aphorism>

    @Query("select *from Aphorism where id = :id")
    fun getById(id: Int): Aphorism

    @Query("SELECT *FROM Aphorism WHERE news = :newAphorism")
    fun getAllNews(newAphorism: Int): Flowable<List<Aphorism>>

    @Query("select *from Aphorism where categoryId = :categoryId")
    fun getAllByCategories(categoryId: Int): Flowable<List<Aphorism>>

    @Query("select *from Aphorism where bookmark = :bookmark")
    fun getAllByFavourite(bookmark: Int): Flowable<List<Aphorism>>

    @Query("SELECT COUNT(*) FROM Aphorism WHERE bookmark = 1")
    fun getAllFavourite(): Int

    @Query("SELECT COUNT(*) FROM Aphorism WHERE aphorismId = :aphorismId")
    fun isAphorismExist(aphorismId: Long): Int

    @Query("UPDATE Aphorism SET bookmark = :bookmark WHERE aphorismId = :aphorismId")
    fun updateFavourite(aphorismId: Long, bookmark: Int)

    @Query("UPDATE Aphorism SET news = 0 WHERE aphorismId = :aphorismId")
    fun updateNew(aphorismId: Long)

    @Query("select *from  Aphorism where aphorismId = :aphorismId")
    fun getByAphorismId(aphorismId: Long): Aphorism

    @Query("select aphorismId from  Aphorism where id = :id")
    fun getAphorismIdById(id: Long): Long
}