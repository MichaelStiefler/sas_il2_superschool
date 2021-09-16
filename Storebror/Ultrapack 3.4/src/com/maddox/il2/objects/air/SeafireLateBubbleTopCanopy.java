// This file is part of the SAS IL-2 Sturmovik 1946
// Late Seafire Mod package.
// If you copy, modify or redistribute this package, parts
// of this package or reuse sources from this package,
// we'd be happy if you could mention the origin including
// our web address
//
// www.sas1946.com
//
// Thank you for your cooperation!
//
// Original file source: 1C/Maddox/TD
// Modified by: SAS - Special Aircraft Services
//              www.sas1946.com
//
// Last Edited by: SAS~Storebror
// Last Edited at: 2013/03/11

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.game.Main3D;

public class SeafireLateBubbleTopCanopy extends SeafireLate {
	public SeafireLateBubbleTopCanopy() {
		bailingOut = false;
		canopyForward = false;
		okToJump = false;
		sideDoorOpened = false;
	}

	public void hitDaSilk() {
		if (okToJump)
			super.hitDaSilk();
		else if (FM.isPlayers() || isNetPlayer() || !FM.AS.isPilotDead(0)) {
			if (FM.CT.getCockpitDoor() < 1.0F && !bailingOut) {
				bailingOut = true;
				FM.AS.setCockpitDoor(this, 1);
			} else if (FM.CT.getCockpitDoor() == 1.0F && !bailingOut) {
				bailingOut = true;
				okToJump = true;
				canopyForward = true;
				super.hitDaSilk();
			}
		}
		if (!sideDoorOpened && FM.AS.bIsAboutToBailout && !FM.AS.isPilotDead(0)) {
			sideDoorOpened = true;
			FM.CT.forceCockpitDoor(0.0F);
			FM.AS.setCockpitDoor(this, 1);
		}
	}

	public void moveCockpitDoor(float f) {
		if (bailingOut && f >= 1.0F && !canopyForward) {
			canopyForward = true;
			FM.CT.forceCockpitDoor(0.0F);
			FM.AS.setCockpitDoor(this, 1);
		} else if (canopyForward) {
			hierMesh().chunkSetAngles("Blister2_D0", 0.0F, 160F * f, 0.0F);
			if (f >= 1.0F) {
				okToJump = true;
				super.hitDaSilk();
			}
		} else {
			Aircraft.xyz[0] = 0.0F;
			Aircraft.xyz[2] = 0.0F;
			Aircraft.ypr[0] = 0.0F;
			Aircraft.ypr[1] = 0.0F;
			Aircraft.ypr[2] = 0.0F;
			Aircraft.xyz[1] = f * 0.548F;
			hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
			float f1 = (float) Math.sin(Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 3.141593F));
			hierMesh().chunkSetAngles("Pilot1_D0", 0.0F, 0.0F, 9F * f1);
			hierMesh().chunkSetAngles("Head1_D0", 12F * f1, 0.0F, 0.0F);
		}
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
				Main3D.cur3D().cockpits[0].onDoorMoved(f);
			setDoorSnd(f);
		}
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (okToJump && FM.CT.getCockpitDoor() >= 1.0F && canopyForward && bailingOut && !FM.AS.bIsAboutToBailout) {
			AircraftState.bCheckPlayerAircraft = false;
			super.hitDaSilk();
			AircraftState.bCheckPlayerAircraft = false;
		}
	}

	private boolean bailingOut;
	private boolean canopyForward;
	private boolean okToJump;
	private boolean sideDoorOpened;
}
