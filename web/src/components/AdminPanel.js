import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import adminService from '../services/admin.service';
import authService from '../services/auth.service';

/**
 * Admin Panel Component
 * FRS Feature 2: Role-Based Access Control
 * UI-level role restriction - Admin only
 * Allows admin to:
 * - View all users
 * - Update user roles
 * - Delete users
 * - View system statistics
 */
const AdminPanel = () => {
    const [users, setUsers] = useState([]);
    const [stats, setStats] = useState({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [currentUser, setCurrentUser] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        checkAdminAccess();
        loadUsers();
        loadStats();
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const checkAdminAccess = async () => {
        try {
            const response = await authService.getCurrentUser();
            setCurrentUser(response.data);
            
            // UI-level role restriction
            if (response.data.role !== 'ADMIN') {
                setError('Access denied. Admin privileges required.');
                setTimeout(() => navigate('/dashboard'), 2000);
            }
        } catch (err) {
            setError('Authentication failed. Please login again.');
            navigate('/login');
        }
    };

    const loadUsers = async () => {
        try {
            const response = await adminService.getAllUsers();
            setUsers(response.data);
            setLoading(false);
        } catch (err) {
            setError('Failed to load users. ' + (err.response?.data?.message || err.message));
            setLoading(false);
        }
    };

    const loadStats = async () => {
        try {
            const response = await adminService.getSystemStats();
            setStats(response.data);
        } catch (err) {
            console.error('Failed to load stats:', err);
        }
    };

    const handleRoleChange = async (userId, newRole) => {
        try {
            await adminService.updateUserRole(userId, newRole);
            setSuccessMessage('Role updated successfully!');
            loadUsers(); // Refresh user list
            setTimeout(() => setSuccessMessage(''), 3000);
        } catch (err) {
            setError('Failed to update role. ' + (err.response?.data?.message || err.message));
            setTimeout(() => setError(''), 3000);
        }
    };

    const handleDeleteUser = async (userId) => {
        if (!window.confirm('Are you sure you want to delete this user? This action cannot be undone.')) {
            return;
        }

        try {
            await adminService.deleteUser(userId);
            setSuccessMessage('User deleted successfully!');
            loadUsers(); // Refresh user list
            loadStats(); // Refresh stats
            setTimeout(() => setSuccessMessage(''), 3000);
        } catch (err) {
            setError('Failed to delete user. ' + (err.response?.data?.message || err.message));
            setTimeout(() => setError(''), 3000);
        }
    };

    if (loading) {
        return (
            <div style={styles.pageContainer}>
                <div style={styles.loadingContainer}>
                    <div style={styles.spinner}></div>
                    <p style={styles.loadingText}>Loading admin panel...</p>
                </div>
            </div>
        );
    }

    return (
        <div style={styles.pageContainer}>
            <div style={styles.container}>
                {/* Header */}
                <div style={styles.header}>
                    <div>
                        <h1 style={styles.title}>Admin Panel</h1>
                        <p style={styles.subtitle}>User Management & System Statistics</p>
                    </div>
                    <button onClick={() => navigate('/dashboard')} style={styles.backButton}>
                        ← Back to Dashboard
                    </button>
                </div>

                {/* Messages */}
                {error && (
                    <div style={{...styles.message, ...styles.errorMessage}}>
                        ⚠️ {error}
                    </div>
                )}
                
                {successMessage && (
                    <div style={{...styles.message, ...styles.successMessage}}>
                        ✓ {successMessage}
                    </div>
                )}

                {/* Statistics Cards */}
                <div style={styles.statsGrid}>
                    <div style={styles.statCard}>
                        <div style={styles.statIcon}>👥</div>
                        <div>
                            <div style={styles.statValue}>{stats.totalUsers || 0}</div>
                            <div style={styles.statLabel}>Total Users</div>
                        </div>
                    </div>
                    
                    <div style={styles.statCard}>
                        <div style={styles.statIcon}>🐾</div>
                        <div>
                            <div style={styles.statValue}>{stats.totalPets || 0}</div>
                            <div style={styles.statLabel}>Total Pets</div>
                        </div>
                    </div>
                    
                    <div style={styles.statCard}>
                        <div style={styles.statIcon}>❤️</div>
                        <div>
                            <div style={styles.statValue}>{stats.totalHealthMetrics || 0}</div>
                            <div style={styles.statLabel}>Health Records</div>
                        </div>
                    </div>
                    
                    <div style={styles.statCard}>
                        <div style={styles.statIcon}>👨‍💼</div>
                        <div>
                            <div style={styles.statValue}>{stats.totalAdmins || 0}</div>
                            <div style={styles.statLabel}>Admins</div>
                        </div>
                    </div>
                </div>

                {/* Users Table */}
                <div style={styles.tableContainer}>
                    <h2 style={styles.tableTitle}>User Management</h2>
                    
                    <div style={styles.tableWrapper}>
                        <table style={styles.table}>
                            <thead>
                                <tr style={styles.tableHeaderRow}>
                                    <th style={styles.tableHeader}>ID</th>
                                    <th style={styles.tableHeader}>Username</th>
                                    <th style={styles.tableHeader}>Name</th>
                                    <th style={styles.tableHeader}>Email</th>
                                    <th style={styles.tableHeader}>Role</th>
                                    <th style={styles.tableHeader}>Status</th>
                                    <th style={styles.tableHeader}>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {users.map((user) => (
                                    <tr key={user.id} style={styles.tableRow}>
                                        <td style={styles.tableCell}>{user.id}</td>
                                        <td style={styles.tableCell}>
                                            <div style={styles.userCell}>
                                                <div style={styles.userAvatar}>
                                                    {user.username.charAt(0).toUpperCase()}
                                                </div>
                                                <span>{user.username}</span>
                                            </div>
                                        </td>
                                        <td style={styles.tableCell}>
                                            {user.firstName} {user.lastName}
                                        </td>
                                        <td style={styles.tableCell}>{user.email}</td>
                                        <td style={styles.tableCell}>
                                            <select
                                                value={user.role}
                                                onChange={(e) => handleRoleChange(user.id, e.target.value)}
                                                style={{
                                                    ...styles.roleSelect,
                                                    backgroundColor: user.role === 'ADMIN' ? '#fee2e2' : '#e0f2fe',
                                                    color: user.role === 'ADMIN' ? '#991b1b' : '#075985'
                                                }}
                                                disabled={currentUser && currentUser.id === user.id}
                                            >
                                                <option value="USER">USER</option>
                                                <option value="ADMIN">ADMIN</option>
                                            </select>
                                        </td>
                                        <td style={styles.tableCell}>
                                            <span style={{
                                                ...styles.badge,
                                                backgroundColor: user.active ? '#d1fae5' : '#fee2e2',
                                                color: user.active ? '#065f46' : '#991b1b'
                                            }}>
                                                {user.active ? 'Active' : 'Inactive'}
                                            </span>
                                        </td>
                                        <td style={styles.tableCell}>
                                            <button
                                                onClick={() => handleDeleteUser(user.id)}
                                                style={styles.deleteButton}
                                                disabled={currentUser && currentUser.id === user.id}
                                                title={currentUser && currentUser.id === user.id ? 
                                                    "Cannot delete yourself" : "Delete user"}
                                            >
                                                🗑️ Delete
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>

                    {users.length === 0 && (
                        <div style={styles.emptyState}>
                            <p>No users found.</p>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

const styles = {
    pageContainer: {
        minHeight: '100vh',
        backgroundColor: 'var(--app-bg)',
        padding: '20px'
    },
    container: {
        maxWidth: '1400px',
        margin: '0 auto'
    },
    header: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: '30px',
        flexWrap: 'wrap',
        gap: '15px'
    },
    title: {
        fontSize: '32px',
        fontWeight: '700',
        color: 'var(--text-primary)',
        margin: '0'
    },
    subtitle: {
        fontSize: '16px',
        color: 'var(--text-secondary)',
        marginTop: '5px'
    },
    backButton: {
        padding: '12px 24px',
        backgroundColor: 'var(--primary)',
        color: 'white',
        border: 'none',
        borderRadius: '8px',
        cursor: 'pointer',
        fontSize: '14px',
        fontWeight: '500',
        transition: 'all 0.2s ease',
        boxShadow: '0 2px 8px rgba(0, 123, 255, 0.3)'
    },
    message: {
        padding: '16px',
        borderRadius: '8px',
        marginBottom: '20px',
        fontSize: '14px',
        fontWeight: '500'
    },
    errorMessage: {
        backgroundColor: '#fee2e2',
        color: '#991b1b',
        border: '1px solid #fecaca'
    },
    successMessage: {
        backgroundColor: '#d1fae5',
        color: '#065f46',
        border: '1px solid #a7f3d0'
    },
    statsGrid: {
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))',
        gap: '20px',
        marginBottom: '30px'
    },
    statCard: {
        backgroundColor: 'var(--card-bg)',
        padding: '24px',
        borderRadius: '12px',
        boxShadow: 'var(--shadow-md)',
        display: 'flex',
        alignItems: 'center',
        gap: '16px',
        transition: 'transform 0.2s ease',
        cursor: 'pointer'
    },
    statIcon: {
        fontSize: '36px',
        width: '60px',
        height: '60px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: 'var(--light-bg)',
        borderRadius: '12px'
    },
    statValue: {
        fontSize: '28px',
        fontWeight: '700',
        color: 'var(--text-primary)'
    },
    statLabel: {
        fontSize: '14px',
        color: 'var(--text-secondary)',
        marginTop: '4px'
    },
    tableContainer: {
        backgroundColor: 'var(--card-bg)',
        borderRadius: '12px',
        boxShadow: 'var(--shadow-md)',
        overflow: 'hidden'
    },
    tableTitle: {
        fontSize: '20px',
        fontWeight: '600',
        color: 'var(--text-primary)',
        padding: '24px',
        borderBottom: '1px solid var(--border-color)',
        margin: '0'
    },
    tableWrapper: {
        overflowX: 'auto'
    },
    table: {
        width: '100%',
        borderCollapse: 'collapse'
    },
    tableHeaderRow: {
        backgroundColor: 'var(--light-bg)'
    },
    tableHeader: {
        padding: '16px',
        textAlign: 'left',
        fontSize: '14px',
        fontWeight: '600',
        color: 'var(--text-secondary)',
        textTransform: 'uppercase',
        letterSpacing: '0.5px'
    },
    tableRow: {
        borderBottom: '1px solid var(--border-color)',
        transition: 'background-color 0.2s ease'
    },
    tableCell: {
        padding: '16px',
        fontSize: '14px',
        color: 'var(--text-primary)'
    },
    userCell: {
        display: 'flex',
        alignItems: 'center',
        gap: '12px'
    },
    userAvatar: {
        width: '36px',
        height: '36px',
        borderRadius: '50%',
        backgroundColor: 'var(--primary)',
        color: 'white',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        fontWeight: '600',
        fontSize: '14px'
    },
    roleSelect: {
        padding: '6px 12px',
        borderRadius: '6px',
        border: 'none',
        fontSize: '13px',
        fontWeight: '500',
        cursor: 'pointer',
        transition: 'all 0.2s ease'
    },
    badge: {
        padding: '4px 12px',
        borderRadius: '12px',
        fontSize: '12px',
        fontWeight: '500',
        display: 'inline-block'
    },
    deleteButton: {
        padding: '8px 16px',
        backgroundColor: '#fee2e2',
        color: '#991b1b',
        border: 'none',
        borderRadius: '6px',
        cursor: 'pointer',
        fontSize: '13px',
        fontWeight: '500',
        transition: 'all 0.2s ease'
    },
    emptyState: {
        padding: '40px',
        textAlign: 'center',
        color: 'var(--text-secondary)',
        fontSize: '14px'
    },
    loadingContainer: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        minHeight: '100vh',
        gap: '16px'
    },
    spinner: {
        width: '48px',
        height: '48px',
        border: '4px solid var(--border-color)',
        borderTop: '4px solid var(--primary)',
        borderRadius: '50%',
        animation: 'spin 1s linear infinite'
    },
    loadingText: {
        color: 'var(--text-secondary)',
        fontSize: '16px'
    }
};

export default AdminPanel;
