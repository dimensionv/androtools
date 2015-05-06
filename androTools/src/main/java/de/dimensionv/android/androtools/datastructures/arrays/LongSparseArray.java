// ////////////////////////////////////////////////////////////////////////////
//
// Author: Volkmar Seifert
// Description:
// Base-class for all long key based sparse array structures.
// This class provides the basic general functionality common to all long
// sparse array classes.
// This includes the management of the long key structure itself, as well as
// provision of the basic infrastructure for Cloneable and Parcelable support.
//
//
// ADMISSION OF INFLUENCE IN THE WORKS OF THIS FILE:
//
// This class and all other LongSparseArray* classes were inspired and heavily
// influenced by the SparseArray classes from the AOSP project. However, no classe
// nor method was copied over directly, and therefore I have not included references
// to either Google Inc. nor their Apache licence in my sources, because I am not
// using their sources. Instead, I re-implemented what I needed or found useful in
// my very own way, which also resulted in a very different code structure. (You
// will notice that my LongSparseArrays use and benefit from inheritance whereas
// Google ignores such feats, re-implementing the same code in five different ways
// seven times all over again).
//
// ////////////////////////////////////////////////////////////////////////////
// License:
// // // // // // // // // // // // // // // // // // // //
// Copyright 2015 Volkmar Seifert <vs@dimensionv.de>.
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

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

/**
 * <p>Base-class for all long key based sparse array structures.</p>
 * <p>In general, all that applies to Google's {@link SparseArray} classes also applies to the
 * {@code LongSparseArray}-classes, as they are written in a similar mindset, only that the key here
 * is a {@code long}, not an {@code int}. Please refer to the {@link SparseArray}-documentation directly
 * to see what this implies.</p>
 * <p>This class provides the basic general functionality common to all long sparse array classes.
 * This includes the management of the long key structure itself, as well as provision of the basic
 * infrastructure for {@link Cloneable} and {@link Parcelable} support.</p>
 *
 * @author Volkmar Seifert &lt;vs@DimensionV.de&gt;
 * @version 1.0
 * @since API 2.0.0
 */
abstract public class LongSparseArray implements Cloneable, Parcelable {
  /**
   * <p>The array that holds the keys</p>
   * <p>The mapping to the values in the concrete implementation is done via index. The index of the
   * key in the key array correlates to the index of its value in the value array.</p>
   *
   * @since Class 1.0, API 2.0.0
   */
  protected long[] keys;
  /**
   * The actual size of the two arrays holding keys and values, with size referring to the number
   * of actual entries, not the capacity. Both arrays might differ in real capacity, while always
   * having the same number of entries.
   *
   * @since Class 1.0, API 2.0.0
   */
  protected int size;
  /**
   * The initial desired capacity for the array
   *
   * @since Class 1.0, API 2.0.0
   */
  protected int initialCapacity;

  /**
   * <p>Creates a new sparse array with the given initial capacity</p>
   *
   * <p>The initial capacity defines the number of elements the array can store before it has to
   * be resized. Since resizing is an expensive operation, it should be held to a minimum, and
   * initial capacity should be chosen wisely. Unlike ArrayLists, SparseArrays do not come with an
   * ensureCapacity() method that would provide a dynamic capacity enhancement on the go.</p>
   *
   * @param initialCapacity the initial capacity of the array
   *
   * @since Class 1.0, API 2.0.0
   */
  protected LongSparseArray(int initialCapacity) {
    this.initialCapacity = initialCapacity;
    clear();
  }

  /**
   * <p>Creates a new sparse array from a {@link Parcel} object.</p>
   *
   * @param source the {@code Parcel} object to load the sparse array from.
   *
   * @since Class 1.0, API 2.0.0
   */
  protected LongSparseArray(Parcel source) {
    initialCapacity = source.readInt();
    size = source.readInt();
    keys = ArraySizeTools.newOptimalSizedLongArray(Math.max(initialCapacity, size));
    source.readLongArray(keys);
  }

  /**
   * <p>Creates and returns a copy of this {@code LongSparseBooleanArray}.</p> <p>An actual deep
   * copy is created by this implementation of the clone method, not just a shallow copy, so that
   * the cloned object is truly independent from the original object.</p>
   *
   * @return a copy of this object.
   *
   * @throws CloneNotSupportedException
   *     if an object within the array does not implement the {@code Cloneable} interface.
   *
   * @since Class 1.0, API 2.0.0
   */
  @Override
  public LongSparseArray clone() throws CloneNotSupportedException {
    // create shallow copy of the object itself...
    LongSparseArray clone = (LongSparseArray) super.clone();
    // now clone reference objects...
    clone.keys = keys.clone();
    return clone;
  }

