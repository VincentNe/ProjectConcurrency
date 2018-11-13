package panel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddCheckout extends JComponent {
    private JPanel addCheckout;
    private JButton button1;
    private JTextField textField1;
    private JTextField scannerDelayInput;

    private UIController uiController;

    public AddCheckout() {
        $$$setupUI$$$();
        uiController = UIController.UIControllerinstance();
    }

    private void createUIComponents() {
        button1 = new JButton();
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uiController.addCheckout(textField1.getText(), Integer.valueOf(scannerDelayInput.getText()));
            }
        });
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        addCheckout = new JPanel();
        addCheckout.setLayout(new GridBagLayout());
        addCheckout.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-14849349)), "Add Checkout", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 14, addCheckout.getFont()), new Color(-14849349)));
        textField1 = new JTextField();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 2.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addCheckout.add(textField1, gbc);
        button1.setText("Add");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addCheckout.add(button1, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Checkout Name");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        addCheckout.add(label1, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Scanner read delay");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        addCheckout.add(label2, gbc);
        scannerDelayInput = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addCheckout.add(scannerDelayInput, gbc);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return addCheckout;
    }
}