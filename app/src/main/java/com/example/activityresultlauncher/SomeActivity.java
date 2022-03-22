package com.example.activityresultlauncher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SomeActivity extends AppCompatActivity {
    private EditText editText;
    public static final String KEY_NAME = "NAME";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_someactivity);
        setTitle("Some Activity");

        editText = findViewById(R.id.etText1);

        findViewById(R.id.btSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredText = editText.getText().toString();
                Intent intent = new Intent();
                intent.putExtra(KEY_NAME, enteredText);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
