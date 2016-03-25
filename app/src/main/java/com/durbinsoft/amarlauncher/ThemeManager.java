package com.durbinsoft.amarlauncher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by Md.Wahuduzzaman on 3/17/2016.
 */
public class ThemeManager {
    Drawable icon;
    Context mContext;
    static PreferenceClassForData sPrefs ;

    Bitmap iconThemeTray;

    ThemeManager(Context context){
        mContext = context;
    }

    //all the name of application that has the icon...
   final String [][] appName = {
            {"phone", "call", "dialer"},
            {"message", "messenger"},
            {"chrome", "browser", "internet", "uc browser", "opera mini", "web"},
            {"camera"},
            {"calculator"},
            {"calendar"},
            {"clock", "time"},
            {"contacts", "people"},
            {"downloads", "download"},
            {"dropbox"},
            {"email","mail","yahoo mail"},
            {"evernote"},
            {"facebook","facebook lite"},
            {"gmail"},
            {"drive","google drive"},
            {"google music","play music"},
            {"google plus"},
            {"gtalk",  "hangouts"},
            {"imdb"},
            {"instagram"},
            {"maps","map"},
            {"market","play store"},
            {"music","music player"},
            {"mxplayer","mx player","mx player pro"},
            {"navigation"},
            {"pocket"},
            {"settings","setting"},
            {"skype"},
            {"soundrecorder","sound","recorder","audio","voice","sound recorder","audio recoder","voice recorder"},
            {"twitter","plume","finix"},
            {"viber"}
    };
    final int [] themeIcons = {
            R.drawable.phone, R.drawable.messaging,R.drawable.browser,R.drawable.cameras,
            R.drawable.calculator,R.drawable.calendar,R.drawable.clock,R.drawable.contacts,
            R.drawable.downloads,R.drawable.dropbox,R.drawable.email,R.drawable.evernote,R.drawable.facebook,R.drawable.gmail,
            R.drawable.googledrive,R.drawable.googlemusic,R.drawable.googleplus,R.drawable.gtalk,R.drawable.imdb,R.drawable.instagram,
            R.drawable.maps,R.drawable.market,R.drawable.music,R.drawable.mxplayer,R.drawable.navigation,R.drawable.pocket,R.drawable.settings,
            R.drawable.skype,R.drawable.soundrecorder,R.drawable.twitter,R.drawable.viber
    };

    private static String currentThemeName;
    private static String currentThemePackName;
    private static ArrayList<Drawable> currentIcon;
    private static ArrayList<String> currentIconPacName;
    private static String currentThemeDetails;
    private static Bitmap thumbnail;

    private final String APP_PATH_SD_CARD = "Android/data/";


    //
    public void updateThemeManager(PreferenceClassForData prefs){
        sPrefs = prefs;
        setCurrentThemeName(sPrefs.getThemeName());
        setCurrentThemePackName(sPrefs.getThemePackName());
        setCurrentThemeDetails(sPrefs.getThemeDetails());
        if(currentThemeName.equals("Default Theme 2.0")) {

        }else{
            initThemeIcon();
        }
    }

    private  void initThemeIcon (){
        Bitmap tmpIcon;
        for(int i =0;i<currentIconPacName.size();i++){
            tmpIcon = getIconFromSd(currentIconPacName.get(i));
            currentIcon.add(new BitmapDrawable(mContext.getResources(),tmpIcon));
        }
    }


    public void setCurrentIconPacName(ArrayList<String> aName){
        currentIconPacName = aName;
    }

    public void setCurrentThemeName(String name){
        currentThemeName = name;
    }

    public void setCurrentThemePackName(String name){
        currentThemePackName = name;
    }

    public void setCurrentThemeDetails(String details){
        currentThemeDetails = details;
    }

    public void getThumbnail(){

    }


    public int getThemeIconIndex(String appLabel,String appPack){
        int val =-1;
        appLabel = appLabel.toLowerCase();
        appPack = appPack.toLowerCase();
        if(currentThemeName.equals("Default Theme 2.0")){
            for(int i =0; i<themeIcons.length; i++){
                for(int j=0; j<appName[i].length;j++){
                    if(appName[i][j].equals(appLabel)){
                        val = i;
                        i = themeIcons.length;
                        break;
                    }
                }
            }
        }else{
            for(int i =0; i<currentIconPacName.size();i++){
                if(appPack.equals(currentIconPacName.get(i))){
                    val = i;
                    break;
                }
            }
        }
        return val;
    }

    public Drawable getThemeIcon(int index){
        Drawable d ;
        if(currentThemeName.equals("Default Theme 2.0")){
            Bitmap tmpImg = BitmapFactory.decodeResource(mContext.getResources(), themeIcons[index]);
            d=new BitmapDrawable(mContext.getResources(),tmpImg);
        }else{
            d = currentIcon.get(index);
        }

        return  d;
    }

    public Drawable createNewThemeIcon(Drawable mainIcon){
        iconThemeTray = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.d_home); //prev was iconback
        Bitmap mIcon = drawableToBitmap(mainIcon);
        mIcon = Bitmap.createScaledBitmap(mIcon, iconThemeTray.getWidth()-iconThemeTray.getWidth()/5, iconThemeTray.getHeight()-iconThemeTray.getHeight()/5, false);

        Bitmap newlyCreatedBitmap = null;
        newlyCreatedBitmap = Bitmap.createBitmap(iconThemeTray.getWidth(),iconThemeTray.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas combo = new Canvas(newlyCreatedBitmap);

        combo.drawBitmap(iconThemeTray,0f,0f,null);
        combo.drawBitmap(mIcon,mIcon.getWidth()/8,mIcon.getWidth()/13,null);
       // combo.drawBitmap(mIcon,iconThemeTray.getWidth()/2-newlyCreatedBitmap.getWidth()/6,iconThemeTray.getHeight()/2-newlyCreatedBitmap.getHeight()/6,null);


        icon =  new BitmapDrawable(mContext.getResources(),newlyCreatedBitmap);

        return  icon;
    }

    private static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    private Bitmap getIconFromSd(String filename) {

        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + APP_PATH_SD_CARD + currentThemePackName;
        Bitmap sdIcon = null;

// Look for the file on the external storage
        try {
            if (isSdReadable() == true) {
                sdIcon = BitmapFactory.decodeFile(fullPath + "/" + filename);
            }
        } catch (Exception e) {
            Log.e("getThumbnail() external", e.getMessage());
        }

// If no file on external storage, look in internal storage
        if (sdIcon == null) {
            try {
                File filePath = mContext.getFileStreamPath(filename);
                FileInputStream fi = new FileInputStream(filePath);
                sdIcon = BitmapFactory.decodeStream(fi);
            } catch (Exception ex) {
                Log.e("getThumbnail() internal", ex.getMessage());
            }
        }
        return sdIcon;
    }

    private boolean isSdReadable() {

        boolean mExternalStorageAvailable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = true;
            Log.i("isSdReadable", "External storage card is readable.");
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            Log.i("isSdReadable", "External storage card is readable.");
            mExternalStorageAvailable = true;
        } else {
        // Something else is wrong. It may be one of many other
        // states, but all we need to know is we can neither read nor write
            mExternalStorageAvailable = false;
        }

        return mExternalStorageAvailable;
    }
}
