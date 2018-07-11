import React from 'react';
import {Switch, Route} from 'react-router-dom';
import Tracker from '../components/content/TrackerPage';

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

          <Route exact path={base+"/create-driver"} component={()=>{
            return (
              <div>
                  crate driver
              </div>
            )
          }}/>
        </Switch>
      </main>
     )
   }

export default(
     <Main/>
);
