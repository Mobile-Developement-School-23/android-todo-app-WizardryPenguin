package ru.winpenguin.todoapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Обеспечивает работу с базой данных измененных дел
 */
@Dao
interface ItemChangeDao {

    @Query("SELECT * FROM changed_items")
    suspend fun getAllItems(): List<ChangedItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ChangedItemEntity)

    @Query("DELETE FROM changed_items WHERE item_id LIKE :itemId")
    suspend fun deleteItem(itemId: String)
}