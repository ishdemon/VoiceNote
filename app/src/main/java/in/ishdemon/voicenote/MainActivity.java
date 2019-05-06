package in.ishdemon.voicenote;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.devlomi.record_view.OnBasketAnimationEnd;
import com.devlomi.record_view.OnRecordClickListener;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public static final int RECORD_AUDIO = 0;

    private MediaRecorder myAudioRecorder;

    private String output = null;
    String RecentPath = null;
    public static final String DOWNLOAD_PATH = "/Voice Notes/";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private Button start, stop, play;

    private boolean permissionToRecordAccepted = false;

    private boolean permissionToWriteAccepted = false;

    private String[] permissions = {"android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        output = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Voice Notes/";
        File file = new File(output);
        if (!file.exists()) {
            file.mkdir();
        }
        setContentView(R.layout.activity_main);
        int requestCode = 200;
        RecordView recordView = (RecordView) findViewById(R.id.record_view);
        final RecordButton recordButton = (RecordButton) findViewById(R.id.record_button);
        recordButton.setRecordView(recordView);

        // if you want to click the button (in case if you want to make the record button a Send Button for example..)
//        recordButton.setListenForRecord(false);

        //ListenForRecord must be false ,otherwise onClick will not be called
        recordButton.setOnRecordClickListener(new OnRecordClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "RECORD BUTTON CLICKED", Toast.LENGTH_SHORT).show();
                Log.d("RecordButton", "RECORD BUTTON CLICKED");
            }
        });


        //Cancel Bounds is when the Slide To Cancel text gets before the timer . default is 8
        recordView.setCancelBounds(8);
        recordView.setSmallMicColor(Color.parseColor("#c2185b"));
        //prevent recording under one Second
        recordView.setLessThanSecondAllowed(false);
        recordView.setSlideToCancelText("Slide To Cancel");
        recordView.setCustomSounds(R.raw.record_start, R.raw.record_finished, 0);


        recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                Log.d("RecordView", "onStart");
                startRecording();
                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "onCancel", Toast.LENGTH_SHORT).show();
                File file = new File(RecentPath);
                file.delete();
                Log.d("RecordView", "onCancel");

            }

            @Override
            public void onFinish(long recordTime) {
                stopRecording();
                String time = getHumanTimeText(recordTime);
                Toast.makeText(MainActivity.this, "onFinishRecord - Recorded Time is: " + time, Toast.LENGTH_SHORT).show();
                Log.d("RecordView", "onFinish");
                Log.d("RecordTime", time);
            }

            @Override
            public void onLessThanSecond() {
                Toast.makeText(MainActivity.this, "OnLessThanSecond", Toast.LENGTH_SHORT).show();
                File file = new File(RecentPath);
                file.delete();
                Log.d("RecordView", "onLessThanSecond");
            }
        });


        recordView.setOnBasketAnimationEndListener(new OnBasketAnimationEnd() {
            @Override
            public void onAnimationEnd() {
                Log.d("RecordView", "Basket Animation Finished");
            }
        });


    }

    private String getUniqueImageFileName() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return timeStamp + ".aac";
    }

    private void startRecording() {
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        RecentPath = output + getUniqueImageFileName();

        Log.wtf("path", RecentPath);
        myAudioRecorder.setOutputFile(RecentPath);
        myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            myAudioRecorder.prepare();
        } catch (IOException e) {
            Log.e("voicenotecrash", "prepare() failed" + e.getLocalizedMessage());
        }

        myAudioRecorder.start();
    }

    private void stopRecording() {
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;
    }

    private ArrayList<String> FetchImages(String path) {

        ArrayList<String> filenames = new ArrayList<String>();
        File directory = new File(path);
        File[] files = directory.listFiles();

        for (int i = 0; i < files.length; i++)
        {
            //files[i].getTotalSpace()

            String file_name = files[i].getName();
            // you can store name to arraylist and use it later
            filenames.add(file_name);
        }
        return filenames;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,

                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case 200:

                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                permissionToWriteAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                break;

        }
        if (!permissionToRecordAccepted) finish();

        if (!permissionToWriteAccepted) finish();

    }


    public void stop(View view) {

        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;
        stop.setEnabled(false);
        play.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_SHORT).show();

    }

    public void play(View view) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException {
        MediaPlayer m = new MediaPlayer();
        m.setDataSource(output);
        m.prepare();
        m.start();
        Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_SHORT).show();
    }


    private String getHumanTimeText(long milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

}
