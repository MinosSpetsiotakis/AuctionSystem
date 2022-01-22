import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;
import java.util.ArrayList;


public class Server {
    static AuctionTimer auctionTimer;
    public static void main(String[] args) throws IOException {
        int threadnum = 0;
        final int PORT = 4567;
        ArrayList<Auction> liveAuctions = new ArrayList<Auction>();
        ArrayList<Notification> notifications = new ArrayList<Notification>();
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Auction site started...");
        System.out.println("Wating for clients...");

        while (true) {

            Socket clientSocket = serverSocket.accept();
            int threadID = threadnum;
            threadnum = threadnum+1;
            Thread t = new Thread(String.valueOf(threadID)) {
                public void run() {

                    try (
                            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                            Scanner in = new Scanner(clientSocket.getInputStream());
                    ) {
                            String input = in.nextLine();
                            while(input!="6") {
                                System.out.println("Received option from client " + threadID + ": " + input);
                                for(int i=0; i<notifications.size();i++){
                                    if(notifications.get(i).reciever.contains(threadID)){
                                        out.println("Bid Notification ! ->"+notifications.get(i).message+" Press 1 to continue!");
                                        int conf = in.nextInt();
                                        notifications.get(i).reciever.remove(i);
                                    }
                                }
                                switch (input) {
                                    case "1": {
                                        String itemName = "";
                                        int itemPrice = 0;
                                        int auctionTime = 0;
                                        out.println("Insert the items name: ");
                                        itemName = in.nextLine();
                                        out.println("Now Insert the items minimum price: ");
                                        itemPrice = Integer.valueOf(in.nextLine());
                                        out.println("Now Insert how long the auction will go (in seconds): ");
                                        auctionTime = Integer.valueOf(in.nextLine());
                                        out.println("Thank you menu");
                                        Auction auction = new Auction();
                                        auction.setSellerName(String.valueOf(threadID));
                                        auction.setItemName(itemName);
                                        auction.setMinBid(itemPrice);
                                        auction.setHighestBid(0);
                                        auction.setStatus(true);
                                        auction.setAuctionTime(auctionTime);
                                        liveAuctions.add(auction);
                                        AuctionTimer.Timer(auction.getAuctionTime()*1000);
                                        break;
                                    }
                                    case "2": {
                                        String result ="List of active auctions:";
                                        for (int i = 0; i < liveAuctions.size(); i++) {
                                            if(liveAuctions.get(i).isStatus()) {
                                                result += "id: " + i + " name: " + liveAuctions.get(i).getItemName() + " Highest Bid: " + liveAuctions.get(i).getHighestBid();

                                            }else{
                                             }
                                        }
                                        out.println(result+" menu");
                                        break;
                                    }
                                    case "3": {
                                        int bid = 0;
                                        int selection = 0;
                                        String result ="Insert Your selection from the following:";
                                        for (int i = 0; i < liveAuctions.size(); i++) {
                                            if(liveAuctions.get(i).isStatus()) {
                                                result += "id: " + i + " name: " + liveAuctions.get(i).getItemName() + " Highest Bid: " + liveAuctions.get(i).getHighestBid();
                                            }
                                        }
                                        out.println(result);
                                        String tempselection = in.nextLine();
                                        selection = Integer.valueOf(tempselection);
                                        if(Objects.equals(liveAuctions.get(selection).getSellerName(), String.valueOf(threadID))){
                                            out.println("You can not bid on an auction you have posted! menu");
                                        }else {
                                            out.println("Insert Your bid");
                                            String tempbid = in.nextLine();
                                            bid = Integer.valueOf(tempbid);
                                            out.println("Do you confirm this bid?(insert 1 to confirm and 0 to deny) -> Your bid: " + bid);
                                            int response = in.nextInt();
                                            if (response == 1) {
                                                if (bid > liveAuctions.get(selection).getHighestBid()) {
                                                    liveAuctions.get(selection).setHighestBid(bid);
                                                    liveAuctions.get(selection).setBidderName(String.valueOf(threadID));
                                                    liveAuctions.get(selection).bidders.add(threadID);
                                                    for(int i=0;i<liveAuctions.get(selection).bidders.size(); i++){
                                                        Notification not = new Notification("New Bid in auction for the item :"+liveAuctions.get(selection).getItemName(), liveAuctions.get(selection).bidders);
                                                        notifications.add(not);
                                                    }

                                                    out.println("You are now the highest bidder ! menu");
                                                } else {
                                                    out.println("Your bid was insufficient. Goodbye");
                                                }
                                            } else if (response == 0) {
                                                out.println("Bid cancelled!");
                                            } else {
                                                while (response != 1 || response != 0) {
                                                    out.println("Input incorrect -> Do you confirm this bid?(insert 1 to confirm and 0 to deny) -> Your bid: " + bid);
                                                    response = in.nextInt();
                                                }
                                            }
                                            break;
                                        }
                                    }
                                    case "4":{
                                        String stringComp ="";
                                        for (int i = 0; i < liveAuctions.size(); i++) {
                                            if(liveAuctions.get(i).isStatus()) {
                                                if(Objects.equals(liveAuctions.get(i).getSellerName(), String.valueOf(threadID))){
                                                    stringComp += "id: " + i + " name: " + liveAuctions.get(i).getItemName() + " Highest Bid: " + liveAuctions.get(i).getHighestBid();
                                                }
                                              }
                                        }
                                        out.println("Choose an auction id to Manage "+stringComp);
                                        String tempbid = in.nextLine();
                                        int aid = Integer.valueOf(tempbid);
                                        out.println("To close the option please type -> close");
                                        if(in.nextLine()=="close"){
                                            liveAuctions.get(aid).setStatus(false);
                                        }
                                        out.println("The auction has officially closed with highest bid: "+ liveAuctions.get(aid).getHighestBid()+"  menu");
                                        liveAuctions.remove(aid);
                                    }
                                    case "5":{
                                        String stringComp ="";
                                        for (int i = 0; i < liveAuctions.size(); i++) {
                                            if(liveAuctions.get(i).isStatus()) {
                                                if(Objects.equals(liveAuctions.get(i).getBidderName(), String.valueOf(threadID))){
                                                    if(liveAuctions.get(i).isStatus()){
                                                        stringComp += "LIVE -> id: " + i + " name: " + liveAuctions.get(i).getItemName() + " Highest Bid: " + liveAuctions.get(i).getHighestBid();
                                                    }else{
                                                        stringComp += "WON -> id: " + i + " name: " + liveAuctions.get(i).getItemName() + " Highest Bid: " + liveAuctions.get(i).getHighestBid();
                                                    }
                                                }
                                            }
                                        }
                                        out.println("The auctions that follow are the ones you are bidding on -> "+stringComp +" menu");
                                    }
                                }
                                input = in.nextLine();
                            }
                    } catch (IOException e) { }
                }
            };
            t.start();
        }
    }
}