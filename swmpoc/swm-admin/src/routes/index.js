import React from 'react';
import {Switch, Route} from 'react-router-dom';
import Tracker from '../components/content/TrackerPage';
import DriverCreate from '../components/content/Driver/Create'
import CollectionPointCreate from '../components/content/CollectionPoint/Create'
import DumpingGroundCreate from '../components/content/DumpingGround/Create'
import VehicleCreate from '../components/content/vehicle/create';
const base = "";

const Main = () => {
    return (
      <main style={{"marginBottom": "50px"}}>
        <Switch>
          <Route exact path={base+"/"} component={()=>{
            return (
              <div>
                  tracking
              </div>
            )
          }}/>

          <Route exact path={base+"/create-router"} component={()=>{
            return (
              <div>
                  crate routes
              </div>
            )
          }}/>

          <Route exact path={base+"/create-driver"} component={DriverCreate}/>
          <Route exact path={base+"/create-collection-point"} component={CollectionPointCreate}/>
          <Route exact path={base+"/create-dumping-ground"} component={DumpingGroundCreate}/>
          <Route exact path={base+"/create-vehicle"} component={VehicleCreate}/>

        </Switch>
      </main>
     )
   }

export default(
     <Main/>
);
