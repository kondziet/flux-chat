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
            "{'$addFields': {'senderIdObjectId': { '$toObjectId': '$senderId' }}}",
            "{'$lookup': {'from': 'user','localField': 'senderIdObjectId','foreignField': '_id','as': 'sender'}}",
            "{'$unwind': '$sender'}",
            "{'$addFields': {'senderUsername': '$sender.username'}}",
            "{'$project': {'senderIdObjectId': 0, 'sender': 0}}"
    })
    Flux<FriendshipDetails> findReceiverFriendshipDetails(String receiverId, FriendshipStatus friendshipStatus);
    Mono<Boolean> existsBySenderIdAndReceiverId(String senderId, String receiverId);
}
