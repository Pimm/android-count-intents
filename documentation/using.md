Using the count intents
=======================

### Relative countdown intents

You're developing an app that's all about Italian food. Your app informs the user that this particular pasta should cook for 8
minutes. At this point, the user might want to start a timer for 8 minutes.  
I'm confident that you are able to develop a timer for your app. However:
 1 it'll produce a larger codebase, therefore require more maintenance and increase the probability that your app contains
   bugs, and
 2 it's not very convenient to a user who already has a timer app (s)he is comfortable with.
It's way easier, more user friendly and more hipster to use a timer app already installed on the device.

To do so, you can use an intent with the action `android.intent.action.COUNTDOWN`

Perhaps the app has a button labelled *open timer*, and you have a `openTimerButton` property or variable. You can simply write
this code:

```java
openTimerButton.setOnClickListener(new android.view.View.OnClickListener() {
	public void onClick(android.view.View button) {
		// Calculate the cooking time (8 minutes) in milliseconds.
		int cookingTime = 8 * 60 * 1000;
		// Create the countdown intent.
		Intent countdownIntent = new Intent("android.intent.action.COUNTDOWN")
			.setDataAndType(Uri.parse(String.valueOf(cookingTime)), "application/relative-milliseconds");
		// Start a timer app.
		startActivity(countdownIntent);
	}
});
```

Clicking the button will now display a timer app. The timer app knows about the 8-minute pasta cook. The user can simply start
the countdown process from within the timer app the moment the pasta hits the boiling water.

### Compatibility

**Note:**  
The above works like a charm if the user has a timer app installed, but not so much if (s)he doesn't. There are
[several ways](//github.com/Pimm/android-count-intents/blob/master/documentation/compatibility-checking.md) to deal with this.

### Absolute countdown intents

The example above starts a timer app with a relative time: *8 minutes*. The user then starts the actual countdown process
from within the timer app.  
If you feel it would better suit your needs, you can also start a timer app with an absolute time: *15 minutes after this
exact moment*. The timer app will then be displayed while the countdown process is in motion.

Simply replace the MIME data type:

```java
startCountingButton.setOnClickListener(new android.view.View.OnClickListener() {
	public void onClick(android.view.View button) {
		long currentTime = SystemClock.elapsedRealtime();
		// Calculate the alarm time, as measured by SystemClock.elapsedRealtime;
		long alarmTime = currentTime + (15 * 60 * 1000);
		// Create the countdown intent.
		Intent countdownIntent = new Intent("android.intent.action.COUNTDOWN")
			.setDataAndType(Uri.parse(String.valueOf(alarmTime)), "application/absolute-milliseconds");
		// Start a timer app.
		startActivity(countdownIntent);
	}
});
```

### Countup intents

Measuring durations can be hours of fun. How long can you hold your breath? In what time do you run 100 metres? Starting a
timer app to do that is even easier:

```java
// Create the countup intent.
Intent countupIntent = new Intent("android.intent.action.COUNTUP");
// Start a timer app.
startActivity(countupIntent);
```
