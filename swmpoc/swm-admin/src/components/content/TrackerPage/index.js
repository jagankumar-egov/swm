import React, { Component } from "react";
import MapsInfo from "./MapInfo";
import { commonApi } from "../../../utility/api";
import _ from "lodash";
import Loader from "../Loader";
import $ from "jquery";
import jp from "jsonpath";
import {SocketProvider, socketConnect} from 'socket.io-react';
import io from 'socket.io-client';

// NB: The environment variable MUST be prefixed with REACT_APP_ to get passed through properly
// See https://github.com/facebook/create-react-app/issues/102
const SOCKET_URL = process.env["REACT_APP_SOCKETIO_SERVER"] || "ws://172.16.4.164:3005/location";
const socket = io.connect(SOCKET_URL);

var appDataInitData = {
  userLocation:{
    address: null,
    latLng: { lat: 12.9226, lng: 77.6174, accuracy: 0 }
  },
  vehicleLocations: [],
  vehicleIconUrl:
    "",
  source: {
    address:
      "UTPL Campus, Warehouse No.1, survey no.35,38,39, IDA Block A, Mindi Village, Gajuwaka, Visakhapatnam 530012",
    latLng: { lat: 17.70080106712, lng: 83.213237792356, accuracy: 0 }
  },
  destination: {
    address:
      "6-3-1  Ramalayam Veedhi, Old Gajuwaka lbs nagar ramalayam veedhi Visakhapatnam 530026",
    latLng: { lat: 17.68660398505811, lng: 83.20296529632571, accuracy: 0 }
  },
  eta: "2016-08-21T19:58:34.272+0000",
  vehicleType: "TRUCK",
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
  ]
};

var timerObject;

class Tracker extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isValidId: true,
      isLoading: false,
      online: window.navigator.onLine,
      appData: appDataInitData,
      refreshSeconds: 30000,
      availWidth: window.screen.availWidth
    };
  }

  componentDidMount() {
    let { initialize } = this;
    let self = this;
    initialize();
    window.addEventListener("resize", () => {
      self.setState({
        availWidth: window.screen.availWidth
      });
    });
  }

  componentWillUnmount() {
    clearInterval(timerObject);
  }

  initialize = () => {
    let self = this;
    let { state } = self;
    let {appData} =this.state;
    socket.on('customer_list', (msg) => {
    console.log(msg);

      if(!_.isEmpty(msg)) {
        msg = JSON.parse(msg);
        if(appData.vehicleLocations && !_.isEmpty(msg.coords)) {
          if(!_.find(appData.vehicleLocations, {'vehicleNo': msg["vehicleNo"]})) {
            let vehicleLocation = {};
            vehicleLocation.vehicleNo= msg["vehicleNo"];
            vehicleLocation.latLng={};
            vehicleLocation.latLng.lat= msg.coords["latitude"];
            vehicleLocation.latLng.lng= msg.coords["longitude"];
            vehicleLocation.latLng.accuracy= msg.coords["accuracy"];
            vehicleLocation.address=null;
            appData.vehicleLocations.push(vehicleLocation);
          }
          else {
            appData.vehicleLocations.map((item) => {
              if(item.vehicleNo && item.vehicleNo===msg["vehicleNo"]) {
                item.latLng={};
                item.latLng.lat= msg.coords["latitude"];
                item.latLng.lng= msg.coords["longitude"];
                item.latLng.accuracy= msg.coords["accuracy"];
                item.address=null;
              }
            })
          }
        }
      }
      console.log(appData.vehicleLocations);

      self.setState({
        ...state,
        appData: appData
      });
    });

  };

  render() {
    let { online, isValidId, appData, isLoading, availWidth } = this.state;
    let { initialize } = this;
    return (
      <div>
        {online ? isValidId ? isLoading ? (
          <Loader />
        ) : (
          <div
            ref={ref => (this.fCon = ref)}
            // className="flex-container"
            // style={{ overflowY: availWidth >= 767 ? "hidden" : "auto" }}
          >
            {appData && <MapsInfo
              {...appData}
              refresh={initialize}
              availWidth={availWidth}
            />}
          </div>
        ) : (
          <div/>
        ) : (
          <div />
        )}
      </div>
    );
  }
}

export default Tracker;
