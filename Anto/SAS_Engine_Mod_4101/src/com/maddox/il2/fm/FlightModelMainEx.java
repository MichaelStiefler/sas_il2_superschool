package com.maddox.il2.fm;

/**
 * This is a helper class in order to get access to the package level
 * protected "Arms" parameter of the FlightModel class
 * @author Storebror
 */
public class FlightModelMainEx {
  
  private FlightModelMainEx() {}

  public static Arm getFmArm(FlightModelMain theFM) {
    return theFM.Arms;
  }

  public static float getFmGCenter(FlightModelMain theFM) {
    return theFM.Arms.GCENTER;
  }
}
