package com.andrew10x.myshoppinglist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.andrew10x.myshoppinglist.R
import com.andrew10x.myshoppinglist.databinding.ActivityMainBinding
import com.andrew10x.myshoppinglist.dialogs.NewListDialog
import com.andrew10x.myshoppinglist.fragments.FragmentManager
import com.andrew10x.myshoppinglist.fragments.NoteFragment
import com.andrew10x.myshoppinglist.fragments.ShopListNamesFragment

class MainActivity : AppCompatActivity(), NewListDialog.Listener {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
        setBottomNavListener()

    }

    private fun setBottomNavListener() {
        binding.bNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.settings->{
                    Log.d("MyLog", "Settings")
                }
                R.id.notes->{
                    FragmentManager.setFragment(NoteFragment.newInstance(), this)
                }
                R.id.shop_list->{
                    FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
                }
                R.id.new_item->{
                    FragmentManager.currentFrag?.onClickNew()

                }
            }
            true
        }
    }

    override fun onClick(name: String) {
        Log.d("MyLog", "Name: $name")
    }
}