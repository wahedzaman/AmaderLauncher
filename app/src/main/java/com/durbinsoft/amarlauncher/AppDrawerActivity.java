package com.durbinsoft.amarlauncher;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.Toast;

public class AppDrawerActivity extends Activity {

    ApplicationPackage packages;
    CustomApplicationDrawerAdapter customDrawerAdapter;

    GridView appDrawerView;
    private boolean isAppDrawerVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_drawer);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        packages = new ApplicationPackage(this);
        packages.initializePackages();

        customDrawerAdapter = new CustomApplicationDrawerAdapter(this, packages);

        appDrawerView = (GridView) findViewById(R.id.appDrawerGridView);
        appDrawerView.setAdapter(customDrawerAdapter);

        appDrawerView.setOnItemClickListener(new AppDrawerClickListener(this, packages));
        appDrawerView.setVisibility(View.INVISIBLE);
    }

    public void dialerClicked(View v){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        startActivity(intent);
    }

    public void messageClicked(View v){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        //intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("vnd.android-dir/mms-sms");
        startActivity(intent);
    }

    public void webClicked(View v){
        PackageManager packagemanager = getPackageManager();
        Intent  launchIntent = packagemanager.getLaunchIntentForPackage("com.android.chrome");
        this.startActivity(launchIntent);
    }

    public void cameraClicked(View v){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        if(isAppDrawerVisible){
            appDrawerView.setVisibility(View.INVISIBLE);;
            isAppDrawerVisible = false;
        }
    }

    //this will open or close the app drawer.
    public void homeClicked(View v){
       if(isAppDrawerVisible){
           appDrawerView.animate()
                   .translationY(v.getHeight())
                   .alpha(0.0f)
                   .setListener(new AnimatorListenerAdapter() {
                       @Override
                       public void onAnimationEnd(Animator animation) {
                           super.onAnimationEnd(animation);
                           appDrawerView.setVisibility(View.INVISIBLE);
                       }
                   });
           isAppDrawerVisible = false;
       }else{
           appDrawerView.setVisibility(View.VISIBLE);
           appDrawerView.setAlpha(0.0f);
           appDrawerView.animate().translationY(0).alpha(1.0f)
                   .setListener(new AnimatorListenerAdapter() {
                       @Override
                       public void onAnimationEnd(Animator animation) {
                           super.onAnimationEnd(animation);
                           appDrawerView.setVisibility(View.VISIBLE);
                       }
                   });;
           isAppDrawerVisible = true;
       }
    }
}
