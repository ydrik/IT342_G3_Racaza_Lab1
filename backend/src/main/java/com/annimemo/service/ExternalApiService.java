package com.annimemo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * External API Service
 * FRS Feature 4.1: External API Integration
 * Consumes public APIs for dog and cat breed information.
 * Provides static curated breed and fact data for bird, rabbit, hamster, and fish.
 */
@Service
public class ExternalApiService {

    private final RestTemplate restTemplate;
    private static final String DOG_API_BASE_URL = "https://api.thedogapi.com/v1";
    private static final String CAT_API_BASE_URL = "https://api.thecatapi.com/v1";
    private static final String DOG_FACTS_API_URL = "https://dogapi.dog/api/v2/facts";
    private static final String CAT_FACTS_API_URL = "https://catfact.ninja/fact";

    public ExternalApiService() {
        this.restTemplate = new RestTemplate();
    }

    // -------------------------------------------------------------------------
    // Static breed and fact data for species without dedicated public APIs
    // -------------------------------------------------------------------------

    private static final List<Map<String, Object>> BIRD_BREEDS = List.of(
            Map.of("id", "bird-1", "name", "Budgerigar (Budgie)", "description", "A small, long-tailed, seed-eating parrot. One of the most popular pet birds worldwide.", "lifespan", "5-10 years", "origin", "Australia"),
            Map.of("id", "bird-2", "name", "Cockatiel", "description", "A small parrot native to Australia known for its crest and whistling ability.", "lifespan", "10-15 years", "origin", "Australia"),
            Map.of("id", "bird-3", "name", "African Grey Parrot", "description", "Highly intelligent parrot known for exceptional mimicry and problem-solving ability.", "lifespan", "40-60 years", "origin", "Central Africa"),
            Map.of("id", "bird-4", "name", "Lovebird", "description", "Small, social parrot species known for their affectionate nature toward their mates.", "lifespan", "10-15 years", "origin", "Africa and Madagascar"),
            Map.of("id", "bird-5", "name", "Conure", "description", "Medium-sized, playful parrots known for their loud calls and affectionate behavior.", "lifespan", "15-30 years", "origin", "Central and South America"),
            Map.of("id", "bird-6", "name", "Macaw", "description", "Large, colorful parrots known for their intelligence and vibrant plumage.", "lifespan", "50-80 years", "origin", "Central and South America"),
            Map.of("id", "bird-7", "name", "Cockatoo", "description", "Known for their expressive crests and highly social, affectionate nature.", "lifespan", "40-70 years", "origin", "Australia and Indonesia"),
            Map.of("id", "bird-8", "name", "Canary", "description", "Small songbird prized for its melodious singing, available in many color varieties.", "lifespan", "10-15 years", "origin", "Canary Islands"),
            Map.of("id", "bird-9", "name", "Zebra Finch", "description", "Small, active seed-eating birds that thrive in pairs and are known for their soft chirping.", "lifespan", "5-10 years", "origin", "Australia"),
            Map.of("id", "bird-10", "name", "Amazon Parrot", "description", "Vocal, intelligent parrots with excellent talking ability and strong personalities.", "lifespan", "25-75 years", "origin", "South America and Caribbean")
    );

