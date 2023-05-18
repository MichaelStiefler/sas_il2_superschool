package com.maddox.il2.objects.air;

import java.util.ArrayList;
import java.util.Map;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Matrix4d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorDraw;
import com.maddox.il2.engine.ActorPosMove;
import com.maddox.il2.engine.Camera3D;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookRender;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Mesh;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Render;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.vehicles.radios.Beacon;
import com.maddox.il2.objects.vehicles.radios.BeaconGeneric;
import com.maddox.il2.objects.vehicles.radios.BlindLandingData;
import com.maddox.il2.objects.vehicles.radios.TypeHasHayRake;
import com.maddox.il2.objects.vehicles.radios.TypeHasYGBeacon;
import com.maddox.opengl.gl;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.Acoustics;
import com.maddox.sound.AudioDevice;
import com.maddox.sound.CmdMusic;
import com.maddox.sound.SoundFX;
import com.maddox.sound.SoundPreset;

public abstract class Cockpit extends Actor
{
	public FlightModel				fm									= null;
	public boolean					cockpitDimControl					= false;
	public boolean					cockpitLightControl					= false;
	protected String[]				cockpitNightMats					= null;
	protected static float[]		ypr									= { 0.0F, 0.0F, 0.0F };
	protected static float[]		xyz									= { 0.0F, 0.0F, 0.0F };
	protected int					_astatePilotIndx					= -1;
	public static final int			SNDCLK_NULL							= 0;
	public static final int			SNDCLK_BUTTONDEPRESSED1				= 1;
	public static final int			SNDCLK_BUTTONDEPRESSED2				= 2;
	public static final int			SNDCLK_BUTTONDEPRESSED3				= 3;
	public static final int			SNDCLK_TUMBLERFLIPPED1				= 4;
	public static final int			SNDCLK_TUMBLERFLIPPED2				= 5;
	public static final int			SNDCLK_TUMBLERFLIPPED3				= 6;
	public static final int			SNDINF_RUSTYWHEELTURNING1			= 7;
	public static final int			SNDINF_OILEDMETALWHEELWITHCHAIN1	= 8;
	public static final int			SNDCLK_SMALLLEVERSLIDES1			= 9;
	public static final int			SNDCLK_SMALLLEVERSLIDES2			= 10;
	public static final int			SNDINF_LEVERSLIDES1					= 11;
	public static final int			SNDINF_LEVERSLIDES2					= 12;
	public static final int			SNDCLK_RUSTYLEVERSLIDES1			= 13;
	public static final int			SNDCLK_SMALLVALVE1					= 14;
	public static final int			SNDINF_SMALLVALVEWITHGASLEAK1		= 15;
	public static final int			SNDINF_WORMGEARTURNS1				= 16;
	public static final int			SNDINF_BUZZER_109					= 17;
	public static final int			SND_COUNT							= 18;
	protected static SoundPreset	sfxPreset							= null;
	protected SoundFX[]				sounds								= null;
	protected Point3d				sfxPos								= new Point3d(1.0, 0.0, 0.0);
	private float					prevWaypointF						= 0.0F;
	private boolean					skip								= false;
	private Vector3d				distanceV							= new Vector3d();
	private float					ndBeaconRange						= 1.0F;
	private float					ndBeaconDirection					= 0.0F;
	private BlindLandingData		blData								= new BlindLandingData();
	private Point3d					acPoint								= new Point3d();
	private Point3d					tempPoint1							= new Point3d();
	private Point3d					tempPoint2							= new Point3d();
	private static float			atmosphereInterference				= 0.0F;
	private static Point3d			lightningPoint						= new Point3d();
	private static boolean			lightningStriked					= false;
	public static final float		radioCompassUpdateF					= 0.02F;
	private static float			terrainAndNightError				= 0.0F;
	private static final double		glideScopeInRads					= Math.toRadians(3.0);
	private int						morseCharsPlayed					= 0;
	private boolean					clearToPlay							= true;
	private static final float[]	volumeLogScale						= { 0.0F, 0.301F, 0.477F, 0.602F, 0.698F, 0.778F, 0.845F, 0.903F, 0.954F, 1.0F };
	private boolean					bNullShow							= false;
	private boolean					bEnableRendering					= true;
	private boolean					bEnableRenderingBall				= true;
	private boolean					bFocus								= false;
	private int[]					world_renderwin						= new int[4];
	private int[]					cockpit_renderwin					= new int[4];
	public HierMesh					mesh;
	private HierMesh				nullMesh;
	private Mesh					nullMeshIL2;
	private Draw					drawMesh							= new Draw();
	private NullDraw				drawNullMesh						= new NullDraw();
	private boolean					bExistMirror						= false;
	private Mat						mirrorMat;
	private Loc						__l									= new Loc();
	private Point3d					__p									= new Point3d();
	private Orient					__o									= new Orient();
	public static Aircraft			_newAircraft						= null;
	private static Point3d			nullP								= new Point3d();
	protected static Point3d		P1									= new Point3d();
	protected static Vector3d		V									= new Vector3d();

	public static class HookOnlyOrient extends Hook
	{
		public void computePos(Actor actor, Loc loc, Loc loc_0_)
		{
			loc_0_.set(Cockpit.nullP, loc.getOrient());
		}
	}

	class NullDraw extends ActorDraw
	{
		private int	iPreRender	= 0;

		public void preRender()
		{
			if (Actor.isValid(pos.base()) && nullMesh != null && nullMeshIL2 != null)
			{
				pos.getRender(__l);
				Main3D.cur3D().cameraCockpit.pos.getRender(__p);
				__l.set(__p);
				if (bEnableRenderingBall)
					nullMesh.setPos(__l);
				else
					nullMeshIL2.setPos(__l);
				if (aircraft() != null)
					nullMesh.chunkSetAngles("Ball", aircraft().FM.getAOS(), -aircraft().FM.getAOA(), 0.0F);
				if (bEnableRenderingBall)
					iPreRender = nullMesh.preRender();
				else
					iPreRender = nullMeshIL2.preRender();
			}
		}

