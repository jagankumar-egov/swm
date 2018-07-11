import React from 'react';
import ReactDOM from 'react-dom';
import './assets/styles/index.css';
import { HashRouter,BrowserRouter } from 'react-router-dom';
import App from './components/App';
import registerServiceWorker from './registerServiceWorker';


ReactDOM.render(<HashRouter><App /></HashRouter>, document.getElementById('root'));
registerServiceWorker();
