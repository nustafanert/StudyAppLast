package com.untygames.dersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GrafikActivity extends AppCompatActivity {

    String activeDate="";
    Integer activeChart=1;
    Integer activeChartSureVeSoru=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafik);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.theme6color6));
        window.setNavigationBarColor(getColor(R.color.theme6color6));

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        String ilkGirisTarihi = prefs.getString("ilkGirisTarihi", "01-01-2023");

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        List<String> s = Arrays.asList(currentDate.split("-"));

        activeDate = currentDate;

        activeChart=1;

        GraphChangeBtnView();
        SoruChartYukle();
    }



    void SoruChartYukle(){
        LineData lineData;
        LineDataSet lineDataSet;

        TextView textView = findViewById(R.id.activeAyView);
        List<String> s = Arrays.asList(activeDate.split("-"));

        int aydakiGunSayisi = 31;
        switch (s.get(1)){
            case "01":
                textView.setText("OCAK " + s.get(2));
                aydakiGunSayisi = 31;
                break;
            case "02":
                textView.setText("ŞUBAT " + s.get(2));
                aydakiGunSayisi = 29;
                break;
            case "03":
                textView.setText("MART " + s.get(2));
                aydakiGunSayisi = 31;
                break;
            case "04":
                textView.setText("NİSAN " + s.get(2));
                aydakiGunSayisi = 30;
                break;
            case "05":
                textView.setText("MAYIS " + s.get(2));
                aydakiGunSayisi = 31;
                break;
            case "06":
                textView.setText("HAZİRAN " + s.get(2));
                aydakiGunSayisi = 30;
                break;
            case "07":
                textView.setText("TEMMUZ " + s.get(2));
                aydakiGunSayisi = 31;
                break;
            case "08":
                textView.setText("AĞUSTOS " + s.get(2));
                aydakiGunSayisi = 31;
                break;
            case "09":
                textView.setText("EYLÜL " + s.get(2));
                aydakiGunSayisi = 30;
                break;
            case "10":
                textView.setText("EKİM " + s.get(2));
                aydakiGunSayisi = 31;
                break;
            case "11":
                textView.setText("KASIM " + s.get(2));
                aydakiGunSayisi = 30;
                break;
            case "12":
                textView.setText("ARALIK " + s.get(2));
                aydakiGunSayisi = 31;
                break;
        }

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);

        BarChart barChart = findViewById(R.id.barChart);
        LineChart lineChart = findViewById(R.id.lineChart);

        barChart.zoomToCenter(0,0);
        lineChart.zoomToCenter(0,0);

        ArrayList<BarEntry> visitors= new ArrayList<>();//Sütun
        ArrayList<Entry> lineEntries = new ArrayList<>();//Çizgi

        for(int i =1;i<=aydakiGunSayisi;i++){
            String ss = (i+"");
            if(ss.length() == 1){
                ss = "0" + i;
            }
            String date = ss + "-" + s.get(1) + "-"+ s.get(2);
            int cozulenSoru = prefs.getInt(date + "_tarihinde_cozulen_soru", 0);

            visitors.add(new BarEntry(i, cozulenSoru));//Sütun
            lineEntries.add(new Entry(i, cozulenSoru));//Çizgi
        }

        if(activeChart==1){
            ////SÜTUN
            barChart.setVisibility(View.VISIBLE);
            lineChart.setVisibility(View.GONE);
            BarDataSet barDataSet = new BarDataSet(visitors,"Soru Sayıları");
            barDataSet.setColors(getColor(R.color.theme6color2));
            barDataSet.setValueTextColor(getColor(R.color.white2));
            barDataSet.setValueTextSize(10f);
            barDataSet.setValueFormatter(new DefaultValueFormatter(0));

            BarData barData= new BarData(barDataSet);
            barChart.setData(barData);
            barChart.setFitBars(true);
            barChart.animateY(1000);
            barChart.getDescription().setText("");
            ////SÜTUN
        }else if(activeChart==2){
            ////ÇİZGİ
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            lineDataSet = new LineDataSet(lineEntries, "Soru");
            lineData = new LineData(lineDataSet);
            lineChart.setData(lineData);
            lineDataSet.setColors(getColor(R.color.theme6color2));
            lineDataSet.setValueTextColor(getColor(R.color.white2));
            lineDataSet.setValueTextSize(10f);

            lineDataSet.setValueFormatter(new DefaultValueFormatter(0));

            lineChart.animateY(1000);

            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillColor(getColor(R.color.theme6color7));

            ////ÇİZGİ
        }
    }

    void SureChartYukle(){
        LineData lineData;
        LineDataSet lineDataSet;

        TextView textView = findViewById(R.id.activeAyView);
        List<String> s = Arrays.asList(activeDate.split("-"));

        switch (s.get(1)){
            case "01":
                textView.setText("OCAK " + s.get(2));
                break;
            case "02":
                textView.setText("ŞUBAT " + s.get(2));
                break;
            case "03":
                textView.setText("MART " + s.get(2));
                break;
            case "04":
                textView.setText("NİSAN " + s.get(2));
                break;
            case "05":
                textView.setText("MAYIS " + s.get(2));
                break;
            case "06":
                textView.setText("HAZİRAN " + s.get(2));
                break;
            case "07":
                textView.setText("TEMMUZ " + s.get(2));
                break;
            case "08":
                textView.setText("AĞUSTOS " + s.get(2));
                break;
            case "09":
                textView.setText("EYLÜL " + s.get(2));
                break;
            case "10":
                textView.setText("EKİM " + s.get(2));
                break;
            case "11":
                textView.setText("KASIM " + s.get(2));
                break;
            case "12":
                textView.setText("ARALIK " + s.get(2));
                break;
        }

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);

        BarChart barChart = findViewById(R.id.barChart);
        LineChart lineChart = findViewById(R.id.lineChart);

        barChart.zoomToCenter(0,0);
        lineChart.zoomToCenter(0,0);

        ArrayList<BarEntry> visitors= new ArrayList<>();//Sütun
        ArrayList<Entry> lineEntries = new ArrayList<>();//Çizgi

        for(int i =1;i<=30;i++){
            String ss = (i+"");
            if(ss.length() == 1){
                ss = "0" + i;
            }
            String date = ss + "-" + s.get(1) + "-"+ s.get(2);
            int calisilanSure = prefs.getInt(date + "_tarihinde_calisilan_sure", 0);
            visitors.add(new BarEntry(i, calisilanSure)); //Sütun
            lineEntries.add(new Entry(i, calisilanSure));//Çizgi
        }

        if(activeChart==1){
            ////SÜTUN
            barChart.setVisibility(View.VISIBLE);
            lineChart.setVisibility(View.GONE);
            BarDataSet barDataSet = new BarDataSet(visitors,"Süre");
            barDataSet.setColors(getColor(R.color.theme6color2));
            barDataSet.setValueTextColor(getColor(R.color.white2));
            barDataSet.setValueTextSize(10f);

            BarData barData= new BarData(barDataSet);
            barChart.setData(barData);
            barChart.setFitBars(true);
            barChart.animateY(1000);
            barChart.getDescription().setText("");

            barDataSet.setValueFormatter(new DefaultValueFormatter(0));
            ////SÜTUN
        }else if(activeChart==2){
            ////ÇİZGİ
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            lineDataSet = new LineDataSet(lineEntries, "Süre");
            lineData = new LineData(lineDataSet);
            lineChart.setData(lineData);
            lineDataSet.setColors(getColor(R.color.theme6color2));
            lineDataSet.setValueTextColor(getColor(R.color.white2));
            lineDataSet.setValueTextSize(10f);

            lineDataSet.setValueFormatter(new DefaultValueFormatter(0));

            lineChart.animateY(1000);

            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillColor(getColor(R.color.theme6color7));
            ////ÇİZGİ
        }
    }
    public void OncekiBtnView(View view) {
        List<String> s = Arrays.asList(activeDate.split("-"));
        int ay = Integer.parseInt(s.get(1));
        int yil = Integer.parseInt(s.get(2));
        if(ay>1){
            int a = (ay - 1);
            String ss = a + "";
            if(ss.length() == 1){
                ss = "0" + a;
            }
            s.set(1, ss);
        }else if(ay == 1){
            int yilint= yil-1;
            s.set(1, "12");
            s.set(2, yilint + "");
        }

        activeDate=  "01-" + s.get(1) + "-"+ s.get(2);
        ChartSec();
    }

    public void SonrakiBtnView(View view) {
        List<String> s = Arrays.asList(activeDate.split("-"));
        int ay = Integer.parseInt(s.get(1));
        int yil = Integer.parseInt(s.get(2));

        if(ay<12){
            int a = (ay + 1);
            String ss = a+"";
            if(ss.length() == 1){
                ss = "0" + a;
            }
            s.set(1, ss);
        }else if(ay == 12){
            int yilint= yil+1;
            s.set(1, "01");
            s.set(2, yilint + "");
        }

        activeDate=  "01-" + s.get(1) + "-"+ s.get(2);
        ChartSec();
    }

    public void MenuyeDonBtnView(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GrafikActivity.this);

        builder.setTitle("Menüye Dön?");
        builder.setMessage("Menüye dönmek istediğinize emin misiniz?");

        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.reklam_aftergrafikbutton = true;
                startActivity(new Intent(GrafikActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.slidefromright,R.anim.slidetoleft);
            }
        });

        builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    void ChartSec(){
        if(activeChartSureVeSoru == 1){
            SoruChartYukle();
        }else if(activeChartSureVeSoru == 2){
            SureChartYukle();
        }else{
            ChartYukle(SureVeSoruDegilseActiveCode);
        }
    }

    int SureVeSoruDegilseActiveCode = 0;
    void ChartYukle(int code){
        SureVeSoruDegilseActiveCode=code;
        switch (code){
            case 0:
                DersSoruGetir("mat");
                break;
            case 1:
                DersSoruGetir("edb");
                break;
            case 2:
                DersSoruGetir("fizik");
                break;
            case 3:
                DersSoruGetir("kimya");
                break;
            case 4:
                DersSoruGetir("biyo");
                break;
            case 5:
                DersSoruGetir("tarih");
                break;
            case 6:
                DersSoruGetir("ing");
                break;
            case 7:
                DersSoruGetir("din");
                break;
            case 8:
                DersSoruGetir("cografya");
                break;
            case 9:
                DersSoruGetir("almanca");
                break;
            case 10:
                DersSoruGetir("saglikbilgisi");
                break;
            case 11:
                DersSoruGetir("diger1");
                break;
            case 12:
                DersSoruGetir("diger2");
                break;
            case 13:
                DersSoruGetir("aytmat");
                break;
            case 14:
                DersSoruGetir("aytedb");
                break;
            case 15:
                DersSoruGetir("aytfizik");
                break;
            case 16:
                DersSoruGetir("aytkimya");
                break;
            case 17:
                DersSoruGetir("aytbiyo");
                break;
            case 18:
                DersSoruGetir("tytmat");
                break;
            case 19:
                DersSoruGetir("tytturkce");
                break;
            case 20:
                DersSoruGetir("tytfizik");
                break;
            case 21:
                DersSoruGetir("tytkimya");
                break;
            case 22:
                DersSoruGetir("tytbiyo");
                break;
            case 23:
                DersSoruGetir("geo");
                break;
            case 24:
                DersSoruGetir("paragraf");
                break;
            case 25:
                DersSoruGetir("problem");
                break;
        }
    }

    void DersSoruGetir(String name){
        LineData lineData;
        LineDataSet lineDataSet;

        TextView textView = findViewById(R.id.activeAyView);
        List<String> s = Arrays.asList(activeDate.split("-"));

        switch (s.get(1)){
            case "01":
                textView.setText("OCAK " + s.get(2));
                break;
            case "02":
                textView.setText("ŞUBAT " + s.get(2));
                break;
            case "03":
                textView.setText("MART " + s.get(2));
                break;
            case "04":
                textView.setText("NİSAN " + s.get(2));
                break;
            case "05":
                textView.setText("MAYIS " + s.get(2));
                break;
            case "06":
                textView.setText("HAZİRAN " + s.get(2));
                break;
            case "07":
                textView.setText("TEMMUZ " + s.get(2));
                break;
            case "08":
                textView.setText("AĞUSTOS " + s.get(2));
                break;
            case "09":
                textView.setText("EYLÜL " + s.get(2));
                break;
            case "10":
                textView.setText("EKİM " + s.get(2));
                break;
            case "11":
                textView.setText("KASIM " + s.get(2));
                break;
            case "12":
                textView.setText("ARALIK " + s.get(2));
                break;
        }

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);

        BarChart barChart = findViewById(R.id.barChart);
        LineChart lineChart = findViewById(R.id.lineChart);

        barChart.zoomToCenter(0,0);
        lineChart.zoomToCenter(0,0);

        ArrayList<BarEntry> visitors= new ArrayList<>();//Sütun
        ArrayList<Entry> lineEntries = new ArrayList<>();//Çizgi

        for(int i =1;i<=30;i++){
            String ss = (i+"");
            if(ss.length() == 1){
                ss = "0" + i;
            }
            String date = ss + "-" + s.get(1) + "-"+ s.get(2);

            int cozulenSoru = prefs.getInt(date + "_tarihinde_"+ name +"_cozulen_soru", 0);

            visitors.add(new BarEntry(i, cozulenSoru));//Sütun
            lineEntries.add(new Entry(i, cozulenSoru));//Çizgi
        }

        if(activeChart==1){
            ////SÜTUN
            barChart.setVisibility(View.VISIBLE);
            lineChart.setVisibility(View.GONE);
            BarDataSet barDataSet = new BarDataSet(visitors,name);
            barDataSet.setColors(getColor(R.color.theme6color2));
            barDataSet.setValueTextColor(getColor(R.color.white2));
            barDataSet.setValueTextSize(10f);
            barDataSet.setValueFormatter(new DefaultValueFormatter(0));

            BarData barData= new BarData(barDataSet);
            barChart.setData(barData);
            barChart.setFitBars(true);
            barChart.animateY(1000);
            barChart.getDescription().setText("");
            ////SÜTUN
        }else if(activeChart==2){
            ////ÇİZGİ
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            lineDataSet = new LineDataSet(lineEntries, name);
            lineData = new LineData(lineDataSet);
            lineChart.setData(lineData);
            lineDataSet.setColors(getColor(R.color.theme6color2));
            lineDataSet.setValueTextColor(getColor(R.color.white2));
            lineDataSet.setValueTextSize(10f);
            lineDataSet.setValueFormatter(new DefaultValueFormatter(0));

            lineChart.animateY(1000);

            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillColor(getColor(R.color.theme6color7));
            ////ÇİZGİ
        }
    }

    public void GraphChangeBtnView() {
        RadioGroup radioGroup = findViewById(R.id.graphRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // on below line we are getting radio button from our group.
                RadioButton radioButton = findViewById(checkedId);

                // on below line we are displaying a toast message.
                if(radioButton.getText().equals("Sütun")){
                    activeChart=1;
                }
                if(radioButton.getText().equals("Çizgi")){
                    activeChart=2;
                }

                ChartSec();
            }
        });

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);

        String strders = prefs.getString("calisilacak_ders_isimleri", "");
        String[] parts = strders.split(", ");

        List<String> partsList = Arrays.asList(parts);

        String[] pa = new String[ (parts.length+2) ];
        pa[1]="Süre";
        pa[0]="Soru Sayısı";

        for(int i=2; i < (parts.length + 2); i++){
            pa[i] = parts[i-2];
        }

        ArrayAdapter<String> adapter =  new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pa);

        Spinner spinner = (Spinner)findViewById(R.id.grafik_spinner);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    activeChartSureVeSoru = 1;
                    ChartSec();
                }else if(i==1){
                    activeChartSureVeSoru = 2;
                    ChartSec();
                }else{
                    activeChartSureVeSoru = 3;
                    int x = 0;
                    /*
        "Matematik","Türk Dili ve Edebiyatı","Fizik","Kimya","Biyoloji","Tarih","İngilizce","Din Kültürü ve Ahlak Bilgisi","Coğrafya","Almanca","Sağlık Bilgisi","Diğer 1","Diğer 2",
                "AYT Matematik","AYT Edebiyat","AYT Fizik","AYT Kimya","AYT Biyoloji",
                "TYT Matematik","TYT Türkçe","TYT Fizik","TYT Kimya","TYT Biyoloji"*/
                    switch (partsList.get(i-2)){
                        case "Matematik":
                            x = 0;
                            break;
                        case "Türk Dili ve Edebiyatı":
                            x = 1;
                            break;
                        case "Fizik":
                            x = 2;
                            break;
                        case "Kimya":
                            x = 3;
                            break;
                        case "Biyoloji":
                            x = 4;
                            break;
                        case "Tarih":
                            x = 5;
                            break;
                        case "İngilizce":
                            x = 6;
                            break;
                        case "Din Kültürü ve Ahlak Bilgisi":
                            x = 7;
                            break;
                        case "Coğrafya":
                            x = 8;
                            break;
                        case "Almanca":
                            x = 9;
                            break;
                        case "Sağlık Bilgisi":
                            x = 10;
                            break;
                        case "Diğer 1":
                            x = 11;
                            break;
                        case "Diğer 2":
                            x = 12;
                            break;
                        case "AYT Matematik":
                            x = 13;
                            break;
                        case "AYT Edebiyat":
                            x = 14;
                            break;
                        case "AYT Fizik":
                            x = 15;
                            break;
                        case "AYT Kimya":
                            x = 16;
                            break;
                        case "AYT Biyoloji":
                            x = 17;
                            break;

                        case "TYT Matematik":
                            x = 18;
                            break;
                        case "TYT Türkçe":
                            x = 19;
                            break;
                        case "TYT Fizik":
                            x = 20;
                            break;
                        case "TYT Kimya":
                            x = 21;
                            break;
                        case "TYT Biyoloji":
                            x = 22;
                            break;

                        case "Geometri":
                            x = 23;
                            break;
                        case "Paragraf":
                            x = 24;
                            break;
                        case "Problem":
                            x = 25;
                            break;
                        case "Tüm Zamanlar":
                            x = 26;
                            break;
                    }
                    //kod 0 vya 1 değilse kodlardan 2 eksiltin göndersin çünkü veriler 0dan baslıyo
                    ChartYukle(x);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Bir seçenek kaldırılırsa ne yapmalı
                // veya başka bir şey
                activeChartSureVeSoru=1;
                ChartSec();
            }
        });

    }

    @Override
    public void onBackPressed() {
        MenuyeDonBtnView(findViewById(R.id.barChart));
    }

}