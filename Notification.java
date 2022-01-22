import java.util.ArrayList;

public class Notification {
    String message;
    ArrayList<Integer> reciever;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Integer> getReciever() {
        return reciever;
    }

    public void setReciever(ArrayList<Integer> reciever) {
        this.reciever = reciever;
    }

    public Notification(String message, ArrayList<Integer> reciever) {
        this.message = message;
        this.reciever = reciever;
    }
}
