package org.egov.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class VehicleInfo {
	
	private UUID id;
			
	private String vehicleNo;
	
	private String routeCode;
	
	private BatteryInfo batteryInfo;
	
	private Coords coords;
	
	private Boolean mocked;
	
	private Long timestamp;

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getRouteCode() {
		return routeCode;
	}

	public void setRouteCode(String routeCode) {
		this.routeCode = routeCode;
	}

	public BatteryInfo getBatteryInfo() {
		return batteryInfo;
	}

	public void setBatteryInfo(BatteryInfo batteryInfo) {
		this.batteryInfo = batteryInfo;
	}

	public Coords getCoords() {
		return coords;
	}

	public void setCoords(Coords coords) {
		this.coords = coords;
	}

	public Boolean getMocked() {
		return mocked;
	}

	public void setMocked(Boolean mocked) {
		this.mocked = mocked;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	
	public UUID getId() {
		return UUID.randomUUID();
	}

	public void setId(UUID id) {
		this.id = id;
	}
	

}
