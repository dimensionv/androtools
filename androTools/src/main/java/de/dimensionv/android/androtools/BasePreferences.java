package de.dimensionv.android.androtools;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.dimensionv.android.androtools.filetools.FileUtils;
import de.dimensionv.android.androtools.general.ContextTools;
import de.dimensionv.android.androtools.general.Initializer;
import de.dimensionv.android.androtools.general.MemoryTrimmer;
import de.dimensionv.common.utilities.StringUtils;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public abstract class BasePreferences implements SharedPreferences, SharedPreferences.Editor, Initializer, MemoryTrimmer {



  protected SharedPreferences settings;
  protected Editor editor = null;
  protected static String cachePath = null;
  protected Context context = null;
  private HashMap<String, String> keyMap = null;
  private boolean initialized = false;

  public BasePreferences(Context context) {
    initialize(context, getFileName());
  }

  public BasePreferences(Context context, String fileName) {
    initialize(context, fileName);
  }

  private void initialize(Context context, String fileName) {
    if(!initialized) {
      this.context = ContextTools.getApplicationContext(context);
      settings = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
      if(cachePath == null) {
        cachePath = FileUtils.getApplicationCachePath(this.context);
      }
      initialized = true;
    }
  }


  @SuppressLint("CommitPrefEdits")
  protected Editor getEditor() {
    if(editor == null) {
      editor = settings.edit();
    }
    return editor;
  }

  /**
   * @return the cachePath
   */
  public static String getCachePath() {
    return BasePreferences.cachePath;
  }

  public abstract String getFileName();

  protected String getHash(String value) {
    if(!getKeyMap().containsKey(value)) {
      String hash;

      try {
        hash = StringUtils.sha1Hash(value);
      } catch(Exception e) {
        hash = String.valueOf(value.hashCode());
      }

      keyMap.put(value, hash);
      return hash;
    }
    return keyMap.get(value);
  }

  protected HashMap<String, String> getKeyMap() {
    if(keyMap == null) {
      keyMap = new HashMap<String, String>();
    }

    return keyMap;
  }

  @Override
  public void onTrim() {
    if(keyMap != null) {
      keyMap.clear();
      keyMap = null;
    }

    settings = null;
    cachePath = null;
    context = null;

    // shouldn't happen, but better be safe than sorry...
    if(editor != null) {
      commit();
    }

    initialized = false;
  }

  @Override
  public void onForceTrim() {
    if(initialized) {
      onTrim(); // only trim, if not already trimmed.
    }
  }

  @Override
  public void onInitialize(Context context) {
    initialize(context, getFileName());
  }

  @Override
  public Map<String, ?> getAll() {
    return settings.getAll();
  }

  @Override
  public String getString(String key, String defValue) {
    return settings.getString(getHash(key), defValue);
  }

  @Override
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public Set<String> getStringSet(String key, Set<String> defValues) {
    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
      return settings.getStringSet(getHash(key), defValues);
    } else {
      throw new RuntimeException("Method not supported for this API-level");
    }
  }

  @Override
  public int getInt(String key, int defValue) {
    return settings.getInt(getHash(key), defValue);
  }

  @Override
  public long getLong(String key, long defValue) {
    return settings.getLong(getHash(key), defValue);
  }

  @Override
  public float getFloat(String key, float defValue) {
    return settings.getFloat(getHash(key), defValue);
  }

  @Override
  public boolean getBoolean(String key, boolean defValue) {
    return settings.getBoolean(getHash(key), defValue);
  }

  @Override
  public boolean contains(String key) {
    return settings.contains(getHash(key));
  }

  @Override
  public Editor edit() {
    return getEditor();
  }

  @Override
  public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
    settings.registerOnSharedPreferenceChangeListener(listener);
  }

  @Override
  public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
    settings.unregisterOnSharedPreferenceChangeListener(listener);
  }

  @Override
  public Editor putString(String key, String value) {
    return edit().putString(getHash(key), value);
  }

  @Override
  public Editor putStringSet(String key, Set<String> values) {
    return edit().putStringSet(getHash(key), values);
  }

  @Override
  public Editor putInt(String key, int value) {
    return edit().putInt(getHash(key), value);
  }

  @Override
  public Editor putLong(String key, long value) {
    return edit().putLong(getHash(key), value);
  }

  @Override
  public Editor putFloat(String key, float value) {
    return edit().putFloat(getHash(key), value);
  }

  @Override
  public Editor putBoolean(String key, boolean value) {
    return edit().putBoolean(getHash(key), value);
  }

  @Override
  public Editor remove(String key) {
    return edit().remove(getHash(key));
  }

  @Override
  public Editor clear() {
    return edit().clear();
  }

  @Override
  public boolean commit() {
    if(editor == null) {
      return false;
    }

    boolean result = true;
    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
      result = editor.commit();
    } else {
      editor.apply();
    }
    editor = null;

    return result;
  }

  @Override
  public void apply() {
    commit();
  }
}