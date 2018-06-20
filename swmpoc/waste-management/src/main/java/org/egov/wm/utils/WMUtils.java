package org.egov.wm.utils;

import java.util.Date;

import org.egov.wm.model.AuditInfo;
import org.springframework.stereotype.Component;

@Component
public class WMUtils {
	public AuditInfo getAuditInfo(String by, Boolean isCreate) {

		Long dt = new Date().getTime();
		if (isCreate)
			return AuditInfo.builder().createdBy(by).createdTime(dt).lastModifiedBy(by).lastModifiedTime(dt).build();
		else
			return AuditInfo.builder().lastModifiedBy(by).lastModifiedTime(dt).build();
	}
}
