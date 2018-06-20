package org.egov.wm.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Trip {
	
	private UUID id;
	
	private UUID routeId;
	
	private UUID vehicleId;
	
	private AuditInfo auditInfo;
	
	private Long startTime;

	private Long endTime;
	
	private String status;
	
}
