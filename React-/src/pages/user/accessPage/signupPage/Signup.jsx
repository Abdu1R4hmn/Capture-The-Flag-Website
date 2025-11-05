import React, { useState } from 'react';
import './signup.css'
import Button from '../../../../components/ui/Button';
import { Link, useNavigate } from 'react-router-dom' 

function Signup() {
    // State for signup form
    // Using useState to manage form inputs, error messages, and success messages
    const [form, setForm] = useState({
        username: '',
        email: '',
        password: '',
        confirmPassword: ''
    });
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);

    // Using useNavigate from react-router-dom to navigate after successful registration
    const navigate = useNavigate();

    // Handler functions for form submission and input changes
    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    // Submit handler for the signup form
    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccess('');

        if (form.password !== form.confirmPassword) {
            setError("Passwords do not match");
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/api/user/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    username: form.username,
                    email: form.email,
                    password: form.password
                })
            });

            const data = await response.json();

            if (!response.ok || data.status !== "SUCCESS") {
                setError(
                    data.message ||
                    data.response ||
                    'Registration failed'
                );
            } else {
                setSuccess('Registration successful! Redirecting to login...');
                setForm({ username: '', email: '', password: '', confirmPassword: '' });
                setTimeout(() => {
                    navigate('/login');
                }, 1200); // short delay to show success message
            }
        } catch (err) {
            setError('Server error');
        }
    };

    // Render the signup form
    return ( 
        <>
        <div className="signup-Section">
            <div className="image-Side">
                <img src="/images/Login_image.jpg" alt="" />
            </div>
            <div className="input-Side">
                <div className="signup-header">
                    <h1>SIGNUP</h1>
                </div>
                {error && <div className="error-message" style={{ color: 'red', textAlign: 'center', marginBottom: '0.5rem' }}>{error}</div>}
                {success && <div className="success-message" style={{ color: 'green', textAlign: 'center', marginBottom: '0.5rem' }}>{success}</div>}
                <form className="inputs" onSubmit={handleSubmit}>
                    <div className='inputbox name'>
                        <img src="/images/login_username.png" alt="" />
                        <input
                            placeholder='Name'
                            type='text'
                            name='username'
                            value={form.username}
                            onChange={handleChange}
                            
                            required
                        />  
                    </div>
                    <div className='inputbox email'>
                        <img src="/images/login_email.png" alt="" />
                        <input
                            placeholder='Email'
                            type='email'
                            name='email'
                            value={form.email}
                            onChange={handleChange}
                            required
                        />  
                    </div>
                    <div className='inputbox password'>
                    <img src="/images/login_password.png" alt="" className="icon-lock" />
                    <input
                        placeholder='Password'
                        type={showPassword ? 'text' : 'password'}
                        name='password'
                        value={form.password}
                        onChange={handleChange}
                        required
                    />  
                    <img
                        src="/images/Password.png"
                        alt="Toggle password"
                        className="icon-toggle"
                        onClick={() => setShowPassword(prev => !prev)}
                    />
                    </div>
                    <div className='inputbox conf-password'>
                    <img src="/images/login_password.png" alt="" className="icon-lock" />
                    <input
                        placeholder='Confirm Password'
                        type={showConfirmPassword ? 'text' : 'password'}
                        name='confirmPassword'
                        value={form.confirmPassword}
                        onChange={handleChange}
                        required
                    />  
                    <img
                        src="/images/Password.png"
                        alt="Toggle confirm password"
                        className="icon-toggle"
                        onClick={() => setShowConfirmPassword(prev => !prev)}
                    />
                    </div>
                    <Link to='/login'>Already Have an account?</Link>
                    <div className="signup-button">
                        <Button name="Signup" type="submit" />
                    </div>
                </form>
            </div>    
        </div>
        </>
     );
}

export default Signup;