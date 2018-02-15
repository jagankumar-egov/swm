import React from 'react';
import PropTypes from 'prop-types'


const ComponentTemplate=({spec,children})=>{
  return (
    <div className={`${spec.className}`}>
      {children}
    </div>
  )
}

export default ComponentTemplate;
