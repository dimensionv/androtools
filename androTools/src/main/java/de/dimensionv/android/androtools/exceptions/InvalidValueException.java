/**
 * 
 */
package de.dimensionv.android.androtools.exceptions;

/**
 * @author mjoellnir
 * 
 */
@SuppressWarnings("serial")
public class InvalidValueException extends RuntimeException {
  private int value;

  /**
   *
   */
  public InvalidValueException(int value) {
    super();
    this.value = value;
  }

  /**
   * @param detailMessage
   * @param throwable
   */
  public InvalidValueException(int value, Throwable throwable) {
    super(throwable);
    this.value = value;
  }

  /**
   * @return the value
   */
  public int getValue() {
    return value;
  }
}
