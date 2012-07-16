package org.ilumbo.countintents;

import java.util.ArrayList;

import org.ilumbo.countintents.IntentProvider.IntentContainer;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public abstract class IntentContainerArrayAdapter extends ArrayAdapter<IntentContainer> {
	private static final class ViewPositionPair {
		public final View view;
		public final int position;
		public ViewPositionPair(View view, int position) {
			this.view = view;
			this.position = position;
		}
	}
	/**
	 * The views created by this adapter.
	 */
	private final ArrayList<ViewPositionPair> existingViews;
	public IntentContainerArrayAdapter(Context context, IntentContainer[] intentContainers) {
		super(context, R.layout.intent_view, R.id.type_field, intentContainers);
		existingViews = new ArrayList<ViewPositionPair>();
	}
	/**
	 * Returns the number of activities that can be performed for the given intent.
	 */
	protected abstract int getIntentActivityCount(Intent intent);
	@Override
	public final View getView(int position, View convertView, ViewGroup parent) {
		// Call the super's implementation to create the view. This implementation also fills in the type field.
		final View view = super.getView(position, convertView, parent);
		// Add the view to the list, so it can be updated.
		existingViews.add(new ViewPositionPair(view, position));
		// Update the view, filling the compatibility field as well as enabling/disabling the fields.
		return updateView(view, getIntentActivityCount(getItem(position).getIntent()));
	}
	/**
	 * Updates the view, based on the passed intent activity count. (Returns the view itself).
	 */
	private final View updateView(View view, int intentActivityCount) {
		// Find and fill the compatibility field.
		final TextView compatibilityField = (TextView) view.findViewById(R.id.compatibility_field);
		final int compatibilityTextIdentifier;
		switch (intentActivityCount) {
		case 0:
			compatibilityTextIdentifier = R.string.intent_compatibility_none;
			break;
		case 1:
			compatibilityTextIdentifier = R.string.intent_compatibility_one_activity;
			break;
		default:
			compatibilityTextIdentifier = R.string.intent_compatibility_multiple_activities;
			break;
		}
		compatibilityField.setText(compatibilityTextIdentifier);
		// If the intent is not supported, make both text fields half opaque.
		compatibilityField.setEnabled(0 != intentActivityCount);
		view.findViewById(R.id.type_field).setEnabled(0 != intentActivityCount);
		return view;
	}
	/**
	 * Updates the views created by this adapter.
	 */
	public final void updateExistingViews() {
		for (final ViewPositionPair existingView : existingViews) {
			updateView(existingView.view, getIntentActivityCount(getItem(existingView.position).getIntent()));
		}
	}
}