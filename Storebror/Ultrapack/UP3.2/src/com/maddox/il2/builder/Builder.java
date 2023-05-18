/*4.10.1 class*/
package com.maddox.il2.builder;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Map;

import com.maddox.JGP.Point2d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.gwindow.GPoint;
import com.maddox.gwindow.GRegion;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowHScrollBar;
import com.maddox.gwindow.GWindowMenu;
import com.maddox.gwindow.GWindowMenuBarItem;
import com.maddox.gwindow.GWindowMenuItem;
import com.maddox.gwindow.GWindowMenuPopUp;
import com.maddox.gwindow.GWindowMessageBox;
import com.maddox.gwindow.GWindowRootMenu;
import com.maddox.gwindow.GWindowVScrollBar;
import com.maddox.gwindow.GWindowVSliderInt;
import com.maddox.gwindow.GWindowTabDialogClient.Tab;
import com.maddox.il2.ai.Army;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorFilter;
import com.maddox.il2.engine.ActorPosMove;
import com.maddox.il2.engine.ActorPosStatic;
import com.maddox.il2.engine.Camera3D;
import com.maddox.il2.engine.CameraOrtho2D;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.GUIWindowManager;
import com.maddox.il2.engine.IconDraw;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.TTFont;
import com.maddox.il2.engine.TextScr;
import com.maddox.il2.engine.hotkey.MouseXYZATK;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.gui.SquareLabels;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.il2.objects.air.Runaway;
import com.maddox.il2.objects.bridges.Bridge;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.rts.HotKeyCmd;
import com.maddox.rts.HotKeyCmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.MsgTimerListener;
import com.maddox.rts.MsgTimerParam;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.rts.SectFile;
import com.maddox.rts.SoftClass;
import com.maddox.rts.Time;
import com.maddox.util.HashMapExt;

public class Builder
{
	private ArrayList mis_cBoxes;
	private ArrayList mis_properties;
	private ArrayList mis_clipLoc;
	private Point3d mis_clipP0;
	public static String PLUGINS_SECTION = "builder_plugins";
	public static String envName;
	public static float defaultAzimut = 0.0F;
	public static final int colorTargetPrimary = -1;
	public static final int colorTargetSecondary = -16711936;
	public static final int colorTargetSecret = -8454144;
	public static float MaxVisualDistance = 16000.0F;
	public static float saveMaxVisualDistance = 5000.0F;
	public static float saveMaxStaticVisualDistance = 3000.0F;
	public BldConfig conf;
	public TTFont smallFont;
	public static final double viewHLandMin = 50.0;
	private Actor selectedActor;
	private HashMapExt selectedActors = new HashMapExt();
	public Camera3D camera;
	public CameraOrtho2D camera2D;
	private MouseXYZATK mouseXYZATK;
	private CursorMesh cursor;
	public boolean bMultiSelect;
	public boolean bSnap = false;
	public double snapStep = 10.0;
	public Pathes pathes;
	private boolean bFreeView = false;
	private double viewDistance = 100000.0;
	private double viewHMax = -1.0;
	private double viewH = -1.0;
	private double viewHLand = -1.0;
	private double viewX = 1.0;
	private double viewY = 1.0;
	private boolean bView3D = false;
	private Point3d _camPoint = new Point3d();
	private Orient _camOrient = new Orient(-90.0F, -90.0F, 0.0F);
	private Point3d __posScreenToLand = new Point3d();
	private SelectFilter _selectFilter = new SelectFilter();
	private Point3d __pi = new Point3d();
	private Orient __oi = new Orient();
	ArrayList _deleted = new ArrayList();
	private Actor[] _selectedActors;
	private boolean _bSelect;
	private double _selectX0;
	private double _selectY0;
	private double _selectX1;
	private double _selectY1;
	private FilterSelect filterSelect = new FilterSelect();
	private int saveMouseMode = 2;
	private Loc _savCameraNoFreeLoc = new Loc();
	private Loc _savCameraFreeLoc = new Loc();
	public static final int MOUSE_NONE = 0;
	public static final int MOUSE_OBJECT_MOVE = 1;
	public static final int MOUSE_WORLD_ZOOM = 2;
	public static final int MOUSE_MULTISELECT = 3;
	public static final int MOUSE_SELECT_TARGET = 4;
	public static final int MOUSE_FILL = 5;
	public int mouseState = 0;
	int mouseFirstPosX = 0;
	int mouseFirstPosY = 0;
	int mousePosX = 0;
	int mousePosY = 0;
	boolean bMouseRenderRect = false;
	Actor movedActor = null;
	Point3d movedActorPosSnap = new Point3d();
	Point3d movedActorPosStepSave = new Point3d();
	private Point3d _objectMoveP = new Point3d();
	protected Point3d projectPos3d = new Point3d();
	private float[] line5XYZ = new float[15];
	private Mat emptyMat;
	private float[] line2XYZ = new float[6];
	protected TTFont _gridFont;
	private int _gridCount;
	private int[] _gridX = new int[20];
	private int[] _gridY = new int[20];
	private int[] _gridVal = new int[20];
	protected Point2d projectPos2d = new Point2d();
	private float[] lineNXYZ = new float[6];
	private int lineNCounter;
	private Actor overActor = null;
	private Mat selectTargetMat;
	private GWindowMenuPopUp popUpMenu;
	private GWindowMessageBox deletingMessageBox;
	private boolean bDeletingChangeSelection;
	private Actor deletingActor;
	private boolean bRotateObjects = false;
	public ClientWindow clientWindow;
	public ViewWindow viewWindow;
	public XScrollBar mapXscrollBar;
	public YScrollBar mapYscrollBar;
	public ZSlider mapZSlider;
	public WSelect wSelect;
	public WViewLand wViewLand;
	public WSnap wSnap;
	public GWindowMenuBarItem mFile;
	public GWindowMenuBarItem mEdit;
	public GWindowMenuBarItem mConfigure;
	public GWindowMenuBarItem mView;
	public GWindowMenuItem mSelectItem;
	public GWindowMenuItem mViewLand;
	public GWindowMenuItem mSnap;
	public GWindowMenuItem mAlignItem;
	public GWindowMenuItem mDisplayFilter;
	public GWindowMenuItem mGrayScaleMap;
	public GWindowMenuItem mIcon8;
	public GWindowMenuItem mIcon16;
	public GWindowMenuItem mIcon32;
	public GWindowMenuItem mIcon64;
	private static int[] _viewPort = new int[4];
	
	public class ClientWindow extends GWindow
	{
		public void resized()
		{
			GRegion gregion = parentWindow.getClientRegion();
			win.set(gregion);
			mapXscrollBar.setSize(win.dx, lookAndFeel().getHScrollBarH());
			mapXscrollBar.setPos(0.0F, win.dy - lookAndFeel().getHScrollBarH());
			mapYscrollBar.setSize(lookAndFeel().getVScrollBarW(), win.dy - lookAndFeel().getHScrollBarH());
			mapYscrollBar.setPos(win.dx - lookAndFeel().getVScrollBarW(), 0.0F);
			mapZSlider.setSize(lookAndFeel().getVSliderIntW(), win.dy - lookAndFeel().getHScrollBarH());
			mapZSlider.setPos(0.0F, 0.0F);
			viewWindow.setPos(lookAndFeel().getVSliderIntW(), 0.0F);
			viewWindow.setSize(win.dx - 2.0F * lookAndFeel().getVScrollBarW(), win.dy - lookAndFeel().getHScrollBarH());
			if (isLoadedLandscape())
				computeViewMap2D(viewH, camera2D.worldXOffset + viewX / 2.0, camera2D.worldYOffset + viewY / 2.0);
		}
		
		public void resolutionChanged()
		{
			resized();
		}
	}
	
	public class ViewWindow extends GWindow
	{
		public void mouseMove(float f, float f_0_)
		{
			f_0_ = win.dy - f_0_ - 1.0F;
			doMouseAbsMove((int)f, (int)f_0_);
		}
		
		public void keyFocusEnter()
		{
			HotKeyCmdEnv.enable(Builder.envName, true);
		}
		
		public void keyFocusExit()
		{
			HotKeyCmdEnv.enable(Builder.envName, false);
		}
	}
	
	public class ZSlider extends GWindowVSliderInt
	{
		public boolean notify(int i, int i_1_)
		{
			if (i == 2)
			{
				if (isLoadedLandscape())
				{
					double d = (double)pos() / 100.0;
					double d_2_ = d * d;
					computeViewMap2D(d_2_, camera2D.worldXOffset + viewX / 2.0, camera2D.worldYOffset + viewY / 2.0);
				}
				return true;
			}
			return false;
		}
		
		public void setScrollPos(boolean bool, boolean bool_3_)
		{
			int i = posCount / 64;
			if (i <= 0)
				i = 1;
			setPos(pos + (bool ? i : -i), bool_3_);
		}
		
		public ZSlider(GWindow gwindow)
		{
			super(gwindow);
		}
	}
	
	public class YScrollBar extends GWindowVScrollBar
	{
		public boolean notify(int i, int i_4_)
		{
			if (i == 2)
			{
				if (isLoadedLandscape())
					Builder.this.worldScrool(0.0, (Main3D.cur3D().land2D.mapSizeY() - viewY - (double)(pos() / 100.0F) - camera2D.worldYOffset - Main3D.cur3D().land2D.worldOfsY()));
				return true;
			}
			if (i == 17)
				return super.notify(i, i_4_);
			return false;
		}
		
		public YScrollBar(GWindow gwindow)
		{
			super(gwindow);
		}
	}
	
	public class XScrollBar extends GWindowHScrollBar
	{
		public boolean notify(int i, int i_5_)
		{
			if (i == 2)
			{
				if (isLoadedLandscape())
					Builder.this.worldScrool(((double)(pos() / 100.0F) - camera2D.worldXOffset - Main3D.cur3D().land2D.worldOfsX()), 0.0);
				return true;
			}
			return false;
		}
		
		public XScrollBar(GWindow gwindow)
		{
			super(gwindow);
		}
	}
	
	class AnimateView implements MsgTimerListener
	{
		Actor aView;
		Loc from;
		Loc to;
		Loc cur = new Loc();
		
		public void msgTimer(MsgTimerParam msgtimerparam, int i, boolean bool, boolean bool_6_)
		{
			float f = (float)(i + 1) / 100.0F;
			cur.interpolate(from, to, f);
			camera.pos.setAbs(cur);
			if (bool_6_)
			{
				if (aView != null)
					camera.pos.setBase(aView, Main3D.cur3D().hookView, false);
				else
					Builder.this.endClearActorView();
				HotKeyCmdEnv.enable(Builder.envName, true);
			}
		}
		
		public AnimateView(Actor actor, Loc loc, Loc loc_7_)
		{
			aView = actor;
			from = loc;
			to = loc_7_;
			if (actor != null)
				camera.pos.setBase(null, null, false);
			HotKeyCmdEnv.enable(Builder.envName, false);
			RTSConf.cur.realTimer.msgAddListener(this, new MsgTimerParam(0, 0, Time.currentReal(), 100, 10, true, true));
		}
	}
	
	class FilterSelect implements ActorFilter
	{
		public boolean isUse(Actor actor, double d)
		{
			if (!conf.bEnableSelect)
				return false;
			if (!Property.containsValue(actor, "builderSpawn"))
				return false;
			if (actor instanceof ActorLabel)
				return false;
			if (actor instanceof BridgeSegment)
				return false;
			if (actor instanceof Bridge)
				return false;
			Point3d point3d = actor.pos.getAbsPoint();
			if (point3d.x < _selectX0 || point3d.x > _selectX1)
				return false;
			if (point3d.y < _selectY0 || point3d.y > _selectY1)
				return false;
			if (_bSelect)
				selectedActors.put(actor, null);
			else
				selectedActors.remove(actor);
			return true;
		}
	}
	
