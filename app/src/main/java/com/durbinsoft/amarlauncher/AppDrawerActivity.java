package com.durbinsoft.amarlauncher;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;


public class AppDrawerActivity extends Activity implements View.OnClickListener{

    ApplicationPackage packages;
    CustomApplicationDrawerAdapter customDrawerAdapter;
    AppDrawerClickListener appDrawerClickListener;

    GridView appDrawerView, newAppGrid, googleAppGrid;
    LinearLayout appTrayView, slideDrawerView, mainHomeView, bottomDrawerView, appdrawerLongpressDetails, rightSideDrawerView;
    ImageButton appDrawerHomeButton, appDrawerBuuton1,appDrawerBuuton2,appDrawerBuuton3,appDrawerBuuton4,bottomDrawerbutton1,bottomDrawerbutton2,bottomDrawerbutton3,bottomDrawerbutton4,bottomDrawerbutton5,bottomDrawerbutton6,bottomDrawerbutton7,bottomDrawerbutton8,bottomDrawerbutton9,bottomDrawerbutton10;

    private boolean isAppDrawerVisible = false;
    private boolean isAppTrayVisible = false;
    private boolean isBottomDrawerVisible = false;
    private boolean isAppDrawerLongpressDetailsVisible = false;
    private boolean isAnyChangeMade = false;

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
    private TextView appDeleteTv,appLockTv,appHideTv;

    private PreferenceClassForData sPrefs;

    private String selectedAppPackageName = "";

    private Camera camera;
    Camera.Parameters params;


    private boolean initialSetup;
    PackageManager packagemanager;

    CalenderConversion myCalenderConversion;

    private boolean justStarted = true;

    //for left side calculator
    private TextView tv_show;
    private Button btn_clear;
    private StringBuffer str_show = new StringBuffer("");
    private BigDecimal num1, num2;
    private boolean flag_dot = true;
    private boolean flag_num1 = false;
    private String str_oper = null;
    private String str_result = null;
    private int scale = 2;
    private boolean flag_minus = false;
    //end for left side calculator



