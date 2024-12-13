package roundrobinschedulingalgorithmsimulation;

// Importing classes
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;


// Class to represent a process
class Process {
    int processId;        // Process ID
    int arrivalTime;      // Arrival time of the process
    int burstTime;        // Burst time (execution time) of the process
    int remainingTime;    // Remaining time left to execute the process
    int waitingTime;      // Time the process spent waiting
    int turnaroundTime;   // Time from arrival to completion

    // Constructor to initialize process attributes
    public Process(int processId, int arrivalTime, int burstTime) {
        this.processId = processId;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime; // Initially, remaining time is equal to burst time
        this.waitingTime = 0;           // Initialize waiting time to 0
        this.turnaroundTime = 0;        // Initialize turnaround time to 0
    }
}


public class RoundRobinSchedulingAlgorithmSimulation {

    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);

        // Input number of processes
        System.out.println("Enter the number of processes: ");
        int n = sc.nextInt();

        List<Process> processes = new ArrayList<>(); // List to store processes

        // Input arrival time and burst time for each process
        for (int i = 0; i < n; i++) {
            System.out.println("Enter arrival time for process " + (i + 1) + ": ");
            int arrivalTime = sc.nextInt();
            System.out.println("Enter burst time for process " + (i + 1) + ": ");
            int burstTime = sc.nextInt();
            processes.add(new Process(i + 1, arrivalTime, burstTime));
        }

        // Input time quantum for Round Robin scheduling
        System.out.println("Enter the time quantum: ");
        int timeQuantum = sc.nextInt();

        // Sort processes by their arrival time
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        int time = 0;  // Current time
        Queue<Process> queue = new LinkedList<>();  // Queue to manage processes in Round Robin fashion

        // Run the Round Robin scheduling algorithm
        while (true) {
            boolean allDone = true; // Flag to check if all processes are completed

            // Add processes to the queue if they are ready and not completed
            for (Process p : processes) {
                if (p.remainingTime > 0) {
                    allDone = false; // If any process is not completed, set allDone to false
                    if (p.arrivalTime <= time) {
                        queue.add(p); // Add process to the queue if it has arrived
                    }
                }
            }

            if (allDone) {
                break;  // Exit the loop if all processes are completed
            }

            // Process the queue in Round Robin manner
            for (Iterator<Process> it = queue.iterator(); it.hasNext();) {
                Process p = it.next();
                if (p.remainingTime > timeQuantum) {
                    time += timeQuantum; // Execute the process for the time quantum
                    p.remainingTime -= timeQuantum; // Reduce the remaining time
                } else {
                    time += p.remainingTime; // Complete the process
                    p.turnaroundTime = time - p.arrivalTime; // Calculate turnaround time
                    p.waitingTime = p.turnaroundTime - p.burstTime; // Calculate waiting time
                    p.remainingTime = 0; // Process is completed
                }

                if (p.remainingTime == 0) {
                    it.remove(); // Remove the process from the queue if completed
                }
            }
        }

        // Output summary table of process details
        System.out.println("\nProcess ID | Arrival Time | Burst Time | Waiting Time | Turnaround Time");
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        for (Process p : processes) {
            System.out.println(p.processId + "\t\t" + p.arrivalTime + "\t\t" + p.burstTime + "\t\t" + p.waitingTime + "\t\t" + p.turnaroundTime);
            totalWaitingTime += p.waitingTime;       // Sum up waiting times
            totalTurnaroundTime += p.turnaroundTime; // Sum up turnaround times
        }

        // Calculate and display average waiting time and turnaround time
        double avgWaitingTime = (double) totalWaitingTime / n;
        double avgTurnaroundTime = (double) totalTurnaroundTime / n;
        System.out.printf("\nAverage Waiting Time: %.2f\n", avgWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", avgTurnaroundTime);

        sc.close(); // Close the scanner
    }
}