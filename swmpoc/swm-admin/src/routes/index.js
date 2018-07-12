import React from 'react';
import {Switch, Route} from 'react-router-dom';
import Tracker from '../components/content/TrackerPage';
import DriverCreate from '../components/content/Driver/Create'
import CollectionPointCreate from '../components/content/CollectionPoint/Create'
import DumpingGroundCreate from '../components/content/DumpingGround/Create'
import VehicleCreate from '../components/content/vehicle/create';
import RouteCreate from '../components/content/route/create';

const base = "";

const Main = () => {
    return (
      <main style={{"marginBottom": "50px"}}>
        <Switch>
          <Route exact path={base+"/"} component={Tracker}/>

          <Route exact path={base+"/create-driver"} component={DriverCreate}/>
          <Route exact path={base+"/create-collection-point"} component={CollectionPointCreate}/>
          <Route exact path={base+"/create-dumping-ground"} component={DumpingGroundCreate}/>
          <Route exact path={base+"/create-vehicle"} component={VehicleCreate}/>
          <Route exact path={base+"/create-route"} component={RouteCreate}/>

        </Switch>
      </main>
     )
   }

export default(
     <Main/>
);
