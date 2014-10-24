package com.finapps.neveralone.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;

/**
 * Created by OVIL on 24/10/2014.
 */
public class UtilPictures {

    public static Bitmap getRoundedShape(Bitmap scaleBitmapImage, int size) {
        // TODO Auto-generated method stub
        Bitmap output = Bitmap.createBitmap(size, size,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Path path = new Path();
        path.addCircle(((float) size - 1) / 2,
                ((float) size - 1) / 2,
                (Math.min(((float) size), ((float) size)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        if (sourceBitmap!=null){
            canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
                    sourceBitmap.getHeight()), new Rect(0, 0, size,
                    size), null);
            return output;
        }else{
            return null;
        }

    }
}
