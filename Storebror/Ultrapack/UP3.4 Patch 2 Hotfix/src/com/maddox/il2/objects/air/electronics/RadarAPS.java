package com.maddox.il2.objects.air.electronics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.TrueRandom;

public class RadarAPS {

    public RadarAPS(int type, Aircraft owner, float minDistance, float maxDistance, int numTargets) {
        this.radarPlane = new ArrayList();
        this.rRange = maxDistance;
        this.rClose = minDistance;
        this.nTgts = numTargets;
        this.counter = 0;
        this.messages = false;
        this.power = false;
        this.owner = owner;
        this.type = type;
    }

    public void rareAction() {
        if (!Config.isUSE_RENDER() || (this.owner != World.getPlayerAircraft()) || !this.owner.isAlive()) {
            return;
        }
        if ((this.counter++ % 5) == 0) {
            this.radarScanAircraft();
        }
    }

    public void clearRadarMarks(HierMesh mesh, int radarMarks, int distanceSteps) {
        for (int radarMarkIndex = 0; radarMarkIndex < radarMarks; radarMarkIndex++) {
            this.clearDistanceMarks(mesh, radarMarkIndex, distanceSteps);
        }
    }

    public void clearDistanceMarks(HierMesh mesh, int radarMark, int distanceSteps) {
        for (int radarMarkIndex2 = 1; radarMarkIndex2 <= distanceSteps; radarMarkIndex2++) {
            mesh.chunkVisible("Radarmark" + radarMarkIndex2 + "0" + radarMark, false);
        }
    }

