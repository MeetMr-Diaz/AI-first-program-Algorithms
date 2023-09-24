import java.util.*;

class BruteForceSearch {
    private final Graph graph;
    private final Map<String, City> cityMap;

    public BruteForceSearch(Graph graph, Map<String, City> cityMap) {
        this.graph = graph;
        this.cityMap = cityMap;
    }

    public Object[] findShortestPath(String start, String goal) {
        // Implement Dijkstra's algorithm here
        PriorityQueue<CityDistance> queue = new PriorityQueue<>(Comparator.comparingDouble(cd -> cd.distance));
        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previousCities = new HashMap<>();

        for (String city : cityMap.keySet()) {
            distances.put(city, Double.POSITIVE_INFINITY);
        }
        distances.put(start, 0.0);
        queue.offer(new CityDistance(start, 0.0));

        while (!queue.isEmpty()) {
            CityDistance current = queue.poll();
            String currentCity = current.city;

            if (currentCity.equals(goal)) {
                Object[] pathAndDistance = reconstructPath(previousCities, currentCity, distances.get(currentCity));
                return pathAndDistance;
            }

            for (String neighbor : graph.getNeighbors(currentCity)) {
                if (cityMap.containsKey(neighbor)) { // Check if neighbor exists in cityMap
                    double tentativeDistance = distances.get(currentCity) + calculateDistance(cityMap.get(currentCity), cityMap.get(neighbor));

                    if (tentativeDistance < distances.get(neighbor)) {
                        distances.put(neighbor, tentativeDistance);
                        previousCities.put(neighbor, currentCity);
                        queue.offer(new CityDistance(neighbor, tentativeDistance));
                    }
                }
            }
        }

        // If the loop finishes and goal is not reached, return null to indicate no path
        return null;
    }

    private Object[] reconstructPath(Map<String, String> previousCities, String currentCity, double totalDistance){
        List<String> path = new ArrayList<>();

        while (currentCity != null) {
            path.add(currentCity);
            String previousCity = previousCities.get(currentCity);
            if (previousCity != null) {
                totalDistance += calculateDistance(cityMap.get(currentCity), cityMap.get(previousCity));
            }
            currentCity = previousCity;
        }

        Collections.reverse(path);
        return new Object[]{path, totalDistance};

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

    private static class CityDistance {
        String city;
        double distance;

        public CityDistance(String city, double distance) {
            this.city = city;
            this.distance = distance;
        }
    }
}
