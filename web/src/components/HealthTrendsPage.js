import React, { useEffect, useMemo, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const API_BASE = 'http://localhost:8080';

const HealthTrendsPage = () => {
    const navigate = useNavigate();
    const [pets, setPets] = useState([]);
    const [recordsByPet, setRecordsByPet] = useState({});
    const [selectedPetId, setSelectedPetId] = useState('all');
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState('');

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
        setError('');
        try {
            const token = localStorage.getItem('token');
            const headers = { Authorization: `Bearer ${token}` };

            const petsResponse = await axios.get(`${API_BASE}/api/pets`, { headers });
            const petList = petsResponse.data || [];
            setPets(petList);

            const healthCalls = petList.map((pet) =>
                axios
                    .get(`${API_BASE}/api/pets/${pet.id}/health`, { headers })
                    .then((response) => ({ petId: String(pet.id), records: response.data || [] }))
                    .catch(() => ({ petId: String(pet.id), records: [] }))
            );

            const allHealthResults = await Promise.all(healthCalls);
            const grouped = allHealthResults.reduce((acc, item) => {
                acc[item.petId] = item.records;
                return acc;
            }, {});
            setRecordsByPet(grouped);

            if (petList.length > 0) {
                setSelectedPetId((prev) => (prev === 'all' ? 'all' : prev || String(petList[0].id)));
            }
        } catch (loadErr) {
            setError('Failed to load health trends. Please check backend connection.');
        } finally {
            setIsLoading(false);
        }
    };

    const normalizeType = (type) => {
        if (!type) return 'other';
        const lowered = type.toLowerCase();
        if (lowered === 'vet_visit' || lowered === 'vetvisit') {
            return 'vetVisit';
        }
        return lowered;
    };

    const parseWeight = (record) => {
        if (record.weight !== null && record.weight !== undefined && !Number.isNaN(Number(record.weight))) {
            return Number(record.weight);
        }

        if (record.value && typeof record.value === 'string') {
            const parsed = parseFloat(record.value.replace(/[^0-9.]/g, ''));
            if (!Number.isNaN(parsed)) {
                return parsed;
            }
        }

        return null;
    };

    const selectedPetRecords = useMemo(() => {
        if (selectedPetId === 'all') {
            return Object.values(recordsByPet).flat();
        }
        return recordsByPet[selectedPetId] || [];
    }, [recordsByPet, selectedPetId]);

    const typeSummary = useMemo(() => {
        const limitDate = new Date();
        limitDate.setDate(limitDate.getDate() - 30);

        const summary = {
            weight: 0,
            medication: 0,
            vaccination: 0,
            vetVisit: 0,
            other: 0
        };

        selectedPetRecords.forEach((record) => {
            const date = new Date(record.date || record.createdAt || 0);
            if (Number.isNaN(date.getTime()) || date < limitDate) {
                return;
            }
            const type = normalizeType(record.type);
            summary[type] = (summary[type] || 0) + 1;
        });

        return summary;
    }, [selectedPetRecords]);

    const weightPoints = useMemo(() => {
        return selectedPetRecords
            .filter((record) => normalizeType(record.type) === 'weight')
            .map((record) => {
                const date = new Date(record.date || record.createdAt || 0);
                return {
                    date,
                    label: Number.isNaN(date.getTime())
                        ? 'Unknown'
                        : date.toLocaleDateString('en-US', { month: 'short', day: 'numeric' }),
                    value: parseWeight(record)
                };
            })
            .filter((point) => point.value !== null && !Number.isNaN(point.value))
            .sort((a, b) => a.date.getTime() - b.date.getTime())
            .slice(-8);
    }, [selectedPetRecords]);

    const maxWeight = weightPoints.length ? Math.max(...weightPoints.map((point) => point.value)) : 1;

    const totalLast30Days =
        typeSummary.weight + typeSummary.medication + typeSummary.vaccination + typeSummary.vetVisit + typeSummary.other;

    if (isLoading) {
        return <div style={styles.loading}>Loading health trends...</div>;
    }

    return (
        <div style={styles.page}>
            <div style={styles.container}>
                <button onClick={() => navigate('/dashboard')} style={styles.backButton}>← Back to Dashboard</button>
                <div style={styles.headerRow}>
                    <div>
                        <h1 style={styles.title}>Health Trends</h1>
                        <p style={styles.subtitle}>Track changes over time and see where care activity is concentrated.</p>
                    </div>
                    <select
                        value={selectedPetId}
                        onChange={(event) => setSelectedPetId(event.target.value)}
                        style={styles.select}
                    >
                        <option value="all">All Pets</option>
                        {pets.map((pet) => (
                            <option key={pet.id} value={String(pet.id)}>{pet.name}</option>
                        ))}
                    </select>
                </div>

                {error && <div style={styles.error}>{error}</div>}

                <div style={styles.grid}>
                    <section style={styles.card}>
                        <h3 style={styles.cardTitle}>Weight Trend (Latest 8 Entries)</h3>
                        {weightPoints.length === 0 ? (
                            <p style={styles.muted}>No weight records yet.</p>
                        ) : (
                            <div style={styles.weightGrid}>
                                {weightPoints.map((point, index) => {
                                    const barHeight = Math.max(12, Math.round((point.value / maxWeight) * 120));
                                    return (
                                        <div key={`${point.label}-${index}`} style={styles.weightColumn}>
                                            <div style={styles.weightValue}>{point.value} kg</div>
                                            <div style={{ ...styles.weightBar, height: `${barHeight}px` }} />
                                            <div style={styles.weightLabel}>{point.label}</div>
                                        </div>
                                    );
                                })}
                            </div>
                        )}
                    </section>

                    <section style={styles.card}>
                        <h3 style={styles.cardTitle}>Care Activity (Last 30 Days)</h3>
                        <p style={styles.summaryText}>Total logged metrics: {totalLast30Days}</p>
                        <div style={styles.summaryList}>
                            <div style={styles.summaryItem}><span>📊 Weight</span><strong>{typeSummary.weight}</strong></div>
                            <div style={styles.summaryItem}><span>💊 Medication</span><strong>{typeSummary.medication}</strong></div>
                            <div style={styles.summaryItem}><span>💉 Vaccination</span><strong>{typeSummary.vaccination}</strong></div>
                            <div style={styles.summaryItem}><span>🏥 Vet Visits</span><strong>{typeSummary.vetVisit}</strong></div>
                            <div style={styles.summaryItem}><span>📝 Other</span><strong>{typeSummary.other}</strong></div>
                        </div>
                    </section>
                </div>
            </div>
        </div>
    );
};

