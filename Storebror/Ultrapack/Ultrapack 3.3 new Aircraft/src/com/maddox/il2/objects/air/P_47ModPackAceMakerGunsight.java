package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;

public class P_47ModPackAceMakerGunsight extends P_47ModPack implements TypeFighterAceMaker {

	public int   k14Mode;
	public int   k14WingspanType;
	public float k14Distance;

	public P_47ModPackAceMakerGunsight() {
		this.k14Mode = 0;
		this.k14WingspanType = 0;
		this.k14Distance = 200.0F;
	}

	public boolean typeFighterAceMakerToggleAutomation() {
		this.k14Mode += 1;
		if (this.k14Mode > 2) this.k14Mode = 0;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + this.k14Mode);
		return true;
	}

	public void typeFighterAceMakerAdjDistanceReset() {
	}

	public void typeFighterAceMakerAdjDistancePlus() {
		this.k14Distance += 10.0F;
		if (this.k14Distance > 800.0F) this.k14Distance = 800.0F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
	}

	public void typeFighterAceMakerAdjDistanceMinus() {
		this.k14Distance -= 10.0F;
		if (this.k14Distance < 200.0F) this.k14Distance = 200.0F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
	}

	public void typeFighterAceMakerAdjSideslipReset() {
	}

	public void typeFighterAceMakerAdjSideslipPlus() {
		this.k14WingspanType -= 1;
		if (this.k14WingspanType < 0) this.k14WingspanType = 0;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
	}

	public void typeFighterAceMakerAdjSideslipMinus() {
		this.k14WingspanType += 1;
		if (this.k14WingspanType > 9) this.k14WingspanType = 9;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
	}

	public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted paramNetMsgGuaranted) throws IOException {
		paramNetMsgGuaranted.writeByte(this.k14Mode);
		paramNetMsgGuaranted.writeByte(this.k14WingspanType);
		paramNetMsgGuaranted.writeFloat(this.k14Distance);
	}

	public void typeFighterAceMakerReplicateFromNet(NetMsgInput paramNetMsgInput) throws IOException {
		this.k14Mode = paramNetMsgInput.readByte();
		this.k14WingspanType = paramNetMsgInput.readByte();
		this.k14Distance = paramNetMsgInput.readFloat();
	}
}
