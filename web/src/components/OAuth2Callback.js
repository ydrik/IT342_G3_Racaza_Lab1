import React, { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';

/**
 * OAuth2 Callback Handler
 * FRS Feature 4.2: Google OAuth Login
 * Handles OAuth2 redirect after successful Google authentication
 * Extracts JWT token from URL and stores it
 */
const OAuth2Callback = () => {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    useEffect(() => {
        const handleOAuthCallback = () => {
            // Extract token from URL query parameter
            const token = searchParams.get('token');
            const error = searchParams.get('error');

            if (error) {
                alert('OAuth login failed: ' + error);
                navigate('/login');
                return;
            }

            if (token) {
                // Store JWT token
                localStorage.setItem('token', token);
                
                // Redirect to dashboard
                navigate('/dashboard');
            } else {
                alert('No token received from OAuth provider');
                navigate('/login');
            }
        };

        handleOAuthCallback();
    }, [searchParams, navigate]);

    return (
        <div style={styles.container}>
            <div style={styles.spinner}></div>
            <p style={styles.text}>Completing Google sign-in...</p>
        </div>
    );
};

const styles = {
    container: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        minHeight: '100vh',
        backgroundColor: 'var(--app-bg)',
        gap: '20px'
    },
    spinner: {
        width: '48px',
        height: '48px',
        border: '4px solid var(--border-color)',
        borderTop: '4px solid var(--primary)',
        borderRadius: '50%',
        animation: 'spin 1s linear infinite'
    },
    text: {
        fontSize: '16px',
        color: 'var(--text-secondary)',
        fontWeight: '500'
    }
};

export default OAuth2Callback;
