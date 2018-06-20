package org.egov.wm.model;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleInfo {
	
	private UUID id;

	private String company;
	
	private String model;
	
	@NotNull		
	private String vehicleNo;
	
	private String type;

	private Integer capacity;
	
	private String color;
	
	private String owner;
	
	private String purchaseDate;
	
	private String code;
	
	private String tenantId;
	
	private UUID driverId;
	
	private AuditInfo auditInfo;



	
}
