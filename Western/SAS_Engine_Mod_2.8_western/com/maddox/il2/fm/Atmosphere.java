/*Modified Atmosphere class for the SAS Engine Mod*/
package com.maddox.il2.fm;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.ObjectsLogLevel;

public class Atmosphere {

	public Atmosphere() {
		g = 9.8F;
		P0 = 101300F;
		T0 = 288.16F;
		ro0 = 1.225F;
		Mu0 = 1.825E-006F;
	}

	public static final float g() {
		return World.cur().Atm.g;
	}

	public static final float P0() {
		return World.cur().Atm.P0;
	}

	public static final float T0() {
		return World.cur().Atm.T0;
	}

	public static final float ro0() {
		return World.cur().Atm.ro0;
	}

	public static final float Mu0() {
		return World.cur().Atm.Mu0;
	}

	private static final float poly(float af[], float f) {
		return (((af[4] * f + af[3]) * f + af[2]) * f + af[1]) * f + af[0];
	}

	public static final void set(float f, float f1) {
		f *= 133.2895F;
		f1 += 273.16F;
		if (Engine.cur == null || Engine.cur.world == null) return;
		int i = Engine.land().config.month;
		if (Engine.land().config.declin < 0) i += 6;
		if (i > 12) i -= 12;
		float f2 = Math.abs(Engine.land().config.declin) / 30F;
		if (Engine.land().config.climate == "HumidSubtropical") {
			switch (i) {
			case 1: // '\001'
				f1 += (int) (8F * f2);
				break;

			case 2: // '\002'
				f1 += (int) (10F * f2);
				break;

			case 3: // '\003'
				f1 += (int) (7F * f2);
				break;

			case 4: // '\004'
				f1 += (int) (3F * f2);
				break;

			case 6: // '\006'
				f1 -= (int) (3F * f2);
				break;

			case 7: // '\007'
				f1 -= (int) (5F * f2);
				break;

			case 8: // '\b'
				f1 -= (int) (8F * f2);
				break;

			case 9: // '\t'
				f1 -= (int) (5F * f2);
				break;

			case 11: // '\013'
				f1 += (int) (4F * f2);
				break;

			case 12: // '\f'
				f1 += (int) (6F * f2);
				break;
			}
			i = Mission.curMonth();
			if (Engine.land().config.declin < 0) i += 6;
			if (i > 12) i -= 12;
			switch (i) {
			case 1: // '\001'
				f1 -= (int) (8F * f2);
				break;

			case 2: // '\002'
				f1 -= (int) (10F * f2);
				break;

			case 3: // '\003'
				f1 -= (int) (7F * f2);
				break;

			case 4: // '\004'
				f1 -= (int) (3F * f2);
				break;

			case 6: // '\006'
				f1 += (int) (3F * f2);
				break;

			case 7: // '\007'
				f1 += (int) (5F * f2);
				break;

			case 8: // '\b'
				f1 += (int) (8F * f2);
				break;

			case 9: // '\t'
				f1 += (int) (5F * f2);
				break;

			case 11: // '\013'
				f1 -= (int) (4F * f2);
				break;

			case 12: // '\f'
				f1 -= (int) (6F * f2);
				break;
			}
			int j = (int) Math.floor(World.getTimeofDay());
			switch (j) {
			case 0: // '\0'
				f1 -= 10F;
				break;

			case 1: // '\001'
				f1 -= 10F;
				break;

			case 2: // '\002'
				f1 -= 11F;
				break;

			case 3: // '\003'
				f1 -= 11F;
				break;

			case 4: // '\004'
				f1 -= 12F;
				break;

			case 5: // '\005'
				f1 -= 12F;
				break;

			case 6: // '\006'
				f1 -= 12F;
				break;

			case 7: // '\007'
				f1 -= 10F;
				break;

			case 8: // '\b'
				f1 -= 8F;
				break;

			case 9: // '\t'
				f1 -= 5F;
				break;

			case 10: // '\n'
				f1 -= 3F;
				break;

			case 11: // '\013'
				f1 -= 2.0F;
				break;

			case 12: // '\f'
				f1--;
				break;

			case 15: // '\017'
				f1--;
				break;

			case 16: // '\020'
				f1 -= 2.0F;
				break;

			case 17: // '\021'
				f1 -= 3F;
				break;

			case 18: // '\022'
				f1 -= 5F;
				break;

			case 19: // '\023'
				f1 -= 7F;
				break;

			case 20: // '\024'
				f1 -= 8F;
				break;

			case 21: // '\025'
				f1 -= 8F;
				break;

			case 22: // '\026'
				f1 -= 9F;
				break;

			case 23: // '\027'
				f1 -= 9F;
				break;
			}
		} else if (Engine.land().config.climate == "Subtropics") {
			switch (i) {
			case 1: // '\001'
				f1 += (int) (4F * f2);
				break;

			case 2: // '\002'
				f1 += (int) (5F * f2);
				break;

			case 3: // '\003'
				f1 += (int) (3.5F * f2);
				break;

			case 4: // '\004'
				f1 += (int) (1.5F * f2);
				break;

			case 6: // '\006'
				f1 -= (int) (1.5F * f2);
				break;

			case 7: // '\007'
				f1 -= (int) (2.5F * f2);
				break;

			case 8: // '\b'
				f1 -= (int) (4F * f2);
				break;

			case 9: // '\t'
				f1 -= (int) (2.5F * f2);
				break;

			case 11: // '\013'
				f1 += (int) (2.0F * f2);
				break;

			case 12: // '\f'
				f1 += (int) (3F * f2);
				break;
			}
			i = Mission.curMonth();
			if (Engine.land().config.declin < 0) i += 6;
			if (i > 12) i -= 12;
			switch (i) {
			case 1: // '\001'
				f1 -= (int) (4F * f2);
				break;

			case 2: // '\002'
				f1 -= (int) (5F * f2);
				break;

			case 3: // '\003'
				f1 -= (int) (3.5F * f2);
				break;

			case 4: // '\004'
				f1 -= (int) (1.5F * f2);
				break;

			case 6: // '\006'
				f1 += (int) (1.5F * f2);
				break;

			case 7: // '\007'
				f1 += (int) (2.5F * f2);
				break;

			case 8: // '\b'
				f1 += (int) (4F * f2);
				break;

			case 9: // '\t'
				f1 += (int) (2.5F * f2);
				break;

			case 11: // '\013'
				f1 -= (int) (2.0F * f2);
				break;

			case 12: // '\f'
				f1 -= (int) (3F * f2);
				break;
			}
			int k = (int) Math.floor(World.getTimeofDay());
			switch (k) {
			case 0: // '\0'
				f1 -= 10F;
				break;

			case 1: // '\001'
				f1 -= 10F;
				break;

			case 2: // '\002'
				f1 -= 11F;
				break;

			case 3: // '\003'
				f1 -= 11F;
				break;

			case 4: // '\004'
				f1 -= 12F;
				break;

			case 5: // '\005'
				f1 -= 12F;
				break;

			case 6: // '\006'
				f1 -= 12F;
				break;

			case 7: // '\007'
				f1 -= 10F;
				break;

			case 8: // '\b'
				f1 -= 8F;
				break;

			case 9: // '\t'
				f1 -= 5F;
				break;

			case 10: // '\n'
				f1 -= 3F;
				break;

			case 11: // '\013'
				f1 -= 2.0F;
				break;

			case 12: // '\f'
				f1--;
				break;

			case 15: // '\017'
				f1--;
				break;

			case 16: // '\020'
				f1 -= 2.0F;
				break;

			case 17: // '\021'
				f1 -= 3F;
				break;

			case 18: // '\022'
				f1 -= 5F;
				break;

			case 19: // '\023'
				f1 -= 7F;
				break;

			case 20: // '\024'
				f1 -= 8F;
				break;

			case 21: // '\025'
				f1 -= 8F;
				break;

			case 22: // '\026'
				f1 -= 9F;
				break;

			case 23: // '\027'
				f1 -= 9F;
				break;
			}
		} else if (Engine.land().config.climate == "TropicalRainforest") {
			switch (i) {
			case 1: // '\001'
				f1 += 3F;
				break;

			case 2: // '\002'
				f1 += 2.0F;
				break;

			case 3: // '\003'
				f1++;
				break;

			case 6: // '\006'
				f1--;
				break;

			case 9: // '\t'
				f1++;
				break;

			case 10: // '\n'
				f1++;
				break;

			case 11: // '\013'
				f1 += 2.0F;
				break;

			case 12: // '\f'
				f1 += 3F;
				break;
			}
			i = Mission.curMonth();
			if (Engine.land().config.declin < 0) i += 6;
			if (i > 12) i -= 12;
			switch (i) {
			case 1: // '\001'
				f1 -= 3F;
				break;

			case 2: // '\002'
				f1 -= 2.0F;
				break;

			case 3: // '\003'
				f1--;
				break;

			case 6: // '\006'
				f1++;
				break;

			case 9: // '\t'
				f1--;
				break;

			case 10: // '\n'
				f1--;
				break;

			case 11: // '\013'
				f1 -= 2.0F;
				break;

			case 12: // '\f'
				f1 -= 3F;
				break;
			}
			int l = (int) Math.floor(World.getTimeofDay());
			switch (l) {
			case 0: // '\0'
				f1 -= 8F;
				break;

			case 1: // '\001'
				f1 -= 9F;
				break;

			case 2: // '\002'
				f1 -= 9F;
				break;

			case 3: // '\003'
				f1 -= 9F;
				break;

			case 4: // '\004'
				f1 -= 9F;
				break;

			case 5: // '\005'
				f1 -= 9F;
				break;

			case 6: // '\006'
				f1 -= 9F;
				break;

			case 7: // '\007'
				f1 -= 7F;
				break;

			case 8: // '\b'
				f1 -= 5F;
				break;

			case 9: // '\t'
				f1 -= 4F;
				break;

			case 10: // '\n'
				f1 -= 3F;
				break;

			case 11: // '\013'
				f1 -= 2.0F;
				break;

			case 12: // '\f'
				f1--;
				break;

			case 15: // '\017'
				f1--;
				break;

			case 16: // '\020'
				f1 -= 2.0F;
				break;

			case 17: // '\021'
				f1 -= 3F;
				break;

			case 18: // '\022'
				f1 -= 4F;
				break;

			case 19: // '\023'
				f1 -= 5F;
				break;

			case 20: // '\024'
				f1 -= 6F;
				break;

			case 21: // '\025'
				f1 -= 7F;
				break;

			case 22: // '\026'
				f1 -= 8F;
				break;

			case 23: // '\027'
				f1 -= 8F;
				break;
			}
		} else if (Engine.land().config.climate == "TropicalSavanna") {
			switch (i) {
			case 1: // '\001'
				f1 += (int) (6F * f2);
				break;

			case 2: // '\002'
				f1 += (int) (4F * f2);
				break;

			case 3: // '\003'
				f1 += (int) (2.0F * f2);
				break;

			case 4: // '\004'
				f1 -= (int) (2.0F * f2);
				break;

			case 6: // '\006'
				f1 += (int) (2.0F * f2);
				break;

			case 7: // '\007'
				f1 += (int) (2.0F * f2);
				break;

			case 8: // '\b'
				f1 += (int) (2.0F * f2);
				break;

			case 9: // '\t'
				f1 += (int) (2.0F * f2);
				break;

			case 10: // '\n'
				f1 += (int) (4F * f2);
				break;

			case 11: // '\013'
				f1 += (int) (4F * f2);
				break;

			case 12: // '\f'
				f1 += (int) (6F * f2);
				break;
			}
			i = Mission.curMonth();
			if (Engine.land().config.declin < 0) i += 6;
			if (i > 12) i -= 12;
			switch (i) {
			case 1: // '\001'
				f1 -= (int) (6F * f2);
				break;

			case 2: // '\002'
				f1 -= (int) (4F * f2);
				break;

			case 3: // '\003'
				f1 -= (int) (2.0F * f2);
				break;

			case 4: // '\004'
				f1 += (int) (2.0F * f2);
				break;

			case 6: // '\006'
				f1 -= (int) (2.0F * f2);
				break;

			case 7: // '\007'
				f1 -= (int) (2.0F * f2);
				break;

			case 8: // '\b'
				f1 -= (int) (2.0F * f2);
				break;

			case 9: // '\t'
				f1 -= (int) (2.0F * f2);
				break;

			case 10: // '\n'
				f1 -= (int) (4F * f2);
				break;

			case 11: // '\013'
				f1 -= (int) (4F * f2);
				break;

			case 12: // '\f'
				f1 -= (int) (6F * f2);
				break;
			}
			int i1 = (int) Math.floor(World.getTimeofDay());
			switch (i1) {
			case 0: // '\0'
				f1 -= 10F;
				break;

			case 1: // '\001'
				f1 -= 10F;
				break;

			case 2: // '\002'
				f1 -= 10F;
				break;

			case 3: // '\003'
				f1 -= 10F;
				break;

			case 4: // '\004'
				f1 -= 10F;
				break;

			case 5: // '\005'
				f1 -= 10F;
				break;

			case 6: // '\006'
				f1 -= 9F;
				break;

			case 7: // '\007'
				f1 -= 7F;
				break;

			case 8: // '\b'
				f1 -= 6F;
				break;

			case 9: // '\t'
				f1 -= 4F;
				break;

			case 10: // '\n'
				f1 -= 2.0F;
				break;

			case 11: // '\013'
				f1--;
				break;

			case 15: // '\017'
				f1--;
				break;

			case 16: // '\020'
				f1 -= 2.0F;
				break;

			case 17: // '\021'
				f1 -= 4F;
				break;

			case 18: // '\022'
				f1 -= 6F;
				break;

			case 19: // '\023'
				f1 -= 7F;
				break;

			case 20: // '\024'
				f1 -= 8F;
				break;

			case 21: // '\025'
				f1 -= 8F;
				break;

			case 22: // '\026'
				f1 -= 9F;
				break;

			case 23: // '\027'
				f1 -= 9F;
				break;
			}
		} else if (Engine.land().config.climate == "HumidContinental") {
			switch (i) {
			case 1: // '\001'
				f1 += (int) (13F * f2);
				break;

			case 2: // '\002'
				f1 += (int) (12F * f2);
				break;

			case 3: // '\003'
				f1 += (int) (9F * f2);
				break;

			case 4: // '\004'
				f1 += (int) (4F * f2);
				break;

			case 6: // '\006'
				f1 -= (int) (2.8F * f2);
				break;

			case 7: // '\007'
				f1 -= (int) (5.6F * f2);
				break;

			case 8: // '\b'
				f1 -= (int) (7F * f2);
				break;

			case 9: // '\t'
				f1 -= (int) (3.5F * f2);
				break;

			case 10: // '\n'
				f1 += (int) (0.7F * f2);
				break;

			case 11: // '\013'
				f1 += (int) (6.3F * f2);
				break;

			case 12: // '\f'
				f1 += (int) (10.5F * f2);
				break;
			}
			i = Mission.curMonth();
			if (Engine.land().config.declin < 0) i += 6;
			if (i > 12) i -= 12;
			switch (i) {
			case 1: // '\001'
				f1 -= (int) (13F * f2);
				break;

			case 2: // '\002'
				f1 -= (int) (12F * f2);
				break;

			case 3: // '\003'
				f1 -= (int) (9F * f2);
				break;

			case 4: // '\004'
				f1 -= (int) (4F * f2);
				break;

			case 6: // '\006'
				f1 += (int) (2.8F * f2);
				break;

			case 7: // '\007'
				f1 += (int) (5.6F * f2);
				break;

			case 8: // '\b'
				f1 += (int) (7F * f2);
				break;

			case 9: // '\t'
				f1 += (int) (3.5F * f2);
				break;

			case 10: // '\n'
				f1 -= (int) (0.7F * f2);
				break;

			case 11: // '\013'
				f1 -= (int) (6.3F * f2);
				break;

			case 12: // '\f'
				f1 -= (int) (10.5F * f2);
				break;
			}
			int j1 = (int) Math.floor(World.getTimeofDay());
			switch (j1) {
			case 0: // '\0'
				f1 -= 10F;
				break;

			case 1: // '\001'
				f1 -= 10F;
				break;

			case 2: // '\002'
				f1 -= 10F;
				break;

			case 3: // '\003'
				f1 -= 10F;
				break;

			case 4: // '\004'
				f1 -= 10F;
				break;

			case 5: // '\005'
				f1 -= 10F;
				break;

			case 6: // '\006'
				f1 -= 9F;
				break;

			case 7: // '\007'
				f1 -= 7F;
				break;

			case 8: // '\b'
				f1 -= 6F;
				break;

			case 9: // '\t'
				f1 -= 4F;
				break;

			case 10: // '\n'
				f1 -= 2.0F;
				break;

			case 11: // '\013'
				f1--;
				break;

			case 15: // '\017'
				f1--;
				break;

			case 16: // '\020'
				f1 -= 2.0F;
				break;

			case 17: // '\021'
				f1 -= 4F;
				break;

			case 18: // '\022'
				f1 -= 6F;
				break;

			case 19: // '\023'
				f1 -= 7F;
				break;

			case 20: // '\024'
				f1 -= 8F;
				break;

			case 21: // '\025'
				f1 -= 8F;
				break;

			case 22: // '\026'
				f1 -= 9F;
				break;

			case 23: // '\027'
				f1 -= 9F;
				break;
			}
		} else if (Engine.land().config.climate == "Mediterranean") {
			switch (i) {
			case 1: // '\001'
				f1 += (int) (9F * f2);
				break;

			case 2: // '\002'
				f1 += (int) (8F * f2);
				break;

			case 3: // '\003'
				f1 += (int) (6F * f2);
				break;

			case 4: // '\004'
				f1 += (int) (2.0F * f2);
				break;

			case 6: // '\006'
				f1 -= (int) (2.0F * f2);
				break;

			case 7: // '\007'
				f1 -= (int) (5F * f2);
				break;

			case 9: // '\t'
				f1 += (int) (3F * f2);
				break;

			case 10: // '\n'
				f1 += (int) (6F * f2);
				break;

			case 11: // '\013'
				f1 += (int) (9F * f2);
				break;

			case 12: // '\f'
				f1 += (int) (10F * f2);
				break;
			}
			i = Mission.curMonth();
			if (Engine.land().config.declin < 0) i += 6;
			if (i > 12) i -= 12;
			switch (i) {
			case 1: // '\001'
				f1 -= (int) (9F * f2);
				break;

			case 2: // '\002'
				f1 -= (int) (8F * f2);
				break;

			case 3: // '\003'
				f1 -= (int) (6F * f2);
				break;

			case 4: // '\004'
				f1 -= (int) (2.0F * f2);
				break;

			case 6: // '\006'
				f1 += (int) (2.0F * f2);
				break;

			case 7: // '\007'
				f1 += (int) (5F * f2);
				break;

			case 9: // '\t'
				f1 -= (int) (3F * f2);
				break;

			case 10: // '\n'
				f1 -= (int) (6F * f2);
				break;

			case 11: // '\013'
				f1 -= (int) (9F * f2);
				break;

			case 12: // '\f'
				f1 -= (int) (10F * f2);
				break;
			}
			int k1 = (int) Math.floor(World.getTimeofDay());
			switch (k1) {
			case 0: // '\0'
				f1 -= 10F;
				break;

			case 1: // '\001'
				f1 -= 10F;
				break;

			case 2: // '\002'
				f1 -= 10F;
				break;

			case 3: // '\003'
				f1 -= 10F;
				break;

			case 4: // '\004'
				f1 -= 10F;
				break;

			case 5: // '\005'
				f1 -= 10F;
				break;

			case 6: // '\006'
				f1 -= 9F;
				break;

			case 7: // '\007'
				f1 -= 7F;
				break;

			case 8: // '\b'
				f1 -= 6F;
				break;

			case 9: // '\t'
				f1 -= 4F;
				break;

			case 10: // '\n'
				f1 -= 2.0F;
				break;

			case 11: // '\013'
				f1--;
				break;

			case 15: // '\017'
				f1--;
				break;

			case 16: // '\020'
				f1 -= 2.0F;
				break;

			case 17: // '\021'
				f1 -= 4F;
				break;

			case 18: // '\022'
				f1 -= 6F;
				break;

			case 19: // '\023'
				f1 -= 7F;
				break;

			case 20: // '\024'
				f1 -= 8F;
				break;

			case 21: // '\025'
				f1 -= 8F;
				break;

			case 22: // '\026'
				f1 -= 9F;
				break;

			case 23: // '\027'
				f1 -= 9F;
				break;
			}
		} else if (Engine.land().config.climate == "Subarctic") {
			switch (i) {
			case 1: // '\001'
				f1 += (int) (20F * f2);
				break;

			case 2: // '\002'
				f1 += (int) (18F * f2);
				break;

			case 3: // '\003'
				f1 += (int) (12F * f2);
				break;

			case 4: // '\004'
				f1 += (int) (3.5F * f2);
				break;

			case 6: // '\006'
				f1 -= (int) (4.5F * f2);
				break;

			case 7: // '\007'
				f1 -= (int) (6.5F * f2);
				break;

			case 8: // '\b'
				f1 -= (int) (4F * f2);
				break;

			case 10: // '\n'
				f1 += (int) (3.5F * f2);
				break;

			case 11: // '\013'
				f1 += (int) (9F * f2);
				break;

			case 12: // '\f'
				f1 += (int) (19.5F * f2);
				break;
			}
			i = Mission.curMonth();
			if (Engine.land().config.declin < 0) i += 6;
			if (i > 12) i -= 12;
			switch (i) {
			case 1: // '\001'
				f1 -= (int) (20F * f2);
				break;

			case 2: // '\002'
				f1 -= (int) (18F * f2);
				break;

			case 3: // '\003'
				f1 -= (int) (12F * f2);
				break;

			case 4: // '\004'
				f1 -= (int) (3.5F * f2);
				break;

			case 6: // '\006'
				f1 += (int) (4.5F * f2);
				break;

			case 7: // '\007'
				f1 += (int) (6.5F * f2);
				break;

			case 8: // '\b'
				f1 += (int) (4F * f2);
				break;

			case 10: // '\n'
				f1 -= (int) (3.5F * f2);
				break;

			case 11: // '\013'
				f1 -= (int) (9F * f2);
				break;

			case 12: // '\f'
				f1 -= (int) (19.5F * f2);
				break;
			}
			int l1 = (int) Math.floor(World.getTimeofDay());
			switch (l1) {
			case 0: // '\0'
				f1 -= 11F;
				break;

			case 1: // '\001'
				f1 -= 12F;
				break;

			case 2: // '\002'
				f1 -= 12F;
				break;

			case 3: // '\003'
				f1 -= 12F;
				break;

			case 4: // '\004'
				f1 -= 13F;
				break;

			case 5: // '\005'
				f1 -= 13F;
				break;

			case 6: // '\006'
				f1 -= 10F;
				break;

			case 7: // '\007'
				f1 -= 9F;
				break;

			case 8: // '\b'
				f1 -= 7F;
				break;

			case 9: // '\t'
				f1 -= 5F;
				break;

			case 10: // '\n'
				f1 -= 4F;
				break;

			case 11: // '\013'
				f1 -= 3F;
				break;

			case 12: // '\f'
				f1--;
				break;

			case 15: // '\017'
				f1--;
				break;

			case 16: // '\020'
				f1 -= 2.0F;
				break;

			case 17: // '\021'
				f1 -= 4F;
				break;

			case 18: // '\022'
				f1 -= 6F;
				break;

			case 19: // '\023'
				f1 -= 7F;
				break;

			case 20: // '\024'
				f1 -= 8F;
				break;

			case 21: // '\025'
				f1 -= 9F;
				break;

			case 22: // '\026'
				f1 -= 10F;
				break;

			case 23: // '\027'
				f1 -= 11F;
				break;
			}
		} else if (Engine.land().config.climate == "Arid") {
			switch (i) {
			case 1: // '\001'
				f1 += (int) (14F * f2);
				break;

			case 2: // '\002'
				f1 += (int) (11F * f2);
				break;

			case 3: // '\003'
				f1 += (int) (9F * f2);
				break;

			case 4: // '\004'
				f1 += (int) (4F * f2);
				break;

			case 6: // '\006'
				f1 -= (int) (2.0F * f2);
				break;

			case 7: // '\007'
				f1 -= (int) (2.0F * f2);
				break;

			case 8: // '\b'
				f1 -= (int) (2.0F * f2);
				break;

			case 9: // '\t'
				f1 -= (int) (1.0F * f2);
				break;

			case 10: // '\n'
				f1 += (int) (3F * f2);
				break;

			case 11: // '\013'
				f1 += (int) (8F * f2);
				break;

			case 12: // '\f'
				f1 += (int) (13F * f2);
				break;
			}
			i = Mission.curMonth();
			if (Engine.land().config.declin < 0) i += 6;
			if (i > 12) i -= 12;
			switch (i) {
			case 1: // '\001'
				f1 -= (int) (14F * f2);
				break;

			case 2: // '\002'
				f1 -= (int) (11F * f2);
				break;

			case 3: // '\003'
				f1 -= (int) (9F * f2);
				break;

			case 4: // '\004'
				f1 -= (int) (4F * f2);
				break;

			case 6: // '\006'
				f1 += (int) (2.0F * f2);
				break;

			case 7: // '\007'
				f1 += (int) (2.0F * f2);
				break;

			case 8: // '\b'
				f1 += (int) (2.0F * f2);
				break;

			case 9: // '\t'
				f1 += (int) (1.0F * f2);
				break;

			case 10: // '\n'
				f1 -= (int) (3F * f2);
				break;

			case 11: // '\013'
				f1 -= (int) (8F * f2);
				break;

			case 12: // '\f'
				f1 -= (int) (13F * f2);
				break;
			}
			int i2 = (int) Math.floor(World.getTimeofDay());
			switch (i2) {
			case 0: // '\0'
				f1 -= 24F;
				break;

			case 1: // '\001'
				f1 -= 25F;
				break;

			case 2: // '\002'
				f1 -= 26F;
				break;

			case 3: // '\003'
				f1 -= 27F;
				break;

			case 4: // '\004'
				f1 -= 28F;
				break;

			case 5: // '\005'
				f1 -= 28F;
				break;

			case 6: // '\006'
				f1 -= 27F;
				break;

			case 7: // '\007'
				f1 -= 20F;
				break;

			case 8: // '\b'
				f1 -= 15F;
				break;

			case 9: // '\t'
				f1 -= 10F;
				break;

			case 10: // '\n'
				f1 -= 5F;
				break;

			case 11: // '\013'
				f1 -= 3F;
				break;

			case 12: // '\f'
				f1--;
				break;

			case 15: // '\017'
				f1--;
				break;

			case 16: // '\020'
				f1 -= 2.0F;
				break;

			case 17: // '\021'
				f1 -= 4F;
				break;

			case 18: // '\022'
				f1 -= 6F;
				break;

			case 19: // '\023'
				f1 -= 9F;
				break;

			case 20: // '\024'
				f1 -= 12F;
				break;

			case 21: // '\025'
				f1 -= 15F;
				break;

			case 22: // '\026'
				f1 -= 18F;
				break;

			case 23: // '\027'
				f1 -= 21F;
				break;
			}
		} else {
			switch (i) {
			case 1: // '\001'
				f1 += (int) (14F * f2);
				break;

			case 2: // '\002'
				f1 += (int) (10F * f2);
				break;

			case 3: // '\003'
				f1 += (int) (8F * f2);
				break;

			case 4: // '\004'
				f1 += (int) (4F * f2);
				break;

			case 6: // '\006'
				f1 -= (int) (5F * f2);
				break;

			case 7: // '\007'
				f1 -= (int) (6F * f2);
				break;

			case 8: // '\b'
				f1 -= (int) (4F * f2);
				break;

			case 9: // '\t'
				f1 -= (int) (2.0F * f2);
				break;

			case 10: // '\n'
				f1 += (int) (7F * f2);
				break;

			case 11: // '\013'
				f1 += (int) (11F * f2);
				break;

			case 12: // '\f'
				f1 += (int) (14F * f2);
				break;
			}
			i = Mission.curMonth();
			if (Engine.land().config.declin < 0) i += 6;
			if (i > 12) i -= 12;
			switch (i) {
			case 1: // '\001'
				f1 -= (int) (14F * f2);
				break;

			case 2: // '\002'
				f1 -= (int) (10F * f2);
				break;

			case 3: // '\003'
				f1 -= (int) (8F * f2);
				break;

			case 4: // '\004'
				f1 -= (int) (4F * f2);
				break;

			case 6: // '\006'
				f1 += (int) (5F * f2);
				break;

			case 7: // '\007'
				f1 += (int) (6F * f2);
				break;

			case 8: // '\b'
				f1 += (int) (4F * f2);
				break;

			case 9: // '\t'
				f1 -= (int) (2.0F * f2);
				break;

			case 10: // '\n'
				f1 -= (int) (7F * f2);
				break;

			case 11: // '\013'
				f1 -= (int) (11F * f2);
				break;

			case 12: // '\f'
				f1 -= (int) (14F * f2);
				break;
			}
			int j2 = (int) Math.floor(World.getTimeofDay());
			switch (j2) {
			case 0: // '\0'
				f1 -= 8F;
				break;

			case 1: // '\001'
				f1 -= 9F;
				break;

			case 2: // '\002'
				f1 -= 9F;
				break;

			case 3: // '\003'
				f1 -= 9F;
				break;

			case 4: // '\004'
				f1 -= 9F;
				break;

			case 5: // '\005'
				f1 -= 9F;
				break;

			case 6: // '\006'
				f1 -= 9F;
				break;

			case 7: // '\007'
				f1 -= 7F;
				break;

			case 8: // '\b'
				f1 -= 5F;
				break;

			case 9: // '\t'
				f1 -= 4F;
				break;

			case 10: // '\n'
				f1 -= 3F;
				break;

			case 11: // '\013'
				f1 -= 2.0F;
				break;

			case 12: // '\f'
				f1--;
				break;

			case 15: // '\017'
				f1--;
				break;

			case 16: // '\020'
				f1 -= 2.0F;
				break;

			case 17: // '\021'
				f1 -= 3F;
				break;

			case 18: // '\022'
				f1 -= 4F;
				break;

			case 19: // '\023'
				f1 -= 5F;
				break;

			case 20: // '\024'
				f1 -= 6F;
				break;

			case 21: // '\025'
				f1 -= 7F;
				break;

			case 22: // '\026'
				f1 -= 8F;
				break;

			case 23: // '\027'
				f1 -= 8F;
				break;
			}
		}
		World.cur().Atm.P0 = f;
		World.cur().Atm.T0 = f1;
		World.cur().Atm.ro0 = 1.225F * (f / 101300F) * (288.16F / f1);
                // TODO: +++ to avoid excessive logfile output
        if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
    		System.out.println("Year = " + Mission.curYear() + " , Month = " + Mission.curMonth() + " , Hour = " + (int) Math.floor(World.getTimeofDay()));
	    	System.out.println("Temperature -     0m = " + (temperature(0.0F) - 273.16F) + " .");
		    System.out.println("Temperature -  1000m = " + (temperature(1000F) - 273.16F) + " .");
    		System.out.println("Temperature -  2000m = " + (temperature(2000F) - 273.16F) + " .");
	    	System.out.println("Temperature -  3000m = " + (temperature(3000F) - 273.16F) + " .");
		    System.out.println("Temperature -  4000m = " + (temperature(4000F) - 273.16F) + " .");
    		System.out.println("Temperature -  5000m = " + (temperature(5000F) - 273.16F) + " .");
	    	System.out.println("Temperature -  6000m = " + (temperature(6000F) - 273.16F) + " .");
		    System.out.println("Temperature -  7000m = " + (temperature(7000F) - 273.16F) + " .");
    		System.out.println("Temperature -  8000m = " + (temperature(8000F) - 273.16F) + " .");
	    	System.out.println("Temperature -  9000m = " + (temperature(9000F) - 273.16F) + " .");
		    System.out.println("Temperature - 10000m = " + (temperature(10000F) - 273.16F) + " .");
    		System.out.println("Temperature - 11000m = " + (temperature(11000F) - 273.16F) + " .");
	    	System.out.println("Temperature - 12000m = " + (temperature(12000F) - 273.16F) + " .");
        } else {
    		System.out.print("Y=" + Mission.curYear() + " / M=" + Mission.curMonth() + " / H= " + (int) Math.floor(World.getTimeofDay()) + " , ");
	    	System.out.println("Temperature - 0m = " + (temperature(0.0F) - 273.16F) + " .");
        }
                // TODO: --- to avoid excessive logfile output
	}

	public static final float pressure(float f) {
		if (f > 18300F) return (18300F / f) * P0() * poly(Pressure, 18300F);
		else return P0() * poly(Pressure, f);
	}

	public static final float temperature(float f) {
		if (f > 18300F) f = 18300F;
		float f1 = T0() - f * 0.00649F;
		if (f1 < 216.66F) f1 = 216.66F;
		return f1;
	}

	public static final float sonicSpeed(float f) {
		return 20.1F * (float) Math.sqrt(temperature(f));
	}

	public static final float density(float f) {
		if (f > 18300F) return (18300F / f) * ro0() * poly(Density, 18300F);
		else return ro0() * poly(Density, f);
	}

	public static final float viscosity(float f) {
		return Mu0() * (float) Math.pow(temperature(f) / T0(), 0.76000000000000001D);
	}

	public static final float kineticViscosity(float f) {
		return viscosity(f) / density(f);
	}

	private float g;
	private float P0;
	private float T0;
	private float ro0;
	private float Mu0;
	private static final float Density[] = { 1.0F, -9.59387E-005F, 3.53118E-009F, -5.83556E-014F, 2.28719E-019F };
	private static final float Pressure[] = { 1.0F, -0.000118441F, 5.6763E-009F, -1.3738E-013F, 1.60373E-018F };
}