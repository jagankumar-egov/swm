package org.egov.wm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditInfo {
	
	private String createdBy;
	
	private Long createdTime;
	
	private String lastModifiedBy;
	
	private Long lastModifiedTime;

}
