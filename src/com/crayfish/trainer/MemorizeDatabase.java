package com.crayfish.trainer;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MemorizeDatabase {

	// Database fields
	  private SQLiteDatabase database;
	  private MemorizerDbHelper MemDbHelper;
	  private String[] allColumns = MemorizerDbHelper.ALL_COLUMNS;

	  public MemorizeDatabase(Context context) {
	    MemDbHelper = new MemorizerDbHelper(context);
	  }

	  public void open() throws SQLException {
	    database = MemDbHelper.getWritableDatabase();
	  }

	  public void close() {
	    MemDbHelper.close();
	  }

	  /*
	   * create Association in Database
	   */
	  public Association createAssociation(String question, String answer, String questionKind, String answerKind) {
		  ContentValues values = new ContentValues();
		  values.put(MemorizerDbHelper.COLUMN_QUESTION, question);
		  values.put(MemorizerDbHelper.COLUMN_ANSWER, answer);
		  values.put(MemorizerDbHelper.COLUMN_KINDOF_QUESTION, questionKind);
		  values.put(MemorizerDbHelper.COLUMN_KINDOF_ANSWER, answerKind);
		  
		  long insertId = database.insert(MemorizerDbHelper.TABLE_NAME, null, values);
		  
		  // read back
		  Cursor cursor = database.query(MemorizerDbHelper.TABLE_NAME, MemorizerDbHelper.ALL_COLUMNS, MemorizerDbHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		  
		  cursor.moveToFirst();
		  Association newAssociation = getAssociationFromCursor(cursor);
		  cursor.close();
		  
		  return newAssociation;
	  }

	  /*
	   * get Association from Database
	   */
	  public Association getAssociationFromCursor(Cursor cursor) {
		  Association association = new Association();

		  association.setID(cursor.getLong(0));
		  association.setAssotiation(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
		  association.setStatistic(cursor.getLong(5), cursor.getLong(6));
		  
		  return association;
	  }

	  public void deleteAssociation(Association association) {
		  long id = association.getID();
		  
		  Log.e("memorizer", "Association deleted with id: " + id +" Association:"+association.toString());
		  
		  database.delete(MemorizerDbHelper.TABLE_NAME, MemorizerDbHelper.COLUMN_ID + " = " + id, null);
		  Log.e("memorizer", "Association deleted with id: " + id +" deleted.");
	  }
	  
	  public List<Association> getAllAssociations() {
	    List<Association> associations = new ArrayList<Association>();

	    Cursor cursor = database.query(MemorizerDbHelper.TABLE_NAME, 
	        MemorizerDbHelper.ALL_COLUMNS, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Association association = getAssociationFromCursor(cursor);
	      associations.add(association);
	      cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	    return associations;
	  }

	 
}
