import React from 'react';
import './ButtonNormal.css';
import {Link} from 'react-router-dom';

function ButtonNormal(props) {
    return ( 
        <>
            <button className='ButtonNormal'>
                <Link to={`/${props.address}`}>{props.name}</Link>
            </button>
        </>
     );
}

export default ButtonNormal;