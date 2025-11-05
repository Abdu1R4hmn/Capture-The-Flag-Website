import React from 'react';
import './hero.css';

function Hero() {
    return ( 
        <div id='hero' className='Hero-Section'>
            {/* Image */}
            <div className='Hero-Image'>
                <img src="./images/Hero_Image.jpg" alt="" />
            </div>
            {/* Text */}
            <div className='Hero-Text'>
                <div className='Hero-Header'>
                    <h1>Test <br/> your <br/> Knowledge?</h1>
                </div>
                <div className='Hero-Subtext'>   
                    <h3>What is Capture The Flag?</h3>
                    <p>Capture the Flag (CTF) challenges are gamified cybersecurity tasks where participants solve puzzles and to find hidden "flags," enhancing their problem-solving and technical skills.</p>
                </div>
            </div>

        </div>
     );
}

export default Hero;