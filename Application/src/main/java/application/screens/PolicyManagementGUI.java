package application.screens;
import javax.swing.*;

import api.policy.Policy;
import api.policy.PolicyService;

import java.awt.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class PolicyManagementGUI extends JFrame {
    private JFrame addPolicyFrame;
    private JFrame updatePolicyFrame;
    private PolicyService policyService;
    private SimpleDateFormat dateFormat;

    public PolicyManagementGUI() {
        policyService = new PolicyService();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        createAddPolicyFrame();
        createUpdatePolicyFrame();
    }

    private void createAddPolicyFrame() {
        addPolicyFrame = new JFrame("Add New Policy");
        addPolicyFrame.setSize(400, 300);
        addPolicyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addPolicyFrame.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Policy Name
        JLabel nameLabel = new JLabel("Policy Name:");
        JTextField nameField = new JTextField(20);
        
        // Policy Type
        JLabel typeLabel = new JLabel("Policy Type:");
        JTextField typeField = new JTextField(20);
        
        // Policy Holder ID
        JLabel holderLabel = new JLabel("Policy Holder ID:");
        JTextField holderField = new JTextField(20);
        
        // Policy Cost
        JLabel costLabel = new JLabel("Policy Cost:");
        JTextField costField = new JTextField(20);
        
        // Expiry Date
        JLabel expiryLabel = new JLabel("Expiry Date (YYYY-MM-DD):");
        JTextField expiryField = new JTextField(20);

        // Submit Button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            try {
                Policy newPolicy = new Policy(
                    0, // PolicyNumber will be generated by database
                    holderField.getText(),
                    nameField.getText(),
                    typeField.getText(),
                    new BigDecimal(costField.getText()),
                    new Date(dateFormat.parse(expiryField.getText()).getTime())
                );
                
                if (policyService.insertPolicy(newPolicy)) {
                    JOptionPane.showMessageDialog(addPolicyFrame, 
                        "Policy added successfully!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    clearFields(nameField, typeField, holderField, costField, expiryField);
                } else {
                    JOptionPane.showMessageDialog(addPolicyFrame, 
                        "Failed to add policy.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(addPolicyFrame, 
                    "Invalid date format. Please use YYYY-MM-DD", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addPolicyFrame, 
                    "Invalid cost format. Please enter a valid number.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Cancel Button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> clearFields(nameField, typeField, holderField, costField, expiryField));

        // Add components using GridBagLayout
        gbc.gridx = 0; gbc.gridy = 0;
        addPolicyFrame.add(nameLabel, gbc);
        gbc.gridx = 1;
        addPolicyFrame.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        addPolicyFrame.add(typeLabel, gbc);
        gbc.gridx = 1;
        addPolicyFrame.add(typeField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        addPolicyFrame.add(holderLabel, gbc);
        gbc.gridx = 1;
        addPolicyFrame.add(holderField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        addPolicyFrame.add(costLabel, gbc);
        gbc.gridx = 1;
        addPolicyFrame.add(costField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        addPolicyFrame.add(expiryLabel, gbc);
        gbc.gridx = 1;
        addPolicyFrame.add(expiryField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        addPolicyFrame.add(buttonPanel, gbc);
    }

    private void createUpdatePolicyFrame() {
        updatePolicyFrame = new JFrame("Update Policy");
        updatePolicyFrame.setSize(400, 300);
        updatePolicyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        updatePolicyFrame.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Policy Number (for searching)
        JLabel searchLabel = new JLabel("Policy Number:");
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        // Other fields
        JLabel nameLabel = new JLabel("Policy Name:");
        JTextField nameField = new JTextField(20);
        
        JLabel typeLabel = new JLabel("Policy Type:");
        JTextField typeField = new JTextField(20);
        
        JLabel holderLabel = new JLabel("Policy Holder ID:");
        JTextField holderField = new JTextField(20);
        
        JLabel costLabel = new JLabel("Policy Cost:");
        JTextField costField = new JTextField(20);
        
        JLabel expiryLabel = new JLabel("Expiry Date (YYYY-MM-DD):");
        JTextField expiryField = new JTextField(20);

        // Search functionality
        searchButton.addActionListener(e -> {
            try {
                int policyNumber = Integer.parseInt(searchField.getText());
                Policy policy = policyService.getPolicyById(policyNumber);
                if (policy != null) {
                    nameField.setText(policy.getPolicyName());
                    typeField.setText(policy.getPolicyType());
                    holderField.setText(policy.getPolicyHolderID());
                    costField.setText(policy.getPolicyCost().toString());
                    expiryField.setText(dateFormat.format(policy.getExpiryDate()));
                } else {
                    JOptionPane.showMessageDialog(updatePolicyFrame, 
                        "Policy not found.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(updatePolicyFrame, 
                    "Please enter a valid policy number.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        // Update Button
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            try {
                int policyNumber = Integer.parseInt(searchField.getText());
                Policy updatedPolicy = new Policy(
                    policyNumber,
                    holderField.getText(),
                    nameField.getText(),
                    typeField.getText(),
                    new BigDecimal(costField.getText()),
                    new Date(dateFormat.parse(expiryField.getText()).getTime())
                );
                
                if (policyService.updatePolicy(policyNumber, updatedPolicy)) {
                    JOptionPane.showMessageDialog(updatePolicyFrame, 
                        "Policy updated successfully!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    clearFields(searchField, nameField, typeField, holderField, costField, expiryField);
                } else {
                    JOptionPane.showMessageDialog(updatePolicyFrame, 
                        "Failed to update policy.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(updatePolicyFrame, 
                    "Invalid date format. Please use YYYY-MM-DD", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(updatePolicyFrame, 
                    "Invalid number format.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Cancel Button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> 
            clearFields(searchField, nameField, typeField, holderField, costField, expiryField));

        // Add components using GridBagLayout
        gbc.gridx = 0; gbc.gridy = 0;
        updatePolicyFrame.add(searchLabel, gbc);
        gbc.gridx = 1;
        updatePolicyFrame.add(searchField, gbc);
        gbc.gridx = 2;
        updatePolicyFrame.add(searchButton, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        updatePolicyFrame.add(nameLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        updatePolicyFrame.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 1;
        updatePolicyFrame.add(typeLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        updatePolicyFrame.add(typeField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 1;
        updatePolicyFrame.add(holderLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        updatePolicyFrame.add(holderField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 1;
        updatePolicyFrame.add(costLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        updatePolicyFrame.add(costField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 1;
        updatePolicyFrame.add(expiryLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        updatePolicyFrame.add(expiryField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 3;
        updatePolicyFrame.add(buttonPanel, gbc);
    }

    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    public void showAddPolicy() {
        addPolicyFrame.setVisible(true);
        updatePolicyFrame.setVisible(false);
    }

    public void showUpdatePolicy() {
        updatePolicyFrame.setVisible(true);
        addPolicyFrame.setVisible(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PolicyManagementGUI gui = new PolicyManagementGUI();
            gui.showAddPolicy();
        });
    }
}