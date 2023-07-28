package pl.kondziet.springbackend.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kondziet.springbackend.repository.MessageRepository;
import pl.kondziet.springbackend.service.MessageService;

@AllArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
}
