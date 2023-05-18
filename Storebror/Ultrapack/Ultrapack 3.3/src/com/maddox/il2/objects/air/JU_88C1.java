package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Property;

public class JU_88C1 extends JU_88Axx implements TypeFighter, TypeBNZFighter, TypeStormovik {

    public JU_88C1() {
        this.blisterRemoved = false;
        this.topBlisterRemoved = false;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
    }

    public void doWreck(String s) {
        if (this.hierMesh().chunkFindCheck(s) != -1) {
            this.hierMesh().hideSubTrees(s);
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind(s));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    public void blisterRemoved(int i) {
        if (i < 3) {
            if (!this.topBlisterRemoved) {
                this.doWreck("BlisterTop_D0");
                this.hierMesh().chunkVisible("Turret1B_D0", false);
                this.hierMesh().chunkVisible("Turret1C_D0", false);
                this.topBlisterRemoved = true;
            }
        } else if (!this.blisterRemoved && i == 3) {
            this.doWreck("BlisterDown_D0");
            this.hierMesh().chunkVisible("Turret2B_D0", false);
            this.hierMesh().chunkVisible("Turret2C_D0", false);
            this.blisterRemoved = true;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int i;
            if (s.endsWith("a")) {
                byte0 = 1;
                i = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                i = s.charAt(6) - 49;
            } else i = s.charAt(5) - 49;
            if (i == 2) i = 1;
            else if (i == 3) i = 2;
            this.hitFlesh(i, shot, byte0);
            return;
        }
        if (s.startsWith("xxmgun")) {
            if (s.endsWith("01")) {
                this.debuggunnery("MG17-1: Disabled..");
                this.FM.AS.setJamBullets(0, 0);
            }
            if (s.endsWith("02")) {
                this.debuggunnery("MG17-2: Disabled..");
                this.FM.AS.setJamBullets(0, 1);
            }
            if (s.endsWith("03")) {
                this.debuggunnery("MG17-3: Disabled..");
                this.FM.AS.setJamBullets(0, 2);
            }
            this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 12.96F), shot);
        }
        if (s.startsWith("xxcannon")) {
            if (s.endsWith("01")) {
                this.debuggunnery("Cannon MG151/20-1: Disabled..");
                this.FM.AS.setJamBullets(1, 0);
            }
            this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
        }
        if (s.equals("xxarmorp6")) this.getEnergyPastArmor(0.5F, shot);
        if (s.equals("xxarmorp7")) this.getEnergyPastArmor(0.5F, shot);
        super.hitBone(s, shot, point3d);
    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("Bay1_D0", 0.0F, 87F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay2_D0", 0.0F, -86F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay3_D0", 0.0F, 86F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay4_D0", 0.0F, -87F * f, 0.0F);
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 3; i++)
            if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    public void update(float f) {
        super.update(f);
        if (Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) > 70F && this.FM.CT.getFlap() > 0.01D && this.FM.CT.FlapsControl != 0.0F) {
            this.FM.CT.FlapsControl = 0.0F;
            if (((Interpolate) this.FM).actor == World.getPlayerAircraft()) HUD.log("FlapsRaised");
        }
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        if (f1 != 0.0F) this.hierMesh().chunkSetAngles("Radl11_D0", -30F * f1, 0.0F, 0.0F);
        f1 = this.FM.EI.engines[1].getControlRadiator();
        if (f1 != 0.0F) this.hierMesh().chunkSetAngles("Radr11_D0", -30F * f1, 0.0F, 0.0F);
//		if (this.FM.AS.bNavLightsOn) this.FM.CT.StabilizerControl = true;
//		else this.FM.CT.StabilizerControl = false;
    }

    public boolean turretAngles(int i, float af[]) {
        for (int j = 0; j < 2; j++) {
            af[j] = (af[j] + 3600F) % 360F;
            if (af[j] > 180F) af[j] -= 360F;
        }

        af[2] = 0.0F;
        boolean flag = true;
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f < -40F) {
                    f = -40F;
                    flag = false;
                }
                if (f > 40F) {
                    f = 40F;
                    flag = false;
                }
                if (f1 < -5F) {
                    f1 = -5F;
                    flag = false;
                }
                if (f1 > 60F) {
                    f1 = 60F;
                    flag = false;
                }
                if (f > 30F) {
                    if (f1 < Aircraft.cvt(f, 30F, 40F, -5F, 25F)) f1 = Aircraft.cvt(f, 30F, 40F, -5F, 25F);
                } else if (f > 5.3F) {
                    if (f1 < Aircraft.cvt(f, 5.3F, 25F, -1.72F, -5F)) f1 = Aircraft.cvt(f, 5.3F, 25F, -1.72F, -5F);
                } else if (f > -5.3F) {
                    if (f1 < -1.72F) f1 = -1.72F;
                } else if (f > -30F) {
                    if (f1 < Aircraft.cvt(f, -30F, -5.3F, 5F, -1.72F)) f1 = Aircraft.cvt(f, -30F, -5.3F, 5F, -1.72F);
                } else if (f1 < Aircraft.cvt(f, -40F, -30F, 25F, -5F)) f1 = Aircraft.cvt(f, -40F, -30F, 25F, -5F);
                break;

            case 1:
                if (f < -40F) {
                    f = -40F;
                    flag = false;
                }
                if (f > 40F) {
                    f = 40F;
                    flag = false;
                }
                if (f1 < -50F) {
                    f1 = -50F;
                    flag = false;
                }
                if (f1 > 0.0F) {
                    f1 = 0.0F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    protected void moveAirBrake(float f) {
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                break;

            case 2:
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D1", true);
                this.hierMesh().chunkVisible("HMask3_D0", false);
                break;
        }
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, -65F, 65F, 65F, -65F), 0.0F);
    }

    public static boolean bChangedPit = false;
    private boolean       blisterRemoved;
    private boolean       topBlisterRemoved;

    static {
        Class class1 = JU_88C1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ju-88");
        Property.set(class1, "meshName", "3DO/Plane/Ju-88C-1/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Ju-88C-1.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitJU_88C1.class });
        Property.set(class1, "LOSElevation", 1.0F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 0, 0, 0, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MG1701", "_MG1702", "_MG1703", "_MGFF01", "_BombSpawn15", "_BombSpawn16", "_BombSpawn17", "_BombSpawn18", "_BombSpawn19", "_BombSpawn20", "_BombSpawn21", "_BombSpawn22",
                "_BombSpawn23", "_BombSpawn24" });
    }
}
