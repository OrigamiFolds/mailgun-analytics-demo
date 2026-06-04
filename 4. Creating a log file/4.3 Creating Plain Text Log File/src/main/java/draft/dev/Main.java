package draft.dev;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mailgun.api.v1.MailgunAccountMetricsApi;
import com.mailgun.api.v1.MailgunAnalyticsTagsApi;
import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.api.v3.suppression.MailgunSuppressionBouncesApi;
import com.mailgun.api.v3.suppression.MailgunSuppressionComplaintsApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.api.v1.MailgunLogsApi;
import com.mailgun.model.Filter;
import com.mailgun.model.FilterItem;

import com.mailgun.model.logs.LogsPagination;
import com.mailgun.model.logs.LogsRequest;
import com.mailgun.model.logs.LogsResponse;
import com.mailgun.model.logs.LogsResponseItem;

import com.mailgun.model.message.Message;
import com.mailgun.model.message.MessageResponse;
import com.mailgun.model.metrics.*;

import com.mailgun.model.suppression.bounces.BouncesResponse;
import com.mailgun.model.suppression.complaints.ComplaintsItemResponse;
import com.mailgun.model.tags.AnalyticsTagListRequest;
import com.mailgun.model.tags.AnalyticsTagListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // Fetch environment variables
        String apiKey = System.getenv("MAILGUN_API_KEY");
        String domain = System.getenv("MAILGUN_DOMAIN");


        // Create and configure a new Mailgun Client (builder)
        MailgunClient.MailgunClientBuilder mailgunClientBuilder = MailgunClient.config(apiKey);

        // Create an instance of the Mailgun Logs API to reach its endpoints
        MailgunLogsApi mailgunLogsApi = mailgunClientBuilder.createApi(MailgunLogsApi.class);


        LogsResponse logsResponse = mailgunLogsApi.getLogs(LogsRequest.builder().duration("24h").build());

        // Connect a new MailgunSuppressionBouncesApi client
        MailgunSuppressionBouncesApi mailgunSuppressionBouncesApi = mailgunClientBuilder.createApi(MailgunSuppressionBouncesApi.class);
        // Fetch all addresses from the bounce list
        BouncesResponse bouncesResponse = mailgunSuppressionBouncesApi.getBounces(domain);

        // Connect a new MailgunSuppressionComplaintsApi client
        MailgunSuppressionComplaintsApi mailgunSuppressionComplaintsApi = mailgunClientBuilder.createApi(MailgunSuppressionComplaintsApi.class);
        // Fetch all addresses from complaints list


        ComplaintsItemResponse complaintsItemResponse = mailgunSuppressionComplaintsApi.getAllComplaints(domain);

        MailgunAccountMetricsApi mailgunAccountMetricsAPI = mailgunClientBuilder.createApi(MailgunAccountMetricsApi.class);
        // Fetch all account metrics
        MetricsResponse<AccountMetrics> metricsResponse = mailgunAccountMetricsAPI.getMetrics(MetricsRequest.builder().build());

        // Create a timestamp string for your file
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Build the filename with timestamp
        String filename = "mailgun-health-" + timestamp + ".txt";

        // Use a Java's I/O to write response object contents to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("-----------------------------\n");
            writer.write("-----  LOG DATA -----");
            writer.write("-----------------------------\n");
            for (LogsResponseItem item : logsResponse.getItems()) {
                writer.write("ID: " + item.getId() + "\n");
                writer.write("Event: " + item.getEvent() + "\n");
                writer.write("Recipient: " + item.getRecipient() + "\n");
                writer.write("Timestamp: " + item.getTimestamp() + "\n");
                writer.write("-----------------------------\n");
            }

            writer.write("-----------------------------\n");
            writer.write("-----  SUPPRESSION LIST COUNT -----");
            writer.write("-----------------------------\n");
            writer.write("Bounce List Size: " + bouncesResponse.getItems().size() + "\n");
            writer.write("Complaints List Size: " + complaintsItemResponse.getItems().size() + "\n");
            // Add code for Unsubscribe list size here
            writer.write("-----------------------------\n");

            writer.write("-----------------------------\n");
            writer.write("-----  AGGREGATES AND STATS  -----");
            writer.write("-----------------------------\n");

            for (MetricsItem <AccountMetrics>  item : metricsResponse.getItems()) {
                writer.write("Delivered Rate: " + item.getMetrics().getDeliveredRate() + "\n");
                writer.write("Failed Count: " + item.getMetrics().getFailedCount() + "\n");
                writer.write("Sent Count: " + item.getMetrics().getSentCount() + "\n");
                writer.write("Webhook Count: " + item.getMetrics().getWebhookCount() + "\n");

                // Add more desired fields
                writer.write("-----------------------------\n");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Initialize MailgunMessagesAPI
        MailgunMessagesApi mailgunMessagesApi = mailgunClientBuilder.createApi(MailgunMessagesApi.class);
        // Send message with file as attachment
        MessageResponse messageResponse = mailgunMessagesApi.sendMessage(domain, Message.builder().from("postmaster@mailgun.gqamagroup.com").subject("Here's the daily log file").text("Hi Mdu! Please find the email log file attached to this message").attachment(new File(filename)).to("mdu.sibisi@zoho.com").build());
        System.out.println(messageResponse);

    }
}