package com.programmsoft.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.programmsoft.room.dao.AphorismCategoryDao
import com.programmsoft.room.dao.AphorismDao
import com.programmsoft.room.dao.AphorismWithCategoriesDao
import com.programmsoft.room.entity.Aphorism
import com.programmsoft.room.entity.AphorismCategory

@Database(
    entities = [Aphorism::class, AphorismCategory::class],
    version = 1
)
abstract class AphorismDB : RoomDatabase(){
    abstract fun aphorismDao(): AphorismDao
    abstract fun aphorismCategoryDao(): AphorismCategoryDao
    abstract fun aphorismWithCategoryDao(): AphorismWithCategoriesDao


    companion object {
        private var instance: AphorismDB? = null

        @Synchronized
        fun getInstance(context: Context): AphorismDB {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(
                        context,
                        AphorismDB::class.java,
                        "aphorism_db"
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
            }
            return instance!!
        }
    }
}
