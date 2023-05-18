package com.maddox.il2.game;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.EventLog;

/**
 * Source File Name:   NetSafeLog.java
 * @author Stiefler
 */
public class NetSafeLog {
  
  private NetSafeLog() {}
  
  private static boolean isLocalActor(Actor theActor) {
    if(theActor == World.getPlayerAircraft() && !theActor.isNetMirror())
      return true;
    return false;    
  }
  
  public static void log(Actor logActor, String logLine) {
    if(isLocalActor(logActor))
      HUD.log(logLine);
  }
  
  public static void log(Actor logActor, int i, String logLine) {
    if(isLocalActor(logActor))
      HUD.log(i, logLine);
  }

  public static void training(Actor logActor, String logLine) {
    if(isLocalActor(logActor))
      HUD.training(logLine);
  }
  
  public static void type(Actor logActor, String logLine) {
    if(isLocalActor(logActor))
      EventLog.type(logLine);
  }
}
