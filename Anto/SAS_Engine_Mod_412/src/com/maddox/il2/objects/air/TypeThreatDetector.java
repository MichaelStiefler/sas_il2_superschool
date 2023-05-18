// Source File Name: TypeThreatDetector.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.air;

public interface TypeThreatDetector {

  public abstract void setCommonThreatActive();

  public abstract void setRadarLockThreatActive();

  public abstract void setMissileLaunchThreatActive();
}
