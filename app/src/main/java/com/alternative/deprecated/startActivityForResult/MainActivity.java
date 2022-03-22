package com.alternative.deprecated.startActivityForResult;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alternative.deprecated.R;

public class MainActivity extends AppCompatActivity {

    //UI Init
    private TextView textView;
    private ImageView imageView;

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null)
                            doSomeOperations(data);
                    }
                }
            });

    ActivityResultLauncher<String> pickFromGallery = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    imageView.setImageURI(result);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Main Activity");
        textView = findViewById(R.id.txtView1);
        imageView = findViewById(R.id.imgGallery);

        findViewById(R.id.btNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSomeActivityForResult(true);
            }
        });

        findViewById(R.id.btPick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFromGallery.launch("image/*");
            }
        });
    }

    public void openSomeActivityForResult(boolean isNew) {
        Intent intent = new Intent(this, SomeActivity.class);
        if (isNew) {
            someActivityResultLauncher.launch(intent);
        } else {
            startActivityForResult(intent, 123);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 123) {
            if (data != null && data.getStringExtra(SomeActivity.KEY_NAME) != null)
                doSomeOperations(data);
        }
    }

    private void doSomeOperations(Intent data) {
        textView.setText(data.getStringExtra(SomeActivity.KEY_NAME));
    }

}