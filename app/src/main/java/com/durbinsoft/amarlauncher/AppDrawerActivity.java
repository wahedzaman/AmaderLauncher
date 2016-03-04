package com.durbinsoft.amarlauncher;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.UiThread;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;


public class AppDrawerActivity extends Activity implements View.OnClickListener{

    ApplicationPackage packages;
    CustomApplicationDrawerAdapter customDrawerAdapter;
    AppDrawerClickListener appDrawerClickListener;

    GridView appDrawerView;
    LinearLayout appTrayView, slideDrawerView, mainHomeView, bottomDrawerView, appdrawerLongpressDetails;
    ImageButton appDrawerHomeButton, appDrawerBuuton1,appDrawerBuuton2,appDrawerBuuton3,appDrawerBuuton4,bottomDrawerbutton1,bottomDrawerbutton2,bottomDrawerbutton3,bottomDrawerbutton4,bottomDrawerbutton5,bottomDrawerbutton6,bottomDrawerbutton7,bottomDrawerbutton8,bottomDrawerbutton9,bottomDrawerbutton10;

    private boolean isAppDrawerVisible = false;
    private boolean isAppTrayVisible = false;
    private boolean isBottomDrawerVisible = false;
    private boolean isAppDrawerLongpressDetailsVisible = false;

    DisplayMetrics dmetrics = new DisplayMetrics();

    int widthPixels=dmetrics.widthPixels;
    int heightPixels=dmetrics.heightPixels;

    private boolean appTrayFadeInOut, appTraySlideInOUt, appTrayZoomInOut,appTraySlideLeftRight;

    private boolean wifiState,bluetoothState,rotationState, airplaneState, lightState, isFlashOn;

    private WifiManager wifiManager;
    private BluetoothAdapter bluetoothAdapter;

    private SeekBar brightnessBar;
    private ImageView airplaneToggle,wifiToggle,bluetoothToggle,rotationToggle,lightToggle;

    private TextView leftDrawerClockTimeText,leftDrawerClockDateText,leftDrawerNameText;

    //these textview below, acts like button
    private TextView appDeleteTv,appDetailsTv,appHideTv;

    private CalendarView calendar;
    private PreferenceClassForData sPrefs;

    private String selectedAppPackageName = "";

    private Camera camera;
    Camera.Parameters params;


    private boolean initialSetup;
    PackageManager packagemanager;

