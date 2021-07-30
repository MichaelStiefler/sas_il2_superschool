package com.maddox.il2.fm;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.il2.objects.weapons.BombGunNull;
import com.maddox.il2.objects.weapons.BombGunSC50;
import com.maddox.il2.objects.weapons.BombGunSC70;
import com.maddox.il2.objects.weapons.FuelTankGun;
import com.maddox.il2.objects.weapons.Pylon;
import com.maddox.il2.objects.weapons.PylonHS129BK37;
import com.maddox.il2.objects.weapons.PylonHS129BK75;
import com.maddox.il2.objects.weapons.PylonMG15120Internal;
import com.maddox.il2.objects.weapons.PylonP38RAIL3FL;
import com.maddox.il2.objects.weapons.PylonP38RAIL3FR;
import com.maddox.il2.objects.weapons.PylonP38RAIL3WL;
import com.maddox.il2.objects.weapons.PylonP38RAIL3WR;
import com.maddox.il2.objects.weapons.PylonP38RAIL5;
import com.maddox.il2.objects.weapons.PylonP38RAILS;
import com.maddox.il2.objects.weapons.PylonPE8_FAB100;
import com.maddox.il2.objects.weapons.PylonPE8_FAB250;
import com.maddox.il2.objects.weapons.PylonRO_82_1;
import com.maddox.il2.objects.weapons.PylonRO_82_3;
import com.maddox.il2.objects.weapons.PylonRO_WfrGr21;
import com.maddox.il2.objects.weapons.PylonRO_WfrGr21Dual;
import com.maddox.il2.objects.weapons.RocketBombGun;
import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.il2.objects.weapons.RocketGunR4M;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;

public class Squares {

    public Squares() {
        this.dragParasiteCx = 0.0F;
        this.dragAirbrakeCx = 0.0F;
        this.dragFuselageCx = 0.0F;
        this.dragProducedCx = 0.0F;
        this.toughness = new float[44];
        this.eAbsorber = new float[44];
    }

    public void load(SectFile sectfile) {
        String s1 = "Zero Square processed from " + sectfile.toString();
        String s = "Squares";
        float f = sectfile.get(s, "Wing", 0.0F);
        if (f == 0.0F) throw new RuntimeException(s1);
        this.squareWing = f;
        f = sectfile.get(s, "Aileron", 0.0F);
        if (f == 0.0F) throw new RuntimeException(s1);
        this.squareAilerons = f;
        f = sectfile.get(s, "Flap", 0.0F);
        if (f == 0.0F) throw new RuntimeException(s1);
        this.squareFlaps = f;
        f = sectfile.get(s, "Stabilizer", 0.0F);
        if (f == 0.0F) throw new RuntimeException(s1);
        this.liftStab = f;
        f = sectfile.get(s, "Elevator", 0.0F);
        if (f == 0.0F) throw new RuntimeException(s1);
        this.squareElevators = f;
        f = sectfile.get(s, "Keel", 0.0F);
        if (f == 0.0F) throw new RuntimeException(s1);
        this.liftKeel = f;
        f = sectfile.get(s, "Rudder", 0.0F);
        if (f == 0.0F) throw new RuntimeException(s1);
        this.squareRudders = f;
        f = sectfile.get(s, "Wing_In", 0.0F);
        if (f == 0.0F) throw new RuntimeException(s1);
        this.liftWingLIn = this.liftWingRIn = f;
        f = sectfile.get(s, "Wing_Mid", 0.0F);
        if (f == 0.0F) throw new RuntimeException(s1);
        this.liftWingLMid = this.liftWingRMid = f;
        f = sectfile.get(s, "Wing_Out", 0.0F);
        if (f == 0.0F) throw new RuntimeException(s1);
        this.liftWingLOut = this.liftWingROut = f;
        f = sectfile.get(s, "AirbrakeCxS", -1F);
        if (f == -1F) throw new RuntimeException(s1);
        this.dragAirbrakeCx = f;
        f = sectfile.get("Params", "SpinCxLoss", -1F);
        if (f == -1F) throw new RuntimeException(s1);
        this.spinCxloss = f;
        f = sectfile.get("Params", "SpinCyLoss", -1F);
        if (f == -1F) throw new RuntimeException(s1);
        this.spinCyloss = f;
        for (int i = 0; i < 8; i++)
            this.dragEngineCx[i] = 0.0F;

        s = "Toughness";
        for (int j = 0; j < 44; j++)
            this.toughness[j] = sectfile.get(s, Aircraft.partNames()[j], 100) * 0.0001F;

        this.toughness[43] = 3.402823E+038F;
        float f1 = 2.0F * (this.liftWingLIn + this.liftWingLMid + this.liftWingLOut) / (this.squareWing + 0.01F);
        if (f1 < 0.9F || f1 > 1.1F) {
            if (World.cur().isDebugFM()) System.out.println("Error in flightmodel " + sectfile.toString() + ": (wing square) != (sum of squares*2)");
            if (f1 > 1.0F) this.squareWing = 2.0F * (this.liftWingLIn + this.liftWingLMid + this.liftWingLOut);
            else this.liftWingLIn = this.liftWingLMid = this.liftWingLOut = this.liftWingRIn = this.liftWingRMid = this.liftWingROut = 0.166667F * this.squareWing;
        }
    }

