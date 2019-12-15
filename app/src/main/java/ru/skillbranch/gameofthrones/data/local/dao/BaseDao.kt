package ru.skillbranch.gameofthrones.data.local.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/*
 * Created by yasina on 2019-12-14
*/
interface BaseDao<T: Any> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(obj:List<T>): List<Long>

    @Update
    fun update(obj: List<T>)
}