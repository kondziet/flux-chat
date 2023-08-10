import { useState } from "react";
import usePrivateClientRequest from "../hooks/usePrivateClientRequest";

const PendingFriendship = ({ friendshipId, senderUsername }) => {
  const [accepted, setAccepted] = useState(false);
  const [declined, setDeclined] = useState(false);

  const privateClientRequest = usePrivateClientRequest();

  const handleFriendshipAccept = async () => {
    try {
      const response = await privateClientRequest.post(
        `/api/friendship/accept/${friendshipId}`
      );
    } catch (error) {
      console.log(error);
    }
  };

  const handleFriendshipDecline = async () => {
    try {
      const response = await privateClientRequest.post(
        `/api/friendship/decline/${friendshipId}`
      );
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div className="h-full w-full flex flex-col items-center justify-between bg-blue-300">
      <p className="text-2xl">Friend request</p>
      <p>{friendshipId}</p>
      <p>{senderUsername}</p>
      <button onClick={handleFriendshipAccept}>Accept</button>
      <button onClick={handleFriendshipDecline}>Decline</button>
    </div>
  );
};

export default PendingFriendship;
