import java.util.*;

public class ID_DFS {
    private final Graph graph;
    private final Map<String, City> cityMap;

    public ID_DFS(Graph graph, Map<String, City> cityMap) {
        this.graph = graph;
        this.cityMap = cityMap;
    }

    public SearchResult findShortestPath(String start, String goal) {
        int maxDepth = 1; // Start with a maximum depth of 1 and increase iteratively

        while (true) {
            Map<String, String> previousCities = new HashMap<>();
            Set<String> visited = new HashSet<>();
            boolean found = depthLimitedDFS(start, goal, maxDepth, previousCities, visited);

            if (found) {
                List<String> path = reconstructPath(previousCities, goal);
                double totalDistance = calculateTotalDistance(path);
                return new SearchResult(path, totalDistance);
            }

            maxDepth++; // Increase the maximum depth for the next iteration
        }
    }

    private boolean depthLimitedDFS(String currentCity, String goal, int maxDepth, Map<String, String> previousCities, Set<String> visited) {
        if (currentCity.equals(goal)) {
            return true;
        }

        if (maxDepth == 0) {
            return false;
        }

        visited.add(currentCity);

        for (String neighbor : graph.getNeighbors(currentCity)) {
            if (!visited.contains(neighbor)) {
                previousCities.put(neighbor, currentCity);
                boolean found = depthLimitedDFS(neighbor, goal, maxDepth - 1, previousCities, visited);
                if (found) {
                    return true;
                }
            }
        }

        visited.remove(currentCity);
        return false;
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
            totalDistance += calculateDistance(cityMap.get(city1), cityMap.get(city2));
        }
        return totalDistance;
    }

    private double calculateDistance(City city1, City city2) {
        // Use Haversine formula to calculate the distance between two cities based on their coordinates
        double lat1 = Math.toRadians(city1.latitude);
        double lon1 = Math.toRadians(city1.longitude);
        double lat2 = Math.toRadians(city2.latitude);
        double lon2 = Math.toRadians(city2.longitude);

        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;

        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double radiusOfEarth = 6371.0; // Earth's radius in kilometers
        return radiusOfEarth * c;
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
