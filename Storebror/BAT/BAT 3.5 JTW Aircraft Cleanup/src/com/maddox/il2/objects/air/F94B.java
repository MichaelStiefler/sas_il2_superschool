package com.maddox.il2.objects.air;

import java.util.Random;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class F94B extends P_80 implements TypeFastJet, TypeFighterAceMaker, TypeFighter {

    public F94B() {
        this.lTimeNextEject = 0L;
        this.overrideBailout = false;
        this.ejectComplete = false;
        this.lTimeNextEject = 0L;
        this.emergencyEject = false;
        this.APmode1 = false;
        this.APmode2 = false;
        this.counter = 0;
        this.freq = 800;
        this.Timer1 = this.Timer2 = this.freq;
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Head2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
                return super.cutFM(34, j, actor);

            case 36:
                return super.cutFM(37, j, actor);

            case 11:
                this.cutFM(17, j, actor);
                this.FM.cut(17, j, actor);
                this.cutFM(18, j, actor);
                this.FM.cut(18, j, actor);
                return super.cutFM(i, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if ((this.FM.Gears.nearGround() || this.FM.Gears.onGround()) && (this.FM.CT.getCockpitDoor() == 1.0F)) {
            this.hierMesh().chunkVisible("HMask2_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Head2_D0"));
        }
        if ((this.counter++ % 5) == 0) {
            this.APG33();
        }
        super.rareAction(f, flag);
    }

    private boolean APG33() {
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        boolean flag1 = false;
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
        double d3 = d2 - Landscape.Hmin((float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y);
        if (aircraft instanceof F94B) {
            flag1 = true;
        }
        if (d3 < 0.0D) {
            d3 = 0.0D;
        }
        int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
        if (i < 0) {
            i += 360;
        }
        int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
        if (j < 0) {
            j += 360;
        }
        for (java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
            Actor actor = (Actor) entry.getValue();
            if (flag1 && (actor instanceof Aircraft) && (actor.getArmy() != World.getPlayerArmy()) && (actor != World.getPlayerAircraft()) && (actor.getSpeed(vector3d) > 20D)) {
                this.pos.getAbs(point3d);
                double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                new String();
                new String();
                double d7 = (int) (Math.ceil((d2 - d6) / 10D) * 10D);
                Engine.land();
                String s = "level with us";
                if ((d2 - d6 - 300D) >= 0.0D) {
                    s = "below us";
                }
                if (((d2 - d6) + 300D) <= 0.0D) {
                    s = "above us";
                }
                if (((d2 - d6 - 300D) < 0.0D) && ((d2 - d6 - 150D) >= 0.0D)) {
                    s = "slightly below";
                }
                if ((((d2 - d6) + 300D) > 0.0D) && (((d2 - d6) + 150D) < 0.0D)) {
                    s = "slightly above";
                }
                new String();
                double d8 = d4 - d;
                double d9 = d5 - d1;
                float f = 57.32484F * (float) Math.atan2(d9, -d8);
                int k = (int) (Math.floor((int) f) - 90D);
                if (k < 0) {
                    k += 360;
                }
                int l = k - i;
                double d10 = d - d4;
                double d11 = d1 - d5;
                Random random = new Random();
                float f1 = ((random.nextInt(20) - 10F) / 100F) + 1.0F;
                int i1 = random.nextInt(6) - 3;
                float f2 = 14000F;
                float f3 = f2;
                if (d3 < 1400D) {
                    f3 = (float) (d3 * 0.8D * 3D);
                }
                int j1 = (int) (Math.ceil(Math.sqrt(((d11 * d11) + (d10 * d10)) * f1) / 10D) * 10D);
                if (j1 > f2) {
                    j1 = (int) (Math.ceil(Math.sqrt((d11 * d11) + (d10 * d10)) / 10D) * 10D);
                }
                float f4 = 57.32484F * (float) Math.atan2(j1, d7);
                int k1 = (int) (Math.floor((int) f4) - 90D);
                int l1 = (k1 - (90 - j)) + i1;
                int i2 = (int) f2;
                if (j1 < f2) {
                    if (j1 > 1150) {
                        i2 = (int) (Math.ceil(j1 / 900D) * 900D);
                    } else {
                        i2 = (int) (Math.ceil(j1 / 500D) * 500D);
                    }
                }
                int j2 = l + i1;
                int k2 = j2;
                if (k2 < 0) {
                    k2 += 360;
                }
                float f5 = (float) (f3 + (Math.sin(Math.toRadians(Math.sqrt(l * l) * 3D)) * (f3 * 0.25D)));
                int l2 = (int) (f5 * Math.cos(Math.toRadians(l1)));
                if (k2 < 5) {
                }
                if ((k2 >= 5) && (k2 <= 7.5D)) {
                }
                if ((k2 > 7.5D) && (k2 <= 12.5D)) {
                }
                if ((k2 > 12.5D) && (k2 <= 17.5D)) {
                }
                if ((k2 > 17.5D) && (k2 <= 25)) {
                }
                if ((k2 > 25) && (k2 <= 35)) {
                }
                if ((k2 > 35) && (k2 <= 45)) {
                }
                if ((k2 > 45) && (k2 <= 60)) {
                }
                if (k2 > 355) {
                }
                if ((k2 <= 355) && (k2 >= 352.5D)) {
                }
                if ((k2 < 352.5D) && (k2 >= 347.5D)) {
                }
                if ((k2 < 347.5D) && (k2 >= 342.5D)) {
                }
                if ((k2 < 342.5D) && (k2 >= 335)) {
                }
                if ((k2 < 335) && (k2 >= 325)) {
                }
                if ((k2 < 325) && (k2 >= 315)) {
                }
                if ((k2 < 345) && (k2 >= 300)) {
                }
                if (l1 < -10) {
                }
                if ((l1 >= -10) && (l1 <= -5)) {
                }
                if ((l1 > -5) && (l1 < 5)) {
                }
                if ((l1 <= 10) && (l1 >= 5)) {
                }
                if (l1 > 10) {
                }
                String s3 = "  ";
                if (k2 < 5) {
                    s3 = "dead ahead, ";
                }
                if ((k2 >= 5) && (k2 <= 7.5D)) {
                    s3 = "right by 5\260, ";
                }
                if ((k2 > 7.5D) && (k2 <= 12.5D)) {
                    s3 = "right by 10\260, ";
                }
                if ((k2 > 12.5D) && (k2 <= 17.5D)) {
                    s3 = "right by 15\260, ";
                }
                if ((k2 > 17.5D) && (k2 <= 25)) {
                    s3 = "right by 20\260, ";
                }
                if ((k2 > 25) && (k2 <= 35)) {
                    s3 = "right by 30\260, ";
                }
                if ((k2 > 35) && (k2 <= 45)) {
                    s3 = "right by 40\260, ";
                }
                if ((k2 > 45) && (k2 <= 60)) {
                    s3 = "off our right, ";
                }
                if (k2 > 355) {
                    s3 = "dead ahead, ";
                }
                if ((k2 <= 355) && (k2 >= 352.5D)) {
                    s3 = "left by 5\260, ";
                }
                if ((k2 < 352.5D) && (k2 >= 347.5D)) {
                    s3 = "left by 10\260, ";
                }
                if ((k2 < 347.5D) && (k2 >= 342.5D)) {
                    s3 = "left by 15\260, ";
                }
                if ((k2 < 342.5D) && (k2 >= 335)) {
                    s3 = "left by 20\260, ";
                }
                if ((k2 < 335) && (k2 >= 325)) {
                    s3 = "left by 30\260, ";
                }
                if ((k2 < 325) && (k2 >= 315)) {
                    s3 = "left by 40\260, ";
                }
                if ((k2 < 345) && (k2 >= 300)) {
                    s3 = "off our left, ";
                }
                if (((double) j1 <= (double) l2) && (j1 > 200D) && (l1 >= -20) && (l1 <= 20) && (Math.sqrt(j2 * j2) <= 60D)) {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "WSO: Contact " + s3 + s + ", " + i2 + "m");
                    this.freq = 7;
                } else {
                    this.freq = 7;
                }
                this.setTimer(this.freq);
            }
        }

        return true;
    }

    public void doEjectCatapultStudent() {
        new MsgAction(false, this) {

            public void doAction(Object obj) {
                Aircraft aircraft = (Aircraft) obj;
                if (Actor.isValid(aircraft)) {
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
                    new EjectionSeat(1, loc, vector3d, aircraft);
                }
            }

        };
        this.hierMesh().chunkVisible("Seat2_D0", false);
    }

    public void doEjectCatapultInstructor() {
        new MsgAction(false, this) {

            public void doAction(Object obj) {
                Aircraft aircraft = (Aircraft) obj;
                if (Actor.isValid(aircraft)) {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 10D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat02");
                    aircraft.pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += aircraft.FM.Vwld.x;
                    vector3d.y += aircraft.FM.Vwld.y;
                    vector3d.z += aircraft.FM.Vwld.z;
                    new EjectionSeat(1, loc, vector3d, aircraft);
                }
            }

        };
        this.hierMesh().chunkVisible("Seat1_D0", false);
    }

    private void bailout() {
        if (this.overrideBailout) {
            if ((this.FM.AS.astateBailoutStep >= 0) && (this.FM.AS.astateBailoutStep < 2)) {
                if ((this.FM.CT.cockpitDoorControl > 0.5F) && (this.FM.CT.getCockpitDoor() > 0.5F)) {
                    this.FM.AS.astateBailoutStep = 11;
                } else {
                    this.FM.AS.astateBailoutStep = 2;
                }
            } else if ((this.FM.AS.astateBailoutStep >= 2) && (this.FM.AS.astateBailoutStep <= 3)) {
                switch (this.FM.AS.astateBailoutStep) {
                    case 2:
                        if (this.FM.CT.cockpitDoorControl < 0.5F) {
                            if ((this.FM.AS.getPilotHealth(0) < 0.5F) || (this.FM.AS.getPilotHealth(1) < 0.5F) || (this.FM.EI.engines[0].getReadyness() < 0.7F) || !this.FM.CT.bHasCockpitDoorControl || (this.FM.getSpeedKMH() < 250F) || (World.Rnd().nextFloat() < 0.2F)) {
                                this.emergencyEject = true;
                                this.breakBlister();
                            } else {
                                this.doRemoveBlister1();
                            }
                        }
                        break;

                    case 3:
                        if (!this.emergencyEject) {
                            this.lTimeNextEject = Time.current() + 1000L;
                        }
                        break;
                }
                if (this.FM.AS.isMaster()) {
                    this.FM.AS.netToMirrors(20, this.FM.AS.astateBailoutStep, 1, null);
                }
                this.FM.AS.astateBailoutStep = (byte) (this.FM.AS.astateBailoutStep + 1);
                if (this.FM.AS.astateBailoutStep == 4) {
                    this.FM.AS.astateBailoutStep = 11;
                }
            } else if ((this.FM.AS.astateBailoutStep >= 11) && (this.FM.AS.astateBailoutStep <= 19)) {
                byte byte0 = this.FM.AS.astateBailoutStep;
                if (this.FM.AS.isMaster()) {
                    this.FM.AS.netToMirrors(20, this.FM.AS.astateBailoutStep, 1, null);
                }
                this.FM.AS.astateBailoutStep = (byte) (this.FM.AS.astateBailoutStep + 1);
                if ((this.FM instanceof Maneuver) && (((Maneuver) this.FM).get_maneuver() != 44)) {
                    World.cur();
                    if (this.FM.AS.actor != World.getPlayerAircraft()) {
                        ((Maneuver) this.FM).set_maneuver(44);
                    }
                }
                if (this.FM.AS.astatePilotStates[byte0 - 11] < 99) {
                    if (byte0 == 11) {
                        this.doRemoveBodyFromPlane(2);
                        this.doEjectCatapultStudent();
                        this.lTimeNextEject = Time.current() + 1000L;
                    } else if (byte0 == 12) {
                        this.doRemoveBodyFromPlane(1);
                        this.doEjectCatapultInstructor();
                        this.FM.AS.astateBailoutStep = 51;
                        this.FM.setTakenMortalDamage(true, null);
                        this.FM.CT.WeaponControl[0] = false;
                        this.FM.CT.WeaponControl[1] = false;
                        this.FM.AS.astateBailoutStep = -1;
                        this.overrideBailout = false;
                        this.FM.AS.bIsAboutToBailout = true;
                        this.ejectComplete = true;
                    }
                    this.FM.AS.astatePilotStates[byte0 - 11] = 99;
                } else {
                    EventLog.type("astatePilotStates[" + (byte0 - 11) + "]=" + this.FM.AS.astatePilotStates[byte0 - 11]);
                }
            }
        }
    }

    private final void breakBlister() {
        if ((this.hierMesh().chunkFindCheck("Blister1_D0") != -1) && (this.FM.AS.getPilotHealth(0) > 0.0F)) {
            this.hierMesh().hideSubTrees("Blister1_D0");
            this.hierMesh().chunkVisible("BlisterBroken_D0", true);
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("BlisterPiece1_D0"));
            wreckage.collide(false);
            Vector3d vector3d = new Vector3d();
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
            Wreckage wreckage1 = new Wreckage(this, this.hierMesh().chunkFind("BlisterPiece2_D0"));
            wreckage1.collide(false);
            Vector3d vector3d1 = new Vector3d();
            vector3d1.set(this.FM.Vwld);
            wreckage1.setSpeed(vector3d1);
            Wreckage wreckage2 = new Wreckage(this, this.hierMesh().chunkFind("BlisterPiece3_D0"));
            wreckage2.collide(false);
            Vector3d vector3d2 = new Vector3d();
            vector3d2.set(this.FM.Vwld);
            wreckage2.setSpeed(vector3d2);
        }
    }

    private final void doRemoveBlister1() {
        if ((this.hierMesh().chunkFindCheck("Blister1_D0") != -1) && (this.FM.AS.getPilotHealth(0) > 0.0F)) {
            this.hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(false);
            Vector3d vector3d = new Vector3d();
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        this.hierMesh().chunkSetAngles("BlisterMove", 0.0F, 0.0F, 25F * f);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.7F);
        this.hierMesh().chunkSetLocate("Cylinder_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void update(float f) {
        if ((this.FM.AS.bIsAboutToBailout || this.overrideBailout) && !this.ejectComplete && (this.FM.getSpeedKMH() > 15F)) {
            this.overrideBailout = true;
            this.FM.AS.bIsAboutToBailout = false;
            if (Time.current() > this.lTimeNextEject) {
                this.bailout();
            }
            if ((this.FM.getAltitude() > 0.0F) && (this.FM.EI.engines[0].getThrustOutput() < 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
                this.FM.producedAF.x -= 4280D;
            }
        }
        this.computeJ33_AB();
        super.update(f);
    }

    public float getAirPressure(float f) {
        float f1 = 1.0F - ((0.0065F * f) / 288.15F);
        float f2 = 5.255781F;
        return 101325F * (float) Math.pow(f1, f2);
    }

    public float getAirPressureFactor(float f) {
        return this.getAirPressure(f) / 101325F;
    }

    public float getAirDensity(float f) {
        return (this.getAirPressure(f) * 0.0289644F) / (8.31447F * (288.15F - (0.0065F * f)));
    }

    public float getAirDensityFactor(float f) {
        return this.getAirDensity(f) / 1.225F;
    }

    public float getMachForAlt(float f) {
        f /= 1000F;
        int i = 0;
        for (i = 0; i < TypeSupersonic.fMachAltX.length; i++) {
            if (TypeSupersonic.fMachAltX[i] > f) {
                break;
            }
        }

        if (i == 0) {
            return TypeSupersonic.fMachAltY[0];
        } else {
            float f1 = TypeSupersonic.fMachAltY[i - 1];
            float f2 = TypeSupersonic.fMachAltY[i] - f1;
            float f3 = TypeSupersonic.fMachAltX[i - 1];
            float f4 = TypeSupersonic.fMachAltX[i] - f3;
            float f5 = (f - f3) / f4;
            return f1 + (f2 * f5);
        }
    }

    public float calculateMach() {
        return this.FM.getSpeedKMH() / this.getMachForAlt(this.FM.getAltitude());
    }

    public void computeJ33_AB() {
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x += 5210D;
        }
        float f = this.FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() == 6)) {
            if (f > 10F) {
                f1 = 5.21F;
            } else {
                f1 = 0.521F * f;
            }
        }
        this.FM.producedAF.x -= f1 * 1000F;
    }

    public void auxPressed(int i) {
        super.auxPressed(i);
        if (i == 20) {
            if (!this.APmode1) {
                this.APmode1 = true;
                HUD.log("Autopilot Mode: Altitude ON");
                this.FM.AP.setStabAltitude(1000F);
            } else if (this.APmode1) {
                this.APmode1 = false;
                HUD.log("Autopilot Mode: Altitude OFF");
                this.FM.AP.setStabAltitude(false);
            }
        }
        if (i == 21) {
            if (!this.APmode2) {
                this.APmode2 = true;
                HUD.log("Autopilot Mode: Direction ON");
                this.FM.AP.setStabDirection(true);
                this.FM.CT.bHasRudderControl = false;
            } else if (this.APmode2) {
                this.APmode2 = false;
                HUD.log("Autopilot Mode: Direction OFF");
                this.FM.AP.setStabDirection(false);
                this.FM.CT.bHasRudderControl = true;
            }
        }
    }

    public void setTimer(int i) {
        Random random = new Random();
        this.Timer1 = (float) (random.nextInt(i) * 0.1D);
        this.Timer2 = (float) (random.nextInt(i) * 0.1D);
    }

    public void resetTimer(float f) {
        this.Timer1 = f;
        this.Timer2 = f;
    }

    public static boolean bChangedPit = false;
    private boolean       overrideBailout;
    private boolean       ejectComplete;
    private long          lTimeNextEject;
    private boolean       emergencyEject;
    public boolean        APmode1;
    public boolean        APmode2;
    public float          Timer1;
    public float          Timer2;
    private int           freq;
    private int           counter;

    static {
        Class class1 = F94B.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F-94");
        Property.set(class1, "meshName", "3DO/Plane/F94B/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1946.9F);
        Property.set(class1, "yearExpired", 1955.3F);
        Property.set(class1, "FlightModel", "FlightModels/F94BUSA.fmd:F80_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF94B.class, CockpitT_33i.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08" });
    }
}
