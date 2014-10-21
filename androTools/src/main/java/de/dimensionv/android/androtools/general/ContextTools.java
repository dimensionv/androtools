package de.dimensionv.android.androtools.general;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by vseifert on 18/10/14.
 */
public class ContextTools {
  private static final String LOGTAG = ContextTools.class.getName();

  public static Context getApplicationContext(Context context) {
    Context appContext = context.getApplicationContext();
    return (appContext == null) ? context : appContext;
  }

  public static String getApplicationName(Context context) {
    Context appContext = getApplicationContext(context);

    PackageManager packageManager = appContext.getPackageManager();
    ApplicationInfo applicationInfo = null;

    try {
      applicationInfo = packageManager.getApplicationInfo(appContext.getApplicationInfo().packageName, 0);
    } catch(final PackageManager.NameNotFoundException e) {
      // ignore this, it's handled on the return...
      Log.w(LOGTAG, e);
    }

    return (String) ((applicationInfo != null) ? packageManager.getApplicationLabel(applicationInfo)
                                               : "Unknown");
  }
}
