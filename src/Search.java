import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.*;

class City {
    String name;
    double latitude;
    double longitude;

    public City(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}

class Graph {
    Map<String, List<String>> adjacencyList;

    public Graph() {
        adjacencyList = new HashMap<>();
    }

    public void addEdge(String city1, String city2) {
        adjacencyList.computeIfAbsent(city1, k -> new ArrayList<>()).add(city2);
        adjacencyList.computeIfAbsent(city2, k -> new ArrayList<>()).add(city1);
    }

    public List<String> getNeighbors(String city) {
        return adjacencyList.getOrDefault(city, Collections.emptyList());
    }
}

public class Search {
    static Map<String, City> coordinates;
    static Graph cityAdjacency;
    static JTextArea resultTextArea;

    // Files/coordinates.csv did not work. file error here? try absolute path
    private static final String COORDINATES_FILE = "C:\\Users\\meetm\\OneDrive\\Desktop\\java code\\bruteForceAlgo\\src\\Files\\coordinates.csv";
    private static final String ADJACENCY_FILE = "C:\\Users\\meetm\\OneDrive\\Desktop\\java code\\bruteForceAlgo\\src\\Files\\Adjacencies.txt";

    public static void main(String[] args) {
        // search through files and store info
        try {
            // the CSV file containing city and its coordinates
            coordinates = new HashMap<>();

            BufferedReader cityReader = new BufferedReader(new FileReader(COORDINATES_FILE));
            String cityLine;
            while ((cityLine = cityReader.readLine()) != null) {
                String[] cityData = cityLine.split(",");
                String cityName = cityData[0];
                double latitude = Double.parseDouble(cityData[1]);
                double longitude = Double.parseDouble(cityData[2]);
                coordinates.put(cityName, new City(cityName, latitude, longitude));
            }
            cityReader.close();

            // the text file containing adjacency information and create a graph
            cityAdjacency = new Graph();
            BufferedReader adjacencyReader = new BufferedReader(new FileReader(ADJACENCY_FILE));
            String adjacencyLine;
            while ((adjacencyLine = adjacencyReader.readLine()) != null) {
                String[] cities = adjacencyLine.split(" ");
                String city1 = cities[0];
                String city2 = cities[1];
                cityAdjacency.addEdge(city1, city2);
            }
            adjacencyReader.close();

            // Create a Swing GUI for user input
            SwingUtilities.invokeLater(Search::createAndShowGUI);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //GUI for the options
    private static void createAndShowGUI() {

        JFrame frame = new JFrame("City Path Finder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 350);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel algorithmLabel = new JLabel("    Select Algorithm: ");
        String[] algorithms = {"A*","Best First","Breadth First","Brute Force",  "Depth First", "ID-DFS",  };
        JComboBox<String> algorithmComboBox = new JComboBox<>(algorithms);

        JLabel startLabel = new JLabel("    Enter starting city: ");
        JTextField startField = new JTextField();
        JLabel goalLabel = new JLabel("     Enter goal city: ");
        JTextField goalField = new JTextField();
        JButton searchButton = new JButton("Search");

        panel.add(algorithmLabel);
        panel.add(algorithmComboBox);
        panel.add(startLabel);
        panel.add(startField);
        panel.add(goalLabel);
        panel.add(goalField);
        panel.add(new JLabel()); // Empty label for spacer
        panel.add(searchButton);

        // Create a text area to display the result
        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultTextArea);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(e -> {
            String algorithm = (String) algorithmComboBox.getSelectedItem();
            String startCity = startField.getText();
            String goalCity = goalField.getText();

            algorithmMethods.Algorithm algorithmRunner = new algorithmMethods.Algorithm(algorithm, startCity, goalCity);
            algorithmRunner.execute();
        });

        frame.setVisible(true);
    }
}
