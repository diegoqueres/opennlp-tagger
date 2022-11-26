package net.diegoqueres.opennlptagger.domain.service;

import java.io.File;
import java.io.IOException;

public interface FileService {
    
    StringBuffer open(File file) throws IOException;
    void save(File file, String content) throws IOException;
    
}
