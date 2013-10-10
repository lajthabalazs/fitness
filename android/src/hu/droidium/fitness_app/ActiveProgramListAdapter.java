package hu.droidium.fitness_app;

import hu.droidium.fitness_app.activities.ProgramsOverviewActivity;
import hu.droidium.fitness_app.database.DatabaseManager;
import hu.droidium.fitness_app.database.Program;
import hu.droidium.fitness_app.database.ProgramProgress;
import hu.droidium.fitness_app.model.helpers.ProgressComparator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ActiveProgramListAdapter implements ListAdapter {

	List<ProgramProgress> programs = new ArrayList<ProgramProgress>();
	private ProgramsOverviewActivity programsOverviewActivity;
	private HashSet<DataSetObserver> observers = new HashSet<DataSetObserver>();
	private DatabaseManager databaseManager;
	
	public ActiveProgramListAdapter(ProgramsOverviewActivity programsOverviewActivity, DatabaseManager databaseManager) {
		this.programsOverviewActivity = programsOverviewActivity;
		this.databaseManager = databaseManager;
	}
	
	public void updatePrograms(List<ProgramProgress> progresses) {
		programs.clear();
		TreeSet<ProgramProgress> orderer = new TreeSet<ProgramProgress>(new ProgressComparator(databaseManager));
		orderer.addAll(progresses);
		for (ProgramProgress progress : orderer) {
			programs.add(progress);
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
		ProgramProgress progress = (ProgramProgress)getItem(position);
		progress = databaseManager.getProgress(progress.getProgressId());
		Program program = databaseManager.getProgram(progress.getProgram().getId());
		if (convertView == null){
			convertView = programsOverviewActivity.getLayoutInflater().inflate(R.layout.active_program_list_item, null);
		}
		((TextView)convertView.findViewById(R.id.programNameInActiveProgramList)).setText(program.getName());	
		((TextView)convertView.findViewById(R.id.programStartInActiveProgramList)).setText(progress.getDateOfNextWorkoutText(databaseManager, programsOverviewActivity));
		((ProgressBar)convertView.findViewById(R.id.programProgressBarInList)).setProgress(progress.getProgressPercentage(databaseManager));
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