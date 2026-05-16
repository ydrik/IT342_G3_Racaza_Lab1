import React, { useState, useEffect, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

/**
 * Breeds Component
 * FRS Feature 4.1: External API Integration
 * Displays dog and cat breeds from external API
 */
const Breeds = () => {
    const [activeTab, setActiveTab] = useState('dogs');
    const [dogBreeds, setDogBreeds] = useState([]);
    const [catBreeds, setCatBreeds] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [searchTerm, setSearchTerm] = useState('');
    const [isSearchFocused, setIsSearchFocused] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        loadBreeds();
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const loadBreeds = async () => {
        try {
            const token = localStorage.getItem('token');
            if (!token) {
                navigate('/login');
                return;
            }

            const config = {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            };

            // Load both dog and cat breeds
            const [dogsResponse, catsResponse] = await Promise.all([
                axios.get('http://localhost:8080/api/breeds/dogs', config),
                axios.get('http://localhost:8080/api/breeds/cats', config)
            ]);

            setDogBreeds(dogsResponse.data);
            setCatBreeds(catsResponse.data);
            setLoading(false);
        } catch (err) {
            setError('Failed to load breeds. ' + (err.response?.data?.message || err.message));
            setLoading(false);
            if (err.response?.status === 401) {
                navigate('/login');
            }
        }
    };

    const filteredBreeds = () => {
        const breeds = activeTab === 'dogs' ? dogBreeds : catBreeds;
        const term = searchTerm.trim().toLowerCase();
        if (!term) return breeds;

        const tokens = term.split(/\s+/).filter(Boolean);
        return breeds.filter((breed) => {
            const name = (breed.name || '').toLowerCase();
            return tokens.every((token) => name.includes(token));
        });
    };

    const breedSuggestions = useMemo(() => {
        const term = searchTerm.trim().toLowerCase();
        if (!term) {
            return [];
        }

        const breeds = activeTab === 'dogs' ? dogBreeds : catBreeds;
        return breeds
            .map((breed) => (breed.name || '').trim())
            .filter(Boolean)
            .filter((name) => name.toLowerCase().includes(term))
            .sort((a, b) => {
                const aStarts = a.toLowerCase().startsWith(term) ? 0 : 1;
                const bStarts = b.toLowerCase().startsWith(term) ? 0 : 1;
                return aStarts - bStarts || a.localeCompare(b);
            })
            .filter((name, index, arr) => arr.findIndex((entry) => entry.toLowerCase() === name.toLowerCase()) === index)
            .slice(0, 8);
    }, [activeTab, catBreeds, dogBreeds, searchTerm]);

    if (loading) {
        return (
            <div style={styles.pageContainer}>
                <div style={styles.loadingContainer}>
                    <div style={styles.spinner}></div>
                    <p style={styles.loadingText}>Loading breeds...</p>
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
                        <h1 style={styles.title}>Pet Breeds</h1>
                        <p style={styles.subtitle}>Explore different dog and cat breeds</p>
                    </div>
                    <button onClick={() => navigate('/dashboard')} style={styles.backButton}>
                        ← Back to Dashboard
                    </button>
                </div>

                {error && (
                    <div style={styles.errorMessage}>
                        ⚠️ {error}
                    </div>
                )}

                {/* Search Bar */}
                <div style={styles.searchContainer}>
                    <div style={styles.searchWrap}>
                        <input
                            type="text"
                            placeholder="Search breeds..."
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            onFocus={() => setIsSearchFocused(true)}
                            onBlur={() => setTimeout(() => setIsSearchFocused(false), 100)}
                            style={styles.searchInput}
                        />
                        {isSearchFocused && breedSuggestions.length > 0 && (
                            <ul style={styles.suggestionList}>
                                {breedSuggestions.map((name) => (
                                    <li
                                        key={name}
                                        onMouseDown={() => setSearchTerm(name)}
                                        style={styles.suggestionItem}
                                    >
                                        <span>{name}</span>
                                        <small style={styles.suggestionType}>{activeTab === 'dogs' ? 'Dog' : 'Cat'}</small>
                                    </li>
                                ))}
                            </ul>
                        )}
                    </div>
                </div>

                {/* Tabs */}
                <div style={styles.tabs}>
                    <button
                        onClick={() => setActiveTab('dogs')}
                        style={{
                            ...styles.tab,
                            ...(activeTab === 'dogs' ? styles.activeTab : {})
                        }}
                    >
                        🐕 Dogs ({dogBreeds.length})
                    </button>
                    <button
                        onClick={() => setActiveTab('cats')}
                        style={{
                            ...styles.tab,
                            ...(activeTab === 'cats' ? styles.activeTab : {})
                        }}
                    >
                        🐱 Cats ({catBreeds.length})
                    </button>
                </div>

                {/* Breeds Grid */}
                <div style={styles.grid}>
                    {filteredBreeds().map((breed, index) => (
                        <div key={index} style={styles.card}>
                            <div style={styles.cardHeader}>
                                <div style={styles.icon}>
                                    {activeTab === 'dogs' ? '🐕' : '🐱'}
                                </div>
                            </div>
                            <div style={styles.cardBody}>
                                <h3 style={styles.breedName}>{breed.name}</h3>
                                {breed.temperament && (
                                    <p style={styles.temperament}>
                                        <strong>Temperament:</strong> {breed.temperament}
                                    </p>
                                )}
                                {breed.origin && (
                                    <p style={styles.info}>
                                        <strong>Origin:</strong> {breed.origin}
                                    </p>
                                )}
                                {breed.lifeSpan && (
                                    <p style={styles.info}>
                                        <strong>Life Span:</strong> {breed.lifeSpan}
                                    </p>
                                )}
                                {breed.weight && (
                                    <p style={styles.info}>
                                        <strong>Weight:</strong> {breed.weight}
                                    </p>
                                )}
                                {breed.height && (
                                    <p style={styles.info}>
                                        <strong>Height:</strong> {breed.height}
                                    </p>
                                )}
                            </div>
                        </div>
                    ))}
                </div>

                {filteredBreeds().length === 0 && (
                    <div style={styles.emptyState}>
                        <div style={styles.emptyIcon}>🔍</div>
                        <p style={styles.emptyText}>No breeds found matching "{searchTerm}"</p>
                    </div>
                )}
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
    errorMessage: {
        padding: '16px',
        borderRadius: '8px',
        backgroundColor: '#fee2e2',
        color: '#991b1b',
        border: '1px solid #fecaca',
        marginBottom: '20px',
        fontSize: '14px',
        fontWeight: '500'
    },
    searchContainer: {
        marginBottom: '24px'
    },
    searchWrap: {
        position: 'relative',
        maxWidth: '400px'
    },
    searchInput: {
        width: '100%',
        padding: '14px 20px',
        fontSize: '15px',
        border: '2px solid var(--card-border)',
        borderRadius: '12px',
        backgroundColor: 'var(--card-bg)',
        color: 'var(--text-primary)',
        outline: 'none',
        transition: 'all 0.3s ease'
    },
    suggestionList: {
        position: 'absolute',
        left: 0,
        right: 0,
        top: 'calc(100% + 6px)',
        margin: 0,
        padding: '8px',
        listStyle: 'none',
        borderRadius: '12px',
        border: '1px solid var(--card-border)',
        background: 'var(--card-bg)',
        boxShadow: '0 10px 24px rgba(0,0,0,0.2)',
        zIndex: 30,
        maxHeight: '220px',
        overflowY: 'auto'
    },
    suggestionItem: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        gap: '12px',
        padding: '8px 10px',
        borderRadius: '8px',
        color: 'var(--text-primary)',
        cursor: 'pointer'
    },
    suggestionType: {
        color: 'var(--text-muted)',
        fontSize: '11px',
        fontWeight: '700',
        textTransform: 'uppercase'
    },
    tabs: {
        display: 'flex',
        gap: '12px',
        marginBottom: '30px',
        borderBottom: '2px solid var(--border-color)',
        paddingBottom: '0'
    },
    tab: {
        padding: '12px 24px',
        fontSize: '15px',
        fontWeight: '500',
        backgroundColor: 'transparent',
        color: 'var(--text-secondary)',
        border: 'none',
        borderBottom: '3px solid transparent',
        cursor: 'pointer',
        transition: 'all 0.2s ease',
        marginBottom: '-2px'
    },
    activeTab: {
        color: 'var(--primary)',
        borderBottomColor: 'var(--primary)',
        fontWeight: '600'
    },
    grid: {
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))',
        gap: '24px'
    },
    card: {
        backgroundColor: 'var(--card-bg)',
        borderRadius: '16px',
        padding: '24px',
        boxShadow: 'var(--shadow-md)',
        transition: 'transform 0.2s ease, box-shadow 0.2s ease',
        cursor: 'pointer',
        border: '1px solid var(--card-border)'
    },
    cardHeader: {
        marginBottom: '16px',
        textAlign: 'center'
    },
    icon: {
        fontSize: '48px',
        width: '80px',
        height: '80px',
        margin: '0 auto',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: 'var(--light-bg)',
        borderRadius: '16px'
    },
    cardBody: {
        textAlign: 'center'
    },
    breedName: {
        fontSize: '20px',
        fontWeight: '700',
        color: 'var(--text-primary)',
        marginBottom: '16px'
    },
    temperament: {
        fontSize: '14px',
        color: 'var(--text-secondary)',
        marginBottom: '12px',
        lineHeight: '1.6',
        textAlign: 'left'
    },
    info: {
        fontSize: '14px',
        color: 'var(--text-secondary)',
        marginBottom: '8px',
        textAlign: 'left'
    },
    emptyState: {
        textAlign: 'center',
        padding: '80px 20px',
        color: 'var(--text-secondary)'
    },
    emptyIcon: {
        fontSize: '64px',
        marginBottom: '16px'
    },
    emptyText: {
        fontSize: '16px'
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

export default Breeds;
