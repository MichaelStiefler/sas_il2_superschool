/*4.10.1 class*/
package com.maddox.il2.ai;

import java.util.ArrayList;
import java.util.List;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.CameraOrtho2D;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Render;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.Chat;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.Paratrooper;
import com.maddox.rts.NetEnv;
import com.maddox.rts.SectFile;
import com.maddox.util.NumberTokenizer;

public class Front
{
	private ArrayList allMarkers = new ArrayList();
	public static final int M = 16;
	private ArrayList markers = new ArrayList();
	private int tNx;
	private int tNy;
	private double camWorldXOffset;
	private double camWorldYOffset;
	private float camLeft;
	private float camBottom;
	private byte[] mask;
	private byte[] mask2;
	private boolean bExistFront = false;
	private Mat[] frontMat = new Mat[4];
	private boolean tilesChanged = false;
	private int prevNCountMarkers = 0;
	private float prevCamLeft = 0.0F;
	private float prevCamBottom = 0.0F;
	private double prevCamWorldScale = 1.0;
	private double prevCamWorldXOffset = 0.0;
	private double prevCamWorldYOffset = 0.0;
	private boolean bTilesUpdated = false;
	private Main3D main;
	private double tStep;
	private Point3d pointLand0 = new Point3d();
	private Point3d pointLand1 = new Point3d();

	//TODO: |ZUTI| variables
	//-------------------------------------------------
	private boolean zutiFrontLineAlreadyLoaded = false;
	//-------------------------------------------------
	
	public static class Marker
	{
		public double x;
		public double y;
		public int army;
		public int _armyMask;
		public double _d2;
		private double _x = 0.0;
		private double _y = 0.0;
		
		//TODO: Added by |ZUTI|: marker carrier name, if any
		//--------------------------------------------------
		public String zutiMarkerCarrierName = "";
		//--------------------------------------------------
	}

	public static List markers()
	{
		return World.cur().front.allMarkers;
	}

	public static void checkAllCaptured()
	{
		List list = Engine.targets();
		int i = list.size();
		for (int i_0_ = 0; i_0_ < i; i_0_++)
		{
			Actor actor = (Actor) list.get(i_0_);
			if (actor instanceof Paratrooper)
			{
				Paratrooper paratrooper = (Paratrooper) actor;
				if (!paratrooper.isChecksCaptured()) paratrooper.checkCaptured();
			}
			else if (actor instanceof Aircraft && !actor.isNetMirror())
			{
				Aircraft aircraft = (Aircraft) actor;
				checkAircraftCaptured(aircraft);
			}
		}
	}

	public static void checkAircraftCaptured(Aircraft aircraft)
	{
		boolean isUsingRenderer = Config.isUSE_RENDER();
		//TODO: Added by |ZUTI|: this method can crash in some cases, probably
		//because I was altering multi-crew cockpit settings.
		//-----------------------------------------------
		if( aircraft == null || (isUsingRenderer && Main3D.cur3D().cockpitCur == null) )
			return;
		//-----------------------------------------------
		
		if (Actor.isAlive(aircraft))
		{
			if (!aircraft.FM.isOk() && isCaptured(aircraft))
			{
				for (int i = 0; i < aircraft.FM.crew; i++)
				{
					if (!aircraft.FM.AS.isPilotDead(i) && !aircraft.FM.AS.isPilotParatrooper(i))
					{
						EventLog.onCaptured(aircraft, i);
						if (isUsingRenderer && aircraft == World.getPlayerAircraft() && i == Main3D.cur3D().cockpitCur.astatePilotIndx())
						{
							World.setPlayerCaptured();
							if (isUsingRenderer) HUD.log("PlayerCAPT");
							if (Mission.isNet()) Chat.sendLog(1, "gore_captured", (NetUser) NetEnv.host(), null);
						}
					}
				}
			}
		}
	}

	public static boolean isCaptured(Actor actor)
	{
		if (actor == null || actor.pos == null) return false;
		return isCaptured(actor.getArmy(), actor.pos.getAbsPoint().x, actor.pos.getAbsPoint().y);
	}

