package Controller;

import Model.Account;
import Model.Message;

import Service.AccountService;
import Service.MessageService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::newAccountHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessageByAccountIDHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void newAccountHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        boolean accountTaken;
        if(accountService.getAccountLogin(account.getUsername(), account.getPassword()) != null){
            accountTaken = true;
        }
        else{
            accountTaken = false;
        }

        if(!accountTaken 
        && !account.getUsername().isEmpty()
        && account.getPassword().length() >= 4){
            Account addedAccount = accountService.addAccount(account);
            context.json(mapper.writeValueAsString(addedAccount));
        }
        else{
            context.status(400);
        }
    }

    private void loginHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account loginAccount = accountService.getAccountLogin(account.getUsername(), account.getPassword());
        
        if(loginAccount != null){
            context.json(mapper.writeValueAsString(loginAccount));
        }
        else{
            context.status(401);
        }
    }

    private void postMessageHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Account account = accountService.getAccountByID(message.getPosted_by());

        if(account != null){
            Message addedMessage = messageService.addMessage(message);
            if(addedMessage != null 
            && !addedMessage.getMessage_text().isEmpty() 
            && message.getMessage_text().length() <= 255){
                context.json(mapper.writeValueAsString(addedMessage));
            }
            else{
                context.status(400);
            }
        }
        else{
            context.status(400);
        }
    }

    public void getAllMessagesHandler(Context context){
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }

    public void getMessageByIDHandler(Context context){
        String messageIDString = context.pathParam("message_id");

        int messageID = Integer.parseInt(messageIDString);
        Message message = messageService.getMessageByID(messageID);

        if(message != null){
            context.json(message);
        }
        else{
            context.json("");
        }
    }

    public void deleteMessageHandler(Context context){
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = messageService.getMessageByID(message_id);
        boolean messageDeleted = messageService.deleteMessage(message_id);

        if(deletedMessage != null && messageDeleted){
            context.json(deletedMessage);
        }
        else{
            context.status(200);
        }
    }

    public void updateMessageHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        int message_id = Integer.parseInt(context.pathParam("message_id"));

        if(message.getMessage_text() != null && !message.getMessage_text().isEmpty()){
            Message updatedMessage = messageService.updateMessage(message_id, message);
            if(updatedMessage != null 
            && updatedMessage.getMessage_text().length() <= 255){
                context.json(mapper.writeValueAsString(updatedMessage));
            }
            else{
                context.status(400);
            }
        }
        else{
            context.status(400);
        }
    }

    private void getMessageByAccountIDHandler(Context context){
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesByAccountID(account_id);
        context.json(messages);
    }



}