	class InterpolateOnLand extends Interpolate
	{
		public boolean tick()
		{
			Actor actor = selectedActor();
			if (!Actor.isValid(actor))
				return true;
			if (isFreeView() && bMultiSelect)
			{
				actor.pos.getAbs(__pi, __oi);
				double d = Engine.land().HQ(__pi.x, __pi.y);
				double d_8_ = __pi.z - d;
				boolean bool = Engine.land().isWater(__pi.x, __pi.y);
				int i = (int)d_8_;
				if (d_8_ < 0.0)
					d_8_ = -d_8_;
				int i_9_ = (int)(d_8_ * 100.0) % 100;
				int i_10_ = TextScr.font().height() - TextScr.font().descender();
				int i_11_ = 5;
				int i_12_ = 5;
				StringBuffer stringbuffer = new StringBuffer().append("curPos: ").append((int)__pi.x).append(" ").append((int)__pi.y).append(" H= ").append(i).append(".").append(i_9_).append(bool ? " water HLand:" : " land HLand:").append((float)d)
						.append(" type=");
				Engine.land();
				TextScr.output(i_11_, i_12_, stringbuffer.append(Landscape.getPixelMapT(Engine.land().WORLD2PIXX(__pi.x), Engine.land().WORLD2PIXY(__pi.y))).toString());
				TextScr.output(5, 5 + i_10_, ("Orient: " + (int)__oi.azimut() + " " + (int)__oi.tangage() + " " + (int)__oi.kren()));
				Point3d point3d = this.actor.pos.getAbsPoint();
				TextScr.output(5, 5 + 2 * i_10_, "Distance: " + (int)point3d.distance(__pi));
			}
			align(actor);
			return true;
		}
	}
	
	class SelectFilter implements ActorFilter
	{
		private Actor _Actor = null;
		private double _Len2;
		private double _maxLen2;
		
		public void reset(double d)
		{
			_Actor = null;
			_maxLen2 = d;
		}
		
		public Actor get()
		{
			return _Actor;
		}
		
		public boolean isUse(Actor actor, double d)
		{
			if (!conf.bEnableSelect)
				return true;
			if (d <= _maxLen2)
			{
				if (actor instanceof BridgeSegment)
				{
					if (conf.bViewBridge)
						actor = actor.getOwner();
					else
						return true;
				}
				if (actor instanceof Bridge)
				{
					if (!conf.bViewBridge)
						return true;
				}
				else if (actor instanceof PPoint)
				{
					if (isFreeView())
						return true;
					Path path = (Path)actor.getOwner();
					if (!path.isDrawing())
						return true;
				}
				else if (!Property.containsValue(actor, "builderSpawn"))
					return true;
				if (_Actor == null)
				{
					_Actor = actor;
					_Len2 = d;
				}
				else if (d < _Len2)
				{
					_Actor = actor;
					_Len2 = d;
				}
			}
			return true;
		}
	}
	
	public static int armyAmount()
	{
		return Army.amountSingle();
	}
	
	public static int colorSelected()
	{
		long l = Time.currentReal();
		long l_13_ = 1000L;
		double d = 2.0 * (double)(l % l_13_) / (double)l_13_;
		if (d >= 1.0)
			d = 2.0 - d;
		int i = (int)(255.0 * d);
		return ~0xffffff | i << 16 | i << 8 | i;
	}
	
	public static int colorMultiSelected(int i)
	{
		if (i == -1)
			i = -16777216;
		long l = Time.currentReal();
		long l_14_ = 1000L;
		double d = 2.0 * (double)(l % l_14_) / (double)l_14_;
		if (d >= 1.0)
			d = 2.0 - d;
		int i_15_ = (int)(255.0 * d);
		return ~0xffffff | i_15_ << 16 | i_15_ << 8 | i_15_ | i;
	}
	
	public boolean isLoadedLandscape()
	{
		return PlMapLoad.getLandLoaded() != null;
	}
	
	public boolean isFreeView()
	{
		return bFreeView;
	}
	
	public boolean isView3D()
	{
		return bView3D;
	}
	
	public double viewDistance()
	{
		return viewDistance;
	}
	
	public void computeViewMap2D(double d, double d_16_, double d_17_)
	{
		if (isLoadedLandscape())
		{
			int i = (int)viewWindow.win.dx;
			int i_18_ = (int)viewWindow.win.dy;
			double d_19_ = (double)camera.FOV() * 3.141592653589793 / 180.0 / 2.0;
			double d_20_ = (double)i / (double)i_18_;
			if (d < 0.0)
			{
				viewHMax = Main3D.cur3D().land2D.mapSizeX() / 2.0 / Math.tan(d_19_);
				double d_21_ = (Main3D.cur3D().land2D.mapSizeY() / 2.0 / Math.tan(d_19_) * d_20_);
				if (d_21_ < viewHMax)
					viewHMax = d_21_;
				int i_22_ = (int)(Math.sqrt(1.0) * 100.0);
				int i_23_ = (int)(Math.sqrt(viewHMax) * 100.0);
				mapZSlider.setRange(i_22_, i_23_ - i_22_ + 1, i_22_);
				d = viewHMax;
				d_16_ = (Main3D.cur3D().land2D.mapSizeX() / 2.0 - Main3D.cur3D().land2D.worldOfsX());
				d_17_ = (Main3D.cur3D().land2D.mapSizeY() / 2.0 - Main3D.cur3D().land2D.worldOfsY());
			}
			double d_24_ = Engine.land().HQ(d_16_, d_17_);
			if (d < 50.0 + d_24_)
				d = 50.0 + d_24_;
			if (d > viewHMax)
				d = viewHMax;
			if (viewH != d)
			{
				viewH = d;
				mapZSlider.setPos((int)(Math.sqrt(viewH) * 100.0), false);
			}
			viewHLand = viewH - d_24_;
			camera2D.worldScale = (double)i / (2.0 * viewH * Math.tan(d_19_));
			boolean bool = (int)(Main3D.cur3D().land2D.mapSizeX() * camera2D.worldScale + 0.5) > i;
			boolean bool_25_ = (int)(Main3D.cur3D().land2D.mapSizeY() * camera2D.worldScale + 0.5) > i_18_;
			viewX = (double)i / camera2D.worldScale;
			if (bool)
			{
				camera2D.worldXOffset = d_16_ - viewX / 2.0;
				if (camera2D.worldXOffset < -Main3D.cur3D().land2D.worldOfsX())
					camera2D.worldXOffset = -Main3D.cur3D().land2D.worldOfsX();
				if (camera2D.worldXOffset > (Main3D.cur3D().land2D.mapSizeX() - Main3D.cur3D().land2D.worldOfsX() - viewX))
					camera2D.worldXOffset = (Main3D.cur3D().land2D.mapSizeX() - Main3D.cur3D().land2D.worldOfsX() - viewX);
				mapXscrollBar.setRange(0.0F, (float)(int)(Main3D.cur3D().land2D.mapSizeX() * 100.0), (float)(int)(viewX * 100.0), (float)(int)(viewX * 100.0 / 64.0), (float)(int)((camera2D.worldXOffset + Main3D.cur3D().land2D.worldOfsX()) * 100.0));
			}
			else
			{
				camera2D.worldXOffset = (-(viewX - Main3D.cur3D().land2D.mapSizeX()) / 2.0 - Main3D.cur3D().land2D.worldOfsX());
				mapXscrollBar.setRange(0.0F, 0.0F, 1.0F, 1.0F, 0.0F);
			}
			viewY = (double)i_18_ / camera2D.worldScale;
			if (bool_25_)
			{
				camera2D.worldYOffset = d_17_ - viewY / 2.0;
				if (camera2D.worldYOffset < -Main3D.cur3D().land2D.worldOfsY())
					camera2D.worldYOffset = -Main3D.cur3D().land2D.worldOfsY();
				if (camera2D.worldYOffset > (Main3D.cur3D().land2D.mapSizeY() - Main3D.cur3D().land2D.worldOfsY() - viewY))
					camera2D.worldYOffset = (Main3D.cur3D().land2D.mapSizeY() - Main3D.cur3D().land2D.worldOfsY() - viewY);
				mapYscrollBar.setRange(0.0F, (float)(int)(Main3D.cur3D().land2D.mapSizeY() * 100.0), (float)(int)(viewY * 100.0), (float)(int)(viewY * 100.0 / 64.0), (float)(int)((Main3D.cur3D().land2D.mapSizeY() - viewY - camera2D.worldYOffset - Main3D
						.cur3D().land2D.worldOfsY()) * 100.0));
			}
			else
			{
				camera2D.worldYOffset = (-(viewY - Main3D.cur3D().land2D.mapSizeY()) / 2.0 - Main3D.cur3D().land2D.worldOfsY());
				mapYscrollBar.setRange(0.0F, 0.0F, 1.0F, 1.0F, 0.0F);
			}
			double d_26_ = Math.tan(d_19_) * viewH;
			double d_27_ = d_26_ / d_20_;
			double d_28_ = Math.sqrt(d_26_ * d_26_ + d_27_ * d_27_);
			viewDistance = Math.sqrt(viewH * viewH + d_28_ * d_28_);
			if (viewDistance > (double)MaxVisualDistance)
			{
				bView3D = false;
				Main3D.cur3D().land2D.show(conf.bShowLandscape);
				Main3D.cur3D().renderMap2D.useClearColor(true);
				Main3D.cur3D().render3D0.setShow(false);
				Main3D.cur3D().render3D1.setShow(false);
				Main3D.cur3D().render2D.setShow(false);
			}
			else
			{
				bView3D = true;
				Main3D.cur3D().land2D.show(false);
				Main3D.cur3D().renderMap2D.useClearColor(false);
				Main3D.cur3D().render3D0.setShow(true);
				Main3D.cur3D().render3D1.setShow(true);
				Main3D.cur3D().render2D.setShow(true);
			}
			setPosCamera3D();
			repaint();
		}
	}
	
	private void setPosCamera3D()
	{
		_camPoint.z = viewH;
		_camPoint.x = camera2D.worldXOffset + ((double)(camera2D.right - camera2D.left) / camera2D.worldScale / 2.0);
		_camPoint.y = camera2D.worldYOffset + ((double)(camera2D.top - camera2D.bottom) / camera2D.worldScale / 2.0);
		camera.pos.setAbs(_camPoint, _camOrient);
		camera.pos.reset();
	}
	
	public double posX2DtoWorld(int i)
	{
		return camera2D.worldXOffset + (double)i / camera2D.worldScale;
	}
	
	public double posY2DtoWorld(int i)
	{
		return camera2D.worldYOffset + (double)i / camera2D.worldScale;
	}
	
	public Point3d posScreenToLand(int i, int i_29_, double d, double d_30_)
	{
		Point3d point3d = __posScreenToLand;
		if (bView3D)
		{
			double d_31_ = (camera2D.worldXOffset + ((double)(camera2D.right - camera2D.left) / camera2D.worldScale / 2.0));
			double d_32_ = (camera2D.worldYOffset + ((double)(camera2D.top - camera2D.bottom) / camera2D.worldScale / 2.0));
			point3d.x = posX2DtoWorld(i);
			point3d.y = posY2DtoWorld(i_29_);
			point3d.z = Engine.land().HQ(point3d.x, point3d.y) + d;
			if (point3d.z > viewH)
				point3d.z = viewH;
			double d_33_ = (point3d.x - d_31_) / viewH;
			double d_34_ = (point3d.y - d_32_) / viewH;
			double d_35_ = 0.0;
			double d_36_ = (point3d.z - d_35_) * (point3d.z - d_35_);
			for (int i_37_ = 0; i_37_ < 8 && d_36_ > d_30_; i_37_++)
			{
				d_35_ = point3d.z;
				point3d.x = (viewH - d_35_) * d_33_ + d_31_;
				point3d.y = (viewH - d_35_) * d_34_ + d_32_;
				point3d.z = Engine.land().HQ(point3d.x, point3d.y) + d;
				if (point3d.z > viewH)
					point3d.z = viewH;
				d_36_ = (point3d.z - d_35_) * (point3d.z - d_35_);
			}
			for (int i_38_ = 0; i_38_ < 8 && d_36_ > d_30_; i_38_++)
			{
				d_35_ = point3d.z;
				point3d.x = ((viewH - d_35_) * d_33_ + d_31_ + point3d.x) / 2.0;
				point3d.y = ((viewH - d_35_) * d_34_ + d_32_ + point3d.y) / 2.0;
				point3d.z = Engine.land().HQ(point3d.x, point3d.y) + d;
				if (point3d.z > viewH)
					point3d.z = viewH;
				d_36_ = (point3d.z - d_35_) * (point3d.z - d_35_);
			}
		}
		else
		{
			point3d.x = posX2DtoWorld(i);
			point3d.y = posY2DtoWorld(i_29_);
			point3d.z = Engine.land().HQ(point3d.x, point3d.y);
		}
		return point3d;
	}
	
	public Point3d mouseWorldPos()
	{
		return posScreenToLand(mousePosX, mousePosY, 0.0, 0.1);
	}
	