	public static boolean isCaptured(int i, double d, double d_1_)
	{
		if (i == 0) 
			return false;
		int i_2_ = army(d, d_1_);
		if (i_2_ == 0)
			return false;
		if (i == i_2_)
			return false;
		List list = markers();
		int i_3_ = list.size();
		Marker marker = null;
		double d_4_ = 9.0E10;
		for (int i_5_ = 0; i_5_ < i_3_; i_5_++)
		{
			Marker marker_6_ = (Marker) list.get(i_5_);
			if (i == marker_6_.army)
			{
				double d_7_ = ((marker_6_.x - d) * (marker_6_.x - d) + (marker_6_.y - d_1_) * (marker_6_.y - d_1_));
				if (d_4_ > d_7_)
				{
					d_4_ = d_7_;
					marker = marker_6_;
				}
			}
		}
		if (marker == null)
			return true;
		double d_8_ = marker.x;
		double d_9_ = marker.y;
		double d_10_ = d;
		double d_11_ = d_1_;
		for (;;)
		{
			double d_12_ = ((d_8_ - d_10_) * (d_8_ - d_10_) + (d_9_ - d_11_) * (d_9_ - d_11_));
			if (d_12_ <= 1000000.0)
				break;
			double d_13_ = (d_8_ + d_10_) * 0.5;
			double d_14_ = (d_9_ + d_11_) * 0.5;
			i_2_ = army(d_13_, d_14_);
			if (i_2_ == i)
			{
				d_8_ = d_13_;
				d_9_ = d_14_;
			}
			else
			{
				d_10_ = d_13_;
				d_11_ = d_14_;
			}
		}
		d_8_ = (d_8_ + d_10_) * 0.5;
		d_9_ = (d_9_ + d_11_) * 0.5;
		d_4_ = (d_8_ - d) * (d_8_ - d) + (d_9_ - d_1_) * (d_9_ - d_1_);
		if (d_4_ >= 2.5E9)
			return true;
		List list_15_ = Engine.targets();
		i_3_ = list_15_.size();
		for (int i_16_ = 0; i_16_ < i_3_; i_16_++)
		{
			Actor actor = (Actor) list_15_.get(i_16_);
			if (!(actor instanceof Aircraft) && i != actor.getArmy())
			{
				Point3d point3d = actor.pos.getAbsPoint();
				double d_17_ = ((point3d.x - d) * (point3d.x - d) + (point3d.y - d_1_) * (point3d.y - d_1_));
				if (d_17_ < 1000000.0)
					return true;
			}
		}
		double d_18_ = Math.sqrt(d_4_) / 50000.0;
		double d_19_ = d + d_1_;
		if (d_19_ < 0.0)
			d_19_ = -d_19_;
		d_19_ -= (double) (int) d_19_;
		
		return 0.6 + d_18_ * 0.5 + (d_19_ - 0.5) >= 0.5;
	}

	public static int army(double d, double d_20_)
	{
		return World.cur().front._army(d, d_20_);
	}

	public int _army(double d, double d_21_)
	{
		int i = allMarkers.size();
		if (i == 0) return 0;
		int i_22_ = 0;
		double d_23_ = 9.0E10;
		for (int i_24_ = 0; i_24_ < i; i_24_++)
		{
			Marker marker = (Marker) allMarkers.get(i_24_);
			double d_25_ = ((marker.x - d) * (marker.x - d) + (marker.y - d_21_) * (marker.y - d_21_));
			if (d_23_ > d_25_)
			{
				i_22_ = marker.army;
				d_23_ = d_25_;
			}
		}
		return i_22_;
	}

	public void resetGameClear()
	{
		zutiFrontLineAlreadyLoaded = false;
		allMarkers.clear();
		tilesChanged = true;
	}

	public void resetGameCreate()
	{
	}

	public static void loadMission(SectFile sectfile)
	{
		World.cur().front._loadMission(sectfile);
	}

	public void _loadMission(SectFile sectfile)
	{
		//TODO: Added by |ZUTI|
		//--------------------------------
		if( zutiFrontLineAlreadyLoaded )
			return;
		//--------------------------------
		
		allMarkers.clear();
		
		int i = sectfile.sectionIndex("FrontMarker");
		if (i >= 0)
		{
			int i_26_ = sectfile.vars(i);
			for (int i_27_ = 0; i_27_ < i_26_; i_27_++)
			{
				NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(i, i_27_));
				String string = numbertokenizer.next((String) null);
				if (string != null && string.startsWith("FrontMarker"))
				{
					double d = numbertokenizer.next(0.0);
					double d_28_ = numbertokenizer.next(0.0);
					int i_29_ = numbertokenizer.next(1, 1, Army.amountNet() - 1);

					//TODO: Edited by |ZUTI|: render markers for coops too (not only red/blue ones)
					//if (i_29_ <= Army.amountSingle() - 1 || Mission.isDogfight())
					{
						Marker marker = new Marker();
						marker.x = d;
						marker.y = d_28_;
						marker.army = i_29_;
						marker._armyMask = 1 << i_29_ - 1;
						allMarkers.add(marker);
					}
				}
			}
		}
		tilesChanged = true;
		
