import { Link } from "react-router-dom";

const HomePage = () => {
  return (
    <div>
      <h1>Home page</h1>
      <Link to={"/authentication"}>
        <h1>Authentication page</h1>
      </Link>
    </div>
  );
};

export default HomePage;
