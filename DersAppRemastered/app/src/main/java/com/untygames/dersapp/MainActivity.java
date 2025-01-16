package com.untygames.dersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.google.android.gms.tasks.*;

public class MainActivity extends AppCompatActivity {

    public static int hedef_CalismaSuresi;
    public static int hedef_CozulenSoruSayisi;

    public static boolean reklam_afterderscalisIsShow = false;

    public static boolean reklam_aftergrafikbutton = false;

    public static boolean isDuzenle = false;
    private static InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        OrtakAyarlar.DarkMode = prefs.getBoolean("checkBoxAyarlar_themeChangeAyar", false);


        if(OrtakAyarlar.DarkMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        //showOpenAd();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        if(!(prefs.getBoolean("reklam_kaldir",false))){
            loadAndCheckGecisAd();
        }else{
            Log.d("TAGTAG", "premium aktif");
        }
        if(isDuzenle){
            DuzenlemelerYapılıyor();
            isDuzenle=false;
        }

        hedef_CozulenSoruSayisi = prefs.getInt("gunluk_hedef_soru_sayisi", 0);
        hedef_CalismaSuresi = prefs.getInt("gunluk_hedef_calisma_sayisi", 0);

        editor.putBoolean("is_New",false);

        String oldDate = prefs.getString("date","");
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());


        OrtakAyarlar.currentDate = currentDate;

        TextView text_main_tarih = findViewById(R.id.main_tarih);
        text_main_tarih.setText(currentDate);

        if(oldDate.equals("")){
            editor.putString("ilkGirisTarihi",currentDate);
        }
        if(!(oldDate.equals(currentDate))){
            editor.putString("date",currentDate);
            Sıfırla();
        }

        /////////////////
        GrafikReklamGoster();

        int dateDifference = (int) getDateDiff(new SimpleDateFormat("dd-MM-yyyy"), oldDate, currentDate);
        System.out.println("dateDifference: " + dateDifference);

        if(dateDifference >= 3 && prefs.getBoolean("is_anamenu_geribildirim_firstime", true)){
            geriBildirimGonderKontrolBuilder();
        }
        /////////////////

        LinearLayout main_hedefler = findViewById(R.id.main_hedefler);
        if(prefs.getInt("gunluk_hedef_soru_sayisi",0) == 0 && prefs.getInt("gunluk_hedef_calisma_sayisi",0) == 0){
            main_hedefler.setVisibility(View.GONE);
        }

        TextView main_text_hedefSure = findViewById(R.id.main_text_hedefSure);
        TextView main_text_hedefSoru = findViewById(R.id.main_text_hedefSoru);
        main_text_hedefSoru.setText(hedef_CozulenSoruSayisi + " Soru");
        main_text_hedefSure.setText(hedef_CalismaSuresi + "dk");

        TextView text_bugunCalisilanSure = findViewById(R.id.main_text_bugunCalisilanSure);
        TextView text_bugunCozulenSoru = findViewById(R.id.main_text_bugunCozulenSoru);

        int bugunCalisilanSure = prefs.getInt(currentDate + "_tarihinde_calisilan_sure", 0);
        int bugunCozulenSoru = prefs.getInt(currentDate + "_tarihinde_cozulen_soru", 0);

        text_bugunCalisilanSure.setText(bugunCalisilanSure + "dk");
        text_bugunCozulenSoru.setText(bugunCozulenSoru + " Soru");


        TextView text_tümCalisilanSure = findViewById(R.id.main_text_tümZamanCalisilanSure);
        TextView text_tümCozulenSoru = findViewById(R.id.main_text_tümZamanCozulenSoru);
        int tümZamanCozulenSoru = prefs.getInt("simdiye_kadar_cozulen_soru", 0);
        int tümZamanCalisilanSure = prefs.getInt("simdiye_kadar_calisilan_sure", 0);
        text_tümCalisilanSure.setText(tümZamanCalisilanSure + "dk");
        text_tümCozulenSoru.setText(tümZamanCozulenSoru + " Soru");

        editor.commit();

        Kontrol();
        UpdateApp();
    }

    private void DuzenlemelerYapılıyor() {
        Log.d("TAGTAG", "DuzenlemelerYapılıyor...");
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        int topSoru = 0;
        for(int a=1; a<=12;a++){
            String ay = a + "";
            if(ay.toString().length() == 1){
                ay = "0" + a;
            }

            for(int i=1; i<=31; i++){
                String gun = i + "";
                if(gun.toString().length() == 1){
                    gun = "0" + i;
                }
                String newcurrentDate = gun +"-"+ ay +"-"+ "2023";
                int sure = prefs.getInt(newcurrentDate + "_tarihinde_calisilan_sure", 0);
                topSoru += sure;
            }
        }
        editor.putInt("simdiye_kadar_calisilan_sure", topSoru);
        editor.apply();
        TextView main_text_tümZamanCalisilanSure = findViewById(R.id.main_text_tümZamanCalisilanSure);
        main_text_tümZamanCalisilanSure.setText(topSoru + "dk");
    }

    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void loadAndCheckGecisAd(){
        AdRequest adRequest = new AdRequest.Builder().build();

        if(mInterstitialAd != null){
            Log.i("TAGTAG", "onAdReady");
            if (reklam_afterderscalisIsShow){
                reklam_afterderscalisIsShow = false;
                Log.i("TAGTAG", "onAdShowed");

                mInterstitialAd.show(MainActivity.this);
                mInterstitialAd = null;
            }
        }else{
            Log.i("TAGTAG", "onAdShow not, loading...");
            InterstitialAd.load(this,"ca-app-pub-8446349352597923/5812546254", adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            mInterstitialAd = interstitialAd;
                            Log.i("TAGTAG", "onAdLoaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            //Log.d("TAGTAG", loadAdError.toString() + "");
                            //Log.d("TAGTAG", loadAdError.getCode() + "");
                            mInterstitialAd = null;
                        }
                    });


        }

    }

    private void Kontrol() {
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            int oldVersionCode = prefs.getInt("versionCode", 0);
            boolean yenilikler_lastInfoIsClicked = prefs.getBoolean("yenilikler_lastInfoIsClicked",false);
            if(oldVersionCode != pInfo.versionCode){
                editor.putBoolean("yenilikler_lastInfoIsClicked",false);
                editor.apply();
            }
            if(oldVersionCode != pInfo.versionCode || !yenilikler_lastInfoIsClicked){
                YenilikleriGoster();
                editor.putInt("versionCode", pInfo.versionCode);
                editor.apply();
            }

        } catch (Exception e) {
        }
    }

    private void YenilikleriGoster() {
        DuzenlemelerYapılıyor();
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        //CreateBuilderYenilikler("Yenilikler!", " Uygulamayı Kullanmaya Devam Edebilirsiniz Teşekkürler :)", false);
        //CreateBuilderYenilikler("Yenilikler!", "Durumu Görüntüle Ekranı'na yapılacaklar yüzdesi eklendi.\nYapılacaklar listesine bağlı olarak otomatik güncellenir.\nDüzenle butonuna tıklayarak kendi isteğinizle değiştirebilirsiniz. ", false);
        //CreateBuilderYenilikler("ÖNEMLİ!", "Uygulamaya kullanıcıyı rahatsız etmeyecek şekilde, çok az miktarda, reklam eklendi.\nAnlayışınız için teşekkürler :)", false);

        //CreateBuilderYenilikler("Güncelleme", " Değerlendirmeleriniz bizim için önemli teşekkür ederiz.\n Dersle Kalın!", false);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Güncelleme");
        builder.setMessage(" Değerlendirmeleriniz bizim için önemli teşekkür ederiz.\n Dersle Kalın!");
        builder.setCancelable(false);

        builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editor.putBoolean("yenilikler_lastInfoIsClicked",true);
                editor.apply();
            }
        });
        builder.show();

        CreateBuilderYenilikler("Güncelleme", " 'Sınav Sayaç' ekranı eklendi!\nSınavlara kalan süreleri takip edebilirsiniz.", false);
        CreateBuilderYenilikler("Güncelleme", " 'Koyu Mod' eklendi.\nAyarlar kısmından uygulama modunu değiştirebiliriniz!", false);
        CreateBuilderYenilikler("Güncelleme", " Yeni Güncellemede Eklenenler -->", false);

    }

    private void CreateBuilderYenilikler(String title, String message, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(cancelable);

        builder.setPositiveButton("İleri", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void Sıfırla() {
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if(prefs.getBoolean("checkBoxAyarlar_yapilacaklarListesi", true)){
            int x = prefs.getInt("todolist_listsayi",0);
            for(int i=0;i<x;i++){
                editor.putBoolean("todolist_listItemBool"+i,false);
            }
            editor.apply();
        }
    }

    public void DersCalisBtn(View view) {
        startActivity(new Intent(this, ActivityDersCalis.class));
        overridePendingTransition(R.anim.slidefromright,R.anim.slidetoleft);
    }

    public void VerileriAcBtnView(View view) {
        startActivity(new Intent(this, GrafikActivity.class));
        overridePendingTransition(R.anim.slidefromleft,R.anim.slidetoright);

    }

    public void SinavSayacBtnView(View view) {
        startActivity(new Intent(this, SinavaKalanSureActivity.class));
        overridePendingTransition(R.anim.slidefromleft,R.anim.slidetoright);
    }

    private static InterstitialAd mInterstitialAdGrafik;

    void GrafikReklamGoster(){
        AdRequest adRequest = new AdRequest.Builder().build();

        if(mInterstitialAdGrafik != null){
            Log.i("TAGTAG", "onAdReady");
            if (reklam_aftergrafikbutton){
                reklam_aftergrafikbutton = false;
                Log.i("TAGTAG", "onAdShowed");

                mInterstitialAdGrafik.show(MainActivity.this);
                mInterstitialAdGrafik = null;
            }
        }else{
            Log.i("TAGTAG", "onAdShow not, loading...");
            InterstitialAd.load(this,"ca-app-pub-8446349352597923/3988021203", adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            mInterstitialAdGrafik = interstitialAd;
                            Log.i("TAGTAG", "onAdLoaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            Log.d("TAGTAG", loadAdError.toString() + "");
                            Log.d("TAGTAG", loadAdError.getCode() + "");
                            mInterstitialAdGrafik = null;
                        }
                    });
        }

    }
    private AppOpenAd mAppOpenAd;

    private boolean isShowingAd = false;
    private void showOpenAd(){

        AdRequest adRequest = new AdRequest.Builder().build();
        AppOpenAd.load(this, "ca-app-pub-8446349352597923/2898795656",
                adRequest,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                new AppOpenAd.AppOpenAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                    }

                    @Override
                    public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                        super.onAdLoaded(appOpenAd);
                        mAppOpenAd = appOpenAd;

                        if(!isShowingAd) {
                            mAppOpenAd.show(MainActivity.this);
                            isShowingAd = true;
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        OrtakAyarlar.currentDate = currentDate;

        TextView text_main_tarih = findViewById(R.id.main_tarih);
        text_main_tarih.setText(currentDate);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if(true){
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Çıkış Yap");
        builder.setMessage("Çıkış mı yapmak istiyorsunuz?");
        builder.setCancelable(true);

        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });
        builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    public void DurumuGoruntuleBtnView(View view) {
        startActivity(new Intent(this, ActivityDurumuGoruntule.class));
        overridePendingTransition(R.anim.slidefromleft,R.anim.slidetoright);
    }

    public void AyarlarAc(View view) {
        startActivity(new Intent(this, ActivitySettings.class));
        overridePendingTransition(R.anim.slidefromleft,R.anim.slidetoright);
    }

    public void YapılacaklarListesiAcBtnView(View view) {
        startActivity(new Intent(this, ActivityToDoList.class));
        overridePendingTransition(R.anim.slidefromleft,R.anim.slidetoright);
    }

    int geriBildirimSayisiInteger = 0;
    void geriBildirimGonderKontrolBuilder(){
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Geri Bildirim");
        builder.setMessage("Uygulama ile ilgili yaşadığınız bir sorun var mı?");
        builder.setCancelable(false);

        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ScrollView geriBildirimGonderView = findViewById(R.id.geriBildirimGonderView);
                geriBildirimGonderView.setVisibility(View.VISIBLE);
                geriBildirimGonderKontrol();
            }
        });
        builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CreateBuilderAlertDialog("Teşekkürler", "Bunu duyduğumuza sevindik. \n Olası bir hata durumunda ayarlar kısmından geri bildirim gönderebilirsiniz. \n İyi çalışmalar!", true);

                editor.putBoolean("is_anamenu_geribildirim_firstime", false);
                editor.apply();
            }
        });
        builder.show();
    }
    void geriBildirimGonderKontrol(){
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference top_geriBildirimSayRef = database.getReference("/GeriBildirim_Main_istek" + "/toplamSayi");
        top_geriBildirimSayRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer value = dataSnapshot.getValue(Integer.class);

                try {
                    geriBildirimSayisiInteger = value;
                }catch (Exception e){
                    geriBildirimSayisiInteger = 0;
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
        Button geriBildirimMenuyeDonBtn = findViewById(R.id.geriBildirimGeriBtn);

        geriBildirimMenuyeDonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("is_anamenu_geribildirim_firstime", false);
                editor.apply();
                geriBildirimGonderView.setVisibility(View.GONE);
            }
        });

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
                 String currentDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss ", Locale.getDefault()).format(new Date());
                    String versionName = "";
                    try {
                        PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
                        versionName = pInfo.versionName;
                    } catch (PackageManager.NameNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference geriBildirimRef = database.getReference("GeriBildirim_Main_istek/geribildirim_" + geriBildirimSayisiInteger);

                    geriBildirimRef.setValue("Geri Bildirim : " + geriBildirimStringText + "//// E-mail : " + geriBildirimMailStringText + "//// phoneORinsta : " + geriBildirimInstagramStringText + "////  Tarih: " + currentDate + "////  Version Name: " + versionName);
                    DatabaseReference kisiSayisiRef = database.getReference("GeriBildirim_Main_istek" + "/toplamSayi");
                    kisiSayisiRef.setValue((geriBildirimSayisiInteger+1)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            CreateBuilderAlertDialog("Başarılı", "Geri bildiriminiz başarıyla gönderildi! En kısa zamanda ilgileneceğiz. \n Olası bir hata durumunda tekrardan ayarlar kısmından geri bildirim gönderebilirsiniz.", false);
                            editor.putBoolean("is_anamenu_geribildirim_firstime", false);
                            editor.apply();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            CreateBuilderAlertDialog("Başarısız", "Geri bildiriminiz gönderilemedi! Bağlantınızı kontrol edin veya daha sonra tekrar deneyin..", false);
                        }
                    });

                    geriBildirimGonderView.setVisibility(View.GONE);

            }
        });
    }
    private void CreateBuilderAlertDialog(String title, String message, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

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

    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;
    private static final int FLEXIBLE_APP_UPDATE_REQ_CODE = 123;

    private static boolean guncellestirmeSorulduMu = false;
    public void UpdateApp(){
        if(guncellestirmeSorulduMu){
            return;
        }
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        installStateUpdatedListener = state -> {
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate();
            } else if (state.installStatus() == InstallStatus.INSTALLED) {
                removeInstallStateUpdateListener();
            } else {
            }
        };
        appUpdateManager.registerListener(installStateUpdatedListener);
        checkUpdate();
    }

    private void checkUpdate() {
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                startUpdateFlow(appUpdateInfo);
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                //popupSnackBarForCompleteUpdate();
            }
        });
    }

    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Güncelleme");
        builder.setMessage("Uygulamanın bir güncellemesi var indirmek ister misiniz?");
        builder.setCancelable(false);

        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.untygames.dersapp"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                guncellestirmeSorulduMu=true;
            }
        });
        builder.show();

        /*
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, this, MainActivity.FLEXIBLE_APP_UPDATE_REQ_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FLEXIBLE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                guncellestirmeSorulduMu = true;
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(),"Güncelleme İndiriliyor", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Güncelleme Yüklenemedi", Toast.LENGTH_LONG).show();
                checkUpdate();
            }
        }
    }

    private void popupSnackBarForCompleteUpdate() {
        Snackbar.make(findViewById(android.R.id.content).getRootView(), "Uygulama hazır!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Yükle", view -> {
                    if (appUpdateManager != null) {
                        appUpdateManager.completeUpdate();
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.red))
                .show();
    }

    private void removeInstallStateUpdateListener() {
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeInstallStateUpdateListener();
    }

}