import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const LoginPage = () => {
    const [credentials, setCredentials] = useState({ identifier: '', password: '' });
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    const handleChange = (e) => {
        setCredentials({ ...credentials, [e.target.name]: e.target.value });
    };

    const handleLogin = async (e) => {
        e.preventDefault();
        setError('');
        setIsLoading(true);

        const payload = {
            identifier: credentials.identifier.trim(),
            password: credentials.password
        };

        try {
            const response = await axios.post('http://localhost:8080/api/auth/login', payload);
            
            if (response.status === 200) {
                // Activity Diagram: Generate and store Session Token (JWT)
                const token = response.data.token;
                localStorage.setItem('token', token);
                
                // Activity Diagram: Redirect to Dashboard
                navigate('/dashboard');
            }
        } catch (err) {
            setError('Invalid credentials. Please try again.');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div style={styles.pageContainer}>
            <div style={styles.backgroundOverlay}></div>
            <div style={styles.container}>
                <div style={styles.card}>
                    <div style={styles.header}>
                        <div style={styles.iconContainer}>
                            <svg style={styles.icon} viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M12 2C6.48 2 2 6.48 2 12C2 17.52 6.48 22 12 22C17.52 22 22 17.52 22 12C22 6.48 17.52 2 12 2ZM12 5C13.66 5 15 6.34 15 8C15 9.66 13.66 11 12 11C10.34 11 9 9.66 9 8C9 6.34 10.34 5 12 5ZM12 19.2C9.5 19.2 7.29 17.92 6 15.98C6.03 13.99 10 12.9 12 12.9C13.99 12.9 17.97 13.99 18 15.98C16.71 17.92 14.5 19.2 12 19.2Z" fill="currentColor"/>
                            </svg>
                        </div>
                        <h1 style={styles.title}>Welcome to AnniMemo</h1>
                        <p style={styles.subtitle}>Organize and monitor your pet's health</p>
                    </div>
                    
                    <form onSubmit={handleLogin} style={styles.form}>
                        <div style={styles.inputGroup}>
                            <label style={styles.label}>Email or Username</label>
                            <input
                                type="text"
                                name="identifier"
                                placeholder="Enter your email or username"
                                value={credentials.identifier}
                                onChange={handleChange}
                                required
                                style={styles.input}
                            />
                        </div>
                        
                        <div style={styles.inputGroup}>
                            <label style={styles.label}>Password</label>
                            <input
                                type="password"
                                name="password"
                                placeholder="Enter your password"
                                value={credentials.password}
                                onChange={handleChange}
                                required
                                style={styles.input}
                            />
                        </div>
                        
                        {error && (
                            <div style={styles.errorContainer}>
                                <span style={styles.errorIcon}>⚠️</span>
                                <p style={styles.error}>{error}</p>
                            </div>
                        )}
                        
                        <button 
                            type="submit" 
                            disabled={isLoading}
                            style={{
                                ...styles.button,
                                opacity: isLoading ? 0.7 : 1,
                                cursor: isLoading ? 'not-allowed' : 'pointer'
                            }}
                        >
                            {isLoading ? 'Logging in...' : 'Login'}
                        </button>
                    </form>
                    
                    {/* OAuth Login Section */}
                    {/* FRS Feature 4.2: Google OAuth Login */}
                    <div style={styles.divider}>
                        <span style={styles.dividerText}>or continue with</span>
                    </div>
                    
                    <a 
                        href="http://localhost:8080/oauth2/authorization/google"
                        style={styles.googleButton}
                    >
                        <svg style={styles.googleIcon} viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                            <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
                            <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
                            <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"/>
                            <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"/>
                        </svg>
                        Sign in with Google
                    </a>
                    
                    <div style={styles.footer}>
                        <p style={styles.footerText}>
                            Don't have an account? {' '}
                            <a href="/register" style={styles.link}>Register here</a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
};

// Modern styling for the UI
const styles = {
    pageContainer: {
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        background: 'var(--app-bg)',
        fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif',
        position: 'relative',
        overflow: 'hidden'
    },
    backgroundOverlay: {
        position: 'absolute',
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
        background: 'radial-gradient(circle at 20% 50%, rgba(255, 255, 255, 0.1) 0%, transparent 50%), radial-gradient(circle at 80% 80%, rgba(255, 255, 255, 0.1) 0%, transparent 50%)',
        pointerEvents: 'none'
    },
    container: {
        width: '100%',
        maxWidth: '450px',
        padding: '20px',
        position: 'relative',
        zIndex: 1
    },
    card: {
        backgroundColor: 'var(--card-bg)',
        borderRadius: '24px',
        padding: '48px 40px',
        boxShadow: 'var(--shadow-strong)',
        backdropFilter: 'blur(10px)',
        animation: 'slideIn 0.5s ease-out'
    },
    header: {
        textAlign: 'center',
        marginBottom: '32px'
    },
    iconContainer: {
        width: '80px',
        height: '80px',
        margin: '0 auto 20px',
        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
        borderRadius: '20px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        boxShadow: '0 10px 30px rgba(102, 126, 234, 0.4)'
    },
    icon: {
        width: '40px',
        height: '40px',
        color: 'white'
    },
    title: {
        fontSize: '28px',
        fontWeight: '700',
        color: 'var(--text-primary)',
        margin: '0 0 8px 0',
        letterSpacing: '-0.5px'
    },
    subtitle: {
        fontSize: '15px',
        color: 'var(--text-muted)',
        margin: 0,
        fontWeight: '400'
    },
    form: {
        display: 'flex',
        flexDirection: 'column',
        gap: '20px'
    },
    inputGroup: {
        display: 'flex',
        flexDirection: 'column',
        gap: '8px'
    },
    label: {
        fontSize: '14px',
        fontWeight: '600',
        color: 'var(--text-secondary)',
        marginBottom: '4px'
    },
    input: {
        padding: '14px 16px',
        fontSize: '15px',
        borderRadius: '12px',
        border: '2px solid var(--card-border)',
        backgroundColor: 'var(--surface)',
        transition: 'all 0.3s ease',
        outline: 'none',
        fontFamily: 'inherit',
        color: 'var(--text-primary)'
    },
    button: {
        padding: '16px',
        fontSize: '16px',
        fontWeight: '600',
        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
        color: 'white',
        border: 'none',
        borderRadius: '12px',
        cursor: 'pointer',
        marginTop: '8px',
        transition: 'all 0.3s ease',
        boxShadow: '0 4px 15px rgba(102, 126, 234, 0.4)',
        fontFamily: 'inherit'
    },
    errorContainer: {
        display: 'flex',
        alignItems: 'center',
        gap: '10px',
        backgroundColor: '#fff5f5',
        padding: '12px 16px',
        borderRadius: '10px',
        border: '1px solid #feb2b2'
    },
    errorIcon: {
        fontSize: '18px'
    },
    error: {
        color: '#c53030',
        fontSize: '14px',
        margin: 0,
        fontWeight: '500'
    },
    divider: {
        margin: '24px 0',
        textAlign: 'center',
        position: 'relative',
        height: '1px',
        backgroundColor: 'var(--card-border)'
    },
    dividerText: {
        position: 'absolute',
        top: '50%',
        left: '50%',
        transform: 'translate(-50%, -50%)',
        backgroundColor: 'var(--card-bg)',
        padding: '0 16px',
        fontSize: '13px',
        color: 'var(--text-muted)',
        fontWeight: '500'
    },
    googleButton: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        gap: '12px',
        padding: '14px 16px',
        fontSize: '15px',
        fontWeight: '600',
        backgroundColor: 'white',
        color: '#5f6368',
        border: '2px solid var(--card-border)',
        borderRadius: '12px',
        cursor: 'pointer',
        transition: 'all 0.3s ease',
        textDecoration: 'none',
        fontFamily: 'inherit'
    },
    googleIcon: {
        width: '20px',
        height: '20px'
    },
    footer: {
        marginTop: '28px',
        textAlign: 'center'
    },
    footerText: {
        fontSize: '15px',
        color: 'var(--text-muted)',
        margin: 0
    },
    link: {
        color: '#667eea',
        textDecoration: 'none',
        fontWeight: '600',
        transition: 'color 0.2s ease'
    }
};

// Add CSS for hover effects and animations
const styleSheet = document.createElement('style');
styleSheet.textContent = `
    @keyframes slideIn {
        from {
            opacity: 0;
            transform: translateY(20px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }
    
    input:focus {
        border-color: #667eea !important;
        background-color: white !important;
        box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1) !important;
    }
    
    button:hover:not(:disabled) {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5) !important;
    }
    
    button:active:not(:disabled) {
        transform: translateY(0);
    }
    
    a:hover {
        color: #5a67d8 !important;
        text-decoration: underline !important;
    }
`;
document.head.appendChild(styleSheet);

export default LoginPage;