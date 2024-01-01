package axolotls.latest.collectiondesign;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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

public class CategoryActivity extends AppCompatActivity{
    private List<HomeModel> dataList;
    private SearchView searchView;
    private Toolbar toolbar;
    private TextView titleTextView;
    String dynamicId;
    private String receivedId; // Declare receivedId here
    private CategoryAdapter categoryAdapter;
    RecyclerView recyclerview4;
    ImageView likes;
    ImageView playstore;
    String deviceId;
    ImageView imgback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        initViews();

        // Initialize receivedId before using it
        receivedId = getIntent().getStringExtra("id");
        if (receivedId != null) {
            subcategory(Integer.parseInt(receivedId));
        }

        // Retrieve data from the intent
        Intent intent = getIntent();
        if (intent != null) {
            String itemName = intent.getStringExtra("itemName");
            // Set the title dynamically
            titleTextView.setText(itemName);
        }

        categoryAdapter = new CategoryAdapter(new ArrayList<>());
        recyclerview4.setAdapter(categoryAdapter);
        recyclerview4.setLayoutManager(new GridLayoutManager(this, 2));


    }

    private void initViews() {
        playstore = findViewById(R.id.playstore);
        dataList = new ArrayList<>();
        searchView = findViewById(R.id.searchView);
        imgback = findViewById(R.id.imgback);
        likes = findViewById(R.id.likes);
        recyclerview4 = findViewById(R.id.recyclerview4);
        toolbar = findViewById(R.id.toolbar);
        titleTextView = toolbar.findViewById(R.id.title);
        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_button);

// Set the tint color
        searchIcon.setColorFilter(ContextCompat.getColor(this, android.R.color.black), PorterDuff.Mode.SRC_IN);

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filterList(newText);
                return true;
            }
        });



        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, FavouriteActivity.class);
                startActivity(intent);
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

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        recyclerview4.addOnItemTouchListener(new RecyclerItemClickListener(this,
                recyclerview4, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Get the selected item's data from the filtered list
                HomeModel selectedHomeModel = categoryAdapter.getFilteredList().get(position);

                Log.d("RecyclerViewItemClick", "Clicked on item at position: " + position);
                Log.d("RecyclerViewItemClick", "Selected item ID: " + selectedHomeModel.getId());
                Log.d("RecyclerViewItemClick", "Selected item Name: " + selectedHomeModel.getName());

                // Start the next activity and pass the selected data
                openBlouseDesignActivity(selectedHomeModel.getId(), selectedHomeModel.getName());
            }

            @Override
            public void onLongItemClick(View view, int position) {
                // Handle long item click if needed
                Log.d("RecyclerViewLongItemClick", "Long clicked on item at position: " + position);

            }
        }));


    }

    private void filterList(String text) {
        List<HomeModel> filteredList = new ArrayList<>();
        for (HomeModel homeModel : dataList){
            if (homeModel.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(homeModel);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(this,"No data found", Toast.LENGTH_SHORT).show();
        }
        else {
            categoryAdapter.setFilteredList(filteredList);
        }

    }



    private void subcategory(int id) {
        // Your existing code for making API request
        // Modify the params or URL as needed based on the clicked category ID
        String withdraw_request = "https://mdsvisions.com/system/api/get_sub_cat";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new StringRequest(Request.Method.POST, withdraw_request,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("uuttttt", "efsdc" + response);

//                        List<HomeModel> dataList = new ArrayList<>();

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String id = jsonObject.getString("id");
                                String name = jsonObject.getString("name");
                                String img = jsonObject.getString("img");
                                String favuorite = jsonObject.getString("img");


                                Log.d("Imsadad", img);
                                HomeModel homeModel = new HomeModel(id, name, img, favuorite);
                                dataList.add(homeModel);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        categoryAdapter.setData(dataList);


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
                params.put("cat_id", String.valueOf(id));
                Log.d("dddd",""+id);

                // Retrieve and include the device ID in the params
//                deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//                params.put("user", deviceId);
//
//                Log.d("cvbn",""+deviceId);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    public void openBlouseDesignActivity(String itemId, String itemName) {
        Intent intent = new Intent(this, BlouseDesigns.class);
        intent.putExtra("id", itemId);
        intent.putExtra("cat_id", receivedId);
        intent.putExtra("itemName", itemName);
        startActivity(intent);

    }
}