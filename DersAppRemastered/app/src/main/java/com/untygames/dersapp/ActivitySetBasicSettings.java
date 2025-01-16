package com.untygames.dersapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ActivitySetBasicSettings extends AppCompatActivity {

    public static boolean forDegistirme = false;
    TextView tvDay;
    boolean[] selectedDay;
    ArrayList<Integer> daylist = new ArrayList<>();
    String[] dayArray={
            "Matematik","Türk Dili ve Edebiyatı","Fizik","Kimya","Biyoloji","Tarih","İngilizce","Din Kültürü ve Ahlak Bilgisi","Coğrafya","Almanca","Sağlık Bilgisi","Diğer 1","Diğer 2",
            "AYT Matematik","AYT Edebiyat","AYT Fizik","AYT Kimya","AYT Biyoloji",
            "TYT Matematik","TYT Türkçe","TYT Fizik","TYT Kimya","TYT Biyoloji",
            "Geometri","Paragraf","Problem"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_basic_settings);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);

        boolean isGuideCompleted = prefs.getBoolean("isGuideCompleted",false);

        if(!isGuideCompleted){
            startActivity(new Intent(this, GuideActivity.class));
            finish();
        }else {
            tvDay=findViewById(R.id.dersSec);

            selectedDay=new boolean[dayArray.length];

            tvDay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySetBasicSettings.this);

                    builder.setTitle("Ders Seç");

                    builder.setCancelable(false);

                    builder.setMultiChoiceItems(dayArray, selectedDay, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            if(isChecked){
                                daylist.add(which);
                                Collections.sort(daylist);
                            }else{
                                for (int j=0;j<daylist.size();j++){
                                    if (daylist.get(j)==which){
                                        daylist.remove(j);
                                    }
                                }
                            }
                        }
                    });

                    builder.setPositiveButton("Tamam/Seç", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            StringBuilder stringBuilder = new StringBuilder();

                            for(int j = 0;j<daylist.size();j++){
                                stringBuilder.append(dayArray[daylist.get(j)]);

                                if(j!=daylist.size()-1){
                                    stringBuilder.append(", ");
                                }
                            }

                            tvDay.setText(stringBuilder.toString());
                        }
                    });

                    builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.setNeutralButton("Temizle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for(int j=0;j<selectedDay.length;j++){
                                selectedDay[j]=false;
                                daylist.clear();
                                tvDay.setText("");
                            }
                        }
                    });

                    builder.show();
                }
            });

            init();
        }

    }

    @Override
    public void onBackPressed() {
    }

    boolean hedefBool=true;
    void init(){
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);

        CheckBox giris_hedefCheckBox = findViewById(R.id.giris_hedefCheckBox);
        LinearLayout giris_hedefler = findViewById(R.id.giris_hedefler);
        giris_hedefCheckBox.setChecked(true);
        hedefBool=true;
        giris_hedefCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                hedefBool=b;
                if(b){
                    giris_hedefler.setVisibility(View.VISIBLE);
                }else{
                    giris_hedefler.setVisibility(View.GONE);
                }
            }
        });

        EditText calismaSaati = findViewById(R.id.sec_toplamCalismaSaati);
        EditText soruSayisi = findViewById(R.id.sec_toplamSoruSayisi);
        TextView dersler = findViewById(R.id.dersSec);

        calismaSaati.setFilters(new InputFilter[]{ new Nunber24Filter("1", "1440")});

        if(!forDegistirme){
            if(!(prefs.getBoolean("is_New", true))){
                /*
                OrtakAyarlar.DarkMode = prefs.getBoolean("checkBoxAyarlar_themeChangeAyar", false);

                if(OrtakAyarlar.DarkMode){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }*/

                startActivity(new Intent(this, MainActivity.class));
                finish();
            }

        }else{
            int gunluk_hedef_soru_sayisi = prefs.getInt("gunluk_hedef_soru_sayisi",0);
            int gunluk_hedef_calisma_sayisi = prefs.getInt("gunluk_hedef_calisma_sayisi",0);
            String calisilacak_ders_isimleri = prefs.getString("calisilacak_ders_isimleri","");

            String[] ss = calisilacak_ders_isimleri.split(", ");
            List<String> strings = Arrays.asList(dayArray);

            for(String s : ss){
                selectedDay[strings.indexOf(s)] = true;
                daylist.add(strings.indexOf(s));
                Collections.sort(daylist);
            }

            if(gunluk_hedef_soru_sayisi==0 && gunluk_hedef_calisma_sayisi == 0){
                giris_hedefCheckBox.setChecked(false);
                giris_hedefler.setVisibility(View.GONE);
            }else{
                soruSayisi.setText("" + gunluk_hedef_soru_sayisi);
                calismaSaati.setText("" + gunluk_hedef_calisma_sayisi);
                dersler.setText("" + calisilacak_ders_isimleri);
            }

            Button geriBtn = findViewById(R.id.basic_geriBtn);
            geriBtn.setVisibility(View.VISIBLE);

            geriBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ActivitySetBasicSettings.this, ActivitySettings.class));
                    overridePendingTransition(R.anim.slidefrombottom,R.anim.slidetoup);
                }
            });
        }
    }

    public void DevamEt(View view) {
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        EditText calismaSaati = findViewById(R.id.sec_toplamCalismaSaati);
        EditText soruSayisi = findViewById(R.id.sec_toplamSoruSayisi);
        TextView dersler = findViewById(R.id.dersSec);

        if(hedefBool){
            if (soruSayisi.getText().toString().equals("") || soruSayisi.getText().toString().equals("0") || soruSayisi.getText().toString().equals("00") || soruSayisi.getText().toString().equals("000")|| soruSayisi.getText().toString().equals("0000")){
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySetBasicSettings.this);

                builder.setTitle("HATA!");
                builder.setMessage("Lütfen Hedef Soru Sayısını Giriniz");

                builder.setNeutralButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                return;
            }

            if (calismaSaati.getText().toString().equals("")){
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySetBasicSettings.this);

                builder.setTitle("HATA!");
                builder.setMessage("Lütfen Hedef Çalışma Süresini Giriniz");

                builder.setNeutralButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                return;
            }
        }

        if(dersler.getText().toString().equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySetBasicSettings.this);

            builder.setTitle("HATA!");
            builder.setMessage("Lütfen Dersleri Seçin");

            builder.setNeutralButton("Tamam", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
            return;
        }

        int IntegerCalismaSaati = 0;
        int IntegerSoruSayisi = 0;
        if(hedefBool){
            IntegerCalismaSaati = Integer.parseInt(calismaSaati.getText().toString());
            IntegerSoruSayisi = Integer.parseInt(soruSayisi.getText().toString());
        }else{
            IntegerCalismaSaati=0;
            IntegerSoruSayisi=0;
        }

        editor.putInt("gunluk_hedef_soru_sayisi", IntegerSoruSayisi);
        editor.putInt("gunluk_hedef_calisma_sayisi", IntegerCalismaSaati);
        editor.putString("calisilacak_ders_isimleri", dersler.getText().toString());

        editor.commit();

        forDegistirme=false;
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.slidefromright,R.anim.slidetoleft);
    }}