import { useCallback, useState } from "react";
import LoginForm from "./LoginForm";
import RegisterForm from "./RegisterForm";

const AuthenticationForm = () => {
  const [variant, setVariant] = useState("LOGIN");

  const toggleVariant = useCallback(() => {
    setVariant((previousVariant) =>
      previousVariant === "LOGIN" ? "REGISTER" : "LOGIN"
    );
  }, []);

  return (
    <div className="w-full sm:max-w-md md:max-w-lg">
      {variant === "LOGIN" ? (
        <LoginForm onToggle={toggleVariant} />
      ) : (
        <RegisterForm onToggle={toggleVariant} />
      )}
    </div>
  );
};

export default AuthenticationForm;
