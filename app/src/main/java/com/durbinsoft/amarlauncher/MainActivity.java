package com.durbinsoft.amarlauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    PackageManager pm;
    Intent launchIntent;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pm = getPackageManager();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //do nothing
    }

    public void videoPlayer(View v){
        launchIntent = pm.getLaunchIntentForPackage("com.mxtech.videoplayer.ad");
        startActivity(launchIntent);
    }
    public void catchHint(View v){
        launchIntent = pm.getLaunchIntentForPackage("com.durbinsoft.cth");
        startActivity(launchIntent);
    }
    public void proyashClicked(View v){
        launchIntent = pm.getLaunchIntentForPackage("com.durbinsoft.proyash");
        startActivity(launchIntent);
    }
    public void musicClicked(View v){
        Toast.makeText(this,"COMPONENET NOT FOUND",Toast.LENGTH_LONG).show();
    }


    public void callBaba(View v){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:+8801914220333"));
        startActivity(intent);
    }
    public void callMa(View v){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:+8801914220333"));
        startActivity(intent);
    }
    public void callBhaia(View v){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:+8801914220333"));
        startActivity(intent);
    }
    public void callBhabi(View v){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:+8801914220333"));
        startActivity(intent);
    }
    public void sendMBaba(View v){
        Toast.makeText(this,"SENT MESSAGE TO BABA",Toast.LENGTH_LONG).show();
    }
    public void sendMMa(View v){
        Toast.makeText(this,"SENT MESSAGE TO MA",Toast.LENGTH_LONG).show();
    }
}