    private static final List<Map<String, Object>> RABBIT_BREEDS = List.of(
            Map.of("id", "rabbit-1", "name", "Holland Lop", "description", "A small rabbit with lopped ears, known for its gentle and friendly temperament.", "lifespan", "7-14 years", "weight", "2-4 lbs"),
            Map.of("id", "rabbit-2", "name", "Mini Lop", "description", "A compact rabbit with lopped ears, very popular as a house pet due to its calm nature.", "lifespan", "5-10 years", "weight", "4-6 lbs"),
            Map.of("id", "rabbit-3", "name", "Lionhead", "description", "Distinguished by a wool mane around its head, playful and social.", "lifespan", "7-10 years", "weight", "2.5-3.75 lbs"),
            Map.of("id", "rabbit-4", "name", "Dutch", "description", "One of the oldest domestic rabbit breeds, known for its distinctive color pattern.", "lifespan", "5-8 years", "weight", "3.5-5.5 lbs"),
            Map.of("id", "rabbit-5", "name", "Flemish Giant", "description", "One of the largest rabbit breeds, known for its docile temperament and large size.", "lifespan", "5-10 years", "weight", "13+ lbs"),
            Map.of("id", "rabbit-6", "name", "Rex", "description", "Known for its plush, velvety fur. Calm and good with children.", "lifespan", "5-6 years", "weight", "7.5-10.5 lbs"),
            Map.of("id", "rabbit-7", "name", "Angora", "description", "Produces long, silky wool and requires regular grooming to prevent matting.", "lifespan", "7-12 years", "weight", "5-12 lbs"),
            Map.of("id", "rabbit-8", "name", "Netherland Dwarf", "description", "One of the smallest rabbit breeds with a compact body and short ears.", "lifespan", "10-12 years", "weight", "1.1-2.5 lbs"),
            Map.of("id", "rabbit-9", "name", "Mini Rex", "description", "Miniature version of the Rex with the same velvety fur, popular as a house pet.", "lifespan", "5-7 years", "weight", "3-4.5 lbs"),
            Map.of("id", "rabbit-10", "name", "American Rabbit", "description", "A large breed known for its mandolin body shape and gentle personality.", "lifespan", "8-12 years", "weight", "9-12 lbs")
    );

    private static final List<Map<String, Object>> HAMSTER_BREEDS = List.of(
            Map.of("id", "hamster-1", "name", "Syrian Hamster", "description", "Also called the Golden Hamster, the most common pet hamster. Solitary and territorial.", "lifespan", "2-3 years", "size", "5-7 inches"),
            Map.of("id", "hamster-2", "name", "Dwarf Campbell Russian Hamster", "description", "One of the smallest hamster breeds, more social than Syrians and can be kept in pairs.", "lifespan", "1.5-2 years", "size", "2-4 inches"),
            Map.of("id", "hamster-3", "name", "Dwarf Winter White Russian Hamster", "description", "Changes fur to white in winter months. Social and fast-moving.", "lifespan", "1.5-2 years", "size", "2-4 inches"),
            Map.of("id", "hamster-4", "name", "Roborovski Dwarf Hamster", "description", "The smallest and fastest hamster species, best observed rather than handled frequently.", "lifespan", "3-3.5 years", "size", "1.5-2 inches"),
            Map.of("id", "hamster-5", "name", "Chinese Hamster", "description", "Slender build with a longer tail than most hamsters. Can become quite tame with regular handling.", "lifespan", "2-3 years", "size", "3-5 inches")
    );

    private static final List<Map<String, Object>> FISH_BREEDS = List.of(
            Map.of("id", "fish-1", "name", "Betta (Siamese Fighting Fish)", "description", "Vibrant, flowing fins with brilliant colors. Males must be kept alone as they are territorial.", "lifespan", "3-5 years", "care", "Beginner"),
            Map.of("id", "fish-2", "name", "Goldfish", "description", "One of the oldest domesticated fish, available in many varieties. Produces significant waste.", "lifespan", "10-15 years", "care", "Beginner"),
            Map.of("id", "fish-3", "name", "Guppy", "description", "Hardy, colorful, and easy to breed. One of the most popular aquarium fish worldwide.", "lifespan", "2-3 years", "care", "Beginner"),
            Map.of("id", "fish-4", "name", "Neon Tetra", "description", "Small, peaceful schooling fish with a vivid blue and red stripe.", "lifespan", "5-10 years", "care", "Beginner"),
            Map.of("id", "fish-5", "name", "Angelfish", "description", "Elegant, triangular-shaped cichlid native to the Amazon basin. Semi-aggressive.", "lifespan", "10-15 years", "care", "Intermediate"),
            Map.of("id", "fish-6", "name", "Oscar", "description", "Large, intelligent cichlid capable of recognizing its owner. Requires a spacious tank.", "lifespan", "10-20 years", "care", "Intermediate"),
            Map.of("id", "fish-7", "name", "Plecostomus", "description", "Armored catfish popular for algae control in aquariums. Can grow very large.", "lifespan", "10-15 years", "care", "Intermediate"),
            Map.of("id", "fish-8", "name", "Molly", "description", "Hardy, peaceful livebearers available in many color variants.", "lifespan", "3-5 years", "care", "Beginner"),
            Map.of("id", "fish-9", "name", "Zebra Danio", "description", "Hardy schooling fish with horizontal blue and silver stripes. Great for beginners.", "lifespan", "5-7 years", "care", "Beginner"),
            Map.of("id", "fish-10", "name", "Koi", "description", "Large ornamental carp kept in outdoor ponds, prized for their vibrant patterns and colors.", "lifespan", "25-35 years", "care", "Intermediate")
    );