		public void render()
		{
			if (iPreRender != 0 && Actor.isValid(pos.base()) && nullMesh != null && nullMeshIL2 != null)
			{
				if (bEnableRenderingBall)
					nullMesh.render();
				else
					nullMeshIL2.render();
			}
		}
	}

	class Draw extends ActorDraw
	{
		protected double[]	_modelMatrix3D			= new double[16];
		protected double[]	_modelMatrix3DMirror	= new double[16];
		protected double[]	_projMatrix3DMirror		= new double[16];
		private int			iPreRender				= 0;

		public void preRender()
		{
			if (Actor.isValid(pos.base()) && mesh != null)
			{
				pos.getRender(__l);
				mesh.setPos(__l);
				Cockpit.this.updateBeacons();
				reflectWorldToInstruments(Time.tickOffset());
				iPreRender = mesh.preRender();
			}
		}

		public void render(boolean bool)
		{
			if (iPreRender != 0 && Actor.isValid(pos.base()) && mesh != null)
			{
				Render.currentCamera().activateWorldMode(0);
				gl.GetDoublev(2982, bool ? _modelMatrix3DMirror : _modelMatrix3D);
				if (bool)
					gl.GetDoublev(2983, _projMatrix3DMirror);
				Render.currentCamera().deactivateWorldMode();
				pos.getRender(__l);
				lightUpdate(__l, false);
				pos.base().pos.getRender(__p, __o);
				LightPoint.setOffset((float) __p.x, (float) __p.y, (float) __p.z);
				pos.base().pos.getRender(__l);
				pos.base().draw.lightUpdate(__l, false);
				Render.currentLightEnv().prepareForRender(__p, mesh.visibilityR());
				if (bool)
				{
					if (!bExistMirror)
						System.out.println("*** Internal error: mirr exist");
					if (!Main3D.cur3D().isViewMirror())
						System.out.println("*** Internal error: mirr isview");
					String string = nameOfActiveMirrorSurfaceChunk();
					String string_1_ = nameOfActiveMirrorBaseChunk();
					if (string != null)
					{
						mesh.setCurChunk(string);
						mesh.chunkVisible(false);
					}
					if (string_1_ != null)
					{
						mesh.setCurChunk(string_1_);
						mesh.chunkVisible(true);
					}
					mesh.render();
				}
				else if (!bExistMirror)
					mesh.render();
				else
				{
					boolean bool_2_ = Main3D.cur3D().isViewMirror();
					String string = nameOfActiveMirrorSurfaceChunk();
					String string_3_ = nameOfActiveMirrorBaseChunk();
					if (bool_2_)
					{
						if (string != null)
						{
							mesh.setCurChunk(string);
							mesh.chunkVisible(true);
							mesh.renderChunkMirror(_modelMatrix3D, _modelMatrix3DMirror, _projMatrix3DMirror);
							mesh.chunkVisible(false);
						}
						if (string_3_ != null)
						{
							mesh.setCurChunk(string_3_);
							mesh.chunkVisible(true);
						}
						mesh.render();
					}
					else
					{
						if (string != null)
						{
							mesh.setCurChunk(string);
							mesh.chunkVisible(false);
						}
						if (string_3_ != null)
						{
							mesh.setCurChunk(string_3_);
							mesh.chunkVisible(false);
						}
						mesh.render();
					}
				}
			}
		}
	}

	static class HookCamera3DMirror extends HookRender
	{
		Matrix4d	cam2w		= new Matrix4d();
		Matrix4d	mir2w		= new Matrix4d();
		Matrix4d	cam2mir		= new Matrix4d();
		Point3d		p			= new Point3d();
		Vector3d	X			= new Vector3d();
		Vector3d	Y			= new Vector3d();
		Vector3d	Z			= new Vector3d();
		Matrix4d	cmm2w		= new Matrix4d();
		Matrix4d	cmm2mir		= new Matrix4d();
		Matrix4d	mir2cmm		= new Matrix4d();
		double[]	Eul			= new double[3];
		Point3f		mirLowP		= new Point3f();
		Point3f		mirHigP		= new Point3f();
		float		resultNearClipDepth;
		Loc			mir2w_loc	= new Loc();
		Loc			aLoc		= new Loc();
		boolean		bInCockpit;

