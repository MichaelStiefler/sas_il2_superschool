package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class BF_109B1 extends BF_109 {

    public void registerPit(CockpitBF_109Bx cockpitbf_109bx) {
        this.pit = cockpitbf_109bx;
    }

    public BF_109B1() {
        this.sideWindowOpened = false;
        this.slideRWindow = false;
        this.sideWindow = false;
        this.pit = null;
        this.bChangedPit = true;
        this.canopyF = 0.0F;
        this.fMaxKMHSpeedForOpenCanopy = 250F;
        this.kangle = 0.0F;
        this.tiltCanopyOpened = false;
        this.bHasBlister = true;
    }

    public void update(float f) {
        if (this.FM.getSpeed() > 5F) {
            this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F));
            this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F));
        }
        this.hierMesh().chunkSetAngles("WaterL_D0", 0.0F, -38F * this.kangle, 0.0F);
        this.kangle = (0.95F * this.kangle) + (0.05F * this.FM.EI.engines[0].getControlRadiator());
        if (this.kangle > 1.0F) {
            this.kangle = 1.0F;
        }
        super.update(f);
        if ((this.FM.CT.getCockpitDoor() > 0.20000000000000001D) && this.bHasBlister && (this.FM.getSpeedKMH() > this.fMaxKMHSpeedForOpenCanopy) && (this.hierMesh().chunkFindCheck("Blister1_D0") != -1) && !this.sideWindowOpened) {
            try {
                if (this == World.getPlayerAircraft()) {
                    ((CockpitBF_109Bx) Main3D.cur3D().cockpitCur).removeCanopy();
                }
            } catch (Exception exception) {
            }
            this.hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
            this.bHasBlister = false;
            this.FM.CT.bHasCockpitDoorControl = false;
            this.FM.setGCenter(-0.5F);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        float f1 = 0.8F;
        float f2 = (-0.5F * (float) Math.cos((f / f1) * Math.PI)) + 0.5F;
        if (f <= f1) {
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -78F * f2, 0.0F);
            hiermesh.chunkSetAngles("GearL2_D0", -24F * f2, 0.0F, 0.0F);
        }
        f2 = (-0.5F * (float) Math.cos(((f - (1.0F - f1)) / f1) * Math.PI)) + 0.5F;
        if (f >= (1.0F - f1)) {
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 78F * f2, 0.0F);
            hiermesh.chunkSetAngles("GearR2_D0", 24F * f2, 0.0F, 0.0F);
        }
        if (f > 0.99F) {
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -78F, 0.0F);
            hiermesh.chunkSetAngles("GearL2_D0", -24F, 0.0F, 0.0F);
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 78F, 0.0F);
            hiermesh.chunkSetAngles("GearR2_D0", 24F, 0.0F, 0.0F);
        }
        if (f < 0.01F) {
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, 0.0F);
            hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 0.0F);
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, 0.0F);
            hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 0.0F);
        }
    }

    protected void moveGear(float f) {
        float f1 = 0.9F - (((Wing) this.getOwner()).aircIndex(this) * 0.1F);
        float f2 = (-0.5F * (float) Math.cos((f / f1) * Math.PI)) + 0.5F;
        if (f <= f1) {
            this.hierMesh().chunkSetAngles("GearL3_D0", 0.0F, -78F * f2, 0.0F);
            this.hierMesh().chunkSetAngles("GearL2_D0", -24F * f2, 0.0F, 0.0F);
        }
        f2 = (-0.5F * (float) Math.cos(((f - (1.0F - f1)) / f1) * Math.PI)) + 0.5F;
        if (f >= (1.0F - f1)) {
            this.hierMesh().chunkSetAngles("GearR3_D0", 0.0F, 78F * f2, 0.0F);
            this.hierMesh().chunkSetAngles("GearR2_D0", 24F * f2, 0.0F, 0.0F);
        }
        if (f > 0.99F) {
            this.hierMesh().chunkSetAngles("GearL3_D0", 0.0F, -78F, 0.0F);
            this.hierMesh().chunkSetAngles("GearL2_D0", -24F, 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("GearR3_D0", 0.0F, 78F, 0.0F);
            this.hierMesh().chunkSetAngles("GearR2_D0", 24F, 0.0F, 0.0F);
        }
    }

    public void moveSteering(float f) {
        if (f > 77.5F) {
            f = 77.5F;
            this.FM.Gears.steerAngle = f;
        }
        if (f < -77.5F) {
            f = -77.5F;
            this.FM.Gears.steerAngle = f;
        }
        this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
    }

    public void moveCockpitDoor(float f) {
        if (f > this.canopyF) {
            if (((this.FM.Gears.onGround() && (this.FM.getSpeed() < 5F)) || this.tiltCanopyOpened) && (this.FM.isPlayers() || this.isNetPlayer())) {
                this.sideWindow = false;
                this.tiltCanopyOpened = true;
                this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 90F * f, 0.0F);
            } else {
                this.sideWindow = true;
                if ((this.pit != null) && (this.canopyF == 0.0F)) {
                    this.slideRWindow = this.pit.isViewRight();
                }
                this.sideWindowOpened = true;
                this.resetYPRmodifier();
                Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.24F);
                if (this.slideRWindow) {
                    this.hierMesh().chunkSetLocate("WindowR1_D0", Aircraft.xyz, Aircraft.ypr);
                } else {
                    this.hierMesh().chunkSetLocate("WindowL1_D0", Aircraft.xyz, Aircraft.ypr);
                }
            }
        } else if ((this.FM.Gears.onGround() && (this.FM.getSpeed() < 5F) && !this.sideWindowOpened) || this.tiltCanopyOpened) {
            this.sideWindow = false;
            this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 90F * f, 0.0F);
            if (f == 0.0F) {
                this.tiltCanopyOpened = false;
            }
        } else {
            this.sideWindow = true;
            this.resetYPRmodifier();
            Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.24F);
            if (this.slideRWindow) {
                this.hierMesh().chunkSetLocate("WindowR1_D0", Aircraft.xyz, Aircraft.ypr);
            } else {
                this.hierMesh().chunkSetLocate("WindowL1_D0", Aircraft.xyz, Aircraft.ypr);
            }
            if (f == 0.0F) {
                this.sideWindowOpened = false;
            }
        }
        this.canopyF = f;
        if (this.canopyF < 0.01D) {
            this.canopyF = 0.0F;
        }
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public float    canopyF;
    private float   fMaxKMHSpeedForOpenCanopy;
    private float   kangle;
    public boolean  tiltCanopyOpened;
    public boolean  bHasBlister;
    private boolean sideWindowOpened;
    private boolean slideRWindow;
    public boolean  sideWindow;
    CockpitBF_109Bx pit;
    public boolean  bChangedPit;

    static {
        Class class1 = BF_109B1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bf109");
        Property.set(class1, "meshName", "3DO/Plane/BF_109B1/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "meshName_de", "3DO/Plane/BF_109B1/hier.him");
        Property.set(class1, "PaintScheme_de", new PaintSchemeFCSPar01());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1941F);
        Property.set(class1, "FlightModel", "FlightModels/Bf-109B-1SAS.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBF_109Bx.class });
        Property.set(class1, "LOSElevation", 0.74985F);
        Property.set(class1, "weaponsList", new ArrayList());
        Property.set(class1, "weaponsMap", new HashMapInt());
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON03", "_CANNON01", "_CANNON02" });
    }
}
