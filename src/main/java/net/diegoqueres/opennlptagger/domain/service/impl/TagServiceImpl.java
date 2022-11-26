package net.diegoqueres.opennlptagger.domain.service.impl;

import net.diegoqueres.opennlptagger.domain.model.Tag;
import net.diegoqueres.opennlptagger.domain.service.TagService;
import org.apache.commons.lang3.StringUtils;

public class TagServiceImpl implements TagService {
        
    @Override
    public String tagIt(Tag tag, String text, int startPos, int endPos) {
        final var sb = new StringBuffer(text);
        tagIt(tag, sb, startPos, endPos);
        return sb.toString();
    }

    @Override
    public void tagIt(Tag tag, StringBuffer text, int startPos, int endPos) {     
        var hasBeginWhiteSpace = StringUtils.isBlank(text.substring(startPos-1, startPos));
        var hasEndWhiteSpace = StringUtils.isBlank(text.substring(endPos, endPos+1));
        
        var pieceToBeWraped = text.substring(startPos, endPos);
        var newTextPiece = String.format("%s%s %s %s%s", (hasBeginWhiteSpace ? "" : " "),
                tag.getOpen(), pieceToBeWraped, tag.getClose(),
                (hasEndWhiteSpace ? "" : " "));
        
        text.replace(startPos, endPos, newTextPiece);     
    }
    
}
