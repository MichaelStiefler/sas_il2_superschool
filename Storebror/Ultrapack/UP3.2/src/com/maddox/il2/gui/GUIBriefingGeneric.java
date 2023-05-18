/* 4.10.1 class */
package com.maddox.il2.gui;

import java.util.ResourceBundle;

import com.maddox.JGP.Vector3f;
import com.maddox.gwindow.GBevel;
import com.maddox.gwindow.GColor;
import com.maddox.gwindow.GTexture;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowMessageBox;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.gwindow.GWindowScrollingDialogClient;
import com.maddox.il2.ai.Army;
import com.maddox.il2.ai.Front;
import com.maddox.il2.engine.CameraOrtho2D;
import com.maddox.il2.engine.GUIRenders;
import com.maddox.il2.engine.IconDraw;
import com.maddox.il2.engine.Land2DText;
import com.maddox.il2.engine.Land2Dn;
import com.maddox.il2.engine.LightEnvXY;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.Renders;
import com.maddox.il2.engine.TTFont;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.rts.HomePath;
import com.maddox.rts.RTSConf;
import com.maddox.rts.SectFile;
import com.maddox.util.NumberTokenizer;

public class GUIBriefingGeneric extends GameState
{
	public GUIClient		client;
	public DialogClient		dialogClient;
	public GUIInfoMenu		infoMenu;
	public GUIInfoName		infoName;
	public ScrollDescript	wScrollDescription;
	public Descript			wDescript;
	public GUIButton		bLoodout;
	public GUIButton		bDifficulty;
	public GUIButton		bPrev;
	public GUIButton		bNext;
	public String			textDescription;
	public String[]			textArmyDescription;
	public String			curMissionName;
	public int				curMissionNum	= -1;
	public String			curMapName;
	protected String		briefSound		= null;
	protected Main3D		main;
	protected GUIRenders	renders;
	protected RenderMap2D	renderMap2D;
	protected CameraOrtho2D	cameraMap2D;
	protected TTFont		gridFont;
	protected Mat			emptyMat;
	protected float[]		scale			= { 0.064F, 0.032F, 0.016F, 0.0080F, 0.0040F, 0.0020F, 0.0010F, 5.0E-4F, 2.5E-4F };
	protected int			scales			= scale.length;
	protected int			curScale		= scales - 1;
	protected int			curScaleDirect	= -1;
	protected float			landDX;
	protected float			landDY;
	private float[]			line2XYZ		= new float[6];
	private int				_gridCount;
	private int[]			_gridX			= new int[20];
	private int[]			_gridY			= new int[20];
	private int[]			_gridVal		= new int[20];
	protected boolean		bLPressed		= false;
	protected boolean		bRPressed		= false;

	//TODO: Added by |ZUTI|
	//-----------------------------------
	public GUIButton bZutiAcPositions;

	//-----------------------------------

	public class DialogClient extends GUIDialogClient
	{
		public boolean notify(GWindow gwindow, int i, int i_0_)
		{
			if (i != 2)
				return super.notify(gwindow, i, i_0_);
			if (gwindow == bPrev)
			{
				doBack();
				return true;
			}
			if (gwindow == bNext)
			{
				doNext();
				return true;
			}
			if (gwindow == bDifficulty)
			{
				doDiff();
				return true;
			}
			if (gwindow == bLoodout)
			{
				doLoodout();
				return true;
			}
			//TODO: Added by |ZUTI|
			//--------------------------------------
			if (gwindow == bZutiAcPositions)
			{
				com.maddox.il2.game.Main.stateStack().change(44);

				if (Main.cur().netServerParams != null && Main.cur().netServerParams.isMaster())
					new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20.0F, true, i18n("mds.info.crewPositions"), i18n("mds.info.crewHostLimit"), 3, 0.0F);

				return true;
			}
			//--------------------------------------
			
			return super.notify(gwindow, i, i_0_);
		}

