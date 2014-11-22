package de.dimensionv.android.androtools.filetools;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.util.Log;

import java.io.File;
import java.net.URI;
import java.util.Locale;

import de.dimensionv.java.libraries.common.exceptions.InvalidValueException;
import de.dimensionv.java.libraries.common.utilities.file.FileUtils;

/**
 * This class is a collection of useful routines regarding files, directories and URIs with specific
 * extensions for the Android platform.
 * <p/>
 * <p> Each method is available statically, which means there is no need to instantiate FileTools.
 * This is more memory efficient and allows for faster runtime then a class that has to be
 * instantiated, first.</p>
 * <p/>
 * <p>Please note that the {@code Uri}-objects used with methods introduced by <em>this</em>
 * class are always {@link Uri}-objects, and <em>not</em> {@link URI}-objects. This is important, as
 * there are also methods (inherited from {@link FileUtils}), which <em>use</em> {@link
 * URI}-objects. These two classes are not interchangeable nor castable. They have nothing in common
 * but Java's general base class {@code Object}, and that both handle URIs. Both have a
 * completely different set of features and possibilities for use, and since this class is Android
 * specific, it introduces the Android class {@code Uri} to enhance the feature-set inherited
 * from {@code FileUtils} by this functionality.</p>
 *
 * @author Volkmar Seifert &lt;vs@DimensionV.de&gt;
 * @version 1.0
 * @since API 1.0.0
 */
