const RequestedFriendship = ({ friendshipId, receiverUsername }) => {
    return (
      <div className="h-full w-full flex flex-col items-center justify-between bg-blue-300">
        <p className="text-2xl">Requested</p>
        <p>{friendshipId}</p>
        <p>{receiverUsername}</p>
      </div>
    );
  };
  
  export default RequestedFriendship;