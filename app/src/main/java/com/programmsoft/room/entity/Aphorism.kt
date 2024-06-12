package com.programmsoft.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Aphorism(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var aphorismId: Long = 0,
    var text: String = "",
    var news: Int = 1,
    var bookmark: Int = 1,
    var categoryId: Int = 1
)