package com.bintina.goouttolunchmvvm.user.coworkers.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bintina.goouttolunchmvvm.user.coworkers.view.adapter.Adapter
import com.bintina.goouttolunchmvvm.databinding.FragmentCoworkersListBinding
import com.bintina.goouttolunchmvvm.user.coworkers.viewmodel.CoworkersViewModel
import com.bintina.goouttolunchmvvm.user.coworkers.viewmodel.injection.Injection
import com.bintina.goouttolunchmvvm.user.login.viewmodel.UserViewModel
import com.bintina.goouttolunchmvvm.utils.MyApp

class CoworkerListFragment() : Fragment() {

    lateinit var viewModel: CoworkersViewModel
    lateinit var adapter: Adapter

    private var _binding: FragmentCoworkersListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoworkersListBinding.inflate(inflater, container, false)

        viewModel = Injection.provideCoworkerViewModel(MyApp.myContext)
        initializeViews()

        Log.d("CoworkerFragmentLog", "CoworkerListFragment inflated")
        return binding.root
    }

    private fun initializeViews() {
        adapter = Adapter()
        binding.coworkerRecyclerContainer.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}