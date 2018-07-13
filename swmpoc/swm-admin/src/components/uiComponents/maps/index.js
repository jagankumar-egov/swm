import React from 'react';
import _ from 'lodash';
import carIcon from '../../../assets/images/car.svg';

import truckIcon from '../../../assets/images/truck.png';

import garbagePickup from '../../../assets/images/garbage-pickup.png';


import pickup from '../../../assets/images/pickup/pickup.png';
import pickup2x from '../../../assets/images/pickup/pickup@2x.png';
import pickup3x from '../../../assets/images/pickup/pickup@3x.png';

import drop from '../../../assets/images/drop/drop.png';
import drop2x from '../../../assets/images/drop/drop@2x.png';
import drop3x from '../../../assets/images/drop/drop@3x.png';

const { compose, withProps, lifecycle ,withStateHandlers,withPropsOnChange} = require("recompose");
const {
  withScriptjs,
  withGoogleMap,
  GoogleMap,
  DirectionsRenderer,
  Marker,
  OverlayView,
  Polyline,
  TrafficLayer
} = require("react-google-maps");

const { MarkerWithLabel } = require("react-google-maps/lib/components/addons/MarkerWithLabel");

const { InfoBox } = require("react-google-maps/lib/components/addons/InfoBox");

let bounds;

const getPixelPositionOffset = (width, height) => ({
  x: -(width / 2),
  y: -(height / 2),
})

let gMap;

// const withExtendedProps = withPropsOnChange(
//   (props, nextProps) => {
//     debugger;
//     return nextProps.count % 2 === 0
//   },
//   (props) => ({
//     specialProp : props.count,
//   })
// )

