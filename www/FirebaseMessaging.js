var exec = require("cordova/exec");
var PLUGIN_NAME = "FirebaseMessaging";
var noop = function() {};

module.exports = {
    clearNotifications: function(callack, error) {
        exec(callack, error, PLUGIN_NAME, "clearNotifications", []);
    },
    getToken: function(type) {
        return new Promise(function(resolve, reject) {
            if (type && typeof type !== "string") {
                return reject(new TypeError("type argument must be a string"));
            }
            exec(resolve, reject, PLUGIN_NAME, "getToken", [type || ""]);
        });
    },
    listChannels: function() {
        return new Promise(function(resolve, reject) {
            exec(resolve, reject, PLUGIN_NAME, "listChannels", []);
        });
    },
    createChannel: function(options) {
        return new Promise(function(resolve, reject) {
            exec(resolve, reject, PLUGIN_NAME, "createChannel", [options]);
        });
    }
};
