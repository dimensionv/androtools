// ////////////////////////////////////////////////////////////////////////////
//
// Author: Volkmar Seifert
// Description:
// Helpful tools that retrieve information about screen-orientation, -size, etc.
//
// ////////////////////////////////////////////////////////////////////////////
// License:
// // // // // // // // // // // // // // // // // // // //
// Copyright 2011 Volkmar Seifert <vs@dimensionv.de>.
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY VOLKMAR SEIFERT AND CONTRIBUTORS ``AS IS''
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE FOUNDATION OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
// CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
// DAMAGE.
//
// The views and conclusions contained in the software and documentation are
// those of the authors and should not be interpreted as representing official
// policies, either expressed or implied, of Volkmar Seifert <vs@dimensionv.de>.
// ////////////////////////////////////////////////////////////////////////////
package de.dimensionv.android.androtools.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * Helpful tools that retrieve information about screen-orientation, -size, etc.
 *
 * @author Volkmar Seifert &lt;vs@DimensionV.de&gt;
 *
 * @version 1.0
 * @since API 1.0.0
 */
@SuppressWarnings("UnusedDeclaration")
public class ScreenTools {

  private static DisplayMetrics metrics = null;

  private static final int LAYOUT_SIZE_XLARGE;
  private static final int DENSITY_TV;
  private static final int DENSITY_XHIGH;
  private static final int DENSITY_XXHIGH;
  private static final int DENSITY_XXXHIGH;
  private static final int RESOLUTION_MINBIG_U = 480;
  private static final int RESOLUTION_MINBIG_V = 800;

