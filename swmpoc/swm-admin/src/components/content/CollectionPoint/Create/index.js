import React from 'react';
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


class CollectionPointCreate extends React.Component {
  state = {
    name: '',
    code: '',
    tenantId: '',
    ward: '',
    street: '',
    colony: '',
    binId: '',
    latitude: '',
    longitude: '',
  };

  handleChange = name => event => {
    this.setState({
      [name]: event.target.value,
    });
  };

  onSubmit = async(e) =>{
    let _points=[];
    _points[0]={...this.state};
    let requestBody={};
    requestBody.points=_points;
    let response=commonApi("post","/waste-management/v1/collectionPoint/_create",{},requestBody);
  }

  render() {
    const { classes } = this.props;

    return (
      <form className={classes.container} noValidate autoComplete="off" onSubmit={this.onSubmit}>
        <TextField
          id="name"
          label="Name"
          className={classes.textField}
          value={this.state.name}
          onChange={this.handleChange('name')}
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
          id="ward"
          label="Ward"
          className={classes.textField}
          value={this.state.ward}
          onChange={this.handleChange('ward')}
          margin="normal"
        />
        <TextField
          id="street"
          label="Street"
          className={classes.textField}
          value={this.state.street}
          onChange={this.handleChange('street')}
          margin="normal"
        />
        <TextField
          id="colony"
          label="Colony"
          className={classes.textField}
          value={this.state.colony}
          onChange={this.handleChange('colony')}
          margin="normal"
        />
        <TextField
          id="binId"
          label="Bin Id"
          className={classes.textField}
          value={this.state.binId}
          onChange={this.handleChange('binId')}
          margin="normal"
        />
        <TextField
          id="latitude"
          label="Latitude"
          value={this.state.latitude}
          onChange={this.handleChange('latitude')}
          className={classes.textField}
          onChange={this.handleChange('latitude')}
          margin="normal"
        />
        <TextField
          id="longitude"
          label="Longitude"
          value={this.state.longitude}
          onChange={this.handleChange('longitude')}
          className={classes.textField}
          onChange={this.handleChange('longitude')}
          margin="normal"
        />
        <br/>
        <Button variant="contained" type="submit"  color="secondary" className={classes.button}>
        Submit
        </Button>
      </form>
    );
  }
}

CollectionPointCreate.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(CollectionPointCreate);
