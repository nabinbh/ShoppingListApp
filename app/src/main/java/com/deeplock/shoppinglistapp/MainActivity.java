package com.deeplock.shoppinglistapp;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {

    //Creating instance of recycler view
    private RecyclerView shoppingItems;

    //Creating instance of realm data base
    private Realm realm;

    //Creating list object to hold the data
    private List<ShoppingItem> dataSet;

    //Adapter for the recycler view
    private RecyclerView.Adapter shoppingItemAdapter= new RecyclerView.Adapter() {

        //Setting id for respective views
        private final int ACTIVE_VIEW = 1;
        private final int INACTIVE_VIEW = 2;
        private final int SUBHEADER_VIEW=3;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType==ACTIVE_VIEW)
            {
                //If the view is active item content then this view will be showed
                View v = getLayoutInflater().inflate(R.layout.active_item,parent,false);

                //view will be returned along with its content
                return new ActiveItemViewHolder(v,
                        (CheckBox)v.findViewById(R.id.item_status),(TextView)v.findViewById(R.id.item_name),
                        (TextView)v.findViewById(R.id.item_quantity), (ImageView)v.findViewById(R.id.item_action));
            }
            else if (viewType==INACTIVE_VIEW)
            {
                View v = getLayoutInflater().inflate(R.layout.inactive_item,parent,false);
                return new InactiveItemViewHolder(v,
                        (CheckBox)v.findViewById(R.id.item_status),(TextView)v.findViewById(R.id.item_name),
                        (ImageView)v.findViewById(R.id.item_action));
            }
            else
            {
                View v = getLayoutInflater().inflate(R.layout.subheader,parent,false);
                return new SubheaderViewHolder(v);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ShoppingItem currentItem = dataSet.get(position);
            if (currentItem.getTimeStamp()==-1) return;
            if (currentItem.isCompleted()) {
                InactiveItemViewHolder h = (InactiveItemViewHolder) holder;
                h.itemName.setText(currentItem.getName());
                h.itemName.setPaintFlags(h.itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                h.itemAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        realm.beginTransaction();
                        currentItem.setCompleted(false);
                        currentItem.setTimeStamp(System.currentTimeMillis());
                        realm.commitTransaction();
                        InitializeDataSet();
                        shoppingItemAdapter.notifyDataSetChanged();

                    }
                });
            }
            else
            {
                ActiveItemViewHolder h = (ActiveItemViewHolder) holder;
                h.itemName.setText(currentItem.getName());
                h.quantity.setText(currentItem.getQuantity());
                h.itemStatus.setChecked(false);

                h.itemStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                        if (checked)
                        {
                            realm.beginTransaction();
                            currentItem.setCompleted(true);
                            currentItem.setTimeStamp(System.currentTimeMillis());
                            realm.commitTransaction();
                            InitializeDataSet();
                            shoppingItemAdapter.notifyDataSetChanged();
                        }
                    }
                });
                h.itemAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(MainActivity.this,Item.class);
                        i.putExtra("TITLE","Edit Item");
                        i.putExtra("ITEM_NAME",currentItem.getName());
                        i.putExtra("ITEM_QUANTITY",currentItem.getQuantity());
                        i.putExtra("ITEM_ID",currentItem.getId());
                        startActivityForResult(i,1);
                    }
                });
            }


        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        @Override
        public int getItemViewType(int position) {
           ShoppingItem currentItem = dataSet.get(position);
            if (currentItem.getTimeStamp()==-1)
                return SUBHEADER_VIEW;
            if (currentItem.isCompleted())
                return INACTIVE_VIEW;
            return ACTIVE_VIEW;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RealmConfiguration configuration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(configuration);
        realm = Realm.getDefaultInstance();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,Item.class);
                i.putExtra("TITLE","Add Item");
                startActivityForResult(i, 1);

            }
        });
        shoppingItems=(RecyclerView) findViewById(R.id.shopping_items);
        shoppingItems.setLayoutManager(new LinearLayoutManager(this));

        InitializeDataSet();
        shoppingItems.setAdapter(shoppingItemAdapter);
    }

    private void InitializeDataSet()
    {
        dataSet = new ArrayList<>();
        RealmResults<ShoppingItem> activeItemResults = realm.where(ShoppingItem.class).equalTo("completed",false)
                .findAllSorted("timeStamp", Sort.DESCENDING);

        RealmResults<ShoppingItem> inActiveItemResults = realm.where(ShoppingItem.class).equalTo("completed",true)
                .findAllSorted("timeStamp", Sort.DESCENDING);

        ShoppingItem subHeader = new ShoppingItem();
        subHeader.setTimeStamp(-1);

        for (ShoppingItem item:activeItemResults ) dataSet.add(item);
        dataSet.add(subHeader);

        for (ShoppingItem item:inActiveItemResults) dataSet.add(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==RESULT_OK)
        {
            InitializeDataSet();
            shoppingItemAdapter.notifyDataSetChanged();
        }

    }


}
