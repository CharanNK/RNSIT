package com.smartamigos.rns.StartComponents;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.smartamigos.rns.R;

/**
 * Created by Ashwin Chandlapur on 2/23/2016.
 */
public class Splash extends Activity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        {
            //Remove title bar
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//set content view AFTER ABOVE sequence (to avoid crash)
            this.setContentView(R.layout.splash);
        };
        setContentView(R.layout.splash);

        final ImageView iv = (ImageView) findViewById(R.id.imageView2);
        final Animation an = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        final Animation an2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);

        iv.startAnimation(an);
        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv.startAnimation(an2);
                finish();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
