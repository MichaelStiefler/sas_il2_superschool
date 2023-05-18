/*4.10.1 class*/
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

public abstract class Cockpit extends Actor {
	public FlightModel fm = null;
	public boolean cockpitDimControl = false;
	public boolean cockpitLightControl = false;
	protected String[] cockpitNightMats = null;
	protected static float[] ypr = { 0.0F, 0.0F, 0.0F };
	protected static float[] xyz = { 0.0F, 0.0F, 0.0F };
	protected int _astatePilotIndx = -1;
	public static final int SNDCLK_NULL = 0;
	public static final int SNDCLK_BUTTONDEPRESSED1 = 1;
	public static final int SNDCLK_BUTTONDEPRESSED2 = 2;
	public static final int SNDCLK_BUTTONDEPRESSED3 = 3;
	public static final int SNDCLK_TUMBLERFLIPPED1 = 4;
	public static final int SNDCLK_TUMBLERFLIPPED2 = 5;
	public static final int SNDCLK_TUMBLERFLIPPED3 = 6;
	public static final int SNDINF_RUSTYWHEELTURNING1 = 7;
	public static final int SNDINF_OILEDMETALWHEELWITHCHAIN1 = 8;
	public static final int SNDCLK_SMALLLEVERSLIDES1 = 9;
	public static final int SNDCLK_SMALLLEVERSLIDES2 = 10;
	public static final int SNDINF_LEVERSLIDES1 = 11;
	public static final int SNDINF_LEVERSLIDES2 = 12;
	public static final int SNDCLK_RUSTYLEVERSLIDES1 = 13;
	public static final int SNDCLK_SMALLVALVE1 = 14;
	public static final int SNDINF_SMALLVALVEWITHGASLEAK1 = 15;
	public static final int SNDINF_WORMGEARTURNS1 = 16;
	public static final int SNDINF_BUZZER_109 = 17;
	public static final int SND_COUNT = 18;
	protected static SoundPreset sfxPreset = null;
	protected SoundFX[] sounds = null;
	protected Point3d sfxPos = new Point3d(1.0, 0.0, 0.0);
	private float prevWaypointF = 0.0F;
	private boolean skip = false;
	private Vector3d distanceV = new Vector3d();
	private float ndBeaconRange = 1.0F;
	private float ndBeaconDirection = 0.0F;
	private BlindLandingData blData = new BlindLandingData();
	private Point3d acPoint = new Point3d();
	private Point3d tempPoint1 = new Point3d();
	private Point3d tempPoint2 = new Point3d();
	private static float atmosphereInterference = 0.0F;
	private static Point3d lightningPoint = new Point3d();
	private static boolean lightningStriked = false;
	public static final float radioCompassUpdateF = 0.02F;
	private static float terrainAndNightError = 0.0F;
	private static final double glideScopeInRads = Math.toRadians(3.0);
	private int morseCharsPlayed = 0;
	private boolean clearToPlay = true;
	private static final float[] volumeLogScale = { 0.0F, 0.301F, 0.477F, 0.602F, 0.698F, 0.778F, 0.845F, 0.903F,
			0.954F, 1.0F };
	private boolean bNullShow = false;
	private boolean bEnableRendering = true;
	private boolean bEnableRenderingBall = true;
	private boolean bFocus = false;
	private int[] world_renderwin = new int[4];
	private int[] cockpit_renderwin = new int[4];
	public HierMesh mesh;
	private HierMesh nullMesh;
	private Mesh nullMeshIL2;
	private Draw drawMesh = new Draw();
	private NullDraw drawNullMesh = new NullDraw();
	private boolean bExistMirror = false;
	private Mat mirrorMat;
	private Loc __l = new Loc();
	private Point3d __p = new Point3d();
	private Orient __o = new Orient();
	public static Aircraft _newAircraft = null;
	private static Point3d nullP = new Point3d();
	protected static Point3d P1 = new Point3d();
	protected static Vector3d V = new Vector3d();

	public static class HookOnlyOrient extends Hook {
		public void computePos(Actor actor, Loc loc, Loc loc_0_) {
			loc_0_.set(Cockpit.nullP, loc.getOrient());
		}
	}

	class NullDraw extends ActorDraw {
		private int iPreRender = 0;

