Checking intent compatibility
=============================

Calling
[startActivity](//developer.android.com/reference/android/content/Context.html#startActivity%28android.content.Intent%29)
might result in an `ActivityNotFoundException` being thrown. More precise: if you call startActivity passing an intent the
device running your app has no activity for, the exception will be thrown. This will crash your app.  
Not every user has a timer app installed. You must gracefully deal with this situation.

### The easy way out: try-catch block

You can simply wrap the startActivity call in a try-catch block:

```java
openTimerButton.setOnClickListener(new android.view.View.OnClickListener() {
	public void onClick(android.view.View button) {
		// Calculate the cooking time (8 minutes) in milliseconds.
		int cookingTime = 8 * 60 * 1000;
		// Create the countdown intent.
		Intent countdownIntent = new Intent("android.intent.action.COUNTDOWN")
			.setDataAndType(Uri.parse(String.valueOf(cookingTime)), "application/relative-milliseconds");
		// Start a timer app, if any.
		try {
			startActivity(countdownIntent);
		} catch (ActivityNotFoundException exception) {
			// No activities can be performed for the countdown intent.
		}
	}
});
```

Within the catch, you can put code that will be executed when the user touches/clicks the button whilst no timer app is
available.

### The nicer method: using queryIntentActivities

More elegant solutions are available if you
[determine whether a timer app is installed](http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html) before
it is needed. You can use
[queryIntentActivities](//developer.android.com/reference/android/content/pm/PackageManager.html#queryIntentActivities%28android.content.Intent,%20int%29)
for this purpose. This method returns a list of activities that can be performed for the passed intent. We can check the length
of said list to predict whether startActivity will be succesful:

```java
// Calculate the cooking time (8 minutes) in milliseconds.
int cookingTime = 8 * 60 * 1000;
// Create the countdown intent.
Intent countdownIntent = new Intent("android.intent.action.COUNTDOWN")
	.setDataAndType(Uri.parse(String.valueOf(cookingTime)), "application/relative-milliseconds");
// Check which activities can be performed for the intent, without actually starting them.
if (0 == getPackageManager().queryIntentActivities(countdownIntent, 0).size()) {
	// No activities can be performed for the countdown intent. Perhaps this is the place to hide buttons or show a warning.
} else {
	// One or more activities can be performed for the countdown intent. Here one could show buttons, et cetera.
}
```

### Proposing a solutions

If you have determined that the user does not have a timer app installed, you lead them to one. This can be done using – you
guessed it – an intent.

```java
// Create the Google Play intent.
Intent installTimerAppIntent = new Intent(Intent.ACTION_VIEW)
	.setData(Uri.parse("market://details?id=org.ilumbo.ovo"));
// Start Google Play.
startActivity(installTimerAppIntent);
```
**Note:**  
Not every device has access to Google Play. The startActivity call above could throw an exception of its own. (The solutions
mentioned above work here as well!)