	public Actor selectNear(Point3d point3d)
	{
		Actor actor = selectNear(point3d, 100.0);
		if (actor != null)
			return actor;
		return selectNear(point3d, 10000.0);
	}
	
	public Actor selectNearFull(int i, int i_39_)
	{
		if (i < 0 || i_39_ < 0)
			return null;
		Point3d point3d = posScreenToLand(i, i_39_, 0.0, 0.1);
		return selectNear(point3d);
	}
	
	public Actor selectNear(int i, int i_40_)
	{
		if (i < 0 || i_40_ < 0)
			return null;
		Point3d point3d = posScreenToLand(i, i_40_, 0.0, 0.1);
		double d = viewH - point3d.z;
		if (d < 0.0010)
			d = 0.0010;
		double d_41_ = (double)conf.iconSize * d / viewH / camera2D.worldScale / 2.0;
		return selectNear(point3d, d_41_);
	}
	
	public Actor selectNear(Point3d point3d, double d)
	{
		_selectFilter.reset(d * d);
		Engine.drawEnv().getFiltered((AbstractCollection)null, point3d.x - d, point3d.y - d, point3d.x + d, point3d.y + d, 15, _selectFilter);
		return _selectFilter.get();
	}
	
	private void worldScrool(double d, double d_42_)
	{
		double d_43_ = camera2D.worldXOffset + viewX / 2.0 + d;
		double d_44_ = camera2D.worldYOffset + viewY / 2.0 + d_42_;
		double d_45_ = Engine.land().HQ(d_43_, d_44_);
		double d_46_ = viewH;
		if (conf.bSaveViewHLand)
			d_46_ = d_45_ + viewHLand;
		computeViewMap2D(d_46_, d_43_, d_44_);
	}
	
	public void align(Actor actor)
	{
		if (actor instanceof ActorAlign)
			((ActorAlign)actor).align();
		else
		{
			actor.pos.getAbs(__pi);
			double d = Engine.land().HQ(__pi.x, __pi.y) + 0.2;
			__pi.z = d;
			actor.pos.setAbs(__pi);
		}
	}
	
	public void deleteAll()
	{
		setSelected(null);
		Plugin.doDeleteAll();
		Plugin.doAfterDelete();
		selectedActorsValidate();
		pathes.clear();
	}
	
	public int countSelectedActors()
	{
		if (bMultiSelect)
		{
			/* empty */
		}
		if (Actor.isValid(selectedActor))
		{
			if (selectedActors.containsKey(selectedActor))
				return selectedActors.size();
			return selectedActors.size() + 1;
		}
		return selectedActors.size();
	}
	
	public void selectActorsClear()
	{
		if (bMultiSelect)
		{
			/* empty */
		}
		selectedActors.clear();
	}
	
	public void selectActorsAdd(Actor actor)
	{
		if (bMultiSelect)
		{
			/* empty */
		}
		selectedActors.put(actor, null);
	}
	
	public Actor[] selectedActors()
	{
		if (bMultiSelect)
		{
			/* empty */
		}
		int i = countSelectedActors();
		Actor[] actors = _selectedActors(i > 0 ? i : 1);
		int i_47_ = 0;
		if (Actor.isValid(selectedActor))
			actors[i_47_++] = selectedActor;
		if (selectedActors.size() > 0)
		{
			for (Map.Entry entry = selectedActors.nextEntry(null); entry != null; entry = selectedActors.nextEntry(entry))
			{
				Actor actor = (Actor)entry.getKey();
				if (Actor.isValid(actor) && actor != selectedActor)
					actors[i_47_++] = actor;
			}
		}
		if (i_47_ == 0)
			actors[0] = null;
		else if (actors.length > i_47_)
			actors[i_47_] = null;
		return actors;
	}
	
	private Actor[] _selectedActors(int i)
	{
		if (_selectedActors == null || _selectedActors.length < i)
			_selectedActors = new Actor[i];
		return _selectedActors;
	}
	
	public void selectedActorsValidate()
	{
		Actor[] actors = selectedActors();
		for (int i = 0; i < actors.length; i++)
		{
			Actor actor = actors[i];
			if (actor == null)
				break;
			if (!Actor.isValid(actor) || !actor.isDrawing())
				selectedActors.remove(actor);
		}
	}
	
	public void select(double d, double d_48_, double d_49_, double d_50_, boolean bool)
	{
		if (d_49_ > d)
		{
			_selectX0 = d;
			_selectX1 = d_49_;
		}
		else
		{
			_selectX0 = d_49_;
			_selectX1 = d;
		}
		if (d_50_ > d_48_)
		{
			_selectY0 = d_48_;
			_selectY1 = d_50_;
		}
		else
		{
			_selectY0 = d_50_;
			_selectY1 = d_48_;
		}
		_bSelect = bool;
		Engine.drawEnv().getFiltered((AbstractCollection)null, _selectX0, _selectY0, _selectX1, _selectY1, 15, filterSelect);
	}
	
	public boolean isMiltiSelected(Actor actor)
	{
		if (bMultiSelect)
		{
			/* empty */
		}
		return selectedActors.containsKey(actor);
	}
	
	public boolean isSelected(Actor actor)
	{
		if (bMultiSelect)
		{
			/* empty */
		}
		if (actor == selectedActor)
			return true;
		return selectedActors.containsKey(actor);
	}
	
	public Actor selectedActor()
	{
		return selectedActor;
	}
	
	public PPoint selectedPoint()
	{
		if (pathes == null)
			return null;
		return pathes.currentPPoint;
	}
	
	public Path selectedPath()
	{
		if (pathes == null)
			return null;
		if (!Actor.isValid(pathes.currentPPoint))
			return null;
		return (Path)selectedPoint().getOwner();
	}
	
	public void setSelected(Actor actor)
	{
		//TODO: Added by |ZUTI|: do not select spawn place indicators
		//-----------------------------------------------------------
		if( !ZutiSupportMethods_Builder.canSelectActor(actor) )
			return;
		//-----------------------------------------------------------
		if (conf.bEnableSelect)
		{
			if (Actor.isValid(selectedActor))
			{
				Plugin plugin = (Plugin)Property.value(selectedActor, "builderPlugin");
				if (plugin instanceof PlMisStatic)
					defaultAzimut = selectedActor.pos.getAbsOrient().azimut();
			}
			int i = wSelect.tabsClient.getCurrent();
			wSelect.clearExtendTabs();
			if (actor != null && actor instanceof PPoint)
			{
				pathes.currentPPoint = (PPoint)actor;
				selectedActor = null;
				Plugin plugin = (Plugin)Property.value(actor.getOwner(), "builderPlugin");
				plugin.syncSelector();
				if (actor instanceof PAir)
					tip(Plugin.i18n("Selected") + " " + ((PathAir)actor.getOwner()).typedName);
				else if (actor instanceof PNodes)
					tip(Plugin.i18n("Selected") + " " + Property.stringValue(actor.getOwner(), "i18nName", ""));
			}
			else
			{
				if (pathes != null)
					pathes.currentPPoint = null;
				selectedActor = actor;
				if (isFreeView())
					setActorView(actor);
				if (actor != null)
				{
					Plugin plugin = (Plugin)Property.value(actor, "builderPlugin");
					if (plugin != null)
					{
						plugin.syncSelector();
						if (plugin instanceof PlMisStatic)
							defaultAzimut = actor.pos.getAbsOrient().azimut();
					}
					else if (bMultiSelect)
					{
						plugin = Plugin.getPlugin("MapActors");
						plugin.syncSelector();
					}
					String string;
					if (bMultiSelect)
					{
						string = (actor instanceof SoftClass ? ((SoftClass)actor).fullClassName() : actor.getClass().getName());
						int i_51_ = string.lastIndexOf('.');
						string = string.substring(i_51_ + 1);
					}
					else
						string = Property.stringValue(actor.getClass(), "i18nName", "");
					tip(Plugin.i18n("Selected") + " " + string);
				}
				else
					tip("");
			}
			if (i > 0 && i < wSelect.tabsClient.sizeTabs())
				wSelect.tabsClient.setCurrent(i);
		}
	}
	
	public void doUpdateSelector()
	{
		if (Actor.isValid(pathes.currentPPoint))
		{
			Plugin plugin = (Plugin)Property.value(pathes.currentPPoint.getOwner(), "builderPlugin");
			plugin.updateSelector();
		}
	}
	
	private void setActorView(Actor actor)
	{
		if (Actor.isValid(actor))
		{
			if (actorView() != actor)
			{
				if (actor.pos instanceof ActorPosStatic)
				{
					boolean bool = actor.isCollide();
					actor.collide(false);
					actor.drawing(false);
					actor.pos = new ActorPosMove(actor.pos);
					actor.drawing(true);
					if (bool)
						actor.collide(true);
				}
				mouseXYZATK.setTarget(actor);
				camera.pos.setBase(actor, Main3D.cur3D().hookView, false);
				camera.pos.reset();
				cursor.drawing(actor == cursor);
			}
		}
	}
	
	private void setActorView()
	{
		pathes.currentPPoint = null;
		bFreeView = true;
		saveMouseMode = RTSConf.cur.getUseMouse();
		if (saveMouseMode != 2)
			RTSConf.cur.setUseMouse(2);
		camera.pos.getAbs(_savCameraNoFreeLoc);
		Actor actor;
		if (Actor.isValid(selectedActor()))
			actor = selectedActor();
		else
		{
			actor = cursor;
			selectedActor = actor;
			Point3d point3d = new Point3d();
			camera.pos.getAbs(point3d);
			point3d.z = Engine.land().HQ(point3d.x, point3d.y) + 0.2;
			actor.pos.setAbs(point3d);
		}
		setActorView(actor);
		clientWindow.root.manager.activateMouse(false);
		clientWindow.root.manager.activateKeyboard(false);
		HotKeyCmdEnv.enable("HookView", true);
		HotKeyCmdEnv.enable("MouseXYZ", true);
		viewWindow.mouseCursor = 0;
		if (!bMultiSelect)
		{
			Main3D.cur3D().spritesFog.setShow(true);
			if (Main3D.cur3D().clouds != null)
			{
				Main3D.cur3D().bDrawClouds = true;
				Main3D.cur3D().clouds.setShow(true);
			}
			Main3D.cur3D().bEnableFog = true;
		}
		if (conf.bAnimateCamera)
		{
			camera.pos.getAbs(_savCameraFreeLoc);
			new AnimateView(actor, _savCameraNoFreeLoc, _savCameraFreeLoc);
		}
	}
	
	private void clearActorView()
	{
		camera.pos.getAbs(_savCameraFreeLoc);
		mouseXYZATK.setTarget(null);
		camera.pos.setBase(null, null, false);
		computeViewMap2D(_savCameraNoFreeLoc.getZ(), _savCameraFreeLoc.getX(), _savCameraFreeLoc.getY());
		camera.pos.getAbs(_savCameraNoFreeLoc);
		if (conf.bAnimateCamera)
			new AnimateView(null, _savCameraFreeLoc, _savCameraNoFreeLoc);
		else
			endClearActorView();
	}
	
	private void endClearActorView()
	{
		bFreeView = false;
		if (saveMouseMode != 2)
			RTSConf.cur.setUseMouse(saveMouseMode);
		cursor.drawing(false);
		if (selectedActor() == cursor)
			setSelected(null);
		selectedActorsValidate();
		clientWindow.root.manager.activateMouse(true);
		clientWindow.root.manager.activateKeyboard(true);
		HotKeyCmdEnv.enable("HookView", false);
		HotKeyCmdEnv.enable("MouseXYZ", false);
		viewWindow.mouseCursor = 1;
		PlMission.setChanged();
		if (!bMultiSelect)
		{
			Main3D.cur3D().spritesFog.setShow(false);
			if (Main3D.cur3D().clouds != null)
				Main3D.cur3D().clouds.setShow(false);
			Main3D.cur3D().bEnableFog = false;
		}
		repaint();
	}
	
	private Actor actorView()
	{
		return camera.pos.base();
	}
	
