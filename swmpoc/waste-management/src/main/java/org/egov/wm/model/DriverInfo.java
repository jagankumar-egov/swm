package org.egov.wm.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverInfo {

	private UUID id;
	@NonNull
	private String name;
	
	private String code;
	
	private String tenantId;
	
	private Long dateOfBirth;
	
	private Long phoneNo;
	
	private Long aadhaarNo;
	
	private Long licenseNo;

	private String bloodGroup;
	
	private AuditInfo auditInfo;
	
	private String emailId;
	
	
}
