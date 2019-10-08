package com.google.android.exoplayer2.upstream;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import androidx.annotation.Nullable;
import com.google.android.exoplayer2.util.EventDispatcher;

/**
 * TODO
 *
 * @author Arnold Szabo
 * @since 10/07/2019
 */
public class DefaultThermalLevelMeter implements ThermalLevelMeter {

  @Nullable
  private static DefaultThermalLevelMeter singletonInstance;

  /**
   * Returns a singleton instance of a {@link DefaultThermalLevelMeter} with default configuration.
   *
   * @param context A {@link Context}.
   * @return The singleton instance.
   */
  public static synchronized DefaultThermalLevelMeter getSingletonInstance(Context context) {
    if (singletonInstance == null) {
      singletonInstance = new DefaultThermalLevelMeter(context);
    }
    return singletonInstance;
  }

  private final EventDispatcher<ThermalLevelMeter.EventListener> eventDispatcher;

  private MitigationListener mitigationListener;
  private int thermalLevel;

  private DefaultThermalLevelMeter(@Nullable Context context) {
    this.eventDispatcher = new EventDispatcher<>();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      if (context != null) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
          powerManager.addThermalStatusListener(new PowerManager.OnThermalStatusChangedListener() {
            @Override
            public void onThermalStatusChanged(int newThermalLevel) {
              if (thermalLevel < PowerManager.THERMAL_STATUS_LIGHT
                  && newThermalLevel >= PowerManager.THERMAL_STATUS_LIGHT) {

                mitigationListener.startMitigation();

              } else if (thermalLevel > PowerManager.THERMAL_STATUS_LIGHT
                  && newThermalLevel < PowerManager.THERMAL_STATUS_LIGHT) {
                mitigationListener.resetMitigation();
              }

              thermalLevel = newThermalLevel;
              eventDispatcher.dispatch(listener -> listener.onThermalLevelChanged(thermalLevel));
            }
          });
        }
      }
    }
  }

  @Override
  public int getThermalLevel() {
    return thermalLevel;
  }

  @Override
  public void setMitigationListener(MitigationListener mitigationListener) {
    this.mitigationListener = mitigationListener;
  }

  @Override
  public void addEventListener(Handler eventHandler, EventListener eventListener) {
    eventDispatcher.addListener(eventHandler, eventListener);
  }

  @Override
  public void removeEventListener(EventListener eventListener) {
    eventDispatcher.removeListener(eventListener);
  }
}
