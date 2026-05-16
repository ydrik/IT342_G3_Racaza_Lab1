import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

// Import your components
import LoginPage from './components/LoginPage'; 
import RegisterPage from './components/RegisterPage';
import LandingPage from './components/LandingPage';
import Dashboard from './components/Dashboard';
import UserProfile from './components/UserProfile';
import PetList from './components/PetList';
import AddPet from './components/AddPet';
import EditPet from './components/EditPet';
import HealthMetrics from './components/HealthMetrics';
import Footer from './components/Footer';
import ThemeToggle from './components/ThemeToggle';
import PrivacyPage from './components/PrivacyPage';
import TermsPage from './components/TermsPage';
import CookiesPage from './components/CookiesPage';
import SupportPage from './components/SupportPage';
import AdminPanel from './components/AdminPanel';
import Breeds from './components/Breeds';
import OAuth2Callback from './components/OAuth2Callback';
import RemindersPage from './components/RemindersPage';
import PetFactsPage from './components/PetFactsPage';
import SettingsPage from './components/SettingsPage';
import HealthTrendsPage from './components/HealthTrendsPage';
import AppointmentTrackerPage from './components/AppointmentTrackerPage';

function App() {
  const [theme, setTheme] = useState(() => {
    const stored = localStorage.getItem('theme');
    if (stored) {
      return stored;
    }
    return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
  });

  useEffect(() => {
    document.body.setAttribute('data-theme', theme);
    localStorage.setItem('theme', theme);
  }, [theme]);

  const toggleTheme = () => {
    setTheme((current) => (current === 'dark' ? 'light' : 'dark'));
  };

  return (
    <Router>
      <Routes>
        {/* Public Routes */}
        <Route path="/" element={<LandingPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        
        {/* OAuth2 Callback - FRS Feature 4.2: Google OAuth Login */}
        <Route path="/oauth2/callback" element={<OAuth2Callback />} />
        
        {/* Footer Link Routes */}
        <Route path="/privacy" element={<PrivacyPage />} />
        <Route path="/terms" element={<TermsPage />} />
        <Route path="/cookies" element={<CookiesPage />} />
        <Route path="/support" element={<SupportPage />} />
        
        {/* Protected Routes - FRS Feature 1: User Authentication */}
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/profile" element={<UserProfile />} />
        
        {/* Protected Routes - FRS Feature 2: Pet Health Tracking */}
        <Route path="/pets" element={<PetList />} />
        <Route path="/pets/add" element={<AddPet />} />
        <Route path="/pets/edit/:id" element={<EditPet />} />
        <Route path="/pets/:id/health" element={<HealthMetrics />} />
        
        {/* Protected Routes - FRS Feature 2: Role-Based Access Control */}
        <Route path="/admin" element={<AdminPanel />} />
        
        {/* Protected Routes - FRS Feature 4.1: External API Integration */}
        <Route path="/breeds" element={<Breeds />} />

        {/* Protected Routes - Additional Feature: Reminders & Notifications */}
        <Route path="/reminders" element={<RemindersPage />} />

        {/* Protected Routes - Additional Feature: Pet Facts */}
        <Route path="/facts" element={<PetFactsPage />} />

        {/* Protected Routes - Additional Feature: Settings */}
        <Route path="/settings" element={<SettingsPage />} />

        {/* Protected Routes - Additional Feature: Health Trends */}
        <Route path="/health-trends" element={<HealthTrendsPage />} />

        {/* Protected Routes - Additional Feature: Appointment Tracker */}
        <Route path="/appointments" element={<AppointmentTrackerPage />} />
      </Routes>
      <Footer />
      <ThemeToggle theme={theme} onToggle={toggleTheme} />
    </Router>
  );
}

export default App;