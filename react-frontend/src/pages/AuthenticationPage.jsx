import AuthenticationForm from "../components/AuthenticationForm";
import logo from "../images/logo.png";

const AuthenticationPage = () => {
  return (
    <div className="flex flex-col justify-center min-h-full bg-gray-100">
      <div className="mx-auto w-full sm:max-w-md md:max-w-lg">
        <img src={logo} alt="logo" className="w-28 h-28 mx-auto" />
        <AuthenticationForm />
      </div>
    </div>
  );
};

export default AuthenticationPage;
