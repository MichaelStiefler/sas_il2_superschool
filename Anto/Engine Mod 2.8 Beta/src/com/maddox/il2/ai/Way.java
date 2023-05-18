// Source File Name:   Way.java

package com.maddox.il2.ai;

import java.util.ArrayList;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.rts.SectFile;
import com.maddox.util.NumberTokenizer;

// Referenced classes of package com.maddox.il2.ai:
//            WayPoint, World, Airport

public class Way {

	public int Cur() {
		return Cur;
	}

	public Way() {
		WList = new ArrayList();
		prevdist2 = 100000000D;
		prevdistToNextWP2 = 3.4028234663852886E+038D;
		WList.clear();
		Cur = 0;
		landing = false;
		landingOnShip = false;
		landingAirport = null;
		takeoffAirport = null;
	}

	public Way(Way way) {
		WList = new ArrayList();
		prevdist2 = 100000000D;
		prevdistToNextWP2 = 3.4028234663852886E+038D;
		set(way);
	}

	public void set(Way way) {
		WList.clear();
		Cur = 0;
		for (int i = 0; i < way.WList.size(); i++) {
			WayPoint waypoint = new WayPoint();
			waypoint.set(way.get(i));
			WList.add(waypoint);
			if (waypoint.Action == 2 && waypoint.sTarget != null) landingOnShip = true;
		}

		landing = way.landing;
		landingAirport = way.landingAirport;
		if (takeoffAirport == null) takeoffAirport = way.takeoffAirport;
	}

	public WayPoint first() {
		Cur = 0;
		return curr();
	}

	public WayPoint last() {
		Cur = Math.max(0, WList.size() - 1);
		return curr();
	}

	public WayPoint next() {
		int i = WList.size();
		Cur++;
		if (Cur >= i) {
			Cur = Math.max(0, i - 1);
			WayPoint waypoint = curr();
			return waypoint;
		} else {
			return curr();
		}
	}

	public WayPoint look_at_point(int i) {
		int j = WList.size();
		if (j == 0) return defaultWP;
		if (i < 0) i = 0;
		if (i > j - 1) i = j - 1;
		return (WayPoint) WList.get(i);
	}

	public void setCur(int i) {
		if (i >= WList.size() || i < 0) {
			return;
		} else {
			Cur = i;
			return;
		}
	}

	public WayPoint prev() {
		Cur--;
		if (Cur < 0) Cur = 0;
		return curr();
	}

	public WayPoint curr() {
		if (WList.size() == 0) return defaultWP;
		else return (WayPoint) WList.get(Cur);
	}

	public WayPoint auto(Point3d point3d) {
		if (Cur == 0 || isReached(point3d)) return next();
		else return curr();
	}

	public WayPoint autoFastJet(Point3d point3d) {
		if (Cur == 0 || isReachedFastJet(point3d)) return next();
		else return curr();
	}

	public double getCurDist() {
		return Math.sqrt(curDist);
	}

	public boolean isReached(Point3d point3d) {
		if (bAlreadyTrue) {
			bAlreadyTrue = false;
			return true;
		}
		curr().getP(P);
		V.sub(point3d, P);
		if (curr().timeout == -1 && !isLast()) {
			((WayPoint) WList.get(Cur + 1)).getP(tmpP);
			V.sub(point3d, tmpP);
			curDist = V.x * V.x + V.y * V.y;
			if (curDist < 100000000D && curDist > prevdistToNextWP2) {
				curr().setTimeout(0);
				prevdistToNextWP2 = 3.4028234663852886E+038D;
				bAlreadyTrue = true;
				return true;
			}
			prevdistToNextWP2 = curDist;
		} else {
			curDist = V.x * V.x + V.y * V.y;
			if (curDist < 1000000D && curDist > prevdist2) {
				prevdist2 = 100000000D;
				bAlreadyTrue = true;
				return true;
			}
			prevdist2 = curDist;
		}
		return false;
	}

	public boolean isReachedFastJet(Point3d point3d) {
		if (bAlreadyTrue) {
			bAlreadyTrue = false;
			return true;
		}
		curr().getP(P);
		V.sub(point3d, P);
		if (curr().timeout == -1 && !isLast()) {
			((WayPoint) WList.get(Cur + 1)).getP(tmpP);
			V.sub(point3d, tmpP);
			curDist = V.x * V.x + V.y * V.y;
			if (bLogDetail) System.out.println("Way: 190 - curDist=" + Integer.toString((int) Math.floor(curDist)) + " , prevdistToNextWP2=" + Integer.toString((int) Math.floor(prevdistToNextWP2)));
			if (curDist < 100000000D && curDist > prevdistToNextWP2) {
				curr().setTimeout(0);
				prevdistToNextWP2 = 3.4028234663852886E+038D;
				if (bLogDetail) System.out.println("Way: 196 - return true;");
				bAlreadyTrue = true;
				return true;
			}
			prevdistToNextWP2 = curDist;
		} else {
			curDist = V.x * V.x + V.y * V.y;
			if (bLogDetail) System.out.println("Way: 204 - curDist=" + Integer.toString((int) Math.floor(curDist)) + " , prevdist2=" + Integer.toString((int) Math.floor(prevdist2)));
			if (curDist < 3000000D && curDist > prevdist2) {
				prevdist2 = 200000000D;
				if (bLogDetail) System.out.println("Way: 209 - return true;");
				bAlreadyTrue = true;
				return true;
			}
			prevdist2 = curDist;
		}
		return false;
	}

	public boolean isLanding() {
		return landing;
	}

	public boolean isLandingOnShip() {
		return landingOnShip;
	}

	public boolean isLast() {
		return Cur == WList.size() - 1;
	}

	public void setLanding(boolean flag) {
		landing = flag;
	}

