package hu.droidium.fitness_app.database;

import android.content.Context;
import android.util.Log;

public class CashedDatabaseManager extends DatabaseManager{
	
	private static final String TAG = "CashedDatabaseManager";
	private static CashedDatabaseManager instance;

	protected CashedDatabaseManager(Context context) {
		super(context);
		Log.e(TAG, "create database manager");
	}
		
	public static CashedDatabaseManager getInstance(Context context){
		if (instance == null) {
			instance = new CashedDatabaseManager(context);
		}
		return instance;
	}

}
