import React from "react";
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import MenuItem from '@material-ui/core/MenuItem';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import {commonApi} from '../../../../utility/api';

const styles = theme => ({
  container: {
    display: 'flex',
    flexWrap: 'wrap',
  },
  textField: {
    marginLeft: theme.spacing.unit,
    marginRight: theme.spacing.unit,
    width: 300,
  },
  button: {
   margin: theme.spacing.unit,
   height: 40,
 },
});

class VehicleCreate extends React.Component {
  state = {
  	company:'',
    model:'',
    vehicleNo:'',
    type:'',
    capacity:'',
    color:'',
    owner:'',
    purchaseDate:'',
    code:'',
    tenantId:'',
    driverId:'',
};

handleChange = name => event => {
  this.setState({
    [name]: event.target.value,
  });
};

onSubmit = async(e) =>{
  let _vehicles=[];
  _vehicles[0]={...this.state};
  let epoch=new Date(this.state.purchaseDate).getTime();
  _vehicles[0]["purchaseDate"]=epoch;
  let requestBody={};
  requestBody.vehicles=_vehicles;
  let response=commonApi("post","/waste-management/v1/vehicle/_create",{},requestBody);
}
  render() {
    const { classes } = this.props;

    return (
      <form className={classes.container} noValidate autoComplete="off" onSubmit={this.onSubmit}>
        <TextField
          id="company"
          label="Company"
          className={classes.textField}
          value={this.state.company}
          onChange={this.handleChange('company')}
          margin="normal"
        />
        <TextField
          id="model"
          label="Model"
          className={classes.textField}
          value={this.state.model}
          onChange={this.handleChange('model')}
          margin="normal"
        />
        <TextField
          id="vehicleno"
          label="Vehicle Number"
          className={classes.textField}
          value={this.state.vehicleno}
          onChange={this.handleChange('vehicleno')}
          margin="normal"
        />
        <TextField
          id="type"
          label="Type"
          className={classes.textField}
          value={this.state.type}
          onChange={this.handleChange('type')}
          margin="normal"
        />
        <TextField
          id="capacity"
          label="Capacity"
          className={classes.textField}
          value={this.state.capacity}
          onChange={this.handleChange('capacity')}
          margin="normal"
        />
        <TextField
          id="color"
          label="Color"
          className={classes.textField}
          value={this.state.color}
          onChange={this.handleChange('color')}
          margin="normal"
        />
        <TextField
          id="owner"
          label="Owner"
          className={classes.textField}
          value={this.state.owner}
          onChange={this.handleChange('owner')}
          margin="normal"
        />
        <TextField
          id="purchaseDate"
          label="Purchase Date"
          type="date"
          className={classes.textField}
          value={this.state.purchaseDate}
          InputLabelProps={{
            shrink: true,
          }}
          onChange={this.handleChange('purchaseDate')}
          margin="normal"
        />
        <TextField
          id="code"
          label="Code"
          className={classes.textField}
          value={this.state.code}
          onChange={this.handleChange('code')}
          margin="normal"
        />
        <TextField
          id="tenantId"
          label="Tenant Id"
          className={classes.textField}
          value={this.state.tenantId}
          onChange={this.handleChange('tenantId')}
          margin="normal"
        />
        <TextField
          id="driverId"
          label="Driver Id"
          className={classes.textField}
          value={this.state.driverId}
          onChange={this.handleChange('driverId')}
          margin="normal"
        />
        <Button type="submit" variant="contained" color="secondary" className={classes.button}>
          Submit
        </Button>

      </form>
    );
  }
}



VehicleCreate.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(VehicleCreate);