		private float computeMirroredCamera(Loc loc, Loc loc_4_, Point3f point3f, Point3f point3f_5_, int i, int i_6_, float f, Loc loc_7_, int[] is)
		{
			loc.getMatrix(cam2w);
			loc_4_.getMatrix(mir2w);
			cam2mir.set(mir2w);
			cam2mir.invert();
			cam2mir.mul(cam2mir, cam2w);
			cmm2mir.setIdentity();
			cmm2mir.m00 = 0.0;
			cmm2mir.m10 = 0.0;
			cmm2mir.m20 = 1.0;
			cmm2mir.m01 = 1.0;
			cmm2mir.m11 = 0.0;
			cmm2mir.m21 = 0.0;
			cmm2mir.m02 = 0.0;
			cmm2mir.m12 = 1.0;
			cmm2mir.m22 = 0.0;
			cmm2mir.m03 = cam2mir.m03;
			cmm2mir.m13 = cam2mir.m13;
			cmm2mir.m23 = -cam2mir.m23;
			cmm2mir.m03 *= 0.45;
			cmm2mir.m13 *= 0.45;
			cmm2mir.m23 *= 0.45;
			cmm2w.mul(mir2w, cmm2mir);
			mir2cmm.set(cmm2mir);
			mir2cmm.invert();
			float f_8_ = (float) -cmm2mir.m23;
			if (f_8_ <= 0.0010F)
				return -1.0F;
			float f_10_;
			float f_11_;
			float f_12_;
			float f_9_ = f_10_ = f_11_ = f_12_ = 0.0F;
			for (int i_13_ = 0; i_13_ < 8; i_13_++)
			{
				switch (i_13_)
				{
					case 0:
						p.set((double) point3f.x, (double) point3f.y, (double) point3f.z);
					break;
					case 1:
						p.set((double) point3f_5_.x, (double) point3f.y, (double) point3f.z);
					break;
					case 2:
						p.set((double) point3f.x, (double) point3f_5_.y, (double) point3f.z);
					break;
					case 3:
						p.set((double) point3f.x, (double) point3f.y, (double) point3f_5_.z);
					break;
					case 4:
						p.set((double) point3f_5_.x, (double) point3f_5_.y, (double) point3f_5_.z);
					break;
					case 5:
						p.set((double) point3f.x, (double) point3f_5_.y, (double) point3f_5_.z);
					break;
					case 6:
						p.set((double) point3f_5_.x, (double) point3f.y, (double) point3f_5_.z);
					break;
					case 7:
						p.set((double) point3f_5_.x, (double) point3f_5_.y, (double) point3f.z);
					break;
				}
				mir2cmm.transform(p);
				float f_14_ = -(float) ((double) f_8_ * p.y / p.x);
				float f_15_ = (float) ((double) f_8_ * p.z / p.x);
				if (i_13_ == 0)
				{
					f_9_ = f_10_ = f_14_;
					f_11_ = f_12_ = f_15_;
				}
				else
				{
					if (f_14_ < f_9_)
						f_9_ = f_14_;
					if (f_14_ > f_10_)
						f_10_ = f_14_;
					if (f_15_ < f_11_)
						f_11_ = f_15_;
					if (f_15_ > f_12_)
						f_12_ = f_15_;
				}
			}
			float f_16_ = f_10_ - f_9_;
			float f_17_ = f_12_ - f_11_;
			if (f_16_ <= 0.0010F || f_17_ <= 0.0010F)
				return -1.0F;
			f_9_ -= 0.0010F;
			f_10_ += 0.0010F;
			f_11_ -= 0.0010F;
			f_12_ += 0.0010F;
			f_16_ = f_10_ - f_9_;
			f_17_ = f_12_ - f_11_;
			float f_18_ = f_16_ / (float) i;
			float f_19_ = f_18_ * f;
			float f_20_ = f_19_ * (float) i_6_;
			if (f_20_ > f_17_)
			{
				f_17_ = f_20_;
				f_11_ = (f_11_ + f_12_) * 0.5F - f_17_ * 0.5F;
				f_12_ = f_11_ + f_17_;
			}
			else
			{
				f_16_ *= f_17_ / f_20_;
				f_9_ = (f_9_ + f_10_) * 0.5F - f_16_ * 0.5F;
				f_10_ = f_9_ + f_16_;
			}
			float f_21_ = 2.0F * Math.max(Math.abs(f_9_), Math.abs(f_10_));
			float f_22_ = 2.0F * Math.max(Math.abs(f_11_), Math.abs(f_12_));
			int i_23_ = (int) (0.5F + (float) i * f_21_ / f_16_);
			int i_24_ = (int) (0.5F + (float) i_6_ * f_22_ / f_17_);
			float f_25_ = (2.0F * Geom.RAD2DEG((float) Math.atan2((double) (f_21_ * 0.5F), (double) f_8_)));
			int i_26_ = (int) (-((f_9_ - -f_21_ / 2.0F) / f_21_) * (float) i_23_);
			int i_27_ = (int) (-((f_11_ - -f_22_ / 2.0F) / f_22_) * (float) i_24_);
			cmm2w.getEulers(Eul);
			Eul[0] *= -57.29577791868205;
			Eul[1] *= -57.29577791868205;
			Eul[2] *= 57.29577791868205;
			loc_7_.set(cmm2w.m03, cmm2w.m13, cmm2w.m23, (float) Eul[0], (float) Eul[1], (float) Eul[2]);
			is[0] = i_26_;
			is[1] = i_27_;
			is[2] = i_23_;
			is[3] = i_24_;
			resultNearClipDepth = f_8_;
			return f_25_;
		}

		public boolean computeRenderPos(Actor actor, Loc loc, Loc loc_28_)
		{
			computePos(actor, loc, loc_28_, true);
			return true;
		}

		public void computePos(Actor actor, Loc loc, Loc loc_29_)
		{
			computePos(actor, loc, loc_29_, false);
		}

		public void computePos(Actor actor, Loc loc, Loc loc_30_, boolean bool)
		{
			if (Actor.isValid(Main3D.cur3D().cockpitCur) && Main3D.cur3D().cockpitCur.bExistMirror && Main3D.cur3D().isViewMirror() && Main3D.cur3D().cockpitCur.isFocused()
					&& Actor.isValid(World.getPlayerAircraft()))
			{
				Loc loc_31_ = loc;
				Main3D.cur3D().cockpitCur.mesh.setCurChunk(Main3D.cur3D().cockpitCur.nameOfActiveMirrorSurfaceChunk());
				Main3D.cur3D().cockpitCur.mesh.getChunkLocObj(mir2w_loc);
				if (bool)
				{
					if (bInCockpit)
						Main3D.cur3D().cockpitCur.pos.getRender(aLoc);
					else
						World.getPlayerAircraft().pos.getRender(aLoc);
				}
				else if (bInCockpit)
					Main3D.cur3D().cockpitCur.pos.getAbs(aLoc);
				else
					World.getPlayerAircraft().pos.getAbs(aLoc);
				mir2w_loc.add(mir2w_loc, aLoc);
				Main3D.cur3D().cockpitCur.mesh.getChunkCurVisBoundBox(mirLowP, mirHigP);
				float f = (computeMirroredCamera(loc_31_, mir2w_loc, mirLowP, mirHigP, Main3D.cur3D().mirrorWidth(), Main3D.cur3D().mirrorHeight(), 1.0F, loc_30_,
						(bInCockpit ? Main3D.cur3D().cockpitCur.cockpit_renderwin : Main3D.cur3D().cockpitCur.world_renderwin)));
				if (bool)
				{
					if (bInCockpit)
					{
						Main3D.cur3D().cameraCockpitMirror.set(f);
						Main3D.cur3D().cameraCockpitMirror.ZNear = resultNearClipDepth;
					}
					else
					{
						Main3D.cur3D().camera3DMirror.set(f);
						Main3D.cur3D().camera3DMirror.ZNear = resultNearClipDepth;
					}
				}
			}
		}

		public HookCamera3DMirror(boolean bool)
		{
			bInCockpit = bool;
		}
	}