    private static final List<String> BIRD_FACTS = List.of(
            "Budgerigars are one of the most popular pet birds in the world.",
            "Cockatiels can live up to 20 years with proper care.",
            "African Grey Parrots are considered among the most intelligent birds, capable of learning hundreds of words.",
            "Some parrots mate for life and can experience grief if their partner passes away.",
            "Birds need mental stimulation and social interaction to stay healthy and prevent feather plucking.",
            "Canaries were historically used in coal mines to detect toxic gases like carbon monoxide.",
            "Macaws can live as long as 80 years, potentially outliving their owners.",
            "Birds have hollow bones that reduce their weight and make flight possible.",
            "Parrots use their beak as a third limb to climb and manipulate objects.",
            "Many pet bird species form strong bonds with their owners and can suffer from loneliness when left alone."
    );

    private static final List<String> RABBIT_FACTS = List.of(
            "Rabbits are social animals that thrive in pairs or groups and can suffer from loneliness when isolated.",
            "A healthy rabbit can live 8 to 12 years with proper indoor care.",
            "Rabbits need at least 3 to 4 hours of exercise outside their enclosure every day.",
            "Rabbits are obligate herbivores; hay should make up at least 80% of their diet.",
            "A rabbit's teeth never stop growing, which is why constant chewing on hay keeps them at the proper length.",
            "Rabbits communicate through body language: thumping signals danger, binkying signals joy.",
            "Rabbits are crepuscular, meaning they are most active at dawn and dusk.",
            "Rabbits can be litter box trained in a similar way to cats.",
            "A group of rabbits is called a colony or a herd.",
            "Rabbits should never be picked up by their ears as it causes pain and can injure their spine."
    );

    private static final List<String> HAMSTER_FACTS = List.of(
            "Hamsters are nocturnal animals and are most active during nighttime hours.",
            "Syrian hamsters are solitary and must always be housed alone to prevent fighting.",
            "Hamsters have cheek pouches that can stretch to nearly three times the size of their head to carry food.",
            "A hamster can run 5 to 8 miles in a single night on its wheel.",
            "Wild hamsters dig complex underground burrow systems with separate rooms for sleeping and food storage.",
            "Hamsters have poor eyesight but compensate with an excellent sense of smell and hearing.",
            "Hamsters can enter a state of torpor (light hibernation) if kept in cold temperatures.",
            "The name hamster comes from the German word hamstern, meaning to hoard.",
            "Roborovski dwarf hamsters are the smallest and fastest of the hamster species.",
            "Hamsters are naturally curious animals that benefit greatly from environmental enrichment in their habitat."
    );

    private static final List<String> FISH_FACTS = List.of(
            "Betta fish breathe air directly from the surface using a labyrinth organ, allowing them to survive in low-oxygen water.",
            "Goldfish do not have a three-second memory; studies show they can remember things for months.",
            "Clownfish are protandrous hermaphrodites: if the dominant female of a group dies, the male changes sex.",
            "Fish communicate through vibrations, low-frequency sounds, and electrical signals in some species.",
            "Guppies can adapt to a wide range of water conditions, making them ideal for beginner fishkeepers.",
            "Proper water quality, temperature, and filtration are critical to preventing disease in pet fish.",
            "Koi can recognize their owners and have been known to eat from their hands.",
            "A school of fish can react to threats in milliseconds by sensing pressure changes through their lateral line.",
            "The elaborate fins of Betta fish were selectively bred by humans over hundreds of years.",
            "Some fish species like the Oscar cichlid can recognize their owners and respond to their presence."
    );

