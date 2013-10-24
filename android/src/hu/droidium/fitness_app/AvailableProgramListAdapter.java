package hu.droidium.fitness_app;

import hu.droidium.fitness_app.database.DatabaseManager;
import hu.droidium.fitness_app.database.Program;
import hu.droidium.fitness_app.model.comparators.ProgramComparator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import android.content.res.Resources;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

public class AvailableProgramListAdapter implements ListAdapter {

	List<Program> programs = new ArrayList<Program>();
	private HashSet<DataSetObserver> observers = new HashSet<DataSetObserver>();
	private DatabaseManager databaseManager;
	private LayoutInflater layoutInflater;
	private Resources resources;
	
	public AvailableProgramListAdapter(Resources resources, LayoutInflater layoutInflater, DatabaseManager databaseManager) {
		this.resources = resources;
		this.layoutInflater = layoutInflater;
		this.databaseManager = databaseManager;
	}
	
	public void updatePrograms(List<Program> programs, ProgramComparator comparator) {
		this.programs.clear();
		TreeSet<Program> orderer = new TreeSet<Program>(comparator);
		orderer.addAll(programs);
		for (Program progress : orderer) {
			this.programs.add(progress);
		}
		for (DataSetObserver observer : observers) {
			observer.onChanged();
		}
	}

	@Override
	public int getCount() {
		return programs.size();
	}

	@Override
	public Object getItem(int position) {
		return programs.get(position);
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
		Program program = (Program)getItem(position);
		program.refresh(databaseManager);
		int workoutCount = program.getWorkouts().size();
		int totalLengthOfProgram = program.getTotalLength(databaseManager);
		float totalUnits = program.getTotalUnits(databaseManager);
		if (convertView == null){
			convertView = layoutInflater.inflate(R.layout.available_program_list_item, null);
		}
		((TextView)convertView.findViewById(R.id.programNameInAvailableProgramList)).setText(Translator.getTranslation(program.getName()));
		if (program.getDescription() != null && program.getDescription().length() > 0) {
			((TextView)convertView.findViewById(R.id.programDescriptionInAvailableProgramList)).setText(Translator.getTranslation(program.getDescription()));
			convertView.findViewById(R.id.programDescriptionInAvailableProgramList).setVisibility(View.VISIBLE);
		} else {
			convertView.findViewById(R.id.programDescriptionInAvailableProgramList).setVisibility(View.GONE);
		}
		((TextView)convertView.findViewById(R.id.programWorkoutsInAvailableProgramList)).
			setText(String.format(resources.getString(R.string.programWorkoutCount), workoutCount, totalLengthOfProgram, totalUnits));
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
		return programs.isEmpty();
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