import React from 'react';
import Navbar from '../../../components/Navbar/Navbar';
import EditProfile from './editProfile/EditProfile';

function index() {
    return ( 
        <>
            <Navbar />
            <EditProfile />
        </>
     );
}

export default index;