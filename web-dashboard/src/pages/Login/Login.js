import './Login.scss';
import '../../components/Common/Common.scss';
import React from 'react';
import { useEffect, useState } from 'react';

const Login = () => {
    const [showPassword, setShowPassword] = useState(false);

    const [formData, setFormData] = useState({
        email:"",
        password:""
    })

    const showPasswordVisiblity = () => {
        setShowPassword(showPassword ? false : true);
    };

    //login
    const handleLogin = async () => {};

    const handlePressEnter = (event)=> {
        if(event.code === "Enter"){
            handleLogin();
        }
    }

    //register new account
    const handleRegister = () => {};

    //process handle forget password
    const handleForgetPassword = () => {};

    return(
        <div className='container mt-5'>
            <div className='row mx-auto'>
                <div className='col-12 col-md-4 mx-auto text-center d-flex flex-column gap-4'>
                    <h4>Đăng nhập</h4>
                    <input
                        type='email'
                        style={{ borderWidth: '1.3px' }}
                        className='form-control'
                        placeholder='Email'
                        value={formData.email}
                        onChange={(event) => {setFormData({ ...formData, email: event.target.value })}}
                    />
                    <div className="position-relative">
                        <input
                            type={showPassword ? "text" : "password"}
                            style={{ borderWidth: '1.3px' }}
                            className='form-control'
                            placeholder='Mật khẩu'
                            value={formData.password}
                            onChange={(event) => {setFormData({ ...formData, password: event.target.value })}}
                            onKeyDown={(event) => handlePressEnter(event)}
                        />
                        <i className={`fa ${showPassword ? 'fa fa-eye-slash' : 'fa fa-eye'}`} 
                            onClick={showPasswordVisiblity}
                            style={{
                            position: 'absolute',
                            top: '50%',
                            right: '15px',
                            transform: 'translateY(-50%)',
                            cursor: 'pointer',
                            color: '#878585'
                        }}/>
                    </div>
                    <button className='btn btn-primary' onClick={handleLogin}>Đăng nhập</button> 
                    <span className='text-center'>
                        Chưa có tài khoản? <a className='register-account' onClick={handleRegister}>Đăng ký tài khoản mới</a>
                    </span> 
                    <span className='text-center'>
                        <a className='forgot-password' onClick={handleForgetPassword}>Quên mật khẩu?</a>
                    </span>               
                </div>
            </div>
        </div>
    )
};
export default Login;