    public void radarScanCockpit(HierMesh mesh, int radarMarks, int distanceSteps) {
        if (!this.power || !Config.isUSE_RENDER() || (this.owner != World.getPlayerAircraft()) || !this.owner.isAlive()) {
            return;
        }
        try {
            if ((Time.current() + 50L) > this.timeout) {
                this.radarPlane.clear();
            }
            if (Actor.isValid(this.owner) && Actor.isAlive(this.owner)) {
                if (Time.current() > this.timeout) {
                    this.timeout = Time.current() + 500L;
                    Point3d aircraftPoint = this.owner.pos.getAbsPoint();
                    Orient aircraftOrient = this.owner.pos.getAbsOrient();
                    List targetList = Engine.targets();
                    int targetListSize = targetList.size();
                    for (int targetListIndex = 0; targetListIndex < targetListSize; targetListIndex++) {
                        Actor actor = (Actor) targetList.get(targetListIndex);
                        if ((actor instanceof Aircraft) && (actor != World.getPlayerAircraft()) && (actor.getArmy() != World.getPlayerArmy())) {
                            Vector3d vectorTarget = new Vector3d();
                            vectorTarget.set(aircraftPoint);
                            Point3d pointTarget = new Point3d();
                            pointTarget.set(actor.pos.getAbsPoint());
                            pointTarget.sub(aircraftPoint);
                            aircraftOrient.transformInv(pointTarget);
                            if ((pointTarget.x > this.rClose) && (pointTarget.x < this.rRange) && (pointTarget.y < (pointTarget.x * 0.46630765815D)) && (pointTarget.y > (-pointTarget.x * 0.46630765815D)) && (pointTarget.z < (pointTarget.x * 0.1763269807D)) && (pointTarget.z > (-pointTarget.x * 0.1763269807D))) {
                                this.radarPlane.add(pointTarget);
                            }
                        }
                    }

                }
                int radarPlaneSize = this.radarPlane.size();
                if (radarPlaneSize == 0) {
                    this.clearRadarMarks(mesh, radarMarks, distanceSteps);
                }
                int radarMarkIndex = 0;
                for (int radarPlaneIndex = 0; radarPlaneIndex < radarPlaneSize; radarPlaneIndex++) {
                    double distance = ((Point3d) this.radarPlane.get(radarPlaneIndex)).x;
                    if ((distance > this.rClose) && (radarMarkIndex < this.nTgts)) {
                        float x = Aircraft.cvt((float) ((((Point3d) this.radarPlane.get(radarPlaneIndex)).y * 60D) / distance), -10F, 10F, -0.04F, 0.04F);
                        float y = Aircraft.cvt((float) ((((Point3d) this.radarPlane.get(radarPlaneIndex)).z * 60D) / distance), -10F, 10F, -0.04F, 0.04F);
                        this.clearDistanceMarks(mesh, radarMarkIndex, distanceSteps);
                        String mark = "";
                        int range = 0;
                        if (distance <= 300D) {
                            range = 4;
                        }
                        if ((distance < 450D) && (distance >= 300D)) {
                            range = 3;
                        }
                        if ((distance < 600D) && (distance >= 450D)) {
                            range = 2;
                        }
                        if (distance >= 600D) {
                            range = 1;
                        }
                        mark = "Radarmark" + range + "0" + radarMarkIndex;
                        mesh.setCurChunk(mark);
                        RadarAPS.xyz[2] = 0.0F;
                        RadarAPS.xyz[1] = 0.0F;
                        RadarAPS.xyz[0] = 0.0F;
                        RadarAPS.ypr[2] = 0.0F;
                        RadarAPS.ypr[1] = 0.0F;
                        RadarAPS.ypr[0] = 0.0F;
                        RadarAPS.xyz[0] = x;
                        RadarAPS.xyz[2] = y;
                        mesh.chunkSetLocate(RadarAPS.xyz, RadarAPS.ypr);
                        mesh.render();
                        if (!mesh.isChunkVisible(mark)) {
                            mesh.chunkVisible(mark, true);
                        }
                        radarMarkIndex++;
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void radarScanAircraft() {
        if (!this.power || !this.messages || !Config.isUSE_RENDER() || (this.owner != World.getPlayerAircraft())) {
            return;
        }
        if (!this.owner.isAlive()) {
            return;
        }
        double aircraftX = Main3D.cur3D().land2D.worldOfsX() + this.owner.pos.getAbsPoint().x;
        double aircraftY = Main3D.cur3D().land2D.worldOfsY() + this.owner.pos.getAbsPoint().y;
        double aircraftZ = Main3D.cur3D().land2D.worldOfsY() + this.owner.pos.getAbsPoint().z;
        double altitudeAboveGround = aircraftZ - Landscape.Hmin((float) this.owner.pos.getAbsPoint().x, (float) this.owner.pos.getAbsPoint().y);
        if (altitudeAboveGround < 0.0D) {
            altitudeAboveGround = 0.0D;
        }
        int aircraftAzimuth = (int) (-(this.owner.pos.getAbsOrient().getYaw() - 90D));
        while (aircraftAzimuth < 0) {
            aircraftAzimuth += 360;
        }
        int aircraftElevation = (int) (-(this.owner.pos.getAbsOrient().getPitch() - 90D));
        while (aircraftElevation < 0) {
            aircraftElevation += 360;
        }
        for (Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
            Actor target = (Actor) entry.getValue();
            if ((target instanceof Aircraft) && (target.getArmy() != this.owner.getArmy()) && (target != this.owner) && (target.getSpeed(null) > 20D)) {
                double targetX = Main3D.cur3D().land2D.worldOfsX() + target.pos.getAbsPoint().x;
                double targetY = Main3D.cur3D().land2D.worldOfsY() + target.pos.getAbsPoint().y;
                double targetZ = Main3D.cur3D().land2D.worldOfsY() + target.pos.getAbsPoint().z;
                double altitudeDifference = (int) (Math.ceil((aircraftZ - targetZ) / 10D) * 10D);
                String elevationMessage = "level with us";
                if ((aircraftZ - targetZ - 300D) >= 0.0D) {
                    elevationMessage = "below us";
                }
                if (((aircraftZ - targetZ) + 300D) <= 0.0D) {
                    elevationMessage = "above us";
                }
                if (((aircraftZ - targetZ - 300D) < 0.0D) && ((aircraftZ - targetZ - 150D) >= 0.0D)) {
                    elevationMessage = "slightly below";
                }
                if ((((aircraftZ - targetZ) + 300D) > 0.0D) && (((aircraftZ - targetZ) + 150D) < 0.0D)) {
                    elevationMessage = "slightly above";
                }
                double xDifference = targetX - aircraftX;
                double yDifference = targetY - aircraftY;
                int targetAzimuth = (int) (Math.floor(Math.toDegrees(Math.atan2(yDifference, -xDifference)) - 90D));
                if (targetAzimuth < 0) {
                    targetAzimuth += 360;
                }
                int azimuthDifference = targetAzimuth - aircraftAzimuth;
                float randomFactor = ((TrueRandom.nextInt(20) - 10F) / 100F) + 1.0F;
                int randomAngle = TrueRandom.nextInt(6) - 3;
                int distance = (int) (Math.ceil(Math.sqrt((((aircraftY - targetY) * (aircraftY - targetY)) + ((aircraftX - targetX) * (aircraftX - targetX))) * randomFactor) / 10D) * 10D);
                if ((float) distance > 10000) {
                    distance = (int) (Math.ceil(Math.sqrt(((aircraftY - targetY) * (aircraftY - targetY)) + ((aircraftX - targetX) * (aircraftX - targetX))) / 10D) * 10D);
                }
                int elevation = ((int) (Math.floor((int) Math.toDegrees(Math.atan2(distance, altitudeDifference)) - 90D)) - (90 - aircraftElevation)) + randomAngle;
                int azimuth = azimuthDifference + randomAngle;
                int azimuth360 = azimuth;
                while (azimuth360 < 0) {
                    azimuth360 += 360;
                }
                String azimuthMessage = "  ";
                if (azimuth360 < 5) {
                    azimuthMessage = "dead ahead, ";
                }
                if ((azimuth360 >= 5) && (azimuth360 <= 7)) {
                    azimuthMessage = "right by 5\260, ";
                }
                if ((azimuth360 > 7D) && (azimuth360 <= 12)) {
                    azimuthMessage = "right by 10\260, ";
                }
                if ((azimuth360 > 12) && (azimuth360 <= 17)) {
                    azimuthMessage = "right by 15\260, ";
                }
                if ((azimuth360 > 17) && (azimuth360 <= 25)) {
                    azimuthMessage = "right by 20\260, ";
                }
                if ((azimuth360 > 25) && (azimuth360 <= 35)) {
                    azimuthMessage = "right by 30\260, ";
                }
                if ((azimuth360 > 35) && (azimuth360 <= 45)) {
                    azimuthMessage = "right by 40\260, ";
                }
                if ((azimuth360 > 45) && (azimuth360 <= 60)) {
                    azimuthMessage = "off our right, ";
                }
                if (azimuth360 > 355) {
                    azimuthMessage = "dead ahead, ";
                }
                if ((azimuth360 <= 355) && (azimuth360 >= 352)) {
                    azimuthMessage = "left by 5\260, ";
                }
                if ((azimuth360 < 352) && (azimuth360 >= 347)) {
                    azimuthMessage = "left by 10\260, ";
                }
                if ((azimuth360 < 347) && (azimuth360 >= 342)) {
                    azimuthMessage = "left by 15\260, ";
                }
                if ((azimuth360 < 342) && (azimuth360 >= 335)) {
                    azimuthMessage = "left by 20\260, ";
                }
                if ((azimuth360 < 335) && (azimuth360 >= 325)) {
                    azimuthMessage = "left by 30\260, ";
                }
                if ((azimuth360 < 325) && (azimuth360 >= 315)) {
                    azimuthMessage = "left by 40\260, ";
                }
                if ((azimuth360 < 345) && (azimuth360 >= 300)) {
                    azimuthMessage = "off our left, ";
                }
//                System.out.println("RadarAPS6 radarScanAircraft xyDifference=" + xyDifference + ", elevation=" + elevation + ", Math.sqrt(azimuth²)=" + Math.sqrt(azimuth * azimuth));
                if ((distance <= 25000) && (distance > 2000) && (elevation >= -20) && (elevation <= 20) && (Math.sqrt(azimuth * azimuth) <= 60)) {
                    HUD.logCenter(RadarAPS.NAMES[this.type] + ": Contact " + azimuthMessage + elevationMessage + ", " + distance + "m");
                }
            }
        }
    }

    public void auxPressed(int auxKey, int auxKeyPower, int auxKeyMessages) {
        if (!Config.isUSE_RENDER() || (this.owner != World.getPlayerAircraft()) || !this.owner.isAlive()) {
            return;
        }
        if (auxKey == auxKeyPower) {
            this.power = !this.power;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Radar " + RadarAPS.NAMES[this.type] + ": " + (this.power ? "ON" : "OFF"));
        } else if (auxKey == auxKeyMessages) {
            this.messages = !this.messages;
            HUD.log("Radar station " + RadarAPS.NAMES[this.type] + " messages: " + (this.messages ? "ON" : "OFF"));
        }
    }

    public boolean hasMessages() {
        if (!Config.isUSE_RENDER() || (this.owner != World.getPlayerAircraft()) || !this.owner.isAlive()) {
            return false;
        }
        return this.messages;
    }

    public boolean hasPower() {
        if (!Config.isUSE_RENDER() || (this.owner != World.getPlayerAircraft()) || !this.owner.isAlive()) {
            return false;
        }
        return this.power;
    }

    private int                   type;
    public static final int       APS6  = 0;
    public static final int       APS19 = 1;
    private static final String[] NAMES = { "AN/APS-6", "AN/APS-19" };

    private Aircraft              owner;
    private boolean               messages;
    private boolean               power;
    private int                   counter;
    private static float[]        ypr   = { 0.0F, 0.0F, 0.0F };
    private static float[]        xyz   = { 0.0F, 0.0F, 0.0F };
    private long                  timeout;
    private ArrayList             radarPlane;
    private float                 rRange;
    private float                 rClose;
    private int                   nTgts;
}
