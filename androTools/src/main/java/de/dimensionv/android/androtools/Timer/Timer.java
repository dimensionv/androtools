package de.dimensionv.android.androtools.Timer;

import android.content.Context;
import android.os.Handler;

import de.dimensionv.android.androtools.general.Initializer;
import de.dimensionv.android.androtools.general.MemoryTrimmer;
import de.dimensionv.android.androtools.general.TrimState;
import de.dimensionv.android.androtools.general.exceptions.TrimStateException;

/**
 * <p>{@code Timer} is a class for periodic (intervals) operations on the UI.</p>
 * <p>It runs using a {@link Handler} and its {@link Handler#postDelayed(Runnable, long)} method,
 * handing itself over with the given interval (or the default interval of 1 second, if none was
 * specified.).</p>
 *
 * @author Volkmar Seifert
 * @version 1.0
 * @since API 2.0
 */
public class Timer implements Runnable, Initializer, MemoryTrimmer {

  /**
   * Default interval: 1000 ms => 1 s
   *
   * @since Class 1.0, API 2.0.0
   */
  private static final long DEFAULT_INTERVAL = 1000L; // pause delay is one second -> 1000 ms

  /**
   * The {@link TimerCallback} interface to call when the {@code Timer} has reached the given interval time.
   *
   * @since Class 1.0, API 2.0.0
   */
  private final TimerCallback timerCallback;
  /**
   * The actual interval for the {@code Timer} to run
   *
   * @since Class 1.0, API 2.0.0
   */
  private final long interval;
  /**
   * The {@link Handler} object used to schedule the next run of the {@code Timer} in the given
   * interval.
   *
   * @since Class 1.0, API 2.0.0
   */
  private Handler postHandler = null;
  /**
   * A flag whether to schedule a new {@code Timer} run or not.
   *
   * @since Class 1.0, API 2.0.0
   */
  private boolean stopped = false;
  /**
   * <p>The current {@link TrimState} of this {@code Timer}.</p><p>This is necessary for the
   * {@link Initializer} and {@link MemoryTrimmer} interface implementations within the
   * {@code Timer} to work properly.</p>
   *
   * @since Class 1.0, API 2.0.0
   */
  private TrimState trimState = TrimState.NONE;

  /**
   * Creates a {@code Timer} object with a given {@link TimerCallback} object, using the default
   * interval of 1000 ms.
   *
   * @param timerCallback the {@code TimerCallback} interface to call when the interval has run out.
   *
   * @since Class 1.0, API 2.0.0
   */
  public Timer(TimerCallback timerCallback) {
    this(timerCallback, DEFAULT_INTERVAL);
  }

  /**
   * Creates a {@code Timer} object with a given {@link TimerCallback} object, and the interval specified
   * by the parameter {@code interval}.
   *
   * @param timerCallback the {@code TimerCallback} interface to call when the interval has run out.
   * @param interval The interval in which the Timer shall run and call the {@code TimerCallback}.
   *
   * @since Class 1.0, API 2.0.0
   */
  public Timer(TimerCallback timerCallback, long interval) {
    this.timerCallback = timerCallback;
    this.interval = interval;
  }

  /**
   * {@inheritDoc}
   *
   * @since Class 1.0, API 2.0.0
   */
  @Override
  public void run() {
    if(!stopped && timerCallback.onTimerAction()) {
      scheduleNext();
    }
  }


  /**
   * {@inheritDoc}
   *
   * This implementation re-initializes and if necessary starts the {@code Timer}.
   *
   * @since Class 1.0, API 2.0.0
   */
  @Override
  public void onInitialize(Context context, TrimState trimState) {
    if(this.trimState != trimState) {
      throw new TrimStateException(this.trimState, trimState);
    }

    switch(trimState) {
      case NONE: {
        initializeHandler();
        break;
      }
      case FORCE_TRIMMED: // handle FORCE_TRIMMED and TRIMMED equally...
      case TRIMMED: {
        shutdown();
        initializeHandler();
        break;
      }
      case INITIALIZED: {
        // paranoid safety fallback, should be unnecessary
        initializeHandler();
        break;
      }
    }
    this.trimState = TrimState.INITIALIZED;
  }

  /**
   * {@inheritDoc}
   *
   * This implementation will shutdown and stop the {@code Timer}.
   *
   * @since Class 1.0, API 2.0.0
   */
  @Override
  public void onTrim() {
    shutdown();
    trimState = TrimState.TRIMMED;
  }

  /**
   * {@inheritDoc}
   *
   * This implementation will shutdown and stop the {@code Timer}.
   *
   * @since Class 1.0, API 2.0.0
   */
  @Override
  public void onForceTrim() {
    shutdown();
    trimState = TrimState.FORCE_TRIMMED;
  }

  /**
   * Starts the {@code Timer}.
   *
   * @since Class 1.0, API 2.0.0
   */
  public void start() {
    stopped = false;
    scheduleNext();
  }

  /**
   * Schedules the next run of the {@code Timer}.
   *
   * @since Class 1.0, API 2.0.0
   */
  public void scheduleNext() {
    if(!stopped && (postHandler != null)) {
      postHandler.postDelayed(this, interval);
    }
  }

  /**
   * Stops the {@code Timer}.
   *
   * @since Class 1.0, API 2.0.0
   */
  public void stop() {
    stopped = true;
    postHandler.removeCallbacks(this);
  }

  /**
   * Shuts the {@code Timer} down and by that stops the running instance.
   *
   * @since Class 1.0, API 2.0.0
   */
  public void shutdown() {
    if(postHandler != null) {
      stop();
      postHandler = null;
    }
  }

  /**
   * Initializes the {@link Handler} so that it is available for scheduling each {@code Timer} run.
   *
   * @since Class 1.0, API 2.0.0
   */
  private void initializeHandler() {
    if(postHandler == null) {
      postHandler = new Handler();
    }
  }
}
