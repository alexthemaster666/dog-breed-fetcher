package dogapi;

import java.util.*;

public class BreedFetcherForLocalTesting implements BreedFetcher {
    private final Map<String, Integer> callsByBreed = new HashMap<>();
    private int callCount = 0; // total times getSubBreeds(...) was called on this instance

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        callCount++; // count every attempt, success or failure

        if (breed == null) throw new BreedNotFoundException("null");
        String key = breed.trim().toLowerCase(Locale.ROOT);

        if (!"hound".equals(key)) {
            // invalid breed → still counted, then throw
            throw new BreedNotFoundException(breed);
        }

        int n = callsByBreed.merge(key, 1, Integer::sum); // 1 on first call
        if (n == 1) {
            // first call returns only two
            return List.of("afghan", "basset");
        } else {
            // subsequent calls return full list
            return List.of("afghan", "basset", "blood", "english", "ibizan", "plott", "walker");
        }
    }

    // ✨ This is what the test looks for
    public int getCallCount() {
        return callCount;
    }
}