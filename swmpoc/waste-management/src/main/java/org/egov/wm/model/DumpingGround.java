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
public class DumpingGround {
	private UUID id;
	
	private String binId;
	
	private Double latitude;
	
	private Double longitude;
	
	private String name;
	
	private String ward;
	
	private String street;
	
	private String colony;
	
	private String code;
	
	private String tenantId;
	
	private AuditInfo auditInfo;

}
