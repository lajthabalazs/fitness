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
	
	private Dao<ExerciseType, String> exerciseTypeDao;
	private Dao<Muscle, String> muscleDao;	
	private Dao<ExerciseTypeMuscle, String> exerciseTypeMuscleDao;	

	private Dao<Block, String> blockDao;
	private Dao<Exercise, String> exerciseDao;
	private Dao<Program, String> programDao;
	private Dao<Workout, String> workoutDao;

	private Dao<ExerciseProgress, Long> exerciseProgressDao;
	private Dao<WorkoutProgress, Long> workoutProgressDao;

	public DatabaseHelper (Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
		try {
			
			TableUtils.createTable(connectionSource, ExerciseType.class);
			TableUtils.createTable(connectionSource, Muscle.class);
			TableUtils.createTable(connectionSource, ExerciseTypeMuscle.class);
			
			TableUtils.createTable(connectionSource, Block.class);
			TableUtils.createTable(connectionSource, Exercise.class);
			TableUtils.createTable(connectionSource, Program.class);
			TableUtils.createTable(connectionSource, Workout.class);

			TableUtils.createTable(connectionSource, ProgramProgress.class);
			TableUtils.createTable(connectionSource, WorkoutProgress.class);
			TableUtils.createTable(connectionSource, ExerciseProgress.class);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Can't create database.", e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db,ConnectionSource connectionSource, int oldVersion, int newVersion) {
		// TODO think of something...
	}
	
	public Dao<Muscle, String> getMuscleDao() {
		if (muscleDao == null) {
			try {
				muscleDao = getDao(Muscle.class);
			} catch (SQLException e) {
				Log.e(TAG, "Error creating DAO " + e.getMessage());
				e.printStackTrace();
			}
		}
		return muscleDao;
	}
	
	public Dao<ExerciseType, String> getExerciseTypeDao() {
		if (exerciseTypeDao == null) {
			try {
				exerciseTypeDao = getDao(ExerciseType.class);
			} catch (SQLException e) {
				Log.e(TAG, "Error creating DAO " + e.getMessage());
				e.printStackTrace();
			}
		}
		return exerciseTypeDao;
	}

	public Dao<ExerciseTypeMuscle, String> getExerciseTypeMuscleDao() {
		if (exerciseTypeMuscleDao == null) {
			try {
				exerciseTypeMuscleDao = getDao(ExerciseTypeMuscle.class);
			} catch (SQLException e) {
				Log.e(TAG, "Error creating DAO " + e.getMessage());
				e.printStackTrace();
			}
		}
		return exerciseTypeMuscleDao;
	}

	public Dao<Block, String> getBlockDao() {
		if (blockDao == null) {
			try {
				blockDao = getDao(Block.class);
			} catch (SQLException e) {
				Log.e(TAG, "Error creating DAO " + e.getMessage());
				e.printStackTrace();
			}
		}
		return blockDao;
	}

	public Dao<Exercise, String> getExerciseDao() {
		if (exerciseDao == null) {
			try {
				exerciseDao = getDao(Exercise.class);
			} catch (SQLException e) {
				Log.e(TAG, "Error creating DAO " + e.getMessage());
				e.printStackTrace();
			}
		}
		return exerciseDao;
	}

	public Dao<Program, String> getProgramDao() {
		if (programDao == null) {
			try {
				programDao = getDao(Program.class);
			} catch (SQLException e) {
				Log.e(TAG, "Error creating DAO " + e.getMessage());
				e.printStackTrace();
			}
		}
		return programDao;
	}

	public Dao<Workout, String> getWorkoutDao() {
		if (workoutDao == null) {
			try {
				workoutDao = getDao(Workout.class);
			} catch (SQLException e) {
				Log.e(TAG, "Error creating DAO " + e.getMessage());
				e.printStackTrace();
			}
		}
		return workoutDao;
	}

	public Dao<ExerciseProgress, Long> getExerciseProgressDao() {
		if (exerciseProgressDao == null) {
			try {
				exerciseProgressDao = getDao(ExerciseProgress.class);
			} catch (SQLException e) {
				Log.e(TAG, "Error creating DAO " + e.getMessage());
				e.printStackTrace();
			}
		}
		return exerciseProgressDao;
	}

	public Dao<WorkoutProgress, Long> getWorkoutProgressDao() {
		if (workoutProgressDao == null) {
			try {
				workoutProgressDao = getDao(WorkoutProgress.class);
			} catch (SQLException e) {
				Log.e(TAG, "Error creating DAO " + e.getMessage());
				e.printStackTrace();
			}
		}
		return workoutProgressDao;
	}	
}
