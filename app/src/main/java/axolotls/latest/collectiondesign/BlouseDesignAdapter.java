package axolotls.latest.collectiondesign;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BlouseDesignAdapter extends RecyclerView.Adapter<BlouseDesignAdapter.ViewHolder> {
    private List<HomeModel> dataList;
    private OnItemClickListener listener;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(ArrayList<String> imageUrls, String productId);
        void onFavoriteClick(String imageUrl, boolean isFavorite, String itemId);
        void onFavoriteClick(String imageUrl, boolean isFavorite);

    }

    public BlouseDesignAdapter(List<HomeModel> dataList, OnItemClickListener listener) {
        this.dataList = dataList;
        this.listener = listener;
        this.context = context;
    }

    public void setData(List<HomeModel> newData) {
        dataList.clear();
        dataList.addAll(newData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView1;
        RelativeLayout heartIcon;
        ImageView heartIcon1;
        ImageView heartIcon2;
        TextView itemCount;
        private String itemId; // Change the type to String

        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            imageView1 = itemView.findViewById(R.id.imageView1);
            heartIcon = itemView.findViewById(R.id.heartIcon);
            heartIcon1 = itemView.findViewById(R.id.heartIcon1);
            heartIcon2 = itemView.findViewById(R.id.heartIcon2);
            itemCount = itemView.findViewById(R.id.itemCount);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Create an ArrayList with a single image URL
                        ArrayList<String> imageUrls = new ArrayList<>();
                        imageUrls.add(dataList.get(position).getImg());

                        // Pass the ArrayList and product ID to the listener
                        listener.onItemClick(imageUrls, dataList.get(position).getId());
                    }
                }
            });


            heartIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        itemId = dataList.get(position).getId(); // Store the ID
                        toggleHeartIconsVisibility(ViewHolder.this);
                        if (heartIcon2.getVisibility() == View.VISIBLE) {
                            listener.onFavoriteClick(dataList.get(position).getImg(), true, itemId);
                        } else {
                            listener.onFavoriteClick(dataList.get(position).getImg(), false, itemId);
                        }
                    }
                }
            });


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_recycler, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HomeModel currentItem = dataList.get(position);

        // Check if the id is not null before using it
        String itemId = currentItem.getId();
        if (itemId != null) {
            currentItem.setId(itemId);
            // Rest of your code...

            String url = "https://mdsvisions.com/system/axxests/products_img/";
            String imageUrl = url + currentItem.getImg();

            Log.e("gfu","jgu"+imageUrl);
            String favuoriteId = currentItem.getFavuoriteId();

            if (favuoriteId.equalsIgnoreCase("1")){

                holder.heartIcon1.setVisibility(View.GONE);
                holder.heartIcon2.setVisibility(View.VISIBLE);

            }

            Picasso.get().load(imageUrl).into(holder.imageView1);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String imageUrl = dataList.get(position).getImg();
                    String productId = dataList.get(position).getId();
                    Intent intent = new Intent(v.getContext(), DownloadActivity.class);
                    intent.putExtra("imageUrl", imageUrl);
                    intent.putExtra("prod_id", productId);
                    intent.putExtra("position", position);

                    intent.putExtra("dataList", (Serializable) dataList);

                    Log.d("oiyiuy","jhuygu"+dataList);


                    v.getContext().startActivity(intent);
                }
            });


            holder.heartIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleHeartIconsVisibility(holder);
                    if (holder.heartIcon2.getVisibility() == View.VISIBLE) {
                        listener.onFavoriteClick(dataList.get(position).getImg(), true, itemId);
                    } else {
                        listener.onFavoriteClick(dataList.get(position).getImg(), false, itemId);
                    }
                }
            });
            holder.itemCount.setText(String.valueOf(position + 1));
        }
    }

    private void toggleHeartIconsVisibility(ViewHolder holder) {
        ImageView heartIcon1 = holder.heartIcon1;
        ImageView heartIcon2 = holder.heartIcon2;

        if (heartIcon1.getVisibility() == View.VISIBLE) {
            heartIcon1.setVisibility(View.GONE);
            heartIcon2.setVisibility(View.VISIBLE);

            Log.e("og","");
        } else {
            heartIcon1.setVisibility(View.VISIBLE);
            heartIcon2.setVisibility(View.GONE);

            Log.e("hjhh","");
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
