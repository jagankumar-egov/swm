import 'materialize-css/dist/css/materialize.min.css';
import 'materialize-css/dist/js/materialize.min.js';

import React from 'react';
import ReactDOM from 'react-dom';
import './assets/styles/index.css';
import { HashRouter,BrowserRouter } from 'react-router-dom';
import App from './components/App';
import registerServiceWorker from './registerServiceWorker';
import { I18nextProvider } from 'react-i18next';
import i18n from './utility/i18n';

ReactDOM.render(<HashRouter><I18nextProvider i18n={ i18n }><App /></I18nextProvider></HashRouter>, document.getElementById('root'));
registerServiceWorker();
