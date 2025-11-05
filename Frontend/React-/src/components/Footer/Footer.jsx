import React from 'react';
import './footer.css'

function Footer() {
    return ( 
        <>
            <div className="Footer-Section">

                <div className="profile-pic">
                    <img src="/images/PP.png"/>
                    
                    <div className="profile-subtext">
                        <h4>Developed By:</h4>
                        <p> Abdulrahman Turky</p>
                    </div>
                </div>

                <div className="contact-us">
                    <h3>Contacts:</h3>
                    <h4><span>Email: </span> Hello@gmail.com</h4>
                    <h4><span>Phone: </span> +123-123-123</h4>
                </div>

                <div className="links">
                    <h3>Links:</h3>

                    <a href="#linkedin"><img src="/images/linkedin_footer.png"/></a>
                    <a href="#github"><img src="/images/github_Footer.png"/></a>

                </div>
            </div>

        </>
     );
}

export default Footer;