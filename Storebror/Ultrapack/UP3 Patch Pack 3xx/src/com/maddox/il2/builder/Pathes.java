/* 4.10.1 class */
package com.maddox.il2.builder;

import com.maddox.JGP.Point2d;
import com.maddox.il2.ai.Army;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.CameraOrtho2D;
import com.maddox.il2.engine.IconDraw;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Render;
import com.maddox.rts.Message;
import com.maddox.rts.Property;

public class Pathes extends Actor
{
	public PPoint			currentPPoint;
	private Point2d			p2d			= new Point2d();
	private float[]			lineNXYZ	= new float[6];
	private int				lineNCounter;
	private StringBuffer	strBuf		= new StringBuffer();
	private Object[]		pathes		= new Object[1];
	private Object[]		points		= new Object[1];

	//TODO: Added by |ZUTI|
	//-------------------------------------
	private boolean zutiIsPluginLoaded = false;
	private boolean zutiIsInfrastructure = false;
	//-------------------------------------
	
	public void renderMap2D(boolean bool, int i)
	{
		Render.drawBeginLines(-1);
		CameraOrtho2D cameraortho2d = (CameraOrtho2D) Render.currentCamera();
		IconDraw.setScrSize(i, i);
		double d = (double) (cameraortho2d.left - (float) (i / 2));
		double d_0_ = (double) (cameraortho2d.bottom - (float) (i / 2));
		double d_1_ = (double) (cameraortho2d.right + (float) (i / 2));
		double d_2_ = (double) (cameraortho2d.top + (float) (i / 2));
		int i_3_ = 0;
		int i_4_ = 0;
		int i_5_ = 1;
		this.pathes = getOwnerAttached(this.pathes);
		for (int i_6_ = 0; i_6_ < this.pathes.length; i_6_++)
		{
			Path path = (Path) this.pathes[i_6_];
			this.pathes[i_6_] = null;
			if (path == null)
				break;
			path.renderPoints = 0;
			if (path.isDrawing())
			{
				points = path.getOwnerAttached(points);
				PPoint ppoint = null;
				boolean bool_7_ = false;
				if (lineNXYZ.length / 3 <= points.length)
					lineNXYZ = new float[(points.length + 1) * 3];
				lineNCounter = 0;
				for (int i_8_ = 0; i_8_ < points.length; i_8_++)
				{
					PPoint ppoint_9_ = (PPoint) points[i_8_];
					points[i_8_] = null;
					if (ppoint_9_ == null)
						break;
					Plugin.builder.project2d(ppoint_9_.pos.getCurrentPoint(), p2d);
					ppoint_9_.screenX = p2d.x;
					ppoint_9_.screenY = p2d.y;
					if (ppoint_9_.screenX < d)
					{
						if (ppoint_9_.screenY < d_0_)
							ppoint_9_.screenQuad = 6;
						else if (ppoint_9_.screenY > d_2_)
							ppoint_9_.screenQuad = 0;
						else
							ppoint_9_.screenQuad = 7;
					}
					else if (ppoint_9_.screenX > d_1_)
					{
						if (ppoint_9_.screenY < d_0_)
							ppoint_9_.screenQuad = 4;
						else if (ppoint_9_.screenY > d_2_)
							ppoint_9_.screenQuad = 2;
						else
							ppoint_9_.screenQuad = 3;
					}
					else if (ppoint_9_.screenY < d_0_)
						ppoint_9_.screenQuad = 5;
					else if (ppoint_9_.screenY > d_2_)
						ppoint_9_.screenQuad = 1;
					else
						ppoint_9_.screenQuad = 8;
					if (ppoint_9_ instanceof PNodes)
					{
						PNodes pnodes = (PNodes) ppoint_9_;
						if (pnodes.posXYZ != null)
						{
							boolean bool_10_ = (currentPPoint != null && currentPPoint.getOwner() == path);
							if (lineNXYZ.length / 3 <= pnodes.posXYZ.length / 4)
								lineNXYZ = new float[(pnodes.posXYZ.length / 4 + 1) * 3];
							lineNCounter = 0;
							for (int i_11_ = 0; i_11_ < pnodes.posXYZ.length / 4; i_11_++)
							{
								Plugin.builder.project2d((double) pnodes.posXYZ[i_11_ * 4 + 0], (double) pnodes.posXYZ[i_11_ * 4 + 1], (double) pnodes.posXYZ[i_11_ * 4 + 2], p2d);
								lineNXYZ[lineNCounter * 3 + 0] = (float) p2d.x;
								lineNXYZ[lineNCounter * 3 + 1] = (float) p2d.y;
								lineNXYZ[lineNCounter * 3 + 2] = 0.0F;
								lineNCounter++;
							}
							if (bool_10_)
							{
								i_4_ = Builder.colorSelected();
								i_5_ = 3;
							}
							else
							{
								i_4_ = Army.color(path.getArmy());
								i_5_ = 1;
							}
							Render.drawLines(lineNXYZ, lineNCounter, (float) i_5_, i_4_, (Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE | Mat.BLEND), 3);
						}
					}
					else if (ppoint != null)
					{
						boolean bool_12_;
						for (bool_12_ = (ppoint_9_.screenQuad == 8 || ppoint.screenQuad == 8); (!bool_12_ && ppoint_9_.screenQuad != ppoint.screenQuad
								&& ppoint_9_.screenQuad != (ppoint.screenQuad + 1 & 0x7) && ppoint.screenQuad != (ppoint_9_.screenQuad + 1 & 0x7) && ((ppoint_9_.screenQuad & 0x1) != 0
								|| (ppoint.screenQuad & 0x1) != 0 || ((ppoint_9_.screenQuad != (ppoint.screenQuad + 2 & 0x7)) && (ppoint.screenQuad != (ppoint_9_.screenQuad + 2 & 0x7))))); bool_12_ = true)
						{
							/* empty */
						}
						if (bool_12_)
						{
							if (!bool_7_)
							{
								bool_7_ = true;
								boolean bool_13_ = (currentPPoint != null && currentPPoint.getOwner() == path);
								if (bool_13_)
								{
									i_4_ = Builder.colorSelected();
									i_5_ = 3;
								}
								else
								{
									i_4_ = Army.color(path.getArmy());
									i_5_ = 1;
								}
								lineNXYZ[lineNCounter * 3 + 0] = (float) ppoint.screenX;
								lineNXYZ[lineNCounter * 3 + 1] = (float) ppoint.screenY;
								lineNXYZ[lineNCounter * 3 + 2] = 0.0F;
								lineNCounter++;
							}
							lineNXYZ[lineNCounter * 3 + 0] = (float) ppoint_9_.screenX;
							lineNXYZ[lineNCounter * 3 + 1] = (float) ppoint_9_.screenY;
							lineNXYZ[lineNCounter * 3 + 2] = 0.0F;
							lineNCounter++;
						}
						else if (bool_7_)
						{
							bool_7_ = false;
							Render.drawLines(lineNXYZ, lineNCounter, (float) i_5_, i_4_, (Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE | Mat.BLEND), 3);
						}
					}
					if (ppoint_9_.screenQuad == 8)
					{
						path.renderPoints++;
						i_3_++;
					}
					ppoint = ppoint_9_;
				}
				if (bool_7_)
				{
					bool_7_ = false;
					Render.drawLines(lineNXYZ, lineNCounter, (float) i_5_, i_4_, (Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE | Mat.BLEND), 3);
				}
			}
		}
		Render.drawEnd();
		if (i_3_ != 0)
		{
			this.pathes = getOwnerAttached(this.pathes);
			for (int i_14_ = 0; i_14_ < this.pathes.length; i_14_++)
			{
				Path path = (Path) this.pathes[i_14_];
				
				//TODO: Added by |ZUTI|
				//------------------------------
				zutiIsInfrastructure = false;
				if( path != null && path.getArmy() == 0 )
					zutiIsInfrastructure = true;
				//------------------------------
				
				this.pathes[i_14_] = null;
				if (path == null)
					break;
				if (path.renderPoints != 0)
				{
					points = path.getOwnerAttached(points);
					for (int i_15_ = 0; i_15_ < points.length; i_15_++)
					{
						PPoint ppoint = (PPoint) points[i_15_];
						points[i_15_] = null;
						if (ppoint == null)
							break;
						if (ppoint.screenQuad == 8)
						{
							if (currentPPoint == ppoint && (currentPPoint.getOwner() == ppoint.getOwner()))
								i_4_ = Builder.colorSelected();
							else if (Plugin.builder.isMiltiSelected(ppoint))
								i_4_ = -16776961;
							else
								i_4_ = Army.color(path.getArmy());
							IconDraw.setColor(i_4_);

							//TODO: Altered by |ZUTI|: draw these icons ONLY if PlMapAirdrome plugin is loaded!
							//---------------------------------------------------------------------------------
							if (zutiIsPluginLoaded || !zutiIsInfrastructure)
								IconDraw.render(ppoint, ppoint.screenX, ppoint.screenY);
							//---------------------------------------------------------------------------------

							strBuf.delete(0, strBuf.length());
							strBuf.append(i_15_);
							if (Plugin.builder.conf.bShowTime && !(ppoint instanceof PAirdrome))
							{
								strBuf.append('(');
								int i_16_ = ((int) Math.round(ppoint.time / 60.0) + (int) (World.getTimeofDay() * 60.0F));
								strBuf.append(i_16_ / 60 % 24);
								strBuf.append(':');
								int i_17_ = i_16_ % 60;
								if (i_17_ < 10)
									strBuf.append('0');
								strBuf.append(i_17_);
								strBuf.append(')');
							}
							
							
							//TODO: Added by |ZUTI|: display numbers near lines only if appropriate plugin is loaded
							//Added only the if condition!
							//-----------------------------------------------------------------
							if (zutiIsPluginLoaded || !zutiIsInfrastructure)
							{
								Plugin.builder.smallFont.output(i_4_, (float) ((int) ppoint.screenX + IconDraw.scrSizeX() / 2 + 2), (float) ((int) ppoint.screenY - IconDraw.scrSizeY() / 2 - 2), 0.0F, strBuf.toString());
								if (Plugin.builder.conf.bShowName && path.isNamed())
								{
									if (path instanceof PathAir)
										Plugin.builder.smallFont.output(i_4_, (float) ((int) ppoint.screenX + IconDraw.scrSizeX() / 2 + 2), (float) ((int) ppoint.screenY
												+ Plugin.builder.smallFont.height() - Plugin.builder.smallFont.descender() - IconDraw.scrSizeY() / 2 - 2), 0.0F, ((PathAir) path).typedName);
									else if (path instanceof PathChief)
										Plugin.builder.smallFont
												.output(i_4_, (float) ((int) ppoint.screenX + IconDraw.scrSizeX() / 2 + 2), (float) ((int) ppoint.screenY + Plugin.builder.smallFont.height()
														- Plugin.builder.smallFont.descender() - IconDraw.scrSizeY() / 2 - 2), 0.0F, Property.stringValue(path, "i18nName", ""));
									else
										Plugin.builder.smallFont.output(i_4_, (float) ((int) ppoint.screenX + IconDraw.scrSizeX() / 2 + 2), (float) ((int) ppoint.screenY
												+ Plugin.builder.smallFont.height() - Plugin.builder.smallFont.descender() - IconDraw.scrSizeY() / 2 - 2), 0.0F, path.name());
								}
							}
							//-----------------------------------------------------------------
						}
					}
				}
			}
		}
		this.pathes = getOwnerAttached(this.pathes);
		for (int i_18_ = 0; i_18_ < this.pathes.length; i_18_++)
		{
			Path path = (Path) this.pathes[i_18_];
			this.pathes[i_18_] = null;
			if (path == null)
				break;
			if (path instanceof PathAir)
			{
				points = path.getOwnerAttached(points);
				for (int i_19_ = 0; i_19_ < points.length; i_19_++)
				{
					PAir pair = (PAir) points[i_19_];
					points[i_19_] = null;
					if (pair == null)
						break;
					if (Actor.isValid(pair.getTarget()))
					{
						Plugin.builder.project2d(pair.getTarget().pos.getCurrentPoint(), p2d);
						int i_20_ = Plugin.builder.conf.iconSize;
						Render.drawTile((float) (p2d.x - (double) (i_20_ / 2)), (float) (p2d.y - (double) (i_20_ / 2)), (float) i_20_, (float) i_20_, 0.0F, Plugin.targetIcon, -16711936, 0.0F, 0.0F,
								1.0F, 1.0F);
					}
				}
			}
		}
		if (i_3_ != 0 && Actor.isValid(Path.player) && Path.player.renderPoints != 0)
		{
			points = Path.player.getOwnerAttached(points);
			for (int i_21_ = 0; i_21_ < points.length; i_21_++)
			{
				PPoint ppoint = (PPoint) points[i_21_];
				if (ppoint == null)
					break;
				if (ppoint.screenQuad == 8)
				{
					Render.drawTile((float) ppoint.screenX, (float) ppoint.screenY, (float) i, (float) i, 0.0F, Plugin.playerIcon, Army.color(Path.player.getArmy()), 0.0F, 1.0F, 1.0F, -1.0F);
					break;
				}
			}
			for (int i_22_ = 0; i_22_ < points.length; i_22_++)
				points[i_22_] = null;
		}
	}

