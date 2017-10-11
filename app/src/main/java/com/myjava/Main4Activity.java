package com.myjava;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mydata.Fruit;
import com.mydata.FruitDataAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Main4Activity extends Activity {

    @Bind(R.id.listview)
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        ButterKnife.bind(this);


        float xdpi = getResources().getDisplayMetrics().xdpi;
        float ydpi = getResources().getDisplayMetrics().ydpi;

        Log.d("Main4Activity", "xdpi is " + xdpi);
        Log.d("Main4Activity", "ydpi is " + ydpi);


        this.test();
    }

    List<Fruit> fruitList = new ArrayList<Fruit>();

    private void test() {

//        String[] data = {
//                "1", "2", "3", "4", "5"
//        };
//        ArrayAdapter<String> list = new ArrayAdapter<String>(Main4Activity.this, android.R.layout.simple_list_item_1, data);
//
//        listview.setAdapter(list);

        initFruits();

        FruitDataAdapter adapter = new FruitDataAdapter(Main4Activity.this, R.layout.fruit_item, fruitList);
        listview.setAdapter(adapter);
    }

    private void initFruits() {
        Fruit apple = new Fruit("Apple", R.mipmap.ic_launcher);
        fruitList.add(apple);
        Fruit banana = new Fruit("Banana", R.mipmap.ic_launcher);
        fruitList.add(banana);
        Fruit orange = new Fruit("Orange", R.mipmap.ic_launcher);
        fruitList.add(orange);
        Fruit watermelon = new Fruit("Watermelon", R.mipmap.ic_launcher);
        fruitList.add(watermelon);
        Fruit pear = new Fruit("Pear", R.mipmap.ic_launcher);
        fruitList.add(pear);
        Fruit grape = new Fruit("Grape", R.mipmap.ic_launcher);
        fruitList.add(grape);
        Fruit pineapple = new Fruit("Pineapple", R.mipmap.ic_launcher);
        fruitList.add(pineapple);
        Fruit strawberry = new Fruit("Strawberry", R.mipmap.ic_launcher);
        fruitList.add(strawberry);
        Fruit cherry = new Fruit("Cherry", R.mipmap.ic_launcher);
        fruitList.add(cherry);
        Fruit mango = new Fruit("Mango", R.mipmap.ic_launcher);
        fruitList.add(mango);
    }
}
