package org.egov.model;

public class BatteryInfo {
	
	private Object batteryLevel;
	
	private Boolean charging;

	public Object getBatteryLevel() {
		return batteryLevel;
	}

	public void setBatteryLevel(Object batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	public Boolean getCharging() {
		return charging;
	}

	public void setCharging(Boolean charging) {
		this.charging = charging;
	}

	@Override
	public String toString() {
		return "BatteryInfo [batteryLevel=" + batteryLevel + ", charging=" + charging + "]";
	}

}
