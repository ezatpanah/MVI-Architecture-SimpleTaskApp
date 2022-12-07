package com.ezatpanah.simplenoteapp_mvi.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ezatpanah.simplenoteapp_mvi.R
import com.ezatpanah.simplenoteapp_mvi.adapter.NoteAdapter
import com.ezatpanah.simplenoteapp_mvi.databinding.ActivityMainBinding
import com.ezatpanah.simplenoteapp_mvi.db.NoteEntity
import com.ezatpanah.simplenoteapp_mvi.ui.add.AddNoteFragment
import com.ezatpanah.simplenoteapp_mvi.utils.Constants.BUNDLE_ID
import com.ezatpanah.simplenoteapp_mvi.utils.Constants.DELETE
import com.ezatpanah.simplenoteapp_mvi.utils.Constants.EDIT
import com.ezatpanah.simplenoteapp_mvi.utils.Constants.HIGH
import com.ezatpanah.simplenoteapp_mvi.utils.Constants.LOW
import com.ezatpanah.simplenoteapp_mvi.utils.Constants.NORMAL
import com.ezatpanah.simplenoteapp_mvi.viewmodel.add.AddNoteViewModel
import com.ezatpanah.simplenoteapp_mvi.viewmodel.main.MainIntent
import com.ezatpanah.simplenoteapp_mvi.viewmodel.main.MainState
import com.ezatpanah.simplenoteapp_mvi.viewmodel.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var noteAdapter: NoteAdapter

    @Inject
    lateinit var noteEntity: NoteEntity

    //Other
    private val viewModel: MainViewModel by viewModels()
    private var selectedItem = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {

            setSupportActionBar(notesToolbar)

            btnAddNote.setOnClickListener { AddNoteFragment().show(supportFragmentManager, AddNoteFragment().tag) }

            viewModel.handleIntent(MainIntent.LoadAllNotes)

            lifecycleScope.launch {
                viewModel.state.collect { state ->
                    when (state) {
                        is MainState.Empty -> {
                            emptyLay.visibility = View.VISIBLE
                            noteList.visibility = View.GONE
                        }
                        is MainState.LoadNotes -> {
                            emptyLay.visibility = View.GONE
                            noteList.visibility = View.VISIBLE

                            noteAdapter.setData(state.list)
                            noteList.apply {
                                layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                                adapter = noteAdapter
                            }

                            noteAdapter.setOnItemClickListener { entity, type ->
                                when (type) {
                                    EDIT -> {
                                        viewModel.handleIntent(MainIntent.ClickToDetail(entity.id))
                                    }
                                    DELETE -> {
                                        noteEntity.id = entity.id
                                        noteEntity.title = entity.title
                                        noteEntity.desc = entity.desc
                                        noteEntity.cat = entity.cat
                                        noteEntity.pr = entity.pr
                                        viewModel.handleIntent(MainIntent.DeleteNote(noteEntity))
                                    }
                                }
                            }
                        }
                        is MainState.DeleteNote -> {
                            //For example show toast, snack bar and more ...
                        }
                        is MainState.GoToDetail -> {
                            val noteFragment = AddNoteFragment()
                            val bundle = Bundle()
                            bundle.putInt(BUNDLE_ID, state.id)
                            noteFragment.arguments = bundle
                            noteFragment.show(supportFragmentManager, AddNoteFragment().tag)
                        }
                    }
                }
            }
            //Filter
            notesToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.actionFilter -> {
                        priorityFilter()
                        return@setOnMenuItemClickListener true
                    }
                    else -> {
                        return@setOnMenuItemClickListener false
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        val search = menu.findItem(R.id.actionSearch)
        val searchView = search.actionView as SearchView
        searchView.queryHint = getString(R.string.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.handleIntent(MainIntent.SearchNote(newText))
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun priorityFilter() {
        val builder = AlertDialog.Builder(this)

        val priority = arrayOf("All", HIGH, NORMAL, LOW)
        builder.setSingleChoiceItems(priority, selectedItem) { dialog, item ->
            when (item) {
                0 -> {
                    viewModel.handleIntent(MainIntent.LoadAllNotes)
                }
                in 1..3 -> {
                    viewModel.handleIntent(MainIntent.FilterNote(priority[item]))
                }
            }
            selectedItem = item
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}