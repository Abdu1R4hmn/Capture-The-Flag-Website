import React from 'react';
import Navbar from '../../../components/Navbar/Navbar.jsx';
import Footer from '../../../components/Footer/Footer.jsx';
import Hero from './heroSection/Hero.jsx'
import Body_One from './body_one/Body_One.jsx';
import Body_Two from './body_two/Body_Two.jsx';
import About from './aboutSection/About.jsx'


function MainPage() {
    return ( 
        <> 
            <Navbar />
            <Hero />
            <Body_One />
            <Body_Two />
            <About />
            <Footer />
        </>
     );
}

export default MainPage;