@SuppressWarnings("UnusedDeclaration")
public class FileTools extends FileUtils {
  /**
   * TAG for log messages.
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  private static final String TAG = FileTools.class.getName();
  /**
   * Flag whether debug-message shall be assembled and written or not.
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  private static final boolean DEBUG = false; // Set to true to enable logging
  /**
   * Path to the app's local cache-directory
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  private static String cachePath = null;
  private static String extCachePath = null;

  /**
   * <p>Returns true if {@link Uri} is a media URI.</p>
   * <p/>
   * <p>A media URI is either an audio, video or image {@link MediaStore} content URI. Every other
   * URI is not a media URI per definition of this method.</p>
   * <p/>
   * <p>Please note that the {@code Uri}-object for this method is a {@link Uri}-object, and
   * <em>not</em> a {@link URI}-object.</p>
   *
   * @param uri
   *     The {@code Uri}-object to check
   *
   * @return {@code true} if {@code Uri}-object is a media URI, {@code false}
   * otherwise.
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  public static boolean isMediaUri(Uri uri) {
    String uriString = uri.toString();
    return (uriString.startsWith(Audio.Media.INTERNAL_CONTENT_URI.toString())
        || uriString.startsWith(Audio.Media.EXTERNAL_CONTENT_URI.toString())
        || uriString.startsWith(Video.Media.INTERNAL_CONTENT_URI.toString())
        || uriString.startsWith(Video.Media.EXTERNAL_CONTENT_URI.toString())
        || uriString.startsWith(Images.Media.INTERNAL_CONTENT_URI.toString())
        || uriString.startsWith(Images.Media.EXTERNAL_CONTENT_URI.toString()));
  }

  /**
   * <p>Returns true if the {@link Uri} has thumbnail-support through {@link MediaStore}.</p>
   * <p/>
   * <p>Please note that the {@code Uri}-object for this method is a {@link Uri}-object, and
   * <em>not</em> a {@link URI}-object.</p>
   *
   * @param uri
   *     The {@code Uri}-object to check
   *
   * @return {@code true} if the file denoted by the {@code Uri}-object has
   * thumbnail-support through Android's Media-framework.
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  public static boolean hasThumbnailSupport(Uri uri) {
    String uriString = uri.toString();
    return (uriString.startsWith(Video.Media.INTERNAL_CONTENT_URI.toString())
        || uriString.startsWith(Video.Media.EXTERNAL_CONTENT_URI.toString())
        || uriString.startsWith(Images.Media.INTERNAL_CONTENT_URI.toString())
        || uriString.startsWith(Images.Media.EXTERNAL_CONTENT_URI.toString()));
  }

  /**
   * Convert a {@link File} into a {@link Uri}.
   *
   * @param file
   *     The {@code File}-object to convert.
   *
   * @return the new  {@code Uri}-object.
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  public static Uri getUri(File file) {
    return Uri.fromFile(file);
  }

  /**
   * Convert a {@link Uri} into a {@link File}.
   *
   * @param uri
   *     The {@code Uri}-object to convert.
   *
   * @return The new {@code File}-object.
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  public static File getFile(Context context, Uri uri) {
    return new File(getPath(context, uri));
  }

  /**
   * <p>Get a file path from a {@link Uri} as {@link String}.</p>
   * <p/>
   * <p>In case the {@code Uri} denotes a content-URI, the appropriate {@link ContentProvider}
   * is queried to retrieve the path.</p>
   *
   * @param context
   *     The {@link Context}-object in case the {@link Uri} is a content-URI.
   * @param uri
   *     The {@code Uri}-object to extract the file-path from.
   *
   * @return The path of the file denoted by the {@code Uri}-object.
   *
   * @throws InvalidValueException
   *     Thrown if the {@code Uri} object does not contain a valid file-path or the
   *     {@code ContentProvider} query fails.
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  public static String getPath(Context context, Uri uri) {
    String path = null;

    if(FileTools.DEBUG) {
      Log.d(FileTools.TAG + " File -",
          "Authority: " + uri.getAuthority() +
              ", Fragment: " + uri.getFragment() +
              ", Port: " + uri.getPort() +
              ", Query: " + uri.getQuery() +
              ", Scheme: " + uri.getScheme() +
              ", Host: " + uri.getHost() +
              ", Segments: " + uri.getPathSegments().toString()
      );
    }

    String scheme = uri.getScheme().toLowerCase(Locale.US); // a scheme must be in US-ASCII
    if("content".equals(scheme)) {
      String[] projection = {"_data"};
      Cursor cursor;

      try {
        cursor = context.getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow("_data");
        if(cursor.moveToFirst()) {
          path = cursor.getString(column_index);
        }
      } catch(Exception ex) {
        throw new InvalidValueException(uri, ex);
      }
    } else if("file".equals(scheme)) {
      path = uri.getPath();
    } else {
      throw new InvalidValueException(uri);
    }

    if(path == null) {
      throw new InvalidValueException(uri);
    }

    return path;
  }

  /**
   * <p>Retrieves the application's cache directory as {@link String}.</p>
   * <p/>
   * <p>Upon the first call, this method queries for both cache-directories, the one on the internal
   * storage and a possible one on an possible external storage. Both (if there is an external one)
   * are then cached in a static variable which is simply returned on subsequent calls. If the
   * external storage is not available for whatever reasons, but becomes available at a later time,
   * the external storage will not be queried again and will remain inaccessible.</p>
   * <p/>
   * <p>The directory returned is determined by the second parameter.</p>
   *
   * @param context
   *     The {@link Context}-object used to identify and retrieve the cache-directory.
   * @param external
   *     Whether to return the directory on the external or internal storage.
   *
   * @return The path to the cache-directory. Can be null if {@code external} is
   * {@code true} and the external storage is not available.
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  @TargetApi(Build.VERSION_CODES.GINGERBREAD)
  public static String getApplicationCachePath(Context context, boolean external) {
    if(cachePath == null) {
      File extCacheDir = context.getExternalCacheDir();
      File cacheDir = context.getCacheDir();

      boolean useExternalStorage = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
      if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
        useExternalStorage |= !Environment.isExternalStorageRemovable();
      }

      if(useExternalStorage) {
        cachePath = cacheDir.getPath();
        if(extCacheDir != null) {
          extCachePath = extCacheDir.getPath();
        }
      } else {
        cachePath = cacheDir.getPath();
        extCachePath = null;
      }
    }

    return (external) ? extCachePath : cachePath;
  }

  /**
   * <p>Retrieves the application's cache directory as {@link String}.</p>
   * <p/>
   * <p>This method is a convenience method to retrieve any cache-directory, with preference to the
   * external storage, if available and mounted. If the external storage is not available, the
   * internal storage will be used. The external storage will not be queried again for availability,
   * in that case.</p>
   *
   * @param context
   *     The {@link Context}-object used to identify and retrieve the cache-directory.
   *
   * @return The path to the cache-directory. Will not be null, because if there is no external
   * storage, the internal storage will be returned.
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  public static String getApplicationCachePath(Context context) {
    String path = getApplicationCachePath(context, true);
    if(path == null) {
      path = getApplicationCachePath(context, false);
    }
    return path;
  }
}