package ru.winpenguin.todoapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.winpenguin.todoapp.domain.models.TodoItem


@Dao
interface TodoDao {

    @Query("SELECT * FROM todo_items")
    fun allItemsFlow(): Flow<List<TodoItem>>

    @Query("SELECT * FROM todo_items WHERE id LIKE :id")
    suspend fun getItemById(id: String): TodoItem?

    @Insert
    suspend fun insertItem(item: TodoItem)

    @Update
    suspend fun updateItem(item: TodoItem)

    @Query("DELETE FROM todo_items WHERE id LIKE :id")
    suspend fun deleteItem(id: String)
}