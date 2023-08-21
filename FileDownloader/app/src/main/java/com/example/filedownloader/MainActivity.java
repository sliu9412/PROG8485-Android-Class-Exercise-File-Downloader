package com.example.filedownloader;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.RatingBar;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.filedownloader.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RatingBar.OnRatingBarChangeListener, SeekBar.OnSeekBarChangeListener {

    ActivityMainBinding binding;
    int prograssVal = 0;
    Thread thread1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.skbarFileSize.setMax(50);
        binding.txtSizeSelected.setText(binding.skbarFileSize.getMin() + "GB");

        binding.rtApp.setNumStars(5);
        binding.rtApp.setRating(0);
        binding.txtSizeSelected.setText(String.valueOf(binding.rtApp.getRating()));

        binding.prgFileDownload.setProgress(0);

        SetListeners();
    }

    private void SetListeners() {
        binding.skbarFileSize.setOnSeekBarChangeListener(this);
        binding.rtApp.setOnRatingBarChangeListener(this);
        binding.btnDownload.setOnClickListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        binding.txtSizeSelected.setText(progress + "GB");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        binding.txtRatingSelected.setText(String.valueOf(rating));
    }

    @Override
    public void onClick(View v) {
        binding.prgFileDownload.setMax(binding.skbarFileSize.getProgress());
        initDownload();
    }

    private void initDownload() {
        String uri = "https://drive.google.com/uc?export=download&id=1F-1614HBqXaHFSTGX5aWtp9YGkeZMK1R";
        downloadFile(getApplicationContext(), "welcome", "pdf", uri);
    }

    private void downloadFile(Context applicationContext, String fileName, String extension, String url) {
        DownloadManager downloadManager = (DownloadManager) applicationContext.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(applicationContext, Environment.DIRECTORY_DOWNLOADS, fileName + extension);
        assert downloadManager != null;
        downloadManager.enqueue(request);
        loadProgressBar(prograssVal);
        Snackbar.make(binding.lytDownload, "Downloading...", Snackbar.LENGTH_LONG).show();
    }

    private void loadProgressBar(int prograssValue) {
        binding.prgFileDownload.setProgress(prograssVal);
        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    if (prograssValue < binding.skbarFileSize.getProgress()) {
                        loadProgressBar(prograssValue + 10);
                    } else {
                        thread1.interrupt();
                    }
                } catch (InterruptedException e) {

                }
            }
        });
        thread1.start();
    }
}