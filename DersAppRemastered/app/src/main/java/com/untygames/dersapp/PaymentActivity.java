package com.untygames.dersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        init();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

    }

    void init(){
        ImageButton geriBtn = findViewById(R.id.paymentBackButton);
        geriBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PaymentActivity.this, ActivitySettings.class));
                overridePendingTransition(R.anim.slidefromleft,R.anim.slidetoright);
            }
        });

        Button btnBuyMonthlyBtn = findViewById(R.id.payment_btnBuyMonthly);
        Button btnBuyYearlyBtn = findViewById(R.id.payment_btnBuyYearly);
        Button btnBuyWatch = findViewById(R.id.payment_btnBuyWatch);

        btnBuyWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadAds();
            }
        });
    }

    private RewardedAd rewardedAd;
    private final String TAG = "MainActivity";

    void LoadAds(){
        RelativeLayout reklamLoadingLayout = findViewById(R.id.payment_reklamLoadingLayout);

        reklamLoadingLayout.setVisibility(View.VISIBLE);

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-8446349352597923/1823959423",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.toString());
                        rewardedAd = null;
                        Toast.makeText(PaymentActivity.this, "Reklam Yüklenmedi.\nDaha sonra tekrar deneyin.", Toast.LENGTH_SHORT).show();
                        reklamLoadingLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        showRewardedAd();
                        Log.d(TAG, "Ad was loaded.");
                        reklamLoadingLayout.setVisibility(View.GONE);
                    }
                });

    }

    void showRewardedAd(){
        if (rewardedAd != null) {
            Activity activityContext = PaymentActivity.this;
            rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d(TAG, "The user earned the reward.");
                    Toast.makeText(PaymentActivity.this, "Desteğiniz için teşekkürlerr!", Toast.LENGTH_SHORT).show();
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                }
            });
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
            Toast.makeText(PaymentActivity.this, "Reklam Yüklenmedi.\nDaha sonra tekrar deneyin.", Toast.LENGTH_SHORT).show();
        }
    }

}

