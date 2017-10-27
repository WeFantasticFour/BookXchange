package com.fantastic.bookxchange.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fantastic.bookxchange.models.User;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;


/**
 * Created by m3libea on 10/10/17.
 */

public class MapUtils {

    public interface MarkerListener{
        void onReadyMarker(Marker marker);
    }

    public static BitmapDescriptor createBubble(Context context, int style, String title) {
        IconGenerator iconGenerator = new IconGenerator(context);
        iconGenerator.setStyle(style);
        Bitmap bitmap = iconGenerator.makeIcon(title);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        return bitmapDescriptor;

    }

    public static void addMarker(Context c, GoogleMap map, User u, int color, MarkerListener listener) {

        Glide.with(c)
            .load(u.getUrlProfileImage())
            .asBitmap()
            .centerCrop()
            .into(new SimpleTarget<Bitmap>(80,80) {
                      @Override
                      public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                          Marker marker = map.addMarker(new MarkerOptions()
                                  .position(u.getLocation())
                                  .icon(BitmapDescriptorFactory.fromBitmap(getImageMarker(resource, color)))
                                  .anchor(0.5f, 1));
                          marker.setTag(u);

                          listener.onReadyMarker(marker);
                      }
                  }
            );
    }

    private static Bitmap getImageMarker(Bitmap resource, int color) {

        final int width = resource.getWidth();
        final int height = resource.getHeight();
        final Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        final Path path = new Path();

        path.addCircle(
                (float)(width / 2)
                , (float)(height / 2)
                , (float) Math.min(width, (height / 2))
                , Path.Direction.CCW);

        final Canvas canvas = new Canvas(outputBitmap);
        canvas.clipPath(path);

        canvas.drawBitmap(resource, 0, 0, null);


        return addBorderToBitmap(outputBitmap, 20, color);
    }

    public static void modifyMarker(Context c, Marker marker, User u, int color) {

        Glide.with(c)
                .load(u.getUrlProfileImage())
                .asBitmap()
                .centerCrop()
                .into(new SimpleTarget<Bitmap>(80,80) {
                          @Override
                          public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                              marker.setIcon(BitmapDescriptorFactory.fromBitmap(getImageMarker(resource, color)));
                          }
                      }
                );
    }


    public static Bitmap addBorderToBitmap(Bitmap srcBitmap, int borderWidth, int borderColor) {
        // Initialize a new Bitmap to make it bordered bitmap
        Bitmap dstBitmap = Bitmap.createBitmap(
                srcBitmap.getWidth() + borderWidth * 2, // Width
                srcBitmap.getHeight() + borderWidth * 2, // Height
                Bitmap.Config.ARGB_8888 // Config
        );

               // Initialize a new Canvas instance
        Canvas canvas = new Canvas(dstBitmap);

        // Initialize a new Paint instance to draw border
        Paint paint = new Paint();
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        paint.setAntiAlias(true);

        Rect rect = new Rect(
            borderWidth / 2,
            borderWidth / 2,
            canvas.getWidth() - borderWidth / 2,
            canvas.getHeight() - borderWidth / 2
        );

        canvas.drawCircle(rect.centerX(), rect.centerY(), srcBitmap.getHeight()/2, paint);
//        canvas.drawRect(rect, paint);

        // Draw source bitmap to canvas
        canvas.drawBitmap(srcBitmap, borderWidth, borderWidth, null);
        srcBitmap.recycle();

        // Return the bordered circular bitmap
        return dstBitmap;
    }
}
