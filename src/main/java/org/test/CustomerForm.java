package org.test;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;



public class CustomerForm extends FormLayout {
    private MainView mainView;
    private CustomerService service = CustomerService.getInstance();
    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private ComboBox<CustomerStatus> status = new ComboBox<>("Status");
    private DatePicker birthDate = new DatePicker("Birthdate");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private Binder<Customer> binder = new Binder<>(Customer.class);

    public CustomerForm(MainView mainView) {
        this.mainView = mainView;
        save.addClickListener(event-> save());
        delete.addClickListener(event-> delete());
        status.setItems(CustomerStatus.values());

        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(firstName, lastName, status, birthDate, buttons);
        binder.bindInstanceFields(this);


    }

    public void save(){
        Customer customer = binder.getBean();
        service.save(customer);
        mainView.updateList();
        setCustomer(null);
    }

    public void delete(){
        Customer customer = binder.getBean();
        service.delete(customer);
        mainView.updateList();
        setCustomer(null);
    }

    public void setCustomer(Customer customer){
        binder.setBean(customer);

        if(customer == null){
            setVisible(false);
        }else{
            setVisible(true);
            firstName.focus();
        }
    }
}
