import React, { Component } from 'react';
import { MapWithDirectionsRenderer } from '../../../uiComponents/maps';
import _ from 'lodash';
const moment = require('moment');

class MapsInfo extends Component {
	constructor(props) {
		super(props);
		this.state = {
			vehicleLocations: [],
			userLocation:{},
			eta: 9,
			routes:[]
		};
	}

	componentDidMount() {
		let { props } = this;
		this.initializeMap(props);
	}

	initializeMap(props) {
		// if(props.vehicleLocations) {
		// 	this.setState({
		// 		vehicleLocations: props.vehicleLocations,
		// 		routes: props.routes,
		// 		userLocation:props.userLocation
		// 	})
		// }
	}

	componentWillReceiveProps(nextProps) {
		// if(!_.isEqual(this.props, nextProps)) {
		// 	this.initializeMap(nextProps);
		// }
	}

	render() {
		const {eta} = this.state;
		const { refresh, t, availWidth ,vehicleLocations, routes, userLocation} = this.props;
		return (
			<div className="map-box">
				{/*<a className="refresh-btn btn-floating btn-large waves-effect waves-light lightBlack" href="javascript:void(0)" onClick={refresh}>
					<i className="material-icons">refresh</i>
				</a>*/}
				<MapWithDirectionsRenderer
					availWidth={availWidth}
					vehicleLocations={vehicleLocations}
					eta={eta}
					routes={routes}
					userLocation={userLocation}
				/>
			</div>
		);
	}
}

export default MapsInfo;
