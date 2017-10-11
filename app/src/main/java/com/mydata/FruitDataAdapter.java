package com.mydata;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myjava.R;

import java.util.List;

/**
 * Created by ysj on 2017/10/10.
 */

public class FruitDataAdapter extends ArrayAdapter<Fruit> {

    private int resourceId;

    /**
     *
     * @param context 你好
     * @param resource 我好
     * @param objects 他好
     */
    public FruitDataAdapter(Context context, int resource, List<Fruit> objects) {
        super(context, resource, objects);

        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Fruit fruit = getItem(position);
        View view = null;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            convertView = view;

            ImageView fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
            TextView fruitName = (TextView) view.findViewById(R.id.fruit_name);

            viewHolder = new ViewHolder();
            viewHolder.fruitImage = fruitImage;
            viewHolder.fruitName = fruitName;

            view.setTag(viewHolder);

        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }


        viewHolder.fruitImage.setImageResource(fruit.getImageId());
        viewHolder.fruitName.setText(fruit.getName());
        return view;
    }

    class ViewHolder {
        ImageView fruitImage;
        TextView fruitName;
    }
}
