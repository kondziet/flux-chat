import { useEffect } from "react";
import useAuthenticationContext from "./useAuthenticationContext";
import { privateClientRequest } from "../api/clientRequest";

const usePrivateClientRequest = () => {
  const { authentication } = useAuthenticationContext();

  useEffect(() => {
    const requestIntercept = privateClientRequest.interceptors.request.use(
      (config) => {
        if (!config.headers.Authorization) {
          config.headers.Authorization = `Bearer ${authentication?.accessToken}`;
        }
        return config;
      },
      (error) => Promise.reject(error)
    );

    const cleanup = () => {
      privateClientRequest.interceptors.request.eject(requestIntercept);
    };

    return cleanup;
  }, [authentication]);

  return privateClientRequest;
};

export default usePrivateClientRequest;