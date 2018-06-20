package org.egov.wm.contract;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.wm.model.DriverInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverResponse {
	
	private ResponseInfo responseInfo;

	private List<DriverInfo> drivers;
}
