import React from "react";
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

class RouteCreate extends React.Component {
  state = {
  	name:'',
    code:'',
    tenantId:'',
    dumpingGroundId:'',
    collectionPointIds:'',
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
          onChange={this.handleChange('tenantId')}
          margin="normal"
        />
        <TextField
          id="collectionPointIds"
          label="Collection Point Ids"
          multiline
          rowsMax="3"
          className={classes.textField}
          value={this.state.collectionPointIds}
          onChange={this.handleChange('collectionPointIds')}
          margin="normal"
        />
        <TextField
          id="dumpingGroundId"
          label="Dumping Ground Id"
          className={classes.textField}
          value={this.state.dumpingGroundId}
          onChange={this.handleChange('dumpingGroundId')}
          margin="normal"
        />
        <Button type="submit" variant="contained" color="secondary" className={classes.button}>
          Submit
        </Button>

      </form>
    );
  }
}



RouteCreate.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(RouteCreate);
