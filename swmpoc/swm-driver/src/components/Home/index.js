import React from "react";
import { View } from "react-native";
import { Card } from "react-native-elements";
import VehicleDetails from "./VehicleDetails";
import CollectionMap from "./CollectionMap";

export default class Home extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isVehicleNumberEntered: false,
      mapDetails: {
        routes: [
          {
            dumpingGround: {
              latitude: 12.9234947,
              longitude: 77.6851068999999,
              latitudeDelta: 0.0922,
              longitudeDelta: 0.0421,
              title: "Dumping ground 1",
              description: "Test dumping ground"
            },
            collectionPoints: [
              {
                latitude: 12.9188752,
                longitude: 77.6701266,
                latitudeDelta: 0.0922,
                longitudeDelta: 0.0421,
                title: "Collection point 1",
                description: "Test collection points"
              },
              {
                latitude: 12.9201078,
                longitude: 77.6708444999999,
                latitudeDelta: 0.0922,
                longitudeDelta: 0.0421,
                title: "Collection point 2",
                description: "Test collection points"
              },
              {
                latitude: 12.9223732,
                longitude: 77.6719273999999,
                latitudeDelta: 0.0922,
                longitudeDelta: 0.0421,
                title: "Collection point 3",
                description: "Test collection points"
              },
              {
                latitude: 12.9283654,
                longitude: 77.6824808999999,
                latitudeDelta: 0.0922,
                longitudeDelta: 0.0421,
                title: "Collection point 3",
                description: "Test collection points"
              },
              {
                latitude: 12.9256681,
                longitude: 77.6863472,
                latitudeDelta: 0.0922,
                longitudeDelta: 0.0421,
                title: "Collection point 3",
                description: "Test collection points"
              }
            ]
          },
          {
            dumpingGround: {
              latitude: 12.928366,
              longitude: 77.6317758999999,
              latitudeDelta: 0.0922,
              longitudeDelta: 0.0421,
              title: "Dumping ground 1",
              description: "Test dumping ground"
            },
            collectionPoints: [
              {
                latitude: 12.9188752,
                longitude: 77.6701266,
                latitudeDelta: 0.0922,
                longitudeDelta: 0.0421,
                title: "Collection point 1",
                description: "Test collection points"
              },
              {
                latitude: 12.9197637,
                longitude: 77.6685876999999,
                latitudeDelta: 0.0922,
                longitudeDelta: 0.0421,
                title: "Collection point 2",
                description: "Test collection points"
              },
              {
                latitude: 12.925194,
                longitude: 77.6386129999999,
                latitudeDelta: 0.0922,
                longitudeDelta: 0.0421,
                title: "Collection point 3",
                description: "Test collection points"
              },
              {
                latitude: 12.9248121,
                longitude: 77.6337108999999,
                latitudeDelta: 0.0922,
                longitudeDelta: 0.0421,
                title: "Collection point 3",
                description: "Test collection points"
              },
              {
                latitude: 12.928632,
                longitude: 77.6329729999999,
                latitudeDelta: 0.0922,
                longitudeDelta: 0.0421,
                title: "Collection point 3",
                description: "Test collection points"
              },
              {
                latitude: 12.929584,
                longitude: 77.6329299999999,
                latitudeDelta: 0.0922,
                longitudeDelta: 0.0421,
                title: "Collection point 3",
                description: "Test collection points"
              }
            ]
          },
          {
            dumpingGround: {
              latitude: 12.9288091,
              longitude: 77.5858991999999,
              latitudeDelta: 0.0922,
              longitudeDelta: 0.0421,
              title: "Dumping ground 1",
              description: "Test dumping ground"
            },
            collectionPoints: [
              {
                latitude: 12.9188752,
                longitude: 77.6701266,
                latitudeDelta: 0.0922,
                longitudeDelta: 0.0421,
                title: "Collection point 1",
                description: "Test collection points"
              },
              {
                latitude: 12.916546,
                longitude: 77.641994,
                latitudeDelta: 0.0922,
                longitudeDelta: 0.0421,
                title: "Collection point 2",
                description: "Test collection points"
              },
              {
                latitude: 12.9156454024866,
                longitude: 77.6379679275779,
                latitudeDelta: 0.0922,
                longitudeDelta: 0.0421,
                title: "Collection point 3",
                description: "Test collection points"
              },
              {
                latitude: 12.9178576,
                longitude: 77.6245242999999,
                latitudeDelta: 0.0922,
                longitudeDelta: 0.0421,
                title: "Collection point 3",
                description: "Test collection points"
              },
              {
                latitude: 12.91632,
                longitude: 77.6137939999999,
                latitudeDelta: 0.0922,
                longitudeDelta: 0.0421,
                title: "Collection point 3",
                description: "Test collection points"
              },
              {
                latitude: 12.9156753,
                longitude: 77.5998551999999,
                latitudeDelta: 0.0922,
                longitudeDelta: 0.0421,
                title: "Collection point 3",
                description: "Test collection points"
              }
            ]
          }
        ],
        vehicleType: "truck",
        vehicleIconUrl:
          "https://s3.amazonaws.com/locus-client-assets/test/vehicles/car.png"
      },
      inputData: {}
    };
  }

  _startMyRoute = status => {
    this.setState({
      isVehicleNumberEntered: status
    });
  };

  _setInputData = (prop, itemValue, itemIndex) => {
    this.setState({
      inputData: {
        ...this.state.inputData,
        [prop]: itemValue
      }
    });
  };

  render() {
    let { _startMyRoute, _setInputData } = this;
    let { isVehicleNumberEntered, mapDetails, inputData } = this.state;
    return isVehicleNumberEntered ? (
      <CollectionMap
        mapDetails={mapDetails}
        inputData={inputData}
        startMyRoute={_startMyRoute}
      />
    ) : (
      <VehicleDetails
        startMyRoute={_startMyRoute}
        setInputParent={_setInputData}
        inputData={inputData}
      />
    );
  }
}