	public void doMouseAbsMove(int i, int i_52_)
	{
		if (isLoadedLandscape() && !isFreeView())
		{
			if (mousePosX == -1)
			{
				mousePosX = i;
				mousePosY = i_52_;
			}
			else
			{
				//TODO: Added by |ZUTI|: do not select spawn place indicators
				//-----------------------------------------------------------
				if( !ZutiSupportMethods_Builder.canSelectActor(selectNear(i, i_52_)) )
					return;
				//-----------------------------------------------------------				
				
				Point3d point3d = posScreenToLand(mousePosX, mousePosY, 0.0, 0.1);
				switch (mouseState)
				{
					case 1 :
					{
						double d = camera2D.worldScale;
						if (bView3D)
						{
							double d_53_ = viewH - point3d.z;
							if (d_53_ < 0.0010)
								d_53_ = 0.0010;
							d *= viewH / d_53_;
						}
						if (movedActor == null)
							worldScrool((double)(mousePosX - i) / d, (double)(mousePosY - i_52_) / d);
						else
						{
							double d_54_ = (double)(i - mousePosX) / d;
							double d_55_ = (double)(i_52_ - mousePosY) / d;
							if (bSnap)
							{
								movedActorPosSnap.x += d_54_;
								movedActorPosSnap.y += d_55_;
								movedActor.pos.getAbs(movedActorPosStepSave);
								_objectMoveP.set(movedActorPosSnap);
								_objectMoveP.x = (double)Math.round(movedActorPosSnap.x / snapStep) * snapStep;
								_objectMoveP.y = (double)Math.round(movedActorPosSnap.y / snapStep) * snapStep;
								_objectMoveP.z = movedActorPosSnap.z;
							}
							else
							{
								movedActor.pos.getAbs(_objectMoveP);
								_objectMoveP.x += d_54_;
								_objectMoveP.y += d_55_;
							}
							try
							{
								if (movedActor instanceof PPoint)
								{
									PPoint ppoint = (PPoint)movedActor;
									ppoint.moveTo(_objectMoveP);
									((Path)ppoint.getOwner()).pointMoved(ppoint);
									PlMission.setChanged();
								}
								else if (!(movedActor instanceof Bridge) && !(movedActor instanceof ActorLabel) && !(movedActor instanceof ActorBorn))
								{
									movedActor.pos.setAbs(_objectMoveP);
									movedActor.pos.reset();
									align(movedActor);
									PlMission.setChanged();
								}
								if (bMultiSelect)
								{
									/* empty */
								}
								if (selectedActors.containsKey(movedActor))
								{
									Actor[] actors = selectedActors();
									for (int i_56_ = 0; i_56_ < actors.length; i_56_++)
									{
										Actor actor = actors[i_56_];
										if (actor == null)
											break;
										if (Actor.isValid(actor) && actor != movedActor)
										{
											if (actor instanceof ActorAlign)
											{
												actor.pos.getAbs(__pi);
												if (bSnap)
												{
													__pi.x += (_objectMoveP.x - (movedActorPosStepSave.x));
													__pi.y += (_objectMoveP.y - (movedActorPosStepSave.y));
												}
												else
												{
													__pi.x += d_54_;
													__pi.y += d_55_;
												}
												actor.pos.setAbs(__pi);
												((ActorAlign)actor).align();
											}
											else
											{
												actor.pos.getAbs(__pi);
												__pi.x += d_54_;
												__pi.y += d_55_;
												double d_57_ = (Engine.land().HQ(__pi.x, __pi.y) + 0.2);
												__pi.z = d_57_;
												actor.pos.setAbs(__pi);
											}
											actor.pos.reset();
										}
									}
								}
							}
							catch (Exception exception)
							{
								mouseState = 0;
								viewWindow.mouseCursor = 1;
							}
							repaint();
						}
						break;
					}
					case 0 :
					{
						Actor actor = selectNear(i, i_52_);
						if (!bMultiSelect && actor != null && actor instanceof Bridge)
						{
							if (movedActor != null)
								viewWindow.mouseCursor = 1;
							movedActor = null;
							setOverActor(actor);
						}
						else
						{
							if (actor != null)
							{
								if (movedActor == null)
									viewWindow.mouseCursor = 7;
								movedActor = actor;
								movedActor.pos.getAbs(movedActorPosSnap);
							}
							else
							{
								if (movedActor != null)
									viewWindow.mouseCursor = 1;
								movedActor = null;
							}
							setOverActor(movedActor);
						}
						break;
					}
					case 4 :
						movedActor = null;
						setOverActor(selectNear(i, i_52_));
						if (!Actor.isValid(selectedPoint()) && !(selectedActor() instanceof PlMisRocket.Rocket))
							breakSelectTarget();
						break;
				}
				mousePosX = i;
				mousePosY = i_52_;
				if (mouseState == 5)
					Plugin.doFill(point3d);
				if (bMouseRenderRect)
					repaint();
			}
		}
	}
	
	public void render3D()
	{
		if (isLoadedLandscape())
		{
			Plugin.doRender3D();
			if (!isFreeView())
			{
				if (conf.bShowGrid && bView3D)
					drawGrid3D();
			}
		}
	}
	
	public boolean project2d(Point3d point3d, Point2d point2d)
	{
		return project2d(point3d.x, point3d.y, point3d.z, point2d);
	}
	
	public boolean project2d(double d, double d_58_, double d_59_, Point2d point2d)
	{
		if (bView3D)
		{
			Main3D.cur3D().project2d(d, d_58_, d_59_, projectPos3d);
			point2d.x = projectPos3d.x - (double)_viewPort[0];
			point2d.y = projectPos3d.y - (double)_viewPort[1];
		}
		else
		{
			point2d.x = (d - camera2D.worldXOffset) * camera2D.worldScale;
			point2d.y = (d_58_ - camera2D.worldYOffset) * camera2D.worldScale;
		}
		if (point2d.x + (double)conf.iconSize < 0.0)
			return false;
		if (point2d.y + (double)conf.iconSize < 0.0)
			return false;
		if (point2d.x - (double)conf.iconSize > (double)(camera2D.right - camera2D.left))
			return false;
		if (point2d.y - (double)conf.iconSize > (double)(camera2D.top - camera2D.bottom))
			return false;
		return true;
	}
	
	public void preRenderMap2D()
	{
		if (isLoadedLandscape())
			Plugin.doPreRenderMap2D();
	}
	
	public void renderMap2D()
	{
		if (isLoadedLandscape())
		{
			if (!isFreeView() && conf.iLightLand != 255)
			{
				int i = 255 - conf.iLightLand << 24 | 0x3f3f3f;
				Render.drawTile(0.0F, 0.0F, viewWindow.win.dx, viewWindow.win.dy, 0.0F, emptyMat, i, 0.0F, 0.0F, 1.0F, 1.0F);
				Render.drawEnd();
			}
			Plugin.doRenderMap2DBefore();
			if (!isFreeView())
				pathes.renderMap2DTargetLines();
			Plugin.doRenderMap2D();
			if (!isFreeView())
			{
				if (conf.bShowGrid)
				{
					if (!bView3D)
						drawGrid2D();
					drawGridText();
				}
				pathes.renderMap2D(bView3D, conf.iconSize);
				Plugin.doRenderMap2DAfter();
				if (bMouseRenderRect && (mouseFirstPosX != mousePosX || mouseFirstPosY != mousePosY))
				{
					line5XYZ[0] = (float)mouseFirstPosX;
					line5XYZ[1] = (float)mouseFirstPosY;
					line5XYZ[2] = 0.0F;
					line5XYZ[3] = (float)mousePosX;
					line5XYZ[4] = (float)mouseFirstPosY;
					line5XYZ[5] = 0.0F;
					line5XYZ[6] = (float)mousePosX;
					line5XYZ[7] = (float)mousePosY;
					line5XYZ[8] = 0.0F;
					line5XYZ[9] = (float)mouseFirstPosX;
					line5XYZ[10] = (float)mousePosY;
					line5XYZ[11] = 0.0F;
					Render.drawBeginLines(-1);
					Render.drawLines(line5XYZ, 4, 1.0F, colorSelected(), (Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE), 4);
					Render.drawEnd();
				}
				drawInfoOverActor();
				drawSelectTarget();
				if (Main3D.cur3D().land2DText != null)
					Main3D.cur3D().land2DText.render();
				if (conf.bShowGrid && !bView3D)
					SquareLabels.draw(camera2D, Main3D.cur3D().land2D.worldOfsX(), Main3D.cur3D().land2D.worldOfsX(), Main3D.cur3D().land2D.mapSizeX());
			}
		}
	}
	
	private int gridStep()
	{
		double d = viewX;
		if (viewY < viewX)
			d = viewY;
		d *= viewHLand / viewH;
		int i = 100000;
		for (int i_60_ = 0; i_60_ < 5 && !((double)(i * 3) <= d); i_60_++)
			i /= 10;
		return i;
	}
	
	private void drawGrid2D()
	{
		int i = gridStep();
		int i_61_ = (int)((camera2D.worldXOffset + Main3D.cur3D().land2D.worldOfsX()) / (double)i);
		int i_62_ = (int)((camera2D.worldYOffset + Main3D.cur3D().land2D.worldOfsY()) / (double)i);
		int i_63_ = (int)(viewX / (double)i) + 2;
		int i_64_ = (int)(viewY / (double)i) + 2;
		float f = (float)((((double)(i_61_ * i) - camera2D.worldXOffset - Main3D.cur3D().land2D.worldOfsX()) * camera2D.worldScale) + 0.5);
		float f_65_ = (float)((((double)(i_62_ * i) - camera2D.worldYOffset - Main3D.cur3D().land2D.worldOfsY()) * camera2D.worldScale) + 0.5);
		float f_66_ = (float)((double)(i_63_ * i) * camera2D.worldScale);
		float f_67_ = (float)((double)(i_64_ * i) * camera2D.worldScale);
		float f_68_ = (float)((double)i * camera2D.worldScale);
		_gridCount = 0;
		Render.drawBeginLines(-1);
		for (int i_69_ = 0; i_69_ <= i_64_; i_69_++)
		{
			float f_70_ = f_65_ + (float)i_69_ * f_68_;
			int i_71_ = (i_69_ + i_62_) % 10 == 0 ? 192 : 127;
			line2XYZ[0] = f;
			line2XYZ[1] = f_70_;
			line2XYZ[2] = 0.0F;
			line2XYZ[3] = f + f_66_;
			line2XYZ[4] = f_70_;
			line2XYZ[5] = 0.0F;
			Render.drawLines(line2XYZ, 2, 1.0F, ~0xffffff | i_71_ << 16 | i_71_ << 8 | i_71_, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 0);
			if (i_71_ == 192)
				drawGridText(0, (int)f_70_, (i_62_ + i_69_) * i);
		}
		for (int i_72_ = 0; i_72_ <= i_63_; i_72_++)
		{
			float f_73_ = f + (float)i_72_ * f_68_;
			int i_74_ = (i_72_ + i_61_) % 10 == 0 ? 192 : 127;
			line2XYZ[0] = f_73_;
			line2XYZ[1] = f_65_;
			line2XYZ[2] = 0.0F;
			line2XYZ[3] = f_73_;
			line2XYZ[4] = f_65_ + f_67_;
			line2XYZ[5] = 0.0F;
			Render.drawLines(line2XYZ, 2, 1.0F, ~0xffffff | i_74_ << 16 | i_74_ << 8 | i_74_, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 0);
			if (i_74_ == 192)
				drawGridText((int)f_73_, 0, (i_61_ + i_72_) * i);
		}
		Render.drawEnd();
	}
	
	private void drawGridText(int i, int i_75_, int i_76_)
	{
		if (i >= 0 && i_75_ >= 0 && i_76_ > 0 && _gridCount != 20)
		{
			_gridX[_gridCount] = i;
			_gridY[_gridCount] = i_75_;
			_gridVal[_gridCount] = i_76_;
			_gridCount++;
		}
	}
	
	private void drawGridText()
	{
		for (int i = 0; i < _gridCount; i++)
			_gridFont.output(-4144960, (float)(_gridX[i] + 2), (float)(_gridY[i] + 2), 0.0F, (_gridVal[i] / 1000 + "." + _gridVal[i] % 1000 / 100));
		_gridCount = 0;
	}
	
