package com.programmsoft.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.programmsoft.room.entity.AphorismCategory
import io.reactivex.rxjava3.core.Flowable

@Dao
interface AphorismCategoryDao {
    @Insert
    fun insert(aphorismCategory: AphorismCategory)

    @Query("select *from AphorismCategory")
    fun getAll(): Flowable<List<AphorismCategory>>

    @Query("select *from AphorismCategory")
    fun getAllWithoutFlowable(): List<AphorismCategory>

    @Query("select *from AphorismCategory where id = :id")
    fun getById(id: Int): AphorismCategory
}