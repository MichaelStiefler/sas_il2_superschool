/*Here only because of obfuscation problems*/
package com.maddox.il2.fm;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeFighter;
import com.maddox.il2.objects.air.TypeStormovik;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.rts.Time;

public class AIFlightModel extends FlightModel
{
	private long		w;
	public float		Density;
	public float		Kq;
	protected boolean	callSuperUpdate	= true;
	protected boolean	dataDrawn		= false;
	Vector3d			Cw				= new Vector3d();
	Vector3d			Fw				= new Vector3d();
	protected Vector3d	Wtrue			= new Vector3d();
	private Point3d		TmpP			= new Point3d();
	private Vector3d	Vn				= new Vector3d();
	private Vector3d	TmpV			= new Vector3d();
//	private Vector3d	TmpVd			= new Vector3d();
//	private Loc			L				= new Loc();

	public AIFlightModel(String string)
	{
		super(string);
		w = Time.current();
	}

	private void flutter()
	{
		((Aircraft) actor).msgCollision(actor, "CF_D0", "CF_D0");
	}

	public Vector3d getW()
	{
		return Wtrue;
	}

	public void update(float f)
	{
		CT.update(f, (getSpeed() + 50.0F) * 0.5F, EI, false);
		Wing.setFlaps(CT.getFlap());
		EI.update(f);
		super.update(f);
		Gravity = M.getFullMass() * Atmosphere.g();
		M.computeFullJ(J, J0);
		if (isTick(44, 0))
		{
			AS.update(f * 44.0F);
			((Aircraft) actor).rareAction(f * 44.0F, true);
			M.computeParasiteMass(CT.Weapons);
			Sq.computeParasiteDrag(CT, CT.Weapons);
			if (((Pilot) ((Aircraft) actor).FM).get_task() == 7 || ((Maneuver) ((Aircraft) actor).FM).get_maneuver() == 43)
				putScareShpere();
			if (World.Rnd().nextInt(0, 99) < 25)
			{
				Aircraft aircraft = (Aircraft) actor;
				if (aircraft.aircIndex() == 0 && !(aircraft instanceof TypeFighter))
				{
					int i = actor.getArmy() - 1 & 0x1;
					int i_0_ = (int) (Time.current() / 60000L);
					if (i_0_ > Voice.cur().SpeakBombersUnderAttack[i] || i_0_ > Voice.cur().SpeakBombersUnderAttack1[i] || i_0_ > Voice.cur().SpeakBombersUnderAttack2[i])
					{
						Aircraft aircraft_1_ = War.getNearestEnemy(aircraft, 6000.0F);
						if (aircraft_1_ != null && (aircraft_1_ instanceof TypeFighter || aircraft_1_ instanceof TypeStormovik))
							Voice.speakBombersUnderAttack(aircraft, false);
					}
					if (i_0_ > Voice.cur().SpeakNearBombers[i])
					{
						Aircraft aircraft_2_ = War.getNearestFriendlyFighter(aircraft, 4000.0F);
						if (aircraft_2_ != null)
							Voice.speakNearBombers(aircraft);
					}
				}
			}
		}
		if (World.cur().diffCur.Wind_N_Turbulence && !Gears.onGround() && Time.current() > w + 50L)
			World.wind().getVectorAI(Loc, Vwind);
		else
			Vwind.set(0.0, 0.0, 0.0);
		Vair.sub(Vwld, Vwind);
		Or.transformInv(Vair, Vflow);
		Density = Atmosphere.density((float) Loc.z);
		AOA = RAD2DEG(-(float) Math.atan2(Vflow.z, Vflow.x));
		AOS = RAD2DEG((float) Math.atan2(Vflow.y, Vflow.x));
		V2 = (float) Vflow.lengthSquared();
		V = (float) Math.sqrt((double) V2);
		Mach = V / Atmosphere.sonicSpeed((float) Loc.z);
		if (Mach > 0.8F)
			Mach = 0.8F;
		Kq = 1.0F / (float) Math.sqrt((double) (1.0F - Mach * Mach));
		q_ = Density * V2 * 0.5F;
		if (callSuperUpdate)
		{
			double d = Loc.z - Gears.screenHQ;
			if (d < 0.0)
				d = 0.0;
			Cw.x = (double) (-q_ * (Wing.new_Cx(AOA) + 2.0F * GearCX * CT.getGear() + 2.0F * Sq.dragAirbrakeCx * CT.getAirBrake()));
			Cw.z = (double) (q_ * Wing.new_Cy(AOA) * Kq);
			if (fmsfxCurrentType != 0 && fmsfxCurrentType == 1)
				Cw.z *= (double) Aircraft.cvt(fmsfxPrevValue, 0.0030F, 0.8F, 1.0F, 0.0F);
			if (d < 0.4 * (double) Length)
			{
				double d_3_ = 1.0 - d / (0.4 * (double) Length);
				double d_4_ = 1.0 + 0.3 * d_3_;
				double d_5_ = 1.0 + 0.3 * d_3_;
				Cw.z *= d_4_;
				Cw.x *= d_5_;
			}
			Cw.y = (double) (-q_ * Wing.new_Cz(AOS));
			for (int i = 0; i < EI.getNum(); i++)
				Cw.x += (double) (-q_ * (0.8F * Sq.dragEngineCx[i]));
			Fw.scale((double) (Sq.liftWingLIn + Sq.liftWingLMid + Sq.liftWingLOut + Sq.liftWingRIn + Sq.liftWingRMid + Sq.liftWingROut), Cw);
			Fw.x -= (double) (q_ * (Sq.dragParasiteCx + Sq.dragProducedCx));
			AF.set(Fw);
			AF.y -= (double) (AOS * q_ * 0.1F);
			if (isNAN(AF))
			{
				AF.set(0.0, 0.0, 0.0);
				flutter();
			}
			else if (AF.length() > (double) (Gravity * 50.0F))
			{
				AF.normalize();
				AF.scale((double) (Gravity * 50.0F));
				flutter();
			}
			AM.set(0.0, 0.0, 0.0);
			Wtrue.set(0.0, 0.0, 0.0);
			AM.x = (double) (Sq.liftWingLIn * Arms.WING_ROOT + Sq.liftWingLMid * Arms.WING_MIDDLE + Sq.liftWingLOut * Arms.WING_END - Sq.liftWingRIn * Arms.WING_ROOT - Sq.liftWingRMid
					* Arms.WING_MIDDLE - Sq.liftWingROut * Arms.WING_END)
					* Cw.z;
			if (fmsfxCurrentType != 0)
			{
				if (fmsfxCurrentType == 2)
				{
					AM.x = (double) (-Sq.liftWingRIn * Arms.WING_ROOT - Sq.liftWingRMid * Arms.WING_MIDDLE - Sq.liftWingROut * Arms.WING_END) * Cw.z;
					if (Time.current() >= fmsfxTimeDisable)
						doRequestFMSFX(0, 0);
				}
				if (fmsfxCurrentType == 3)
				{
					AM.x = (double) (Sq.liftWingLIn * Arms.WING_ROOT + Sq.liftWingLMid * Arms.WING_MIDDLE + Sq.liftWingLOut * Arms.WING_END) * Cw.z;
					if (Time.current() >= fmsfxTimeDisable)
						doRequestFMSFX(0, 0);
				}
			}
			if (Math.abs(AOA) > 33.0F)
				AM.y = (double) (1.0F * (Sq.liftStab * Arms.HOR_STAB) * (q_ * Tail.new_Cy(AOA) * Kq));
			if (Math.abs(AOS) > 33.0F)
				AM.z = (double) (1.0F * ((0.2F + Sq.liftKeel) * Arms.VER_STAB) * (q_ * Tail.new_Cy(AOS) * Kq));
			float f_6_ = Sq.liftWingLIn + Sq.liftWingLMid + Sq.liftWingLOut;
			float f_7_ = Sq.liftWingRIn + Sq.liftWingRMid + Sq.liftWingROut;
			float f_8_ = (float) Vflow.lengthSquared() - 120.0F;
			if (f_8_ < 0.0F)
				f_8_ = 0.0F;
			if (Vflow.x < 0.0)
				f_8_ = 0.0F;
			if (f_8_ > 15000.0F)
				f_8_ = 15000.0F;
			if (((Maneuver) ((Aircraft) actor).FM).get_maneuver() != 20)
			{
				float f_9_ = f_6_ - f_7_;
				if (!getOp(19) && d > 20.0 && f_8_ > 10.0F)
				{
					AM.y += (double) (5.0F * Sq.squareWing) * Vflow.x;
					AM.z += (double) (80.0F * Sq.squareWing * EI.getPropDirSign());
					if (AOA > 20.0F || AOA < -20.0F)
					{
						float f_10_ = 1.0F;
						if (W.z < 0.0)
							f_10_ = -1.0F;
						AM.z += ((double) (30.0F * f_10_ * Sq.squareWing) * (3.0 * Vflow.z + Vflow.x));
						AM.x -= ((double) (50.0F * f_10_ * Sq.squareWing) * (Vflow.z + 3.0 * Vflow.x));
					}
				}
				else
				{
					if (!Gears.onGround())
					{
						float f_11_ = AOA * 3.0F;
						if (f_11_ > 25.0F)
							f_11_ = 25.0F;
						if (f_11_ < -25.0F)
							f_11_ = -25.0F;
						if (!getOp(34))
							AM.x -= (double) (f_11_ * f_7_ * f_8_);
						else if (!getOp(37))
							AM.x += (double) (f_11_ * f_6_ * f_8_);
						else if (((Maneuver) ((Aircraft) actor).FM).get_maneuver() == 44 && (AOA > 15.0F || AOA < -12.0F))
						{
							if (f_9_ > 0.0F && W.z > 0.0)
								W.z = -9.999999747378752E-5;
							if (f_9_ < 0.0F && W.z < 0.0)
								W.z = 9.999999747378752E-5;
							if (f_8_ > 1000.0F)
								f_8_ = 1000.0F;
							if (W.z < 0.0)
							{
								AM.z -= (double) (3.0F * Sq.squareWing * f_8_);
								if (AOA > 0.0F)
									AM.x += (double) (40.0F * Sq.squareWing * f_8_);
								else
									AM.x -= (double) (40.0F * Sq.squareWing * f_8_);
							}
							else
							{
								AM.z += (double) (3.0F * Sq.squareWing * f_8_);
								if (AOA > 0.0F)
									AM.x -= (double) (40.0F * Sq.squareWing * f_8_);
								else
									AM.x += (double) (40.0F * Sq.squareWing * f_8_);
							}
						}
					}
					if (Sq.liftKeel > 0.1F)
						AM.z += (double) (AOS * q_ * 0.5F);
					else
						AM.x += (double) (AOS * q_ * 1.0F);
					double d_12_ = 1.0;
					if (d < 1.5 * (double) Length)
					{
						d_12_ += (d - 1.5 * (double) Length) / (double) Length;
						if (d_12_ < 0.0)
							d_12_ = 0.0;
					}
					if (Vflow.x < 20.0 && Math.abs(AOS) < 33.0F)
						AM.y += d_12_ * AF.z;
					float f_13_ = (float) Vflow.x;
					if (f_13_ > 150.0F)
						f_13_ = 150.0F;
					float f_14_ = SensYaw;
					if (f_14_ > 0.2F)
						f_14_ = 0.2F;
					float f_15_ = SensRoll;
					if (f_15_ > 4.0F)
						f_15_ = 4.0F;
					double d_16_ = 20.0 - (double) Math.abs(AOA);
					if (d_16_ < (double) minElevCoeff)
						d_16_ = (double) minElevCoeff;
					double d_17_ = (double) AOA - (double) CT.getElevator() * d_16_;
					double d_18_ = 0.017 * (double) Math.abs(AOA);
					if (d_18_ < 1.0)
						d_18_ = 1.0;
					if (d_17_ > 90.0)
						d_17_ = -(180.0 - d_17_);
					if (d_17_ < -90.0)
						d_17_ = 180.0 - d_17_;
					d_17_ *= d_18_;
					double d_19_ = 12.0 - (double) Math.abs(AOS);
					if (d_19_ < 0.0)
						d_19_ = 0.0;
					double d_20_ = (double) AOS - (double) CT.getRudder() * d_19_;
					double d_21_ = 0.01 * (double) Math.abs(AOS);
					if (d_21_ < 1.0)
						d_21_ = 1.0;
					if (d_20_ > 90.0)
						d_20_ = -(180.0 - d_20_);
					if (d_20_ < -90.0)
						d_20_ = 180.0 - d_20_;
					d_20_ *= d_21_;
					float f_22_ = Sq.squareWing;
					if (f_22_ < 1.0F)
						f_22_ = 1.0F;
					f_22_ = 1.0F / f_22_;
					Wtrue.x = (double) (CT.getAileron() * f_13_ * f_15_ * Sq.squareAilerons * f_22_ * 1.0F);
					Wtrue.y = (d_17_ * (double) f_13_ * (double) SensPitch * (double) Sq.squareElevators * (double) f_22_ * 0.012000000104308128);
					Wtrue.z = (d_20_ * (double) f_13_ * (double) f_14_ * (double) Sq.squareRudders * (double) f_22_ * 0.15000000596046448);
				}
			}
			else
			{
				float f_23_ = 1.0F;
				if (W.z < 0.0)
					f_23_ = -1.0F;
				AM.z += ((double) (30.0F * f_23_ * Sq.squareWing) * (3.0 * Vflow.z + Vflow.x));
				AM.x -= ((double) (50.0F * f_23_ * Sq.squareWing) * (Vflow.z + 3.0 * Vflow.x));
			}
			if (Sq.squareElevators < 0.1F && d > 20.0 && f_8_ > 10.0F)
				AM.y += (double) (Gravity * 0.4F);
			AM.add(producedAM);
			AF.add(producedAF);
			producedAM.set(0.0, 0.0, 0.0);
			producedAF.set(0.0, 0.0, 0.0);
			AF.add(EI.producedF);
			if (W.lengthSquared() > 36.0)
				W.scale(6.0 / W.length());
			double d_24_ = 0.1 + (double) Sq.squareWing;
			if (d_24_ < 1.0)
				d_24_ = 1.0;
			d_24_ = 1.0 / d_24_;
			W.x *= 1.0 - 0.12 * (0.2 + (double) f_6_ + (double) f_7_) * d_24_;
			W.y *= 1.0 - 0.5 * (0.2 + (double) Sq.liftStab) * d_24_;
			W.z *= 1.0 - 0.5 * (0.2 + (double) Sq.liftKeel) * d_24_;
			GF.set(0.0, 0.0, 0.0);
			GM.set(0.0, 0.0, 0.0);
			Gears.roughness = 0.5;
			Gears.ground(this, true);
			GM.x *= 0.1;
			GM.y *= 0.4;
			GM.z *= 0.8;
			int i = 2;
			if (GF.lengthSquared() == 0.0 && GM.lengthSquared() == 0.0 || brakeShoe)
				i = 1;
			SummF.add(AF, GF);
			ACmeter.set(SummF);
			ACmeter.scale((double) (1.0F / Gravity));
			TmpV.set(0.0, 0.0, (double) -Gravity);
			Or.transformInv(TmpV);
			GF.add(TmpV);
			SummF.add(AF, GF);
			SummM.add(AM, GM);
			double d_25_ = 1.0 / (double) M.mass;
			LocalAccel.scale(d_25_, SummF);
			if (isNAN(AM))
			{
				AM.set(0.0, 0.0, 0.0);
				flutter();
			}
			else if (AM.length() > (double) (Gravity * 100.0F))
			{
				AM.normalize();
				AM.scale((double) (Gravity * 100.0F));
				flutter();
			}
			dryFriction -= 0.01;
			if (Gears.gearsChanged)
				dryFriction = 1.0F;
			if (Gears.nOfPoiOnGr > 0)
				dryFriction += 0.02F;
			if (dryFriction < 1.0F)
				dryFriction = 1.0F;
			if (dryFriction > 32.0F)
				dryFriction = 32.0F;
			float f_26_ = 4.0F * (0.25F - EI.getPowerOutput());
			if (f_26_ < 0.0F)
				f_26_ = 0.0F;
			f_26_ *= f_26_;
			f_26_ *= dryFriction;
			float f_27_ = f_26_ * M.mass * M.mass;
			if (!brakeShoe
					&& (Gears.nOfPoiOnGr == 0 && Gears.nOfGearsOnGr < 3 || f_26_ == 0.0F || SummM.lengthSquared() > (double) (2.0F * f_27_) || SummF.lengthSquared() > (double) (80.0F * f_27_)
							|| W.lengthSquared() > (double) (1.4E-4F * f_26_) || Vwld.lengthSquared() > (double) (0.09F * f_26_)))
			{
				double d_28_ = 1.0 / (double) i;
				for (int i_29_ = 0; i_29_ < i; i_29_++)
				{
					SummF.add(AF, GF);
					SummM.add(AM, GM);
					AW.x = ((J.y - J.z) * W.y * W.z + SummM.x) / J.x;
					AW.y = ((J.z - J.x) * W.z * W.x + SummM.y) / J.y;
					AW.z = ((J.x - J.y) * W.x * W.y + SummM.z) / J.z;
					TmpV.scale(d_28_ * (double) f, AW);
					W.add(TmpV);
					Or.transform(W, Vn);
					Wtrue.add(W);
					TmpV.scale(d_28_ * (double) f, Wtrue);
					Or.increment((float) -RAD2DEG(TmpV.z), (float) -RAD2DEG(TmpV.y), (float) RAD2DEG(TmpV.x));
					Or.transformInv(Vn, W);
					TmpV.scale(d_25_, SummF);
					Or.transform(TmpV);
					Accel.set(TmpV);
					TmpV.scale(d_28_ * (double) f);
					Vwld.add(TmpV);
					TmpV.scale(d_28_ * (double) f, Vwld);
					TmpP.set(TmpV);
					Loc.add(TmpP);
					if (isNAN(Loc))
					{
//						boolean bool = false;
					}
					GF.set(0.0, 0.0, 0.0);
					GM.set(0.0, 0.0, 0.0);
					if (i_29_ < i - 1)
					{
						Gears.ground(this, true);
						GM.x *= 0.1;
						GM.y *= 0.4;
						GM.z *= 0.8;
						TmpV.set(0.0, 0.0, (double) -Gravity);
						Or.transformInv(TmpV);
						GF.add(TmpV);
					}
				}
				for (int i_30_ = 0; i_30_ < 3; i_30_++)
				{
					Gears.gWheelAngles[i_30_] = ((Gears.gWheelAngles[i_30_] + (float) Math.toDegrees(Math.atan((Gears.gVelocity[i_30_]) * (double) f / 0.375))) % 360.0F);
					Gears.gVelocity[i_30_] *= 0.949999988079071;
				}
				HM.chunkSetAngles("GearL1_D0", 0.0F, -Gears.gWheelAngles[0], 0.0F);
				HM.chunkSetAngles("GearR1_D0", 0.0F, -Gears.gWheelAngles[1], 0.0F);
				HM.chunkSetAngles("GearC1_D0", 0.0F, -Gears.gWheelAngles[2], 0.0F);
			}
		}
	}
}
