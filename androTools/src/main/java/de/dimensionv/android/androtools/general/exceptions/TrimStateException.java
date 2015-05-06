package de.dimensionv.android.androtools.general.exceptions;

import android.content.Context;

import de.dimensionv.android.androtools.general.Initializer;
import de.dimensionv.android.androtools.general.TrimState;

/**
 * The TrimStateException is thrown when an {@link Initializer} has a local trim-state that, upon
 * calling of the {@link Initializer#onInitialize(Context, TrimState)} method has a different state
 * than the one handed over two the {@code Initializer#onInitialize(Context, TrimState)} method.
 *
 * Since this state is not easily recoverable and usually hints at a programming error, this exception
 * is thrown by the {@code Initializer}, showing the local and the global TrimState.
 *
 * As this is some kind of illegal state, the TrimStateException is a direct sub-class of the
 * {@link IllegalStateException}.
 *
 * @author Volkmar Seifert
 * @version 1.0
 * @since API 2.0
 */
public class TrimStateException extends IllegalStateException {

  /**
   * Template for showing the local and global TrimState in logcat.
   *
   * @since Class 1.0, API 2.0.0
   */
  private static final String template = "TrimStates do not match: local TrimState is %s " +
      "while global TrimState is %s.";

  /**
   * Creates the TrimStateException object with a local TrimState (the one of the {@link Initializer})
   * and the global TrimState (the one given to the {@link Initializer#onInitialize(Context, TrimState)}
   * method.
   *
   * @param local TrimState of the {@code Initializer}
   * @param global TrimState handed over to the {@code onInitialize()} method
   *
   * @since Class 1.0, API 2.0.0
   */
  public TrimStateException(TrimState local, TrimState global) {
    super(String.format(template, local.name(), global.name()));
  }
}
