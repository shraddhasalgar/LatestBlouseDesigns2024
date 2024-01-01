package axolotls.latest.collectiondesign;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownloadActivity extends AppCompatActivity implements ImageSliderAdapter.OnItemClickListener {
    private String deviceId;
    private boolean isLiked = false;
    private ImageView shareIcon;
    private ImageView download;
    private ViewPager viewPager;
    private ImageView back;
    private String imageUrl, productId;
    ImageView likes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        download = findViewById(R.id.download);
        shareIcon = findViewById(R.id.share);
        viewPager = findViewById(R.id.viewPager);
        back = findViewById(R.id.back);
        likes = findViewById(R.id.likes);

        Intent intent = getIntent();
        if (intent != null) {
            imageUrl = intent.getStringExtra("imageUrl");
            productId = intent.getStringExtra("prod_id");

            int position = intent.getIntExtra("position", 0);

            List<HomeModel> dataList = (List<HomeModel>) intent.getSerializableExtra("dataList");

            if (dataList != null) {
                ImageSliderAdapter adapter = new ImageSliderAdapter(this, dataList, this);
                viewPager.setAdapter(adapter);
                viewPager.setCurrentItem(position);
            } else {
                // Handle the case where dataList is null (log, show a message, etc.)
                Log.e("DownloadActivity", "dataList is null");
                // You might want to handle this case or show an error message to the user.
            }

            Log.d("uyuu", "jhuygu" + dataList);

            String url = "https://mdsvisions.com/system/axxests/products_img/";
            String imageUrl = url + getIntent().getStringExtra("imageUrl");

            likes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleLike();
                }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });

            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call a method to initiate the download process
                    downloadImageToGallery(imageUrl);
                }
            });

            shareIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call a method to initiate the download process and share the image
//                    downloadAndShareImage(imageUrl);

                    shareImageDirectly(imageUrl);
                }
            });
        }
    }

    private void shareImageDirectly(String imageUrl) {
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .skipMemoryCache(true)  // Add this line to skip memory cache
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Check if the resource is valid
                        if (resource != null) {
                            // Share the image directly without saving to gallery
                            shareImage(resource);
                        } else {
                            // Handle the case where the resource is null or not found
                            Toast.makeText(DownloadActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void shareImage(Bitmap bitmap) {
        // Convert the Bitmap to a Uri
        Uri imageUri = getImageUri(bitmap);

        // Create an intent to share the image
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
//        shareIntent.putExtra(Intent.EXTRA_TEXT, "😍 Download the Latest Designs 2024 app now to discover thousands of mesmerizing designs and unleash your creativity. 😍");


        // Set the text to be shared
        String appLink = "https://play.google.com/store/apps/details?id=axolotls.latest.collectiondesign";
        String shareText = "\uD83D\uDE0D Download the Blouse Design app now to discover thousands of mesmerizing designs and unleash your creativity. \uD83D\uDE0D\n\nGet the app:" + appLink;
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(shareIntent, "Share Image"));
    }

    private Uri getImageUri(Bitmap bitmap) {
        // Save the Bitmap to a temporary file
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "ImageTitle", null);

        // Return the Uri of the saved image
        return Uri.parse(path);
    }
    private void downloadImageToGallery(String imageUrl) {
        Glide.with(this)
                .asDrawable()
                .load(imageUrl)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        // Check if the resource is valid
                        if (resource != null) {
                            // Save the drawable/resource to the gallery
                            saveImageToGallery(resource);
                        } else {
                            // Handle the case where the resource is null or not found
                            Toast.makeText(DownloadActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveImageToGallery(Drawable imageDrawable) {
        // Convert the Drawable to a Bitmap
        Bitmap bitmap = ((BitmapDrawable) imageDrawable).getBitmap();

        // Save the Bitmap to the gallery
        String displayName = "ImageTitle";
        String description = "ImageDescription";

        MediaStore.Images.Media.insertImage(
                getContentResolver(),
                bitmap,
                displayName,
                description
        );

        // Display a toast message indicating that the image has been saved
        Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show();
    }

    private void toggleLike() {
        isLiked = !isLiked;

        if (isLiked) {
            // Add the image to favorites
            // You can use a database, a list, or any storage mechanism
            // For simplicity, let's just show a toast message
            Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show();
            likes.setImageResource(R.drawable.like); // Change the icon to a filled heart

            // Call addToFavorites method with the product ID (you need to get the product ID from somewhere)
            addToFavorites();
        } else {
            // Remove the image from favorites
            // Again, for simplicity, we show a toast message
            Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_SHORT).show();
            likes.setImageResource(R.drawable.favourite); // Change the icon to an empty heart
            addToFavorites();
        }
    }

    @Override
    public void onFavoriteCheck(String imageUrl, boolean isFavorite, String itemId) {
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        // Handle the click on heartIcon1 and heartIcon2
        if (isFavorite) {
            // Item is marked as favorite

            likes.setImageResource(R.drawable.like); // Change the icon to a filled heart

        } else {
            // Item is not marked as favorite
            likes.setImageResource(R.drawable.favourite); // Change the icon to an empty heart

        }
    }

    void addToFavorites() {
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
                        Log.e("AddFavoriteError", "Error adding to favorites: " + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                // Check if deviceId is not null
                params.put("userid", deviceId);

                // Check for null before putting in the map
                if (productId != null) {
                    params.put("prod_id", productId);
                    Log.e("RequestParams", "userid: " + deviceId + ", prod_id: " + productId);
                } else {
                    // Handle the case when productId is null (log, show a message, etc.)
                    Log.e("AddFavoriteError", "ProductId is null");
                }

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

}
