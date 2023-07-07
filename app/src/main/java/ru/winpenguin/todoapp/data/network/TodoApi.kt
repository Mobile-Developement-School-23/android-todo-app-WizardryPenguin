package ru.winpenguin.todoapp.data.network

import retrofit2.Response
import retrofit2.http.*
import ru.winpenguin.todoapp.data.network.models.SingleTodoItemDto
import ru.winpenguin.todoapp.data.network.models.TodoItemsListDto

const val LAST_KNOWN_REVISION_HEADER = "X-Last-Known-Revision"

interface TodoApi {

    @GET("todobackend/list")
    suspend fun getAllItems(): Response<TodoItemsListDto>

    @PATCH("todobackend/list")
    suspend fun updateTodoItems(
        @Header(LAST_KNOWN_REVISION_HEADER) revision: Int,
        @Body items: TodoItemsListDto
    ): Response<TodoItemsListDto>

    @GET("todobackend/list/{id}")
    suspend fun getOneItem(@Path("id") id: String): Response<SingleTodoItemDto>

    @POST("todobackend/list")
    suspend fun addNewItem(
        @Header(LAST_KNOWN_REVISION_HEADER) revision: Int,
        @Body item: SingleTodoItemDto
    ): Response<SingleTodoItemDto>

    @PUT("todobackend/list/{id}")
    suspend fun changeItem(
        @Header(LAST_KNOWN_REVISION_HEADER) revision: Int,
        @Path("id") id: String,
        @Body item: SingleTodoItemDto
    ): Response<SingleTodoItemDto>

    @DELETE("todobackend/list/{id}")
    suspend fun deleteItem(
        @Header(LAST_KNOWN_REVISION_HEADER) revision: Int,
        @Path("id") id: String,
    ): Response<SingleTodoItemDto>
}