package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public List<Message> getAllMessagesByAccountID(int account_id) {
        return messageDAO.getAllMessagesByAccountID(account_id);
    }

    public Message getMessageByID(int id){
        return messageDAO.getMessageByID(id);
    }

    public Message addMessage(Message message) {
        Message persistedMessage = messageDAO.addMessage(message);
        return persistedMessage;
    }

    public boolean deleteMessage(int id){
        return messageDAO.deleteMessage(id);
    }

    public Message updateMessage(int id, Message message){
        Message updatedMessage = messageDAO.getMessageByID(id);

        if(updatedMessage != null){
            updatedMessage.setMessage_text(message.getMessage_text());
            return updatedMessage;
        }
        else{
            return null;
        }
    }
}
