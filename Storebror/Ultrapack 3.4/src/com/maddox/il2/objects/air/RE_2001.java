package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class RE_2001 extends RE_2001xyz implements TypeDiveBomber, TypeFighter {

    public RE_2001() {
        this.canopyF = 0.0F;
        this.tiltCanopyOpened = false;
        this.slideCanopyOpened = false;
        this.blisterRemoved = false;
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

    public float    canopyF;
    private boolean tiltCanopyOpened;
    private boolean slideCanopyOpened;
    private boolean blisterRemoved;

    static {
        Class class1 = RE_2001.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "RE.2001");
        Property.set(class1, "meshName_it", "3DO/Plane/RE-2001(it)/hier.him");
        Property.set(class1, "PaintScheme_it", new PaintSchemeFMPar00());
        Property.set(class1, "meshName", "3DO/Plane/RE-2001(multi)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitRE_2001.class });
        Property.set(class1, "FlightModel", "FlightModels/RE-2001.fmd");
        Property.set(class1, "LOSElevation", 0.9119F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 0, 0, 3, 3, 3, 3, 3, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalDev01" });
    }
}
