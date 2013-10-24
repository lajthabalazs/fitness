package hu.droidium.fitness_app;

import hu.droidium.fitness_app.activities.ProgramProgressDetailsActivity;
import hu.droidium.fitness_app.database.DatabaseManager;
import hu.droidium.fitness_app.database.ProgramProgress;
import hu.droidium.fitness_app.database.Workout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

public class UpcomingWorkoutAdapter implements ListAdapter, OnClickListener {

	private List<Workout> workouts = new ArrayList<Workout>();
	private HashSet<DataSetObserver> observers = new HashSet<DataSetObserver>();
	private LayoutInflater inflater;
	private ProgramProgressDetailsActivity activity;
	private boolean hasActual = false;
	private DatabaseManager databaseManager;
	private ProgramProgress progress;
	
	public UpcomingWorkoutAdapter(ProgramProgress progress, ProgramProgressDetailsActivity activity, DatabaseManager databaseManager, LayoutInflater inflater){
		this.progress = progress;
		this.activity = activity;
		this.inflater = inflater;
		this.databaseManager = databaseManager;
	}
	
	public void setWorkouts(List<Workout> workouts, boolean hasActual) {
		this.workouts.clear();
		this.hasActual = hasActual;
		this.workouts.addAll(workouts);
		for (DataSetObserver observer : observers){
			observer.onChanged();
		}
	}
	
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
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.upcoming_workout_list_item, null);
		}
		Workout workout = workouts.get(position);
		workout.refresh(databaseManager);
		((TextView)convertView.findViewById(R.id.workoutNameInUpcomingList)).setText(Translator.getTranslation(workout.getName()));
		int secs = (int) workout.getTotalTime(databaseManager);
		
		String timeText = Constants.getEstimatedTimeString(secs, activity);
		String unitsString = String.format(activity.getString(R.string.totalUnits), (int)workout.getTotalUnits(databaseManager));
		String dueString = workout.getDueDateString(progress.getProgressId(), databaseManager, activity);
		
		((TextView)convertView.findViewById(R.id.workoutEstimatedTimeInUpcomingList)).setText(timeText);
		((TextView)convertView.findViewById(R.id.workoutUnitsInUpcomingList)).setText(unitsString);
		((TextView)convertView.findViewById(R.id.workoutDueDateInUpcomingList)).setText(dueString);
		if (workout.getDescription() != null) {
			convertView.findViewById(R.id.workoutDetailsInUpcomingList).setVisibility(View.VISIBLE);
			((TextView)convertView.findViewById(R.id.workoutDetailsInUpcomingList)).setText(Translator.getTranslation(workout.getDescription()));
		} else {
			convertView.findViewById(R.id.workoutDetailsInUpcomingList).setVisibility(View.GONE);
		}
		Button jumpButton = (Button)convertView.findViewById(R.id.workoutSkipToThisInUpcomingList);
		jumpButton.setOnClickListener(this);
		jumpButton.setTag(workout);
		if (position == 0 && !hasActual) {
			jumpButton.setText(R.string.upcomingWorkoutStartNext);
		} else {
			jumpButton.setText(R.string.upcomingWorkoutJump);
		}
		return convertView;
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

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.workoutSkipToThisInUpcomingList) {
			Workout workout = (Workout)v.getTag();
			activity.skipToWorkout(workouts.indexOf(workout), workout);
		}
	}

}
