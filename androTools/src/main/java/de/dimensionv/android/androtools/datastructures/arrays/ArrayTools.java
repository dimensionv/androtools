// ////////////////////////////////////////////////////////////////////////////
//
// Author: Volkmar Seifert
// Description:
// A utility class providing functionality optimized array search and manipulation
// functionality.
// All methods in this class assume that the length of an array is equivalent to its
// capacity and *not* necessarily equal to the number of elements in the array. The
// current size of the array (meaning the actual number of elements the array is holding at
// that moment) has always to be passed in as a parameter.
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

import java.util.Arrays;

/**
 * <p>A utility class providing functionality optimized array search and manipulation
 * functionality.</p>
 * <p>All methods in this class assume that the length of an array is equivalent to its capacity and
 * <em>not</em> necessarily equal to the number of elements in the array. The current size of the
 * array (meaning the actual number of element the array is holding at that moment) has always to be
 * passed in as a parameter.</p>
 *
 * @author Volkmar Seifert &lt;vs@DimensionV.de&gt;
 * @version 1.0
 * @since API 2.0.0
 */
public final class ArrayTools {

  /**
   * Error string for size assertions gone wrong.
   *
   * @since Class 1.0, API 2.0.0
   */
  private static final String ASSERT_CURRENT_SIZE = "actualSize [%d] > array.length [%d]";

