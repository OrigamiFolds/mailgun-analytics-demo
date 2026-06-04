package draft.dev;


import com.mailgun.api.v3.suppression.MailgunSuppressionBouncesApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.metrics.AccountMetrics;
import com.mailgun.model.metrics.MetricsItem;
import com.mailgun.model.metrics.MetricsRequest;
import com.mailgun.api.v1.MailgunAccountMetricsApi;
import com.mailgun.model.metrics.MetricsResponse;

public class Main {

    public static void main (String[] args) {
        String apiKey = System.getenv("MAILGUN_API_KEY");
        String domain = System.getenv("MAILGUN_DOMAIN");

        // Create and configure a new Mailgun Client (builder)
        MailgunClient.MailgunClientBuilder mailgunClientBuilder = MailgunClient.config(apiKey);
        MailgunAccountMetricsApi mailgunAccountMetricsApi = mailgunClientBuilder.createApi(MailgunAccountMetricsApi.class);
        MetricsResponse<AccountMetrics> metricsResponse = mailgunAccountMetricsApi.getMetrics(MetricsRequest.builder().build());

        for (MetricsItem<AccountMetrics> metricsItem : metricsResponse.getItems()) {
            System.out.println("Sent count for each domain: " + metricsItem.getMetrics().getSentCount());
        }

    }
}
