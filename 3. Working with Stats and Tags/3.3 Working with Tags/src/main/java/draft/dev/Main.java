package draft.dev;

import com.mailgun.api.v1.MailgunAnalyticsTagsApi;
import com.mailgun.api.v1.MailgunLogsApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.tags.AnalyticsTagItem;
import com.mailgun.model.tags.AnalyticsTagListRequest;
import com.mailgun.model.tags.AnalyticsTagListResponse;
import com.mailgun.model.tags.AnalyticsTagUpdateRequest;

public class Main {
    public static void main (String[] args) {

        String apiKey = System.getenv("MAILGUN_API_KEY");
        String domain = System.getenv("MAILGUN_DOMAIN");

        // Create and configure a new Mailgun Client (builder)
        MailgunClient.MailgunClientBuilder mailgunClientBuilder = MailgunClient.config(apiKey);

        // Create an instance of the Mailgun Logs API to reach its endpoints
        MailgunLogsApi mailgunLogsApi = mailgunClientBuilder.createApi(MailgunLogsApi.class);
        MailgunAnalyticsTagsApi mailgunAnalyticsTagsApi = mailgunClientBuilder.createApi(MailgunAnalyticsTagsApi.class);
        AnalyticsTagListResponse analyticsTagListResponse = mailgunAnalyticsTagsApi.listTags(AnalyticsTagListRequest.builder().build());


        for (AnalyticsTagItem analyticsTagItem :analyticsTagListResponse.getItems()) {
            System.out.println(analyticsTagItem.getTag());
        }

    }
}
