package realstaq.component.test.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Objects;

//
// Instead of using House model, Any is used, but this is left here in case it's needed later on.
//

@JsonIgnoreProperties(ignoreUnknown = true)
public class House {

    private String id;
    private int mls_id;
    private String mls_listing_id;
    private String property_type;
    private String formatted_address;
    private String address;
    private String zip;
    private String city;
    private String state;
    private List<Double> location;
    private int bedrooms;
    private int bathrooms;
    private String list_date;
    private String mls_update_date;
    private String price_display;
    private int price;
    private int square_feet;

    public String getId() {
        return id;
    }

    public int getMls_id() {
        return mls_id;
    }

    public String getMls_listing_id() {
        return mls_listing_id;
    }

    public String getProperty_type() {
        return property_type;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public String getAddress() {
        return address;
    }

    public String getZip() {
        return zip;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public List<Double> getLocation() {
        return location;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public String getList_date() {
        return list_date;
    }

    public String getMls_update_date() {
        return mls_update_date;
    }

    public String getPrice_display() {
        return price_display;
    }

    public int getPrice() {
        return price;
    }

    public int getSquare_feet() {
        return square_feet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        House house = (House) o;
        return mls_id == house.mls_id &&
                bedrooms == house.bedrooms &&
                bathrooms == house.bathrooms &&
                price == house.price &&
                square_feet == house.square_feet &&
                Objects.equals(id, house.id) &&
                Objects.equals(mls_listing_id, house.mls_listing_id) &&
                Objects.equals(property_type, house.property_type) &&
                Objects.equals(formatted_address, house.formatted_address) &&
                Objects.equals(address, house.address) &&
                Objects.equals(zip, house.zip) &&
                Objects.equals(city, house.city) &&
                Objects.equals(state, house.state) &&
                Objects.equals(location, house.location) &&
                Objects.equals(list_date, house.list_date) &&
                Objects.equals(mls_update_date, house.mls_update_date) &&
                Objects.equals(price_display, house.price_display);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mls_id, mls_listing_id, property_type, formatted_address, address, zip, city, state, location, bedrooms, bathrooms, list_date, mls_update_date, price_display, price, square_feet);
    }

    @Override
    public String toString() {
        return "House{" +
                "id='" + id + '\'' +
                ", mls_id=" + mls_id +
                ", mls_listing_id='" + mls_listing_id + '\'' +
                ", property_type='" + property_type + '\'' +
                ", formatted_address='" + formatted_address + '\'' +
                ", address='" + address + '\'' +
                ", zip='" + zip + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", location=" + location +
                ", bedrooms=" + bedrooms +
                ", bathrooms=" + bathrooms +
                ", list_date='" + list_date + '\'' +
                ", mls_update_date='" + mls_update_date + '\'' +
                ", price_display='" + price_display + '\'' +
                ", price=" + price +
                ", square_feet=" + square_feet +
                '}';
    }
}
