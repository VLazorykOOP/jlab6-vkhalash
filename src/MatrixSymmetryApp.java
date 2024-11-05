import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class MatrixSymmetryApp extends JFrame {
    private final JTextField sizeField;
    private final DefaultTableModel tableModel;
    private final JTextArea resultArea;
    private final JButton checkButton;
    private int currentSize = 0;

    public MatrixSymmetryApp() {
        setTitle("Matrix Symmetry Checker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JLabel sizeLabel = new JLabel("Enter matrix size (<=20):");
        sizeField = new JTextField(5);
        JButton createButton = new JButton("Create Matrix");

        topPanel.add(sizeLabel);
        topPanel.add(sizeField);
        topPanel.add(createButton);

        tableModel = new DefaultTableModel();
        JTable matrixTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(matrixTable);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        checkButton = new JButton("Check Symmetry");
        JButton loadButton = new JButton("Load from File");
        buttonPanel.add(loadButton);
        buttonPanel.add(checkButton);

        resultArea = new JTextArea(5, 40);
        resultArea.setEditable(false);
        JScrollPane resultScrollPane = new JScrollPane(resultArea);

        centerPanel.add(tableScrollPane, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        bottomPanel.add(resultScrollPane, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        createButton.addActionListener(e -> createMatrix());
        checkButton.addActionListener(e -> checkSymmetry());
        loadButton.addActionListener(e -> loadFromFile());

        checkButton.setEnabled(false);

        pack();
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(500, 400));
    }

    private void createMatrix() {
        try {
            int size = Integer.parseInt(sizeField.getText());
            if (size <= 0 || size > 20) {
                JOptionPane.showMessageDialog(this, "Please enter a size between 1 and 20");
                return;
            }

            currentSize = size;
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);

            for (int i = 0; i < size; i++) {
                tableModel.addColumn("Col " + (i + 1));
            }

            for (int i = 0; i < size; i++) {
                Object[] row = new Object[size];
                Arrays.fill(row, 0.0);
                tableModel.addRow(row);
            }

            checkButton.setEnabled(true);
            resultArea.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number");
        }
    }

    private void checkSymmetry() {
        resultArea.setText("");
        StringBuilder result = new StringBuilder("Symmetry Results:\n");
        boolean foundSymmetry = false;

        for (int i = 0; i < currentSize; i++) {
            boolean isSymmetric = true;
            int left = 0;
            int right = currentSize - 1;

            while (left < right) {
                double leftVal = Double.parseDouble(tableModel.getValueAt(i, left).toString());
                double rightVal = Double.parseDouble(tableModel.getValueAt(i, right).toString());
                if (leftVal != rightVal) {
                    isSymmetric = false;
                    break;
                }
                left++;
                right--;
            }

            if (isSymmetric) {
                result.append("Row ").append(i + 1).append(" is symmetric\n");
                foundSymmetry = true;
            }
        }

        if (!foundSymmetry) {
            result.append("No symmetry found.");
        }

        resultArea.setText(result.toString());
    }

    private void loadFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (Scanner scanner = new Scanner(selectedFile)) {
                int size = scanner.nextInt();
                if (size <= 0 || size > 20) {
                    JOptionPane.showMessageDialog(this, "Matrix size must be between 1 and 20.");
                    return;
                }

                sizeField.setText(String.valueOf(size));
                createMatrix();

                boolean isValid = true;
                scanner.nextLine();
                for (int i = 0; i < size && isValid; i++) {
                    if (!scanner.hasNextLine()) {
                        isValid = false;
                        break;
                    }

                    String[] values = scanner.nextLine().trim().split("\\s+");
                    if (values.length != size) {
                        isValid = false;
                        break;
                    }

                    for (int j = 0; j < size; j++) {
                        try {
                            tableModel.setValueAt(Double.parseDouble(values[j]), i, j);
                        } catch (NumberFormatException e) {
                            isValid = false;
                            break;
                        }
                    }
                }

                if (!isValid || scanner.hasNext()) {
                    JOptionPane.showMessageDialog(this, "File format is invalid or matrix data is incomplete.");
                    createMatrix();
                }
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this, "File not found!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MatrixSymmetryApp().setVisible(true));
    }
}
