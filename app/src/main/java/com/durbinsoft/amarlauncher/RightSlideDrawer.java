package com.durbinsoft.amarlauncher;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

/**
 * Created by Md.Wahuduzzaman on 3/6/2016.
 */
public class RightSlideDrawer {

    Context mcontext;
    LinearLayout rightSlideDrawer,mainHomeView;
    GridView newAppDrawer, googleAppDrawer;

    DisplayMetrics displayMetrics = new DisplayMetrics();
    int widthPixels = displayMetrics.widthPixels;

    ApplicationPackage aPackage;
    CustomApplicationDrawerAdapter customAdaper;

    boolean isRightSideDrawerInView = false;

    RightSlideDrawer(Context context,ApplicationPackage ap, LinearLayout rightDrawer,LinearLayout mainHome, GridView newDrawerGrid, GridView googleDrawerGrid){
        mcontext = context;
        rightSlideDrawer = rightDrawer;
        mainHomeView = mainHome;

        newAppDrawer = newDrawerGrid;
        googleAppDrawer = googleDrawerGrid;

        this.aPackage = ap;

        customAdaper = new CustomApplicationDrawerAdapter(mcontext,aPackage);


        googleAppDrawer.setAdapter(customAdaper);

        //newAppDrawer.setOnItemClickListener( new NewAppDrawerClickListener(mcontext, ap));
        googleAppDrawer.setOnItemClickListener(new GoogleAppDrawerClickListener(mcontext, aPackage));

    }

    public void getRightSlideDrawerInView(){
        // direction true == in and false == out
        if(isRightSideDrawerInView == false){
            rightSlideDrawer.setVisibility(View.VISIBLE);
            rightSlideDrawer.setAlpha(0.0f);
            mainHomeView.animate().translationX(-rightSlideDrawer.getWidth()+90);
            rightSlideDrawer.animate().translationX(widthPixels).alpha(1.0f);
            isRightSideDrawerInView = true;
        }else{

            mainHomeView.animate().translationX(0);
            rightSlideDrawer.animate().translationX(rightSlideDrawer.getWidth()).alpha(0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    rightSlideDrawer.setVisibility(View.VISIBLE);
                }
            });

            isRightSideDrawerInView = false;
        }
    }

    public boolean getRightSlideStatus(){
        return  isRightSideDrawerInView;
    }
}
