package com.andrew10x.myshoppinglist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrew10x.myshoppinglist.R
import com.andrew10x.myshoppinglist.databinding.ActivityShopListBinding
import com.andrew10x.myshoppinglist.db.MainViewModel
import com.andrew10x.myshoppinglist.db.ShopListItemAdapter
import com.andrew10x.myshoppinglist.entities.ShopListItem
import com.andrew10x.myshoppinglist.entities.ShopListNameItem

class ShopListActivity2 : AppCompatActivity(), ShopListItemAdapter.Listener {
    private lateinit var binding: ActivityShopListBinding
    private var shopListNameItem: ShopListNameItem? = null
    private lateinit var saveItem: MenuItem
    private var edItem: EditText? = null
    private var adapter: ShopListItemAdapter? = null


    private  val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopListBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_shop_list2)
        init()
        initRcView()
        listItemObserver()
    }

    private fun addNewShopItem() {
        if(edItem?.text.toString().isEmpty())
            return
        val item = ShopListItem(null, edItem?.text.toString(), null, 0,
            shopListNameItem?.id!!, 0)
        edItem?.setText("")
        mainViewModel.insertShopItem(item)
    }

    private fun listItemObserver() {
        mainViewModel.getAllItemsFromList(shopListNameItem?.id!!).observe(this) {
            Log.d("MyLog", "dsfds $it")
            adapter?.submitList(it)
            Log.d("MyLog", "dsfds $it")
            binding.tvEmpty.visibility = View.GONE
            /*binding.tvEmpty.visibility = if(it.isEmpty()){
                View.VISIBLE
            }
            else {
                View.GONE
            }*/
        }
    }

    private fun initRcView()= with(binding) {
        adapter = ShopListItemAdapter(this@ShopListActivity2)
        rcView.layoutManager = LinearLayoutManager(this@ShopListActivity2)
        rcView.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.save_item)
            addNewShopItem()
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.shop_list_menu, menu)
        saveItem = menu?.findItem(R.id.save_item)!!
        val newItem = menu.findItem(R.id.new_item)
        edItem = newItem.actionView.findViewById(R.id.edNewShopItem) as EditText
        newItem.setOnActionExpandListener(expandActionView())
        saveItem.isVisible = false
        return true
    }

    private fun expandActionView(): MenuItem.OnActionExpandListener{
        return object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                saveItem.isVisible = true
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                saveItem.isVisible = false
                invalidateOptionsMenu()
                return true
            }
        }
    }

    private fun init() {
        shopListNameItem = intent.getSerializableExtra(SHOP_LIST_NAME) as ShopListNameItem
    }

    companion object {
        const val SHOP_LIST_NAME = "shop_list_name"
    }

    override fun deleteItem(id: Int) {

    }

    override fun onClickItem(shopListNameItem: ShopListNameItem) {

    }

    override fun editItem(shopListNameItem: ShopListNameItem) {

    }
}