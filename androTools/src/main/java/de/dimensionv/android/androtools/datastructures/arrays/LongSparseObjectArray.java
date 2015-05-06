package de.dimensionv.android.androtools.datastructures.arrays;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;

import de.dimensionv.android.androtools.datastructures.exceptions.ParcelableNotSupportedException;

/**
 * <p>This class extends {@link LongSparseArray} to map arbitrary {@code Object}s of the given
 * type {@code T} to {@code long}s.</p>
 * <p>Arbitrary means this class can be used for any object. However, within the array, all objects
 * have to be of the same type, specified by {@code T}.</p>
 *
 * @see LongSparseArray
 *
 * @version 1.0
 * @since API 2.0.0
 */
public class LongSparseObjectArray<T> extends LongSparseArray {

  /**
   * <p>The specific creator object for this {@code LongSparseArray}.</p>
   * <p>The creator object is a factory method pattern used by the {@link Parcelable} framework of
   * Android to pull an object out of "nothing" by calling this factory method and handing in the
   * {@link Parcel} object, into which the object was serialized before.</p>
   *
   * @since Class 1.0, API 2.0.0
   */
  public static final Parcelable.Creator<LongSparseObjectArray> CREATOR = new Parcelable.Creator<LongSparseObjectArray>() {
    /**
     * Actual factory method to create a real {@code LongSparseObjectArray} object from a
     * {@link Parcel} object.
     *
     * @param source the {@code Parcel} object to read the data from.
     *
     * @return the newly created {@code LongSparseObjectArray} object.
     *
     * @since Class 1.0, API 2.0.0
     */
    public LongSparseObjectArray createFromParcel(Parcel source) {
      return new LongSparseObjectArray(source);
    }

    /**
     * This factory method initializes an array of {@code LongSparseObjectArray}s of the given size,
     * but doesn't add any "life" elements to the array. (meaning, the array will be initialized, but
     * every element will be {@code null}.
     *
     * @param size the desired size of the array.
     *
     * @return the initialized (and correctly capacitated/sized) but otherwise empty array.
     *
     * @since Class 1.0, API 2.0.0
     */
    public LongSparseObjectArray[] newArray(int size) {
      return new LongSparseObjectArray[size];
    }
  };


  private class InternalCreator implements Parcelable.Creator<T> {
    /**
     * Actual factory method to create a real {@code LongSparseObjectArray} object from a
     * {@link Parcel} object.
     *
     * @param source the {@code Parcel} object to read the data from.
     *
     * @return the newly created {@code LongSparseObjectArray} object.
     *
     * @throws ParcelableNotSupportedException when an {@link Object} does not support the {@link Parcelable} interface.
     *
     * @since Class 1.0, API 2.0.0
     */
    @SuppressWarnings("unchecked")
    public T createFromParcel(Parcel source) {
      return (T) source.readParcelable(null);
    }

    /**
     * This factory method initializes an array of {@code LongSparseObjectArray}s of the given size,
     * but doesn't add any "life" elements to the array. (meaning, the array will be initialized, but
     * every element will be {@code null}.
     *
     * @param size the desired size of the array.
     *
     * @return the initialized (and correctly capacitated/sized) but otherwise empty array.
     *
     * @since Class 1.0, API 2.0.0
     */
    public T[] newArray(int size) {
      return ArraySizeTools.newOptimalSizedArray(typeClass, size);
    }
  }

  /**
   * The actual value array
   *
   * @since Class 1.0, API 2.0.0
   */
  private final Class<T> typeClass;
  private final boolean isParcelable;
  private T[] values;

  /**
   * Creates a new empty {@code LongSparseObjectArray} with a default capacity of 10 elements.
   *
   * @since Class 1.0, API 2.0.0
   */
  public LongSparseObjectArray(Class<T> typeClass) {
    this(typeClass, 10);
  }

  /**
   * <p>Creates a new empty {@code LongSparseObjectArray} with the given {@code initialCapacity},
   * meaning no memory (re-)allocation are necessary when adding elements to the array until this
   * number of elements is reached.</p>
   *
   * @param initialCapacity The initial capacity of this newly created {@code LongSparseObjectArray}.
   *
   * @since Class 1.0, API 2.0.0
   */
  public LongSparseObjectArray(Class<T> typeClass, int initialCapacity) {
    super(initialCapacity);
    isParcelable = isTypeParcelable(typeClass);
    this.typeClass = typeClass;
  }

  /**
   * <p>Creates a new {@code LongSparseObjectArray} from the given {@link Parcel} object's data.</p>
   *
   * @param source The {@code Parcel} object to fill the new {@code LongSparseObjectArray} from.
   *
   * @since Class 1.0, API 2.0.0
   */
  @SuppressWarnings("unchecked")
  private LongSparseObjectArray(Parcel source) {
    super(source);

    String className = source.readString();
    Class<?> typeClass;
    try {
      typeClass = Class.forName(className);
    } catch(Exception ex) {
      // wrap all possible exceptions into a runtime exception and pass it on
      throw new RuntimeException(ex);
    }

    isParcelable = isTypeParcelable(typeClass);
    this.typeClass = (Class<T>) typeClass;
    values = (T[]) ArraySizeTools.newOptimalSizedArray(typeClass, initialCapacity);

    if(isParcelable) {
      source.readTypedArray(values, new InternalCreator());
    } else if(typeClass.getSimpleName().equals("String")) {
      source.readStringArray((String[]) values);
    } else {
      values = null;
      throw new ParcelableNotSupportedException(typeClass);
    }

  }

