
// UpdatePolicyScreen.java
package application.screens;

import api.policy.Policy;
import api.policy.PolicyService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class UpdatePolicyScreen extends JFrame {
    private final JTextField searchField;
    private final JTextField policyNameField;
    private final JTextField policyTypeField;
    private final JTextField policyHolderIDField;
    private final JTextField policyCostField;
    private final JTextField expiryDateField;
    private final PolicyService service;

    public UpdatePolicyScreen() {
        service = new PolicyService();
        
        setTitle("Update Policy");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 2));

        // Search field
        add(new JLabel("Enter Policy Number:"));
        searchField = new JTextField();
        add(searchField);

        // Search button
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int policyNumber = Integer.parseInt(searchField.getText());
                    Policy policy = service.getPolicyById(policyNumber);
                    if (policy != null) {
                        policyNameField.setText(policy.getPolicyName());
                        policyTypeField.setText(policy.getPolicyType());
                        policyHolderIDField.setText(policy.getPolicyHolderID());
                        policyCostField.setText(policy.getPolicyCost().toString());
                        expiryDateField.setText(policy.getExpiryDate().toString());
                    } else {
                        JOptionPane.showMessageDialog(null, "Policy not found.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid policy number.");
                }
            }
        });
        add(searchButton);

        // Input fields
        add(new JLabel("Policy Name:"));
        policyNameField = new JTextField();
        add(policyNameField);

        add(new JLabel("Policy Type:"));
        policyTypeField = new JTextField();
        add(policyTypeField);

        add(new JLabel("Policy Holder ID:"));
        policyHolderIDField = new JTextField();
        add(policyHolderIDField);

        add(new JLabel("Policy Cost:"));
        policyCostField = new JTextField();
        add(policyCostField);

        add(new JLabel("Expiry Date (YYYY-MM-DD):"));
        expiryDateField = new JTextField();
        add(expiryDateField);

        // Update button
        JButton updateButton = new JButton("Update Policy");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int policyNumber = Integer.parseInt(searchField.getText());
                    Policy updatedPolicy = new Policy(
                            policyNumber,
                            policyHolderIDField.getText(),
                            policyNameField.getText(),
                            policyTypeField.getText(),
                            new BigDecimal(policyCostField.getText()),
                            java.sql.Date.valueOf(expiryDateField.getText())
                    );

                    if (service.updatePolicy(policyNumber, updatedPolicy)) {
                        JOptionPane.showMessageDialog(null, "Policy Updated Successfully!");
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error Updating Policy.");
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, 
                        "Please check your input format. Date should be YYYY-MM-DD and Cost should be a valid number.");
                }
            }
        });
        add(updateButton);

        // Cancel button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);

        setVisible(true);
    }
}