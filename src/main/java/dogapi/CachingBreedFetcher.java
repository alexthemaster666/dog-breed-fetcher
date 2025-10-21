package dogapi;

import java.util.*;

public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher delegate;
    private final Map<String, List<String>> cache = new HashMap<>();
    private int callsMade = 0;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        if (fetcher == null) throw new IllegalArgumentException("fetcher cannot be null");
        this.delegate = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // Normalize key so "Hound", "hound ", etc. hit the same entry
        String key = (breed == null) ? "" : breed.trim().toLowerCase(Locale.ROOT);

        // Cache hit → return cached list
        List<String> cached = cache.get(key);
        if (cached != null) return cached;

        // Cache miss → we MUST call the delegate and count it,
        // even if the delegate will throw (invalid breed).
        callsMade++;
        try {
            List<String> subs = delegate.getSubBreeds(breed);
            // Cache successful results only (including empty list)
            List<String> toCache = Collections.unmodifiableList(new ArrayList<>(subs));
            cache.put(key, toCache);
            return toCache;
        } catch (BreedNotFoundException e) {
            // Do NOT cache failures; propagate the exception
            throw e;
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}