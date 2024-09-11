const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

// Function to listen for changes in the 'users' node
exports.notifyUserDataChange = functions.database.ref('/users/{userId}')
    .onWrite((change, context) => {
        const userId = context.params.userId;
        const newValue = change.after.val();
        const payload = {
            notification: {
                title: 'User Data Changed',
                body: `User ${userId} data updated.`
            }
        };

        return admin.messaging().sendToTopic(`users/${userId}`, payload);
    });

// Function to listen for changes in the 'restaurants' node
exports.notifyRestaurantDataChange = functions.database.ref('/restaurants/{restaurantId}')
    .onWrite((change, context) => {
        const restaurantId = context.params.restaurantId;
        const newValue = change.after.val();
        const payload = {
            notification: {
                title: 'Restaurant Data Changed',
                body: `Restaurant ${restaurantId} data updated.`
            }
        };

        return admin.messaging().sendToTopic(`restaurants/${restaurantId}`, payload);
    });

      // Function to listen for changes in the 'userWithRestaurant' node
  exports.notifyUserWithRestaurantDataChange = functions.database.ref('/userWithRestaurant/{uid}')
      .onWrite((change, context) => {
          const userId = context.params.uid;
          const newValue = change.after.val();
          const payload = {
              data: {
                title: 'UserWithRestaurant Data Changed',
                body: `UserWithRestaurant ${userId} data updated.`,
                userId: userId
              }
          };

          return admin.messaging().sendToTopic(`userWithRestaurant/${uid}`, payload);
      });

      // Schedule a Cloud Function to delete the 'userWithRestaurant' node every day at midnight
      exports.deleteUserWithRestaurantDaily = functions.pubsub.schedule('0 0 * * *').timeZone('Europe/Istanbul').onRun((context) => {
          return admin.database().ref('/userWithRestaurant').remove()
              .then(() => {
                  console.log('Deleted userWithRestaurant node successfully');
              })
              .catch((error) => {
                  console.error('Error deleting userWithRestaurant node:', error);
              });
      });

      // Add new function to send a "Lunch Time Reminder" notification
      exports.sendLunchTimeReminder = functions.https.onRequest((req, res) => {
          const token = req.body.token;  // Get the FCM token from request

          const payload = {
              data: {
                  title: "Go For Lunch",
                  message: "It's time to eat!!",
                  click_action: "OPEN_NOTIFICATION_ACTIVITY"
              },
              notification: {
                  title: "Go For Lunch",
                  body: "It's time to eat!!"
              }
          };

          admin.messaging().sendToDevice(token, payload)
              .then(response => {
                  console.log('Lunch Time Reminder sent successfully:', response);
                  res.status(200).send('Notification sent successfully');
              })
              .catch(error => {
                  console.error('Error sending notification:', error);
                  res.status(500).send('Error sending notification');
              });
      });
