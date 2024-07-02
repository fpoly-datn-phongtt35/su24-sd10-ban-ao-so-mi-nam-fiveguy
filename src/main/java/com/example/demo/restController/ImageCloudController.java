package com.example.demo.restController;

import com.example.demo.service.ImageCloud;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ImageCloudController {

    @Autowired
    private ImageCloud imageCloud;


    @PostMapping("/rest/upload")
    public JsonNode upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File is missing");
        }

        String url = imageCloud.saveImage(file);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode response = mapper.createObjectNode();
        response.put("name", url);
        return response;
    }


    @PostMapping("/rest/uploadListImage")
    public List<JsonNode> upload(@RequestParam("files") List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("Files are missing");
        }

        List<JsonNode> responses = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        for (MultipartFile file : files) {
            String url = imageCloud.saveImage(file);
            ObjectNode response = mapper.createObjectNode();
            response.put("name", url);
            responses.add(response);
        }

        return responses;
    }

}
