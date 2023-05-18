package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class SPITFIRELF14E extends SPITFIRE9 implements TypeFighterAceMaker, TypeBNZFighter {
    public int   k14Mode;
    public int   k14WingspanType;
    public float k14Distance;
//    private float flapps;

    public SPITFIRELF14E() {
        this.k14Mode = 0;
        this.k14WingspanType = 0;
        this.k14Distance = 200.0f;
//        this.flapps = 0.0f;
    }

    public void moveCockpitDoor(final float n) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(n, 0.01f, 0.99f, 0.0f, 0.55f);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        final float n2 = (float) Math.sin(Aircraft.cvt(n, 0.01f, 0.99f, 0.0f, (float)Math.PI));
        this.hierMesh().chunkSetAngles("Pilot1_D0", 0.0f, 0.0f, 9.0f * n2);
        this.hierMesh().chunkSetAngles("Head1_D0", 12.0f * n2, 0.0f, 0.0f);
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(n);
            this.setDoorSnd(n);
        }
    }

    public static void moveGear(final HierMesh hierMesh, final float f) {
        hierMesh.chunkSetAngles("GearL2_D0", 0.0f, Aircraft.cvt(f, 0.0f, 0.6f, 0.0f, -95.0f), 0.0f);
        hierMesh.chunkSetAngles("GearR2_D0", 0.0f, Aircraft.cvt(f, 0.2f, 1.0f, 0.0f, -95.0f), 0.0f);
        hierMesh.chunkSetAngles("GearC2_D0", 0.0f, Aircraft.cvt(f, 0.01f, 0.99f, 0.0f, -75.0f), 0.0f);
        hierMesh.chunkSetAngles("GearC3_D0", 0.0f, 0.0f, 0.0f);
        hierMesh.chunkSetAngles("GearC4_D0", 0.0f, Aircraft.cvt(f, 0.01f, 0.09f, 0.0f, -75.0f), 0.0f);
        hierMesh.chunkSetAngles("GearC5_D0", 0.0f, Aircraft.cvt(f, 0.01f, 0.09f, 0.0f, -75.0f), 0.0f);
    }

    protected void moveGear(final float n) {
        moveGear(this.hierMesh(), n);
    }

    public void moveSteering(final float n) {
        this.hierMesh().chunkSetAngles("GearC3_D0", 0.0f, -n, 0.0f);
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0f, 0.247f, 0.0f, -0.247f);
        this.hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0f, 0.247f, 0.0f, 0.247f);
        this.hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public boolean typeFighterAceMakerToggleAutomation() {
        ++this.k14Mode;
        if (this.k14Mode > 2) this.k14Mode = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + this.k14Mode);
        return true;
    }

    public void typeFighterAceMakerAdjDistanceReset() {
    }

    // -------------------------------------------------------------------------------------------------------
    // TODO: skylla: gyro-gunsight distance HUD log (for details please see
    // P_51D25NA.class):

    public void typeFighterAceMakerAdjDistancePlus() {
        this.adjustK14AceMakerDistance(+10.0f);
    }

    public void typeFighterAceMakerAdjDistanceMinus() {
        this.adjustK14AceMakerDistance(-10.0f);
    }

    private void adjustK14AceMakerDistance(float f) {
        this.k14Distance += f;
        if (this.k14Distance > 730.0f) this.k14Distance = 730.0f;
        else if (this.k14Distance < 160.0f) this.k14Distance = 160.0f;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Distance: " + (int) this.k14Distance + "m");
    }
    /*
     * public void typeFighterAceMakerAdjDistancePlus() { this.k14Distance += 10.0f; if (this.k14Distance > 800.0f) { this.k14Distance = 800.0f; } HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc"); }
     *
     * public void typeFighterAceMakerAdjDistanceMinus() { this.k14Distance -= 10.0f; if (this.k14Distance < 200.0f) { this.k14Distance = 200.0f; } HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec"); }
     */
    // -------------------------------------------------------------------------------------------------------

    public void typeFighterAceMakerAdjSideslipReset() {
    }

    public void typeFighterAceMakerAdjSideslipPlus() {
        --this.k14WingspanType;
        if (this.k14WingspanType < 0) this.k14WingspanType = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
    }

    public void typeFighterAceMakerAdjSideslipMinus() {
        ++this.k14WingspanType;
        if (this.k14WingspanType > 9) this.k14WingspanType = 9;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
    }

    public void typeFighterAceMakerReplicateToNet(final NetMsgGuaranted netMsgGuaranted) throws IOException {
        netMsgGuaranted.writeByte(this.k14Mode);
        netMsgGuaranted.writeByte(this.k14WingspanType);
        netMsgGuaranted.writeFloat(this.k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(final NetMsgInput netMsgInput) throws IOException {
        this.k14Mode = netMsgInput.readByte();
        this.k14WingspanType = netMsgInput.readByte();
        this.k14Distance = netMsgInput.readFloat();
    }

    public void typeBomberReplicateToNet(final NetMsgGuaranted netMsgGuaranted) throws IOException {
    }

    public void typeBomberReplicateFromNet(final NetMsgInput netMsgInput) throws IOException {
    }

    static {
        final Class class1 = SPITFIRELF14E.class;
        new SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Spit");
        Property.set(class1, "meshName", "3DO/Plane/SpitfireMkLFXIVE(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1943.0f);
        Property.set(class1, "yearExpired", 1946.5f);
        Property.set(class1, "FlightModel", "FlightModels/Spitfire-F-XIV-G65-21.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSpitLF14E.class });
        Property.set(class1, "LOSElevation", 0.5926f);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN03", "_MGUN04", "_CANNON01", "_CANNON02", "_ExternalDev08", "_ExternalDev01", "_ExternalBomb01" });
    }
}
