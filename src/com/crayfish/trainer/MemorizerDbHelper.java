package com.crayfish.trainer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MemorizerDbHelper extends SQLiteOpenHelper {

	// public abstract class MemoryEntry implements BaseColumns {
	public static final String TABLE_NAME = "entry";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_ENTRY_ID = "entryid";
	public static final String COLUMN_QUESTION = "question";
	public static final String COLUMN_ANSWER = "answer";
	public static final String COLUMN_KINDOF_QUESTION = "question_kind";
	public static final String COLUMN_KINDOF_ANSWER = "answer_kind";
	public static final String COLUMN_ANSWERED_CORRECT_COUNT = "answered_correct";
	public static final String COLUMN_ANSWERED_WRONG_COUNT = "answered_wrong";

	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " // .
			+ MemorizerDbHelper.TABLE_NAME + " (" //
			+ MemorizerDbHelper.COLUMN_ID + " INTEGER PRIMARY KEY," //
			+ MemorizerDbHelper.COLUMN_QUESTION + TEXT_TYPE + COMMA_SEP //
			+ MemorizerDbHelper.COLUMN_ANSWER + TEXT_TYPE + COMMA_SEP //
			+ MemorizerDbHelper.COLUMN_KINDOF_QUESTION + TEXT_TYPE + COMMA_SEP //
			+ MemorizerDbHelper.COLUMN_KINDOF_ANSWER + TEXT_TYPE + COMMA_SEP //
			+ MemorizerDbHelper.COLUMN_ANSWERED_CORRECT_COUNT + TEXT_TYPE + COMMA_SEP //
			+ MemorizerDbHelper.COLUMN_ANSWERED_WRONG_COUNT + TEXT_TYPE //
			+ MemorizerDbHelper.COLUMN_ENTRY_ID + TEXT_TYPE + COMMA_SEP //
			// ... // Any other options for the CREATE command
			+ " )";

	public static final String[] ALL_COLUMNS = { COLUMN_ID, COLUMN_QUESTION, COLUMN_ANSWER,
			COLUMN_KINDOF_QUESTION, COLUMN_KINDOF_ANSWER, COLUMN_ANSWERED_CORRECT_COUNT, COLUMN_ANSWERED_WRONG_COUNT, COLUMN_ENTRY_ID};

	private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;// _ENTRIES;
	// }

	// If you change the database schema, you must increment the database
	// version.
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "Memorizer.db";

	public MemorizerDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL(MemorizerDbHelper.SQL_CREATE_ENTRIES);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// This database is only a cache for online data, so its upgrade policy
		// is
		// to simply to discard the data and start over
		Log.w(MemorizerDbHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL(MemorizerDbHelper.SQL_DELETE_ENTRIES);
		onCreate(db);
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

}