const car = "M17.402,0H5.643C2.526,0,0,3.467,0,6.584v34.804c0,3.116,2.526,5.644,5.643,5.644h11.759c3.116,0,5.644-2.527,5.644-5.644 V6.584C23.044,3.467,20.518,0,17.402,0z M22.057,14.188v11.665l-2.729,0.351v-4.806L22.057,14.188z M20.625,10.773 c-1.016,3.9-2.219,8.51-2.219,8.51H4.638l-2.222-8.51C2.417,10.773,11.3,7.755,20.625,10.773z M3.748,21.713v4.492l-2.73-0.349 V14.502L3.748,21.713z M1.018,37.938V27.579l2.73,0.343v8.196L1.018,37.938z M2.575,40.882l2.218-3.336h13.771l2.219,3.336H2.575z M19.328,35.805v-7.872l2.729-0.355v10.048L19.328,35.805z";
export const MapWithDirectionsRenderer = compose(
  withProps(props=>{
    return {googleMapURL: "https://maps.googleapis.com/maps/api/js?key=AIzaSyBN01pR2wGavj2_q3v4-vFgQzmcx-gllk0&v=3.exp&libraries=geometry,drawing,places",
    loadingElement: <div style={{ height: `100%` }} />,
    containerElement: <div style={{ height: `100vh`, minWidth: `100%` }} />,
    mapElement: <div style={{ height: `100%` }} />,
    props}
  }),
  withScriptjs,
  withGoogleMap,
  withStateHandlers(() => ({
    isOpen: false,
    }), {
    onToggleOpen: ({ isOpen }) => () => ({
      isOpen: !isOpen,
    })
  }),
  lifecycle({
    componentDidMount() {
      this.reRender(this.props);
    },

    componentWillReceiveProps(nextProps) {
      if(!_.isEqual(this.props, nextProps)) {
        this.reRender(nextProps);
      }
    },

    reRender(props) {
      // if(props.userLocation) {
        const DirectionsService = new window.google.maps.DirectionsService();
        bounds = new window.google.maps.LatLngBounds();
        let count = 2; //1: if don't want to show pickup
        let waypts = [];

        const fitBound = () => {
          count--;
          if(count == 0 && gMap) {
            gMap.fitBounds(bounds);
          }
        }

        if(props.enrouteOrders && props.enrouteOrders.length) {
          for(let i=0; i< props.enrouteOrders.length; i++) {
            waypts.push({
              location: new window.google.maps.LatLng(props.enrouteOrders[i].latLng.lat, props.enrouteOrders[i].latLng.lng),
              stopover: true
            })
          }
        }

        DirectionsService.route({
          origin: new window.google.maps.LatLng(12.9188752, 77.6701266),
          destination: new window.google.maps.LatLng(12.9288091, 77.5858991999999),
          travelMode: window.google.maps.TravelMode.DRIVING,
          waypoints: waypts,
          optimizeWaypoints: true
        }, (result, status) => {
          if (status === window.google.maps.DirectionsStatus.OK) {
            this.setState({
              directions: result,
              duration: result.routes[0].legs[0].duration.text.split(' ')[0],
              heading: window.google.maps.geometry.spherical.computeHeading(new window.google.maps.LatLng(12.9188752, 77.6701266), new window.google.maps.LatLng(12.9288091, 77.5858991999999)),
              waypoints: waypts
            });

            bounds.union(result.routes[0].bounds);
            fitBound();
          } else {
            console.error(`Error fetching directions ${result}`);
          }
        });

        //DO THIS ONLY IF WE NEED TO SHOW PICKUP LOCATION BASED ON API CONFIGURATION
        if(true) {
          DirectionsService.route({
            origin: new window.google.maps.LatLng(12.9188752, 77.6701266),
            destination: new window.google.maps.LatLng(12.9288091, 77.5858991999999),
            travelMode: window.google.maps.TravelMode.DRIVING,
          }, (result, status) => {
            if (status === window.google.maps.DirectionsStatus.OK) {
              this.setState({
                pickupDirections: result
              });

              bounds.union(result.routes[0].bounds);
              fitBound();
            } else {
              console.error(`Error fetching directions ${result}`);
            }
          });
        }
      // }
    }
  })
)(props => {
  console.log(props.vehicleLocations);
  return (<div>
    { (props.eta != -1 && props.eta <= 10)
      ?
      <GoogleMap
        ref={ el => { gMap = el; } }
        defaultOptions={{
          mapTypeControl: false
        }}
        options={{
          mapTypeControl: false,
          disableDoubleClickZoom: false,
          draggable: true,
          fullscreenControl: true,
          scaleControl: true,
          scrollwheel: true,
          streetViewControl: true,
          zoomControl: true,
          styles: [
              {
                featureType: 'poi.business',
                stylers: [{visibility: 'off'}]
              },
              {
                featureType: 'poi',
                stylers: [{visibility: 'off'}]
              },
              {
                featureType: 'transit',
                elementType: 'labels.icon',
                stylers: [{visibility: 'visible'}]
              },
              {elementType: 'labels.text.stroke', stylers: [{visibility: 'visible'}]},
              {elementType: 'labels.text.fill', stylers: [{visibility: 'visible'}]},
          ]}
        }
      >
        <TrafficLayer autoUpdate />
        {props.routes && props.routes.map((route,routeKey)=>
          {
            return route.collectionPoints.map((marker,markerKey)=>
              <Marker
              key={markerKey}
              position={{ lat: marker.latitude, lng: marker.longitude }}
              icon={{
                url: (props.availWidth < 479 ? garbagePickup : garbagePickup),
                scaledSize: (props.availWidth < 479 ? new window.google.maps.Size(24, 24) : new window.google.maps.Size(32, 32)),
                anchor: (props.availWidth < 479 ? new window.google.maps.Point(15, 15) : new window.google.maps.Point(15, 15))
              }}
             />
            )
          })
        }

        {/*props.routes && props.routes.map((route,routeKey)=>
          {
            let options={strokeColor: 'red',strokeWeight: 3}
            let collectionPoints=route.collectionPoints.map((collectionPoint)=>
            {
              return {lat:collectionPoint.latitude,lng:collectionPoint.longitude}
            })

            if (routeKey==1) {
              options={strokeColor: 'green',strokeWeight: 3}
            } else if(routeKey==2){
              options={strokeColor: 'blue',strokeWeight: 3}
            }



            return (<Polyline key={routeKey} path={[...collectionPoints,{lat:route.dumpingGround.latitude,lng:route.dumpingGround.longitude}]} options={options}/>)
          })
      */  }



        {props.routes && props.routes.map((route,routeKey)=>
            (<Marker
              key={routeKey}
              position={{ lat: route.dumpingGround.latitude, lng: route.dumpingGround.longitude }}
              onClick={props.onToggleOpen}
              // icon={{
              //   url: (props.availWidth < 479 ? garbagePickup : garbagePickup),
              //   scaledSize: (props.availWidth < 479 ? new window.google.maps.Size(24, 24) : new window.google.maps.Size(32, 32)),
              //   anchor: (props.availWidth < 479 ? new window.google.maps.Point(15, 15) : new window.google.maps.Point(15, 15))
              // }}
             >
             {/*props.isOpen && <InfoBox
                onCloseClick={props.onToggleOpen}
                options={{ closeBoxURL: ``, enableEventPropagation: true }}
                >
                   <div style={{ backgroundColor: `white`, opacity: 0.75, padding: `12px` }}>
                     <div style={{ fontSize: `16px`, fontColor: `#08233B` }}>
                       Test
                     </div>
                   </div>
              </InfoBox>*/}
             </Marker>
           )
          )
        }

        {props.directions && <DirectionsRenderer
                                directions={props.directions}
                                options={{
                                  preserveViewport: true,
                                  suppressMarkers: true,
                                  polylineOptions: {
                                    strokeColor: '#0bb4f1',
                                    strokeOpacity: 0,
                                    icons: [{
                                        icon: {
                                            path: window.google.maps.SymbolPath.CIRCLE,
                                            fillOpacity: 1,
                                            scale: 3
                                        },
                                        offset: '0',
                                        repeat: '1px'
                                    }]
                                  }
                                }}/>
        }

        {props.vehicleLocations && props.vehicleLocations.map((marker,markerKey)=>(
          <Marker
          key={markerKey}
          position={{
            lat: marker.latLng.lat,
            lng: marker.latLng.lng
          }}
          icon={{
            url: props.icon || truckIcon,
            scaledSize: new window.google.maps.Size(40, 35), // scaled size
            // origin: new window.google.maps.Point(0, -10),
            // path: car,
            // scale: .7,
            strokeColor: 'white',
            strokeWeight: .10,
            fillOpacity: 1,
            fillColor: '#404040',
            offset: '5%',
            rotation: props.heading,
            anchor: (props.tripStatus == "COMPLETED" || props.tripStatus == "CANCELLED") ? new window.google.maps.Point(-20, 25) : new window.google.maps.Point(10, 25)
          }}
          options={{animation: window.google.maps.Animation.DROP}}
         >
         </Marker>
        ))

      }



        {/*<MarkerWithLabel
          position={{ lat: props.destination.latLng.lat, lng: props.destination.latLng.lng }}
          labelAnchor={new window.google.maps.Point(0, 0)}
          labelStyle={{"boxShadow": "0px 0px 5px 0px rgba(0,0,0,0.43)"}}
          icon={{
            url: (props.availWidth < 479 ? drop : drop2x),
            scaledSize: (props.availWidth < 479 ? new window.google.maps.Size(24, 24) : new window.google.maps.Size(32, 32)),
            anchor: (props.availWidth < 479 ? new window.google.maps.Point(15, 15) : new window.google.maps.Point(15, 15))
          }}
        >
          {["COMPLETED", "CANCELLED"].indexOf(props.tripStatus) == -1 ? !props.dNearBy ?
            <div className="eta-box">
              <div style={{"fontSize": "19px", "marginTop": "-2px"}}>{props.eta}</div>
              <div style={{"fontSize": "8px", "marginLeft": "2px"}}>MIN <br/> ETA</div>
            </div> :
            <div className="d-box">{props.dNearByLabel}</div> : <div></div>}
        </MarkerWithLabel>*/}

        {
          props.waypoints && props.waypoints.length
          ?
          props.waypoints.map((wp, i) => {
            return (
              <Marker
                key={i}
                position={wp.location}></Marker>
            );
          })
          :
          <span></span>
        }
      </GoogleMap>
      :
      <div>
        <div style={{
          position: "absolute",
          top: 0,
          left: props.availWidth < 769 ? "10%" : "45%",
          transform: props.availWidth < 769 ? "translate(0, 0)" : "translate(-50%, 0)",
          color: "grey",
          fontSize: "25px"
        }}>{props.yetToStartLabel}</div>
        <GoogleMap
          defaultZoom={19}
          defaultCenter={{ lat: 12.969158, lng: 77.600947 }}
          clickableIcons={false}
          options={{
            mapTypeControl: false,
            disableDoubleClickZoom: true,
            draggable: false,
            fullscreenControl: false,
            scaleControl: false,
            scrollwheel: false,
            streetViewControl: false,
            zoomControl: false,
            styles: [
                {
                  featureType: 'poi.business',
                  stylers: [{visibility: 'off'}]
                },
                {
                  featureType: 'transit',
                  elementType: 'labels.icon',
                  stylers: [{visibility: 'off'}]
                },
                {elementType: 'labels.text.stroke', stylers: [{visibility: 'off'}]},
                {elementType: 'labels.text.fill', stylers: [{visibility: 'off'}]},
            ]}
          }
        >
          <Marker
            position={{ lat: 12.969158, lng: 77.600947 }}
            icon={{
              url: props.icon || truckIcon,
              scaledSize: new window.google.maps.Size(40, 35), // scaled size
              // origin: new window.google.maps.Point(0, -10),
              // path: car,
              // scale: .7,
              strokeColor: 'white',
              strokeWeight: .10,
              fillOpacity: 1,
              fillColor: '#404040',
              offset: '5%',
              rotation: 0,
              anchor: new window.google.maps.Point(10, 55)
            }}>
          </Marker>
        </GoogleMap>
      </div>
    }
  </div>);
});