  /**
   * {@inheritDoc}
   *
   * @since Class 1.0, API 2.0.0
   */
  @Override
  public final void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(initialCapacity);
    dest.writeInt(size);
    dest.writeLongArray(keys);
    writeValuesToParcel(dest, flags);
  }

  /**
   * {@inheritDoc}
   *
   * @since Class 1.0, API 2.0.0
   */
  @Override
  public int describeContents() {
    return 0;
  }

  /**
   * <p>Remove an key/value-pair from the {@code LongSparseArray} object.</p>
   * <p>This removes the key/value-pair with the give {@code key} from the {@code LongSparseArray} object.</p>
   *
   * @param key the {@code key} of the key/value-pair that should be removed.
   *
   * @since Class 1.0, API 2.0.0
   */
  public void delete(long key) {
    if(size == 0) {
      return; // list is empty, don't bother searching or doing anything on it...
    }

    int position = ArrayTools.binarySearch(keys, size, key);

    if(position > -1) {
      System.arraycopy(keys, position + 1, keys, position, size - (position + 1));
      deleteValue(position + 1, position, size - (position + 1));
      size--;
    }
  }

  /**
   * <p>Removes key and value from a given index position, shifting the rest of the key/value-pairs
   * after the removed pair one position down in the structure so that the opened gap is closed
   * again.</p>
   * <p>The {@code LongSparseArray} may have gaps between the keys, but not within its internal
   * data-structures.</p>
   *
   * @param index the index of the key/value-pair that should be removed.
   *
   * @since Class 1.0, API 2.0.0
   */
  public void removeAt(int index) {
    if((index < 0) || (index >= size)) {
      throw new ArrayIndexOutOfBoundsException(index);
    }
    System.arraycopy(keys, index + 1, keys, index, size - (index + 1));
    deleteValue(index + 1, index, size - (index + 1));
    size--;
  }

  /**
   * Returns the number of key-value mappings that this SparseBooleanArray currently stores.
   *
   * @since Class 1.0, API 2.0.0
   */
  public int size() {
    return size;
  }

  /**
   * Convenience method to check whether this {@link LongSparseArray} is empty or not.
   *
   * @return {@code true}, if no element has been added so far, {@code false} otherwise.
   */
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * <p>Returns a copy of the keys associated with this {@code LongSparseBooleanArray} object.</p>
   * <p>Changing a value within the array returned by this method has no effect on the {@code
   * LongSparseBooleanArray} object's key set.</p> <p>In case that the array is empty, an empty
   * array is returned, too.</p>
   * <p>Please note that this method <em>always</em> creates a new array into which it copies the keys. This
   * newly created array is then returned.</p>
   *
   * @return A new array containing the keys associated with this {@code LongSparseBooleanArray}.
   *
   * @since Class 1.0, API 2.0.0
   */
  public long[] getKeys() {
    if(size == 0) {
      return EmptyArray.LONG;
    }

    long[] keysCopy = new long[size];
    System.arraycopy(keys, 0, keysCopy, 0, size);
    return keysCopy;
  }

  /**
   * <p>This method takes an arbitrary integer as index and returns the corresponding key, if there is
   * a key with this index.</p>
   * <p>If you use an {@code index < 0} or {@code index >= size()} an you will encounter an
   * {@link ArrayIndexOutOfBoundsException}.</p>
   *
   * <p>As the keys are guaranteed to be in ascending order (otherwise the binary search could not work),
   * this also implies that {@code keyAt(0)} returns the smallest key, and {@code key(size()-1)} returns
   * the largest key.</p>
   *
   * @param index the index for the desired key.
   *
   * @return the key at the specified {@code index}
   *
   * @throws ArrayIndexOutOfBoundsException thrown in case of an invalid index (see above).
   *
   * @since Class 1.0, API 2.0.0
   */
  public long keyAt(int index) {
    if((index < 0) || (index >= size)) {
      throw new ArrayIndexOutOfBoundsException(index);
    }
    return keys[index];
  }

  /**
   * Returns the index for which {@link #keyAt} would return the specified key, or a negative number
   * if the specified key is not mapped.
   *
   * @param key the key, for which the index shall be returned.
   *
   * @return the index of the {@code key} or a negative number if the specified key is not mapped.
   *
   * @since Class 1.0, API 2.0.0
   */
  public int indexOfKey(long key) {
    return ArrayTools.binarySearch(keys, size, key);
  }

  /**
   * Clears the {@code LongSparseArray} of all key/value mappings, frees the memory and
   * re-initializes the internal data-structures to the initial state.
   *
   * @since Class 1.0, API 2.0.0
   */
  public void clear() {
    keys = (initialCapacity == 0) ? EmptyArray.LONG
                                  : ArraySizeTools.newOptimalSizedLongArray(initialCapacity);
    clearValues();
    size = 0;
  }

  /**
   * <p>Deletes a single value from the position {@code positionSource}.</p>
   * <p>{@code positionTarget} indicates the first position where content after {@code positionSource}
   * shall be moved to within the array (usually {@code positionSource - 1}).</p>
   *
   * @param positionSource the position of the value that should be removed.
   * @param positionTarget the position where the rest of the array should be moved to
   * @param length the length of the rest of the array, minus the removed element and the part that is unchanged.
   *
   * @since Class 1.0, API 2.0.0
   */
  protected abstract void deleteValue(int positionSource, int positionTarget, int length);

  /**
   * Clears and resets the value array structure.
   *
   * @since Class 1.0, API 2.0.0
   */
  protected abstract void clearValues();

  /**
   * Writes the sparse array structure data into the given {@link Parcel} object.
   *
   * @param dest the {@code Parcel} object.
   * @param flags possible flags to respect / take care of while writing data to the {@code Parcel} object.
   *
   * @since Class 1.0, API 2.0.0
   */
  protected abstract void writeValuesToParcel(Parcel dest, int flags);
}
