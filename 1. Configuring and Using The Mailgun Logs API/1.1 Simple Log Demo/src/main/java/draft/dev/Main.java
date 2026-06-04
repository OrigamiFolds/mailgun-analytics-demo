package draft.dev; // Replace with your package

import com.mailgun.client.MailgunClient;
import com.mailgun.api.v1.MailgunLogsApi;

import com.mailgun.api.v1.MailgunLogsApi;
import com.mailgun.model.logs.LogsResponse;

import com.mailgun.model.Filter;
import com.mailgun.model.FilterItem;
import com.mailgun.model.logs.LogsPagination;
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
        // Fetch all logs
        LogsResponse logsResponse = mailgunLogsApi.getLogs(LogsRequest.builder().build());
        // Print entire log to the console
        System.out.print(logsResponse);

    }
}