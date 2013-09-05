package hu.droidium.fitness_app;

import hu.droidium.fitness_app.model.ProgramProgress;
import hu.droidium.fitness_app.model.ProgramProgressHelper;
import hu.droidium.fitness_app.model.ProgramProgressManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import android.database.DataSetObserver;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

public class ActiveProgramListAdapter implements ListAdapter {

	List<ProgramProgress> programs = new ArrayList<ProgramProgress>();
	private ProgramsOverviewActivity programsOverviewActivity;
	private HashSet<DataSetObserver> observers = new HashSet<DataSetObserver>();
	
	public ActiveProgramListAdapter(
			ProgramsOverviewActivity programsOverviewActivity,
			ProgramProgressManager programProgressManager) {
		programs.clear();
		TreeSet<ProgramProgress> orderer = new TreeSet<ProgramProgress>(new ProgramProgress.ProgressComparator());
		orderer.addAll(programProgressManager.getProgress());
		for (ProgramProgress progress : orderer) {
			programs.add(progress);
		}
		this.programsOverviewActivity = programsOverviewActivity;
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
		/*
		ProgramProgress program = (ProgramProgress)getItem(position);
		if (program.getTerminationDate() != -1) {
			return 0;
		} else {
			return 1;
		}
		*/
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ProgramProgress progress = (ProgramProgress)getItem(position);
		if (convertView == null){
			convertView = programsOverviewActivity.getLayoutInflater().inflate(R.layout.active_program_list_item, null);
		}
		((TextView)convertView.findViewById(R.id.programNameInActiveProgramList)).setText(progress.getProgram().getName());	
		((TextView)convertView.findViewById(R.id.programStartInActiveProgramList)).setText(ProgramProgressHelper.getDateOfNextWorkoutText(progress, programsOverviewActivity));
		convertView.getBackground().clearColorFilter();
		convertView.getBackground().setColorFilter(new PorterDuffColorFilter(ProgramProgressHelper.getBackgroundColor(progress, programsOverviewActivity), PorterDuff.Mode.MULTIPLY));
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
		observers .add(observer);
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