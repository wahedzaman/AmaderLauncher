package com.durbinsoft.amarlauncher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by Md.Wahuduzzaman on 3/17/2016.
 */
public class ThemeManager {
    Drawable icon;
    Context mContext;

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



    public int getThemeIconIndex(String appLabel){
        appLabel = appLabel.toLowerCase();
        int val =-1;
        for(int i =0; i<themeIcons.length; i++){
            for(int j=0; j<appName[i].length;j++){
                if(appName[i][j].equals(appLabel)){
                    val = i;
                    i = themeIcons.length;
                    break;
                }
            }
        }
        return val;
    }

    public Drawable getThemeIcon(int index){
        Bitmap tmpImg = BitmapFactory.decodeResource(mContext.getResources(), themeIcons[index]);
        return  new BitmapDrawable(mContext.getResources(),tmpImg);
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
}
