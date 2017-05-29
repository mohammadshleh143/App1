package com.mahkota_company.android.common;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RebootReceiver extends BroadcastReceiver {
	private String LOG_TAG = RebootReceiver.class.getSimpleName();
	private long tm = 1;
	private Context _context;

	@Override
	public void onReceive(Context context, Intent intentReceive) {
		_context = context;
		if (intentReceive.getAction().equals(
				"android.intent.action.BOOT_COMPLETED")) {
			Log.d(LOG_TAG, "Initialisasi startMonitoring Success");
			//_context.startService(new Intent(_context, TrackingService.class));
			try {
				final AlarmManager alarms = (AlarmManager) _context
						.getSystemService(Context.ALARM_SERVICE);
				Intent intent = new Intent(_context, AlarmReceiver.class);
				intent.putExtra(AlarmReceiver.ACTION_ALARM,
						AlarmReceiver.ACTION_ALARM);
				intent.putExtra("mahkota", "tracking");
				final PendingIntent pIntent = PendingIntent.getBroadcast(
						_context, 1234567, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);

				tm = 20 * 1000 * 60;
				alarms.setRepeating(AlarmManager.RTC_WAKEUP,
						System.currentTimeMillis(), tm, pIntent);

			} catch (Exception e) {
				Log.d(LOG_TAG, "Initialisasi startMonitoring Error");
			}
		}

	}

}
