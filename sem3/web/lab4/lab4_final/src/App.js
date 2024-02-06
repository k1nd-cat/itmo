import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import StartPage from './pages/start';
import MainPage from './pages/main';

export const REST_API = 'http://localhost:8080/lab4_rest';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="*" element={<StartPage />} />
        <Route path="main" element={<MainPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;