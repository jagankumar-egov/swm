package org.egov.wm.contract;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wm.model.DriverInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverRequest {
	
	private RequestInfo requestInfo;
	
	private List<DriverInfo> drivers;
	
}
