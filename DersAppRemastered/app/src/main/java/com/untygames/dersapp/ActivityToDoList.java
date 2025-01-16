package com.untygames.dersapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivityToDoList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        Read();
        KontrolEt();
        YuzdeKontrol(0);
    }

    public void ToDoList_Ekle(View view) {
        LinearLayout ll = findViewById(R.id.todolist_YeniGorevEkle);
        ll.setVisibility(View.VISIBLE);

        Button close = findViewById(R.id.btnToDoListKapat);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll.setVisibility(View.GONE);
            }
        });
    }

    public void Kaydet(View view) {
        EditText editText = findViewById(R.id.todolist_yeniGorevEditText);
        String name = editText.getText().toString();

        if(TextUtils.isEmpty(name)){
            return;
        }

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int todolist_listsayi = prefs.getInt("todolist_listsayi",0);

        editor.putString("todolist_listItemName" + todolist_listsayi, name);
        editor.putBoolean("todolist_listItemBool" + todolist_listsayi, false);
        editor.putBoolean("todolist_listItemIsActive" + todolist_listsayi, true);

        todolist_listsayi+=1;
        editor.putInt("todolist_listsayi",todolist_listsayi);
        editor.apply();
        startActivity(new Intent(this,ActivityToDoList.class));
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        finish();
    }

    int toplamBools;
    int isFinishedBools;
    void Read(){
        isFinishedBools=0;
        toplamBools=0;
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int todolist_listsayi = prefs.getInt("todolist_listsayi",0);
        for(int i=0; i<todolist_listsayi; i++){
            Boolean listItemIsActive = prefs.getBoolean("todolist_listItemIsActive" + i, true);

            if(listItemIsActive){
                String listItemName = prefs.getString("todolist_listItemName" + i, "None");
                Boolean listItemBool = prefs.getBoolean("todolist_listItemBool" + i, false);

                RelativeLayout.LayoutParams Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                RelativeLayout rl = new RelativeLayout(ActivityToDoList.this);
                rl.setLayoutParams(Params);

                CheckBox checkBox = new CheckBox(ActivityToDoList.this);
                checkBox.setTextSize(25);
                checkBox.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                checkBox.setTextColor(getColor(R.color.white1));
                checkBox.setLayoutParams(Params);
                checkBox.setPadding(30,30,30,30);
                checkBox.setBackgroundResource(R.drawable.bg_arka3);
                checkBox.setText(listItemName);
                checkBox.setChecked(listItemBool);
                Typeface typeface = ResourcesCompat.getFont(this, R.font.a1);
                checkBox.setTypeface(typeface);
                checkBox.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                checkBox.setButtonTintList(ColorStateList.valueOf(getColor(R.color.white1)));

                RelativeLayout.LayoutParams ParamsRules = (RelativeLayout.LayoutParams) checkBox.getLayoutParams();
                ParamsRules.leftMargin = 15;
                ParamsRules.rightMargin = 15;
                ParamsRules.topMargin = 15;
                ParamsRules.bottomMargin = 15;

                rl.addView(checkBox, ParamsRules);

                LinearLayout lr = findViewById(R.id.todolist_linear);
                lr.addView(rl,Params);

                toplamBools++;
                if(listItemBool){
                    isFinishedBools++;
                }

                int a = i;
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b){
                            YuzdeKontrol(1);
                        }
                        else{
                            YuzdeKontrol(-1);
                        }
                        editor.putBoolean("todolist_listItemBool" + a, b);
                        editor.apply();
                    }
                });

                checkBox.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        DoYouWantDelete(a);
                        return false;
                    }
                });
            }
        }
    }

    private void YuzdeKontrol(int i) {
        isFinishedBools += i;
        TextView todolist_isCompletedText = findViewById(R.id.todolist_isCompletedText);
        ImageView todolist_isCompletedImage = findViewById(R.id.todolist_isCompletedImage);

        float yuzde;
        try {
            yuzde = ((float) isFinishedBools/ (float)toplamBools) * 100;
            Log.d("TAGTAG", "isFinishedBools: " + isFinishedBools);
            Log.d("TAGTAG", "toplamBools: " + toplamBools);
            Log.d("TAGTAG", "YuzdeKontrol: " + yuzde);
        }catch (Exception e){
            yuzde = 100;
        }
        if(toplamBools == 0){
            yuzde = 100;
        }
        todolist_isCompletedText.setText("%" + Math.round(yuzde) + "\nTAMAMLANDI");

        if(isFinishedBools == toplamBools){
            todolist_isCompletedImage.setImageResource(R.drawable.check);
        }else{
            todolist_isCompletedImage.setImageResource(R.drawable.yanlis);
        }

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        editor.putInt(currentDate + "_tarihinde_yuzde", Math.round(yuzde));
        editor.apply();
    }

    private void KontrolEt() {
    }

    private void DoYouWantDelete(int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityToDoList.this);

        builder.setTitle("Sil");
        builder.setMessage("Bu notu silmek istediğinize emin misiniz?");

        builder.setPositiveButton("Sil", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("todolist_listItemIsActive" + i, false);
                editor.apply();

                startActivity(new Intent(ActivityToDoList.this, ActivityToDoList.class));
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                finish();
            }

        });

        builder.setNegativeButton("Kapat", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        Geri(findViewById(R.id.todolist_isDone));
    }

    public void Geri(View view) {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.slidefromright,R.anim.slidetoleft);
    }
}