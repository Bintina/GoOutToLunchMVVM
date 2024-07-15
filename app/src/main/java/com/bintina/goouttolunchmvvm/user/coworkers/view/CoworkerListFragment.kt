package com.bintina.goouttolunchmvvm.user.coworkers.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintina.goouttolunchmvvm.databinding.FragmentCoworkersListBinding
import com.bintina.goouttolunchmvvm.user.coworkers.view.adapter.Adapter
import com.bintina.goouttolunchmvvm.user.viewmodel.UserViewModel

class CoworkerListFragment : Fragment(), LifecycleOwner {

    val TAG = "CoworkerListFragLog"
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
        viewModel.getLocalCoworkers()

        initializeViews()

        Log.d(TAG, "CoworkerListFragment inflated")
        return binding.root
    }

    private fun initializeViews() {

        binding.coworkerRecyclerContainer.layoutManager = LinearLayoutManager(requireContext())
        adapter = com.bintina.goouttolunchmvvm.user.coworkers.view.adapter.Adapter()

        // Observe coworkerList and update the adapter when it changes
        viewModel.coworkerList.observe(viewLifecycleOwner, { coworkerList ->
            adapter.coworkerList = coworkerList
            adapter.notifyDataSetChanged()
        })
        Log.d(TAG, "Adapter list has ${adapter.coworkerList.size} items")
        binding.coworkerRecyclerContainer.adapter = adapter
        /*            adapter.coworkerList = users
                })
                    */


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        Log.d("CoworkerFragmentLog", "CoworkerListFragment destroyed")
    }
}