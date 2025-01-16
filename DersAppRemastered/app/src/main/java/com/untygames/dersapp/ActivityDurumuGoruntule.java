package com.untygames.dersapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ActivityDurumuGoruntule extends AppCompatActivity {

    String currentDate;

    public static String StaticCurrentDate = "";

    String activeGun;
    String activeAy;
    String activeYil;

    public static int yuzdeYapilacaklar = 0;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_durumu_goruntule);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);

        if(!(prefs.getBoolean("reklam_kaldir",false))){
            mAdView = findViewById(R.id.bannerAd);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdClicked() {
                    Log.d("TAGTAG", "onAdClicked: ");
                }

                @Override
                public void onAdClosed() {
                    Log.d("TAGTAG", "onAdClosed: ");
                }

                @Override
                public void onAdFailedToLoad(LoadAdError adError) {
                    Log.d("TAGTAG", "onAdFailedToLoad: " + adError.getMessage().toString());
                    Log.d("TAGTAG", "onAdFailedToLoad: " + adError.getCode());
                }

                @Override
                public void onAdImpression() {
                    Log.d("TAGTAG", "onAdImpression: ");
                }

                @Override
                public void onAdLoaded() {
                    Log.d("TAGTAG", "onAdLoaded: ");
                }

                @Override
                public void onAdOpened() {
                    Log.d("TAGTAG", "onAdOpened: ");
                }
            });
        }else{
            Log.d("TAGTAG", "premium aktif");
        }


        EditText yapilacaklarYuzdeEt = findViewById(R.id.yapilacaklarYuzdeEt);
        yapilacaklarYuzdeEt.setFilters(new InputFilter[]{ new Nunber24Filter("0", "100")});

        //String ilkGirisTarihi = prefs.getString("ilkGirisTarihi", "01-01-2023");
        //currentDate = ilkGirisTarihi;

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        if(StaticCurrentDate.equals("")){
            StaticCurrentDate=currentDate;
        }else{
            currentDate = StaticCurrentDate;
        }

        List<String> s = Arrays.asList(currentDate.split("-"));

        activeGun = s.get(0);
        activeAy = s.get(1);
        activeYil = s.get(2);

        init();
        oku1();
        oku2();

        DurumuDegistirKontrol();
    }

    boolean isGunlukDurumActive;
    void DurumuDegistirKontrol(){
        isGunlukDurumActive = true;
        DurumuDegistir();
        Button durumuGoruntule_gunlukBtn = findViewById(R.id.durumuGoruntule_gunlukBtn);
        Button durumuGoruntule_aylikBtn = findViewById(R.id.durumuGoruntule_aylikBtn);

        durumuGoruntule_gunlukBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isGunlukDurumActive){
                    return;
                }
                isGunlukDurumActive = true;
                durumuGoruntule_gunlukBtn.setBackgroundResource(R.drawable.bg_alti_cizim);
                durumuGoruntule_aylikBtn.setBackgroundResource(R.drawable.bg_alti_cizim1);
                currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                List<String> s = Arrays.asList(currentDate.split("-"));

                activeGun = s.get(0);
                activeAy = s.get(1);
                activeYil = s.get(2);
                DurumuDegistir();
            }
        });

        durumuGoruntule_aylikBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isGunlukDurumActive){
                    return;
                }
                isGunlukDurumActive = false;
                durumuGoruntule_gunlukBtn.setBackgroundResource(R.drawable.bg_alti_cizim1);
                durumuGoruntule_aylikBtn.setBackgroundResource(R.drawable.bg_alti_cizim);
                DurumuDegistir();
            }
        });
    }

    void DurumuDegistir(){
        PieChart pieChart = findViewById(R.id.pieChart);
        Button pieChartTamEkran = findViewById(R.id.btnPieChartTamEkran);
        //LinearLayout //Yuzdelerin tasarımını düzelt
        TextView tarih = findViewById(R.id.activeDateView);
        LinearLayout durum_hedefler = findViewById(R.id.durum_hedefler);
        LinearLayout durumuGoruntule_notlar = findViewById(R.id.durumuGoruntule_notlar);
        Button btnDurumGoruntuleEdit = findViewById(R.id.btnDurumGoruntuleEdit);

        if(isGunlukDurumActive){
            //pieChart.setVisibility(View.GONE);
            //pieChartTamEkran.setVisibility(View.GONE);


            oku1();
            oku2();
            durum_hedefler.setVisibility(View.VISIBLE);
            durumuGoruntule_notlar.setVisibility(View.VISIBLE);
            btnDurumGoruntuleEdit.setVisibility(View.VISIBLE);
        }
        else{
            //pieChart.setVisibility(View.VISIBLE);
            //pieChartTamEkran.setVisibility(View.VISIBLE);
            LoadDataMonthly();
            String strAy = "OCAK";
            String ssMonth = (activeAy+"");
            if(ssMonth.length() == 1){
                activeAy = "0" + ssMonth;
            }

            switch (activeAy){
                case "01":
                    strAy = "OCAK";
                    break;
                case "02":
                    strAy = "ŞUBAT";
                    break;
                case "03":
                    strAy = "MART";
                    break;
                case "04":
                    strAy = "NİSAN";
                    break;
                case "05":
                    strAy = "MAYIS";
                    break;
                case "06":
                    strAy = "HAZİRAN";
                    break;
                case "07":
                    strAy = "TEMMUZ";
                    break;
                case "08":
                    strAy = "AĞUSTOS";
                    break;
                case "09":
                    strAy = "EYLÜL";
                    break;
                case "10":
                    strAy = "EKİM";
                    break;
                case "11":
                    strAy = "KASIM";
                    break;
                case "12":
                    strAy = "ARALIK";
                    break;
            }
            Log.d("TAGTAG", "DurumuDegistir: " + strAy);
            tarih.setText(strAy +" "+ activeYil + "\n-Tıkla Değiştir-");
            durum_hedefler.setVisibility(View.GONE);
            durumuGoruntule_notlar.setVisibility(View.GONE);
            btnDurumGoruntuleEdit.setVisibility(View.GONE);
        }
    }

    void init(){
        EditText text = findViewById(R.id.durum_GünNotEdit);
        text.setFocusable(false);
        text.setFocusableInTouchMode(false);

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        LinearLayout yuzdeGosterView = findViewById(R.id.yuzdeGosterView);
        if(prefs.getBoolean("checkBoxAyarlar_yuzdeGoster", true)){
            yuzdeGosterView.setVisibility(View.VISIBLE);
        }else{
            yuzdeGosterView.setVisibility(View.GONE);
        }

        EditText yapilacaklarYuzdeEt = findViewById(R.id.yapilacaklarYuzdeEt);
        yapilacaklarYuzdeEt.setFocusable(false);
        yapilacaklarYuzdeEt.setFocusableInTouchMode(false);


        LinearLayout durum_hedefler = findViewById(R.id.durum_hedefler);
        if(prefs.getInt("gunluk_hedef_soru_sayisi",0) == 0 && prefs.getInt("gunluk_hedef_calisma_sayisi",0) == 0){
            durum_hedefler.setVisibility(View.GONE);
        }

        RelativeLayout pieChartFullScreenView = findViewById(R.id.pieChartFullScreenView);

        Button btnPieChartTamEkran = findViewById(R.id.btnPieChartTamEkran);
        Button btnPieChartTamEkranKapat = findViewById(R.id.btnPieChartTamEkranKapat);

        btnPieChartTamEkran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pieChartFullScreenView.setVisibility(View.VISIBLE);
                isPieChartFullscreen=true;
                PieChartLoad();
            }
        });
        btnPieChartTamEkranKapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pieChartFullScreenView.setVisibility(View.GONE);
                isPieChartFullscreen=false;
                PieChartLoad();
            }
        });


        Button Onceki = findViewById(R.id.btnDurumGoruntuleÖnceki);
        Button Menu = findViewById(R.id.btnDurumGoruntuleMenu);
        Button Sonraki = findViewById(R.id.btnDurumGoruntuleSonraki);

        Onceki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isGunlukDurumActive){
                    if(Integer.parseInt(activeGun) > 1){
                        int a = (Integer.parseInt(activeGun) - 1);
                        String ss = a + "";
                        if(ss.length() == 1){
                            ss = "0" + a;
                        }
                        activeGun = ss;
                    }else if(Integer.parseInt(activeGun) == 1){
                        activeGun="31";
                        String ssMonth = (activeAy+"");
                        if(ssMonth.length() == 1){
                            activeAy = "0" + ssMonth;
                        }
                        if(Integer.parseInt(activeAy) == 1){
                            activeAy = "12";
                            int IntActiveYil = Integer.parseInt(activeYil)-1;
                            activeYil = IntActiveYil + "";
                        }else{
                            int ayint= Integer.parseInt(activeAy)-1;
                            activeAy = "" + ayint;
                        }
                    }
                    oku2();
                }else{
                    if(Integer.parseInt(activeAy) > 1){
                        int a = (Integer.parseInt(activeAy) - 1);
                        String ss = a + "";
                        if(ss.length() == 1){
                            ss = "0" + a;
                        }
                        activeAy = ss;
                        Log.d("TAGTAG", "activeAy: "+activeAy);
                    }else if(Integer.parseInt(activeAy) == 1){
                        if(Integer.parseInt(activeAy) == 1){
                            activeAy = "12";
                            int IntActiveYil = Integer.parseInt(activeYil)-1;
                            activeYil = IntActiveYil + "";
                        }else{
                            int ayint= Integer.parseInt(activeAy)-1;
                            activeAy = "" + ayint;
                        }
                    }
                    DurumuDegistir();
                }
            }
        });

        Sonraki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isGunlukDurumActive){
                    if(Integer.parseInt(activeGun) < 31){
                        int a = (Integer.parseInt(activeGun) + 1);
                        String ss = a + "";
                        if(ss.length() == 1){
                            ss = "0" + a;
                        }
                        activeGun = ss;
                    }else if(Integer.parseInt(activeGun) == 31){
                        activeGun="01";
                        if(Integer.parseInt(activeAy) == 12){
                            activeAy = "01";
                            int IntActiveYil = Integer.parseInt(activeYil)+1;
                            activeYil = IntActiveYil + "";
                        }else{
                            int ayint= Integer.parseInt(activeAy)+1;
                            activeAy = "" + ayint;
                        }
                    }
                    oku2();
                }else{
                    if(Integer.parseInt(activeAy) == 12){
                        activeAy = "01";
                        int IntActiveYil = Integer.parseInt(activeYil)+1;
                        activeYil = IntActiveYil + "";
                    }else{
                        int ayint= Integer.parseInt(activeAy)+1;
                        activeAy = "" + ayint;
                    }
                    DurumuDegistir();
                    LoadDataMonthly();
                }
            }
        });

        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticCurrentDate = "";

                startActivity(new Intent(ActivityDurumuGoruntule.this, MainActivity.class));
                overridePendingTransition(R.anim.slidefromright,R.anim.slidetoleft);
            }
        });
    }

    @Override
    public void onBackPressed() {
        StaticCurrentDate = "";

        startActivity(new Intent(ActivityDurumuGoruntule.this, MainActivity.class));
        overridePendingTransition(R.anim.slidefromright,R.anim.slidetoleft);
    }


    List<EditText> edits = new ArrayList<>();
    List<LinearLayout> dersLinears = new ArrayList<>();
    void oku1(){
        LinearLayout matLinear = findViewById(R.id.dersCalis_calisilan_ders_mat_ekran);
        EditText matEdittext = findViewById(R.id.dersCalis_calisilan_ders_mat);

        LinearLayout edbLinear = findViewById(R.id.dersCalis_calisilan_ders_edb_ekran);
        EditText edbEdittext = findViewById(R.id.dersCalis_calisilan_ders_edb);

        LinearLayout fizikLinear = findViewById(R.id.dersCalis_calisilan_ders_fizik_ekran);
        EditText fizikEdittext = findViewById(R.id.dersCalis_calisilan_ders_fizik);

        LinearLayout kimyaLinear = findViewById(R.id.dersCalis_calisilan_ders_kimya_ekran);
        EditText kimyaEdittext = findViewById(R.id.dersCalis_calisilan_ders_kimya);

        LinearLayout biyoLinear = findViewById(R.id.dersCalis_calisilan_ders_biyoloji_ekran);
        EditText biyoEdittext = findViewById(R.id.dersCalis_calisilan_ders_biyoloji);

        LinearLayout tarihLinear = findViewById(R.id.dersCalis_calisilan_ders_tarih_ekran);
        EditText tarihEdittext = findViewById(R.id.dersCalis_calisilan_ders_tarih);

        LinearLayout ingLinear = findViewById(R.id.dersCalis_calisilan_ders_ingilizce_ekran);
        EditText ingEdittext = findViewById(R.id.dersCalis_calisilan_ders_ingilizce);

        LinearLayout dinLinear = findViewById(R.id.dersCalis_calisilan_ders_DinKültürüveAhlakBilgisi_ekran);
        EditText dinEdittext = findViewById(R.id.dersCalis_calisilan_ders_DinKültürüveAhlakBilgisi);

        LinearLayout cografyaLinear = findViewById(R.id.dersCalis_calisilan_ders_cografya_ekran);
        EditText cografyaEdittext = findViewById(R.id.dersCalis_calisilan_ders_cografya);

        LinearLayout almancaLinear = findViewById(R.id.dersCalis_calisilan_ders_almanca_ekran);
        EditText almancaEdittext = findViewById(R.id.dersCalis_calisilan_ders_almanca);

        LinearLayout saglikLinear = findViewById(R.id.dersCalis_calisilan_ders_SağlıkBilgisi_ekran);
        EditText saglikEdittext = findViewById(R.id.dersCalis_calisilan_ders_SağlıkBilgisi);

        LinearLayout diger1Linear = findViewById(R.id.dersCalis_calisilan_ders_Diğer1_ekran);
        EditText diger1Edittext = findViewById(R.id.dersCalis_calisilan_ders_Diğer1);

        LinearLayout diger2Linear = findViewById(R.id.dersCalis_calisilan_ders_Diğer2_ekran);
        EditText diger2Edittext = findViewById(R.id.dersCalis_calisilan_ders_Diğer2);


        LinearLayout aytmatLinear = findViewById(R.id.dersCalis_calisilan_ders_aytmat_ekran);
        EditText aytmatEdittext = findViewById(R.id.dersCalis_calisilan_ders_aytmat);

        LinearLayout aytedbLinear = findViewById(R.id.dersCalis_calisilan_ders_aytedb_ekran);
        EditText aytedbEdittext = findViewById(R.id.dersCalis_calisilan_ders_aytedb);

        LinearLayout aytfizikLinear = findViewById(R.id.dersCalis_calisilan_ders_aytfizik_ekran);
        EditText aytfizikEdittext = findViewById(R.id.dersCalis_calisilan_ders_aytfizik);

        LinearLayout aytkimyaLinear = findViewById(R.id.dersCalis_calisilan_ders_aytkimya_ekran);
        EditText aytkimyaEdittext = findViewById(R.id.dersCalis_calisilan_ders_aytkimya);

        LinearLayout aytbiyoLinear = findViewById(R.id.dersCalis_calisilan_ders_aytbiyo_ekran);
        EditText aytbiyoEdittext = findViewById(R.id.dersCalis_calisilan_ders_aytbiyo);

        LinearLayout tytmatLinear = findViewById(R.id.dersCalis_calisilan_ders_tytmat_ekran);
        EditText tytmatEdittext = findViewById(R.id.dersCalis_calisilan_ders_tytmat);

        LinearLayout tytturkceLinear = findViewById(R.id.dersCalis_calisilan_ders_tytturkce_ekran);
        EditText tytturkceEdittext = findViewById(R.id.dersCalis_calisilan_ders_tytturkce);

        LinearLayout tytfizikLinear = findViewById(R.id.dersCalis_calisilan_ders_tytfizik_ekran);
        EditText tytfizikEdittext = findViewById(R.id.dersCalis_calisilan_ders_tytfizik);

        LinearLayout tytkimyaLinear = findViewById(R.id.dersCalis_calisilan_ders_tytkimya_ekran);
        EditText tytkimyaEdittext = findViewById(R.id.dersCalis_calisilan_ders_tytkimya);

        LinearLayout tytbiyoLinear = findViewById(R.id.dersCalis_calisilan_ders_tytbiyo_ekran);
        EditText tytbiyoEdittext = findViewById(R.id.dersCalis_calisilan_ders_tytbiyo);


        LinearLayout geoLinear = findViewById(R.id.dersCalis_calisilan_ders_geo_ekran);
        EditText geoEdittext = findViewById(R.id.dersCalis_calisilan_ders_geo);

        LinearLayout paragrafLinear = findViewById(R.id.dersCalis_calisilan_ders_paragraf_ekran);
        EditText paragrafEdittext = findViewById(R.id.dersCalis_calisilan_ders_paragraf);

        LinearLayout problemLinear = findViewById(R.id.dersCalis_calisilan_ders_problem_ekran);
        EditText problemEdittext = findViewById(R.id.dersCalis_calisilan_ders_problem);

        edits.clear();

        edits.add(matEdittext);
        edits.add(edbEdittext);
        edits.add(fizikEdittext);
        edits.add(kimyaEdittext);
        edits.add(biyoEdittext);
        edits.add(tarihEdittext);
        edits.add(ingEdittext);
        edits.add(dinEdittext);
        edits.add(cografyaEdittext);
        edits.add(almancaEdittext);
        edits.add(saglikEdittext);
        edits.add(diger1Edittext);
        edits.add(diger2Edittext);

        edits.add(aytmatEdittext);
        edits.add(aytedbEdittext);
        edits.add(aytfizikEdittext);
        edits.add(aytkimyaEdittext);
        edits.add(aytbiyoEdittext);

        edits.add(tytmatEdittext);
        edits.add(tytturkceEdittext);
        edits.add(tytfizikEdittext);
        edits.add(tytkimyaEdittext);
        edits.add(tytbiyoEdittext);

        edits.add(geoEdittext);
        edits.add(paragrafEdittext);
        edits.add(problemEdittext);

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);

        String strders = prefs.getString("calisilacak_ders_isimleri", "");

        String[] parts = strders.split(", ");