    /**
     * Get dog breeds from The Dog API
     * FRS Feature 4.1: Consume real public API
     */
    public List<Map<String, Object>> getDogBreeds() {
        try {
            String url = DOG_API_BASE_URL + "/breeds";
            Map<String, Object>[] response = restTemplate.getForObject(url, Map[].class);
            return response != null ? List.of(response) : List.of();
        } catch (RestClientException e) {
            System.err.println("Error fetching dog breeds: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Get cat breeds from The Cat API
     * FRS Feature 4.1: Consume real public API
     */
    public List<Map<String, Object>> getCatBreeds() {
        try {
            String url = CAT_API_BASE_URL + "/breeds";
            Map<String, Object>[] response = restTemplate.getForObject(url, Map[].class);
            return response != null ? List.of(response) : List.of();
        } catch (RestClientException e) {
            System.err.println("Error fetching cat breeds: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Search for dog breed by name
     * FRS Feature 4.1: Display data from external API
     */
    public Map<String, Object> searchDogBreed(String breedName) {
        try {
            String url = DOG_API_BASE_URL + "/breeds/search?q=" + breedName;
            Map<String, Object>[] response = restTemplate.getForObject(url, Map[].class);
            
            if (response != null && response.length > 0) {
                return response[0];
            }
            return new HashMap<>();
        } catch (RestClientException e) {
            System.err.println("Error searching dog breed: " + e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * Search for cat breed by name
     * FRS Feature 4.1: Display data from external API
     */
    public Map<String, Object> searchCatBreed(String breedName) {
        try {
            String url = CAT_API_BASE_URL + "/breeds/search?q=" + breedName;
            Map<String, Object>[] response = restTemplate.getForObject(url, Map[].class);
            
            if (response != null && response.length > 0) {
                return response[0];
            }
            return new HashMap<>();
        } catch (RestClientException e) {
            System.err.println("Error searching cat breed: " + e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * Get random dog image
     * FRS Feature 4.1: External API integration
     */
    public Map<String, Object> getRandomDogImage() {
        try {
            String url = DOG_API_BASE_URL + "/images/search";
            Map<String, Object>[] response = restTemplate.getForObject(url, Map[].class);
            
            if (response != null && response.length > 0) {
                return response[0];
            }
            return new HashMap<>();
        } catch (RestClientException e) {
            System.err.println("Error fetching random dog image: " + e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * Get random cat image
     * FRS Feature 4.1: External API integration
     */
    public Map<String, Object> getRandomCatImage() {
        try {
            String url = CAT_API_BASE_URL + "/images/search";
            Map<String, Object>[] response = restTemplate.getForObject(url, Map[].class);
            
            if (response != null && response.length > 0) {
                return response[0];
            }
            return new HashMap<>();
        } catch (RestClientException e) {
            System.err.println("Error fetching random cat image: " + e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * Get random dog fact from a public API.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getRandomDogFact() {
        try {
            Map<String, Object> response = restTemplate.getForObject(DOG_FACTS_API_URL, Map.class);
            if (response == null) {
                return Map.of("species", "dog", "fact", "No fact available right now.", "source", "dogapi.dog");
            }

            Object dataObj = response.get("data");
            if (dataObj instanceof List<?> dataList && !dataList.isEmpty() && dataList.get(0) instanceof Map<?, ?> factWrapper) {
                Object attributesObj = ((Map<String, Object>) factWrapper).get("attributes");
                if (attributesObj instanceof Map<?, ?> attributes && attributes.get("body") != null) {
                    return Map.of(
                            "species", "dog",
                            "fact", String.valueOf(attributes.get("body")),
                            "source", "dogapi.dog"
                    );
                }
            }

            return Map.of("species", "dog", "fact", "No fact available right now.", "source", "dogapi.dog");
        } catch (RestClientException e) {
            System.err.println("Error fetching dog fact: " + e.getMessage());
            return Map.of("species", "dog", "fact", "Could not load dog fact right now.", "source", "dogapi.dog");
        }
    }

    /**
     * Get random cat fact from a public API.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getRandomCatFact() {
        try {
            Map<String, Object> response = restTemplate.getForObject(CAT_FACTS_API_URL, Map.class);
            if (response != null && response.get("fact") != null) {
                return Map.of(
                        "species", "cat",
                        "fact", String.valueOf(response.get("fact")),
                        "source", "catfact.ninja"
                );
            }
            return Map.of("species", "cat", "fact", "No fact available right now.", "source", "catfact.ninja");
        } catch (RestClientException e) {
            System.err.println("Error fetching cat fact: " + e.getMessage());
            return Map.of("species", "cat", "fact", "Could not load cat fact right now.", "source", "catfact.ninja");
        }
    }

    /**
     * Get random bird fact from static data.
     */
    public Map<String, Object> getRandomBirdFact() {
        String fact = BIRD_FACTS.get(ThreadLocalRandom.current().nextInt(BIRD_FACTS.size()));
        return Map.of("species", "bird", "fact", fact, "source", "AnniMemo Pet Encyclopedia");
    }

    /**
     * Get random rabbit fact from static data.
     */
    public Map<String, Object> getRandomRabbitFact() {
        String fact = RABBIT_FACTS.get(ThreadLocalRandom.current().nextInt(RABBIT_FACTS.size()));
        return Map.of("species", "rabbit", "fact", fact, "source", "AnniMemo Pet Encyclopedia");
    }

    /**
     * Get random hamster fact from static data.
     */
    public Map<String, Object> getRandomHamsterFact() {
        String fact = HAMSTER_FACTS.get(ThreadLocalRandom.current().nextInt(HAMSTER_FACTS.size()));
        return Map.of("species", "hamster", "fact", fact, "source", "AnniMemo Pet Encyclopedia");
    }

    /**
     * Get random fish fact from static data.
     */
    public Map<String, Object> getRandomFishFact() {
        String fact = FISH_FACTS.get(ThreadLocalRandom.current().nextInt(FISH_FACTS.size()));
        return Map.of("species", "fish", "fact", fact, "source", "AnniMemo Pet Encyclopedia");
    }

    /**
     * Get breeds for any supported species.
     * Dogs and cats use live external APIs; other species use curated static data.
     */
    public List<Map<String, Object>> getBreedsBySpecies(String species) {
        return switch (species.toLowerCase()) {
            case "dog" -> getDogBreeds();
            case "cat" -> getCatBreeds();
            case "bird" -> new ArrayList<>(BIRD_BREEDS);
            case "rabbit" -> new ArrayList<>(RABBIT_BREEDS);
            case "hamster" -> new ArrayList<>(HAMSTER_BREEDS);
            case "fish" -> new ArrayList<>(FISH_BREEDS);
            default -> List.of();
        };
    }

    /**
     * Search breed by species and name.
     * Dogs and cats use live external APIs; other species search through static data.
     */
    public Map<String, Object> searchBreedBySpecies(String species, String name) {
        return switch (species.toLowerCase()) {
            case "dog" -> searchDogBreed(name);
            case "cat" -> searchCatBreed(name);
            case "bird" -> BIRD_BREEDS.stream()
                    .filter(b -> b.get("name").toString().toLowerCase().contains(name.toLowerCase()))
                    .findFirst().orElse(Map.of());
            case "rabbit" -> RABBIT_BREEDS.stream()
                    .filter(b -> b.get("name").toString().toLowerCase().contains(name.toLowerCase()))
                    .findFirst().orElse(Map.of());
            case "hamster" -> HAMSTER_BREEDS.stream()
                    .filter(b -> b.get("name").toString().toLowerCase().contains(name.toLowerCase()))
                    .findFirst().orElse(Map.of());
            case "fish" -> FISH_BREEDS.stream()
                    .filter(b -> b.get("name").toString().toLowerCase().contains(name.toLowerCase()))
                    .findFirst().orElse(Map.of());
            default -> Map.of();
        };
    }

    /**
     * Get random pet fact for any supported species.
     */
    public Map<String, Object> getRandomPetFact(String species) {
        return switch (species.toLowerCase()) {
            case "dog" -> getRandomDogFact();
            case "cat" -> getRandomCatFact();
            case "bird" -> getRandomBirdFact();
            case "rabbit" -> getRandomRabbitFact();
            case "hamster" -> getRandomHamsterFact();
            case "fish" -> getRandomFishFact();
            default -> {
                // "any" or unknown: randomly pick from all supported species
                int pick = ThreadLocalRandom.current().nextInt(6);
                yield switch (pick) {
                    case 0 -> getRandomDogFact();
                    case 1 -> getRandomCatFact();
                    case 2 -> getRandomBirdFact();
                    case 3 -> getRandomRabbitFact();
                    case 4 -> getRandomHamsterFact();
                    default -> getRandomFishFact();
                };
            }
        };
    }
}
