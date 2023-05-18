// Source File Name: F_86D45.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.weapons.GunNull;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class F_86D45 extends F_86D 
{

	public F_86D45() {
		removeChuteTimer = -1L;
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		bHasDeployedDragChute = false;
		this.setGunNullOwner();
	    FM.CT.bHasBayDoors = true;
	}
	
	public void update(float f){
		super.update(f);
		if(FM.CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
		{
	        chute = new Chute(this);
	        chute.setMesh("3do/plane/ChuteF86/mono.sim");
	        chute.collide(true);
	        chute.mesh().setScale(0.8F);
	        chute.pos.setRel(new Point3d(-5.0D, 0.0D, 0.5D), new Orient(0.0F, 90F, 0.0F));
			bHasDeployedDragChute = true;
		}
		else if (bHasDeployedDragChute  && FM.CT.bHasDragChuteControl){
			if(((FM.CT.DragChuteControl == 1.0F && (FM.getSpeedKMH() > 600F))|| FM.CT.DragChuteControl < 1.0F))
			{
				if(chute != null)
				{
					chute.tangleChute(this);
					chute.pos.setRel(new Point3d(-10D, 0.0D, 1D), new Orient(0.0F, 80F, 0.0F));
				}
				FM.CT.DragChuteControl = 0.0F;
				FM.CT.bHasDragChuteControl = false;
				FM.Sq.dragChuteCx = 0F;
				removeChuteTimer = Time.current() + 250L;
			}
			else if((FM.CT.DragChuteControl == 1.0F && (FM.getSpeedKMH() < 20F)))
			{
				if(chute != null)
					chute.tangleChute(this);
					chute.pos.setRel(new Orient(0.0F, 100F, 0.0F));
					FM.CT.DragChuteControl = 0.0F;
					FM.CT.bHasDragChuteControl = false;
					FM.Sq.dragChuteCx = 0F;
					removeChuteTimer = Time.current() + 10000L;
			}
		}
		if(removeChuteTimer > 0L && !FM.CT.bHasDragChuteControl)
		{
			if(Time.current() > (removeChuteTimer))
			{
				chute.destroy();
			}
		}
	}

	
	protected void moveBayDoor(float f) {
		this.resetYPRmodifier();
		Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 1.0F, 0.0F, -0.22F);
		this.hierMesh().chunkSetLocate("Launcher_D0", Aircraft.xyz,
				Aircraft.ypr);
	}

	private void setGunNullOwner() {
		try {
			for (int l = 0; l < FM.CT.Weapons.length; l++) {
				if (FM.CT.Weapons[l] != null) {
					for (int l1 = 0; l1 < FM.CT.Weapons[l].length; l1++) {
						if (FM.CT.Weapons[l][l1] != null) {
							if (FM.CT.Weapons[l][l1] instanceof GunNull) {
								((GunNull) FM.CT.Weapons[l][l1]).setOwner(this);
							}
						}
					}
				}
			}
		} catch (Exception exception) {
		}
	}

	public void rareAction(float paramFloat, boolean paramBoolean) {
		super.rareAction(paramFloat, paramBoolean);
		Maneuver maneuver = (Maneuver) FM;
		targeted = War.GetNearestEnemyAircraft(FM.actor, 2000F, 9);
		if(!FM.CT.Weapons[2][23].haveBullets()){
			((GunNull) FM.CT.Weapons[0][0]).emptyGun();
			((GunNull) FM.CT.Weapons[1][0]).emptyGun();
		}
		if ((!FM.isPlayers() || !(FM instanceof RealFlightModel) || !((RealFlightModel) FM).isRealMode()) && (FM instanceof Maneuver)) {
			FM.CT.weaponFireMode = 2;
			if (((Maneuver) FM).get_maneuver() == Maneuver.FVSB_FROM_AHEAD || ((Maneuver) FM).get_maneuver() == Maneuver.ATTACK_BOMBER && (maneuver.target != null) && FM.actor.pos.getAbsPoint().distance(targeted.pos.getAbsPoint()) < 1200F) {
				FM.CT.BayDoorControl = 1.0F;
			} else if ((maneuver.target == null) || FM.actor.pos.getAbsPoint().distance(targeted.pos.getAbsPoint()) >= 1500F) {

				FM.CT.BayDoorControl = 0.0F;
			}
		}
	}

	private static Actor targeted = null;
	private boolean bHasDeployedDragChute;
    private Chute chute;
    private long removeChuteTimer;

	static {
		Class localClass = com.maddox.il2.objects.air.F_86D45.class;
		new NetAircraft.SPAWN(localClass);
		Property.set(localClass, "iconFar_shortClassName", "F-86D");
		Property.set(localClass, "meshName", "3DO/Plane/F_86D(Multi1)/hier.him");
		Property.set(localClass, "PaintScheme", new PaintSchemeFMPar06());
		Property.set(localClass, "meshName_us", "3DO/Plane/F_86D(USA)/hier.him");
		Property.set(localClass, "PaintScheme_us", new PaintSchemeFMPar06());
		Property.set(localClass, "yearService", 1949.9F);
		Property.set(localClass, "yearExpired", 1960.3F);
		Property.set(localClass, "FlightModel", "FlightModels/F-86D-45.fmd");
		Property.set(localClass, "cockpitClass", new Class[]{
				com.maddox.il2.objects.air.CockpitF_86K.class
		});
		Property.set(localClass, "LOSElevation", 0.725F);
		Aircraft.weaponTriggersRegister(localClass, new int[]{
				9, 9, 9, 9, 2, 2, 2, 2, 2, 2,
				2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
				2, 2, 2, 2, 2, 2, 2, 2, 0, 1
		});
		Aircraft.weaponHooksRegister(localClass, new String[]{
				"_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_InternalRock01", "_InternalRock02", "_InternalRock03", "_InternalRock04", "_InternalRock05", "_InternalRock06",
				"_InternalRock07", "_InternalRock08", "_InternalRock09", "_InternalRock10", "_InternalRock11", "_InternalRock12", "_InternalRock13", "_InternalRock14", "_InternalRock15", "_InternalRock16",
				"_InternalRock17", "_InternalRock18", "_InternalRock19", "_InternalRock20", "_InternalRock21", "_InternalRock22", "_InternalRock23", "_InternalRock24", "_MGUN01", "_MGUN02"
		});
		com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "default", new java.lang.String[]{
				null, null, null, null, "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", 
				"RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4 1", "RocketGunFFARMk4 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", 
				"RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4 1", "RocketGunFFARMk4 1", "MGunNull 1", "MGunNull 1"
		});
		com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x120gal", new java.lang.String[]{
				"PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_TankC120galL 1", "FuelTankGun_TankC120galL 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", 
				"RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4 1", "RocketGunFFARMk4 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", 
				"RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4 1", "RocketGunFFARMk4 1", "MGunNull 1", "MGunNull 1"
		});
		com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x207gal", new java.lang.String[]{
				"PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_Tank207galL 1", "FuelTankGun_Tank207galL 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", 
				"RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4 1", "RocketGunFFARMk4 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", 
				"RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4Salvo 1", "RocketGunFFARMk4 1", "RocketGunFFARMk4 1", "MGunNull 1", "MGunNull 1"
		});
		com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "none", new java.lang.String[]{
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null
		});
	}
}