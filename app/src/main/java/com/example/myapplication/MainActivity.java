package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final byte REQUEST_PERMISSION_CODE = 0;
    // Declare variables
    Button btnRecord,btnStopRecord, btnPlay, btnStopPlay;
    String pathFile ="";
    MediaRecorder mr;
    MediaPlayer mp;
    boolean mRecording = false, mPlaying = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pathFile = getExternalCacheDir().getAbsolutePath();
        pathFile +="/AudioRecording.3gp";
        Toast.makeText(MainActivity.this,pathFile,Toast.LENGTH_LONG).show();

        // request rutime permission
        if (checkMyPermission())
            requestMyPermission();

        // Init View
        btnPlay = (Button) findViewById(R.id.btnStartPlay);
        btnRecord = (Button) findViewById(R.id.btnStartRecord);
//        btnStopPlay = (Button) findViewById(R.id.btnStopPlay);
//        btnStopRecord = (Button) findViewById(R.id.btnStopRecord);
        // Init Action
        // Record;
        btnRecord.setOnClickListener((view)-> {
            if (mRecording==false)
            {
                if (checkMyPermission())
                {
                    try
                    {
                        //                 Toast.makeText(MainActivity.this,"Recording...Error0",Toast.LENGTH_SHORT).show();
                        mr = new MediaRecorder();
                        mr.setAudioSource(MediaRecorder.AudioSource.MIC);
                        mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        mr.setOutputFile(pathFile);
                        mr.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    btnRecord.setText("Stop Recording");
                    btnRecord.setEnabled(true);
                    btnPlay.setEnabled(false);
                    mr.start();
                    mRecording=true;
                }
                else
                {
                    requestMyPermission();
                }
            }
            else
            {
                mr.stop();
                btnRecord.setText("Record");
                btnRecord.setEnabled(true);
                btnPlay.setEnabled(true);
                mRecording=false;
            }

        });
//        btnStopRecord.setOnClickListener((view)-> {
//            mr.stop();
//            btnStopPlay.setEnabled(false);
//            btnRecord.setEnabled(true);
//            btnPlay.setEnabled(true);
//            btnStopRecord.setEnabled(false);
//        });
        btnPlay.setOnClickListener((view)-> {
            if (mPlaying==false) {
                btnRecord.setEnabled(false);
                btnPlay.setEnabled(true);
                btnPlay.setText("Stop Playing");
//            btnStopPlay.setEnabled(true);
//            btnStopRecord.setEnabled(false);
                mp = new MediaPlayer();
                try {
                    mp.setDataSource(pathFile);
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.start();
                mPlaying=true;
                Toast.makeText(MainActivity.this, "Playing...", Toast.LENGTH_SHORT).show();
            }
            else
            {
                if (mp!=null)
                {
                    mp.stop();
                    mp.release();
                }
                btnRecord.setEnabled(true);
                btnPlay.setText("Play");
                btnPlay.setEnabled(true);
                mPlaying=false;

            }
        });
//        btnStopPlay.setOnClickListener((view)->{
//            if (mp!=null)
//            {
//                mp.stop();
//                mp.release();
//                setupMediaRecorder();
//            }
//            btnStopRecord.setEnabled(false);
//            btnStopPlay.setEnabled(false);
//            btnRecord.setEnabled(true);
//            btnPlay.setEnabled(true);
//        });
    }

//    private void setupMediaRecorder() {
//       // mr = new MediaRecorder();
//       // mr.setAudioSource((MediaRecorder.AudioSource.MIC));
//       // mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//       // mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//       // mr.setOutputFile(pathSave);
//    }

    private boolean checkMyPermission() {
        boolean write_external_storage_result = ContextCompat.checkSelfPermission
                (this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
        boolean record_audio_result = ContextCompat.checkSelfPermission
                (this,Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED;
        return (write_external_storage_result&& record_audio_result);
    }
    private void requestMyPermission() {
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);
    }

    // Press CTRL+O
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_PERMISSION_CODE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }
}