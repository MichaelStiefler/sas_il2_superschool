package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class ME_262HGII extends ME_262 {

    public ME_262HGII() {
    }

    protected void moveRudder(float f) {
        this.updateControlsVisuals();
        if (this.FM.CT.getGear() > 0.75F) {
            this.hierMesh().chunkSetAngles("GearC21_D0", 0.0F, 40F * f, 0.0F);
        }
    }

    protected void moveElevator(float f) {
        this.updateControlsVisuals();
    }

    private final void updateControlsVisuals() {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, (-21.25F * this.FM.CT.getElevator()) - (21.25F * this.FM.CT.getRudder()), 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, (-21.25F * this.FM.CT.getElevator()) + (21.25F * this.FM.CT.getRudder()), 0.0F);
    }

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
                    vector3d.x += aircraft.FM.Vwld.x;
                    vector3d.y += aircraft.FM.Vwld.y;
                    vector3d.z += aircraft.FM.Vwld.z;
                    new EjectionSeat(2, loc, vector3d, aircraft);
                    return;
                }
            }

        };
        this.hierMesh().chunkVisible("Seat_D0", false);
    }

    static {
        Class class1 = ME_262HGII.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Me 262");
        Property.set(class1, "meshName", "3DO/Plane/Me-262HG-II/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1946F);
        Property.set(class1, "yearExpired", 1958.2F);
        Property.set(class1, "FlightModel", "FlightModels/Me-262HG-II.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitME_262HGII.class });
        Property.set(class1, "LOSElevation", 0.74615F);
        Property.set(class1, "weaponsList", new ArrayList());
        Property.set(class1, "weaponsMap", new HashMapInt());
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24" });
    }
}