const styles = {
    page: {
        minHeight: '100vh',
        background: 'var(--app-bg)',
        padding: '30px 20px 90px'
    },
    container: {
        maxWidth: '1200px',
        margin: '0 auto'
    },
    loading: {
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center'
    },
    backButton: {
        border: 'none',
        background: 'transparent',
        color: 'var(--text-primary)',
        cursor: 'pointer',
        marginBottom: '12px'
    },
    headerRow: {
        display: 'flex',
        flexWrap: 'wrap',
        justifyContent: 'space-between',
        alignItems: 'flex-end',
        gap: '12px',
        marginBottom: '18px'
    },
    title: {
        margin: 0,
        color: 'var(--text-primary)'
    },
    subtitle: {
        marginTop: '6px',
        marginBottom: 0,
        color: 'var(--text-muted)'
    },
    select: {
        borderRadius: '10px',
        border: '1px solid var(--card-border)',
        padding: '10px 12px',
        background: 'var(--card-bg)',
        color: 'var(--text-primary)',
        minWidth: '180px'
    },
    error: {
        marginBottom: '12px',
        padding: '10px 12px',
        borderRadius: '10px',
        background: '#fee2e2',
        color: '#991b1b'
    },
    grid: {
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))',
        gap: '16px'
    },
    card: {
        background: 'var(--card-bg)',
        border: '1px solid var(--card-border)',
        borderRadius: '14px',
        padding: '16px'
    },
    cardTitle: {
        marginTop: 0,
        marginBottom: '10px',
        color: 'var(--text-primary)'
    },
    muted: {
        color: 'var(--text-muted)',
        marginBottom: 0
    },
    weightGrid: {
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(60px, 1fr))',
        gap: '8px',
        alignItems: 'end',
        minHeight: '180px'
    },
    weightColumn: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'flex-end',
        gap: '6px'
    },
    weightValue: {
        fontSize: '11px',
        color: 'var(--text-muted)'
    },
    weightBar: {
        width: '100%',
        maxWidth: '36px',
        borderRadius: '8px 8px 4px 4px',
        background: 'linear-gradient(180deg, #22d3ee 0%, #0ea5e9 100%)'
    },
    weightLabel: {
        fontSize: '11px',
        color: 'var(--text-primary)',
        textAlign: 'center'
    },
    summaryText: {
        marginTop: 0,
        marginBottom: '12px',
        color: 'var(--text-muted)'
    },
    summaryList: {
        display: 'grid',
        gap: '8px'
    },
    summaryItem: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        border: '1px solid var(--card-border)',
        borderRadius: '10px',
        padding: '10px 12px',
        color: 'var(--text-primary)'
    }
};

export default HealthTrendsPage;
