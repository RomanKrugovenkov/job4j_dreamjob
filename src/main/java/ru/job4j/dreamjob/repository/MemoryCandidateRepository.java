package ru.job4j.dreamjob.repository;

import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.*;

public class MemoryCandidateRepository implements CandidateRepository {
    private static final MemoryCandidateRepository INSTANCE = new MemoryCandidateRepository();
    private int nextId = 1;
    private final Map<Integer, Candidate> candidates = new HashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Roman", "Intern Java Developer", LocalDateTime.now()));
        save(new Candidate(0, "Sergey", "Junior Java Developer", LocalDateTime.now()));
        save(new Candidate(0, "Petr", "Junior+ Java Developer", LocalDateTime.now()));
        save(new Candidate(0, "Ivan", "Middle Java Developer", LocalDateTime.now()));
        save(new Candidate(0, "Olga", "Middle+ Java Developer", LocalDateTime.now()));
        save(new Candidate(0, "Daria", "Senior Java Developer", LocalDateTime.now()));
    }

    public static MemoryCandidateRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId++);
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public void deleteById(int id) {
        candidates.remove(id);
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(),
                (id, oldCandidate) -> new Candidate(
                        oldCandidate.getId(),
                        candidate.getName(),
                        candidate.getDescription(),
                        candidate.getCreationDate()))
                != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
