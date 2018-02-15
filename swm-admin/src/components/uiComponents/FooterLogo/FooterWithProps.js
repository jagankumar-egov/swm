import React from "react";
import logo2x from '../../../assets/images/logo/logo@2x.png';

const FooterWithProps = ({styles, label, marginTop}) => {
	return (
		<div className="footer-404" style={styles}>
			<div style={{"marginTop": marginTop || "2px"}}> {label} &nbsp;&nbsp; </div>
			<div>
				<img src={logo2x} width='100px'/>
			</div>
		</div>
	);
}

export default FooterWithProps;
