package de.dimensionv.android.androtools.general;

import android.content.Context;

/**
 * <p>An interface for use when the initialization of objects, providers, services, modules, etc.
 * needs to be deferred, e.g. until a password has been entered.</p> <p/> <p>Initialization routines
 * can then be triggered by calling the method {@link Initializer#onInitialize(Context,
 * TrimState)}.</p>
 *
 * @author Volkmar Seifert &lt;vs@DimensionV.de&gt;
 * @version 2.0
 * @since API 1.0.0
 */
@SuppressWarnings("UnusedDeclaration")
public interface Initializer {
  /**
   * This method triggers the initialization of its implementer.
   *
   * @param context
   *     The {@link Context}-object, because it might be necessary for the initialization.
   * @param trimState
   *     The current {@TrimState} of the app, which should be the same {@code TrimState} of all
   *     {@code Initializer}s.
   *
   * @since Interface 1.0, API 1.0.0
   */
  void onInitialize(Context context, TrimState trimState);
}
