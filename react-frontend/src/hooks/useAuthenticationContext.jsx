import AuthenticationContext from "../context/AuthenticationContext";
const { useContext } = require("react");

function useAuthenticationContext() {
    return useContext(AuthenticationContext);
}

export default useAuthenticationContext;