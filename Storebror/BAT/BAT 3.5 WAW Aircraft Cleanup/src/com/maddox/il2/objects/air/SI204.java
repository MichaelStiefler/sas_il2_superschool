package com.maddox.il2.objects.air;

import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;

public class SI204 extends Scheme2 implements TypeTransport {

    public SI204() {
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, -90F * f);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, -90F * f);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 0.0F, 90F * f);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 0.0F, 90F * f);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -20F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Rudder2_D0", 0.0F, -20F * f, 0.0F);
    }

    protected void moveFlap(float f) {
        float f1 = -45F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap03_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f1, 0.0F);
    }

    protected void moveGear(float f) {
        SI204.moveGear(this.hierMesh(), f);
    }

    public void msgShot(Shot shot) {
        this.setShot(shot);
        if (shot.chunkName.startsWith("WingLOut") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) && (Math.abs(((Tuple3d) (Aircraft.Pd)).y) < 6D)) {
            this.FM.AS.hitTank(shot.initiator, 0, 1);
        }
        if (shot.chunkName.startsWith("WingROut") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) && (Math.abs(((Tuple3d) (Aircraft.Pd)).y) < 6D)) {
            this.FM.AS.hitTank(shot.initiator, 3, 1);
        }
        if (shot.chunkName.startsWith("WingLIn") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) && (Math.abs(((Tuple3d) (Aircraft.Pd)).y) < 1.940000057220459D)) {
            this.FM.AS.hitTank(shot.initiator, 1, 1);
        }
        if (shot.chunkName.startsWith("WingRIn") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) && (Math.abs(((Tuple3d) (Aircraft.Pd)).y) < 1.940000057220459D)) {
            this.FM.AS.hitTank(shot.initiator, 2, 1);
        }
        if (shot.chunkName.startsWith("Engine1") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            this.FM.AS.hitEngine(shot.initiator, 0, 1);
        }
        if (shot.chunkName.startsWith("Engine2") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            this.FM.AS.hitEngine(shot.initiator, 1, 1);
        }
        if (shot.chunkName.startsWith("Nose") && (((Tuple3d) (Aircraft.Pd)).x > 4.9000000953674316D) && (((Tuple3d) (Aircraft.Pd)).z > -0.090000003576278687D) && (World.Rnd().nextFloat() < 0.1F)) {
            if (((Tuple3d) (Aircraft.Pd)).y > 0.0D) {
                this.killPilot(shot.initiator, 0);
                this.FM.setCapableOfBMP(false, shot.initiator);
            } else {
                this.killPilot(shot.initiator, 2);
                if ((((Tuple3d) (Aircraft.Pd)).z > 1.3350000381469727D) && (shot.initiator == World.getPlayerAircraft()) && World.cur().isArcade()) {
                    HUD.logCenter("H E A D S H O T");
                }
            }
        } else {
            if (shot.chunkName.startsWith("Tail") && (((Tuple3d) (Aircraft.Pd)).x < -5.8000001907348633D) && (((Tuple3d) (Aircraft.Pd)).x > -6.7899999618530273D) && (((Tuple3d) (Aircraft.Pd)).z > -0.44900000000000001D) && (((Tuple3d) (Aircraft.Pd)).z < 0.12399999797344208D)) {
                this.FM.AS.hitPilot(shot.initiator, World.Rnd().nextInt(3, 4), (int) (shot.mass * 1000F * World.Rnd().nextFloat(0.9F, 1.1F)));
            }
            if ((this.FM.AS.astateEngineStates[0] > 2) && (this.FM.AS.astateEngineStates[1] > 2) && (World.Rnd().nextInt(0, 99) < 33)) {
                this.FM.setCapableOfBMP(false, shot.initiator);
            }
            super.msgShot(shot);
        }
    }

    public void doKillPilot(int i) {
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
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
            this.hierMesh().chunkVisible("HMask2_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            default:
                break;

            case 13:
                this.killPilot(this, 0);
                this.killPilot(this, 1);
                break;

            case 35:
                if (World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.hitTank(this, 1, World.Rnd().nextInt(2, 6));
                }
                break;

            case 38:
                if (World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.hitTank(this, 2, World.Rnd().nextInt(2, 6));
                }
                break;
        }
        return super.cutFM(i, j, actor);
    }

    static {
        Class class1 = SI204.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Si-204");
        Property.set(class1, "meshName", "3do/plane/Si-204/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/Si-204.fmd:Si204");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSI204.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_BombSpawn01" });
    }
}
