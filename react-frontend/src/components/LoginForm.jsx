import { useCallback, useState } from "react";
import publicClientRequest from "../api/clientRequest";
import useAuthenticationContext from "../hooks/useAuthenticationContext";

const LoginForm = ({ onToggle }) => {
  const [emailInput, setEmailInput] = useState("");
  const [emailFocus, setEmailFocus] = useState("");

  const [passwordInput, setPasswordInput] = useState("");
  const [passwordFocus, setPasswordFocus] = useState("");

  const [errorMessage, setErrorMessage] = useState("");

  const { setAuthentication } = useAuthenticationContext();

  const handleLoginSubmit = async (event) => {
    event.preventDefault();

    try {
      const response = await publicClientRequest.post(
          "/api/authentication/login",
          {
            email: emailInput,
            password: passwordInput
          }
      );
      const accessToken = response.data.token;
      setAuthentication({ accessToken });

      setEmailInput("");
      setPasswordInput("");

      console.log(accessToken);
    } catch (error) {
      if (!error?.response) {
          setErrorMessage("No server response");
      } else if (error.response?.status === 400) {
          setErrorMessage("Missing email or password")
      } else if (error.response?.status === 403) {
          setErrorMessage("Invalid email or password")
      }
    }
  };

  return (
    <div className="mx-auto w-full max-w-sm sm:max-w-md md:max-w-lg">
      <h2 className="mt-6 text-center tracking-tight text-gray-900 text-3xl font-serif font-bold">
        Log in to your account
      </h2>
      <form className="bg-white rounded-lg shadow-md p-6 m-6 md:p-8 md:m-8 lg:p-10 lg:m-10" onSubmit={handleLoginSubmit}>
        <div className={errorMessage && (!emailFocus && !passwordFocus) ? "flex items-center justify-center bg-red-200 rounded shadow p-2 mb-4" : "hidden"}>{errorMessage}</div>
        <div className="mb-10">
          <div className="block text-gray-700 text-md font-bold mb-2">E-mail</div>
          <input className="shadow-sm border rounded w-full py-2 px-3 text-gray-700 leading-tight" value={emailInput} onChange={(event) => setEmailInput(event.target.value)} onFocus={() => setEmailFocus(true)} onBlur={() => setEmailFocus(false)} type="text" placeholder="E-mail" required />
        </div>
        <div className="mb-10">
          <div className="block text-gray-700 text-md font-bold mb-2">Password</div>
          <input className="shadow-sm border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight" value={passwordInput} onChange={(event) => setPasswordInput(event.target.value)} onFocus={() => setPasswordFocus(true)} onBlur={() => setPasswordFocus(false)} type="password" placeholder="********" required />
        </div>
        <div className="flex flex-col">
          <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 mb-6 rounded shadow" type="submit">Log in</button>
          <p className="cursor-pointer block font-semibold text-md text-center text-blue-500 hover:text-blue-800" onClick={onToggle}>Create account</p>
        </div>
      </form>
    </div>
  );
};

export default LoginForm;
