package org.techtown.flashlight;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class sensor extends Fragment {
    private static boolean FlashOn = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sensor, container, false);
        Button button4 = rootView.findViewById(R.id.btn_flash_on2);
        final TextView textView1 = rootView.findViewById(R.id.flash);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FlashOn) {
                    FlashOn = false;
                    UtilFlash.flash_off();
                    textView1.setText("Flashlight");
                }else{
                    FlashOn = true;
                    UtilFlash.flash_on();
                    textView1.setText("Flashlight Off");
                }
            }
        });
        final Button button6 = rootView.findViewById(R.id.sound_btn);
        final TextView textView2 = rootView.findViewById(R.id.sound);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MainActivity activity = (MainActivity) getActivity();
                if(activity.mediaPlayer != null &&  activity.mediaPlayer.isPlaying())
                {
                    activity.mediaPlayer.stop();
                    activity.mediaPlayer.release();
                    activity.mediaPlayer = null;
                    textView2.setText("Siren");
                }
                else // 미디어 리소스를 생성하고 플레이 시킨다.
                {
                    activity.mediaPlayer = MediaPlayer.create(activity, R.raw.bgm);
                    activity.mediaPlayer.start();
                    textView2.setText("Siren Off");
                }
            }
        });
        return rootView;
    }
}