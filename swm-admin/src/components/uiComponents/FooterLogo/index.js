import React from "react";
import logo2x from '../../../assets/images/logo/logo@2x.png';

const FooterLogo = ({styles, label, marginTop}) => {
	return (
		<div className="row roboto-regular footer-order-details">
			<div className="col s12 center">
					<span>{label}</span> &nbsp;&nbsp;<img src={logo2x} width='100px' style={{marginBottom: "-5px"}}/>
			</div>
		</div>
	);
}

export default FooterLogo;
