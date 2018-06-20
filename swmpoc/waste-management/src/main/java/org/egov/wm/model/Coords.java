package org.egov.wm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Coords {

	private Double accuracy;
	
	private Double altitude;
	
	private Integer heading;
	
	private Double latitude;
	
	private Double longitude;
	
	private Integer speed;
	
	private Long altitudeAccuracy;

}
