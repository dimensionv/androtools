package de.dimensionv.android.androtools.general;

import android.app.Application.ActivityLifecycleCallbacks;

/**
 * <p>An interface for use memory usage aware apps that implement the
 * {@link ActivityLifecycleCallbacks}.</p>
 *
 * <p>Can be implemented by any long-existing class for being called by the activity lifecycle
 * callback methods {@link ActivityLifecycleCallbacks#onTrimMemory(int)} and
 * {@link ActivityLifecycleCallbacks#onLowMemory()}.</p>
 *
 * @author Volkmar Seifert &lt;vs@DimensionV.de&gt;
 * @version 1.0
 * @since API 1.0.0
 */
@SuppressWarnings("UnusedDeclaration")
public interface MemoryTrimmer {
  /**
   * <p>Relaxed release of memory</p>
   * <p>This is the method that should be called from
   * {@link ActivityLifecycleCallbacks#onTrimMemory(int)} on a level like
   * {@link ActivityLifecycleCallbacks#TRIM_MEMORY_BACKGROUND}.<br />
   * Though this is not mandatory, and this method can be run at any level, it is recommended
   * to have this method run at a lower level than the {@link MemoryTrimmer#onForceTrim()} method.</p>
   *
   * @since Interface 1.0, API 1.0.0
   */
  void onTrim();

  /**
   * <p>Strict release of all not strictly necessary memory resources.</p>
   * <p>The call to this method should also ensure that all resources that would be freed by
   * {@link MemoryTrimmer#onTrim()} are definitely freed.</p>
   * <p>This is the method that should be called from
   * {@link ActivityLifecycleCallbacks#onLowMemory()} or preferrably from
   * {@link ActivityLifecycleCallbacks#onTrimMemory(int)} with the
   * {@link ActivityLifecycleCallbacks#TRIM_MEMORY_COMPLETE} level.</p>
   *
   * @since Interface 1.0, API 1.0.0
   */
  void onForceTrim();
}
