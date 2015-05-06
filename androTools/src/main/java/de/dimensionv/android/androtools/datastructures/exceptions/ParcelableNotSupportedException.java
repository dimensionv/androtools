package de.dimensionv.android.androtools.datastructures.exceptions;

import android.os.Parcelable;

/**
 * The {@code ParcelableNotSupportedException} is thrown when an arbitrary {@link Object} does not
 * implement the {@link Parcelable} interface, but it would be necessary for the current operation
 * to continue.
 *
 * @author Volkmar Seifert
 * @version 1.0
 * @since API 2.0
 */
public class ParcelableNotSupportedException extends RuntimeException {

  /**
   * Template for showing the local and global TrimState in logcat.
   *
   * @since Class 1.0, API 2.0.0
   */
  private static final String template = "Class [%s] does not support / implement the Parcelable interface.";

  /**
   * The class information of the (failed) object in question.
   *
   * @since Class 1.0, API 2.0.0
   */
  private Class<?> typeClass = null;

  /**
   * Creates a new {@code ParcelableNotSupportedException} with the given class information.
   *
   * @param typeClass the class information about the object that failed to be {@link Parcelable}
   *
   * @since Class 1.0, API 2.0.0
   */
  public ParcelableNotSupportedException(Class<?> typeClass) {
    super(String.format(template, typeClass.getCanonicalName()));
    this.typeClass = typeClass;
  }
}
