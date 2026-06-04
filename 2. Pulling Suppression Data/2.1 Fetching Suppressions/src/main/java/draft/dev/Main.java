package draft.dev;

import com.mailgun.api.v3.suppression.MailgunSuppressionBouncesApi;
import com.mailgun.api.v3.suppression.MailgunSuppressionComplaintsApi;

import com.mailgun.client.MailgunClient;
import com.mailgun.model.suppression.bounces.BouncesResponse;
import com.mailgun.model.suppression.complaints.ComplaintsItem;
import com.mailgun.model.suppression.complaints.ComplaintsItemResponse;

public class Main {

    public static void main (String[] args) {
        String apiKey = System.getenv("MAILGUN_API_KEY");
        String domain = System.getenv("MAILGUN_DOMAIN");

        // Create and configure a new Mailgun Client (builder)
        MailgunClient.MailgunClientBuilder mailgunClientBuilder = MailgunClient.config(apiKey);

        MailgunSuppressionBouncesApi mailgunSuppressionBouncesApi = mailgunClientBuilder.createApi(MailgunSuppressionBouncesApi.class);
        // Fetch all addresses from the bounce list
        BouncesResponse bouncesResponse = mailgunSuppressionBouncesApi.getBounces(domain);


        // Connect a new MailgunSuppressionComplaintsApi client
        MailgunSuppressionComplaintsApi mailgunSuppressionComplaintsApi = mailgunClientBuilder.createApi(MailgunSuppressionComplaintsApi.class);

        // Fetch all addresses from complaints list
        ComplaintsItemResponse complaintsItemResponse = mailgunSuppressionComplaintsApi.getAllComplaints(domain);

        for (ComplaintsItem complaintsItem: complaintsItemResponse.getItems())  {
            System.out.println(complaintsItem.getAddress());
        }

    }
}
