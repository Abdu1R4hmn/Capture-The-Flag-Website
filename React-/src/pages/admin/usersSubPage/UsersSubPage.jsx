import React, { useEffect, useState, useCallback } from 'react';
import '../../../components/AdminSubPageLayout/adminSubPageLayout.css';
import axios from 'axios';

function UsersSubPage() {
    // Pagination state
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);

    // Search state
    const [searchEmail, setSearchEmail] = useState('');
    const [searchMessage, setSearchMessage] = useState('');
    const [isSearching, setIsSearching] = useState(false);

    // Edit user state
    const [selectedUser, setSelectedUser] = useState(null);
    const [editUserData, setEditUserData] = useState({ username: '', email: '', password: '', role: 'ROLE_USER' });
    const [showEditForm, setShowEditForm] = useState(false);
    const [editError, setEditError] = useState(null);

    // Add user state
    const [newUser, setNewUser] = useState({ username: '', email: '', password: '', role: 'ROLE_USER' });
    const [showAddForm, setShowAddForm] = useState(false);
    const [addError, setAddError] = useState(null);

    // User data
    const [data, setData] = useState([]);

    // Other UI state
    const [submitting, setSubmitting] = useState(false);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // --- API HANDLERS ---

    // Search for a user by email
    const handleSearch = (e) => {
        e.preventDefault();
        setIsSearching(true);
        const trimmedEmail = searchEmail.trim();

        if (!trimmedEmail) {
            setIsSearching(false);
            handleUsersAll();
            return;
        }

        setLoading(true);
        setError(null);

        axios.get(`/api/user/get/email/${trimmedEmail}`)
            .then((res) => {
                const user = res.data.response;
                setData(user ? [user] : []);
                setSearchMessage(user ? '' : "No user found with that email.");
                setLoading(false);
            })
            .catch((error) => {
                if (error.response?.status === 404) {
                    setData([]);
                    setSearchMessage("No user found with that email.");
                } else {
                    setError("Something went wrong. Please try again.");
                }
                setLoading(false);
            });
    }

    // Get all users (paginated)
    const handleUsersAll = (pageNum = 0) => {
        setIsSearching(false);
        axios.get(`/api/user/get/all?page=${pageNum}&size=5`)
            .then((userResponse) => {
                setData(userResponse.data.response);
                setTotalPages(userResponse.data.totalPages);
                setPage(pageNum);
                setLoading(false);
            })
            .catch((err) => {
                const message =
                    err.response?.data?.response ||
                    err.response?.data?.message ||
                    err.message ||
                    "Something went wrong. Please try again!";
                setError(message);
                setLoading(false);
            });
    }

    // Add a new user
    const handleAddUser = () => {
        setAddError(null);
        setSubmitting(true);

        axios.post(`/api/user/post`, newUser)
            .then((res) => {
                if (res.data && res.data.status == "SUCCESS") {
                    alert(res.data.response);
                    handleUsersAll();
                    setShowAddForm(false);
                    setNewUser({ username: '', email: '', password: '', role: 'ROLE_USER' });
                } else {
                    setAddError(res.data.response || "failed to add user.");
                }
            }).catch((err) => {
                const message =
                    err.response?.data?.response ||
                    err.response?.data?.message ||
                    err.message ||
                    "Failed to add user.";
                setAddError(message);
            }).finally(() => {
                setSubmitting(false);
            });
    }

    // Edit an existing user
    const handleEditUser = (id) => {
        const confirmEdit = window.confirm("Are you sure you want to save the changes?");
        if (!confirmEdit) return;

        setEditError(null);
        setSubmitting(true);

        // Prepare payload for user update
        const payload = {
            username: editUserData.username,
            email: editUserData.email,
            role: editUserData.role,
        };

        // Only include password if it's set
        if (editUserData.password) {
            payload.password = editUserData.password;
        }

        axios.patch(`/api/user/edit/${id}`, payload)
            .then((res) => {
                if (res.data.status === "SUCCESS") {
                    alert("User updated successfully!");
                    handleUsersAll();
                    setShowEditForm(false);
                    setSelectedUser(null);
                } else {
                    setEditError(res.data.response || "Failed to update user.");
                }
            })
            .catch((err) => {
                console.log("API Error:", err.response);
                const message =
                    err.response?.data?.response ||
                    "Something went wrong. Please try again!";
                setEditError(message);
            }).finally(() => {
                setSubmitting(false);
            });
    }

    // Delete a user
    const deleteUser = (id) => {
        const confirmDelete = window.confirm("are you sure you want to delete the user?")
        if (!confirmDelete) return;

        axios.delete(`http://localhost:8080/api/user/delete/${id}`)
            .then((userResponse) => {
                setData(prevData => prevData.filter(user => user.id !== id))

                if (userResponse.data && userResponse.data.status == "SUCCESS"){
                    alert(userResponse.data.response);
                    handleUsersAll();
                } else {
                    alert("Unexpected response from server.")
                }
            })
            .catch((err) => {
                const msg =
                    err.response?.data?.response ||
                    err.response?.data?.message ||
                    err.message ||
                    "Failed to delete user.";
                setError(msg);
            })
    }

    // --- USE EFFECTS ---

    // Fetch all users on mount
    useEffect(() => {
        handleUsersAll();
    },[]);
    
    // Keyboard shortcut: ESC closes modals and clears errors
    useEffect(() => {
        const handleKeyPress = (e) => {
            if (e.key === 'Escape') {
                setShowAddForm(false);
                setShowEditForm(false);
                setSelectedUser(null);
                setAddError(null);
                setEditError(null);
            }
        };
        document.addEventListener('keydown', handleKeyPress);
        return () => document.removeEventListener('keydown', handleKeyPress);
    }, []);

    // --- RENDER LOGIC ---

    // Loading spinner
    if (loading) return (
        <div className='loading'>
            <div className="spinner" />
            <p>Loading, please wait...</p>
        </div>
    );
      
    // Error message
    if (error) return (
        <div style={{ textAlign: 'center', color: 'red', padding: '2rem' }}>
            <strong>Error:</strong> {error}
        </div>
    );
      
    return ( 
        <div className="admin-layout">

            {/* Search and Add User */}
            <div className="upper-part">
                <form typeof='submit' method='GET' className="search" onSubmit={handleSearch}>
                    <input id="searchEmail" type="text" placeholder='Search by EMAIL'
                        value={searchEmail}
                        onChange={(e) => {
                            setSearchEmail(e.target.value);
                            if (e.target.value.trim() === '') {
                                handleUsersAll();
                            }
                        }}
                    />
                    {searchEmail && (
                        <button type="button" onClick={() => { setSearchEmail(''); handleUsersAll(); }}>✖</button>
                    )}
                    <img src="/images/search_icon.png" alt="Search" onClick={handleSearch}/>
                </form>

                <p> User Management </p>

                <div className="AddNew">
                    <button className='green' onClick={() => setShowAddForm(true)} >Add New</button>
                </div>
            </div>

            {/* User table */}
            <div className="data-table">
                <table>
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th>Time</th>
                            <th>Actions</th>
                        </tr>
                   </thead>

                    <tbody>
                    {data.length > 0 ? (
                        data.map((user) => (
                            <tr key={user.id}>
                                <td>{user.username}</td>
                                <td>{user.email}</td>
                                <td>{user.role}</td>
                                <td>{new Date(user.regDateAndTime).toLocaleString()}</td>
                                <td>
                                    <button className='green' onClick={() => { setSelectedUser(user); setEditUserData({ username: user.username, email: user.email,password: '', role: user.role}); setShowEditForm(true);}}>Edit</button>
                                    <button className='red' onClick={() => deleteUser(user.id)}>Delete</button>
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="5" style={{ textAlign: "center", padding: "1rem" }}>
                                {searchMessage || "No users available."}
                            </td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>

            {/* Pagination controls */}
            {!isSearching && <div className="pagination">
                <button className="green" disabled={page === 0} onClick={() => handleUsersAll(page - 1)}>{`<`}</button>
                <span>Page {page + 1} of {totalPages}</span>
                <button className="green" disabled={page + 1 >= totalPages} onClick={() => handleUsersAll(page + 1)}>{`>`}</button>
            </div>}

            {/* Add User Modal */}
            {showAddForm && (
                <div className="modal-backdrop">
                    <form
                        onSubmit={(e) => {
                            e.preventDefault();
                            handleAddUser();
                        }}
                        className="modal"
                    >
                        <h2>Add New User</h2>

                        {addError && <p>{addError}</p>}
                        <input
                            type="text"
                            placeholder="Username"
                            value={newUser.username}
                            onChange={(e) => setNewUser({ ...newUser, username: e.target.value })}
                        />
                        <input
                            type="email"
                            placeholder="Email"
                            value={newUser.email}
                            onChange={(e) => setNewUser({ ...newUser, email: e.target.value })}
                        />
                        <input
                            type="password"
                            placeholder="Password"
                            value={newUser.password}
                            onChange={(e) => setNewUser({ ...newUser, password: e.target.value })}
                        />
                        <select
                            value={newUser.role}
                            onChange={(e) => setNewUser({ ...newUser, role: e.target.value })}
                        >
                            <option value="ROLE_USER">USER</option>
                            <option value="ROLE_ADMIN">ADMIN</option>
                            <option value="ROLE_LECTURER">LECTURER</option>
                        </select>
                        <div className="modal-actions">
                            <button type="submit" className="green" disabled={submitting}>Submit</button>
                            <button type="button" onClick={() => {setShowAddForm(false); setAddError(null); setNewUser({ username: '', email: '', password: '', role: 'ROLE_USER' });}} className="red">Cancel</button>
                        </div>
                    </form>
                </div>
            )}

            {/* Edit User Modal */}
            {showEditForm && selectedUser && (
                <div className="modal-backdrop">
                    <form 
                        onSubmit={(e) => {
                            e.preventDefault();
                            handleEditUser(selectedUser.id); // always pass the id!
                        }}
                        className="modal">
                        <h2>Edit User</h2>

                        {editError && <p>{editError}</p>}

                        <input
                            type="text"
                            placeholder="Username"
                            value={editUserData.username}
                            onChange={(e) => setEditUserData({ ...editUserData, username: e.target.value })}
                        />
                        <input
                            type="email"
                            placeholder="Email"
                            value={editUserData.email}
                            onChange={(e) => setEditUserData({ ...editUserData, email: e.target.value })}
                        />
                        <input
                            type="password"
                            placeholder="New Password (leave blank to keep current)"
                            value={editUserData.password}
                            onChange={(e) => setEditUserData({ ...editUserData, password: e.target.value })}
                        />
                        <select
                            value={editUserData.role}
                            onChange={(e) => setEditUserData({ ...editUserData, role: e.target.value })}
                        >
                            <option value="ROLE_USER">USER</option>
                            <option value="ROLE_ADMIN">ADMIN</option>
                            <option value="ROLE_LECTURER">LECTURER</option>
                        </select>

                        <div className="modal-actions">
                            <button type="submit" className="green" disabled={submitting}>Submit</button>
                            <button type="button" onClick={() => {setShowEditForm(false); setSelectedUser(null); setEditError(null)}} className="red">Cancel</button>
                        </div>
                    </form>
                </div>
            )}

        </div>
     );
}

export default UsersSubPage;
