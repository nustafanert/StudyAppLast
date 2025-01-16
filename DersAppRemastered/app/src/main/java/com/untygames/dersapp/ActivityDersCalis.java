package com.untygames.dersapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaExtractor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ActivityDersCalis extends AppCompatActivity {

    String[] sozler = {"" +
            "Zafer, zafer benimdir diyebilenindir. Başarı ise “başaracağım” diye başlayarak sonunda “başardım diyenindir. (Mustafa Kemal Atatürk)\n",
            "Kazanma isteği ve başarıya ulaşma arzusu birleşirse kişisel mükemmelliğin kapısını açar. (Konfüçyüs)\n",
            "Hiçbir şeyden vazgeçme, çünkü sadece kaybedenler vazgeçer. (Abraham Lincoln)\n",
            "Başarıya çıkan asansör bozuk. Bekleyerek zaman kaybetmeyin, adım adım merdivenleri çıkmaya başlayın. (Joe Girard)\n",
            "Fırsatlar durup dururken karşınıza çıkmaz, onları siz yaratırsınız. (Chris Grosser)\n",
            "Şansa çok inanırım ve ne kadar çok çalıştıysam ona o kadar çok sahip oldum. (Thomas Jefferson)\n",
            "Bir şeye başlayıp başarısız olmaktan daha kötü tek şey hiçbir şeye başlamamaktır. (Seth Godin)\n",
            "Sadece sınırlarını aşmanın riskini alanlar ne kadar ileri gidebildiklerini görürler. (T.S. Elliot)\n",
            "Hayat her ne kadar zor görünse de, yapabileceğimiz ve başarabileceğimiz bir şey mutlaka vardır. (Stephen Hawking)\n",
            "Bir şeyi başarmak ne kadar zorsa, zaferin tadı o kadar güzeldir. (Pele)\n",
            "Bir şeye başlayıp başarısız olmaktan daha kötü tek şey hiçbir şeye başlamamaktır. (Seth Godin)\n",
            "Hiç kimse başarı merdivenine elleri cebinde tırmanmamıştır. (J. Keth Moorhead)\n",
            "Ne zaman başarılı bir iş görseniz, birisi bir zamanlar mutlaka cesur bir karar almıştır. (Peter Ducker)\n",
            "Sessizce sıkı çalışın, bırakın başarı sesiniz olsun. (Frank Ocean)\n",
            "Eğer her şey kontrol altında gibi görünüyorsa, yeterince hızlı gitmiyorsunuzdur. (Mario Andretti)\n",
            "Başarısız insanlar içerisinde bulundukları duruma göre karar verirler. Başarılı insanlar ise olmak istedikleri yere göre karar verirler. (Benjamin Hardy)\n",
            "Sadece başarılı bir insan olmaya değil, değerli bir insan olmaya çalışın. (Albert Einstein)\n",
            "Başarı son değildir, başarısızlık ise ölümcül değildir: Önemli olan ilerlemeye cesaret etmektir. (Winston Churchill)",
            "Hayatımı sadece ben değiştirebilirim. Kimse benim için bunu yapmaz. (Carol Burnett)\n",
            "Hiç kimse geriye gidip yeni bir başlangıç yapamaz ama bugün yeni bir son yapıp yeniden başlayabilir. (Frank M. Robinson)\n",
            "Diğerlerinden daha akıllı olmak zorunda değiliz. Diğerlerinden daha disiplinli olmak zorundayız. (Warren Buffett)\n",
            "Uyandığınızda; yaşamanın, zevk almanın, düşünmenin ve sevmenin ne kadar büyük bir ayrıcalık olduğunu hatırlayın. (Marcus Aurelius)\n" ,
            "Siz kendinize inanın, başkaları da size inanacaktır. (Goethe)\n" ,
            "Karanlıktan korkan bir çocuğu kolayca affedebiliriz. Hayatın gerçek trajedisi büyükler ışıktan korktuğunda başlar. (Plato)\n" ,
            "Dünyayı değiştirebilen insanla buna inanacak kadar deli olanlardır. (Steve Jobs)\n" ,
            "Sadece görülmeyeni gören imkansızı başarabilir. (Frank L. Gaines)\n" ,
            "Her gün kendini yeniliyorsun. Bazen başarılı olursun, bazen olamazsın, ama önemli olan ortalamadır. (Satya Nadella)\n" ,
            "Kısa vadede acımasızca dürüst olun, uzun vade için ise iyimser ve kendinizden emin olun. (Reed Hastings)\n" ,
            "Birisi size bir şeyi yapamayacağını söylediğinde, belki de size sadece kendi yapamadıklarını söylüyordur. (Sheldon Cahoon)\n" ,
            "Zeki insanlar herkesten ve her şeyden öğrenirler. Ortalama insanlar deneyimlerinden öğrenirler. Aptal insanlar ise zaten bütün cevaplara sahiptir. (Socrates)\n" ,
            "Bardağın yarısının dolu mu, boş mu olduğunu tartışan insanlar asıl odaklanmaları gereken noktayı kaçırıyorlar. Bardak doldurulabilir. (Anonim)\n" ,
            "Yüzünüzü güneşe çevirin, böylece gölgeler her zaman arkanızda kalacaktır. (Walt Whitman)\n" ,
            "Dün zekiydim, bu yüzden dünyayı değiştirmek istedim. Şimdi ise bilgeyim, bu sebeple kendimi değiştiriyorum. (Rumi)\n" ,
            "Eğer bir şeyi değiştiremiyorsanız bırakın. Değiştiremediğiniz şeylerin mahkûmu olmayın. (Tsang Lindsay)\n" ,
            "Dışarıda olanları her zaman kontrol edemezsiniz. Ama içinde olanları edebilirsin. (Wayne Dyer)\n" ,
            "En büyük düşmanınızın iki kulağınızın arasında yaşamadığına emin olun. (Laird Hamilton)\n" ,
            "Dünden ders alın, bugün için yaşayın, yarın için umutlu olun. (Albert Einstein)"};
    boolean isRunning = false;
    boolean isRunningCountDown = true;
    String currentDate = "null";


    Chronometer chronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ders_calis);
        currentDate = OrtakAyarlar.currentDate;

        chronometer = findViewById(R.id.cronometer);

        YeniKronometer();

        TextView sozlerTextView = findViewById(R.id.sozlerTextView);

        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        Intent intent = new Intent();
        String packageName = getPackageName();
        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            AlertDialog.Builder build1 = new AlertDialog.Builder(ActivityDersCalis.this);
            build1.setCancelable(false);
            build1.setTitle("Pil İzini");
            build1.setMessage("Uygulamanın arkaplanda çalışması için pil izinini kabul edin");

            build1.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    startActivity(intent);
                }
            });
            build1.show();
        }

        Random rand = new Random();
        int sozlerRastgeleSayi = rand.nextInt(sozler.length-1);

        sozlerTextView.setText(sozler[sozlerRastgeleSayi]);

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        long d = prefs.getLong("chronometerKaldigiYerdenDevam", 0);

        Log.d(TAG, "TAGTAG onCreate: " + d);

        if(d != 0){
            isRunning=false;
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDersCalis.this);
            builder.setCancelable(false);
            builder.setTitle("Yanlışlıkla Kapandı!");
            builder.setMessage("En son kapattığınız yerden devam etmek misiniz?");

            builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    isRunning = true;
                }
            });

            builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong("chronometerKaldigiYerdenDevam", 0);
                    editor.apply();

                    cronometer.setBase(SystemClock.elapsedRealtime());
                    format = new SimpleDateFormat("hh:mm:ss aa");

                    sessionManager.setCurrentTime(format.format((new Date())));

                    isRunning = true;
                    cronometer.start();
                }
            });
            builder.show();
        }else{
            cronometer.setBase(SystemClock.elapsedRealtime());
            isRunning = true;
        }


        cronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long delta = SystemClock.elapsedRealtime() - cArg.getBase();

                int h = (int) ((delta / 1000) / 3600);
                int m = (int) (((delta / 1000) / 60) % 60);
                int s = (int) ((delta / 1000) % 60);

                String hStr = h + "";
                String mStr = m + "";
                String sStr = s + "";

                if(hStr.length()==1){
                    hStr = "0" + hStr;
                }
                if(mStr.length()==1){
                    mStr = "0" + mStr;
                }
                if(sStr.length()==1){
                    sStr = "0" + sStr;
                }

                String customText = hStr +":" + mStr + ":" + sStr;

                cArg.setText(customText);
            }
        });

        init();
        Sonuc();
    }

    Chronometer cronometer;
    Button btnReset;
    SessionManager sessionManager;

    SimpleDateFormat format;
    String currentTime;

    void YeniKronometer(){
        cronometer=findViewById(R.id.cronometer);

        sessionManager = new SessionManager(getApplicationContext());

        format = new SimpleDateFormat("hh:mm:ss aa");

        currentTime = format.format((new Date()));

        boolean flag = sessionManager.getFlag();

        if(!flag){
            sessionManager.setCurrentTime(currentTime);

            sessionManager.setFlag(true);

            cronometer.start();
        }else{
            String sessionManagerCurrentTime = sessionManager.getCurrentTime();
            try {
                Date date1 = format.parse(sessionManagerCurrentTime);
                Date date2 = format.parse(currentTime);

                long milis = date2.getTime() - date1.getTime();

                cronometer.setBase((SystemClock.elapsedRealtime()) - milis);

                cronometer.start();
            }catch (ParseException e){
                e.printStackTrace();
            }
        }

        /*btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cronometer.setBase(SystemClock.elapsedRealtime());
                sessionManager.setCurrentTime(currentTime);
                cronometer.start();
            }
        });*/
    }


    void init(){
        Notify();

        CheckBox dersCalis_GeriSayimTitretCheckBox = findViewById(R.id.dersCalis_GeriSayimTitretCheckBox);
        dersCalis_GeriSayimTitretCheckBox.setChecked(true);

        ScrollView dersCalis_sonucYuzdeliklerEkran = findViewById(R.id.dersCalis_sonucYuzdeliklerEkran);
        Button dersCalis_kaydetmedenCikBtn = findViewById(R.id.dersCalis_kaydetmedenCikBtn);
        Button btnDersCalis_devam = findViewById(R.id.btnDersCalis_devam);
        btnDersCalis_devam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong("chronometerKaldigiYerdenDevam", 0);
                editor.apply();
                isDone=true;

                dersCalis_sonucYuzdeliklerEkran.setVisibility(View.VISIBLE);
                SonucYuzdelikEkran();
                cozulenSoru=0;
                DersSoruOku();
            }
        });

        dersCalis_kaydetmedenCikBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDersCalis.this);

                builder.setTitle("Menüye Dön?");
                builder.setMessage("Verileriniz kaydedilmeden menüye dönmek istediğinize emin misiniz?");

                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putLong("chronometerKaldigiYerdenDevam", 0);
                        editor.apply();
                        isDone=true;

                        /*
                        cronometer.setBase(SystemClock.elapsedRealtime());
                        format = new SimpleDateFormat("hh:mm:ss aa");

                        sessionManager.setCurrentTime(format.format((new Date())));*/

                        cancelAlarm();

                        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        nMgr.cancel(2);

                        startActivity(new Intent(ActivityDersCalis.this, MainActivity.class));
                        overridePendingTransition(R.anim.slidefromleft,R.anim.slidetoright);
                    }
                });

                builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });

        Button btnDersCalisBitir = findViewById(R.id.btnDersCalisBitir);
        btnDersCalisBitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDersCalis.this);

                builder.setTitle("Emin misiniz?");
                builder.setMessage("Ders çalışmayı bitirmek istediğinize emin misiniz?");

                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isSelectedSayac == 1){
                            VoidBtnDersCalisBitir(0);
                        }
                        if(isSelectedSayac == 2){
                            TextView dersCalis_GeriSayimText = findViewById(R.id.dersCalis_GeriSayimText);
                            String[] ss = dersCalis_GeriSayimText.getText().toString().split(":");
                            int s = 0;
                            try {
                                s = Integer.parseInt(ss[0]);
                            }catch (Exception e){
                                s = 0;
                            }
                            VoidBtnDersCalisBitir(countDownHedefSure - (s + 1));
                        }
                    }
                });

                builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();

            }
        });

        TextView durdurulduText = findViewById(R.id.durdurulduText);

        Button btnDersCalis_duraklatDevamEt = findViewById(R.id.btnDersCalisDuraklatDevamEt);
        btnDersCalis_duraklatDevamEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSelectedSayac == 1){

                    Chronometer meter = findViewById(R.id.cronometer);

                    ObjectAnimator colorAnim = ObjectAnimator.ofInt(meter, "textColor", Color.BLACK, getColor(R.color.text8));
                    colorAnim.setDuration(1000);
                    colorAnim.setEvaluator(new ArgbEvaluator());
                    colorAnim.setRepeatCount(ValueAnimator.INFINITE);
                    colorAnim.setRepeatMode(ValueAnimator.REVERSE);

                    if(isRunning){
                        durdurulduText.setVisibility(View.VISIBLE);
                        btnDersCalis_duraklatDevamEt.setText("DEVAM ET");
                        timeWhenStopped = cronometer.getBase() - SystemClock.elapsedRealtime();
                        cronometer.stop();
                        isRunning=false;
                        colorAnim.start();
                        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        nMgr.cancel(2);
                    }else{
                        durdurulduText.setVisibility(View.GONE);
                        cronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                        cronometer.start();
                        btnDersCalis_duraklatDevamEt.setText("DURAKLAT");
                        isRunning=true;
                        colorAnim.cancel();

                        ObjectAnimator colorAnim1 = ObjectAnimator.ofInt(meter, "textColor", Color.BLACK, Color.BLACK);
                        colorAnim1.setDuration(1000);
                        colorAnim1.setEvaluator(new ArgbEvaluator());
                        colorAnim1.setRepeatCount(ValueAnimator.INFINITE);
                        colorAnim1.setRepeatMode(ValueAnimator.REVERSE);
                        colorAnim1.start();
                        Notify();
                    }
                }

                else if(isSelectedSayac == 2){
                    TextView meter = findViewById(R.id.dersCalis_GeriSayimText);

                    ObjectAnimator colorAnim = ObjectAnimator.ofInt(meter, "textColor", getColor(R.color.text7), getColor(R.color.text4));
                    colorAnim.setDuration(1000);
                    colorAnim.setEvaluator(new ArgbEvaluator());
                    colorAnim.setRepeatCount(ValueAnimator.INFINITE);
                    colorAnim.setRepeatMode(ValueAnimator.REVERSE);

                    if(isRunningCountDown){
                        durdurulduText.setVisibility(View.VISIBLE);
                        Log.d("TAGTAG", "onClick: start" );
                        myCountDownTimer.cancel();
                        cancelAlarm();
                        btnDersCalis_duraklatDevamEt.setText("DEVAM ET");
                        isRunningCountDown=false;
                        colorAnim.start();
                        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        nMgr.cancel(2);

                        ProgressBar dersCalis_GeriSayimProgress = findViewById(R.id.dersCalis_GeriSayimProgress);
                        float percentage = 100 - (countDownLeft / (float) (countDownHedefSure * 60000) * 100);
                        dersCalis_GeriSayimProgress.setProgress(Math.round(percentage),true);
                    }else{
                        durdurulduText.setVisibility(View.GONE);
                        Notify();
                        colorAnim.cancel();

                        ObjectAnimator colorAnim1;
                        if(OrtakAyarlar.DarkMode){
                            colorAnim1 = ObjectAnimator.ofInt(meter, "textColor", Color.WHITE, Color.WHITE);
                        }else{
                            colorAnim1 = ObjectAnimator.ofInt(meter, "textColor", Color.BLACK, Color.BLACK);
                        }
                        colorAnim1.setDuration(1000);
                        colorAnim1.setEvaluator(new ArgbEvaluator());
                        colorAnim1.setRepeatCount(ValueAnimator.INFINITE);
                        colorAnim1.setRepeatMode(ValueAnimator.REVERSE);
                        colorAnim1.start();

                        Log.d("TAGTAG", "onClick: cancel" );
                        timerStart(countDownLeft);
                        btnDersCalis_duraklatDevamEt.setText("DURAKLAT");
                        isRunningCountDown=true;

                        ProgressBar dersCalis_GeriSayimProgress = findViewById(R.id.dersCalis_GeriSayimProgress);
                        float percentage = 100 - (countDownLeft / (float) (countDownHedefSure * 60000) * 100);
                        dersCalis_GeriSayimProgress.setProgress(Math.round(percentage),true);
                    }
                }
            }
        });

        dersCalis_GeriSayimTitretCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cancelAlarm();
                startAlarm(countDownLeft, b);
            }
        });

        SwitchCompat dersCalis_switchCompat = findViewById(R.id.dersCalis_switchCompat);

        RelativeLayout dersCalis_GeriSayimView = findViewById(R.id.dersCalis_GeriSayimView);
        Chronometer sayac = findViewById(R.id.cronometer);

        dersCalis_switchCompat.setChecked(false);
        dersCalis_switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b = dersCalis_switchCompat.isChecked();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ActivityDersCalis.this);
                builder1.setCancelable(false);
                builder1.setTitle("Değiştir");
                builder1.setMessage("Sayacınız sıfırlanacak emin misiniz?");

                builder1.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(b){
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDersCalis.this);
                            builder.setCancelable(false);
                            builder.setTitle("Zamanlayıcı");
                            builder.setMessage("Kaç dakikalık bir zamanlayıcı kurmak istiyorsunuz?");
                            RelativeLayout.LayoutParams isimParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                            RelativeLayout rl = new RelativeLayout(ActivityDersCalis.this);

                            EditText edittext = new EditText(ActivityDersCalis.this);
                            edittext.setTypeface(Typeface.DEFAULT_BOLD);
                            edittext.setTextSize(20);
                            edittext.setLayoutParams(isimParams);
                            edittext.setHint("Dakika giriniz... 0-180");
                            edittext.setFilters(new InputFilter[]{ new Nunber24Filter("1", "180")});
                            edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
                            edittext.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }
                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }
                                @Override
                                public void afterTextChanged(Editable editable) {
                                    edittext.clearComposingText();
                                }
                            });

                            RelativeLayout.LayoutParams isimParamsRules = (RelativeLayout.LayoutParams) edittext.getLayoutParams();
                            isimParamsRules.addRule(RelativeLayout.CENTER_IN_PARENT);
                            isimParamsRules.leftMargin = 40;
                            isimParamsRules.rightMargin = 40;
                            isimParamsRules.topMargin = -30;

                            rl.addView(edittext, isimParamsRules);
                            builder.setView(rl);

                            builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        Integer.parseInt(edittext.getText().toString());
                                    }catch (Exception e){
                                        dersCalis_switchCompat.setChecked(false);
                                        return;
                                    }

                                    durdurulduText.setVisibility(View.GONE);
                                    btnDersCalis_duraklatDevamEt.setText("DURAKLAT");

                                    TextView meter = findViewById(R.id.dersCalis_GeriSayimText);
                                    ObjectAnimator colorAnim1;
                                    if(OrtakAyarlar.DarkMode){
                                        colorAnim1 = ObjectAnimator.ofInt(meter, "textColor", Color.WHITE, Color.WHITE);
                                    }else{
                                        colorAnim1 = ObjectAnimator.ofInt(meter, "textColor", Color.BLACK, Color.BLACK);
                                    }
                                    colorAnim1.setDuration(1000);
                                    colorAnim1.setEvaluator(new ArgbEvaluator());
                                    colorAnim1.setRepeatCount(ValueAnimator.INFINITE);
                                    colorAnim1.setRepeatMode(ValueAnimator.REVERSE);
                                    colorAnim1.start();

                                    cronometer.setBase(SystemClock.elapsedRealtime());
                                    isRunning = false;
                                    cronometer.stop();
                                    sayac.setVisibility(View.GONE);
                                    dersCalis_GeriSayimView.setVisibility(View.VISIBLE);
                                    GeriSayim(edittext.getText().toString());
                                    //alert dialog oluştur ve kaç dk diye sor maks 180dk
                                    isSelectedSayac = 2;
                                    Notify();

                                    Button btnDersCalis_duraklatDevamEt = findViewById(R.id.btnDersCalisDuraklatDevamEt);
                                   // btnDersCalis_duraklatDevamEt.setVisibility(View.GONE);

                                    SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putLong("chronometerKaldigiYerdenDevam", 0);
                                    editor.apply();
                                    isDone=true;
                                }
                            });

                            builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dersCalis_switchCompat.setChecked(false);
                                }
                            });
                            builder.show();
                        }else{
                            durdurulduText.setVisibility(View.GONE);
                            btnDersCalis_duraklatDevamEt.setText("DURAKLAT");

                            Chronometer meter = findViewById(R.id.cronometer);
                            ObjectAnimator colorAnim1;
                            if(OrtakAyarlar.DarkMode){
                                colorAnim1 = ObjectAnimator.ofInt(meter, "textColor", Color.WHITE, Color.WHITE);
                            }else{
                                colorAnim1 = ObjectAnimator.ofInt(meter, "textColor", Color.BLACK, Color.BLACK);
                            }
                            colorAnim1.setDuration(1000);
                            colorAnim1.setEvaluator(new ArgbEvaluator());
                            colorAnim1.setRepeatCount(ValueAnimator.INFINITE);
                            colorAnim1.setRepeatMode(ValueAnimator.REVERSE);
                            colorAnim1.start();

                            myCountDownTimer.cancel();
                            dersCalis_GeriSayimView.setVisibility(View.GONE);
                            sayac.setVisibility(View.VISIBLE);
                            sayac.setBase(SystemClock.elapsedRealtime());
                            sayac.start();

                            Button btnDersCalis_duraklatDevamEt = findViewById(R.id.btnDersCalisDuraklatDevamEt);
                            //btnDersCalis_duraklatDevamEt.setVisibility(View.VISIBLE);

                            cancelAlarm();

                            isSelectedSayac = 1;
                            Notify();
                        }
                    }
                });

                builder1.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dersCalis_switchCompat.setChecked(!b);
                    }
                });
                builder1.show();

            }
        });
    }

    private void SonucYuzdelikEkran() {
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        EditText text = findViewById(R.id.dersCalis_sonuc_GünNotEdit);

        String önce = prefs.getString(currentDate + "_tarihinde_not", "");
        text.setText(önce);

        Button btnDersCalis_kapat = findViewById(R.id.btnDersCalis_kapat);
        btnDersCalis_kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.reklam_afterderscalisIsShow = true;

                editor.putString(currentDate + "_tarihinde_not", text.getText().toString());
                editor.apply();

                startActivity(new Intent(ActivityDersCalis.this, MainActivity.class));
                overridePendingTransition(R.anim.slidefromleft,R.anim.slidetoright);
            }
        });


    }

    public static final String channelID = "DersID";
    public static final String channelName = "Ders Takip";

    private void Notify() {
        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(2);

        NotificationManager mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_LOW);
            mManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder nb = null;
        if(isSelectedSayac==1){
             nb = new NotificationCompat.Builder(getApplicationContext(), channelID)
                    .setContentTitle("Kronometre Çalışıyor")
                    .setOngoing(true)
                    .setDefaults(0)
                    .setSmallIcon(R.mipmap.ic_launcher_round);
        }
        if(isSelectedSayac==2){
             nb = new NotificationCompat.Builder(getApplicationContext(), channelID)
                    .setContentTitle("Sayaç Çalışıyor")
                    .setOngoing(true)
                    .setDefaults(0)
                    .setSmallIcon(R.mipmap.ic_launcher_round);
        }

        mManager.notify(2, nb.build());
    }

    int countDownHedefSure;
    long countDownLeft;
    CountDownTimer myCountDownTimer;
    void GeriSayim(String dk){
        countDownHedefSure = Integer.parseInt(dk);
        int sure = Integer.parseInt(dk);
        long hedefSure = (long) sure * 1000 * 60;
        timerStart(hedefSure);
    }
    private void timerStart(long milis) {
        ProgressBar dersCalis_GeriSayimProgress = findViewById(R.id.dersCalis_GeriSayimProgress);
        TextView dersCalis_GeriSayimText = findViewById(R.id.dersCalis_GeriSayimText);

        CheckBox dersCalis_GeriSayimTitretCheckBox = findViewById(R.id.dersCalis_GeriSayimTitretCheckBox);

        startAlarm(milis, dersCalis_GeriSayimTitretCheckBox.isChecked());


        isRunningCountDown=true;

        myCountDownTimer = new CountDownTimer(milis, 1000) {
            public void onTick(long millisUntilFinished) {

                float percentage = 1000 - (millisUntilFinished / (float) (countDownHedefSure * 60000) * 1000);

                countDownLeft = millisUntilFinished;

                dersCalis_GeriSayimProgress.setProgress(Math.round(percentage),true);

                long minute = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                long second = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));

                String StrMinute = minute + "";
                String StrSecond = second + "";

                if(StrMinute.length() == 1){
                    StrMinute = "0" + StrMinute;
                }
                if(StrSecond.length() == 1){
                    StrSecond = "0" + StrSecond;
                }
                dersCalis_GeriSayimText.setText(StrMinute + ":" + StrSecond);
            }
            public void onFinish() {
                dersCalis_GeriSayimText.setText("00:00:00");
                VoidBtnDersCalisBitir(countDownHedefSure);
            }
        }.start();
    }

    private void startAlarm(long delta, boolean isTitretBool) {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.SCHEDULE_EXACT_ALARM);

        Intent intents = new Intent();
        String packageName = getPackageName();

        if(result == PackageManager.PERMISSION_DENIED){
            intents.setAction(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
            intents.setData(Uri.parse("package:" + packageName));
            startActivity(intents);
        }

        if(result==PackageManager.PERMISSION_GRANTED){
            NotificationHelper.isTitret = isTitretBool;
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);

            delta = System.currentTimeMillis() + delta;
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, delta, pendingIntent);
        }
    }

    private void cancelAlarm(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.cancel(pendingIntent);
    }

    @Override
    protected void onDestroy() {
        cancelAlarm();

        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(2);
        Log.d(TAG, "onDestroy: nMgr Destroy");

        Chronometer meter = findViewById(R.id.cronometer);

        Log.d(TAG, "onDestroy: ");

        if(!isDone){
            SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            long delta = 0;

            if(isRunning){
                Log.d(TAG, "Run" + delta);
                delta = meter.getBase() - SystemClock.elapsedRealtime();
            }else{
                Log.d(TAG, "UnRun" + delta);
                delta = timeWhenStopped;
            }
            Log.d(TAG, "onPause: delta" + delta);

            editor.putLong("chronometerKaldigiYerdenDevam", delta);
            editor.apply();
        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDersCalis.this);

        builder.setTitle("Menüye Dön?");
        builder.setMessage("Verileriniz kaydedilmeden menüye dönmek istediğinize emin misiniz?");

        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong("chronometerKaldigiYerdenDevam", 0);
                editor.apply();
                isDone=true;

                cancelAlarm();

                NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                nMgr.cancel(2);

                startActivity(new Intent(ActivityDersCalis.this, MainActivity.class));
                overridePendingTransition(R.anim.slidefromleft,R.anim.slidetoright);
            }
        });

        builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    String TAG="TAGTAG";
    void DersSoruOku(){
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
    }
    void DersSoruKaydet(String name, int soruSay){
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int önceCozulenSoru = prefs.getInt(currentDate + "_tarihinde_"+ name +"_cozulen_soru", 0);
        int sonSoru = önceCozulenSoru + soruSay;
        editor.putInt(currentDate + "_tarihinde_"+ name +"_cozulen_soru", sonSoru);

        cozulenSoru += soruSay;
        editor.commit();
    }
    void DersSoruTopla(){
        ProgressBar progressBarCalisilanSaat = findViewById(R.id.dersCalis_progress_calisilanSaat);
        ProgressBar progressBarCozulenSoruSayisi = findViewById(R.id.dersCalis_progress_cozulenSoruSayisi);
        TextView progressBarTextCalisilanSaat = findViewById(R.id.dersCalis_progress_text_calisilanSaat);
        TextView progressBarTextCozulenSoruSayisi = findViewById(R.id.dersCalis_progress_text_cozulenSoruSayisi);

        int hedef_CalismaSuresi = MainActivity.hedef_CalismaSuresi;
        int hedef_CozulenSoruSayisi = MainActivity.hedef_CozulenSoruSayisi;

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int önceCozulenSoru = prefs.getInt(currentDate + "_tarihinde_cozulen_soru", 0);
        int önceSimdiyeKadar = prefs.getInt("simdiye_kadar_cozulen_soru", 0);
        int sonSoru = önceCozulenSoru + cozulenSoru;
        editor.putInt(currentDate + "_tarihinde_cozulen_soru", sonSoru);

        editor.putInt("simdiye_kadar_cozulen_soru", cozulenSoru + önceSimdiyeKadar);//Şimdiye Kadar Bütün Çözülen Soru

        editor.commit();

        float x = (Float.parseFloat(String.valueOf(calisilanSure))/Float.parseFloat(String.valueOf(hedef_CalismaSuresi)))*100;
        progressBarCalisilanSaat.setProgress(Math.round(x), true);

        float y = (Float.parseFloat(String.valueOf(sonSoru))/Float.parseFloat(String.valueOf(hedef_CozulenSoruSayisi)))*100;
        progressBarCozulenSoruSayisi.setProgress(Math.round(y), true);

        progressBarTextCalisilanSaat.setText(calisilanSure+"/"+hedef_CalismaSuresi + "dk");
        progressBarTextCozulenSoruSayisi.setText(sonSoru+"/"+hedef_CozulenSoruSayisi + " Soru");

        TextView dersCalis_cozulenSoruSay = findViewById(R.id.dersCalis_cozulenSoruSay);
        dersCalis_cozulenSoruSay.setText(cozulenSoru + " Soru");
    }
    int isSelectedSayac = 1;

    int cozulenSoru;
    int calisilanSure;
    long timeWhenStopped = 0;
    boolean isDone = false;

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: " + isDone);
        Chronometer meter = findViewById(R.id.cronometer);

        if(!isDone){
            SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            long delta = 0;

            if(isRunning){
                Log.d(TAG, "Run" + delta);
                delta = meter.getBase() - SystemClock.elapsedRealtime();
            }else{
                Log.d(TAG, "UnRun" + delta);
                delta = timeWhenStopped;
            }
            Log.d(TAG, "onPause: delta" + delta);

            editor.putLong("chronometerKaldigiYerdenDevam", delta);
            editor.apply();

        }

        super.onPause();
    }




    private void VoidBtnDersCalisBitir(int sure) {
        cancelAlarm();

        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(2);

        Sonuc();
        ScrollView btnDersCalisBitirEkranı = findViewById(R.id.btnDersCalisBitirEkranı);

        Chronometer chronometer = findViewById(R.id.cronometer);
        isRunning = false;

        //cronometer.setBase(SystemClock.elapsedRealtime());

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        long delta = 0;
        editor.putLong("chronometerKaldigiYerdenDevam", delta);
        editor.apply();

        chronometer.stop();

        int minutes = 0;

        if(isSelectedSayac == 1){
            Log.d(TAG, "VoidBtnDersCalisBitir: " + chronometer.getText().toString());
            String[] chSplit = chronometer.getText().toString().split(":");

            minutes = (Integer.parseInt(chSplit[0])*60) + Integer.parseInt(chSplit[1]);
        }else if (isSelectedSayac == 2){
            minutes = sure;
        }

        ///////////
        if(prefs.getInt("gunluk_hedef_soru_sayisi",0) == 0 && prefs.getInt("gunluk_hedef_calisma_sayisi",0) == 0){
            LinearLayout hedefGunlukDakikaLinear = findViewById(R.id.hedefGunlukDakikaLinear);
            LinearLayout hedefGunlukSoruLinear = findViewById(R.id.hedefGunlukSoruLinear);

            //hedefGunlukDakikaLinear.setVisibility(View.GONE);
            //hedefGunlukSoruLinear.setVisibility(View.GONE);
        }

        TextView progressBarCalisilanSure = findViewById(R.id.dersCalis_calisilan_sure);
        progressBarCalisilanSure.setText(minutes + "dk");


        int önceCalisilanSure = prefs.getInt(currentDate + "_tarihinde_calisilan_sure", 0);
        calisilanSure = minutes + önceCalisilanSure;
        editor.putInt(currentDate+ "_tarihinde_calisilan_sure", calisilanSure);
        btnDersCalisBitirEkranı.setVisibility(View.VISIBLE);

        int önceSimdiyeKadarCalisilanSure = prefs.getInt("simdiye_kadar_calisilan_sure", 0);
        editor.putInt("simdiye_kadar_calisilan_sure",önceSimdiyeKadarCalisilanSure + minutes);//Şimdiye Kadar Bütün Çalışılan Saat

        editor.commit();
    }

    List<EditText> edits = new ArrayList<>();
    void Sonuc() {
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

        ingLinear.setVisibility(View.GONE);
        dinLinear.setVisibility(View.GONE);
        diger1Linear.setVisibility(View.GONE);
        diger2Linear.setVisibility(View.GONE);
        almancaLinear.setVisibility(View.GONE);
        saglikLinear.setVisibility(View.GONE);
        matLinear.setVisibility(View.GONE);
        edbLinear.setVisibility(View.GONE);
        fizikLinear.setVisibility(View.GONE);
        kimyaLinear.setVisibility(View.GONE);
        biyoLinear.setVisibility(View.GONE);
        tarihLinear.setVisibility(View.GONE);
        cografyaLinear.setVisibility(View.GONE);

        aytmatLinear.setVisibility(View.GONE);
        aytedbLinear.setVisibility(View.GONE);
        aytfizikLinear.setVisibility(View.GONE);
        aytkimyaLinear.setVisibility(View.GONE);
        aytbiyoLinear.setVisibility(View.GONE);

        tytmatLinear.setVisibility(View.GONE);
        tytturkceLinear.setVisibility(View.GONE);
        tytfizikLinear.setVisibility(View.GONE);
        tytkimyaLinear.setVisibility(View.GONE);
        tytbiyoLinear.setVisibility(View.GONE);

        geoLinear.setVisibility(View.GONE);
        paragrafLinear.setVisibility(View.GONE);
        problemLinear.setVisibility(View.GONE);


        for (String asil : parts){
            switch (asil) {
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
}