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