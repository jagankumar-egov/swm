import React from "react";

const Common = ({ spec, children }) => {
  return <div className={`${spec.className}`}>{children}</div>;
};

export default Common;
