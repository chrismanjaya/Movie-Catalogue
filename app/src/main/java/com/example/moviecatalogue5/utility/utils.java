package com.example.moviecatalogue5.utility;

import android.util.Log;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class utils {

    private static final String TAG = utils.class.getSimpleName();

    public static String formatDate(String date) {
        String sDate = date;
        try {
            Date stringToDate = new SimpleDateFormat("yyyy-MM-dd").parse(sDate);
            DateFormat dateFormat = new SimpleDateFormat("MMM dd, YYYY");
            sDate = dateFormat.format(stringToDate);
        } catch (Exception e) {
            Log.d(TAG + "-Exception formatDate", e.getMessage());
        }
        return sDate;
    }

    public static String formatYear(String date) {
        String sYear = date;
        try {
            Date stringToDate = new SimpleDateFormat("yyyy-MM-dd").parse(sYear);
            DateFormat dateFormat = new SimpleDateFormat("YYYY");
            sYear = dateFormat.format(stringToDate);
        } catch (Exception e) {
            Log.d(TAG + "-Exception formatYear", e.getMessage());
        }
        return sYear;
    }


}
