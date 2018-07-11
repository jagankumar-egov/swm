import React from 'react';
import {Switch, Route} from 'react-router-dom';
import Tracker from '../components/content/TrackerPage';
<<<<<<< Updated upstream
import DriverCreate from '../components/content/Driver/Create'
import CollectionPointCreate from '../components/content/CollectionPoint/Create'
import DumpingGroundCreate from '../components/content/DumpingGround/Create'
=======
import DriverCreate from '../components/content/Driver/Create';
import VehicleCreate from '../components/content/vehicle/create';
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
          <Route exact path={base+"/create-collection-point"} component={CollectionPointCreate}/>
          <Route exact path={base+"/create-dumping-ground"} component={DumpingGroundCreate}/>
=======

          <Route exact path={base+"/create-vehicle"} component={VehicleCreate}/>

>>>>>>> Stashed changes
        </Switch>
      </main>
     )
   }

export default(
     <Main/>
);