	public static class Camera3DMirror extends Camera3D
	{
		public boolean activate(float f, int i, int i_32_, int i_33_, int i_34_, int i_35_, int i_36_, int i_37_, int i_38_, int i_39_, int i_40_)
		{
			if (!Actor.isValid(Main3D.cur3D().cockpitCur))
				return super.activate(f, i, i_32_, i_33_, i_34_, i_35_, i_36_, i_37_, i_38_, i_39_, i_40_);
			pos.getRender(_tmpLoc);
			int[] is = Main3D.cur3D().cockpitCur.world_renderwin;
			if (this == Main3D.cur3D().cameraCockpitMirror)
				is = Main3D.cur3D().cockpitCur.cockpit_renderwin;
			return super.activate(f, i, i_32_, Main3D.cur3D().mirrorX0() + is[0], Main3D.cur3D().mirrorY0() + is[1], is[2], is[3], Main3D.cur3D().mirrorX0(), Main3D.cur3D().mirrorY0(), Main3D.cur3D()
					.mirrorWidth(), Main3D.cur3D().mirrorHeight());
		}
	}

	protected void resetYPRmodifier()
	{
		ypr[0] = ypr[1] = ypr[2] = xyz[0] = xyz[1] = xyz[2] = 0.0F;
	}

	public int astatePilotIndx()
	{
		if (_astatePilotIndx == -1)
			_astatePilotIndx = Property.intValue(this.getClass(), "astatePilotIndx", 0);
		return _astatePilotIndx;
	}

	public boolean isEnableHotKeysOnOutsideView()
	{
		return false;
	}

	public String[] getHotKeyEnvs()
	{
		return null;
	}

	protected void initSounds()
	{
		if (sfxPreset == null)
			sfxPreset = new SoundPreset("aircraft.cockpit");
		sounds = new SoundFX[18];
	}

	protected void sfxClick(int i)
	{
		sfxStart(i);
	}

	protected void sfxStart(int i)
	{
		if (sounds != null && sounds.length > i)
		{
			SoundFX soundfx = sounds[i];
			if (soundfx == null)
			{
				soundfx = aircraft().newSound(sfxPreset, false, false);
				if (soundfx == null)
					return;
				soundfx.setParent(aircraft().getRootFX());
				sounds[i] = soundfx;
				soundfx.setUsrFlag(i);
			}
			soundfx.play(sfxPos);
		}
	}

	protected void sfxStop(int i)
	{
		if (sounds != null && sounds.length > i && sounds[i] != null)
			sounds[i].stop();
	}

	protected void sfxSetAcoustics(Acoustics acoustics)
	{
		for (int i = 0; i < sounds.length; i++)
		{
			if (sounds[i] != null)
				sounds[i].setAcoustics(acoustics);
		}
	}

	protected void loadBuzzerFX()
	{
		if (sounds != null)
		{
			SoundFX soundfx = sounds[17];
			if (soundfx == null)
			{
				soundfx = aircraft().newSound(sfxPreset, false, false);
				if (soundfx != null)
				{
					soundfx.setParent(aircraft().getRootFX());
					sounds[17] = soundfx;
					soundfx.setUsrFlag(17);
					soundfx.setPosition(sfxPos);
				}
			}
		}
	}

	protected void buzzerFX(boolean bool)
	{
		SoundFX soundfx = sounds[17];
		if (soundfx != null)
			soundfx.setPlay(bool);
	}

	public void onDoorMoved(float f)
	{
		if (acoustics != null && acoustics.globFX != null)
			acoustics.globFX.set(1.0F - f);
	}

	public void doToggleDim()
	{
		Cockpit[] cockpits = Main3D.cur3D().cockpits;
		sfxClick(9);
		for (int i = 0; i < cockpits.length; i++)
		{
			Cockpit cockpit_41_ = cockpits[i];
			if (Actor.isValid(cockpit_41_))
				cockpit_41_.toggleDim();
		}
	}

	public boolean isToggleDim()
	{
		return cockpitDimControl;
	}

	public void doToggleLight()
	{
		Cockpit[] cockpits = Main3D.cur3D().cockpits;
		sfxClick(1);
		for (int i = 0; i < cockpits.length; i++)
		{
			Cockpit cockpit_42_ = cockpits[i];
			if (Actor.isValid(cockpit_42_))
				cockpit_42_.toggleLight();
		}
	}

	public boolean isToggleLight()
	{
		return cockpitLightControl;
	}

	public void doReflectCockitState()
	{
		Cockpit[] cockpits = Main3D.cur3D().cockpits;
		for (int i = 0; i < cockpits.length; i++)
		{
			Cockpit cockpit_43_ = cockpits[i];
			if (Actor.isValid(cockpit_43_))
				cockpit_43_.reflectCockpitState();
		}
	}

	protected void setNightMats(boolean bool)
	{
		if (cockpitNightMats != null)
		{
			for (int i = 0; i < cockpitNightMats.length; i++)
			{
				int i_44_ = mesh.materialFind(cockpitNightMats[i] + "_night");
				if (i_44_ < 0)
				{
					if (World.cur().isDebugFM())
						System.out.println(" * * * * * did not find " + cockpitNightMats[i] + "_night");
				}
				else
				{
					Mat mat = mesh.material(i_44_);
					if (mat.isValidLayer(0))
					{
						mat.setLayer(0);
						mat.set((short) 0, bool);
					}
					else if (World.cur().isDebugFM())
						System.out.println(" * * * * * " + cockpitNightMats[i] + "_night layer 0 invalid");
				}
			}
		}
	}

	public void reflectWorldToInstruments(float f)
	{
		/* empty */
	}

	public void toggleDim()
	{
		/* empty */
	}

	public void toggleLight()
	{
		/* empty */
	}

	public void reflectCockpitState()
	{
		/* empty */
	}

	public boolean isNullShow()
	{
		return bNullShow;
	}

	public void setNullShow(boolean bool)
	{
		Cockpit[] cockpits = Main3D.cur3D().cockpits;
		for (int i = 0; i < cockpits.length; i++)
			cockpits[i]._setNullShow(bool);
		if (bFocus)
		{
			Aircraft aircraft = aircraft();
			if (Actor.isValid(aircraft))
				aircraft.drawing(!bool);
		}
	}

	protected void _setNullShow(boolean bool)
	{
		bNullShow = bool;
	}

