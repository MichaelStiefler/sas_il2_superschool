package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class VoisinLAP extends Voisin1 {

    public VoisinLAP() {
        VoisinLAP.bChangedPit = true;
        this.lLightHook = new Hook[4];
        this.lightTime = 0.0F;
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 1:
                this.FM.turret[0].bIsOperable = false;
                break;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            default:
                break;

            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                if (!this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Gore1_D0", true);
                }
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                if (!this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Gore2_D0", true);
                }
                break;
        }
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberAdjDistanceReset() {
    }

    public void typeBomberAdjDistancePlus() {
    }

    public void typeBomberAdjDistanceMinus() {
    }

    public void typeBomberAdjSideslipReset() {
    }

    public void typeBomberAdjSideslipPlus() {
    }

    public void typeBomberAdjSideslipMinus() {
    }

    public void typeBomberAdjAltitudeReset() {
    }

    public void typeBomberAdjAltitudePlus() {
    }

    public void typeBomberAdjAltitudeMinus() {
    }

    public void typeBomberAdjSpeedReset() {
    }

    public void typeBomberAdjSpeedPlus() {
    }

    public void typeBomberAdjSpeedMinus() {
    }

    public void typeBomberUpdate(float f) {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    public void updateLLights() {
        this.pos.getRender(Actor._tmpLoc);
        if (this.lLight == null) {
            if (Actor._tmpLoc.getX() >= 1.0D) {
                this.lLight = new LightPointWorld[4];
                for (int i = 0; i < 4; i++) {
                    this.lLight[i] = new LightPointWorld();
                    this.lLight[i].setColor(1.0F, 1.0F, 1.0F);
                    this.lLight[i].setEmit(0.0F, 0.0F);
                    try {
                        this.lLightHook[i] = new HookNamed(this, "_LandingLight0" + i);
                    } catch (Exception exception) {
                    }
                }

            }
        } else {
            for (int j = 0; j < 4; j++) {
                if (this.FM.AS.astateLandingLightEffects[j] != null) {
                    VoisinLAP.lLightLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    this.lLightHook[j].computePos(this, Actor._tmpLoc, VoisinLAP.lLightLoc1);
                    VoisinLAP.lLightLoc1.get(VoisinLAP.lLightP1);
                    VoisinLAP.lLightLoc1.set(2000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    this.lLightHook[j].computePos(this, Actor._tmpLoc, VoisinLAP.lLightLoc1);
                    VoisinLAP.lLightLoc1.get(VoisinLAP.lLightP2);
                    Engine.land();
                    if (Landscape.rayHitHQ(VoisinLAP.lLightP1, VoisinLAP.lLightP2, VoisinLAP.lLightPL)) {
                        VoisinLAP.lLightPL.z++;
                        VoisinLAP.lLightP2.interpolate(VoisinLAP.lLightP1, VoisinLAP.lLightPL, 0.95F);
                        this.lLight[j].setPos(VoisinLAP.lLightP2);
                        float f = (float) VoisinLAP.lLightP1.distance(VoisinLAP.lLightPL);
                        float f1 = (f * 0.5F) + 60F;
                        float f2 = 0.7F - ((0.8F * f * this.lightTime) / 2000F);
                        this.lLight[j].setEmit(f2, f1);
                    } else {
                        this.lLight[j].setEmit(0.0F, 0.0F);
                    }
                } else if (this.lLight[j].getR() != 0.0F) {
                    this.lLight[j].setEmit(0.0F, 0.0F);
                }
            }

        }
    }

    protected void moveFan(float f) {
        if (this.bDynamoOperational) {
            this.pk = Math.abs((int) (this.FM.Vwld.length() / 14D));
            if (this.pk >= 1) {
                this.pk = 1;
            }
        }
        if (this.bDynamoRotary != (this.pk == 1)) {
            this.bDynamoRotary = this.pk == 1;
            this.hierMesh().chunkVisible("Gener_D0", !this.bDynamoRotary);
            this.hierMesh().chunkVisible("Generrot_D0", this.bDynamoRotary);
        }
        this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 17.987F) % 360F : (float) (this.dynamoOrient - (this.FM.Vwld.length() * 1.5444015264511108D)) % 360F;
        this.hierMesh().chunkSetAngles("Gener_D0", 0.0F, this.dynamoOrient, 0.0F);
        super.moveFan(f);
    }

    static Class mthclass$(String s) {
        Class class1;
        try {
            class1 = Class.forName(s);
        } catch (ClassNotFoundException classnotfoundexception) {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
        return class1;
    }

    private float           lightTime;
    private LightPointWorld lLight[];
    private Hook            lLightHook[];
    public static boolean   bChangedPit    = false;
    public static boolean   bSecondPilot   = false;
    public static boolean   bSecondCockpit = false;
    private static Loc      lLightLoc1     = new Loc();
    private static Point3d  lLightP1       = new Point3d();
    private static Point3d  lLightP2       = new Point3d();
    private static Point3d  lLightPL       = new Point3d();
    private boolean         bDynamoOperational;
    private float           dynamoOrient;
    private boolean         bDynamoRotary;
    private int             pk;

    static {
        Class class1 = VoisinLAP.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "VoisinLAP");
        Property.set(class1, "meshName", "3do/plane/VoisinLAP/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1967.8F);
        Property.set(class1, "FlightModel", "FlightModels/VoisinLAP.fmd:Voisin_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitVoisinLAP.class, CockpitVoisinLAP_TGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08" });
    }
}
