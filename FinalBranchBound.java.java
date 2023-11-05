import java.util.*;

class Job {
    String id;
    int deadline;
    int profit;

    public Job(String id, int deadline, int profit) {
        this.id = id;
        this.deadline = deadline;
        this.profit = profit;
    }
}

class Node {
    int profit;
    int bound;
    String id;
    Job[] jobs;

    public Node(int n) {
        profit = 0;
        bound = 0;
        id = "";
        jobs = new Job[n];
    }

    public Node(Node node) {
        profit = node.profit;
        bound = node.bound;
        id = node.id;
        jobs = Arrays.copyOf(node.jobs, node.jobs.length);
    }
}


class FinalBranchBound {
    public static int maxProfit;
    public static String bestSequence;

    public static void getMaximumProfit(List<Job> jobs) {
        int n = jobs.size();

        Node root = new Node(n);
        root.bound = getBound(root, n, jobs);

        DFS(root, jobs, n);
    }

    private static void DFS(Node node, List<Job> jobs, int n) {
        if (node.profit > maxProfit) {
            maxProfit = node.profit;
            bestSequence = node.id;
        }

        if (node.bound <= maxProfit) {
            return;
        }

        for (Job job : jobs) {
            if (!containsJob(node.jobs, job)) {
                Node child = new Node(node);
                int index = getFirstEmptyIndex(child.jobs);
                child.jobs[index] = job;
                child.id = node.id + job.id + " ";

                child.profit += job.profit;
                child.bound = getBound(child, n, jobs);

                if (child.bound > maxProfit) {
                    DFS(child, jobs, n);
                }
            }
        }
    }



    private static int getFirstEmptyIndex(Job[] jobs) {
        for (int i = 0; i < jobs.length; i++) {
            if (jobs[i] == null) {
                return i;
            }
        }
        return -1;
    }

    private static boolean containsJob(Job[] jobs, Job job) {
        for (Job j : jobs) {
            if (j != null && j.id.equals(job.id)) {
                return true;
            }
        }
        return false;
    }

    private static int getBound(Node node, int n, List<Job> jobs) {
        int maxPossibleDeadline = jobs.stream().mapToInt(j -> j.deadline).max().orElse(0);
        int[] slot = new int[maxPossibleDeadline + 1];
        for (Job job : node.jobs) {
            if (job != null) {
                slot[job.deadline] = job.profit;
            }
        }

        int bound = node.profit;
        for (Job job : jobs) {
            for (int i = job.deadline; i > 0; i--) {
                if (slot[i] == 0) {
                    slot[i] = job.profit;
                    bound += job.profit;
                    break;
                }
            }
        }

        return bound;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of jobs: ");
        int n = scanner.nextInt();

        List<Job> jobs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            System.out.print("Enter job id: ");
            String id = scanner.next();
            System.out.print("Enter deadline: ");
            int deadline = scanner.nextInt();
            System.out.print("Enter profit: ");
            int profit = scanner.nextInt();
            jobs.add(new Job(id, deadline, profit));
        }

        Collections.sort(jobs, Comparator.comparingInt(j -> j.deadline));

        getMaximumProfit(jobs);

        System.out.println("Optimal Job Sequence: " + bestSequence);
        System.out.println("Maximum Profit: " + maxProfit);
    }

}
