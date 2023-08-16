package pl.kondziet.springbackend.repository;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.kondziet.springbackend.model.aggregation.FriendshipDetails;
import pl.kondziet.springbackend.model.entity.Friendship;
import pl.kondziet.springbackend.model.enumerable.FriendshipStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FriendshipRepository extends ReactiveMongoRepository<Friendship, String> {

    @Aggregation(pipeline = {
            "{'$match': {'receiverId': ?0, 'friendshipStatus':  ?1}}",
            "{'$addFields': {'senderIdObjectId': { '$toObjectId': '$senderId' }, 'receiverIdObjectId': { '$toObjectId': '$receiverId' }}}",
            "{'$lookup': {'from': 'user','localField': 'senderIdObjectId','foreignField': '_id','as': 'sender'}}",
            "{'$lookup': {'from': 'user','localField': 'receiverIdObjectId','foreignField': '_id','as': 'receiver'}}",
            "{'$unwind': '$sender'}",
            "{'$unwind': '$receiver'}",
            "{'$addFields': {'senderUsername': '$sender.username'}}",
            "{'$addFields': {'receiverUsername': '$receiver.username'}}",
            "{'$project': {'senderIdObjectId': 0, 'receiverIdObjectId':  0, 'sender': 0}}"
    })
    Flux<FriendshipDetails> findReceiverFriendships(String receiverId, FriendshipStatus friendshipStatus);
    @Aggregation(pipeline = {
            "{'$match': {'senderId': ?0, 'friendshipStatus':  ?1}}",
            "{'$addFields': {'senderIdObjectId': { '$toObjectId': '$senderId' }, 'receiverIdObjectId': { '$toObjectId': '$receiverId' }}}",
            "{'$lookup': {'from': 'user','localField': 'senderIdObjectId','foreignField': '_id','as': 'sender'}}",
            "{'$lookup': {'from': 'user','localField': 'receiverIdObjectId','foreignField': '_id','as': 'receiver'}}",
            "{'$unwind': '$sender'}",
            "{'$unwind': '$receiver'}",
            "{'$addFields': {'senderUsername': '$sender.username'}}",
            "{'$addFields': {'receiverUsername': '$receiver.username'}}",
            "{'$project': {'senderIdObjectId': 0, 'receiverIdObjectId':  0, 'sender': 0}}"
    })
    Flux<FriendshipDetails> findSenderFriendships(String senderId, FriendshipStatus friendshipStatus);

    @Aggregation(pipeline = {
            "{'$match': {'$or': [{'senderId': ?0}, {'receiverId': ?0}], 'friendshipStatus': ?1}}",
            "{'$addFields': {'senderIdObjectId': { '$toObjectId': '$senderId' }, 'receiverIdObjectId': { '$toObjectId': '$receiverId' }}}",
            "{'$lookup': {'from': 'user','localField': 'senderIdObjectId','foreignField': '_id','as': 'sender'}}",
            "{'$lookup': {'from': 'user','localField': 'receiverIdObjectId','foreignField': '_id','as': 'receiver'}}",
            "{'$unwind': '$sender'}",
            "{'$unwind': '$receiver'}",
            "{'$addFields': {'senderUsername': '$sender.username'}}",
            "{'$addFields': {'receiverUsername': '$receiver.username'}}",
            "{'$project': {'senderIdObjectId': 0, 'receiverIdObjectId':  0, 'sender': 0}}"
    })
    Flux<FriendshipDetails> findAllFriendshipsContainingUserId(String userId, FriendshipStatus friendshipStatus);
    Mono<Boolean> existsBySenderIdAndReceiverId(String senderId, String receiverId);
}
