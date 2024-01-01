package axolotls.latest.collectiondesign;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavouriteActivity extends AppCompatActivity implements FavouriteAdapter.OnItemClickListener {
    private String receivedId; // Declare receivedId here
    String deviceId;
    RecyclerView recyclerview2;
    ImageView back; ImageView playstore;
    private FavouriteAdapter favouriteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        initViews();

        Intent intent = getIntent();
        if (intent != null) {
            // Retrieve data using intent.getStringExtra(), intent.getIntExtra(), etc.
            // For example:
            receivedId = intent.getStringExtra("id");

            // Use the receivedId as needed
            // Example: Log.d("BlouseDesigns", "Received ID: " + receivedId);
        }

        apicall3();

        favouriteAdapter = new FavouriteAdapter(new ArrayList<>(), this);
        recyclerview2.setAdapter(favouriteAdapter);
        recyclerview2.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void initViews() {

        back = findViewById(R.id.back); playstore = findViewById(R.id.playstore);
        recyclerview2 = findViewById(R.id.recyclerview2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        playstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "https://play.google.com/store/apps/details?id=axolotls.latest.collectiondesign";

                // Create an intent with the ACTION_VIEW action and the URL
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                // Verify that there is an app available to handle the intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    // Start the activity if there is an app available
                    startActivity(intent);
                }

            }
        });

    }

    private void apicall3() {
        // Your existing code for making API request
        // Modify the params or URL as needed based on the clicked category ID
        String withdraw_request = "https://mdsvisions.com/system/api/get_fvrt";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new StringRequest(Request.Method.POST, withdraw_request,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("hhhhhhhh", "efsdc" + response);

                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            // Check the status in the response
                            String status = jsonResponse.getString("status");

                            if ("success".equals(status)) {
                                // The "data" key contains an array of favorites
                                JSONArray dataArray = jsonResponse.getJSONArray("data");

                                List<HomeModel> dataList = new ArrayList<>();

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject jsonObject = dataArray.getJSONObject(i);

                                    String id = jsonObject.getString("id");
                                    String name = jsonObject.getString("name");
                                    String img = jsonObject.getString("img");
                                    String favuorite = jsonObject.getString("img");


                                    Log.d("scedc", img);
                                    Log.d("ImageUrl", "Image URL: " + img);

                                    HomeModel homeModel = new HomeModel(id, name, img,favuorite);
                                    dataList.add(homeModel);
                                }

                                // Set the data to your adapter
                                favouriteAdapter.setData(dataList);
                            } else {
                                // Handle the case where status is not "success"
                                String message = jsonResponse.getString("message");
                                Log.e("JSONError", "Error in JSON response: " + message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSONError", "Error parsing JSON: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e("VolleyError", "Error in Volley Request: " + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                // Include the clicked category ID in the params
//                params.put("id", String.valueOf(id));

                // Retrieve and include the device ID in the params
                deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                params.put("id", deviceId);

                Log.d("cvbn", "" + deviceId);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }


    void addToFavorites(String productId) {
        String addFavoriteUrl = "https://mdsvisions.com/system/api/add_fvrt";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest postRequest = new StringRequest(Request.Method.POST, addFavoriteUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response, if needed
                        Log.e("AddFavoriteResponse", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        // Handle errors, if needed
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();


                params.put("userid", deviceId);
                params.put("prod_id", productId);

                Log.e("oooooo",""+deviceId+ "hjbjopkpo"+productId);

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    @Override
    public void onItemClick(String imageUrl) {
        // Handle item click, open DownloadActivity with the selected image URL
        Intent intent = new Intent(this, DownloadActivity.class);
        intent.putExtra("imageUrl", imageUrl);
        startActivity(intent);
    }



    @Override
    public void onFavoriteClick(String imageUrl, boolean isFavorite, String itemId) {
        // Handle favorite click here

        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        // Handle the click on heartIcon1 and heartIcon2
        if (isFavorite) {
            // Item is marked as favorite
            Log.e("Favorite", "Item with ID " + itemId + " marked as favorite from device ID " + deviceId);
            // You can now use the itemId and deviceId as needed, for example, to add to favorites
            addToFavorites(itemId);
        } else {
            // Item is not marked as favorite
            Log.e("Not Favorite", "Item with ID " + itemId + " not marked as favorite from device ID " + deviceId);
            addToFavorites(itemId);
        }
    }

    @Override
    public void onFavoriteClick(String imageUrl, boolean isFavorite) {

        if (isFavorite) {
            // Add imageUrl to the list of favorites or perform any other action
            showToast("Added to Favorites");

            // You can update your UI or perform other actions based on the favorite status
        } else {
            // Remove imageUrl from the list of favorites or perform any other action
            showToast("Removed from Favorites");
            // You can update your UI or perform other actions based on the favorite status
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
