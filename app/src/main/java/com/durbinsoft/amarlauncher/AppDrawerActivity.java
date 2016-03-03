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

    GridView appDrawerView;
    LinearLayout appTrayView, slideDrawerView, mainHomeView, bottomDrawerView;
    ImageButton appDrawerHomeButton, appDrawerBuuton1,appDrawerBuuton2,appDrawerBuuton3,appDrawerBuuton4,bottomDrawerbutton1,bottomDrawerbutton2,bottomDrawerbutton3,bottomDrawerbutton4,bottomDrawerbutton5,bottomDrawerbutton6,bottomDrawerbutton7,bottomDrawerbutton8,bottomDrawerbutton9,bottomDrawerbutton10;

    private boolean isAppDrawerVisible = false;
    private boolean isAppTrayVisible = false;
    private boolean isBottomDrawerVisible = false;

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

    private CalendarView calendar;
    private PreferenceClassForData sPrefs;

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

        packagemanager = getPackageManager();
        sPrefs = new PreferenceClassForData(this);
        sPrefs.initializeSharedPrefs();
        initialSetup = sPrefs.getBool();



        appTrayFadeInOut = false;
        appTraySlideInOUt = true;

        myCalenderConversion = new CalenderConversion();

        packages = new ApplicationPackage(this);
        packages.initializePackages();

        customDrawerAdapter = new CustomApplicationDrawerAdapter(this, packages);

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


        appDrawerBuuton1 = (ImageButton) findViewById(R.id.appDrawerButton1);
        appDrawerBuuton2 = (ImageButton) findViewById(R.id.appDrawerButton2);
        appDrawerBuuton3 = (ImageButton) findViewById(R.id.appDrawerButton3);
        appDrawerBuuton4 = (ImageButton) findViewById(R.id.appDrawerButton4);

        brightnessBar = (SeekBar) findViewById(R.id.bottomDrawerAppBrightnessSlider);

        calendar = (CalendarView) findViewById(R.id.calendar);

        appDrawerHomeButton = (ImageButton) findViewById(R.id.homeButtonAppDrawer);
        bottomDrawerView = (LinearLayout) findViewById(R.id.bottomDrawerViewHolder);
        mainHomeView = (LinearLayout) findViewById(R.id.mainHomeView);
        slideDrawerView = (LinearLayout) findViewById(R.id.slideDrawer);
        appTrayView = (LinearLayout) findViewById(R.id.appTrayHolder);
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

        airplaneToggle.setOnClickListener(this);
        wifiToggle.setOnClickListener(this);
        bluetoothToggle.setOnClickListener(this);
        rotationToggle.setOnClickListener(this);
        lightToggle.setOnClickListener(this);


        appDrawerView.setAdapter(customDrawerAdapter);

        appDrawerView.setTextFilterEnabled(true);

        appDrawerView.setOnItemClickListener(new AppDrawerClickListener(this, packages));

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
        }

        //below are the code to handle swipe gestures...

        mainHomeView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                checkSystemStatus();
                getCamera();
                getBottomDrawerInView();
            }

            public void onSwipeRight() {
                getSliderDrawerInView();
            }

            public void onSwipeLeft() {
                getSliderDrawerInView();
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

            public void onSingleTap(){
                homeClicked();
            }
            public void onSwipeTop() {
                getBottomDrawerInView();
            }

        });

        // appDrawer application click and swipe handler
        appDrawerBuuton1.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                cameraClicked();
            }

            public void onSingleTap() {
                Intent  launchIntent = packagemanager.getLaunchIntentForPackage(packages.getPackageName(sPrefs.getSelectedApp(1)));
                startActivity(launchIntent);;
            }

        });
        appDrawerBuuton2.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                webClicked();
            }


            public void onSingleTap() {
                Intent  launchIntent = packagemanager.getLaunchIntentForPackage(packages.getPackageName(sPrefs.getSelectedApp(2)));
                startActivity(launchIntent);
            }

        });
        appDrawerBuuton3.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                dialerClicked();
            }


            public void onSingleTap() {
                Intent  launchIntent = packagemanager.getLaunchIntentForPackage(packages.getPackageName(sPrefs.getSelectedApp(3)));
                startActivity(launchIntent);
            }

        });
        appDrawerBuuton4.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                messageClicked();
            }

            public void onSingleTap() {
                Intent  launchIntent = packagemanager.getLaunchIntentForPackage(packages.getPackageName(sPrefs.getSelectedApp(4)));
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

    private void setBottomDrawerApps(){
        int [] items = new int[4] ;
        items[0] = sPrefs.getSelectedApp(1);
        items[1] = sPrefs.getSelectedApp(2);
        items[2] = sPrefs.getSelectedApp(3);
        items[3] = sPrefs.getSelectedApp(4);
        appDrawerBuuton1.setImageDrawable(packages.getIcon(items[0]));
        appDrawerBuuton2.setImageDrawable(packages.getIcon(items[1]));
        appDrawerBuuton3.setImageDrawable(packages.getIcon(items[2]));
        appDrawerBuuton4.setImageDrawable(packages.getIcon(items[3]));
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
        if(isAppDrawerVisible){
            homeClicked();
        }
         else if(isAppTrayVisible){
            getSliderDrawerInView();
        }
         else if(isBottomDrawerVisible){
            getBottomDrawerInView();
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

    private void getApplicationListForBottomDrawer(){
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
                    }
                });

        builderSingle.show();
    }

    private void changeBrightness(int bVal){

            if( bVal == 999) {
                //auto mode
            }
           else {
                bVal = bVal *2;
                //any value 0-10 but check, if value is under 10, then always keep it 10
                if(bVal < 0)
                    bVal = 0;
                else if(bVal > 255)
                    bVal = 255;

                Log.d("bval", "" + bVal);
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
