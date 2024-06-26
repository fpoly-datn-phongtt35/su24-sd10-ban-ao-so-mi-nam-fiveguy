package com.example.demo.restController.Customer;

import org.springframework.web.bind.annotation.RestController;

@RestController

public class ImageCloudController {
//    @Autowired
//    ImageCloud imageCloud;
//    @PostMapping("/rest/upload")
//    public JsonNode upload(@PathParam("file") MultipartFile file)  throws IOException {
//        String url = imageCloud.saveImage(file);
//        ObjectMapper mapper = new ObjectMapper();
//        ObjectNode response = mapper.createObjectNode();
//        response.put("name", url);
//        return response;
//    }
//
//    @PostMapping("/rest/uploadListImage")
//    public List<JsonNode> upload(@RequestParam("files") List<MultipartFile> files) throws IOException {
//        List<JsonNode> responses = new ArrayList<>();
//
//        for (MultipartFile file : files) {
//            String url = imageCloud.saveImage(file);
//            ObjectMapper mapper = new ObjectMapper();
//            ObjectNode response = mapper.createObjectNode();
//            response.put("name", url);
//            responses.add(response);
//        }
//
//        return responses;
//    }
}
