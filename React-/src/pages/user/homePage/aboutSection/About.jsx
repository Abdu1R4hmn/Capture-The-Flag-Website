import React from 'react';
import './about.css'

function About() {
    return ( 

        <>
            <div id='about' className="about-Section">

                <div className="about">

                    <div className="aboutHeader">
                        <h1>About</h1>
                    </div>

                    <div className="aboutSubtext">
                        <p>We aim to make cybersecurity learning engaging and practical by offering interactive challenges that build essential skills in key cybersecurity fields</p>
                    </div>

                </div>

                <div className="goal">

                    <div className="aboutHeader">
                        <h1>Goal</h1>
                    </div>

                    <div className="aboutSubtext">
                        <p>Our goal is to equip users with hands-on experience to tackle real-world cybersecurity issues, fostering confidence and expertise.</p>
                    </div>
                
                </div>

            </div>
        </>
    );
}

export default About;