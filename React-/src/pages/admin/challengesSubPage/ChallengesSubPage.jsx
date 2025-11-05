import React, { useEffect, useState } from 'react';
import '../../../components/AdminSubPageLayout/adminSubPageLayout.css';
import axios from 'axios';

function ChallengesSubPage() {

    // --- State Management ---

    // Pagination
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);

    // Search
    const [searchName, setSearchName] = useState('');
    const [searchMessage, setSearchMessage] = useState('');
    const [isSearching, setIsSearching] = useState(false);
    const [selectedCategory, setSelectedCategory] = useState('');

    // Edit Challenge
    const [selectedChallenge, setSelectedChallenge] = useState(null);
    const [editChallengeData, setEditChallengeData] = useState({ name: '', description: '', flag: '', difficulty: 'EASY', hint1: '', hint2: '', categoryId: ''  });
    const [showEditForm, setShowEditForm] = useState(false);
    const [editError, setEditError] = useState(null);

    // Add Challenge
    const [newChallenge, setNewChallenge] = useState({ name: '', description: '', flag: '', difficulty: 'EASY', hint1: '', hint2: '', solution: '', categoryId: '' });
    const [showAddForm, setShowAddForm] = useState(false);
    const [addError, setAddError] = useState(null);

    // Challenge Data
    const [data, setData] = useState([]);

    // Delete Challenge
    const [deleteError, setDeleteError] = useState(null);

    // Other UI State
    const [categories, setCategories] = useState([]);
    const [submitting, setSubmitting] = useState(false);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Image upload state
    const [newChallengeImage, setNewChallengeImage] = useState(null);
    const [editChallengeImage, setEditChallengeImage] = useState(null);

    // --- API HANDLERS ---

    // Search for a challenge by name
    const handleSearch = (e) => {
        e.preventDefault();
        setIsSearching(true);
        const trimmedName = searchName.trim();

        if (!trimmedName) {
            setIsSearching(false);
            handleChallengesAll();
            return;
        }

        setLoading(true);
        setError(null);

        axios.get(`/api/challenge/get/name/${trimmedName}`)
            .then((res) => {
                const challenge = res.data.response;
                setData(challenge ? [challenge] : []);
                setSearchMessage(challenge ? '' : "No challenge found with that name.");
                setLoading(false);
            })
            .catch((error) => {
                if (error.response?.status === 404) {
                    setData([]);
                    setSearchMessage("No challenge found with that name.");
                } else {
                    setError("Something went wrong. Please try again.");
                }
                setLoading(false);
            });
    };

    // Get all challenges (paginated)
    const handleChallengesAll = (pageNum = 0) => {
        setIsSearching(false);
        axios.get(`/api/challenge/get/all?page=${pageNum}&size=5`)
            .then((res) => {
                setData(res.data.response);
                setTotalPages(res.data.totalPages);
                setPage(pageNum);
                setLoading(false);
            })
            .catch((err) => {
                setError(err);
                setLoading(false);
            });
    };

    // Add a new challenge
    const handleAddChallenge = () => {
        setAddError(null);
        setSubmitting(true);

        // Prepare payload for challenge creation
        const payload = {
            name: newChallenge.name,
            description: newChallenge.description,
            flag: newChallenge.flag,
            difficulty: newChallenge.difficulty,
            hint1: newChallenge.hint1,
            hint2: newChallenge.hint2,
            solution: newChallenge.solution // <-- Add this
        };

        console.log(payload);

        axios.post(`/api/challenge/post/${newChallenge.categoryId}`, payload)
            .then((res) => {
                if (res.data.status === "SUCCESS") {
                    // Always extract the ID from response.id
                    const challengeId = res.data.response?.id;

                    // If image is attached, upload it after challenge creation
                    if (challengeId && newChallengeImage) {
                      uploadImage(challengeId, newChallengeImage).then(() => {
                        alert("Challenge and image uploaded!");
                        handleChallengesAll();
                      });
                    } else {
                      alert(res.data.response?.name || "Challenge added!");
                      handleChallengesAll();
                    }
                    setShowAddForm(false);
                    setNewChallenge({ name: '', description: '', flag: '', difficulty: 'EASY', hint1: '', hint2: '', solution: '', categoryId: '' });
                    setNewChallengeImage(null);
                } else {
                    setAddError(res.data.response || "Failed to add challenge.");
                }
            })
            .catch((err) => {
                const message = err.response?.data?.response || "Failed to add challenge.";
                setAddError(message);
            })
            .finally(() => {
                setSubmitting(false);
            });
    };

    // Edit an existing challenge
    const handleEditChallenge = (id) => {
        setEditError(null);
        setSubmitting(true);

        // Prepare payload for challenge update
        const payload = {
            name: editChallengeData.name,
            description: editChallengeData.description,
            flag: editChallengeData.flag,
            difficulty: editChallengeData.difficulty,
            hint1: editChallengeData.hint1,
            hint2: editChallengeData.hint2,
            solution: editChallengeData.solution // <-- Add this
        };

        axios.patch(`/api/challenge/put/${id}?catid=${editChallengeData.categoryId}`, payload)
            .then((res) => {
                if (res.data.status === "SUCCESS") {
                  // If image is attached, upload it after challenge update
                  if (editChallengeImage) {
                    uploadImage(id, editChallengeImage).then(() => {
                      alert("Challenge updated and image uploaded!");
                      handleChallengesAll();
                    });
                  } else {
                    alert(res.data.response);
                    handleChallengesAll();
                  }
                  setShowEditForm(false);
                  setSelectedChallenge(null);
                  setEditChallengeImage(null);
                } else {
                    setEditError(res.data.response || "Failed to update challenge.");
                }
            })
            .catch((err) => {
                const message = err.response?.data?.response || "Error updating challenge.";
                setEditError(message);
            })
            .finally(() => {
                setSubmitting(false);
            });
    };

    // Delete a challenge
    const handleDeleteChallenge = (id) => {
        setDeleteError(null);
        const confirmDelete = window.confirm("Are you sure you want to delete this challenge?");
        if (!confirmDelete) return;

        axios.delete(`/api/challenge/delete/${id}`)
            .then((res) => {
                alert(res.data.response);
                setData(prev => prev.filter(challenge => challenge.id !== id));
            })
            .catch(err => {
                setDeleteError(err.response?.data?.response || 'Failed to delete challenge.');
            });
    };

    // Filter challenges by category
    const handleCategoryFilter = (categoryId) => {
        setSelectedCategory(categoryId);
        if (categoryId === '') {
            handleChallengesAll();
        } else {
            const selected = categories.find(c => c.id.toString() === categoryId);
            if (selected) {
                axios.get(`/api/challenge/get/category/${selected.type}`)
                    .then((res) => {
                        setData(res.data.response);
                        setTotalPages(1);
                        setPage(0);
                        setIsSearching(true);
                        setSearchMessage(res.data.response.length === 0 ? 'No challenges in this category.' : '');
                    })
                    .catch((err) => {
                        setError('Failed to fetch category challenges.');
                    });
            }
        }
    };

    // --- USE EFFECTS ---

    // Fetch all challenges on mount
    useEffect(() => {
        handleChallengesAll();
    }, []);

    // Keyboard shortcut: ESC closes modals and clears errors
    useEffect(() => {
        const handleKeyPress = (e) => {
            if (e.key === 'Escape') {
                setShowAddForm(false);
                setShowEditForm(false);
                setSelectedChallenge(null);
                setAddError(null);
                setEditError(null);
            }
        };
        document.addEventListener('keydown', handleKeyPress);
        return () => document.removeEventListener('keydown', handleKeyPress);
    }, []);

    // Fetch all categories for dropdowns
    useEffect(() => {
        axios.get('/api/category/get/all')
            .then((res) => {
                setCategories(res.data.response);
            })
            .catch((err) => {
                console.error("Failed to load categories", err);
            });
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
            {/* Search and filter section */}
            <div className="upper-part">
                <form className="search" onSubmit={handleSearch}>
                    <input
                        type="text"
                        placeholder="Search by Name"
                        value={searchName}
                        onChange={(e) => {
                            setSearchName(e.target.value)
                            if (e.target.value.trim() === '') {
                                handleChallengesAll();
                            }
                        }}
                    />
                    {searchName && <button type="button" onClick={() => { setSearchName(''); handleChallengesAll(); }}>✖</button>}
                    <img src="/images/search_icon.png" alt="Search" onClick={handleSearch} />
                </form>

                <p> Challenge Management </p>
                <select
                    value={selectedCategory}
                    onChange={(e) => handleCategoryFilter(e.target.value)}
                    className="category-filter styled-dropdown"
                >
                    <option value="">All Categories</option>
                    {categories.map(cat => (
                        <option key={cat.id} value={cat.id}>{cat.type}</option>
                    ))}
                </select>

                <div className="AddNew">
                    <button className='green' onClick={() => setShowAddForm(true)}>Add New</button>
                </div>
            </div>

            {/* Data table for challenges */}
            <div className="data-table">
                <table>
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Description</th>
                            <th>Category</th>
                            <th>Difficulty</th>
                            <th>Flag</th>
                            <th>Hint 1</th>
                            <th>Hint 2</th>
                            <th>Solution</th> {/* <-- Add this */}
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {data.length > 0 ? (
                            data.map((challenge) => (
                                <tr key={challenge.id}>
                                    <td>{challenge.name}</td>
                                    <td className="truncate description" title={challenge.description}>{challenge.description}</td>
                                    <td>{challenge.category.type}</td>
                                    <td>{challenge.difficulty}</td>
                                    <td>{challenge.flag}</td>
                                    <td className="truncate" title={challenge.hint1}>{challenge.hint1}</td>
                                    <td className="truncate" title={challenge.hint2}>{challenge.hint2}</td>
                                    <td className="truncate solution" title={challenge.solution}>{challenge.solution}</td>
                                    <td>
                                        {/* Edit and Delete buttons */}
                                        <button
                                            className='green'
                                            title="Edit"
                                            onClick={() => {
                                                setSelectedChallenge(challenge);
                                                setEditChallengeData({
                                                    name: challenge.name,
                                                    description: challenge.description,
                                                    flag: challenge.flag,
                                                    difficulty: challenge.difficulty,
                                                    hint1: challenge.hint1,
                                                    hint2: challenge.hint2,
                                                    solution: challenge.solution,
                                                    categoryId: challenge.category?.id
                                                });
                                                setShowEditForm(true);
                                            }}
                                        >Edit</button>
                                        <button
                                            className='red'
                                            title="Delete"
                                            onClick={() => handleDeleteChallenge(challenge.id)}
                                        >Delete</button>
                                    </td>
                                </tr>
                            ))
                        ): (
                            <tr>
                                <td colSpan="9" style={{ textAlign: "center", padding: "1rem" }}>
                                    {searchMessage || "No challenges available."}
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>

            {/* Pagination controls */}
            {!isSearching && <div className="pagination">
                <button className="green" disabled={page === 0} onClick={() => handleChallengesAll(page - 1)}>{`<`}</button>
                <span>Page {page + 1} of {totalPages}</span>
                <button className="green" disabled={page + 1 >= totalPages} onClick={() => handleChallengesAll(page + 1)}>{`>`}</button>
            </div>}

            {/* Add Challenge Modal */}
            {showAddForm && (
                <div className="modal-backdrop">
                    <form className="modal" onSubmit={(e) => { e.preventDefault(); handleAddChallenge(); }}>
                        <h2>Add New Challenge</h2>

                        {addError && <p>{addError}</p>}
                        <input
                            type="text"
                            placeholder="Name"
                            value={newChallenge.name}
                            onChange={(e) => setNewChallenge({ ...newChallenge, name: e.target.value })}
                        />
                        <textarea
                            type="text"
                            placeholder="Description"
                            value={newChallenge.description}
                            onChange={(e) => setNewChallenge({ ...newChallenge, description: e.target.value })}
                        />
                        <input
                            type="text"
                            placeholder="Flag"
                            value={newChallenge.flag}
                            onChange={(e) => setNewChallenge({ ...newChallenge, flag: e.target.value })}
                        />
                        <select
                            value={newChallenge.difficulty}
                            onChange={(e) => setNewChallenge({ ...newChallenge, difficulty: e.target.value })}
                        >
                            <option value="EASY">EASY</option>
                            <option value="MEDIUM">MEDIUM</option>
                            <option value="HARD">HARD</option>
                        </select>
                        <select
                            value={newChallenge.categoryId}
                            onChange={(e) => setNewChallenge({ ...newChallenge, categoryId: e.target.value })}
                            required
                        >
                            <option value="">Select Category</option>
                            {categories.map((cat) => (
                                <option key={cat.id} value={cat.id}>{cat.type}</option>
                            ))}
                        </select>

                        <input
                            type="text"
                            placeholder="Hint 1"
                            value={newChallenge.hint1}
                            onChange={(e) => setNewChallenge({ ...newChallenge, hint1: e.target.value })}
                        />
                        <input
                            type="text"
                            placeholder="Hint 2"
                            value={newChallenge.hint2}
                            onChange={(e) => setNewChallenge({ ...newChallenge, hint2: e.target.value })}
                        />
                        <input
                            type="text"
                            placeholder="Solution"
                            value={newChallenge.solution || ''}
                            onChange={(e) => setNewChallenge({ ...newChallenge, solution: e.target.value })}
                        />

                        {/* Image upload input */}
                        <input
                            type="file"
                            accept="image/*"
                            onChange={e => setNewChallengeImage(e.target.files[0])}
                        />

                        <div className="modal-actions">
                            <button type="submit" className="green" disabled={submitting}>Submit</button>
                            <button type="button" onClick={() => { setShowAddForm(false); setAddError(null); setNewChallenge({ name: '', description: '', flag: '', difficulty: 'EASY', hint1: '', hint2: '', solution: '', categoryId: '' }) }} className="red">Cancel</button>
                        </div>
                    </form>
                </div>
            )}

            {/* Edit Challenge Modal */}
            {showEditForm && selectedChallenge && (
                <div className="modal-backdrop">
                    <form className="modal" onSubmit={(e) => { e.preventDefault(); handleEditChallenge(selectedChallenge.id); }}>
                        <h2>Edit Challenge</h2>

                        {editError && <p>{editError}</p>}
                        <input
                            type="text"
                            placeholder="Name"
                            value={editChallengeData.name}
                            onChange={(e) => setEditChallengeData({ ...editChallengeData, name: e.target.value })}
                        />
                        <textarea
                            type="text"
                            placeholder="Description"
                            value={editChallengeData.description}
                            onChange={(e) => setEditChallengeData({ ...editChallengeData, description: e.target.value })}
                        />
                        <input
                            type="text"
                            placeholder="Flag"
                            value={editChallengeData.flag}
                            onChange={(e) => setEditChallengeData({ ...editChallengeData, flag: e.target.value })}
                        />
                        <select
                            value={editChallengeData.difficulty}
                            onChange={(e) => setEditChallengeData({ ...editChallengeData, difficulty: e.target.value })}
                        >
                            <option value="EASY">EASY</option>
                            <option value="MEDIUM">MEDIUM</option>
                            <option value="HARD">HARD</option>
                        </select>

                        <select
                            value={editChallengeData.categoryId}
                            onChange={(e) => setEditChallengeData({ ...editChallengeData, categoryId: e.target.value })}
                            required
                        >
                            <option value="">Select Category</option>
                            {categories.map((cat) => (
                                <option key={cat.id} value={cat.id}>{cat.type}</option>
                            ))}
                        </select>

                        <input
                            type="text"
                            placeholder="Hint 1"
                            value={editChallengeData.hint1}
                            onChange={(e) => setEditChallengeData({ ...editChallengeData, hint1: e.target.value })}
                        />
                        <input
                            type="text"
                            placeholder="Hint 2"
                            value={editChallengeData.hint2}
                            onChange={(e) => setEditChallengeData({ ...editChallengeData, hint2: e.target.value })}
                        />
                        <input
                            type="text"
                            placeholder="Solution"
                            value={editChallengeData.solution || ''}
                            onChange={(e) => setEditChallengeData({ ...editChallengeData, solution: e.target.value })}
                        />

                        {/* Image upload input */}
                        <input
                            type="file"
                            accept="image/*"
                            onChange={e => setEditChallengeImage(e.target.files[0])}
                        />

                        <div className="modal-actions">
                            <button type="submit" className="green" disabled={submitting}>Submit</button>
                            <button type="button" onClick={() => { setShowEditForm(false); setSelectedChallenge(null); setEditError(null); }} className="red">Cancel</button>
                        </div>
                    </form>
                </div>
            )}

        </div>
    );
}

// Helper function to upload image for a challenge
const uploadImage = (challengeId, imageFile) => {
  if (!imageFile) return Promise.resolve();
  const formData = new FormData();
  formData.append('image', imageFile);
  return axios.post(`/api/challenge/${challengeId}/image`, formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  });
};

export default ChallengesSubPage;
