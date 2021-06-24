package org.techtown.flashlight;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import androidx.annotation.Nullable;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {
    TabLayout tabLayout ;
    ViewPager2 pager2;
    Adapter adapter;
    private Context mContext;
    //블루투스
    public int major;
    public int minor;
    public String address;
    public String uuid="";
    private static final String TAG = "Beacontest";
    private BeaconManager beaconManager;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private List<Beacon> beaconList = new ArrayList<>();

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    public MediaPlayer mediaPlayer;
    private String url = "서버 ip 입력하는 곳";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer = MediaPlayer.create(this, R.raw.bgm);
        //블루투스
        //비콘 매니저 생성,
        beaconManager = BeaconManager.getInstanceForApplication(this);

        //비콘 매니저에서 layout 설정 'm:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25'
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        //beaconManager 설정 bind
        beaconManager.bind(this);

        //beacon 을 활용하려면 블루투스 권한획득(Andoird M버전 이상)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app requires location access" );
                builder.setMessage("Give this app permission to access locations to detect beacons.");
                builder.setPositiveButton(android.R.string.ok,null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        }
        //카메라
        int permssionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (permssionCheck!= PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this,"Permission approval is required",Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                Toast.makeText(this,"Camera permission is required to use the flash part.",Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
                Toast.makeText(this,"Camera permission is required to use the flash part.",Toast.LENGTH_LONG).show();

            }
        }
        // 카메라끝
        BluetoothAdapter   mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Intent intent;

        if (mBluetoothAdapter.isEnabled()) {
            // 블루투스 관련 실행 진행
        } else {
            // 블루투스 활성화 하도록
            intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1);
        }

        // 블루투스 끝
        tabLayout= findViewById(R.id.tabs);
        pager2 = findViewById(R.id.pager);
        FragmentManager fm =getSupportFragmentManager();
        adapter = new Adapter(fm, getLifecycle());
        pager2.setAdapter(adapter);
    //탭
        mContext = getApplicationContext();
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("Map",R.drawable.map)));;
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("Signal",R.drawable.sensor)));
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("Help",R.drawable.help)));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        //
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1: // requestCode 값 1
                if(resultCode==RESULT_OK){
                    // 블루투스 기능을 켰을 때
                }
                break;
        }
    }// onActivityResult()..
    //new블루투스끝
    //서버
    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private json json1;

   //     public NetworkTask(String url, ContentValues values) {
         public NetworkTask(String url, json json1) {
            this.url = url;
            this.json1 = json1;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, json1); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TextView location1 = (TextView)findViewById(R.id.location1);
            String target = "l";
            int target_num = s.indexOf(target);
            String setlocation = s.substring(target_num,(s.substring(target_num).indexOf(',')+target_num));
            setlocation = setlocation.replace("\"","");
            location1.setText(setlocation);
            Toast.makeText(getApplicationContext(), "현재위치는\n"+setlocation+"입니다.", Toast.LENGTH_SHORT).show();
        }
    }
    //
    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }
    String olduuid="";
    String newuuid="";
    //스레드
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            // 비콘이 감지되면 해당 함수가 호출된다. Collection<Beacon> beacons에는 감지된 비콘의 리스트가,
            // region에는 비콘들에 대응하는 Region 객체가 들어온다.
            public void didRangeBeaconsInRegion(final Collection<Beacon> beacons, Region region) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (beacons.size() > 0) {
                                    beaconList.clear();
                                    for (Beacon beacon : beacons) {
                                        if(beacon.getRssi() >= -75) {
                                            beaconList.add(beacon);
                                        }
                                    }
                                    for(Beacon beacon : beaconList){
                                        uuid=beacon.getId1().toString(); //beacon uuid
                                        major = beacon.getId2().toInt(); //beacon major
                                        minor = beacon.getId3().toInt();// beacon minor
                                        address = beacon.getBluetoothAddress();
                                    }
                                    newuuid = uuid.replace("-","");
                                    if(!olduuid.equals(newuuid)) {
                                        if(newuuid.equals("12ebd24abedd3090addb3a8d45cabd46")||newuuid.equals("3057ba80952a49eab4bbcef61570f7af")||newuuid.equals("50ebdb374a1a4de4bb42f44a91dafe37")||newuuid.equals("ba353fa2de4ba52fef43fbcdba34ff2d")){
                                            olduuid = newuuid;
                                            json json1 = new json(3, olduuid);
                                            NetworkTask networkTask = new NetworkTask(url, json1);
                                            networkTask.execute();
                                        }
                                    }
                                }

                            }
                        });
                    }
                }).start();
            }

        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Approximate location authorization granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functional Restrictions");
                    builder.setMessage("This app cannot retrieve beacons when in the background because location access is not granted.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }
//탭
private View createTabView(String tabName, int tabimage) {

    View tabView = LayoutInflater.from(mContext).inflate(R.layout.tabview, null);

    TextView tab_name = (TextView) tabView.findViewById(R.id.tabname);
    ImageView tab_view = (ImageView) tabView.findViewById(R.id.tabimage);
    tab_name.setText(tabName);
    tab_view.setImageResource(tabimage);

    return tabView;

}
//탭끝
}
