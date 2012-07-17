action.COUNTDOWN and action.COUNTUP
=====================

We create apps to make phones, tablets and other devices around the world more useful. Useful devices equal happy people. If
apps seamlessly work together, the devices become even more useful. More useful, happier people. **Love everywhere!**  
This is where [Android's intents come in](http://android-developers.blogspot.nl/2012/02/share-with-intents.html).

This repository is devoted to two – timing-related – intent actions:

    android.intent.action.COUNTDOWN

and

    android.intent.action.COUNTUP

Both intent actions are designed to display a timer app.

Using the intents
---------------------

### Relative countdown intents

You're developing an app that's all about Italian food. The app informs the user that this particular pasta should cook for 8
minutes.  
Now, you could include a timer in your app, but that's extra work. Plus a larger codebase means more maintenance. Not to
mention that the more stuff you add, the more likely it is that you'll introduce bugs. It's way easier and more hipster to use
a timer app the user already has on his/her device.

To do so, you can use an intent with the action:

    android.intent.action.COUNTDOWN

Perhaps the app has a button labelled _open timer_. You can simply write this code:

    openTimerButton.setOnClickListener(new android.view.View.OnClickListener() {
    	public void onClick(View button) {
    		int cookingTime = 8 * 60 * 1000;
    		Intent countdownIntent = new Intent("android.intent.action.COUNTDOWN")
    			.setDataAndType(Uri.parse(String.valueOf(cookingTime)), "application/relative-milliseconds");
    		startActivity(countdownIntent);
    	}
    });

Clicking the button will now display a timer app. The timer app knows about the 8-minute pasta cook. The user can simply start
the countdown process from within the timer app when the pasta hits the boiling water.

### Compatibility

The above works like a charm if the user has a timer app installed, but not so much if (s)he doesn't. There are several ways to
deal with this.  
You can use a try-catch block:

    startCountingButton.setOnClickListener(new android.view.View.OnClickListener() {
    	public void onClick(View button) {
    		int cookingTime = 8 * 60 * 1000;
    		Intent countdownIntent = new Intent("android.intent.action.COUNTDOWN")
    			.setDataAndType(Uri.parse(String.valueOf(cookingTime)), "application/relative-milliseconds");
    		try {
    			startActivity(countdownIntent);
    		} catch (ActivityNotFoundException exception) {
    			// No activities can be performed for the countdown intent.
    		}
    	}
    });

Within the catch you could use yet another intent to install a timer app, like so:

    Intent installTimerAppIntent = new Intent(Intent.ACTION_VIEW)
    	.setData(Uri.parse("market://details?id=org.ilumbo.ovo"));
    startActivity(installTimerAppIntent);

(Note that not every device has access to Google Play.)

More elegant solutions are available if you determine whether a timer app is installed before it is needed:

    int cookingTime = 8 * 60 * 1000;
    Intent countdownIntent = new Intent("android.intent.action.COUNTDOWN")
    	.setDataAndType(Uri.parse(String.valueOf(cookingTime)), "application/relative-milliseconds");
    // Check which activities can be performed for the intent, without actually starting them.
    if (0 == getPackageManager().queryIntentActivities(countdownIntent, 0).size()) {
    	// No activities can be performed for the countdown intent.
    }

### Absolute countdown intents

The example above starts a timer app with a relative time: __8 minutes__. The user then starts the actual countdown process
from within the timer app.  
If you feel it would better suit your needs, you can also start a timer app with an absolute time: __15 minutes after this
exact moment__. The timer app will then be displayed while the countdown process is in motion.

Simply replace the MIME data type:

    startCountingButton.setOnClickListener(new android.view.View.OnClickListener() {
    	public void onClick(View button) {
    		long currentTime = SystemClock.elapsedRealtime();
    		long alarmTime = currentTime + (15 * 60 * 1000);
    		Intent countdownIntent = new Intent("android.intent.action.COUNTDOWN")
    			.setDataAndType(Uri.parse(String.valueOf(alarmTime)), "application/absolute-milliseconds");
    		startActivity(countdownIntent);
    	}
    });

### Countup intents

Measuring durations can also be hours of fun. Starting a timer app to do that is even easier:

    Intent countupIntent = new Intent("android.intent.action.COUNTUP");
    startActivity(countupIntent);

Supporting the intents
---------------------

TODO: add a bit of documentation on how to support the intents.
