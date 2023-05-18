package com.maddox.il2.objects.ships;

import com.maddox.JGP.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.vehicles.radios.*;
import com.maddox.rts.SectFile;

public class CarrierShipILSGeneric extends BigshipGeneric
  implements TypeHasILSBlindLanding
{
	public CarrierShipILSGeneric() {
	}

	public CarrierShipILSGeneric(String paramString1, int paramInt, SectFile paramSectFile1, String paramString2, SectFile paramSectFile2, String paramString3)
	{
		super(paramString1, paramInt, paramSectFile1, paramString2, paramSectFile2, paramString3);

		p1 = new Point3d();
		p2 = new Point3d();
		tempPoint1 = new Point3d();
		tempPoint2 = new Point3d();
		emittingPoint = new Point3d();
		outerMarkerDist = 3000F;
		innerMarkerDist = 300F;
		inner2MarkerDist = 185F;
	}

	public void rideBeam(Aircraft aircraft, BlindLandingData blindlandingdata) {
		HierMesh carrierhiermesh = this.hierMesh();
		Loc ldghookloc = new Loc();
		Loc tmpLoc = new Loc();
		if (!findHook(carrierhiermesh, "_RWY_LDG", ldghookloc)) {
			System.out.println("WARNING: " + this +" doesn't have the Hook _RWY_LDG even ILS carrier.");
			return;
		}
		tmpLoc.set(ldghookloc);
		tmpLoc.add(this.initLoc);
		aircraft.pos.getAbs(p2);
		p1 = tmpLoc.getPoint();
		p1.sub(p2);
		double d = Math.sqrt(p1.x * p1.x + p1.y * p1.y);
		double d1 = (double) aircraft.FM.getAltitude() - tmpLoc.getZ() - 2D;
		float f = getConeOfSilence(d, d1);
		if (d > 35000D) {
			blindlandingdata.addSignal(0.0F, 0.0F, 35000F, false, 0.0F, 0.0F, 0.0F, 0.0F, false);
			return;
		}
		float f1 = f;
		if (Landscape.rayHitHQ(emittingPoint, aircraft.FM.Loc, tempPoint1)) {
			Landscape.rayHitHQ(aircraft.FM.Loc, emittingPoint, tempPoint2);
			tempPoint1.sub(tempPoint2);
			double d2 = Math.sqrt(tempPoint1.x * tempPoint1.x + tempPoint1.y * tempPoint1.y);
			f1 = cvt((float) d2, 0.0F, 5000F, 1.0F, 0.1F);
		}
		f1 *= cvt((float) d, 0.0F, 35000F, 1.0F, 0.0F);
		float f2 = 57.32484F * (float) Math.atan2(p1.x, p1.y);
		f2 -= tmpLoc.getAzimut();
		for (f2 = (f2 + 180F) % 360F; f2 < 0.0F; f2 += 360F)
			;
		for (; f2 >= 360F; f2 -= 360F)
			;
		float f3 = f2 - 90F;
		boolean flag = true;
		boolean flag1 = false;
		if (f3 > 90F) {
			f3 = -(f3 - 180F);
			flag = false;
		}
		float f4 = 0.0F;
		if (!flag) f4 = f3 * -18F;
		else f4 = f3 * 18F;
		blindlandingdata.addSignal(f3, f4 * f, (float) d, flag, f1, outerMarkerDist, innerMarkerDist, inner2MarkerDist, flag1);
	}

	private float getConeOfSilence(double d, double d1) {
		float f = 57.32484F * (float) Math.atan2(d, d1);
		return cvt(f, 20F, 40F, 0.0F, 1.0F);
	}

	public void missionStarting() {
		emittingPoint.x = pos.getAbsPoint().x;
		emittingPoint.y = pos.getAbsPoint().y;
		emittingPoint.z = pos.getAbsPoint().z + 20D;
	}

	public void showGuideArrows() {
	}

	public void align() {
		super.align();
		if(emittingPoint == null) emittingPoint = new Point3d();
		emittingPoint.x = pos.getAbsPoint().x;
		emittingPoint.y = pos.getAbsPoint().y;
		emittingPoint.z = pos.getAbsPoint().z + 20D;
	}

	protected static float cvt(float f, float f1, float f2, float f3, float f4) {
		f = Math.min(Math.max(f, f1), f2);
		return f3 + ((f4 - f3) * (f - f1)) / (f2 - f1);
	}

	private boolean findHook(HierMesh hiermesh, String s, Loc loc1)
	{
		Matrix4d m1 = new Matrix4d();
		double tmp[] = new double[3];
		Point3d p = new Point3d();
		Orient o = new Orient();
		int i = hiermesh.hookFind(s);
		if(i == -1)
		{
			return false;
		} else
		{
			hiermesh.hookMatrix(i, m1);
			m1.getEulers(tmp);
			o.setYPR(Geom.RAD2DEG((float)tmp[0]), 360F - Geom.RAD2DEG((float)tmp[1]), 360F - Geom.RAD2DEG((float)tmp[2]));
			p.set(m1.m03, m1.m13, m1.m23);
			loc1.set(p, o);
			return true;
		}
	}

	Point3d p1;
	Point3d p2;
	private Point3d tempPoint1;
	private Point3d tempPoint2;
	private Point3d emittingPoint;
	public float outerMarkerDist;
	public float innerMarkerDist;
	public float inner2MarkerDist;
}
