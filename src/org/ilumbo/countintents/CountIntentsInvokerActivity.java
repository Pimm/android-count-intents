package org.ilumbo.countintents;

import org.ilumbo.countintents.IntentProvider.IntentContainer;

import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * The activity shows a list of test intents. When the user "clicks" the intents, they are invoked.
 */
public final class CountIntentsInvokerActivity extends ListActivity implements OnItemClickListener {
	private IntentContainerArrayAdapter arrayAdapter;
	/**
	 * The intent containers for the test intents.
	 */
	private IntentContainer[] intentContainers;
	@Override
	public final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Create the list of intent containers.
		final String[] intentNames = getResources().getStringArray(R.array.intent_names);
		intentContainers = new IntentContainer[3];
		intentContainers[0] = IntentProvider.getAbsoluteCountdownIntent(intentNames[0]);
		intentContainers[1] = IntentProvider.getRelativeCountdownIntent(intentNames[1]);
		intentContainers[2] = IntentProvider.getCountupIntent(intentNames[2]);
		// Set the list adapter.
		setListAdapter(arrayAdapter = new IntentContainerArrayAdapter(this, intentContainers) {
			@Override
			protected final int getIntentActivityCount(Intent intent) {
				return getPackageManager().queryIntentActivities(intent, 0).size();
			}
		});
		// Make the list view interactive.
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(this);
	}
	@Override
	protected final void onRestart() {
		super.onRestart();
		// When the activity has been hidden and then re-appears, update the views created by the array adapter. After all,
		// during the absence of this app another app could've been installed or uninstalled, changing the compatibility of the
		// intents.
		arrayAdapter.updateExistingViews();
	}
	public final void onItemClick(AdapterView<?> parent, View view, int position, long identifier) {
		try {
			startActivity(intentContainers[Long.valueOf(identifier).intValue()].getIntent());
		// Display a warning in a toast message if no activity can be performed for the "clicked" intent.
		} catch (ActivityNotFoundException exception) {
			Toast.makeText(this, R.string.warning_no_compatibility, Toast.LENGTH_SHORT).show();
		}
	}
}