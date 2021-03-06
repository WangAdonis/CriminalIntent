package cn.adonis.criminalintent;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.widget.ImageView;

/**
 * Created by thinkpad on 2016/2/29.
 */
public class PictureUtils {
    public static BitmapDrawable getScaledDrawable(Activity a,String path){
        Display display=a.getWindowManager().getDefaultDisplay();
        float destWidth=display.getWidth();
        float destHight=display.getHeight();

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path,options);

        float srcWidth=options.outWidth;
        float srcHeight=options.outHeight;

        int inSampleSize=1;
        if(srcHeight>destHight||srcWidth>destWidth){
            if(srcWidth>srcHeight){
                inSampleSize=Math.round(srcHeight/srcWidth);
            }else {
                inSampleSize=Math.round(srcWidth/srcHeight);
            }
        }

        options=new BitmapFactory.Options();
        options.inSampleSize=inSampleSize;

        Bitmap bitmap=BitmapFactory.decodeFile(path,options);
        return new BitmapDrawable(a.getResources(),bitmap);
    }

    public static void cleanImageView(ImageView imageView){
        if(!(imageView.getDrawable() instanceof BitmapDrawable)){
            return;
        }
        BitmapDrawable bitmapDrawable=(BitmapDrawable)imageView.getDrawable();
        bitmapDrawable.getBitmap().recycle();
        imageView.setImageDrawable(null);
    }
}
