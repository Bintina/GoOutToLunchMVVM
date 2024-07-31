const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.notifyDataChange = functions.database.ref('/your-node-path')
    .onWrite((change, context) => {
        const newValue = change.after.val();
        const payload = {
            notification: {
                title: 'Data Changed',
                body: `New value: ${newValue}`
            }
        };

        return admin.messaging().sendToTopic('your-topic', payload);
    });