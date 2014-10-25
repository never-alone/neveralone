package com.finapps.neveralone.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.finapps.neveralone.Application;

public class UtilGps {
	
	/**
	 * Distance between 2 points in meters
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return
	 */
	public static double distFrom(float lat1, float lng1, float lat2, float lng2) {
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		int meterConversion = 1609;

		return (float) (dist * meterConversion);
	}
	
	public static String getAddressFromLocation(float latitude, float longitude) {
		Context context = Application.getContext();
		Geocoder gCoder = new Geocoder(context);
		List<Address> addresses = null;
		String res = null;
		
		try {
			addresses = (List<Address>) gCoder.getFromLocation(latitude,
					longitude, 1);

		} catch (IOException e) {
			res = latitude + "," + longitude;
		}
		
		if (res==null && (addresses == null || addresses.size()==0)){
			res = latitude + ";" + longitude;
		}else if (res == null){
			res = UtilGps.getFormatAddress(addresses);
		}
		return res;
	}
	
	public static String getFormatAddress(List<Address> address) {

		if (address.size() > 0) {
			Address returnedAddress = address.get(0);
			StringBuilder strReturnedAddress = new StringBuilder();
			for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
				if (i != 0) {
					strReturnedAddress.append("\n");
				}
				strReturnedAddress.append(returnedAddress.getAddressLine(i));
			}
			return strReturnedAddress.toString();
		} else {
			return "";
		}

	}
	
	

	public static HashMap googleValidationLocation(String search)
			throws IOException {

		Context context = Application.getContext();
		Geocoder  g = new Geocoder(context);
		List<Address> address = null;
		ArrayList<Address> addressOK = new ArrayList<Address>();
		HashMap res = null;
	/*	
		Double minLatitude   = Double.valueOf(context.getString(R.string.minLatitud));
		Double maxLatitude   = Double.valueOf(context.getString(R.string.maxLatitud));
		Double minLogngitude = Double.valueOf(context.getString(R.string.minLongitud));
		Double maxLongitude  = Double.valueOf(context.getString(R.string.maxLongitud));
*/
		//address = g.getFromLocationName(search, 5);
		address = g.getFromLocationName(search, 5);
		
		for (int x = 0;x<address.size();x++){
			boolean ciudadOK = false;
			Address elem = address.get(x);
			String ciudad = UtilGps.getCityFromLocation((float) elem.getLatitude(), (float) elem.getLongitude()).toUpperCase();
			/*
			//si alguna parte del nombre de la ciudad está en la búsqueda, OK
			StringTokenizer strTknCity = new StringTokenizer(ciudad);
			while (!ciudadOK && strTknCity.hasMoreTokens()){
				ciudadOK = search.contains(strTknCity.nextToken());
			}
			*/
			//Si alguna palabra de la búsqueda está dentro del nombre de la ciudad, OK
			StringTokenizer searchTkn = new StringTokenizer(search);
			while (!ciudadOK && searchTkn.hasMoreTokens()){
				String tkn = searchTkn.nextToken().toUpperCase();
				ciudadOK = ciudad.contains(tkn);
			}
			if (ciudadOK){
				addressOK.add(elem);
			}
		}
		
		
		for (int x = 0; x<addressOK.size();x++){
			String direccion = UtilGps.getAddressFromLocation((float) addressOK.get(x).getLatitude(), (float) addressOK.get(x).getLongitude());
			System.out.println(direccion);
		}
		
		if (addressOK != null && addressOK.size() > 0) {
			res=new HashMap();
			res.put("longitud", (float) addressOK.get(0).getLongitude());
			res.put("latitud", (float) addressOK.get(0).getLatitude());
			res.put("direccion", UtilGps.getAddressFromLocation(
                    (float) addressOK.get(0).getLatitude(), (float) addressOK
                            .get(0).getLongitude()));
			
		}

		return res;

	}
	

	public static String getCityFromLocation(float latitude, float longitude) {
		Context context = Application.getContext();
		
		Geocoder gCoder = new Geocoder(context);
		List<Address> addresses = null;

		try {
			addresses = (List<Address>) gCoder.getFromLocation(latitude,
					longitude, 1);

		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}

		if (addresses.size() > 0) {
			Address returnedAddress = addresses.get(0);
			StringBuilder strReturnedAddress = new StringBuilder();
			strReturnedAddress.append(returnedAddress.getLocality());
			return strReturnedAddress.toString();
		} else {
			return "";
		}
	}
}

