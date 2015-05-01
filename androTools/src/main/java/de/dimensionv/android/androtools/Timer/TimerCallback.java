package de.dimensionv.android.androtools.Timer;

/**
 * Callback interface to schedule actions in the interval specified in the {@link Timer} class.
 *
 * @author Volkmar Seifert
 * @version 1.0
 * @since API 2.0
 */
public interface TimerCallback {
  /**
   * Execute scheduled action.
   * If the method returns true, a next action call will be scheduled for the next interval.
   * If this is not desired, return false.
   *
   * @return true, if another action call should be scheduled, false otherwise.
   *
   * @since Interface 1.0, API 2.0
   */
  boolean onTimerAction();
}
