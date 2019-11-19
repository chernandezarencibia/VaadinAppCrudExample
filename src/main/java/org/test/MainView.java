    package org.test;

    import org.apache.http.client.ClientProtocolException;
    import org.apache.http.client.methods.CloseableHttpResponse;
    import org.apache.http.client.methods.HttpGet;
    import org.apache.http.impl.client.CloseableHttpClient;
    import org.apache.http.impl.client.HttpClients;

    import com.vaadin.flow.component.button.Button;
    import com.vaadin.flow.component.grid.Grid;
    import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
    import com.vaadin.flow.component.orderedlayout.VerticalLayout;
    import com.vaadin.flow.component.textfield.TextField;
    import com.vaadin.flow.data.value.ValueChangeMode;
    import com.vaadin.flow.server.PWA;
    import com.vaadin.flow.router.Route;


    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStreamReader;
    import java.net.HttpURLConnection;
    import java.net.MalformedURLException;
    import java.net.URL;

    /**
     * The main view contains a button and a click listener.
     */
    @Route
    @PWA(name = "My Application", shortName = "My Application")
    public class MainView extends VerticalLayout {

        private CustomerService service = CustomerService.getInstance();
        private CustomerForm form = new CustomerForm(this);
        private Grid<Customer> grid = new Grid<>(Customer.class);
        private TextField filterText = new TextField();


        public MainView() {
        Button addCustomerBtn = new Button("Add new customer");
            addCustomerBtn.addClickListener(e -> {
                grid.asSingleSelect().clear();
                form.setCustomer(new Customer());
            });
        Button getButton = new Button("GET");
        getButton.addClickListener(e -> {
            try {
                getFunction();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        form.setCustomer(null);
        grid.asSingleSelect().addValueChangeListener(event -> form.setCustomer(grid.asSingleSelect().getValue()));
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.EAGER);
        filterText.addValueChangeListener(e -> updateList());

        grid.setColumns("firstName", "lastName","status");
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addCustomerBtn, getButton);
        HorizontalLayout mainContent = new HorizontalLayout(grid, form);
        mainContent.setSizeFull();
        grid.setSizeFull();
        add(toolbar, mainContent);
        setSizeFull();
        updateList();

    //        Button button = new Button("Click me",
    //                event -> Notification.show("Clicked!"));
    //        add(button);
    //        Button button1 = new Button("sup");
    //        add(button1);

        }

        private void getFunction() throws IOException {
            StringBuilder result = new StringBuilder();
            URL url = new URL("http://192.168.103.89:8080/HelloWorldJAXRS/rest/hellows/getAllDogs");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            System.out.println(result.toString());


        }

        public void updateList() {
            grid.setItems(service.findAll(filterText.getValue()));

        }
    }
