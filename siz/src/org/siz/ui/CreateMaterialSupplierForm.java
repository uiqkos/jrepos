package org.siz.ui;

import org.siz.models.Material;
import org.siz.models.MaterialSupplier;
import org.siz.models.Supplier;

import javax.swing.*;

public class CreateMaterialSupplierForm extends BaseForm {
    private final Material material;
    private JPanel panel;
    private JTextArea CommentTextArea;
    private JComboBox<Supplier> supplierСomboBox;
    private JButton saveButton;
    private JLabel materialLabel;

    public CreateMaterialSupplierForm(Material material) {
        super("Создать возможного поставщика", 800, 800);

        this.material = material;

        setContentPane(panel);

        setVisible(true);

        initFields();
        initButtons();

    }

    private void initButtons() {
        saveButton.addActionListener(
            l -> {
                var supplier = (Supplier) supplierСomboBox.getSelectedItem();

                if (supplier.getId() != null);{
                    MaterialSupplier.insert(material.getId(), supplier.getId(), CommentTextArea.getText());
                    dispose();
                }
            }
        );
    }

    private void initFields() {
        materialLabel.setText(material.getId() + " " + material.getTitle());
        var suppliers = Supplier.all();
        supplierСomboBox.setModel(new DefaultComboBoxModel<Supplier>(suppliers.toArray(Supplier[]::new)));
    }
}
