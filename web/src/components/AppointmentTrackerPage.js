import React, { useEffect, useMemo, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const API_BASE = 'http://localhost:8080';

const AppointmentTrackerPage = () => {
    const navigate = useNavigate();
    const [pets, setPets] = useState([]);
    const [appointments, setAppointments] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [message, setMessage] = useState('');
    const [messageType, setMessageType] = useState('');

    const [form, setForm] = useState({
        petId: '',
        title: '',
        dueDate: new Date().toISOString().split('T')[0],
        notes: ''
    });

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (!token) {
            navigate('/login');
            return;
        }
        fetchData();
    }, [navigate]);

    const fetchData = async () => {
        setIsLoading(true);
        setMessage('');
        try {
            const token = localStorage.getItem('token');
            const headers = { Authorization: `Bearer ${token}` };

            const [petsResponse, reminderResponse] = await Promise.all([
                axios.get(`${API_BASE}/api/pets`, { headers }),
                axios.get(`${API_BASE}/api/reminders`, { headers })
            ]);

            const petList = petsResponse.data || [];
            const reminderList = reminderResponse.data || [];
            const appointmentList = reminderList
                .filter((item) => (item.type || '').toLowerCase() === 'appointment')
                .sort((a, b) => new Date(a.dueDate).getTime() - new Date(b.dueDate).getTime());

            setPets(petList);
            setAppointments(appointmentList);

            if (petList.length > 0) {
                setForm((prev) => ({ ...prev, petId: prev.petId || String(petList[0].id) }));
            }
        } catch (error) {
            setMessage('Failed to load appointments.');
            setMessageType('error');
        } finally {
            setIsLoading(false);
        }
    };

    const handleChange = (event) => {
        const { name, value } = event.target;
        setForm((prev) => ({ ...prev, [name]: value }));
    };

    const createAppointment = async (event) => {
        event.preventDefault();
        setMessage('');

        if (!form.petId || !form.title.trim()) {
            setMessage('Pet and title are required.');
            setMessageType('error');
            return;
        }

        try {
            const token = localStorage.getItem('token');
            await axios.post(
                `${API_BASE}/api/reminders`,
                {
                    petId: Number(form.petId),
                    title: form.title.trim(),
                    type: 'appointment',
                    dueDate: form.dueDate,
                    notes: form.notes.trim()
                },
                { headers: { Authorization: `Bearer ${token}` } }
            );

            setMessage('Appointment added successfully.');
            setMessageType('success');
            setForm((prev) => ({ ...prev, title: '', notes: '' }));
            fetchData();
        } catch (error) {
            setMessage('Failed to create appointment.');
            setMessageType('error');
        }
    };

    const toggleComplete = async (appointment) => {
        try {
            const token = localStorage.getItem('token');
            await axios.put(
                `${API_BASE}/api/reminders/${appointment.id}/status?completed=${!appointment.completed}`,
                null,
                { headers: { Authorization: `Bearer ${token}` } }
            );
            fetchData();
        } catch (error) {
            setMessage('Failed to update appointment status.');
            setMessageType('error');
        }
    };

    const removeAppointment = async (appointmentId) => {
        try {
            const token = localStorage.getItem('token');
            await axios.delete(`${API_BASE}/api/reminders/${appointmentId}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            fetchData();
        } catch (error) {
            setMessage('Failed to delete appointment.');
            setMessageType('error');
        }
    };

    const categorized = useMemo(() => {
        const today = new Date();
        const dayStart = new Date(today.toDateString());

        const upcoming = [];
        const overdue = [];
        const completed = [];

        appointments.forEach((item) => {
            if (item.completed) {
                completed.push(item);
                return;
            }
            const due = new Date(item.dueDate);
            if (Number.isNaN(due.getTime())) {
                upcoming.push(item);
                return;
            }
            if (due < dayStart) {
                overdue.push(item);
            } else {
                upcoming.push(item);
            }
        });

        return { upcoming, overdue, completed };
    }, [appointments]);

    if (isLoading) {
        return <div style={styles.loading}>Loading appointments...</div>;
    }

    return (
        <div style={styles.page}>
            <div style={styles.container}>
                <button onClick={() => navigate('/dashboard')} style={styles.backButton}>← Back to Dashboard</button>
                <h1 style={styles.title}>Appointment Tracker</h1>
                <p style={styles.subtitle}>Schedule vet visits and keep follow-up care visible.</p>

                {message && (
                    <div style={{ ...styles.message, ...(messageType === 'success' ? styles.success : styles.error) }}>
                        {message}
                    </div>
                )}

                <section style={styles.card}>
                    <h3 style={styles.cardTitle}>New Appointment</h3>
                    <form onSubmit={createAppointment} style={styles.form}>
                        <select name="petId" value={form.petId} onChange={handleChange} style={styles.input}>
                            {pets.map((pet) => (
                                <option key={pet.id} value={pet.id}>{pet.name}</option>
                            ))}
                        </select>
                        <input name="title" value={form.title} onChange={handleChange} placeholder="Appointment title" style={styles.input} />
                        <input type="date" name="dueDate" value={form.dueDate} onChange={handleChange} style={styles.input} />
                        <textarea name="notes" value={form.notes} onChange={handleChange} placeholder="Visit notes or purpose" style={styles.textarea} />
                        <button type="submit" style={styles.primaryButton}>Save Appointment</button>
                    </form>
                </section>

                <div style={styles.grid}>
                    <section style={styles.card}>
                        <h3 style={styles.cardTitle}>Upcoming ({categorized.upcoming.length})</h3>
                        <AppointmentList
                            items={categorized.upcoming}
                            onToggle={toggleComplete}
                            onDelete={removeAppointment}
                            actionLabel="Mark Done"
                        />
                    </section>

                    <section style={styles.card}>
                        <h3 style={styles.cardTitle}>Overdue ({categorized.overdue.length})</h3>
                        <AppointmentList
                            items={categorized.overdue}
                            onToggle={toggleComplete}
                            onDelete={removeAppointment}
                            actionLabel="Mark Done"
                        />
                    </section>
                </div>

                <section style={styles.card}>
                    <h3 style={styles.cardTitle}>Completed ({categorized.completed.length})</h3>
                    <AppointmentList
                        items={categorized.completed}
                        onToggle={toggleComplete}
                        onDelete={removeAppointment}
                        actionLabel="Reopen"
                    />
                </section>
            </div>
        </div>
    );
};

const AppointmentList = ({ items, onToggle, onDelete, actionLabel }) => {
    if (!items.length) {
        return <p style={styles.muted}>No items in this section.</p>;
    }

    return (
        <ul style={styles.list}>
            {items.map((item) => (
                <li key={item.id} style={styles.listItem}>
                    <div>
                        <strong>{item.title}</strong>
                        <div style={styles.muted}>{item.petName} • due {item.dueDate}</div>
                    </div>
                    <div style={styles.rowActions}>
                        <button onClick={() => onToggle(item)} style={styles.completeButton}>{actionLabel}</button>
                        <button onClick={() => onDelete(item.id)} style={styles.deleteButton}>Delete</button>
                    </div>
                </li>
            ))}
        </ul>
    );
};

const styles = {
    page: { minHeight: '100vh', background: 'var(--app-bg)', padding: '30px 20px 90px' },
    container: { maxWidth: '1100px', margin: '0 auto' },
    loading: { minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center' },
    backButton: { border: 'none', background: 'transparent', color: 'var(--text-primary)', cursor: 'pointer', marginBottom: '12px' },
    title: { color: 'var(--text-primary)', marginTop: 0, marginBottom: '4px' },
    subtitle: { color: 'var(--text-muted)', marginTop: 0, marginBottom: '16px' },
    message: { padding: '10px 12px', borderRadius: '10px', marginBottom: '14px' },
    success: { background: '#d1fae5', color: '#065f46' },
    error: { background: '#fee2e2', color: '#991b1b' },
    grid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '16px', marginTop: '16px', marginBottom: '16px' },
    card: { background: 'var(--card-bg)', border: '1px solid var(--card-border)', borderRadius: '14px', padding: '16px' },
    cardTitle: { marginTop: 0, color: 'var(--text-primary)' },
    form: { display: 'grid', gap: '10px' },
    input: { borderRadius: '10px', border: '1px solid var(--card-border)', padding: '10px 12px', background: 'var(--app-bg)', color: 'var(--text-primary)' },
    textarea: { minHeight: '90px', borderRadius: '10px', border: '1px solid var(--card-border)', padding: '10px 12px', background: 'var(--app-bg)', color: 'var(--text-primary)' },
    primaryButton: { border: 'none', borderRadius: '10px', padding: '10px 12px', cursor: 'pointer', color: '#fff', background: 'linear-gradient(135deg, #0ea5e9, #0284c7)' },
    list: { listStyle: 'none', margin: 0, padding: 0, display: 'grid', gap: '10px' },
    listItem: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', gap: '10px', border: '1px solid var(--card-border)', borderRadius: '10px', padding: '10px 12px' },
    rowActions: { display: 'flex', gap: '8px' },
    completeButton: { border: 'none', borderRadius: '10px', padding: '8px 10px', cursor: 'pointer', color: '#fff', background: 'linear-gradient(135deg, #10b981, #059669)' },
    deleteButton: { border: 'none', borderRadius: '10px', padding: '8px 10px', cursor: 'pointer', color: '#fff', background: 'linear-gradient(135deg, #ef4444, #dc2626)' },
    muted: { color: 'var(--text-muted)', fontSize: '13px' }
};

export default AppointmentTrackerPage;
