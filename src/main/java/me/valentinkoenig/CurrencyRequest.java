package me.valentinkoenig;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
// import java.io.Reader; // needed for HTTP Code handeling (currently passive)
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * The {@code CurrencyRequest} class handles currency conversion requests using the FreeCurrencyAPI.
 * It allows setting base and target currencies, specifying amounts, making API requests, and
 * parsing the responses to retrieve exchange rates.
 */
public class CurrencyRequest {
    private String baseCurrency; // Base currency to be converted from
    private String targetCurrency; // Target currency to be converted to
    private double baseAmount; // Amount in base currency to be converted
    private double targetAmount; // Converted amount in target currency
    public String[] supportedCurrencies = { "EUR", "USD", "JPY", "BGN", "CZK", "DKK", "GBP", "HUF", "PLN", "RON",
            "SEK", "CHF", "ISK", "NOK", "HRK", "RUB", "TRY", "AUD", "BRL", "CAD",
            "CNY", "HKD", "IDR", "ILS", "INR", "KRW", "MXN", "MYR", "NZD", "PHP",
            "SGD", "THB", "ZAR"
    }; // List of supported currencies

    /**
     * Constructs the API URL for fetching the exchange rate.
     *
     * @return A string representing the API request URL.
     */
    private String buildURL() {
        // API Key for FreeCurrencyAPI
        String API_KEY = ApiKeys.FREECURRENCYAPI_KEY;
        return "https://api.freecurrencyapi.com/v1/latest?apikey=" + API_KEY +
                "&currencies=" + targetCurrency +
                "&base_currency=" + baseCurrency;
    }

    /**
     * Sets the base currency for conversion.
     *
     * @param baseCurrency The base currency to be converted from.
     * @throws IllegalArgumentException if the currency is invalid.
     */
    public void setBaseCurrency(String baseCurrency) {
        if (!(baseCurrency == null || baseCurrency.isEmpty()) &&
                Arrays.asList(supportedCurrencies).contains(baseCurrency)) {
            this.baseCurrency = baseCurrency;
        } else {
            throw new IllegalArgumentException("baseCurrency '" + baseCurrency + "' does not exist");
        }
    }

    /**
     * Sets the target currency for conversion.
     *
     * @param targetCurrency The target currency to be converted to.
     * @throws IllegalArgumentException if the currency is invalid.
     */
    public void setTargetCurrency(String targetCurrency) {
        if (!(targetCurrency == null || targetCurrency.isEmpty()) &&
                Arrays.asList(supportedCurrencies).contains(targetCurrency)) {
            this.targetCurrency = targetCurrency;
        } else {
            throw new IllegalArgumentException("targetCurrency '" + targetCurrency + "' does not exist");
        }
    }

    /**
     * Sets the amount to be converted.
     *
     * @param baseAmount The amount in the base currency.
     */
    public void setBaseAmount(double baseAmount) {
        this.baseAmount = baseAmount;
    }

    public String getBaseCurrency() { return this.baseCurrency; }

    public String getTargetCurrency() { return this.targetCurrency; }

    public double getBaseAmount() { return this.baseAmount; }

    public double getTargetAmount() { return this.targetAmount; }

    public String[] getSupportedCurrencies() { return this.supportedCurrencies; }

    /**
     * Makes a request to the FreeCurrencyAPI service to retrieve the exchange rate.
     *
     * @return The exchange rate for the target currency.
     * @throws IOException If an I/O error occurs.
     * @throws JSONException If parsing the response fails.
     */
    public double makeRequest() throws IOException, JSONException, URISyntaxException {
        URI uri = new URI(buildURL()); // Uses the generated API request URL
        URL url = uri.toURL();
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setConnectTimeout(5000);
        httpURLConnection.setReadTimeout(5000);
        // HTTP Code handeling (currently passive)
        /*int status = httpURLConnection.getResponseCode();
        Reader streamReader;

        // Handles HTTP status codes
        if (status > 299) {
            streamReader = new InputStreamReader(httpURLConnection.getErrorStream());
        } else {
            streamReader = new InputStreamReader(httpURLConnection.getInputStream());
        }*/

        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        StringBuilder content = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        httpURLConnection.disconnect();
        return parseRequest(content.toString());
    }

    /**
     * Parses the API response and extracts the exchange rate.
     *
     * @param origRequest The JSON response string from the API.
     * @return The extracted exchange rate.
     * @throws JSONException If JSON parsing fails.
     */
    private double parseRequest(String origRequest) throws JSONException {
        JSONObject jsonString = new JSONObject(origRequest);
        JSONObject dataObject = jsonString.getJSONObject("data");
        Object variableKey = dataObject.keys().next();
        return dataObject.getDouble(variableKey.toString());
    }
}