package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Interpolate;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class P_11CPL extends P_11 {
    class Move extends Interpolate {

        public boolean tick() {
            if (Time.current() > (2200L + P_11CPL.this.animStartTime)) {
                P_11CPL.this.finishAnimation();
                return false;
            } else {
                P_11CPL.this.setAnimFrame(Time.tickNext());
                return true;
            }
        }

        Move() {
        }
    }

    public P_11CPL() {
        this.timer = 0L;
    }

    protected void moveFan(float f) {
        if (!Config.isUSE_RENDER()) {
            return;
        } else {
            float f1 = f;
            super.moveFan(f1);
            return;
        }
    }

    public void update(float f) {
        this.stageTiming();
        super.update(f);
    }

    private void stageTiming() {
        if (this.isNetMirror()) {
            return;
        }
        int i = this.FM.EI.engines[0].getStage();
        if (i < 2) {
            this.timer = Time.current();
        } else if (i == 2) {
            this.timeLapsed = Time.current() - this.timer;
            if (this.timeLapsed > 2250L) {
                this.FM.EI.engines[0].setStage(this, 3);
                this.timer = 0L;
            }
        }
    }

    public void hitDaSilk() {
        if ((this.FM.EI.engines[0].getStage() == 0) && this.FM.isStationedOnGround()) {
            if (P_11CPL.bPilotInPit) {
                P_11CPL.bPilotInPit = false;
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.reverse = true;
                if (!this.startAnimation()) {
                    this.hierMesh().chunkVisible("Pilot1_Out_D0", true);
                }
            } else {
                P_11CPL.bPilotInPit = true;
                this.reverse = false;
                if (!this.startAnimation()) {
                    this.hierMesh().chunkVisible("Pilot1_Out_D0", false);
                    this.hierMesh().chunkVisible("Pilot1_D0", true);
                    this.hierMesh().chunkVisible("Head1_D0", true);
                }
            }
        } else {
            super.hitDaSilk();
        }
    }

    private void setAnimFrame(long l) {
        double d = 2200D;
        double d1 = l - this.animStartTime;
        float f;
        if (d1 < 0.0D) {
            f = 0.0F;
        } else if (d1 > d) {
            f = 1.0F;
        } else {
            f = (float) (d1 / d);
        }
        this.hierMesh().setFrameFromRange(this.firstFrame, this.lastFrame, f);
        this.hierMesh().chunkVisible(true);
    }

    private boolean startAnimation() {
        int i = this.hierMesh().chunkFindCheck("Pilot1_Anim_D0");
        if (i != -1) {
            this.hierMesh().setCurChunk("Pilot1_Anim_D0");
            if (this.reverse) {
                this.firstFrame = this.hierMesh().frames() - 1;
                this.lastFrame = 0;
            } else {
                this.firstFrame = 0;
                this.lastFrame = this.hierMesh().frames() - 1;
            }
            this.animStartTime = Time.tick();
            if (!this.interpEnd("move")) {
                this.interpPut(new Move(), "move", Time.current(), null);
            }
            return true;
        } else {
            return false;
        }
    }

    private void finishAnimation() {
        if (!this.reverse) {
            this.hierMesh().chunkVisible(false);
            this.hierMesh().chunkVisible("Pilot1_D0", true);
            this.hierMesh().chunkVisible("Head1_D0", true);
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.startsWith("2")) {
            this.hierMesh().chunkVisible("WingGuns_D0", false);
            this.FM.M.mass += 23F;
        } else {
            this.FM.M.mass += 46F;
        }
        if (this.thisWeaponsName.endsWith("125") || this.thisWeaponsName.endsWith("5Z")) {
            this.hierMesh().chunkVisible("RackL_D0", true);
            this.hierMesh().chunkVisible("RackR_D0", true);
        }
        System.out.println("*** Weapons mass " + this.FM.CT.getWeaponMass() + "kg");
        System.out.println("*** Fuel mass " + this.FM.M.fuel + "kg");
        System.out.println("*** Aircraft base mass " + this.FM.M.massEmpty + "kg");
        float f = this.FM.M.mass + this.FM.CT.getWeaponMass();
        System.out.println("*** Aircraft take off mass " + f + "kg");
        System.out.println("*** Aircraft max mass " + this.FM.M.maxWeight + "kg");
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            P_11CPL.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            P_11CPL.bChangedPit = true;
        }
    }

    public static boolean bChangedPit = false;
    public static boolean bPilotInPit = true;
    private long          timer;
    private long          timeLapsed;
    private long          animStartTime;
    private int           firstFrame;
    private int           lastFrame;
    private boolean       reverse;
    static {
        Class class1 = P_11CPL.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P.11");
        Property.set(class1, "meshName", "3DO/Plane/P-11c/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFCSPar01());
        Property.set(class1, "originCountry", PaintScheme.countryPoland);
        Property.set(class1, "yearService", 1934F);
        Property.set(class1, "yearExpired", 1939.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-11c.fmd:PZL_P11");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_11CPL.class });
        Property.set(class1, "LOSElevation", 0.7956F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02" });
    }

}