	public boolean isEnableRendering()
	{
		return bEnableRendering;
	}

	public void setEnableRendering(boolean bool)
	{
		Cockpit[] cockpits = Main3D.cur3D().cockpits;
		for (int i = 0; i < cockpits.length; i++)
			cockpits[i]._setEnableRendering(bool);
	}

	protected void _setEnableRendering(boolean bool)
	{
		bEnableRendering = bool;
	}

	public boolean isEnableRenderingBall()
	{
		return bEnableRenderingBall;
	}

	public void setEnableRenderingBall(boolean bool)
	{
		Cockpit[] cockpits = Main3D.cur3D().cockpits;
		for (int i = 0; i < cockpits.length; i++)
			cockpits[i]._setEnableRenderingBall(bool);
	}

	protected void _setEnableRenderingBall(boolean bool)
	{
		bEnableRenderingBall = bool;
	}

	public boolean isFocused()
	{
		return bFocus;
	}

	public boolean isEnableFocusing()
	{
		if (!Actor.isValid(aircraft()))
			return false;
		if (aircraft().FM.AS.isPilotParatrooper(astatePilotIndx()))
			return false;
		return true;
	}

	public boolean focusEnter()
	{
		if (!isEnableFocusing())
			return false;
		if (bFocus)
			return true;
		if (!doFocusEnter())
			return false;
		bFocus = true;
		Main3D.cur3D().enableCockpitHotKeys();
		return true;
	}

	public void focusLeave()
	{
		if (bFocus)
		{
			doFocusLeave();
			bFocus = false;
			aircraft().stopMorseSounds();
			if (!isEnableHotKeysOnOutsideView())
				Main3D.cur3D().disableCockpitHotKeys();
		}
	}

	protected boolean doFocusEnter()
	{
		return true;
	}

	protected void doFocusLeave()
	{
		/* empty */
	}

	public boolean existPadlock()
	{
		return false;
	}

	public boolean isPadlock()
	{
		if (!bFocus)
			return false;
		return false;
	}

	public Actor getPadlockEnemy()
	{
		return null;
	}

	public boolean startPadlock(Actor actor)
	{
		if (!bFocus)
			return false;
		return false;
	}

	public void stopPadlock()
	{
		/* empty */
	}

	public void endPadlock()
	{
		/* empty */
	}

	public void setPadlockForward(boolean bool)
	{
		/* empty */
	}

	public boolean isToggleAim()
	{
		if (!bFocus)
			return false;
		return false;
	}

	public void doToggleAim(boolean bool)
	{
		/* empty */
	}

	public boolean isToggleUp()
	{
		if (!bFocus)
			return false;
		return false;
	}

	public void doToggleUp(boolean bool)
	{
		/* empty */
	}

	public String nameOfActiveMirrorSurfaceChunk()
	{
		return "Mirror";
	}

	public String nameOfActiveMirrorBaseChunk()
	{
		return "BaseMirror";
	}

	public static Hook getHookCamera3DMirror(boolean bool)
	{
		return new HookCamera3DMirror(bool);
	}

	public void grabMirrorFromScreen(int i, int i_45_, int i_46_, int i_47_)
	{
		mirrorMat.grabFromScreen(i, i_45_, i_46_, i_47_);
	}

	public void preRender(boolean bool)
	{
		if (!bool && bNullShow)
		{
			Aircraft aircraft = World.getPlayerAircraft();
			if (Actor.isValid(aircraft))
			{
				aircraft.pos.getRender(__l);
				aircraft.draw.soundUpdate(aircraft, __l);
				aircraft.updateLLights();
			}
		}
		if (bEnableRendering)
		{
			if (bNullShow)
				drawNullMesh.preRender();
			else
				drawMesh.preRender();
		}
	}

	public void render(boolean bool)
	{
		if (bEnableRendering)
		{
			if (bNullShow)
				drawNullMesh.render();
			else
				drawMesh.render(bool);
		}
	}

	public Aircraft aircraft()
	{
		if (fm == null)
			return null;
		return (Aircraft) fm.actor;
	}

	public boolean isExistMirror()
	{
		return bExistMirror;
	}

	public void destroy()
	{
		if (isFocused())
			Main3D.cur3D().setView(fm.actor, true);
		fm = null;
		super.destroy();
	}

	public Cockpit(String string, String string_48_)
	{
		fm = _newAircraft.FM;
		pos = new ActorPosMove(this);
		if (string != null)
		{
			mesh = new HierMesh(string);
			int i = mesh.materialFind("MIRROR");
			if (i != -1)
			{
				bExistMirror = true;
				mirrorMat = mesh.material(i);
			}

			//TODO: Added by |ZUTI|
			//------------------------------------------
			ZutiSupportMethods_Air.backupCockpit(this);
			//------------------------------------------
		}
		nullMesh = new HierMesh("3DO/Cockpit/Nill/hier.him");
		nullMeshIL2 = new Mesh("3DO/Cockpit/null/mono.sim");
		try
		{
			acoustics = new Acoustics(string_48_);
			acoustics.setParent(Engine.worldAcoustics());
			initSounds();
		}
		catch (Exception exception)
		{
			System.out.println("Cockpit Acoustics NOT initialized: " + exception.getMessage());
		}
		if (this instanceof CockpitPilot)
			AircraftLH.printCompassHeading = false;
	}

	protected void createActorHashCode()
	{
		makeActorRealHashCode();
	}

	protected float cvt(float f, float f_49_, float f_50_, float f_51_, float f_52_)
	{
		f = Math.min(Math.max(f, f_49_), f_50_);
		return f_51_ + (f_52_ - f_51_) * (f - f_49_) / (f_50_ - f_49_);
	}

	protected float interp(float f, float f_53_, float f_54_)
	{
		return f_53_ + (f - f_53_) * f_54_;
	}

	protected float floatindex(float f, float[] fs)
	{
		int i = (int) f;
		if (i >= fs.length - 1)
			return fs[fs.length - 1];
		if (i < 0)
			return fs[0];
		if (i == 0)
		{
			if (f > 0.0F)
				return fs[0] + f * (fs[1] - fs[0]);
			return fs[0];
		}
		return fs[i] + f % (float) i * (fs[i + 1] - fs[i]);
	}

