package com.mahkota_company.android;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

//@ReportsCrashes(formKey = "", mailTo = "d.pramukti@yahoo.com", mode = ReportingInteractionMode.TOAST, resToastText = (R.string.crash_toast))
public class CrashApplicationClass extends Application {
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		//ACRA.init(this);
		super.onCreate();
	}

}