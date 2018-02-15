import React, { Component } from "react";
import { translate, Trans } from 'react-i18next';
import FooterWithProps from '../../uiComponents/FooterLogo/FooterWithProps';

const { availWidth } = window.screen;

class NoInternet extends Component {

	render() {
		const { t, availWidth } = this.props;

		return (
			<div className="page-404">
				{/* <img className="image-404" src={availWidth < 479 ? backgroundS : (availWidth < 769 ? background : (availWidth < 1025 ? background2x : background3x))} width="100%" height="100%" alt={t('message404')}/> */}
				<div className="overlay-text" style={{"minWidth" : (availWidth > 550 ? "550px" : "270px")}}>
					<div className="conn-header-in poppins-medium opacity-95">{t('noInternet')}</div>
					<br/>
					<div className="conn-subheader poppins-medium opacity-95">{t('noConnection')}</div>
					<br/>
					<div className="conn-detail poppins-regular opacity-95">{t('onyxWireMessage')}</div>
				</div>
			</div>
		);
	}
}

export default translate('translations')(NoInternet);