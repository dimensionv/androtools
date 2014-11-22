package de.dimensionv.android.androtools.general;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Useful collection of methods that revolve around the {@link Context}-object or require its use.
 *
 * @author Volkmar Seifert &lt;vs@DimensionV.de&gt;
 * @version 1.0
 * @since API 1.0.0
 */
@SuppressWarnings("UnusedDeclaration")
public class ContextTools {
  /**
   * The log-tag for logging messages.
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  private static final String LOGTAG = ContextTools.class.getName();

  /**
   * <p>Returns the application {@link Context}.</p>
   *
   * <p>If the application-context provided by the given {@code Context}-object returns null,
   * it is safe to assume that it is the application-context already, so it is plainly returned.</p>
   *
   * @param context The {@code Context}-object to request the application-context from.
   * @return The application-context.
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  public static Context getApplicationContext(Context context) {
    Context appContext = context.getApplicationContext();
    return (appContext == null) ? context : appContext;
  }

  /**
   * Returns the application's name.
   *
   * @param context The The {@code Context}-object to use for retrieving the application-name.
   * @return The application's name or "Unknown" if a retrieval was not possible for some reason.
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
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
