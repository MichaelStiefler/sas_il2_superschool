/*Modified Pylon class for the SAS Engine Mod*/

/*
 * Changelog:
 * 11th/Apr./2016: adding Pylons' properties of massa and dragCx
 * 04th/Oct./2020: Engine mod 2.9.x hook position in _Hardpoint refering
 */

package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.*;
import com.maddox.rts.Message;
import com.maddox.rts.Property;


public class Pylon extends ActorMesh
	implements BulletEmitter
{

	public BulletEmitter detach(HierMesh hiermesh, int i) {
		if(isDestroyed())
			return GunEmpty.get();
		if(i == -1 || i == chunkIndx) {
			destroy();
			return GunEmpty.get();
		} else {
			return this;
		}
	}

	public boolean isEnablePause() {
		return false;
	}

	public boolean isPause() {
		return false;
	}

	public void setPause(boolean flag) {
	}

	public float bulletMassa() {
		return 0.0F;
	}

	public int countBullets() {
		return 0;
	}

	public boolean haveBullets() {
		return false;
	}

	public void loadBullets() {
	}

	public void loadBullets(int i) {
	}

	public void _loadBullets(int i) {
	}

	public Class bulletClass() {
		return null;
	}

	public void setBulletClass(Class class1) {
	}

	public boolean isShots() {
		return false;
	}

	public void shots(int i, float f) {
	}

	public void shots(int i) {
	}

	public String getHookName() {
		return hookName;
	}

	public Object getSwitchListener(Message message) {
		return this;
	}

		// +++ Engine MOD massa and dragCx
	public float getMassa() {
		return massa;
	}

	public float setMassa(float f) {
		return massa = f;
	}

	public float getDragCx() {
		return dragCx;
	}

	public float setDragCx(float f) {
		return dragCx = f;
	}

	public boolean isMinusDrag() {
		return bMinusDrag;
	}

	public boolean setMinusDrag(boolean b) {
		return bMinusDrag = b;
	}
		// --- Engine MOD massa and dragCx

	public Pylon() {
		setMesh(MeshShared.get(Property.stringValue(getClass(), "mesh", null)));
		collide(false);
		drawing(true);

		// +++ Engine MOD massa and dragCx
		massa = Property.floatValue(getClass(), "massa", -1F);
		dragCx = Property.floatValue(getClass(), "dragCx", -1F);
		int i = Property.intValue(getClass(), "bMinusDrag", 0);
		bMinusDrag = (i == 1);
		// --- Engine MOD massa and dragCx
	}

	public void set(Actor actor, String s, Loc loc) {
		set(actor, s);
	}

	public void set(Actor actor, String s, String s1) {
		set(actor, s);
	}

	public void set(Actor actor, String s) {
		hookName = s;
		setOwner(actor);
		if(s != null) {
			Hook hook = actor.findHook(s);
			// +++ western: Engine mod 2.9.x Pylon hook position in _Hardpoint refering
			HookNamed hookatc = null;
			try {
				hookatc = (HookNamed)actor.findHook("_Hardpoint");
			} catch(Exception exception) { }
			if(hookatc == null)
				pos.setBase(actor, hook, false);
			else
				pos.setBase(actor, hook, false, hookatc);
			// ---
			pos.changeHookToRel();
			chunkIndx = hook.chunkNum();
		} else {
			pos.setBase(actor, null, false);
			chunkIndx = -1;
		}
		visibilityAsBase(true);
		pos.setUpdateEnable(false);
		pos.reset();
	}

	private String hookName;
	private int chunkIndx;
	private float massa;
	private float dragCx;
	private boolean bMinusDrag;
}
