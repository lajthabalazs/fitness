package hu.droidium.fitness_app;

import hu.droidium.fitness_app.database.DatabaseManager;
import hu.droidium.fitness_app.database.Workout;
import hu.droidium.fitness_app.model.comparators.WorkoutComparator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

public class AvailableWorkoutListAdapter implements ListAdapter {

	List<Workout> workouts = new ArrayList<Workout>();
	private HashSet<DataSetObserver> observers = new HashSet<DataSetObserver>();
	private DatabaseManager databaseManager;
	private LayoutInflater layoutInflater;
	private Context context;
	
	public AvailableWorkoutListAdapter(Context context, LayoutInflater layoutInflater, DatabaseManager databaseManager) {
		this.context = context;
		this.layoutInflater = layoutInflater;
		this.databaseManager = databaseManager;
	}
	
	public void updateWorkouts(List<Workout> workouts) {
		this.workouts.clear();
		TreeSet<Workout> orderer = new TreeSet<Workout>(new WorkoutComparator());
		orderer.addAll(workouts);
		for (Workout workout : orderer) {
			this.workouts.add(workout);
		}
		for (DataSetObserver observer : observers) {
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
		if (convertView == null){
			convertView = layoutInflater.inflate(R.layout.available_workout_list_item, null);
		}
		Workout workout = (Workout)getItem(position);
		workout = databaseManager.getWorkout(workout.getId());
		int day = workout.getDay() + 1;
		String workoutName = workout.getName();
		String title = String.format(context.getString(R.string.workoutDay), day);
		if (workoutName!=null && workoutName.length() > 0) {
			title = title + ": " + workoutName; 
			
		}
		String totalReps = workout.getExercisesList(0, true, context, databaseManager);
		((TextView)convertView.findViewById(R.id.workoutNameInAvailableWorkoutList)).setText(title);

		int secs = (int) workout.getTotalTime(databaseManager);
		String timeText = Constants.getEstimatedTimeString(secs, context);
		String unitsString = String.format(context.getString(R.string.totalUnits), (int)workout.getTotalUnits(databaseManager));
		((TextView)convertView.findViewById(R.id.workoutEstimatedTimeInAvailableWorkoutList)).setText(timeText);
		((TextView)convertView.findViewById(R.id.workoutUnitsInAvailableWorkoutList)).setText(unitsString);
		
		if ((workout.getDescription() == null) || (workout.getDescription().equals(""))){
			convertView.findViewById(R.id.workoutDescriptionInAvailableWorkoutList).setVisibility(View.GONE);
		} else {
			((TextView)convertView.findViewById(R.id.workoutDescriptionInAvailableWorkoutList)).setText(workout.getDescription());
			convertView.findViewById(R.id.workoutDescriptionInAvailableWorkoutList).setVisibility(View.VISIBLE);
		}
		((TextView)convertView.findViewById(R.id.workoutTotalRepsInAvailableWorkoutList)).
			setText(String.format(context.getString(R.string.workoutExerciseListInAvailableWorkoutList), totalReps));
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
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}
}