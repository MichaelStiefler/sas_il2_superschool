package com.maddox.il2.objects.air;

import com.maddox.il2.engine.*;
import com.maddox.rts.*;

public class CockpitMi24_GUNNER extends CockpitGunner {

	
	protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        super.doFocusLeave();
    }
    
    protected void reflectPlaneToModel()
    {
    }
	
	public void moveGun(Orient orient) {
		super.moveGun(orient);
		mesh.chunkSetAngles("Body", 0.0F, 0.0F, -1F);
		mesh.chunkSetAngles("Turret1A", -orient.getYaw(), 0.0F, 0.0F);
		mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage(), 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (isRealMode())
			if (!aiTurret().bIsOperable) {
				orient.setYPR(0.0F, 0.0F, 0.0F);
			} else {
				float f = orient.getYaw();
				float f1 = orient.getTangage();
				if (f < -60F)
					f = -60F;
				if (f > 60F)
					f = 60F;
				if (f1 > 60F)
					f1 = 60F;
				if (f1 < -60F)
					f1 = -60F;
				orient.setYPR(f, f1, 0.0F);
				orient.wrap();
			}
	}

	protected void interpTick() {
		if (isRealMode()) {
			if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
				bGunFire = false;
			fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
		}
	}

	public void doGunFire(boolean flag) {
		if (isRealMode()) {
			if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
				bGunFire = false;
			else
				bGunFire = flag;
			fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
		}
	}

	public void reflectCockpitState() {
	}

	public CockpitMi24_GUNNER() {
		super("3DO/Cockpit/A-20G-TGun/TGunnerMi24.him", "he111_gunner");
	}
	
	public void reflectWorldToInstruments(float f)
    {
		 reflectPlaneToModel();
    }

	static {
		Property.set(CLASS.THIS(), "aiTuretNum", 1);
		Property.set(CLASS.THIS(), "weaponControlNum", 11);
		Property.set(CLASS.THIS(), "astatePilotIndx", 1);
	}
}