    private static final int ADMIN_INTENT = 15;
    private static final String description = "Please provide proper permission to lock the phone on double tap";
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mComponentName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_drawer);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);




        appTrayFadeInOut = false;
        appTraySlideInOUt = true;

        justStarted = true;


        initiateView();
        setAllAdapterAndEverything();


        if(initialSetup){
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,description);
            startActivityForResult(intent, ADMIN_INTENT);

            //to remove admin
            //mDevicePolicyManager.removeActiveAdmin(mComponentName);

            Intent i = new Intent(this,AppChooserApplication.class);
            startActivity(i);
        }
    }




    @Override
    protected void onResume() {

        //use the boolean isAnyChangeMade to fix the bug

        super.onResume();
        initialSetup = sPrefs.getBool();
        isAnyChangeMade = sPrefs.getAnyChangeMadeBool();
        if((initialSetup == false)&&(isAnyChangeMade)){
            setBottomDrawerApps();
            setAllAdapterAndEverything();
            isAnyChangeMade = false;
            sPrefs.setChangeMadeBool(isAnyChangeMade);
        }else if(justStarted){
            setBottomDrawerApps();
            justStarted = false;
        }
    }

    public void initiateView()
    {
        initView();

        airplaneToggle = (ImageView)findViewById(R.id.airplaneToggle);
        wifiToggle = (ImageView)findViewById(R.id.wifiToggle);
        bluetoothToggle = (ImageView)findViewById(R.id.bluetoothToggle);
        rotationToggle = (ImageView)findViewById(R.id.rotationToggle);
        lightToggle = (ImageView)findViewById(R.id.lightToggle);

        leftDrawerClockDateText = (TextView)findViewById(R.id.leftclockDateTv);
        leftDrawerClockTimeText = (TextView)findViewById(R.id.leftclockTimeTv);
        leftDrawerNameText = (TextView) findViewById(R.id.leftDrawerNameText);


        appDeleteTv = (TextView) findViewById(R.id.appUnistalltv);
        appLockTv = (TextView) findViewById(R.id.appLockTv);
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

        appDrawerHomeButton = (ImageButton) findViewById(R.id.homeButtonAppDrawer);

        bottomDrawerView = (LinearLayout) findViewById(R.id.bottomDrawerViewHolder);
        mainHomeView = (LinearLayout) findViewById(R.id.mainHomeView);
        slideDrawerView = (LinearLayout) findViewById(R.id.slideDrawer);
        appTrayView = (LinearLayout) findViewById(R.id.appTrayHolder);
        appdrawerLongpressDetails = (LinearLayout) findViewById(R.id.appdrawerLongPressDetailsLayout);

        rightSideDrawerView = (LinearLayout)findViewById(R.id.rightSlideDrawer);

        appDrawerView = (GridView) findViewById(R.id.appDrawerGridView);
        newAppGrid = (GridView) findViewById(R.id.newappDrawerGridView);
        googleAppGrid = (GridView) findViewById(R.id.googleappDrawerGridView);

    }

    public void setAllAdapterAndEverything(){

        mDevicePolicyManager = (DevicePolicyManager)getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(this, MyAdminReceiver.class);



        packagemanager = getPackageManager();
        sPrefs = new PreferenceClassForData(this);
        sPrefs.initializeSharedPrefs();
        initialSetup = sPrefs.getBool();

        packages = new ApplicationPackage(this);
        packages.initializePackages();

        myCalenderConversion = new CalenderConversion();

        customDrawerAdapter = new CustomApplicationDrawerAdapter(this, packages);

        airplaneToggle.setOnClickListener(this);
        wifiToggle.setOnClickListener(this);
        bluetoothToggle.setOnClickListener(this);
        rotationToggle.setOnClickListener(this);
        lightToggle.setOnClickListener(this);


        appDrawerView.setAdapter(customDrawerAdapter);

        appDrawerView.setTextFilterEnabled(true);
        appDrawerClickListener = new AppDrawerClickListener(this, packages,appdrawerLongpressDetails,appDrawerView,sPrefs);

        appDrawerView.setOnItemClickListener(new AppDrawerClickListener(this, packages,sPrefs));
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

        appLockTv.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSingleTap() {
                sPrefs.toggleAppLock(appDrawerClickListener.getPressedAppName());
                appDrawerClickListener.resetVisibilityAndOther();
            }

        });
        /*
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
                if(!isAppTrayVisible){
                    getSliderDrawerInView();
                }

            }

            public void onSwipeLeft() {
                if(isAppTrayVisible){
                    getSliderDrawerInView();
                }
            }

            public void onDoubleTapOccured(){
                appDrawerClickListener.hapticVibreationFeedback();
                boolean isAdmin = mDevicePolicyManager.isAdminActive(mComponentName);
                if (isAdmin) {
                    mDevicePolicyManager.lockNow();
                }else{
                    Toast.makeText(getApplicationContext(), "Not Registered as Admin", Toast.LENGTH_SHORT).show();
                }
            }

            public void onSwipeBottom() {
                //this should bring down the notification panel
            }

            public void onLongPressDown(){
                appDrawerClickListener.hapticVibreationFeedback();
                Toast.makeText(getApplicationContext(),"long pressed",Toast.LENGTH_SHORT).show();
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
                appDrawerClickListener.resetVisibilityAndOther();
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
                appDrawerClickListener.hapticVibreationFeedback();
                getApplicationListForBottomDrawer(appDrawerBuuton1, sPrefs.SP_APP1);
            }

        });
        appDrawerBuuton2.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                webClicked();
            }

            public void onLongPressDown(){
                appDrawerClickListener.hapticVibreationFeedback();
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
                appDrawerClickListener.hapticVibreationFeedback();
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
                appDrawerClickListener.hapticVibreationFeedback();
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
                appDrawerClickListener.hapticVibreationFeedback();
                getApplicationListForBottomDrawer(bottomDrawerbutton1, sPrefs.SP_APP5);
            }

            public void onSingleTap() {
                getBottomDrawerInView();
                Intent launchIntent = packagemanager.getLaunchIntentForPackage(sPrefs.getSelectedApp(5));
                startActivity(launchIntent);
            }

        });
        bottomDrawerbutton2.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onLongPressDown() {
                appDrawerClickListener.hapticVibreationFeedback();
                getApplicationListForBottomDrawer(bottomDrawerbutton2, sPrefs.SP_APP6);
            }

            public void onSingleTap() {
                getBottomDrawerInView();
                Intent launchIntent = packagemanager.getLaunchIntentForPackage(sPrefs.getSelectedApp(6));
                startActivity(launchIntent);
            }

        });
        bottomDrawerbutton3.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onLongPressDown() {
                appDrawerClickListener.hapticVibreationFeedback();
                getApplicationListForBottomDrawer(bottomDrawerbutton3, sPrefs.SP_APP7);
            }

            public void onSingleTap() {
                getBottomDrawerInView();
                Intent launchIntent = packagemanager.getLaunchIntentForPackage(sPrefs.getSelectedApp(7));
                startActivity(launchIntent);
            }

        });
        bottomDrawerbutton4.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onLongPressDown() {
                appDrawerClickListener.hapticVibreationFeedback();
                getApplicationListForBottomDrawer(bottomDrawerbutton4, sPrefs.SP_APP8);
            }

            public void onSingleTap() {
                getBottomDrawerInView();
                Intent launchIntent = packagemanager.getLaunchIntentForPackage(sPrefs.getSelectedApp(8));
                startActivity(launchIntent);
            }

        });
        bottomDrawerbutton5.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onLongPressDown() {
                appDrawerClickListener.hapticVibreationFeedback();
                getApplicationListForBottomDrawer(bottomDrawerbutton5, sPrefs.SP_APP9);
            }

            public void onSingleTap() {
                getBottomDrawerInView();
                Intent launchIntent = packagemanager.getLaunchIntentForPackage(sPrefs.getSelectedApp(9));
                startActivity(launchIntent);
            }

        });
        bottomDrawerbutton6.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onLongPressDown() {
                appDrawerClickListener.hapticVibreationFeedback();
                getApplicationListForBottomDrawer(bottomDrawerbutton6, sPrefs.SP_APP10);
            }

            public void onSingleTap() {
                getBottomDrawerInView();
                Intent launchIntent = packagemanager.getLaunchIntentForPackage(sPrefs.getSelectedApp(10));
                startActivity(launchIntent);
            }

        });
        bottomDrawerbutton7.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onLongPressDown() {
                appDrawerClickListener.hapticVibreationFeedback();
                getApplicationListForBottomDrawer(bottomDrawerbutton7, sPrefs.SP_APP11);
            }

            public void onSingleTap() {
                getBottomDrawerInView();
                Intent launchIntent = packagemanager.getLaunchIntentForPackage(sPrefs.getSelectedApp(11));
                startActivity(launchIntent);
            }

        });
        bottomDrawerbutton8.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onLongPressDown() {
                appDrawerClickListener.hapticVibreationFeedback();
                getApplicationListForBottomDrawer(bottomDrawerbutton8, sPrefs.SP_APP12);
            }

            public void onSingleTap() {
                getBottomDrawerInView();
                Intent launchIntent = packagemanager.getLaunchIntentForPackage(sPrefs.getSelectedApp(12));
                startActivity(launchIntent);
            }

        });
        bottomDrawerbutton9.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onLongPressDown() {
                appDrawerClickListener.hapticVibreationFeedback();
                getApplicationListForBottomDrawer(bottomDrawerbutton9, sPrefs.SP_APP13);
            }

            public void onSingleTap() {
                getBottomDrawerInView();
                Intent launchIntent = packagemanager.getLaunchIntentForPackage(sPrefs.getSelectedApp(13));
                startActivity(launchIntent);
            }

        });
        bottomDrawerbutton10.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onLongPressDown() {
                appDrawerClickListener.hapticVibreationFeedback();
                getApplicationListForBottomDrawer(bottomDrawerbutton10, sPrefs.SP_APP14);
            }

            public void onSingleTap() {
                getBottomDrawerInView();
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
                changeBrightness(seekBarProgress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {

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
           appDrawerClickListener.resetVisibilityAndOther();
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

    public void animateSlideInOut() {
        if (isAppDrawerVisible) {
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

    public void animateFadeInOut() {
        if (isAppDrawerVisible) {
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
        } else {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADMIN_INTENT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Registered As Admin", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Failed to register as Admin", Toast.LENGTH_SHORT).show();
            }
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
        int brightVal = Integer.parseInt(x);
        if(brightVal==0){
            brightnessBar.setProgress(brightVal);
        }else{
            brightnessBar.setProgress(brightVal);
        }

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
        Button btn;
        appDrawerClickListener.hapticVibreationFeedback();

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

            //for calculator
            case R.id.btn_point:
                if (str_show.toString() == "") {
                    break;
                } else if (flag_dot) {
                    str_show.append(".");
                    showInTextView(str_show.toString());
                    flag_dot = false;
                }
                break;
            case R.id.btn_clear:
                if (!(str_show.toString() == "")) {
                    if (!flag_dot) {
                        String lastStr = String.valueOf(str_show.charAt(str_show
                                .length() - 1));
                        if (lastStr.equals(".")) {
                            flag_dot = true;
                        }
                    }
                    str_show.deleteCharAt(str_show.length() - 1);
                    if(str_show.toString().equals("")){
                        flag_minus = false;
                    }
                    showInTextView(str_show.toString());
                } else {
                    showInTextView("");
                    str_result = null;
                    str_show = new StringBuffer("");
                    flag_dot = true;
                    flag_minus = false;
                }
                flag_num1 = false;
                break;
            case R.id.btn_add:
                btn = (Button) v;
                setNum1(btn.getText().toString());
                break;
            case R.id.btn_sub:
                btn = (Button) v;
                if (!flag_minus) {
                    if (str_show.toString().equals("")) {
                        str_show.append("-");
                        showInTextView(str_show.toString());
                        flag_minus = true;
                        break;
                    }
                }
                setNum1(btn.getText().toString());
                break;
            case R.id.btn_mul:
                btn = (Button) v;
                setNum1(btn.getText().toString());
                break;
            case R.id.btn_div:
                btn = (Button) v;
                setNum1(btn.getText().toString());
                break;
            case R.id.btn_equal:
                if (str_oper == null || str_show.toString().equals("")
                        || !flag_num1)
                    break;
                calculate();
                break;
            default:
                btn = (Button) v;
                str_show.append(btn.getText().toString());
                showInTextView(str_show.toString());
                break;
        }
    }



//for left side calculator
private void setNum1(String oper) {
    if (str_oper != null && !str_show.toString().equals("") && flag_num1) {
        calculate();
    }
    str_oper = oper;
    if (!(str_show.toString() == "") && !str_show.toString().equals("-")) {
        num1 = new BigDecimal(str_show.toString());
        showInTextView(str_show.toString());
        str_show = new StringBuffer("");
        str_result = null;
        flag_num1 = true;
        flag_minus = false;
    } else if (str_result != null) {
        num1 = new BigDecimal(str_result);
        showInTextView(str_result);
        str_result = null;
        flag_num1 = true;
        flag_minus = false;
    }
    flag_dot = true;
}

    private void calculate() {
        if(str_show.toString().equals("-")) return;
        double result = 0;
        num2 = new BigDecimal(str_show.toString());
        if (str_oper.equals("+")) {
            result = Calculate.add(num1, num2);
        }
        if (str_oper.equals("-")) {
            result = Calculate.sub(num1, num2);
        }
        if (str_oper.equals("*")) {
            result = Calculate.mul(num1, num2);
        }
        if (str_oper.equals("/")) {
            if (!num2.equals(BigDecimal.ZERO)) {
                result = Calculate.div(num1, num2, scale);
            } else {
                showInTextView("");
                str_show = new StringBuffer("");
                str_oper = null;
                flag_num1 = false;
                flag_dot = true;
                return;
            }
        }
        str_result = String.valueOf(Calculate.round(result, scale));
        String[] resultStrings = str_result.split("\\.");
        if (resultStrings[1].equals("0")) {
            str_result = resultStrings[0];
        }
        showInTextView(str_result);
        str_show = new StringBuffer("");
        flag_dot = true;
        flag_num1 = false;
        str_oper = null;
        flag_minus = true;
    }

    private void showInTextView(String str) {
        tv_show.setText(str);
    }

    private void initView() {
        tv_show = (TextView) findViewById(R.id.tv_show);
        findViewById(R.id.btn0).setOnClickListener(this);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        findViewById(R.id.btn6).setOnClickListener(this);
        findViewById(R.id.btn7).setOnClickListener(this);
        findViewById(R.id.btn8).setOnClickListener(this);
        findViewById(R.id.btn9).setOnClickListener(this);
        findViewById(R.id.btn_div).setOnClickListener(this);
        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.btn_mul).setOnClickListener(this);
        findViewById(R.id.btn_equal).setOnClickListener(this);
        findViewById(R.id.btn_point).setOnClickListener(this);
        findViewById(R.id.btn_sub).setOnClickListener(this);
        btn_clear = (Button) findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(this);
        btn_clear.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // flag_longClick = true;
                tv_show.setText("");
                str_show = new StringBuffer("");
                flag_dot = true;
                flag_num1 = false;
                flag_minus = false;
                str_oper = null;
                return true;
            }
        });
    }


    //end for left side calculator




public class PackageChangeBroadCastListener extends BroadcastReceiver{


        @Override
        public void onReceive(Context context, Intent intent) {
            /*packages = new ApplicationPackage(getApplicationContext());
            packages.initializePackages();

            customDrawerAdapter = new CustomApplicationDrawerAdapter(getApplicationContext(), packages);
            appDrawerView.setAdapter(customDrawerAdapter);
            appDrawerView.setOnItemClickListener(new AppDrawerClickListener(getApplicationContext(), packages));
            */
            setAllAdapterAndEverything();
            sPrefs.setChangeMadeBool(true);
        }
    }

}