    CalenderConversion myCalenderConversion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_drawer);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);




        appTrayFadeInOut = false;
        appTraySlideInOUt = true;



        initiateView();
        setAllAdapterAndEverything();


        if(initialSetup){
            Intent i = new Intent(this,AppChooserApplication.class);
            startActivity(i);
        }
    }


    private void initiateView(){
        airplaneToggle = (ImageView)findViewById(R.id.airplaneToggle);
        wifiToggle = (ImageView)findViewById(R.id.wifiToggle);
        bluetoothToggle = (ImageView)findViewById(R.id.bluetoothToggle);
        rotationToggle = (ImageView)findViewById(R.id.rotationToggle);
        lightToggle = (ImageView)findViewById(R.id.lightToggle);

        leftDrawerClockDateText = (TextView)findViewById(R.id.leftclockDateTv);
        leftDrawerClockTimeText = (TextView)findViewById(R.id.leftclockTimeTv);
        leftDrawerNameText = (TextView) findViewById(R.id.leftDrawerNameText);


        appDeleteTv = (TextView) findViewById(R.id.appUnistalltv);
       //appDetailsTv = (TextView) findViewById(R.id.appDetailstv);
       // appHideTv = (TextView) findViewById(R.id.appHidetv);


        appDrawerBuuton1 = (ImageButton) findViewById(R.id.appDrawerButton1);
        appDrawerBuuton2 = (ImageButton) findViewById(R.id.appDrawerButton2);
        appDrawerBuuton3 = (ImageButton) findViewById(R.id.appDrawerButton3);
        appDrawerBuuton4 = (ImageButton) findViewById(R.id.appDrawerButton4);

        bottomDrawerbutton1 = (ImageButton)findViewById(R.id.bottomDrawerApp1);
        bottomDrawerbutton2 = (ImageButton)findViewById(R.id.bottomDrawerApp2);
        bottomDrawerbutton3 = (ImageButton)findViewById(R.id.bottomDrawerApp3);
        bottomDrawerbutton4 = (ImageButton)findViewById(R.id.bottomDrawerApp4);
        bottomDrawerbutton5 = (ImageButton)findViewById(R.id.bottomDrawerApp5);
        bottomDrawerbutton6 = (ImageButton)findViewById(R.id.bottomDrawerApp6);
        bottomDrawerbutton7 = (ImageButton)findViewById(R.id.bottomDrawerApp7);
        bottomDrawerbutton8 = (ImageButton)findViewById(R.id.bottomDrawerApp8);
        bottomDrawerbutton9 = (ImageButton)findViewById(R.id.bottomDrawerApp9);
        bottomDrawerbutton10 = (ImageButton)findViewById(R.id.bottomDrawerApp10);

        brightnessBar = (SeekBar) findViewById(R.id.bottomDrawerAppBrightnessSlider);

        calendar = (CalendarView) findViewById(R.id.calendar);

        appDrawerHomeButton = (ImageButton) findViewById(R.id.homeButtonAppDrawer);
        bottomDrawerView = (LinearLayout) findViewById(R.id.bottomDrawerViewHolder);
        mainHomeView = (LinearLayout) findViewById(R.id.mainHomeView);
        slideDrawerView = (LinearLayout) findViewById(R.id.slideDrawer);
        appTrayView = (LinearLayout) findViewById(R.id.appTrayHolder);
        appdrawerLongpressDetails = (LinearLayout) findViewById(R.id.appdrawerLongPressDetailsLayout);
        appDrawerView = (GridView) findViewById(R.id.appDrawerGridView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialSetup = sPrefs.getBool();
        if(initialSetup == false){
            setBottomDrawerApps();
            setAllAdapterAndEverything();
        }
    }

    private void setAllAdapterAndEverything(){

        packagemanager = getPackageManager();
        sPrefs = new PreferenceClassForData(this);
        sPrefs.initializeSharedPrefs();
        initialSetup = sPrefs.getBool();

        myCalenderConversion = new CalenderConversion();

        packages = new ApplicationPackage(this);
        packages.initializePackages();

        customDrawerAdapter = new CustomApplicationDrawerAdapter(this, packages);

        airplaneToggle.setOnClickListener(this);
        wifiToggle.setOnClickListener(this);
        bluetoothToggle.setOnClickListener(this);
        rotationToggle.setOnClickListener(this);
        lightToggle.setOnClickListener(this);


        appDrawerView.setAdapter(customDrawerAdapter);

        appDrawerView.setTextFilterEnabled(true);
        appDrawerClickListener = new AppDrawerClickListener(this, packages,appdrawerLongpressDetails,appDrawerView);

        appDrawerView.setOnItemClickListener(new AppDrawerClickListener(this, packages));
        appDrawerView.setOnItemLongClickListener(appDrawerClickListener);

        //Drawable d = getResources().getDrawable(R.drawable.blueblurbg);
        //  d.setAlpha(200);
        //  bottomDrawerView.setBackground(d);

        //  Bitmap tmpImg = BitmapFactory.decodeResource(getResources(),R.drawable.bluetoothblack);
        //   BitmapDrawable draw = new BitmapDrawable(Bitmap.createScaledBitmap(tmpImg, 16, 16, false));
        //   airplaneToggleButton.setBackground(draw);





        if(isBottomDrawerVisible){
            //do nothing
        }else{
            bottomDrawerView.setVisibility(View.INVISIBLE);
        }
        if(isAppDrawerVisible){
            //do nothing
        }else{
            appDrawerView.setVisibility(View.INVISIBLE);
        }
        if(isAppTrayVisible){
            //do nothing
        }else{
            slideDrawerView.setVisibility(View.INVISIBLE);
        }if(isAppDrawerLongpressDetailsVisible){
            //do nothing
        }else{
            appdrawerLongpressDetails.setVisibility(View.INVISIBLE);
        }



        //below code handles appdelete,apphide and appdetails
        appDeleteTv.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSingleTap(){
                Intent intent = new Intent(Intent.ACTION_DELETE, Uri.fromParts("package", appDrawerClickListener.getPressedAppName(), null));
                startActivity(intent);
                appDrawerClickListener.resetVisibilityAndOther();
            }

        });
        /*
        appDetailsTv.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSingleTap() {
                appDrawerClickListener.resetVisibilityAndOther();
            }

        });
        appHideTv.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSingleTap(){
                sPrefs.setHiddenApps(appDrawerClickListener.getPressedAppName());
                appDrawerClickListener.resetVisibilityAndOther();
                setAllAdapterAndEverything();
            }

        });

*/


        //below are the code to handle swipe gestures...

        mainHomeView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                checkSystemStatus();
                getBottomDrawerInView();
            }

            public void onSwipeRight() {
                getSliderDrawerInView();
            }

            public void onSwipeLeft() {
                if(isAppTrayVisible){
                    getSliderDrawerInView();
                }
            }

            public void onSwipeBottom() {
                //this should bring down the notification panel
            }

        });
        bottomDrawerView.setOnTouchListener(new OnSwipeTouchListener(this) {

            public void onSwipeBottom() {
                getBottomDrawerInView();
            }


        });

        slideDrawerView.setOnTouchListener(new OnSwipeTouchListener(this) {

            public void onSwipeLeft() {
                getSliderDrawerInView();
            }


        });
        appDrawerHomeButton.setOnTouchListener(new OnSwipeTouchListener(this) {

            public void onSingleTap() {
                homeClicked();
            }

            public void onSwipeTop() {
                getBottomDrawerInView();
            }

        });



        // appDrawer application click and swipe handler
        //add a on/off check from prefs and check whether configured or not,if not configured than open app chooser,.....
        appDrawerBuuton1.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                cameraClicked();
            }

            public void onSingleTap() {
                Intent launchIntent = packagemanager.getLaunchIntentForPackage(sPrefs.getSelectedApp(1));
                startActivity(launchIntent);
            }

            public void onLongPressDown() {
                getApplicationListForBottomDrawer(appDrawerBuuton1, sPrefs.SP_APP1);
            }

        });
        appDrawerBuuton2.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                webClicked();
            }

            public void onLongPressDown(){
                getApplicationListForBottomDrawer(appDrawerBuuton2, sPrefs.SP_APP2);
            }


            public void onSingleTap() {
                Intent  launchIntent = packagemanager.getLaunchIntentForPackage(sPrefs.getSelectedApp(2));
                startActivity(launchIntent);
            }

        });
        appDrawerBuuton3.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                dialerClicked();
            }

            public void onLongPressDown(){
                getApplicationListForBottomDrawer(appDrawerBuuton3, sPrefs.SP_APP3);
            }


            public void onSingleTap() {
                Intent  launchIntent = packagemanager.getLaunchIntentForPackage(sPrefs.getSelectedApp(3));
                startActivity(launchIntent);
            }

        });
        appDrawerBuuton4.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                messageClicked();
            }

            public void onLongPressDown(){
                getApplicationListForBottomDrawer(appDrawerBuuton4, sPrefs.SP_APP4);
            }

            public void onSingleTap() {
                Intent  launchIntent = packagemanager.getLaunchIntentForPackage(sPrefs.getSelectedApp(4));
                startActivity(launchIntent);
            }

        });


        //bottom drawer favourite app click listener
        //add a check if it is initialize or not.

        bottomDrawerbutton1.setOnTouchListener(new OnSwipeTouchListener(this) {
           public void onLongPressDown() {
                getApplicationListForBottomDrawer(bottomDrawerbutton1, sPrefs.SP_APP5);
            }

            public void onSingleTap() {
                Intent launchIntent = packagemanager.getLaunchIntentForPackage(sPrefs.getSelectedApp(5));
                startActivity(launchIntent);
            }

        });
        bottomDrawerbutton2.setOnTouchListener(new OnSwipeTouchListener(this) {
           public void onLongPressDown() {
                getApplicationListForBottomDrawer(bottomDrawerbutton2, sPrefs.SP_APP6);
            }

            public void onSingleTap() {
                Intent launchIntent = packagemanager.getLaunchIntentForPackage(sPrefs.getSelectedApp(6));
                startActivity(launchIntent);
            }

        });
        bottomDrawerbutton3.setOnTouchListener(new OnSwipeTouchListener(this) {
           public void onLongPressDown() {
                getApplicationListForBottomDrawer(bottomDrawerbutton3, sPrefs.SP_APP7);
            }

            public void onSingleTap() {
                Intent launchIntent = packagemanager.getLaunchIntentForPackage(sPrefs.getSelectedApp(7));
                startActivity(launchIntent);
            }

        });
        bottomDrawerbutton4.setOnTouchListener(new OnSwipeTouchListener(this) {
           public void onLongPressDown() {
                getApplicationListForBottomDrawer(bottomDrawerbutton4, sPrefs.SP_APP8);
            }

            public void onSingleTap() {
                Intent launchIntent = packagemanager.getLaunchIntentForPackage(sPrefs.getSelectedApp(8));
                startActivity(launchIntent);
            }

        });
        bottomDrawerbutton5.setOnTouchListener(new OnSwipeTouchListener(this) {
           public void onLongPressDown() {
                getApplicationListForBottomDrawer(bottomDrawerbutton5, sPrefs.SP_APP9);
            }

            public void onSingleTap() {
                Intent launchIntent = packagemanager.getLaunchIntentForPackage(sPrefs.getSelectedApp(9));
                startActivity(launchIntent);
            }

        });
        bottomDrawerbutton6.setOnTouchListener(new OnSwipeTouchListener(this) {
           public void onLongPressDown() {
                getApplicationListForBottomDrawer(bottomDrawerbutton6, sPrefs.SP_APP10);
            }

            public void onSingleTap() {
                Intent launchIntent = packagemanager.getLaunchIntentForPackage(sPrefs.getSelectedApp(10));
                startActivity(launchIntent);
            }

        });
        bottomDrawerbutton7.setOnTouchListener(new OnSwipeTouchListener(this) {
           public void onLongPressDown() {
                getApplicationListForBottomDrawer(bottomDrawerbutton7, sPrefs.SP_APP11);
            }

            public void onSingleTap() {
                Intent launchIntent = packagemanager.getLaunchIntentForPackage(sPrefs.getSelectedApp(11));
                startActivity(launchIntent);
            }

        });
        bottomDrawerbutton8.setOnTouchListener(new OnSwipeTouchListener(this) {
           public void onLongPressDown() {
                getApplicationListForBottomDrawer(bottomDrawerbutton8, sPrefs.SP_APP12);
            }

            public void onSingleTap() {
                Intent launchIntent = packagemanager.getLaunchIntentForPackage(sPrefs.getSelectedApp(12));
                startActivity(launchIntent);
            }

        });
        bottomDrawerbutton9.setOnTouchListener(new OnSwipeTouchListener(this) {
           public void onLongPressDown() {
                getApplicationListForBottomDrawer(bottomDrawerbutton9, sPrefs.SP_APP13);
            }

            public void onSingleTap() {
                Intent launchIntent = packagemanager.getLaunchIntentForPackage(sPrefs.getSelectedApp(13));
                startActivity(launchIntent);
            }

        });
        bottomDrawerbutton10.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onLongPressDown() {
                getApplicationListForBottomDrawer(bottomDrawerbutton10, sPrefs.SP_APP14);
            }

            public void onSingleTap() {
                Intent launchIntent = packagemanager.getLaunchIntentForPackage(sPrefs.getSelectedApp(14));
                startActivity(launchIntent);
            }

        });

        //below is registering our BroadcastReceiver to check any new package install or uninstall
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        registerReceiver(new PackageChangeBroadCastListener(), filter);

        brightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int seekBarProgress = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarProgress = progress;

            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                changeBrightness(seekBarProgress);
            }

        });
    }

    private void setChangedApplicationToButton(ImageButton imageButton, String packageName, String appPositionName){ // stage reffers to the button of favourite button or appbar button. 0 or 1
        //update sharedpreferences, update icon, update packageName link
        imageButton.setImageDrawable(packages.getIcon(packages.searchAndReturnPackage(packageName)));
        sPrefs.setApps(packageName,appPositionName);
    }

    private void setBottomDrawerApps(){
        String [] items = new String[14];
        int [] itemsIcon = new int[14];

        items[0] = sPrefs.getSelectedApp(1);
        items[1] = sPrefs.getSelectedApp(2);
        items[2] = sPrefs.getSelectedApp(3);
        items[3] = sPrefs.getSelectedApp(4);
        items[4] = sPrefs.getSelectedApp(5);
        items[5] = sPrefs.getSelectedApp(6);
        items[6] = sPrefs.getSelectedApp(7);
        items[7] = sPrefs.getSelectedApp(8);
        items[8] = sPrefs.getSelectedApp(9);
        items[9] = sPrefs.getSelectedApp(10);
        items[10] = sPrefs.getSelectedApp(11);
        items[11] = sPrefs.getSelectedApp(12);
        items[12] = sPrefs.getSelectedApp(13);
        items[13] = sPrefs.getSelectedApp(14);

        itemsIcon[0] = packages.searchAndReturnPackage(items[0]);
        itemsIcon[1] = packages.searchAndReturnPackage(items[1]);
        itemsIcon[2] = packages.searchAndReturnPackage(items[2]);
        itemsIcon[3] = packages.searchAndReturnPackage(items[3]);

        itemsIcon[4] = packages.searchAndReturnPackage(items[4]);
        itemsIcon[5] = packages.searchAndReturnPackage(items[5]);
        itemsIcon[6] = packages.searchAndReturnPackage(items[6]);
        itemsIcon[7] = packages.searchAndReturnPackage(items[7]);
        itemsIcon[8] = packages.searchAndReturnPackage(items[8]);
        itemsIcon[9] = packages.searchAndReturnPackage(items[9]);
        itemsIcon[10] = packages.searchAndReturnPackage(items[10]);
        itemsIcon[11] = packages.searchAndReturnPackage(items[11]);
        itemsIcon[12] = packages.searchAndReturnPackage(items[12]);
        itemsIcon[13] = packages.searchAndReturnPackage(items[13]);

        appDrawerBuuton1.setImageDrawable(packages.getIcon(itemsIcon[0]));
        appDrawerBuuton2.setImageDrawable(packages.getIcon(itemsIcon[1]));
        appDrawerBuuton3.setImageDrawable(packages.getIcon(itemsIcon[2]));
        appDrawerBuuton4.setImageDrawable(packages.getIcon(itemsIcon[3]));

        bottomDrawerbutton1.setImageDrawable(packages.getIcon(itemsIcon[4]));
        bottomDrawerbutton2.setImageDrawable(packages.getIcon(itemsIcon[5]));
        bottomDrawerbutton3.setImageDrawable(packages.getIcon(itemsIcon[6]));
        bottomDrawerbutton4.setImageDrawable(packages.getIcon(itemsIcon[7]));
        bottomDrawerbutton5.setImageDrawable(packages.getIcon(itemsIcon[8]));
        bottomDrawerbutton6.setImageDrawable(packages.getIcon(itemsIcon[9]));
        bottomDrawerbutton7.setImageDrawable(packages.getIcon(itemsIcon[10]));
        bottomDrawerbutton8.setImageDrawable(packages.getIcon(itemsIcon[11]));
        bottomDrawerbutton9.setImageDrawable(packages.getIcon(itemsIcon[12]));
        bottomDrawerbutton10.setImageDrawable(packages.getIcon(itemsIcon[13]));
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
        Intent launchIntent = packagemanager.getLaunchIntentForPackage("com.android.chrome");
        this.startActivity(launchIntent);
    }

    public void cameraClicked(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        if(appDrawerClickListener.getAppDetailsMenuVisibility()){
            appDrawerClickListener.getDetailsMenuInView();
        }else if(isBottomDrawerVisible){
            getBottomDrawerInView();
        }
         else if(isAppTrayVisible){
            getSliderDrawerInView();
        }
         else if(isAppDrawerVisible){
            homeClicked();
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

    private void getSliderDrawerInView(){

        // direction true == in and false == out
        if(!isAppTrayVisible){

            ScrollView sv = (ScrollView)findViewById(R.id.scrl);
            sv.scrollTo(sv.getBottom(), 0);

            Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/customfont.ttf");

            leftDrawerNameText.setTypeface(custom_font);
           // sv.fullScroll(View.FOCUS_UP);
            leftDrawerClockTimeText.setText(myCalenderConversion.returnConvertedTime());
            leftDrawerClockDateText.setText(myCalenderConversion.retunConvertedDate());
            mainHomeView.animate().translationX(slideDrawerView.getWidth());
            slideDrawerView.setVisibility(View.VISIBLE);
            slideDrawerView.setAlpha(0.0f);
            slideDrawerView.animate().translationX(widthPixels).alpha(1.0f);
            isAppTrayVisible = true;
        }else{
            mainHomeView.animate().translationX(0);
            slideDrawerView.animate().translationX(-slideDrawerView.getWidth()).alpha(0.0f).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    slideDrawerView.setVisibility(View.VISIBLE);
                }
            });
            isAppTrayVisible = false;
        }
    }

    private void getBottomDrawerInView(){
        // direction true == in and false == out
        if(isBottomDrawerVisible == false){
            initializeCalendar();
          //  mainHomeView.animate().translationX(slideDrawerView.getWidth());
            bottomDrawerView.setVisibility(View.VISIBLE);
            bottomDrawerView.setAlpha(0.0f);
            bottomDrawerView.animate().translationY(heightPixels).alpha(1.0f);
            isBottomDrawerVisible = true;
        }else{
            //mainHomeView.animate().translationX(0);
            bottomDrawerView.animate().translationY(bottomDrawerView.getHeight()).alpha(0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    bottomDrawerView.setVisibility(View.VISIBLE);
                }
            });
            isBottomDrawerVisible = false;
        }
    }

    public void animateSlideInOut(){
        if(isAppDrawerVisible) {
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
        if(isAppDrawerVisible) {
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

    private void getApplicationListForBottomDrawer(final ImageButton appDrawerBuuton, final String SP_APP){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle("Select Application");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.select_dialog_singlechoice);
        for(int i=0;i<packages.size;i++){
            arrayAdapter.add(""+packages.getAppLabel(i));
        }

        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        selectedAppPackageName = packages.getPackageName(which);
                        setChangedApplicationToButton(appDrawerBuuton, selectedAppPackageName, SP_APP);
                        /*
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(AppDrawerActivity.this);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Your Selected App is");
                        builderInner.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builderInner.show();
                        */
                    }
                });

        builderSingle.show();
    }

    private void changeBrightness(int bVal){

            if( bVal == 999) {
                //auto mode
            }
           else {
                //any value 0-10 but check, if value is under 10, then always keep it 10
                if(bVal < 0)
                    bVal = 0;
                else if(bVal > 255)
                    bVal = 255;

                ContentResolver cResolver = this.getApplicationContext().getContentResolver();
                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, bVal);
            }
    }

    private void changeWifiState(boolean val){
        wifiManager.setWifiEnabled(val);
    }

    private void changeBluetoothState(boolean val){
        if(val){
            bluetoothAdapter.enable();
        }else{
            bluetoothAdapter.disable();
        }
    }

    private void changeRotationState(boolean val){
        Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, val ? 1 : 0);
    }

    private void changeAireplaneState(boolean val){

    }

    private void checkSystemStatus(){
        //check wifi state,bluetooth state, rotation state, brightness state and set the value accordingly to the view.
        //call the updateGfx method to update gfx AND BROADCAST LISTERNER FOR ALL THESE SERVICE,, SO THAT ANY CHANGE CAN EFFECT IMMEDIATELY.

        ContentResolver cResolver = this.getApplicationContext().getContentResolver();
        String x = Settings.System.getString(cResolver, Settings.System.SCREEN_BRIGHTNESS);
        brightnessBar.setProgress(Integer.parseInt(x));

        wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        wifiState = wifiManager.isWifiEnabled();
        airplaneState = false;
        lightState = isFlashOn;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothState = bluetoothAdapter.isEnabled();

        try {
            int result = Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION);
            if(result==1){
                rotationState = true;
            }else{
                rotationState = false;
            }
        } catch (Settings.SettingNotFoundException e) {

        }

        Bitmap tmpImg ;
        if(wifiState){
            tmpImg = BitmapFactory.decodeResource(getResources(),R.drawable.wifiwhite);
           // Toast.makeText(AppDrawerActivity.this, "state: on", Toast.LENGTH_SHORT).show();
        }else{
            tmpImg = BitmapFactory.decodeResource(getResources(),R.drawable.wifiblack);
           // Toast.makeText(AppDrawerActivity.this, "state: off", Toast.LENGTH_SHORT).show();
        }
        wifiToggle.setImageBitmap(tmpImg);

        if(rotationState){
            tmpImg = BitmapFactory.decodeResource(getResources(),R.drawable.rotationwhite);
            // Toast.makeText(AppDrawerActivity.this, "state: on", Toast.LENGTH_SHORT).show();
        }else{
            tmpImg = BitmapFactory.decodeResource(getResources(),R.drawable.rotationblack);
            // Toast.makeText(AppDrawerActivity.this, "state: off", Toast.LENGTH_SHORT).show();
        }
        rotationToggle.setImageBitmap(tmpImg);

        if(bluetoothState){
            tmpImg = BitmapFactory.decodeResource(getResources(),R.drawable.bluetoothwhite);
            // Toast.makeText(AppDrawerActivity.this, "state: on", Toast.LENGTH_SHORT).show();
        }else{
            tmpImg = BitmapFactory.decodeResource(getResources(),R.drawable.bluetoothblack);
            // Toast.makeText(AppDrawerActivity.this, "state: off", Toast.LENGTH_SHORT).show();
        }
        bluetoothToggle.setImageBitmap(tmpImg);
        if(lightState){
            tmpImg = BitmapFactory.decodeResource(getResources(),R.drawable.lightwhite);
            // Toast.makeText(AppDrawerActivity.this, "state: on", Toast.LENGTH_SHORT).show();
        }else{
            tmpImg = BitmapFactory.decodeResource(getResources(),R.drawable.lightblack);
            // Toast.makeText(AppDrawerActivity.this, "state: off", Toast.LENGTH_SHORT).show();
        }
        lightToggle.setImageBitmap(tmpImg);


    }

    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
            }
        }
    }


    private void turnOnFlash() {
        getCamera();
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;
        }

    }


    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;
        }
        releaseCamera();
    }

    private void releaseCamera(){
        if(camera != null){
            camera.release();
            camera = null;
        }
    }


    public void preferenceButtonClicked(View v){
        //method is invoked when called from the app_drawer.xml layout
        Intent prefs = new Intent(this, PreferenceScreen.class);
        startActivity(prefs);
    }

    @Override
    public void onClick(View v) {
        Bitmap tmpImg;
        switch (v.getId()){
            case R.id.airplaneToggle:
                 if(airplaneState){
                    airplaneState = false;
                    tmpImg = BitmapFactory.decodeResource(getResources(),R.drawable.airplaneblack);
                }else{
                    airplaneState = true;
                    tmpImg = BitmapFactory.decodeResource(getResources(),R.drawable.airplanewhite);
                }
                airplaneToggle.setImageBitmap(tmpImg);
                changeAireplaneState(airplaneState);
                break;
            case R.id.wifiToggle:
                if(wifiState){
                    wifiState = false;
                    tmpImg = BitmapFactory.decodeResource(getResources(),R.drawable.wifiblack);
                }else{
                    wifiState = true;
                    tmpImg = BitmapFactory.decodeResource(getResources(),R.drawable.wifiwhite);
                }
                wifiToggle.setImageBitmap(tmpImg);
                changeWifiState(wifiState);
                break;
            case R.id.bluetoothToggle:
                if(bluetoothState){
                    bluetoothState = false;
                    tmpImg = BitmapFactory.decodeResource(getResources(),R.drawable.bluetoothblack);
                }else{
                    bluetoothState = true;
                    tmpImg = BitmapFactory.decodeResource(getResources(),R.drawable.bluetoothwhite);
                }
                bluetoothToggle.setImageBitmap(tmpImg);
                changeBluetoothState(bluetoothState);
                break;
            case R.id.rotationToggle:
                if(rotationState){
                    rotationState = false;
                    tmpImg = BitmapFactory.decodeResource(getResources(),R.drawable.rotationblack);
                }else{
                    rotationState = true;
                    tmpImg = BitmapFactory.decodeResource(getResources(),R.drawable.rotationwhite);
                }
                rotationToggle.setImageBitmap(tmpImg);
                changeRotationState(rotationState);
                break;
            case R.id.lightToggle:
                if(lightState){
                    lightState = false;
                    tmpImg = BitmapFactory.decodeResource(getResources(),R.drawable.lightblack);
                    turnOffFlash();
                }else{
                    lightState = true;
                    tmpImg = BitmapFactory.decodeResource(getResources(),R.drawable.lightwhite);
                    turnOnFlash();
                }
                lightToggle.setImageBitmap(tmpImg);
                break;
        }
    }

    public void initializeCalendar() {
        calendar.setEnabled(false);
       calendar.setShowWeekNumber(false);
        calendar.setFirstDayOfWeek(1);
        calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.green));
        calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));
        calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));
        calendar.setSelectedDateVerticalBar(R.color.darkgreen);
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
