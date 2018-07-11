import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import MenuItem from '@material-ui/core/MenuItem';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';

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


class DriverCreate extends React.Component {
  state = {
    name: '',
    code: '',
    tenantId: '',
    dateOfBirth: '',
    bloodGroup:'',
    aadhaarNo:'',
    licenseNo:'',
    phoneNo:'',
    emailId:'',
  };

  handleChange = name => event => {
    this.setState({
      [name]: event.target.value,
    });
  };

  render() {
    const { classes } = this.props;

    return (
      <form className={classes.container} noValidate autoComplete="off">
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
          onChange={this.handleChange('value')}
          margin="normal"
        />
        <TextField
        id="date"
        label="Date Of Birth"
        type="date"
        defaultValue="2017-05-24"
        className={classes.textField}
        InputLabelProps={{
          shrink: true,
        }}
        value={this.state.dateOfBirth}
        onChange={this.handleChange('dateOfBirth')}
        margin="normal"
        />
        <TextField
          id="bloodGroup"
          label="Blood Group"
          className={classes.textField}
          value={this.state.bloodGroup}
          onChange={this.handleChange('bloodGroup')}
          margin="normal"
        />
        <TextField
          id="aadhaarNo"
          label="Aadhaar No"
          value={this.state.aadhaarNo}
          onChange={this.handleChange('aadhaarNo')}
          className={classes.textField}
          onChange={this.handleChange('aadhaarNo')}
          margin="normal"
        />
        <TextField
          id="licenseNo"
          label="License No"
          value={this.state.licenseNo}
          onChange={this.handleChange('licenseNo')}
          className={classes.textField}
          onChange={this.handleChange('licenseNo')}
          margin="normal"
        />
        <TextField
          id="phoneNo"
          label="Phone No"
          value={this.state.phoneNo}
          onChange={this.handleChange('phoneNo')}
          className={classes.textField}
            margin="normal"
        />
        <TextField
          id="emailId"
          label="EmailId"
          value={this.state.emailId}
          onChange={this.handleChange('emailId')}
          className={classes.textField}
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

DriverCreate.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(DriverCreate);
