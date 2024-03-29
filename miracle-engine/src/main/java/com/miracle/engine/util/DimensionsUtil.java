package com.miracle.engine.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class DimensionsUtil {
    public static int getDimensionByAttributeId(Context context, int resId){
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(resId,typedValue,false);
        return typedValue.data;
    }

    public static float dpToPx(Context context, float dp) {
        if (dp == 0){
            return 0;
        } else {
            return dp * context.getResources().getDisplayMetrics().density;
        }
    }

    public static float pxToDp(Context context, float px){
        if (px == 0){
            return 0;
        } else{
            return px/context.getResources().getDisplayMetrics().density;
        }
    }


}
