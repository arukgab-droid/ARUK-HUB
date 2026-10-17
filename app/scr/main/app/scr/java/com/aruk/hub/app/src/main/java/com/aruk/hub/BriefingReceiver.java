package com.aruk.hub;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class BriefingReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        TextToSpeech voice = new TextToSpeech(
            context.getApplicationContext(),
            status -> {
                if (status == TextToSpeech.SUCCESS) {
                    voice.setLanguage(Locale.UK);
                    voice.setSpeechRate(0.88f);
                    voice.setPitch(0.85f);

                    voice.speak(
                        "Good morning. Today's hope is simple. Become stronger than yesterday. I hope tomorrow is better than today.",
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        "ARUK_HUB_DAILY"
                    );
                }
            }
        );
    }
}
