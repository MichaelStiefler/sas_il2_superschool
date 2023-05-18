package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class Kurogane extends Scheme1 implements TypeScout {

    public Kurogane() {
        Kurogane.bChangedPit = true;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            Kurogane.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            Kurogane.bChangedPit = true;
        }
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", -120F * f, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("GearL2_D0", -30F * f, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("GearR2_D0", -30F * f, 0.0F, 0.0F);
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        float f = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 4F, 0.0F, 4F);
        this.hierMesh().chunkSetAngles("GearL3_D0", 0.0F, 55F * f, 0.0F);
        this.hierMesh().chunkSetAngles("GearL4_D0", 0.0F, 55F * f, 0.0F);
        this.hierMesh().chunkSetAngles("GearL5_D0", 0.0F, 55F * f, 0.0F);
        Aircraft.xyz[2] = 0.63F * f;
        this.hierMesh().chunkSetLocate("GearL6_D0", Aircraft.xyz, Aircraft.ypr);
        this.hierMesh().chunkSetLocate("GearL7_D0", Aircraft.xyz, Aircraft.ypr);
        f = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 4F, 0.0F, 4F);
        this.hierMesh().chunkSetAngles("GearR3_D0", 0.0F, -55F * f, 0.0F);
        this.hierMesh().chunkSetAngles("GearR4_D0", 0.0F, -55F * f, 0.0F);
        this.hierMesh().chunkSetAngles("GearR5_D0", 0.0F, -55F * f, 0.0F);
        Aircraft.xyz[2] = 0.63F * f;
        this.hierMesh().chunkSetLocate("GearR6_D0", Aircraft.xyz, Aircraft.ypr);
        this.hierMesh().chunkSetLocate("GearR7_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void update(float f) {
        super.update(f);
        this.hierMesh().chunkSetAngles("GearL8_D0", 0.0F, -this.FM.Gears.gWheelAngles[0], 0.0F);
        this.hierMesh().chunkSetAngles("GearR8_D0", 0.0F, -this.FM.Gears.gWheelAngles[1], 0.0F);
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("Head1_D0", false);
                break;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxcontrols")) {
                int i = s.charAt(10) - 48;
                switch (i) {
                    case 1:
                        if ((this.getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 2.3F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Steering Wheel: Disabled..");
                        }
                        break;
                }
            }
            if (s.startsWith("xxeng1")) {
                Aircraft.debugprintln(this, "*** Engine Module: Hit..");
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(2.1F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 175000F)) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Crank Ball Bearing..");
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 50000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                        }
                        this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(12.7F, shot);
                } else if (s.startsWith("xxeng1cyls") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 4.4F), shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 1.12F))) {
                    this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                    Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                    if (World.Rnd().nextFloat() < (shot.power / 48000F)) {
                        this.FM.AS.hitEngine(shot.initiator, 0, 3);
                        Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
                    }
                    if (World.Rnd().nextFloat() < 0.005F) {
                        this.FM.AS.setEngineStuck(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Piston Head..");
                    }
                    this.getEnergyPastArmor(22.5F, shot);
                }
                return;
            }
            if (s.startsWith("xxtank1")) {
                int j = s.charAt(6) - 49;
                if ((this.getEnergyPastArmor(0.4F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.99F)) {
                    if (this.FM.AS.astateTankStates[j] == 0) {
                        Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                        this.FM.AS.hitTank(shot.initiator, j, 2);
                    }
                    if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.25F)) {
                        this.FM.AS.hitTank(shot.initiator, j, 2);
                        Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                    }
                }
            }
            return;
        }
        if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int k;
            if (s.endsWith("a")) {
                byte0 = 1;
                k = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                k = s.charAt(6) - 49;
            } else {
                k = s.charAt(5) - 49;
            }
            if (k == 2) {
                k = 1;
            }
            Aircraft.debugprintln(this, "*** hitFlesh..");
            this.hitFlesh(k, shot, byte0);
        } else if (s.startsWith("xcf") && (this.chunkDamageVisible("CF") < 2)) {
            this.hitChunk("CF", shot);
        }
    }

    public static boolean bChangedPit;
    static {
        Class class1 = Kurogane.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Kurogane");
        Property.set(class1, "meshName", "3do/plane/Kurogane/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "originCountry", PaintScheme.countryJapan);
        Property.set(class1, "yearService", 1936F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/Kurogane.fmd:Kurogane");
        Property.set(class1, "cockpitClass", new Class[] { CockpitKurogane.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { null });
    }
}