  static {
    // For compatibility with older Android systems, use self-made constants.
    // They are initialized once using either what the system provides us with, if supported,
    // or uses literals that actually equate to the real system values of newer systems.
    // This ensures that the routines works and behave the same on all systems.

    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
      LAYOUT_SIZE_XLARGE = Configuration.SCREENLAYOUT_SIZE_XLARGE;
      DENSITY_XHIGH = DisplayMetrics.DENSITY_XHIGH;
    } else {
      // for compatibility with API levels < 9
      LAYOUT_SIZE_XLARGE = 4;
      DENSITY_XHIGH = 320;
    }

    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR1) {
      DENSITY_TV = DisplayMetrics.DENSITY_TV;
    } else {
      DENSITY_TV = 213;
    }

    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
      DENSITY_XXHIGH = DisplayMetrics.DENSITY_XXHIGH;
    } else {
      DENSITY_XXHIGH = 480;
    }

    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
      DENSITY_XXXHIGH = DisplayMetrics.DENSITY_XXXHIGH;
    } else {
      DENSITY_XXXHIGH = 640;
    }
  }

  /**
   * <p>Checks if the device has a big screen using the currently shown {@code Activity} for
   * accessing window manager in order to retrieve the device's {@code DisplayMetrics}.</p>
   * <p/>
   * <p>The decision whether is screen size is "big" or not depends on a couple of things. First of
   * all, it is checked whether {@code Configuration.screenLayout} is set to eiter large or
   * extra-large. Depending on this check, on of the following two things are checked:</p>
   * <ol><li>If large or xlarge: it's a matter of DPI whether it really is a big screen or not.</li>
   * <li>If not large and not xlarge: check whether the screen-size in pixels is greater than
   * 480x800 and 800x480.</li></ol> <p>If this evaluates to {@code true}, the screen is a big
   * one.</p>
   *
   * @param activity
   *     The currently shown activity.
   *
   * @return Returns true if the device has a big screen, false otherwise.
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
  public static boolean haveBigScreen(Activity activity) {
    // Verifies if the generalized size of the device is LARGE or LAYOUT_SIZE_XLARGE
    Configuration config = activity.getResources().getConfiguration();

    int screenLayout = (config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK);
    boolean xlarge = (screenLayout == Configuration.SCREENLAYOUT_SIZE_LARGE) || (screenLayout == LAYOUT_SIZE_XLARGE);

    DisplayMetrics metrics = getDisplayMetrics(activity);

    if(xlarge) {
      // If XLarge, checks if the generalized density is at least MDPI
      // (160dpi)
      return (metrics.densityDpi >= DisplayMetrics.DENSITY_MEDIUM);
    } else {
      boolean portrait = (metrics.widthPixels > RESOLUTION_MINBIG_U) && (metrics.heightPixels > RESOLUTION_MINBIG_V);
      boolean landscape = (metrics.widthPixels > RESOLUTION_MINBIG_V) && (metrics.heightPixels > RESOLUTION_MINBIG_U);
      return portrait || landscape;
    }
  }

  /**
   * Returns the display metrics of the device, using the currently shown {@code Activity} for
   * accessing the window manager.
   *
   * @param activity
   *     The currently shown {@code Activity}.
   *
   * @return the {@code DisplayMetrics} of the device.
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  public static DisplayMetrics getDisplayMetrics(Activity activity) {
    if(ScreenTools.metrics == null) {
      ScreenTools.metrics = new DisplayMetrics();
      activity.getWindowManager().getDefaultDisplay().getMetrics(ScreenTools.metrics);
    }
    return ScreenTools.metrics;
  }

  /**
   * Returns the current orientation of the device.
   *
   * @param activity
   *     The currently shown activity
   *
   * @return ORIENTATION_PORTRAIT if it's portrait-oriented, Configuration.ORIENTATION_LANDSCAPE if
   * it's landscape or Configuration.ORIENTATION_SQUARE if the orientation has equal width and
   * height, and the device has a recent enough operating-system.
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
  @SuppressWarnings("deprecation")
  public static int getScreenOrientation(Activity activity) {
    Display display = activity.getWindowManager().getDefaultDisplay();
    Configuration config = activity.getResources().getConfiguration();

    int orientation;
    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
      // until Froyo, Display.getOrientation() was a proper method to determine
      // the current orientation...
      orientation = display.getOrientation();
      if(orientation == Configuration.ORIENTATION_UNDEFINED) {
        // if getOrientation fails, try config.orientation
        orientation = config.orientation;
      }
    } else {
      // as Display.getOrientation had been deprecated with the release of
      // Froyo, try config.orientation.
      orientation = config.orientation;
    }

    // If the API-Level is 8 or above, or the quick methods don't know the
    // orientation, "ORIENTATION_UNDEFINED" is returned.
    // By simply verifying the screen-dimensions ourselves, this problem can
    // by solved quite easily.

    if(orientation == Configuration.ORIENTATION_UNDEFINED) {

      int height = 0;
      int width = 0;

      // fetch screen-dimensions according to API-level
      if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR1) {
        Point p = new Point();
        display.getSize(p);
        width = p.x;
        height = p.y;
      } else {
        width = display.getWidth();
        height = display.getHeight();
      }

      // if height and widht of screen are equal then it orientation is square
      // This has been deprecated in API-Level 16.
      if((width == height) && (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)) {
        orientation = Configuration.ORIENTATION_SQUARE;
      } else {
        if(width < height) {
          // if width is less than height than it is portrait
          orientation = Configuration.ORIENTATION_PORTRAIT;
        } else {
          // if it is not any of the above it let's define this to be
          // landscape
          orientation = Configuration.ORIENTATION_LANDSCAPE;
        }
      }
    }

    return orientation;
  }

  /**
   * Convenience method to quickly identify landscape-orientation
   *
   * @param activity
   *     The currently shown activity
   *
   * @return true if orientation is landscape, false otherwise
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  public static boolean orientationIsLandscape(Activity activity) {
    return getScreenOrientation(activity) == Configuration.ORIENTATION_LANDSCAPE;
  }

  /**
   * Convenience method to quickly identify portrait-orientation
   *
   * @param activity
   *     The currently shown activity
   *
   * @return true if orientation is portrait, false otherwise
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  public static boolean orientationIsPortrait(Activity activity) {
    return getScreenOrientation(activity) == Configuration.ORIENTATION_PORTRAIT;
  }
}
