package com.ezatpanah.simpletodoapp_mvi.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ezatpanah.simpletodoapp_mvi.databinding.FragmentAddTaskBinding
import com.ezatpanah.simpletodoapp_mvi.db.TaskEntity
import com.ezatpanah.simpletodoapp_mvi.utils.Constants.BUNDLE_ID
import com.ezatpanah.simpletodoapp_mvi.utils.Constants.EDIT
import com.ezatpanah.simpletodoapp_mvi.utils.Constants.NEW
import com.ezatpanah.simpletodoapp_mvi.utils.getIndexFromList
import com.ezatpanah.simpletodoapp_mvi.utils.setupList
import com.ezatpanah.simpletodoapp_mvi.viewmodel.add.AddTaskIntent
import com.ezatpanah.simpletodoapp_mvi.viewmodel.add.AddTaskState
import com.ezatpanah.simpletodoapp_mvi.viewmodel.add.AddTaskViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddTaskFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var entity: TaskEntity

    private val viewModel: AddTaskViewModel by viewModels()

    private var type = ""
    private var cat = ""
    private var pr = ""
    private val categoriesList: MutableList<String> = mutableListOf()
    private val prioriesList: MutableList<String> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddTaskBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noteId = arguments?.getInt(BUNDLE_ID) ?: 0

        type = if (noteId > 0) EDIT else NEW

        binding.apply {

            imgClose.setOnClickListener {
                dismiss()
            }

            lifecycleScope.launchWhenCreated {
                viewModel.addTaskIntent.send(AddTaskIntent.SpinnerList)

                if (type == EDIT) {
                    viewModel.addTaskIntent.send(AddTaskIntent.TaskDetail(noteId))
                }

                viewModel.state.collect { state ->
                    when (state) {
                        is AddTaskState.Idle -> {}
                        is AddTaskState.SpinnerData -> {
                            categoriesList.addAll(state.catList)
                            categoriesSpinner.setupList(state.catList) {
                                cat = it
                            }

                            prioriesList.addAll(state.prList)
                            prioritySpinner.setupList(state.prList) {
                                pr = it
                            }
                        }
                        is AddTaskState.SaveTask -> {
                            dismiss()
                        }
                        is AddTaskState.ErrorMsg -> {
                            Toast.makeText(requireContext(), state.msg, Toast.LENGTH_SHORT).show()
                        }
                        is AddTaskState.TaskDetail -> {
                            titleEdt.setText(state.entity.title)
                            descEdt.setText(state.entity.desc)
                            categoriesSpinner.setSelection(categoriesList.getIndexFromList(state.entity.cat))
                            prioritySpinner.setSelection(prioriesList.getIndexFromList(state.entity.pr))
                        }
                        is AddTaskState.UpdateTask -> {
                            dismiss()
                        }

                    }
                }
            }

            saveNote.setOnClickListener {

                val title = titleEdt.text.toString()
                val desc = descEdt.text.toString()
                entity.id = noteId
                entity.title = title
                entity.desc = desc
                entity.cat = cat
                entity.pr = pr

                lifecycleScope.launch {
                    if (type == NEW) {
                        viewModel.addTaskIntent.send(AddTaskIntent.SaveTask(entity))
                    } else {
                        viewModel.addTaskIntent.send(AddTaskIntent.UpdateTask(entity))
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        _binding = null
    }

}