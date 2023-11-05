import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.*;

class Job {
    String id;
    int deadline;
    int profit;

    public Job(String id, int deadline, int profit) {
        this.id = id;
        this.deadline = deadline;
        this.profit = profit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Job job = (Job) o;

        if (deadline != job.deadline) return false;
        if (profit != job.profit) return false;
        return id.equals(job.id);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + deadline;
        result = 31 * result + profit;
        return result;
    }
}

class BranchBoundMpr extends JFrame {

    public BranchBoundMpr() {
        JFrame f = new JFrame();
        JLabel l1 = new JLabel();
        f.setTitle("TaskFLow");
        l1.setText("TaskFlow");
        l1.setFont(new Font("Book Antiqua", Font.PLAIN, 75));
        l1.setForeground(new Color(0xedf6f9));
        l1.setHorizontalAlignment(JLabel.CENTER);
        l1.setVerticalAlignment(JLabel.CENTER);
        l1.setBounds(50, 50, 400, 150);
        JButton b1 = new JButton();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(500, 500);
        f.getContentPane().setBackground(new Color(0x006d77));
        b1.setText("Get Started");
        b1.setBackground(new Color(0x83c5be));
        b1.setOpaque(true);
        b1.setBorderPainted(false);
        b1.setBounds(150, 200, 200, 50);
        b1.setForeground(new Color(0xedf6f9));
        b1.setFont(new Font("Arial", Font.PLAIN, 20));
        f.setLayout(null);
        f.setVisible(true);
        f.add(l1);
        f.add(b1);

        b1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                b1.setBackground(new Color(0x2c7da0));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                b1.setBackground(new Color(0x83c5be));
            }
        });

        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                f.dispose();
                new SecondFrame();
            }
        });
    }

    public static void main(String[] args) {
        new BranchBoundMpr();
    }
}

class SecondFrame extends JFrame {
    private JFrame f = new JFrame("TaskFlow");
    private JTextField jobIdField, deadlineField, profitField;
    private JButton addJobButton, calculateButton, deleteJobButton;
    private TextArea t1, t2;
    private DefaultListModel<Job> jobListModel;

