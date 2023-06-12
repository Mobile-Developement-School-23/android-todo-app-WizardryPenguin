package ru.winpenguin.todoapp

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

class TodoItemsRepository {

    private val _items = MutableStateFlow<List<TodoItem>>(mockedItems)
    val items: StateFlow<List<TodoItem>> = _items.asStateFlow()

    private fun getItems(): List<TodoItem> {
        return _items.value
    }

    fun addItem(item: TodoItem) {
        _items.update {
            _items.value + item
        }
    }

    fun getById(id: String): TodoItem? {
        return getItems().find { it.id == id }
    }

    fun updateItem(updatedItem: TodoItem) {
        _items.update {
            val itemsList = _items.value.toMutableList()
            val index = itemsList.indexOfFirst { item -> item.id == updatedItem.id }
            if (index == -1) {
                return@update _items.value
            }

            itemsList.removeAt(index)
            itemsList.add(index, updatedItem)
            itemsList.toList()
        }
    }

    private companion object {
        val mockedItems = listOf(
            TodoItem(
                id = "1",
                text = "Купить что-то",
                importance = Importance.LOW,
                isDone = false,
                creationDate = LocalDateTime.of(2023, Month.JUNE, 1, 12, 0, 0),
            ),
            TodoItem(
                id = "2",
                text = "Купить что-то, где-то, зачем-то, но зачем не очень понятно",
                importance = Importance.NORMAL,
                isDone = false,
                creationDate = LocalDateTime.of(2023, Month.JUNE, 4, 10, 20, 0),
                deadline = LocalDate.now().minusDays(1)
            ),
            TodoItem(
                id = "3",
                text = "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обрезается длинный длинный текст",
                importance = Importance.HIGH,
                isDone = false,
                creationDate = LocalDateTime.of(2023, Month.JUNE, 1, 13, 2, 0),
            ),
            TodoItem(
                id = "4",
                text = "Продать что-то",
                importance = Importance.NORMAL,
                isDone = true,
                creationDate = LocalDateTime.of(2023, Month.JUNE, 2, 12, 0, 0),
            ),
            TodoItem(
                id = "5",
                text = "Посмотреть сериал",
                importance = Importance.HIGH,
                isDone = false,
                creationDate = LocalDateTime.of(2023, Month.MAY, 27, 13, 4, 0),
            ),
            TodoItem(
                id = "6",
                text = "Погладить кошку",
                importance = Importance.HIGH,
                isDone = true,
                creationDate = LocalDateTime.of(2023, Month.JUNE, 5, 11, 0, 0),
            ),
            TodoItem(
                id = "7",
                text = "Купить продукты",
                importance = Importance.LOW,
                isDone = false,
                creationDate = LocalDateTime.of(2023, Month.JUNE, 1, 12, 0, 0),
            ),
            TodoItem(
                id = "8",
                text = "Погулять в парке",
                importance = Importance.LOW,
                isDone = false,
                creationDate = LocalDateTime.of(2023, Month.JUNE, 4, 12, 0, 0),
            ),
            TodoItem(
                id = "9",
                text = "Сходить на тренировку",
                importance = Importance.LOW,
                isDone = false,
                creationDate = LocalDateTime.of(2023, Month.JUNE, 1, 12, 0, 0),
            ),
            TodoItem(
                id = "10",
                text = "Пополнить тройку",
                importance = Importance.LOW,
                isDone = false,
                creationDate = LocalDateTime.of(2023, Month.JUNE, 8, 12, 0, 0),
            ),
            TodoItem(
                id = "11",
                text = "Фьючерсный контракт — это договор между покупателем и продавцом о покупке/продаже какого-то актива в будущем. Стороны заранее оговаривают, через какой срок и по какой цене состоится сделка.\n" +
                        "\n" +
                        "Например, сейчас одна акция «Лукойла» стоит около 5700 рублей. Фьючерс на акции «Лукойла» — это, например, договор между покупателем и продавцом о том, что покупатель купит акции «Лукойла» у продавца по цене 5700 рублей через 3 месяца. При этом не важно, какая цена будет у акций через 3 месяца: цена сделки между покупателем и продавцом все равно останется 5700 рублей. Если реальная цена акции через три месяца не останется прежней, одна из сторон в любом случае понесет убытки.",
                importance = Importance.NORMAL,
                isDone = false,
                creationDate = LocalDateTime.of(2023, Month.JUNE, 7, 12, 0, 0),
            ),
            TodoItem(
                id = "12",
                text = "Убраться в квартире",
                importance = Importance.LOW,
                isDone = true,
                creationDate = LocalDateTime.of(2023, Month.JUNE, 3, 12, 0, 0),
            ),
        )
    }
}