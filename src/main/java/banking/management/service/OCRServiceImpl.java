package banking.management.service;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class OCRServiceImpl implements OCRService {
    public String getOCR() {
        String url = "http://digital-data-transformation-production.eba-8vhmze7m.ap-south-1.elasticbeanstalk.com/api/ocr/v1/getAdjudicationJson?ticketId=1027213";
        HttpResponse<JsonNode> response = Unirest
                .get(url)
                .header("access_key", "token eqIbv902I6vLu6BDsHLLFOJvRMHM3KlE")
                .header("Content-Type", "application/json")
                .asJson();

        kong.unirest.json.JSONObject responseBody = response.getBody().getObject();

        JSONObject data = responseBody.getJSONObject("data");

//        JSONArray array = data.getJSONArray("data");

        return data.toString();
    }
}
