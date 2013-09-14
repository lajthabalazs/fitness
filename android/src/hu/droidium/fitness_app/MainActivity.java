package hu.droidium.fitness_app;

import java.io.IOException;

import hu.droidium.fitness_app.database.DataLoader;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				loadFromAssets();
			}
		}).start();
	}

	protected void loadFromAssets() {
		try {
			DataLoader.loadDataFromAssets(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
