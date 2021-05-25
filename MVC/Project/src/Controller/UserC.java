package Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JsonObject;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class UserC {
    private static int fileCount = 1;
    @FXML
    private ComboBox<String> allCakeShops;

    @FXML
    private TextArea allCakesFromCakeShop;
    @FXML
    private TextField searchCakeField;
    @FXML
    private TextArea foundCakeField;

    @FXML
    private RadioButton jsonRadioButton;
    @FXML
    private RadioButton csvRadioButton;

    @FXML
    private TextField cakeName;
    @FXML
    private TextField cakeDisponibility;
    @FXML
    private TextField cakeAvailability;
    @FXML
    private TextField cakePrice;
    @FXML
    private Label validDataLabel;
    @FXML
    private ComboBox selectCakeComboBox;

    @FXML
    private TextField newCakeNameField;
    @FXML
    private TextField newCakeDisponibilityField;
    @FXML
    private TextField newCakeAvailabilityField;
    @FXML
    private TextField newCakePriceField;
    @FXML
    private RadioButton availabilityRadioButton;
    @FXML
    private TextField disponibilityField;
    @FXML
    private TextField firstPrice;
    @FXML
    private TextField secondPrice;

    @FXML
    private TableView<Cake> tableView;
    @FXML
    private TableColumn<Cake, String> cakeNameColumn;

    @FXML
    private TableColumn<Cake, Integer> quantityColumn;

    @FXML
    private TableColumn<Cake, String> availabilityColumn;

    @FXML
    private TableColumn<Cake, Double> priceColumn;

    @FXML
    private ComboBox<String> optionalComboBox;

    @FXML
    private ComboBox<String> viewStatisticsComboBox;

    @FXML
    private ComboBox<String> statisticsForComboBox;
    @FXML
    private PieChart pieChart;
    @FXML
    private LineChart<String, Double> lineChart;
    @FXML
    private NumberAxis value;
    @FXML
    private CategoryAxis cakeNameA;
    @FXML
    private BarChart<String, Double> barChart;
    @FXML
    private ImageView img2;

    @FXML
    private ImageView img1;

    public UserC() {

    }

    public void viewCakesFromCakeShop() {
        CakePersistence cakePersistence = new CakePersistence();
        try {
            allCakesFromCakeShop.setEditable(false);
            Cakes cakes = cakePersistence.getCakesFromCakeShop(allCakeShops.getValue().toString());
            if (!cakes.getCakeList().isEmpty()) {
                allCakesFromCakeShop.setText(cakes.toString());
            } else {
                allCakesFromCakeShop.setText("No cakes found.");
            }
        } catch (Exception e) {
        }
    }

    public CakeShops viewAllCakeShops() {
        CakePersistence cakePersistence = new CakePersistence();
        return cakePersistence.getCakeShops();
    }

    public void searchCake() {
        Cake cake = null;
        try {
            CakePersistence cakePersistence = new CakePersistence();
            cake = cakePersistence.searchCakeWithName(searchCakeField.getText(), LogInC.getLoggedUser());
            foundCakeField.setText(cakePersistence.cakeByNameFromCakeShops(cake) + cake.toString());
            foundCakeField.setEditable(false);
        } catch (Exception e) {
            foundCakeField.setText("Cake not found.");
        }
    }

    public Cake getCakeByName(String name) {
        Cake cake;
        try {
            CakePersistence cakePersistence = new CakePersistence();
            cake = cakePersistence.searchCakeWithName(name, LogInC.getLoggedUser());
            return cake;
        } catch (Exception e) {
        }
        return null;
    }

    public void saveReports() {
        CakePersistence cakePersistence = new CakePersistence();
        if (csvRadioButton.isSelected()) {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("Report_" + fileCount + ".csv"));
                fileCount++;
                PrintWriter printWriter = new PrintWriter(bufferedWriter);
                if (allCakeShops.getValue() != null) {
                    printWriter.print(cakePersistence.getCakesFromCakeShop(allCakeShops.getValue()).toString());
                } else {
                    printWriter.print(cakePersistence.readFile().toString());
                }
                printWriter.flush();
                printWriter.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            if (jsonRadioButton.isSelected()) {
                JSONArray cakes = new JSONArray();
                Cakes cakes1;
                if (allCakeShops.getValue() != null) {
                    cakes1 = cakePersistence.getCakesFromCakeShop((allCakeShops.getValue().toString()));
                } else {
                    cakes1 = cakePersistence.readFile();
                }
                for (Cake cake : cakes1.getCakeList()) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.put("name", cake.getName());
                    jsonObject.put("disponibility", cake.getDisponibility());
                    String date = new SimpleDateFormat("dd-MM-yyyy").format(cake.getAvailability());
                    jsonObject.put("availability", date);
                    jsonObject.put("price", cake.getPrice());
                    cakes.add(jsonObject);
                }
                try (FileWriter file = new FileWriter("Report_" + fileCount + ".json")) {
                    fileCount++;
                    file.write(cakes.toJSONString());
                    file.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addCake() {
        try {
            Date date = new SimpleDateFormat("dd-MM-yyy").parse(cakeAvailability.getText());
            Cake cake = new Cake(cakeName.getText(), Integer.parseInt(cakeDisponibility.getText()), date, Float.parseFloat(cakePrice.getText()));
            CakePersistence cakePersistence = new CakePersistence();
            cakePersistence.addCake(cake);
            if (optionalComboBox.getValue() != null) {
                cakePersistence.addCakeToCakeShop(optionalComboBox.getValue(), cake.getId());
                cakeName.setText("");
                cakeAvailability.setText("");
                cakeDisponibility.setText("");
                cakePrice.setText("");
            } else {
                AdministratorC administratorC = new AdministratorC();
                cakePersistence.addCakeToCakeShop(administratorC.workPlaceForUser(LogInC.getLoggedUser()), cake.getId());
            }
            validDataLabel.setText("");
        } catch (Exception e) {
            validDataLabel.setText("Invalid data.");
        }
    }

    public void deleteCake() {
        try {
            CakePersistence cakePersistence = new CakePersistence();
            cakePersistence.deleteCake(selectCakeComboBox.getValue().toString(), LogInC.getLoggedUser());
            selectCakeComboBox.setValue("");
            newCakeAvailabilityField.setText("");
            newCakeNameField.setText("");
            newCakePriceField.setText("");
            newCakeDisponibilityField.setText("");
        } catch (Exception e) {

        }
    }

    public void updateCake() {
        CakePersistence cakePersistence = new CakePersistence();
        try {
            Date date = new SimpleDateFormat("dd-MM-yyy").parse(newCakeAvailabilityField.getText());
            Cake cake = new Cake(newCakeNameField.getText(), Integer.parseInt(newCakeDisponibilityField.getText()), date, Float.parseFloat(newCakePriceField.getText()));
            cakePersistence.updateCake(selectCakeComboBox.getValue().toString(), cake);
            newCakeNameField.setText("");
            newCakePriceField.setText("");
            newCakeAvailabilityField.setText("");
            newCakeDisponibilityField.setText("");
        } catch (Exception e) {
        }
    }

    public void filterCakes() {
        CakePersistence cakePersistence = new CakePersistence();
        try {
            Cakes cakes = new Cakes();
            if (availabilityRadioButton.isSelected()) {
                cakes = cakePersistence.returnByValability(LogInC.getLoggedUser());
            }
            if (!firstPrice.getText().equals("") || !secondPrice.getText().equals("")) {
                cakes = filterBetweenPrices();
            }
            if (!disponibilityField.getText().equals("")) {
                cakes = filterByDisponibility();
            }
            for (Cake cake : getObservableCakes(cakes)) {
                cakeNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                quantityColumn.setCellValueFactory(new PropertyValueFactory<>("disponibility"));
                availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));
                priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            }
            tableView.setItems(getObservableCakes(cakes));
            disponibilityField.setText("");
            availabilityRadioButton.setSelected(false);
            firstPrice.setText("");
            secondPrice.setText("");
            lineChart.setTitle("");
        } catch (Exception e) {

        }
    }

    public Cakes filterBetweenPrices() {
        CakePersistence cakePersistence = new CakePersistence();
        try {
            double first = Double.parseDouble(firstPrice.getText());
            double second = Double.parseDouble(secondPrice.getText());
            Cakes cakes = cakePersistence.filterBetweenPrices(first, second, LogInC.getLoggedUser());
            return cakes;
        } catch (Exception e) {
            return null;
        }
    }

    public Cakes filterByDisponibility() {
        CakePersistence cakePersistence = new CakePersistence();
        try {
            int value = Integer.parseInt(disponibilityField.getText());
            return cakePersistence.filterByDisponibility(value, LogInC.getLoggedUser());
        } catch (Exception e) {
            return null;
        }
    }

    public ObservableList<Cake> getObservableCakes(Cakes cakes) {
        ObservableList<Cake> cakes1 = FXCollections.observableArrayList();
        for (Cake cake : cakes.getCakeList()) {
            cakes1.add(cake);
        }
        return cakes1;
    }

    public ObservableList<PieChart.Data> createPieChartData(String about) throws ParseException {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        CakePersistence cakePersistence = new CakePersistence();
        Cakes cakes = cakePersistence.readFile();
        for (Cake cake : cakes.getCakeList()) {
            if (about.equals("Price")) {
                pieData.add(new PieChart.Data(cake.getName(), cake.getPrice()));
            } else {
                if (about.equals("Disponibility")) {
                    pieData.add(new PieChart.Data(cake.getName(), cake.getDisponibility()));
                } else {
                    if (about.equals("Availability")) {
                        Date currentDate = new Date(System.currentTimeMillis());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                        String firstDate = dateFormat.format(currentDate);
                        String secondDate = dateFormat.format(cake.getAvailability());
                        Date firstDate1 = dateFormat.parse(firstDate);
                        Date secondDate1 = dateFormat.parse(secondDate);
                        long diffMillies = Math.abs(secondDate1.getTime() - firstDate1.getTime());
                        long diff = TimeUnit.DAYS.convert(diffMillies, TimeUnit.MILLISECONDS);
                        pieData.add(new PieChart.Data(cake.getName(), diff));
                    }
                }
            }
        }
        return pieData;
    }

    public ObservableList<XYChart.Data> createLineChartData(String about) {
        ObservableList<LineChart.Data> lineData = FXCollections.observableArrayList();
        CakePersistence cakePersistence = new CakePersistence();
        Cakes cakes = cakePersistence.readFile();
        for (Cake cake : cakes.getCakeList()) {
            if (about.equals("Price")) {
                lineData.add(new XYChart.Data(cake.getName(), cake.getPrice()));
            } else {
                if (about.equals("Disponibility")) {
                    lineData.add(new XYChart.Data(cake.getName(), cake.getDisponibility()));
                } else {
                    if (about.equals("Availability")) {
                        try {
                            Date currentDate = new Date(System.currentTimeMillis());
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                            String firstDate = dateFormat.format(currentDate);
                            String secondDate = dateFormat.format(cake.getAvailability());
                            Date firstDate1 = dateFormat.parse(firstDate);
                            Date secondDate1 = dateFormat.parse(secondDate);
                            long diffMillies = Math.abs(secondDate1.getTime() - firstDate1.getTime());
                            long diff = TimeUnit.DAYS.convert(diffMillies, TimeUnit.MILLISECONDS);
                            lineData.add(new XYChart.Data(cake.getName(), diff));
                        } catch (Exception e) {

                        }
                    }
                }
            }
        }
        return lineData;
    }

    public ObservableList<BarChart.Data> createBarChartData(String about) {
        ObservableList<BarChart.Data> lineData = FXCollections.observableArrayList();
        CakePersistence cakePersistence = new CakePersistence();
        Cakes cakes = cakePersistence.readFile();
        for (Cake cake : cakes.getCakeList()) {
            if (about.equals("Price")) {
                lineData.add(new BarChart.Data(cake.getName(), cake.getPrice()));
            } else {
                if (about.equals("Disponibility")) {
                    lineData.add(new BarChart.Data(cake.getName(), cake.getDisponibility()));
                } else {
                    if (about.equals("Availability")) {
                        try {
                            Date currentDate = new Date(System.currentTimeMillis());
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                            String firstDate = dateFormat.format(currentDate);
                            String secondDate = dateFormat.format(cake.getAvailability());
                            Date firstDate1 = dateFormat.parse(firstDate);
                            Date secondDate1 = dateFormat.parse(secondDate);
                            long diffMillies = Math.abs(secondDate1.getTime() - firstDate1.getTime());
                            long diff = TimeUnit.DAYS.convert(diffMillies, TimeUnit.MILLISECONDS);
                            lineData.add(new BarChart.Data(cake.getName(), diff));
                        } catch (Exception e) {

                        }
                    }
                }
            }
        }
        return lineData;
    }

    public void fillWithShops() {
        try {
            allCakeShops.getItems().clear();
            CakeShops cakeShops = viewAllCakeShops();
            for (CakeShop cakeShop : cakeShops.getCakeShopList()) {
                allCakeShops.getItems().add(cakeShop.getName());
            }
        } catch (Exception e) {

        }
    }

    public void fillWithShopsOptional() {
        optionalComboBox.getItems().clear();
        CakeShops cakeShops = viewAllCakeShops();
        for (CakeShop cakeShop : cakeShops.getCakeShopList()) {
            optionalComboBox.getItems().add(cakeShop.getName());
        }
    }

    public void fillSelectCakeComboBox() {
        try {
            selectCakeComboBox.getItems().clear();
            CakePersistence cakePersistence = new CakePersistence();
            String name = cakePersistence.cakeShopWhereUserWorks(LogInC.getLoggedUser()).getName();
            Cakes cakes = cakePersistence.getCakesFromCakeShop(name);
            for (Cake cake : cakes.getCakeList()) {
                selectCakeComboBox.getItems().add(cake.getName());
            }
        } catch (Exception e) {

        }
    }

    public void fillFields() {
        try {
            String s = (String) selectCakeComboBox.getValue();
            if (s != null) {
                UserC userC = new UserC();
                Cake cake = userC.getCakeByName(s);
                newCakeNameField.setText(cake.getName());
                newCakeDisponibilityField.setText(String.valueOf(cake.getDisponibility()));

                DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                DateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
                newCakeAvailabilityField.setText(formatter1.format(formatter.parse(cake.getAvailability().toString())));
                newCakePriceField.setText(String.valueOf(cake.getPrice()));
            }
        } catch (Exception e) {

        }
    }

    public void fillStatistics() {
        viewStatisticsComboBox.getItems().clear();
        viewStatisticsComboBox.getItems().add("Pie Chart");
        viewStatisticsComboBox.getItems().add("Line Chart");
        viewStatisticsComboBox.getItems().add("Bar Chart");
    }

    public void fillStatisticsAbout() {
        statisticsForComboBox.getItems().clear();
        statisticsForComboBox.getItems().add("Price");
        statisticsForComboBox.getItems().add("Disponibility");
        statisticsForComboBox.getItems().add("Availability");
    }

    public void showStatistics(ActionEvent actionEvent) throws IOException {
        img1.setVisible(false);
        img2.setVisible(false);
        barChart.setVisible(false);
        lineChart.setVisible(false);
        pieChart.setVisible(false);
        UserC userC = new UserC();
        String about = statisticsForComboBox.getValue();
        pieChart.setVisible(false);
        lineChart.setVisible(false);
        if (about != null && viewStatisticsComboBox.getValue() != null) {
            if (viewStatisticsComboBox.getValue().equals("Pie Chart")) {
                try {
                    ObservableList<PieChart.Data> pieData = userC.createPieChartData(about);
                    pieChart.getData().clear();
                    pieChart.setData(pieData);
                    pieChart.setVisible(true);
                } catch (Exception e) {

                }

            } else {
                if (viewStatisticsComboBox.getValue().equals("Line Chart")) {
                    XYChart.Series series = new XYChart.Series();
                    ObservableList<XYChart.Data> lineData = userC.createLineChartData(about);
                    cakeNameA.setStartMargin(0);
                    cakeNameA.setEndMargin(0);
                    value.minHeight(20000000);
                    series.getData().addAll(lineData);
                    lineChart.getData().clear();
                    lineChart.getData().addAll(series);
                    lineChart.setVisible(true);
                } else {
                    if (viewStatisticsComboBox.getValue().equals("Bar Chart")) {
                        BarChart.Series series = new BarChart.Series();
                        ObservableList<BarChart.Data> lineData = userC.createBarChartData(about);
                        series.getData().addAll(lineData);
                        barChart.setVisible(true);
                        barChart.getData().clear();
                        barChart.getData().addAll(series);
                    }
                }
            }
        }
    }

    public void notSetJson() {
        jsonRadioButton.setSelected(false);
    }

    public void notSetCsv() {
        csvRadioButton.setSelected(false);
    }

    public void hide() {
        img1.setVisible(true);
        img2.setVisible(true);
        barChart.setVisible(false);
        lineChart.setVisible(false);
        pieChart.setVisible(false);
    }

    @FXML
    public void logOut(ActionEvent actionEvent) {
        try {
            Parent logInAccess = FXMLLoader.load(getClass().getResource("../View/login.fxml"));
            Scene scene = new Scene(logInAccess, 658, 366);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Image img = new Image("./c3.png");
            window.setResizable(true);
            window.getIcons().add(img);
            window.setTitle("Cake Shop");
            window.setScene(scene);
            window.show();
        } catch (Exception exception) {

        }
    }
}
