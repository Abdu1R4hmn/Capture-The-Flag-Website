import React from 'react';
import './Button.css';
import { Link } from 'react-router-dom';

function Button(props) {

    // Otherwise, render a normal button (for form submit)
    return (
        <button type={props.type || "button"} className="custom-btn">
            {props.name}
        </button>
    );
}

export default Button;