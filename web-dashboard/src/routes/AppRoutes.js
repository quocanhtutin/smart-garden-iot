import { Routes, Route } from 'react-router-dom';
import Dashboard from '../pages/Dashboard/Dashboard';
import Login from '../pages/Login/Login';
import Account from '../pages/Account/Account';

const AppRoutes = (props) => {
    return (
        <Routes>
            <Route path="/" element={<Dashboard isSidebarOpen={props.isSidebarOpen} />} />
            <Route path="/login" element={<Login />} />
            <Route path="/account" element={<Account />} />
        </Routes>
    );
}

export default AppRoutes;