    public SecondFrame() {
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setTitle("TaskFlow");
        f.setSize(500, 500);
        f.setLayout(null);
        f.setVisible(true);

        JPanel p1 = new JPanel();
        p1.setSize(500, 200);
        p1.setBackground(new Color(0x005f73));
        f.add(p1);

        p1.setLayout(new GridBagLayout());
        GridBagConstraints gui = new GridBagConstraints();
        gui.insets = new Insets(5, 5, 5, 5);
        gui.anchor = GridBagConstraints.LINE_START;

        gui.gridx = 0;
        gui.gridy = 0;
        JLabel Lid = new JLabel("Job ID:");
        Lid.setForeground(new Color(0xedf6f9));
        p1.add(Lid, gui);
        jobIdField = new JTextField(12);
        gui.gridx = 1;
        p1.add(jobIdField, gui);

        gui.gridx = 0;
        gui.gridy = 1;
        JLabel Ldead = new JLabel("Deadline:");
        Ldead.setForeground(new Color(0xedf6f9));
        p1.add(Ldead, gui);
        deadlineField = new JTextField(12);
        gui.gridx = 1;
        p1.add(deadlineField, gui);

        gui.gridx = 0;
        gui.gridy = 2;
        JLabel Lpro = new JLabel("Profit:");
        Lpro.setForeground(new Color(0xedf6f9));
        p1.add(Lpro, gui);
        profitField = new JTextField(12);
        gui.gridx = 1;
        p1.add(profitField, gui);

        addJobButton = new JButton("Add Job");
        addJobButton.setOpaque(true);
        addJobButton.setBorderPainted(false);
        addJobButton.setBackground(new Color(0x83c5be));
        addJobButton.setForeground(new Color(0xfefae0));
        addJobButton.setFont(new Font("Arial", Font.PLAIN, 18));
        addJobButton.setBorder(BorderFactory.createLineBorder(new Color(0x2c7da0), 2));
        addJobButton.addActionListener(new AddJobListener());

        gui.gridx = 0;
        gui.gridy = 3;
        gui.gridwidth = 2;
        p1.add(addJobButton, gui);

        deleteJobButton = new JButton("Delete Job");
        deleteJobButton.setOpaque(true);
        deleteJobButton.setBorderPainted(false);
        deleteJobButton.setBackground(new Color(0x83c5be));
        deleteJobButton.setForeground(new Color(0xfefae0));
        deleteJobButton.setFont(new Font("Arial", Font.PLAIN, 18));
        deleteJobButton.setBorder(BorderFactory.createLineBorder(new Color(0x2c7da0), 2));
        deleteJobButton.addActionListener(new DeleteJobListener());

        gui.gridx = 0;
        gui.gridy = 4;
        gui.gridwidth = 2;
        p1.add(deleteJobButton, gui);

        calculateButton = new JButton("Calculate Schedule");
        calculateButton.setOpaque(true);
        calculateButton.setBorderPainted(false);
        calculateButton.setBackground(new Color(0x83c5be));
        calculateButton.setForeground(new Color(0xfefae0));
        calculateButton.setFont(new Font("Arial", Font.PLAIN, 18));
        calculateButton.setBorder(BorderFactory.createLineBorder(new Color(0x2c7da0), 2));
        calculateButton.addActionListener(new CalculateListener());
        gui.gridy = 10;
        p1.add(calculateButton, gui);

        t2 = new TextArea();
        t2.setBackground(new Color(0x94d2bd));
        t2.setBounds(0, 200, 500, 150);
        t2.append("Jobs Enlisted:");
        f.add(t2);

        jobListModel = new DefaultListModel<>();
        JList<Job> jobList = new JList<>(jobListModel);

        t1 = new TextArea();
        t1.setBounds(0, 350, 500, 500);
        t1.setBackground(new Color(0xedf6f9));
        f.add(t1);

        addJobButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                addJobButton.setBackground(new Color(0x2c7da0));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addJobButton.setBackground(new Color(0x83c5be));
            }
        });

        deleteJobButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                deleteJobButton.setBackground(new Color(0x2c7da0));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                deleteJobButton.setBackground(new Color(0x83c5be));
            }
        });

        calculateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                calculateButton.setBackground(new Color(0x2c7da0));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                calculateButton.setBackground(new Color(0x83c5be));
            }
        });
    }

    private class AddJobListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String id = jobIdField.getText();
            int deadline = Integer.parseInt(deadlineField.getText());
            int profit = Integer.parseInt(profitField.getText());
            t2.append("\n"+"Job Id: "+ id+"\t\t"+" Deadline: "+deadline+"\t\t"+" Profit: "+profit);
            Job job = new Job(id, deadline, profit);
            jobListModel.addElement(job);

            jobIdField.setText("");
            deadlineField.setText("");
            profitField.setText("");
        }
    }

    private class DeleteJobListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String id = jobIdField.getText();
            int deadline = Integer.parseInt(deadlineField.getText());
            int profit = Integer.parseInt(profitField.getText());
            Job jobToDelete = new Job(id, deadline, profit);

            if (jobListModel.contains(jobToDelete)) {
                jobListModel.removeElement(jobToDelete);
                t2.append("\nJob " + jobToDelete.id + " deleted successfully.");
            } else {
                t2.append("\nJob " + jobToDelete.id + " not found in the list.");
            }
        }
    }

    private class CalculateListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Job[] jobs = new Job[jobListModel.size()];
            for (int i = 0; i < jobListModel.size(); i++) {
                jobs[i] = jobListModel.elementAt(i);
            }

            Arrays.sort(jobs, (a, b) -> b.deadline - a.deadline);

            int maxProfit = 0;
            String[] sequence = new String[jobs.length];
            boolean[] slot = new boolean[jobs.length];
            for (int i = 0; i < jobs.length; i++) {
                for (int j = Math.min(jobs.length, jobs[i].deadline) - 1; j >= 0; j--) {
                    if (!slot[j]) {
                        sequence[j] = jobs[i].id;
                        slot[j] = true;

                        maxProfit += jobs[i].profit;
                        break;
                    }
                }
            }

            t1.append("Optimal Job Sequence: " + Arrays.toString(sequence) + "\n");
            t1.append("Maximum Profit: " + maxProfit + "\n");
        }
    }
}
