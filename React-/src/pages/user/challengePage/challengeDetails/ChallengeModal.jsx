import React, { useState, useEffect } from 'react';
import './challengeModal.css';
import FeedbackModal from '../feedbackModal/FeedbackModal';
import axios from 'axios';

// ChallengeModal displays the details of a selected challenge in a modal
function ChallengeModal({ challenge, visible, onClose, onSubmitFlag }) {
  // State for flag input
  const [flag, setFlag] = useState('');
  // State for which hints have been used
  const [usedHints, setUsedHints] = useState([]);
  // State for which hints are visible
  const [visibleHints, setVisibleHints] = useState([]);
  // State for showing feedback modal
  const [showFeedback, setShowFeedback] = useState(false);
  // State for showing solution modal
  const [showSolution, setShowSolution] = useState(false);
  // State for showing hint modal (null, 1, or 2)
  const [showHintModal, setShowHintModal] = useState(null);
  // State for image existence
  const [imageExists, setImageExists] = useState(true);

  // Reset modal state when challenge or visibility changes
  useEffect(() => {
    if (visible && challenge) {
        setFlag('');
        setUsedHints([]);
        setVisibleHints([]);
        setImageExists(true); // <-- Reset image existence when challenge changes
    }
  }, [challenge, visible]);

  // If modal is not visible or no challenge is selected, render nothing
  if (!visible || !challenge) return null;

  // Handle flag submission
  const handleSubmit = () => {
    if (!flag.trim()) return alert('Flag cannot be empty');
    onSubmitFlag(challenge.id, flag, usedHints, () => setShowFeedback(true));
    setFlag('');
  };

  return (
    <div className="modal-overlay">
      <div className="modal-box">
        {/* Close button */}
        <button className="close-button" onClick={onClose}>✖</button>

        {/* Challenge header info */}
        <div className='challenge-header'>
          <h2>{challenge.name}</h2>
          <hr />
          <div className="details">
            <p><strong>Category:</strong> {challenge.category?.type}</p>
            <p><strong>Difficulty:</strong> {challenge.difficulty}</p>
          </div>
        </div>

        {/* Challenge image */}
        {imageExists && (
          <img
            src={`/api/challenge/${challenge.id}/image`}
            alt={challenge.name}
            className="challenge-image"
            style={{ maxWidth: '100%', maxHeight: '250px', objectFit: 'contain', margin: '1rem 0' }}
            onError={() => setImageExists(false)}
          />
        )}

        {/* Challenge description */}
        <p className="description">{challenge.description}</p>

        {/* Flag submission and hint section */}
        <div className="flag-submit">
          <div className="hint-buttons">
            {/* Hint buttons */}
            {[1, 2].map(num => (
              <div key={num}>
                <button
                  className="hint-btn"
                  onClick={() => {
                    setUsedHints(prev => prev.includes(num) ? prev : [...prev, num]);
                    setVisibleHints(prev => prev.includes(num) ? prev : [...prev, num]);
                    setShowHintModal(num); // Open the modal for this hint
                  }}
                >
                  Hint: {num}
                </button>
              </div>
            ))}
            {/* Reveal solution button */}
            <button
              className="reveal-btn"
              onClick={() => setShowSolution(true)}
            >
              Reveal Solution
            </button>
          </div>

          {/* Flag input and submit */}
          <input
            type="text"
            value={flag}
            onChange={(e) => setFlag(e.target.value)}
            placeholder="Enter flag here..."
          />
          <button className="green" onClick={handleSubmit}>Submit Flag</button>
        </div>
      </div>

      {/* Feedback modal for user comments */}
      <FeedbackModal
        visible={showFeedback}
        onClose={() => setShowFeedback(false)}
        onSubmit={(comment, challengeId) => {
          axios.post(`/api/feedback/post/${challengeId}`, { comment })
            .then(() => {
              alert('Feedback submitted!');
              setShowFeedback(false);
              onClose(); // Closes both feedback modal and challenge modal
            })
            .catch(err => {
              console.error('Failed to submit feedback', err);
              alert('Submission failed');
            });
        }}
        challengeId={challenge.id}
      />

      {/* Solution Modal */}
      {showSolution && (
        <div className="modal-overlay">
          <div className="modal-box" style={{ maxWidth: '40rem', padding: '2rem' }}>
            <button className="close-button" onClick={() => setShowSolution(false)}>✖</button>
            <h3 style={{margin: '0.6rem 0rem'}}>Solution</h3>
            <hr />
            <p style={{ whiteSpace: 'pre-wrap', margin: '0.4rem 0rem 0rem 0rem'} }>{challenge.solution}</p>
          </div>
        </div>
      )}

      {/* Hint Modal */}
      {showHintModal && (
        <div className="modal-overlay">
          <div className="modal-box" style={{ maxWidth: '30rem', padding: '1.5rem' }}>
            <button className="close-button" onClick={() => setShowHintModal(null)}>✖</button>
            <h3 style={{margin: '0.6rem 0rem'}}>Hint {showHintModal}</h3>
            <hr />
            <p style={{ whiteSpace: 'pre-wrap', margin: '0.4rem 0rem 0rem 0rem'} }>
              {challenge[`hint${showHintModal}`]}
            </p>
          </div>
        </div>
      )}
    </div>
  );
}

export default ChallengeModal;
