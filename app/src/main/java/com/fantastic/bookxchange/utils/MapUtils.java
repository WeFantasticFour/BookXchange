package com.fantastic.bookxchange.utils;

import android.content.Context;
import android.graphics.Bitmap;

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

    public static BitmapDescriptor createBubble(Context context, int style, String title) {
        IconGenerator iconGenerator = new IconGenerator(context);
        iconGenerator.setStyle(style);
        Bitmap bitmap = iconGenerator.makeIcon(title);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        return bitmapDescriptor;

    }

    public static Marker addMarker(GoogleMap map, User u) {
        BitmapDescriptor marker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
        MarkerOptions options = new MarkerOptions()
                .position(u.getLocation())
                .icon(marker);
        Marker result = map.addMarker(options);
        result.setTag(u);
        return result;
    }

}
