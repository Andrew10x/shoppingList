package com.andrew10x.myshoppinglist.activities

import android.app.Application
import com.andrew10x.myshoppinglist.db.MainDataBase

class MainApp : Application() {

    val database by lazy {MainDataBase.getDataBase(this)}
}