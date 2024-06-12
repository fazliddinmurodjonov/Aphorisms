package com.programmsoft.room.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity
data class AphorismWithCategories(
    @Embedded
    val category: AphorismCategory,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val aphorisms: List<Aphorism>,
)