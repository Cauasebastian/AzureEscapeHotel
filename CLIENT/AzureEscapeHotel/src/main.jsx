import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.jsx';
import './index.css';
import { Provider } from 'react-redux';
import store from './Components/store/index.js';  // Importa o store que você configurou

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <Provider store={store}>  {/* Envolva o App com o Provider */}
      <App />
    </Provider>
  </React.StrictMode>,
);
