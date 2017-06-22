package com.mahkota_company.android.customer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mahkota_company.android.R;


public class MainActivity extends FragmentActivity implements LocationListener {

	GoogleMap mGoogleMap;
	ArrayList<LatLng> mMarkerPoints;
	double mLatitude=0;
    double mLongitude=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_map);
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        if(status!=ConnectionResult.SUCCESS){

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        }else {
			mMarkerPoints = new ArrayList<LatLng>();
			SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
			mGoogleMap = fm.getMap();
			mGoogleMap.setMyLocationEnabled(true);
	        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	        Criteria criteria = new Criteria();
	        String provider = locationManager.getBestProvider(criteria, true);
	        Location location = locationManager.getLastKnownLocation(provider);
	        
	        if(location!=null){
                onLocationChanged(location);
            }

            locationManager.requestLocationUpdates(provider, 20000, 0, this);

			mGoogleMap.setOnMapClickListener(new OnMapClickListener() {
				@Override
				public void onMapClick(LatLng point) {
					if(mMarkerPoints.size()>1){
						FragmentManager fm = getSupportFragmentManager();
						mMarkerPoints.clear();
						mGoogleMap.clear();
						LatLng startPoint = new LatLng(mLatitude, mLongitude);
						drawMarker(startPoint);
					}
					
					drawMarker(point);

					if(mMarkerPoints.size() >= 2){					
						LatLng origin = mMarkerPoints.get(0);
						LatLng dest = mMarkerPoints.get(1);
						
						// Getting URL to the Google Directions API
						String url = getDirectionsUrl(origin, dest);				
						
						DownloadTask downloadTask = new DownloadTask();
						downloadTask.execute(url);
					}					
				}
			});
        }
	}

	private String getDirectionsUrl(LatLng origin,LatLng dest){
		String str_origin = "origin="+origin.latitude+","+origin.longitude;
		String str_dest = "destination="+dest.latitude+","+dest.longitude;
		String sensor = "sensor=false";
		String parameters = str_origin+"&"+str_dest+"&"+sensor;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

		return url;
	}

	/** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
                URL url = new URL(strUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                iStream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
                StringBuffer sb  = new StringBuffer();
                String line = "";
                while( ( line = br.readLine())  != null){
                        sb.append(line);
                }

                data = sb.toString();
                br.close();

        }catch(Exception e){
                Log.d("Exception while downloading url", e.toString());
        }finally{
                iStream.close();
                urlConnection.disconnect();
        }
        return data;
     }

    /** A class to download data from Google Directions URL */
	private class DownloadTask extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... url) {
			String data = "";
					
			try{
				data = downloadUrl(url[0]);
			}catch(Exception e){
				Log.d("Background Task",e.toString());
			}
			return data;		
		}

		@Override
		protected void onPostExecute(String result) {			
			super.onPostExecute(result);			
			
			ParserTask parserTask = new ParserTask();
			parserTask.execute(result);
		}
	}
	
	/** A class to parse the Google Directions in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

		@Override
		protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
			JSONObject jObject;	
			List<List<HashMap<String, String>>> routes = null;
            try{
            	jObject = new JSONObject(jsonData[0]);
            	DirectionsJSONParser parser = new DirectionsJSONParser();

            	routes = parser.parse(jObject);    
            }catch(Exception e){
            	e.printStackTrace();
            }
            return routes;
		}

		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;

			for(int i=0;i<result.size();i++){
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();

				List<HashMap<String, String>> path = result.get(i);

				for(int j=0;j<path.size();j++){
					HashMap<String,String> point = path.get(j);
					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);
					points.add(position);						
				}

				lineOptions.addAll(points);
				lineOptions.width(8);
				lineOptions.color(Color.BLUE);
			}

			mGoogleMap.addPolyline(lineOptions);
		}			
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_map, menu);
		return true;
	}
	
	private void drawMarker(LatLng point){
		mMarkerPoints.add(point);
		MarkerOptions options = new MarkerOptions();

		options.position(point);
		
		/** 
		 * For the start location, the color of marker is GREEN and
		 * for the end location, the color of marker is RED.
		 */
		if(mMarkerPoints.size()==1){
			options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		}else if(mMarkerPoints.size()==2){
			options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		}
		mGoogleMap.addMarker(options);		
	}

	@Override
	public void onLocationChanged(Location location) {
		if(mMarkerPoints.size() < 2){
			
			mLatitude = location.getLatitude();
	        mLongitude = location.getLongitude();
	        LatLng point = new LatLng(mLatitude, mLongitude);
	
	        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
	        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));        
        
        	drawMarker(point);			
        } 
        
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}
}