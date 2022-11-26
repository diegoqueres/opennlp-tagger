package net.diegoqueres.opennlptagger.domain.service.impl;

import net.diegoqueres.opennlptagger.domain.exception.InvalidInputException;
import net.diegoqueres.opennlptagger.domain.service.FileService;

import java.io.*;

import static java.util.Objects.isNull;

public class FileServiceImpl implements FileService {
   
    @Override
    public StringBuffer open(File file) throws IOException {
        if (isNull(file))
            throw new InvalidInputException("Input file is mandatory");
        if (!file.exists())
            throw new InvalidInputException("File not found");
        
        var content = new StringBuffer();
        try (var buffReader = new BufferedReader(new FileReader(file))) {
            String text;
            while ((text = buffReader.readLine()) != null) {
                content.append(text).append("\n");
            }
        } 
        
        return content;
    }

    @Override
    public void save(File file, String content) throws IOException {
        if (isNull(file))
            throw new InvalidInputException("Input file is mandatory");
        if (!file.exists())
            throw new FileNotFoundException("File not found");   
        
        try (var out = new BufferedWriter(new PrintWriter(file))) {
            out.write(content);
        }
    }
    
}
