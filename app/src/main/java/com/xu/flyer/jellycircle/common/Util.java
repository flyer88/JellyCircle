package com.xu.flyer.jellycircle.common;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by flyer on 15/11/13.
 */
public class Util {
    public static int dpToPx(float dp, Resources resources){
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }
}
