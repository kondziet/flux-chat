import { useEffect, useState } from "react";
import usePrivateClientRequest from "../hooks/usePrivateClientRequest";
import AcceptedFriendship from "../components/AcceptedFriendship";
import PendingFriendship from "../components/PendingFriendship";
import RequestedFriendship from "../components/RequestedFriendship";

const FriendsPage = () => {
  const [requestedFriendships, setRequestedFriendships] = useState([]);
  const [pendingFriendships, setPendingFriendships] = useState([]);
  const [acceptedFriendships, setAcceptedFriendships] = useState([]);
  const [friendshipRequestInput, setFriendshipRequestInput] = useState([]);
  const [errorMessage, setErrorMessage] = useState("");

  const privateClientRequest = usePrivateClientRequest();

  const handleFriendshipRequest = async () => {
    try {
      const response = await privateClientRequest.post(
        `/api/friendship/request/${friendshipRequestInput}`
      );
    } catch (error) {
      if (!error?.response) {
        setErrorMessage("No server response");
      } else if (error.response?.status === 400) {
        setErrorMessage("User with given username doesn't exist");
      } else if (error.response?.status === 409) {
        setErrorMessage("Conflict");
      }
    }
    setFriendshipRequestInput("");
  };

  useEffect(() => {
    let isMounted = true;
    const abortController = new AbortController();

    const cleanup = () => {
      isMounted = false;
      abortController.abort();
    };

    const fetchPendingFriendships = async () => {
      try {
        const response = await privateClientRequest.get(
          "/api/friendship/pending",
          {
            signal: abortController.signal,
          }
        );
        if (isMounted) {
          setPendingFriendships(response.data);
        }
      } catch (error) {
        console.error(error);
      }
    };

    const fetchRequestedFriendships = async () => {
      try {
        const response = await privateClientRequest.get(
          "/api/friendship/requested",
          {
            signal: abortController.signal,
          }
        );
        if (isMounted) {
          setRequestedFriendships(response.data);
        }
      } catch (error) {
        console.error(error);
      }
    };

    const fetchAcceptedFriendships = async () => {
      try {
        const response = await privateClientRequest.get(
          "/api/friendship/accepted",
          {
            signal: abortController.signal,
          }
        );
        if (isMounted) {
          setAcceptedFriendships(response.data);
        }
      } catch (error) {
        console.error(error);
      }
    };

    fetchRequestedFriendships();
    fetchPendingFriendships();
    fetchAcceptedFriendships();
    return cleanup;
  }, []);

  const renderedPendingFriendships = pendingFriendships.map(
    (requested, index) => (
      <PendingFriendship
        key={index}
        friendshipId={requested.friendshipId}
        senderUsername={requested.senderUsername}
      />
    )
  );

  const renderedRequestedFriendships = requestedFriendships.map(
    (requested, index) => (
      <RequestedFriendship
        key={index}
        friendshipId={requested.friendshipId}
        receiverUsername={requested.receiverUsername}
      />
    )
  );

  const renderedAcceptedFriendships = acceptedFriendships.map(
    (accepted, index) => (
      <AcceptedFriendship
        key={index}
        friendshipId={accepted.friendshipId}
        friendUsername={accepted.friendUsername}
      />
    )
  );

  return (
    <div>
      <h1>Friends Page</h1>
      <div>
        <input
          value={friendshipRequestInput}
          onChange={(event) => setFriendshipRequestInput(event.target.value)}
          type="text"
        />
        <button className="bg-gray-200" onClick={handleFriendshipRequest}>
          Send request
        </button>
        <p>{errorMessage}</p>
      </div>
      {renderedPendingFriendships}
      {renderedRequestedFriendships}
      {renderedAcceptedFriendships}
    </div>
  );
};

export default FriendsPage;