		//TODO: Added by |ZUTI|: FrontLines loaded
		//----------------------------------------
		zutiFrontLineAlreadyLoaded = true;
		//----------------------------------------
	}

	public static void preRender(boolean bool)
	{
		World.cur().front._preRender(bool);
	}

	public static void render(boolean bool)
	{
		World.cur().front._render(bool);
	}

	public static void setMarkersChanged()
	{
		World.cur().front._setMarkersChanged();
	}

	public void _setMarkersChanged()
	{
		tilesChanged = true;
	}

	private boolean isOneArmy(int i)
	{
		int i_30_ = Army.amountNet() - 1;
		int i_31_ = 0;
		for (int i_32_ = 0; i_32_ < i_30_; i_32_++)
		{
			if ((i & 1 << i_32_) != 0 && ++i_31_ > 1) return false;
		}
		return true;
	}

	private void tilesUpdate()
	{
		CameraOrtho2D cameraortho2d = (CameraOrtho2D) Render.currentCamera();
		
		//TODO: Added by |ZUTI|
		//--------------------------------
		if( cameraortho2d == null )
			return;
		//--------------------------------
		
		int i = (int) (cameraortho2d.right - cameraortho2d.left) / 16 + 5;
		int i_33_ = (int) (cameraortho2d.top - cameraortho2d.bottom) / 16 + 5;
		if (i != tNx || i_33_ != tNy)
		{
			tNx = i;
			tNy = i_33_;
			mask = new byte[i * i_33_];
			mask2 = new byte[i * i_33_];
		}
		int i_34_ = allMarkers.size();
		if (i_34_ < 2)
			bExistFront = false;
		else
		{
			int i_35_ = 0;
			for (int i_36_ = 0; i_36_ < i_34_; i_36_++)
			{
				Marker marker = (Marker) allMarkers.get(i_36_);
				i_35_ |= marker._armyMask;
			}
			if (isOneArmy(i_35_))
				bExistFront = false;
			else
			{
				double d = 16.0 / cameraortho2d.worldScale;
				long l = (long) (cameraortho2d.worldXOffset / d) - 1L;
				camWorldXOffset = (double) l * d;
				camLeft = (float) ((double) cameraortho2d.left - ((cameraortho2d.worldXOffset - camWorldXOffset) * cameraortho2d.worldScale));
				l = (long) (cameraortho2d.worldYOffset / d) - 1L;
				camWorldYOffset = (double) l * d;
				camBottom = (float) ((double) cameraortho2d.bottom - ((cameraortho2d.worldYOffset - camWorldYOffset) * cameraortho2d.worldScale));
				double d_37_ = (cameraortho2d.worldYOffset + ((double) (cameraortho2d.right - cameraortho2d.left) / cameraortho2d.worldScale * 0.5));
				double d_38_ = (cameraortho2d.worldXOffset + ((double) (cameraortho2d.top - cameraortho2d.bottom) / cameraortho2d.worldScale * 0.5));
				double d_39_ = 9.0E10;
				for (int i_40_ = 0; i_40_ < i_34_; i_40_++)
				{
					Marker marker = (Marker) allMarkers.get(i_40_);
					double d_41_ = marker.x - d_38_;
					double d_42_ = marker.y - d_37_;
					marker._d2 = d_41_ * d_41_ + d_42_ * d_42_;
					if (d_39_ > marker._d2) d_39_ = marker._d2;
				}
				d_39_ = Math.sqrt(d_39_);
				d_39_ += ((double) (cameraortho2d.right - cameraortho2d.left + (cameraortho2d.top - cameraortho2d.bottom)) / cameraortho2d.worldScale);
				d_39_ *= d_39_;
				i_35_ = 0;
				for (int i_43_ = 0; i_43_ < i_34_; i_43_++)
				{
					Marker marker = (Marker) allMarkers.get(i_43_);
					if (d_39_ > marker._d2)
					{
						markers.add(marker);
						i_35_ |= marker._armyMask;
					}
				}
				if (markers.size() < 2 || isOneArmy(i_35_))
				{
					markers.clear();
					bExistFront = false;
				}
				else
				{
					i_34_ = markers.size();
					double d_44_ = camWorldYOffset - 0.5 * d;
					double d_45_ = camWorldXOffset - 0.5 * d;
					int i_46_ = 0;
					int i_47_ = 0;
					i_35_ = 0;
					for (int i_48_ = 0; i_48_ < i_33_; i_48_++)
					{
						double d_49_ = d_44_ + (double) i_48_ * d;
						for (int i_50_ = 0; i_50_ < i; i_50_++)
						{
							double d_51_ = d_45_ + (double) i_50_ * d;
							double d_52_ = d_39_;
							for (int i_53_ = 0; i_53_ < i_34_; i_53_++)
							{
								Marker marker = (Marker) markers.get(i_53_);
								double d_54_ = ((marker.x - d_51_) * (marker.x - d_51_) + (marker.y - d_49_) * (marker.y - d_49_));
								if (d_52_ > d_54_)
								{
									d_52_ = d_54_;
									i_46_ = marker.army;
									i_47_ = marker._armyMask;
								}
							}
							mask[i * i_48_ + i_50_] = (byte) i_46_;
							i_35_ |= i_47_;
						}
					}
					markers.clear();
					if (isOneArmy(i_35_))
						bExistFront = false;
					else
					{
						for (int i_55_ = 1; i_55_ < i_33_ - 1; i_55_++)
						{
							for (int i_56_ = 1; i_56_ < i - 1; i_56_++)
							{
								int i_57_ = i_55_ * i + i_56_;
								byte i_58_ = mask[i_57_];
								if (i_58_ == mask[i_57_ - 1] && i_58_ == mask[i_57_ + 1] && i_58_ == mask[i_57_ - i] && i_58_ == mask[i_57_ + i])
									mask2[i_57_] = (byte) 0;
								else
									mask2[i_57_] = (byte) i_58_;
							}
						}
						for (int i_59_ = 1; i_59_ < i_33_ - 1; i_59_++)
						{
							for (int i_60_ = 1; i_60_ < i - 1; i_60_++)
							{
								int i_61_ = i_59_ * i + i_60_;
								byte i_62_ = mask2[i_61_];
								if (i_62_ == 0)
									mask[i_61_] = (byte) 0;
								else
								{
									int i_63_ = 0;
									if (mask2[i_61_ + 1] == i_62_)
									{
										if ((mask2[i_61_ + i] != mask2[i_61_ - i + 1]) && (mask2[i_61_ - i] != mask2[i_61_ + i + 1])) i_63_ |= 0x1;
									}
									else if (mask2[i_61_ + i + 1] == i_62_ && mask2[i_61_ + i] != i_62_ && (mask2[i_61_ + i] != mask2[i_61_ + 1])) i_63_ |= 0x2;
									if (mask2[i_61_ + i] == i_62_)
									{
										if ((mask2[i_61_ - 1] != mask2[i_61_ + i + 1]) && (mask2[i_61_ + i - 1] != mask2[i_61_ + 1])) i_63_ |= 0x4;
									}
									else if (mask2[i_61_ + i - 1] == i_62_ && mask2[i_61_ - 1] != i_62_ && (mask2[i_61_ - 1] != mask2[i_61_ + i])) i_63_ |= 0x8;
									mask[i_61_] = (byte) i_63_;
								}
							}
						}
						bExistFront = true;
					}
				}
			}
		}
	}

	public void _preRender(boolean bool)
	{
		CameraOrtho2D cameraortho2d = (CameraOrtho2D) Render.currentCamera();
		
		//TODO: Added by |ZUTI|
		//--------------------------------
		if( cameraortho2d == null )
			return;
		//--------------------------------
		
		if (!tilesChanged)
			tilesChanged = (prevNCountMarkers != allMarkers.size() || prevCamLeft != cameraortho2d.left || prevCamBottom != cameraortho2d.bottom || prevCamWorldScale != cameraortho2d.worldScale || prevCamWorldXOffset != cameraortho2d.worldXOffset || prevCamWorldYOffset != cameraortho2d.worldYOffset);
		if (!tilesChanged && bool)
		{
			for (int i = 0; i < prevNCountMarkers; i++)
			{
				Marker marker = (Marker) allMarkers.get(i);
				if (marker.x != marker._x || marker.y != marker._y)
				{
					tilesChanged = true;
					break;
				}
			}
		}
		if (tilesChanged)
		{
			tilesUpdate();
			prevNCountMarkers = allMarkers.size();
			prevCamLeft = cameraortho2d.left;
			prevCamBottom = cameraortho2d.bottom;
			prevCamWorldScale = cameraortho2d.worldScale;
			prevCamWorldXOffset = cameraortho2d.worldXOffset;
			prevCamWorldYOffset = cameraortho2d.worldYOffset;
			for (int i = 0; i < prevNCountMarkers; i++)
			{
				Marker marker = (Marker) allMarkers.get(i);
				marker._x = marker.x;
				marker._y = marker.y;
			}
			tilesChanged = false;
			bTilesUpdated = true;
		}
		else
			bTilesUpdated = false;
		if (bExistFront && frontMat[0] == null)
		{
			frontMat[0] = Mat.New("icons/front1.mat");
			frontMat[1] = Mat.New("icons/front2.mat");
			frontMat[2] = Mat.New("icons/front3.mat");
			frontMat[3] = Mat.New("icons/front4.mat");
		}
	}

	public static boolean isMarkersUpdated()
	{
		return World.cur().front.bTilesUpdated;
	}

	private void drawTile3D(float f, float f_64_, Mat mat, int i)
	{
		double d = camWorldXOffset + (double) f * tStep;
		double d_65_ = camWorldYOffset + (double) f_64_ * tStep;
		double d_66_ = (double) Landscape.HQ((float) d, (float) d_65_);
		main.project2d(d, d_65_, d_66_, pointLand0);
		d += 32.0 * tStep;
		d_65_ += 32.0 * tStep;
		d_66_ = (double) Landscape.HQ((float) d, (float) d_65_);
		main.project2d(d, d_65_, d_66_, pointLand1);
		Render.drawTile((float) pointLand0.x, (float) pointLand0.y, (float) (pointLand1.x - pointLand0.x), (float) (pointLand1.y - pointLand0.y), 0.0F, mat, i, 0.0F, 0.0F, 1.0F, 1.0F);
	}

	public void _render(boolean bool)
	{
		if (bExistFront)
		{
			int i = tNx;
			int i_67_ = tNy;
			if (bool)
			{
				main = Main3D.cur3D();
				tStep = 1.0 / prevCamWorldScale;
				for (int i_68_ = 1; i_68_ < i_67_ - 1; i_68_++)
				{
					int i_69_ = 16 * (i_68_ - 1);
					for (int i_70_ = 1; i_70_ < i - 1; i_70_++)
					{
						int i_71_ = i_68_ * i + i_70_;
						byte i_72_ = mask[i_71_];
						if (i_72_ != 0)
						{
							int i_73_ = Army.color(mask2[i_71_]) | ~0xffffff;
							int i_74_ = 16 * (i_70_ - 1);
							if ((i_72_ & 0x1) != 0)
								drawTile3D((float) i_74_, (float) i_69_, frontMat[0], i_73_);
							else if ((i_72_ & 0x2) != 0) drawTile3D((float) i_74_, (float) i_69_, frontMat[1], i_73_);
							if ((i_72_ & 0x4) != 0)
								drawTile3D((float) i_74_, (float) i_69_, frontMat[2], i_73_);
							else if ((i_72_ & 0x8) != 0) drawTile3D((float) (i_74_ - 16), (float) i_69_, frontMat[3], i_73_);
						}
					}
				}
			}
			else
			{
				for (int i_75_ = 1; i_75_ < i_67_ - 1; i_75_++)
				{
					int i_76_ = 16 * (i_75_ - 1);
					for (int i_77_ = 1; i_77_ < i - 1; i_77_++)
					{
						int i_78_ = i_75_ * i + i_77_;
						byte i_79_ = mask[i_78_];
						if (i_79_ != 0)
						{
							int i_80_ = Army.color(mask2[i_78_]) | ~0xffffff;
							int i_81_ = 16 * (i_77_ - 1);
							if ((i_79_ & 0x1) != 0)
								Render.drawTile(camLeft + (float) i_81_, camBottom + (float) i_76_, 32.0F, 32.0F, 0.0F, frontMat[0], i_80_, 0.0F, 0.0F, 1.0F, 1.0F);
							else if ((i_79_ & 0x2) != 0) Render.drawTile(camLeft + (float) i_81_, camBottom + (float) i_76_, 32.0F, 32.0F, 0.0F, frontMat[1], i_80_, 0.0F, 0.0F, 1.0F, 1.0F);
							if ((i_79_ & 0x4) != 0)
								Render.drawTile(camLeft + (float) i_81_, camBottom + (float) i_76_, 32.0F, 32.0F, 0.0F, frontMat[2], i_80_, 0.0F, 0.0F, 1.0F, 1.0F);
							else if ((i_79_ & 0x8) != 0) Render.drawTile((camLeft + (float) i_81_ - 16.0F), camBottom + (float) i_76_, 32.0F, 32.0F, 0.0F, frontMat[3], i_80_, 0.0F, 0.0F, 1.0F, 1.0F);
						}
					}
				}
			}
		}
	}
}