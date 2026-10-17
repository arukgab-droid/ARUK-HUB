package com.aruk.hub;

import android.app.*;
import android.os.*;
import android.content.*;
import android.speech.tts.TextToSpeech;
import android.view.*;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {

    TextToSpeech tts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(1);
        layout.setPadding(32, 40, 32, 32);
        layout.setBackgroundColor(0xFF080A0F);

        TextView title = new TextView(this);
        title.setText("ARUK HUB");
        title.setTextSize(32);
        title.setTextColor(0xFFFFD700);
        layout.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("03:30 AM • DAILY BRIEFING");
        subtitle.setTextSize(14);
        subtitle.setTextColor(0xFFFFFFFF);
        layout.addView(subtitle);

        Button test = new Button(this);
        test.setText("TEST VOICE");
        layout.addView(test);

        Button alarm = new Button(this);
        alarm.setText("ENABLE 3:30 AM BRIEFING");
        layout.addView(alarm);

        setContentView(layout);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.UK);
                tts.setSpeechRate(0.88f);
                tts.setPitch(0.85f);
            }
        });

        test.setOnClickListener(v -> speak(
            "Good morning. Today's hope is simple. Become stronger than yesterday."
        ));

        alarm.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 31) {
                AlarmManager manager =
                    (AlarmManager) getSystemService(ALARM_SERVICE);

                if (!manager.canScheduleExactAlarms()) {
                    Intent intent = new Intent(
                        android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                    );
                    startActivity(intent);
                    return;
                }
            }
            scheduleAlarm();
            Toast.makeText(this, "Daily 3:30 AM briefing enabled",
                Toast.LENGTH_LONG).show();
        });
    }

    void speak(String message) {
        if (tts != null) {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, "ARUK_HUB");
        }
    }

    void scheduleAlarm() {
        AlarmManager manager =
            (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(this, BriefingReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(
            this, 1, intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Calendar time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, 3);
        time.set(Calendar.MINUTE, 30);
        time.set(Calendar.SECOND, 0);
        time.set(Calendar.MILLISECOND, 0);

        if (time.getTimeInMillis() <= System.currentTimeMillis()) {
            time.add(Calendar.DAY_OF_YEAR, 1);
        }

        manager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pending
        );
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
