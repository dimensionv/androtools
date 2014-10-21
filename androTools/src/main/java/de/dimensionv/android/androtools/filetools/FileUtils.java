package de.dimensionv.android.androtools.filetools;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Video;
import android.provider.MediaStore.Images;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FileUtils {
  /** TAG for log messages. */
  private static final String TAG = FileUtils.class.getName();
  private static final boolean DEBUG = false; // Set to true to enable logging

  private static final int KILOBYTE = 1024;
  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###.#");
  private static final String UNIT_KB = " KB";
  private static final String UNIT_MB = " MB";
  private static final String UNIT_GB = " GB";

  static final String HIDDEN_PREFIX = ".";

  private static String cachePath = null;


  /**
   * File and folder comparator.
   */
  private static Comparator<File> comparator = new FileComparator();

  /**
   * File (not directories) filter.
   */
  private static FileFilter fileFilter = new FileFileFilter();

  /**
   * Folder (directories) filter.
   */
  private static FileFilter directoryFilter = new DirectoryFileFilter();

  /**
   * Whether the filename is a video file.
   *
   * @param filename
   * @return
   */
  /*
   * public static boolean isVideo(String filename) {
   * String mimeType = getMimeType(filename);
   * if (mimeType != null && mimeType.startsWith("video/")) {
   * return true;
   * } else {
   * return false;
   * }
   * }
   */

  /**
   * Whether the URI is a local one.
   *
   * @param uri
   *
   * @return
   */
  public static boolean isLocal(String uri) {
    if((uri != null) && ! uri.startsWith("http://")) {
      return true;
    }
    return false;
  }

  /**
   * Gets the extension of a file name, like ".png" or ".jpg".
   *
   * @param uri
   *
   * @return Extension including the dot("."); "" if there is no extension; null if uri was null.
   */
  public static String getExtension(String uri) {
    if(uri == null) {
      return null;
    }

    int dot = uri.lastIndexOf(".");
    if(dot >= 0) {
      return uri.substring(dot);
    } else {
      // No extension.
      return "";
    }
  }

  /**
   * Returns true if uri is a media uri.
   *
   * @param uri
   *
   * @return
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
   * Returns true if uri points to an image or video.
   *
   * @param uri
   *
   * @return
   */
  public static boolean hasThumbnailSupport(Uri uri) {
    String uriString = uri.toString();
    return (uriString.startsWith(Video.Media.INTERNAL_CONTENT_URI.toString())
        || uriString.startsWith(Video.Media.EXTERNAL_CONTENT_URI.toString())
        || uriString.startsWith(Images.Media.INTERNAL_CONTENT_URI.toString())
        || uriString.startsWith(Images.Media.EXTERNAL_CONTENT_URI.toString()));
  }

  /**
   * Convert File into Uri.
   *
   * @param file
   *
   * @return uri
   */
  public static Uri getUri(File file) {
    if(file != null) {
      return Uri.fromFile(file);
    }
    return null;
  }

  /**
   * Convert Uri into File.
   *
   * @param uri
   *
   * @return file
   */
  public static File getFile(Uri uri) {
    if(uri != null) {
      String filepath = uri.getPath();
      if(filepath != null) {
        return new File(filepath);
      }
    }
    return null;
  }

  /**
   * Returns the path only (without file name).
   *
   * @param file
   *
   * @return
   */
  public static File getPathWithoutFilename(File file) {
    if(file != null) {
      if(file.isDirectory()) {
        // no filename to be split off. Return everything
        return file;
      } else {
        String fileName = file.getName();
        String filePath = file.getAbsolutePath();

        // remove filename from path
        String pathWithoutName = filePath.substring(0, filePath.length() - fileName.length());
        if(pathWithoutName.endsWith("/")) {
          pathWithoutName = pathWithoutName.substring(0, pathWithoutName.length() - 1);
        }
        return new File(pathWithoutName);
      }
    }
    return null;
  }

  /**
   * Constructs a file from a path and file name.
   *
   * @param curDir
   * @param file
   *
   * @return
   */
  public static File getFile(String curDir, String file) {
    String separator = curDir.endsWith("/") ? "" : "/";
    return new File(curDir + separator + file);
  }

  public static File getFile(File curDir, String file) {
    return FileUtils.getFile(curDir.getAbsolutePath(), file);
  }

  /**
   * Get a file path from a Uri.
   *
   * @param context
   * @param uri
   *
   * @return
   *
   * @throws URISyntaxException
   */
  public static String getPath(Context context, Uri uri) throws URISyntaxException {

    if(FileUtils.DEBUG) {
      Log.d(FileUtils.TAG + " File -",
          "Authority: " + uri.getAuthority() +
              ", Fragment: " + uri.getFragment() +
              ", Port: " + uri.getPort() +
              ", Query: " + uri.getQuery() +
              ", Scheme: " + uri.getScheme() +
              ", Host: " + uri.getHost() +
              ", Segments: " + uri.getPathSegments().toString()
      );
    }

    if("content".equalsIgnoreCase(uri.getScheme())) {
      String[] projection = {"_data"};
      Cursor cursor = null;

      try {
        cursor = context.getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow("_data");
        if(cursor.moveToFirst()) {
          return cursor.getString(column_index);
        }
      } catch(Exception e) {
        // ignore this one
      }
    } else if("file".equalsIgnoreCase(uri.getScheme())) {
      return uri.getPath();
    }

    return null;
  }

  /**
   * Get the file size in a human-readable string.
   *
   * @param size
   *
   * @return
   */
  public static String getReadableFileSize(int size) {
    float fileSize = 0;
    String suffix = UNIT_KB;

    if(size > KILOBYTE) {
      fileSize = size / KILOBYTE;
      if(fileSize > KILOBYTE) {
        fileSize = fileSize / KILOBYTE;
        if(fileSize > KILOBYTE) {
          fileSize = fileSize / KILOBYTE;
          suffix = UNIT_GB;
        } else {
          suffix = UNIT_MB;
        }
      }
    }
    return String.valueOf(DECIMAL_FORMAT.format(fileSize) + suffix);
  }

  /**
   * Get a list of Files in the give path
   *
   * @param path
   *
   * @return Collection of files in give directory
   */
  public static List<File> getFileList(String path) {
    ArrayList<File> list = new ArrayList<File>();

    // Current directory File instance
    final File pathDir = new File(path);

    // List file in this directory with the directory filter
    final File[] dirs = pathDir.listFiles(FileUtils.directoryFilter);
    if(dirs != null) {
      // Sort the folders alphabetically
      Arrays.sort(dirs, FileUtils.comparator);
      // Add each folder to the File list for the list adapter
      for(File dir : dirs) {
        list.add(dir);
      }
    }

    // List file in this directory with the file filter
    final File[] files = pathDir.listFiles(FileUtils.fileFilter);
    if(files != null) {
      // Sort the files alphabetically
      Arrays.sort(files, FileUtils.comparator);
      // Add each file to the File list for the list adapter
      for(File file : files) {
        list.add(file);
      }
    }

    return list;
  }

  public static String getApplicationCachePath(Context context) {
    if(cachePath == null) {
      File extCacheDir = context.getExternalCacheDir();
      File cacheDir = context.getCacheDir();

      boolean useExternalStorage = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
      if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
        useExternalStorage = !Environment.isExternalStorageRemovable() || useExternalStorage;
      }

      if(useExternalStorage) {
        cachePath = (extCacheDir != null) ? extCacheDir.getPath() : cacheDir.getPath();
      } else {
        cachePath = cacheDir.getPath();
      }
    }

    return cachePath;
  }
}