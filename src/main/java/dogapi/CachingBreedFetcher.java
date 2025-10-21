package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher delegate;
    private final Map<String, List<String>> cache = new HashMap<>();
    private int callsMade = 0;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        if (fetcher == null) {
            throw new IllegalArgumentException("fetcher cannot be null");
        }
        this.delegate = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        String key = (breed == null) ? "" : breed.toLowerCase(Locale.ROOT);

        // Cache hit
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        // Cache miss → delegate
        callsMade++;
        try {
            List<String> subs = delegate.getSubBreeds(breed);
            // Store an unmodifiable copy to keep cache stable
            subs = Collections.unmodifiableList(new ArrayList<>(subs));
            cache.put(key, subs);
            return subs;
        } catch (BreedNotFoundException e) {
            // Do NOT cache failures
            throw e;
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}