import React from 'react';

const Row=({spec,children})=>{
  return (
    <div className={`${spec.className}`}>
      {children}
    </div>
  )
}
export default Row;
