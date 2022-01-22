
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        final String HOST = "127.0.0.1";
        final int PORT = 4567;
        System.out.println("You have successfully connected to the Auction site");
        displaymenu();
        try (
                Socket socket = new Socket(HOST, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Scanner in = new Scanner(socket.getInputStream());
                Scanner s = new Scanner(System.in);
        ) {
            while (true) {
                System.out.print("Your Choice: ");
                String input = s.nextLine();
                out.println(input);
                String serverInput = in.nextLine();
                if (serverInput=="menu"){
                    displaymenu();
                }else if(serverInput.contains("menu")){
                    String strOutput = serverInput.replaceFirst("menu", "");
                    System.out.println(strOutput);
                    displaymenu();
                }else{
                    System.out.println(serverInput);
                }
            }
        }
    }
    public static void displaymenu(){
        System.out.println( "-------------------------------------------");
        System.out.println( "| Choose one of the following operations: |");
        System.out.println( "| 1.List an item for sale                 |");
        System.out.println( "| 2.View all live auctions                |");
        System.out.println( "| 3.Bid on an auction                     |");
        System.out.println( "| 4.Manage your auctions                  |");
        System.out.println( "| 5.View Your Bids                        |");
        System.out.println( "| 6.Disconnect from server                |");
        System.out.println( "-------------------------------------------");
    }

}
