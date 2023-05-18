package com.maddox.il2.objects.weapons;
import com.maddox.JGP.Color3f;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.air.Aircraft;

public abstract class MGunAdjustableMiniGun extends MGunAircraftGeneric
{

    public MGunAdjustableMiniGun()
    {
        rpm = 0.0F;
        maxRPM = 0.0F;
        minRPM = 0.0F;
        stepRPM = 0.0F;
        vtemp = new Vector3d();
        vwldtemp = new Vector3d();
    }

    public float incRPM()
    {
        if(rpm + stepRPM <= maxRPM)
            rpm += stepRPM;
        else
            rpm = maxRPM;
        return rpm;
    }

    public float decRPM()
    {
        if(rpm - stepRPM >= minRPM)
            rpm -= stepRPM;
        else
            rpm = minRPM;
        return rpm;
    }

    public float resRPM()
    {
        rpm = maxRPM;
        return rpm;
    }

    public void shots(int i, float f)
    {
        _shotStep = (float)prop.bulletsCluster / (rpm / 60F);
        super.shots(i, f);
    }

    public void doStartBullet(double d)
    {
        com.maddox.il2.engine.Actor actor = getOwner();
        boolean flag = false;
        if(actor == World.getPlayerAircraft() && World.cur().diffCur.Realistic_Gunnery && (((Aircraft)actor).FM instanceof RealFlightModel))
            flag = true;
        if(flag)
        {
            vtemp.set(((RealFlightModel)((Aircraft)actor).FM).producedAM);
            vwldtemp.set(((Aircraft)actor).FM.Vwld);
            ftemp = ((RealFlightModel)((Aircraft)actor).FM).producedShakeLevel;
        }
        super.doStartBullet(d);
        if(flag)
        {
            ((RealFlightModel)((Aircraft)actor).FM).producedAM.set(factorizedVector(((RealFlightModel)((Aircraft)actor).FM).producedAM, vtemp, RECOIL_FACTOR));
            ((Aircraft)actor).FM.Vwld.set(factorizedVector(((Aircraft)actor).FM.Vwld, vwldtemp, RECOIL_FACTOR));
            ((RealFlightModel)((Aircraft)actor).FM).producedShakeLevel = ((RealFlightModel)((Aircraft)actor).FM).producedShakeLevel * RECOIL_FACTOR + ftemp * 0.917F;
        }
    }

    private Vector3d factorizedVector(Vector3d vector3d, Vector3d vector3d1, float f)
    {
        vector3d.x = vector3d.x * (double)f + vector3d1.x * (double)(1.0F - f);
        vector3d.y = vector3d.y * (double)f + vector3d1.y * (double)(1.0F - f);
        vector3d.z = vector3d.z * (double)f + vector3d1.z * (double)(1.0F - f);
        return vector3d;
    }

