import React from 'react';

const Column=({spec,children})=>{
  return (
    <div className={`${spec.className}`}>
      {children}
    </div>
  )
}

export default Column;
