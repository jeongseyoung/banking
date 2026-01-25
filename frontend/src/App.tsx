import './App.css';
import LoginPage from './pages/LoginPage';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import BankingMain from './pages/BankingMain';
import { AuthenticatedRoute } from './utils/AuthRoute';
import { useAuthStore } from './store/UseAuthStore';
import { useEffect } from 'react';
import AccountsTab from './pages/tabs/AccountsTab';

function App() {

  const checkAuth = useAuthStore((state) => state.checkAuth);

  useEffect(() => {
    checkAuth();
  },[checkAuth]);

  return (
    <BrowserRouter>
      <Routes>
        <Route path='/' element={<LoginPage />}/>

        <Route path='/main' element={
          <AuthenticatedRoute>
            <BankingMain />
          </AuthenticatedRoute>} /> 

        {/* <Route path='/api/account'>

          <Route path='accounttab' element={
            <AuthenticatedRoute>
              <AccountsTab />
            </AuthenticatedRoute> } />

        </Route>        */}
      </Routes>
    </BrowserRouter>
  );
}

export default App;
