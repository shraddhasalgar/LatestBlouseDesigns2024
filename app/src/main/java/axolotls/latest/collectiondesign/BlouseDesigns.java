package axolotls.latest.collectiondesign;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
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

public class BlouseDesigns extends AppCompatActivity implements BlouseDesignAdapter.OnItemClickListener {
    private String receivedId,catid; // Declare receivedId here
    RecyclerView recyclerview1;
    ImageView likes;
    ImageView playstore;
    private Toolbar toolbar;
    private TextView titleTextView;
    String deviceId;

    ImageView imgback;

    private BlouseDesignAdapter blouseDesignAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blouse_design_activity);
        initViews();


        Intent intent = getIntent();
        if (intent != null) {
            // Retrieve data using intent.getStringExtra(), intent.getIntExtra(), etc.
            // For example:
            receivedId = intent.getStringExtra("id");
            catid = intent.getStringExtra("cat_id");

            // Use the receivedId as needed
             Example: Log.d("BlouseDesigns", "Received ID: " + receivedId);


            String itemName = intent.getStringExtra("itemName");
            // Set the title dynamically
            titleTextView.setText(itemName);
        }

        apicall1(Integer.parseInt(receivedId));

        blouseDesignAdapter = new BlouseDesignAdapter(new ArrayList<>(), this);
        recyclerview1.setAdapter(blouseDesignAdapter);
        recyclerview1.setLayoutManager(new GridLayoutManager(this, 2));

        LinearLayout namesContainer = findViewById(R.id.namesContainer);
        apicall(namesContainer, receivedId);
//        apicall1();
    }

    private void initViews() {
        imgback = findViewById(R.id.imgback);
        likes = findViewById(R.id.likes);
        playstore = findViewById(R.id.playstore);
        recyclerview1 = findViewById(R.id.recyclerview1);
        toolbar = findViewById(R.id.toolbar);
        titleTextView = toolbar.findViewById(R.id.name);



        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BlouseDesigns.this, FavouriteActivity.class);
                startActivity(intent);
            }
        });


        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recyclerview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the RecyclerView click event here
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

    private void apicall1(int id) {
        // Your existing code for making API request
        // Modify the params or URL as needed based on the clicked category ID
        String withdraw_request = "https://mdsvisions.com/system/api/get_prod";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new StringRequest(Request.Method.POST, withdraw_request,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("edsa", "efsdc" + response);

                        List<HomeModel> dataList = new ArrayList<>();

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String id = jsonObject.getString("id");
                                String name = jsonObject.getString("name");
                                String img = jsonObject.getString("img");
                                String favuorite = jsonObject.getString("is_favorite");


                                Log.d("Imsadad", img);
                                HomeModel homeModel = new HomeModel(id, name, img, favuorite);
                                dataList.add(homeModel);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        blouseDesignAdapter.setData(dataList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                // Include the clicked category ID in the params
                params.put("id", String.valueOf(id));
                Log.d("dddd",""+id);

                // Retrieve and include the device ID in the params
                 deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                params.put("user", deviceId);

                Log.d("cvbn",""+deviceId);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void apicall(LinearLayout namesContainer, String dynamicId) {
        String url = "https://mdsvisions.com/system/api/get_sub_cat";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create a Map with the necessary parameters
        Map<String, String> params = new HashMap<>();
        params.put("cat_id", catid);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    List<String> namesList = new ArrayList<>();

                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.getString("name");
                            String id = jsonObject.getString("id");
                            namesList.add(name);

                            Log.d("qwesa", id);
                        }

                        // Add TextView for each name dynamically
                        addTextViews(namesContainer, namesList);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle error
                    Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Error fetching data", error);
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void addTextViews(LinearLayout container, List<String> namesList) {
        for (String name : namesList) {
            BoldUnderlineTextView textView = new BoldUnderlineTextView(this);
            textView.setText(name);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(18);
            textView.setPadding(16, 8, 16, 10);
            container.addView(textView);

            // Set a unique ID for each TextView
            int categoryId = ViewCompat.generateViewId();
            textView.setId(categoryId);

            // Set click listener
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle click on the TextView
                    String categoryName = textView.getText().toString();
                    int id = textView.getId();

                    // Clear bold formatting and hide underline from all TextViews in the container
                    for (int i = 0; i < container.getChildCount(); i++) {
                        View child = container.getChildAt(i);
                        if (child instanceof BoldUnderlineTextView) {
                            ((BoldUnderlineTextView) child).setBold(false);
                            ((BoldUnderlineTextView) child).setUnderlineVisible(false);
                            ((BoldUnderlineTextView) child).setTextColor(Color.WHITE);
                            // Set the background color back to the original color for all TextViews
                            ((BoldUnderlineTextView) child).setBackgroundColor(Color.TRANSPARENT);

                        }
                    }

                    // Make the clicked TextView bold and show underline
                    textView.setBold(true);
                    textView.setUnderlineVisible(true);
                    textView.setTextColor(Color.BLACK); // Change the text color to your desired color
                    textView.setBackgroundColor(Color.WHITE);

                    // Use categoryId as needed
                    // You can use clickedCategoryId in your logic or pass it to another function
                    handleTextViewClick(categoryName, id);
                }
            });
        }
    }


    private void handleTextViewClick(String categoryName, int id) {

        Log.d("hjkloiuytfvb", "Category: " + categoryName + ", ID: " + id);
        // You can perform actions based on the clicked category

        // Call apicall1 with the clicked category ID
        apicall1(id);
    }

    public BlouseDesignAdapter getBlouseDesignAdapter() {
        return blouseDesignAdapter;
    }

    @Override
    public void onItemClick(ArrayList<String> imageUrls, String productId) {
        // Handle item click, open DownloadActivity with the selected image URLs and product ID
        if (imageUrls != null && !imageUrls.isEmpty()) {
            Intent intent = new Intent(this, DownloadActivity.class);
            intent.putStringArrayListExtra("imageUrls", imageUrls);
            intent.putExtra("prod_id", productId);


            Log.d("aaaaaaaaaa", "product id reci" + imageUrls.toString());

            Log.d("hiulnjij", "product id reci" + productId);

            startActivity(intent);
        }
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

}

