import { createContext, useEffect, useState } from "react";

const AuthenticationContext = createContext();

const AuthenticationProvider = ({ children }) => {
  const [authentication, setAuthentication] = useState({});
  const [userAuthenticated, setUserAuthenticated] = useState(false);

  useEffect(() => {
    setUserAuthenticated(!!authentication.accessToken);
  }, [authentication]);

  return (
    <AuthenticationContext.Provider
      value={{ authentication, setAuthentication, userAuthenticated }}
    >
      {children}
    </AuthenticationContext.Provider>
  );
};

export { AuthenticationProvider };
export default AuthenticationContext;
