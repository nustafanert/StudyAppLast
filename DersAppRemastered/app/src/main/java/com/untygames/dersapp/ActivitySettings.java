package com.untygames.dersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivitySettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();
    }

    void init(){
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        CheckBox checkBoxAyarlar_yapilacaklarListesi = findViewById(R.id.ayarlar_yapılacaklarListesiAyar);
        checkBoxAyarlar_yapilacaklarListesi.setChecked(prefs.getBoolean("checkBoxAyarlar_yapilacaklarListesi", true));

        checkBoxAyarlar_yapilacaklarListesi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean("checkBoxAyarlar_yapilacaklarListesi", b);
                editor.apply();
            }
        });

        CheckBox ayarlar_yuzdeGosterAyar = findViewById(R.id.ayarlar_yuzdeGosterAyar);
        ayarlar_yuzdeGosterAyar.setChecked(prefs.getBoolean("checkBoxAyarlar_yuzdeGoster", true));

        ayarlar_yuzdeGosterAyar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean("checkBoxAyarlar_yuzdeGoster", b);
                editor.apply();
            }
        });

        TextView version = findViewById(R.id.version);
        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            version.setText("Version : " + pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }

        RelativeLayout settings_progressBarRL = findViewById(R.id.settings_progressBarRL);
        CheckBox ayarlar_themeChangeAyar = findViewById(R.id.ayarlar_themeChangeAyar);
        ayarlar_themeChangeAyar.setChecked(prefs.getBoolean("checkBoxAyarlar_themeChangeAyar", false));

        ayarlar_themeChangeAyar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settings_progressBarRL.setVisibility(View.VISIBLE);
                editor.putBoolean("checkBoxAyarlar_themeChangeAyar", b);
                editor.apply();

                OrtakAyarlar.DarkMode = b;
                OrtakAyarlar.DarkMode = prefs.getBoolean("checkBoxAyarlar_themeChangeAyar", false);

                if(OrtakAyarlar.DarkMode){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                settings_progressBarRL.setVisibility(View.VISIBLE);

                startActivity(new Intent(ActivitySettings.this, ActivitySetBasicSettings.class));
                finish();

                /*
                if(OrtakAyarlar.DarkMode){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    startActivity(new Intent(ActivitySettings.this, ActivitySettings.class));
                    finish();
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    startActivity(new Intent(ActivitySettings.this, ActivitySettings.class));
                    finish();

                }*/

            }
        });


        Button ayarlar_geriBildirimBtn = findViewById(R.id.ayarlar_geriBildirimBtn);
        Button geriBildirimGeriBtn = findViewById(R.id.geriBildirimGeriBtn);
        EditText geriBildirimEditText = findViewById(R.id.geriBildirimEditText);
        ScrollView geriBildirimGonderView = findViewById(R.id.geriBildirimGonderView);
        ayarlar_geriBildirimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geriBildirimGonderView.setVisibility(View.VISIBLE);
                geriBildirimEditText.setText("");
                geriBildirimGonderKontrol();
            }
        });
        geriBildirimGeriBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geriBildirimGonderView.setVisibility(View.GONE);
            }
        });

        Button btnremoveads = findViewById(R.id.ayarlar_btnremoveads);

        btnremoveads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivitySettings.this, PaymentActivity.class));
                overridePendingTransition(R.anim.slidefromright,R.anim.slidetoleft);
            }
        });



        if(prefs.getBoolean("reklam_kaldir",false)) {
            TextView premiumText = findViewById(R.id.premiumText);
            premiumText.setVisibility(View.VISIBLE);
        }


    }

    int top_reklamsiz_sayi = 0;
    int top_geriBildirimSay = 0;
    void geriBildirimGonderKontrol(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference top_sayiRef = database.getReference("Reklam_Kaldiran_Kisiler" + "/top_sayi");
        // Read from the database
        top_sayiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer value = dataSnapshot.getValue(Integer.class);

                try {
                    top_reklamsiz_sayi = value;
                }catch (Exception e){
                    top_reklamsiz_sayi = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
            }
        });


        DatabaseReference top_geriBildirimSayRef = database.getReference("Geri_Bildirim" + "/top_geriBildirimSay");
        top_geriBildirimSayRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer value = dataSnapshot.getValue(Integer.class);

                try {
                    top_geriBildirimSay = value;
                }catch (Exception e){
                    top_geriBildirimSay = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
            }
        });

        ScrollView geriBildirimGonderView = findViewById(R.id.geriBildirimGonderView);
        EditText geriBildirimEditText = findViewById(R.id.geriBildirimEditText);
        EditText geriBildirimMailEditText = findViewById(R.id.geriBildirimMailEditText);
        EditText geriBildiriminstagramEditText = findViewById(R.id.geriBildiriminstagramEditText);
        Button geriBildirimGonderBtn = findViewById(R.id.geriBildirimGonderBtn);

        geriBildirimGonderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String geriBildirimStringText = geriBildirimEditText.getText().toString();
                String geriBildirimMailStringText = geriBildirimMailEditText.getText().toString();
                String geriBildirimInstagramStringText = geriBildiriminstagramEditText.getText().toString();

                if(geriBildirimStringText.equals("")){
                    CreateBuilderAlertDialog("Hata", "Geri bildirim kısmı boş bırakılamaz",false);
                    return;
                }
                else if(geriBildirimMailStringText.equals("")){
                    CreateBuilderAlertDialog("Hata", "E-mail kısmı kısmı boş bırakılamaz",false);
                    return;
                }else if(!Patterns.EMAIL_ADDRESS.matcher(geriBildirimMailStringText).matches()){
                    CreateBuilderAlertDialog("Hata", "Lütfen geçerli bir e- mail adresi giriniz.",false);
                    return;
                }

                if(geriBildirimStringText.equals("reklam_kaldir_from_app-dev_by_nert")){

                    AlertDialog.Builder isimbuilder = new AlertDialog.Builder(ActivitySettings.this);

                    isimbuilder.setTitle("Reklam Kaldır");
                    isimbuilder.setMessage("Reklamları kaldırmadan önce isminizi girin lütfen.");
                    isimbuilder.setCancelable(false);

                    EditText edittext = new EditText(ActivitySettings.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    edittext.setLayoutParams(lp);
                    isimbuilder.setView(edittext);
                    edittext.setHint("Buraya giriniz...");

                    isimbuilder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String currentDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss ", Locale.getDefault()).format(new Date());
                            String versionName = "";
                            try {
                                PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
                                versionName = pInfo.versionName;
                            } catch (PackageManager.NameNotFoundException e) {
                                throw new RuntimeException(e);
                            }

                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                            DatabaseReference kisiRef = database.getReference("Reklam_Kaldiran_Kisiler" + "/kisi_" + top_reklamsiz_sayi);
                            kisiRef.setValue("İsim : " + edittext.getText().toString() + "\n // Tarih: " + currentDate + "\n // Version Name: " + versionName);

                            DatabaseReference kisiSayisiRef = database.getReference("Reklam_Kaldiran_Kisiler" + "/top_sayi");
                            kisiSayisiRef.setValue((top_reklamsiz_sayi+1));

                            ReklamKaldırma(edittext.getText().toString());
                        }
                    });
                    isimbuilder.show();
                }
                else if (geriBildirimStringText.equals("yenile")){
                    MainActivity.isDuzenle = true;
                    geriBildirimGonderView.setVisibility(View.GONE);
                    CreateBuilderAlertDialog("Yenilendi!", "Tüm zamanlar süresi tekrar sayıldı!",true);
                }
                else{
                    String currentDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss ", Locale.getDefault()).format(new Date());
                    String versionName = "";
                    try {
                        PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
                        versionName = pInfo.versionName;
                    } catch (PackageManager.NameNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference geriBildirimRef = database.getReference("Geri_Bildirim/geribildirim_" + top_geriBildirimSay);
                    geriBildirimRef.setValue("Geri Bildirim : " + geriBildirimStringText + "//// E-mail : " + geriBildirimMailStringText + "//// phoneORinsta : " + geriBildirimInstagramStringText + "////  Tarih: " + currentDate + "////  Version Name: " + versionName);
                    DatabaseReference kisiSayisiRef = database.getReference("Geri_Bildirim" + "/top_geriBildirimSay");
                    kisiSayisiRef.setValue((top_geriBildirimSay+1)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            CreateBuilderAlertDialog("Başarılı", "Geri bildiriminiz başarıyla gönderildi! En kısa zamanda ilgileneceğiz.", false);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            CreateBuilderAlertDialog("Başarısız", "Geri bildiriminiz gönderilemedi! Bağlantınızı kontrol edin veya daha sonra tekrar deneyin..", false);
                        }
                    });

                    geriBildirimGonderView.setVisibility(View.GONE);

                }
            }
        });
    }

    private void ReklamKaldırma(String name) {
        ScrollView geriBildirimGonderView = findViewById(R.id.geriBildirimGonderView);

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySettings.this);

        builder.setTitle("Özel Kod Demek!");
        builder.setMessage("Bu kodu aldığına göre gerçekten özel birisin " + name + "!" + "\n Artık reklamlarla uğraşmana gerek yok hepsini senin için kapadım!\n Premium DersApp'e Hoşgeldin!");
        builder.setCancelable(false);

        builder.setPositiveButton("Teşekkürler!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editor.putBoolean("reklam_kaldir", true);
                editor.apply();
                geriBildirimGonderView.setVisibility(View.GONE);
                TextView premiumText = findViewById(R.id.premiumText);
                premiumText.setVisibility(View.VISIBLE);
            }
        });
        builder.show();
    }

    private void CreateBuilderAlertDialog(String title, String message, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySettings.this);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(cancelable);

        builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    public void HedefEkranAc(View view) {
        ActivitySetBasicSettings.forDegistirme = true;
        startActivity(new Intent(this, ActivitySetBasicSettings.class));
        overridePendingTransition(R.anim.slidefrombottom,R.anim.slidetoup);
    }

    public void AyarlarKapat(View view) {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.slidefromright,R.anim.slidetoleft);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.slidefromright,R.anim.slidetoleft);
    }
}