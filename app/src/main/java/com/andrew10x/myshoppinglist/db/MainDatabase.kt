package com.andrew10x.myshoppinglist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.andrew10x.myshoppinglist.entities.LibraryItem
import com.andrew10x.myshoppinglist.entities.NoteItem
import com.andrew10x.myshoppinglist.entities.ShopListItem
import com.andrew10x.myshoppinglist.entities.ShopListNameItem

@Database (entities = [LibraryItem::class, ShopListItem::class, ShopListNameItem::class, NoteItem::class], version = 1)
abstract class MainDataBase: RoomDatabase() {

    abstract fun getDao(): Dao

    companion object {
        @Volatile
        private var INSTANCE: MainDataBase? = null
        fun getDataBase(context: Context): MainDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDataBase::class.java,
                    "shopping_list.db"
                ).build()
                instance
            }
        }
    }
}