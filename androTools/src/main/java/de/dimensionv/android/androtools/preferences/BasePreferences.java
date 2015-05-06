// ////////////////////////////////////////////////////////////////////////////
//
// Author: Volkmar Seifert
// Description:
// Helpful tools regarding views.
//
// ////////////////////////////////////////////////////////////////////////////
// License:
// // // // // // // // // // // // // // // // // // // //
// Copyright 2014 Volkmar Seifert <vs@dimensionv.de>.
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
package de.dimensionv.android.androtools.preferences;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.dimensionv.android.androtools.filetools.FileTools;
import de.dimensionv.android.androtools.general.ContextTools;
import de.dimensionv.android.androtools.general.Initializer;
import de.dimensionv.android.androtools.general.MemoryTrimmer;
import de.dimensionv.android.androtools.general.TrimState;
import de.dimensionv.java.libraries.common.utilities.strings.StringUtils;

/**
 * <p>Base-class for {@link SharedPreferences} handling.</p> <p>This base-class implements the
 * {@code SharedPreferences} and {@link Editor} interfaces directly for a more
 * convenient access and easier handling.</p> <p>Since this class is using caching mechanisms for
 * certain aspects, it also implements the {@link MemoryTrimmer} interface. In order to be properly
 * available after a trim memory event, the {@link Initializer} interface is implemented as well,
 * doing necessary initialization.</p>
 * <p/>
 * <p>Furthermore, this class has support for easily storing string-sets into the properties-file, a
 * feature that is only available from API-Level 11 (Honeycomb) and later. If the appropriate
 * API-Level is detected during runtime, the "native" method for storing string sets will be used,
 * otherwise the "support-implementation" introduced by this class will be used.<br /> In order to
 * be backwards compatible for devices that get updated from an API-Level below 11 to an API-Level
 * of 11 or above, this implementation can detect that previously the old routines were used, and
 * will silently make a transition to the new native routines.</p>
 *
 * @author Volkmar Seifert &lt;vs@DimensionV.de&gt;
 * @version 2.0
 * @since API 1.0.0
 */
