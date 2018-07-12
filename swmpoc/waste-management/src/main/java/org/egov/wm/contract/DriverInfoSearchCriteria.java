package org.egov.wm.contract;

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
public class DriverInfoSearchCriteria {
	
	private UUID id;
	
	private String name;
	
	private String phoneNo;
	
	private String tenantId;

}