  /**
   * {@inheritDoc}
   *
   * @return a new {@code LongSparseObjectArray} object with the same content as the current one.
   * @throws CloneNotSupportedException
   *
   * @since Class 1.0, API 2.0.0
   */
  @Override
  public LongSparseArray clone() throws CloneNotSupportedException {
    LongSparseObjectArray clone = (LongSparseObjectArray) super.clone();
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
   * <dd>{@code {1=[<object representation>], 3=[<object representation>], 5=[<object representation>]} }</dd>
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
      buffer.append("=[");
      T value = values[i];
      buffer.append(value);
      buffer.append("], ");
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
    if(typeClass == null) {
      return; // only call when typeClass has already been initialized...
    }
    values = ArraySizeTools.newOptimalSizedArray(typeClass, initialCapacity);
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
    dest.writeString(typeClass.getCanonicalName());
    if(isParcelable) {
      dest.writeTypedArray((Parcelable[]) values, flags);
    } else if(typeClass.getSimpleName().equals("String")) {
      dest.writeStringArray((String[]) values);
    } else {
      throw new ParcelableNotSupportedException(typeClass);
    }
  }

  /**
   * Gets the {@code Object} value associated with the given {@code key}, or {@code null} if the
   * {@code key} does not exist in this {@code LongSparseObjectArray}.
   *
   * @param key
   *     The key / index to look up.
   *
   * @return the value associated with {@code key} or {@code null} if {@code key} could not be
   * found.
   *
   * @since Class 1.0, API 2.0.0
   */
  public T get(long key) {
    return get(key, null);
  }

  /**
   * Gets the {@code Object} value associated with the given {@code key}, or {@code defaultValue} if the
   * {@code key} does not exist in this {@code LongSparseObjectArray}.
   *
   * @param key
   *     The key / index to look up.
   *
   * @return the value associated with {@code key} or {@code defaultValue} if {@code key} could not be
   * found.
   *
   * @since Class 1.0, API 2.0.0
   */
  public T get(long key, T defaultValue) {
    if(size == 0) {
      return defaultValue;
    }

    int position = ArrayTools.binarySearch(keys, size, key);
    return (position < 0) ? defaultValue : values[position];
  }

  /**
   * <p>Puts the the given {@code value} under the given {@code key} into the {@code LongSparseObjectArray}.</p>
   * <p>If the {@code key} doesn't exist, it's added, otherwise it's current value is replaced with the
   * given one.</p>
   *
   * @param key the key under which the given {@code value} shall be put into the {@code LongSparseObjectArray}.
   * @param value the value to put into the {@code LongSparseObjectArray}.
   *
   * @since Class 1.0, API 2.0.0
   */
  public void put(long key, T value) {
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
   * <p>Returns a copy of the values associated with this {@code LongSparseObjectArray} object.</p>
   * <p>Changing a value within the array returned by this method has no effect on the {@code
   * LongSparseObjectArray} object's set of values.</p> <p>In case that the array is empty, an
   * empty array is returned, too.</p>
   *
   * @return An array containing the values associated with this {@code LongSparseObjectArray}.
   *
   * @since Class 1.0, API 2.0.0
   */
  @SuppressWarnings("unchecked")
  public T[] getValues() {
    if(size == 0) {
      return (T[]) Array.newInstance(typeClass.getComponentType(), 0);
    }
    T[] valuesCopy = (T[]) Array.newInstance(typeClass.getComponentType(), size);
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
  public T valueAt(int index) {
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
  public int indexOfValue(T value) {
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
   * insert the element into the appropriate place, as the {@code LongSparseObjectArray} is always
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
  public void append(long key, T value) {
    if(size != 0 && key <= keys[size - 1]) {
      put(key, value);
      return;
    }

    keys = ArraySizeTools.append(keys, size, key);
    values = ArraySizeTools.append(values, size, value);
    size++;
  }

  /**
   * Evaluates whether the given {@code typeClass} is implementing the {@link Parcelable} interface
   * or not.
   *
   * @param typeClass The class to check for the {@code Parcelable} interface
   * @return {@code true} if the {@code Parcelable} interface is implemented, {@code false} otherwise.
   *
   * @since Class 1.0, API 2.0.0
   */
  private static boolean isTypeParcelable(Class<?> typeClass) {
    Class<?>[] interfaces = typeClass.getInterfaces();
    boolean isParcelable = false;
    for(Class<?> entry : interfaces) {
      if(entry.getSimpleName().equals("Parcelable")) {
        isParcelable = true;
        break;
      }
    }

    // perform a deep check through the whole class hierachy of this object...
    Class<?> superClass = typeClass.getSuperclass();
    if(!isParcelable && (superClass != null)) {
      isParcelable = isTypeParcelable(superClass);
    }

    return isParcelable;
  }
}
