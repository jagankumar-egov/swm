import React from "react";
import PropTypes from "prop-types";

const LI = ({ spec, children }) => {
  return <LI {...spec}>{children && children}</LI>;
};

LI.propTypes = {
  spec: PropTypes.object
};

export default LI;
