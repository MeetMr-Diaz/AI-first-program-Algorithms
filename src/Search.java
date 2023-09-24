import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private static Map<String, City> cityMapCoordinates;
    private static Graph cityAjacency;
    private static JTextArea resultTextArea;
    private static final String COORDINATES_FILE = "C:\\Users\\meetm\\OneDrive\\Desktop\\java code\\bruteForceAlgo\\src\\Files\\coordinates.csv";
    private static final String ADJACENCIES_FILE = "C:\\Users\\meetm\\OneDrive\\Desktop\\java code\\bruteForceAlgo\\src\\Files\\Adjacencies.txt";


    public static void main(String[] args) {
        try {
            // Parse the CSV file containing city information
            cityMapCoordinates = new HashMap<>();

            BufferedReader cityReader = new BufferedReader(new FileReader(COORDINATES_FILE));
            String cityLine;
            while ((cityLine = cityReader.readLine()) != null) {
                String[] cityData = cityLine.split(",");
                String cityName = cityData[0];
                double latitude = Double.parseDouble(cityData[1]);
                double longitude = Double.parseDouble(cityData[2]);
                cityMapCoordinates.put(cityName, new City(cityName, latitude, longitude));
            }
            cityReader.close();

            // Parse the text file containing adjacency information and create a graph
            cityAjacency = new Graph();
            BufferedReader adjacencyReader = new BufferedReader(new FileReader(ADJACENCIES_FILE));
            String adjacencyLine;
            while ((adjacencyLine = adjacencyReader.readLine()) != null) {
                String[] cities = adjacencyLine.split(" ");
                String city1 = cities[0];
                String city2 = cities[1];
                cityAjacency.addEdge(city1, city2);
            }
            adjacencyReader.close();

            // Create a Swing GUI for user input
            SwingUtilities.invokeLater(() -> createAndShowGUI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("City Path Finder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 350);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel algorithmLabel = new JLabel("    Select Algorithm: ");
        String[] algorithms = { "Brute Force", "Breadth First", "Depth First", "ID-DFS", "Best First", "A*" };
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

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String algorithm = (String) algorithmComboBox.getSelectedItem();
                String startCity = startField.getText();
                String goalCity = goalField.getText();

                AlgorithmRunner  algorithmRunner = new AlgorithmRunner (algorithm, startCity,goalCity);
                algorithmRunner.execute();
            }
        });

        frame.setVisible(true);
    }

    static class AlgorithmRunner{
        private String algorithm;
        private String startCity;
        private String goalCity ;

        public AlgorithmRunner(String algorithm,String startCity, String goalCity){
            this.algorithm = algorithm;
            this.startCity = startCity;
            this.goalCity = goalCity;
        }

        public void execute(){
            switch (algorithm){
                case "Brute Force":
                    runBruteForce();
                    break;
                case "Breadth First":
                    runBreadthFirst();
                    break;
                case "Depth First":
                    runDepthFirst();
                    break;
                case "ID-DFS":
                    runIDDFS();
                    break;
                case "Best First":
                    runBestFirst();
                    break;
                case "A*":
                    runAStar();
                    break;
            }
        }
        private void runBruteForce() {
            // Create an instance of BruteForceSearch and find the shortest path
            BruteForceSearch bfs = new BruteForceSearch(cityAjacency, cityMapCoordinates);

            long startTime = System.nanoTime();
            Object[] result = bfs.findShortestPath(startCity, goalCity);
            long endTime = System.nanoTime();
            double executionTime = (endTime - startTime) / 1e6; // Convert to milliseconds

            if (result != null) {
                List<String> shortestPath = (List<String>) result[0];
                double totalDistance = (double) result[1];

                displayResult("Brute Force", shortestPath, totalDistance, executionTime);
            }
        }
        private void runBreadthFirst() {
            BreadthFirstSearch breadthFirstSearch = new BreadthFirstSearch(cityAjacency, cityMapCoordinates);

            long startTime = System.nanoTime();
            BreadthFirstSearch.SearchResult result = breadthFirstSearch.findShortestPath(startCity, goalCity);
            long endTime = System.nanoTime();
            double executionTime = (endTime - startTime) / 1e6; // Convert to milliseconds

            if (result != null) {
                List<String> shortestPath = result.getPath();
                double totalDistance = result.getTotalDistance();

                displayResult("Breadth First", shortestPath, totalDistance, executionTime);
            }
        }
        private void runDepthFirst() {
            DepthFirstSearch depthFirstSearch = new DepthFirstSearch(cityAjacency, cityMapCoordinates);
            long startTime = System.nanoTime();
            DepthFirstSearch.SearchResult result = depthFirstSearch.findShortestPath(startCity, goalCity);
            long endTime = System.nanoTime();
            double executionTime = (endTime - startTime) / 1e6; // Convert to milliseconds

            if (result != null) {
                List<String> shortestPath = result.getPath();
                double totalDistance = result.getTotalDistance();

                displayResult("Depth First", shortestPath, totalDistance, executionTime);
            }
        }

        private void runIDDFS() {

            ID_DFS idDfs = new ID_DFS(cityAjacency, cityMapCoordinates);

            long startTime = System.nanoTime();
            ID_DFS.SearchResult result = idDfs.findShortestPath(startCity, goalCity);
            long endTime = System.nanoTime();

            double executionTime = (endTime - startTime) / 1e6; // Convert to milliseconds

            if (result != null) {
                List<String> shortestPath = result.getPath();
                double totalDistance = result.getTotalDistance();

                displayResult("ID-DFS", shortestPath, totalDistance, executionTime);
            }
        }
        private void runBestFirst() {

            BestFirstSearch bestFirstSearch  = new BestFirstSearch(cityAjacency, cityMapCoordinates );

            long startTime = System.nanoTime();
            BestFirstSearch.SearchResult result = bestFirstSearch.findShortestPath(startCity, goalCity);
            long endTime = System.nanoTime();

            double executionTime = (endTime - startTime) / 1e6; // Convert to milliseconds

            if (result != null) {
                List<String> shortestPath = result.getPath();
                double totalDistance = result.getTotalDistance();

                displayResult("Best First", shortestPath, totalDistance, executionTime);
            }
        }
        private void runAStar() {

            AStarSearch aStar = new AStarSearch(cityAjacency, cityMapCoordinates);


            long startTime = System.nanoTime();
            List<String> result = aStar.findShortestPath(startCity, goalCity);


            long endTime = System.nanoTime();
            double executionTime = (endTime - startTime) / 1e6; // Convert to milliseconds


            if (result != null) {
                double totalDistance = 0.0;

                for (int i = 0; i < result.size() - 1; i++) {
                    String city1 = result.get(i);
                    String city2 = result.get(i + 1);
                    totalDistance += aStar.calculateDistance(cityMapCoordinates.get(city1), cityMapCoordinates.get(city2));
                }
                displayResult("A*", result, totalDistance, executionTime);
            }
        }



        private void displayResult(String algorithmName, List<String> shortestPath, double totalDistance, double executionTime) {
            // Display the result in the resultTextArea
            String pathString = String.join(" -> ", shortestPath);
            String message = "Method: " + algorithmName + "\n" +
                    "Path: " + pathString + "\n" +
                    "Execution time: " + executionTime + " milliseconds\n" +
                    "Total Distance: " + totalDistance + " kilometers\n\n";
            resultTextArea.append(message);
        }

    }




}
