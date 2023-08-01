import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import AuthenticationPage from './pages/AuthenticationPage';
import { AuthenticationProvider } from './context/AuthenticationContext';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <AuthenticationProvider>
    <div className="h-screen">
      <AuthenticationPage />
      {/* <App /> */}
    </div>
  </AuthenticationProvider>
);