import React from "react";

const Button=({spec,children})=>
{
  return (
    <a className={`${spec.className}`}>{children}</a>
  )
}

export default Button;
