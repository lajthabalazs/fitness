package hu.droidium.fitness_app;

import hu.droidium.fitness_app.database.Workout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

public class UpcomingWorkoutAdapter implements ListAdapter {

	private List<Workout> workouts = new ArrayList<Workout>();
	private HashSet<DataSetObserver> observers = new HashSet<DataSetObserver>();
	
	@Override
	public int getCount() {
		return workouts.size();
	}

	@Override
	public Object getItem(int position) {
		return workouts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return workouts.isEmpty();
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		observers.add(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		observers.remove(observer);
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

}
