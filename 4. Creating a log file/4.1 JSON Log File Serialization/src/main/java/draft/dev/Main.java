package draft.dev; // Replace with your package

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mailgun.api.v1.MailgunLogsApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.logs.LogsRequest;
import com.mailgun.model.logs.LogsResponse;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        // Fetch environment variables
        String apiKey = System.getenv("MAILGUN_API_KEY");
        String domain = System.getenv("MAILGUN_DOMAIN");

        // Create and configure a new Mailgun Client (builder)
        MailgunClient.MailgunClientBuilder mailgunClientBuilder = MailgunClient.config(apiKey);

        // Create an instance of the Mailgun Logs API to reach its endpoints
        MailgunLogsApi mailgunLogsApi = mailgunClientBuilder.createApi(MailgunLogsApi.class);
        // Fetch all logs
        LogsResponse logsResponse = mailgunLogsApi.getLogs(LogsRequest.builder().build());

        // Use the Object Mapper to serialize all your logs into a single file
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("metrics.json"), logsResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}