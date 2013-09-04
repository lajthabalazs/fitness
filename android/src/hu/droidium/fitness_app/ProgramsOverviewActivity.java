package hu.droidium.fitness_app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

public class ProgramsOverviewActivity extends Activity {
	private Button startNewProgram;
	private ListView programList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.programs_overview);
		startNewProgram = (Button)findViewById(R.id.startNewProgram);
		programList = (ListView)findViewById(R.id.programList);
	}
}
