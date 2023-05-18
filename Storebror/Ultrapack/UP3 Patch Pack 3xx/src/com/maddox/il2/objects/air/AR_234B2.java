package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.BombStarthilfe109500;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class AR_234B2 extends AR_234 {

	public AR_234B2() {
		this.bHasBoosters = true;
		this.boosterFireOutTime = -1L;
		this.bSightAutomation = false;
		this.bSightBombDump = false;
		this.fSightCurDistance = 0.0F;
		this.fSightCurForwardAngle = 0.0F;
		this.fSightCurSideslip = 0.0F;
		this.fSightCurAltitude = 850F;
		this.fSightCurSpeed = 150F;
		this.fSightCurReadyness = 0.0F;
	}

	public void destroy() {
		this.doCutBoosters();
		super.destroy();
	}

	public void doFireBoosters() {
		Eff3DActor.New(this, this.findHook("_Booster1"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff",
				30F);
		Eff3DActor.New(this, this.findHook("_Booster2"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff",
				30F);
	}

	public void doCutBoosters() {
		for (int i = 0; i < 2; i++)
			if (this.booster[i] != null) {
				this.booster[i].start();
				this.booster[i] = null;
			}

	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		for (int i = 0; i < 2; i++)
			try {
				this.booster[i] = new BombStarthilfe109500();
				this.booster[i].pos.setBase(this, this.findHook("_BoosterH" + (i + 1)), false);
				this.booster[i].pos.resetAsBase();
				this.booster[i].drawing(true);
			} catch (Exception exception) {
				this.debugprintln("Structure corrupt - can't hang Starthilferakete..");
			}

	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
		case 33: // '!'
		case 34: // '"'
		case 35: // '#'
		case 36: // '$'
		case 37: // '%'
		case 38: // '&'
			this.doCutBoosters();
			this.FM.AS.setGliderBoostOff();
			this.bHasBoosters = false;
			break;
		}
		return super.cutFM(i, j, actor);
	}

	public void update(float f) {
		super.update(f);
		if (!(this.FM instanceof Pilot))
			return;
		if (this.bHasBoosters) {
			// TODO: Changed Booster cutoff reasons from absolute altitude to altitude above
			// ground
//			if (FM.getAltitude() > 300F && boosterFireOutTime == -1L && FM.Loc.z != 0.0D && World.Rnd().nextFloat() < 0.05F) {
			if (this.FM.getAltitude() - World.land().HQ_Air(this.FM.Loc.x, this.FM.Loc.y) > 300F
					&& this.boosterFireOutTime == -1L && this.FM.Loc.z != 0.0D && World.Rnd().nextFloat() < 0.05F) {
				this.doCutBoosters();
				this.FM.AS.setGliderBoostOff();
				this.bHasBoosters = false;
			}
			if (this.bHasBoosters && this.boosterFireOutTime == -1L && this.FM.Gears.onGround()
					&& this.FM.EI.getPowerOutput() > 0.8F && this.FM.EI.engines[0].getStage() == 6
					&& this.FM.EI.engines[1].getStage() == 6 && this.FM.getSpeedKMH() > 20F) {
				this.boosterFireOutTime = Time.current() + 30000L;
				this.doFireBoosters();
				this.FM.AS.setGliderBoostOn();
			}
			if (this.bHasBoosters && this.boosterFireOutTime > 0L) {
				if (Time.current() < this.boosterFireOutTime)
					this.FM.producedAF.x += 20000D;
				if (Time.current() > this.boosterFireOutTime + 10000L) {
					this.doCutBoosters();
					this.FM.AS.setGliderBoostOff();
					this.bHasBoosters = false;
				}
			}
		}
	}

	public boolean typeDiveBomberToggleAutomation() {
		return false;
	}

	public void typeDiveBomberAdjAltitudeReset() {
	}

	public void typeDiveBomberAdjAltitudePlus() {
	}

	public void typeDiveBomberAdjAltitudeMinus() {
	}

	public void typeDiveBomberAdjVelocityReset() {
	}

	public void typeDiveBomberAdjVelocityPlus() {
	}

	public void typeDiveBomberAdjVelocityMinus() {
	}

	public void typeDiveBomberAdjDiveAngleReset() {
	}

	public void typeDiveBomberAdjDiveAnglePlus() {
	}

	public void typeDiveBomberAdjDiveAngleMinus() {
	}

	public void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
	}

	public void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
	}

	public boolean typeBomberToggleAutomation() {
		this.bSightAutomation = !this.bSightAutomation;
		this.bSightBombDump = false;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAutomation" + (this.bSightAutomation ? "ON" : "OFF"));
		return this.bSightAutomation;
	}

	public void typeBomberAdjDistanceReset() {
		this.fSightCurDistance = 0.0F;
		this.fSightCurForwardAngle = 0.0F;
	}

	public void typeBomberAdjDistancePlus() {
		this.fSightCurForwardAngle++;
		if (this.fSightCurForwardAngle > 85F)
			this.fSightCurForwardAngle = 85F;
		this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation",
				new Object[] { new Integer((int) this.fSightCurForwardAngle) });
		if (this.bSightAutomation)
			this.typeBomberToggleAutomation();
	}

	public void typeBomberAdjDistanceMinus() {
		this.fSightCurForwardAngle--;
		if (this.fSightCurForwardAngle < 0.0F)
			this.fSightCurForwardAngle = 0.0F;
		this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation",
				new Object[] { new Integer((int) this.fSightCurForwardAngle) });
		if (this.bSightAutomation)
			this.typeBomberToggleAutomation();
	}

	public void typeBomberAdjSideslipReset() {
		this.fSightCurSideslip = 0.0F;
	}

	public void typeBomberAdjSideslipPlus() {
		this.fSightCurSideslip += 0.05F;
		if (this.fSightCurSideslip > 3F)
			this.fSightCurSideslip = 3F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip",
				new Object[] { new Float(this.fSightCurSideslip * 10F) });
	}

	public void typeBomberAdjSideslipMinus() {
		this.fSightCurSideslip -= 0.05F;
		if (this.fSightCurSideslip < -3F)
			this.fSightCurSideslip = -3F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip",
				new Object[] { new Float(this.fSightCurSideslip * 10F) });
	}

	public void typeBomberAdjAltitudeReset() {
		this.fSightCurAltitude = 850F;
	}

	public void typeBomberAdjAltitudePlus() {
		this.fSightCurAltitude += 10F;
		if (this.fSightCurAltitude > 6000F)
			this.fSightCurAltitude = 6000F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude",
				new Object[] { new Integer((int) this.fSightCurAltitude) });
		this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
	}

	public void typeBomberAdjAltitudeMinus() {
		this.fSightCurAltitude -= 10F;
		if (this.fSightCurAltitude < 850F)
			this.fSightCurAltitude = 850F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude",
				new Object[] { new Integer((int) this.fSightCurAltitude) });
		this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
	}

	public void typeBomberAdjSpeedReset() {
		this.fSightCurSpeed = 250F;
	}

	public void typeBomberAdjSpeedPlus() {
		this.fSightCurSpeed += 10F;
		if (this.fSightCurSpeed > 900F)
			this.fSightCurSpeed = 900F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed",
				new Object[] { new Integer((int) this.fSightCurSpeed) });
	}

	public void typeBomberAdjSpeedMinus() {
		this.fSightCurSpeed -= 10F;
		if (this.fSightCurSpeed < 150F)
			this.fSightCurSpeed = 150F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed",
				new Object[] { new Integer((int) this.fSightCurSpeed) });
	}

	public void typeBomberUpdate(float f) {
		if (Math.abs(this.FM.Or.getKren()) > 4.5D) {
			this.fSightCurReadyness -= 0.0666666F * f;
			if (this.fSightCurReadyness < 0.0F)
				this.fSightCurReadyness = 0.0F;
		}
		if (this.fSightCurReadyness < 1.0F)
			this.fSightCurReadyness += 0.0333333F * f;
		else if (this.bSightAutomation) {
			this.fSightCurDistance -= (this.fSightCurSpeed / 3.6F) * f;
			if (this.fSightCurDistance < 0.0F) {
				this.fSightCurDistance = 0.0F;
				this.typeBomberToggleAutomation();
			}
			this.fSightCurForwardAngle = (float) Math
					.toDegrees(Math.atan(this.fSightCurDistance / this.fSightCurAltitude));
			if (this.fSightCurDistance < this.fSightCurSpeed / 3.6F * Math.sqrt(this.fSightCurAltitude * 0.2038736F))
				this.bSightBombDump = true;
			if (this.bSightBombDump)
				if (this.FM.isTick(3, 0)) {
					if (this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1] != null
							&& this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].haveBullets()) {
						this.FM.CT.WeaponControl[3] = true;
						HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
					}
				} else {
					this.FM.CT.WeaponControl[3] = false;
				}
		}
	}

	public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
		netmsgguaranted.writeByte((this.bSightAutomation ? 1 : 0) | (this.bSightBombDump ? 2 : 0));
		netmsgguaranted.writeFloat(this.fSightCurDistance);
		netmsgguaranted.writeByte((int) this.fSightCurForwardAngle);
		netmsgguaranted.writeByte((int) ((this.fSightCurSideslip + 3F) * 33.33333F));
		netmsgguaranted.writeFloat(this.fSightCurAltitude);
		netmsgguaranted.writeFloat(this.fSightCurSpeed);
		netmsgguaranted.writeByte((int) (this.fSightCurReadyness * 200F));
	}

	public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
		int i = netmsginput.readUnsignedByte();
		this.bSightAutomation = (i & 1) != 0;
		this.bSightBombDump = (i & 2) != 0;
		this.fSightCurDistance = netmsginput.readFloat();
		this.fSightCurForwardAngle = netmsginput.readUnsignedByte();
		this.fSightCurSideslip = -3F + netmsginput.readUnsignedByte() / 33.33333F;
		this.fSightCurAltitude = netmsginput.readFloat();
		this.fSightCurSpeed = netmsginput.readFloat();
		this.fSightCurReadyness = netmsginput.readUnsignedByte() / 200F;
	}

	private Bomb booster[] = { null, null };
	protected boolean bHasBoosters;
	protected long boosterFireOutTime;
	private boolean bSightAutomation;
	private boolean bSightBombDump;
	private float fSightCurDistance;
	public float fSightCurForwardAngle;
	public float fSightCurSideslip;
	public float fSightCurAltitude;
	public float fSightCurSpeed;
	public float fSightCurReadyness;

	static {
		Class class1 = AR_234B2.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Ar 234");
		Property.set(class1, "meshName", "3DO/Plane/Ar-234B-2/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
		Property.set(class1, "yearService", 1944F);
		Property.set(class1, "yearExpired", 1948.8F);
		Property.set(class1, "FlightModel", "FlightModels/Ar-234B-2.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitAR_234B2.class, CockpitAR_234B2_Bombardier.class });
		Property.set(class1, "LOSElevation", 1.14075F);
		weaponTriggersRegister(class1, new int[] { 0, 0, 3, 3, 3, 9, 9 });
		weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb02",
				"_ExternalBomb03", "_ExternalDev01", "_ExternalDev02" });
	}
}
