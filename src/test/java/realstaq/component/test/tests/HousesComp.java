package realstaq.component.test.tests;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import realstaq.component.test.constants.Endpoints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static realstaq.component.test.constants.TestSuiteNames.RETRIEVE_HOUSES;
import static realstaq.component.test.constants.Uri.URI;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HousesComp {

    private static final Logger LOGGER = LoggerFactory.getLogger(HousesComp.class);
    private List<Any> allHousesResponse, filteredHousesResponse;
    private List<String> cities;
    private String filteredCity;
    private int priceLowerLimit, priceUpperLimit;
    private MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

    @BeforeAll
    void setup() {

        // Set url of the API:
        RestAssured.baseURI = URI;

        // Retrieve all houses and find and populate lists of all cities and prices.
        allHousesResponse = retrieveAllFilteredHouses(queryParams);
        cities = new ArrayList<>();
        List<Integer> prices = new ArrayList<>();
        for (Any h: allHousesResponse) {
            String city = h.get("city").toString();
            cities.add(city);
            int price = h.get("price").toInt();
            prices.add(price);
        }

        // Get min and max prices of houses.
        priceLowerLimit = getMin(prices);
        priceUpperLimit = getMax(prices);
    }

    @BeforeEach
    void beforeMethod() {

        // Get random city.
        filteredCity = getRandomCity(cities);

        // Clear query parameter list.
        queryParams.clear();
    }

    @DisplayName("Retrieve houses by city and price range")
    @Test
    void retrieve_houses_by_city_and_price_range() {

        LOGGER.info("{} -> Entered GET houses filtered by city and price range test.", RETRIEVE_HOUSES);

        // Set up filters / query parameters.
        queryParams.add("city", filteredCity);
        queryParams.add("price_gte", String.valueOf(priceLowerLimit));
        queryParams.add("price_lte", String.valueOf(priceUpperLimit));

        // Retrieve all houses filtered by city and lower and upper price limit and assert the response.
        filteredHousesResponse = retrieveAllFilteredHouses(queryParams);
        assertRetrievedHouses(filteredHousesResponse, filteredCity, priceLowerLimit, priceUpperLimit);
    }

    @DisplayName("Retrieve houses by city only")
    @Test
    void retrieve_houses_by_city_only() {

        LOGGER.info("{} -> Entered GET houses filtered by city only test.", RETRIEVE_HOUSES);

        // Set up city filter / query parameter.
        queryParams.add("city", filteredCity);

        // Retrieve all houses with city filter only and assert the response.
        filteredHousesResponse = retrieveAllFilteredHouses(queryParams);
        assertRetrievedHouses(filteredHousesResponse, filteredCity, priceLowerLimit, priceUpperLimit);
    }

    @DisplayName("Retrieve houses by lower price limit only")
    @Test
    void retrieve_houses_by_lower_price_limit_only() {

        LOGGER.info("{} -> Entered GET houses filtered by lower price limit only test.", RETRIEVE_HOUSES);

        // Set up lower price limit filter / query parameter.
        queryParams.add("price_gte", String.valueOf(priceLowerLimit));

        // Retrieve all houses filtered by lower price limit only and assert the response.
        filteredHousesResponse = retrieveAllFilteredHouses(queryParams);
        Assertions.assertEquals(filteredHousesResponse, allHousesResponse);
    }

    @DisplayName("Retrieve houses by upper price limit only")
    @Test
    void retrieve_houses_by_upper_price_limit_only() {

        LOGGER.info("{} -> Entered GET houses filtered by upper price limit only test.", RETRIEVE_HOUSES);

        // Set up upper price limit filter / query parameter.
        queryParams.add("price_lte", String.valueOf(priceUpperLimit));

        // Retrieve all houses filtered by upper price limit only and assert the response.
        filteredHousesResponse = retrieveAllFilteredHouses(queryParams);
        Assertions.assertEquals(filteredHousesResponse, allHousesResponse);
    }

    @DisplayName("Retrieve houses by city and lower price limit")
    @Test
    void retrieve_houses_by_city_and_lower_price_limit() {

        LOGGER.info("{} -> Entered GET houses filtered by city and lower price limit test.", RETRIEVE_HOUSES);

        // Set up filters / query parameters.
        queryParams.add("city", filteredCity);
        queryParams.add("price_gte", String.valueOf(priceLowerLimit));

        // Retrieve all houses with city and lower price limit filters and assert the response.
        filteredHousesResponse = retrieveAllFilteredHouses(queryParams);
        assertRetrievedHouses(filteredHousesResponse, filteredCity, priceLowerLimit, priceUpperLimit);
    }

    @DisplayName("Retrieve houses by city and upper price limit")
    @Test
    void retrieve_houses_by_city_and_upper_price_limit() {

        LOGGER.info("{} -> Entered GET houses filtered by city and upper price limit test.", RETRIEVE_HOUSES);

        // Set up filters / query parameters.
        queryParams.add("city", filteredCity);
        queryParams.add("price_lte", String.valueOf(priceUpperLimit));

        // Retrieve all houses with city and upper price limit filters and assert the response.
        filteredHousesResponse = retrieveAllFilteredHouses(queryParams);
        assertRetrievedHouses(filteredHousesResponse, filteredCity,0, priceUpperLimit);
    }

    @DisplayName("Retrieve houses by price range")
    @Test
    void retrieve_houses_by_price_range() {

        LOGGER.info("{} -> Entered GET houses filtered by price range test.", RETRIEVE_HOUSES);

        // Set up filters / query parameters.
        queryParams.add("price_gte", String.valueOf(priceLowerLimit));
        queryParams.add("price_lte", String.valueOf(priceUpperLimit));

        // Retrieve all houses with price range filters and assert the response.
        filteredHousesResponse = retrieveAllFilteredHouses(queryParams);
        Assertions.assertEquals(filteredHousesResponse, allHousesResponse);
    }

    @DisplayName("Retrieve houses with minimum price value greater than maximum price value")
    @Test
    void retrieve_houses_with_minimum_price_greater_than_maximum_price() {

        LOGGER.info("{} -> Entered GET houses with minimum price greater than maximum price test", RETRIEVE_HOUSES);

        // Set up filters / query parameters.
        queryParams.add("price_gte", String.valueOf(priceUpperLimit));
        queryParams.add("price_lte", String.valueOf(priceLowerLimit));

        // Retrieve all houses with given filter and assert the response.
        // In this case, there should probably be error displayed because upper price limit is lower than
        // lower price limit filter, but instead we have empty response. This was intended to be a negative test scenario.
        filteredHousesResponse = retrieveAllFilteredHouses(queryParams);
        Assertions.assertTrue(filteredHousesResponse.isEmpty());
    }

    @DisplayName("Retrieve houses with non existing city")
    @Test
    void retrieve_houses_with_non_existing_city() {

        LOGGER.info("{} -> Entered GET houses with non existing city test", RETRIEVE_HOUSES);

        // Set up non existing city filter / query parameter.
        queryParams.add("city", "xyz");

        // Retrieve houses with non existing city and assert that response is empty.
        filteredHousesResponse = retrieveAllFilteredHouses(queryParams);
        Assertions.assertTrue(filteredHousesResponse.isEmpty());
    }

    @DisplayName("Retrieve houses by lower price limit greater than actual maximum price")
    @Test
    void retrieve_houses_by_lower_price_limit_greater_than_maximum_price() {

        LOGGER.info("{} -> Entered GET houses filtered by lower price limit greater than maximum price test.", RETRIEVE_HOUSES);

        // Set up lower price limit filter / query parameter.
        queryParams.add("price_gte", String.valueOf(priceUpperLimit+1));

        // Retrieve all houses filtered by lower price greater than actual maximum price and assert the response.
        filteredHousesResponse = retrieveAllFilteredHouses(queryParams);
        Assertions.assertTrue(filteredHousesResponse.isEmpty());
    }

    @DisplayName("Retrieve houses by upper price limit lesser than actual minimum price")
    @Test
    void retrieve_houses_by_upper_price_limit_lesser_than_minimum_price() {

        LOGGER.info("{} -> Entered GET houses filtered by upper price limit lesser than minimum price test.", RETRIEVE_HOUSES);

        // Set up upper price limit filter / query parameter.
        queryParams.add("price_lte", String.valueOf(priceLowerLimit-1));

        // Retrieve all houses filtered by upper price lesser than actual minimum price and assert the response.
        filteredHousesResponse = retrieveAllFilteredHouses(queryParams);
        Assertions.assertTrue(filteredHousesResponse.isEmpty());
    }

    //
    // Methods are put in this class as they are only used here.
    //

    /**
     * Method will send request to retrieve all houses for provided filters.
     *
     * @param queryParams query parameters.
     * @return {@link Any} list of houses that match filters.
     */
    private static List<Any> retrieveAllFilteredHouses(MultiValueMap<String, String> queryParams) {
        Response response = RestAssured.given()
                .queryParams(queryParams)
                .accept(ContentType.JSON)
                .get(Endpoints.HOUSES)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();
        return JsonIterator.deserialize(response.asString()).asList();
    }

    /**
     * Method will assert houses with provided city, lower price limit and upper price limit.
     *
     * @param houses list of houses.
     * @param city city of the house.
     * @param priceLowerLimit lower price limit.
     * @param priceUpperLimit upper price limit.
     */
    private void assertRetrievedHouses(List<Any> houses, String city, int priceLowerLimit, int priceUpperLimit) {
        SoftAssertions softly = new SoftAssertions();
        for (Any h: houses) {
            softly.assertThat(h.get("city").toString()).as("Cities do not match").isEqualTo(city);
            softly.assertThat(h.get("price").toInt()).as("Price is not between %s and %s", priceLowerLimit, priceUpperLimit).isBetween(priceLowerLimit, priceUpperLimit);
            softly.assertAll();
        }
    }

    /**
     * Method will pick random city from the list of cities and return it.
     *
     * @param cities list of cities.
     * @return random city.
     */
    private static String getRandomCity(List<String> cities)
    {
        Random rand = new Random();
        return cities.get(rand.nextInt(cities.size()));
    }

    /**
     *  Method will find the element with minimum value in a list.
     *
     * @param list for which we want to find element with minimum value.
     * @return element with minimum value.
     */
    private static Integer getMin(List<Integer> list)
    {
        if (list == null || list.size() == 0) {
            return Integer.MAX_VALUE;
        }

        return Collections.min(list);
    }

    /**
     *  Method will find the element with maximum value in a list.
     *
     * @param list for which we want to find element with maximum value.
     * @return element with maximum value.
     */
    public static Integer getMax(List<Integer> list)
    {
        if (list == null || list.size() == 0) {
            return Integer.MIN_VALUE;
        }

        return Collections.max(list);
    }
}
