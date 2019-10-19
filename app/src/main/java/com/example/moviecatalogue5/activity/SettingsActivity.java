package com.example.moviecatalogue5.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import com.example.moviecatalogue5.R;
import com.example.moviecatalogue5.alarm.AlarmReceiver;

public class SettingsActivity extends AppCompatActivity {

    public static final String DAILY_REMINDER_TIME = "07:00";
    public static final String RELEASE_REMINDER_TIME = "08:00";

    public static final String DAILY_REMINDER_TITLE = "Catalogue Movie";
    public static final String DAILY_REMINDER_MESSAGE = "Catalogue Movie missing you!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private Context context;
        private String DAILY_REMINDER_KEY;
        private String RELEASE_REMINDER_KEY;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            context = getContext();

            DAILY_REMINDER_KEY = getString(R.string.daily_reminder_key);
            RELEASE_REMINDER_KEY = getString(R.string.release_reminder_key);

            Preference changeLanguagePref = findPreference("change_language_key");
            changeLanguagePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                    startActivity(mIntent);
                    return false;
                }
            });

            SwitchPreference dailyReminderPref = findPreference(DAILY_REMINDER_KEY);
            dailyReminderPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object switchIsOn) {
                    boolean isDailyReminderOn = (boolean) switchIsOn;
                    if (isDailyReminderOn) {
                        setDailyReminder(context);
                    } else {
                        cancelDailyReminder(context);
                    }
                    return true;
                }
            });

            SwitchPreference releaseReminderPref = findPreference(RELEASE_REMINDER_KEY);
            releaseReminderPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object switchIsOn) {
                    boolean isReleaseReminderOn = (boolean) switchIsOn;
                    if (isReleaseReminderOn) {
                        setReleaseReminder(context);
                    } else {
                        cancelReleaseReminder(context);
                    }
                    return true;
                }
            });
        }

        private void setDailyReminder(Context context) {
            if (context != null) {
                AlarmReceiver alarmReceiver = new AlarmReceiver();
                alarmReceiver.setRepeatingAlarm(
                        context,
                        AlarmReceiver.TYPE_DAILY_REMINDER,
                        DAILY_REMINDER_TIME,
                        DAILY_REMINDER_TITLE,
                        DAILY_REMINDER_MESSAGE);
                Toast.makeText(context, "Daily Reminder Notification has been Created", Toast.LENGTH_SHORT).show();
            }
        }

        private void setReleaseReminder(Context context) {
            if (context != null) {
                AlarmReceiver alarmReceiver = new AlarmReceiver();
                alarmReceiver.setRepeatingAlarm(
                        context,
                        AlarmReceiver.TYPE_RELEASE_REMINDER,
                        RELEASE_REMINDER_TIME,
                        null,
                        null);
                Toast.makeText(context, "Release Today Reminder Notification has been Created", Toast.LENGTH_SHORT).show();
            }
        }

        private void cancelDailyReminder (Context context) {
            AlarmReceiver alarmReceiver = new AlarmReceiver();
            alarmReceiver.cancelAlarm(context, AlarmReceiver.TYPE_DAILY_REMINDER);
            Toast.makeText(context, "Daily Reminder Notification has been Cancelled", Toast.LENGTH_SHORT).show();
        }

        private void cancelReleaseReminder (Context context) {
            AlarmReceiver alarmReceiver = new AlarmReceiver();
            alarmReceiver.cancelAlarm(context, AlarmReceiver.TYPE_RELEASE_REMINDER);
            Toast.makeText(context, "Release Reminder Notification has been Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}