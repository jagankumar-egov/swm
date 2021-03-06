package org.egov.wm.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Track {
	
	private UUID id;

	private UUID tripId;
	
	private Coords coords;
	
	private Long timeStamp;
	
}
