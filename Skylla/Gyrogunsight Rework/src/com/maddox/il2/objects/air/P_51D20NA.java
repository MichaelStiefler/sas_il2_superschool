// 
// Decompiled by Procyon v0.5.29
// 

package com.maddox.il2.objects.air;

import com.maddox.rts.Property;
import com.maddox.rts.NetMsgInput;
import java.io.IOException;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.AircraftHotKeys;

public class P_51D20NA extends P_51 implements TypeFighterAceMaker
{
    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    static /* synthetic */ Class class$com$maddox$il2$objects$air$P_51D20NA;
    static /* synthetic */ Class class$com$maddox$il2$objects$air$CockpitP_51D20K14;
    
    public P_51D20NA() {
        this.k14Mode = 0;
        this.k14WingspanType = 0;
        this.k14Distance = 200.0f;
    }
    
    public boolean typeFighterAceMakerToggleAutomation() {
        ++this.k14Mode;
        if (this.k14Mode > 2) {
            this.k14Mode = 0;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + this.k14Mode);
        return true;
    }
    
    public void typeFighterAceMakerAdjDistanceReset() {
    }
    
  //-------------------------------------------------------------------------------------------------------
    //TODO: skylla: gyro-gunsight distance HUD log (for details please see P_51D25NA.class):
    
    public void typeFighterAceMakerAdjDistancePlus() {
    	adjustK14AceMakerDistance(+10.0f);
    }
    
    public void typeFighterAceMakerAdjDistanceMinus() {
    	adjustK14AceMakerDistance(-10.0f);
    }
    
    private void adjustK14AceMakerDistance(float f) {
    	this.k14Distance += f;
    	if(this.k14Distance > 730.0f) {
    		this.k14Distance = 730.0f;
    	} else if(this.k14Distance < 180.0f) {
    		this.k14Distance = 180.0f;
    	}
    	HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Distance: " + (int)(this.k14Distance) + "m" );
    }
    
    /*
    public void typeFighterAceMakerAdjDistancePlus() {
        this.k14Distance += 10.0f;
        if (this.k14Distance > 800.0f) {
            this.k14Distance = 800.0f;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
    }
    
    public void typeFighterAceMakerAdjDistanceMinus() {
        this.k14Distance -= 10.0f;
        if (this.k14Distance < 200.0f) {
            this.k14Distance = 200.0f;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
    }
    */
  //-------------------------------------------------------------------------------------------------------
    
    public void typeFighterAceMakerAdjSideslipReset() {
    }
    
    public void typeFighterAceMakerAdjSideslipPlus() {
        --this.k14WingspanType;
        if (this.k14WingspanType < 0) {
            this.k14WingspanType = 0;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
    }
    
    public void typeFighterAceMakerAdjSideslipMinus() {
        ++this.k14WingspanType;
        if (this.k14WingspanType > 9) {
            this.k14WingspanType = 9;
        }
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
    
    static /* synthetic */ Class class$(final String s) {
        try {
            return Class.forName(s);
        }
        catch (ClassNotFoundException ex) {
            throw new NoClassDefFoundError(ex.getMessage());
        }
    }
    
    static {
        final Class var_class = (P_51D20NA.class$com$maddox$il2$objects$air$P_51D20NA == null) ? (P_51D20NA.class$com$maddox$il2$objects$air$P_51D20NA = class$("com.maddox.il2.objects.air.P_51D20NA")) : P_51D20NA.class$com$maddox$il2$objects$air$P_51D20NA;
        new SPAWN(var_class);
        Property.set(var_class, "iconFar_shortClassName", "P-51");
        Property.set(var_class, "meshNameDemo", "3DO/Plane/P-51D-20NA(USA)/hier.him");
        Property.set(var_class, "meshName", "3DO/Plane/P-51D-20NA(Multi1)/hier.him");
        Property.set(var_class, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(var_class, "meshName_us", "3DO/Plane/P-51D-20NA(USA)/hier.him");
        Property.set(var_class, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(var_class, "noseart", 1);
        Property.set(var_class, "yearService", 1944.0f);
        Property.set(var_class, "yearExpired", 1947.5f);
        Property.set(var_class, "FlightModel", "FlightModels/P-51D-20.fmd");
        Property.set(var_class, "cockpitClass", (Object)((P_51D20NA.class$com$maddox$il2$objects$air$CockpitP_51D20K14 == null) ? (P_51D20NA.class$com$maddox$il2$objects$air$CockpitP_51D20K14 = class$("com.maddox.il2.objects.air.CockpitP_51D20K14")) : P_51D20NA.class$com$maddox$il2$objects$air$CockpitP_51D20K14));
        Property.set(var_class, "LOSElevation", 1.06935f);
        Aircraft.weaponTriggersRegister(var_class, new int[] { 0, 0, 0, 0, 0, 0, 9, 9, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(var_class, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02" });
        Aircraft.weaponsRegister(var_class, "default", new String[] { "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 270", "MGunBrowning50k 270", "MGunBrowning50k 270", "MGunBrowning50k 270", null, null, null, null, null, null });
        Aircraft.weaponsRegister(var_class, "2x250", new String[] { "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 270", "MGunBrowning50k 270", "MGunBrowning50k 270", "MGunBrowning50k 270", "PylonP51PLN2", "PylonP51PLN2", "BombGun250lbs 1", "BombGun250lbs 1", null, null });
        Aircraft.weaponsRegister(var_class, "2x500", new String[] { "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 270", "MGunBrowning50k 270", "MGunBrowning50k 270", "MGunBrowning50k 270", "PylonP51PLN2", "PylonP51PLN2", "BombGun500lbs 1", "BombGun500lbs 1", null, null });
        Aircraft.weaponsRegister(var_class, "2x1000", new String[] { "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 270", "MGunBrowning50k 270", "MGunBrowning50k 270", "MGunBrowning50k 270", "PylonP51PLN2", "PylonP51PLN2", "BombGun1000lbs 1", "BombGun1000lbs 1", null, null });
        Aircraft.weaponsRegister(var_class, "2xTank", new String[] { "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 270", "MGunBrowning50k 270", "MGunBrowning50k 270", "MGunBrowning50k 270", "PylonP51PLN2", "PylonP51PLN2", null, null, "FuelTankGun_Tank75gal2", "FuelTankGun_Tank75gal2" });
        Aircraft.weaponsRegister(var_class, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null });
    }
}