    public float getToughness(int i) {
        return this.toughness[i];
    }

    public void computeParasiteDrag(Controls controls, BulletEmitter abulletemitter[][]) {
        this.dragParasiteCx = 0.0F;
        for (int i = 0; i < abulletemitter.length; i++) {
            if (abulletemitter[i] == null || abulletemitter[i].length <= 0) continue;
            for (int j = 0; j < abulletemitter[i].length; j++) {
                if (abulletemitter[i][j] instanceof RocketGun) {
                    Class theBulletClass = ((RocketGun) abulletemitter[i][j]).bulletClass();
                    if (com.maddox.il2.objects.weapons.Missile.class.isAssignableFrom(theBulletClass)) continue;
                }
                if (abulletemitter[i][j] instanceof BombGunNull) continue;
                if ((abulletemitter[i][j] instanceof BombGun || abulletemitter[i][j] instanceof RocketBombGun) && abulletemitter[i][j].haveBullets() && abulletemitter[i][j].getHookName().startsWith("_External") && this.dragParasiteCx < 0.704F)
                    if (abulletemitter[i][j] instanceof BombGunSC50 || abulletemitter[i][j] instanceof BombGunSC70 || abulletemitter[i][j] instanceof FuelTankGun) this.dragParasiteCx += 0.02F;
                    else this.dragParasiteCx += 0.06F;
                if (abulletemitter[i][j] instanceof RocketGun && abulletemitter[i][j].haveBullets() && !(abulletemitter[i][j] instanceof RocketGunR4M)) this.dragParasiteCx += 0.02F;
                if (!(abulletemitter[i][j] instanceof Pylon) || abulletemitter[i][j] instanceof PylonRO_82_1 || abulletemitter[i][j] instanceof PylonRO_82_3 || abulletemitter[i][j] instanceof PylonPE8_FAB100
                        || abulletemitter[i][j] instanceof PylonPE8_FAB250 || abulletemitter[i][j] instanceof PylonP38RAIL3FL || abulletemitter[i][j] instanceof PylonP38RAIL3FR || abulletemitter[i][j] instanceof PylonP38RAIL3WL
                        || abulletemitter[i][j] instanceof PylonP38RAIL3WR || abulletemitter[i][j] instanceof PylonP38RAIL5 || abulletemitter[i][j] instanceof PylonP38RAILS || abulletemitter[i][j] instanceof PylonMG15120Internal)
                    continue;

                // +++ TODO: By SAS~Storebror: Parasite Pylon Drag from Pylon Properties +++
//				dragParasiteCx += 0.035F;
//				if ((abulletemitter[i][j] instanceof PylonHS129BK75) || (abulletemitter[i][j] instanceof PylonHS129BK37)) dragParasiteCx += 0.45F;
//				if (abulletemitter[i][j] instanceof PylonRO_WfrGr21) dragParasiteCx += 0.015F;
//				if (abulletemitter[i][j] instanceof PylonRO_WfrGr21Dual) dragParasiteCx += 0.02F;
                if (abulletemitter[i][j] instanceof PylonHS129BK75 || abulletemitter[i][j] instanceof PylonHS129BK37) this.dragParasiteCx += 0.45F;
                else if (abulletemitter[i][j] instanceof PylonRO_WfrGr21) this.dragParasiteCx += 0.05F;
                else if (abulletemitter[i][j] instanceof PylonRO_WfrGr21Dual) this.dragParasiteCx += 0.055F;
                else this.dragParasiteCx += Property.floatValue(abulletemitter[i][j].getClass(), "drag", 0.01F);
                // --- TODO: By SAS~Storebror: Parasite Pylon Drag from Pylon Properties ---

            }

        }

        this.dragParasiteCx += 0.02F * controls.getCockpitDoor();
    }

    public float       squareWing;
    public float       squareAilerons;
    public float       squareElevators;
    public float       squareRudders;
    public float       squareFlaps;
    public float       liftWingLIn;
    public float       liftWingLMid;
    public float       liftWingLOut;
    public float       liftWingRIn;
    public float       liftWingRMid;
    public float       liftWingROut;
    public float       liftStab;
    public float       liftKeel;
    public float       dragEngineCx[] = { 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F };
    public float       dragParasiteCx;
    public float       dragAirbrakeCx;
    public float       dragFuselageCx;
    public float       dragProducedCx;
    float              spinCxloss;
    float              spinCyloss;
    public float       toughness[];
    public float       eAbsorber[];
    public final float dragSmallHole  = 0.06F;
    public final float dragBigHole    = 0.12F;
    public final float wingSmallHole  = 0.4F;
    public final float wingBigHole    = 0.8F;
}
