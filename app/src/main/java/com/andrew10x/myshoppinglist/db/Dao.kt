package com.andrew10x.myshoppinglist.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.andrew10x.myshoppinglist.entities.NoteItem
import com.andrew10x.myshoppinglist.entities.ShopListNameItem
import com.andrew10x.myshoppinglist.entities.ShopListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Query ("select * from note_list")
    fun getAllNotes(): Flow<List<NoteItem>>

    @Query ("select * from shopping_list_names")
    fun getAllShopListNames(): Flow<List<ShopListNameItem>>

    @Query ("select * from shop_list_item where listId like :listId")
    fun getAllShopListItems(listId: Int): Flow<List<ShopListItem>>

    @Query ("delete  from note_list where id is :id")
    suspend fun deleteNote(id: Int)

    @Query ("delete  from shopping_list_names where id is :id")
    suspend fun deleteShopListName(id: Int)

    @Insert
    suspend fun insertNote(note: NoteItem)

    @Insert
    suspend fun insertItem(shopListItem: ShopListItem)

    @Insert
    suspend fun insertShopListName(nameItem: ShopListNameItem)

    @Update
    suspend fun updateNote(note: NoteItem)

    @Update
    suspend fun updateListName(shopListNameItem: ShopListNameItem)
}