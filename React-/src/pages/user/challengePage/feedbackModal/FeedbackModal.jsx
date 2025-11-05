import React, { useState } from 'react';
import './feedbackModal.css';

function FeedbackModal({ visible, onClose, onSubmit, challengeId }) {
  const [comment, setComment] = useState('');

  if (!visible) return null;

  const handleSubmit = () => {
    if (!comment.trim()) return alert("Feedback cannot be empty.");
    onSubmit(comment, challengeId);
    setComment('');
  };

  return (
    <div className="feedback-modal-overlay">
      <div className="feedback-modal-box">
        <button className="feedback-close-button" onClick={onClose}>✖</button>

        <h2>🎉 Challenge Solved!</h2>
        <p>We’d love to hear your feedback:</p>
        <textarea
          rows={5}
          value={comment}
          onChange={(e) => setComment(e.target.value)}
          placeholder="Enter your feedback here..."
        />
        <button className="feedback-submit green" onClick={handleSubmit}>
          Submit Feedback
        </button>
      </div>
    </div>
  );
}

export default FeedbackModal;
