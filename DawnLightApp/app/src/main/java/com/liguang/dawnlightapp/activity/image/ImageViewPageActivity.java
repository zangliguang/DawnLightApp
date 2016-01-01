package com.liguang.dawnlightapp.activity.image;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.liguang.dawnlightapp.R;
import com.squareup.picasso.Picasso;

public class ImageViewPageActivity extends AppCompatActivity {

    public static String URL_ARGUMENTS_EXTRA = "URL_EXTRAL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view_page);
        String urls_str = getIntent().getStringExtra(URL_ARGUMENTS_EXTRA);
        String[] urls_array = urls_str.split(",");
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
//        pager.setOffscreenPageLimit();
        pager.setAdapter(new ImageAdapter(this, urls_array));
    }

    private class ImageAdapter extends PagerAdapter {
        private String[] IMAGE_URLS;
        private Context context;

        private LayoutInflater inflater;

        public ImageAdapter(Context context, String[] urls_array) {
            inflater = LayoutInflater.from(context);
            IMAGE_URLS = urls_array;
            this.context = context;
        }

        @Override
        public int getCount() {
            return IMAGE_URLS.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View imageLayout = inflater.inflate(R.layout.item_pager_image, container, false);
            assert imageLayout != null;
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
            Picasso.with(context).load(IMAGE_URLS[position]).into(imageView);
            container.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
        }
    }
}
