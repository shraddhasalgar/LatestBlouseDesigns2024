package axolotls.latest.collectiondesign;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private SearchView searchView;

    private String receivedId; // Declare receivedId here
    ImageView likes;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    Button exitButton, stayButton;
    private TextView name;
    private HomeAdapter homeAdapter;
    Fragment fragment;
    private LinearLayout rateus, sharethis, privacy, disclaimer, contact, exit;
    SwipeRefreshLayout swiperefresh;
    private NavigationView navView;
    ImageView shareNow;
    RecyclerView recyclerview;

    private ImageView back;

    private List<HomeModel> dataList;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initViews();
        Intent intent = getIntent();
        if (intent != null) {
            // Retrieve data using intent.getStringExtra(), intent.getIntExtra(), etc.
            // For example:
            receivedId = intent.getStringExtra("id");

            // Use the receivedId as needed
            Example: Log.d("BlouseDesigns", "Received ID: " + receivedId);
        }
        apicall(receivedId);
        homeAdapter = new HomeAdapter(new ArrayList<>());
        recyclerview.setAdapter(homeAdapter);
        Log.d("MainActivity", "onCreate: NavigationView ID - " + navigationView.getId());

        // Set the layout manager after setting the adapter
        recyclerview.setLayoutManager(new GridLayoutManager(this, 2));

    }

    private void initViews() {

        Log.d("wwwwwww", "Disclaimer clicked");
        likes = findViewById(R.id.likes);
        stayButton = findViewById(R.id.stayButton);
        exitButton = findViewById(R.id.exitButton);
        recyclerview = findViewById(R.id.recyclerview);
        shareNow = findViewById(R.id.share);
        back = findViewById(R.id.back);
//        swiperefresh = findViewById(R.id.swiperefresh);
        name =  findViewById(R.id.name);
         searchView = findViewById(R.id.searchView);
         dataList = new ArrayList<>();

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


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();


        shareNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create a share intent
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");

                // Set the text to be shared
                String appLink = "https://play.google.com/store/apps/details?id=axolotls.latest.collectiondesign";
                String shareText = "\uD83D\uDE0D Download the Blouse Design app now to discover thousands of mesmerizing designs and unleash your creativity. \uD83D\uDE0D\n\nGet the app:" + appLink;
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

                // Start the chooser for sharing
                startActivity(Intent.createChooser(shareIntent, "Share App via"));
            }

        });

        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FavouriteActivity.class);
                startActivity(intent);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId(); // Get the ID dynamically

                Log.d("NavigationClick", "Item clicked with ID: " + itemId);

                if (itemId == R.id.rate_us) {
                    Log.d("NavigationClick", "Home clicked");

                    String url = "https://play.google.com/store/apps/details?id=axolotls.latest.collectiondesign";

                    // Create an intent with the ACTION_VIEW action and the URL
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                    // Verify that there is an app available to handle the intent
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        // Start the activity if there is an app available
                        startActivity(intent);
                    }
                } else if (itemId == R.id.sharethis) {
                    Log.d("NavigationClick", "Contact clicked");
                    Log.d("eeeeee", "Disclaimer clicked");

                    // Create a share intent
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");

                    // Set the text to be shared
                    String appLink = "https://play.google.com/store/apps/details?id=axolotls.latest.collectiondesign";
                    String shareText = "\uD83D\uDE0D 😍 Download the Latest Designs 2024 app now to discover thousands of mesmerizing designs and unleash your creativity. 😍 \uD83D\uDE0D\n\nGet the app:" + appLink;
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

                    // Start the chooser for sharing
                    startActivity(Intent.createChooser(shareIntent, "Share App via"));

                } else if (itemId == R.id.prvacy) {
                    Log.d("NavigationClick", "Gallery clicked");
                    Intent intent = new Intent(MainActivity.this, PrivacyPolicy.class);
                    startActivity(intent);
                    finish();
                } else if (itemId == R.id.disclaimer) {
                    Log.d("NavigationClick", "About clicked");
                    showDisclaimerDialog();
                } else if (itemId == R.id.contact) {
                    Log.d("NavigationClick", "Login clicked");
                    Intent intent = new Intent(MainActivity.this, Contactus.class);
                    startActivity(intent);
                    finish();                } else if (itemId == R.id.exit) {
                    Log.d("NavigationClick", "Share clicked");
                    showExitDialog();
                }

                drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer after handling click

                return true; // Change this line to true
            }
        });


        // Set click listener for RecyclerView items
        recyclerview.addOnItemTouchListener(new RecyclerItemClickListener(this,
                recyclerview, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Get the selected item's data from the filtered list
                HomeModel selectedHomeModel = homeAdapter.getFilteredList().get(position);

                Log.d("RecyclerViewItemClick", "Clicked on item at position: " + position);
                Log.d("RecyclerViewItemClick", "Selected item ID: " + selectedHomeModel.getId());
                Log.d("RecyclerViewItemClick", "Selected item Name: " + selectedHomeModel.getName());

                // Start the next activity and pass the selected data
                openCategoryActivity(selectedHomeModel.getId(), selectedHomeModel.getName());
            }

            @Override
            public void onLongItemClick(View view, int position) {
                // Handle long item click if needed
                Log.d("RecyclerViewLongItemClick", "Long clicked on item at position: " + position);

            }
        }));

   back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the navigation drawer when back is clicked
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

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
              homeAdapter.setFilteredList(filteredList);
        }

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    public void openCategoryActivity(String itemId, String itemName) {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("id", itemId);
        intent.putExtra("itemName", itemName);
        startActivity(intent);

    }

    private void showExitDialog() {
        // Create a custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // Optional: Remove dialog title
        dialog.setContentView(R.layout.exit_dialog);

        // Find views in the custom dialog layout
        TextView messageTextView = dialog.findViewById(R.id.messageTextView);
        Button exitButton = dialog.findViewById(R.id.exitButton);
        Button stayButton = dialog.findViewById(R.id.stayButton);

        // Set the message in the TextView
        messageTextView.setText("Are you sure you want to exit?");

        // Set click listeners for the exit and stay buttons
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform the exit action
                finish();
            }
        });

        stayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        // Show the dialog
        dialog.show();
    }

    private void showDisclaimerDialog() {
        // Create a custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // Optional: Remove dialog title
        dialog.setContentView(R.layout.custome_dialog);

        // Customize the dialog appearance and behavior as needed

        TextView ok = dialog.findViewById(R.id.ok);

        // Set an OnClickListener on the Ok TextView
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make the Ok TextView invisible when it is clicked
                dialog.dismiss();
            }
        });
        // Show the dialog
        dialog.show();
    }

    private void apicall(String receivedId) {
        String url = "https://mdsvisions.com/system/api/get_cat";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
//                    List<HomeModel> dataList = new ArrayList<>();

                    Log.d("ftuyiu","gjhfjh"+response);

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String name = jsonObject.getString("name");  // Fetch the name field
                            String img = jsonObject.getString("img");
                            String favuorite = jsonObject.getString("img");

                            Log.d("Imsadad", img);
                            Log.d("dfghjk", id);

                            HomeModel homeModel = new HomeModel(id, name, img, favuorite);
                            dataList.add(homeModel);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // Add the data to the adapter
                    homeAdapter.setData(dataList);


                }, error -> {
            // Handle error
            Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show();
            Log.e("API Error", "Error fetching data", error);

        });

        requestQueue.add(jsonArrayRequest);
    }

    public HomeAdapter getHomeAdapter() {

        return homeAdapter;
    }

}