	public boolean useRealisticNavigationInstruments()
	{
		return World.cur().diffCur.RealisticNavigationInstruments;
	}

	public static void lightningStrike(Point3d point3d)
	{
		lightningPoint = point3d;
		lightningStriked = true;
	}

	private void updateBeacons()
	{
		if (lightningStriked)
		{
			lightningStriked = false;
			V.sub(fm.Loc, lightningPoint);
			float f = (float) V.length();
			atmosphereInterference = cvt(f, 1000.0F, 9000.0F, 1.0F, 0.0F);
		}
		else if (atmosphereInterference > 0.01F)
			atmosphereInterference *= 0.98;
		if (bFocus)
		{
			Aircraft aircraft = aircraft();
			if (aircraft.FM.AS.listenLorenzBlindLanding)
				listenLorenzBlindLanding(aircraft);
			else if (aircraft.FM.AS.listenNDBeacon)
				listenNDBeacon(aircraft, false);
			else if (aircraft.FM.AS.listenRadioStation)
				listenNDBeacon(aircraft, true);
			else if (aircraft.FM.AS.listenYGBeacon)
			{
				ndBeaconRange = 1.0F;
				ndBeaconDirection = 0.0F;
				Actor actor = (Actor) Main.cur().mission.getBeacons(aircraft.getArmy()).get(aircraft.FM.AS.getBeacon() - 1);
				if (actor.isAlive())
					playYEYGMorseCode(aircraft, actor, "DWRKANUGMLFS".toCharArray());
			}
			else if (aircraft.FM.AS.hayrakeCarrier != null && aircraft.FM.AS.hayrakeCarrier.isAlive())
			{
				ndBeaconRange = 1.0F;
				ndBeaconDirection = 0.0F;
				playYEYGMorseCode(aircraft, aircraft.FM.AS.hayrakeCarrier, aircraft.FM.AS.hayrakeCode.toCharArray());
			}
			else
			{
				ndBeaconRange = 1.0F;
				ndBeaconDirection = 0.0F;
			}
		}
	}

	private void playYEYGMorseCode(Aircraft aircraft, Actor actor, char[] cs)
	{
		float f = cvt((float) Time.current() % 30000.0F, 0.0F, 30000.0F, 0.0F, 360.0F);
		boolean bool = false;
		if ((float) Time.current() % 300000.0F <= 30000.0F)
			bool = true;
		actor.pos.getAbs(tempPoint1);
		aircraft.pos.getAbs(tempPoint2);
		tempPoint2.sub(tempPoint1);
		float f_55_ = 57.32484F * (float) Math.atan2(tempPoint2.x, tempPoint2.y);
		f_55_ -= 180.0F;
		for (f_55_ = (f_55_ + 180.0F) % 360.0F; f_55_ < 0.0F; f_55_ += 360.0F)
		{
			/* empty */
		}
		for (/**/; f_55_ >= 360.0F; f_55_ -= 360.0F)
		{
			/* empty */
		}
		float f_56_ = Math.abs(f - f_55_);
		Point3d point3d = new Point3d();
		point3d.x = actor.pos.getAbsPoint().x;
		point3d.y = actor.pos.getAbsPoint().y;
		point3d.z = actor.pos.getAbsPoint().z + 40.0;
		float f_57_ = 15.0F;
		if (f_56_ > f_57_)
		{
			BeaconGeneric.getSignalAttenuation(point3d, aircraft, false, false, true, true);
			aircraft.playYEYGCarrier(false, 0.0F);
			clearToPlay = true;
		}
		else
		{
			float f_58_ = BeaconGeneric.getSignalAttenuation(point3d, aircraft, false, false, true, false);
			if (f_58_ != 1.0F)
			{
				float f_59_ = (1.0F - f_58_) * ((f_57_ - f_56_) / f_57_);
				aircraft.playYEYGCarrier(true, f_59_ * 0.5F);
				int i = (int) f;
				if (i % 15 == 0)
					clearToPlay = true;
				if (i % 13 == 0 && (!aircraft.isMorseSequencePlaying() || clearToPlay))
				{
					if (bool)
					{
						String string = Beacon.getBeaconID(aircraft.FM.AS.getBeacon() - 1);
						String string_60_ = "";
						if (morseCharsPlayed % 2 == 0)
							string_60_ = "" + string.charAt(0);
						else
							string_60_ = "" + string.charAt(1);
						aircraft.morseSequenceStart(string_60_, f_59_);
						clearToPlay = false;
						morseCharsPlayed++;
					}
					else
					{
						morseCharsPlayed = 0;
						float f_61_ = 0.0F;
						if (cs.length == 12)
							f_61_ = 0.033333335F * f;
						else if (cs.length == 24)
							f_61_ = 0.06666667F * f;
						if (f_61_ >= (float) cs.length)
							f_61_ = 0.0F;
						char c = cs[(int) f_61_];
						String string = "" + c;
						aircraft.morseSequenceStart(string, f_59_);
						clearToPlay = false;
					}
				}
			}
		}
	}

	private void listenLorenzBlindLanding(Aircraft aircraft)
	{
		blData.reset();
		Beacon.LorenzBLBeacon lorenzblbeacon = (Beacon.LorenzBLBeacon) getBeacon();
		if (lorenzblbeacon == null || !lorenzblbeacon.isAlive())
			aircraft.stopMorseSounds();
		else
		{
			lorenzblbeacon.rideBeam(aircraft, blData);
			float f = blData.blindLandingAzimuthBP;
			float f_62_ = (float) Math.random() * (0.5F - blData.signalStrength);
			float f_63_ = (cvt(blData.signalStrength * 2.0F, 0.0F, 0.75F, 0.0F, 1.2F) - f_62_);
			float f_64_ = 12.0F;
			float f_65_ = 0.3F;
			float f_66_ = 0.0F;
			float f_67_ = 0.0F;
			if (f < f_65_ && f > -f_65_)
			{
				aircraft.playLorenzDash(false, 0.0F);
				aircraft.playLorenzDot(false, 0.0F);
				aircraft.playLorenzSolid(true, f_63_);
			}
			else
			{
				if (f > f_64_)
				{
					f_66_ = 1.0F;
					f_67_ = 0.0F;
				}
				else if (f < -f_64_)
				{
					f_67_ = 1.0F;
					f_66_ = 0.0F;
				}
				else
				{
					f_66_ = cvt(f, -f_64_ / 2.0F, f_65_ * 10.0F, 0.0F, 1.0F);
					f_67_ = cvt(f, -f_65_ * 10.0F, f_64_ / 2.0F, 1.0F, 0.0F);
				}
				aircraft.playLorenzSolid(true, 0.0F);
				aircraft.playLorenzDash(true, f_67_ * f_63_);
				aircraft.playLorenzDot(true, f_66_ * f_63_);
			}
		}
	}

