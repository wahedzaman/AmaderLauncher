package com.durbinsoft.amarlauncher;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.service.notification.StatusBarNotification;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.reflect.Method;

public class AppDrawerActivity extends Activity {

    ApplicationPackage packages;
    CustomApplicationDrawerAdapter customDrawerAdapter;

    GridView appDrawerView;
    LinearLayout appTrayView, slideDrawerView, mainHomeView;
    ImageButton appDrawerHomeButton;

    private boolean isAppDrawerVisible = false;
    private boolean isAppTrayVisible = true;

    DisplayMetrics dmetrics = new DisplayMetrics();
    int widthPixels=dmetrics.widthPixels;
    int heightPixels=dmetrics.heightPixels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_drawer);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        packages = new ApplicationPackage(this);
        packages.initializePackages();

        customDrawerAdapter = new CustomApplicationDrawerAdapter(this, packages);

        appDrawerHomeButton = (ImageButton) findViewById(R.id.homeButtonAppDrawer);
        mainHomeView = (LinearLayout) findViewById(R.id.mainHomeView);
        slideDrawerView = (LinearLayout) findViewById(R.id.slideDrawer);
        appTrayView = (LinearLayout) findViewById(R.id.appTrayHolder);
        appDrawerView = (GridView) findViewById(R.id.appDrawerGridView);
        appDrawerView.setAdapter(customDrawerAdapter);

        appDrawerView.setOnItemClickListener(new AppDrawerClickListener(this, packages));
        appDrawerView.setVisibility(View.INVISIBLE);

        slideDrawerView.setVisibility(View.INVISIBLE);



        //below are the code to handle swipe gestures...

        mainHomeView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {

            }

            public void onSwipeRight() {
                getSliderDrawerInView(true);
            }

            public void onSwipeLeft() {
                getSliderDrawerInView(false);
            }

            public void onSwipeBottom() {
                //this should bring down the notification panel
            }

        });
        appDrawerHomeButton.setOnTouchListener(new OnSwipeTouchListener(this) {

            public void onSingleTap(){
                homeClicked();
            }

        });

        //below is registering our BroadcastReceiver to check any new package install or uninstall
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        registerReceiver(new PackageChangeBroadCastListener(),filter);
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
            homeClicked();
        }
        else if(isAppTrayVisible){
            getSliderDrawerInView(false);
        }
    }

    //this will open or close the app drawer.
    public void homeClicked(){
       if(isAppDrawerVisible){
           appDrawerView.animate()
                   .translationY(heightPixels)
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

    // This will hide or open the application tray (bottom)
    // ** Later to be used swipe...

    public void arrowClicked(){
        if(isAppTrayVisible){
            appTrayView.animate()
                    .translationX(widthPixels)
                    .alpha(0.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            appTrayView.setVisibility(View.INVISIBLE);
                        }
                    });
            isAppTrayVisible = false;
        }else{
            appTrayView.setVisibility(View.VISIBLE);
            appTrayView.setAlpha(0.0f);
            appTrayView.animate().translationX(0).alpha(1.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            appTrayView.setVisibility(View.INVISIBLE);
                        }
                    });
            isAppTrayVisible = true;
        }
    }

    private void getSliderDrawerInView(boolean direction){
        // direction true == in and false == out
        if(direction){
            mainHomeView.animate().translationX(slideDrawerView.getWidth());
            slideDrawerView.setVisibility(View.VISIBLE);
            slideDrawerView.setAlpha(0.5f);
            slideDrawerView.animate().translationX(widthPixels).alpha(1.0f);
        }else{
            mainHomeView.animate().translationX(0);
            slideDrawerView.animate().translationX(-slideDrawerView.getWidth()).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    slideDrawerView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public class PackageChangeBroadCastListener extends BroadcastReceiver{


        @Override
        public void onReceive(Context context, Intent intent) {
            packages = new ApplicationPackage(getApplicationContext());
            packages.initializePackages();

            customDrawerAdapter = new CustomApplicationDrawerAdapter(getApplicationContext(), packages);
            appDrawerView.setAdapter(customDrawerAdapter);
            appDrawerView.setOnItemClickListener(new AppDrawerClickListener(getApplicationContext(), packages));
        }
    }
}
