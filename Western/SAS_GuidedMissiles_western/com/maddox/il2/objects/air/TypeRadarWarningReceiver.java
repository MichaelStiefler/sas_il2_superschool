// Source File Name: TypeRadarWarningReceiver.java
// Author:           western0221
// Last Modified by: western0221 on 22nd/Jun./2018
package com.maddox.il2.objects.air;
import com.maddox.il2.engine.Actor;

public interface TypeRadarWarningReceiver {

	public abstract com.maddox.il2.objects.air.RadarWarningReceiverUtils getRadarWarningReceiverUtils();

	public abstract void myRadarSearchYou(Actor actor, String soundpreset);

	public abstract void myRadarLockYou(Actor actor, String soundpreset);
}
