import { useCallback, useEffect, useRef, useState } from "react";
import { AiOutlineCheck, AiOutlineClose } from "react-icons/ai";
import publicClientRequest from "../api/clientRequest";

const USERNAME_REGEX = /^[a-zA-Z0-9_]{3,20}$/;
const EMAIL_REGEX = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
const PASSWORD_REGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/;

const RegisterForm = ({ onToggle }) => {

  const usernameRef = useRef();
  const errorRef = useRef();

  const [errorMessage, setErrorMessage] = useState("");
  const [success, setSuccess] = useState(false);

  const [usernameInput, setUsernameInput] = useState("");
  const [validUsername, setValidUsername] = useState(false);
  const [usernameFocus, setUsernameFocus] = useState(false);

  const [emailInput, setEmailInput] = useState("");
  const [validEmail, setValidEmail] = useState(false);
  const [emailFocus, setEmailFocus] = useState(false);

  const [passwordInput, setPasswordInput] = useState("");
  const [validPassword, setValidPassword] = useState(false);
  const [passwordFocus, setPasswordFocus] = useState(false);

  const [passwordConfirmInput, setPasswordConfirmInput] = useState("");
  const [validPasswordConfirm, setValidPasswordConfirm] = useState(false);
  const [passwordConfirmFocus, setPasswordConfirmFocus] = useState(false);

  useEffect(() => {
    const validationResult = USERNAME_REGEX.test(usernameInput);
    setValidUsername(validationResult);
  }, [usernameInput]);

  useEffect(() => {
    const validationResult = EMAIL_REGEX.test(emailInput);
    setValidEmail(validationResult);
  }, [emailInput]);

  useEffect(() => {
    const validationResult = PASSWORD_REGEX.test(passwordInput);
    setValidPassword(validationResult);
    const passwordConfirmMatch = (passwordInput !== "" && passwordConfirmInput !== "") && passwordInput === passwordConfirmInput;
    setValidPasswordConfirm(passwordConfirmMatch);
  }, [passwordInput, passwordConfirmInput]);
  
  const handleRegisterSubmit = async (event) => {
    event.preventDefault();

    try {
      const response = await publicClientRequest.post(
          "/api/authentication/register",
          {
            username: usernameInput,
            email: emailInput,
            password: passwordInput
          }
      );

    } catch (error) {
      if (!error?.response) {
          setErrorMessage("No server response");
      } else if (error.response?.status === 409) {
          setErrorMessage("User with given Username or E-mail already exists")
      }
    }
  };

  return (
    <div className="mx-auto w-full max-w-sm sm:max-w-md md:max-w-lg">
      <h2 className="mt-6 text-center tracking-tight text-gray-900 text-3xl font-serif font-bold">
        Create your account
      </h2>
      <form className="bg-white rounded-lg shadow-md p-6 m-6 md:p-8 md:m-8 lg:p-10 lg:m-10" onSubmit={handleRegisterSubmit}>
        <div className={errorMessage && (!usernameFocus && !emailFocus && !passwordFocus && !passwordConfirmFocus) ? "flex items-center justify-center bg-red-200 rounded shadow p-2 mb-4" : "hidden"}>{errorMessage}</div>
        <div className="mb-8 sm:mb-10">
          <div className="flex items-center gap-2 text-gray-700 text-md font-bold mb-2">
            Username
            <AiOutlineCheck className={validUsername ? "" : "hidden"} size={20} />
            <AiOutlineClose className={!validUsername && usernameInput ? "" : "hidden"} size={20} />
          </div>
          <input className="shadow-sm border rounded w-full py-2 px-3 text-gray-700 leading-tight" value={usernameInput} onChange={(event) => setUsernameInput(event.target.value)} onFocus={() => setUsernameFocus(true)} onBlur={() => setUsernameFocus(false)} ref={usernameRef} type="text" placeholder="Username" required />
          <div className={usernameFocus && !validUsername ? "bg-gray-200 rounded shadow p-2 mt-2" : "hidden"}>
            3 to 20 characters long. Only letters, numbers, and underscores are allowed.
          </div>
          {/* <div className={usernameFocus && !validUsername ? "" : "hidden"}>already in use</div> */}
        </div>
        <div className="mb-8 sm:mb-10">
          <div className="flex items-center gap-2 text-gray-700 text-md font-bold mb-2">
            E-mail
            <AiOutlineCheck className={validEmail ? "" : "hidden"} size={20} />
            <AiOutlineClose className={!validEmail && emailInput ? "" : "hidden"} size={20} />
          </div>
          <input className="shadow-sm border rounded w-full py-2 px-3 text-gray-700 leading-tight" value={emailInput} onChange={(event) => setEmailInput(event.target.value)} onFocus={() => setEmailFocus(true)} onBlur={() => setEmailFocus(false)} type="text" placeholder="E-mail" required />
          <div className={emailFocus && !validEmail ? "bg-gray-200 rounded shadow p-2 mt-2" : "hidden"}>
            The email format should be in the form of user@example.com
          </div>
        </div>
        <div className="mb-8 sm:mb-10">
          <div className="flex items-center gap-2 text-gray-700 text-md font-bold mb-2">
            Password
            <AiOutlineCheck className={validPassword ? "" : "hidden"} size={20} />
            <AiOutlineClose className={!validPassword && passwordInput ? "" : "hidden"} size={20} />
          </div>
          <input className="shadow-sm border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight" value={passwordInput} onChange={(event) => setPasswordInput(event.target.value)} onFocus={() => setPasswordFocus(true)} onBlur={() => setPasswordFocus(false)} type="password" placeholder="********" required />
          <div className={passwordFocus && !validPassword ? "bg-gray-200 rounded shadow p-2 mt-2" : "hidden"}>
            Must be at least 8 characters long. Must contain at least one lowercase letter, one uppercase letter, and one digit. Can include letters (both uppercase and lowercase) and numbers.
          </div>
        </div>
        <div className="mb-10 sm:mb-12">
          <div className="flex items-center gap-2 text-gray-700 text-md font-bold mb-2">
            Confirm password
            <AiOutlineCheck className={validPasswordConfirm ? "" : "hidden"} size={20} />
            <AiOutlineClose className={!validPasswordConfirm && passwordConfirmInput ? "" : "hidden"} size={20} />
          </div>
          <input className="shadow-sm border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight" value={passwordConfirmInput} onChange={(event) => setPasswordConfirmInput(event.target.value)} onFocus={() => setPasswordConfirmFocus(true)} onBlur={() => setPasswordConfirmFocus(false)} type="password" placeholder="********" required />
          <div className={passwordConfirmFocus && !validPasswordConfirm ? "bg-gray-200 rounded shadow p-2 mt-2" : "hidden"}>
            The password should match the one you entered earlier
          </div>
        </div>
        <div className="flex flex-col">
          <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 mb-6 rounded shadow" disabled={!validUsername || !validEmail || !validPassword || !validPasswordConfirm} type="submit">
            Create account
          </button>
          <p className="cursor-pointer font-semibold text-md text-center text-blue-500 hover:text-blue-800" onClick={onToggle}>
            Log in
          </p>
        </div>
      </form>
    </div>
  );
};

export default RegisterForm;
