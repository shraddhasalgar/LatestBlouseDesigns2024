package axolotls.latest.collectiondesign;

import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<HomeModel> dataList;
    private int selectedItemPosition = RecyclerView.NO_POSITION;

    public void setFilteredList(List<HomeModel>filteredList){
        this.dataList = filteredList;
        notifyDataSetChanged();
    }

    public CategoryAdapter(List<HomeModel> dataList) {
        this.dataList = dataList;

    }
    public void setData(List<HomeModel> newData) {
        dataList.clear();
        dataList.addAll(newData);
        notifyDataSetChanged();
    }

    public List<HomeModel> getFilteredList() {
        return dataList;
    }

    // ViewHolder class should extend RecyclerView.ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Update the selected item position
                        selectedItemPosition = position;

                        // Notify the adapter that data set changed to trigger onBindViewHolder
                        notifyDataSetChanged();

                        // Handle item click, open BlouseDesignActivity with the selected item id and name
                        HomeModel currentItem = dataList.get(position);
                        // Check the type of the context before casting
                        if (v.getContext() instanceof MainActivity) {
                            ((MainActivity) v.getContext()).openCategoryActivity(currentItem.getId(), currentItem.getName());
                        } else {
                            // Handle the case where the context is not MainActivity
                            ((CategoryActivity) v.getContext()).openBlouseDesignActivity(currentItem.getId(), currentItem.getName());

                            Log.e("categoryadapter", "Context is not an instance of MainActivity");
                        }
                    }
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HomeModel currentItem = dataList.get(position);

        // Log the image URL
        Log.d("Image URL", currentItem.getImg());

        // Set the name
        holder.textView.setText(currentItem.getName());

        String url = "https://mdsvisions.com/system/axxests/sub_categories_img/";

        // Use Picasso to load the image
        String imageUrl = url + currentItem.getImg();

        Log.e("jhgu","jgu"+imageUrl);

        // Load the image using Picasso
        Picasso.get().load(imageUrl).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle item click, open BlouseDesignActivity with the selected item id and name
                HomeModel currentItem = dataList.get(position);

                // Check the type of the context before casting
                if (v.getContext() instanceof MainActivity) {
                    ((MainActivity) v.getContext()).openCategoryActivity(currentItem.getId(), currentItem.getName());
                } else {
                    // Handle the case where the context is not MainActivity
                    ((CategoryActivity) v.getContext()).openBlouseDesignActivity(currentItem.getId(), currentItem.getName());

                    Log.e("CategoryAdapter", "Context is not an instance of CategoruActivity");
                }
            }
        });

        holder.textView.setTypeface(null, position == selectedItemPosition ? Typeface.BOLD : Typeface.NORMAL);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}


