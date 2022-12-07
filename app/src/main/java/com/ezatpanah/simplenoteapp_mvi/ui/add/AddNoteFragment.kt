package com.ezatpanah.simplenoteapp_mvi.ui.add

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ezatpanah.simplenoteapp_mvi.R
import com.ezatpanah.simplenoteapp_mvi.databinding.FragmentAddNoteBinding
import com.ezatpanah.simplenoteapp_mvi.db.NoteEntity
import com.ezatpanah.simplenoteapp_mvi.utils.Constants.BUNDLE_ID
import com.ezatpanah.simplenoteapp_mvi.utils.Constants.EDIT
import com.ezatpanah.simplenoteapp_mvi.utils.Constants.NEW
import com.ezatpanah.simplenoteapp_mvi.utils.getIndexFromList
import com.ezatpanah.simplenoteapp_mvi.utils.setupList
import com.ezatpanah.simplenoteapp_mvi.viewmodel.add.AddNoteIntent
import com.ezatpanah.simplenoteapp_mvi.viewmodel.add.AddNoteState
import com.ezatpanah.simplenoteapp_mvi.viewmodel.add.AddNoteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddNoteFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var entity: NoteEntity

    private val viewModel: AddNoteViewModel by viewModels()

    private var type = ""
    private var cat = ""
    private var pr = ""
    private val categoriesList: MutableList<String> = mutableListOf()
    private val prioriesList: MutableList<String> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddNoteBinding.inflate(layoutInflater)
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
                viewModel.addNoteIntent.send(AddNoteIntent.SpinnerList)

                if (type == EDIT) {
                    viewModel.addNoteIntent.send(AddNoteIntent.NoteDetail(noteId))
                }

                viewModel.state.collect { state ->
                    when (state) {
                        is AddNoteState.Idle -> {}
                        is AddNoteState.SpinnerData -> {
                            categoriesList.addAll(state.catList)
                            categoriesSpinner.setupList(state.catList) {
                                cat = it
                            }

                            prioriesList.addAll(state.prList)
                            prioritySpinner.setupList(state.prList) {
                                pr = it
                            }
                        }
                        is AddNoteState.SaveNote -> {
                            dismiss()
                        }
                        is AddNoteState.ErrorMsg -> {
                            Toast.makeText(requireContext(), state.msg, Toast.LENGTH_SHORT).show()
                        }
                        is AddNoteState.NoteDetail -> {
                            titleEdt.setText(state.entity.title)
                            descEdt.setText(state.entity.desc)
                            categoriesSpinner.setSelection(categoriesList.getIndexFromList(state.entity.cat))
                            prioritySpinner.setSelection(prioriesList.getIndexFromList(state.entity.pr))
                        }
                        is AddNoteState.UpdateNote -> {
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
                        viewModel.addNoteIntent.send(AddNoteIntent.SaveNote(entity))
                    } else {
                        viewModel.addNoteIntent.send(AddNoteIntent.UpdateNote(entity))
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