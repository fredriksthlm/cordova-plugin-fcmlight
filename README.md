# Cordova plugin for [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging/)

This is a fork of https://github.com/chemerisuk/cordova-plugin-firebase-messaging

So visit Chemerisuk for hints.
I've forked it to remove some libraries and scripts that I don't want to use. (The old badger thing, the Android Core and the full MessagingService is removed etc).
Google Analytics will also not automatically be installed.

## Index

<!-- MarkdownTOC levels="2" autolink="true" -->

- [Supported platforms](#supported-platforms)
- [Installation](#installation)
- [Adding configuration files](#adding-configuration-files)
- [Methods](#methods)
- [Notification channels on Android 8+](#notification-channels-on-android-8)
- [Android tips](#android-tips)

<!-- /MarkdownTOC -->

## Supported platforms

- Android

## Installation

    $ cordova plugin add https://github.com/fredriksthlm/cordova-plugin-fcmlight

## Adding configuration file

The google-services.json file must be placed in the app folder of the project, before building.


## Methods

### onMessage(_callback_)
Called when a push message received while app is in foreground.
```js
cordova.plugins.firebase.messaging.onMessage(function(payload) {
    console.log("New foreground FCM message: ", payload);
});
```

### onBackgroundMessage(_callback_)
Called when a push message received while app is in background.
```js
cordova.plugins.firebase.messaging.onBackgroundMessage(function(payload) {
    console.log("New background FCM message: ", payload);
});
```


### getToken(_type_)
Returns a promise that fulfills with the current FCM token.
```js
cordova.plugins.firebase.messaging.getToken().then(function(token) {
    console.log("Got device token: ", token);
});
```

### clearNotifications
Clear all notifications from system notification bar.
```js
cordova.plugins.firebase.messaging.clearNotifications(function() {
    console.log("Notification messages cleared successfully");
});
```

### deleteToken
Delete the Instance ID (Token) and the data associated with it.
Call getToken to generate a new one.
```js
cordova.plugins.firebase.messaging.deleteToken().then(function() {
    console.log("Token revoked successfully");
});
```

### onTokenRefresh(_callback_)
Triggers every time when FCM token updated. You should usually call `getToken` to get an updated token and send it to server.
```js
cordova.plugins.firebase.messaging.onTokenRefresh(function() {
    console.log("Device token updated");
});
```

Use this callback to get initial token and to refresh stored value in future.



## Notification channels on Android 8+
Starting in Android 8.0 (API level 26), all notifications must be assigned to a channel or it will not appear. By categorizing notifications into channels, users can disable specific notification channels for your app (instead of disabling all your notifications), and users can control the visual and auditory options for each channel—all from the Android system settings.

On devices running Android 7.1 (API level 25) and lower methods below does not work.

### createChannel(_options_)
Creates a notification channel that notifications can be posted to.
```js
cordova.plugins.firebase.messaging.createChannel({
    id: "custom_channel_id",
    name: "Custom channel",
    importance: 0
});
```

#### Channel options
| Property | Type | Description |
| -------- | ---- | ----------- |
| `id` | `String` | The id of the channel. Must be unique per package. |
| `name` | `String` | User visible name of the channel. |
| `description` | `String` | User visible description of this channel. |
| `importance` | `Integer` | The importance of the channel. This controls how interruptive notifications posted to this channel are. The importance property goes from 1 = Lowest, 2 = Low, 3 = Normal, 4 = High and 5 = Highest. |
| `sound` | `String` | The name of the sound file to be played upon receipt of the notification in this channel. Cannot be changed after channel is created. |
| `badge` | `Boolean` | Sets whether notifications posted to this channel can appear as application icon badges in a Launcher. |
| `vibration` | `Boolean` or `Array` | Sets whether notification posted to this channel should vibrate. Pass array value instead of a boolean to set a vibration pattern. |
 
### findChannel(_channelId_)
Returns the notification channel settings for a given channel id.
```js
cordova.plugins.firebase.messaging.findChannel(channelId).then(function(channel) {
    console.log(channel);
});
```

### listChannels()
Returns all notification channels belonging to the app.
```js
cordova.plugins.firebase.messaging.listChannels().then(function(channels) {
    console.log(channels);
});
```

### deleteChannel(_channelId_)
Deletes the given notification channel.
```js
cordova.plugins.firebase.messaging.deleteChannel(channelId);
```

## Android tips

### Set custom default notification icon
Setting a custom default icon allows you to specify what icon is used for notification messages if no icon is set in the notification payload. Also use the custom default icon to set the icon used by notification messages sent from the Firebase console. If no custom default icon is set and no icon is set in the notification payload, the application icon (rendered in white) is used.
```xml
<config-file parent="/manifest/application" target="app/src/main/AndroidManifest.xml">
    <meta-data
        android:name="com.google.firebase.messaging.default_notification_icon"
        android:resource="@drawable/my_custom_icon_id"/>
</config-file>
```

### Set custom default notification color
You can also define what color is used with your notification. Different android versions use this settings in different ways: Android < N use this as background color for the icon. Android >= N use this to color the icon and the app name.
```xml
<config-file parent="/manifest/application" target="app/src/main/AndroidManifest.xml">
    <meta-data
        android:name="com.google.firebase.messaging.default_notification_color"
        android:resource="@drawable/my_custom_color"/>
</config-file>
```

### Set custom default notification channel
You can also define a default notification channel. 
```xml
<config-file parent="/manifest/application" target="app/src/main/AndroidManifest.xml">
<meta-data
    android:name="com.google.firebase.messaging.default_notification_channel_id"
    android:value="@string/default_notification_channel_id" />
</config-file>
```
