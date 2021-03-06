import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Drawer from '@material-ui/core/Drawer';
import List from '@material-ui/core/List';
import Divider from '@material-ui/core/Divider';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import Collapse from '@material-ui/core/Collapse';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import StarBorder from '@material-ui/icons/StarBorder';
import ExpandLess from '@material-ui/icons/ExpandLess';
import ExpandMore from '@material-ui/icons/ExpandMore';
import {Link} from "react-router-dom";

const drawerWidth = 240;

const styles = theme => ({
  root: {
    flexGrow: 1,
    height: 430,
    zIndex: 1,
    overflow: 'hidden',
    position: 'relative',
    display: 'flex'
  },
  drawerPaper: {
    position: 'relative',
    width: drawerWidth,
  },
  nested: {
    paddingLeft: theme.spacing.unit * 4,
  },
});

class ClippedDrawer extends React.Component{
  state={
    toggleDriver : false,
    toggleRoute: false,
    toggleVehicle: false,
    toggleCollPt: false,
    toggleDumpGr: false,
  };
  /*handleClick = name => event => {
    console.log("toggle-stat:"+[this.state.name]);
    this.setState({
      [name]: [!this.state.name],
    });
  };*/
  handleDriverClick = () => {
    this.setState({
      toggleDriver : !this.state.toggleDriver,
    });
  };
  handleVehicleClick = () => {
    this.setState({
      toggleVehicle : !this.state.toggleVehicle,
    });
  };
  handleRouteClick = () => {
    this.setState({
      toggleRoute : !this.state.toggleRoute,
    });
  };
  handleCollPtClick = () => {
    this.setState({
      toggleCollPt : !this.state.toggleCollPt,
    });
  };
  handleDumpGrClick = () => {
    this.setState({
      toggleDumpGr : !this.state.toggleDumpGr,
    });
  };
  handleDumpGrClick = () => {
    this.setState({
      toggleDumpGr : !this.state.toggleDumpGr,
    });
  };

  render() {
    const { classes } = this.props;
    return (
    <div className={classes.root}>
      <Drawer
          variant="permanent"
          classes={{
            paper: classes.drawerPaper,
          }}
        >
          <List component="nav" >
            <Link to="/" style={{ textDecoration: 'none' }}>
              <ListItem button>
                <ListItemText primary="Dashboard" />
              </ListItem>
            </Link>

          <ListItem button onClick={this.handleDriverClick}>
              <ListItemText  primary="Driver" />
              {this.state.toggleDriver ? <ExpandLess /> : <ExpandMore />}
            </ListItem>

          <Collapse in={this.state.toggleDriver} timeout="auto" unmountOnExit>
              <List component="div" disablePadding>
                <Link to="/create-driver" style={{ textDecoration: 'none' }}>
                  <ListItem button className={classes.nested}>
                    <ListItemText  primary="Add new" />
                  </ListItem>
                </Link>
              </List>
            </Collapse>

            <ListItem button onClick={this.handleVehicleClick}>
                <ListItemText  primary="Vehicle" />
                {this.state.toggleVehicle ? <ExpandLess /> : <ExpandMore />}
              </ListItem>

            <Collapse in={this.state.toggleVehicle} timeout="auto" unmountOnExit>
                <List component="div" disablePadding>
                  <Link to="/create-vehicle" style={{ textDecoration: 'none' }}>
                    <ListItem button className={classes.nested}>
                      <ListItemText  primary="Add new" />
                    </ListItem>
                  </Link>
                </List>
              </Collapse>

            <ListItem button onClick={this.handleRouteClick}>
                <ListItemText  primary="Route" />
                {this.state.toggleRoute ? <ExpandLess /> : <ExpandMore />}
              </ListItem>

            <Collapse in={this.state.toggleRoute} timeout="auto" unmountOnExit>
                <List component="div" disablePadding>
                  <Link to="/create-route" style={{ textDecoration: 'none' }}>
                    <ListItem button className={classes.nested}>
                      <ListItemText  primary="Add new" />
                    </ListItem>
                  </Link>
                </List>
            </Collapse>

            <ListItem button onClick={this.handleCollPtClick}>
                <ListItemText  primary="Collection Point" />
                {this.state.toggleCollPt ? <ExpandLess /> : <ExpandMore />}
              </ListItem>

            <Collapse in={this.state.toggleCollPt} timeout="auto" unmountOnExit>
                <List component="div" disablePadding>
                  <Link to="/create-collection-point" style={{ textDecoration: 'none' }}>
                    <ListItem button className={classes.nested}>
                      <ListItemText  primary="Add new" />
                    </ListItem>
                  </Link>
                </List>
            </Collapse>

            <ListItem button onClick={this.handleDumpGrClick}>
                <ListItemText  primary="Dumping Ground" />
                {this.state.toggleDumpGr ? <ExpandLess /> : <ExpandMore />}
              </ListItem>

            <Collapse in={this.state.toggleDumpGr} timeout="auto" unmountOnExit>
                <List component="div" disablePadding>
                  <Link to="/create-dumping-ground" style={{ textDecoration: 'none' }}>
                    <ListItem button className={classes.nested}>
                      <ListItemText  primary="Add new" />
                    </ListItem>
                  </Link>
                </List>
            </Collapse>


         </List>
        </Drawer>
      </div>
    )
  }
}

ClippedDrawer.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(ClippedDrawer);
