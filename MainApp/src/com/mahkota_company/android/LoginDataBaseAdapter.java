package com.mahkota_company.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LoginDataBaseAdapter{
	private static final String TABLE_STAFF = "staff";
	private static final String KEY_STAFF_ID_STAFF = "id_staff";
	private static final String KEY_STAFF_NAMA_LENGKAP = "nama_lengkap";
	private static final String KEY_STAFF_USERNAME = "username";
	private static final String KEY_STAFF_NO_TELP = "notelp";
	private static final String KEY_STAFF_PASSWORD = "password";
	private static final String KEY_STAFF_LEVEL = "level";
	private static final String KEY_STAFF_ID_BRANCH = "id_branch";
	private static final String KEY_STAFF_TYPE_CUSTOMER = "id_type_customer";
	private static final String KEY_STAFF_ID_DEPO = "id_depo";


	static final String DATABASE_NAME = "mahkota";
	static final int DATABASE_VERSION = 1;
	public static final int NAME_COLUMN = 1;
	// TODO: Create public field for each column in your table.
	// SQL Statement to create a new database.
	static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_STAFF + "("
			+ KEY_STAFF_ID_STAFF + " INTEGER PRIMARY KEY,"
			+ KEY_STAFF_NAMA_LENGKAP + " TEXT," + KEY_STAFF_USERNAME
			+ " TEXT," + KEY_STAFF_PASSWORD + " TEXT," + KEY_STAFF_NO_TELP
			+ " TEXT," + KEY_STAFF_LEVEL + " INTEGER,"
			+ KEY_STAFF_ID_BRANCH + " TEXT," + KEY_STAFF_TYPE_CUSTOMER
			+ " TEXT," + KEY_STAFF_ID_DEPO + " TEXT" + ")";

	// Variable to hold the database instance
	public  SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DataBaseHelper dbHelper;
	public  LoginDataBaseAdapter(Context _context)
	{
		context = _context;
		dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	public  LoginDataBaseAdapter open() throws SQLException
	{
		db = dbHelper.getWritableDatabase();
		return this;
	}
	public void close()
	{
		db.close();
	}

	public  SQLiteDatabase getDatabaseInstance()
	{
		return db;
	}

	public void insertEntry(String userNameStaff,String password)
	{
		ContentValues newValues = new ContentValues();
		// Assign values for each row.
		newValues.put(KEY_STAFF_USERNAME, userNameStaff);
		newValues.put(KEY_STAFF_PASSWORD,password);

		// Insert the row into your table
		db.insert(TABLE_STAFF, null, newValues);
		///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
	}
	public int deleteEntry(String UserName)
	{
		//String id=String.valueOf(ID);
		String where=KEY_STAFF_USERNAME+"=?";
		int numberOFEntriesDeleted= db.delete(TABLE_STAFF, where, new String[]{UserName}) ;
		// Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
		return numberOFEntriesDeleted;
	}

	public String getSinlgeEntry(String userNameStaff){
		Cursor cursor=db.query(TABLE_STAFF, null, KEY_STAFF_USERNAME+"=?", new String[]{userNameStaff}, null, null, null);
		if(cursor.getCount()<1) // UserName Not Exist
		{
			cursor.close();
			return "NOT EXIST";
		}
		cursor.moveToFirst();
		String password= cursor.getString(cursor.getColumnIndex(KEY_STAFF_PASSWORD));
		cursor.close();
		return password;
	}

	public void  updateEntry(String userNameStaff,String password)
	{
		// Define the updated row content.
		ContentValues updatedValues = new ContentValues();
		// Assign values for each row.
		updatedValues.put(KEY_STAFF_USERNAME, userNameStaff);
		updatedValues.put(KEY_STAFF_PASSWORD,password);

		String where=KEY_STAFF_USERNAME+"= ?";
		db.update(TABLE_STAFF,updatedValues, where, new String[]{userNameStaff});
	}
}

