package com.example.moviecatalogue5.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import com.example.moviecatalogue5.BuildConfig;
import com.example.moviecatalogue5.R;
import com.example.moviecatalogue5.model.Film;
import com.example.moviecatalogue5.model.FilmPageResult;
import com.example.moviecatalogue5.network.ApiService;
import com.example.moviecatalogue5.network.RetrofitInstance;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = AlarmReceiver.class.getSimpleName();

    public static final String TYPE_DAILY_REMINDER = "DailyReminder";
    public static final String TYPE_RELEASE_REMINDER = "ReleaseTodayReminder";

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";

    public static final String TIME_FORMAT = "HH:mm";

    private final int ID_DAILY_REMINDER = 100;
    private final int ID_RELEASE_REMINDER = 200;

    public AlarmReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String alarmType = intent.getStringExtra(EXTRA_TYPE);
        String alarmMessage = intent.getStringExtra(EXTRA_MESSAGE);
        String alarmTitle = intent.getStringExtra(EXTRA_TITLE);

        if (alarmType.equalsIgnoreCase(TYPE_DAILY_REMINDER)) {
            int alarmNotifId = ID_DAILY_REMINDER;
            showNotification(context, alarmTitle, alarmMessage, alarmNotifId);
            Log.d(TAG + "-onReceive", "DAILY_REMINDER Finished...");
        } else {
            notifyMovieReleaseToday(context);
            Log.d(TAG + "-onReceive", "RELEASE_REMINDER Finished...");
        }
    }

    public boolean isDateInvalid(String date, String format) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            dateFormat.setLenient(false);
            dateFormat.parse(date);
            return false;
        } catch (ParseException e) {
            Log.d(TAG + "-ParseException", e.getMessage());
            return true;
        }
    }

    public void setRepeatingAlarm(Context context, String type, String time, String title, String message) {
        Log.d(TAG + "-setRepeatingAlarm", "Entering...");
        if (isDateInvalid(time, TIME_FORMAT)) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);

        String timeArray[] = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        int requestCode = type.equalsIgnoreCase(TYPE_DAILY_REMINDER) ? ID_DAILY_REMINDER : ID_RELEASE_REMINDER;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);

        if (alarmManager != null) {
            Log.d(TAG + "-setRepeatingAlarm", "Registering Alarm at " + time);
            alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent);
        }

        Log.d(TAG + "-setRepeatingAlarm", "Repeating alarm set up...");
    }

    private void showNotification(Context context, String title, String message, int notificationId) {
        String CHANNEL_ID = "CH001";
        String CHANNEL_NAME = "AlarmManager Channel";

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[] {1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[] {1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManager != null) {
            notificationManager.notify(notificationId, notification);
        }
    }

    public void cancelAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        int requestCode = type.equalsIgnoreCase(TYPE_DAILY_REMINDER) ? ID_DAILY_REMINDER : ID_RELEASE_REMINDER;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    public void notifyMovieReleaseToday(Context context) {
        String dateToday = getTodayDate();
        ApiService movieReleaseApiService = RetrofitInstance.getRetrofitInstance().create(ApiService.class);
        Call<FilmPageResult> call = movieReleaseApiService.getMovieReleaseList(dateToday, dateToday, BuildConfig.ApiKey);
        Log.d(TAG, "-URL: " + call.request().url().toString());

        call.enqueue(new Callback<FilmPageResult>() {
            @Override
            public void onResponse(Call<FilmPageResult> call, Response<FilmPageResult> response) {
                try {
                    assert response.body() != null;
                    ArrayList<Film> items = response.body().getFilmResult();
                    for (Film item : items) {
                        Log.d(TAG, "-Release Today MOVIE: " + item.getTitle() + " | " + item.getReleaseDate());
                        if (item.getReleaseDate().equals(dateToday)) {
                            String alarmTitle = item.getTitle();
                            String alarmMessage = alarmTitle + " has been release today!";
                            showNotification(context, alarmTitle, alarmMessage, item.getId());
                        }
                    }
                } catch (Exception e) {
                    Log.d(TAG + "-exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<FilmPageResult> call, Throwable t) {
                Log.d(TAG + "-onFailure", t.getMessage());
            }
        });
    }

    private String getTodayDate(){
        Date dateToday = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = simpleDateFormat.format(dateToday);
        return formattedDate;
    }
}
