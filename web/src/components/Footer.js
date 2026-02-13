import React from 'react';
import { Link } from 'react-router-dom';

const Footer = () => {
    const year = new Date().getFullYear();

    return (
        <footer style={styles.footer}>
            <div style={styles.glow} aria-hidden="true"></div>
            <div style={styles.container}>
                <div style={styles.brandColumn}>
                    <div style={styles.brandTitle}>AnniMemo</div>
                    <p style={styles.brandTagline}>
                        A calm, organized home for every pet's health story.
                    </p>
                    <div style={styles.badges}>
                        <span style={styles.badge}>Secure</span>
                        <span style={styles.badge}>Web + Mobile</span>
                        <span style={styles.badge}>Pet First</span>
                    </div>
                </div>

                <div style={styles.linksColumn}>
                    <div style={styles.sectionTitle}>Product</div>
                    <Link to="/dashboard" style={styles.link}>Dashboard</Link>
                    <Link to="/pets" style={styles.link}>Pet Profiles</Link>
                    <Link to="/pets/add" style={styles.link}>Add Pet</Link>
                    <Link to="/profile" style={styles.link}>Profile Settings</Link>
                </div>

                <div style={styles.linksColumn}>
                    <div style={styles.sectionTitle}>Resources</div>
                    <a href="#features" style={styles.link}>Features</a>
                    <Link to="/support" style={styles.link}>Support</Link>
                    <a href="#faq" style={styles.link}>FAQ</a>
                    <a href="#updates" style={styles.link}>Release Notes</a>
                </div>

                <div style={styles.linksColumn}>
                    <div style={styles.sectionTitle}>Contact</div>
                    <div style={styles.textLine}>CIT-U, Cebu City</div>
                    <div style={styles.textLine}>hello@annimemo.app</div>
                    <div style={styles.textLine}>+63 900 000 0000</div>
                    <div style={styles.textLine}>Mon-Fri, 9:00-17:00</div>
                </div>

                <div style={styles.newsletterColumn}>
                    <div style={styles.sectionTitle}>Stay in the loop</div>
                    <p style={styles.newsletterText}>
                        Get product updates and pet care tips once a month.
                    </p>
                    <div style={styles.newsletterRow}>
                        <input
                            type="email"
                            placeholder="Email address"
                            style={styles.newsletterInput}
                            aria-label="Email address"
                        />
                        <button type="button" style={styles.newsletterButton}>Subscribe</button>
                    </div>
                    <p style={styles.smallPrint}>No spam. Unsubscribe anytime.</p>
                </div>
            </div>

            <div style={styles.bottomBar}>
                <div style={styles.bottomText}>© {year} AnniMemo. All rights reserved.</div>
                <div style={styles.bottomLinks}>
                    <Link to="/privacy" style={styles.bottomLink}>Privacy</Link>
                    <Link to="/terms" style={styles.bottomLink}>Terms</Link>
                    <Link to="/cookies" style={styles.bottomLink}>Cookies</Link>
                </div>
            </div>
        </footer>
    );
};

const styles = {
    footer: {
        position: 'relative',
        overflow: 'hidden',
        background: 'var(--footer-bg)',
        color: 'white',
        padding: '60px 20px 28px',
        fontFamily: '"Space Grotesk", "Segoe UI", sans-serif'
    },
    glow: {
        position: 'absolute',
        top: '-120px',
        right: '-120px',
        width: '260px',
        height: '260px',
        background: 'radial-gradient(circle, rgba(248, 244, 208, 0.25) 0%, rgba(248, 244, 208, 0) 65%)',
        pointerEvents: 'none'
    },
    container: {
        maxWidth: '1200px',
        margin: '0 auto',
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))',
        gap: '24px'
    },
    brandColumn: {
        display: 'flex',
        flexDirection: 'column',
        gap: '14px'
    },
    brandTitle: {
        fontFamily: '"Cormorant Garamond", Georgia, serif',
        fontSize: '30px',
        letterSpacing: '1px'
    },
    brandTagline: {
        fontSize: '14px',
        color: 'var(--footer-text)',
        lineHeight: '1.6'
    },
    badges: {
        display: 'flex',
        flexWrap: 'wrap',
        gap: '8px'
    },
    badge: {
        fontSize: '12px',
        padding: '6px 12px',
        borderRadius: '999px',
        backgroundColor: 'rgba(255, 255, 255, 0.12)',
        border: '1px solid rgba(255, 255, 255, 0.15)'
    },
    linksColumn: {
        display: 'flex',
        flexDirection: 'column',
        gap: '10px'
    },
    sectionTitle: {
        fontSize: '14px',
        textTransform: 'uppercase',
        letterSpacing: '1.5px',
        color: 'rgba(255, 255, 255, 0.65)',
        marginBottom: '6px'
    },
    link: {
        color: 'var(--footer-text)',
        textDecoration: 'none',
        fontSize: '14px'
    },
    textLine: {
        fontSize: '14px',
        color: 'var(--footer-text)'
    },
    newsletterColumn: {
        display: 'flex',
        flexDirection: 'column',
        gap: '12px'
    },
    newsletterText: {
        fontSize: '14px',
        color: 'var(--footer-text)',
        lineHeight: '1.6'
    },
    newsletterRow: {
        display: 'flex',
        flexWrap: 'wrap',
        gap: '10px'
    },
    newsletterInput: {
        flex: '1 1 160px',
        padding: '10px 14px',
        borderRadius: '12px',
        border: '1px solid rgba(255, 255, 255, 0.2)',
        backgroundColor: 'rgba(15, 23, 42, 0.5)',
        color: 'white'
    },
    newsletterButton: {
        padding: '10px 16px',
        borderRadius: '12px',
        border: 'none',
        background: 'linear-gradient(120deg, #f4ce6a 0%, #f3a952 100%)',
        color: '#1b2a4a',
        fontWeight: '700',
        cursor: 'pointer'
    },
    smallPrint: {
        fontSize: '12px',
        color: 'rgba(255, 255, 255, 0.6)'
    },
    bottomBar: {
        maxWidth: '1200px',
        margin: '30px auto 0',
        paddingTop: '18px',
        borderTop: '1px solid rgba(255, 255, 255, 0.15)',
        display: 'flex',
        flexWrap: 'wrap',
        justifyContent: 'space-between',
        alignItems: 'center',
        gap: '12px'
    },
    bottomText: {
        fontSize: '13px',
        color: 'var(--footer-text)'
    },
    bottomLinks: {
        display: 'flex',
        gap: '16px'
    },
    bottomLink: {
        color: 'var(--footer-text)',
        fontSize: '13px',
        textDecoration: 'none'
    }
};

export default Footer;
