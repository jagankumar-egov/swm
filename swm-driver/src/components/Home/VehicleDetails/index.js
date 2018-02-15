import React from 'react';
import { Picker, Button,Image } from 'react-native'
import { Card } from 'react-native-elements'
import truck from '../../../../assets/garbagetruck.jpg';

export default class VehicleDetails extends React.Component {
  constructor(props) {
    super(props);
  }
  handleChange = (prop, itemValue, itemIndex) => {
    this.props.setInputParent(prop, itemValue, itemIndex);
  }

  setParent = () => {
    if (this.props.inputData["regNo"] && this.props.inputData["routeNo"]) {
      this.props.startMyRoute(true);
    }
    else {
      alert("Plase enter valid data");
    }
  }

  render() {
    let { regNo, routeNo } = this.props.inputData
    return (
      <Card title="Find Your Route" style={{marginTop:"10px"}}>
        {/*<Image source={truck} style={{flex: 1 ,width: 100, height: 100 }} />*/}
        <Picker
          selectedValue={regNo}
          onValueChange={(itemValue, itemIndex) => this.handleChange("regNo", itemValue, itemIndex)}>
          <Picker.Item label="Enter Vehicle No." value="" />
          <Picker.Item label="KA1201" value="KA1201" />
          <Picker.Item label="KA1202" value="KA1202" />
          <Picker.Item label="KA1203" value="KA1203" />
        </Picker>

        <Picker
          selectedValue={routeNo}
          onValueChange={(itemValue, itemIndex) => this.handleChange("routeNo", itemValue, itemIndex)}>
          <Picker.Item label="Enter Route" value="" />
          <Picker.Item label="Route1" value="Route1" />
          <Picker.Item label="Route2" value="Route2" />
          <Picker.Item label="Route3" value="Route3" />
        </Picker>

        <Button
          onPress={() => { this.setParent() }}
          title="Start Trip"
          color="#841584"
          accessibilityLabel="Displays the route"
        />
      </Card>
    )
  }
}
