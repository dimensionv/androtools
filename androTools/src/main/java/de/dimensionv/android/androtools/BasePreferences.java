package de.dimensionv.android.androtools;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;

import de.dimensionv.android.androtools.filetools.FileUtils;
import de.dimensionv.android.androtools.general.ContextTools;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public abstract class BasePreferences {

  protected SharedPreferences settings;
  protected Editor editor = null;
  protected static String cachePath = null;
  protected Context context = null;

  public BasePreferences(Context context) {
    super();
    this.context = ContextTools.getApplicationContext(context);
    settings = context.getSharedPreferences(getFileName(), Context.MODE_PRIVATE);
    if(cachePath == null) {
      cachePath = FileUtils.getApplicationCachePath(context);
    }
  }

  @SuppressLint("CommitPrefEdits")
  protected Editor getEditor() {
    if(editor == null) {
      editor = settings.edit();
    }
    return editor;
  }

  @TargetApi(Build.VERSION_CODES.GINGERBREAD)
  protected void commit() {
    if(editor == null) {
      return;
    }
    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
      editor.commit();
    } else {
      editor.apply();
    }
    editor = null;
  }

  /**
   * @return the cachePath
   */
  public static String getCachePath() {
    return BasePreferences.cachePath;
  }

  public abstract String getFileName();

}