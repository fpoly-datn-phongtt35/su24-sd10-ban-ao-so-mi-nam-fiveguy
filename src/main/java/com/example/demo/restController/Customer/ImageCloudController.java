package com.example.demo.restController.Customer;

import com.example.demo.service.Customer.ServiceImpl.ImageCloud;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController

public class ImageCloudController {
    @Autowired
    ImageCloud imageCloud;
    @PostMapping("/rest/upload")
    public JsonNode upload(@PathParam("file") MultipartFile file)  throws IOException {
        String url = imageCloud.saveImage(file);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode response = mapper.createObjectNode();
        response.put("name", url);
        return response;
    }

    @PostMapping("/rest/uploadListImage")
    public List<JsonNode> upload(@RequestParam("files") List<MultipartFile> files) throws IOException {
        List<JsonNode> responses = new ArrayList<>();

        for (MultipartFile file : files) {
            String url = imageCloud.saveImage(file);
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode response = mapper.createObjectNode();
            response.put("name", url);
            responses.add(response);
        }

        return responses;
    }
}
