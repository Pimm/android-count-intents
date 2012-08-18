Supporting the count intents
============================

If you have an app that can count up and/or down, you should support the intent actions described in this repository. This will
allow other apps to cooperate with yours, making your app more useful and users more likely to install it.

Supporting the intent actions shouldn't be too difficult.

### Adding intent filters to your manifest

The first step is letting Android know that your app, or more precise: your activity, supports intents with the actions.

The part of your manifest that defines the (or an) activity of the app might look like this:

```xml
<activity android:name=".TimerActivity">
	<intent-filter>
		<action android:name="android.intent.action.MAIN" />
		<category android:name="android.intent.category.LAUNCHER" />
	</intent-filter>
</activity>
```

You can simply add more [intent filters](http://developer.android.com/guide/components/intents-filters.html), like so:

```xml
<activity android:name=".TimerActivity">
	<intent-filter>
		<action android:name="android.intent.action.MAIN" />
		<category android:name="android.intent.category.LAUNCHER" />
	</intent-filter>
	<!--
		This activity can be started by an app that needs an alarm to go off at a specific time. The data will be the
		number of milliseconds since boot. (Such as "SystemClock.elapsedRealtime() + 15000".)
	-->
	<intent-filter>
		<action android:name="android.intent.action.COUNTDOWN" />
		<category android:name="android.intent.category.DEFAULT" />
		<data android:mimeType="application/absolute-milliseconds" />
	</intent-filter>
	<!--
		This activity can be started by an app that needs an alarm to run for a specific time. The data be that time
		in milliseconds. (Such as "15000".) The app will start "paused".
	-->
	<intent-filter>
		<action android:name="android.intent.action.COUNTDOWN" />
		<category android:name="android.intent.category.DEFAULT" />
		<data android:mimeType="application/relative-milliseconds" />
	</intent-filter>
	<!--
		This activity can be started by an app that wants to display a stopwatch counting up.
	-->
	<intent-filter>
		<action android:name="android.intent.action.COUNTUP" />
		<category android:name="android.intent.category.DEFAULT" />
		<data android:mimeType="application/absolute-milliseconds" />
	</intent-filter>
</activity>
```

### Testing

If the above went well, Android now assumes that your app knows how to deal with the intents. To verify this, you can install
the *[count intent invoker](//github.com/Pimm/android-count-intents/tree/master/invoker)* in this repository.

Once installed, touch/click the intents in the count intent invoker. Doing so should either show your activity, or show a list
of activities that can be performed for that intent if there are multiple.

### Handling the intents

The next step is to actually respond to the intents. In your `onCreate`, you can see what intent caused it to be created using
[getIntent](//developer.android.com/reference/android/app/Activity.html#getIntent%28%29).

If an existing activity receives the intent,
[onNewIntent](//developer.android.com/reference/android/app/Activity.html#onNewIntent%28android.content.Intent%29) will be
called, but that's only if you're developing a rather weird app in the first place.

Once you have the intent, you should check which action is used and read the data inside:

```java
if ("android.intent.action.COUNTDOWN".equals(intent.getAction())) {
	if ("application/absolute-milliseconds".equals(intent.getType())) {
		// Parse the data, which should be the number of milliseconds since boot at which the alarm should go off.
		long time;
		try {
			time = Long.parseLong(intent.getDataString());
		} catch (NumberFormatException exception) {
			android.util.Log.w(LOG_TAG, "Unexpected intent data. expected a number, got " + intent.getDataString() + ". java.lang.Long.parseLong was used, which failed.");
			return;
		}
		// Check whether the passed time is no earlier than one minute in the past. If a time of more than one minute in the past
		// is passed, it's probably an error.
		if (time - SystemClock.elapsedRealtime() < -60000) {
			android.util.Log.w(LOG_TAG, "Unexpected intent data: too far in the past. An example of an expected value is \"SystemClock.elapsedRealtime() + 15000\".");
			return;
		}
		/**
		 * The activity has been started by an app that wants an alarm to go off at the time as specified by the time variable.
		 *
		 * Make sure an alarm goes off at the moment SystemClock.elapsedRealtime() returns time (the variable).
		 */
	} else if ("application/absolute-milliseconds".equals(intent.getType())) {
		// Parse the data, which should be the number of milliseconds that will be on the timer.
		int time;
		try {
			time = Integer.parseInt(intent.getDataString());
		} catch (NumberFormatException exception) {
			android.util.Log.w(LOG_TAG, "Unexpected intent data. expected a number, got " + intent.getDataString() + ". java.lang.Integer.parseLong was used, which failed.");
			return;
		}
		// Check whether the passed time is less than one second. If such a time is passed, it's probably an error.
		if (time < 1000) {
			android.util.Log.w(LOG_TAG, "Unexpected intent data: number too small. An example of an expected value is \"15000\".");
			return;
		}
		/**
		 * The activity has been started by an app that wants it to countdown for an amount of time as specified by the time
		 * variable.
		 *
		 * Allow the user to somehow start the countdown process, and then count for the time as specified by the time variable
		 * (in milliseconds). Make sure an alarm goes off when the countdown process has finished.
		 */
	}
} else if ("android.intent.action.COUNTUP".equals(intent.getAction())) {
	/**
	 * The activity has been started by an app that wants it to count up (stopwatch-style).
	 *
	 * Either start counting up here, or allow the user to somehow start the counting process.
	 */
}
```

### More testing

You must now test your support. This can be easily done using the count intent invoker installed earlier.
