package com.gamaliel.todoapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo (
    @ColumnInfo(name="title")//kalau nama dengan table database tidak match pake ini
    var title:String,
    @ColumnInfo(name="notes")
    var notes:String,
    @ColumnInfo(name="priority")
    var priority:Int,
    @ColumnInfo(name="is_done", defaultValue = "0")
    var isDone: Int = 0  // data tipe boolean tidak support dalam database

){
    @PrimaryKey(autoGenerate = true)
    var uuid:Int =0
}
