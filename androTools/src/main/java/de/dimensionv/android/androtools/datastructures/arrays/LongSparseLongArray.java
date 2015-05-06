package de.dimensionv.android.androtools.datastructures.arrays;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p>This class extends {@link LongSparseArray} to map {@code long}s to {@code long}s.</p>
 *
 * @see LongSparseArray
 *
 * @version 1.0
 * @since API 2.0.0
 */
public class LongSparseLongArray extends LongSparseArray {

  /**
   * <p>The specific creator object for this {@code LongSparseArray}.</p>
   * <p>The creator object is a factory method pattern used by the {@link Parcelable} framework of
   * Android to pull an object out of "nothing" by calling this factory method and handing in the
   * {@link Parcel} object, into which the object was serialized before.</p>
   *
   * @since Class 1.0, API 2.0.0
   */
  public static final Parcelable.Creator<LongSparseLongArray> CREATOR = new Parcelable.Creator<LongSparseLongArray>() {
    /**
     * Actual factory method to create a real {@code LongSparseLongArray} object from a
     * {@link Parcel} object.
     *
     * @param source the {@code Parcel} object to read the data from.
     *
     * @return the newly created {@code LongSparseLongArray} object.
     *
     * @since Class 1.0, API 2.0.0
     */
    public LongSparseLongArray createFromParcel(Parcel source) {
      return new LongSparseLongArray(source);
    }

    /**
     * This factory method initializes an array of {@code LongSparseLongArray}s of the given size,
     * but doesn't add any "life" elements to the array. (meaning, the array will be initialized, but
     * every element will be {@code null}.
     *
     * @param size the desired size of the array.
     *
     * @return the initialized (and correctly capacitated/sized) but otherwise empty array.
     *
     * @since Class 1.0, API 2.0.0
     */
    public LongSparseLongArray[] newArray(int size) {
      return new LongSparseLongArray[size];
    }
  };

  /**
   * The actual value array
   *
   * @since Class 1.0, API 2.0.0
   */
  private long[] values;

  /**
   * Creates a new empty {@code LongSparseLongArray} with a default capacity of 10 elements.
   *
   * @since Class 1.0, API 2.0.0
   */
  public LongSparseLongArray() {
    this(10);
  }

  /**
   * <p>Creates a new empty {@code LongSparseLongArray} with the given {@code initialCapacity},
   * meaning no memory (re-)allocation are necessary when adding elements to the array until this
   * number of elements is reached.</p>
   *
   * @param initialCapacity The initial capacity of this newly created {@code LongSparseLongArray}.
   *
   * @since Class 1.0, API 2.0.0
   */
  public LongSparseLongArray(int initialCapacity) {
    super(initialCapacity);
  }

  /**
   * <p>Creates a new {@code LongSparseLongArray} from the given {@link Parcel} object's data.</p>
   *
   * @param source The {@code Parcel} object to fill the new {@code LongSparseLongArray} from.
   *
   * @since Class 1.0, API 2.0.0
   */
  private LongSparseLongArray(Parcel source) {
    super(source);
    values = ArraySizeTools.newOptimalSizedLongArray(Math.max(initialCapacity, size));
    source.readLongArray(values);
  }

  /**
   * {@inheritDoc}
   *
   * @return a new {@code LongSparseLongArray} object with the same content as the current one.
   * @throws CloneNotSupportedException
   *
   * @since Class 1.0, API 2.0.0
   */
  @Override
  public LongSparseArray clone() throws CloneNotSupportedException {
    LongSparseLongArray clone = (LongSparseLongArray) super.clone();
    clone.values = values.clone();
    return clone;
  }

  /**
   * {@inheritDoc}
   * <p>This specific implementation composes a {@link String} by iterating over the {@code key}/
   * {@code value}-pairs and adding them as a comma-separated list of assignments.</p>
   * <p><strong>Example:</strong></p>
   * <dl>
   * <dt>For an empty array: </dt>
   * <dd>{@code {} }</dd>
   * <dt>For an array with 3 pairs: </dt>
   * <dd>{@code {1=1929388371923, 3=1283749, 5=7346218128} }</dd>
   * </dl>
   *
   * @since Class 1.0, API 2.0.0
   */
  @Override
  public String toString() {
    if(size < 1) {
      return "{}";
    }

    StringBuilder buffer = new StringBuilder(size * 28);
    buffer.append('{');
    for(int i = 0; i < size; i++) {
      long key = keys[i];
      buffer.append(key);
      buffer.append('=');
      long value = values[i];
      buffer.append(value);
      buffer.append(", ");
    }
    // remove last ", " because it's not necessary...
    buffer.delete(buffer.length() - 2, buffer.length());
    buffer.append('}');
    return buffer.toString();
  }

  /**
   * {@inheritDoc}
   * @param positionSource the position of the value that should be removed.
   * @param positionTarget the position where the rest of the array should be moved to
   * @param length the length of the rest of the array, minus the removed element and the part that is unchanged.
   *
   * @since Class 1.0, API 2.0.0
   */
  @Override
  protected void deleteValue(int positionSource, int positionTarget, int length) {
    System.arraycopy(values, positionSource, values, positionTarget, length);
  }

