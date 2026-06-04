package draft.dev; // Replace with your package

import com.mailgun.api.v1.MailgunLogsApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.logs.LogsResponse;
// For later usage
import com.mailgun.model.Filter;
import com.mailgun.model.FilterItem;

import com.mailgun.model.logs.LogsRequest;
import com.mailgun.model.logs.LogsResponseItem;

import java.util.ArrayList;
import java.util.List;


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
        List<FilterItem> filterItems = new ArrayList<>();
        // Create an empty filter item value list for the filter item
        List<FilterItem.FilterItemValue> filterItemValues = new ArrayList<>();

        // Build a filter item value for domain-filtration and then adds it to the filter items value list
        filterItemValues.add(FilterItem.FilterItemValue.builder().label("domain").value(domain).build());

        //Create a new domain filter item with the domain attribute and then add the filter item values to it
        FilterItem domainFilterItem = FilterItem.builder().values(filterItemValues).attribute("domain").comparator("=").build();

        // Add the newly created domain filter item to the filter items list
        filterItems.add(domainFilterItem);

        // Create a new filter with the domain filter list
        Filter filter = Filter.builder().and(filterItems).build();

        // Fetch the logs with the domain filter
        LogsResponse logsResponse = mailgunLogsApi.getLogs(LogsRequest.builder().duration("12h").includeTotals(false).filter(filter).build());
        // Print log data to console
        for (LogsResponseItem logsResponseItem : logsResponse.getItems()) {
            System.out.println(logsResponseItem);
        }

    }
}