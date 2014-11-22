// ////////////////////////////////////////////////////////////////////////////
// $Id$
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
//
// $Log: ScreenTools.java,v $
// Revision 1.3  2013/12/05 21:15:13  mjoellnir
// bug-fixes and improvements
//
// Revision 1.2 2013/10/12 08:27:33 mjoellnir
// Header-comment with ID and Log-entries added
//
// Revision 1.1 2013/10/12 07:29:17 mjoellnir
// Initial commit of the project into the repository
//
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
 * @author mjoellnir
 * @version 1.0
 */
public class ScreenTools {

  private static DisplayMetrics metrics = null;

  /**
   * Checks if the device has a big screen
   * 
   * @param activityContext
   *          The Activity Context.
   * @return Returns true if the device has a big screen
   * @since Class 1.0
   * @since API 1.0
   */
  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
  public static boolean haveBigScreen(Activity activity) {
    final int XLARGE;
    final int DENSITY_TV;
    final int DENSITY_XHIGH;
    final int DENSITY_XXHIGH;
    final int DENSITY_XXXHIGH;

    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
      XLARGE = Configuration.SCREENLAYOUT_SIZE_XLARGE;
      DENSITY_XHIGH = DisplayMetrics.DENSITY_XHIGH;
    } else {
      // for compatibility with API levels < 9
      XLARGE = 4;
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

    // Verifies if the generalized size of the device is LARGE or XLARGE
    Configuration config = activity.getResources().getConfiguration();

    int screenLayout = (config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK);
    boolean xlarge = (screenLayout == Configuration.SCREENLAYOUT_SIZE_LARGE)
        || (screenLayout == XLARGE);

    DisplayMetrics metrics = ScreenTools.getDisplayMetrics(activity);

    if(xlarge) {
      // If XLarge, checks if the Generalized Density is at least MDPI
      // (160dpi)

      // MDPI=160, DEFAULT=160, DENSITY_HIGH=240, DENSITY_MEDIUM=160,
      // DENSITY_TV=213, DENSITY_XHIGH=320, DENSITY_XXHIGH=480,
      // DENSITY_XXXHIGH=640
      return ((metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT)
          || (metrics.densityDpi == DisplayMetrics.DENSITY_HIGH)
          || (metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM)
          || (metrics.densityDpi == DENSITY_TV)
          || (metrics.densityDpi == DENSITY_XHIGH)
          || (metrics.densityDpi == DENSITY_XXHIGH)
          || (metrics.densityDpi == DENSITY_XXXHIGH));

    } else {
      return ((metrics.widthPixels > 480) && (metrics.heightPixels > 800))
          || ((metrics.widthPixels > 800) && (metrics.heightPixels > 480));
    }
  }

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
   *          The currently shown activity
   * @return ORIENTATION_PORTRAIT if it's portrait-oriented,
   *         Configuration.ORIENTATION_LANDSCAPE if it's landscape or
   *         Configuration.ORIENTATION_SQUARE if the orientation has equal width
   *         and height, and the device has a recent enough operating-system.
   * @since Class 1.0
   * @since API 1.0
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
  @SuppressWarnings("deprecation")
  public static int getScreenOrientation(Activity activity)
  {
    Display display = activity.getWindowManager().getDefaultDisplay();
    Configuration config = activity.getResources().getConfiguration();

    int orientation = Configuration.ORIENTATION_UNDEFINED;
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
   *          The currently shown activity
   * @return true if orientation is landscape, false otherwise
   * @since Class 1.0
   * @since API 1.0
   */
  public static boolean orientationIsLandscape(Activity activity) {
    return ScreenTools.getScreenOrientation(activity) == Configuration.ORIENTATION_LANDSCAPE;
  }

  /**
   * Convenience method to quickly identify portrait-orientation
   * 
   * @param activity
   *          The currently shown activity
   * @return true if orientation is portrait, false otherwise
   * @since Class 1.0
   * @since API 1.0
   */
  public static boolean orientationIsPortrait(Activity activity) {
    return ScreenTools.getScreenOrientation(activity) == Configuration.ORIENTATION_PORTRAIT;
  }
}
