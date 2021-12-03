package au.edu.unsw.infs3634.recommendlunch;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestaurantService {
    // Return a list of all restaurants
    @GET("/restaurants")
    Call<List<Restaurant>> getRestaurants();

    // TODO for Question 1a)
    // Implement code for a second API endpoint "/restaurants/{id}
    // Details about the API can be found here: https://pqd82925.pythonanywhere.com/
    @GET("/restaurants/{id}")
    Call<Restaurant> getRestaurant(@Path("id") int id);


    // TODO for Question 1a)
    // Implement code for a third API endpoint "/restaurants/location/{location}
    // Details about the API can be found here: https://pqd82925.pythonanywhere.com/
    @GET("restaurants/location/{location}")
    Call<Restaurant> getRestaurant(@Path("location") String location);

}
