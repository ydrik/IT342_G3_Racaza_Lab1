import axios from 'axios';

const API_URL = "http://localhost:8080/api/admin/";

/**
 * Admin Service
 * FRS Feature 2: Role-Based Access Control
 * Admin operations for user management
 */
class AdminService {
    /**
     * Get all users (Admin only)
     */
    getAllUsers() {
        const token = localStorage.getItem('token');
        return axios.get(API_URL + 'users', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
    }

    /**
     * Update user role (Admin only)
     */
    updateUserRole(userId, role) {
        const token = localStorage.getItem('token');
        return axios.put(API_URL + `users/${userId}/role?role=${role}`, {}, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
    }

    /**
     * Delete user (Admin only)
     */
    deleteUser(userId) {
        const token = localStorage.getItem('token');
        return axios.delete(API_URL + `users/${userId}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
    }

    /**
     * Get system statistics (Admin only)
     */
    getSystemStats() {
        const token = localStorage.getItem('token');
        return axios.get(API_URL + 'stats', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
    }
}

const adminServiceInstance = new AdminService();
export default adminServiceInstance;
