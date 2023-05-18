/*4.10.1 class*/
package com.maddox.il2.builder;

import java.lang.reflect.Method;
import com.maddox.JGP.Geom;
import com.maddox.JGP.Matrix4d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.ActorSimpleHMesh;
import com.maddox.il2.objects.ActorSimpleMesh;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;

public class PathChief extends Path
{
	public ActorMesh[] units;
	public int _iType;
	public int _iItem;
	public int _sleep = 0;
	public int _skill = 2;
	public float _slowfire = 1.0F;
	private Point3d prevStart = new Point3d();
	private Point3d prevEnd = new Point3d();
	private Point3d p2 = new Point3d();

	public void computeTimes()
	{
		int i = points();
		if (i != 0)
		{
			PNodes pnodes = (PNodes) point(0);
			pnodes.time = 0.0;
			if (PlMisChief.moveType(_iType) != 2)
			{
				double d = PlMisChief.speed(_iType, _iItem);
				for (int i_0_ = 1; i_0_ < i; i_0_++)
				{
					double d_1_ = pnodes.len();
					double d_2_ = d_1_ / d;
					PNodes pnodes_3_ = (PNodes) point(i_0_);
					pnodes_3_.time = pnodes.time + pnodes.timeoutMin * 60.0 + d_2_;
					pnodes = pnodes_3_;
				}
			}
			else
			{
				for (int i_4_ = 1; i_4_ < i; i_4_++)
				{
					PNodes pnodes_5_ = (PNodes) point(i_4_);
					double d = pnodes.computeTime(pnodes_5_);
					pnodes_5_.time = pnodes.time + pnodes.timeoutMin * 60.0 + d;
					pnodes = pnodes_5_;
				}
			}
			Plugin.builder.doUpdateSelector();
		}
	}

	public void computeTimesLoaded()
	{
		int i = points();
		if (i != 0)
		{
			PNodes pnodes = (PNodes) point(0);
			pnodes.time = 0.0;
			if (PlMisChief.moveType(_iType) != 2)
			{
				double d = PlMisChief.speed(_iType, _iItem);
				for (int i_6_ = 1; i_6_ < i; i_6_++)
				{
					double d_7_ = pnodes.len();
					double d_8_ = d_7_ / d;
					PNodes pnodes_9_ = (PNodes) point(i_6_);
					pnodes_9_.time = pnodes.time + pnodes.timeoutMin * 60.0 + d_8_;
					if (pnodes_9_.timeoutMin > 0.0)
						pnodes_9_.timeoutMin = (double) Math.round((pnodes_9_.timeoutMin * 60.0 - pnodes_9_.time) / 60.0);
					if (pnodes_9_.timeoutMin < 0.0)
						pnodes_9_.timeoutMin = 0.0;
					pnodes = pnodes_9_;
				}
			}
			else
			{
				for (int i_10_ = 1; i_10_ < i; i_10_++)
				{
					PNodes pnodes_11_ = (PNodes) point(i_10_);
					double d = pnodes.computeTime(pnodes_11_);
					pnodes_11_.time = pnodes.time + pnodes.timeoutMin * 60.0 + d;
					if (pnodes_11_.timeoutMin > 0.0)
						pnodes_11_.timeoutMin = (double) Math.round((pnodes_11_.timeoutMin * 60.0 - pnodes_11_.time) / 60.0);
					if (pnodes_11_.timeoutMin < 0.0)
						pnodes_11_.timeoutMin = 0.0;
					pnodes = pnodes_11_;
				}
			}
		}
	}

