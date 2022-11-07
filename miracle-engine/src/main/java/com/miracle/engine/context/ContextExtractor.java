package com.miracle.engine.context;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

import androidx.annotation.Nullable;

import com.miracle.engine.MiracleApp;
import com.miracle.engine.activity.MiracleActivity;
import com.miracle.engine.activity.tabs.TabsActivity;
import com.miracle.engine.fragment.MiracleFragment;

public class ContextExtractor {

    @Nullable
    public static Activity extractActivity(@Nullable Context context){
        if(context!=null){
            if(context instanceof Activity){
                return (Activity) context;
            }
        }
        return null;
    }

    @Nullable
    public static MiracleActivity extractMiracleActivity(@Nullable Context context){
        if(context!=null){
            if(context instanceof MiracleActivity){
                return (MiracleActivity) context;
            }
        }
        return null;
    }

    @Nullable
    public static TabsActivity extractTabsActivity(@Nullable Context context){
        if(context!=null){
            if(context instanceof TabsActivity){
                return (TabsActivity) context;
            }
        }
        return null;
    }

    @Nullable
    public static MiracleApp extractMiracleApp(@Nullable Context context){
        if(context!=null){
            if(context instanceof MiracleApp){
                return (MiracleApp) context;
            }
        }
        return null;
    }

}
