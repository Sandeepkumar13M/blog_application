package com.blog_application.service;

import com.blog_application.model.Tag;
import com.blog_application.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public List<Tag> processTagString(String tagString) {
        if(tagString == null || tagString.trim().isEmpty()) {
            return Collections.emptyList();
        }

        return Arrays.stream(tagString.split(","))
                .map(String::trim)
                .filter(t -> !t.isEmpty())
                .map(this::findOrCreateTag)
                .collect(Collectors.toList());
    }

    private Tag findOrCreateTag(String name) {
        return tagRepository.findByName(name)
                .orElseGet(() -> tagRepository.save(new Tag(name)));
    }

    public Optional<Tag> findByName(String name) {
        return tagRepository.findByName(name);
    }
}
