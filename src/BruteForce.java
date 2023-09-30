import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BruteForce {

    private  Map<String, List<String>> adjacencyMap = new HashMap<>();
    private static Map<String, Coordinates> coordinatesMap = new HashMap<>();

    public BruteForce(String start, String goal) {
        // Load city adjacency and coordinates data

        loadData(start, goal);
    }

    private void loadData(String start, String goal) {
        // File paths... the src\file... did not work
        String adjacencyFilePath = "C:\\Users\\meetm\\OneDrive\\Desktop\\AI\\src\\files\\Adjacencies.txt";
        String coordinatesFilePath = "C:\\Users\\meetm\\OneDrive\\Desktop\\AI\\src\\files\\coordinates.csv";

        // Create a set to store known cities
        Set<String> knownCities = new HashSet<>();


        try {
            // Read and process adjacencies.txt
            FileReader adjacenciesFileReader = new FileReader(adjacencyFilePath);
            BufferedReader adjacenciesBufferedReader = new BufferedReader(adjacenciesFileReader);

            String adjacenciesLine;
            while ((adjacenciesLine = adjacenciesBufferedReader.readLine()) != null) {
                // for space
                String[] parts = adjacenciesLine.split("\\s+");

                if (parts.length == 2) {
                    String city1 = parts[0];
                    String city2 = parts[1];


                    // Add city2 to the adjacency list of city1
                    adjacencyMap.computeIfAbsent(city1, k -> new ArrayList<>()).add(city2);

                    // Add city1 to the adjacency list of city2 (assuming adjacency is bidirectional)
                    adjacencyMap.computeIfAbsent(city2, k -> new ArrayList<>()).add(city1);

                    // Add cities to the knownCities set
                    knownCities.add(city1);
                    knownCities.add(city2);
                }
            }
            adjacenciesBufferedReader.close();

            // Read and process coordinates.csv
            FileReader coordinatesFileReader = new FileReader(coordinatesFilePath);
            BufferedReader coordinatesBufferedReader = new BufferedReader(coordinatesFileReader);

            String coordinatesLine;
            while ((coordinatesLine = coordinatesBufferedReader.readLine()) != null) {
                // Split each line by a comma
                String[] parts = coordinatesLine.split(",");

                if (parts.length == 3) {
                    String city = parts[0];
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);

                    // Store coordinates information in the coordinatesMap
                    coordinatesMap.put(city, new Coordinates(x, y));

                    // Add city to the knownCities set
                    knownCities.add(city);
                }
            }

            coordinatesBufferedReader.close();

        } catch (IOException e) {
            System.err.println("Error reading the files: " + e.getMessage());
        }
        // check file for cities
        DisplayResults displayResults = new DisplayResults();
        if (!knownCities.contains(start) || !knownCities.contains(goal)) {
            displayResults.ErrorMessage("start or goal city not in file");
        }

    }


    public List<String> findPath(String start, String goal) {

        Set<String> visited = new HashSet<>();
        List<String> path = new ArrayList<>();
        path.add(start);

        List<String> result = findPathRecursive(start, goal, visited, path);


        return result;
    }


    private List<String> findPathRecursive(String currentCity, String goal, Set<String> visited, List<String> path) {

        if (currentCity.equals(goal)) {
            return path; // Goal reached
        }

        visited.add(currentCity);

        List<String> neighbors = adjacencyMap.get(currentCity);
        if (neighbors != null) {
            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    path.add(neighbor);
                    List<String> result = findPathRecursive(neighbor, goal, visited, path);
                    if (result != null) {
                        return result; // Path found
                    }
                    path.remove(path.size() - 1);
                }
            }
        }

        visited.remove(currentCity);
        return null; // Path not found
    }

    static double calculateTotalDistance(List<String> path) {
        double totalDistance = 0.0;

        // Iterate through the path to calculate distances between consecutive cities
        for (int i = 0; i < path.size() - 1; i++) {
            String city1 = path.get(i);
            String city2 = path.get(i + 1);

            Coordinates coordinates1 = coordinatesMap.get(city1);
            Coordinates coordinates2 = coordinatesMap.get(city2);

            // Calculate the distance between the two cities and add it to the total distance
            totalDistance += adjencentCityDistance.calculateDistance(coordinates1, coordinates2);
        }
        return totalDistance;
    }

    // store coordinates (x and y)
    static class Coordinates {
        double x;
        double y;

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public Coordinates(double x, double y) {
            this.x = x;
            this.y = y;
        }

    }
}