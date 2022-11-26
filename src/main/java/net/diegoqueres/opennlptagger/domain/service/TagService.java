package net.diegoqueres.opennlptagger.domain.service;

import net.diegoqueres.opennlptagger.domain.model.Tag;

public interface TagService {
    
    String tagIt(Tag tag, String text, int startPos, int endPos);
    void tagIt(Tag tag, StringBuffer text, int startPos, int endPos);
    
}
