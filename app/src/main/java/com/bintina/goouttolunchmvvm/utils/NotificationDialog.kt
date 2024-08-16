package com.bintina.goouttolunchmvvm.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bintina.goouttolunchmvvm.databinding.FragmentNotificationDialogBinding
import com.bintina.goouttolunchmvvm.databinding.FragmentSettingsBinding

class NotificationDialog: DialogFragment() {
    private var _binding: FragmentNotificationDialogBinding? = null
    private val binding get() = _binding!!
    companion object{
        private const val ARG_MESSAGE_DETAIL = "message_detail"

        fun newInstance(messageDetail: String): NotificationDialog {
            val fragment = NotificationDialog()
            val args = Bundle()
            args.putString(ARG_MESSAGE_DETAIL, messageDetail)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val messageDetail = arguments?.getString(ARG_MESSAGE_DETAIL)
        binding.notificationTv.text =messageDetail

    }
}