package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Property;

public class BV_155 extends FW_190 {

    public BV_155() {
        this.kangle = 0.0F;
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 157F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 157F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC99_D0", 60F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 0.0F);
        float f1 = Math.max(-f * 1500F, -94F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -f1, 0.0F);
    }

    protected void moveGear(float f) {
        BV_155.moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGear() < 0.98F) {
            return;
        } else {
            this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
            return;
        }
    }

    public void setOnGround(Point3d point3d, Orient orient, Vector3d vector3d) {
        super.setOnGround(point3d, orient, vector3d);
        if (this.FM.isPlayers()) {
            this.FM.CT.cockpitDoorControl = 1.0F;
        }
    }

    public void update(float f) {
        for (int i = 1; i < 13; i++) {
            this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -10F * this.kangle, 0.0F);
        }

        this.kangle = (0.95F * this.kangle) + (0.05F * this.FM.EI.engines[0].getControlRadiator());
        float f1 = World.Rnd().nextFloat(0.87F, 1.04F);
        if (this.FM.isPlayers() && (this.FM.CT.cockpitDoorControl > 0.9F) && (this.FM.getSpeedKMH() > (180F * f1)) && (this.FM.AS.aircraft.hierMesh().chunkFindCheck("Blister1_D0") != -1) && (this.FM.AS.getPilotHealth(0) > 0.0F) && this.FM.isPlayers() && (this.FM.CT.cockpitDoorControl > 0.9F) && (this.FM.getSpeedKMH() > (180F * f1)) && (this.FM.AS.aircraft.hierMesh().chunkFindCheck("Wire_D0") != -1) && (this.FM.AS.getPilotHealth(0) > 0.0F)) {
            this.playSound("aircraft.arrach", true);
            if (this == World.getPlayerAircraft()) {
                ((CockpitFW_190D9early) Main3D.cur3D().cockpitCur).blowoffcanopyforCirXD9();
            }
            this.FM.AS.aircraft.hierMesh().hideSubTrees("Wire_D0");
            this.FM.AS.aircraft.hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage((ActorHMesh) this.FM.AS.actor, this.FM.AS.aircraft.hierMesh().chunkFind("Blister1_D0"));
            Wreckage wreckage1 = new Wreckage((ActorHMesh) this.FM.AS.actor, this.FM.AS.aircraft.hierMesh().chunkFind("Wire_D0"));
            wreckage.collide(true);
            wreckage1.collide(true);
            Vector3d vector3d = new Vector3d();
            vector3d.set(((FlightModelMain) (((SndAircraft) (this.FM.AS.aircraft)).FM)).Vwld);
            wreckage.setSpeed(vector3d);
            wreckage1.setSpeed(vector3d);
            this.FM.CT.cockpitDoorControl = 0.9F;
            this.FM.CT.bHasCockpitDoorControl = false;
            this.FM.VmaxAllowed = 161F;
            this.FM.Sq.dragEngineCx[0] *= 6.2F;
        }
        super.update(f);
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -0.53F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    private float kangle;

    static {
        Class class1 = BV_155.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "BV155");
        Property.set(class1, "meshName", "3DO/Plane/BV-155/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1944.6F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/BV155B.fmd:BV155FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190D9early.class });
        Property.set(class1, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 1, 1, 9, 9, 2, 2, 9, 9, 9, 3, 9, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON03", "_CANNON04", "_CANNON01", "_CANNON02", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalDev09", "_ExternalDev10", "_ExternalDev03", "_ExternalBomb03", "_ExternalDev04", "_ExternalBomb04", "_ExternalBomb05" });
    }
}
