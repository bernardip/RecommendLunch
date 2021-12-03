package au.edu.unsw.infs3634.recommendlunch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.reflect.Executable;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {
    public static final String INTENT_EXTRA = "id";
    //declare xml variables
    private ImageView ivDetail;
    private TextView tvDetailName;
    private TextView tvDetailLocation;
    private TextView tvDetailCuisine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // TODO for Question 1c)
        // Complete the DetailActivity class so that it displays all details about a restaurant
        // and integrates with all other classes of the app.
        // Implement the activity_detail.xml file accordingly

        //link variables to xml components
        ivDetail = findViewById(R.id.ivDetail);
        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailLocation = findViewById(R.id.tvDetailLocation);
        tvDetailCuisine = findViewById(R.id.tvDetailCuisine);

        //gets intent from MainActivity
        Intent intent = getIntent();
        //gets message from MainActivity
        int restaurantId = intent.getIntExtra(INTENT_EXTRA,0);

        //Puts API call on background thread to avoid freezing up UI
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                //retrofit boiler code
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://pqd82925.pythonanywhere.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RestaurantService service = retrofit.create(RestaurantService.class);
                //makes API call with id from intent message for specific restaurant
                Call<Restaurant> restaurantCall = service.getRestaurant(restaurantId);

                restaurantCall.enqueue(new Callback<Restaurant>() {
                    @Override
                    public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                        //saves JSON from API as Java object
                        Restaurant restaurant = response.body();
                        //use Java object to set xml components
                        Glide.with(ivDetail)
                                .load(restaurant.getImage())
                                .fitCenter()
                                .into(ivDetail);
                        tvDetailName.setText(restaurant.getName());
                        tvDetailLocation.setText(restaurant.getLocation());

                        //converts List into a String to display all cuisines on TextView
                        StringBuilder builder = new StringBuilder();
                        for (String cuisine : restaurant.getCuisine()) {
                            builder.append(cuisine + "\n");
                        }
                        tvDetailCuisine.setText(builder.toString());
                    }

                    @Override
                    public void onFailure(Call<Restaurant> call, Throwable t) {

                    }
                });
            }
        });

    }

}