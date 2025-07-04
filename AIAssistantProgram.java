import java.util.*;
import java.time.LocalDateTime;

enum CommandType 
{
    MUSIC,      //predefined values for subclass
    FITNESS,
}

//USER Profile Class
class UserProfile 
{
    private String name;    
    private int age;
    private Map<String, String> preferences; //using map for this
    private boolean isPremium;

    public UserProfile(String name, int age, Map<String, String> preferences, boolean isPremium) 
    {
        if (name == null || name.isEmpty()) //check name isn't empty
        {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (age <= 0) //age must be valid
        {
            throw new IllegalArgumentException("Age must be positive.");
        }
        this.name = name;
        this.age = age;
        this.preferences = preferences != null ? preferences : new HashMap<>(); //always ensure map is valid
        this.isPremium = isPremium;
    }

    public String getName() 
    {
        return name; 
    }
    public int getAge() 
    { 
        return age; 
    }
    public Map<String, String> getPreferences() 
    { 
        return preferences; 
    }
    public boolean isPremium() 
    { 
        return isPremium; 
    }
}

//Request Class
class Request 
{
    private String inputText;
    private LocalDateTime timestamp;
    private CommandType commandType;

    public Request(String inputText, LocalDateTime timestamp, CommandType commandType) 
    {
        if (inputText == null || inputText.isEmpty()) 
        {
            throw new IllegalArgumentException("Input cannot be empty."); //ensure nothing is empty
        }
        if (timestamp == null) 
        {
            throw new IllegalArgumentException("Timestamp cannot be null."); 
        }
        if (commandType == null) 
        {
            throw new IllegalArgumentException("Command type cannot be null.");
        }
        this.inputText = inputText;
        this.timestamp = timestamp;
        this.commandType = commandType;
    }

    public String getInputText() 
    { 
        return inputText; 
    }
    public LocalDateTime getTimestamp() 
    { 
        return timestamp; 
    }
    public CommandType getCommandType() 
    { 
        return commandType; 
    }
}

//Response Class
class Response 
{
    private String message;
    private float confidence;
    private boolean actionPerformed;

    public Response(String message, float confidence, boolean actionPerformed) 
    {
        if (message == null || message.isEmpty())
        {
            throw new IllegalArgumentException("Message cannot be empty."); //ensures AI responses 
        }
        if (confidence < 0 || confidence > 1) 
        {
            throw new IllegalArgumentException("Confidence must be between 0 and 1.");
        }
        this.message = message;
        this.confidence = confidence;
        this.actionPerformed = actionPerformed;
    }

    public String getMessage() 
    { 
        return message; 
    }
    public float getConfidence() 
    { 
        return confidence; 
    }
    public boolean isActionPerformed() 
    { 
        return actionPerformed; 
    }
}

//AIAssistant Class 
class AIAssistant 
{
    protected String name;

    public AIAssistant(String name) 
    {
        this.name = name;   //used to return which assistant is used
    }

    public String greetUser(UserProfile user) 
    {
        return "Welcome " + user.getName() + "! How can I be of service today?";
    }

    public Response handleRequest(UserProfile user, Request request) 
    {
        return generateResponse("I'm not to sure what you're asking.", 0.1f, false); 
        //specialized later by subclass
    }

    public Response generateResponse(String message, float confidence, boolean actionPerformed) 
    {
        return new Response(message, confidence, actionPerformed);
    }
}

//Subclass 1. MusicAssistant
class MusicAssistant extends AIAssistant 
{
    public MusicAssistant() 
    {
        super("MusicAssistantAI");
    }

    @Override
    public Response handleRequest(UserProfile user, Request request) //override handle request if music is selected
    {
        if (request.getCommandType() == CommandType.MUSIC) 
        {
            return recommendPlaylist(user);
        }
        return super.handleRequest(user, request);
    }

    public Response recommendPlaylist(UserProfile user) 
    {
        String mood = user.getPreferences().getOrDefault("mood", "average"); //base value
        String playlist = "OK, generating a playlist based on a " + mood + " mood."; //AI response
        return generateResponse(playlist, 0.88f, true);
    }
}

// FitnessAssistant subclass
class FitnessAssistant extends AIAssistant //same thing override handle request if fitness is selected
{
    public FitnessAssistant() 
    {
        super("FitnessAssistantAI");
    }

    @Override
    public Response handleRequest(UserProfile user, Request request) 
    {
        if (request.getCommandType() == CommandType.FITNESS) 
        {
            return suggestWorkout(user);
        }
        return super.handleRequest(user, request);
    }

    public Response suggestWorkout(UserProfile user) 
    {
        String goal = user.getPreferences().getOrDefault("goal", "general fitness"); //base value
        String workout = "OK based on the goal you entered of " + goal + " , here is your recommended workout."; //AI response
        return generateResponse(workout, 0.88f, true);
    }
}

// Main simulation class
public class AIAssistantProgram
{
    public static void main(String[] args) 
    {
        //TestCases
        Map<String, String> pref1 = new HashMap<>();
        pref1.put("mood", "sad");
        UserProfile user1 = new UserProfile("John", 18, pref1, true);

        Map<String, String> prefs2 = new HashMap<>();
        prefs2.put("goal", "weight loss");
        UserProfile user2 = new UserProfile("Becky", 22, prefs2, false);
        
        //User Requests
        Request req1 = new Request("Play some music", LocalDateTime.now(), CommandType.MUSIC);
        Request req2 = new Request("Suggest a workout", LocalDateTime.now(), CommandType.FITNESS);
        AIAssistant[] assistants = 
        {
            new MusicAssistant(),
            new FitnessAssistant(),
        };

        UserProfile[] users = {user1, user2};
        Request[] requests = {req1, req2};
        //Loop Through Array to Print Info for each user and request
        for (int i = 0; i < assistants.length; i++) 
        {
            AIAssistant assistant = assistants[i];
            UserProfile user = users[i];
            Request request = requests[i];
            System.out.println(assistant.name);
            System.out.println(assistant.greetUser(user));
            Response response = assistant.handleRequest(user, request);
            System.out.println("Response: " + response.getMessage());
            System.out.println("Confidence Percentage: " + response.getConfidence() + "%");
            System.out.println("Action Performed: " + response.isActionPerformed());
            System.out.println("User Premium Status: " + (user.isPremium() ? "Premium Member" : "Standard Member"));
            System.out.println("Request Timestamp: " + request.getTimestamp());
            System.out.println();
        }
    }
}
