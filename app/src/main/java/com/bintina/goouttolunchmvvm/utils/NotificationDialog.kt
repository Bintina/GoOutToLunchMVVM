package com.bintina.goouttolunchmvvm.utils

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bintina.goouttolunchmvvm.databinding.FragmentNotificationDialogBinding
import com.bintina.goouttolunchmvvm.databinding.FragmentSettingsBinding
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.getRestaurantUsers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        //This should fetch the title instead.
        val messageDetail = arguments?.getString(ARG_MESSAGE_DETAIL)

        val fullText = composeNotification()
        binding.notificationTv.text = fullText

    }


    private fun composeNotification(): String {
        val currentUserName = MyApp.currentUser!!.displayName
        val currentRestaurant = MyApp.currentUserWithRestaurant.restaurant
        var fullNotificationText = ""

        if (currentRestaurant != null) {
            val currentUserRestaurantChoice = currentRestaurant.name
            val currentRestaurantVicinity = currentRestaurant.address

            listAttendingUsers { displayNames ->
                // Handle the list of display names here
                Log.d("AttendingUsers", "Attending users' names: $displayNames")

                // Example of updating the UI or performing another action
                if (displayNames.isNotEmpty()) {
                    // Update UI with the list of names, for example:
                    val attendingCoworkerNames = displayNames.joinToString(", ")
                    fullNotificationText =
                        "$currentUserName, you are going to $currentUserRestaurantChoice, $currentRestaurantVicinity. $attendingCoworkerNames will be joining you."
                } else {
                    // Handle case where no users are attending
                    val noUsersText = "No coworkers are joining you."
                    fullNotificationText =
                        "$currentUserName, you are going to $currentUserRestaurantChoice, $currentRestaurantVicinity. $noUsersText"
                }
            }
        } else {
            val noRestaurantText = "You have not chosen a restaurant today"
            fullNotificationText = noRestaurantText
        }

        return fullNotificationText

    }

    private fun listAttendingUsers(callback: (List<String>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val currentUsersAttendingObjects =
                getRestaurantUsers(MyApp.currentUserWithRestaurant.restaurant!!.restaurantId)

            val currentUsersAttendingNames = currentUsersAttendingObjects
                .map { it?.displayName } // Transform each LocalUser to their displayName
                .filterNotNull() // Remove any null display names if applicable

            withContext(Dispatchers.Main) {
                callback(currentUsersAttendingNames) // Return the result via callback
            }
        }
    }
}