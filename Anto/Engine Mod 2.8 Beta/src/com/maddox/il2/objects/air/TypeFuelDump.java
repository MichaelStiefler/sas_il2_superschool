package com.maddox.il2.objects.air;

public interface TypeFuelDump {
	public abstract float getFlowRate(); // Returns flow rate in litres per second, defined in aircraft class

	public abstract float getFuelReserve(); // Return size of reserve fuel, defined in aircraft class
}