	public void renderMap2DTargetLines()
	{
		Render.drawBeginLines(-1);
		this.pathes = getOwnerAttached(this.pathes);
		for (int i = 0; i < this.pathes.length; i++)
		{
			Path path = (Path) this.pathes[i];
			this.pathes[i] = null;
			if (path == null)
				break;
			if (path instanceof PathAir)
			{
				points = path.getOwnerAttached(points);
				for (int i_23_ = 0; i_23_ < points.length; i_23_++)
				{
					PAir pair = (PAir) points[i_23_];
					points[i_23_] = null;
					if (pair == null)
						break;
					if (Actor.isValid(pair.getTarget()))
					{
						Plugin.builder.project2d(pair.pos.getCurrentPoint(), p2d);
						lineNXYZ[0] = (float) p2d.x;
						lineNXYZ[1] = (float) p2d.y;
						lineNXYZ[2] = 0.0F;
						Plugin.builder.project2d(pair.getTarget().pos.getCurrentPoint(), p2d);
						lineNXYZ[3] = (float) p2d.x;
						lineNXYZ[4] = (float) p2d.y;
						lineNXYZ[5] = 0.0F;
						Render.drawLines(lineNXYZ, 2, 3.0F, -16711936, (Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE | Mat.BLEND), 3);
					}
				}
			}
		}
		Render.drawEnd();
	}

	public Object getSwitchListener(Message message)
	{
		return this;
	}

	public Pathes()
	{
		flags |= 0x2000;
		
		//TODO: Added by |ZUTI|: set this flag
		//--------------------------------------------
		if( Plugin.getPlugin("MapAirdrome") != null )
			zutiIsPluginLoaded = true;
		//--------------------------------------------
	}

	protected void createActorHashCode()
	{
		makeActorRealHashCode();
	}

	public void clear()
	{
		this.pathes = getOwnerAttached(this.pathes);
		for (int i = 0; i < this.pathes.length; i++)
		{
			Actor actor = (Actor) this.pathes[i];
			if (actor == null)
				break;
			actor.destroy();
			this.pathes[i] = null;
		}
		currentPPoint = null;
	}

	public void destroy()
	{
		if (!isDestroyed())
		{
			this.pathes = getOwnerAttached(this.pathes);
			for (int i = 0; i < this.pathes.length; i++)
			{
				Actor actor = (Actor) this.pathes[i];
				if (actor == null)
					break;
				actor.destroy();
				this.pathes[i] = null;
			}
			currentPPoint = null;
			super.destroy();
		}
	}
}
