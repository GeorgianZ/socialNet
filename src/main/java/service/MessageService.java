package service;

import domain.Message;
import domain.User;
import repository.database.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class MessageService {
    private Repository<Long, Message> repo;

    public MessageService(Repository<Long, Message> repo) {
        this.repo = repo;
    }

    public Message addMessage(Message message){
        repo.save(message);
        return message;
    }

    public List<Message> getAll(){
        return (List<Message>) repo.getAll();
    }

    public List<Message> getMessagesForUsers(User u1, User u2){
        List<Message> messages = new ArrayList<>();

        messages = repo.findByUsers(u1.getId(), u2.getId());
        return messages;
    }

    public List<Message> getMessagesInOrder(User u1, User u2){
        List<Message> messages = getMessagesForUsers(u1, u2);
        if(messages != null) {
            messages.sort(
                    Comparator.comparing(Message::getDate));
        }
        return messages;
    }

    
}
