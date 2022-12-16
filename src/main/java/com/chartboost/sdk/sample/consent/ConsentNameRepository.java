package com.chartboost.sdk.sample.consent;

import android.content.Context;
import androidx.preference.PreferenceManager;
import com.chartboost.sdk.privacy.model.DataUseConsent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class ConsentNameRepository {
    private static final String PREFS_KEY_CONSENT_SETTINGS = "consent_setting";

    private final Context context;
    private final Type gsonType = new TypeToken<Set<String>>() {}.getType();

    public ConsentNameRepository(Context context) {
        this.context = context;
    }

    public void save(DataUseConsent consent) {
        Set<String> names = load();
        names.add(consent.getPrivacyStandard());
        String json = new Gson().toJson(names);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREFS_KEY_CONSENT_SETTINGS, json).apply();
    }

    public void remove(DataUseConsent consent) {
        Set<String> names = load();
        names.remove(consent.getPrivacyStandard());
        String json = new Gson().toJson(names);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREFS_KEY_CONSENT_SETTINGS, json).apply();
    }

    public Set<String> load() {
        String json = PreferenceManager.getDefaultSharedPreferences(context).getString(PREFS_KEY_CONSENT_SETTINGS, "");
        Set<String> data = new Gson().fromJson(json, gsonType);
        if (data == null) {
            data = new HashSet<>();
        }
        return data;
    }
}
