import React, { Component } from 'react';
import routes from '../../routes';
import AppBar from "../uiComponents/AppBar";
import Drawer from "../uiComponents/Drawer";
import Grid from '@material-ui/core/Grid';
import './index.css';
import '../../assets/styles/App.css';

class App extends Component {
  render() {
    return (
      <div className="App">
        <AppBar/>
        <Grid container>
          <Grid item xs={2}>
            <Drawer/>
          </Grid>
          <Grid item xs={10}>
            { routes }
          </Grid>
        </Grid>
      </div>
    );
  }
}

export default App;


//appbar
//left menu
//right content
