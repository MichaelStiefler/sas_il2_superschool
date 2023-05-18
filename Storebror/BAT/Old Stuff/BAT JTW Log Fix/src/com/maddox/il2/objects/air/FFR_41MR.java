package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.RocketGunAAM3;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.util.HashMapInt;

public class FFR_41MR extends FFR_00 implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeAcePlane {

    public FFR_41MR() {
        this.upperAngle = 0.0F;
        this.atackAngle = 0.0F;
        this.windFoldValue = 0.0F;
        this.bulletEmitters = null;
        this.bJettisonPylons = true;
        this.guidedMissileUtils = null;
        this.hasChaff = false;
        this.hasFlare = false;
        this.lastChaffDeployed = 0L;
        this.lastFlareDeployed = 0L;
        this.lastCommonThreatActive = 0L;
        this.intervalCommonThreat = 1000L;
        this.lastRadarLockThreatActive = 0L;
        this.intervalRadarLockThreat = 1000L;
        this.lastMissileLaunchThreatActive = 0L;
        this.intervalMissileLaunchThreat = 1000L;
        this.guidedMissileUtils = new GuidedMissileUtils(this);
    }

    public long getChaffDeployed() {
        if (this.hasChaff) {
            return this.lastChaffDeployed;
        } else {
            return 0L;
        }
    }

    public long getFlareDeployed() {
        if (this.hasFlare) {
            return this.lastFlareDeployed;
        } else {
            return 0L;
        }
    }

