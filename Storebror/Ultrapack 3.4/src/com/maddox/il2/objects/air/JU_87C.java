package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.AircraftTools;

public class JU_87C extends JU_87 {

    private boolean bDynamoOperational;
    private float   dynamoOrient;
    private float   arrestor2;
    private boolean bDynamoRotary;
    private int     pk;
    private boolean bGearJettisoned;
    private boolean bGearInitialized;
    private boolean bOldStatusAI;

    public JU_87C() {
        this.bDynamoOperational = true;
        this.dynamoOrient = 0.0F;
        this.arrestor2 = 0.0F;
        this.bDynamoRotary = false;
        this.bGearJettisoned = false;
        this.bGearInitialized = false;
        this.bOldStatusAI = false;
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        if (i == 36 || i == 37 || i == 10) {
            this.hierMesh().chunkVisible("GearR3_D0", false);
            this.hierMesh().chunkVisible("GearR3Rot_D0", false);
            this.bDynamoOperational = false;
        }
        return super.cutFM(i, j, actor);
    }

    private boolean isAI() {
        return (this != World.getPlayerAircraft() || !((RealFlightModel) this.FM).isRealMode()) && this.FM instanceof Pilot;
    }

    private void updateGearStatus() {
        float f = this.FM.getAltitude() - Landscape.HQ_Air((float) this.FM.Loc.x, (float) this.FM.Loc.y);
        if (f < 30F && !this.bGearJettisoned) {
            this.FM.Gears.rgear = true;
            this.FM.Gears.lgear = true;
        } else {
            this.FM.Gears.rgear = false;
            this.FM.Gears.lgear = false;
        }
        if (!this.bGearInitialized) {
            ((FlightModelMain) this.FM).CT.GearControl = 1.0F;
            ((FlightModelMain) this.FM).CT.setGear(1.0F);
            this.bGearInitialized = true;
        }
        if (this.isAI()) {
            if (this.bGearJettisoned) {
                ((FlightModelMain) this.FM).CT.GearControl = 0.0F;
                ((FlightModelMain) this.FM).CT.setGear(0.0F);
            } else {
                ((FlightModelMain) this.FM).CT.GearControl = 1.0F;
                ((FlightModelMain) this.FM).CT.setGear(1.0F);
            }
            ((FlightModelMain) this.FM).CT.bHasGearControl = false;
            this.bOldStatusAI = true;
        } else {
            if (!this.bGearJettisoned) {
                ((FlightModelMain) this.FM).CT.bHasGearControl = true;
                if (this.bOldStatusAI) {
                    ((FlightModelMain) this.FM).CT.GearControl = 1.0F;
                    ((FlightModelMain) this.FM).CT.setGear(1.0F);
                }
            }
            this.bOldStatusAI = false;
        }
    }

    protected void moveGear(float f) {
        if (this.isAI()) return;
        if (this.bGearJettisoned) {
            ((FlightModelMain) this.FM).CT.GearControl = 0.0F;
            ((FlightModelMain) this.FM).CT.setGear(0.0F);
            return;
        }
        if (!this.bGearJettisoned && f < 0.95F) {
            this.bGearJettisoned = true;
            super.cutFM(9, 0, this);
            super.cutFM(10, 0, this);
            ((FlightModelMain) this.FM).CT.GearControl = 0.0F;
            ((FlightModelMain) this.FM).CT.setGear(0.0F);
            ((FlightModelMain) this.FM).Gears.setOperable(false);
            ((FlightModelMain) this.FM).CT.bHasGearControl = false;
            this.FM.setGCenter(-0.5F);
            this.FM.GearCX = 0.0F;
            if (this == World.getPlayerAircraft()) HUD.log("Gear Jettisoned");
            return;
        } else return;
    }

    protected void moveWingFold(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("WingLFold", 100F * f, 22F * f, -90F * f);
        hiermesh.chunkSetAngles("WingRFold", -100F * f, -22F * f, -90F * f);
    }

