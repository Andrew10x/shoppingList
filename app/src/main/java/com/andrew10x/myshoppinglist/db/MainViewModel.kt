package com.andrew10x.myshoppinglist.db

import android.util.Log
import androidx.lifecycle.*
import com.andrew10x.myshoppinglist.entities.NoteItem
import com.andrew10x.myshoppinglist.entities.ShopListItem
import com.andrew10x.myshoppinglist.entities.ShopListNameItem
import kotlinx.coroutines.launch

class MainViewModel(dataBase: MainDataBase): ViewModel() {
    val dao = dataBase.getDao()
    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()
    val allShopListNamesItem: LiveData<List<ShopListNameItem>> = dao.getAllShopListNames().asLiveData()

    fun getAllItemsFromList(listId: Int): LiveData<List<ShopListItem>>{
        Log.d("MyLog", " ff ${listId}")
        return dao.getAllShopListItems(listId).asLiveData()
    }

    fun insertNote(note: NoteItem) = viewModelScope.launch {
        dao.insertNote(note)
    }

    fun insertShoppingListName(nameItem: ShopListNameItem) = viewModelScope.launch {
        dao.insertShopListName(nameItem)
    }

    fun insertShopItem(shopListItem: ShopListItem) = viewModelScope.launch {
        dao.insertItem(shopListItem)
    }

    fun updateNote(note: NoteItem) = viewModelScope.launch {
        dao.updateNote(note)
    }
    fun updateListName(shopListNameItem: ShopListNameItem) = viewModelScope.launch {
        dao.updateListName(shopListNameItem)
    }

    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNote(id)
    }

    fun deleteShopListName(id: Int) = viewModelScope.launch {
        dao.deleteShopListName(id)
    }

    class MainViewModelFactory(val dataBase: MainDataBase): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MainViewModel::class.java)){
                @Suppress( "UNCHECKED_CAST")
                return MainViewModel(dataBase) as T
            }
            throw IllegalArgumentException("Unknown viewmodel class")
        }

    }
}