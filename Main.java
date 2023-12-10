import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static List<String> history = new ArrayList<>();
    private static JFrame f;
    private static JLabel answer;
    private static JComboBox<String> temp, temp2;
    private static JTextField temptext;
    private static int entryCount = 1;

    public static void main(String[] args) {
        f = new JFrame("Temperature Converter");
        f.setSize(400, 400);
        f.setLayout(null);
        f.setVisible(true);

        JLabel text = new JLabel("TEMPERATURE CONVERTER");
        f.add(text);
        text.setBounds(110, 10, 500, 80);

        JLabel enterTemp = new JLabel("Enter temperature");
        f.add(enterTemp);
        enterTemp.setBounds(150, 70, 150, 35);

        String temps[] = {"Celsius", "Fahrenheit", "Kelvin"};
        temp = new JComboBox<>(temps);
        f.add(temp);
        temp.setBounds(20, 100, 100, 35);

        temptext = new JTextField();
        f.add(temptext);
        temptext.setBounds(150, 100, 200, 35);

        JButton convert = new JButton("CONVERT");
        f.add(convert);
        convert.setBounds(150, 150, 90, 30);

        JButton clear = new JButton("CLEAR");
        f.add(clear);
        clear.setBounds(250, 150, 90, 30);

        answer = new JLabel("ANSWER");
        f.add(answer);
        answer.setBounds(150, 200, 150, 35);

        String temps2[] = {"Celsius", "Fahrenheit", "Kelvin"};
        temp2 = new JComboBox<>(temps2);
        f.add(temp2);
        temp2.setBounds(20, 230, 100, 35);

        JButton historyButton = new JButton("HISTORY");
        f.add(historyButton);
        historyButton.setBounds(250, 280, 90, 30);

        // ActionListener for CLEAR button
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                temptext.setText("");
                answer.setText("ANSWER");
            }
        });

        // ActionListener for HISTORY button
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHistoryPanel();
            }
        });

        
        // ActionListener for CONVERT button
        convert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputTemp = temptext.getText();
                if (!inputTemp.isEmpty()) {
                    double temperature = Double.parseDouble(inputTemp);
                    String selectedFromUnit = temp.getSelectedItem().toString();
                    String selectedToUnit = temp2.getSelectedItem().toString();

                    double convertedTemp = convertTemperature(temperature, selectedFromUnit, selectedToUnit);

                    answer.setText("Answer: " + convertedTemp + " " + selectedToUnit);
                    String entry = temperature + " " + selectedFromUnit + " to " + convertedTemp + " " + selectedToUnit;
                    history.add(entry);

                    // Save history entry to a file
                    saveToFile(entry);

                    // Update entry count for the next file
                    entryCount++;
                }
            }
        });
 
    }

    private static double convertTemperature(double temperature, String fromUnit, String toUnit) {
        if (fromUnit.equals("Celsius") && toUnit.equals("Fahrenheit")) {
            return (temperature * 9 / 5) + 32;
        } else if (fromUnit.equals("Fahrenheit") && toUnit.equals("Celsius")) {
            return (temperature - 32) * 5 / 9;
        } else if (fromUnit.equals("Celsius") && toUnit.equals("Kelvin")) {
            return temperature + 273.15;
        } else if (fromUnit.equals("Kelvin") && toUnit.equals("Celsius")) {
            return temperature - 273.15;
        } else {
            return temperature;
        }
    }

    private static void saveToFile(String entry) {
        String fileName = "history_entry_" + entryCount + ".txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write(entry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showHistoryPanel() {
        JFrame historyFrame = new JFrame("History Panel");
        historyFrame.setSize(400, 400);
        historyFrame.setLayout(null);
        historyFrame.setVisible(true);

        JList<String> historyList = new JList<>(history.toArray(new String[0]));
        JScrollPane scrollPane = new JScrollPane(historyList);
        historyFrame.add(scrollPane);
        scrollPane.setBounds(20, 20, 350, 250);

        JButton deleteButton = new JButton("Delete Selected");
        historyFrame.add(deleteButton);
        deleteButton.setBounds(20, 300, 150, 30);

        JButton closeButton = new JButton("Close");
        historyFrame.add(closeButton);
        closeButton.setBounds(200, 300, 150, 30);

        // ActionListener for Delete Selected button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = historyList.getSelectedIndex();
                if (selectedIndex != -1) {
                    // Remove the selected entry from the history list
                    history.remove(selectedIndex);

                    // Delete the corresponding file
                    deleteFile(selectedIndex + 1);

                    // Update the history list
                    historyList.setListData(history.toArray(new String[0]));
                }
            }
        });

        // ActionListener for Close button
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                historyFrame.dispose();
            }
        });

        historyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private static void deleteFile(int entryNumber) {
        String fileName = "history_entry_" + entryNumber + ".txt";
        try {
            Files.deleteIfExists(Path.of(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
