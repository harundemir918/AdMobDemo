package com.harundemir918.admobdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class MainActivity extends AppCompatActivity {

    TextView showAdText;
    Button showAdButton, increasePointsButton;
    int totalNumber = 0;

    RewardedAd rewardedAd;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showAdText = findViewById(R.id.showAdText);
        showAdButton = findViewById(R.id.showAdButton);
        increasePointsButton = findViewById(R.id.increasePointsButton);
        adView = findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        rewardedAd = createAndLoadRewardedAd();

        increasePointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalNumber += 1;
                showAdText.setText("Total Points: " + totalNumber);
            }
        });

        showAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rewardedAd.isLoaded()) {
                    RewardedAdCallback adCallback = new RewardedAdCallback() {
                        @Override
                        public void onRewardedAdOpened() {
                            Toast.makeText(MainActivity.this, "Ad opened.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onRewardedAdClosed() {
                            Toast.makeText(MainActivity.this, "Ad closed", Toast.LENGTH_SHORT).show();
                            MainActivity.this.rewardedAd = createAndLoadRewardedAd();
                        }

                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            totalNumber += 10;
                            showAdText.setText("Total Points: " + totalNumber);
                            Toast.makeText(MainActivity.this, "You won 10 points!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onRewardedAdFailedToShow(int errorCode) {
                            Toast.makeText(MainActivity.this, "Ad failed to show", Toast.LENGTH_SHORT).show();
                        }
                    };
                    rewardedAd.show(MainActivity.this, adCallback);
                }
                else {
                    Toast.makeText(MainActivity.this, "Ad does not load yet.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public RewardedAd createAndLoadRewardedAd() {
        RewardedAd rewardedAd = new RewardedAd(this,
                "ca-app-pub-3940256099942544/5224354917");
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                Toast.makeText(MainActivity.this, "Ad successfully loaded.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                Toast.makeText(MainActivity.this, "Ad failed to load.", Toast.LENGTH_SHORT).show();
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }

}
