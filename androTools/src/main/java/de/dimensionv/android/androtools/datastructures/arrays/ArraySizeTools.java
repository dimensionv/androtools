// ////////////////////////////////////////////////////////////////////////////
//
// Author: Volkmar Seifert
// Description:
// A utility class providing functionality to primitive arrays that is comparable
// in performance to an ArrayList, and in parts even faster because of the omitted
// auto-boxing.
// All methods in this class assume that the length of an array is equivalent to its
// capacity and *not* necessarily equal to the number of elements in the array. The
// current size of the array (meaning the actual number of elements the array is holding at
// that moment) has always to be passed in as a parameter.
//
//
// ADMISSION OF INFLUENCE IN THE WORKS OF THIS FILE:
//
// A number of methods in this class were inspired and influenced by internal methods
// from the AOSP project. However, no method was copied over directly, and therefore
// I have not included references to either Google Inc. nor their Apache licence in
// my sources, because I am not using their sources. Instead, I re-implemented what
// I needed or found useful in my very own way.
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
package de.dimensionv.android.androtools.datastructures.arrays;

import java.lang.reflect.Array;

/**
 * <p>A utility class providing functionality to primitive arrays that is comparable in performance
 * to an ArrayList, and in parts even faster because of the omitted auto-boxing.</p>
 * <p>All methods in this class assume that the length of an array is equivalent to its capacity and
 * <em>not</em> necessarily equal to the number of elements in the array. The current size of the
 * array (meaning the actual number of element the array is holding at that moment) has always to be
 * passed in as a parameter.</p>
 *
 * @author Volkmar Seifert &lt;vs@DimensionV.de&gt;
 * @version 1.0
 * @since API 2.0.0
 */
public final class ArraySizeTools {

  /**
   * Error string for size assertions gone wrong.
   *
   * @since Class 1.0, API 2.0.0
   */
  private static final String ASSERT_CURRENT_SIZE = "actualSize [%d] > array.length [%d]";

