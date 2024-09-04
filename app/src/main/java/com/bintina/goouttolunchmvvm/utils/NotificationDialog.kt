package com.bintina.goouttolunchmvvm.utils

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.databinding.FragmentNotificationDialogBinding
import com.bintina.goouttolunchmvvm.model.UserWithRestaurant
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.getRestaurantUsers
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.getUsersRestaurant
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.getUsersRestaurantName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class NotificationDialog : DialogFragment() {
    private var _binding: FragmentNotificationDialogBinding? = null
    private val binding get() = _binding!!

    private var currentUserRestaurant: UserWithRestaurant? = null
    private var currentUserId = ""

    companion object {
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
    ): View {
        _binding = FragmentNotificationDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //This should fetch the title instead.
        val messageDetail = arguments?.getString(ARG_MESSAGE_DETAIL)

        // Show loading message
        binding.notificationTv.text = getString(R.string.fetching_data_txt)

        // Fetch data and update the UI
        CoroutineScope(Dispatchers.Main).launch {
            val fullText = composeNotification()
            binding.notificationTv.text = fullText
        }

    }


    private suspend fun composeNotification(): String {
        return withContext(Dispatchers.IO) {
            val currentUserName = MyApp.currentUser!!.displayName
            currentUserId = MyApp.currentUser!!.uid
            currentUserRestaurant = getUsersRestaurant(currentUserId)
            val currentRestaurant = currentUserRestaurant
            var fullNotificationText = ""

            if (currentRestaurant != null) {
                val currentUserRestaurantChoice = currentRestaurant.restaurant!!.name
                val currentRestaurantVicinity = currentRestaurant.restaurant!!.address
                val currentRestaurantId = currentRestaurant.restaurant!!.restaurantId

                val attendingCoworkerNames = listAttendingUsers(currentRestaurantId)
                fullNotificationText =
                    if (attendingCoworkerNames.isNotEmpty()) {
                        val attendingNames = attendingCoworkerNames.joinToString(", ")
                        "$currentUserName, you are going to $currentUserRestaurantChoice, $currentRestaurantVicinity. $attendingNames will be joining you."
                    } else {
                        "$currentUserName, you are going to $currentUserRestaurantChoice, $currentRestaurantVicinity. No coworkers are joining you."
                    }

            } else {
                fullNotificationText = "$currentUserName, you have not chosen a restaurant today"

            }

            fullNotificationText

        }
    }

    private suspend fun listAttendingUsers(restaurantId: String): List<String> {
        return withContext(Dispatchers.IO) {
            val currentUsersAttendingObjects =
                MyApp.db.userDao().getUsersForRestaurant(restaurantId)

            currentUsersAttendingObjects
                .filter { it.uid != currentUserId } // Exclude the current user
                .map { it.displayName } // Transform each LocalUser to their displayName and remove nulls
        }
    }
}
