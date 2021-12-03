package au.edu.unsw.infs3634.recommendlunch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

// TODO for Question 1b)
// Fix all the bugs, provide comments that explain the bugs and how your code fixes them.
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {
    //changed from ArrayList to List to match with Retrofit format
    private List<Restaurant> mRestaurants;
    private RecyclerViewClickListener mListener;

    public RestaurantAdapter(List<Restaurant> restaurants, RecyclerViewClickListener listener) {
        mRestaurants = restaurants;
        mListener = listener;
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int id);
    }

    @NonNull
    @Override
    public RestaurantAdapter.RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //changed from activity_detail xml to list_row xml to display correct view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new RestaurantViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantAdapter.RestaurantViewHolder holder, int position) {
        //changed from get(0) to get(position) to display all restaurants, not only the first one
        Restaurant restaurant = mRestaurants.get(position);

        holder.name.setText(restaurant.getName());
        holder.location.setText(restaurant.getLocation());
        //changed tag from name to id for eventual v.getTag() usage for intent from MainActivity to DetailActivity
        //DetailActivity uses the id instead of name for API call using RestaurantServices
        holder.itemView.setTag(restaurant.getId());
        //changed from holder.itemView to holder.image to reference correct xml component
        //getName() into getImage() to get correct image link for the jpg file
        Glide.with(holder.image)
                .load(restaurant.getImage())
                .fitCenter()
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        //changed from 0 to mRestaurants.size() to display the correct number of list items
        return mRestaurants.size();
    }

    //added implementation of View.OnClickListener for View to have listener to respond to user interaction
    public static class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView image;
        public TextView name, location;
        private RecyclerViewClickListener listener;

        public RestaurantViewHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
            super(itemView);

            this.listener = listener;
            //changed listener to this for context
            itemView.setOnClickListener(this);

            image = itemView.findViewById(R.id.ivCover);
            //change tvLocation to tvName to reference correct xml component
            name = itemView.findViewById(R.id.tvName);
            location = itemView.findViewById(R.id.tvLocation);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, (int) v.getTag());
        }
    }

    public void setData(List<Restaurant> data) {
        mRestaurants.clear();
        //changed null to data to set adapter data into the list of restaurants provided in the input (from API call in MainActivity)
        mRestaurants.addAll(data);
        //changed notify to notifyDataSetChanged()
        notifyDataSetChanged();
    }
}