/*
        "Matematik","Türk Dili ve Edebiyatı","Fizik","Kimya","Biyoloji","Tarih","İngilizce","Din Kültürü ve Ahlak Bilgisi","Coğrafya","Almanca","Sağlık Bilgisi","Diğer 1","Diğer 2",
                "AYT Matematik","AYT Edebiyat","AYT Fizik","AYT Kimya","AYT Biyoloji",
                "TYT Matematik","TYT Türkçe","TYT Fizik","TYT Kimya","TYT Biyoloji"*/

        dersLinears.clear();

        dersLinears.add(matLinear);
        dersLinears.add(edbLinear);
        dersLinears.add(fizikLinear);
        dersLinears.add(kimyaLinear);
        dersLinears.add(biyoLinear);
        dersLinears.add(tarihLinear);
        dersLinears.add(ingLinear);
        dersLinears.add(dinLinear);
        dersLinears.add(cografyaLinear);
        dersLinears.add(almancaLinear);
        dersLinears.add(saglikLinear);
        dersLinears.add(diger1Linear);
        dersLinears.add(diger2Linear);

        dersLinears.add(aytmatLinear);
        dersLinears.add(aytedbLinear);
        dersLinears.add(aytfizikLinear);
        dersLinears.add(aytkimyaLinear);
        dersLinears.add(aytbiyoLinear);

        dersLinears.add(tytmatLinear);
        dersLinears.add(tytturkceLinear);
        dersLinears.add(tytfizikLinear);
        dersLinears.add(tytkimyaLinear);
        dersLinears.add(tytbiyoLinear);

        dersLinears.add(geoLinear);
        dersLinears.add(paragrafLinear);
        dersLinears.add(problemLinear);

        for(LinearLayout lr : dersLinears){
            lr.setVisibility(View.GONE);
        }

        for (String asil : parts){
            switch (asil){
                case "İngilizce":
                    ingLinear.setVisibility(View.VISIBLE);
                    break;
                case "Din Kültürü ve Ahlak Bilgisi":
                    dinLinear.setVisibility(View.VISIBLE);
                    break;
                case "Diğer 1":
                    diger1Linear.setVisibility(View.VISIBLE);
                    break;
                case "Diğer 2":
                    diger2Linear.setVisibility(View.VISIBLE);
                    break;
                case "Almanca":
                    almancaLinear.setVisibility(View.VISIBLE);
                    break;
                case "Coğrafya":
                    cografyaLinear.setVisibility(View.VISIBLE);
                    break;
                case "Sağlık Bilgisi":
                    saglikLinear.setVisibility(View.VISIBLE);
                    break;
                case "Matematik":
                    matLinear.setVisibility(View.VISIBLE);
                    break;
                case "Türk Dili ve Edebiyatı":
                    edbLinear.setVisibility(View.VISIBLE);
                    break;
                case "Fizik":
                    fizikLinear.setVisibility(View.VISIBLE);
                    break;
                case "Kimya":
                    kimyaLinear.setVisibility(View.VISIBLE);
                    break;
                case "Biyoloji":
                    biyoLinear.setVisibility(View.VISIBLE);
                    break;
                case "Tarih":
                    tarihLinear.setVisibility(View.VISIBLE);
                    break;

                case "AYT Matematik":
                    aytmatLinear.setVisibility(View.VISIBLE);
                    break;
                case "AYT Edebiyat":
                    aytedbLinear.setVisibility(View.VISIBLE);
                    break;
                case "AYT Fizik":
                    aytfizikLinear.setVisibility(View.VISIBLE);
                    break;
                case "AYT Kimya":
                    aytkimyaLinear.setVisibility(View.VISIBLE);
                    break;
                case "AYT Biyoloji":
                    aytbiyoLinear.setVisibility(View.VISIBLE);
                    break;

                case "TYT Matematik":
                    tytmatLinear.setVisibility(View.VISIBLE);
                    break;
                case "TYT Türkçe":
                    tytturkceLinear.setVisibility(View.VISIBLE);
                    break;
                case "TYT Fizik":
                    tytfizikLinear.setVisibility(View.VISIBLE);
                    break;
                case "TYT Kimya":
                    tytkimyaLinear.setVisibility(View.VISIBLE);
                    break;
                case "TYT Biyoloji":
                    tytbiyoLinear.setVisibility(View.VISIBLE);
                    break;

                case "Geometri":
                    geoLinear.setVisibility(View.VISIBLE);
                    break;
                case "Paragraf":
                    paragrafLinear.setVisibility(View.VISIBLE);
                    break;
                case "Problem":
                    problemLinear.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    int cozulenSoru;
    boolean isEditableActive = false;
    public void DuzenleVeKaydet(View view){
        Button btn = findViewById(R.id.btnDurumGoruntuleEdit);
        if(!isEditableActive){
            for(EditText et : edits){
                et.setEnabled(true);
            }
            EditText text = findViewById(R.id.durum_GünNotEdit);
            text.setFocusable(true);
            text.setFocusableInTouchMode(true);

            EditText yapilacaklarYuzdeEt = findViewById(R.id.yapilacaklarYuzdeEt);
            yapilacaklarYuzdeEt.setFocusable(true);
            yapilacaklarYuzdeEt.setFocusableInTouchMode(true);

            isEditableActive = true;
            StaticCurrentDate = currentDate;
            btn.setText("KAYDET");

            EditText dersCalis_sureEditText = findViewById(R.id.dersCalis_sureEditText);
            dersCalis_sureEditText.setEnabled(true);

            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDurumuGoruntule.this);

            builder.setTitle("Verileri Değiştir");
            builder.setMessage("Soru sayılarını güncelleyebilirsiniz.");

            builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDurumuGoruntule.this);

            builder.setTitle("Veriler Değiştirilsin Mi?");
            builder.setMessage("Bugünle ilgili eski verileriniz silinip yerine bu veriler yazılacak emin misiniz?");
            builder.setCancelable(false);
            builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();

                    EditText dersCalis_sureEditText = findViewById(R.id.dersCalis_sureEditText);
                    if(dersCalis_sureEditText.getText().toString().equals("")){
                        dersCalis_sureEditText.setText("0");
                    }
                    dersCalis_sureEditText.setEnabled(false);

                    int sure1 = prefs.getInt(currentDate + "_tarihinde_calisilan_sure", 0);

                    editor.putInt(currentDate + "_tarihinde_calisilan_sure", Integer.parseInt(dersCalis_sureEditText.getText().toString()));

                    int sure2 = Integer.parseInt(dersCalis_sureEditText.getText().toString());

                    int fark = sure2 - sure1;
                    int önceSimdiyeKadarCalisilanSure = prefs.getInt("simdiye_kadar_calisilan_sure", 0);
                    editor.putInt("simdiye_kadar_calisilan_sure",önceSimdiyeKadarCalisilanSure + fark);//Şimdiye Kadar Bütün Çalışılan Saat

                    önceSimdiyeKadarDate = prefs.getInt(currentDate + "_tarihinde_cozulen_soru", 0);

                    for(EditText et : edits){
                        et.setEnabled(false);
                    }
                    EditText text = findViewById(R.id.durum_GünNotEdit);
                    text.setFocusable(false);
                    text.setFocusableInTouchMode(false);

                    EditText yapilacaklarYuzdeEt = findViewById(R.id.yapilacaklarYuzdeEt);
                    if(yapilacaklarYuzdeEt.getText().toString().equals("")){
                        yapilacaklarYuzdeEt.setText("0");
                    }
                    yapilacaklarYuzdeEt.setFocusable(false);
                    yapilacaklarYuzdeEt.setFocusableInTouchMode(false);

                    editor.putString(currentDate + "_tarihinde_not", text.getText().toString());

                    editor.putInt(currentDate + "_tarihinde_yuzde", Integer.parseInt(yapilacaklarYuzdeEt.getText().toString()));
                    editor.apply();

                    for (int i=0;i<edits.size();i++){
                        EditText et = edits.get(i);
                        if(et.getText().equals("") || et.getText().equals(null)){
                            et.setText("0");
                        }
                        int soruSay=0;
                        try {
                            soruSay= Integer.parseInt(et.getText().toString());
                        }catch (Exception e){
                            soruSay=0;
                        }
                        switch (i){
                            case 0:
                                DersSoruKaydet("mat", soruSay);
                                break;
                            case 1:
                                DersSoruKaydet("edb", soruSay);
                                break;
                            case 2:
                                DersSoruKaydet("fizik", soruSay);
                                break;
                            case 3:
                                DersSoruKaydet("kimya", soruSay);
                                break;
                            case 4:
                                DersSoruKaydet("biyo", soruSay);
                                break;
                            case 5:
                                DersSoruKaydet("tarih", soruSay);
                                break;
                            case 6:
                                DersSoruKaydet("ing", soruSay);
                                break;
                            case 7:
                                DersSoruKaydet("din", soruSay);
                                break;
                            case 8:
                                DersSoruKaydet("cografya", soruSay);
                                break;
                            case 9:
                                DersSoruKaydet("almanca", soruSay);
                                break;
                            case 10:
                                DersSoruKaydet("saglikbilgisi", soruSay);
                                break;
                            case 11:
                                DersSoruKaydet("diger1", soruSay);
                                break;
                            case 12:
                                DersSoruKaydet("diger2", soruSay);
                                break;
                            case 13:
                                DersSoruKaydet("aytmat", soruSay);
                                break;
                            case 14:
                                DersSoruKaydet("aytedb", soruSay);
                                break;
                            case 15:
                                DersSoruKaydet("aytfizik", soruSay);
                                break;
                            case 16:
                                DersSoruKaydet("aytkimya", soruSay);
                                break;
                            case 17:
                                DersSoruKaydet("aytbiyo", soruSay);
                                break;
                            case 18:
                                DersSoruKaydet("tytmat", soruSay);
                                break;
                            case 19:
                                DersSoruKaydet("tytturkce", soruSay);
                                break;
                            case 20:
                                DersSoruKaydet("tytfizik", soruSay);
                                break;
                            case 21:
                                DersSoruKaydet("tytkimya", soruSay);
                                break;
                            case 22:
                                DersSoruKaydet("tytbiyo", soruSay);
                                break;
                            case 23:
                                DersSoruKaydet("geo", soruSay);
                                break;
                            case 24:
                                DersSoruKaydet("paragraf", soruSay);
                                break;
                            case 25:
                                DersSoruKaydet("problem", soruSay);
                                DersSoruTopla();
                                break;
                        }
                    }
                    isEditableActive = false;
                    btn.setText("DÜZENLE");
                }
            });

            builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(ActivityDurumuGoruntule.this, ActivityDurumuGoruntule.class));
                    overridePendingTransition(R.anim.slidetoup,R.anim.slidefrombottom);
                }
            });
            builder.show();
        }
    }
    int önceSimdiyeKadarDate;
    void DersSoruKaydet(String name, int soruSay){
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(currentDate + "_tarihinde_"+ name +"_cozulen_soru", soruSay);

        cozulenSoru += soruSay;

        editor.commit();
    }
    void DersSoruTopla(){
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int önceSimdiyeKadar = prefs.getInt("simdiye_kadar_cozulen_soru", 0);
        editor.putInt(currentDate + "_tarihinde_cozulen_soru", cozulenSoru);
        int topSimdiyeKadar = (cozulenSoru - önceSimdiyeKadarDate) + önceSimdiyeKadar;
        editor.putInt("simdiye_kadar_cozulen_soru", topSimdiyeKadar);//Şimdiye Kadar Bütün Çözülen Soru

        editor.commit();

        startActivity(new Intent(this, ActivityDurumuGoruntule.class));
        overridePendingTransition(R.anim.slidetoup,R.anim.slidefrombottom);
    }

    void oku2(){
        TextView textView = findViewById(R.id.activeDateView);

        String strAy = "OCAK";

        String ssDay = (activeGun+"");
        if(ssDay.length() == 1){
            activeGun = "0" + ssDay;
        }

        String ssMonth = (activeAy+"");
        if(ssMonth.length() == 1){
            activeAy = "0" + ssMonth;
        }

        switch (activeAy){
            case "01":
                strAy = "OCAK";
                break;
            case "02":
                strAy = "ŞUBAT";
                break;
            case "03":
                strAy = "MART";
                break;
            case "04":
                strAy = "NİSAN";
                break;
            case "05":
                strAy = "MAYIS";
                break;
            case "06":
                strAy = "HAZİRAN";
                break;
            case "07":
                strAy = "TEMMUZ";
                break;
            case "08":
                strAy = "AĞUSTOS";
                break;
            case "09":
                strAy = "EYLÜL";
                break;
            case "10":
                strAy = "EKİM";
                break;
            case "11":
                strAy = "KASIM";
                break;
            case "12":
                strAy = "ARALIK";
                break;
        }

        currentDate = activeGun +"-"+ activeAy +"-"+ activeYil;

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerVoid();
            }
        });

        if(isGunlukDurumActive){
            textView.setText(activeGun +" "+ strAy +" "+ activeYil + "\n-Tıkla Değiştir-");
            LoadDatas();
        }else{
            textView.setText(strAy +" "+ activeYil + "\n-Tıkla Değiştir-");
            LoadDataMonthly();
        }

    }

    void DatePickerVoid(){
        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // on below line we are creating a variable for date picker dialog.
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                // on below line we are passing context.
                ActivityDurumuGoruntule.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // on below line we are setting date to our text view.
                        activeAy = (monthOfYear + 1) + "";
                        activeYil = year + "";
                        activeGun = dayOfMonth + "";
                        oku2();
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    List<Integer> dersSoruSayiVerileri = new ArrayList<>();
    void LoadDatas(){
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        EditText text = findViewById(R.id.durum_GünNotEdit);
        String önce = prefs.getString(currentDate + "_tarihinde_not", "");
        text.setText(önce);

        EditText yapilacaklarYuzdeEt = findViewById(R.id.yapilacaklarYuzdeEt);
        int getYapilacaklarYuzdePrefs = prefs.getInt(currentDate + "_tarihinde_yuzde", 0);
        yapilacaklarYuzdeEt.setText(getYapilacaklarYuzdePrefs + "");

        dersSoruSayiVerileri.clear();
        String[] derslerPrefs={"mat","edb","fizik","kimya","biyo","tarih","ing","din","cografya","almanca","saglikbilgisi","diger1","diger2",
        "aytmat","aytedb","aytfizik","aytkimya","aytbiyo","tytmat","tytturkce","tytfizik","tytkimya","tytbiyo", "geo", "paragraf", "problem"};


        int GunCozulenSoru = prefs.getInt(currentDate + "_tarihinde_cozulen_soru", 0);
        int GunCalisilanSure = prefs.getInt(currentDate + "_tarihinde_calisilan_sure", 0);

        for (String name : derslerPrefs){
            dersSoruSayiVerileri.add(prefs.getInt(currentDate + "_tarihinde_"+ name +"_cozulen_soru", 0));
        }

        for(int i=0;i<edits.size();i++){
            edits.get(i).setText(dersSoruSayiVerileri.get(i) + "");
            edits.get(i).setEnabled(false);
        }

        int saat = GunCalisilanSure / 60;
        int dakika = GunCalisilanSure % 60;

        Log.d("TAGTAG", "LoadDatas: ");

        ///VERİLERİ DEĞERLERNDİR
        TextView toplamSure = findViewById(R.id.dersCalis_calisilan_sure);
        TextView toplamSoruSayi = findViewById(R.id.durum_toplamSoruSayi);

        toplamSoruSayi.setText("Çözülen Soru Sayısı : " + GunCozulenSoru);
        toplamSure.setText("Çalıştığın Süre : " + GunCalisilanSure +"dk\n("+saat + "sa " + dakika+"dk)");

        EditText dersCalis_sureEditText = findViewById(R.id.dersCalis_sureEditText);
        dersCalis_sureEditText.setText(GunCalisilanSure + "");

        ImageView durum_hedefGunlukSoru = findViewById(R.id.durum_hedefGunlukSoru);
        ImageView durum_hedefÇalışmaSüresi = findViewById(R.id.durum_hedefÇalışmaSüresi);

        TextView durum_hedefGunlukSoruText = findViewById(R.id.durum_hedefGunlukSoruText);
        TextView durum_hedefÇalışmaSüresiText = findViewById(R.id.durum_hedefÇalışmaSüresiText);

        if(GunCozulenSoru >= MainActivity.hedef_CozulenSoruSayisi){
            durum_hedefGunlukSoru.setBackgroundResource(R.drawable.check);
            durum_hedefGunlukSoruText.setText("GÜNLÜK SORU\nHEDEFİNE ULAŞILDI");
        }else{
            durum_hedefGunlukSoru.setBackgroundResource(R.drawable.yanlis);
            durum_hedefGunlukSoruText.setText("GÜNLÜK SORU\nHEDEFİNE ULAŞILMADI");
        }

        if(GunCalisilanSure >= MainActivity.hedef_CalismaSuresi){
            durum_hedefÇalışmaSüresi.setBackgroundResource(R.drawable.check);
            durum_hedefÇalışmaSüresiText.setText("GÜNLÜK SÜRE\nHEDEFİNE ULAŞILDI");
        }else{
            durum_hedefÇalışmaSüresi.setBackgroundResource(R.drawable.yanlis);
            durum_hedefÇalışmaSüresiText.setText("GÜNLÜK SÜRE\nHEDEFİNE ULAŞILMADI");
        }
    }

    List<Integer> MonthlyDersSoruSayiVerileri = new ArrayList<>();
    void LoadDataMonthly(){
        Log.d("TAGTAG", "LoadDataMonthly: ");
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        MonthlyDersSoruSayiVerileri.clear();

        String[] derslerPrefs={"mat","edb","fizik","kimya","biyo","tarih","ing","din","cografya","almanca","saglikbilgisi","diger1","diger2",
        "aytmat","aytedb","aytfizik","aytkimya","aytbiyo","tytmat","tytturkce","tytfizik","tytkimya","tytbiyo", "geo", "paragraf", "problem"};


        for (String name : derslerPrefs){
            int toplam = 0;
            for(int i = 1; i<=31; i++){
                String str = i + "";
                if(str.toString().length() == 1){
                    str = "0" + i;
                }
                String newcurrentDate = str +"-"+ activeAy +"-"+ activeYil;
                int x = prefs.getInt(newcurrentDate + "_tarihinde_"+ name +"_cozulen_soru", 0);
                toplam += x;
                if(i==31){
                    MonthlyDersSoruSayiVerileri.add(toplam);
                }
            }
        }

        for(int i=0;i<edits.size();i++){
            edits.get(i).setText(MonthlyDersSoruSayiVerileri.get(i) + "");
            edits.get(i).setEnabled(false);
        }

        int aylikSoru = 0;
        for(int i : MonthlyDersSoruSayiVerileri){
            aylikSoru += i;
        }

        int aylikSure = 0;
        for(int i=1; i<=31; i++){
            String str = i + "";
            if(str.toString().length() == 1){
                str = "0" + i;
            }
            String newcurrentDate = str +"-"+ activeAy +"-"+ activeYil;
            int sure = prefs.getInt(newcurrentDate + "_tarihinde_calisilan_sure", 0);
            aylikSure += sure;
        }

        int saat = aylikSure / 60;
        int dakika = aylikSure % 60;

        ///VERİLERİ DEĞERLERNDİR
        TextView toplamSure = findViewById(R.id.dersCalis_calisilan_sure);
        TextView toplamSoruSayi = findViewById(R.id.durum_toplamSoruSayi);

        toplamSoruSayi.setText("Çözülen Soru Sayısı : " + aylikSoru);
        toplamSure.setText("Çalıştığın Süre : " + aylikSure +"dk\n("+saat + "sa " + dakika+"dk)");

        EditText dersCalis_sureEditText = findViewById(R.id.dersCalis_sureEditText);
        dersCalis_sureEditText.setText(aylikSure + "");

        LoadYuzdeler();
        PieChartLoad();
    }

    private void LoadYuzdeler() {
        if(true){
            return;
        }

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);

        String strders = prefs.getString("calisilacak_ders_isimleri", "");
        String[] parts = strders.split(", ");

        LinearLayout lr = findViewById(R.id.Durum_aylikSoruSayiYuzdeleri);

        LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        int aylikSoru = 0;
        for(int i : MonthlyDersSoruSayiVerileri){
            aylikSoru += i;
        }

        int i = 0;
        for (String asil : parts){
            TextView tx = new TextView(this);
            tx.setLayoutParams(Params);
            tx.setTextSize(30);
            tx.setPadding(10,10,10,10);
            tx.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tx.setTextColor(Color.BLACK);

            LinearLayout.LayoutParams ParamsRules = (LinearLayout.LayoutParams) tx.getLayoutParams();
            ParamsRules.leftMargin = 10;
            ParamsRules.rightMargin = 10;
            ParamsRules.topMargin = 10;
            ParamsRules.bottomMargin = 10;

            float yuzde = (Float.parseFloat(MonthlyDersSoruSayiVerileri.get(i) + "") / Float.parseFloat(aylikSoru + "")) * 100;
            tx.setText(asil + " : %" + Math.round(yuzde));
            i++;

            lr.addView(tx,ParamsRules);
        }
    }

    boolean isPieChartFullscreen = false;
    void PieChartLoad(){
        /*
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);

        String strders = prefs.getString("calisilacak_ders_isimleri", "");

        String[] parts = strders.split(", ");

        PieChart pieChart = findViewById(R.id.pieChart);
        PieData pieData;
        PieDataSet pieDataSet;
        ArrayList pieEntries = new ArrayList<>();

        int i = 0;
        for (String asil : parts){
            pieEntries.add(new PieEntry(MonthlyDersSoruSayiVerileri.get(i), asil));
            i++;
        }

        pieDataSet = new PieDataSet(pieEntries, "");
        pieData = new PieData(pieDataSet);
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(15f);
        pieDataSet.setSliceSpace(4f);

        pieChart.setData(pieData);
        pieChart.invalidate();*/

        PieChart pieChart;
        if(isPieChartFullscreen){
            pieChart = findViewById(R.id.pieChartTamEkran);
        }else{
            pieChart = findViewById(R.id.pieChart);
        }

        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(9);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Dersler");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);

        String strders = prefs.getString("calisilacak_ders_isimleri", "");

        String[] parts = strders.split(", ");


        ArrayList<PieEntry> entries = new ArrayList<>();
        /*entries.add(new PieEntry(0.2f, "Food & Dining"));
        entries.add(new PieEntry(0.15f, "Medical"));
        entries.add(new PieEntry(0.10f, "Entertainment"));
        entries.add(new PieEntry(0.25f, "Electricity and Gas"));
        entries.add(new PieEntry(0.3f, "Housing"));*/

        int i = 0;
        for (String asil : parts){
            if(!(MonthlyDersSoruSayiVerileri.get(i) == 0)){
                entries.add(new PieEntry(MonthlyDersSoruSayiVerileri.get(i), asil));
            }
            i++;
        }

        ArrayList<Integer> colors = new ArrayList<>();

        for (int color: ColorTemplate.COLORFUL_COLORS) {
            colors.add(color);
        }
        for (int color: ColorTemplate.JOYFUL_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }


    public void Geri(View view) {
        StaticCurrentDate = "";

        startActivity(new Intent(ActivityDurumuGoruntule.this, MainActivity.class));
        overridePendingTransition(R.anim.slidefromright,R.anim.slidetoleft);
    }
}