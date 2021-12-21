package org.learn.ui;

import org.learn.models.Service;
import org.learn.util.DialogUtils;
import org.learn.util.ParserValidator;
import org.learn.util.PredicateValidator;
import org.learn.util.Validator;

import javax.swing.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ServiceCreateForm extends BaseForm {
    private JTextField nameTextField;
    private JTextField costTextField;
    private JTextField discountTextField;
    private JTextField descriptionTextField;
    private JTextField imgPathTextField;
    private JSpinner durationSpinner;
    private JLabel nameLabel;
    private JPanel panel;
    private JButton createButton;

    public ServiceCreateForm() {
        super("Создать услугу");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setContentPane(panel);

        createButton.addActionListener(
            e -> {
                var service = new Service();

                Stream.of(
                    new PredicateValidator<Integer>(length -> length > 100)
                        .extractor(() -> nameTextField.getText().length())
                        .onError(() -> DialogUtils.showError(this, "Название доджно быть <= 100 символов")),

                    new PredicateValidator<>(String::isEmpty)
                        .extractor(nameTextField::getText)
                        .onError(() -> DialogUtils.showError(this, "Название не может быть пустым"))
                        .onComplete(service::setTitle),

                    new ParserValidator<>(Integer.class::cast)
                        .extractor(durationSpinner::getValue)
                        .onError(() -> DialogUtils.showError(this, "Неверно введен период"))
                        .onComplete(service::setDurationInSeconds),

                    new PredicateValidator<Integer>(i -> i < 0)
                        .extractor(service::getDurationInSeconds)
                        .onError(() -> DialogUtils.showError(this, "Период не может быть меньше 0")),

                    new ParserValidator<String, Integer>(Integer::parseInt)
                        .extractor(costTextField::getText)
                        .onError(() -> DialogUtils.showError(this, "Неверно введена цена"))
                        .onComplete(service::setCost),

                    new ParserValidator<>(Double::parseDouble)
                        .extractor(discountTextField::getText)
                        .onError(() -> DialogUtils.showError(this, "Неверно введена скидка"))
                        .onComplete(service::setDiscount),

                    new PredicateValidator<>((String o) -> false)
                        .extractor(descriptionTextField::getText)
                        .onComplete(service::setDescription),

                    new PredicateValidator<>(String::isEmpty)
                        .extractor(imgPathTextField::getText)
                        .onError(() -> DialogUtils.showError(this, "Путь до картинки не может быть пустым"))
                        .onComplete(service::setMainImagePath),

                    new PredicateValidator<>(Predicate.not(Service::save))
                        .extractor(() -> service)
                        .onError(() -> DialogUtils.showError(this, "Ошибка сохранения услуги"))
                        .onComplete((s) -> {
                            dispose();
                            new ServiceTableFrom().setVisible(true);
                        })
                ).allMatch(Validator::extractValidate);
            }
        );

    }

}
