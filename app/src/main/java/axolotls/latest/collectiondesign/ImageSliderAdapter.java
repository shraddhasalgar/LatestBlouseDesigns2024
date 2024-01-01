package axolotls.latest.collectiondesign;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageSliderAdapter extends PagerAdapter {

    private List<HomeModel> dataList;
    private ImageSliderAdapter.OnItemClickListener listener;
    private Context context;

    public interface OnItemClickListener {
        void onFavoriteCheck(String imageUrl, boolean isFavorite, String itemId);
    }

    public ImageSliderAdapter(Context context, List<HomeModel> dataList, OnItemClickListener listener) {
        this.dataList = dataList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.slide_item, container, false);

        HomeModel currentItem = dataList.get(position);

        PhotoView photoView = view.findViewById(R.id.imageView);

        String url = "https://mdsvisions.com/system/axxests/products_img/";
        String imageUrl = url + currentItem.getImg();

        String favuoriteId = currentItem.getFavuoriteId();
        String itemId = currentItem.getId();

        if (favuoriteId.equalsIgnoreCase("1")) {
            Log.d("sert123", "" + itemId);
            listener.onFavoriteCheck(dataList.get(position).getImg(), true, itemId);
        } else {
            Log.d("sert321", "" + itemId);
            listener.onFavoriteCheck(dataList.get(position).getImg(), false, itemId);
        }

        // Load image into PhotoView using Picasso
        Picasso.get().load(imageUrl).into(photoView);

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

