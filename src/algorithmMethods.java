import java.util.List;

public class algorithmMethods {

    static class Algorithm {
        private final String algorithm;
        private final String startCity;
        private final String goalCity;
        DisplayResults displayResults = new DisplayResults();

        public Algorithm(String algorithm, String startCity, String goalCity) {
            this.algorithm = algorithm;
            this.startCity = startCity;
            this.goalCity = goalCity;
        }

        // choice for algorithm
        public void execute() {
            switch (algorithm) {
                case "A*":
                    runAStar();
                    break;
                case "Best First":
                    runBestFirst();
                    break;
                case "Breadth First":
                    runBreadthFirst();
                    break;
                case "Brute Force":
                    runBruteForce();
                    break;
                case "Depth First":
                    runDepthFirst();
                    break;
                case "ID-DFS":
                    runIDDFS();
                    break;
            }
        }

        private void runAStar() {

            AStarSearch aStar = new AStarSearch(Search.cityAdjacency, Search.coordinates);

            long startTime = System.nanoTime();
            List<String> result = aStar.Path(startCity, goalCity);
            long endTime = System.nanoTime();
            double executionTime = (endTime - startTime) / 1e6; // Convert to milliseconds

            if (result != null) {
                double totalDistance = 0.0;

                for (int i = 0; i < result.size() - 1; i++) {
                    String city1 = result.get(i);
                    String city2 = result.get(i + 1);
                    totalDistance += adjencentCityDistance.calculateDistance(Search.coordinates.get(city1), Search.coordinates.get(city2));
                }
                displayResults.ErrorMessage("A*", result, totalDistance, executionTime);
            }
        }

        private void runBruteForce() {
            BruteForce bruteForce = new BruteForce(startCity, goalCity);

            List<String> path = bruteForce.findPath(startCity, goalCity);

            if (path != null) {

                long startTime = System.nanoTime();
                double totalDistance = BruteForce.calculateTotalDistance(path);
                long endTime = System.nanoTime();
                double executionTime = (endTime - startTime) / 1e6; // Convert to milliseconds

                displayResults.ErrorMessage("Method Brute " , path,totalDistance, executionTime);

            }
        }

        private void runBreadthFirst() {
            BreadthFirstSearch breadthFirstSearch = new BreadthFirstSearch(Search.cityAdjacency, Search.coordinates);

            long startTime = System.nanoTime();
            BreadthFirstSearch.SearchResult result = breadthFirstSearch.findPath(startCity, goalCity);
            long endTime = System.nanoTime();
            double executionTime = (endTime - startTime) / 1e6; // Convert to milliseconds

            if (result != null) {
                List<String> Path = result.getPath();
                double totalDistance = result.getTotalDistance();

                displayResults.ErrorMessage("Breadth First", Path, totalDistance, executionTime);
            }
        }

        private void runDepthFirst() {
            DepthFirstSearch depthFirstSearch = new DepthFirstSearch(Search.cityAdjacency, Search.coordinates);
            long startTime = System.nanoTime();
            DepthFirstSearch.SearchResult result = depthFirstSearch.findPath(startCity, goalCity);
            long endTime = System.nanoTime();
            double executionTime = (endTime - startTime) / 1e6; // Convert to milliseconds

            if (result != null) {
                List<String> Path = result.getPath();
                double totalDistance = result.getTotalDistance();

                displayResults.ErrorMessage("Depth First", Path, totalDistance, executionTime);
            }
        }

        private void runIDDFS() {

            ID_DFS idDfs = new ID_DFS(Search.cityAdjacency, Search.coordinates);

            long startTime = System.nanoTime();
            ID_DFS.SearchResult PathResult = idDfs.findPath(startCity, goalCity);
            long endTime = System.nanoTime();
            double executionTime = (endTime - startTime) / 1e6; // Convert to milliseconds

            if (PathResult != null) {
                List<String> Path = PathResult.getPath();
                double totalDistance = PathResult.getTotalDistance();

                displayResults.ErrorMessage("ID-DFS", Path, totalDistance, executionTime);
            }
        }

        private void runBestFirst() {

            BestFirstSearch bestFirstSearch = new BestFirstSearch(Search.cityAdjacency, Search.coordinates);

            long startTime = System.nanoTime();
            BestFirstSearch.SearchResult result = bestFirstSearch.findPath(startCity, goalCity);
            long endTime = System.nanoTime();
            double executionTime = (endTime - startTime) / 1e6; // Convert to milliseconds

            if (result != null) {
                List<String> Path = result.getPath();
                double totalDistance = result.getTotalDistance();

                displayResults.ErrorMessage("Best First", Path, totalDistance, executionTime);
            }
        }


    }
}
