import React from "react";

const Icon=({spec})=>{
  return (
    <i className={`${spec.className}`}>{spec.iconName}</i>
  )
}

export default Icon;
