package com.bintina.goouttolunchmvvm.user.coworkers.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintina.goouttolunchmvvm.databinding.FragmentCoworkersListBinding
import com.bintina.goouttolunchmvvm.user.coworkers.view.adapter.Adapter
import com.bintina.goouttolunchmvvm.user.model.User
import com.bintina.goouttolunchmvvm.user.viewmodel.UserViewModel
import com.bintina.goouttolunchmvvm.utils.MyApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CoworkerListFragment : Fragment(), LifecycleOwner {

    lateinit var viewModel: UserViewModel
    lateinit var adapter: Adapter

    private var _binding: FragmentCoworkersListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoworkersListBinding.inflate(inflater, container, false)


        viewModel = ViewModelProvider(
            this,
            com.bintina.goouttolunchmvvm.user.viewmodel.injection.Injection.provideViewModelFactory(
                requireContext()
            )
        ).get(UserViewModel::class.java)


        initializeViews()

        Log.d("CoworkerFragmentLog", "CoworkerListFragment inflated")
        return binding.root
    }

    private fun initializeViews() {


        binding.coworkerRecyclerContainer.layoutManager = LinearLayoutManager(requireContext())
        adapter = Adapter(mutableListOf())
        binding.coworkerRecyclerContainer.adapter = adapter

        // Observe coworkerList and update the adapter when it changes
        viewModel.coworkerList.observe(viewLifecycleOwner, { users ->
            adapter.updateData(users)
/*            adapter.coworkerList = users
            adapter.notifyDataSetChanged()*/
        })


        viewModel.getCoworkers(requireContext())
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        Log.d("CoworkerFragmentLog", "CoworkerListFragment destroyed")
    }
}