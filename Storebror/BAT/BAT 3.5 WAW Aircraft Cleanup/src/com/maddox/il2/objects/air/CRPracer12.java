package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Property;

public class CRPracer12 extends P_51 implements TypeBNZFighter {

    public CRPracer12() {
        this.bHasBlister = true;
        this.SecondProp = 0;
    }

    public void onAircraftLoaded() {
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.55F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        float f1 = (float) Math.sin(Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 3.141593F));
        this.hierMesh().chunkSetAngles("Pilot1_D0", 0.0F, 0.0F, 9F * f1);
        this.hierMesh().chunkSetAngles("Head1_D0", 12F * f1, 0.0F, 0.0F);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    protected void moveFan(float f) {
        this.hierMesh().chunkFind(Aircraft.Props[1][0]);
        this.SecondProp = 1;
        int i = 0;
        for (int j = 0; j < (this.SecondProp == 1 ? 2 : 1); j++) {
            if (this.oldProp[j] < 2) {
                i = Math.abs((int) (this.FM.EI.engines[0].getw() * 0.06F));
                if (i >= 1) {
                    i = 1;
                }
                if ((i != this.oldProp[j]) && this.hierMesh().isChunkVisible(Aircraft.Props[j][this.oldProp[j]])) {
                    this.hierMesh().chunkVisible(Aircraft.Props[j][this.oldProp[j]], false);
                    this.oldProp[j] = i;
                    this.hierMesh().chunkVisible(Aircraft.Props[j][i], true);
                }
            }
            if (i == 0) {
                this.propPos[j] = (this.propPos[j] + (57.3F * this.FM.EI.engines[0].getw() * f)) % 360F;
            } else {
                float f1 = 57.3F * this.FM.EI.engines[0].getw();
                f1 %= 2880F;
                f1 /= 2880F;
                if (f1 <= 0.5F) {
                    f1 *= 2.0F;
                } else {
                    f1 = (f1 * 2.0F) - 2.0F;
                }
                f1 *= 1200F;
                this.propPos[j] = (this.propPos[j] + (f1 * f)) % 360F;
            }
            if (j == 0) {
                this.hierMesh().chunkSetAngles(Aircraft.Props[j][i], 0.0F, this.propPos[j], 0.0F);
            } else {
                this.hierMesh().chunkSetAngles(Aircraft.Props[j][i], 0.0F, -(this.propPos[j] - 20F), 0.0F);
            }
        }

    }

    public void hitProp(int i, int j, Actor actor) {
        if ((i > (this.FM.EI.getNum() - 1)) || (this.oldProp[i] == 2)) {
            return;
        }
        if ((this.isChunkAnyDamageVisible("Prop" + (i + 1)) || this.isChunkAnyDamageVisible("PropRot" + (i + 1))) && (this.SecondProp == 1)) {
            this.hierMesh().chunkVisible(Aircraft.Props[i + 1][0], false);
            this.hierMesh().chunkVisible(Aircraft.Props[i + 1][1], false);
            this.hierMesh().chunkVisible(Aircraft.Props[i + 1][2], true);
        }
        super.hitProp(i, j, actor);
    }

    public void update(float f) {
        World.cur().diffCur.Torque_N_Gyro_Effects = false;
        super.update(f);
        super.update(f);
        this.FM.EI.engines[0].addVside *= 9.9999999999999995E-008D;
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        this.hierMesh().chunkSetAngles("Oil_D0", 0.0F, 15F * f1, 0.0F);
        this.hierMesh().chunkSetAngles("Water_D0", 0.0F, -12F + (15F * f1), 0.0F);
    }

    public void doEjectCatapult() {
        new MsgAction(false, this) {

            public void doAction(Object obj) {
                Aircraft aircraft = (Aircraft) obj;
                if (Actor.isValid(aircraft)) {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 10D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    ((Actor) (aircraft)).pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    new EjectionSeat(1, loc, vector3d, aircraft);
                }
            }

        };
    }

    public boolean bHasBlister;
    protected int  SecondProp;

    static {
        Class class1 = CRPracer12.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Racer");
        Property.set(class1, "meshName", "3DO/Plane/CRPracer12(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1945F);
        Property.set(class1, "yearExpired", 1980.5F);
        Property.set(class1, "FlightModel", "FlightModels/CRPracer.fmd:CRPracer_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitCRPracer.class });
        Property.set(class1, "LOSElevation", 1.03F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 9, 9, 9, 9, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_MGUN07", "_MGUN08", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21" });
    }
}
