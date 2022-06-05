package com.fredriksthlm.cordova.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.media.AudioAttributes;
import android.net.Uri;

import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CordovaPlugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class FirebaseMEssagingPlugin extends CordovaPlugin {

    private NotificationManager notificationManager;
    private FirebaseMessaging firebaseMessaging;

    private void createChannel(JSONObject options, CallbackContext callbackContext) throws JSONException {

        String channelId = options.getString("id");
        String channelName = options.getString("name");
        int importance = options.getInt("importance");
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.setDescription(options.optString("description", ""));
        channel.setShowBadge(options.optBoolean("badge", true));

        String soundName = options.optString("sound", "default");
        if (!"default".equals(soundName)) {
            String packageName = cordova.getActivity().getPackageName();
            Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + packageName + "/raw/" + soundName);
            channel.setSound(soundUri, new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build());
        }
        JSONArray vibrationPattern = options.optJSONArray("vibration");
        if (vibrationPattern != null) {
            int patternLength = vibrationPattern.length();
            long[] patternArray = new long[patternLength];
            for (int i = 0; i < patternLength; i++) {
                patternArray[i] = vibrationPattern.getLong(i);
            }
            channel.setVibrationPattern(patternArray);
            channel.enableVibration(true);
        } else {
            channel.enableVibration(options.optBoolean("vibration", true));
        }
        notificationManager.createNotificationChannel(channel);
        callbackContext.success();
    }



    private void clearNotifications(CallbackContext callbackContext) {
        notificationManager.cancelAll();
        callbackContext.success();
    }



    private void getToken(String type, final CallbackContext callbackContext) {
        if (type.isEmpty()) {
            firebaseMessaging.getToken().addOnCompleteListener(cordova.getActivity(), task -> {
                if (task.isSuccessful()) {
                    callbackContext.success(task.getResult());
                } else {
                    callbackContext.error(task.getException().getMessage());
                }
            });
        } else {
            callbackContext.sendPluginResult(
                    new PluginResult(PluginResult.Status.OK, (String)null));
        }
    }



}
