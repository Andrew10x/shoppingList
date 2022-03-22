package com.andrew10x.myshoppinglist.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrew10x.myshoppinglist.R
import com.andrew10x.myshoppinglist.activities.MainApp
import com.andrew10x.myshoppinglist.activities.NewNoteActivity
import com.andrew10x.myshoppinglist.databinding.FragmentNoteBinding
import com.andrew10x.myshoppinglist.db.MainViewModel
import com.andrew10x.myshoppinglist.db.NoteAdapter
import com.andrew10x.myshoppinglist.entities.NoteItem


class NoteFragment : BaseFragment(), NoteAdapter.Listener {

    private lateinit var binding: FragmentNoteBinding
    private lateinit var editLauncher: ActivityResultLauncher<Intent>
    private lateinit var adapter:NoteAdapter
    private  val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }
    override fun onClickNew() {
        editLauncher.launch(Intent(activity, NewNoteActivity::class.java))
    }

    override fun  onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        onEditResult()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        observer()
    }

    private fun observer() {
        mainViewModel.allNotes.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun initRcView() = with(binding) {
        rcViewNote.layoutManager = LinearLayoutManager(activity)
        adapter = NoteAdapter(this@NoteFragment)
        rcViewNote.adapter = adapter
    }

    private fun onEditResult() {
        editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK) {
                val editState = it.data?.getStringExtra(EditStateKey)

                if(editState == "update"){
                    mainViewModel.updateNote(it.data?.getSerializableExtra(NewNoteKey) as NoteItem)
                }else{
                    mainViewModel.insertNote(it.data?.getSerializableExtra(NewNoteKey) as NoteItem)
                }

            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = NoteFragment()
        const val NewNoteKey = "new_note_key"
        const val EditStateKey = "edit_state_key"

    }

    override fun deleteItem(id: Int) {
        mainViewModel.deleteNote(id)
    }

    override fun onClickItem(note: NoteItem) {
        val intent = Intent(activity, NewNoteActivity::class.java).apply{
            putExtra(NewNoteKey, note)
        }

        editLauncher.launch(intent)
    }
}