	public boolean isOnBlindLandingMarker()
	{
		Aircraft aircraft = aircraft();
		if (blData.isOnInnerMarker)
		{
			aircraft.playLorenzInnerMarker(true, 1.0F);
			return true;
		}
		if (blData.isOnOuterMarker)
		{
			aircraft.playLorenzOuterMarker(true, 1.0F);
			return true;
		}
		aircraft.playLorenzInnerMarker(false, 0.0F);
		aircraft.playLorenzOuterMarker(false, 0.0F);
		return false;
	}

	public float getBeaconRange()
	{
		Aircraft aircraft = aircraft();
		if (aircraft.FM.AS.listenLorenzBlindLanding)
			return (7.0422F * (1.0F - blData.signalStrength + World.Rnd().nextFloat(-atmosphereInterference * 2.0F, (atmosphereInterference * 2.0F))));
		if (useRealisticNavigationInstruments())
			return (ndBeaconRange + (float) Math.random() * 0.2F * ndBeaconRange + World.Rnd().nextFloat(-atmosphereInterference * 2.0F, atmosphereInterference * 2.0F));
		return 1.0F;
	}

	public float getGlidePath()
	{
		com.maddox.il2.objects.air.Aircraft aircraft1 = aircraft();
		if (aircraft1.FM.AS.listenLorenzBlindLanding)
		{
			int i = fm.AS.getBeacon();
			java.util.ArrayList arraylist = com.maddox.il2.game.Main.cur().mission.getBeacons(fm.actor.getArmy());
			com.maddox.il2.engine.Actor actor = (com.maddox.il2.engine.Actor) arraylist.get(i - 1);
			double d = actor.pos.getAbsPoint().z + 10D;

			float f = (1.0F - blData.signalStrength) * 100F;
			double d1 = java.lang.Math.abs(aircraft1.pos.getAbsPoint().x - actor.pos.getAbsPoint().x);
			double d2 = java.lang.Math.abs(aircraft1.pos.getAbsPoint().y - actor.pos.getAbsPoint().y);
			float f1 = 1700F + com.maddox.il2.ai.World.Rnd().nextFloat(-f, f);
			double d3 = java.lang.Math.sqrt(d1 * d1 + d2 * d2) - (double) f1;
			double d4 = aircraft1.pos.getAbsPoint().z - d;

			double d5 = java.lang.Math.asin(d4 / d3);
			double d6 = glideScopeInRads - d5;
			float f2 = (float) java.lang.Math.toDegrees(d6) * blData.signalStrength + com.maddox.il2.ai.World.Rnd().nextFloat(-atmosphereInterference * 2.0F, atmosphereInterference * 2.0F);
			if (f2 > 1.0F)
				f2 = 1.0F;
			else if (f2 < -1F)
				f2 = -1F;
			return f2;
		}
		else
		{
			return 0.0F;
		}
	}

	public float getBeaconDirection()
	{
		Aircraft aircraft = aircraft();
		if (bFocus && aircraft.FM.AS.listenLorenzBlindLanding)
			return (blData.blindLandingAzimuthPB + World.Rnd().nextFloat(-atmosphereInterference * 90.0F, atmosphereInterference * 90.0F));
		if (useRealisticNavigationInstruments() && Main.cur().mission.hasBeacons(fm.actor.getArmy()))
			return (ndBeaconDirection + World.Rnd().nextFloat(-atmosphereInterference * 90.0F, atmosphereInterference * 90.0F));
		return 0.0F;
	}

	private Actor getBeacon()
	{
		int i = fm.AS.getBeacon();
		if (i == 0)
			return null;
		ArrayList arraylist = Main.cur().mission.getBeacons(fm.actor.getArmy());
		Actor actor = (Actor) arraylist.get(i - 1);
		if (actor instanceof TypeHasYGBeacon || actor instanceof TypeHasHayRake)
			return null;
		ArrayList arraylist_72_ = Main.cur().mission.getMeacons(fm.actor.getArmy());
		if (arraylist_72_.size() >= i && !(actor instanceof Beacon.LorenzBLBeacon))
		{
			Actor actor_73_ = (Actor) arraylist_72_.get(i - 1);
			if (actor_73_.isAlive())
			{
				distanceV.sub(actor_73_.pos.getAbsPoint(), fm.Loc);
				double d = distanceV.length();
				distanceV.sub(actor.pos.getAbsPoint(), fm.Loc);
				double d_74_ = distanceV.length();
				if (d < d_74_ || !actor.isAlive())
					actor = actor_73_;
			}
		}
		return actor;
	}

