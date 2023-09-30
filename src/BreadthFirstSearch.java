import java.util.*;

public class BreadthFirstSearch {
    private final Graph graph;
    private final Map<String, City> cityMap;

    public BreadthFirstSearch(Graph graph, Map<String, City> cityMap) {
        this.graph = graph;
        this.cityMap = cityMap;
    }

    public SearchResult findPath(String start, String goal) {
        // check file for cities
        DisplayResults displayResults = new DisplayResults();
        if (!cityMap.containsKey(start) ||!cityMap.containsKey(goal)) {
            displayResults.ErrorMessage("start or goal city not in file");
        }
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> previousCities = new HashMap<>();

        queue.add(start);
        visited.add(start);
        previousCities.put(start, null);

        while (!queue.isEmpty()) {
            String currentCity = queue.poll();

            if (currentCity.equals(goal)) {
                List<String> shortestPath = reconstructPath(previousCities, currentCity);
                double totalDistance = calculateTotalDistance(shortestPath);
                return new SearchResult(shortestPath, totalDistance);
            }

            for (String neighbor : graph.getNeighbors(currentCity)) {
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                    previousCities.put(neighbor, currentCity);
                }
            }
        }

        return null;
    }
    private List<String> reconstructPath(Map<String, String> previousCities, String currentCity) {
        List<String> path = new ArrayList<>();
        while (currentCity != null) {
            path.add(currentCity);
            currentCity = previousCities.get(currentCity);
        }
        Collections.reverse(path);
        return path;
    }

    private double calculateTotalDistance(List<String> path) {
        double totalDistance = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            String city1 = path.get(i);
            String city2 = path.get(i + 1);
            totalDistance += adjencentCityDistance.calculateDistance(cityMap.get(city1), cityMap.get(city2));
        }
        return totalDistance;
    }


    public class SearchResult {
        private final List<String> path;
        private final double totalDistance;

        public SearchResult(List<String> path, double totalDistance) {
            this.path = path;
            this.totalDistance = totalDistance;
        }

        public List<String> getPath() {
            return path;
        }

        public double getTotalDistance() {
            return totalDistance;
        }
    }
}