@SuppressWarnings("UnusedDeclaration")
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public abstract class BasePreferences implements SharedPreferences, Editor, Initializer, MemoryTrimmer {

  /**
   * The actual {@link SharedPreferences} object that is used internally.
   *
   * @since Class 1.0, API 1.0.0
   */
  protected SharedPreferences settings;
  /**
   * An editor object used internally to write the preferences and finally commit/apply them. It
   * needs to be aquired through {@link BasePreferences#getEditor()}, and will be released by
   * calling {@link BasePreferences#apply()} or {@link BasePreferences#commit()}.
   *
   * @since Class 1.0, API 1.0.0
   */
  protected Editor editor = null;
  /**
   * The application's cachePath.
   *
   * @see BasePreferences#getCachePath()
   *
   * @since Class 1.0, API 1.0.0
   */
  protected static String cachePath = null;
  /**
   * The application context
   *
   * @since Class 1.0, API 1.0.0
   */
  protected Context context = null;
  /**
   * Cache for the hashed keys, to speed up key-hashing.
   *
   * @since Class 1.0, API 1.0.0
   */
  private HashMap<String, String> keyMap = null;
  /**
   * Flag that indicates whether initialization is complete ({@code true}) or pending ({@code
   * false}).
   *
   * @since Class 1.0, API 1.0.0
   */
  private TrimState trimState = TrimState.NONE;

  /**
   * Creates and initializes {@link BasePreferences} object with the given {@link Context}.
   * <p/>
   * For the setting of the required preferences-file's file name the abstract method {@link
   * BasePreferences#getFileName()}
   *
   * @param context
   *     The application context
   *
   * @since Class 1.0, API 1.0.0
   */
  public BasePreferences(Context context) {
    initialize(context, getFileName(), trimState);
  }

  /**
   * Creates and initializes {@link BasePreferences} object with the given {@link Context}.
   *
   * @param context
   *     The application context
   * @param fileName
   *     The name of the preference file.
   *
   * @since Class 1.0, API 1.0.0
   */
  public BasePreferences(Context context, String fileName) {
    initialize(context, fileName, trimState);
  }

  /**
   * Initializes all necessary data-structures and sets up the object for proper usage. Needs to be
   * called whenever {@link BasePreferences#onTrim()} or {@link BasePreferences#onForceTrim()} were
   * triggered and the object is used again.
   *
   * @param context
   *     The application context
   * @param fileName
   *     The name of the preference file.
   * @param trimState
   *     The current {@TrimState} of the app, which should be the same {@code TrimState} of all
   *     {@code Initializer}s.
   *
   * @param trimState
   * @since Class 1.0, API 1.0.0
   */
  private void initialize(Context context, String fileName, TrimState trimState) {
    if(this.trimState == TrimState.INITIALIZED) {
      return;
    }

    // not initialized, so check if sync states are in sync...
    if(this.trimState != trimState) {
      throw new IllegalStateException("TrimStates don't match!");
    }

    this.context = ContextTools.getApplicationContext(context);
    settings = this.context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    if(cachePath == null) {
      cachePath = FileTools.getApplicationCachePath(this.context);
    }

    this.trimState = TrimState.INITIALIZED;
  }

  /**
   * Aquires a valid {@link Editor}-object for writing preferences.
   *
   * @return the aquired {@code Editor}-object.
   *
   * @since Class 1.0, API 1.0.0
   */
  @SuppressLint("CommitPrefEdits")
  private Editor getEditor() {
    if(editor == null) {
      editor = settings.edit();
    }
    return editor;
  }

  /**
   * Returns the application's cache-path
   *
   * @return the cachePath
   *
   * @since Class 1.0, API 1.0.0
   */
  public static String getCachePath() {
    return BasePreferences.cachePath;
  }

  /**
   * Returns the filename of the {@link SharedPreferences} file used by this {@code BasePreferences}
   * object.
   *
   * @return The filename
   *
   * @since Class 1.0, API 1.0.0
   */
  public abstract String getFileName();

  /**
   * <p>Aquires the hash for the given {@code value}.</p> <p>This method checks the keyMap first if
   * the value has already been hashed before, and only if that's not the case, attempts to compute
   * a new hash of the value.<br /> If possible, the SHA1-Algorithm will be used for hashing. Should
   * that fail, a the simple standard {@link String#hashCode()} method will be used.</p> <p>In case
   * the value has been hashed before, it will be taken from the in-memory cache.</p>
   *
   * @param value
   *     The value to be hashed.
   *
   * @return The hash-value as a hexadecimal {@link String}.
   *
   * @since Class 1.0, API 1.0.0
   */
  protected String getHash(String value) {
    if(!getKeyMap().containsKey(value)) {
      String hash;

      try {
        hash = StringUtils.sha1Hash(value);
      } catch(Exception e) {
        hash = Integer.toHexString(value.hashCode());
      }

      keyMap.put(value, hash);
      return hash;
    }
    return keyMap.get(value);
  }

  /**
   * <p>Returns the keyMap cache.</p> <p>In case it wasn't instantiated, yet, this method will take
   * care of that.</p>
   *
   * @return The keyMap cache
   *
   * @since Class 1.0, API 1.0.0
   */
  protected HashMap<String, String> getKeyMap() {
    if(keyMap == null) {
      keyMap = new HashMap<String, String>();
    }

    return keyMap;
  }

  /**
   * {@inheritDoc}
   */
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

    trimState = TrimState.TRIMMED;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onForceTrim() {
    if(trimState == TrimState.INITIALIZED) {
      onTrim(); // only trim, if not already trimmed.
    }
    trimState = TrimState.FORCE_TRIMMED;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onInitialize(Context context, TrimState trimState) {
    initialize(context, getFileName(), trimState);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, ?> getAll() {
    return settings.getAll();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getString(String key, String defValue) {
    return settings.getString(getHash(key), defValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public Set<String> getStringSet(String key, Set<String> defValues) {
    Set<String> values;
    String list = settings.getString(getHash(key), null);
    TextUtils.StringSplitter splitter = getStringsSplitter(list);
    if(((Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) && (splitter != null)) || (splitter != null)) {
      values = new HashSet<String>(StringUtils.countOccurrences(list, ',') + 1);
      for(String value : splitter) {
        // filter out a leading space, if there is one...
        values.add((value.charAt(0) == ' ') ? value.substring(1) : value);
      }
      // just because the splitter is not null doesn't mean it really contains the separator-char
      // and therefore useful items.
      if(values.isEmpty()) {
        values = new HashSet<String>(defValues);
      }
    } else if(Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
      values = settings.getStringSet(getHash(key), defValues);
    } else {
      values = new HashSet<String>(defValues);
    }
    return values;
  }

  /**
   * Retrieves a {@code byte}-array from the preferences.
   *
   * @param key The name of the preference to retrieve.
   * @param defValues Values to return if this preference does not exist.
   *
   * @return Returns the preference values if they exist, or defValues.
   */
  public byte[] getByteArray(String key, byte[] defValues) {
    byte[] values;
    String list = settings.getString(getHash(key), null);
    TextUtils.StringSplitter splitter = getStringsSplitter(list);
    if(splitter != null) {
      values = new byte[StringUtils.countOccurrences(list, ',') + 1];
      int pos = 0;
      for(String value : splitter) {
        // filter out a leading space, if there is one...
        values[pos++] = Byte.parseByte(value.trim());
      }
      // just because the splitter is not null doesn't mean it really contains the separator-char
      // and therefore useful items.
      if(pos == 0) {
        if((defValues != null) && (defValues.length > 0)) {
          values = defValues.clone();
        } else {
          values = defValues;
        }
      }
    } else {
      if((defValues != null) && (defValues.length > 0)) {
        values = defValues.clone();
      } else {
        values = defValues;
      }
    }
    return values;
  }

  /**
   * Retrieves a {@code int}-array from the preferences.
   *
   * @param key The name of the preference to retrieve.
   * @param defValues Values to return if this preference does not exist.
   *
   * @return Returns the preference values if they exist, or defValues.
   */
  public int[] getIntArray(String key, int[] defValues) {
    int[] values;
    String list = settings.getString(getHash(key), null);
    TextUtils.StringSplitter splitter = getStringsSplitter(list);
    if(splitter != null) {
      values = new int[StringUtils.countOccurrences(list, ',') + 1];
      int pos = 0;
      for(String value : splitter) {
        // filter out a leading space, if there is one...
        values[pos++] = Integer.parseInt(value.trim());
      }
      // just because the splitter is not null doesn't mean it really contains the separator-char
      // and therefore useful items.
      if(pos == 0) {
        if((defValues != null) && (defValues.length > 0)) {
          values = defValues.clone();
        } else {
          values = defValues;
        }
      }
    } else {
      if((defValues != null) && (defValues.length > 0)) {
        values = defValues.clone();
      } else {
        values = defValues;
      }
    }
    return values;
  }

  /**
   * Retrieves a {@code long}-array from the preferences.
   *
   * @param key The name of the preference to retrieve.
   * @param defValues Values to return if this preference does not exist.
   *
   * @return Returns the preference values if they exist, or defValues.
   */
  public long[] getLongArray(String key, long[] defValues) {
    long[] values;
    String list = settings.getString(getHash(key), null);
    TextUtils.StringSplitter splitter = getStringsSplitter(list);
    if(splitter != null) {
      values = new long[StringUtils.countOccurrences(list, ',') + 1];
      int pos = 0;
      for(String value : splitter) {
        // filter out a leading space, if there is one...
        values[pos++] = Long.parseLong(value.trim());
      }
      // just because the splitter is not null doesn't mean it really contains the separator-char
      // and therefore useful items.
      if(pos == 0) {
        if((defValues != null) && (defValues.length > 0)) {
          values = defValues.clone();
        } else {
          values = defValues;
        }
      }
    } else {
      if((defValues != null) && (defValues.length > 0)) {
        values = defValues.clone();
      } else {
        values = defValues;
      }
    }
    return values;
  }

  /**
   * Retrieves a {@code float}-array from the preferences.
   *
   * @param key The name of the preference to retrieve.
   * @param defValues Values to return if this preference does not exist.
   *
   * @return Returns the preference values if they exist, or defValues.
   */
  public float[] getFloatArray(String key, float[] defValues) {
    float[] values;
    String list = settings.getString(getHash(key), null);
    TextUtils.StringSplitter splitter = getStringsSplitter(list);
    if(splitter != null) {
      values = new float[StringUtils.countOccurrences(list, ',') + 1];
      int pos = 0;
      for(String value : splitter) {
        // filter out a leading space, if there is one...
        values[pos++] = Float.parseFloat(value.trim());
      }
      // just because the splitter is not null doesn't mean it really contains the separator-char
      // and therefore useful items.
      if(pos == 0) {
        if((defValues != null) && (defValues.length > 0)) {
          values = defValues.clone();
        } else {
          values = defValues;
        }
      }
    } else {
      if((defValues != null) && (defValues.length > 0)) {
        values = defValues.clone();
      } else {
        values = defValues;
      }
    }
    return values;
  }

  /**
   * Retrieves a {@code double}-array from the preferences.
   *
   * @param key The name of the preference to retrieve.
   * @param defValues Values to return if this preference does not exist.
   *
   * @return Returns the preference values if they exist, or defValues.
   */
  public double[] getDoubleArray(String key, double[] defValues) {
    double[] values;
    String list = settings.getString(getHash(key), null);
    TextUtils.StringSplitter splitter = getStringsSplitter(list);
    if(splitter != null) {
      values = new double[StringUtils.countOccurrences(list, ',') + 1];
      int pos = 0;
      for(String value : splitter) {
        // filter out a leading space, if there is one...
        values[pos++] = Double.parseDouble(value.trim());
      }
      // just because the splitter is not null doesn't mean it really contains the separator-char
      // and therefore useful items.
      if(pos == 0) {
        if((defValues != null) && (defValues.length > 0)) {
          values = defValues.clone();
        } else {
          values = defValues;
        }
      }
    } else {
      if((defValues != null) && (defValues.length > 0)) {
        values = defValues.clone();
      } else {
        values = defValues;
      }
    }
    return values;
  }

  /**
   * Retrieves a {@code char}-array from the preferences.
   *
   * @param key The name of the preference to retrieve.
   * @param defValues Values to return if this preference does not exist.
   *
   * @return Returns the preference values if they exist, or defValues.
   */
  public char[] getCharArray(String key, char[] defValues) {
    char[] values;
    String list = settings.getString(getHash(key), null);
    TextUtils.StringSplitter splitter = getStringsSplitter(list);
    if(splitter != null) {
      values = new char[StringUtils.countOccurrences(list, ',') + 1];
      int pos = 0;
      for(String value : splitter) {
        // filter out a leading space, if there is one...
        values[pos++] = value.trim().charAt(0);
      }
      // just because the splitter is not null doesn't mean it really contains the separator-char
      // and therefore useful items.
      if(pos == 0) {
        if((defValues != null) && (defValues.length > 0)) {
          values = defValues.clone();
        } else {
          values = defValues;
        }
      }
    } else {
      if((defValues != null) && (defValues.length > 0)) {
        values = defValues.clone();
      } else {
        values = defValues;
      }
    }
    return values;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getInt(String key, int defValue) {
    return settings.getInt(getHash(key), defValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getLong(String key, long defValue) {
    return settings.getLong(getHash(key), defValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getFloat(String key, float defValue) {
    return settings.getFloat(getHash(key), defValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean getBoolean(String key, boolean defValue) {
    return settings.getBoolean(getHash(key), defValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean contains(String key) {
    return settings.contains(getHash(key));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Editor edit() {
    getEditor();
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
    settings.registerOnSharedPreferenceChangeListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
    settings.unregisterOnSharedPreferenceChangeListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Editor putString(String key, String value) {
    return getEditor().putString(getHash(key), value);
  }

  /**
   * {@inheritDoc}
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  @Override
  public Editor putStringSet(String key, Set<String> values) {
    Editor editor = getEditor();
    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
      editor.putStringSet(getHash(key), values);
    } else {
      String list = Arrays.toString(values.toArray(new String[values.size()]));
      editor.putString(getHash(key), list);
    }
    return editor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Editor putInt(String key, int value) {
    return getEditor().putInt(getHash(key), value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Editor putLong(String key, long value) {
    return getEditor().putLong(getHash(key), value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Editor putFloat(String key, float value) {
    return getEditor().putFloat(getHash(key), value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Editor putBoolean(String key, boolean value) {
    return getEditor().putBoolean(getHash(key), value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Editor remove(String key) {
    return getEditor().remove(getHash(key));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Editor clear() {
    return getEditor().clear();
  }

  /**
   * {@inheritDoc}
   */
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

  /**
   * {@inheritDoc}
   */
  @Override
  public void apply() {
    commit();
  }

  /**
   * Set an array of {@code byte} values in the preferences {@link Editor}, to be written back once
   * {@link #commit()} is called.
   *
   * @param key The name of the preference to modify.
   * @param values The array of new values for the preference. Passing {@code null} for
   *               this argument is equivalent to calling {@link #remove(String)} with this key.
   *
   * @return Returns a reference to the same {@link Editor} object, so you can chain put calls
   * together.
   *
   * @since Class 2.0, API 2.0.0
   */
  public Editor putByteArray(String key, byte[] values) {
    Editor editor = getEditor();
    String list = ((values != null) && (values.length > 0)) ? Arrays.toString(values) : null;
    editor.putString(getHash(key), list);
    return editor;
  }

  /**
   * Set an array of {@code int} values in the preferences {@link Editor}, to be written back once
   * {@link #commit()} is called.
   *
   * @param key The name of the preference to modify.
   * @param values The array of new values for the preference. Passing {@code null} for
   *               this argument is equivalent to calling {@link #remove(String)} with this key.
   *
   * @return Returns a reference to the same {@link Editor} object, so you can chain put calls
   * together.
   *
   * @since Class 2.0, API 2.0.0
   */
  public Editor putIntArray(String key, int[] values) {
    Editor editor = getEditor();
    String list = ((values != null) && (values.length > 0)) ? Arrays.toString(values) : null;
    editor.putString(getHash(key), list);
    return editor;
  }

  /**
   * Set an array of {@code long} values in the preferences {@link Editor}, to be written back once
   * {@link #commit()} is called.
   *
   * @param key The name of the preference to modify.
   * @param values The array of new values for the preference. Passing {@code null} for
   *               this argument is equivalent to calling {@link #remove(String)} with this key.
   *
   * @return Returns a reference to the same {@link Editor} object, so you can chain put calls
   * together.
   *
   * @since Class 2.0, API 2.0.0
   */
  public Editor putLongArray(String key, long[] values) {
    Editor editor = getEditor();
    String list = ((values != null) && (values.length > 0)) ? Arrays.toString(values) : null;
    editor.putString(getHash(key), list);
    return editor;
  }

  /**
   * Set an array of {@code float} values in the preferences {@link Editor}, to be written back once
   * {@link #commit()} is called.
   *
   * @param key The name of the preference to modify.
   * @param values The array of new values for the preference. Passing {@code null} for
   *               this argument is equivalent to calling {@link #remove(String)} with this key.
   *
   * @return Returns a reference to the same {@link Editor} object, so you can chain put calls
   * together.
   *
   * @since Class 2.0, API 2.0.0
   */
  public Editor putFloatArray(String key, float[] values) {
    Editor editor = getEditor();
    String list = ((values != null) && (values.length > 0)) ? Arrays.toString(values) : null;
    editor.putString(getHash(key), list);
    return editor;
  }

  /**
   * Set an array of {@code double} values in the preferences {@link Editor}, to be written back once
   * {@link #commit()} is called.
   *
   * @param key The name of the preference to modify.
   * @param values The array of new values for the preference. Passing {@code null} for
   *               this argument is equivalent to calling {@link #remove(String)} with this key.
   *
   * @return Returns a reference to the same {@link Editor} object, so you can chain put calls
   * together.
   *
   * @since Class 2.0, API 2.0.0
   */
  public Editor putDoubleArray(String key, double[] values) {
    Editor editor = getEditor();
    String list = ((values != null) && (values.length > 0)) ? Arrays.toString(values) : null;
    editor.putString(getHash(key), list);
    return editor;
  }

  /**
   * Set an array of {@code char} values in the preferences {@link Editor}, to be written back once
   * {@link #commit()} is called.
   *
   * @param key The name of the preference to modify.
   * @param values The array of new values for the preference. Passing {@code null} for
   *               this argument is equivalent to calling {@link #remove(String)} with this key.
   *
   * @return Returns a reference to the same {@link Editor} object, so you can chain put calls
   * together.
   *
   * @since Class 2.0, API 2.0.0
   */
  public Editor putCharArray(String key, char[] values) {
    Editor editor = getEditor();
    String list = ((values != null) && (values.length > 0)) ? Arrays.toString(values) : null;
    editor.putString(getHash(key), list);
    return editor;
  }

  private TextUtils.StringSplitter getStringsSplitter(String list) {
    TextUtils.StringSplitter splitter = null;
    if(list != null) {
      int len = list.length();
      int lastIndex = len - 1;
      if((list.charAt(0) == '[') && (list.charAt(lastIndex) == ']')) {
        splitter = new TextUtils.SimpleStringSplitter(',');
        splitter.setString(list.substring(1, lastIndex));
      }
    }
    return splitter;
  }
}