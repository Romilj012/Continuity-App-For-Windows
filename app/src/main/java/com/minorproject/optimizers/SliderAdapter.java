package com.minorproject.optimizers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class SliderAdapter extends PagerAdapter {
Context context;
LayoutInflater layoutInflater;
public SliderAdapter(Context context){

    this.context=context;
}

public int[] slide_images ={
                R.drawable.connectivity,

                R.drawable.sharing,
        R.drawable.screen

};

public String[] slide_headings={
    "Hands Off",
    "Picture Sharing",
    "Screen Mirroring"
    };
public String[] slide_descs={
    "Work on a document and continue it on your computer",
        " Click Images on your phone and get them instantly on your PC",
        "Share your mobile screen on your PC"
    };
    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
    layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    View view = layoutInflater.inflate(R.layout.slide_layout,container,false);
        CircleImageView slideImageView=(CircleImageView) view.findViewById(R.id.imageView);
        TextView slideHeading=(TextView)view.findViewById(R.id.textView);
        TextView slideDescription=(TextView)view.findViewById(R.id.textView2);


        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
