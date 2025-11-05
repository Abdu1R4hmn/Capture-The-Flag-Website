import React from 'react';
import './challengeCard.css';
import GetElementDimensions from '../../../../hooks/GetElementDimensions';
import { Link } from 'react-router-dom';
import axios from 'axios';
axios.defaults.withCredentials = true;

// ChallengeCard component displays a list of challenge cards
function ChallengeCard({ challenges, onCardClick, progressMap }) {

  // If there are no challenges, show a message
  if (!challenges || challenges.length === 0) {
    return <div style={{ padding: '1rem', textAlign: 'center' }}>No challenges available.</div>;
  }

  return (
    <div className="challenge-page-container">
      <div className="challengeCard-Section">
        
        {/* Map through each challenge and render a card */}
        {challenges.map((ch) => (
          <Link
            to="#"
            key={ch.id}
            onClick={(e) => {
              e.preventDefault(); // Prevent default link behavior
              onCardClick(ch);    // Call the parent handler with the clicked challenge
            }}
            className="Card"
          >
            {/* Display category and difficulty */}
            <div className="cat-Diff">
              <p>{ch.category?.type}</p>
              <p>{ch.difficulty}</p>
            </div>

            {/* Display challenge name with custom dimension hook */}
            <GetElementDimensions kid={<>{ch.name}</>} />

            {/* Display progress as stars */}
            <div className="star-progress">
              <p>Progress:</p>
              {[...Array(3)].map((_, i) => {
                const earnedStars = progressMap[ch.id] || 0;
                return (
                  <img
                    key={i}
                    src={i < earnedStars ? '/images/Star_full.png' : '/images/Star_empty.png'}
                    className={i === 1 ? 'Center' : 'Side'}
                    alt="star"
                  />
                );
              })}
            </div>
          </Link>
        ))}
      </div>
    </div>
  );
}

export default ChallengeCard;