	private void drawGrid3D()
	{
		int i = gridStep();
		int i_77_ = (int)((camera2D.worldXOffset + Main3D.cur3D().land2D.worldOfsX()) / (double)i);
		int i_78_ = (int)((camera2D.worldYOffset + Main3D.cur3D().land2D.worldOfsY()) / (double)i);
		int i_79_ = (int)(viewX / (double)i) + 2;
		int i_80_ = (int)(viewY / (double)i) + 2;
		Render.drawBeginLines(0);
		for (int i_81_ = 0; i_81_ <= i_80_; i_81_++)
		{
			float f = (float)((i_81_ + i_78_) * i);
			int i_82_ = 64;
			boolean bool = true;
			if ((i_81_ + i_78_) % 2 == 0)
			{
				i_82_ = 150;
				if ((i_81_ + i_78_) % 10 == 0)
				{
					bool = false;
					i_82_ = 240;
				}
			}
			double d = -1.0;
			double d_83_ = -1.0;
			if (lineNXYZ.length / 3 <= i_79_)
				lineNXYZ = new float[(i_79_ + 1) * 3];
			lineNCounter = 0;
			for (int i_84_ = 0; i_84_ <= i_79_; i_84_++)
			{
				float f_85_ = (float)((i_84_ + i_77_) * i);
				Engine.land();
				float f_86_ = Landscape.HQ(f_85_ - (float)Main3D.cur3D().land2D.worldOfsX(), f - (float)Main3D.cur3D().land2D.worldOfsY());
				lineNXYZ[lineNCounter * 3 + 0] = f_85_ - (float)Main3D.cur3D().land2D.worldOfsX();
				lineNXYZ[lineNCounter * 3 + 1] = f - (float)Main3D.cur3D().land2D.worldOfsY();
				lineNXYZ[lineNCounter * 3 + 2] = f_86_;
				lineNCounter++;
				if (!bool)
				{
					project2d((double)(f_85_ - (float)Main3D.cur3D().land2D.worldOfsX()), (double)(f - (float)Main3D.cur3D().land2D.worldOfsY()), (double)f_86_, projectPos2d);
					if (projectPos2d.x > 0.0)
					{
						if (i_84_ > 0)
							drawGridText(0, (int)(d_83_ - (d / (projectPos2d.x - d) * (projectPos2d.y - d_83_))), (int)f);
						else
							drawGridText(0, (int)projectPos2d.y, (int)f);
						bool = true;
					}
					d = projectPos2d.x;
					d_83_ = projectPos2d.y;
				}
			}
			Render.drawLines(lineNXYZ, lineNCounter, 1.0F, ~0xffffff | i_82_ << 16 | i_82_ << 8 | i_82_, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 2);
		}
		for (int i_87_ = 0; i_87_ <= i_79_; i_87_++)
		{
			float f = (float)((i_87_ + i_77_) * i);
			int i_88_ = 64;
			boolean bool = true;
			if ((i_87_ + i_77_) % 2 == 0)
			{
				i_88_ = 150;
				if ((i_87_ + i_77_) % 10 == 0)
				{
					bool = false;
					i_88_ = 240;
				}
			}
			double d = -1.0;
			double d_89_ = -1.0;
			if (lineNXYZ.length / 3 <= i_80_)
				lineNXYZ = new float[(i_80_ + 1) * 3];
			lineNCounter = 0;
			for (int i_90_ = 0; i_90_ <= i_80_; i_90_++)
			{
				float f_91_ = (float)((i_90_ + i_78_) * i);
				Engine.land();
				float f_92_ = Landscape.HQ(f - (float)Main3D.cur3D().land2D.worldOfsX(), f_91_ - (float)Main3D.cur3D().land2D.worldOfsY());
				lineNXYZ[lineNCounter * 3 + 0] = f - (float)Main3D.cur3D().land2D.worldOfsX();
				lineNXYZ[lineNCounter * 3 + 1] = f_91_ - (float)Main3D.cur3D().land2D.worldOfsY();
				lineNXYZ[lineNCounter * 3 + 2] = f_92_;
				lineNCounter++;
				if (!bool)
				{
					project2d((double)(f - (float)Main3D.cur3D().land2D.worldOfsX()), (double)(f_91_ - (float)Main3D.cur3D().land2D.worldOfsY()), (double)f_92_, projectPos2d);
					if (projectPos2d.y > 0.0)
					{
						if (i_90_ > 0)
							drawGridText((int)(d - (d_89_ / (projectPos2d.y - d_89_) * (projectPos2d.x - d))), 0, (int)f);
						else
							drawGridText((int)projectPos2d.x, 0, (int)f);
						bool = true;
					}
					d = projectPos2d.x;
					d_89_ = projectPos2d.y;
				}
			}
			Render.drawLines(lineNXYZ, lineNCounter, 1.0F, ~0xffffff | i_88_ << 16 | i_88_ << 8 | i_88_, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 2);
		}
		Render.drawEnd();
	}
	
	public Actor getOverActor()
	{
		return overActor;
	}
	
	public void setOverActor(Actor actor)
	{
		overActor = actor;
	}
	
	private void drawInfoOverActor()
	{
		if (Actor.isValid(overActor))
		{
			Plugin plugin = (Plugin)Property.value((overActor instanceof PPoint ? overActor.getOwner() : overActor), "builderPlugin");
			if (plugin != null)
			{
				String[] strings = plugin.actorInfo(overActor);
				if (strings != null)
				{
					Point3d point3d = overActor.pos.getAbsPoint();
					if (project2d(point3d, projectPos2d))
					{
						float f = 0.0F;
						int i = 0;
						for (int i_93_ = 0; i_93_ < strings.length; i_93_++)
						{
							String string = strings[i_93_];
							if (string == null)
								break;
							float f_94_ = smallFont.width(string);
							if (f_94_ > f)
								f = f_94_;
							i++;
						}
						if (f != 0.0F)
						{
							float f_95_ = (float)-smallFont.descender();
							float f_96_ = (float)smallFont.height();
							float f_97_ = (float)i * (f_96_ + f_95_) + f_95_;
							int i_98_ = Army.color(overActor instanceof PPoint ? overActor.getOwner().getArmy() : overActor.getArmy());
							Render.drawTile((float)projectPos2d.x, (float)(projectPos2d.y + (double)(conf.iconSize / 2) + (double)f_95_), f + 2.0F * f_95_, f_97_, 0.0F, emptyMat, i_98_ & 0x7fffffff, 0.0F, 0.0F, 1.0F, 1.0F);
							Render.drawEnd();
							for (int i_99_ = 0; i_99_ < strings.length; i_99_++)
							{
								String string = strings[i_99_];
								if (string == null)
									break;
								smallFont.output(~0xffffff | i_98_ ^ 0xffffffff, (float)(projectPos2d.x + (double)f_95_),
										(float)(projectPos2d.y + (double)(conf.iconSize / 2) + (double)f_95_ + (double)((float)(i - i_99_ - 1) * (f_95_ + f_96_)) + (double)f_95_), 0.0F, string);
							}
						}
					}
				}
			}
		}
	}
	
	private void drawSelectTarget()
	{
		if (mouseState == 4 && viewWindow.isMouseOver())
		{
			PAir pair = (PAir)selectedPoint();
			Point3d point3d;
			if (Actor.isValid(pair))
				point3d = pair.pos.getAbsPoint();
			else
			{
				Actor actor = selectedActor();
				if (!(actor instanceof PlMisRocket.Rocket))
					return;
				point3d = actor.pos.getAbsPoint();
			}
			project2d(point3d, projectPos2d);
			lineNXYZ[0] = (float)projectPos2d.x;
			lineNXYZ[1] = (float)projectPos2d.y;
			lineNXYZ[2] = 0.0F;
			lineNXYZ[3] = (float)mousePosX;
			lineNXYZ[4] = (float)mousePosY;
			lineNXYZ[5] = 0.0F;
			Render.drawBeginLines(-1);
			Render.drawLines(lineNXYZ, 2, 1.0F, -16711936, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 2);
			Render.drawEnd();
			float f = (float)(conf.iconSize * 2);
			Render.drawTile((float)mousePosX - f / 2.0F, (float)mousePosY - f / 2.0F, f, f, 0.0F, selectTargetMat, -1, 0.0F, 0.0F, 1.0F, 1.0F);
		}
	}
	
	public void beginSelectTarget()
	{
		Path path = selectedPath();
		if (path == null || !(path instanceof PathAir))
		{
			Actor actor = selectedActor();
			if (!(actor instanceof PlMisRocket.Rocket))
				return;
		}
		mouseState = 4;
		viewWindow.mouseCursor = 0;
	}
	
	public void endSelectTarget()
	{
		if (mouseState == 4)
		{
			mouseState = 0;
			viewWindow.mouseCursor = 1;
			Path path = selectedPath();
			if (path == null || !(path instanceof PathAir))
			{
				PlMisRocket.Rocket rocket = (PlMisRocket.Rocket)selectedActor();
				Point3d point3d = mouseWorldPos();
				rocket.target = new Point2d(point3d.x, point3d.y);
			}
			else
			{
				Actor actor = getOverActor();
				if (!Actor.isValid(actor) || (actor instanceof PPoint && actor.getOwner() == selectedPath()))
					return;
				PAir pair = (PAir)selectedPoint();
				pair.setTarget(actor);
				Plugin plugin = (Plugin)Property.value(pair.getOwner(), "builderPlugin");
				plugin.updateSelector();
			}
			PlMission.setChanged();
		}
	}
	
	public void breakSelectTarget()
	{
		if (mouseState == 4)
		{
			mouseState = 0;
			viewWindow.mouseCursor = 1;
		}
	}
	
