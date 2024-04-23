import React from 'react';
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min";
//import css
import './App.css';
import AddRoom from './Components/Room/AddRoom'; // Atualize o caminho de importação

function App() {
  return (
    <>
      <div>
        <AddRoom />
      </div>
    </>
  );
}

export default App;
