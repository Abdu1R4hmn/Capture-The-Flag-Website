import React, { useEffect, useState } from 'react';
import '../../../components/AdminSubPageLayout/adminSubPageLayout.css';
import axios from 'axios';

// CategorySubPage handles CRUD operations for challenge categories
function CategorySubPage() {

    // Pagination state
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);

    // Search state
    const [isSearching, setIsSearching] = useState(false);
    const [searchMessage, setSearchMessage] = useState('');
    const [searchTerm, setSearchTerm] = useState('');

    // Edit category state
    const [editValue, setEditValue] = useState('');
    const [editCategory, setEditCategory] = useState(null);
    const [editError, setEditError] = useState(null);

    // Add category state
    const [addError, setAddError] = useState(null);
    const [newCategory, setNewCategory] = useState('');
    const [showAddForm, setShowAddForm] = useState(false);

    // Categories data
    const [categories, setCategories] = useState([]);

    // Delete category state
    const [deleteError, setDeleteError] = useState(null);

    // Other UI state
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [submitting, setSubmitting] = useState(false);

    // --- API HANDLERS ---

    // Search for a category by name
    const handleSearch = (e) => {
        e.preventDefault();
        setIsSearching(true);
        const trimmed = searchTerm.trim();

        if (!trimmed) {
            setIsSearching(false);
            handleCategoriesAll();
            setSearchMessage('');
            return;
        }

        setLoading(true);
        setError(null);

        axios.get(`/api/category/get/type/with-challenge-count/${trimmed}`)
            .then(res => {
                const category = res.data.response;
                setCategories([{ ...category }]);
                setTotalPages(1);
                setPage(0);
                setSearchMessage('');
            })
            .catch(err => {
                if (err.response?.status === 500 || err.response?.status === 404) {
                    setCategories([]);
                    setSearchMessage('Category not found.');
                } else {
                    setError('Search failed.');
                }
            })
            .finally(() => setLoading(false));
    };

    // Get all categories (paginated)
    const handleCategoriesAll = (pageNum = 0) => {
        setIsSearching(false);
        axios.get(`/api/category/get/all/with-challenge-counts?page=${pageNum}&size=5`)
            .then((res) => {
                setCategories(res.data.response);
                setTotalPages(res.data.totalPages);
                setPage(pageNum);
                setLoading(false);
            })
            .catch((err) => {
                setError(err);
                setLoading(false);
            });
    };

    // Add a new category
    const handleAddCategory = (e) => {
        if (e) e.preventDefault();
        if (!newCategory.trim()) {
            setAddError("Category type can't be empty.");
            return;
        }

        setSubmitting(true);
        setAddError(null);

        axios.post('/api/category/post', { type: newCategory })
            .then((res) => {
                alert(res.data.response);
                setNewCategory('');
                setShowAddForm(false);
                handleCategoriesAll();
            })
            .catch(err => {
                setAddError(err.response?.data?.response || 'Failed to add category.');
            })
            .finally(() => {
                setSubmitting(false);
            });
    };

    // Edit an existing category
    const handleEditCategory = (e) => {
        if (e) e.preventDefault();

        if (!editValue.trim()) {
            setEditError("Category type can't be empty.");
            return;
        }

        setSubmitting(true);
        setEditError(null);

        axios.patch(`/api/category/put/${editCategory.id}`, { type: editValue })
            .then((res) => {
                alert(res.data.response);
                setEditCategory(null);
                setEditValue('');
                handleCategoriesAll(page);
            })
            .catch(err => {
                setEditError(err.response?.data?.response || 'Failed to update category.');
            })
            .finally(() => {
                setSubmitting(false);
            });
    };

    // Delete a category
    const handleDeleteCategory = (id) => {
        setDeleteError(null);
        const confirmDelete = window.confirm("Are you sure you want to delete this category?");
        if (!confirmDelete) return;

        axios.delete(`/api/category/delete/${id}`)
            .then((res) => {
                alert(res.data.response);
                setCategories(prev => prev.filter(cat => cat.id !== id));
            })
            .catch(err => {
                setDeleteError(err.response?.data?.response || 'Failed to delete category.');
            });
    };

    // --- USE EFFECTS ---

    // Fetch all categories on mount
    useEffect(() => {
        handleCategoriesAll();
    }, []);

    // Keyboard shortcut: ESC closes modals and clears errors
    useEffect(() => {
        const handleKeyPress = (e) => {
            if (e.key === 'Escape') {
                setShowAddForm(false);
                setEditCategory(null);
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
            {/* Search and Add Category */}
            <div className="upper-part">
                <form className="search" onSubmit={handleSearch}>
                    <input id="searchTerm" type="text" placeholder='Search by Name'
                        value={searchTerm}
                        onChange={(e) => {
                            setSearchTerm(e.target.value);
                            if (e.target.value.trim() === '') {
                                handleCategoriesAll();
                            }
                        }}
                    />
                    {searchTerm && (
                        <button type="button" onClick={() => { setSearchTerm(''); handleCategoriesAll(); }}>✖</button>
                    )}
                    <img src="/images/search_icon.png" alt="Search" onClick={handleSearch} />
                </form>
                <p>Category Management</p>
                <div className="AddNew">
                    <button className='green' onClick={() => setShowAddForm(true)} >Add Category</button>
                </div>
            </div>

            {/* Delete error message */}
            {deleteError && (
                <div style={{ color: 'red', textAlign: 'center', marginBottom: '1rem' }}>
                    {deleteError}
                </div>
            )}

            {/* Category table */}
            <div className="data-table">
                <table>
                    <thead>
                        <tr>
                            <th>Type</th>
                            <th>Total Challenges</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {categories.length > 0 ? (
                            categories.map((cat, idx) => (
                                <tr key={idx}>
                                    <td>{cat.type}</td>
                                    <td>{cat.totalChallenges}</td>
                                    <td>
                                        <button className='green' onClick={() => { setEditCategory(cat); setEditValue(cat.type); }}>Edit</button>
                                        <button className='red' onClick={() => handleDeleteCategory(cat.id)}>Delete</button>
                                    </td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="3" style={{ textAlign: "center", padding: "1rem" }}>
                                    {searchMessage || 'No categories available.'}
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>

            {/* Pagination controls */}
            {!isSearching && (
                <div className="pagination">
                    <button className="green" disabled={page === 0} onClick={() => handleCategoriesAll(page - 1)}>{`<`}</button>
                    <span>Page {page + 1} of {totalPages}</span>
                    <button className="green" disabled={page + 1 >= totalPages} onClick={() => handleCategoriesAll(page + 1)}>{`>`}</button>
                </div>
            )}

            {/* Add Category Modal */}
            {showAddForm && (
                <div className="modal-backdrop">
                    <form className="modal" onSubmit={handleAddCategory}>
                        <h2>Add New Category</h2>

                        {addError && <p>{addError}</p>}
                        <input
                            type="text"
                            placeholder="Category Type"
                            value={newCategory}
                            onChange={(e) => setNewCategory(e.target.value)}
                        />

                        <div className="modal-actions">
                            <button type="submit" className="green" disabled={submitting}>Submit</button>
                            <button type="button" onClick={() => { setShowAddForm(false); setAddError(null); setNewCategory('') }} className="red">Cancel</button>
                        </div>
                    </form>
                </div>
            )}

            {/* Edit Category Modal */}
            {editCategory && (
                <div className="modal-backdrop">
                    <form className="modal" onSubmit={handleEditCategory}>
                        <h2>Edit Category</h2>

                        {editError && <p>{editError}</p>}
                        <input
                            type="text"
                            placeholder="New Category Type"
                            value={editValue}
                            onChange={(e) => setEditValue(e.target.value)}
                        />

                        <div className="modal-actions">
                            <button type="submit" className="green" disabled={submitting}>Submit</button>
                            <button type="button" onClick={() => { setEditCategory(null); setEditError(null);}} className="red">Cancel</button>
                        </div>
                    </form>
                </div>
            )}
        </div>
    );
}

export default CategorySubPage;
