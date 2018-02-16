import React from "react";
import { Platform, View, Text, Image, Button,DeviceEventEmitter,NativeModules ,Dimensions} from "react-native";
import { MapView } from "expo";
import { Constants, Location, Permissions } from "expo";
import { commonApi } from "../../../utility/api";
import collectionPoint from '../../../../assets/garbage-pickup.png';
import truck from '../../../../assets/truck.png';
// import { Button } from 'react-native-elements';
var BatteryManager = require('NativeModules').BatteryManager;

var timerObject;

export default class CollectionMap extends React.Component {
  state = {
    location: null,
    errorMessage: null,
    refreshSeconds: 15000,
    batteryLevel: null,
    charging:false
  };

  componentWillMount() {
    let { refreshSeconds } = this.state;
    let { _getLocationAsync } = this;
    if (Platform.OS === "android" && !Constants.isDevice) {
      this.setState({
        errorMessage:
          "Oops, this will not work on Sketch in an Android emulator. Try it on your device!"
      });
    } else {
      //Make server call to fetch initial and tracking data
      _getLocationAsync();
      //And it will call every refreshSeconds
      timerObject = setInterval(() => {
        _getLocationAsync();
      }, refreshSeconds);
    }
  }

  _onBatteryStatus= (info)=>{
    this.setState({batteryLevel: info.level,charging: info.isPlugged});
  }

  componentDidMount() {
    // let {_onBatteryStatus}=this;
    // BatteryManager.updateBatteryLevel(function(info){
    //   this._subscription = DeviceEventEmitter.addListener('BatteryStatus', _onBatteryStatus);
    //   this.setState({batteryLevel: info.level,charging: info.isPlugged});
    // }.bind(this));
  }


  componentWillUnmount() {
    clearInterval(timerObject);
    // this._subscription.remove();
  }

  _getLocationAsync = async () => {
    let { _updateLocation } = this;
    let { status } = await Permissions.askAsync(Permissions.LOCATION);
    if (status !== "granted") {
      this.setState({
        errorMessage: "Permission to access location was denied"
      });
    }

    let location = await Location.getCurrentPositionAsync({});
    // console.log(location);
    this.setState({ location });
    _updateLocation(location);
  };

  _updateLocation = location => {
    //call api for updating location and batery info
    let {inputData}=this.props;
    let {batteryLevel,charging}=this.state;
    // location={"records":[{"value":{"vehicleNo":inputData.regNo,"routeCode":inputData.routeNo,"batteryInfo":{}, "key":"One",...location}}]};
    console.log({"records":[{"value":{"vehicleNo":inputData.regNo,"routeCode":inputData.routeNo,"batteryInfo":{batteryLevel,charging},...location},"key":"One"}]});
    commonApi("post", "/demo/swm-ingest", {}, {"records":[{"value":{"vehicleNo":inputData.regNo,"routeCode":inputData.routeNo,"batteryInfo":{batteryLevel,charging},...location},"key":"One"}]})
      .then(res => {
        console.log(res);
      })
      .catch(err => {
        // console.log("hai");
        console.log(err);
      });
  };

  _goback = () =>
  {
    let {startMyRoute} =this.props;
    console.log("clicked");
    startMyRoute(false);
  }

  render() {
    let { mapDetails ,inputData} = this.props;
    let { location } = this.state;
    let {_goback} = this;
    let index=0;
    if (inputData.routeNo=="Route2") {
      index=1;
    } else if(inputData.routeNo=="Route3"){
      index=2;
    }
    return (
      <View>

      {location && <MapView
        style={{width: Dimensions.get('window').width, height: Dimensions.get('window').height-35}}
        initialRegion={mapDetails.routes[0].collectionPoints[0]}
        loadingEnabled={true}
        loadingIndicatorColor={"#606060"}
        showsTraffic={true}
        showsMyLocationButton={true}
        showsUserLocation={true}
        showsMyLocationButton={true}
      >
        {location && (
          <MapView.Marker
            key={location.coords.latitude}
            coordinate={{
              latitude: location.coords.latitude,
              longitude: location.coords.longitude
            }}
            title={inputData.regNo}
            description={"Vehicle Location"}
            image={truck}
          />
        )}

        {mapDetails &&
          mapDetails.routes[index].collectionPoints.map((marker, index) => (
            <MapView.Marker
              key={index}
              coordinate={{
                latitude: marker.latitude,
                longitude: marker.longitude
              }}
              title={marker.title}
              description={marker.description}
              image={collectionPoint}
            />
          ))}

        {mapDetails && (
          <MapView.Marker
            coordinate={{
              latitude: mapDetails.routes[index].dumpingGround.latitude,
              longitude: mapDetails.routes[index].dumpingGround.longitude
            }}
            title={mapDetails.routes[index].dumpingGround.title}
            description={mapDetails.routes[index].dumpingGround.description}
          />
        )}

        <MapView.Polyline
          coordinates={
            location ? (
              [
                {
                  latitude: location.coords.latitude,
                  longitude: location.coords.longitude
                },
                ...mapDetails.routes[index].collectionPoints,
                mapDetails.routes[index].dumpingGround
              ]
            ) : (
              [
                mapDetails.routes[index].dumpingGround,
                ...mapDetails.routes[index].collectionPoints
              ]
            )
          }
          strokeColor="#0bb4f1" // fallback for when `strokeColors` is not supported by the map-provider
          strokeColors={[
            "#7F0000",
            "#00000000", // no color, creates a "long" gradient between the previous and next coordinate
            "#B24112",
            "#E5845C"
          ]}
          strokeWidth={6}
        />
      </MapView>}
      {location && <Button
        style={{width: Dimensions.get('window').width, height: 35}}
        onPress={_goback}
        title="End Trip"
        color="#841584"
        accessibilityLabel="Go back to home"
      />
      /*<Button
        text="End Trip"
        textStyle={{ fontWeight: "700" }}
        onPress={_goback}
        buttonStyle={{
          backgroundColor: "rgba(92, 99,216, 1)",
          width: 300,
          height: 35,
          borderColor: "transparent",
          borderWidth: 0,
          borderRadius: 5
        }}
        containerStyle={{ marginTop: 20 }}
      />
      */}
      </View>
    );
  }
}

// followsUserLocation={true}
// image={collectionPoint}

// followsUserLocation={true}
// liteMode={true}