		public void render()
		{
			super.render();
			
			GUISeparate.draw(this, GColor.Gray, x1024(32.0F), y1024(624.0F), x1024(924.0F), 2.5F);
			GUISeparate.draw(this, GColor.Gray, x1024(457.0F), y1024(686.0F), x1024(30.0F), 2.0F);
			GUISeparate.draw(this, GColor.Gray, x1024(537.0F), y1024(686.0F), x1024(30.0F), 2.0F);
			GUISeparate.draw(this, GColor.Gray, x1024(457.0F), y1024(640.0F), 1.0F, x1024(46.0F));
			GUISeparate.draw(this, GColor.Gray, x1024(567.0F), y1024(640.0F), 1.0F, x1024(46.0F));
			setCanvasColorWHITE();
			GUILookAndFeel guilookandfeel = (GUILookAndFeel) lookAndFeel();
			guilookandfeel.drawBevel(this, x1024(32.0F), y1024(32.0F), x1024(528.0F), y1024(560.0F), guilookandfeel.bevelComboDown, guilookandfeel.basicelements);
			setCanvasFont(0);
			setCanvasColor(GColor.Gray);
			
			//TODO: Added by |ZUTI|: render Crew text
			//--------------------------------------------------------
			if (Mission.isDogfight())
			{
				float x1 = dialogClient.x1024(755.0F);
				float y1 = dialogClient.y1024(633.0F);
				float x2 = dialogClient.x1024(170.0F);
				float y2 = dialogClient.y1024(48.0F);
				dialogClient.draw(x1, y1, x2, y2, 2, i18n("brief.Crew"));
			}
			//--------------------------------------------------------
			
			clientRender();
		}

		public void resized()
		{
			super.resized();
			if (renders != null)
			{
				GUILookAndFeel guilookandfeel = (GUILookAndFeel) lookAndFeel();
				GBevel gbevel = guilookandfeel.bevelComboDown;
				renders.setPosSize(x1024(32.0F) + gbevel.L.dx, y1024(32.0F) + gbevel.T.dy, x1024(528.0F) - gbevel.L.dx - gbevel.R.dx, y1024(560.0F) - gbevel.T.dy - gbevel.B.dy);
			}
		}