  /**
   * {@inheritDoc}
   *
   * @since Class 1.0, API 2.0.0
   */
  @Override
  protected void clearValues() {
    values = (initialCapacity == 0) ? EmptyArray.LONG : new long[keys.length];
  }

  /**
   * {@inheritDoc}
   * @param dest the {@code Parcel} object.
   * @param flags possible flags to respect / take care of while writing data to the {@code Parcel} object.
   *
   * @since Class 1.0, API 2.0.0
   */
  @Override
  protected void writeValuesToParcel(Parcel dest, int flags) {
    dest.writeLongArray(values);
  }

  /**
   * Gets the {@code long} value associated with the given {@code key}, or {@code 0} if the
   * {@code key} does not exist in this {@code LongSparseLongArray}.
   *
   * @param key
   *     The key / index to look up.
   *
   * @return the value associated with {@code key} or {@code 0} if {@code key} could not be
   * found.
   *
   * @since Class 1.0, API 2.0.0
   */
  public long get(long key) {
    return get(key, 0);
  }

  /**
   * Gets the {@code long} value associated with the given {@code key}, or {@code defaultValue} if the
   * {@code key} does not exist in this {@code LongSparseLongArray}.
   *
   * @param key
   *     The key / index to look up.
   *
   * @return the value associated with {@code key} or {@code defaultValue} if {@code key} could not be
   * found.
   *
   * @since Class 1.0, API 2.0.0
   */
  public long get(long key, long defaultValue) {
    if(size == 0) {
      return defaultValue;
    }

    int position = ArrayTools.binarySearch(keys, size, key);
    return (position < 0) ? defaultValue : values[position];
  }

  /**
   * <p>Puts the the given {@code value} under the given {@code key} into the {@code LongSparseLongArray}.</p>
   * <p>If the {@code key} doesn't exist, it's added, otherwise it's current value is replaced with the
   * given one.</p>
   *
   * @param key the key under which the given {@code value} shall be put into the {@code LongSparseLongArray}.
   * @param value the value to put into the {@code LongSparseLongArray}.
   *
   * @since Class 1.0, API 2.0.0
   */
  public void put(long key, long value) {
    int position = (size > 0) ? ArrayTools.binarySearch(keys, size, key) : -1;

    if(position > -1) {
      values[position] = value;
    } else {
      position = ~position;

      keys = ArraySizeTools.insert(keys, size, position, key);
      values = ArraySizeTools.insert(values, size, position, value);
      size++;
    }
  }

  /**
   * <p>Returns a copy of the values associated with this {@code LongSparseLongArray} object.</p>
   * <p>Changing a value within the array returned by this method has no effect on the {@code
   * LongSparseLongArray} object's set of values.</p> <p>In case that the array is empty, an
   * empty array is returned, too.</p>
   *
   * @return An array containing the values associated with this {@code LongSparseLongArray}.
   *
   * @since Class 1.0, API 2.0.0
   */
  public long[] getValues() {
    if(size == 0) {
      return EmptyArray.LONG;
    }
    long[] valuesCopy = new long[size];
    System.arraycopy(values, 0, valuesCopy, 0, size);
    return valuesCopy;
  }

  /**
   * <p>This method takes an arbitrary integer as index and returns the corresponding value.</p>
   * <p>If you use an {@code index < 0} or {@code index >= size()} an you will encounter an
   * {@link ArrayIndexOutOfBoundsException}.</p>
   *
   * @param index the index for the desired value.
   *
   * @return the value at the specified {@code index}
   *
   * @throws ArrayIndexOutOfBoundsException thrown in case of an invalid index (see above).
   *
   * @since Class 1.0, API 2.0.0
   */
  public long valueAt(int index) {
    if((index < 0) || (index >= size)) {
      throw new ArrayIndexOutOfBoundsException(index);
    }
    return values[index];
  }

  /**
   * <p>Returns the index for which {@link #valueAt} would return the specified value, or a negative
   * number if the specified value is not mapped.</p>
   *
   * <p>Please note, that the array is iterated from the beginning to the end, and the index of the
   * first exact match is returned. Potential sub-sequent matching values will not be seen by this
   * method.</p>
   *
   * @param value the value, for which the index shall be returned.
   *
   * @return the index of the {@code value} or a negative number if the specified key is not mapped.
   *
   * @since Class 1.0, API 2.0.0
   */
  public int indexOfValue(long value) {
    for(int i = 0; i < size; i++) {
      if(values[i] == value) {
        return i;
      }
    }

    return -1;
  }

  /**
   * <p>Adds a key/value pair to the array.</p> <p>This method is optimized for the case that the
   * key is greater than all existing keys in the array, but it will work with any other key and
   * insert the element into the appropriate place, as the {@code LongSparseLongArray} is always
   * sorted by keys in ascending order. This is because otherwise the binary search used for key
   * lookups could not work.</p> <p>If the key already exists, it will be replaced.</p>
   *
   * @param key
   *     The key that should be associated with with the {@code value}.
   * @param value
   *     The value that should be associated with the {@code key}.
   *
   * @since Class 1.0, API 2.0.0
   */
  public void append(long key, long value) {
    if(size != 0 && key <= keys[size - 1]) {
      put(key, value);
      return;
    }

    keys = ArraySizeTools.append(keys, size, key);
    values = ArraySizeTools.append(values, size, value);
    size++;
  }
}