  /**
   * <p>Binary search algorithm for primitive {@code long} data type.</p> <p>Basically, this method
   * is the same as {@link Arrays#binarySearch(long[], int, int, long)}, with the major difference
   * that this implementation omits any kind of argument validation checks. These checks are omitted
   * in order to gain performance when used within any of the {@link LongSparseArray}
   * implementations.</p>
   *
   * @param array
   *     The {@code long} array to search.
   * @param actualSize
   *     The actual number of elements within the given array
   * @param value
   *     The value to look for
   *
   * @return The position of the value, if it was found, -1 if not found.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static int binarySearch(long[] array, int actualSize, long value) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }

    int lo = 0;
    int hi = actualSize - 1;

    while(lo <= hi) {
      final int mid = (lo + hi) >>> 1;
      final long midVal = array[mid];

      if(midVal < value) {
        lo = mid + 1;
      } else if(midVal > value) {
        hi = mid - 1;
      } else {
        return mid;  // value found
      }
    }
    return ~lo;  // value not present
  }

  /**
   * <p>Shifts the complete array by one element to the left, returning the element that is "pushed
   * out" of the array in the process.</p> <p>This is the generic implementation for all kinds of
   * objects.</p> <p>Since after the shift the left most element is free, it is set to {@code
   * null}.</p>
   * <p>This method takes the number of actual elements in the array as second parameter and uses
   * it as upper limit for shifting. This means an array will not be shifted by its capacity, but
   * by its real content, unless you specify {@code array.length} as second parameter explicitly,
   * yourself.</p>
   *
   * @param array
   *     The array to be shifted
   * @param actualSize
   *     The actual number of elements within the given array
   * @param <T>
   *     The actual type of the array, implicitly given by the expected return object.
   *
   * @return The "dropped out" element of the array.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static <T> T shiftLeft(T[] array, int actualSize) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }
    int shiftLength = actualSize - 1;
    T last = array[shiftLength];
    System.arraycopy(array, 0, array, 1, shiftLength);
    array[0] = null;
    return last;
  }

  /**
   * <p>Shifts the complete array by one element to the right, returning the element that is "pushed
   * out" of the array in the process.</p> <p>This is the generic implementation for all kinds of
   * objects.</p> <p>Since after the shift the right most element is free, it is set to {@code
   * null}.</p>
   * <p>This method takes the number of actual elements in the array as second parameter and uses
   * it as upper limit for shifting. This means an array will not be shifted by its capacity, but
   * by its real content, unless you specify {@code array.length} as second parameter excplicitly,
   * yourself.</p>
   *
   * @param array
   *     The array to be shifted
   * @param actualSize
   *     The actual number of elements within the given array
   * @param <T>
   *     The actual type of the array, implicitly given by the expected return object.
   *
   * @return The "dropped out" element of the array.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static <T> T shiftRight(T[] array, int actualSize) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }
    int shiftLength = actualSize - 1;
    T first = array[0];
    System.arraycopy(array, 1, array, 0, shiftLength);
    array[shiftLength] = null;
    return first;
  }

  /**
   * <p>Shifts the complete array by one element to the left, returning the element that is "pushed
   * out" of the array in the process.</p> <p>This is the spefic, optimized implementation for the
   * primitive {@code byte} data-type.</p> <p>Since after the shift the left most element is free,
   * it is set to {@code 0}.</p>
   * <p>This method takes the number of actual elements in the array as second parameter and uses
   * it as upper limit for shifting. This means an array will not be shifted by its capacity, but
   * by its real content, unless you specify {@code array.length} as second parameter excplicitly,
   * yourself.</p>
   *
   * @param array
   *     The array to be shifted
   * @param actualSize
   *     The actual number of elements within the given array
   *
   * @return The value of the "dropped out" element of the array.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static byte shiftLeft(byte[] array, int actualSize) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }
    int shiftLength = actualSize - 1;
    byte last = array[shiftLength];
    System.arraycopy(array, 0, array, 1, shiftLength);
    array[0] = 0;
    return last;
  }

  /**
   * <p>Shifts the complete array by one element to the right, returning the element that is "pushed
   * out" of the array in the process.</p> <p>This is the spefic, optimized implementation for the
   * primitive {@code byte} data-type.</p> <p>Since after the shift the right most element is free,
   * it is set to {@code 0}.</p>
   * <p>This method takes the number of actual elements in the array as second parameter and uses
   * it as upper limit for shifting. This means an array will not be shifted by its capacity, but
   * by its real content, unless you specify {@code array.length} as second parameter excplicitly,
   * yourself.</p>
   *
   * @param array
   *     The array to be shifted
   * @param actualSize
   *     The actual number of elements within the given array
   *
   * @return The value of the "dropped out" element of the array.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static byte shiftRight(byte[] array, int actualSize) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }
    int shiftLength = actualSize - 1;
    byte first = array[0];
    System.arraycopy(array, 1, array, 0, shiftLength);
    array[shiftLength] = 0;
    return first;
  }

  /**
   * <p>Shifts the complete array by one element to the left, returning the element that is "pushed
   * out" of the array in the process.</p> <p>This is the spefic, optimized implementation for the
   * primitive {@code int} data-type.</p> <p>Since after the shift the left most element is free, it
   * is set to {@code 0}.</p>
   * <p>This method takes the number of actual elements in the array as second parameter and uses
   * it as upper limit for shifting. This means an array will not be shifted by its capacity, but
   * by its real content, unless you specify {@code array.length} as second parameter excplicitly,
   * yourself.</p>
   *
   * @param array
   *     The array to be shifted
   * @param actualSize
   *     The actual number of elements within the given array
   *
   * @return The value of the "dropped out" element of the array.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static int shiftLeft(int[] array, int actualSize) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }
    int shiftLength = actualSize - 1;
    int last = array[shiftLength];
    System.arraycopy(array, 0, array, 1, shiftLength);
    array[0] = 0;
    return last;
  }

  /**
   * <p>Shifts the complete array by one element to the right, returning the element that is "pushed
   * out" of the array in the process.</p> <p>This is the spefic, optimized implementation for the
   * primitive {@code int} data-type.</p> <p>Since after the shift the right most element is free,
   * it is set to {@code 0}.</p>
   * <p>This method takes the number of actual elements in the array as second parameter and uses
   * it as upper limit for shifting. This means an array will not be shifted by its capacity, but
   * by its real content, unless you specify {@code array.length} as second parameter excplicitly,
   * yourself.</p>
   *
   * @param array
   *     The array to be shifted
   * @param actualSize
   *     The actual number of elements within the given array
   *
   * @return The value of the "dropped out" element of the array.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static int shiftRight(int[] array, int actualSize) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }
    int shiftLength = actualSize - 1;
    int first = array[0];
    System.arraycopy(array, 1, array, 0, shiftLength);
    array[shiftLength] = 0;
    return first;
  }

  /**
   * <p>Shifts the complete array by one element to the left, returning the element that is "pushed
   * out" of the array in the process.</p> <p>This is the spefic, optimized implementation for the
   * primitive {@code long} data-type.</p> <p>Since after the shift the left most element is free,
   * it is set to {@code 0}.</p>
   * <p>This method takes the number of actual elements in the array as second parameter and uses
   * it as upper limit for shifting. This means an array will not be shifted by its capacity, but
   * by its real content, unless you specify {@code array.length} as second parameter excplicitly,
   * yourself.</p>
   *
   * @param array
   *     The array to be shifted
   * @param actualSize
   *     The actual number of elements within the given array
   *
   * @return The value of the "dropped out" element of the array.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static long shiftLeft(long[] array, int actualSize) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }
    int shiftLength = actualSize - 1;
    long last = array[shiftLength];
    System.arraycopy(array, 0, array, 1, shiftLength);
    array[0] = 0;
    return last;
  }

  /**
   * <p>Shifts the complete array by one element to the right, returning the element that is "pushed
   * out" of the array in the process.</p> <p>This is the spefic, optimized implementation for the
   * primitive {@code long} data-type.</p> <p>Since after the shift the right most element is free,
   * it is set to {@code 0}.</p>
   * <p>This method takes the number of actual elements in the array as second parameter and uses
   * it as upper limit for shifting. This means an array will not be shifted by its capacity, but
   * by its real content, unless you specify {@code array.length} as second parameter excplicitly,
   * yourself.</p>
   *
   * @param array
   *     The array to be shifted
   * @param actualSize
   *     The actual number of elements within the given array
   *
   * @return The value of the "dropped out" element of the array.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static long shiftRight(long[] array, int actualSize) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }
    int shiftLength = actualSize - 1;
    long first = array[0];
    System.arraycopy(array, 1, array, 0, shiftLength);
    array[shiftLength] = 0;
    return first;
  }

  /**
   * <p>Shifts the complete array by one element to the left, returning the element that is "pushed
   * out" of the array in the process.</p> <p>This is the spefic, optimized implementation for the
   * primitive {@code float} data-type.</p> <p>Since after the shift the left most element is free,
   * it is set to {@code 0}.</p>
   * <p>This method takes the number of actual elements in the array as second parameter and uses
   * it as upper limit for shifting. This means an array will not be shifted by its capacity, but
   * by its real content, unless you specify {@code array.length} as second parameter excplicitly,
   * yourself.</p>
   *
   * @param array
   *     The array to be shifted
   * @param actualSize
   *     The actual number of elements within the given array
   *
   * @return The value of the "dropped out" element of the array.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static float shiftLeft(float[] array, int actualSize) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }
    int shiftLength = actualSize - 1;
    float last = array[shiftLength];
    System.arraycopy(array, 0, array, 1, shiftLength);
    array[0] = 0;
    return last;
  }

  /**
   * <p>Shifts the complete array by one element to the right, returning the element that is "pushed
   * out" of the array in the process.</p> <p>This is the spefic, optimized implementation for the
   * primitive {@code float} data-type.</p> <p>Since after the shift the right most element is free,
   * it is set to {@code 0}.</p>
   * <p>This method takes the number of actual elements in the array as second parameter and uses
   * it as upper limit for shifting. This means an array will not be shifted by its capacity, but
   * by its real content, unless you specify {@code array.length} as second parameter excplicitly,
   * yourself.</p>
   *
   * @param array
   *     The array to be shifted
   * @param actualSize
   *     The actual number of elements within the given array
   *
   * @return The value of the "dropped out" element of the array.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static float shiftRight(float[] array, int actualSize) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }
    int shiftLength = actualSize - 1;
    float first = array[0];
    System.arraycopy(array, 1, array, 0, shiftLength);
    array[shiftLength] = 0;
    return first;
  }

  /**
   * <p>Shifts the complete array by one element to the left, returning the element that is "pushed
   * out" of the array in the process.</p> <p>This is the spefic, optimized implementation for the
   * primitive {@code double} data-type.</p> <p>Since after the shift the left most element is free,
   * it is set to {@code 0}.</p>
   * <p>This method takes the number of actual elements in the array as second parameter and uses
   * it as upper limit for shifting. This means an array will not be shifted by its capacity, but
   * by its real content, unless you specify {@code array.length} as second parameter excplicitly,
   * yourself.</p>
   *
   * @param array
   *     The array to be shifted
   * @param actualSize
   *     The actual number of elements within the given array
   *
   * @return The value of the "dropped out" element of the array.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static double shiftLeft(double[] array, int actualSize) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }
    int shiftLength = actualSize - 1;
    double last = array[shiftLength];
    System.arraycopy(array, 0, array, 1, shiftLength);
    array[0] = 0;
    return last;
  }

  /**
   * <p>Shifts the complete array by one element to the right, returning the element that is "pushed
   * out" of the array in the process.</p> <p>This is the spefic, optimized implementation for the
   * primitive {@code double} data-type.</p> <p>Since after the shift the right most element is
   * free, it is set to {@code 0}.</p>
   * <p>This method takes the number of actual elements in the array as second parameter and uses
   * it as upper limit for shifting. This means an array will not be shifted by its capacity, but
   * by its real content, unless you specify {@code array.length} as second parameter excplicitly,
   * yourself.</p>
   *
   * @param array
   *     The array to be shifted
   * @param actualSize
   *     The actual number of elements within the given array
   *
   * @return The value of the "dropped out" element of the array.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static double shiftRight(double[] array, int actualSize) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }
    int shiftLength = actualSize - 1;
    double first = array[0];
    System.arraycopy(array, 1, array, 0, shiftLength);
    array[shiftLength] = 0;
    return first;
  }

  /**
   * <p>Shifts the complete array by one element to the left, returning the element that is "pushed
   * out" of the array in the process.</p> <p>This is the spefic, optimized implementation for the
   * primitive {@code char} data-type.</p> <p>Since after the shift the left most element is free,
   * it is set to {@code 0}.</p>
   * <p>This method takes the number of actual elements in the array as second parameter and uses
   * it as upper limit for shifting. This means an array will not be shifted by its capacity, but
   * by its real content, unless you specify {@code array.length} as second parameter excplicitly,
   * yourself.</p>
   *
   * @param array
   *     The array to be shifted
   * @param actualSize
   *     The actual number of elements within the given array
   *
   * @return The value of the "dropped out" element of the array.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static char shiftLeft(char[] array, int actualSize) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }
    int shiftLength = actualSize - 1;
    char last = array[shiftLength];
    System.arraycopy(array, 0, array, 1, shiftLength);
    array[0] = 0;
    return last;
  }

  /**
   * <p>Shifts the complete array by one element to the right, returning the element that is "pushed
   * out" of the array in the process.</p> <p>This is the spefic, optimized implementation for the
   * primitive {@code char} data-type.</p> <p>Since after the shift the right most element is free,
   * it is set to {@code 0}.</p>
   * <p>This method takes the number of actual elements in the array as second parameter and uses
   * it as upper limit for shifting. This means an array will not be shifted by its capacity, but
   * by its real content, unless you specify {@code array.length} as second parameter excplicitly,
   * yourself.</p>
   *
   * @param array
   *     The array to be shifted
   * @param actualSize
   *     The actual number of elements within the given array
   *
   * @return The value of the "dropped out" element of the array.
   *
   * @since Class 1.0, API 2.0.0
   */
  public static char shiftRight(char[] array, int actualSize) {
    if(actualSize > array.length) {
      throw new AssertionError(String.format(ASSERT_CURRENT_SIZE, actualSize, array.length));
    }
    int shiftLength = actualSize - 1;
    char first = array[0];
    System.arraycopy(array, 1, array, 0, shiftLength);
    array[shiftLength] = 0;
    return first;
  }
}