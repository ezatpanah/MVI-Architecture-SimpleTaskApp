package com.ezatpanah.simpletodoapp_mvi.ui.deleteall

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ezatpanah.simpletodoapp_mvi.databinding.FragmentDeleteAllBinding
import com.ezatpanah.simpletodoapp_mvi.viewmodel.deleteall.DeleteAllIntent
import com.ezatpanah.simpletodoapp_mvi.viewmodel.deleteall.DeleteAllState
import com.ezatpanah.simpletodoapp_mvi.viewmodel.deleteall.DeleteAllViewModel
import com.ezatpanah.simpletodoapp_mvi.viewmodel.main.MainIntent
import com.ezatpanah.simpletodoapp_mvi.viewmodel.main.MainState
import com.ezatpanah.simpletodoapp_mvi.viewmodel.main.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DeleteAllFragment : DialogFragment() {

    private lateinit var binding: FragmentDeleteAllBinding

    private val viewModel: DeleteAllViewModel by viewModels()

    companion object {
        const val TAG = "DeleteAllFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDeleteAllBinding.inflate(inflater, container, false)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {


            lifecycleScope.launch {
                viewModel.state.collect { state ->
                    when (state) {
                        is DeleteAllState.Idle -> {}
                        is DeleteAllState.DeleteAllTask -> {
                            Snackbar.make(binding.root, "All Tasks deleted!", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }


            btnPositive.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.handleIntent(DeleteAllIntent.DeleteAllTask)
                }
                dismiss()
            }
            btnNegative.setOnClickListener {
                dismiss()
            }
        }

    }

}