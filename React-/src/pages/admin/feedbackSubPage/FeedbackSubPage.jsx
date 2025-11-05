import React, { useEffect, useState } from 'react';
import '../../../components/AdminSubPageLayout/adminSubPageLayout.css';
import axios from 'axios';

function FeedbackSubPage() {
  // State for feedback data and form inputs
  const [feedbacks, setFeedbacks] = useState([]);
  const [comment, setComment] = useState('');
  const [challengeId, setChallengeId] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Pagination and search state
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [isSearching, setIsSearching] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');

  // GET all feedbacks (Admin View)
  const fetchAllFeedbacks = (pageNum = 0) => {
    setLoading(true);
    setError(null);

    axios.get(`/api/feedback/get/all?page=${pageNum}&size=5`)
      .then(res => {
        setFeedbacks(res.data.response);
        setTotalPages(res.data.totalPages);
        setPage(pageNum);
      })
      .catch(err => {
        setError(err.response?.data?.response || 'Failed to fetch feedbacks');
      })
      .finally(() => {
        setLoading(false);
      });
  };

  // GET feedbacks by challenge name
  const fetchFeedbacksByChallengeId = (name) => {
    if (!name) return;
    setLoading(true);
    setError(null);
    setIsSearching(true);

    axios.get(`/api/feedback/get/challenge/${name}`)
      .then(res => {
        setFeedbacks(res.data.response);
        setTotalPages(1);
        setPage(0);
      })
      .catch(err => {
        setError(err.response?.data?.response || 'Failed to fetch feedbacks for challenge');
      })
      .finally(() => {
        setLoading(false);
      });
  };

  // POST feedback (admin can add feedback for a challenge)
  const handleAddFeedback = () => {
    if (!comment.trim() || !challengeId) return alert("Comment and Challenge ID are required");

    setLoading(true);
    setError(null);

    axios.post(`/api/feedback/post/${challengeId}`, { comment })
      .then(res => {
        alert(res.data.response);
        fetchFeedbacksByChallengeId(challengeId);
        setComment('');
      })
      .catch(err => {
        setError(err.response?.data?.response || 'Failed to add feedback');
      })
      .finally(() => {
        setLoading(false);
      });
  };

  // DELETE feedback by feedback ID
  const handleDeleteFeedback = (id) => {
    const confirmDelete = window.confirm("Are you sure you want to delete this feedback?");
    if (!confirmDelete) return;

    setLoading(true);
    setError(null);

    axios.delete(`/api/feedback/delete/${id}`)
      .then(res => {
        alert(res.data.response);
        fetchAllFeedbacks();
      })
      .catch(err => {
        setError(err.response?.data?.response || 'Failed to delete feedback');
      })
      .finally(() => {
        setLoading(false);
      });
  };

  // Utility: Get challenge name by ID (not used in table, but available if needed)
  const getChallengeNameById = (id) => {
    return axios.get(`/api/challenge/get/${id}`)
      .then(res => res.data.response.name)
      .catch(err => {
        console.error('Failed to fetch challenge name:', err);
        return 'Unknown Challenge';
      });
  }

  // --- USE EFFECTS ---

  // Fetch all feedbacks on mount
  useEffect(() => {
    fetchAllFeedbacks();
  }, []);

  // Keyboard shortcut: ESC to close modals and clear errors (legacy, not used here)
  useEffect(() => {
    const handleKeyPress = (e) => {
      if (e.key === 'Escape') {
        // No modals in this page, but could be extended
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
      {/* Search bar for feedback by challenge name */}
      <div className="upper-part">
        <form className="search" onSubmit={(e) => { e.preventDefault(); fetchFeedbacksByChallengeId(searchTerm); }}>
          <input
            type="text"
            placeholder="by Challenge Name"
            value={searchTerm}
            onChange={(e) => {
              setSearchTerm(e.target.value)
              if (e.target.value.trim() === '') {
                fetchAllFeedbacks();
              }
            }}
          />
          {searchTerm && <button type="button" onClick={() => { setSearchTerm(''); setIsSearching(false); fetchAllFeedbacks(); }}>✖</button>}
          <img src="/images/search_icon.png" alt="Search" onClick={() => fetchFeedbacksByChallengeId(searchTerm)} />
        </form>
        <p>Feedback Management</p>
      </div>

      {/* Feedback table */}
      <div className="data-table">
        <table>
          <thead>
            <tr>
              <th>Comment</th>
              <th>challenge</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {feedbacks.length > 0 ? (
              feedbacks.map((fb) => (
                <tr key={fb.id}>
                  <td>{fb.comment}</td>
                  <td>{fb.challengeName}</td>
                  <td>
                    <button className='red' onClick={() => handleDeleteFeedback(fb.id)}>Delete</button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="3" style={{ textAlign: "center", padding: "1rem" }}>
                  {searchTerm ? "No feedback found for this challenge." : "No feedback available."}
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* Pagination controls */}
      {!isSearching && (
        <div className="pagination">
          <button className="green" disabled={page === 0} onClick={() => fetchAllFeedbacks(page - 1)}>{`<`}</button>
          <span>Page {page + 1} of {totalPages}</span>
          <button className="green" disabled={page + 1 >= totalPages} onClick={() => fetchAllFeedbacks(page + 1)}>{`>`}</button>
        </div>
      )}
    </div>
  );
}

export default FeedbackSubPage;