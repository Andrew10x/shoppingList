package com.andrew10x.myshoppinglist.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrew10x.myshoppinglist.activities.MainApp
import com.andrew10x.myshoppinglist.activities.ShopListActivity
import com.andrew10x.myshoppinglist.databinding.FragmentShopListNamesBinding
import com.andrew10x.myshoppinglist.db.MainViewModel
import com.andrew10x.myshoppinglist.db.ShopListNameAdapter
import com.andrew10x.myshoppinglist.dialogs.DeleteDialog
import com.andrew10x.myshoppinglist.dialogs.NewListDialog
import com.andrew10x.myshoppinglist.entities.ShopListNameItem
import com.andrew10x.myshoppinglist.utils.TimeManager


class ShopListNamesFragment : BaseFragment(), ShopListNameAdapter.Listener {

    private lateinit var binding: FragmentShopListNamesBinding
    private lateinit var adapter: ShopListNameAdapter

    private  val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }
    override fun onClickNew() {
        NewListDialog.showDialog(activity as AppCompatActivity, object: NewListDialog.Listener{
            override fun onClick(name: String) {
                val shopListName = ShopListNameItem(
                    null, name, TimeManager.getCurrentTime(), 0,
                    0, ""
                )
                mainViewModel.insertShoppingListName(shopListName)
            }

        }, "")
    }

    override fun  onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopListNamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        observer()
    }

    private fun observer() {
        mainViewModel.allShopListNamesItem.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun initRcView() = with(binding) {
        rcView.layoutManager = LinearLayoutManager(activity)
        adapter = ShopListNameAdapter(this@ShopListNamesFragment)
        rcView.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = ShopListNamesFragment()

    }

    override fun deleteItem(id: Int) {
        DeleteDialog.showDialog(context as AppCompatActivity, object: DeleteDialog.Listener {
            override fun onClick() {
                mainViewModel.deleteShopListName(id)
            }

        })
    }

    override fun onClickItem(shopListNameItem: ShopListNameItem) {
        val i = Intent(activity, ShopListActivity:: class.java).apply {
            putExtra(ShopListActivity.SHOP_LIST_NAME, shopListNameItem)
        }
        startActivity(i)
    }

    override fun editItem(shopListNameItem: ShopListNameItem) {
        NewListDialog.showDialog(activity as AppCompatActivity, object: NewListDialog.Listener{
            override fun onClick(name: String) {

                mainViewModel.updateListName(shopListNameItem.copy(name = name))
            }

        }, shopListNameItem.name)
    }

}