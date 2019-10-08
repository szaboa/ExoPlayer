package com.google.android.exoplayer2.upstream;

import android.os.Handler;

/**
 * TODO
 *
 * @author Arnold Szabo
 * @since 10/07/2019
 */
public interface ThermalLevelMeter {

  /**
   * TODO
   */
  interface EventListener{

    /**
     * TODO
     * @param level
     */
    void onThermalLevelChanged(int level);
  }

  /**
   * TODO
   */
  interface MitigationListener {

    /**
     * TODO
     */
    void startMitigation();

    /**
     * TODO
     */
    void resetMitigation();
  }

  /**
   * TODO
   * @return
   */
  int getThermalLevel();

  /**
   * TODO
   * @param mitigationListener
   */
  void setMitigationListener(MitigationListener mitigationListener);

  /**
   * Adds an {@link ThermalLevelMeter.EventListener}.
   *
   * @param eventHandler A handler for events.
   * @param eventListener A listener of events.
   */
  void addEventListener(Handler eventHandler, ThermalLevelMeter.EventListener eventListener);

  /**
   * Removes an {@link ThermalLevelMeter.EventListener}.
   *
   * @param eventListener The listener to be removed.
   */
  void removeEventListener(ThermalLevelMeter.EventListener eventListener);
}
