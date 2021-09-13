package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class RE_2002m extends RE_2002xyz implements TypeDiveBomber {

    public RE_2002m() {
        this.canopyF = 0.0F;
        this.tiltCanopyOpened = false;
        this.slideCanopyOpened = false;
        this.blisterRemoved = false;
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -30F * f, 0.0F);
        this.arrestor = f;
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.tiltCanopyOpened && !this.blisterRemoved && (this.FM.getSpeed() > 75F)) {
            this.doRemoveBlister1();
        }
    }

    private final void doRemoveBlister1() {
        this.blisterRemoved = true;
        if (this.hierMesh().chunkFindCheck("Blister1_D0") != -1) {
            this.hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    public void moveCockpitDoor(float f) {
        if (f > this.canopyF) {
            if ((this.FM.Gears.onGround() && (this.FM.getSpeed() < 5F)) || this.tiltCanopyOpened) {
                this.tiltCanopyOpened = true;
                this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
            } else {
                this.slideCanopyOpened = true;
                this.resetYPRmodifier();
                Aircraft.xyz[0] = -0.01F;
                Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.3F);
                this.hierMesh().chunkSetLocate("Blister4L_D0", Aircraft.xyz, Aircraft.ypr);
                Aircraft.xyz[0] = 0.01F;
                this.hierMesh().chunkSetLocate("Blister4R_D0", Aircraft.xyz, Aircraft.ypr);
            }
        } else if ((this.FM.Gears.onGround() && (this.FM.getSpeed() < 5F) && !this.slideCanopyOpened) || this.tiltCanopyOpened) {
            this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
            if ((this.FM.getSpeed() > 50F) && (f < 0.6F) && !this.blisterRemoved) {
                this.doRemoveBlister1();
            }
            if (f == 0.0F) {
                this.tiltCanopyOpened = false;
            }
        } else {
            this.resetYPRmodifier();
            Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.3F);
            this.hierMesh().chunkSetLocate("Blister4L_D0", Aircraft.xyz, Aircraft.ypr);
            this.hierMesh().chunkSetLocate("Blister4R_D0", Aircraft.xyz, Aircraft.ypr);
            if (f == 0.0F) {
                this.slideCanopyOpened = false;
            }
        }
        this.canopyF = f;
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public boolean typeDiveBomberToggleAutomation() {
        return false;
    }

    public void typeDiveBomberAdjAltitudeReset() {
    }

    public void typeDiveBomberAdjAltitudePlus() {
    }

    public void typeDiveBomberAdjAltitudeMinus() {
    }

    public void typeDiveBomberAdjVelocityReset() {
    }

    public void typeDiveBomberAdjVelocityPlus() {
    }

    public void typeDiveBomberAdjVelocityMinus() {
    }

    public void typeDiveBomberAdjDiveAngleReset() {
    }

    public void typeDiveBomberAdjDiveAnglePlus() {
    }

    public void typeDiveBomberAdjDiveAngleMinus() {
    }

    public void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    protected void mydebug(String s) {
        System.out.println(s);
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.CT.getArrestor() > 0.2F) {
            if (this.FM.Gears.arrestorVAngle != 0.0F) {
                float f1 = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -26F, 11F, 1.0F, 0.0F);
                this.arrestor = (0.8F * this.arrestor) + (0.2F * f1);
                this.moveArrestorHook(this.arrestor);
            } else {
                float f2 = (-42F * this.FM.Gears.arrestorVSink) / 30F;
                if ((f2 < 0.0F) && (this.FM.getSpeedKMH() > 60F)) {
                    Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                }
                if ((f2 > 0.0F) && (this.FM.CT.getArrestor() < 0.95F)) {
                    f2 = 0.0F;
                }
                if (f2 > 0.0F) {
                    this.arrestor = (0.7F * this.arrestor) + (0.3F * (this.arrestor + f2));
                } else {
                    this.arrestor = (0.3F * this.arrestor) + (0.7F * (this.arrestor + f2));
                }
                if (this.arrestor < 0.0F) {
                    this.arrestor = 0.0F;
                } else if (this.arrestor > 1.0F) {
                    this.arrestor = 1.0F;
                }
                this.moveArrestorHook(this.arrestor);
            }
        }
    }

    protected float arrestor;
    public float    canopyF;
    private boolean tiltCanopyOpened;
    private boolean slideCanopyOpened;
    private boolean blisterRemoved;

    static {
        Class class1 = RE_2002m.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "RE.2002");
        Property.set(class1, "meshName_it", "3DO/Plane/RE-2002m(it)/hier.him");
        Property.set(class1, "PaintScheme_it", new PaintSchemeBMPar09());
        Property.set(class1, "meshName", "3DO/Plane/RE-2002m(multi)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitRE_2002m.class });
        Property.set(class1, "FlightModel", "FlightModels/RE-2002m.fmd");
        Property.set(class1, "LOSElevation", 0.9119F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 3, 3, 3, 3, 3, 9, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalTorp01", "_ExternalDev01", "_ExternalBomb05" });
    }
}
