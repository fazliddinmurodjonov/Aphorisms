package com.programmsoft.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.programmsoft.room.entity.AphorismWithCategories

@Dao
interface AphorismWithCategoriesDao {
    @Transaction
    @Query("SELECT *FROM AphorismCategory where id = :id")
    fun getAphorismByCategory(id: Int): AphorismWithCategories
}