package com.chartboost.sdk.sample;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.chartboost.sdk.ads.Rewarded;
import com.chartboost.sdk.callbacks.RewardedCallback;
import com.chartboost.sdk.events.CacheError;
import com.chartboost.sdk.events.CacheEvent;
import com.chartboost.sdk.events.ClickError;
import com.chartboost.sdk.events.ClickEvent;
import com.chartboost.sdk.events.DismissEvent;
import com.chartboost.sdk.events.ImpressionEvent;
import com.chartboost.sdk.events.RewardEvent;
import com.chartboost.sdk.events.ShowError;
import com.chartboost.sdk.events.ShowEvent;

public class RewardedSample extends BaseSample implements RewardedCallback {

    private Rewarded chartboostRewarded = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chartboost_sample);

        impressionType = ImpressionType.REWARDED;
        chartboostRewarded = new Rewarded("start", this, null);

        title = (TextView) findViewById(R.id.title);
        title.setText("Rewarded");

        logTextView = (TextView) findViewById(R.id.logText);
        logTextView.setText(logTextView.getText(), TextView.BufferType.EDITABLE);
        logTextView.setMovementMethod(new ScrollingMovementMethod());

        displayCounter = findViewById(R.id.displayCounter);
        failClickCounter = findViewById(R.id.failClickCounter);
        clickCounter = findViewById(R.id.clickCounter);
        cacheCounter = findViewById(R.id.cacheCounter);
        dismissCounter = findViewById(R.id.dismissCounter);
        impressionCounter = findViewById(R.id.impressionCounter);
        failLoadCounter = findViewById(R.id.failLoadCounter);
        failDisplayCounter = findViewById(R.id.failDisplayCounter);
        rewardCounter = findViewById(R.id.rewardCounter);
        hasLocation = findViewById(R.id.hasText);

        Button cacheButton = findViewById(R.id.cacheButton);
        Button showButton = findViewById(R.id.showButton);
        Button clearButton = findViewById(R.id.clearButton);
        ImageButton settingsButton = findViewById(R.id.settingsButton);

        cacheButton.setOnClickListener(v -> cacheRewarded());
        showButton.setOnClickListener(v -> showRewarded());
        clearButton.setOnClickListener(v -> clearUI());
        settingsButton.setOnClickListener(v -> openSettings());

        locationSpinner = findViewById(R.id.locationSpinner);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                onLocationAdapterChange(parentView, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void onLocationAdapterChange(AdapterView<?> parentView, int position) {
        location = parentView.getItemAtPosition(position).toString();
        addToUILog("Location changed to " + location);
    }

    private void openSettings() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private void cacheRewarded() {
        if(chartboostRewarded != null) {
            chartboostRewarded.cache();
        }
    }

    private void showRewarded() {
        if(chartboostRewarded != null) {
            chartboostRewarded.show();
        }
    }

    @Override
    public void onAdClicked(@NonNull ClickEvent clickEvent, @Nullable ClickError clickError) {
        if(clickError != null) {
            addToUILog("Rewarded clicked error: " + clickError.getCode().name());
            incrementCounter(failClickCounter);
        } else {
            addToUILog("Rewarded clicked");
            incrementCounter(clickCounter);
        }
    }

    @Override
    public void onAdLoaded(@NonNull CacheEvent cacheEvent, @Nullable CacheError cacheError) {
        if(cacheError != null) {
            addToUILog("Rewarded cached error: " + cacheError.getCode().name());
            incrementCounter(failLoadCounter);
        } else {
            hasLocation.setText("Yes");
            addToUILog("Rewarded cached");
            incrementCounter(cacheCounter);
        }
    }

    @Override
    public void onAdRequestedToShow(@NonNull ShowEvent showEvent) {
        addToUILog("Rewarded onAdRequestedToShow: " + showEvent.getAd().getLocation());
    }

    @Override
    public void onAdShown(@NonNull ShowEvent showEvent, @Nullable ShowError showError) {
        hasLocation.setText("No");
        if(showError != null) {
            addToUILog("Rewarded shown error: " + showError.getCode().name());
            incrementCounter(failDisplayCounter);
        } else {
            addToUILog("Rewarded shown for location: " + showEvent.getAd().getLocation());
            incrementCounter(displayCounter);
        }
    }

    @Override
    public void onImpressionRecorded(@NonNull ImpressionEvent impressionEvent) {
        incrementCounter(impressionCounter);
        addToUILog("Rewarded impressionEvent: " + impressionEvent.getAd().getLocation());
    }

    @Override
    public void onAdDismiss(@NonNull DismissEvent dismissEvent) {
        incrementCounter(dismissCounter);
        addToUILog("Rewarded onAdDismiss: " + dismissEvent.getAd().getLocation());
    }

    @Override
    public void onRewardEarned(@NonNull RewardEvent rewardEvent) {
        incrementCounter(rewardCounter);
        addToUILog("Rewarded onAdDismiss: " + rewardEvent.getAd().getLocation()+" reward: "+rewardEvent.getReward());
    }
}
