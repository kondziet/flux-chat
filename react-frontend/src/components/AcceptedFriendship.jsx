const AcceptedFriendship = ({ friendshipId, friendUsername }) => {
  return (
    <div className="h-full w-full flex flex-col items-center justify-between bg-blue-300">
      <p className="text-2xl">Friend</p>
      <p>{friendshipId}</p>
      <p>{friendUsername}</p>
    </div>
  );
};

export default AcceptedFriendship;
