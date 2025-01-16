package com.untygames.dersapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {

    int activeGuideNumber = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        String s0_selam = "Selam! \n Uygulama rehberine hoşgeldin.";
        String s1_setbasic = "Uygulamaya giriş ekranında çalışacağınız dersleri seçmeniz gerekmektedir.(Sonradan 'Ayarlar' bölümünden değiştirebilirsiniz.) İsterseniz günlük hedeflerinizi girebilirsiniz. Girmek istemiyorsanız 'Hedefler' kutucuğundaki işareti kaldırabilirsiniz.";
        String s2_main = "Ana Menüden istediğin bölüme kolayca ulaşabilirsin.\nGitmeyi istediğin ekrana göre butona tıklaman yeterli.";
        String s3_grafik = "Ana Menüden 'Veri Grafiği' butonuna tıklayarak uygulamaya girdiğin verilere göre oluşturulmuş sütun ve çizgi grafiklerine bakabilirsin.";
        String s4_yapilacaklarlistesi = "Ana Menüden 'Yapılacaklar Listesi' butonuna tıklayarak kendi yapılacaklar listeni oluşturabilirsin.";
        String s5_durumugoruntule1 = "Ana Menüden 'Durumu Görüntüle' butonuna tıklayarak 'Ders Çalış' bölümüne girdiğiniz verilere bakabilirsiniz.";
        String s5_durumugoruntule2 = "Ayrıca 'Durumu Görüntüle' bölümündeki 'Düzenle' butonuna tıklayarak süreyi ve soru sayılarını güncelleyebilirsiniz.";
        String s6_derscalis = "Ana Menüden 'Ders Çalış' butonuna tıklayarak kronometre veya sayacı kullanabilirsiniz. İkisi arasında geçis yapmak için ortalarındaki butona tıklamanız yeterli.";
        String s7_ayarlar = "Ana Menüden 'Ayarlar' butonuna tıklayarak uygulamayı özelleştirebilirsiniz. Karanlık Mod'u kullanmak için kutucuğu aktif etmeniz yeterli.";
        //String s1_selam = "Selam! \n Uygulama rehberine hoşgeldin.";

        Button btnIleri = findViewById(R.id.guideapplayout_ileriBtn);
        btnIleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePaperBtn(true);
            }
        });
        Button btnGeri = findViewById(R.id.guideapplayout_geriBtn);
        btnGeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePaperBtn(false);
            }
        });
    }

    void changePaperBtn(boolean isIleri){
        Button btnIleri = findViewById(R.id.guideapplayout_ileriBtn);
        Button btnGeri = findViewById(R.id.guideapplayout_geriBtn);


        if (isIleri){

            if(activeGuideNumber == 9){
                GuideKapat();
            }
            if(activeGuideNumber < 9){
                activeGuideNumber++;
            }
        }else{
            if(activeGuideNumber > 0){
                activeGuideNumber--;
            }
        }

        if(activeGuideNumber == 0){
            btnGeri.setVisibility(View.INVISIBLE);
        }else {
            btnGeri.setVisibility(View.VISIBLE);
        }

        if(activeGuideNumber == 9){
            btnIleri.setText("Bitir");
        } else{
            btnIleri.setText("İleri");
        }



        ImageView image = findViewById(R.id.guideapplayout_image);
        TextView title = findViewById(R.id.guideapplayout_title);
        TextView text = findViewById(R.id.guideapplayout_text);
        TextView neredeText = findViewById(R.id.guideapplayout_neredetext);

        if(activeGuideNumber == 3){
            image.setRotation(270);
        }else{
            image.setRotation(0);
        }

        List<Integer> drawableImages = new ArrayList<>();
        drawableImages.add(R.drawable.guide_hosgeldin);
        drawableImages.add(R.drawable.guide_setappbasic);
        drawableImages.add(R.drawable.guide_mainscreen);
        drawableImages.add(R.drawable.guide_grafiksutun);
        drawableImages.add(R.drawable.guide_yapilacaklar);
        drawableImages.add(R.drawable.guide_derscalis);
        drawableImages.add(R.drawable.guide_durumugoruntule1);
        drawableImages.add(R.drawable.guide_durumugoruntule2);
        drawableImages.add(R.drawable.guide_ayarlarekran);
        drawableImages.add(R.drawable.guide_kolaygelsin);

        String[] guide_titles = {"Hoşgeldin!", "Temelleri Ayarla", "Ana Menü", "Veri Grafiği","Yapılacaklar Listesi", "Ders Çalış","Durumu Görüntüle","Durumu Görüntüle Düzenle", "Ayarlar", "Kollay gelsinn :)"};
        String[] guide_texts = {"Selam! \n Uygulama rehberine hoşgeldiniz.",
                "Uygulamaya giriş ekranında çalışacağınız dersleri seçmeniz gerekmektedir.(Sonradan 'Ayarlar' bölümünden değiştirebilirsiniz.)\nİsterseniz günlük hedeflerinizi girebilirsiniz. Girmek istemiyorsanız 'Hedefler' kutucuğundaki işareti kaldırabilirsiniz.",
                "Ana Menüden istediğiniz bölüme kolayca ulaşabilirsiniz.\nGitmeyi istediğiniz bölüme göre butona tıklamanız yeterli.",
                "Ana Menüden 'Veri Grafiği' butonuna tıklayarak uygulamaya girdiğiniz verilere göre oluşturulmuş sütun ve çizgi grafiklerine bakabilirsiniz.",
                "Ana Menüden 'Yapılacaklar Listesi' butonuna tıklayarak kendi yapılacaklar listenizi oluşturabilirsiniz.",
                "Ana Menüden 'Ders Çalış' butonuna tıklayarak kronometre veya sayacı kullanabilirsiniz. İkisi arasında geçiş yapmak için aralarındaki butona tıklamanız yeterli.",
                "Ana Menüden 'Durumu Görüntüle' butonuna tıklayarak 'Ders Çalış' bölümüne girdiğiniz verilere bakabilirsiniz.",
                "Ayrıca 'Durumu Görüntüle' bölümündeki 'Düzenle' butonuna tıklayarak süreyi ve soru sayılarını güncelleyebilirsiniz.",
                "Ana Menüden 'Ayarlar' butonuna tıklayarak uygulamayı özelleştirebilirsiniz. Karanlık Mod'u ayarlar kısmından etkinleştirebilirsiniz.\nUygulamaya eklenmesini istediğiniz veya talep ettiğiniz özellikleri 'Geri Bildirim Gönder' kısmından gönderebilirsiniz.",
                "İyi Çalışmalar! Dersle kalın."
        };

        image.setImageDrawable(getResources().getDrawable(drawableImages.get(activeGuideNumber)));
        title.setText(guide_titles[activeGuideNumber]);
        text.setText(guide_texts[activeGuideNumber]);
        neredeText.setText((activeGuideNumber+1) + "/" + drawableImages.stream().count());
    }

    void GuideKapat() {
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean("isGuideCompleted",true);
        editor.apply();

        startActivity(new Intent(this, ActivitySetBasicSettings.class));
        overridePendingTransition(R.anim.slidefromright,R.anim.slidetoleft);
        finish();
    }
}