    public void setCommonThreatActive() {
        long l = Time.current();
        if ((l - this.lastCommonThreatActive) > this.intervalCommonThreat) {
            this.lastCommonThreatActive = l;
            this.doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive() {
        long l = Time.current();
        if ((l - this.lastRadarLockThreatActive) > this.intervalRadarLockThreat) {
            this.lastRadarLockThreatActive = l;
            this.doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive() {
        long l = Time.current();
        if ((l - this.lastMissileLaunchThreatActive) > this.intervalMissileLaunchThreat) {
            this.lastMissileLaunchThreatActive = l;
            this.doDealMissileLaunchThreat();
        }
    }

    private void doDealCommonThreat() {
    }

    private void doDealRadarLockThreat() {
    }

    private void doDealMissileLaunchThreat() {
    }

    private boolean sirenaWarning() {
        Point3d point3d = new Point3d();
        super.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        if (World.getPlayerAircraft() == null) {
            return false;
        }
        double d = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).z;
        int i = (int) (-(((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
        if (i < 0) {
            i += 360;
        }
        int j = (int) (-(((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
        if (j < 0) {
            j += 360;
        }
        Aircraft aircraft1 = War.getNearestEnemy(this, 4000F);
        if ((aircraft1 instanceof Aircraft) && (aircraft1.getArmy() != World.getPlayerArmy()) && (aircraft1 instanceof TypeFighterAceMaker) && (aircraft1 instanceof TypeRadarGunsight) && (aircraft1 != World.getPlayerAircraft()) && (aircraft1.getSpeed(vector3d) > 20D)) {
            super.pos.getAbs(point3d);
            double d3 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).x;
            double d4 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).y;
            double d5 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).z;
            new String();
            new String();
            double d6 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
            new String();
            double d7 = d3 - d;
            double d8 = d4 - d1;
            float f = 57.32484F * (float) Math.atan2(d8, -d7);
            int k = (int) (Math.floor((int) f) - 90D);
            if (k < 0) {
                k += 360;
            }
            int l = k - i;
            double d9 = d - d3;
            double d10 = d1 - d4;
            double d11 = Math.sqrt(d6 * d6);
            int i1 = (int) (Math.ceil(Math.sqrt((d10 * d10) + (d9 * d9)) / 10D) * 10D);
            float f1 = 57.32484F * (float) Math.atan2(i1, d11);
            int j1 = (int) (Math.floor((int) f1) - 90D);
            if (j1 < 0) {
                j1 += 360;
            }
            int k1 = j1 - j;
            int l1 = (int) (Math.ceil((i1 * 3.2808399000000001D) / 100D) * 100D);
            if (l1 >= 5280) {
                l1 = (int) Math.floor(l1 / 5280);
            }
            this.bRadarWarning = (i1 <= 3000D) && (i1 >= 50D) && (k1 >= 195) && (k1 <= 345) && (Math.sqrt(l * l) >= 120D);
            this.playSirenaWarning(this.bRadarWarning);
        } else {
            this.bRadarWarning = false;
            this.playSirenaWarning(this.bRadarWarning);
        }
        return true;
    }

    public void playSirenaWarning(boolean flag1) {
    }

    public void getGFactors(TypeGSuit.GFactors gfactors) {
        gfactors.setGFactors(5F, 5F, 7F, 7.8F, 9F, 8F);
    }

    public GuidedMissileUtils getGuidedMissileUtils() {
        return this.guidedMissileUtils;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.guidedMissileUtils.onAircraftLoaded();
        super.FM.Skill = 3;
        ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = true;
        if (super.thisWeaponsName.startsWith("FULL")) {
            this.hierMesh().chunkVisible("Pyron1_D0", true);
            this.hierMesh().chunkVisible("Pyron2_D0", true);
            this.hierMesh().chunkVisible("Pyron3_D0", true);
            this.hierMesh().chunkVisible("Pyron4_D0", true);
            this.hierMesh().chunkVisible("Pyron5_D0", true);
            this.hierMesh().chunkVisible("Pyron6_D0", true);
            this.hierMesh().chunkVisible("Pyron7_D0", true);
            this.hierMesh().chunkVisible("Pyron8_D0", true);
            this.hierMesh().chunkVisible("SubPyron1_D0", true);
            this.hierMesh().chunkVisible("SubPyron2_D0", true);
            this.hierMesh().chunkVisible("SubPyron3_D0", true);
            this.hierMesh().chunkVisible("SubPyron4_D0", true);
            this.hierMesh().chunkVisible("SubPyron6_D0", false);
            this.hierMesh().chunkVisible("SubPyron7_D0", false);
        } else if (super.thisWeaponsName.startsWith("CA2")) {
            this.hierMesh().chunkVisible("Pyron1_D0", false);
            this.hierMesh().chunkVisible("Pyron2_D0", false);
            this.hierMesh().chunkVisible("Pyron3_D0", false);
            this.hierMesh().chunkVisible("Pyron4_D0", false);
            this.hierMesh().chunkVisible("Pyron5_D0", true);
            this.hierMesh().chunkVisible("Pyron6_D0", true);
            this.hierMesh().chunkVisible("Pyron7_D0", false);
            this.hierMesh().chunkVisible("Pyron8_D0", false);
            this.hierMesh().chunkVisible("SubPyron1_D0", false);
            this.hierMesh().chunkVisible("SubPyron2_D0", false);
            this.hierMesh().chunkVisible("SubPyron3_D0", false);
            this.hierMesh().chunkVisible("SubPyron4_D0", false);
        } else if (super.thisWeaponsName.startsWith("CA4")) {
            this.hierMesh().chunkVisible("Pyron1_D0", false);
            this.hierMesh().chunkVisible("Pyron2_D0", false);
            this.hierMesh().chunkVisible("Pyron3_D0", false);
            this.hierMesh().chunkVisible("Pyron4_D0", false);
            this.hierMesh().chunkVisible("Pyron5_D0", true);
            this.hierMesh().chunkVisible("Pyron6_D0", true);
            this.hierMesh().chunkVisible("Pyron7_D0", true);
            this.hierMesh().chunkVisible("Pyron8_D0", true);
            this.hierMesh().chunkVisible("SubPyron1_D0", false);
            this.hierMesh().chunkVisible("SubPyron2_D0", false);
            this.hierMesh().chunkVisible("SubPyron3_D0", false);
            this.hierMesh().chunkVisible("SubPyron4_D0", false);
        } else if (super.thisWeaponsName.startsWith("SA2_2")) {
            this.hierMesh().chunkVisible("Pyron1_D0", true);
            this.hierMesh().chunkVisible("Pyron2_D0", true);
            this.hierMesh().chunkVisible("Pyron3_D0", true);
            this.hierMesh().chunkVisible("Pyron4_D0", true);
            this.hierMesh().chunkVisible("SubPyron1_D0", false);
            this.hierMesh().chunkVisible("SubPyron2_D0", false);
            this.hierMesh().chunkVisible("SubPyron3_D0", false);
            this.hierMesh().chunkVisible("SubPyron4_D0", false);
        } else if (super.thisWeaponsName.startsWith("SA2")) {
            this.hierMesh().chunkVisible("Pyron1_D0", true);
            this.hierMesh().chunkVisible("Pyron2_D0", true);
            this.hierMesh().chunkVisible("Pyron3_D0", false);
            this.hierMesh().chunkVisible("Pyron4_D0", false);
            this.hierMesh().chunkVisible("SubPyron1_D0", false);
            this.hierMesh().chunkVisible("SubPyron2_D0", false);
            this.hierMesh().chunkVisible("SubPyron3_D0", false);
            this.hierMesh().chunkVisible("SubPyron4_D0", false);
        } else if (super.thisWeaponsName.startsWith("SA4")) {
            this.hierMesh().chunkVisible("Pyron1_D0", true);
            this.hierMesh().chunkVisible("Pyron2_D0", true);
            this.hierMesh().chunkVisible("Pyron3_D0", false);
            this.hierMesh().chunkVisible("Pyron4_D0", false);
            this.hierMesh().chunkVisible("SubPyron1_D0", true);
            this.hierMesh().chunkVisible("SubPyron2_D0", true);
            this.hierMesh().chunkVisible("SubPyron3_D0", false);
            this.hierMesh().chunkVisible("SubPyron4_D0", false);
        } else if (super.thisWeaponsName.startsWith("SA8")) {
            this.hierMesh().chunkVisible("Pyron1_D0", true);
            this.hierMesh().chunkVisible("Pyron2_D0", true);
            this.hierMesh().chunkVisible("Pyron3_D0", true);
            this.hierMesh().chunkVisible("Pyron4_D0", true);
            this.hierMesh().chunkVisible("SubPyron1_D0", true);
            this.hierMesh().chunkVisible("SubPyron2_D0", true);
            this.hierMesh().chunkVisible("SubPyron3_D0", true);
            this.hierMesh().chunkVisible("SubPyron4_D0", true);
        } else {
            this.hierMesh().chunkVisible("Pyron1_D0", false);
            this.hierMesh().chunkVisible("Pyron2_D0", false);
            this.hierMesh().chunkVisible("Pyron3_D0", false);
            this.hierMesh().chunkVisible("Pyron4_D0", false);
            this.hierMesh().chunkVisible("SubPyron1_D0", false);
            this.hierMesh().chunkVisible("SubPyron2_D0", false);
            this.hierMesh().chunkVisible("SubPyron3_D0", false);
            this.hierMesh().chunkVisible("SubPyron4_D0", false);
        }
        this.bulletEmitters = new BulletEmitter[weponHookArray.length];
        for (int i = 0; i < weponHookArray.length; i++) {
            this.bulletEmitters[i] = this.getBulletEmitterByHookName(weponHookArray[i]);
        }

    }

    public void auxPressed(int i) {
        if ((i == 0) && !this.bJettisonPylons) {
            ((FlightModelMain) (super.FM)).CT.dropExternalStores(true);
            this.bJettisonPylons = true;
        }
        super.auxPressed(i);
    }

    public boolean dropExternalStores(boolean flag) {
        return false;
    }

    public void update(float f) {
        this.guidedMissileUtils.update();
        this.sirenaWarning();
        if (((FlightModelMain) (super.FM)).CT.getArrestor() > 0.2F) {
            if (((FlightModelMain) (super.FM)).Gears.arrestorVAngle != 0.0F) {
                float f1 = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
                this.arrestor = (0.8F * this.arrestor) + (0.2F * f1);
                this.moveArrestorHook(this.arrestor);
            } else {
                float f2 = (-33F * ((FlightModelMain) (super.FM)).Gears.arrestorVSink) / 57F;
                if ((f2 < 0.0F) && (super.FM.getSpeedKMH() > 60F)) {
                    Eff3DActor.New(this, ((FlightModelMain) (super.FM)).Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                }
                if ((f2 > 0.0F) && (((FlightModelMain) (super.FM)).CT.getArrestor() < 0.95F)) {
                    f2 = 0.0F;
                }
                if (f2 > 0.2F) {
                    f2 = 0.2F;
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
        super.update(f);
    }

    public void updateHook() {
        for (int i = 0; i < weponHookArray.length; i++) {
            try {
                if (this.bulletEmitters[i] instanceof RocketGunAAM3) {
                    ((RocketGunAAM3) this.bulletEmitters[i]).updateHook(weponHookArray[i]);
                }
            } catch (Exception exception) {
            }
        }

    }

    public void hideWepon(boolean hideFlg) {
        for (int i = 0; i < weponHookArray.length; i++) {
            try {
                if (this.bulletEmitters[i] instanceof RocketGunAAM3) {
                    ((RocketGunAAM3) this.bulletEmitters[i]).hide(hideFlg);
                }
            } catch (Exception exception) {
            }
        }

    }

    public boolean haveWingWepon() {
        for (int i = 0; i < weponHookArray.length; i++) {
            try {
                if ((this.bulletEmitters[i] instanceof RocketGunAAM3)) {
                    if ((i > 0) && (i < 25)) {
                        if (((RocketGunAAM3) this.bulletEmitters[i]).countBullets() > 0) {
                            return true;
                        }
                    }
                }
            } catch (Exception localException) {
            }
        }
        return false;
    }

    public void changeSupersonic(int mode) {
        if (!super.FM.isPlayers()) {
            return;
        }
        if (mode == 0) {
            super.changeSupersonic(mode);
            if (this.atackAngle != 0.0F) {
                if (this.atackAngle > 0.0F) {
                    this.atackAngle--;
                }
                if (this.atackAngle < 0.0F) {
                    this.atackAngle++;
                }
                if (Math.abs(this.atackAngle - 0.0F) < 1.0F) {
                    this.atackAngle = 0.0F;
                }
                this.hierMesh().chunkSetAngles("WingLOut_D0", 0.0F, 0.0F, this.atackAngle);
                this.hierMesh().chunkSetAngles("WingROut_D0", 0.0F, 0.0F, this.atackAngle);
                super.needUpdateHook = true;
            } else if (this.upperAngle != 0.0F) {
                if (this.upperAngle > 0.0F) {
                    this.upperAngle--;
                }
                if (this.upperAngle < 0.0F) {
                    this.upperAngle++;
                }
                if (Math.abs(this.upperAngle - 0.0F) < 1.0F) {
                    this.upperAngle = 0.0F;
                }
                this.hierMesh().chunkSetAngles("WingLMid_D0", 0.0F, this.upperAngle, 0.0F);
                this.hierMesh().chunkSetAngles("WingRMid_D0", 0.0F, -this.upperAngle, 0.0F);
                super.needUpdateHook = true;
            }
        } else {
            super.changeSupersonic(mode);
            if (this.upperAngle != 40F) {
                if (this.upperAngle > 40F) {
                    this.upperAngle--;
                }
                if (this.upperAngle < 40F) {
                    this.upperAngle++;
                }
                if (Math.abs(this.upperAngle - 40F) < 1.0F) {
                    this.upperAngle = 40F;
                }
                this.hierMesh().chunkSetAngles("WingLMid_D0", 0.0F, this.upperAngle, 0.0F);
                this.hierMesh().chunkSetAngles("WingRMid_D0", 0.0F, -this.upperAngle, 0.0F);
                super.needUpdateHook = true;
            } else if (this.atackAngle != 180F) {
                if (this.atackAngle > 180F) {
                    this.atackAngle--;
                }
                if (this.atackAngle < 180F) {
                    this.atackAngle++;
                }
                if (Math.abs(this.atackAngle - 180F) < 1.0F) {
                    this.atackAngle = 180F;
                }
                this.hierMesh().chunkSetAngles("WingLOut_D0", 0.0F, 0.0F, this.atackAngle);
                this.hierMesh().chunkSetAngles("WingROut_D0", 0.0F, 0.0F, this.atackAngle);
                super.needUpdateHook = true;
            }
        }
    }

    public void moveWingFold(float f) {
        if (f < 0.10000000000000001D) {
            f = 0.0F;
        }
        if (f > 0.90000000000000002D) {
            f = 1.0F;
        }
        super.moveWingFold(f);
        if (this.windFoldValue != f) {
            this.windFoldValue = f;
            super.needUpdateHook = true;
        }
    }

    public void moveFan(float f) {
        super.moveFan(f);
    }

    protected static void weaponsRegister(Class var_class, String string, String strings[]) {
        try {
            int triggers[] = Aircraft.getWeaponTriggersRegistered(var_class);
            int length = triggers.length;
            int count = strings.length;
            ArrayList arraylist = (ArrayList) Property.value(var_class, "weaponsList");
            if (arraylist == null) {
                arraylist = new ArrayList();
                Property.set(var_class, "weaponsList", arraylist);
            }
            HashMapInt hashmapint = (HashMapInt) Property.value(var_class, "weaponsMap");
            if (hashmapint == null) {
                hashmapint = new HashMapInt();
                Property.set(var_class, "weaponsMap", hashmapint);
            }
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[length];
//            System.out.println("FFR-41MR Wepon Loading List:" + string);
            for (int i = 0; i < count; i++) {
                String weponName = strings[i];
                int weponCount = 1;
                if (weponName != null) {
                    for (int j = weponName.length() - 1; j > 0; j--) {
                        if (weponName.charAt(j) != ' ') {
                            continue;
                        }
                        try {
                            weponCount = Integer.parseInt(weponName.substring(j + 1));
                            weponName = weponName.substring(0, j);
                        } catch (Exception e) {
//                            System.out.println(strings[i] + ":" + weponName.substring(j + 1) + "(" + j + ")");
                        }
                        break;
                    }

//                    System.out.println("    No." + (i + 1) + ":" + weponName + "(" + weponCount + ")");
                    a_lweaponslot[i] = new Aircraft._WeaponSlot(triggers[i], weponName, weponCount);
                } else {
                    a_lweaponslot[i] = null;
                }
            }

            for (int i = count; i < length; i++) {
                a_lweaponslot[i] = null;
            }

            arraylist.add(string);
            hashmapint.put(Finger.Int(string), a_lweaponslot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    float                      upperAngle;
    float                      atackAngle;
    float                      windFoldValue;
    private GuidedMissileUtils guidedMissileUtils;
    private boolean            bRadarWarning;
    private boolean            hasChaff;
    private boolean            hasFlare;
    private long               lastChaffDeployed;
    private long               lastFlareDeployed;
    private long               lastCommonThreatActive;
    private long               intervalCommonThreat;
    private long               lastRadarLockThreatActive;
    private long               intervalRadarLockThreat;
    private long               lastMissileLaunchThreatActive;
    private long               intervalMissileLaunchThreat;
    public boolean             bToFire;
    private float              arrestor;
    private boolean            bJettisonPylons;
    static String              weponHookArray[] = { "_CANNON01", "_ExternalDev01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev02", "_ExternalDev03", "_ExternalDev03", "_ExternalDev04", "_ExternalDev04", "_ExternalDev05", "_ExternalDev05", "_ExternalDev06", "_ExternalDev06", "_ExternalDev07", "_ExternalDev07", "_ExternalDev08", "_ExternalDev08", "_ExternalDev09", "_ExternalDev09", "_ExternalDev10", "_ExternalDev10", "_ExternalDev11", "_ExternalDev11", "_ExternalDev12", "_ExternalDev12", "_ExternalDev13", "_ExternalDev13", "_ExternalDev14", "_ExternalDev14", "_ExternalDev15", "_ExternalDev15", "_ExternalDev16", "_ExternalDev16" };
    BulletEmitter              bulletEmitters[];

    static {
        Class class1 = FFR_41MR.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "YUKIKAZE");
        Property.set(class1, "meshName", "3DO/Plane/FFR-41MR/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1965F);
        Property.set(class1, "yearExpired", 1990F);
        Property.set(class1, "FlightModel", "FlightModels/FFR-41MR.fmd:FAF");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFFR_41MR.class, CockpitFFR_41MR.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, weponHookArray);
        String as[];
        String as1[];
        String as2[];
        String as3[];
        String as4[];
        String as5[];
        try {
            as = new String[33];
            as[0] = "MGunM61NR 10000";
            weaponsRegister(class1, "Default", as);
            weaponsRegister(class1, "FULL:AIM-IIIx8+4+2", new String[] { "MGunM61NR 10000", null, null, "RocketGunAAM3 1", "RocketGunNull 1", "RocketGunAAM3 1", "RocketGunNull 1", null, null, "RocketGunAAM3 1", "RocketGunNull 1", "RocketGunAAM3 1", "RocketGunNull 1", null, null, "RocketGunAAM3 1", "RocketGunNull 1", "RocketGunAAM3 1", "RocketGunNull 1", null, null, "RocketGunAAM3 1", "RocketGunNull 1", "RocketGunAAM3 1", "RocketGunNull 1", "RocketGunAAM3 1", "RocketGunNull 1", "RocketGunAAM3 1", "RocketGunNull 1", "RocketGunAAM3 1", "RocketGunNull 1", "RocketGunAAM3 1", "RocketGunNull 1" });
            as1 = new String[33];
            as1[0] = "MGunM61NR 10000";
            as1[25] = "RocketGunAAM3 1";
            as1[26] = "RocketGunNull 1";
            as1[27] = "RocketGunAAM3 1";
            as1[28] = "RocketGunNull 1";
            weaponsRegister(class1, "CA2:AIM-IIIx2", as1);
            weaponsRegister(class1, "CA4:AIM-IIIx4", new String[] { "MGunM61NR 10000", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "RocketGunAAM3 1", "RocketGunNull 1", "RocketGunAAM3 1", "RocketGunNull 1", "RocketGunAAM3 1", "RocketGunNull 1", "RocketGunAAM3 1", "RocketGunNull 1" });
            as2 = new String[33];
            as2[0] = "MGunM61NR 10000";
            as2[1] = "RocketGunAAM3 1";
            as2[4] = "RocketGunNull 1";
            as2[7] = "RocketGunAAM3 1";
            as2[10] = "RocketGunNull 1";
            weaponsRegister(class1, "SA2:AIM-IIIx2", as2);
            as3 = new String[33];
            as3[0] = "MGunM61NR 10000";
            as3[3] = "RocketGunAAM3 1";
            as3[4] = "RocketGunNull 1";
            as3[5] = "RocketGunAAM3 1";
            as3[6] = "RocketGunNull 1";
            as3[9] = "RocketGunAAM3 1";
            as3[10] = "RocketGunNull 1";
            as3[11] = "RocketGunAAM3 1";
            as3[12] = "RocketGunNull 1";
            as3[15] = "RocketGunAAM3 1";
            as3[16] = "RocketGunNull 1";
            as3[17] = "RocketGunAAM3 1";
            as3[18] = "RocketGunNull 1";
            as3[21] = "RocketGunAAM3 1";
            as3[22] = "RocketGunNull 1";
            as3[23] = "RocketGunAAM3 1";
            as3[24] = "RocketGunNull 1";
            weaponsRegister(class1, "SA4:AIM-IIIx4", as3);
            as4 = new String[33];
            as4[0] = "MGunM61NR 10000";
            as4[3] = "RocketGunAAM3 1";
            as4[4] = "RocketGunNull 1";
            as4[5] = "RocketGunAAM3 1";
            as4[6] = "RocketGunNull 1";
            as4[9] = "RocketGunAAM3 1";
            as4[10] = "RocketGunNull 1";
            as4[11] = "RocketGunAAM3 1";
            as4[12] = "RocketGunNull 1";
            as4[15] = "RocketGunAAM3 1";
            as4[16] = "RocketGunNull 1";
            as4[17] = "RocketGunAAM3 1";
            as4[18] = "RocketGunNull 1";
            as4[21] = "RocketGunAAM3 1";
            as4[22] = "RocketGunNull 1";
            as4[23] = "RocketGunAAM3 1";
            as4[24] = "RocketGunNull 1";
            weaponsRegister(class1, "SA8:AIM-IIIx8", as4);
            as5 = new String[33];
            as5[0] = "MGunM61NR 10000";
            as5[1] = "RocketGunAAM3 1";
            as5[2] = "RocketGunNull 1";
            as5[7] = "RocketGunAAM3 1";
            as5[8] = "RocketGunNull 1";
            as5[13] = "RocketGunAAM3 1";
            as5[14] = "RocketGunNull 1";
            as5[19] = "RocketGunAAM3 1";
            as5[20] = "RocketGunNull 1";
            weaponsRegister(class1, "SA2_2:AIM-IIIx2x2", as5);
        } catch (Exception exception) {
        }
    }
}
