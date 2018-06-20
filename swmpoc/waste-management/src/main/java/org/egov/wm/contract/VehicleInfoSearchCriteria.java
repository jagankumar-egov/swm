package org.egov.wm.contract;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleInfoSearchCriteria {
	
	@NotNull
	@JsonProperty("tenantId")
	private String tenantId;
	

	@JsonProperty("id")
	private UUID id;
	
	@JsonProperty("vehicleNo")
	private String vehicleNo;

}