    public GunProperties createProperties()
    {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bCannon = false;
        gunproperties.bUseHookAsRel = true;
        gunproperties.bEnablePause = true;
        gunproperties.fire = null;
        gunproperties.shells = null;
        gunproperties.sound = "weapon.MiniGun";
        gunproperties.shotFreq = 50F;
        gunproperties.bullets = 8000;
        gunproperties.maxDeltaAngle = 0.255F;
        gunproperties.fireMesh = "3DO/Effects/GunFire/12mm/mono.sim";
        gunproperties.sprite = "3DO/Effects/GunFire/12mm/GunFlare.eff";
        gunproperties.smoke = "effects/smokes/MachineGun.eff";
        gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
        gunproperties.emitI = 2.5F;
        gunproperties.emitR = 1.5F;
        gunproperties.emitTime = 0.03F;
        gunproperties.aimMinDist = 10F;
        gunproperties.aimMaxDist = 1000F;
        gunproperties.weaponType = -1;
        gunproperties.traceFreq = 2;
        gunproperties.bulletsCluster = 2;
        int i = 2;
        i = i * 5 + 4;
        gunproperties.bullet = new BulletProperties[i];
        for(int j = 0; j < i; j++)
            gunproperties.bullet[j] = new BulletProperties();

        for(int k = 0; k < i - 4; k += 5)
        {
            gunproperties.bullet[k + 0].massa = 0.046F;
            gunproperties.bullet[k + 0].kalibr = 0.0001181215F;
            gunproperties.bullet[k + 0].speed = 870F;
            gunproperties.bullet[k + 0].power = 0.0F;
            gunproperties.bullet[k + 0].powerType = 0;
            gunproperties.bullet[k + 0].powerRadius = 0.0F;
            gunproperties.bullet[k + 0].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
            gunproperties.bullet[k + 0].traceTrail = "3DO/Effects/TEXTURES/fumeefine.eff";
            gunproperties.bullet[k + 0].traceColor = 0xf90000ff;
            gunproperties.bullet[k + 0].timeLife = 6.5F;
            gunproperties.bullet[k + 1].massa = 0.041F;
            gunproperties.bullet[k + 1].kalibr = 0.0001185215F;
            gunproperties.bullet[k + 1].speed = 900F;
            gunproperties.bullet[k + 1].power = 0.0022F;
            gunproperties.bullet[k + 1].powerType = 0;
            gunproperties.bullet[k + 1].powerRadius = 0.0F;
            gunproperties.bullet[k + 1].traceMesh = null;
            gunproperties.bullet[k + 1].traceTrail = null;
            gunproperties.bullet[k + 1].traceColor = 0;
            gunproperties.bullet[k + 1].timeLife = 6.3F;
            gunproperties.bullet[k + 2].massa = 0.046F;
            gunproperties.bullet[k + 2].kalibr = 0.0001181215F;
            gunproperties.bullet[k + 2].speed = 870F;
            gunproperties.bullet[k + 2].power = 0.0F;
            gunproperties.bullet[k + 2].powerType = 0;
            gunproperties.bullet[k + 2].powerRadius = 0.0F;
            gunproperties.bullet[k + 2].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
            gunproperties.bullet[k + 2].traceTrail = "3DO/Effects/TEXTURES/fumeefine.eff";
            gunproperties.bullet[k + 2].traceColor = 0xf90000ff;
            gunproperties.bullet[k + 2].timeLife = 6.5F;
            gunproperties.bullet[k + 3].massa = 0.041F;
            gunproperties.bullet[k + 3].kalibr = 0.0001185215F;
            gunproperties.bullet[k + 3].speed = 900F;
            gunproperties.bullet[k + 3].power = 0.0022F;
            gunproperties.bullet[k + 3].powerType = 0;
            gunproperties.bullet[k + 3].powerRadius = 0.0F;
            gunproperties.bullet[k + 3].traceMesh = null;
            gunproperties.bullet[k + 3].traceTrail = null;
            gunproperties.bullet[k + 3].traceColor = 0;
            gunproperties.bullet[k + 3].timeLife = 6.3F;
            gunproperties.bullet[k + 4].massa = 0.042F;
            gunproperties.bullet[k + 4].kalibr = 0.0001182215F;
            gunproperties.bullet[k + 4].speed = 820F;
            gunproperties.bullet[k + 4].power = 0.001F;
            gunproperties.bullet[k + 4].powerType = 0;
            gunproperties.bullet[k + 4].powerRadius = 0.0F;
            gunproperties.bullet[k + 4].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
            gunproperties.bullet[k + 4].traceTrail = "3DO/Effects/TEXTURES/fumeefine.eff";
            gunproperties.bullet[k + 4].traceColor = 0xf90000ff;
            gunproperties.bullet[k + 4].timeLife = 6.5F;
        }

        gunproperties.bullet[i - 4].massa = 0.043F;
        gunproperties.bullet[i - 4].kalibr = 0.0001209675F;
        gunproperties.bullet[i - 4].speed = 890F;
        gunproperties.bullet[i - 4].power = 0.002F;
        gunproperties.bullet[i - 4].powerType = 0;
        gunproperties.bullet[i - 4].powerRadius = 0.0F;
        gunproperties.bullet[i - 4].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
        gunproperties.bullet[i - 4].traceTrail = "Effects/Smokes/SmokeBlack_BuletteTrail.eff";
        gunproperties.bullet[i - 4].traceColor = 0xd90000ff;
        gunproperties.bullet[i - 4].timeLife = 6.5F;
        gunproperties.bullet[i - 3].massa = 0.043F;
        gunproperties.bullet[i - 3].kalibr = 0.0001105062F;
        gunproperties.bullet[i - 3].speed = 890F;
        gunproperties.bullet[i - 3].power = 0.002F;
        gunproperties.bullet[i - 3].powerType = 0;
        gunproperties.bullet[i - 3].powerRadius = 0.0F;
        gunproperties.bullet[i - 3].traceMesh = null;
        gunproperties.bullet[i - 3].traceTrail = null;
        gunproperties.bullet[i - 3].traceColor = 0;
        gunproperties.bullet[i - 3].timeLife = 6.52F;
        gunproperties.bullet[i - 2].massa = 0.043F;
        gunproperties.bullet[i - 2].kalibr = 0.0001105062F;
        gunproperties.bullet[i - 2].speed = 890F;
        gunproperties.bullet[i - 2].power = 0.0009768F;
        gunproperties.bullet[i - 2].powerType = 0;
        gunproperties.bullet[i - 2].powerRadius = 0.15F;
        gunproperties.bullet[i - 2].traceMesh = null;
        gunproperties.bullet[i - 2].traceTrail = null;
        gunproperties.bullet[i - 2].traceColor = 0;
        gunproperties.bullet[i - 1].timeLife = 6.5F;
        gunproperties.bullet[i - 1].massa = 0.043F;
        gunproperties.bullet[i - 1].kalibr = 0.0001105062F;
        gunproperties.bullet[i - 1].speed = 890F;
        gunproperties.bullet[i - 1].power = 0.002F;
        gunproperties.bullet[i - 1].powerType = 0;
        gunproperties.bullet[i - 1].powerRadius = 0.0F;
        gunproperties.bullet[i - 1].traceMesh = null;
        gunproperties.bullet[i - 1].traceTrail = null;
        gunproperties.bullet[i - 1].traceColor = 0;
        gunproperties.bullet[i - 1].timeLife = 6.25F;
        return gunproperties;
    }

    protected float rpm;
    protected float maxRPM;
    protected float minRPM;
    protected float stepRPM;
    private static final float RECOIL_FACTOR = 0.083F;
    private Vector3d vtemp;
    private Vector3d vwldtemp;
    private float ftemp;
}
