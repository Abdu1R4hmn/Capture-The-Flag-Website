import React, { useState } from 'react';
import './Navbar.css';
import ButtonNormal from '../ui/ButtonNormal';
import { Link, NavLink, useLocation } from 'react-router-dom';
import { Link as Scroll } from 'react-scroll';

function SharedNavbar() {
  const location = useLocation();
  const [isActive, setIsActive] = useState(false);

  // Toggle hamburger
  const toggleActive = () => setIsActive(!isActive);

  // Detect current path type
  const isAdmin = location.pathname.startsWith('/admin');
  const isChallengePage = location.pathname.startsWith('/challenge');
  const isProfile = location.pathname === '/profile';
  const isHome = location.pathname === '/';

  // 🔐 Replace with real login state
  const isLoggedIn = true;

  return (
    <nav>
      <div className="nav-bar">
        {/* Logo */}
        <Link to={isAdmin ? '/admin' : '/'}>
          <div className="nav-brand">
            <img src="/images/navbarImage.png" alt="logo" />
            <h3>Capture The Flag</h3>
          </div>
        </Link>

        {/* Hamburger */}
        <div className="Hamburger-nav">
          <img
            src={
              isActive
              ? '/images/Hamburfer_closed.png'
              : '/images/Hamburfer_open.png'
            }
            onClick={toggleActive}
            alt="menu"
          />
        </div>

        {/* Tabs and buttons */}
        <div className={`nav-tabs ${isActive ? 'activated' : ''}`}>
          <ul>
            {isHome && (
              <>
                <li>
                  <Scroll to="hero" smooth duration={500}>
                    Home
                  </Scroll>
                </li>
                <li>
                  <Scroll to="challenge" smooth duration={500}>
                    Challenges
                  </Scroll>
                </li>
                <li>
                  <Scroll to="about" smooth duration={500}>
                    About
                  </Scroll>
                </li>
              </>
            )}
            {isChallengePage && (
              <>

                <li>
                  <NavLink to="/challenge" smooth duration={500}>
                    Challenges
                  </NavLink>
                </li>
              </>
            )}

            {isProfile && (
              <>

                <li>
                  <NavLink to="/challenge" smooth duration={500}>
                    Challenges
                  </NavLink>
                </li>
              </>
            )}

            {isAdmin && (
              <>
                <li>
                  <NavLink to="/admin/users">Users</NavLink>
                </li>
                <li>
                  <NavLink to="/admin/category">Category</NavLink>
                </li>
                <li>
                  <NavLink to="/admin/challenges">Challenges</NavLink>
                </li>
                <li>
                  <NavLink to="/admin/feedback">Feedback</NavLink>
                </li>
              </>
            )}
          </ul>

            {/* Right Button */}
            <div className="Nav-button">
            {(isAdmin || isChallengePage || isProfile) ? (
                <ButtonNormal name="Profile" address="./profile" />
            ) : (
                <ButtonNormal name="Login / Signup" address="./login" />
            )}
            </div>

        </div>
      </div>
    </nav>
  );
}

export default SharedNavbar;
