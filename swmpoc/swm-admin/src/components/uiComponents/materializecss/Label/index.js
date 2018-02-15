import React from 'react';

const Label=({spec})=>{
  return (
    <div className={`${spec.className}`}>
      {spec.defaultValue}
    </div>
  )
}

export default Label;
