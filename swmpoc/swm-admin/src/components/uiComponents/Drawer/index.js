import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Drawer from '@material-ui/core/Drawer';
import List from '@material-ui/core/List';
import Divider from '@material-ui/core/Divider';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
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
  }
});

const ClippedDrawer=(props)=>{
  const {classes}=props;
  return (
  <div className={classes.root}>
    <Drawer
        variant="permanent"
        classes={{
          paper: classes.drawerPaper,
        }}
      >
        <List component="nav">
          <Link to="/">
            <ListItem button>
              <ListItemText primary="Dashboard" />
            </ListItem>
          </Link>
        <Link to="/create-driver">
          <ListItem button>
            <ListItemText primary="Create driver" />
          </ListItem>
        </Link>
        <Link to="/create-router">
          <ListItem button>
            <ListItemText primary="Create route" />
          </ListItem>
        </Link>
       </List>

      </Drawer>
    </div>
  )
}

ClippedDrawer.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(ClippedDrawer);
