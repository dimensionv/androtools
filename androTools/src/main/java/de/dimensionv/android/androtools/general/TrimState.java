package de.dimensionv.android.androtools.general;

import de.dimensionv.java.libraries.common.exceptions.InvalidEnumValueException;

/**
 * The TrimState enum represents the initialization and/or trimming state of the application and/or
 * {@link Initializer} classes. As a rule of thumb, there should only be one state concurrent state
 * throughout all instances of the {@code Initializer} classes of the app at the same time. Otherwise,
 * the onInitialize() should throw an {@link IllegalStateException} in order to indicate that an
 * illegal state has occurred.
 */
public enum TrimState {
  NONE,
  INITIALIZED,
  TRIMMED,
  FORCE_TRIMMED;

  private static TrimState[] VALUES = values();

  /**
   * This method takes an arbitrary int value and returns the appropriate enum-value In case the
   * int-value is not a valid value of the enum, an Exception will be thrown.
   *
   * @param value
   *     The integer value to convert into the enum (also called "ordinal")
   *
   * @return the enum-object associated with the given ordinal.
   *
   * @throws de.dimensionv.java.libraries.common.exceptions.InvalidEnumValueException
   *     if value is not a valid value for the enum.
   */
  public static TrimState fromOrdinal(int value) {
    try {
      return VALUES[value];
    } catch(ArrayIndexOutOfBoundsException ex) {
      throw new InvalidEnumValueException(value, VALUES.length);
    }
  }
}
