package org.egov.wm.model;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Route {
	
	private UUID id;
	
	private AuditInfo auditInfo;
	
	private List<UUID> collectionPointIds;
	
	private List<UUID> dumpingGroundIds;

	private String name;
	
	private String code;
	
	private String tenantId;
}