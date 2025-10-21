package dogapi;

import java.util.*;

public class BreedFetcherForLocalTesting implements BreedFetcher {
    private final Map<String, Integer> calls = new HashMap<>();

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        if (breed == null) throw new BreedNotFoundException("null");

        String key = breed.trim().toLowerCase(Locale.ROOT);
        if (!"hound".equals(key)) {
            throw new BreedNotFoundException(breed);
        }

        int n = calls.merge(key, 1, Integer::sum); // 1 on first call, 2+ afterwards
        if (n == 1) {
            // First call returns only two sub-breeds
            return List.of("afghan", "basset");
        } else {
            // Subsequent calls return the expanded list
            return List.of("afghan", "basset", "blood", "english", "ibizan", "plott", "walker");
        }
    }
}