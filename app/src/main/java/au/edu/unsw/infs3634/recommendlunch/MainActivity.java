package au.edu.unsw.infs3634.recommendlunch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RestaurantAdapter mAdapter;
    //moved declarations to top
    private RecyclerView rvList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvList = findViewById(R.id.rvList);
        rvList.setHasFixedSize(true);

        RestaurantAdapter.RecyclerViewClickListener listener = new RestaurantAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.INTENT_EXTRA, id);
                startActivity(intent);
            }
        };

        mAdapter = new RestaurantAdapter(new ArrayList<Restaurant>(), listener);
        rvList.setAdapter(mAdapter);

        //puts API call on background thread to prevent freezing up UI
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://pqd82925.pythonanywhere.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();


                // TODO for Question 1a)
                // Implement code that creates a Retrofit call object to receive the list of all restaurants

                //Instantiate RestaurantService class using Retrofit - combines start and end of url query for API call
                RestaurantService service = retrofit.create(RestaurantService.class);
                //initiates call
                Call<List<Restaurant>> restaurantsCall = service.getRestaurants();
                //what to do during call
                restaurantsCall.enqueue(new Callback<List<Restaurant>>() {
                    @Override
                    public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                        //creates a list of restaurant objects to hold the data retrieved from API call - converting JSON into Java objects
                        List<Restaurant> restaurants = response.body();
                        // Implement code that makes a Retrofit API call and updates the adapter with the received data
                        //sets list into Adapter to display on RecyclerView
                        mAdapter.setData(restaurants);
                    }

                    @Override
                    public void onFailure(Call<List<Restaurant>> call, Throwable t) {

                    }
                });
            }
        });



    }
}
