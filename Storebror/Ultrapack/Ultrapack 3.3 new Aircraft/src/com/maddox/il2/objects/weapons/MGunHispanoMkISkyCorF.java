package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.GunProperties;

public class MGunHispanoMkISkyCorF extends MGunHispanoMkISkyFlame {

	public GunProperties createProperties() {
		GunProperties gunproperties = super.createProperties();
		gunproperties.shotFreq = 10.83333F;
		gunproperties.maxDeltaAngle = 0.24F;
		gunproperties.shotFreqDeviation = 0.02F;
		gunproperties.bullet[0].traceMesh = null;
		gunproperties.bullet[0].traceTrail = null;
		gunproperties.bullet[0].traceColor = 0;
		return gunproperties;
	}
}
