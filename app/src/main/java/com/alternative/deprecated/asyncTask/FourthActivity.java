package com.alternative.deprecated.asyncTask;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alternative.deprecated.R;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FourthActivity extends AppCompatActivity {

    private static final String imgUrlDownload = "https://images.pexels.com/photos/2486168/pexels-photo-2486168.jpeg";
    private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private static final int KEEP_ALIVE_TIME = 1000;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.MILLISECONDS;
    private int count = 0;

    //UI Init
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        mImageView = findViewById(R.id.imgUrl);

        Handler handler = new Handler(Looper.getMainLooper());
       // ExecutorService executorService = Executors.newFixedThreadPool(10); // to execute 10 different task in 10 different thread
        ExecutorService executorService = Executors.newSingleThreadExecutor(); // to execute a task in a single thread
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = fetchImage();
                if(bitmap != null) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mImageView.setImageBitmap(bitmap);
//                        }
//                    });
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mImageView.setImageBitmap(bitmap);
                            executorService.shutdown();
                        }
                    });
                }
            }
        });

        findViewById(R.id.singleButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                Executor mSingleThreadExecutor = Executors.newSingleThreadExecutor();

                for (int i = 0; i < 100; i++) {
                    mSingleThreadExecutor.execute(runnable);
                }
            }
        });

        findViewById(R.id.poolButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                ThreadPoolExecutor mThreadPoolExecutor = new ThreadPoolExecutor(
                        NUMBER_OF_CORES + 5,   // Initial pool size
                        NUMBER_OF_CORES + 8,   // Max pool size
                        KEEP_ALIVE_TIME,       // Time idle thread waits before terminating
                        KEEP_ALIVE_TIME_UNIT,  // Sets the Time Unit for KEEP_ALIVE_TIME
                        new LinkedBlockingDeque<Runnable>());  // Work Queue

                for (int i = 0; i < 100; i++) {
                    mThreadPoolExecutor.execute(runnable);
                }
            }
        });
    }

    private static Bitmap fetchImage() {
        Bitmap bitmapImg = null;
        try {
            InputStream inputStream = new URL(imgUrlDownload).openStream();
            bitmapImg = BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmapImg;
    }

    // This is the runnable task that we will run 100 times
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // Do some work that takes 50 milliseconds
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Update the UI with progress
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    count++;
                    String msg = count < 100 ? "working " : "done ";
                    updateStatus(msg + count);
                }
            });

        }
    };

    private void updateStatus(String msg) {
        ((TextView) findViewById(R.id.text)).setText(msg);
    }
}