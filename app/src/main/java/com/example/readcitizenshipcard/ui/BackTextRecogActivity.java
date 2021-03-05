package com.example.readcitizenshipcard.ui;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.readcitizenshipcard.R;

public class BackTextRecogActivity extends FragmentActivity {
private Button doneBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_ReadCitizenshipCard);
        setContentView(R.layout.activity_back_text_recog);
        doneBtn2=findViewById(R.id.done_btn2);
        doneBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move = new Intent(
                        BackTextRecogActivity.this,
                        FormActivity.class);
                startActivity(move);
            }
        });
    }
}