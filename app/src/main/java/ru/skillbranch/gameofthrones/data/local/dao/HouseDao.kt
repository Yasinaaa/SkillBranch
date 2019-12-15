package ru.skillbranch.gameofthrones.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ru.skillbranch.gameofthrones.data.local.entities.House

/*
 * Created by yasina on 2019-12-14
*/
@Dao
interface HouseDao : BaseDao<House> {
    @Query(
        """
            SELECT COUNT(*) FROM houses
        """
    )
    suspend fun recordsCount(): Int

    @Transaction
    fun upsert(objList: List<House>){
        insert(objList)
            .mapIndexed { index, l ->  if (l == -1L) objList[index] else null}
            .filterNotNull()
            .also { if (it.isNotEmpty()) update(it) }
    }
}