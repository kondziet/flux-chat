import axios from "axios";

const publicClientRequest = axios.create({
    baseURL: "http://localhost:8080"
});

const privateClientRequest = axios.create({
    baseURL: "http://localhost:8080",
    headers: { "Content-Type": "application/json" },
    withCredentials: true
});

export { privateClientRequest };
export default publicClientRequest;