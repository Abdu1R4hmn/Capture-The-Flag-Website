import React from 'react';
import Navbar from '../../../components/Navbar/Navbar';
import WelcomePage from './welcomeDashboard/WelcomePage';
import Footer from '../../../components/Footer/Footer';

function index() {
    return ( 
        <>
            <Navbar />
            <WelcomePage />
            {/* <Footer /> */}

        </>
    );
}

export default index;