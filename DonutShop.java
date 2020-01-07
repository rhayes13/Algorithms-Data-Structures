/*  Name: Ryan Hayes
 *  COSC 311  FA19
 *  (#1) pp1008
 *  URL:  <https://github.com/rybo-hub/skooldump>
 */

package donutshop;
        
import java.util.*;

public class DonutShop {
    public class Customer {
        int serviceTime;
        int waitTime;
        public Customer(int s){
            serviceTime = s;
            waitTime = 0;
        }
        public int getServiceTime(){
            return serviceTime;
        }
        public void incrementWaitTime(){
            waitTime++;
        }
        public int getWaitTime(){
            return waitTime;
        }
    }
    
    public class Server {
        boolean busy;
        int serviceRemaining;
        int customerWaitTime;

        public Server() {
            busy = false;
            serviceRemaining = 0;
            customerWaitTime = 0;
        }
        public boolean isBusy() {
            return busy;
        }
        public void setCustomer(Customer c) {
            serviceRemaining = c.getServiceTime();
            customerWaitTime = c.getWaitTime();
            busy = true;
        }
        public void decServiceRemaining() {
            serviceRemaining--;
            if(serviceRemaining == 0) {
                busy = false;
            }
        }
        public void releaseServer() {
            if(serviceRemaining == 0) {
                busy = false;
            }
        }
        public int getCustomersWaitTime() {
            return customerWaitTime;
        }
    }
    
    // DonutShop class variables
    int ticks = 0;
    int ticksPerMinute = 1;
    Queue<Customer> q = new LinkedList<>();
    Random r = new Random(97);
    int numCusts, completed = 0;
    int min, max = 0; // wait times
    int totalWait1, tw2, tw3 = 0; //totalWait & temp wait times
    Server[] servers;

    // Parameters
    int numServers;
    int maxServiceReq;
    double poissonMean;

    public DonutShop(int srvrs, int maxTime, double arrivalMean) {
        numServers = srvrs;
        maxServiceReq = maxTime;
        poissonMean = arrivalMean;
        servers = new Server[numServers];
        for (int i = 0; i < numServers; i++) {
            servers[i] = new Server();
        }
    }

    public int getPoissonRandom(double mean) {
        double L = Math.exp(-mean);
        int k = 0;
        double p = 1.0;
        do {
            p = p * r.nextDouble();
            k++;
        } while (p > L);
        return k - 1;
    }

    public void addCustomers() {
        int arriving = getPoissonRandom(poissonMean);
        numCusts += arriving;
        for (int i = 0; i < arriving; i++) {
            q.add(new Customer(r.nextInt(maxServiceReq) + 1));
        }
    }

    public void assignServers() {
        tw2 = 0; // wait time for inService customer returned to 0
        for (int i = 0; i < servers.length; i++) {
            if(!servers[i].isBusy()) {
                // add to total wait time
                totalWait1 += servers[i].getCustomersWaitTime();
                if(!q.isEmpty()) {
                    Customer temp = q.remove();
                    completed++;
                    tw2 += temp.getWaitTime();
                    servers[i].setCustomer(temp);
                }
            } else {
                servers[i].decServiceRemaining();
            }
        }
    }

    public void releaseCustomer() {
        for (int i = 0; i < servers.length; i++) {
            servers[i].releaseServer();
        }
    }

    public void incWaitTime() {
        tw3 = 0; // wait time for customers in queue returned to 0

        // increments wait time for each customer in the queue
        for (int i = 0; i < q.size(); i++) {
            Customer temp = q.remove();
            temp.incrementWaitTime();

            //update min and max wait times
            if (temp.getWaitTime() > max) {
                max = temp.getWaitTime();
            }
            if (temp.getWaitTime() < min) {
                min = temp.getWaitTime();
            }

            tw3 += temp.getWaitTime();
            q.add(temp);
        }
    }

    public int getAvgWaitTime() {
        if(numCusts == 0) {
            return 0;
        } else {
            return (totalWait1 + tw2 + tw3)/numCusts;
        }
    }

    public int getNumInService() {
        int num = 0;
        for (int i = 0; i < servers.length; i++) {
            if (servers[i].isBusy()) {
                num++;
            }
        }
        return num;
    }

    public void print() {
        System.out.println("Tick #: " + ticks);
        System.out.println("# Customers in service: " + getNumInService());
        System.out.println("# Customers with completed service: " + completed);
        System.out.println("# Customers in queue: " + q.size());
        System.out.println("Total wait time: " + 
                (totalWait1 + tw2 + tw3)/ticksPerMinute + " minutes");
        System.out.println("Wait time: (min: " + min/ticksPerMinute + 
                ", average: " + (double)getAvgWaitTime()/ticksPerMinute + 
                ", max: " + max/ticksPerMinute + ")");
        System.out.println("*********************************************");
    }

    public void run(int simTime) {
        simTime = simTime * ticksPerMinute;
        System.out.println("*********************************************");
        System.out.println("[Simulation] Servers: " + numServers + ", Max service requirement: " + maxServiceReq + ", Poisson customer arrivals: " + poissonMean);
        for (int i = 0; i < simTime; i++) {
            releaseCustomer();
            incWaitTime();
            addCustomers();
            assignServers();
            ticks++;
            print();
        }
    }

    public static void main(String[] args) {

        DonutShop sim1 = new DonutShop(1, 12, 2);
        sim1.run(20);
        System.out.println("[Simulation Complete: One server, heavy demand]");

        DonutShop sim2 = new DonutShop(1, 3, .25);
        sim2.run(20);
        System.out.println("[Simulation Complete: One server, low demand]");

        DonutShop sim3 = new DonutShop(2, 12, 2);
        sim3.run(20);
        System.out.println("[Simulation Complete: Two servers, heavy demand]");

        DonutShop sim4 = new DonutShop(2, 3, .25);
        sim4.run(20);
        System.out.println("[Simulation Complete: Two servers, low demand]");

        DonutShop sim5 = new DonutShop(4, 12, 2);
        sim5.run(20);
        System.out.println("[Simulation Complete: Four servers, heavy demand]");

        DonutShop sim6 = new DonutShop(4, 3, .25);
        sim6.run(20);
        System.out.println("[Simulation Complete: Four servers, low demand]");

        DonutShop sim7 = new DonutShop(8, 12, 2);
        sim7.run(20);
        System.out.println("[Simulation Complete: Eight servers, heavy demand]");

        DonutShop sim8 = new DonutShop(8, 3, .25);
        sim8.run(20);
        System.out.println("[Simulation Complete: Eight servers, low demand]");

        DonutShop simFinal = new DonutShop(4, 3, 2);
        simFinal.run(20);
        System.out.println("[Simulation Complete: Four servers, 2 arrivals/min, 1-3 minute service time]");
    }
}
