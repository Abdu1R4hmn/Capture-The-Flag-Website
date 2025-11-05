import React, { createContext, useContext, useEffect, useState } from 'react';
import axios from 'axios';

// Create authentication context
const AuthContext = createContext();

// AuthProvider wraps the app and provides user authentication state
export const AuthProvider = ({ children }) => {
  // State for the current user and loading status
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  // On mount, fetch the current user info from backend
  useEffect(() => {
    axios.get('http://localhost:8080/api/user/me', { withCredentials: true })
      .then(res => setUser(res.data.response)) // Set user if authenticated
      .catch(() => setUser(null))              // Set user to null if not authenticated
      .finally(() => setLoading(false));       // Always stop loading
  }, []);

  // Provide user, loading state, and setUser function to children
  return (
    <AuthContext.Provider value={{ user, loading, setUser }}>
      {children}
    </AuthContext.Provider>
  );
};

// Custom hook to access authentication context
export const useAuth = () => useContext(AuthContext);