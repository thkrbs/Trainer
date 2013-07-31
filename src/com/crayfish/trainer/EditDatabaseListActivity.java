package com.crayfish.trainer;

import java.io.File;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class EditDatabaseListActivity extends ListActivity {
	private MemorizeDatabase memoryDatabase;
	private Association currentListAssociation;

	// Constant for identifying the dialog
	private static final int DIALOG_ALERT = 10;

//	 /* The activity that creates an instance of this dialog fragment must
//     * implement this interface in order to receive event callbacks.
//     * Each method passes the DialogFragment in case the host needs to query it. */
//    public interface NoticeDialogListener {
//        public void onDialogPositiveClick(AlertDialog dialog);
//        public void onDialogNegativeClick(AlertDialog dialog);
//    }
//	
// // Use this instance of the interface to deliver action events
//    NoticeDialogListener mListener;
//    
//    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        // Verify that the host activity implements the callback interface
//        try {
//            // Instantiate the NoticeDialogListener so we can send events to the host
//            mListener = (NoticeDialogListener) activity;
//        } catch (ClassCastException e) {
//            // The activity doesn't implement the interface, throw exception
//            throw new ClassCastException(activity.toString()
//                    + " must implement NoticeDialogListener");
//        }
//    }
//	
	// @SuppressWarnings("deprecation")
	// public void onClick(View view) {
	// showDialog(DIALOG_ALERT);
	// }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("editdatabase", "onCreate()");
		/*
		 * setContentView(R.layout.activity_display_message); // Show the Up
		 * button in the action bar.
		 * getActionBar().setDisplayHomeAsUpEnabled(true);
		 */
		// ListView listView = (ListView) findViewById(R.id.list_remember);

		memoryDatabase = new MemorizeDatabase(this);
		memoryDatabase.open();

		List<Association> mymemory = memoryDatabase.getAllAssociations();

		ArrayAdapter<Association> adapter = new ArrayAdapter<Association>(this, android.R.layout.simple_list_item_1,
				mymemory);
		// Assign adapter to ListView
		setListAdapter(adapter);

		ListView listView = getListView();

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				Log.e("listclick", "LongClick on " + position + " id:" + id);
				//Log.e("listclick", "parent count" + parent.getCount() + " parent:" + parent.toString() + " child:"
				//		+ parent.getChildAt(0).toString());
				currentListAssociation = (Association) getListAdapter().getItem(position);
				Log.e("listclick", "Item at position " + currentListAssociation.toString());
				showDialog(position);
				// memoryDatabase.deleteAssociation(association);
				return true;
			}
		});
		
		File file_int_dir = getFilesDir();
		Log.e("editdatabase",
				"internal dir: " + file_int_dir.getAbsolutePath() + "  free:" + file_int_dir.getFreeSpace());

		//
		// // Get the message from the intent
		// Intent intent = getIntent();
		// String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		// message = message + " - sagt er";
		// // Create the text view
		// TextView textView = new TextView(this);
		// textView.setTextSize(40);
		// textView.setText(message);
		//
		// // Set the text view as the activity layout
		// setContentView(textView);
	}

	@Override
	protected void onStop() {
		super.onStop();
		memoryDatabase.close();
		Log.e("editdatabase", "EditDatabase - onClose()");
	}

	@Override
	protected void onListItemClick(ListView parent, View view, int position, long id) {
		Log.e("listclick", "Click on " + position + " id:" + id);
		// String item = (String) getListAdapter().getItem(position);
		// Log.e("listclick", "Item:"+item);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.activity_display_message, menu);
	//
	// return true;
	// }

	@Override
	protected Dialog onCreateDialog(int id) {
	    // Create out AlterDialog
	    Builder builder = new AlertDialog.Builder(this);
	    //Association association = (Association) getListAdapter().getItem(id);
	    builder.setMessage("What to Do with this memory? Nr:"+id + " association:"+currentListAssociation.toString());
	    builder.setCancelable(true);
	    builder.setPositiveButton("Save it ", new EditOnClickListener());
	    builder.setNegativeButton("Cancel", new CancelOnClickListener());
	    builder.setNeutralButton("Delete it", new DeleteOnClickListener());
	    
	    AlertDialog dialog = builder.create();
	    dialog.show();
	    
	    return super.onCreateDialog(id);
	}

	private final class CancelOnClickListener implements DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
		}
	}

	private final class EditOnClickListener implements DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			// this.finish();
		}
	}

	private final class DeleteOnClickListener implements DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			//Association association = (Association) getListAdapter().getItem(which);
			Toast.makeText(getApplicationContext(), currentListAssociation.toString() + " - It will be deleted", Toast.LENGTH_LONG).show();
			memoryDatabase.deleteAssociation(currentListAssociation);
			
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
