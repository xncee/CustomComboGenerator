
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Combo extends JFrame {
    private JLabel currentEmailLabel;
    private JButton nextButton;
    private JButton copyUserButton;
    private JButton copyEmailButton;
    private JTextArea passwordsTextArea;
    private JButton saveComboButton;

    private JPanel mainPanel;

    static Scanner input = new Scanner(System.in);
    static List<String> user_email_list = new ArrayList<>();
    static Iterator<String> it;
    static String current;
    public Combo() {
        currentEmailLabel.setText(current);
        passwordsTextArea.setLineWrap(true);
        passwordsTextArea.setWrapStyleWord(true);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!it.hasNext()) {
                    JOptionPane.showMessageDialog(nextButton,"You have reached the end of the list!");
                    return;
                }
                passwordsTextArea.setText("");
                for (;;) {
                    if (!it.hasNext()) {
                        JOptionPane.showMessageDialog(nextButton,"You have reached the end of the list!");
                        break;
                    }
                    current = it.next();
                    if (current.contains(":")) break;
                }
                currentEmailLabel.setText(current);
                String email = current.split(":")[1];
                copyToClipboard(email);
                //JOptionPane.showMessageDialog(nextButton, current);
            }
        });
        copyUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = current.split(":")[0];
                copyToClipboard(user);
            }
        });
        copyEmailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = current.split(":")[1];
                copyToClipboard(email);
            }
        });
        saveComboButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] passwords = passwordsTextArea.getText().split("\n");
                if (passwordsTextArea.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(saveComboButton,"enter passwords!");
                    return;
                }
                String user = current.split(":")[0];
                //String email = current.split(":")[1];
                for (String password: passwords) {
                    writeToFile("combo-user-pass.txt", user+":"+password);
                }

                passwordsTextArea.setText("");
            }
        });
    }

    public static void main(String[] args) {
        loadEmailsList();
        it = user_email_list.iterator();
        if (!it.hasNext()) {
            JOptionPane.showMessageDialog(null,"emails.txt is empty!");
            System.exit(0);
        }
        for (;;) {
            if (!it.hasNext()) {
                JOptionPane.showMessageDialog(null,"The list doesn't contain any valid data.\n*required format is user:email");
                break;
            }
            current = it.next();
            if (current.contains(":")) break;
        }
        String email = current.split(":")[1];
        copyToClipboard(email);

        Combo c = new Combo();
        c.setContentPane(c.mainPanel);
        c.setTitle("Combo generator");
        c.setSize(400, 200);
        c.setVisible(true);
        c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void loadEmailsList() {
        try {
            FileReader fr = new FileReader("emails.txt");
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();
            while (line!=null) {
                user_email_list.add(line);
                line = br.readLine();
            }
        }
        catch (IOException e) {
            //System.out.println(RED+"emails.txt is missing."+RESET);
            JOptionPane.showMessageDialog(null,"emails.txt is missing!");
            System.exit(0);
        }
    }

    public static void copyToClipboard(String str) {
        StringSelection stringSelection = new StringSelection(str);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    public static void writeToFile(String file, String str) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(new FileWriter(file, true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writer.println(str);
        writer.close();
    }
}