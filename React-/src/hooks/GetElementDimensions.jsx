import { useRef, useLayoutEffect, useState } from 'react';

// GetElementDimensions is a utility component that measures the width/height of its child (kid)
// and applies a different class if the width exceeds a threshold (200px)
function GetElementDimensions({ kid }) {
    // Ref to the h1 element
    const ref = useRef(null);

    // State for width and height of the element
    const [width, setWidth] = useState(0);
    const [height, setHeight] = useState(0);

    // On mount, measure the element's dimensions
    useLayoutEffect(() => {
        setWidth(ref.current.offsetWidth);
        setHeight(ref.current.offsetHeight); // Fixed typo: changed to offsetHeight
    }, []);

    // If width is less than 200px, use 'nooverflow' class
    if (width < 200) {
        return (
            <div className={"card-Title nooverflow"}>
                <h1 ref={ref}>{kid}</h1>
                {/* <p>{width}</p> */}
            </div>
        );
    } else {
        // If width is 200px or more, use 'overflowing' class
        return (
            <div className={"card-Title overflowing"}>
                <h1 ref={ref}>
                    {kid}
                </h1>
                {/* <p>{width}</p> */}
            </div>
        );
    }
}

export default GetElementDimensions;