	private void listenNDBeacon(Aircraft aircraft, boolean bool)
	{
		Actor actor = getBeacon();
		if (actor == null || !actor.isAlive())
		{
			ndBeaconRange = 1.0F;
			ndBeaconDirection = 0.0F;
			if (bool)
				CmdMusic.setCurrentVolume(0.0F);
			else
				aircraft.playBeaconCarrier(false, 0.0F);
		}
		else
		{
			tempPoint1.x = actor.pos.getAbsPoint().x;
			tempPoint1.y = actor.pos.getAbsPoint().y;
			tempPoint1.z = actor.pos.getAbsPoint().z + 20.0;
			aircraft.pos.getAbs(acPoint);
			acPoint.sub(tempPoint1);
			float f = aircraft.pos.getAbsOrient().getYaw() + 180.0F;
			float f_75_ = 57.32484F * (float) Math.atan2(acPoint.y, acPoint.x) - f;
			for (f_75_ = (f_75_ + 180.0F) % 360.0F; f_75_ < 0.0F; f_75_ += 360.0F)
			{
				/* empty */
			}
			for (/**/; f_75_ >= 360.0F; f_75_ -= 360.0F)
			{
				/* empty */
			}
			if (f_75_ > 270.0F)
				f_75_ -= 360.0F;
			if (f_75_ > 90.0F)
				f_75_ = -(f_75_ - 180.0F);
			ndBeaconRange = BeaconGeneric.getSignalAttenuation(tempPoint1, aircraft, !bool, bool, false, false);
			if (Math.random() < 0.02)
				terrainAndNightError = BeaconGeneric.getTerrainAndNightError(aircraft);
			f_75_ += terrainAndNightError;
			float f_76_ = floatindex(cvt(1.0F - ndBeaconRange, 0.0F, 1.0F, 0.0F, 9.0F), volumeLogScale);
			float f_77_ = (float) AudioDevice.vMusic.get();
			float f_78_ = (f_77_ + 1.0F) / 15.0F;
			if (!bool)
			{
				float f_79_ = (float) Math.random() * ndBeaconRange;
				float f_80_ = (float) Time.current() % 60000.0F;
				if (f_80_ <= 500.0F && !aircraft.isMorseSequencePlaying())
				{
					String string = Beacon.getBeaconID(aircraft.FM.AS.getBeacon() - 1);
					f_78_ = f_76_ * f_78_ * 0.75F;
					aircraft.morseSequenceStart(string, f_78_);
				}
				else
				{
					f_78_ = f_76_ * f_78_ * 0.75F - f_79_;
					aircraft.playBeaconCarrier(true, f_78_);
				}
			}
			else
			{
				CmdMusic.setCurrentVolume(f_76_);
				aircraft.playRadioStatic(true, ((-0.5F + (1.0F - f_76_ * f_78_)) * 2.0F));
			}
			ndBeaconDirection = f_76_ * f_75_ + (((float) Math.random() - 0.5F) * ndBeaconRange);
		}
	}

	private float getRadioCompassWaypoint(boolean bool, boolean bool_81_, boolean bool_82_)
	{
		Actor actor = getBeacon();
		if (actor == null || !actor.isAlive())
		{
			if (bool_82_)
			{
				prevWaypointF = aircraft().FM.Or.azimut();
				return prevWaypointF;
			}
			prevWaypointF = 0.0F;
			return prevWaypointF;
		}
		aircraft();
		tempPoint1.x = actor.pos.getAbsPoint().x;
		tempPoint1.y = actor.pos.getAbsPoint().y;
		tempPoint1.z = actor.pos.getAbsPoint().z + 20.0;
		V.sub(tempPoint1, fm.Loc);
		float f;
		if (bool)
		{
			if (bool_81_)
			{
				for (f = (float) (57.29577951308232 * Math.atan2(-V.y, V.x)); f <= -180.0F; f += 180.0F)
				{
					/* empty */
				}
			}
			else
			{
				for (f = (float) (57.29577951308232 * Math.atan2(V.y, V.x)); f <= -180.0F; f += 180.0F)
				{
					/* empty */
				}
			}
		}
		else
		{
			for (f = (float) (57.29577951308232 * Math.atan2(V.x, V.y)); f <= -180.0F; f += 180.0F)
			{
				/* empty */
			}
		}
		for (/**/; f > 180.0F; f -= 180.0F)
		{
			/* empty */
		}
		f += terrainAndNightError;
		if ((double) ndBeaconRange > 0.99)
			f = aircraft().FM.Or.azimut();
		else
			f += World.Rnd().nextFloat((-ndBeaconRange - atmosphereInterference * 5.0F), ndBeaconRange + (atmosphereInterference * 5.0F)) * 30.0F;
		return f;
	}

	private float getWaypoint(boolean bool, boolean bool_83_, float f, boolean bool_84_)
	{
		if (useRealisticNavigationInstruments())
		{
			if (bool_84_)
			{
				if (Main.cur().mission.hasBeacons(fm.actor.getArmy()))
				{
					skip = !skip;
					if (skip)
						return prevWaypointF;
					float f_85_ = getRadioCompassWaypoint(bool, bool_83_, bool_84_);
					prevWaypointF = f_85_;
					return f_85_;
				}
				return aircraft().FM.Or.azimut();
			}
			return aircraft().headingBug;
		}
		WayPoint waypoint = fm.AP.way.curr();
		if (waypoint == null)
			return 0.0F;
		waypoint.getP(P1);
		V.sub(P1, fm.Loc);
		float f_86_;
		if (bool)
		{
			if (bool_83_)
			{
				for (f_86_ = (float) (57.29577951308232 * Math.atan2(-V.y, V.x)); f_86_ <= -180.0F; f_86_ += 180.0F)
				{
					/* empty */
				}
			}
			else
			{
				for (f_86_ = (float) (57.29577951308232 * Math.atan2(V.y, V.x)); f_86_ <= -180.0F; f_86_ += 180.0F)
				{
					/* empty */
				}
			}
		}
		else
		{
			for (f_86_ = (float) (57.29577951308232 * Math.atan2(V.x, V.y)); f_86_ <= -180.0F; f_86_ += 180.0F)
			{
				/* empty */
			}
		}
		for (/**/; f_86_ > 180.0F; f_86_ -= 180.0F)
		{
			/* empty */
		}
		return f_86_ + World.Rnd().nextFloat(-f, f);
	}

	protected float waypointAzimuth()
	{
		return getWaypoint(false, false, 0.0F, false);
	}

	protected float waypointAzimuth(float f)
	{
		return getWaypoint(false, false, f, false);
	}

	protected float waypointAzimuthInvert(float f)
	{
		return getWaypoint(true, false, f, false);
	}

	protected float waypointAzimuthInvertMinus(float f)
	{
		return getWaypoint(true, true, f, false);
	}

	protected float radioCompassAzimuthInvertMinus()
	{
		return getWaypoint(true, true, 0.0F, true);
	}

	//TODO: |ZUTI| methods
	//---------------------------------------
	protected Map	zutiOriginalMeshesStates;
	//---------------------------------------
}