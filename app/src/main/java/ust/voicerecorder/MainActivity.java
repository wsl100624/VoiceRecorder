package ust.voicerecorder;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private Button recordingButton;
    private Button stopButton;
    private Button playButton;
    private Button uploadButton;
    private Button cameraButton;
    private TextView indicateLabel;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private MediaRecorder mediaRecorder;
    private String voiceStoragePath;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        voiceStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File audioVoice = new File(voiceStoragePath + File.separator + "voices");
        if(!audioVoice.exists()){
            audioVoice.mkdir();
        }
        voiceStoragePath = voiceStoragePath + File.separator + "voices/" + "audio" + ".3gp";
        System.out.println(voiceStoragePath);

        recordingButton = (Button)findViewById(R.id.startBtn);
        stopButton = (Button)findViewById(R.id.stopBtn);
        playButton = (Button)findViewById(R.id.playBtn);
        uploadButton = (Button)findViewById(R.id.uploadBtn);
        cameraButton = (Button)findViewById(R.id.cameraBtn);
        indicateLabel = (TextView)findViewById(R.id.indicateText);
        

        stopButton.setEnabled(false);
        playButton.setEnabled(false);
        uploadButton.setEnabled(false);
        indicateLabel.setEnabled(false);


        recordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaRecorder == null){
                    initializeMediaRecord();
                }

                if(indicateLabel != null) {
                    indicateLabel.setText("Waiting for Upload");
                    indicateLabel.setEnabled(false);
                    playButton.setEnabled(false);
                }
                startAudioRecording();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAudioRecording();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playLastStoredAudioMusic();
                releaseMediaPlayer();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                uploadToServer();
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openCamera();
            }
        });
        
    }

    private void openCamera() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void uploadToServer() {
        UploadToServer uploadIt = new UploadToServer();
        uploadIt.execute();
        uploadButton.setEnabled(false);
        indicateLabel.setText("Uploaded Successfully");
    }

    private void initializeMediaRecord(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(voiceStoragePath);
    }

    private void startAudioRecording(){
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        recordingButton.setEnabled(false);
        stopButton.setEnabled(true);

    }

    private void stopAudioRecording(){
        if(mediaRecorder != null){
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }

        recordingButton.setEnabled(true);
        stopButton.setEnabled(false);
        playButton.setEnabled(true);
        uploadButton.setEnabled(true);
        indicateLabel.setEnabled(true);

    }

    private void playLastStoredAudioMusic(){
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(voiceStoragePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();

        recordingButton.setEnabled(true);
        playButton.setEnabled(true);
        uploadButton.setEnabled(true);
    }

    private void stopAudioPlay(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void releaseMediaPlayer(){
        if(!mediaPlayer.isPlaying()){
            stopAudioPlay();
        }
    }






}