  /**
   * <p>Appends an element to the end of the array, growing the array if there is no more room.</p>
   * <p>This variant of the append-method is for generic objects.</p>
   *
   * @param array
   *     The array to which to append the element. This must not be null, or you will get a {@link NullPointerException}.
   * @param actualSize
   *     The number of elements in the array. This value must be less than or equal to {@code array.length}.
   * @param value
   *     The element to append to the given {@code array}.
   *
   * @return the array to which the element was appended. The object returned here may actually be
   * a different array than the one given via the parameter {@code array}, in case a resizing was
   * necessary.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static <T> T[] append(T[] array, int actualSize, T value) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }

    if(actualSize + 1 > array.length) {
      @SuppressWarnings("unchecked")
      T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), calculateNewSize(actualSize));
      System.arraycopy(array, 0, newArray, 0, actualSize);
      array = newArray;
    }
    array[actualSize] = value;
    return array;
  }

  /**
   * <p>Appends an element to the end of the array, growing the array if there is no more room.</p>
   * <p>This variant of the append-method is optimized for arrays of the primitive data-type
   * {@code char}.</p>
   *
   * @param array
   *     The array to which to append the element. This must not be null, or you will get a {@link NullPointerException}.
   * @param actualSize
   *     The number of elements in the array. This value must be less than or equal to {@code array.length}.
   * @param value
   *     The element to append to the given {@code array}.
   *
   * @return the array to which the element was appended. The object returned here may actually be
   * a different array than the one given via the parameter {@code array}, in case a resizing was
   * necessary.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static char[] append(char[] array, int actualSize, char value) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }

    if(actualSize + 1 > array.length) {
      char[] newArray = new char[calculateNewSize(actualSize)];
      System.arraycopy(array, 0, newArray, 0, actualSize);
      array = newArray;
    }
    array[actualSize] = value;
    return array;
  }

  /**
   * <p>Appends an element to the end of the array, growing the array if there is no more room.</p>
   * <p>This variant of the append-method is optimized for arrays of the primitive data-type
   * {@code byte}.</p>
   *
   * @param array
   *     The array to which to append the element. This must not be null, or you will get a {@link NullPointerException}.
   * @param actualSize
   *     The number of elements in the array. This value must be less than or equal to {@code array.length}.
   * @param value
   *     The element to append to the given {@code array}.
   *
   * @return the array to which the element was appended. The object returned here may actually be
   * a different array than the one given via the parameter {@code array}, in case a resizing was
   * necessary.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static byte[] append(byte[] array, int actualSize, byte value) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }

    if(actualSize + 1 > array.length) {
      byte[] newArray = new byte[calculateNewSize(actualSize)];
      System.arraycopy(array, 0, newArray, 0, actualSize);
      array = newArray;
    }
    array[actualSize] = value;
    return array;
  }

  /**
   * <p>Appends an element to the end of the array, growing the array if there is no more room.</p>
   * <p>This variant of the append-method is optimized for arrays of the primitive data-type
   * {@code int}.</p>
   *
   * @param array
   *     The array to which to append the element. This must not be null, or you will get a {@link NullPointerException}.
   * @param actualSize
   *     The number of elements in the array. This value must be less than or equal to {@code array.length}.
   * @param value
   *     The element to append to the given {@code array}.
   *
   * @return the array to which the element was appended. The object returned here may actually be
   * a different array than the one given via the parameter {@code array}, in case a resizing was
   * necessary.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static int[] append(int[] array, int actualSize, int value) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }

    if(actualSize + 1 > array.length) {
      int[] newArray = new int[calculateNewSize(actualSize)];
      System.arraycopy(array, 0, newArray, 0, actualSize);
      array = newArray;
    }
    array[actualSize] = value;
    return array;
  }

  /**
   * <p>Appends an element to the end of the array, growing the array if there is no more room.</p>
   * <p>This variant of the append-method is optimized for arrays of the primitive data-type
   * {@code long}.</p>
   *
   * @param array
   *     The array to which to append the element. This must not be null, or you will get a {@link NullPointerException}.
   * @param actualSize
   *     The number of elements in the array. This value must be less than or equal to {@code array.length}.
   * @param value
   *     The element to append to the given {@code array}.
   *
   * @return the array to which the element was appended. The object returned here may actually be
   * a different array than the one given via the parameter {@code array}, in case a resizing was
   * necessary.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static long[] append(long[] array, int actualSize, long value) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }

    if(actualSize + 1 > array.length) {
      long[] newArray = new long[calculateNewSize(actualSize)];
      System.arraycopy(array, 0, newArray, 0, actualSize);
      array = newArray;
    }
    array[actualSize] = value;
    return array;
  }

  /**
   * <p>Appends an element to the end of the array, growing the array if there is no more room.</p>
   * <p>This variant of the append-method is optimized for arrays of the primitive data-type
   * {@code float}.</p>
   *
   * @param array
   *     The array to which to append the element. This must not be null, or you will get a {@link NullPointerException}.
   * @param actualSize
   *     The number of elements in the array. This value must be less than or equal to {@code array.length}.
   * @param value
   *     The element to append to the given {@code array}.
   *
   * @return the array to which the element was appended. The object returned here may actually be
   * a different array than the one given via the parameter {@code array}, in case a resizing was
   * necessary.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static float[] append(float[] array, int actualSize, float value) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }

    if(actualSize + 1 > array.length) {
      float[] newArray = new float[calculateNewSize(actualSize)];
      System.arraycopy(array, 0, newArray, 0, actualSize);
      array = newArray;
    }
    array[actualSize] = value;
    return array;
  }

  /**
   * <p>Appends an element to the end of the array, growing the array if there is no more room.</p>
   * <p>This variant of the append-method is optimized for arrays of the primitive data-type
   * {@code double}.</p>
   *
   * @param array
   *     The array to which to append the element. This must not be null, or you will get a {@link NullPointerException}.
   * @param actualSize
   *     The number of elements in the array. This value must be less than or equal to {@code array.length}.
   * @param value
   *     The element to append to the given {@code array}.
   *
   * @return the array to which the element was appended. The object returned here may actually be
   * a different array than the one given via the parameter {@code array}, in case a resizing was
   * necessary.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static double[] append(double[] array, int actualSize, double value) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }

    if(actualSize + 1 > array.length) {
      double[] newArray = new double[calculateNewSize(actualSize)];
      System.arraycopy(array, 0, newArray, 0, actualSize);
      array = newArray;
    }
    array[actualSize] = value;
    return array;
  }

  /**
   * <p>Appends an element to the end of the array, growing the array if there is no more room.</p>
   * <p>This variant of the append-method is optimized for arrays of the primitive data-type
   * {@code boolean}.</p>
   *
   * @param array
   *     The array to which to append the element. This must not be null, or you will get a {@link NullPointerException}.
   * @param actualSize
   *     The number of elements in the array. This value must be less than or equal to {@code array.length}.
   * @param value
   *     The element to append to the given {@code array}.
   *
   * @return the array to which the element was appended. The object returned here may actually be
   * a different array than the one given via the parameter {@code array}, in case a resizing was
   * necessary.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static boolean[] append(boolean[] array, int actualSize, boolean value) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }

    if(actualSize + 1 > array.length) {
      boolean[] newArray = new boolean[calculateNewSize(actualSize)];
      System.arraycopy(array, 0, newArray, 0, actualSize);
      array = newArray;
    }
    array[actualSize] = value;
    return array;
  }

  /**
   * <p>Inserts an element into the given array at the specified index, growing the array if
   * necessary.</p>
   * <p>This variant of the insert-method is for generic objects.</p>
   *
   * @param array
   *     The array into which to insert the element. This must not be null, or you will get a {@link NullPointerException}.
   * @param actualSize
   *     The number of elements in the array. This value must be less than or equal to {@code array.length}.
   * @param position
   *     The position to insert the {@code value} at within the given {@code array}.
   * @param value
   *     The element to insert into the given {@code array}.
   *
   * @return the array to which the element was appended. This may be different than the given
   * array.
   *
   * @since Class 1.0, API 2.0.0
   */
  @SuppressWarnings("unchecked")
  public static <T> T[] insert(T[] array, int actualSize, int position, T value) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }

    T[] temp;
    int offset = position + 1;

    if(actualSize + 1 > array.length) {
      temp = (T[]) Array.newInstance(array.getClass().getComponentType(), calculateNewSize(actualSize));
      System.arraycopy(array, 0, temp, 0, position);
      temp[position] = value;
      System.arraycopy(array, position, temp, offset, array.length - position);
    } else {
      System.arraycopy(array, position, array, offset, actualSize - position);
      array[position] = value;
      temp = array;
    }

    return temp;
  }

  /**
   * <p>Inserts an element into the given array at the specified index, growing the array if
   * necessary.</p>
   * <p>This variant of the insert-method is optimized for arrays of the primitive data-type
   * {@code char}.</p>
   *
   * @param array
   *     The array into which to insert the element. This must not be null, or you will get a {@link NullPointerException}.
   * @param actualSize
   *     The number of elements in the array. This value must be less than or equal to {@code array.length}.
   * @param position
   *     The position to insert the {@code value} at within the given {@code array}.
   * @param value
   *     The element to insert into the given {@code array}.
   *
   * @return the array to which the element was appended. This may be different than the given
   * array.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static char[] insert(char[] array, int actualSize, int position, char value) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }

    char[] temp;
    int offset = position + 1;

    if(actualSize + 1 > array.length) {
      temp = new char[calculateNewSize(actualSize)];
      System.arraycopy(array, 0, temp, 0, position);
      temp[position] = value;
      System.arraycopy(array, position, temp, offset, array.length - position);
    } else {
      System.arraycopy(array, position, array, offset, actualSize - position);
      array[position] = value;
      temp = array;
    }

    return temp;
  }

  /**
   * <p>Inserts an element into the given array at the specified index, growing the array if
   * necessary.</p>
   * <p>This variant of the insert-method is optimized for arrays of the primitive data-type
   * {@code byte}.</p>
   *
   * @param array
   *     The array into which to insert the element. This must not be null, or you will get a {@link NullPointerException}.
   * @param actualSize
   *     The number of elements in the array. This value must be less than or equal to {@code array.length}.
   * @param position
   *     The position to insert the {@code value} at within the given {@code array}.
   * @param value
   *     The element to insert into the given {@code array}.
   *
   * @return the array to which the element was appended. This may be different than the given
   * array.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static byte[] insert(byte[] array, int actualSize, int position, byte value) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }

    byte[] temp;
    int offset = position + 1;

    if(actualSize + 1 > array.length) {
      temp = new byte[calculateNewSize(actualSize)];
      System.arraycopy(array, 0, temp, 0, position);
      temp[position] = value;
      System.arraycopy(array, position, temp, offset, array.length - position);
    } else {
      System.arraycopy(array, position, array, offset, actualSize - position);
      array[position] = value;
      temp = array;
    }

    return temp;
  }

  /**
   * <p>Inserts an element into the given array at the specified index, growing the array if
   * necessary.</p>
   * <p>This variant of the insert-method is optimized for arrays of the primitive data-type
   * {@code int}.</p>
   *
   * @param array
   *     The array into which to insert the element. This must not be null, or you will get a {@link NullPointerException}.
   * @param actualSize
   *     The number of elements in the array. This value must be less than or equal to {@code array.length}.
   * @param position
   *     The position to insert the {@code value} at within the given {@code array}.
   * @param value
   *     The element to insert into the given {@code array}.
   *
   * @return the array to which the element was appended. This may be different than the given
   * array.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static int[] insert(int[] array, int actualSize, int position, int value) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }

    int[] temp;
    int offset = position + 1;

    if(actualSize + 1 > array.length) {
      temp = new int[calculateNewSize(actualSize)];
      System.arraycopy(array, 0, temp, 0, position);
      temp[position] = value;
      System.arraycopy(array, position, temp, offset, array.length - position);
    } else {
      System.arraycopy(array, position, array, offset, actualSize - position);
      array[position] = value;
      temp = array;
    }

    return temp;
  }

  /**
   * <p>Inserts an element into the given array at the specified index, growing the array if
   * necessary.</p>
   * <p>This variant of the insert-method is optimized for arrays of the primitive data-type
   * {@code long}.</p>
   *
   * @param array
   *     The array into which to insert the element. This must not be null, or you will get a {@link NullPointerException}.
   * @param actualSize
   *     The number of elements in the array. This value must be less than or equal to {@code array.length}.
   * @param position
   *     The position to insert the {@code value} at within the given {@code array}.
   * @param value
   *     The element to insert into the given {@code array}.
   *
   * @return the array to which the element was appended. This may be different than the given
   * array.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static long[] insert(long[] array, int actualSize, int position, long value) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }

    long[] temp;
    int offset = position + 1;

    if(actualSize + 1 > array.length) {
      temp = new long[calculateNewSize(actualSize)];
      System.arraycopy(array, 0, temp, 0, position);
      temp[position] = value;
      System.arraycopy(array, position, temp, offset, array.length - position);
    } else {
      System.arraycopy(array, position, array, offset, actualSize - position);
      array[position] = value;
      temp = array;
    }

    return temp;
  }

  /**
   * <p>Inserts an element into the given array at the specified index, growing the array if
   * necessary.</p>
   * <p>This variant of the insert-method is optimized for arrays of the primitive data-type
   * {@code boolean}.</p>
   *
   * @param array
   *     The array into which to insert the element. This must not be null, or you will get a {@link NullPointerException}.
   * @param actualSize
   *     The number of elements in the array. This value must be less than or equal to {@code array.length}.
   * @param position
   *     The position to insert the {@code value} at within the given {@code array}.
   * @param value
   *     The element to insert into the given {@code array}.
   *
   * @return the array to which the element was appended. This may be different than the given
   * array.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static boolean[] insert(final boolean[] array, int actualSize, int position, boolean value) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }

    boolean[] temp;
    int offset = position + 1;

    if(actualSize + 1 > array.length) {
      temp = new boolean[calculateNewSize(actualSize)];
      System.arraycopy(array, 0, temp, 0, position);
      temp[position] = value;
      System.arraycopy(array, position, temp, offset, array.length - position);
    } else {
      System.arraycopy(array, position, array, offset, actualSize - position);
      array[position] = value;
      temp = array;
    }

    return temp;
  }

  /**
   * <p>Inserts an element into the given array at the specified index, growing the array if
   * necessary.</p>
   * <p>This variant of the insert-method is optimized for arrays of the primitive data-type
   * {@code float}.</p>
   *
   * @param array
   *     The array into which to insert the element. This must not be null, or you will get a {@link NullPointerException}.
   * @param actualSize
   *     The number of elements in the array. This value must be less than or equal to {@code array.length}.
   * @param position
   *     The position to insert the {@code value} at within the given {@code array}.
   * @param value
   *     The element to insert into the given {@code array}.
   *
   * @return the array to which the element was appended. This may be different than the given
   * array.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static float[] insert(final float[] array, int actualSize, int position, float value) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }

    float[] temp;
    int offset = position + 1;

    if(actualSize + 1 > array.length) {
      temp = new float[calculateNewSize(actualSize)];
      System.arraycopy(array, 0, temp, 0, position);
      temp[position] = value;
      System.arraycopy(array, position, temp, offset, array.length - position);
    } else {
      System.arraycopy(array, position, array, offset, actualSize - position);
      array[position] = value;
      temp = array;
    }

    return temp;
  }

  /**
   * <p>Inserts an element into the given array at the specified index, growing the array if
   * necessary.</p>
   * <p>This variant of the insert-method is optimized for arrays of the primitive data-type
   * {@code double}.</p>
   *
   * @param array
   *     The array into which to insert the element. This must not be null, or you will get a {@link NullPointerException}.
   * @param actualSize
   *     The number of elements in the array. This value must be less than or equal to {@code array.length}.
   * @param position
   *     The position to insert the {@code value} at within the given {@code array}.
   * @param value
   *     The element to insert into the given {@code array}.
   *
   * @return the array to which the element was appended. This may be different than the given
   * array.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static double[] insert(double[] array, int actualSize, int position, double value) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }

    double[] temp;
    int offset = position + 1;

    if(actualSize + 1 > array.length) {
      temp = new double[calculateNewSize(actualSize)];
      System.arraycopy(array, 0, temp, 0, position);
      temp[position] = value;
      System.arraycopy(array, position, temp, offset, array.length - position);
    } else {
      System.arraycopy(array, position, array, offset, actualSize - position);
      array[position] = value;
      temp = array;
    }

    return temp;
  }

  /**
   * <p>Returns the ideal new size of an array depending on the size provided.</p> <p>In the current
   * implementation, this means the doubled size, but this may change in the future, so it should
   * not be relied upon.</p> <p>Furthermore, for sizes &lt;=4, this method always returns 8.</p>
   *
   * @param currentSize
   *     The current size of the array
   *
   * @return The new size of the array.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static int calculateNewSize(int currentSize) {
    return currentSize <= 4 ? 8 : currentSize << 2;
  }

  /**
   * <p>Trims the given {@code int} array to the given size and returns it.</p> <p>Please note that
   * the original array handed over to the method remains untouched by the operation. Instead, a new
   * array of the proper size is created and the data is copied over using efficient copy
   * methods.</p>
   *
   * @param array
   *     the int array to trim in size.
   * @param size
   *     the size to trim to.
   *
   * @return the new array of the trimmed sized.
   *
   * @since Class 1.0, API 2.0.0
   */
  @SuppressWarnings("UnusedDeclaration")
  public static int[] trim(int[] array, int size) {
    if(size > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, size, array.length));
    }

    if(size == 0) {
      return EmptyArray.INT;
    }

    int[] temp = new int[size];
    System.arraycopy(array, 0, temp, 0, size);
    return temp;
  }

  /**
   * <p>Trims the given {@code long} array to the given size and returns it.</p> <p>Please note that
   * the original array handed over to the method remains untouched by the operation. Instead, a new
   * array of the proper size is created and the data is copied over using efficient copy
   * methods.</p>
   *
   * @param array
   *     the long array to trim in size.
   * @param size
   *     the size to trim to.
   *
   * @return the new array of the trimmed sized.
   *
   * @since Class 1.0, API 2.0.0
   */
  @SuppressWarnings("UnusedDeclaration")
  public static long[] trim(long[] array, int size) {
    if(size > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, size, array.length));
    }

    if(size == 0) {
      return EmptyArray.LONG;
    }

    long[] temp = new long[size];
    System.arraycopy(array, 0, temp, 0, size);
    return temp;
  }

  /**
   * This method evaluates the optimal size of the array according to the desired array capacity in
   * accordance to the byte-alignment of the underlying data-type, in this case a {@code boolean}.
   *
   * @param capacity
   *     the desired capacity.
   *
   * @return The newly created boolean array. It's actual capacity can be greater than the desired
   * capacity.
   */
  public static boolean[] newOptimalSizedBooleanArray(int capacity) {
    return new boolean[optimalBooleanArraySize(capacity)];
  }

  /**
   * This method evaluates the optimal size of the array according to the desired array capacity in
   * accordance to the byte-alignment of the underlying data-type, in this case a {@code byte}.
   *
   * @param capacity
   *     the desired capacity.
   *
   * @return The newly created byte array. It's actual capacity can be greater than the desired
   * capacity.
   */
  public static byte[] newOptimalSizedByteArray(int capacity) {
    return new byte[optimalByteArraySize(capacity)];
  }

  public static char[] newOptimalSizedCharArray(int capacity) {
    return new char[optimalCharArraySize(capacity)];
  }

  /**
   * This method evaluates the optimal size of the array according to the desired array capacity in
   * accordance to the byte-alignment of the underlying data-type, in this case a {@code int}.
   *
   * @param capacity
   *     the desired capacity.
   *
   * @return The newly created int array. It's actual capacity can be greater than the desired
   * capacity.
   */
  public static int[] newOptimalSizedIntArray(int capacity) {
    return new int[optimalIntArraySize(capacity)];
  }

  /**
   * This method evaluates the optimal size of the array according to the desired array capacity in
   * accordance to the byte-alignment of the underlying data-type, in this case a {@code float}.
   *
   * @param capacity
   *     the desired capacity.
   *
   * @return The newly created int array. It's actual capacity can be greater than the desired
   * capacity.
   */
  public static float[] newOptimalSizedFloatArray(int capacity) {
    return new float[optimalFloatArraySize(capacity)];
  }

  public static double[] newOptimalSizedDoubleArray(int capacity) {
    return new double[optimalDoubleArraySize(capacity)];
  }

  /**
   * This method evaluates the optimal size of the array according to the desired array capacity in
   * accordance to the byte-alignment of the underlying data-type, in this case a {@code long}.
   *
   * @param capacity
   *     the desired capacity.
   *
   * @return The newly created long array. It's actual capacity can be greater than the desired
   * capacity.
   */
  public static long[] newOptimalSizedLongArray(int capacity) {
    return new long[optimalLongArraySize(capacity)];
  }

  /**
   * This method evaluates the optimal size of the array according to the desired array capacity in
   * accordance to the byte-alignment of the underlying data-type, in this case an {@code
   * Object}-reference.
   *
   * @param clazz
   *    the typed class of the underlying object of the array to be instantiated
   * @param capacity
   *     the desired capacity.
   *
   * @return The newly created array. It's actual capacity can be greater than the desired capacity.
   */
  @SuppressWarnings("unchecked")
  public static <T> T[] newOptimalSizedArray(Class<T> clazz, int capacity) {
    return (T[]) Array.newInstance(clazz.getComponentType(), optimalObjectArraySize(capacity));
  }

  /**
   * Evaluates the optimal array size for a byte array in accordance to the given desired size.
   *
   * @param desiredSize
   *     the desired size for the array.
   *
   * @return the actual optimal size for the byte-array.
   */
  public static int optimalByteArraySize(int desiredSize) {
    // calculate optimal byte aligment
    for(int i = 4; i < 32; i++) {
      if(desiredSize <= (1 << i) - 12) {
        return (1 << i) - 12;
      }
    }

    return desiredSize;
  }

  /**
   * Evaluates the optimal array size for a boolean array in accordance to the given desired size.
   *
   * @param desiredSize
   *     the desired size for the array.
   *
   * @return the actual optimal size for the boolean-array.
   */
  public static int optimalBooleanArraySize(int desiredSize) {
    return optimalByteArraySize(desiredSize);
  }

  /**
   * Evaluates the optimal array size for a char array in accordance to the given desired size.
   *
   * @param desiredSize
   *     the desired size for the array.
   *
   * @return the actual optimal size for the char-array.
   */
  public static int optimalCharArraySize(int desiredSize) {
    return optimalByteArraySize(desiredSize * 2) / 2;
  }

  /**
   * Evaluates the optimal array size for a int array in accordance to the given desired size.
   *
   * @param desiredSize
   *     the desired size for the array.
   *
   * @return the actual optimal size for the int-array.
   */
  public static int optimalIntArraySize(int desiredSize) {
    return optimalByteArraySize(desiredSize * 4) / 4;
  }

  /**
   * Evaluates the optimal array size for a float array in accordance to the given desired size.
   *
   * @param desiredSize
   *     the desired size for the array.
   *
   * @return the actual optimal size for the float-array.
   */
  public static int optimalFloatArraySize(int desiredSize) {
    return optimalByteArraySize(desiredSize * 4) / 4;
  }

  /**
   * Evaluates the optimal array size for a float array in accordance to the given desired size.
   *
   * @param desiredSize
   *     the desired size for the array.
   *
   * @return the actual optimal size for the float-array.
   */
  public static int optimalDoubleArraySize(int desiredSize) {
    return optimalByteArraySize(desiredSize * 8) / 8;
  }

  /**
   * Evaluates the optimal array size for an object array in accordance to the given desired size.
   *
   * @param desiredSize
   *     the desired size for the array.
   *
   * @return the actual optimal size for the object-array.
   */
  public static int optimalObjectArraySize(int desiredSize) {
    return optimalByteArraySize(desiredSize * 4) / 4;
  }

  /**
   * Evaluates the optimal array size for a long array in accordance to the given desired size.
   *
   * @param desiredSize
   *     the desired size for the array.
   *
   * @return the actual optimal size for the long-array.
   */
  public static int optimalLongArraySize(int desiredSize) {
    return optimalByteArraySize(desiredSize * 8) / 8;
  }
}