	public void placeUnits(Point3d point3d, Point3d point3d_12_)
	{
		if (point3d_12_ == null || point3d.distance(point3d_12_) < 0.1)
		{
			point3d_12_ = new Point3d(point3d);
			point3d_12_.y += 100.0;
		}
		if (!point3d.equals(prevStart) || !point3d_12_.equals(prevEnd))
		{
			prevStart.set(point3d);
			prevEnd.set(point3d_12_);
			float f = 0.0F;
			float f_13_ = 0.0F;
			for (int i = 0; i < units.length; i++)
			{
				float f_14_ = units[i].mesh().collisionR();
				float f_15_ = f_14_;
				if (f_14_ > f)
					f = f_14_;
				if (f_15_ > f_13_)
					f_13_ = f_15_;
			}
			if (PlMisChief.moveType(_iType) != 3)
			{
				f *= 2.0F;
				f_13_ *= 2.0F;
			}
			if (f <= 0.0010F)
				f = 0.0010F;
			if (f_13_ <= 0.0010F)
				f_13_ = 0.0010F;
			int i = 1;
			int i_16_ = units.length;
			float f_17_ = ((f_13_ * (float) i + f_13_ * 0.5F * (float) (i - 1)) / (f * (float) i_16_ + f * 0.5F * (float) (i_16_ - 1)));
			if (PlMisChief.moveType(_iType) != 3)
			{
				for (int i_18_ = 2; i_18_ <= units.length; i_18_++)
				{
					int i_19_ = (units.length + i_18_ - 1) / i_18_;
					float f_20_ = ((f_13_ * (float) i_18_ + f_13_ * 0.5F * (float) (i_18_ - 1)) / (f * (float) i_19_ + f * 0.5F * (float) (i_19_ - 1)));
					if (Math.abs(f_20_ - 0.5F) < Math.abs(f_17_ - 0.5F))
					{
						f_17_ = f_20_;
						i = i_18_;
						i_16_ = i_19_;
					}
				}
			}
			double d = point3d_12_.x - point3d.x;
			double d_21_ = point3d_12_.y - point3d.y;
			double d_22_ = Math.atan2(d_21_, d);
			double d_23_ = Math.sin(-d_22_);
			double d_24_ = Math.cos(-d_22_);
			d_22_ = (double) Geom.RAD2DEG((float) d_22_);
			float f_25_ = (f_13_ * (float) i + f_13_ * 0.5F * (float) (i - 1)) * 0.5F;
			float f_26_ = (f * (float) i_16_ + f * 0.5F * (float) (i_16_ - 1)) * 0.5F;
			int i_27_ = 0;
			for (int i_28_ = 0; i_28_ < i_16_; i_28_++)
			{
				int i_29_ = 0;
				while (i_29_ < i)
				{
					if (i_27_ >= units.length)
						break;
					double d_30_ = (double) (-f_26_ + f * 0.5F + (float) i_28_ * 1.5F * f);
					double d_31_ = (double) (f_25_ - f_13_ * 0.5F - (float) i_29_ * 1.5F * f_13_);
					double d_32_ = d_24_ * d_30_ + d_23_ * d_31_;
					double d_33_ = -d_23_ * d_30_ + d_24_ * d_31_;
					double d_34_ = 0.0;
					int i_35_ = units[units.length - 1 - i_27_].mesh().hookFind("Ground_Level");
					if (i_35_ != -1)
					{
						Matrix4d matrix4d = new Matrix4d();
						units[units.length - 1 - i_27_].mesh().hookMatrix(i_35_, matrix4d);
						d_34_ = -matrix4d.m23;
					}
					Point3d point3d_36_ = new Point3d(point3d);
					point3d_36_.x += d_32_;
					point3d_36_.y += d_33_;
					point3d_36_.z = Engine.land().HQ(point3d_36_.x, point3d_36_.y) + d_34_;
					Orient orient = new Orient();
					orient.setYPR((float) d_22_, 0.0F, 0.0F);
					Vector3f vector3f = new Vector3f();
					Engine.land().N(point3d_36_.x, point3d_36_.y, vector3f);
					orient.orient(vector3f);
					units[units.length - 1 - i_27_].pos.setAbs(point3d_36_, orient);
					units[units.length - 1 - i_27_].pos.reset();
					i_29_++;
					i_27_++;
				}
			}
		}
	}

	public void drawing(boolean bool)
	{
		super.drawing(bool);
		if (units != null)
		{
			for (int i = 0; i < units.length; i++)
			{
				if (Actor.isValid(units[i]))
					units[i].drawing(bool);
			}
		}
	}

	public void clear()
	{
		if (units != null)
		{
			for (int i = 0; i < units.length; i++)
			{
				if (Actor.isValid(units[i]))
				{
					units[i].destroy();
					units[i] = null;
				}
			}
			units = null;
		}
		prevStart.set(0.0, 0.0, 0.0);
		prevEnd.set(0.0, 0.0, 0.0);
	}

	public void destroy()
	{
		if (!isDestroyed())
		{
			clear();
			super.destroy();
		}
	}

	public void setUnits(int i, int i_37_, SectFile sectfile, int i_38_, Point3d point3d)
	{
		clear();
		try
		{
			int i_39_ = sectfile.vars(i_38_);
			
			//TODO: Added by |ZUTI|
			//----------------------------------------
			zutiUnitsNames = new String[i_39_];
			//----------------------------------------
			
			units = new ActorMesh[i_39_];
			for (int i_40_ = 0; i_40_ < i_39_; i_40_++)
			{
				String string = sectfile.var(i_38_, i_40_);
				
				//TODO: Added by |ZUTI|
				//--------------------------------------------------------
				if( string.indexOf("$") > 0 )
					zutiUnitsNames[i_40_] = string.substring(string.indexOf("$")+1, string.length());
				else
					zutiUnitsNames[i_40_] = null;
				//--------------------------------------------------------
				
				Class var_class = Class.forName(string);
				String string_41_ = Property.stringValue(var_class, "meshName", null);
				
				
				if (string_41_ == null)
				{
					Method method = var_class.getMethod("getMeshNameForEditor", null);
					string_41_ = (String) method.invoke(var_class, null);
				}
				if (string_41_.toLowerCase().endsWith(".sim"))
					units[i_40_] = new ActorSimpleMesh(string_41_);
				else
					units[i_40_] = new ActorSimpleHMesh(string_41_);
			}
			_iType = i;
			_iItem = i_37_;
			placeUnits(point3d, null);
		}
		catch (Exception exception)
		{
			destroy();
			System.out.println(exception.getMessage());
			exception.printStackTrace();
			throw new RuntimeException(exception.toString());
		}
	}

	public void updateUnitsPos()
	{
		if (units != null && points() != 0 && Actor.isValid(point(0)))
		{
			PNodes pnodes = (PNodes) point(0);
			if (pnodes.posXYZ != null)
			{
				p2.set((double) pnodes.posXYZ[4], (double) pnodes.posXYZ[5], (double) pnodes.posXYZ[6]);
				placeUnits(point(0).pos.getAbsPoint(), p2);
			}
			else
				placeUnits(point(0).pos.getAbsPoint(), null);
		}
	}

	public void pointMoved(PPoint ppoint)
	{
		computeTimes();
		int i = pointIndx(ppoint);
		if (i == 0 || i == 1)
			updateUnitsPos();
	}

	public PathChief(Pathes pathes, int i, int i_42_, int i_43_, SectFile sectfile, int i_44_, Point3d point3d)
	{
		super(pathes);
		moveType = i;
		setUnits(i_42_, i_43_, sectfile, i_44_, point3d);
	}
	
	//TODO: Added by |ZUTI|
	//------------------------------------------------
	public String[] zutiUnitsNames;
	//------------------------------------------------
}
