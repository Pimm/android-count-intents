package org.ilumbo.countintents;

import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;

/**
 * Provides the test intents.
 */
public final class IntentProvider {
	private static final String COUNTDOWN_ACTION = "android.intent.action.COUNTDOWN";
	private static final String COUNTUP_ACTION = "android.intent.action.COUNTUP";
	/**
	 * A container that contains an intent and a name for that intent.
	 */
	public static class IntentContainer {
		private final Intent intent;
		/**
		 * A human-readable name for the intent.
		 */
		private final String name;
		public IntentContainer(Intent intent, String name) {
			this.intent = intent;
			this.name = name;
		}
		/**
		 * Returns the intent.
		 */
		public Intent getIntent() {
			return intent;
		}
		@Override
		public final String toString() {
			return name;
		}
	}
	/**
	 * Returns a container around an intent container that will cause a timer app to countdown to three (3) minutes after the
	 * getIntent method of the container is called.
	 *
	 * The user interaction flow associated with this intent is as follows:
	 * 	An app requires some kind of countdown, such as counting to the time the user is done with a three-minute workout. The
	 * 	user touches a start button in the app, and starts working out. The app stores the starting time, adds three minutes,
	 * 	and starts the timer app using an intent with that time in it.
	 */
	public static final IntentContainer getAbsoluteCountdownIntent(String name) {
		return new IntentContainer(new Intent(COUNTDOWN_ACTION), name) {
			@Override
			public final Intent getIntent() {
				return super.getIntent().cloneFilter()
					.setDataAndType(Uri.parse(String.valueOf(SystemClock.elapsedRealtime() + 3 * 60 * 1000)), "application/absolute-milliseconds");
			}
		};
	}
	/**
	 * Returns a container around an intent that will cause a timer app to countup (stopwatch-style).
	 */
	public static final IntentContainer getCountupIntent(String name) {
		return new IntentContainer(new Intent(COUNTUP_ACTION), name);
	}
	/**
	 * Returns a container around an intent that will cause a timer app to start in a paused state, and countdown to one (1)
	 * minute after the app is started/unpaused.
	 *
	 * The user interaction flow associated with this intent is slightly different from the absolute countdown intent:
	 * 	An app requires some kind of countdown, such as counting to the time the user should turn off the stove. The user
	 * 	touches some button in the app, which immediately starts the timer app in a paused state. The user then puts a
	 * 	casserole into the stove, and starts/unpauses the timer app. The timer app counts down for whatever duration was
	 * 	specified in the intent.
	 */
	public static final IntentContainer getRelativeCountdownIntent(String name) {
		return new IntentContainer(new Intent(COUNTDOWN_ACTION)
			.setDataAndType(Uri.parse(String.valueOf(1 * 60 * 1000)), "application/relative-milliseconds"), name);
	}
}