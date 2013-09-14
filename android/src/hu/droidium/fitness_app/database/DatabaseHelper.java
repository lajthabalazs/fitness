package hu.droidium.fitness_app.database;


import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	
	private static final String DATABASE_NAME = "RealFitness";
	private static final int DATABASE_VERSION = 1;
	private static final String TAG = "DatabaseHelper";
	
	private Dao<ORMExerciseType, String> exerciseTypeDao;
	private Dao<ORMMuscle, String> muscleDao;	
	private Dao<ORMExerciseTypeMuscle, String> exerciseTypeMuscleDao;	

	private Dao<ORMBlock, String> blockDao;
	private Dao<ORMExercise, String> exerciseDao;
	private Dao<ORMProgram, String> programDao;
	private Dao<ORMWorkout, String> workoutDao;

	private Dao<ORMExerciseProgress, Long> exerciseProgressDao;
	private Dao<ORMWorkoutProgress, Long> workoutProgressDao;

	public DatabaseHelper (Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, ORMExerciseType.class);
			TableUtils.createTable(connectionSource, ORMMuscle.class);
			TableUtils.createTable(connectionSource, ORMExerciseTypeMuscle.class);
			
			TableUtils.createTable(connectionSource, ORMBlock.class);
			TableUtils.createTable(connectionSource, ORMExercise.class);
			TableUtils.createTable(connectionSource, ORMProgram.class);
			TableUtils.createTable(connectionSource, ORMWorkout.class);

			TableUtils.createTable(connectionSource, ORMExerciseProgress.class);
			TableUtils.createTable(connectionSource, ORMWorkoutProgress.class);			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Can't create database.", e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db,ConnectionSource connectionSource, int oldVersion, int newVersion) {
		// TODO think of something...
	}
	
	public Dao<ORMMuscle, String> getMuscleDao() {
		if (muscleDao == null) {
			try {
				muscleDao = getDao(ORMMuscle.class);
			} catch (SQLException e) {
				Log.e(TAG, "Error creating DAO " + e.getMessage());
				e.printStackTrace();
			}
		}
		return muscleDao;
	}
	
	public Dao<ORMExerciseType, String> getExerciseTypeDao() {
		if (exerciseTypeDao == null) {
			try {
				exerciseTypeDao = getDao(ORMExerciseType.class);
			} catch (SQLException e) {
				Log.e(TAG, "Error creating DAO " + e.getMessage());
				e.printStackTrace();
			}
		}
		return exerciseTypeDao;
	}

	public Dao<ORMExerciseTypeMuscle, String> getExerciseTypeMuscleDao() {
		if (exerciseTypeMuscleDao == null) {
			try {
				exerciseTypeMuscleDao = getDao(ORMExerciseTypeMuscle.class);
			} catch (SQLException e) {
				Log.e(TAG, "Error creating DAO " + e.getMessage());
				e.printStackTrace();
			}
		}
		return exerciseTypeMuscleDao;
	}

	public Dao<ORMBlock, String> getBlockDao() {
		if (blockDao == null) {
			try {
				blockDao = getDao(ORMBlock.class);
			} catch (SQLException e) {
				Log.e(TAG, "Error creating DAO " + e.getMessage());
				e.printStackTrace();
			}
		}
		return blockDao;
	}

	public Dao<ORMExercise, String> getExerciseDao() {
		if (exerciseDao == null) {
			try {
				exerciseDao = getDao(ORMExercise.class);
			} catch (SQLException e) {
				Log.e(TAG, "Error creating DAO " + e.getMessage());
				e.printStackTrace();
			}
		}
		return exerciseDao;
	}

	public Dao<ORMProgram, String> getProgramDao() {
		if (programDao == null) {
			try {
				programDao = getDao(ORMProgram.class);
			} catch (SQLException e) {
				Log.e(TAG, "Error creating DAO " + e.getMessage());
				e.printStackTrace();
			}
		}
		return programDao;
	}

	public Dao<ORMWorkout, String> getWorkoutDao() {
		if (workoutDao == null) {
			try {
				workoutDao = getDao(ORMWorkout.class);
			} catch (SQLException e) {
				Log.e(TAG, "Error creating DAO " + e.getMessage());
				e.printStackTrace();
			}
		}
		return workoutDao;
	}

	public Dao<ORMExerciseProgress, Long> getExerciseProgressDao() {
		if (exerciseProgressDao == null) {
			try {
				exerciseProgressDao = getDao(ORMExerciseProgress.class);
			} catch (SQLException e) {
				Log.e(TAG, "Error creating DAO " + e.getMessage());
				e.printStackTrace();
			}
		}
		return exerciseProgressDao;
	}

	public Dao<ORMWorkoutProgress, Long> getWorkoutProgressDao() {
		if (workoutProgressDao == null) {
			try {
				workoutProgressDao = getDao(ORMWorkoutProgress.class);
			} catch (SQLException e) {
				Log.e(TAG, "Error creating DAO " + e.getMessage());
				e.printStackTrace();
			}
		}
		return workoutProgressDao;
	}	
}
