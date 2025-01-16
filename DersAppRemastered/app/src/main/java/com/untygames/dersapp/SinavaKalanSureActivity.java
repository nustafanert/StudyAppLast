package com.untygames.dersapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.iwgang.countdownview.CountdownView;

public class SinavaKalanSureActivity extends AppCompatActivity {

    FirebaseFirestore fStore;
    private String yks_tyt_tarih;
    private String yks_ayt_tarih;
    private String yks_msu_tarih;
    private String yks_lgs_tarih;
    private String yks_kpss_tarih;
    private String yks_kpss_onlisans_tarih;

    String TAG = "TAGTAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinava_kalan_sure);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        getDataCloudFirebase();
        //getDataFromRealtimeDatabase();
        init();

        fStore = FirebaseFirestore.getInstance();
        DocumentReference documentReference =fStore.collection("readable_files").document("yks_tyt_tarih");
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

            }
        });
    }

    /*
    private void getDataFromCloud() {
        SharedPreferences prefs = SinavaKalanSureActivity.this.getSharedPreferences(SinavaKalanSureActivity.this.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        fStore = FirebaseFirestore.getInstance();

        if(isNewDay()){
            //TYT
            DocumentReference documentReference =fStore.collection("readable_files").document("yks_tyt_tarih");
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value != null || !value.getString("tarih").toString().equals("null")) {
                        yks_tyt_tarih = value.getString("tarih");
                        countdownTYT();

                        editor.putString("yks_tyt_tarih",yks_tyt_tarih); //tarihi kaydetme
                        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                        editor.putString("sinavaSure_lastTimeGetData",currentDate); //tarihi kaydetme
                        editor.apply();

                    }else{
                        Toast.makeText(SinavaKalanSureActivity.this, "Problem" + error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //

            //AYT
        }else{
            yks_tyt_tarih = prefs.getString("yks_tyt_tarih", "21-06-2029 10:15:00");
            countdownTYT();
        }
    }
     */

    void init(){
        ImageButton geriBtn = findViewById(R.id.sinavkalansure_geriBtn);
        geriBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SinavaKalanSureActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.slidefromright,R.anim.slidetoleft);
            }
        });
    }
    private DatabaseReference mDatabase;
    private void getDataFromRealtimeDatabase() {
        //REALTIME DATABASE
        mDatabase = FirebaseDatabase.getInstance().getReference().child("readable_files");

        SharedPreferences prefs = SinavaKalanSureActivity.this.getSharedPreferences(SinavaKalanSureActivity.this.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        RelativeLayout progressbar = findViewById(R.id.sinavakalansure_progressbar);
        progressbar.setVisibility(View.VISIBLE);

        if(isNewDay() && internetIsConnected()){
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    Log.w(TAG, "onDataChange");

                    FirebaseData_SinavaKalanSure val = dataSnapshot.getValue(FirebaseData_SinavaKalanSure.class);

                    if(val != null){
                        progressbar.setVisibility(View.GONE);

                        yks_tyt_tarih = val.yks_tyt_tarih;
                        yks_ayt_tarih = val.yks_ayt_tarih;
                        yks_msu_tarih = val.yks_msu_tarih;
                        yks_lgs_tarih = val.yks_lgs_tarih;
                        yks_kpss_tarih = val.yks_kpss_tarih;
                        yks_kpss_onlisans_tarih = val.yks_kpss_onlisans_tarih;

                        countdowns();
                    }

                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    editor.putString("sinavaSure_lastTimeGetData",currentDate); //tarihi kaydetme

                    editor.putString("yks_tyt_tarih",yks_tyt_tarih); //tarihi kaydetme
                    editor.putString("yks_ayt_tarih",yks_ayt_tarih); //tarihi kaydetme
                    editor.putString("yks_msu_tarih",yks_msu_tarih); //tarihi kaydetme
                    editor.putString("yks_lgs_tarih",yks_lgs_tarih); //tarihi kaydetme
                    editor.putString("yks_kpss_tarih",yks_kpss_tarih); //tarihi kaydetme
                    editor.putString("yks_kpss_onlisans_tarih",yks_kpss_onlisans_tarih); //tarihi kaydetme

                    editor.apply();
                    // ..
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());

                    tarih_getStrings();
                    countdowns();
                    progressbar.setVisibility(View.GONE);
                }
            };
            mDatabase.addValueEventListener(postListener);
        }else{
            progressbar.setVisibility(View.GONE);

            tarih_getStrings();
            countdowns();
        }
    }

    void getDataCloudFirebase(){
        //CLOUD FIRESTORE
        SharedPreferences prefs = SinavaKalanSureActivity.this.getSharedPreferences(SinavaKalanSureActivity.this.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        RelativeLayout progressbar = findViewById(R.id.sinavakalansure_progressbar);
        progressbar.setVisibility(View.VISIBLE);

        fStore = FirebaseFirestore.getInstance();

        if(isNewDay() && internetIsConnected()){
            DocumentReference documentReference =fStore.collection("readable_files").document("tarihler");
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    FirebaseData_SinavaKalanSure val = documentSnapshot.toObject(FirebaseData_SinavaKalanSure.class);
                    Log.w(TAG, "GetData");
                    progressbar.setVisibility(View.GONE);

                    if(val != null){
                        yks_tyt_tarih = val.yks_tyt_tarih;
                        yks_ayt_tarih = val.yks_ayt_tarih;
                        yks_msu_tarih = val.yks_msu_tarih;
                        yks_lgs_tarih = val.yks_lgs_tarih;
                        yks_kpss_tarih = val.yks_kpss_tarih;
                        yks_kpss_onlisans_tarih = val.yks_kpss_onlisans_tarih;

                        countdowns();
                    }

                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    editor.putString("sinavaSure_lastTimeGetData",currentDate); //tarihi kaydetme

                    editor.putString("yks_tyt_tarih",yks_tyt_tarih); //tarihi kaydetme
                    editor.putString("yks_ayt_tarih",yks_ayt_tarih); //tarihi kaydetme
                    editor.putString("yks_msu_tarih",yks_msu_tarih); //tarihi kaydetme
                    editor.putString("yks_lgs_tarih",yks_lgs_tarih); //tarihi kaydetme
                    editor.putString("yks_kpss_tarih",yks_kpss_tarih); //tarihi kaydetme
                    editor.putString("yks_kpss_onlisans_tarih",yks_kpss_onlisans_tarih); //tarihi kaydetme

                    editor.apply();
                    // ..
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressbar.setVisibility(View.GONE);

                    tarih_getStrings();
                    countdowns();
                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    progressbar.setVisibility(View.GONE);

                    tarih_getStrings();
                    countdowns();
                }
            });
        }else{
            progressbar.setVisibility(View.GONE);

            tarih_getStrings();
            countdowns();
        }
    }

    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }
    private boolean isNewDay() {
        SharedPreferences prefs = SinavaKalanSureActivity.this.getSharedPreferences(SinavaKalanSureActivity.this.getPackageName(), Context.MODE_PRIVATE);
        String lastGetDataTime = prefs.getString("sinavaSure_lastTimeGetData", "01-01-2023");
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());


        return !currentDate.equals(lastGetDataTime);
    }

    void countdowns(){
        countdownTYT();
        countdownAYT();
        countdownMSU();
        countdownLGS();
        countdownKPSS();
        countdownKPSSOnlisans();
    }
    void tarih_getStrings(){
        SharedPreferences prefs = SinavaKalanSureActivity.this.getSharedPreferences(SinavaKalanSureActivity.this.getPackageName(), Context.MODE_PRIVATE);

        yks_tyt_tarih = prefs.getString("yks_tyt_tarih", "21-06-2025 10:15:00");
        yks_ayt_tarih = prefs.getString("yks_ayt_tarih", "22-06-2025 10:15:00");
        yks_msu_tarih = prefs.getString("yks_msu_tarih", "02-03-2025 10:15:00");
        yks_lgs_tarih = prefs.getString("yks_lgs_tarih", "15-06-2025 09:30:00");
        yks_kpss_tarih = prefs.getString("yks_kpss_tarih", "13-07-2025 10:15:00");
        yks_kpss_onlisans_tarih = prefs.getString("yks_kpss_onlisans_tarih", "01-09-2026 10:15:00");
    }

    private void countdownTYT() {
        CountdownView mCvCountdownView = findViewById(R.id.sinavakalansure_countdown_tyt);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String countDate = yks_tyt_tarih;
        Date now = new Date();


        try {
            //Formatting from String to Date
            Date date = sdf.parse(countDate);
            long currentTime = now.getTime();
            long newYearDate = date.getTime();
            long countDownToNewYear = newYearDate - currentTime;
            mCvCountdownView.start(countDownToNewYear);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private void countdownAYT() {
        CountdownView mCvCountdownView = findViewById(R.id.sinavakalansure_countdown_ayt);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String countDate = yks_ayt_tarih;
        Date now = new Date();

        try {
            //Formatting from String to Date
            Date date = sdf.parse(countDate);
            long currentTime = now.getTime();
            long newYearDate = date.getTime();
            long countDownToNewYear = newYearDate - currentTime;
            mCvCountdownView.start(countDownToNewYear);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private void countdownMSU() {
        CountdownView mCvCountdownView = findViewById(R.id.sinavakalansure_countdown_msu);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String countDate = yks_msu_tarih;
        Date now = new Date();

        try {
            //Formatting from String to Date
            Date date = sdf.parse(countDate);
            long currentTime = now.getTime();
            long newYearDate = date.getTime();
            long countDownToNewYear = newYearDate - currentTime;
            mCvCountdownView.start(countDownToNewYear);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private void countdownLGS() {
        CountdownView mCvCountdownView = findViewById(R.id.sinavakalansure_countdown_lgs);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String countDate = yks_lgs_tarih;
        Date now = new Date();

        try {
            //Formatting from String to Date
            Date date = sdf.parse(countDate);
            long currentTime = now.getTime();
            long newYearDate = date.getTime();
            long countDownToNewYear = newYearDate - currentTime;
            mCvCountdownView.start(countDownToNewYear);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private void countdownKPSS() {
        CountdownView mCvCountdownView = findViewById(R.id.sinavakalansure_countdown_kpss);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String countDate = yks_kpss_tarih;
        Date now = new Date();

        try {
            //Formatting from String to Date
            Date date = sdf.parse(countDate);
            long currentTime = now.getTime();
            long newYearDate = date.getTime();
            long countDownToNewYear = newYearDate - currentTime;
            mCvCountdownView.start(countDownToNewYear);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private void countdownKPSSOnlisans() {
        CountdownView mCvCountdownView = findViewById(R.id.sinavakalansure_countdown_kpssonlisans);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String countDate = yks_kpss_onlisans_tarih;
        Date now = new Date();

        try {
            //Formatting from String to Date
            Date date = sdf.parse(countDate);
            long currentTime = now.getTime();
            long newYearDate = date.getTime();
            long countDownToNewYear = newYearDate - currentTime;
            mCvCountdownView.start(countDownToNewYear);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    void FirebaseCloseConnection(){

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}