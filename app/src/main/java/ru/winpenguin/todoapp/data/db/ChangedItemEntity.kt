package ru.winpenguin.todoapp.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "changed_items")
data class ChangedItemEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val _id: Long = 0L,

    @ColumnInfo(name = "item_id")
    val itemId: String,

    @ColumnInfo(name = "change_type")
    val changeType: ChangeType
) : Comparable<ChangedItemEntity> {

    override fun compareTo(other: ChangedItemEntity): Int {
        return this.changeType.compareTo(other.changeType)
    }
}

enum class ChangeType {
    ADD,
    UPDATE,
    REMOVE
}