package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Property;

public class MeteorF8 extends MeteorXX {

    public void doEjectCatapult() {
        new MsgAction(false, this) {

            public void doAction(Object obj) {
                Aircraft aircraft = (Aircraft) obj;
                if (!Actor.isValid(aircraft)) {
                    return;
                } else {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 10D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    aircraft.pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += FM.Vwld.x;
                    vector3d.y += FM.Vwld.y;
                    vector3d.z += FM.Vwld.z;
                    new EjectionSeat(11, loc, vector3d, aircraft);
                    return;
                }
            }

        };
        hierMesh().chunkVisible("Seat_D0", false);
    }

    private void bailout() {
        if (overrideBailout)
            if (FM.AS.astateBailoutStep >= 0 && FM.AS.astateBailoutStep < 2) {
                if (FM.CT.cockpitDoorControl > 0.5F && FM.CT.getCockpitDoor() > 0.5F) {
                    FM.AS.astateBailoutStep = 11;
                    doRemoveBlisters();
                } else {
                    FM.AS.astateBailoutStep = 2;
                }
            } else if (FM.AS.astateBailoutStep >= 2 && FM.AS.astateBailoutStep <= 3) {
                switch (FM.AS.astateBailoutStep) {
                    case 2:
                        if (FM.CT.cockpitDoorControl < 0.5F)
                            doRemoveBlister1();
                        break;

                    case 3:
                        doRemoveBlisters();
                        break;
                }
                if (FM.AS.isMaster())
                    FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
                AircraftState aircraftstate = FM.AS;
                aircraftstate.astateBailoutStep = (byte) (aircraftstate.astateBailoutStep + 1);
                if (FM.AS.astateBailoutStep == 4)
                    FM.AS.astateBailoutStep = 11;
            } else if (FM.AS.astateBailoutStep >= 11 && FM.AS.astateBailoutStep <= 19) {
                byte byte0 = FM.AS.astateBailoutStep;
                if (FM.AS.isMaster())
                    FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
                AircraftState aircraftstate1 = FM.AS;
                aircraftstate1.astateBailoutStep = (byte) (aircraftstate1.astateBailoutStep + 1);
                if (byte0 == 11) {
                    FM.setTakenMortalDamage(true, null);
                    if ((FM instanceof Maneuver) && ((Maneuver) FM).get_maneuver() != 44) {
                        World.cur();
                        if (FM.AS.actor != World.getPlayerAircraft())
                            ((Maneuver) FM).set_maneuver(44);
                    }
                }
                if (FM.AS.astatePilotStates[byte0 - 11] < 99) {
                    doRemoveBodyFromPlane(byte0 - 10);
                    if (byte0 == 11) {
                        doEjectCatapult();
                        FM.setTakenMortalDamage(true, null);
                        FM.CT.WeaponControl[0] = false;
                        FM.CT.WeaponControl[1] = false;
                        FM.AS.astateBailoutStep = -1;
                        overrideBailout = false;
                        FM.AS.bIsAboutToBailout = true;
                        ejectComplete = true;
                        if (byte0 > 10 && byte0 <= 19)
                            EventLog.onBailedOut(this, byte0 - 11);
                    }
                }
            }
    }

    private final void doRemoveBlister1() {
        if (hierMesh().chunkFindCheck("Blister1_D0") != -1 && FM.AS.getPilotHealth(0) > 0.0F) {
            hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(false);
            Vector3d vector3d = new Vector3d();
            vector3d.set(FM.Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    private final void doRemoveBlisters() {
        for (int i = 2; i < 10; i++)
            if (hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1 && FM.AS.getPilotHealth(i - 1) > 0.0F) {
                hierMesh().hideSubTrees("Blister" + i + "_D0");
                Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister" + i + "_D0"));
                wreckage.collide(false);
                Vector3d vector3d = new Vector3d();
                vector3d.set(FM.Vwld);
                wreckage.setSpeed(vector3d);
            }

    }

    public void update(float f) {
        if ((FM.AS.bIsAboutToBailout || overrideBailout) && !ejectComplete && FM.getSpeedKMH() > 15F) {
            overrideBailout = true;
            FM.AS.bIsAboutToBailout = false;
            bailout();
        }
        super.update(f);
    }

    private boolean overrideBailout;
    private boolean ejectComplete;

    static {
        Class class1 = MeteorF8.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Meteor");
        Property.set(class1, "meshName", "3DO/Plane/MeteorF8/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "originCountry", PaintScheme.countryBritain);
        Property.set(class1, "yearService", 1948F);
        Property.set(class1, "yearExpired", 1961F);
        Property.set(class1, "FlightModel", "FlightModels/MeteorF8.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMeteorF8.class });
        Property.set(class1, "LOSElevation", 0.6731F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 9, 9, 9, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalBomb01", "_ExternalBomb02",
            "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12",
            "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalRock17",
            "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalDev17" });

    }
}