		public void setPosSize()
		{
			set1024PosSize(0.0F, 32.0F, 1024.0F, 736.0F);
			bPrev.setPosC(x1024(85.0F), y1024(689.0F));
			bDifficulty.setPosC(x1024(298.0F), y1024(689.0F));
			bLoodout.setPosC(x1024(768.0F), y1024(689.0F));
			
			//TODO: Added by |ZUTI|: add crew button
			//--------------------------------------------------------------------------------------
			if (Mission.isDogfight())
				bZutiAcPositions.setPosC(x1024(900.0F), y1024(689.0F));
			else
				bZutiAcPositions.setPosC(x1024(2000.0F), y1024(2000.0F));
			//--------------------------------------------------------------------------------------

			bNext.setPosC(x1024(512.0F), y1024(689.0F));
			wScrollDescription.setPosSize(x1024(592.0F), y1024(32.0F), x1024(400.0F), y1024(560.0F));
			clientSetPosSize();
		}
	}

	public class ScrollDescript extends GWindowScrollingDialogClient
	{
		public void created()
		{
			fixed = wDescript = createDescript(this);
			fixed.bNotify = true;
			bNotify = true;
		}

		public boolean notify(GWindow gwindow, int i, int i_1_)
		{
			if (super.notify(gwindow, i, i_1_))
				return true;
			this.notify(i, i_1_);
			return false;
		}

		public void resized()
		{
			if (wDescript != null)
				wDescript.computeSize();
			super.resized();
			if (vScroll.isVisible())
			{
				GBevel gbevel = ((GUILookAndFeel) lookAndFeel()).bevelComboDown;
				vScroll.setPos((win.dx - lookAndFeel().getVScrollBarW() - gbevel.R.dx), gbevel.T.dy);
				vScroll.setSize(lookAndFeel().getVScrollBarW(), win.dy - gbevel.T.dy - gbevel.B.dy);
			}
		}

		public void render()
		{
			setCanvasColorWHITE();
			GBevel gbevel = ((GUILookAndFeel) lookAndFeel()).bevelComboDown;
			lookAndFeel().drawBevel(this, 0.0F, 0.0F, win.dx, win.dy, gbevel, (((GUILookAndFeel) lookAndFeel()).basicelements), true);
		}
	}

	public class Descript extends GWindowDialogClient
	{
		public void render()
		{
			String string = textDescription();
			if (string != null)
			{
				GBevel gbevel = ((GUILookAndFeel) lookAndFeel()).bevelComboDown;
				setCanvasFont(0);
				setCanvasColorBLACK();
				root.C.clip.y += gbevel.T.dy;
				root.C.clip.dy -= gbevel.T.dy + gbevel.B.dy;
				drawLines(gbevel.L.dx + 2.0F, gbevel.T.dy + 2.0F, string, 0, string.length(), win.dx - gbevel.L.dx - gbevel.R.dx - 4.0F, root.C.font.height);
			}
		}

		public void computeSize()
		{
			String string = textDescription();
			if (string != null)
			{
				win.dx = parentWindow.win.dx;
				GBevel gbevel = ((GUILookAndFeel) lookAndFeel()).bevelComboDown;
				setCanvasFont(0);
				int i = computeLines(string, 0, string.length(), win.dx - gbevel.L.dx - gbevel.R.dx - 4.0F);
				win.dy = (root.C.font.height * (float) i + gbevel.T.dy + gbevel.B.dy + 4.0F);
				if (win.dy > parentWindow.win.dy)
				{
					win.dx = parentWindow.win.dx - lookAndFeel().getVScrollBarW();
					i = computeLines(string, 0, string.length(), (win.dx - gbevel.L.dx - gbevel.R.dx - 4.0F));
					win.dy = (root.C.font.height * (float) i + gbevel.T.dy + gbevel.B.dy + 4.0F);
				}
			}
			else
			{
				win.dx = parentWindow.win.dx;
				win.dy = parentWindow.win.dy;
			}
		}
	}

	public class RenderMap2D extends Render
	{
		public void preRender()
		{
			if (main.land2D != null)
				Front.preRender(false);
		}

		public void render()
		{
			if (main.land2D != null)
			{
				main.land2D.render();
				if (main.land2DText != null)
					main.land2DText.render();
				GUIBriefingGeneric.this.drawGrid2D();
				Front.render(false);
				int i = (int) Math.round(32.0 * (double) (GUIBriefingGeneric.this.renders.root.win.dx) / 1024.0);
				IconDraw.setScrSize(i, i);
				doRenderMap2D();
				SquareLabels.draw(cameraMap2D, Main3D.cur3D().land2D.worldOfsX(), Main3D.cur3D().land2D.worldOfsX(), Main3D.cur3D().land2D.mapSizeX());
			}
		}

		public RenderMap2D(Renders renders, float f)
		{
			super(renders, f);
			useClearDepth(false);
			useClearColor(false);
		}
	}

	public void _enter()
	{
		client.activateWindow();
		try
		{
			SectFile sectfile = Main.cur().currentMissionFile;
			briefSound = sectfile.get("MAIN", "briefSound");
			String string = Main.cur().currentMissionFile.fileName();
			String string_2_ = sectfile.get("MAIN", "MAP");
			if (!string.equals(curMissionName) || !string_2_.equals(curMapName) || curMissionNum != Main.cur().missionCounter || main.land2D == null)
			{
				dialogClient.resized();
				fillTextDescription();
				fillMap();
				Front.loadMission(sectfile);
				curMissionName = string;
				curMapName = string_2_;
				curMissionNum = Main.cur().missionCounter;
			}
		}
		catch (Exception exception)
		{
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		Front.setMarkersChanged();
		wScrollDescription.resized();
		if (wScrollDescription.vScroll.isVisible())
			wScrollDescription.vScroll.setPos(0.0F, true);
	}

	public void _leave()
	{
		client.hideWindow();
	}

	private void setPosCamera(float f, float f_3_)
	{
		float f_4_ = (float) ((double) (cameraMap2D.right - cameraMap2D.left) / cameraMap2D.worldScale);
		cameraMap2D.worldXOffset = (double) (f - f_4_ / 2.0F);
		float f_5_ = (float) ((double) (cameraMap2D.top - cameraMap2D.bottom) / cameraMap2D.worldScale);
		cameraMap2D.worldYOffset = (double) (f_3_ - f_5_ / 2.0F);
		clipCamera();
	}

	private void scaleCamera()
	{
		cameraMap2D.worldScale = (double) (scale[curScale] * renders.root.win.dx / 1024.0F);
	}

	private void clipCamera()
	{
		if (cameraMap2D.worldXOffset < -Main3D.cur3D().land2D.worldOfsX())
			cameraMap2D.worldXOffset = -Main3D.cur3D().land2D.worldOfsX();
		float f = (float) ((double) (cameraMap2D.right - cameraMap2D.left) / cameraMap2D.worldScale);
		if (cameraMap2D.worldXOffset > (Main3D.cur3D().land2D.mapSizeX() - Main3D.cur3D().land2D.worldOfsX() - (double) f))
			cameraMap2D.worldXOffset = (Main3D.cur3D().land2D.mapSizeX() - Main3D.cur3D().land2D.worldOfsX() - (double) f);
		if (cameraMap2D.worldYOffset < -Main3D.cur3D().land2D.worldOfsY())
			cameraMap2D.worldYOffset = -Main3D.cur3D().land2D.worldOfsY();
		float f_6_ = (float) ((double) (cameraMap2D.top - cameraMap2D.bottom) / cameraMap2D.worldScale);
		if (cameraMap2D.worldYOffset > (Main3D.cur3D().land2D.mapSizeY() - Main3D.cur3D().land2D.worldOfsY() - (double) f_6_))
			cameraMap2D.worldYOffset = (Main3D.cur3D().land2D.mapSizeY() - Main3D.cur3D().land2D.worldOfsY() - (double) f_6_);
	}

	private void computeScales()
	{
		float f = renders.win.dx * 1024.0F / renders.root.win.dx;
		float f_7_ = renders.win.dy * 768.0F / renders.root.win.dy;
		int i = 0;
		float f_8_ = 0.064F;
		for (/**/; i < scale.length; i++)
		{
			scale[i] = f_8_;
			float f_9_ = landDX * f_8_;
			if (f_9_ < f)
				break;
			float f_10_ = landDY * f_8_;
			if (f_10_ < f_7_)
				break;
			f_8_ /= 2.0F;
		}
		scales = i;
		if (scales < scale.length)
		{
			float f_11_ = f / landDX;
			float f_12_ = f_7_ / landDY;
			scale[i] = f_11_;
			if (f_12_ > f_11_)
				scale[i] = f_12_;
			scales = i + 1;
		}
		curScale = scales - 1;
		curScaleDirect = -1;
	}

	private void drawGrid2D()
	{
		int i = gridStep();
		int i_13_ = (int) ((cameraMap2D.worldXOffset + Main3D.cur3D().land2D.worldOfsX()) / (double) i);
		int i_14_ = (int) ((cameraMap2D.worldYOffset + Main3D.cur3D().land2D.worldOfsY()) / (double) i);
		double d = ((double) (cameraMap2D.right - cameraMap2D.left) / cameraMap2D.worldScale);
		double d_15_ = ((double) (cameraMap2D.top - cameraMap2D.bottom) / cameraMap2D.worldScale);
		int i_16_ = (int) (d / (double) i) + 2;
		int i_17_ = (int) (d_15_ / (double) i) + 2;
		float f = (float) ((((double) (i_13_ * i) - cameraMap2D.worldXOffset - Main3D.cur3D().land2D.worldOfsX()) * cameraMap2D.worldScale) + 0.5);
		float f_18_ = (float) ((((double) (i_14_ * i) - cameraMap2D.worldYOffset - Main3D.cur3D().land2D.worldOfsY()) * cameraMap2D.worldScale) + 0.5);
		float f_19_ = (float) ((double) (i_16_ * i) * cameraMap2D.worldScale);
		float f_20_ = (float) ((double) (i_17_ * i) * cameraMap2D.worldScale);
		float f_21_ = (float) ((double) i * cameraMap2D.worldScale);
		_gridCount = 0;
		Render.drawBeginLines(-1);
		for (int i_22_ = 0; i_22_ <= i_17_; i_22_++)
		{
			float f_23_ = f_18_ + (float) i_22_ * f_21_;
			int i_24_ = (i_22_ + i_14_) % 10 == 0 ? 192 : 127;
			line2XYZ[0] = f;
			line2XYZ[1] = f_23_;
			line2XYZ[2] = 0.0F;
			line2XYZ[3] = f + f_19_;
			line2XYZ[4] = f_23_;
			line2XYZ[5] = 0.0F;
			Render.drawLines(line2XYZ, 2, 1.0F, ~0xffffff | i_24_ << 16 | i_24_ << 8 | i_24_, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 0);
			if (i_24_ == 192)
				drawGridText(0, (int) f_23_, (i_14_ + i_22_) * i);
		}
		for (int i_25_ = 0; i_25_ <= i_16_; i_25_++)
		{
			float f_26_ = f + (float) i_25_ * f_21_;
			int i_27_ = (i_25_ + i_13_) % 10 == 0 ? 192 : 127;
			line2XYZ[0] = f_26_;
			line2XYZ[1] = f_18_;
			line2XYZ[2] = 0.0F;
			line2XYZ[3] = f_26_;
			line2XYZ[4] = f_18_ + f_20_;
			line2XYZ[5] = 0.0F;
			Render.drawLines(line2XYZ, 2, 1.0F, ~0xffffff | i_27_ << 16 | i_27_ << 8 | i_27_, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 0);
			if (i_27_ == 192)
				drawGridText((int) f_26_, 0, (i_13_ + i_25_) * i);
		}
		Render.drawEnd();
		drawGridText();
	}

	private int gridStep()
	{
		float f = cameraMap2D.right - cameraMap2D.left;
		float f_28_ = cameraMap2D.top - cameraMap2D.bottom;
		double d = (double) f;
		if (f_28_ < f)
			d = (double) f_28_;
		d /= cameraMap2D.worldScale;
		int i = 100000;
		for (int i_29_ = 0; i_29_ < 5; i_29_++)
		{
			if ((double) (i * 3) <= d)
				break;
			i /= 10;
		}
		return i;
	}

	private void drawGridText(int i, int i_30_, int i_31_)
	{
		if (i >= 0 && i_30_ >= 0 && i_31_ > 0 && _gridCount != 20)
		{
			_gridX[_gridCount] = i;
			_gridY[_gridCount] = i_30_;
			_gridVal[_gridCount] = i_31_;
			_gridCount++;
		}
	}

	private void drawGridText()
	{
		for (int i = 0; i < _gridCount; i++)
			gridFont.output(-4144960, (float) (_gridX[i] + 2), (float) (_gridY[i] + 2), 0.0F, (_gridVal[i] / 1000 + "." + _gridVal[i] % 1000 / 100));
		_gridCount = 0;
	}

	protected void doRenderMap2D()
	{
		/* empty */
	}

	protected void doMouseButton(int i, boolean bool, float f, float f_32_)
	{
		int i_33_ = i;
		if (renders != null)
		{
			/* empty */
		}
		if (i_33_ == 0)
		{
			bLPressed = bool;
			GUIRenders guirenders = renders;
			int i_34_;
			if (bLPressed)
			{
				if (renders != null)
				{
					/* empty */
				}
				i_34_ = 7;
			}
			else
			{
				if (renders != null)
				{
					/* empty */
				}
				i_34_ = 3;
			}
			guirenders.mouseCursor = i_34_;
		}
		else
		{
			int i_35_ = i;
			if (renders != null)
			{
				/* empty */
			}
			if (i_35_ == 1 && scales > 1)
			{
				bRPressed = bool;
				if (bRPressed && !bLPressed)
				{
					float f_36_ = (float) (cameraMap2D.worldXOffset + (double) f / cameraMap2D.worldScale);
					float f_37_ = (float) (cameraMap2D.worldYOffset + ((double) (renders.win.dy - f_32_ - 1.0F) / cameraMap2D.worldScale));
					curScale += curScaleDirect;
					if (curScaleDirect < 0)
					{
						if (curScale < 0)
						{
							curScale = 1;
							curScaleDirect = 1;
						}
					}
					else if (curScale == scales)
					{
						curScale = scales - 2;
						curScaleDirect = -1;
					}
					scaleCamera();
					f_36_ -= ((double) (f - renders.win.dx / 2.0F) / cameraMap2D.worldScale);
					f_37_ += ((double) (f_32_ - renders.win.dy / 2.0F) / cameraMap2D.worldScale);
					setPosCamera(f_36_, f_37_);
				}
			}
		}
	}

	protected void doMouseMove(float f, float f_38_)
	{
		if (bLPressed && renders.mouseCursor == 7)
		{
			cameraMap2D.worldXOffset -= (double) renders.root.mouseStep.dx / cameraMap2D.worldScale;
			cameraMap2D.worldYOffset += (double) renders.root.mouseStep.dy / cameraMap2D.worldScale;
			clipCamera();
		}
	}

	protected void createRenderWindow(GWindow gwindow)
	{
		renders = new GUIRenders(gwindow, 0.0F, 0.0F, 1.0F, 1.0F, false)
		{
			public void mouseButton(int i, boolean bool, float f, float f_44_)
			{
				doMouseButton(i, bool, f, f_44_);
			}

			public void mouseMove(float f, float f_45_)
			{
				doMouseMove(f, f_45_);
			}
		};
		renders.mouseCursor = 3;
		renders.bNotify = true;
		cameraMap2D = new CameraOrtho2D();
		cameraMap2D.worldScale = (double) scale[curScale];
		renderMap2D = new RenderMap2D(renders.renders, 1.0F);
		renderMap2D.setCamera(cameraMap2D);
		renderMap2D.setShow(true);
		LightEnvXY lightenvxy = new LightEnvXY();
		renderMap2D.setLightEnv(lightenvxy);
		lightenvxy.sun().setLight(0.5F, 0.5F, 1.0F, 1.0F, 1.0F, 0.8F);
		Vector3f vector3f = new Vector3f(1.0F, -2.0F, -1.0F);
		vector3f.normalize();
		lightenvxy.sun().set(vector3f);
		gridFont = TTFont.font[1];
		emptyMat = Mat.New("icons/empty.mat");
		main = Main3D.cur3D();
	}

	protected void fillMap() throws Exception
	{
		SectFile sectfile = Main.cur().currentMissionFile;
		String string = sectfile.get("MAIN", "MAP");
		if (string == null)
			throw new Exception("No MAP in mission file ");
		SectFile sectfile_46_ = new SectFile("maps/" + string);
		String string_47_ = sectfile_46_.get("MAP", "TypeMap", (String) null);
		if (string_47_ == null)
			throw new Exception("Bad MAP description in mission file ");
		NumberTokenizer numbertokenizer = new NumberTokenizer(string_47_);
		if (numbertokenizer.hasMoreTokens())
		{
			numbertokenizer.next();
			if (numbertokenizer.hasMoreTokens())
				string_47_ = numbertokenizer.next();
		}
		string_47_ = HomePath.concatNames("maps/" + string, string_47_);
		int[] is = new int[3];
		if (!Mat.tgaInfo(string_47_, is))
			throw new Exception("Bad MAP description in mission file ");
		landDX = (float) is[0] * 200.0F;
		landDY = (float) is[1] * 200.0F;
		if (main.land2D != null)
		{
			if (!main.land2D.isDestroyed())
				main.land2D.destroy();
			main.land2D = null;
		}
		int i = sectfile_46_.sectionIndex("MAP2D");
		if (i >= 0)
		{
			int i_48_ = sectfile_46_.vars(i);
			if (i_48_ > 0)
			{
				main.land2D = new Land2Dn(string, (double) landDX, (double) landDY);
				landDX = (float) main.land2D.mapSizeX();
				landDY = (float) main.land2D.mapSizeY();
			}
		}
		if (main.land2DText == null)
			main.land2DText = new Land2DText();
		else
			main.land2DText.clear();
		int i_49_ = sectfile_46_.sectionIndex("text");
		if (i_49_ >= 0 && sectfile_46_.vars(i_49_) > 0)
		{
			String string_50_ = sectfile_46_.var(i_49_, 0);
			main.land2DText.load(HomePath.concatNames("maps/" + string, string_50_));
		}
		computeScales();
		scaleCamera();
		setPosCamera(landDX / 2.0F, landDY / 2.0F);
	}

	protected void prepareTextDescription(int i)
	{
		if (textDescription != null)
		{
			if (textArmyDescription == null || textArmyDescription.length != i)
				textArmyDescription = new String[i];
			for (int i_51_ = 0; i_51_ < i; i_51_++)
			{
				textArmyDescription[i_51_] = null;
				prepareTextDescriptionArmy(i_51_);
			}
		}
	}

	private void prepareTextDescriptionArmy(int i)
	{
		String string = (Army.name(i) + ">").toUpperCase();
		int i_52_ = 0;
		int i_53_ = textDescription.length();
		StringBuffer stringbuffer = new StringBuffer();
		while (i_52_ < i_53_)
		{
			int i_54_ = textDescription.indexOf("<ARMY", i_52_);
			if (i_54_ >= i_52_)
			{
				if (i_54_ > i_52_)
					subString(stringbuffer, textDescription, i_52_, i_54_);
				int i_55_ = textDescription.indexOf("</ARMY>", i_54_);
				if (i_55_ == -1)
					i_55_ = i_53_;
				for (i_54_ += "<ARMY".length(); (i_54_ < i_53_ && Character.isSpaceChar(textDescription.charAt(i_54_))); i_54_++)
				{
					/* empty */
				}
				if (i_54_ == i_53_)
				{
					i_52_ = i_53_;
					break;
				}
				if (textDescription.startsWith(string, i_54_))
				{
					i_54_ += string.length();
					if (i_54_ < i_55_ && textDescription.charAt(i_54_) == '\n')
						i_54_++;
					subString(stringbuffer, textDescription, i_54_, i_55_);
				}
				i_52_ = i_55_ + "</ARMY>".length();
				if (i_52_ < i_53_ && textDescription.charAt(i_52_) == '\n')
					i_52_++;
			}
			else
			{
				subString(stringbuffer, textDescription, i_52_, i_53_);
				i_52_ = i_53_;
			}
		}
		textArmyDescription[i] = new String(stringbuffer);
	}

	private void subString(StringBuffer stringbuffer, String string, int i, int i_56_)
	{
		while (i < i_56_)
			stringbuffer.append(string.charAt(i++));
	}

	protected void fillTextDescription()
	{
		try
		{
			String string = Main.cur().currentMissionFile.fileName();
			for (int i = string.length() - 1; i > 0; i--)
			{
				char c = string.charAt(i);
				if (c == '\\' || c == '/')
					break;
				if (c == '.')
				{
					string = string.substring(0, i);
					break;
				}
			}
			ResourceBundle resourcebundle = ResourceBundle.getBundle(string, RTSConf.cur.locale);
			textDescription = resourcebundle.getString("Description");
		}
		catch (Exception exception)
		{
			textDescription = null;
			textArmyDescription = null;
		}
	}

	protected String textDescription()
	{
		return textDescription;
	}

	protected Descript createDescript(GWindow gwindow)
	{
		return (Descript) gwindow.create(new Descript());
	}

	protected void doNext()
	{

	}

	protected void doDiff()
	{

	}

	protected void doBack()
	{

	}

	protected void doLoodout()
	{

	}

	protected void clientRender()
	{

	}

	protected void clientSetPosSize()
	{

	}

	protected void clientInit(GWindowRoot gwindowroot)
	{

	}

	protected String infoMenuInfo()
	{
		return "Info Menu";
	}

	protected void init(GWindowRoot gwindowroot)
	{
		client = (GUIClient) gwindowroot.create(new GUIClient());
		dialogClient = (DialogClient) client.create(new DialogClient());
		infoMenu = (GUIInfoMenu) client.create(new GUIInfoMenu());
		infoMenu.info = infoMenuInfo();
		infoName = (GUIInfoName) client.create(new GUIInfoName());
		createRenderWindow(dialogClient);
		dialogClient.create(wScrollDescription = new ScrollDescript());
		GTexture gtexture = ((GUILookAndFeel) gwindowroot.lookAndFeel()).buttons2;
		bPrev = (GUIButton) dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96.0F, 48.0F, 48.0F));
		bDifficulty = (GUIButton) dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48.0F, 48.0F, 48.0F));
		bLoodout = (GUIButton) dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48.0F, 48.0F, 48.0F));
		
		//TODO: Added by |ZUTI|
		//-----------------------------------
		bZutiAcPositions = new GUIButton(dialogClient, gtexture, 0.0F, 48.0F, 48.0F, 48.0F);
		dialogClient.addControl(bZutiAcPositions);
		//-----------------------------------
		
		bNext = (GUIButton) dialogClient.addDefault(new GUIButton(dialogClient, gtexture, 0.0F, 192.0F, 48.0F, 48.0F));
		clientInit(gwindowroot);
		dialogClient.activateWindow();
		client.hideWindow();
	}

	public GUIBriefingGeneric(int i)
	{
		super(i);
	}
}