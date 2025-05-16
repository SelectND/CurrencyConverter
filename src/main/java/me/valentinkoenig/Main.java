package me.valentinkoenig;

import org.json.JSONException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Scanner;

/**
 * The {@code Main} class serves as the entry point for the currency converter application.
 * It interacts with the user to gather currency conversion inputs, communicates with
 * the {@code CurrencyRequest} class to fetch exchange rates, and displays the converted amount.
 */
public class Main {
    /**
     * The main method executes the currency conversion process.
     * It prompts the user for input, validates the input, makes an API request,
     * and outputs the converted amount.
     *
     * @param args Command-line arguments (not used).
     * @throws IOException If an I/O error occurs during API communication.
     * @throws JSONException If an error occurs while parsing the API response.
     */
    public static void main(String[] args) throws IOException, JSONException, URISyntaxException {
        // Instantiate the CurrencyRequest object
        CurrencyRequest currencyRequest = new CurrencyRequest();

        // Display the application banner
        System.out.println("Currency Converter (c) 2025 Valentin König");

        // Initialize a scanner for user input
        Scanner currencyScanner = new Scanner(System.in);

        // Prompt user for the base currency
        System.out.println("Enter base currency code: (e.g., EUR, USD, GBP)");
        currencyRequest.setBaseCurrency(currencyScanner.nextLine());

        // Prompt user for the target currency
        System.out.println("Enter target currency code: (e.g., EUR, USD, GBP)");
        currencyRequest.setTargetCurrency(currencyScanner.nextLine());

        // Validate that base and target currencies are not the same
        if (Objects.equals(currencyRequest.getBaseCurrency(), currencyRequest.getTargetCurrency())) {
            currencyScanner.close();
            throw new IllegalArgumentException("The base currency and target currency are the same");
        }

        // Prompt user for the base amount
        System.out.println("Enter amount in base currency: ");
        String baseAmountLine = currencyScanner.nextLine();
        double baseAmount = Double.parseDouble(baseAmountLine);
        currencyRequest.setBaseAmount(baseAmount);

        // Close scanner after input handling
        currencyScanner.close();

        // Make the request to FreeCurrencyAPI using the CurrencyRequest instance
        double exchangeRate = currencyRequest.makeRequest();

        // Display the conversion result
        System.out.println(currencyRequest.getBaseAmount() +
                " in " + currencyRequest.getBaseCurrency() +
                " equals " + (currencyRequest.getBaseAmount() * exchangeRate) +
                " in " + currencyRequest.getTargetCurrency());
    }
}