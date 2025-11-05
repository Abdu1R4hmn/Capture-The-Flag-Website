import React from 'react';
import './body_two.css'
import ButtonNormal from '../../../../components/ui/ButtonNormal.jsx';

function Body_Two() {
    return ( 
        <>
            <div id='challenge' className="b2-Section">

                <div className="b2-header">
                    <h1>Why Practice With Us?</h1>
                </div>

                <div className="b2_reasons-Section">
                    <div className="b2_reason">
                        <h2>Beginner-Friendly:</h2>
                        <p>This website offers hints and solutions, making cybersecurity concepts easy for beginners.</p>
                    </div>
                    
                    <div className="b2_reason">
                        <h2>Gamified Learning:</h2>
                        <p> Learn through interactive, challenge-based activities that make cybersecurity enjoyable.</p>
                    </div>
                    
                    <div className="b2_reason">
                        <h2>Self-Paced:</h2>
                        <p>Learn at your speed with no time pressure, allowing flexibility to suit your schedule and needs.</p>
                    </div>
                </div>

                    <div className="b2_button wiggle">
                        <ButtonNormal name="Start Practice" address="./challenge"/>
                    </div>
            </div>

        </>
     );
}

export default Body_Two;