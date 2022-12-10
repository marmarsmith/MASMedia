import { useHistory } from "react-router-dom";

function Error() {
    const history = useHistory();
    return (
        <>
            <h2>Error</h2>
            <div className="alert alert-danger">{history.location.state}</div>
        </>
    );
}

export default Error;