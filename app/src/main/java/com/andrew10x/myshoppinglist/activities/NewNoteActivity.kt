package com.andrew10x.myshoppinglist.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.method.TextKeyListener.clear
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import com.andrew10x.myshoppinglist.R
import com.andrew10x.myshoppinglist.databinding.ActivityNewNoteBinding
import com.andrew10x.myshoppinglist.entities.NoteItem
import com.andrew10x.myshoppinglist.fragments.NoteFragment
import com.andrew10x.myshoppinglist.utils.HtmlManager
import com.andrew10x.myshoppinglist.utils.MyTouchListener
import com.andrew10x.myshoppinglist.utils.TimeManager
import java.text.SimpleDateFormat
import java.util.*

class NewNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewNoteBinding
    private var note: NoteItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBarSettings()
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getNote()
        init()
        onClickColorPicker()
        actionMenuCallback()
    }

    private fun onClickColorPicker() = with(binding) {
        imRed.setOnClickListener {
            setColorForSelectedText(R.color.picker_red)
        }
        imBlack.setOnClickListener {
            setColorForSelectedText(R.color.picker_black)
        }
        imBlue.setOnClickListener {
            setColorForSelectedText(R.color.picker_blue)
        }
        imYellow.setOnClickListener {
            setColorForSelectedText(R.color.picker_yellow)
        }
        imGreen.setOnClickListener {
            setColorForSelectedText(R.color.picker_green)
        }
        imOrange.setOnClickListener {
            setColorForSelectedText(R.color.picker_orange)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        binding.colorPicker.setOnTouchListener(MyTouchListener())
    }

    private fun getNote() {
        val sNote = intent.getSerializableExtra(NoteFragment.NewNoteKey)
        if(sNote != null){
            note = sNote as NoteItem
            fillNote()
        }

    }

    private fun fillNote() = with(binding) {
            edTitle.setText(note?.title)
            edDescription.setText(HtmlManager.getFromHtml(note?.content!!).trim())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nw_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       if(item.itemId == R.id.id_save){
               setMainResult()
       }
        else if(item.itemId == android.R.id.home){
            finish()
       }
       else if(item.itemId == R.id.id_bold){
           setBoldForSelectedText()
       }
        else if(item.itemId == R.id.id_color){
            if(binding.colorPicker.isShown){
                closeColorPicker()
            }
            else {
                openColorPicker()
            }
       }
        return super.onOptionsItemSelected(item)
    }

    private fun setBoldForSelectedText() = with(binding) {
        val startPos = edDescription.selectionStart
        val endPos = edDescription.selectionEnd

        val styles = edDescription.text.getSpans(startPos, endPos, StyleSpan::class.java)
        var boldStyle: StyleSpan? = null
        if(styles.isNotEmpty()) {
            edDescription.text.removeSpan(styles[0])
        }
        else {
            boldStyle = StyleSpan(Typeface.BOLD)
        }

        edDescription.text.setSpan(boldStyle, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE )
        edDescription.text.trim()
        edDescription.setSelection(startPos)
    }

    private fun setColorForSelectedText(colorId: Int) = with(binding) {
        val startPos = edDescription.selectionStart
        val endPos = edDescription.selectionEnd

        val styles = edDescription.text.getSpans(startPos, endPos, ForegroundColorSpan::class.java)
        if(styles.isNotEmpty())
            edDescription.text.removeSpan(styles[0])

        edDescription.text.setSpan(ForegroundColorSpan(ContextCompat.getColor(
            this@NewNoteActivity, colorId)), startPos, endPos,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE )
        edDescription.text.trim()
        edDescription.setSelection(startPos)
    }

    private fun setMainResult() {
        var editState = "new"
        val tempNote: NoteItem?
        if(note == null){
            tempNote = createNewNote()
        }
        else {
            editState = "update"
            tempNote = updateNote()
        }

        val i = Intent().apply {
            putExtra(NoteFragment.NewNoteKey, tempNote)
            putExtra(NoteFragment.EditStateKey, editState)
        }
        setResult(RESULT_OK, i)
        finish()
    }

    private fun updateNote(): NoteItem? = with(binding) {
        return note?.copy(title = edTitle.text.toString(),
        content = HtmlManager.toHtml(edDescription.text))
    }

    private fun createNewNote(): NoteItem {
        return NoteItem(null, binding.edTitle.text.toString(),
            HtmlManager.toHtml(binding.edDescription.text), TimeManager.getCurrentTime(), "")
    }



    fun actionBarSettings() {
        val ab = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)
    }

    private fun openColorPicker(){
        binding.colorPicker.visibility = View.VISIBLE
        val openAnim = AnimationUtils.loadAnimation(this, R.anim.open_color_picker)
        binding.colorPicker.startAnimation(openAnim)
    }

    private fun closeColorPicker(){
        val openAnim = AnimationUtils.loadAnimation(this, R.anim.close_color_picker)
        openAnim.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                binding.colorPicker.visibility = View.GONE
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        })
        binding.colorPicker.startAnimation(openAnim)
    }

    private fun actionMenuCallback() {
        val actionCallback = object: ActionMode.Callback {
            override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                p1?.clear()
                return true
            }

            override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                p1?.clear()
                return true
            }

            override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
                return true
            }

            override fun onDestroyActionMode(p0: ActionMode?) {

            }

        }
        binding.edDescription.customSelectionActionModeCallback = actionCallback
    }


}