    public void moveWingFold(float f) {
//        if (f < 0.001F) {
//            this.setGunPodsOn(true);
//            this.hideWingWeapons(false);
//        } else {
//            this.setGunPodsOn(false);
//            ((FlightModelMain) this.FM).CT.WeaponControl[0] = false;
//            this.hideWingWeapons(true);
//        }
        this.moveWingFold(this.hierMesh(), f);
        AircraftTools.updateExternalWeaponHooks(this);
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.6F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
            this.setDoorSnd(f);
        }
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -45F * f, 0.0F);
    }

    public void msgCollision(Actor actor, String s, String s1) {
        // System.out.println("Ju-87C msgCollision(" + actor.getClass().getName() + ", "
        // + s + ", " + s1 + ")");
        if (actor == this) if (s.equals(s1)) if (s.startsWith("Gear")) return;
        if ((!this.isNet() || !this.isNetMirror()) && !s.startsWith("Hook")) super.msgCollision(actor, s, s1);
    }

    protected void moveFan(float f) {
        if (this.bDynamoOperational) {
            this.pk = Math.abs((int) (((FlightModelMain) this.FM).Vwld.length() / 14D));
            if (this.pk >= 1) this.pk = 1;
        }
        if (this.bDynamoRotary != (this.pk == 1)) {
            this.bDynamoRotary = this.pk == 1;
            this.hierMesh().chunkVisible("GearR3_D0", !this.bDynamoRotary);
            this.hierMesh().chunkVisible("GearR3Rot_D0", this.bDynamoRotary);
        }
        this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 17.987F) % 360F : (float) (this.dynamoOrient - ((FlightModelMain) this.FM).Vwld.length() * 1.5444015264511108D) % 360F;
        this.hierMesh().chunkSetAngles("GearR3_D0", 0.0F, this.dynamoOrient, 0.0F);
        super.moveFan(f);
    }

    protected void moveAirBrake(float f) {
        this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 80F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 80F * f, 0.0F);
    }

    public void update(float f) {
        this.updateGearStatus();
        for (int i = 1; i < 9; i++)
            this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -15F * ((FlightModelMain) this.FM).EI.engines[0].getControlRadiator(), 0.0F);

        // this.FM.CT.bHasGearControl = false;
        super.update(f);
        // this.FM.CT.bHasGearControl = !bGearJettisoned;
        if (((FlightModelMain) this.FM).CT.getArrestor() > 0.9F) if (((FlightModelMain) this.FM).Gears.arrestorVAngle != 0.0F) {
            this.arrestor2 = Aircraft.cvt(((FlightModelMain) this.FM).Gears.arrestorVAngle, -65F, 3F, 45F, -23F);
            this.hierMesh().chunkSetAngles("Hook_D0", 0.0F, this.arrestor2, 0.0F);
            ((FlightModelMain) this.FM).Gears.getClass();
        } else {
            float f1 = -41F * ((FlightModelMain) this.FM).Gears.arrestorVSink;
            if (f1 < 0.0F && this.FM.getSpeedKMH() > 60F) Eff3DActor.New(this, ((FlightModelMain) this.FM).Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
            if (f1 > 0.0F && ((FlightModelMain) this.FM).CT.getArrestor() < 0.9F) f1 = 0.0F;
            if (f1 > 6.2F) f1 = 6.2F;
            this.arrestor2 += f1;
            if (this.arrestor2 < -23F) this.arrestor2 = -23F;
            else if (this.arrestor2 > 45F) this.arrestor2 = 45F;
            this.hierMesh().chunkSetAngles("Hook_D0", 0.0F, this.arrestor2, 0.0F);
        }
        if (this == World.getPlayerAircraft() && this.FM instanceof RealFlightModel) if (((RealFlightModel) this.FM).isRealMode()) switch (this.diveMechStage) {
            case 1:
                this.FM.CT.setTrimElevatorControl(-0.40F);
                this.FM.CT.trimElevator = -0.40F;
                break;
            case 2:
                if (this.FM.isTick(41, 0)) {
                    this.FM.CT.setTrimElevatorControl(0.40F);
                    this.FM.CT.trimElevator = 0.40F;
                }
                break;
        }
    }

    static {
        Class class1 = JU_87C.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "FlightModel", "FlightModels/Ju-87C.fmd");
        Property.set(class1, "meshName", "3do/plane/Ju-87C/hier.him");
        Property.set(class1, "iconFar_shortClassName", "Ju-87");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "cockpitClass", new Class[] { CockpitJU_87B2.class, CockpitJU_87B2_Gunner.class });
        Property.set(class1, "LOSElevation", 0.8499F);
        Property.set(class1, "yearService", 1939.9F);
        Property.set(class1, "yearExpired", 1945.5F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05" });
    }
}
