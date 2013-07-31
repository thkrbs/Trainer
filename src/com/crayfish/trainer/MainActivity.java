package com.crayfish.trainer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.crayfish.trainer.MESSAGE";
	public final static String EXTRA_LOGFILE = "com.crayfish.trainer.LogFile";
	public static final String STATE_LOGFILE = "com.crayfish.trainer.STATE_LOGFILE";
	public final static String INTERNAL_FILENAME = "memorizer_file.txt";
	public final static String EXTERNAL_FILENAME = "memorizer_file.txt";
	private MemorizeDatabase memoryDatabase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		// TBD: handle configchange with new Fragment API

		// SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		// TextView logText = (TextView) findViewById(R.id.logtext);
		// String log = sharedPref.getString(STATE_LOGFILE, " empty log ");
		//
		// logText.setText(log);
		logText("onCreate() \n");
		// if (log != " empty log ") {
		// logText("[log read] ");
		// }

		ListView listView = (ListView) findViewById(R.id.list_remember);
		String[] values = new String[] { "Android", "iPhone", "WindowsMobile", "Blackberry", "WebOS", "Ubuntu",
				"Windows7", "Max OS X", "Linux", "OS/2" };

		// First paramenter - Context
		// Second parameter - Layout for the row
		// Third parameter - ID of the TextView to which the data is written
		// Forth - the Array of data
		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, android.R.id.text1, values);
		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_2,
		// android.R.id.text1, values);

		memoryDatabase = new MemorizeDatabase(this);
		memoryDatabase.open();

		
		// File file_ext_dir = getExternalFilesDir(null);
		// logText("external dir: ");// + file_ext_dir.toString());

		logText("database creating ...");
		// create database
		MemorizerDbHelper mDbHelper = new MemorizerDbHelper(getBaseContext());

		// Gets the data repository in write mode
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		boolean dbchecked = db.isDatabaseIntegrityOk();
		long dbsize = db.getPageSize();
		String dbpath = db.getPath();

		// Create a new map of values, where column names are the keys
		// ContentValues values_a = new ContentValues();
		// values_a.put(MemorizerDbHelper.COLUMN_ENTRY_ID, 1);
		// values_a.put(MemorizerDbHelper.COLUMN_ANSWER, "first line");
		// values_a.put(MemorizerDbHelper.COLUMN_QUESTION,
		// "first question"+jetzt.toString());

		// Insert the new row, returning the primary key value of the new row
		// long newRowId;
		// newRowId = db.insert(MemorizerDbHelper.TABLE_NAME, null, //
		// MemoryEntryContract.MemoryEntry.COLUMN_NAME_NULLABLE,
		// values_a);

		logText("database          ... created. Check ok:" + dbchecked + " Size:" + dbsize + " Path:" + dbpath);

		Date jetzt = new Date();
		Association ass = memoryDatabase.createAssociation("q-"+jetzt.toString(), "a", "start-q", "start-a");
		logText("association addet: "+ass.toString());
		
		List<Association> mymemory = memoryDatabase.getAllAssociations();

		ArrayAdapter<Association> adapter = new ArrayAdapter<Association>(this, android.R.layout.simple_list_item_1,
				mymemory);
		// Assign adapter to ListView
		listView.setAdapter(adapter);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				Log.e("listclick", "Click on " + position);
				return true;
			}
		});

		File file_int_dir = getFilesDir();
		logText("internal dir: " + file_int_dir.getAbsolutePath() + "  free:" + file_int_dir.getFreeSpace());

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
 
        case R.id.menu_settings:
            Intent i = new Intent(this, EditDatabaseListActivity.class);
            startActivityForResult(i,0);
            break;
 
        case R.id.menu_database:
            Intent i2 = new Intent(this, EditDatabaseListActivity.class);
            startActivityForResult(i2,0);
            break;
 
        }
 
        return true;
    }
	
	@Override
	public void onPause() {
		super.onPause(); // Always call the superclass method first

		// Release the Camera because we don't need it when paused
		// and other activities might need to use it.
		/*
		 * if (mCamera != null) { mCamera.release() mCamera = null; }
		 */
		logText("onPause()");

		// SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		// SharedPreferences.Editor editor = sharedPref.edit();
		// TextView logText = (TextView) findViewById(R.id.logtext);
		// String log = logText.getText().toString();
		// editor.putString(STATE_LOGFILE, log);
		// editor.commit();
	}

	@Override
	public void onResume() {
		super.onResume(); // Always call the superclass method first

		// Get the Camera instance as the activity achieves full user focus
		/*
		 * if (mCamera == null) { initializeCamera(); // Local method to handle
		 * camera init }
		 */
		logText("onResume() ");

	}

	@Override
	protected void onStop() {
		super.onStop(); // Always call the superclass method first

		// Save the note's current draft, because the activity is stopping
		// and we want to be sure the current note progress isn't lost.
		/*
		 * ContentValues values = new ContentValues();
		 * values.put(NotePad.Notes.COLUMN_NAME_NOTE, getCurrentNoteText());
		 * values.put(NotePad.Notes.COLUMN_NAME_TITLE, getCurrentNoteTitle());
		 * 
		 * getContentResolver().update( mUri, // The URI for the note to update.
		 * values, // The map of column names and new values to apply to them.
		 * null, // No SELECT criteria are used. null // No WHERE columns are
		 * used. );
		 */
		logText("onStop() ");

	}

	@Override
	protected void onDestroy() {
		super.onDestroy(); // Always call the superclass method first
		logText(" onDestroy()");
	}

	@Override
	protected void onStart() {
		super.onStart(); // Always call the superclass method first

		// The activity is either being restarted or started for the first time
		// so this is where we should make sure that GPS is enabled
		// LocationManager locationManager =
		// (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// boolean gpsEnabled =
		// locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		//
		// if (!gpsEnabled) {
		// // Create a dialog here that requests the user to enable GPS, and use
		// an intent
		// // with the android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
		// action
		// // to take the user to the Settings screen to enable GPS when they
		// click "OK"
		// }
		logText("onStart()");
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {

		// logText("onSaveInstanceState() ");
		// Save the current logfile
		// TextView logText = (TextView) findViewById(R.id.logtext);
		// String log = logText.getText().toString();
		// savedInstanceState.putString(STATE_LOGFILE, log +
		// "onSaveInstanceState() ");

		// Always call the superclass so it can save the view hierarchy state
		super.onSaveInstanceState(savedInstanceState);
	}

	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Always call the superclass so it can restore the view hierarchy
		super.onRestoreInstanceState(savedInstanceState);

		// Restore state members from saved instance
		// TextView logText = (TextView) findViewById(R.id.logtext);
		// String log = savedInstanceState.getString(STATE_LOGFILE);
		// logText.setText(log + "onRestoreInstanceState()");

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		logText("onConfigurationChanged()");
		// Checks the orientation of the screen
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			logText("landscape");
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			logText("portrait");
		}

	}

	public void onClick(View view) {

		ListView listView = (ListView) findViewById(R.id.list_remember);

		ArrayAdapter<Association> adapter = (ArrayAdapter<Association>) listView.getAdapter();

	}

	/** Called when the user clicks the Send button */
	public void answerIsKnownButton(View view) {
		/*
		 * // Map point based on address Uri location = Uri.parse(
		 * "geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California"); //
		 * Or map point based on latitude/longitude // Uri location =
		 * Uri.parse("geo:37.422219,-122.08364?z=14"); // z param is zoom level
		 * Intent mapIntent = new Intent(Intent.ACTION_VIEW, location); //
		 * Verify it resolves PackageManager packageManager =
		 * getPackageManager(); List<ResolveInfo> activities =
		 * packageManager.queryIntentActivities(mapIntent, 0); boolean
		 * isIntentSafe = activities.size() > 0;
		 * 
		 * // Start an activity if it's safe if (isIntentSafe) {
		 * startActivity(mapIntent); }
		 */

		// Do something in response to button
		// Intent intent = new Intent(this, DisplayMessageActivity.class);
		
		EditText editText = (EditText) findViewById(R.id.answer_a);
		String message = editText.getText().toString();
		// intent.putExtra(EXTRA_MESSAGE, message);
		// add log text
		logText(" send:'" + message + "'");
		// startActivity(intent);
		// String filename = "myfile";

		writeInternalFile(message);
		logText(" saved file - reading ...:");

		String readstring;

		readstring = readInternalFile();
		logText(" read int:" + readstring);

		readstring = readExternalFile();
		logText(" read ext:" + readstring);

		MemorizerDbHelper mDbHelper = new MemorizerDbHelper(getBaseContext());

		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		logText("db: " + db.toString());

		logText("accessing db");
		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { MemorizerDbHelper.COLUMN_ID, MemorizerDbHelper.COLUMN_ENTRY_ID,
				MemorizerDbHelper.COLUMN_QUESTION, MemorizerDbHelper.COLUMN_ANSWER };

		String selection = MemorizerDbHelper.COLUMN_ENTRY_ID + "  ? ";
		String[] selectionArgs = { "*" };

		String sortOrder = MemorizerDbHelper.COLUMN_ANSWER + " DESC";

		logText("db query");
		Cursor c = db.query(MemorizerDbHelper.TABLE_NAME, // The table to query
				projection, // The columns to return
				null, // The columns for the WHERE clause
				null, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				sortOrder // The sort order
				);

		logText("db move");
		c.moveToFirst();
		logText("db getlong");

		if (c.isAfterLast()) {
			logText("query returned empty.");
		}
		while (!c.isAfterLast()) {
			long itemId = c.getLong(c.getColumnIndexOrThrow(MemorizerDbHelper.COLUMN_ENTRY_ID));
			logText("db coursor:" + c.getString(2));
			c.moveToNext();
		}
		logText("db read");

	}

	
	
	// read internal database file
	public String readInternalFile() {
		String readstring;
		StringBuilder text = new StringBuilder();

		File infile = new File(getFilesDir(), INTERNAL_FILENAME);

		try {
			BufferedReader br = new BufferedReader(new FileReader(infile));
			String line;

			while ((line = br.readLine()) != null) {
				text.append(line);
				text.append('\n');
			}
			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		readstring = text.toString();
		return readstring;
	}

	// write internal database file
	public void writeInternalFile(String message) {

		FileOutputStream outputStream;
		try {
			outputStream = openFileOutput(INTERNAL_FILENAME, Context.MODE_PRIVATE);
			outputStream.write(message.getBytes());
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// read external database file
	public String readExternalFile() {
		String readstring = new String();

		// StringBuilder text = new StringBuilder();
		// File infile = new File(getExternalFilesDir(null), EXTERNAL_FILENAME);
		// try {
		// BufferedReader br = new BufferedReader(new FileReader(infile));
		// String line;
		// while ((line = br.readLine()) != null) {
		// text.append(line);
		// text.append('\n');
		// }
		// br.close();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		String eol = System.getProperty("line.separator");
		BufferedReader input = null;
		try {
			input = new BufferedReader(new InputStreamReader(openFileInput(EXTERNAL_FILENAME)));

			String line;
			StringBuffer buffer = new StringBuffer();

			while ((line = input.readLine()) != null) {
				buffer.append(line + eol);
			}
			readstring = buffer.toString();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return readstring;
	}

	// write external database file
	public void writeExternalFile(String message) {
		FileOutputStream outputStream;
		try {
			outputStream = openFileOutput(EXTERNAL_FILENAME, 0);
			outputStream.write(message.getBytes());
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String eol = System.getProperty("line.separator");
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(openFileOutput(EXTERNAL_FILENAME, MODE_WORLD_WRITEABLE)));
			writer.write("This is a test1." + eol);
			writer.write("This is a test2." + eol);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	public void logText(String text) {

		Log.e("memorizer", text);

		// der Rest ist dann eigendlich überflüssig ...

		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

		// get from pref
		String log = sharedPref.getString(STATE_LOGFILE, " empty log ");

		int length = log.length();
		if (length > 1500) {
			length = 1500;
		}
		log = log.substring(0, length);

		// prepare putting back
		SharedPreferences.Editor editor = sharedPref.edit();

		Date jetzt = new Date();

		// put log in pref
		editor.putString(STATE_LOGFILE, jetzt.toString() + text + "\n" + log);
		editor.commit();
		log = sharedPref.getString(STATE_LOGFILE, " empty log "); // and get it
																	// back to
																	// be sure

		// update display
		TextView logText = (TextView) findViewById(R.id.logtext);
		logText.setText(log);

	}

}