		public void preRender() {
			if (Actor.isValid(pos.base()) && nullMesh != null && nullMeshIL2 != null) {
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

		public void render() {
			if (iPreRender != 0 && Actor.isValid(pos.base()) && nullMesh != null && nullMeshIL2 != null) {
				if (bEnableRenderingBall)
					nullMesh.render();
				else
					nullMeshIL2.render();
			}
		}
	}

	class Draw extends ActorDraw {
		protected double[] _modelMatrix3D = new double[16];
		protected double[] _modelMatrix3DMirror = new double[16];
		protected double[] _projMatrix3DMirror = new double[16];
		private int iPreRender = 0;

		public void preRender() {
			if (Actor.isValid(pos.base()) && mesh != null) {
				pos.getRender(__l);
				mesh.setPos(__l);
				Cockpit.this.updateBeacons();
				reflectWorldToInstruments(Time.tickOffset());
				iPreRender = mesh.preRender();
			}
		}

		public void render(boolean bool) {
			if (iPreRender != 0 && Actor.isValid(pos.base()) && mesh != null) {
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
				if (bool) {
					if (!bExistMirror)
						System.out.println("*** Internal error: mirr exist");
					if (!Main3D.cur3D().isViewMirror())
						System.out.println("*** Internal error: mirr isview");
					String string = nameOfActiveMirrorSurfaceChunk();
					String string1 = nameOfActiveMirrorBaseChunk();
					if (string != null) {
						mesh.setCurChunk(string);
						mesh.chunkVisible(false);
					}
					if (string1 != null) {
						mesh.setCurChunk(string1);
						mesh.chunkVisible(true);
					}
					mesh.render();
				} else if (!bExistMirror)
					mesh.render();
				else {
					boolean bool_2_ = Main3D.cur3D().isViewMirror();
					String string = nameOfActiveMirrorSurfaceChunk();
					String string_3_ = nameOfActiveMirrorBaseChunk();
					if (bool_2_) {
						if (string != null) {
							mesh.setCurChunk(string);
							mesh.chunkVisible(true);
							mesh.renderChunkMirror(_modelMatrix3D, _modelMatrix3DMirror, _projMatrix3DMirror);
							mesh.chunkVisible(false);
						}
						if (string_3_ != null) {
							mesh.setCurChunk(string_3_);
							mesh.chunkVisible(true);
						}
						mesh.render();
					} else {
						if (string != null) {
							mesh.setCurChunk(string);
							mesh.chunkVisible(false);
						}
						if (string_3_ != null) {
							mesh.setCurChunk(string_3_);
							mesh.chunkVisible(false);
						}
						mesh.render();
					}
				}
			}
		}
	}

	static class HookCamera3DMirror extends HookRender {
		Matrix4d cam2w = new Matrix4d();
		Matrix4d mir2w = new Matrix4d();
		Matrix4d cam2mir = new Matrix4d();
		Point3d p = new Point3d();
		Vector3d X = new Vector3d();
		Vector3d Y = new Vector3d();
		Vector3d Z = new Vector3d();
		Matrix4d cmm2w = new Matrix4d();
		Matrix4d cmm2mir = new Matrix4d();
		Matrix4d mir2cmm = new Matrix4d();
		double[] Eul = new double[3];
		Point3f mirLowP = new Point3f();
		Point3f mirHigP = new Point3f();
		float resultNearClipDepth;
		Loc mir2w_loc = new Loc();
		Loc aLoc = new Loc();
		boolean bInCockpit;

		private float computeMirroredCamera(Loc loc, Loc loc1, Point3f point3f, Point3f point3f1, int i, int i1,
				float f, Loc loc2, int[] is) {
			loc.getMatrix(cam2w);
			loc1.getMatrix(mir2w);
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
			float f1 = (float) -cmm2mir.m23;
			if (f1 <= 0.0010F)
				return -1.0F;
			float f2;
			float f3;
			float f4;
			float f5 = f2 = f3 = f4 = 0.0F;
			for (int i2 = 0; i2 < 8; i2++) {
				switch (i2) {
				case 0:
					p.set(point3f.x, point3f.y, point3f.z);
					break;
				case 1:
					p.set(point3f1.x, point3f.y, point3f.z);
					break;
				case 2:
					p.set(point3f.x, point3f1.y, point3f.z);
					break;
				case 3:
					p.set(point3f.x, point3f.y, point3f1.z);
					break;
				case 4:
					p.set(point3f1.x, point3f1.y, point3f1.z);
					break;
				case 5:
					p.set(point3f.x, point3f1.y, point3f1.z);
					break;
				case 6:
					p.set(point3f1.x, point3f.y, point3f1.z);
					break;
				case 7:
					p.set(point3f1.x, point3f1.y, point3f.z);
					break;
				}
				mir2cmm.transform(p);
				float f6 = -(float) (f1 * p.y / p.x);
				float f7 = (float) (f1 * p.z / p.x);
				if (i2 == 0) {
					f5 = f2 = f6;
					f3 = f4 = f7;
				} else {
					if (f6 < f5)
						f5 = f6;
					if (f6 > f2)
						f2 = f6;
					if (f7 < f3)
						f3 = f7;
					if (f7 > f4)
						f4 = f7;
				}
			}
			float f8 = f2 - f5;
			float f9 = f4 - f3;
			if (f8 <= 0.0010F || f9 <= 0.0010F)
				return -1.0F;
			f5 -= 0.0010F;
			f2 += 0.0010F;
			f3 -= 0.0010F;
			f4 += 0.0010F;
			f8 = f2 - f5;
			f9 = f4 - f3;
			float f10 = f8 / i;
			float f11 = f10 * f;
			float f12 = f11 * i1;
			if (f12 > f9) {
				f9 = f12;
				f3 = (f3 + f4) * 0.5F - f9 * 0.5F;
				f4 = f3 + f9;
			} else {
				f8 *= f9 / f12;
				f5 = (f5 + f2) * 0.5F - f8 * 0.5F;
				f2 = f5 + f8;
			}
			float f13 = 2.0F * Math.max(Math.abs(f5), Math.abs(f2));
			float f14 = 2.0F * Math.max(Math.abs(f3), Math.abs(f4));
			int i3 = (int) (0.5F + i * f13 / f8);
			int i4 = (int) (0.5F + i1 * f14 / f9);
			float f15 = (2.0F * Geom.RAD2DEG((float) Math.atan2(f13 * 0.5F, f1)));
			int i5 = (int) (-((f5 - -f13 / 2.0F) / f13) * i3);
			int i6 = (int) (-((f3 - -f14 / 2.0F) / f14) * i4);
			cmm2w.getEulers(Eul);
			Eul[0] = -Math.toDegrees(Eul[0]);
			Eul[1] = -Math.toDegrees(Eul[1]);
			Eul[2] = Math.toDegrees(Eul[2]);
			loc2.set(cmm2w.m03, cmm2w.m13, cmm2w.m23, (float) Eul[0], (float) Eul[1], (float) Eul[2]);
			is[0] = i5;
			is[1] = i6;
			is[2] = i3;
			is[3] = i4;
			resultNearClipDepth = f1;
			return f15;
		}

		public boolean computeRenderPos(Actor actor, Loc loc, Loc loc1) {
			computePos(actor, loc, loc1, true);
			return true;
		}

		public void computePos(Actor actor, Loc loc, Loc loc1) {
			computePos(actor, loc, loc1, false);
		}

		public void computePos(Actor actor, Loc loc, Loc loc1, boolean bool) {
			if (Actor.isValid(Main3D.cur3D().cockpitCur) && Main3D.cur3D().cockpitCur.bExistMirror
					&& Main3D.cur3D().isViewMirror() && Main3D.cur3D().cockpitCur.isFocused()
					&& Actor.isValid(World.getPlayerAircraft())) {
				Loc loc2 = loc;
				Main3D.cur3D().cockpitCur.mesh.setCurChunk(Main3D.cur3D().cockpitCur.nameOfActiveMirrorSurfaceChunk());
				Main3D.cur3D().cockpitCur.mesh.getChunkLocObj(mir2w_loc);
				if (bool) {
					if (bInCockpit)
						Main3D.cur3D().cockpitCur.pos.getRender(aLoc);
					else
						World.getPlayerAircraft().pos.getRender(aLoc);
				} else if (bInCockpit)
					Main3D.cur3D().cockpitCur.pos.getAbs(aLoc);
				else
					World.getPlayerAircraft().pos.getAbs(aLoc);
				mir2w_loc.add(mir2w_loc, aLoc);
				Main3D.cur3D().cockpitCur.mesh.getChunkCurVisBoundBox(mirLowP, mirHigP);
				float f = (computeMirroredCamera(loc2, mir2w_loc, mirLowP, mirHigP, Main3D.cur3D().mirrorWidth(),
						Main3D.cur3D().mirrorHeight(), 1.0F, loc1,
						(bInCockpit ? Main3D.cur3D().cockpitCur.cockpit_renderwin
								: Main3D.cur3D().cockpitCur.world_renderwin)));
				if (bool) {
					if (bInCockpit) {
						Main3D.cur3D().cameraCockpitMirror.set(f);
						Main3D.cur3D().cameraCockpitMirror.ZNear = resultNearClipDepth;
					} else {
						Main3D.cur3D().camera3DMirror.set(f);
						Main3D.cur3D().camera3DMirror.ZNear = resultNearClipDepth;
					}
				}
			}
		}

		public HookCamera3DMirror(boolean bool) {
			bInCockpit = bool;
		}
	}

	public static class Camera3DMirror extends Camera3D {
		public boolean activate(float f, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7, int i8,
				int i9) {
			if (!Actor.isValid(Main3D.cur3D().cockpitCur))
				return super.activate(f, i, i1, i2, i3, i4, i5, i6, i7, i8, i9);
			pos.getRender(_tmpLoc);
			int[] is = Main3D.cur3D().cockpitCur.world_renderwin;
			if (this == Main3D.cur3D().cameraCockpitMirror)
				is = Main3D.cur3D().cockpitCur.cockpit_renderwin;
			return super.activate(f, i, i1, Main3D.cur3D().mirrorX0() + is[0], Main3D.cur3D().mirrorY0() + is[1], is[2],
					is[3], Main3D.cur3D().mirrorX0(), Main3D.cur3D().mirrorY0(), Main3D.cur3D().mirrorWidth(),
					Main3D.cur3D().mirrorHeight());
		}
	}

	protected void resetYPRmodifier() {
		ypr[0] = ypr[1] = ypr[2] = xyz[0] = xyz[1] = xyz[2] = 0.0F;
	}

	public int astatePilotIndx() {
		if (_astatePilotIndx == -1)
			_astatePilotIndx = Property.intValue(this.getClass(), "astatePilotIndx", 0);
		return _astatePilotIndx;
	}

	public boolean isEnableHotKeysOnOutsideView() {
		return false;
	}

	public String[] getHotKeyEnvs() {
		return null;
	}

	protected void initSounds() {
		if (sfxPreset == null)
			sfxPreset = new SoundPreset("aircraft.cockpit");
		sounds = new SoundFX[18];
	}

	protected void sfxClick(int i) {
		sfxStart(i);
	}

	protected void sfxStart(int i) {
		if (sounds != null && sounds.length > i) {
			SoundFX soundfx = sounds[i];
			if (soundfx == null) {
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

	protected void sfxStop(int i) {
		if (sounds != null && sounds.length > i && sounds[i] != null)
			sounds[i].stop();
	}

	protected void sfxSetAcoustics(Acoustics acoustics) {
		for (int i = 0; i < sounds.length; i++) {
			if (sounds[i] != null)
				sounds[i].setAcoustics(acoustics);
		}
	}

	protected void loadBuzzerFX() {
		if (sounds != null) {
			SoundFX soundfx = sounds[17];
			if (soundfx == null) {
				soundfx = aircraft().newSound(sfxPreset, false, false);
				if (soundfx != null) {
					soundfx.setParent(aircraft().getRootFX());
					sounds[17] = soundfx;
					soundfx.setUsrFlag(17);
					soundfx.setPosition(sfxPos);
				}
			}
		}
	}

	protected void buzzerFX(boolean bool) {
		SoundFX soundfx = sounds[17];
		if (soundfx != null)
			soundfx.setPlay(bool);
	}

	public void onDoorMoved(float f) {
		if (acoustics != null && acoustics.globFX != null)
			acoustics.globFX.set(1.0F - f);
	}

	public void doToggleDim() {
		Cockpit[] cockpits = Main3D.cur3D().cockpits;
		sfxClick(9);
		for (int i = 0; i < cockpits.length; i++) {
			Cockpit cockpit1 = cockpits[i];
			if (Actor.isValid(cockpit1))
				cockpit1.toggleDim();
		}
	}

	public boolean isToggleDim() {
		return cockpitDimControl;
	}

	public void doToggleLight() {
		Cockpit[] cockpits = Main3D.cur3D().cockpits;
		sfxClick(1);
		for (int i = 0; i < cockpits.length; i++) {
			Cockpit cockpit1 = cockpits[i];
			if (Actor.isValid(cockpit1))
				cockpit1.toggleLight();
		}
	}

	public boolean isToggleLight() {
		return cockpitLightControl;
	}

	public void doReflectCockitState() {
		Cockpit[] cockpits = Main3D.cur3D().cockpits;
		for (int i = 0; i < cockpits.length; i++) {
			Cockpit cockpit1 = cockpits[i];
			if (Actor.isValid(cockpit1))
				cockpit1.reflectCockpitState();
		}
	}

	protected void setNightMats(boolean bool) {
		if (cockpitNightMats != null) {
			for (int i = 0; i < cockpitNightMats.length; i++) {
				int i1 = mesh.materialFind(cockpitNightMats[i] + "_night");
				if (i1 < 0) {
					if (World.cur().isDebugFM())
						System.out.println(" * * * * * did not find " + cockpitNightMats[i] + "_night");
				} else {
					Mat mat = mesh.material(i1);
					if (mat.isValidLayer(0)) {
						mat.setLayer(0);
						mat.set((short) 0, bool);
					} else if (World.cur().isDebugFM())
						System.out.println(" * * * * * " + cockpitNightMats[i] + "_night layer 0 invalid");
				}
			}
		}
	}

	public void reflectWorldToInstruments(float f) {
	}

	public void toggleDim() {
	}

	public void toggleLight() {
	}

	public void reflectCockpitState() {
	}

	public boolean isNullShow() {
		return bNullShow;
	}

	public void setNullShow(boolean bool) {
		Cockpit[] cockpits = Main3D.cur3D().cockpits;
		for (int i = 0; i < cockpits.length; i++)
			cockpits[i]._setNullShow(bool);
		if (bFocus) {
			Aircraft aircraft = aircraft();
			if (Actor.isValid(aircraft))
				aircraft.drawing(!bool);
		}
	}

	protected void _setNullShow(boolean bool) {
		bNullShow = bool;
	}

	public boolean isEnableRendering() {
		return bEnableRendering;
	}

	public void setEnableRendering(boolean bool) {
		Cockpit[] cockpits = Main3D.cur3D().cockpits;
		for (int i = 0; i < cockpits.length; i++)
			cockpits[i]._setEnableRendering(bool);
	}

	protected void _setEnableRendering(boolean bool) {
		bEnableRendering = bool;
	}

	public boolean isEnableRenderingBall() {
		return bEnableRenderingBall;
	}

	public void setEnableRenderingBall(boolean bool) {
		Cockpit[] cockpits = Main3D.cur3D().cockpits;
		for (int i = 0; i < cockpits.length; i++)
			cockpits[i]._setEnableRenderingBall(bool);
	}

	protected void _setEnableRenderingBall(boolean bool) {
		bEnableRenderingBall = bool;
	}

	public boolean isFocused() {
		return bFocus;
	}

	public boolean isEnableFocusing() {
		if (!Actor.isValid(aircraft()))
			return false;
		if (aircraft().FM.AS.isPilotParatrooper(astatePilotIndx()))
			return false;
		return true;
	}

	public boolean focusEnter() {
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

	public void focusLeave() {
		if (bFocus) {
			doFocusLeave();
			bFocus = false;
			aircraft().stopMorseSounds();
			if (!isEnableHotKeysOnOutsideView())
				Main3D.cur3D().disableCockpitHotKeys();
		}
	}

	protected boolean doFocusEnter() {
		return true;
	}

	protected void doFocusLeave() {
	}

	public boolean existPadlock() {
		return false;
	}

	public boolean isPadlock() {
		if (!bFocus)
			return false;
		return false;
	}

	public Actor getPadlockEnemy() {
		return null;
	}

	public boolean startPadlock(Actor actor) {
		if (!bFocus)
			return false;
		return false;
	}

	public void stopPadlock() {
	}

	public void endPadlock() {
	}

	public void setPadlockForward(boolean bool) {
	}

	public boolean isToggleAim() {
		if (!bFocus)
			return false;
		return false;
	}

	public void doToggleAim(boolean bool) {
	}

	public boolean isToggleUp() {
		if (!bFocus)
			return false;
		return false;
	}

	public void doToggleUp(boolean bool) {
	}

	public String nameOfActiveMirrorSurfaceChunk() {
		return "Mirror";
	}

	public String nameOfActiveMirrorBaseChunk() {
		return "BaseMirror";
	}

	public static Hook getHookCamera3DMirror(boolean bool) {
		return new HookCamera3DMirror(bool);
	}

	public void grabMirrorFromScreen(int i, int i_45_, int i_46_, int i_47_) {
		mirrorMat.grabFromScreen(i, i_45_, i_46_, i_47_);
	}

	public void preRender(boolean bool) {
		if (!bool && bNullShow) {
			Aircraft aircraft = World.getPlayerAircraft();
			if (Actor.isValid(aircraft)) {
				aircraft.pos.getRender(__l);
				aircraft.draw.soundUpdate(aircraft, __l);
				aircraft.updateLLights();
			}
		}
		if (bEnableRendering) {
			if (bNullShow)
				drawNullMesh.preRender();
			else
				drawMesh.preRender();
		}
	}

	public void render(boolean bool) {
		if (bEnableRendering) {
			if (bNullShow)
				drawNullMesh.render();
			else
				drawMesh.render(bool);
		}
	}

	public Aircraft aircraft() {
		if (fm == null)
			return null;
		return (Aircraft) fm.actor;
	}

	public boolean isExistMirror() {
		return bExistMirror;
	}

	public void destroy() {
		if (isFocused())
			Main3D.cur3D().setView(fm.actor, true);
		fm = null;
		super.destroy();
	}

	public Cockpit(String string, String string_48_) {
		fm = _newAircraft.FM;
		pos = new ActorPosMove(this);
		if (string != null) {
			mesh = new HierMesh(string);
			int i = mesh.materialFind("MIRROR");
			if (i != -1) {
				bExistMirror = true;
				mirrorMat = mesh.material(i);
			}

			// TODO: Added by |ZUTI|
			// ------------------------------------------
			ZutiSupportMethods_Air.backupCockpit(this);
			// ------------------------------------------
		}
		nullMesh = new HierMesh("3DO/Cockpit/Nill/hier.him");
		nullMeshIL2 = new Mesh("3DO/Cockpit/null/mono.sim");
		try {
			acoustics = new Acoustics(string_48_);
			acoustics.setParent(Engine.worldAcoustics());
			initSounds();
		} catch (Exception exception) {
			System.out.println("Cockpit Acoustics NOT initialized: " + exception.getMessage());
		}
		if (this instanceof CockpitPilot)
			AircraftLH.printCompassHeading = false;
	}

	protected void createActorHashCode() {
		makeActorRealHashCode();
	}

	protected float cvt(float f, float f1, float f2, float f3, float f4) {
		f = Math.min(Math.max(f, f1), f2);
		return f3 + (f4 - f3) * (f - f1) / (f2 - f1);
	}

	protected float interp(float f, float f1, float f2) {
		return f1 + (f - f1) * f2;
	}

	protected float floatindex(float f, float[] fs) {
		int i = (int) f;
		if (i >= fs.length - 1)
			return fs[fs.length - 1];
		if (i < 0)
			return fs[0];
		if (i == 0) {
			if (f > 0.0F)
				return fs[0] + f * (fs[1] - fs[0]);
			return fs[0];
		}
		return fs[i] + f % i * (fs[i + 1] - fs[i]);
	}

	public boolean useRealisticNavigationInstruments() {
		return World.cur().diffCur.RealisticNavigationInstruments;
	}

	public static void lightningStrike(Point3d point3d) {
		lightningPoint = point3d;
		lightningStriked = true;
	}

	private void updateBeacons() {
		if (lightningStriked) {
			lightningStriked = false;
			V.sub(fm.Loc, lightningPoint);
			float f = (float) V.length();
			atmosphereInterference = cvt(f, 1000.0F, 9000.0F, 1.0F, 0.0F);
		} else if (atmosphereInterference > 0.01F)
			atmosphereInterference *= 0.98;
		if (bFocus) {
			Aircraft aircraft = aircraft();
			if (aircraft.FM.AS.listenLorenzBlindLanding)
				listenLorenzBlindLanding(aircraft);
			else if (aircraft.FM.AS.listenNDBeacon)
				listenNDBeacon(aircraft, false);
			else if (aircraft.FM.AS.listenRadioStation)
				listenNDBeacon(aircraft, true);
			else if (aircraft.FM.AS.listenYGBeacon) {
				ndBeaconRange = 1.0F;
				ndBeaconDirection = 0.0F;
				Actor actor = (Actor) Main.cur().mission.getBeacons(aircraft.getArmy())
						.get(aircraft.FM.AS.getBeacon() - 1);
				if (actor.isAlive())
					playYEYGMorseCode(aircraft, actor, "DWRKANUGMLFS".toCharArray());
			} else if (aircraft.FM.AS.hayrakeCarrier != null && aircraft.FM.AS.hayrakeCarrier.isAlive()) {
				ndBeaconRange = 1.0F;
				ndBeaconDirection = 0.0F;
				playYEYGMorseCode(aircraft, aircraft.FM.AS.hayrakeCarrier, aircraft.FM.AS.hayrakeCode.toCharArray());
			} else {
				ndBeaconRange = 1.0F;
				ndBeaconDirection = 0.0F;
			}
		}
	}

	private void playYEYGMorseCode(Aircraft aircraft, Actor actor, char[] cs) {
		float f = cvt(Time.current() % 30000.0F, 0.0F, 30000.0F, 0.0F, 360.0F);
		boolean bool = false;
		if (Time.current() % 300000.0F <= 30000.0F)
			bool = true;
		actor.pos.getAbs(tempPoint1);
		aircraft.pos.getAbs(tempPoint2);
		tempPoint2.sub(tempPoint1);
		float f1 = (float) Math.toDegrees(Math.atan2(tempPoint2.x, tempPoint2.y)) % 360.0F;
		while (f1 < 0.0F)
			f1 += 360.0F;
		while (f1 >= 360.0F)
			f1 -= 360.0F;
		float f2 = Math.abs(f - f1);
		Point3d point3d = new Point3d();
		point3d.x = actor.pos.getAbsPoint().x;
		point3d.y = actor.pos.getAbsPoint().y;
		point3d.z = actor.pos.getAbsPoint().z + 40.0;
		float f3 = 15.0F;
		if (f2 > f3) {
			BeaconGeneric.getSignalAttenuation(point3d, aircraft, false, false, true, true);
			aircraft.playYEYGCarrier(false, 0.0F);
			clearToPlay = true;
		} else {
			float f4 = BeaconGeneric.getSignalAttenuation(point3d, aircraft, false, false, true, false);
			if (f4 != 1.0F) {
				float f5 = (1.0F - f4) * ((f3 - f2) / f3);
				aircraft.playYEYGCarrier(true, f5 * 0.5F);
				int i = (int) f;
				if (i % 15 == 0)
					clearToPlay = true;
				if (i % 13 == 0 && (!aircraft.isMorseSequencePlaying() || clearToPlay)) {
					if (bool) {
						String string = Beacon.getBeaconID(aircraft.FM.AS.getBeacon() - 1);
						String string1 = "";
						if (morseCharsPlayed % 2 == 0)
							string1 = "" + string.charAt(0);
						else
							string1 = "" + string.charAt(1);
						aircraft.morseSequenceStart(string1, f5);
						clearToPlay = false;
						morseCharsPlayed++;
					} else {
						morseCharsPlayed = 0;
						float f6 = 0.0F;
						if (cs.length == 12)
							f6 = 0.033333335F * f;
						else if (cs.length == 24)
							f6 = 0.06666667F * f;
						if (f6 >= cs.length)
							f6 = 0.0F;
						char c = cs[(int) f6];
						String string = "" + c;
						aircraft.morseSequenceStart(string, f5);
						clearToPlay = false;
					}
				}
			}
		}
	}

	private void listenLorenzBlindLanding(Aircraft aircraft) {
		blData.reset();
		Beacon.LorenzBLBeacon lorenzblbeacon = (Beacon.LorenzBLBeacon) getBeacon();
		if (lorenzblbeacon == null || !lorenzblbeacon.isAlive())
			aircraft.stopMorseSounds();
		else {
			lorenzblbeacon.rideBeam(aircraft, blData);
			float f = blData.blindLandingAzimuthBP;
			float f1 = (float) Math.random() * (0.5F - blData.signalStrength);
			float f2 = (cvt(blData.signalStrength * 2.0F, 0.0F, 0.75F, 0.0F, 1.2F) - f1);
			float f3 = 12.0F;
			float f4 = 0.3F;
			float f5 = 0.0F;
			float f6 = 0.0F;
			if (f < f4 && f > -f4) {
				aircraft.playLorenzDash(false, 0.0F);
				aircraft.playLorenzDot(false, 0.0F);
				aircraft.playLorenzSolid(true, f2);
			} else {
				if (f > f3) {
					f5 = 1.0F;
					f6 = 0.0F;
				} else if (f < -f3) {
					f6 = 1.0F;
					f5 = 0.0F;
				} else {
					f5 = cvt(f, -f3 / 2.0F, f4 * 10.0F, 0.0F, 1.0F);
					f6 = cvt(f, -f4 * 10.0F, f3 / 2.0F, 1.0F, 0.0F);
				}
				aircraft.playLorenzSolid(true, 0.0F);
				aircraft.playLorenzDash(true, f6 * f2);
				aircraft.playLorenzDot(true, f5 * f2);
			}
		}
	}

	public boolean isOnBlindLandingMarker() {
		Aircraft aircraft = aircraft();
		if (blData.isOnInnerMarker) {
			aircraft.playLorenzInnerMarker(true, 1.0F);
			return true;
		}
		if (blData.isOnOuterMarker) {
			aircraft.playLorenzOuterMarker(true, 1.0F);
			return true;
		}
		aircraft.playLorenzInnerMarker(false, 0.0F);
		aircraft.playLorenzOuterMarker(false, 0.0F);
		return false;
	}

	public float getBeaconRange() {
		Aircraft aircraft = aircraft();
		if (aircraft.FM.AS.listenLorenzBlindLanding)
			return (7.0422F * (1.0F - blData.signalStrength
					+ World.Rnd().nextFloat(-atmosphereInterference * 2.0F, (atmosphereInterference * 2.0F))));
		if (useRealisticNavigationInstruments())
			return (ndBeaconRange + (float) Math.random() * 0.2F * ndBeaconRange
					+ World.Rnd().nextFloat(-atmosphereInterference * 2.0F, atmosphereInterference * 2.0F));
		return 1.0F;
	}

	public float getGlidePath() {
		Aircraft aircraft1 = aircraft();
		if (aircraft1.FM.AS.listenLorenzBlindLanding) {
			int i = fm.AS.getBeacon();
			ArrayList arraylist = Main.cur().mission.getBeacons(fm.actor.getArmy());
			Actor actor = (Actor) arraylist.get(i - 1);
			double d = actor.pos.getAbsPoint().z + 10D;

			float f = (1.0F - blData.signalStrength) * 100F;
			double d1 = Math.abs(aircraft1.pos.getAbsPoint().x - actor.pos.getAbsPoint().x);
			double d2 = Math.abs(aircraft1.pos.getAbsPoint().y - actor.pos.getAbsPoint().y);
			float f1 = 1700F + World.Rnd().nextFloat(-f, f);
			double d3 = Math.sqrt(d1 * d1 + d2 * d2) - f1;
			double d4 = aircraft1.pos.getAbsPoint().z - d;

			double d5 = Math.asin(d4 / d3);
			double d6 = glideScopeInRads - d5;
			float f2 = (float) Math.toDegrees(d6) * blData.signalStrength
					+ World.Rnd().nextFloat(-atmosphereInterference * 2.0F, atmosphereInterference * 2.0F);
			if (f2 > 1.0F)
				f2 = 1.0F;
			else if (f2 < -1F)
				f2 = -1F;
			return f2;
		} else {
			return 0.0F;
		}
	}

	public float getBeaconDirection() {
		Aircraft aircraft = aircraft();
		if (bFocus && aircraft.FM.AS.listenLorenzBlindLanding)
			return (blData.blindLandingAzimuthPB
					+ World.Rnd().nextFloat(-atmosphereInterference * 90.0F, atmosphereInterference * 90.0F));
		if (useRealisticNavigationInstruments() && Main.cur().mission.hasBeacons(fm.actor.getArmy()))
			return (ndBeaconDirection
					+ World.Rnd().nextFloat(-atmosphereInterference * 90.0F, atmosphereInterference * 90.0F));
		return 0.0F;
	}

	private Actor getBeacon() {
		int i = fm.AS.getBeacon();
		if (i == 0)
			return null;
		ArrayList arraylist = Main.cur().mission.getBeacons(fm.actor.getArmy());
		Actor actor = (Actor) arraylist.get(i - 1);
		if (actor instanceof TypeHasYGBeacon || actor instanceof TypeHasHayRake)
			return null;
		ArrayList arraylist1 = Main.cur().mission.getMeacons(fm.actor.getArmy());
		if (arraylist1.size() >= i && !(actor instanceof Beacon.LorenzBLBeacon)) {
			Actor actor1 = (Actor) arraylist1.get(i - 1);
			if (actor1.isAlive()) {
				distanceV.sub(actor1.pos.getAbsPoint(), fm.Loc);
				double d = distanceV.length();
				distanceV.sub(actor.pos.getAbsPoint(), fm.Loc);
				double d1 = distanceV.length();
				if (d < d1 || !actor.isAlive())
					actor = actor1;
			}
		}
		return actor;
	}

	private void listenNDBeacon(Aircraft aircraft, boolean bool) {
		Actor actor = getBeacon();
		if (actor == null || !actor.isAlive()) {
			ndBeaconRange = 1.0F;
			ndBeaconDirection = 0.0F;
			if (bool)
				CmdMusic.setCurrentVolume(0.001F);
			else
				aircraft.playBeaconCarrier(false, 0.0F);
		} else {
			tempPoint1.x = actor.pos.getAbsPoint().x;
			tempPoint1.y = actor.pos.getAbsPoint().y;
			tempPoint1.z = actor.pos.getAbsPoint().z + 20.0;
			aircraft.pos.getAbs(acPoint);
			acPoint.sub(tempPoint1);
			float f1 = ((float) Math.toDegrees(Math.atan2(acPoint.y, acPoint.x)) - aircraft.pos.getAbsOrient().getYaw())
					% 360.0F;
			while (f1 < 0.0F)
				f1 += 360.0F;
			while (f1 >= 360.0F)
				f1 -= 360.0F;
			if (f1 > 270.0F)
				f1 -= 360.0F;
			if (f1 > 90.0F)
				f1 = -(f1 - 180.0F);
			ndBeaconRange = BeaconGeneric.getSignalAttenuation(tempPoint1, aircraft, !bool, bool, false, false);
			if (Math.random() < 0.02)
				terrainAndNightError = BeaconGeneric.getTerrainAndNightError(aircraft);
			f1 += terrainAndNightError;
			float f2 = floatindex(cvt(1.0F - ndBeaconRange, 0.0F, 1.0F, 0.0F, 9.0F), volumeLogScale);
			float f3 = AudioDevice.vMusic.get();
			float f4 = (f3 + 1.0F) / 15.0F;
			if (!bool) {
				float f5 = (float) Math.random() * ndBeaconRange;
				float f6 = Time.current() % 60000.0F;
				if (f6 <= 500.0F && !aircraft.isMorseSequencePlaying()) {
					String string = Beacon.getBeaconID(aircraft.FM.AS.getBeacon() - 1);
					f4 = f2 * f4 * 0.75F;
					aircraft.morseSequenceStart(string, f4);
				} else {
					f4 = f2 * f4 * 0.75F - f5;
					aircraft.playBeaconCarrier(true, f4);
				}
			} else {
				CmdMusic.setCurrentVolume(f2);
				aircraft.playRadioStatic(true, ((-0.5F + (1.0F - f2 * f4)) * 2.0F));
			}
			ndBeaconDirection = f2 * f1 + (((float) Math.random() - 0.5F) * ndBeaconRange);
		}
	}

	private float getRadioCompassWaypoint(boolean flag, boolean flag1, boolean flag2) {
		Actor actor = getBeacon();
		if (actor == null || !actor.isAlive()) {
			if (flag2) {
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
		if (flag) {
			if (flag1)
				f = (float) Math.toDegrees(Math.atan2(-V.y, V.x));
			else
				f = (float) Math.toDegrees(Math.atan2(V.y, V.x));
		} else
			f = (float) Math.toDegrees(Math.atan2(V.x, V.y));
		while (f <= -180.0F)
			f += 180.0F;
		while (f > 180.0F)
			f -= 180.0F;
		f += terrainAndNightError;
		if (ndBeaconRange > 0.99)
			f = aircraft().FM.Or.azimut();
		else
			f += World.Rnd().nextFloat((-ndBeaconRange - atmosphereInterference * 5.0F),
					ndBeaconRange + (atmosphereInterference * 5.0F)) * 30.0F;
		return f;
	}

	private float getWaypoint(boolean flag, boolean flag1, float f, boolean flag2) {
		if (useRealisticNavigationInstruments()) {
			if (flag2) {
				if (Main.cur().mission.hasBeacons(fm.actor.getArmy())) {
					skip = !skip;
					if (skip)
						return prevWaypointF;
					float f1 = getRadioCompassWaypoint(flag, flag1, flag2);
					prevWaypointF = f1;
					return f1;
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
		float f2;
		if (flag) {
			if (flag1)
				f2 = (float) Math.toDegrees(Math.atan2(-V.y, V.x));
			else
				f2 = (float) Math.toDegrees(Math.atan2(V.y, V.x));
		} else
			f2 = (float) Math.toDegrees(Math.atan2(V.x, V.y));
		while (f2 <= -180.0F)
			f2 += 180.0F;
		while (f2 > 180.0F)
			f2 -= 180.0F;
		return f2 + World.Rnd().nextFloat(-f, f);
	}

	protected float waypointAzimuth() {
		return getWaypoint(false, false, 0.0F, false);
	}

	protected float waypointAzimuth(float f) {
		return getWaypoint(false, false, f, false);
	}

	protected float waypointAzimuthInvert(float f) {
		return getWaypoint(true, false, f, false);
	}

	protected float waypointAzimuthInvertMinus(float f) {
		return getWaypoint(true, true, f, false);
	}

	protected float radioCompassAzimuthInvertMinus() {
		return getWaypoint(true, true, 0.0F, true);
	}

	// TODO: |ZUTI| methods
	// ---------------------------------------
	protected Map zutiOriginalMeshesStates;
	// ---------------------------------------
}