package org.egov.wm.model;

import java.util.List;
import java.util.UUID;

public class Route {
	
	private UUID id;
	
	private AuditInfo auditInfo;
	
	private List<UUID> collectionPointIds;
	
	private List<UUID> dumpingGroundIds;

	private String name;
	
	private String code;
	
	private String tenantId;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public AuditInfo getAuditInfo() {
		return auditInfo;
	}

	public void setAuditInfo(AuditInfo auditInfo) {
		this.auditInfo = auditInfo;
	}

	public List<UUID> getCollectionPointIds() {
		return collectionPointIds;
	}

	public void setCollectionPointIds(List<UUID> collectionPointIds) {
		this.collectionPointIds = collectionPointIds;
	}

	public List<UUID> getDumpingGroundIds() {
		return dumpingGroundIds;
	}

	public void setDumpingGroundIds(List<UUID> dumpingGroundIds) {
		this.dumpingGroundIds = dumpingGroundIds;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
	
}