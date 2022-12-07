package com.ezatpanah.simpletodoapp_mvi.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezatpanah.simpletodoapp_mvi.R
import com.ezatpanah.simpletodoapp_mvi.adapter.TaskAdapter
import com.ezatpanah.simpletodoapp_mvi.databinding.ActivityMainBinding
import com.ezatpanah.simpletodoapp_mvi.db.TaskEntity
import com.ezatpanah.simpletodoapp_mvi.ui.add.AddTaskFragment
import com.ezatpanah.simpletodoapp_mvi.ui.deleteall.DeleteAllFragment
import com.ezatpanah.simpletodoapp_mvi.utils.Constants.BUNDLE_ID
import com.ezatpanah.simpletodoapp_mvi.utils.Constants.DELETE
import com.ezatpanah.simpletodoapp_mvi.utils.Constants.EDIT
import com.ezatpanah.simpletodoapp_mvi.utils.Constants.HIGH
import com.ezatpanah.simpletodoapp_mvi.utils.Constants.LOW
import com.ezatpanah.simpletodoapp_mvi.utils.Constants.NORMAL
import com.ezatpanah.simpletodoapp_mvi.viewmodel.main.MainIntent
import com.ezatpanah.simpletodoapp_mvi.viewmodel.main.MainState
import com.ezatpanah.simpletodoapp_mvi.viewmodel.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var taskAdapter: TaskAdapter

    @Inject
    lateinit var taskEntity: TaskEntity

    private val viewModel: MainViewModel by viewModels()
    private var selectedItem = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {

            setSupportActionBar(tasksToolbar)

            btnAddTask.setOnClickListener { AddTaskFragment().show(supportFragmentManager, AddTaskFragment().tag) }

            viewModel.handleIntent(MainIntent.LoadAllNotes)

            lifecycleScope.launch {
                viewModel.state.collect { state ->
                    when (state) {
                        is MainState.Empty -> {
                            emptyBody.visibility = View.VISIBLE
                            listBody.visibility = View.GONE
                        }
                        is MainState.LoadNotes -> {
                            emptyBody.visibility = View.GONE
                            listBody.visibility = View.VISIBLE

                            taskAdapter.setData(state.list)
                            taskList.apply {
                                layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                                adapter = taskAdapter
                            }

                            taskAdapter.setOnItemClickListener { entity, type ->
                                when (type) {
                                    EDIT -> {
                                        viewModel.handleIntent(MainIntent.ClickToDetail(entity.id))
                                    }
                                    DELETE -> {
                                        taskEntity.id = entity.id
                                        taskEntity.title = entity.title
                                        taskEntity.desc = entity.desc
                                        taskEntity.cat = entity.cat
                                        taskEntity.pr = entity.pr
                                        viewModel.handleIntent(MainIntent.DeleteNote(taskEntity))
                                    }
                                }
                            }
                        }
                        is MainState.DeleteNote -> {
                            //For example show toast, snack bar and more ...
                        }
                        is MainState.GoToDetail -> {
                            val noteFragment = AddTaskFragment()
                            val bundle = Bundle()
                            bundle.putInt(BUNDLE_ID, state.id)
                            noteFragment.arguments = bundle
                            noteFragment.show(supportFragmentManager, AddTaskFragment().tag)
                        }
                    }
                }
            }
            //Filter
            tasksToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.actionFilter -> {
                        priorityFilter()
                        return@setOnMenuItemClickListener true
                    }
                    R.id.actionSearch -> {
                        return@setOnMenuItemClickListener true
                    }
                    R.id.actionDeleteAll -> {
                        DeleteAllFragment().show(supportFragmentManager, DeleteAllFragment.TAG)
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