	private void initHotKeys()
	{
		HotKeyCmdEnv.setCurrentEnv(envName);
		HotKeyEnv.fromIni(envName, Config.cur.ini, "HotKey " + envName);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "toLand")
		{
			public void begin()
			{
				if (Actor.isValid(selectedActor()))
				{
					align(selectedActor());
					PlMission.setChanged();
					if (!isFreeView())
						repaint();
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "normalLand")
		{
			public void begin()
			{
				if (Actor.isValid(selectedActor()))
				{
					Point3d point3d = new Point3d();
					Orient orient = new Orient();
					selectedActor().pos.getAbs(point3d, orient);
					Vector3f vector3f = new Vector3f();
					Engine.land().N(point3d.x, point3d.y, vector3f);
					orient.orient(vector3f);
					selectedActor().pos.setAbs(orient);
					Builder.defaultAzimut = orient.azimut();
					PlMission.setChanged();
					if (!isFreeView())
						repaint();
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "land")
		{
			public void begin()
			{
				changeViewLand();
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "stepAzimut-5")
		{
			public void begin()
			{
				Builder.this.stepAzimut(-5);
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "stepAzimut-15")
		{
			public void begin()
			{
				Builder.this.stepAzimut(-15);
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "stepAzimut-30")
		{
			public void begin()
			{
				Builder.this.stepAzimut(-30);
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "stepAzimut5")
		{
			public void begin()
			{
				Builder.this.stepAzimut(5);
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "stepAzimut15")
		{
			public void begin()
			{
				Builder.this.stepAzimut(15);
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "stepAzimut30")
		{
			public void begin()
			{
				Builder.this.stepAzimut(30);
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "resetAngles")
		{
			public void begin()
			{
				if (Actor.isValid(selectedActor()))
				{
					Orient orient = new Orient();
					orient.set(0.0F, 0.0F, 0.0F);
					Builder.defaultAzimut = 0.0F;
					selectedActor().pos.setAbs(orient);
					PlMission.setChanged();
					if (!isFreeView())
						repaint();
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "resetTangage90")
		{
			public void begin()
			{
				if (Actor.isValid(selectedActor()))
				{
					Orient orient = new Orient();
					orient.set(0.0F, 90.0F, 0.0F);
					Builder.defaultAzimut = 0.0F;
					selectedActor().pos.setAbs(orient);
					PlMission.setChanged();
					if (!isFreeView())
						repaint();
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "change+")
		{
			public void begin()
			{
				changeType(false, false);
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "change++")
		{
			public void begin()
			{
				changeType(false, true);
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "change-")
		{
			public void begin()
			{
				changeType(true, false);
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "change--")
		{
			public void begin()
			{
				changeType(true, true);
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "insert")
		{
			public void begin()
			{
				Builder.this.insert(false);
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "insert+")
		{
			public void begin()
			{
				Builder.this.insert(true);
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "delete")
		{
			public void begin()
			{
				Builder.this.delete(false);
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "delete+")
		{
			public void begin()
			{
				Builder.this.delete(true);
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "fill")
		{
			public void begin()
			{
				if (isLoadedLandscape() && !isFreeView() && (!bMultiSelect || bView3D) && mouseState == 0)
				{
					mouseState = 5;
					Point3d point3d = posScreenToLand(mousePosX, mousePosY, 0.0, 0.1);
					Plugin.doBeginFill(point3d);
				}
			}
			
			public void end()
			{
				if (mouseState == 5)
				{
					mouseState = 0;
					Point3d point3d = posScreenToLand(mousePosX, mousePosY, 0.0, 0.1);
					Plugin.doEndFill(point3d);
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "cursor")
		{
			public void begin()
			{
				if (isLoadedLandscape() && isFreeView())
				{
					if (Builder.this.actorView() == cursor)
					{
						Actor actor = selectNear(Builder.this.actorView().pos.getAbsPoint());
						if (actor != null)
						{
							cursor.drawing(false);
							setSelected(actor);
						}
					}
					else if (Actor.isValid(Builder.this.actorView()))
					{
						Loc loc = Builder.this.actorView().pos.getAbs();
						cursor.pos.setAbs(loc);
						cursor.pos.reset();
						cursor.drawing(true);
						setSelected(cursor);
					}
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "objectMove")
		{
			public void begin()
			{
				if (isLoadedLandscape() && !isFreeView())
				{
					if (mouseState == 4)
						endSelectTarget();
					else if (mouseState == 0)
					{
						mouseState = 1;
						viewWindow.mouseCursor = 7;
						Actor actor = selectNear(mousePosX, mousePosY);
						if (actor != null)
						{
							setSelected(actor);
							repaint();
						}
						setOverActor(null);
					}
				}
			}
			
			public void end()
			{
				if (isLoadedLandscape() && !isFreeView() && mouseState == 1)
				{
					mouseState = 0;
					viewWindow.mouseCursor = 1;
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "worldZoom")
		{
			public void begin()
			{
				if (isLoadedLandscape() && !isFreeView() && mouseState == 0)
				{
					mouseState = 2;
					mouseFirstPosX = mousePosX;
					mouseFirstPosY = mousePosY;
					bMouseRenderRect = true;
					viewWindow.mouseCursor = 2;
					setOverActor(null);
				}
			}
			
			public void end()
			{
				if (isLoadedLandscape() && !isFreeView() && mouseState == 2)
				{
					mouseState = 0;
					bMouseRenderRect = false;
					if (mouseFirstPosX == mousePosX)
						repaint();
					else
					{
						Point3d point3d = posScreenToLand(mouseFirstPosX, mouseFirstPosY, 0.0, 0.1);
						double d = point3d.x;
						double d_123_ = point3d.y;
						point3d = posScreenToLand(mousePosX, mousePosY, 0.0, 0.1);
						double d_124_ = ((double)camera.FOV() * 3.141592653589793 / 180.0 / 2.0);
						double d_125_ = point3d.x - d;
						if (d_125_ < 0.0)
							d_125_ = -d_125_;
						double d_126_ = d_125_ / 2.0 / Math.tan(d_124_);
						computeViewMap2D(d_126_, (d + point3d.x) / 2.0, (d_123_ + point3d.y) / 2.0);
						viewWindow.mouseCursor = 1;
					}
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "unselect")
		{
			public void begin()
			{
				if (isLoadedLandscape() && !isFreeView() && mouseState == 0)
				{
					setSelected(null);
					repaint();
				}
			}
		});
		if (bMultiSelect)
		{
			/* empty */
		}
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "select+")
		{
			public void begin()
			{
				if (isLoadedLandscape() && !isFreeView() && mouseState == 0)
				{
					mouseState = 3;
					mouseFirstPosX = mousePosX;
					mouseFirstPosY = mousePosY;
					bMouseRenderRect = true;
				}
			}
			
			public void end()
			{
				if (isLoadedLandscape() && !isFreeView() && mouseState == 3)
				{
					mouseState = 0;
					bMouseRenderRect = false;
					if (mouseFirstPosX != mousePosX)
					{
						Point3d point3d = posScreenToLand(mouseFirstPosX, mouseFirstPosY, 0.0, 0.1);
						double d = point3d.x;
						double d_129_ = point3d.y;
						point3d = posScreenToLand(mousePosX, mousePosY, 0.0, 0.1);
						select(d, d_129_, point3d.x, point3d.y, true);
						setSelected(null);
					}
					repaint();
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "select-")
		{
			public void begin()
			{
				if (isLoadedLandscape() && !isFreeView() && mouseState == 0)
				{
					mouseState = 3;
					mouseFirstPosX = mousePosX;
					mouseFirstPosY = mousePosY;
					bMouseRenderRect = true;
				}
			}
			
			public void end()
			{
				if (isLoadedLandscape() && !isFreeView() && mouseState == 3)
				{
					mouseState = 0;
					bMouseRenderRect = false;
					if (mouseFirstPosX != mousePosX)
					{
						Point3d point3d = posScreenToLand(mouseFirstPosX, mouseFirstPosY, 0.0, 0.1);
						double d = point3d.x;
						double d_131_ = point3d.y;
						point3d = posScreenToLand(mousePosX, mousePosY, 0.0, 0.1);
						select(d, d_131_, point3d.x, point3d.y, false);
						setSelected(null);
					}
					repaint();
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "freeView")
		{
			public void end()
			{
				if (isLoadedLandscape() && mouseState == 0)
				{
					if (isFreeView())
						Builder.this.clearActorView();
					else if (bView3D)
						Builder.this.setActorView();
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "break")
		{
			public void end()
			{
				if (isLoadedLandscape())
				{
					if (isFreeView())
						Builder.this.clearActorView();
					else
					{
						setOverActor(null);
						breakSelectTarget();
						mouseState = 0;
					}
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "popupmenu")
		{
			public void begin()
			{
				if (isLoadedLandscape() && mouseState == 0 && !isFreeView())
					doPopUpMenu();
			}
		});
		if (!bMultiSelect)
		{
			HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "mis_cut")
			{
				public void begin()
				{
					Builder.this.mis_cut();
				}
			});
			HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "mis_copy")
			{
				public void begin()
				{
					Builder.this.mis_copy(true);
				}
			});
			HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "mis_paste")
			{
				public void begin()
				{
					Builder.this.mis_paste();
				}
			});
		}
	}
	
	private void mis_cut()
	{
		if (!Plugin.builder.isFreeView())
		{
			mis_copy(false);
			Actor[] actors = selectedActors();
			for (int i = 0; i < actors.length; i++)
			{
				Actor actor = actors[i];
				if (actor == null)
					break;
				if (Actor.isValid(actor) && isMiltiSelected(actor))
				{
					if (actor instanceof PAirdrome)
					{
						PathAirdrome pathairdrome = (PathAirdrome)actor.getOwner();
						if (pathairdrome.pointIndx((PAirdrome)actor) == 0)
							pathairdrome.destroy();
					}
					else
						actor.destroy();
				}
			}
			selectedActorsValidate();
			PlMission.setChanged();
			repaint();
		}
	}
	
	private void mis_copy(boolean bool)
	{
		if (!Plugin.builder.isFreeView())
		{
			mis_properties.clear();
			mis_cBoxes.clear();
			mis_clipLoc.clear();
			int i = 0;
			mis_clipP0.x = mis_clipP0.y = mis_clipP0.z = 0.0;
			Actor[] actors = selectedActors();
			for (int i_138_ = 0; i_138_ < actors.length; i_138_++)
			{
				Actor actor = actors[i_138_];
				if (actor == null)
					break;
				if (Actor.isValid(actor) && isMiltiSelected(actor))
				{
					Loc loc = new Loc();
					actor.pos.getAbs(loc);
					mis_clipLoc.add(loc);
					mis_clipP0.add(loc.getPoint());
					setSelected(actor);
					mis_cBoxes.add("" + wSelect.comboBox1.getSelected());
					mis_cBoxes.add("" + wSelect.comboBox2.getSelected());
					mis_properties.add(Plugin.mis_doGetProperties(actor));
					setSelected(null);
					i++;
				}
			}
			if (i > 1)
			{
				mis_clipP0.x /= (double)i;
				mis_clipP0.y /= (double)i;
				mis_clipP0.z /= (double)i;
			}
			if (bool)
				selectActorsClear();
			repaint();
		}
	}
	
	private void mis_paste()
	{
		if (!isFreeView())
		{
			selectActorsClear();
			int i = mis_properties.size();
			if (i != 0)
			{
				Point3d point3d = mouseWorldPos();
				Loc loc = new Loc();
				Point3d point3d_139_ = new Point3d();
				for (int i_140_ = 0; i_140_ < i; i_140_++)
				{
					Loc loc_141_ = (Loc)mis_clipLoc.get(i_140_);
					point3d_139_.sub(loc_141_.getPoint(), mis_clipP0);
					point3d_139_.add(point3d);
					loc.set(point3d_139_, loc_141_.getOrient());
					wSelect.comboBox1.setSelected(Integer.parseInt((String)mis_cBoxes.get(i_140_ * 2)), false, true);
					wSelect.comboBox2.setSelected(Integer.parseInt((String)mis_cBoxes.get(i_140_ * 2 + 1)), false, true);
					Actor actor = Plugin.mis_doInsert(loc, (String)mis_properties.get(i_140_));
					selectActorsAdd(actor);
					setSelected(null);
				}
				PlMission.setChanged();
				repaint();
			}
		}
	}
	
	public static final String getFullClassName(Actor actor)
	{
		return (actor instanceof SoftClass ? ((SoftClass)actor).fullClassName() : actor.getClass().getName());
	}
	
	public void doPopUpMenu()
	{
		if (mousePosX != -1)
		{
			if (popUpMenu == null)
				popUpMenu = ((GWindowMenuPopUp)viewWindow.create(new GWindowMenuPopUp()));
			else
			{
				if (popUpMenu.isVisible())
					return;
				popUpMenu.clearItems();
			}
			Point3d point3d = posScreenToLand(mousePosX, mousePosY, 0.0, 0.1);
			float f = viewWindow.win.dy - (float)mousePosY - 1.0F;
			if (Actor.isValid(selectedPoint()) || Actor.isValid(selectedActor()))
			{
				popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("&Unselect"), (Plugin.i18n("TIPUnselect")))
				{
					public void execute()
					{
						setSelected(null);
					}
				});
				popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("&Delete"), Plugin.i18n("TIPDelete"))
				{
					public void execute()
					{
						Builder.this.delete(true);
					}
				});
			}
			if (selectedActors.size() > 0)
			{
				popUpMenu.addItem(new GWindowMenuItem(popUpMenu, (Plugin.i18n("Unselect&All")), (Plugin.i18n("TIPUnselectAll")))
				{
					public void execute()
					{
						setSelected(null);
						selectedActors.clear();
					}
				});
				popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("Cut"), Plugin.i18n("TIPCut"))
				{
					public void execute()
					{
						Builder.this.mis_cut();
					}
				});
				popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("Copy"), Plugin.i18n("TIPCopy"))
				{
					public void execute()
					{
						Builder.this.mis_copy(true);
					}
				});
			}
			if (mis_properties.size() > 0)
				popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("Paste"), Plugin.i18n("TIPPaste"))
				{
					public void execute()
					{
						Builder.this.mis_paste();
					}
				});
			Plugin.doFillPopUpMenu(popUpMenu, point3d);
			if (popUpMenu.size() > 0)
			{
				popUpMenu.setPos((float)mousePosX, f);
				popUpMenu.showModal();
				movedActor = null;
			}
			else
				popUpMenu.hideWindow();
		}
	}
	
	public void setViewLand()
	{
		Main3D.cur3D().setDrawLand(conf.bShowLandscape);
		if (Main3D.cur3D().land2D != null)
			Main3D.cur3D().land2D.show(isView3D() ? false : conf.bShowLandscape);
	}
	
	public void changeViewLand()
	{
		conf.bShowLandscape = !conf.bShowLandscape;
		Main3D.cur3D().setDrawLand(conf.bShowLandscape);
		if (Main3D.cur3D().land2D != null)
			Main3D.cur3D().land2D.show(isView3D() ? false : conf.bShowLandscape);
		wViewLand.wShow.setChecked(conf.bShowLandscape, false);
		if (!isFreeView())
			repaint();
	}
	
	public void changeType(boolean bool, boolean bool_154_)
	{
		if (isLoadedLandscape() && mouseState == 0 && (Actor.isValid(selectedActor()) && selectedActor() != cursor))
		{
			Plugin.doChangeType(bool, bool_154_);
			if (!isFreeView())
				repaint();
		}
	}
	
	private void insert(boolean bool)
	{
		if (isLoadedLandscape() && mouseState == 0)
		{
			Loc loc = new Loc();
			if (isFreeView())
			{
				if (!Actor.isValid(actorView()))
					return;
				actorView().pos.getAbs(loc);
				if (actorView() != cursor)
					loc.add(new Point3d(1.0, 1.0, 0.0));
			}
			else
				loc.set(posScreenToLand(mousePosX, mousePosY, 0.0, 0.1), new Orient(defaultAzimut, 0.0F, 0.0F));
			Plugin.doInsert(loc, bool);
			if (!isFreeView())
				repaint();
		}
	}
	
	private void delete(boolean bool)
	{
		if (isLoadedLandscape() && mouseState == 0)
		{
			if (isFreeView())
			{
				if (Actor.isValid(selectedActor()) && selectedActor() != cursor && !(selectedActor() instanceof Bridge))
				{
					Loc loc = selectedActor().pos.getAbs();
					selectedActor().destroy();
					Plugin.doAfterDelete();
					PlMission.setChanged();
					Actor actor = null;
					if (bool)
						actor = selectNear(loc.getPoint());
					if (actor == null)
					{
						actor = cursor;
						cursor.pos.setAbs(loc);
						cursor.pos.reset();
						cursor.drawing(true);
					}
					setSelected(actor);
				}
			}
			else
			{
				if (Actor.isValid(selectedPoint()))
				{
					Path path = selectedPath();
					try
					{
						PPoint ppoint = path.selectPrev(pathes.currentPPoint);
						pathes.currentPPoint.destroy();
						Plugin.doAfterDelete();
						PlMission.setChanged();
						if (ppoint == null)
						{
							path.destroy();
							Plugin.doAfterDelete();
							setSelected(null);
						}
						else
							setSelected(bool ? ppoint : null);
					}
					catch (Exception exception)
					{
						/* empty */
					}
				}
				else
				{
					boolean bool_155_ = false;
					if (Actor.isValid(selectedActor()) && !selectedActors.containsKey(selectedActor()))
					{
						if (bMultiSelect && selectedActor() instanceof Bridge)
						{
							if (deletingMessageBox == null)
							{
								deletingActor = selectedActor();
								bDeletingChangeSelection = bool;
								deletingMessageBox = new GWindowMessageBox(clientWindow, 20.0F, true, "Confirm DELETE", "Delete Bridge ?", 1, 0.0F)
								{
									public void result(int i)
									{
										if (i == 3)
										{
											if (deletingActor == selectedActor())
												Builder.this.delete(bDeletingChangeSelection);
										}
										else
										{
											deletingMessageBox = null;
											deletingActor = null;
											setSelected(null);
										}
									}
								};
								return;
							}
							deletingMessageBox = null;
							PlMapLoad.bridgeActors.remove(selectedActor());
							selectedActor().destroy();
							deletingActor = null;
						}
						else
						{
							Plugin plugin = (Plugin)Property.value(selectedActor(), "builderPlugin");
							if (plugin != null)
								plugin.delete(selectedActor());
						}
						bool_155_ = true;
					}
					else
					{
						Actor[] actors = selectedActors();
						for (int i = 0; i < actors.length; i++)
						{
							Actor actor = actors[i];
							if (actor == null)
								break;
							if (Actor.isValid(actor))
							{
								Plugin plugin = ((Plugin)Property.value(actor, "builderPlugin"));
								plugin.delete(actor);
								bool_155_ = true;
							}
						}
					}
					if (bool_155_)
					{
						Plugin.doAfterDelete();
						PlMission.setChanged();
						selectedActorsValidate();
						if (bool)
							setSelected(selectNearFull(mousePosX, mousePosY));
						else
							setSelected(null);
					}
					else
					{
						Loc loc = new Loc();
						loc.set(posScreenToLand(mousePosX, mousePosY, 0.0, 0.1));
						Plugin.doDelete(loc);
					}
				}
				repaint();
			}
		}
	}
	
	private void stepAzimut(int i)
	{
		if (isLoadedLandscape())
		{
			if (Actor.isValid(selectedActor()))
			{
				if (!(selectedActor() instanceof Bridge))
				{
					Orient orient = new Orient();
					selectedActor().pos.getAbs(orient);
					orient.wrap();
					orient.set(orient.azimut() + (float)i, orient.tangage(), orient.kren());
					selectedActor().pos.setAbs(orient);
					defaultAzimut = orient.azimut();
					align(selectedActor());
					PlMission.setChanged();
					if (!isFreeView())
						repaint();
				}
			}
			else
			{
				if (bMultiSelect)
				{
					/* empty */
				}
				if (!isFreeView() && countSelectedActors() > 0)
				{
					Point3d point3d = posScreenToLand(mousePosX, mousePosY, 0.0, 0.1);
					Point3d point3d_160_ = new Point3d();
					Orient orient = new Orient();
					Actor[] actors = selectedActors();
					double d = Math.sin((double)i * 3.141592653589793 / 180.0);
					double d_161_ = Math.cos((double)i * 3.141592653589793 / 180.0);
					for (int i_162_ = 0; i_162_ < actors.length; i_162_++)
					{
						Actor actor = actors[i_162_];
						if (actor == null)
							break;
						if (Actor.isValid(actor))
						{
							Point3d point3d_163_ = actor.pos.getAbsPoint();
							point3d_160_.x = point3d_163_.x - point3d.x;
							point3d_160_.y = point3d_163_.y - point3d.y;
							if ((point3d_160_.x * point3d_160_.x + point3d_160_.y * point3d_160_.y) > 1.0E-6)
							{
								double d_164_ = (point3d_160_.x * d_161_ + point3d_160_.y * d);
								double d_165_ = (point3d_160_.y * d_161_ - point3d_160_.x * d);
								point3d_160_.x = d_164_ + point3d.x;
								point3d_160_.y = d_165_ + point3d.y;
							}
							point3d_160_.z = point3d_163_.z;
							if (bRotateObjects)
							{
								actor.pos.getAbs(orient);
								orient.wrap();
								orient.set(orient.azimut() + (float)i, orient.tangage(), orient.kren());
								actor.pos.setAbs(point3d_160_, orient);
							}
							else
								actor.pos.setAbs(point3d_160_);
							align(actor);
						}
					}
					repaint();
				}
			}
		}
	}
	
	public void tipErr(String string)
	{
		System.err.println(string);
		clientWindow.toolTip(string);
	}
	
	public void tip(String string)
	{
		clientWindow.toolTip(string);
	}
	
	protected void doMenu_FileExit()
	{
		if (Plugin.doExitBuilder())
			Main.stateStack().pop();
	}
	
	public void repaint()
	{
	/* empty */
	}
	
	public void enterRenders()
	{
		bView3D = false;
		Main3D.cur3D().renderMap2D.setShow(true);
		Main3D.cur3D().renderMap2D.useClearColor(true);
		Main3D.cur3D().render3D0.setShow(false);
		Main3D.cur3D().render3D1.setShow(false);
		Main3D.cur3D().render2D.setShow(false);
		_viewPort[2] = (int)viewWindow.win.dx;
		_viewPort[3] = (int)viewWindow.win.dy;
		GPoint gpoint = viewWindow.windowToGlobal(0.0F, 0.0F);
		_viewPort[0] = (int)gpoint.x + ((GUIWindowManager)viewWindow.root.manager).render.getViewPortX0();
		_viewPort[1] = ((int)(viewWindow.root.win.dy - gpoint.y - viewWindow.win.dy) + ((GUIWindowManager)viewWindow.root.manager).render.getViewPortY0());
		Main3D.cur3D().renderMap2D.setViewPort(_viewPort);
		Main3D.cur3D().render3D0.setViewPort(_viewPort);
		Main3D.cur3D().render3D1.setViewPort(_viewPort);
		camera2D.set(0.0F, (float)_viewPort[2], 0.0F, (float)_viewPort[3]);
		Render render = (Render)Actor.getByName("renderConsoleGL0");
		if (render != null)
		{
			CameraOrtho2D cameraortho2d = (CameraOrtho2D)render.getCamera();
			render.setViewPort(_viewPort);
			cameraortho2d.set(0.0F, (float)_viewPort[2], 0.0F, (float)_viewPort[3]);
		}
		render = (Render)Actor.getByName("renderTextScr");
		if (render != null)
		{
			CameraOrtho2D cameraortho2d = (CameraOrtho2D)render.getCamera();
			render.setViewPort(_viewPort);
			cameraortho2d.set(0.0F, (float)_viewPort[2], 0.0F, (float)_viewPort[3]);
		}
	}
	
	public void leaveRenders()
	{
		Main3D.cur3D().renderMap2D.setShow(false);
		Main3D.cur3D().renderMap2D.useClearColor(false);
		Main3D.cur3D().render3D0.setShow(true);
		Main3D.cur3D().render3D1.setShow(true);
		Main3D.cur3D().render2D.setShow(true);
		leaveRender(Main3D.cur3D().renderMap2D);
		leaveRender(Main3D.cur3D().render3D0);
		leaveRender(Main3D.cur3D().render3D1);
		leaveRender(Main3D.cur3D().render2D);
		leaveRender((Render)Actor.getByName("renderConsoleGL0"));
		leaveRender((Render)Actor.getByName("renderTextScr"));
	}
	
	private void leaveRender(Render render)
	{
		if (render != null)
			render.contextResized();
	}
	
	public void mapLoaded()
	{
		enterRenders();
		if (!isLoadedLandscape())
		{
			bView3D = false;
			Main3D.cur3D().renderMap2D.setShow(true);
			Main3D.cur3D().render3D0.setShow(false);
			Main3D.cur3D().render3D1.setShow(false);
			Main3D.cur3D().render2D.setShow(false);
			mapXscrollBar.setRange(0.0F, 0.0F, 1.0F, 1.0F, 0.0F);
			mapYscrollBar.setRange(0.0F, 0.0F, 1.0F, 1.0F, 0.0F);
			mapZSlider.setRange(0, 2, 0);
		}
		else
		{
			computeViewMap2D(-1.0, 0.0, 0.0);
			if (Main3D.cur3D().land2D != null)
				Main3D.cur3D().land2D.show(conf.bShowLandscape);
		}
	}
	
	public void enter()
	{
		Main3D.cur3D().resetGame();
		saveMaxVisualDistance = World.MaxVisualDistance;
		saveMaxStaticVisualDistance = World.MaxStaticVisualDistance;
		World.MaxVisualDistance = MaxVisualDistance;
		World.MaxStaticVisualDistance = MaxVisualDistance;
		enterRenders();
		setViewLand();
		Main3D.cur3D().camera3D.dreamFire(true);
		Main3D.cur3D().hookView.use(true);
		Main3D.cur3D().hookView.reset();
		Main3D.cur3D().bEnableFog = false;
		Runaway.bDrawing = bMultiSelect;
		camera.interpPut(new InterpolateOnLand(), "onLand", Time.currentReal(), null);
		viewWindow.mouseCursor = 1;
		pathes = new Pathes();
		PlMission.doMissionReload();
		cursor = new CursorMesh("3do/primitive/coord/mono.sim");
	}
	
	public void leave()
	{
		camera.interpEnd("onLand");
		PlMapLoad plmapload = (PlMapLoad)Plugin.getPlugin("MapLoad");
		plmapload.mapUnload();
		mouseState = 0;
		setSelected(null);
		if (wSelect.isVisible())
			wSelect.hideWindow();
		if (wViewLand.isVisible())
			wViewLand.hideWindow();
		if (bMultiSelect)
		{
			/* empty */
		}
		if (wSnap.isVisible())
			wSnap.hideWindow();
		Plugin.doFreeResources();
		leaveRenders();
		Main3D.cur3D().bEnableFog = true;
		Main3D.cur3D().camera3D.dreamFire(false);
		Runaway.bDrawing = false;
		pathes.destroy();
		pathes = null;
		cursor.destroy();
		cursor = null;
		mouseXYZATK.resetGame();
		Main3D.cur3D().resetGame();
		conf.save();
		World.MaxVisualDistance = saveMaxVisualDistance;
		World.MaxStaticVisualDistance = saveMaxVisualDistance;
	}
	
	public Builder(GWindowRootMenu gwindowrootmenu, String string)
	{
		envName = string;
		((GUIWindowManager)gwindowrootmenu.manager).setUseGMeshs(true);
		mis_properties = new ArrayList();
		mis_cBoxes = new ArrayList();
		mis_clipLoc = new ArrayList();
		mis_clipP0 = new Point3d();
		camera = (Camera3D)Actor.getByName("camera");
		camera2D = (CameraOrtho2D)Actor.getByName("cameraMap2D");
		mouseXYZATK = new MouseXYZATK("MouseXYZ");
		mouseXYZATK.setCamera(camera);
		conf = new BldConfig();
		conf.load(new SectFile("bldconf.ini", 1), "builder config");
		Plugin.loadAll(new SectFile("bldconf.ini", 0), PLUGINS_SECTION, this);
		gwindowrootmenu.clientWindow = gwindowrootmenu.create(clientWindow = new ClientWindow());
		mapXscrollBar = new XScrollBar(clientWindow);
		mapYscrollBar = new YScrollBar(clientWindow);
		mapZSlider = new ZSlider(clientWindow);
		mapXscrollBar.setRange(0.0F, 0.0F, 1.0F, 1.0F, 0.0F);
		mapYscrollBar.setRange(0.0F, 0.0F, 1.0F, 1.0F, 0.0F);
		mapZSlider.setRange(0, 2, 0);
		mapZSlider.bSlidingNotify = true;
		clientWindow.create(viewWindow = new ViewWindow());
		clientWindow.resized();
		gwindowrootmenu.statusBar.defaultHelp = null;
		mFile = (gwindowrootmenu.menuBar.addItem(Plugin.i18n("&File"), Plugin.i18n("Load/SaveMissionFiles")));
		mFile.subMenu = (GWindowMenu)mFile.create(new GWindowMenu());
		mFile.subMenu.close(false);
		mFile.subMenu.addItem("-", null);
		mFile.subMenu.addItem(new GWindowMenuItem(mFile.subMenu, Plugin.i18n("&Exit"), Plugin.i18n("ExitBuilder"))
		{
			public void execute()
			{
				doMenu_FileExit();
			}
		});
		mEdit = gwindowrootmenu.menuBar.addItem(Plugin.i18n("&Edit"), Plugin.i18n("TIPEdit"));
		mEdit.subMenu = (GWindowMenu)mEdit.create(new GWindowMenu());
		mEdit.subMenu.close(false);
		if (bMultiSelect)
			mEdit.subMenu.addItem(new GWindowMenuItem(mEdit.subMenu, Plugin.i18n("&Select_All"), null)
			{
				public void execute()
				{
					Plugin.doSelectAll();
				}
			});
		if (bMultiSelect)
		{
			/* empty */
		}
		mEdit.subMenu.addItem(new GWindowMenuItem(mEdit.subMenu, Plugin.i18n("&Unselect_All"), null)
		{
			public void execute()
			{
				setSelected(null);
				selectedActors.clear();
			}
		});
		GWindowMenuItem gwindowmenuitem = mEdit.subMenu.addItem(new GWindowMenuItem(mEdit.subMenu, Plugin.i18n("&Enable_Select"), null)
		{
			public void execute()
			{
				setSelected(null);
				selectedActors.clear();
				conf.bEnableSelect = !conf.bEnableSelect;
				bChecked = conf.bEnableSelect;
			}
		});
		gwindowmenuitem.bChecked = conf.bEnableSelect;
		mEdit.subMenu.addItem("-", null);
		mEdit.subMenu.addItem(new GWindowMenuItem(mEdit.subMenu, Plugin.i18n("&DeleteAll"), Plugin.i18n("TIPDeleteAll"))
		{
			public void execute()
			{
				deleteAll();
			}
		});
		if (bMultiSelect)
		{
			/* empty */
		}
		mEdit.subMenu.addItem("-", null);
		gwindowmenuitem = mEdit.subMenu.addItem(new GWindowMenuItem((mEdit.subMenu), (Plugin.i18n("&Rotate_Objects")), null)
		{
			public void execute()
			{
				bRotateObjects = !bRotateObjects;
				bChecked = bRotateObjects;
			}
		});
		gwindowmenuitem.bChecked = bRotateObjects;
		mConfigure = gwindowrootmenu.menuBar.addItem(Plugin.i18n("&Configure"), Plugin.i18n("TIPConfigure"));
		mConfigure.subMenu = (GWindowMenu)mConfigure.create(new GWindowMenu());
		mConfigure.subMenu.close(false);
		mView = gwindowrootmenu.menuBar.addItem(Plugin.i18n("&View"), Plugin.i18n("TIPView"));
		mView.subMenu = (GWindowMenu)mView.create(new GWindowMenu());
		mView.subMenu.close(false);
		mSelectItem = mView.subMenu.addItem(new GWindowMenuItem(mView.subMenu, (Plugin.i18n("&Object")), (Plugin.i18n("TIPObject")))
		{
			public void execute()
			{
				if (wSelect.isVisible())
					wSelect.hideWindow();
				else
					wSelect.showWindow();
			}
		});
		mViewLand = mView.subMenu.addItem(new GWindowMenuItem(mView.subMenu, (Plugin.i18n("&Landscape")), (Plugin.i18n("TIPLandscape")))
		{
			public void execute()
			{
				if (wViewLand.isVisible())
					wViewLand.hideWindow();
				else
					wViewLand.showWindow();
			}
		});
		if (bMultiSelect)
		{
			/* empty */
		}
		mSnap = mView.subMenu.addItem(new GWindowMenuItem(mView.subMenu, Plugin.i18n("&Snap"), null)
		{
			public void execute()
			{
				if (wSnap.isVisible())
					wSnap.hideWindow();
				else
					wSnap.showWindow();
			}
		});
		mView.subMenu.addItem("-", null);
		mDisplayFilter = (mView.subMenu.addItem(new GWindowMenuItem(mView.subMenu, Plugin.i18n("&DisplayFilter"), Plugin.i18n("TIPDisplayFilter"))));
		mDisplayFilter.subMenu = (GWindowMenu)mDisplayFilter.create(new GWindowMenu());
		mDisplayFilter.subMenu.close(false);
		mView.subMenu.addItem("-", null);
		gwindowmenuitem = (mView.subMenu.addItem(new GWindowMenuItem(mView.subMenu, Plugin.i18n("&IconSize"), Plugin.i18n("TIPIconSize"))));
		gwindowmenuitem.subMenu = (GWindowMenu)gwindowmenuitem.create(new GWindowMenu());
		gwindowmenuitem.subMenu.close(false);
		mIcon8 = gwindowmenuitem.subMenu.addItem(new GWindowMenuItem((gwindowmenuitem.subMenu), "&8", null)
		{
			public void execute()
			{
				conf.iconSize = 8;
				IconDraw.setScrSize(conf.iconSize, conf.iconSize);
				mIcon8.bChecked = true;
				mIcon16.bChecked = mIcon32.bChecked = mIcon64.bChecked = false;
			}
		});
		mIcon16 = gwindowmenuitem.subMenu.addItem(new GWindowMenuItem((gwindowmenuitem.subMenu), "&16", null)
		{
			public void execute()
			{
				conf.iconSize = 16;
				IconDraw.setScrSize(conf.iconSize, conf.iconSize);
				mIcon16.bChecked = true;
				mIcon8.bChecked = mIcon32.bChecked = mIcon64.bChecked = false;
			}
		});
		mIcon32 = gwindowmenuitem.subMenu.addItem(new GWindowMenuItem((gwindowmenuitem.subMenu), "&32", null)
		{
			public void execute()
			{
				conf.iconSize = 32;
				IconDraw.setScrSize(conf.iconSize, conf.iconSize);
				mIcon32.bChecked = true;
				mIcon8.bChecked = mIcon16.bChecked = mIcon64.bChecked = false;
			}
		});
		mIcon64 = gwindowmenuitem.subMenu.addItem(new GWindowMenuItem((gwindowmenuitem.subMenu), "&64", null)
		{
			public void execute()
			{
				conf.iconSize = 64;
				IconDraw.setScrSize(conf.iconSize, conf.iconSize);
				mIcon64.bChecked = true;
				mIcon8.bChecked = mIcon16.bChecked = mIcon32.bChecked = false;
			}
		});
		switch (conf.iconSize)
		{
			case 8 :
				mIcon8.bChecked = true;
				break;
			case 16 :
				mIcon16.bChecked = true;
				break;
			case 32 :
				mIcon32.bChecked = true;
				break;
			case 64 :
				mIcon64.bChecked = true;
				break;
			default :
				conf.iconSize = 16;
				mIcon16.bChecked = true;
		}
		IconDraw.setScrSize(conf.iconSize, conf.iconSize);
		gwindowmenuitem = mView.subMenu.addItem(new GWindowMenuItem((mView.subMenu), (Plugin.i18n("Save&ViewHLand")), (Plugin.i18n("TIPSaveViewHLand")))
		{
			public void execute()
			{
				conf.bSaveViewHLand = !conf.bSaveViewHLand;
				bChecked = conf.bSaveViewHLand;
			}
		});
		gwindowmenuitem.bChecked = conf.bSaveViewHLand;
		gwindowmenuitem = mView.subMenu.addItem(new GWindowMenuItem((mView.subMenu), (Plugin.i18n("Show&Grid")), (Plugin.i18n("TIPShowGrid")))
		{
			public void execute()
			{
				conf.bShowGrid = !conf.bShowGrid;
				bChecked = conf.bShowGrid;
			}
		});
		gwindowmenuitem.bChecked = conf.bShowGrid;
		gwindowmenuitem = mView.subMenu.addItem(new GWindowMenuItem((mView.subMenu), (Plugin.i18n("&AnimateCamera")), (Plugin.i18n("TIPAnimateCamera")))
		{
			public void execute()
			{
				conf.bAnimateCamera = !conf.bAnimateCamera;
				bChecked = conf.bAnimateCamera;
			}
		});
		gwindowmenuitem.bChecked = conf.bAnimateCamera;
		wSelect = new WSelect(this, clientWindow)
		{
			//TODO: Added by |ZUTI|: tabs sizing based on tab name
			//----------------------------------------------------
			public void doRender(boolean arg0)
			{
				Tab tab = this.tabsClient.getTab(this.tabsClient.current);
				if( tab != null && !Builder.zutiLastTabCaption.equals(tab.cap.caption) )
				{
					if( tab.cap.caption.equals( Plugin.i18n("BornPlaceActor")) )
					{
						Plugin.builder.wSelect.setMetricSize(40, 37);
					}
					else if( tab.cap.caption.equals( Plugin.i18n("bplace_aircraft")) )
					{
						Plugin.builder.wSelect.setMetricSize(40, 30);
					}
					else if( tab.cap.caption.equals( Plugin.i18n("mds.tabSpawn")) )
					{
						Plugin.builder.wSelect.setMetricSize(40, 40);
					}
					else if( tab.cap.caption.equals( Plugin.i18n("mds.tabCapturing")) )
					{
						Plugin.builder.wSelect.setMetricSize(40, 24);
					}
					else if( tab.cap.caption.equals( Plugin.i18n("mds.tabRRR")) )
					{
						Plugin.builder.wSelect.setMetricSize(40, 45);
					}
					else if( tab.cap.caption.equals(Plugin.i18n("Type")) )
					{
						Plugin.builder.wSelect.setMetricSize(20, 25);
					}
					else if( tab.cap.caption.equals(Plugin.i18n("tTarget")) )
					{
						Plugin.builder.wSelect.setMetricSize(36, 29);
					}
					
					Builder.zutiLastTabCaption = tab.cap.caption;
				}
				super.doRender(arg0);
			}
			//----------------------------------------------------
		};
		wViewLand = new WViewLand(this, clientWindow);
		wSnap = new WSnap(this, clientWindow);
		
		//TODO: Added by |ZUTI|
		//----------------------------------------------------------------------------------
		mZutiMds = gwindowrootmenu.menuBar.addItem(Plugin.i18n("mds.menu"), Plugin.i18n("Edit MDS properties"));
		mZutiMds.subMenu = (GWindowMenu)mZutiMds.create(new GWindowMenu());
		mZutiMds.subMenu.close(false);
		//----------------------------------------------------------------------------------
		
		Plugin.doCreateGUI();
		_gridFont = TTFont.font[1];
		smallFont = TTFont.font[0];
		emptyMat = Mat.New("icons/empty.mat");
		selectTargetMat = Mat.New("icons/selecttarget.mat");
		initHotKeys();
		Plugin.doStart();
		HotKeyCmdEnv.enable(envName, false);
		HotKeyCmdEnv.enable("MouseXYZ", false);
	}
	
	//TODO: Added by |ZUTI|
	//---------------------------------------
	public GWindowMenuBarItem mZutiMds;
	private static String zutiLastTabCaption = "";
	//---------------------------------------
}
