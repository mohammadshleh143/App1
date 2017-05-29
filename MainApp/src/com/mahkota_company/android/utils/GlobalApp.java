package com.mahkota_company.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.EditText;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GlobalApp {
	public static final String SHARED_PREFERENCES_NAME = "mahkota_android_app";
	public static final String SHARED_PREFERENCES_PROFILE_DATA = "dsm_app_profile_data";
	public static final String SHARED_PREFERENCES_PROFILE_DATA_ID_USER = "dsm_app_profile_data_id_user";
	public static final String SHARED_PREFERENCES_PROFILE_DATA_EMAIL = "dsm_app_profile_data_email";
	public static final String SHARED_PREFERENCES_PROFILE_DATA_PASSWORD = "dsm_app_profile_data_password";
	public static final String SHARED_PREFERENCES_GALLERY_DATA_ID_GALLERY = "dsm_app_profile_data_id_gallery";

	
	/**
	 * Menyimpan Data Profile
	 * 
	 * @param str
	 * @param preference
	 */
	public static void storeDataIdGallery(String str, SharedPreferences preference) {
		SharedPreferences sp = preference;
		Editor editor = sp.edit();
		editor.putString(GlobalApp.SHARED_PREFERENCES_GALLERY_DATA_ID_GALLERY, str);
		editor.commit();
	}
	
	
	/**
	 * Menyimpan Data Profile
	 * 
	 * @param str
	 * @param preference
	 */
	public static void storeDataProfile(String str, SharedPreferences preference) {
		SharedPreferences sp = preference;
		Editor editor = sp.edit();
		editor.putString(GlobalApp.SHARED_PREFERENCES_PROFILE_DATA, str);
		editor.commit();
	}

	/**
	 * Menyimpan Data Id User
	 * 
	 * @param str
	 * @param preference
	 */
	public static void storeDataProfileIdUser(String str,
			SharedPreferences preference) {
		SharedPreferences sharedpreference = preference;
		Editor editor = sharedpreference.edit();
		editor.putString(GlobalApp.SHARED_PREFERENCES_PROFILE_DATA_ID_USER, str);
		editor.commit();
	}

	/**
	 * Menyimpan Data Email
	 * 
	 * @param str
	 * @param preference
	 */
	public static void storeDataProfileEmail(String str,
			SharedPreferences preference) {
		SharedPreferences sharedpreference = preference;
		Editor editor = sharedpreference.edit();
		editor.putString(GlobalApp.SHARED_PREFERENCES_PROFILE_DATA_EMAIL, str);
		editor.commit();
	}

	/**
	 * Menyimpan Data Password
	 * 
	 * @param str
	 * @param preference
	 */
	public static void storeDataProfilePassword(String str,
			SharedPreferences preference) {
		SharedPreferences sharedpreference = preference;
		Editor editor = sharedpreference.edit();
		editor.putString(GlobalApp.SHARED_PREFERENCES_PROFILE_DATA_PASSWORD,
				str);
		editor.commit();
	}

	public static boolean isBlank(EditText edt) {
		if (edt.getText().toString().trim().equals(""))
			return true;
		return false;
	}

	/**
	 * @param edt
	 *            edit text reference
	 * @param context
	 *            activity reference
	 * @param text
	 *            string that user want to display
	 */
	public static void takeDefaultAction(EditText edt, Context context,
			String text) {
		edt.setTextColor(Color.BLACK);
		edt.setError(text);
		edt.requestFocus();

	}

	/**
	 * Check Koneksi Internet
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkInternetConnection(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isAvailable()
				&& cm.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			Log.w("internet", "Internet Connection Not Present");
			return false;
		}
	}

	public static String getUniqueId() {
		String timeStamp = new SimpleDateFormat("HHmmss", Locale.getDefault())
				.format(new Date());
		final String date = "yyyyMMdd";
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat(date);
		final String dateOutput = dateFormat.format(calendar.getTime());
		String unId = dateOutput + timeStamp;
		return unId;
	}
}
