package draft.dev; // Replace with your package

import com.mailgun.api.v1.MailgunLogsApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.logs.LogsPagination;
import com.mailgun.model.logs.LogsResponse;

import com.mailgun.model.logs.LogsRequest;
import com.mailgun.model.logs.LogsResponseItem;



public class Main {

    public static void main(String[] args) {
        // Fetch environment variables
        String apiKey = System.getenv("MAILGUN_API_KEY");
        String domain = System.getenv("MAILGUN_DOMAIN");

        // Create and configure a new Mailgun Client (builder)
        MailgunClient.MailgunClientBuilder mailgunClientBuilder = MailgunClient.config(apiKey);

        // Create an instance of the Mailgun Logs API to reach its endpoints
        MailgunLogsApi mailgunLogsApi = mailgunClientBuilder.createApi(MailgunLogsApi.class);
        // Create an empty list of filter items for your filter
        // Create a logs pagination object that limits query results to three items and sorts them by timestamp
        LogsPagination logsPagination = LogsPagination.builder().limit(3).sort("timestamp:asc").build();

        // Fetch the logs with the domain filter and pagination
        LogsResponse logsResponse = mailgunLogsApi.getLogs(LogsRequest.builder().duration("12h").pagination(logsPagination).build());
        // Print log data to console


        for (LogsResponseItem logsResponseItem : logsResponse.getItems()) {
            System.out.println(logsResponseItem);
        }

    }
}