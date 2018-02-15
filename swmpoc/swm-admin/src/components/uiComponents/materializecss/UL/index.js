import React,{Component} from 'react';
import PropTypes from 'prop-types';
import $ from 'jquery';

class UL extends Component {
  componentDidMount()
  {
    let {componentDidMount}=this.props;
    eval(componentDidMount);
  }

  render()
  {
    let {spec,children}=this.props;
    return (
      <ul {...spec}>
        {children}
      </ul>
    )
  }
}

UL.propTypes = {
  spec:PropTypes.object
}

export default UL;
