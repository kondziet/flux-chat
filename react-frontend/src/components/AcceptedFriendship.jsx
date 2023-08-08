const AcceptedFriendship = ({ requestId, senderUsername }) => {
  return (
    <div className="h-full w-full flex flex-col items-center justify-between bg-blue-300">
      <p className="text-2xl">Friend</p>
      <p>{requestId}</p>
      <p>{senderUsername}</p>
    </div>
  );
};

export default AcceptedFriendship;