	public void setLanding2(boolean flag) {
		landing2 = flag;
	}

	public boolean isLanding2() {
		return landing2;
	}

	public void add(WayPoint waypoint) {
		WList.add(waypoint);
		if (waypoint.Action == 2 && waypoint.sTarget != null) landingOnShip = true;
	}

	public WayPoint get(int i) {
		if (i < 0 || i >= WList.size()) return null;
		else return (WayPoint) WList.get(i);
	}

	public void insert(int i, WayPoint waypoint) {
		if (i < 0) i = 0;
		else if (i > WList.size()) {
			add(waypoint);
			return;
		}
		WList.add(i, waypoint);
	}

	public int size() {
		return WList.size();
	}

	public void load(SectFile sectfile, String s) throws Exception {
		int i = sectfile.sectionIndex(s);
		int j = sectfile.vars(i);
		for (int k = 0; k < j; k++) {
			String s1 = sectfile.var(i, k);
			WayPoint waypoint = new WayPoint();
			s1 = s1.toUpperCase();
			waypoint.resetWayPointExtra();
			if (s1.startsWith("NORMFLY")) {
				waypoint.Action = 0;
				if (s1.endsWith("_401")) waypoint.waypointType = 401;
				else if (s1.endsWith("_402")) waypoint.waypointType = 402;
				else if (s1.endsWith("_403")) waypoint.waypointType = 403;
				else if (s1.endsWith("_404")) waypoint.waypointType = 404;
				else if (s1.endsWith("_405")) waypoint.waypointType = 405;
				else if (s1.endsWith("_407")) waypoint.waypointType = 407;
			} else if (s1.startsWith("TAKEOFF")) {
				waypoint.Action = 1;
				if (s1.endsWith("_001")) waypoint.waypointType = 1;
				else if (s1.endsWith("_002")) waypoint.waypointType = 2;
				else if (s1.endsWith("_003")) waypoint.waypointType = 3;
				else if (s1.endsWith("_004")) waypoint.waypointType = 4;
				else if (s1.endsWith("_005")) waypoint.waypointType = 5;
			} else if (s1.startsWith("LANDING")) {
				waypoint.Action = 2;
				if (s1.endsWith("_101")) waypoint.waypointType = 101;
				else if (s1.endsWith("_102")) waypoint.waypointType = 102;
				else if (s1.endsWith("_103")) waypoint.waypointType = 103;
				else if (s1.endsWith("_104")) waypoint.waypointType = 104;
			} else if (s1.startsWith("GATTACK")) waypoint.Action = 3;
			NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.value(i, k));
			P.x = numbertokenizer.next(0.0F, -1000000F, 1000000F);
			P.y = numbertokenizer.next(0.0F, -1000000F, 1000000F);
			P.z = (double) numbertokenizer.next(0.0F, -1000000F, 1000000F) + World.land().HQ(P.x, P.y);
			float f = numbertokenizer.next(0.0F, 0.0F, 2800F);
			waypoint.set(P);
			waypoint.set(f / 3.6F);
			String s2 = numbertokenizer.next(null);
			if (s2 != null) if (s2.equals("&0")) {
				waypoint.bRadioSilence = false;
				s2 = null;
			} else if (s2.equals("&1")) {
				waypoint.bRadioSilence = true;
				s2 = null;
			} else {
				numbertokenizer.next(0);
				String s3 = numbertokenizer.next(null);
				if (s3 != null && s3.equals("&1")) waypoint.bRadioSilence = true;
			}
			String s4 = numbertokenizer.next(null);
			if (s4 != null && s4.startsWith("F")) {
				String s5 = s4.substring(1);
				waypoint.formation = Integer.parseInt(s5);
			}
			if (s2 != null && s2.startsWith("Bridge")) s2 = " " + s2;
			waypoint.setTarget(s2);
			if (waypoint.waypointType > 0 && waypoint.Action != 2) {
				String s6 = sectfile.value(i, k + 1);
				NumberTokenizer numbertokenizer1 = new NumberTokenizer(s6);
				switch (waypoint.waypointType) {
				case 401:
				case 402:
				case 403:
				case 404:
				case 405:
					waypoint.cycles = numbertokenizer1.next(0);
					waypoint.delayTimer = numbertokenizer1.next(0);
					waypoint.orient = numbertokenizer1.next(0);
					waypoint.baseSize = numbertokenizer1.next(0);
					waypoint.altDifference = numbertokenizer1.next(0);
					break;

				case 407:
					waypoint.cycles = numbertokenizer1.next(0);
					waypoint.delayTimer = numbertokenizer1.next(0);
					waypoint.orient = numbertokenizer1.next(0);
					waypoint.baseSize = numbertokenizer1.next(0);
					break;

				case 1: // '\001'
				case 2: // '\002'
				case 3: // '\003'
				case 4: // '\004'
				case 5: // '\005'
					waypoint.targetTrigger = numbertokenizer1.next(0);
					waypoint.delayTimer = numbertokenizer1.next(0);
					waypoint.takeoffSpacing = numbertokenizer1.next(0);
					waypoint.ignoreAlt = numbertokenizer1.next(0);
					break;
				}
				k++;
			}
			add(waypoint);
		}

	}

	private ArrayList WList;
	private int Cur;
	private boolean landing;
	private boolean landingOnShip;
	public Airport landingAirport;
	public Airport takeoffAirport;
	private double prevdist2;
	private double prevdistToNextWP2;
	private double curDist;
	private static Vector3d V = new Vector3d();
	private static Point3d P = new Point3d();
	private static Point3d tmpP = new Point3d();
	private static WayPoint defaultWP = new WayPoint();
	private boolean landing2;
	private boolean bAlreadyTrue = false;

	private boolean bLogDetail = false;
}
