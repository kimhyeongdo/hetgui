package org.techtown.flashlight;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class help extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_help, container, false);

        Button button = rootView.findViewById(R.id.firebutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://safekorea.go.kr/idsiSFK/neo/main_m/sot/fire.html"));
                startActivity(myIntent);
            }
        });
        Button button2 = rootView.findViewById(R.id.earthquakebutton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://safekorea.go.kr/idsiSFK/neo/main_m/nat/earthquake.html"));
                startActivity(myIntent);
            }
        });
        Button button3 = rootView.findViewById(R.id.collapsebutton);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://safekorea.go.kr/idsiSFK/neo/main_m/sot/building.html"));
                startActivity(myIntent);
            }
        });

        Button button7 = rootView.findViewById(R.id.call);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:119"));
                startActivity(myIntent);
            }
        });

        return rootView;
    }
}