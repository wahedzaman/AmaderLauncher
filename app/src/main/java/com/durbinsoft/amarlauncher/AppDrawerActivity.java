package com.durbinsoft.amarlauncher;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;


public class AppDrawerActivity extends Activity {

    ApplicationPackage packages;
    CustomApplicationDrawerAdapter customDrawerAdapter;

    GridView appDrawerView;
    LinearLayout appTrayView, slideDrawerView, mainHomeView;
    ImageButton appDrawerHomeButton, appDrawerBuuton1,appDrawerBuuton2,appDrawerBuuton3,appDrawerBuuton4;

    private boolean isAppDrawerVisible = false;
    private boolean isAppTrayVisible = true;

    DisplayMetrics dmetrics = new DisplayMetrics();
    int widthPixels=dmetrics.widthPixels;
    int heightPixels=dmetrics.heightPixels;

    private boolean appTrayFadeInOut, appTraySlideInOUt, appTrayZoomInOut,appTraySlideLeftRight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_drawer);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        appTrayFadeInOut = false;
        appTraySlideInOUt = true;

        packages = new ApplicationPackage(this);
        packages.initializePackages();

        customDrawerAdapter = new CustomApplicationDrawerAdapter(this, packages);

        appDrawerBuuton1 = (ImageButton) findViewById(R.id.appDrawerButton1);
        appDrawerBuuton2 = (ImageButton) findViewById(R.id.appDrawerButton2);
        appDrawerBuuton3 = (ImageButton) findViewById(R.id.appDrawerButton3);
        appDrawerBuuton4 = (ImageButton) findViewById(R.id.appDrawerButton4);

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

        slideDrawerView.setOnTouchListener(new OnSwipeTouchListener(this) {

            public void onSwipeLeft() {
                getSliderDrawerInView(false);
            }


        });
        appDrawerHomeButton.setOnTouchListener(new OnSwipeTouchListener(this) {

            public void onSingleTap(){
                homeClicked();
            }

        });

        // appDrawer application click and swipe handler
        appDrawerBuuton1.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                cameraClicked();
            }

            public void onSingleTap() {
               dialerClicked();
            }

        });
        appDrawerBuuton2.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                webClicked();
            }


            public void onSingleTap() {
                messageClicked();
            }

        });
        appDrawerBuuton3.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                dialerClicked();
            }


            public void onSingleTap() {
                webClicked();
            }

        });
        appDrawerBuuton4.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                messageClicked();
            }

            public void onSingleTap() {
                cameraClicked();
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

    public void dialerClicked(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        startActivity(intent);
    }

    public void messageClicked(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        //intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("vnd.android-dir/mms-sms");
        startActivity(intent);
    }

    public void webClicked(){
        PackageManager packagemanager = getPackageManager();
        Intent  launchIntent = packagemanager.getLaunchIntentForPackage("com.android.chrome");
        this.startActivity(launchIntent);
    }

    public void cameraClicked(){
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
       if(appTrayFadeInOut){
           animateFadeInOut();
       }
        else if(appTraySlideInOUt){
           animateSlideInOut();
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

    public void animateSlideInOut(){
        if(isAppDrawerVisible){
            appDrawerView.animate().translationY(mainHomeView.getHeight()).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    appDrawerView.setVisibility(View.VISIBLE);
                }
            });
            isAppDrawerVisible = false;
        }else{
            appDrawerView.setVisibility(View.VISIBLE);
            appDrawerView.setAlpha(0.5f);
            appDrawerView.animate().alpha(1.0f).translationY(heightPixels);
            isAppDrawerVisible = true;
        }
    }

    public void animateFadeInOut(){
        if(isAppDrawerVisible){
            appDrawerView.animate()
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
            appDrawerView.animate().alpha(1.0f)
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
