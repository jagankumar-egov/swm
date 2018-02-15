import React from "react";
import PropTypes from "prop-types";
import $ from "jquery";

class Collapsible extends React.Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    $(document).ready(function() {
      $(".collapsible").collapsible();
    });
  }

  render() {
    let {spec}=this.props;
    return (
      <ul className={`${spec.className}`} data-collapsible="accordion">
        <li>
          {spec.hasHeader && <div className={`${spec.headerClassName}`}>
            {spec.hasHeaderIcon && <i className={`${spec.headerIconClassName}`}>{spec.headerIconName}</i>}{spec.headerContent}
          </div>}
          <div className={`${spec.bodyClassName}`}>
            {spec.bodyContent}
          </div>
        </li>
      </ul>
    );
  }
}

export default Collapsible;
