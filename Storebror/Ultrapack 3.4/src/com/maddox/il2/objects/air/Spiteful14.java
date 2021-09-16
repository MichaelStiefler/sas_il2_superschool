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

public class Spiteful14 extends SPITFIRE9 implements TypeFighterAceMaker, TypeBNZFighter {

    public Spiteful14() {
        this.k14Mode = 0;
        this.k14WingspanType = 0;
        this.k14Distance = 200F;
        this.flapps = 0.0F;
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -0.55F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        float f1 = (float) Math.sin(Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 3.141593F));
        this.hierMesh().chunkSetAngles("Pilot1_D0", 0.0F, 0.0F, 9F * f1);
        this.hierMesh().chunkSetAngles("Head1_D0", 12F * f1, 0.0F, 0.0F);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    protected void moveFlap(float paramFloat) {
        float f = -50F * paramFloat;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f, 0.0F);
    }

    public static void moveGear(HierMesh hiermesh, float leftGearPos, float rightGearPos, float tailWheelPos, boolean bDown) {
        if (bDown) {
            hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(tailWheelPos, 0.75F, 0.925F, 0.0F, -55F), 0.0F);
            hiermesh.chunkSetAngles("GearC3_D0", 0.0F, Aircraft.cvt(tailWheelPos, 0.71F, 0.76F, 0.0F, -50F), 0.0F);
            hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(tailWheelPos, 0.7F, 0.75F, 0.0F, -50F), 0.0F);
        } else {
            hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(tailWheelPos, 0.06F, 0.225F, 0.0F, -55F), 0.0F);
            hiermesh.chunkSetAngles("GearC3_D0", 0.0F, Aircraft.cvt(tailWheelPos, 0.02F, 0.07F, 0.0F, -50F), 0.0F);
            hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(tailWheelPos, 0.01F, 0.06F, 0.0F, -50F), 0.0F);
        }
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(leftGearPos, 0.1F, 0.5F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(leftGearPos, 0.1F, 0.5F, 0.0F, -90F), 0.0F);
        if (leftGearPos < 0.5F) {
            hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(leftGearPos, 0.01F, 0.12F, 0.0F, -90F), 0.0F);
        } else {
            hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(leftGearPos, 0.48F, 0.6F, -90F, 0.0F), 0.0F);
        }
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(rightGearPos, 0.4F, 0.8F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(rightGearPos, 0.4F, 0.8F, 0.0F, -90F), 0.0F);
        if (rightGearPos < 0.5F) {
            hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(rightGearPos, 0.3F, 0.42F, 0.0F, -90F), 0.0F);
        } else {
            hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(rightGearPos, 0.78F, 0.9F, -90F, 0.0F), 0.0F);
        }
    }

    public static void moveGear(HierMesh hiermesh, float leftGearPos, float rightGearPos, float tailWheelPos) {
        Spiteful14.moveGear(hiermesh, leftGearPos, rightGearPos, tailWheelPos, true);
    }

    protected void moveGear(float leftGearPos, float rightGearPos, float tailWheelPos) {
        Spiteful14.moveGear(this.hierMesh(), leftGearPos, rightGearPos, tailWheelPos, this.FM.CT.GearControl > 0.5F);
    }

    public static void moveGear(HierMesh hiermesh, float gearPos, boolean bDown) {
        Spiteful14.moveGear(hiermesh, gearPos, gearPos, gearPos, bDown);
    }

    public static void moveGear(HierMesh hiermesh, float gearPos) {
        Spiteful14.moveGear(hiermesh, gearPos, gearPos, gearPos, true);
    }

    protected void moveGear(float gearPos) {
        Spiteful14.moveGear(this.hierMesh(), gearPos, this.FM.CT.GearControl > 0.5F);
    }

    public void moveSteering(float f1) {
    }

    public boolean typeFighterAceMakerToggleAutomation() {
        this.k14Mode++;
        if (this.k14Mode > 2) {
            this.k14Mode = 0;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + this.k14Mode);
        return true;
    }

    public void typeFighterAceMakerAdjDistanceReset() {
    }

    public void typeFighterAceMakerAdjDistancePlus() {
        this.k14Distance += 10F;
        if (this.k14Distance > 800F) {
            this.k14Distance = 800F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
    }

    public void typeFighterAceMakerAdjDistanceMinus() {
        this.k14Distance -= 10F;
        if (this.k14Distance < 200F) {
            this.k14Distance = 200F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
    }

    public void typeFighterAceMakerAdjSideslipReset() {
    }

    public void typeFighterAceMakerAdjSideslipPlus() {
        this.k14WingspanType--;
        if (this.k14WingspanType < 0) {
            this.k14WingspanType = 0;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
    }

    public void typeFighterAceMakerAdjSideslipMinus() {
        this.k14WingspanType++;
        if (this.k14WingspanType > 9) {
            this.k14WingspanType = 9;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
    }

    public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeByte(this.k14Mode);
        netmsgguaranted.writeByte(this.k14WingspanType);
        netmsgguaranted.writeFloat(this.k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        this.k14Mode = netmsginput.readByte();
        this.k14WingspanType = netmsginput.readByte();
        this.k14Distance = netmsginput.readFloat();
    }

    public void update(float f) {
        float radiatorPos = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps - radiatorPos) > 0.01F) {
            this.flapps = radiatorPos;
            this.hierMesh().chunkSetAngles("Water7_D0", 0.0F, radiatorPos * -17F, 0.0F);
            this.hierMesh().chunkSetAngles("Water8_D0", 0.0F, radiatorPos * -17F, 0.0F);
        }
        super.update(f);
    }

    public int    k14Mode;
    public int    k14WingspanType;
    public float  k14Distance;
    private float flapps;

    static {
        Class class1 = Spiteful14.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Spit");
        Property.set(class1, "meshName", "3DO/Plane/Spiteful_FMkXIV(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/Spiteful-F-XIV.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSpiteful14.class });
        Property.set(class1, "LOSElevation", 0.5926F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04" });
    }
}
