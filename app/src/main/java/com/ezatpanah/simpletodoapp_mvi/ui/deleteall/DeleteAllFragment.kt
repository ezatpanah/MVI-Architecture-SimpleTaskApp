package com.ezatpanah.simpletodoapp_mvi.ui.deleteall

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ezatpanah.simpletodoapp_mvi.databinding.FragmentDeleteAllBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DeleteAllFragment : DialogFragment() {

    private lateinit var binding: FragmentDeleteAllBinding


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
            btnPositive.setOnClickListener {
                dismiss()
            }
            btnNegative.setOnClickListener {
                dismiss()
            }
        }

    }

}