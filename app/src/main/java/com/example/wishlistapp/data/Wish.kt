package com.example.wishlistapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wish_table")
data class Wish(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "wish-title")
    val title: String = "",
    @ColumnInfo(name = "wish-desc")
    val description: String = ""
)

object DummyWish{
    val wishList = listOf(
        Wish(title = "Google watch 2",
            description = "An adnroid watch"),
        Wish(title = "Bugatti",
            description = "An amazing car"),
        Wish(title = "Dog",
            description = "Best firend"),
        Wish(title = "Villa",
            description = "Cozy home"),
        Wish(title = "LG Screen",
            description = "Great monitor for work")
    )
}